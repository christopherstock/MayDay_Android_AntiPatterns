/*  $Id: PicFoodFlowImage.java 50565 2013-08-12 13:05:39Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  java.io.*;
    import  org.json.*;
    import  android.graphics.drawable.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.*;
    import  com.synapsy.android.lib.ui.dialog.*;
    import  com.synapsy.android.lib.util.*;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  net.picfood.*;
    import  net.picfood.PicFoodSettings.*;
    import  net.picfood.action.*;
    import  net.picfood.data.*;
    import  net.picfood.flow.ui.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.*;
    import  net.picfood.io.jsonrpc.*;

    /**********************************************************************************************
    *   Specifies the flow for image tasks.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50565 $ $Date: 2013-08-12 15:05:39 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowImage.java $"
    **********************************************************************************************/
    public class PicFoodFlowImage
    {
        /** The dialog input handler for the dialog input field 'report abuse'. */
        public              static          LibDialogInputHandler           inputHandlerReportAbuse     = new LibDialogInputHandler();

        /** The active activity when the user performed an image action. */
        public              static          LibState                        lastState                   = null;
        /** The image the user performed an image action on. */
        public              static          PicFoodDataImage                lastImage                   = null;
        /** The last food rating the user has clicked in the 'select rating' dialog. */
        public              static          Integer                         lastFoodRating              = null;

        /** Encapsulates the GridView flow according to the Strategy pattern. */
        public              static          PicFoodGridViewFlow             flow                        = null;

        /**********************************************************************************************
        *   Handles the 'sendImageFeedback'-task.
        *
        *   @param  report  The feedback report to submit for the selected image.
        **********************************************************************************************/
        public static final void reportAbuse( String report )
        {
            //check if report is empty
            if ( LibString.isEmpty( report ) || LibString.isShorterThan( report, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'report required'
                LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                PicFoodActionDialog.EDialogReportAbuseInvalid.run();

                //abort report
                return;
            }

            try
            {
                //commit report
                JSONObject  response    = PicFoodJsonRPCImage.sendImageFeedback( lastState.getActivity(), PicFoodSettings.Image.DEFAULT_IMAGE_FEEDBACK_CATEGORY, lastImage.iImageID, report );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog and show 'thanks for your report'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        PicFoodActionDialog.EDialogReportAbuseSent.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( lastState );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        PicFoodActionDialog.EDialogReportAbuseTechnicalError.run();

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    PicFoodActionDialog.EDialogReportAbuseNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    PicFoodActionDialog.EDialogReportAbuseTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Handles the 'remove image'-task.
        **********************************************************************************************/
        public static final void removeImage()
        {
            try
            {
                //commit remove request
                JSONObject  response    = PicFoodJsonRPCImage.removeImage( lastState.getActivity(), lastImage.iImageID );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );

                        //unselect image buttons
                        PicFoodActionUnselect.EUnselectButtonsImage.run();

                        //update the list that held this image
                        PicFoodActionUpdate.EUpdateImageListAfterImageRemove.run();

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        PicFoodActionDialog.EDialogRemoveImageTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( lastState );
                        break;
                    }

                    default:
                    {
                        //unexpected return code - perform nothing!

                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    PicFoodActionDialog.EDialogRemoveImageNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );

                    //unselect image buttons
                    PicFoodActionUnselect.EUnselectButtonsImage.run();

                    //do NOT show 'technical error' but report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Handles the 'share image'-task.
        **********************************************************************************************/
        public static final void shareImage()
        {
            String url = null;

            try
            {
                //stream the image and save it to disk
                                url                 = lastImage.iURL.replace( "{size}", "" );
                File            share               = PicFoodSD.getFileImageToShare( lastImage );
                BitmapDrawable  b                   = PicFoodCache.getBitmapDrawableFromHttpOrCache( lastState.getActivity(), url );
                LibIO.writeFile( share, LibImage.getBytesFromBitmapAsJPEG( Image.SHARED_JPEG_QUALITY, b.getBitmap() ) );

                //unselect options-button immediately and hide the progress-dialog
                lastImage.unselectAllButtonsUIThreaded(             lastState.getActivity() );
                LibDialogProgress.dismissProgressDialogUIThreaded(  lastState.getActivity() );

                //specify texts
                String dialogTitle  = LibResource.getResourceString( lastState.getActivity(), R.string.dialog_image_share_chooser_title  );
                String subject      = LibResource.getResourceString( lastState.getActivity(), R.string.dialog_image_share_subject        );
                String body         = LibResource.getResourceString( lastState.getActivity(), R.string.dialog_image_share_body           );

                //replace tokens
                subject = subject.replace(  "{username}",   lastImage.iOwner.iUserName );
                body    = body.replace(     "{username}",   lastImage.iOwner.iUserName );
                body    = body.replace(     "{date}",       LibStringFormat.getSingleton().formatDateTimeShort( lastImage.iCreateDate ) );
                body    = body.replace(     "{url}",        url );

                //launch share-image
                LibLauncher.launchShareImage
                (
                    lastState.getActivity(),
                    share,
                    dialogTitle,
                    subject,
                    body
                );
            }
            catch ( IOException ioe )
            {
                //hide the progress-dialog and show 'no network'
                LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                PicFoodActionDialog.EDialogShareImageNoNetwork.run();
            }
            catch ( Throwable t )
            {
                //hide the progress-dialog and show 'technical error'
                LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                PicFoodActionDialog.EDialogShareImageTechnicalError.run();

                //report this error
                PicFoodDebug.DEBUG_THROWABLE( t, "Error on sharing image [" + url + "]!", UncaughtException.ENo );
            }
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the image properties 'comments' is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            flow = new PicFoodGridViewFlow( 0 );
        }
    }
