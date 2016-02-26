/*  $Id: PicFoodFlowImageComment.java 50555 2013-08-12 09:22:12Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodSettings.InputFields;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.GridViews;
    import  org.json.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Specifies the flow for image-comment tasks.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50555 $ $Date: 2013-08-12 11:22:12 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowImageComment.java $"
    **********************************************************************************************/
    public class PicFoodFlowImageComment
    {
        /** The dialog input handler for the dialog input field 'comment'. */
        public              static          LibDialogInputHandler       inputHandlerComment         = new LibDialogInputHandler();

        /**********************************************************************************************
        *   Handles the 'comment'-task.
        *
        *   @param  comment     The comment to submit for the selected image.
        **********************************************************************************************/
        public static final void commentImage( String comment )
        {
            //check if comment is empty
            if ( LibString.isEmpty( comment ) || LibString.isShorterThan( comment, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'report required'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                PicFoodActionDialog.EDialogCommentInvalid.run();

                //abort report
                return;
            }

            try
            {
                //commit report
                JSONObject  response    = PicFoodJsonRPCImage.postComment( PicFoodFlowImage.lastState.getActivity(), PicFoodFlowImage.lastImage.iImageID, comment );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog and show 'thanks for your report'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                        PicFoodActionDialog.EDialogCommentSent.run();

                        //update according image list after image has been changed
                        PicFoodActionUpdate.EUpdateImageListAfterImageChange.run();

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
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                        PicFoodActionDialog.EDialogCommentTechnicalError.run();

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                    PicFoodActionDialog.EDialogCommentNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                    PicFoodActionDialog.EDialogCommentTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /*****************************************************************************
        *   Orders all 'image comments' for one specific image
        *   and displays them in state 'image properties'.
        *
        *   @param  state                   The according state.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *****************************************************************************/
        public static final void orderNextImageComments
        (
            LibState        state,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError
        )
        {
            //order image comments
            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering comments\n" + PicFoodFlowImage.flow.toString() );

                //order detailed image data
                JSONObject  response    = PicFoodJsonRPCImage.getComments
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
                        //parse ImageComments from response
                        JSONObject          imageComments   = LibJSON.getJSONObjectSecure(      response,           "comments"      );
                      //JSONArray           comments        = LibJSON.getJSONArraySecure(       imageComments,      "comments"      );
                        PicFoodData[]       newComments     = PicFoodDataComment.parse(         response );
                        int                 count           = LibJSON.getJSONIntegerSecure( imageComments, "count" );

                        PicFoodDebug.imageProperties.out( "Parsed [" + newComments.length + "] new imageComments from response" );

                        //let the flow handle the parsed data
                        PicFoodFlowImage.flow.handleParsedData
                        (
                            state,
                            newComments,
                            count,
                            PicFoodActionPush.EPushListEntryImagePropertiesResults,
                            GridViews.EImageProperties,
                            PicFoodActionUpdate.EUpdateImagePropertiesCommentsNextOffset,
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
                        //dismiss progress dialog and perform 'technical error'-action
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
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "Throwable caught on loading comments", UncaughtException.ENo );
                }
            }
        }
    }
