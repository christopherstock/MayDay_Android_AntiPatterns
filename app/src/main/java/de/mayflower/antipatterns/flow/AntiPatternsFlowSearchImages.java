
    package de.mayflower.antipatterns.flow;

    import  org.json.*;
    import  android.app.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.ui.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.ui.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds the flow for the search-images system.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowSearchImages
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
        protected   static AntiPatternsImageContainerFlow flow                                        = null;

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
            AntiPatternsDebug.imageSearch.out( "search for images ... [" + lastSearchTerm + "][" + lastLatitude + "][" + lastLongitude + "]" );

            try
            {
                //output before ordering
                AntiPatternsDebug.limitOffset.out( "BEFORE ordering search-images\n" + flow.toString() );

                //show loading-circle if desired
                if ( overlayIcon != null ) AntiPatternsLoadingCircle.showAndStartLoadingCircleUIThreaded(activity, overlayIcon);

                //commit search
                JSONObject  response    = AntiPatternsJsonRPCSearch.searchImagesByTagsAndOrLocationCoordinates
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
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( activity );

                        //parse images from response and handle them
                        JSONObject                  images          = LibJSON.getJSONObjectSecure(  response, "images" );

                        flow.handleNewImages
                        (
                            AntiPatternsState.ESearchImagesResults,
                            images,
                            AntiPatternsActionUpdate.EUpdateSearchImagesResultsNext,
                            null
                        );

                        //remove loading circle
                        if ( overlayIcon != null ) AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(activity, overlayIcon);

                        //check if no results have been returned
                        if ( flow.getOffset() == 0 )
                        {
                            //show 'no results'
                            lastActionOnNoResults.run();
                        }
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //remove loading circle
                        if ( overlayIcon != null ) AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(activity, overlayIcon);

                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(AntiPatternsState.ESearchImagesResults);
                        break;
                    }

                    default:
                    {
                        //remove loading circle
                        if ( overlayIcon != null ) AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(activity, overlayIcon);

                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( activity );
                        lastActionOnTechnicalError.run();

                        //report this error
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //remove loading circle
                    if ( overlayIcon != null ) AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(activity, overlayIcon);

                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( activity );
                    lastActionOnNoNetwork.run();
                }
                else
                {
                    //remove loading circle
                    if ( overlayIcon != null ) AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(activity, overlayIcon);

                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( activity );
                    lastActionOnTechnicalError.run();

                    //report this exception
                    //AntiPatternsDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
            flow.changeNextLoadingItemToNoNetwork( AntiPatternsState.ESearchImagesResults.getActivity(), actionOnClick );
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the image-search-results is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            flow = new AntiPatternsImageContainerFlow
            (
                AntiPatternsState.ESearchImagesResults.getActivity(),
                AntiPatternsStateSearchImagesResults.singleton.iContainerSearchImagesResults,
                AntiPatternsStateSearchImagesResults.singleton.iScrollView,
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
            View        checkboxView            = LibUI.getInflatedLayoutById( AntiPatternsState.EPivotalMenu.getActivity(), R.layout.dialog_view_input_checkbox );
                        checkboxSearchImages    = (CheckBox)checkboxView.findViewById( R.id.dialog_checkbox );
            checkboxSearchImages.setText( LibResource.getResourceString( AntiPatternsState.EPivotalMenu.getActivity(), R.string.dialog_search_images_checkbox_use_gps ) );

            //create check change listener
            checkboxSearchImages.setOnCheckedChangeListener
            (
                new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged( CompoundButton aButtonView, boolean aIsChecked )
                    {
                        AntiPatternsFlowSearchImages.inputHandlerSearchImagesLocation.setVisibility( ( aIsChecked ? View.INVISIBLE : View.VISIBLE ) );
                    }
                }
            );

            return checkboxView;
        }
    }
