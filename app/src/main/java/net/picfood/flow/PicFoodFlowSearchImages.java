/*  $Id: PicFoodFlowSearchImages.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  org.json.*;
    import  android.app.*;
    import  android.view.*;
    import  android.widget.*;
    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.flow.ui.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.jsonrpc.*;
    import  net.picfood.state.*;
    import  net.picfood.ui.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.*;
    import  com.synapsy.android.lib.ui.dialog.*;
    import  com.synapsy.android.lib.util.*;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds the flow for the search-images system.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowSearchImages.java $"
    **********************************************************************************************/
    public class PicFoodFlowSearchImages
    {
        /** The last setting to determine, if the user wants to use GPS for searching images. */
        public      static          boolean                             useGPS                                      = false;
        /** The last activity that was active before entering the state 'image search results'. */
        public      static          LibState                            lastStateBeforeSearch                       = null;
        /** The last search term the user has submitted. */
        public      static          String                              lastSearchTerm                              = null;
        /** The last latitude that shall be used for image-search. */
        public      static          String                              lastLatitude                                = null;
        /** The last longitude that shall be used for image-search. */
        public      static          String                              lastLongitude                               = null;
        /** The last assigned action for the image-search-flow to perform on 'no results'. */
        public      static          Runnable                            lastActionOnNoResults                       = null;
        /** The last assigned action for the image-search-flow to perform on 'no network'. */
        public      static          Runnable                            lastActionOnNoNetwork                       = null;
        /** The last assigned action for the image-search-flow to perform on 'technical error'. */
        public      static          Runnable                            lastActionOnTechnicalError                  = null;

        /** The dialog input handler for the dialog input field 'search images: location'. */
        public      static          LibDialogInputHandler               inputHandlerSearchImagesLocation            = new LibDialogInputHandler();
        /** The dialog input handler for the dialog input field 'search images: term'. */
        public      static          LibDialogInputHandler               inputHandlerSearchImagesTerm                = new LibDialogInputHandler();
        /** The checkbox to display in the 'search images' dialog. */
        public      static          CheckBox                            checkboxSearchImages                        = null;

        /** Encapsulates the image-container flow according to the Strategy pattern. */
        protected   static          PicFoodImageContainerFlow           flow                                        = null;

        /**********************************************************************************************
        *   Performs a search for images with a location and a searchterm.
        *   Both values are optional.
        *
        *   @param  activity        The according activity context.
        *   @param  overlayIcon     The View that holds the state's overlay icon.
        *                           This ImageView can display a spinning loading circle,
        *                           a clickable 'no network' icon or nothing at all.
        **********************************************************************************************/
        public static final void orderNextSearchImages( Activity activity, ImageView overlayIcon )
        {
            PicFoodDebug.imageSearch.out( "search for images ... [" + lastSearchTerm + "][" + lastLatitude + "][" + lastLongitude + "]" );

            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering search-images\n" + flow.toString() );

                //show loading-circle if desired
                if ( overlayIcon != null ) PicFoodLoadingCircle.showAndStartLoadingCircleUIThreaded( activity, overlayIcon );

                //commit search
                JSONObject  response    = PicFoodJsonRPCSearch.searchImagesByTagsAndOrLocationCoordinates
                (
                    activity,
                    lastSearchTerm,
                    lastLatitude,
                    lastLongitude,
                    flow.getOffset()
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( activity );

                        //parse images from response and handle them
                        JSONObject                  images          = LibJSON.getJSONObjectSecure(  response, "images" );

                        flow.handleNewImages
                        (
                            PicFoodState.ESearchImagesResults,
                            images,
                            PicFoodActionUpdate.EUpdateSearchImagesResultsNext,
                            null
                        );

                        //remove loading circle
                        if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( activity, overlayIcon );

                        //check if no results have been returned
                        if ( flow.getOffset() == 0 )
                        {
                            //show 'no results'
                            lastActionOnNoResults.run();
                        }
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //remove loading circle
                        if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( activity, overlayIcon );

                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.ESearchImagesResults );
                        break;
                    }

                    default:
                    {
                        //remove loading circle
                        if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( activity, overlayIcon );

                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( activity );
                        lastActionOnTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //remove loading circle
                    if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( activity, overlayIcon );

                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( activity );
                    lastActionOnNoNetwork.run();
                }
                else
                {
                    //remove loading circle
                    if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( activity, overlayIcon );

                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( activity );
                    lastActionOnTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Changes the loading item of the flow on the bottom of the scrolling list
        *   to a 'no network' icon and assigns the specified OnClick-action.
        *
        *   @param  actionOnClick   The action to perform when the 'no network' icon is clicked.
        **********************************************************************************************/
        public static final void changeNextLoadingItemToNoNetwork( final Runnable actionOnClick )
        {
            flow.changeNextLoadingItemToNoNetwork( PicFoodState.ESearchImagesResults.getActivity(), actionOnClick );
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the image-search-results is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            flow = new PicFoodImageContainerFlow
            (
                PicFoodState.ESearchImagesResults.getActivity(),
                PicFoodStateSearchImagesResults.singleton.iContainerSearchImagesResults,
                PicFoodStateSearchImagesResults.singleton.iScrollView,
                0
            );
        }

        /**********************************************************************************************
        *   Creates the checkbox for the 'search images' dialog.
        *
        *   @return     The container View that contains the checkbox.
        **********************************************************************************************/
        public static final View createDialogCheckbox()
        {
            View        checkboxView            = LibUI.getInflatedLayoutById( PicFoodState.EPivotalMenu.getActivity(), R.layout.dialog_view_input_checkbox );
                        checkboxSearchImages    = (CheckBox)checkboxView.findViewById( R.id.dialog_checkbox );
            checkboxSearchImages.setText( LibResource.getResourceString( PicFoodState.EPivotalMenu.getActivity(), R.string.dialog_search_images_checkbox_use_gps ) );

            //create check change listener
            checkboxSearchImages.setOnCheckedChangeListener
            (
                new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged( CompoundButton aButtonView, boolean aIsChecked )
                    {
                        PicFoodFlowSearchImages.inputHandlerSearchImagesLocation.setVisibility( ( aIsChecked ? View.INVISIBLE : View.VISIBLE ) );
                    }
                }
            );

            return checkboxView;
        }
    }
