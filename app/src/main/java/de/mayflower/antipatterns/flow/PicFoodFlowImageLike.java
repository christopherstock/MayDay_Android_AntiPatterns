/*  $Id: PicFoodFlowImageLike.java 50555 2013-08-12 09:22:12Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.GridViews;
    import  org.json.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Specifies the flow for image-like tasks.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50555 $ $Date: 2013-08-12 11:22:12 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowImageLike.java $"
    **********************************************************************************************/
    public class PicFoodFlowImageLike
    {
        /** Being invoked when 'like' or 'unlike' is pushed. */
        public static final void likeOrUnlike()
        {
            //get current like status and new like status ( to commit )
                  boolean currentLikeState    = PicFoodFlowImage.lastImage.iILike;
            final boolean likeStateToCommit   = !currentLikeState;

            PicFoodDebug.likeUnlike.out( "Push 'like' / 'unlike' on image. Current like state is [" + currentLikeState + "] toCommit: [" + likeStateToCommit + "]" );

            try
            {
                //commit like vote
                JSONObject  response    = PicFoodJsonRPCImage.likeImage( PicFoodFlowImage.lastState.getActivity(), likeStateToCommit, PicFoodFlowImage.lastImage.iImageID );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog if no new progress dialog is shown
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );

                        //unselect image buttons
                        PicFoodActionUnselect.EUnselectButtonsImage.run();

                        //update according image list after image has been changed
                        PicFoodActionUpdate.EUpdateImageListAfterImageChange.run();

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                        PicFoodActionDialog.EDialogLikeUnlikeTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodFlowImage.lastState );
                        break;
                    }

                    default:
                    {
                        //unexpected return code - perform nothing!

                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );

                        //unselect image buttons
                        PicFoodActionUnselect.EUnselectButtonsImage.run();

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                    PicFoodActionDialog.EDialogLikeUnlikeNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );

                    //unselect image buttons
                    PicFoodActionUnselect.EUnselectButtonsImage.run();

                    //do NOT show 'technical error' but report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /*****************************************************************************
        *   Orders all 'image likes' for one specific image
        *   and displays them in state 'image properties'.
        *
        *   @param  state                   The according state.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *****************************************************************************/
        public static final void orderNextImageLikes
        (
            LibState        state,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError
        )
        {
            //order image likes
            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering likes\n" + PicFoodFlowImage.flow.toString() );

                //order detailed image data
                JSONObject  response    = PicFoodJsonRPCImage.getImageLikes
                (
                    state.getActivity(),
                    PicFoodFlowImage.lastImage.iImageID,
                    PicFoodFlowImage.flow.getOffset()
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse ImageLikes from response
                        JSONObject          imageLikes      = LibJSON.getJSONObjectSecure( response,    "imageLikes"    );
                        PicFoodDataLike[]   newLikes        = PicFoodDataLike.parse( response );
                        int                 count           = LibJSON.getJSONIntegerSecure( imageLikes, "count"         );

                        PicFoodDebug.imageProperties.out( "Parsed [" + newLikes.length + "] imageLikes from response" );

                        //let the flow handle the parsed data
                        PicFoodFlowImage.flow.handleParsedData
                        (
                            state,
                            newLikes,
                            count,
                            PicFoodActionPush.EPushListEntryImagePropertiesResults,
                            GridViews.EImageProperties,
                            PicFoodActionUpdate.EUpdateImagePropertiesLikesNextOffset,
                            false
                        );
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
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        actionOnTechnicalError.run();

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
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "Throwable caught on loading likes", UncaughtException.ENo );
                }
            }
        }
    }
