
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.GridViews;
    import  org.json.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Specifies the flow for image-like tasks.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowImageLike
    {
        /** Being invoked when 'like' or 'unlike' is pushed. */
        public static final void likeOrUnlike()
        {
            //get current like status and new like status ( to commit )
                  boolean currentLikeState    = AntiPatternsFlowImage.lastImage.iILike;
            final boolean likeStateToCommit   = !currentLikeState;

            AntiPatternsDebug.likeUnlike.out( "Push 'like' / 'unlike' on image. Current like state is [" + currentLikeState + "] toCommit: [" + likeStateToCommit + "]" );

            try
            {
                //commit like vote
                JSONObject  response    = AntiPatternsJsonRPCImage.likeImage(AntiPatternsFlowImage.lastState.getActivity(), likeStateToCommit, AntiPatternsFlowImage.lastImage.iImageID);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog if no new progress dialog is shown
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );

                        //unselect image buttons
                        AntiPatternsActionUnselect.EUnselectButtonsImage.run();

                        //update according image list after image has been changed
                        AntiPatternsActionUpdate.EUpdateImageListAfterImageChange.run();

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );
                        AntiPatternsActionDialog.EDialogLikeUnlikeTechnicalError.run();

                        //report this error
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(AntiPatternsFlowImage.lastState);
                        break;
                    }

                    default:
                    {
                        //unexpected return code - perform nothing!

                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );

                        //unselect image buttons
                        AntiPatternsActionUnselect.EUnselectButtonsImage.run();

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );
                    AntiPatternsActionDialog.EDialogLikeUnlikeNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );

                    //unselect image buttons
                    AntiPatternsActionUnselect.EUnselectButtonsImage.run();

                    //do NOT show 'technical error' but report this exception
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
                AntiPatternsDebug.limitOffset.out( "BEFORE ordering likes\n" + AntiPatternsFlowImage.flow.toString() );

                //order detailed image data
                JSONObject  response    = AntiPatternsJsonRPCImage.getImageLikes
                        (
                                state.getActivity(),
                                AntiPatternsFlowImage.lastImage.iImageID,
                                AntiPatternsFlowImage.flow.getOffset()
                        );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //parse ImageLikes from response
                        JSONObject          imageLikes      = LibJSON.getJSONObjectSecure( response,    "imageLikes"    );
                        AntiPatternsDataLike[]   newLikes        = AntiPatternsDataLike.parse(response);
                        int                 count           = LibJSON.getJSONIntegerSecure( imageLikes, "count"         );

                        AntiPatternsDebug.imageProperties.out( "Parsed [" + newLikes.length + "] imageLikes from response" );

                        //let the flow handle the parsed data
                        AntiPatternsFlowImage.flow.handleParsedData
                        (
                            state,
                            newLikes,
                            count,
                            AntiPatternsActionPush.EPushListEntryImagePropertiesResults,
                            GridViews.EImageProperties,
                            AntiPatternsActionUpdate.EUpdateImagePropertiesLikesNextOffset,
                            false
                        );
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(state);
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        actionOnTechnicalError.run();

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
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "Throwable caught on loading likes", UncaughtException.ENo);
                }
            }
        }
    }
