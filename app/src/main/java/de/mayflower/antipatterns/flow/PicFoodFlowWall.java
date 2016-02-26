
    package de.mayflower.antipatterns.flow;

    import  org.json.*;
    import  android.app.*;
    import  android.widget.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.*;
    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.ui.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import  de.mayflower.antipatterns.ui.*;

    /**********************************************************************************************
    *   Holds all tasks related to the user wall.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodFlowWall
    {
        /** Encapsulates the image-container flow according to the Strategy pattern. */
        protected   static          PicFoodImageContainerFlow               flow                            = null;

        /**********************************************************************************************
        *   Updates the user's wall by fetching current data.
        *
        *   @param  state                   The according state.
        *   @param  overlayIcon             The View that holds the state's overlay icon.
        *                                   This ImageView can display a spinning loading circle,
        *                                   a clickable 'no network' icon or nothing at all.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        **********************************************************************************************/
        public static final void orderNextUserWallImages
        (
            LibState        state,
            ImageView       overlayIcon,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError
        )
        {
            PicFoodDebug.wall.out( PicFoodFlowWall.class + "::updateUserWall()" );

            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering wall-images\n" + flow.toString() );

                //show loading-circle if desired
                if ( overlayIcon != null ) PicFoodLoadingCircle.showAndStartLoadingCircleUIThreaded( state.getActivity(), overlayIcon );

                //request and get response - parse data images
                JSONObject          response    = PicFoodJsonRPCImage.getUserWall
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
                        //parse new wallImages from response and handle them
                        JSONObject images = LibJSON.getJSONObjectSecure( response, "images" );
                        flow.handleNewImages
                        (
                            state,
                            images,
                            PicFoodActionUpdate.EUpdateUserWallNext,
                            null
                        );

                        //remove loading circle
                        if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( state.getActivity(), overlayIcon );

                        //output after ordering
                        PicFoodDebug.limitOffset.out( "AFTER ordering wall-images\n" + flow.toString() );

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //show 'no network' icon and offer reload - maybe this is a temporary technical error on the server
                        if ( overlayIcon != null ) PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( state.getActivity(), overlayIcon, PicFoodActionUpdate.EUpdateUserWallClean );

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
                        if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( state.getActivity(), overlayIcon );

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
                    if ( overlayIcon != null ) PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( state.getActivity(), overlayIcon, PicFoodActionUpdate.EUpdateUserWallClean );

                    //perform action on no-network
                    actionOnNoNetwork.run();
                }
                else
                {
                    //show 'no network' icon and offer reload
                    if ( overlayIcon != null ) PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( state.getActivity(), overlayIcon, PicFoodActionUpdate.EUpdateUserWallClean );

                    //perform action on technical error
                    actionOnTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Changes the loading item of the flow on the bottom of the scrolling list
        *   to a 'no network' icon and assigns the specified OnClick-action.
        *
        *   @param  activity        The according activity context.
        *   @param  actionOnClick   The action to perform when the 'no network' icon is clicked.
        **********************************************************************************************/
        public static final void changeNextLoadingItemToNoNetwork( final Activity activity, final Runnable actionOnClick )
        {
            flow.changeNextLoadingItemToNoNetwork( activity, actionOnClick );
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the user-wall is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            flow = new PicFoodImageContainerFlow
            (
                PicFoodState.EPivotalMenu.getActivity(),
                PicFoodStatePivotalTabWall.singleton.iContainerWallImages,
                PicFoodStatePivotalTabWall.singleton.iScrollView,
                0
            );
        }

        /**********************************************************************************************
        *   Unselects all buttons and links for all images.
        **********************************************************************************************/
/*
        public static final void unselectAllImages()
        {
            flow.unselectAllImages();
        }
*/
    }
