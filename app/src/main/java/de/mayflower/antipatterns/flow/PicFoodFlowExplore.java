
    package de.mayflower.antipatterns.flow;

    import  org.json.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodSettings.JsonRPC;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.data.PicFoodDataImage.ContentImageStyle;
    import  de.mayflower.antipatterns.flow.ui.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.GridViews;

    /**********************************************************************************************
    *   Holds all tasks related to the explore area.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodFlowExplore
    {
        /** Encapsulates the GridView flow according to the Strategy pattern. */
        public              static          PicFoodGridViewFlow             flow                        = null;

        /**********************************************************************************************
        *   Updates the explore area by fetching current data.
        *
        *   @param  state                   The according state.
        *   @param  overlayIcon             The View that holds the state's overlay icon.
        *                                   This ImageView can display a spinning loading circle,
        *                                   a clickable 'no network' icon or nothing at all.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        **********************************************************************************************/
        public static final void updateNextExploreImages
        (
            LibState        state,
            ImageView       overlayIcon,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError
        )
        {
            PicFoodDebug.explore.out( PicFoodFlowExplore.class + "::updateExplore()" );

            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering explore-images\n" + flow.toString() );

                //show loading-circle if desired
                if ( overlayIcon != null ) PicFoodLoadingCircle.showAndStartLoadingCircleUIThreaded( state.getActivity(), overlayIcon );

                //request and get response - parse data images
                JSONObject          response    = PicFoodJsonRPCImage.getExplore
                (
                    state.getActivity(),
                    flow.getOffset()
                );
                String              status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse new exploreImages from response
                        PicFoodDataImage[] newExploreImages = PicFoodDataImage.parse( response, ContentImageStyle.ETiled, false );
                        PicFoodDebug.explore.out( "Parsed [" + newExploreImages.length + "] new explore images" );

                        //let the flow handle the parsed data
                        flow.handleParsedData
                        (
                            state,
                            newExploreImages,
                            0,
                            PicFoodAction.ENone,
                            GridViews.EExplore,
                            PicFoodActionUpdate.EUpdateExploreAreaNextOffset,

                            //show next-loading-item if
                            ( newExploreImages.length >= JsonRPC.LIMIT_IMAGES_EXPLORE )
                        );

                        //remove loading-circle
                        if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( state.getActivity(), overlayIcon );
/*
                        //increase the offset and add exploreImages to stack
                        exploreOffset += newExploreImages.size();

                        //prune existent loading-items
                        PicFoodUI.pruneLoadingItems( exploreData );

                        //create new adapter data
                        for ( PicFoodDataImage exploreImage : newExploreImages )
                        {
                            //create new item
                            LibAdapterData newItem = new PicFoodGridViewContent
                            (
                                state,
                                exploreImage,
                                PicFoodAction.ENone,
                                GridViews.EExplore
                            );

                            //add new item to stack
                            exploreData.addElement( newItem );
                        }

                        //output after ordering
                        PicFoodDebug.limitOffset.out
                        (
                                "AFTER ordering explore-images"
                            +   " exploreImagesSize     [" + exploreData.size()    + "] "
                            +   " exploreOffset         [" + exploreOffset         + "] "
                        );

                        //check if more explore-images can be loaded ( this is, if the response holds the full limit! ) - The full explore size can't be determined.
                        Runnable actionOnBottomReach = null;
                        if ( newExploreImages.size() >= JsonRPC.LIMIT_IMAGES_EXPLORE )
                        {
                            PicFoodDebug.limitOffset.out( "Set loading item on last position" );

                            //add loading icon
                            lastBottomLoadingItem = new PicFoodGridViewContentLoading( state, false, false );
                            exploreData.addElement( lastBottomLoadingItem );

                            //set action to perform on scrolling to the bottom
                            actionOnBottomReach = PicFoodActionUpdate.EUpdateExploreAreaNextOffset;
                        }

                        //remove loading-circle
                        if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( state.getActivity(), overlayIcon );

                        //assign new adapter data to GridView 'explore'
                        PicFoodAdapterManager.getSingleton( state.getActivity(), GridViews.EExplore ).changeDataUIThreaded( state, exploreData, actionOnBottomReach );
*/
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //show 'no network' icon and offer reload - maybe this is a temporary technical error on the server
                        if ( overlayIcon != null ) PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( state.getActivity(), overlayIcon, PicFoodActionUpdate.EUpdateExploreAreaClean );

                        //perform 'technical error'-action
                        actionOnTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( state );
                        break;
                    }

                    default:
                    {
                        //remove loading circle
                        if ( overlayIcon != null ) PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( state.getActivity(), overlayIcon, PicFoodActionUpdate.EUpdateExploreAreaClean );

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //show 'no network' icon and offer reload
                    if ( overlayIcon != null ) PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( state.getActivity(), overlayIcon, PicFoodActionUpdate.EUpdateExploreAreaClean );

                    //perform action on no-network
                    actionOnNoNetwork.run();
                }
                else
                {
                    //show 'no network' icon and offer reload
                    if ( overlayIcon != null ) PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( state.getActivity(), overlayIcon, PicFoodActionUpdate.EUpdateExploreAreaClean );

                    //perform action on technical error
                    actionOnTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the explore state is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            flow = new PicFoodGridViewFlow( 0 );
        }
    }
