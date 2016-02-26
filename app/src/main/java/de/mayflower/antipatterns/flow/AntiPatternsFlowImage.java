
    package de.mayflower.antipatterns.flow;

    import  java.io.*;
    import  org.json.*;
    import  android.graphics.drawable.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import de.mayflower.antipatterns.*;
    import de.mayflower.antipatterns.AntiPatternsSettings.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.flow.ui.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;

    /**********************************************************************************************
    *   Specifies the flow for image tasks.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowImage
    {
        /** The dialog input handler for the dialog input field 'report abuse'. */
        public              static          LibDialogInputHandler           inputHandlerReportAbuse     = new LibDialogInputHandler();

        /** The active activity when the user performed an image action. */
        public              static          LibState                        lastState                   = null;
        /** The image the user performed an image action on. */
        public              static AntiPatternsDataImage lastImage                   = null;
        /** The last food rating the user has clicked in the 'select rating' dialog. */
        public              static          Integer                         lastFoodRating              = null;

        /** Encapsulates the GridView flow according to the Strategy pattern. */
        public              static AntiPatternsGridViewFlow flow                        = null;

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
                AntiPatternsActionDialog.EDialogReportAbuseInvalid.run();

                //abort report
                return;
            }

            try
            {
                //commit report
                JSONObject  response    = AntiPatternsJsonRPCImage.sendImageFeedback(lastState.getActivity(), AntiPatternsSettings.Image.DEFAULT_IMAGE_FEEDBACK_CATEGORY, lastImage.iImageID, report);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog and show 'thanks for your report'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        AntiPatternsActionDialog.EDialogReportAbuseSent.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(lastState);
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        AntiPatternsActionDialog.EDialogReportAbuseTechnicalError.run();

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    AntiPatternsActionDialog.EDialogReportAbuseNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    AntiPatternsActionDialog.EDialogReportAbuseTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
                JSONObject  response    = AntiPatternsJsonRPCImage.removeImage(lastState.getActivity(), lastImage.iImageID);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );

                        //unselect image buttons
                        AntiPatternsActionUnselect.EUnselectButtonsImage.run();

                        //update the list that held this image
                        AntiPatternsActionUpdate.EUpdateImageListAfterImageRemove.run();

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        AntiPatternsActionDialog.EDialogRemoveImageTechnicalError.run();

                        //report this error
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(lastState);
                        break;
                    }

                    default:
                    {
                        //unexpected return code - perform nothing!

                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    AntiPatternsActionDialog.EDialogRemoveImageNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );

                    //unselect image buttons
                    AntiPatternsActionUnselect.EUnselectButtonsImage.run();

                    //do NOT show 'technical error' but report this exception
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
                File            share               = AntiPatternsSD.getFileImageToShare(lastImage);
                BitmapDrawable  b                   = AntiPatternsCache.getBitmapDrawableFromHttpOrCache(lastState.getActivity(), url);
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
                AntiPatternsActionDialog.EDialogShareImageNoNetwork.run();
            }
            catch ( Throwable t )
            {
                //hide the progress-dialog and show 'technical error'
                LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                AntiPatternsActionDialog.EDialogShareImageTechnicalError.run();

                //report this error
                AntiPatternsDebug.DEBUG_THROWABLE(t, "Error on sharing image [" + url + "]!", UncaughtException.ENo);
            }
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the image properties 'comments' is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            flow = new AntiPatternsGridViewFlow( 0 );
        }
    }
