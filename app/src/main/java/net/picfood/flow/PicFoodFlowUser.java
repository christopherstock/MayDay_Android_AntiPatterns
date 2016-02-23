/*  $Id: PicFoodFlowUser.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  net.picfood.*;
    import  net.picfood.PicFoodSettings.InputFields;
    import  net.picfood.action.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.SaveKey;
    import  net.picfood.io.jsonrpc.*;
    import  org.json.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.*;
    import  com.synapsy.android.lib.ui.dialog.*;
    import  com.synapsy.android.lib.util.*;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds all authentification tasks.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowUser.java $"
    **********************************************************************************************/
    public class PicFoodFlowUser
    {
        /** The dialog input handler for the dialog input field 'send feedback: your feedback'. */
        public      static          LibDialogInputHandler       inputHandlerFeedback                            = new LibDialogInputHandler();
        /** The dialog input handler for the dialog input field 'delete user: your password'. */
        public      static          LibDialogInputHandler       inputHandlerDeleteUserPasswordConfirmation      = new LibDialogInputHandler();
        /** The dialog input handler for the dialog input field 'change password: your old password'. */
        public      static          LibDialogInputHandler       inputHandlerChangePasswordOldPassword           = new LibDialogInputHandler();
        /** The dialog input handler for the dialog input field 'change password: your new password'. */
        public      static          LibDialogInputHandler       inputHandlerChangePasswordNewPassword           = new LibDialogInputHandler();
        /** The dialog input handler for the dialog input field 'change password: your new confirmed password'. */
        public      static          LibDialogInputHandler       inputHandlerChangePasswordNewPasswordRepeat     = new LibDialogInputHandler();

        /** The InputFieldHandler for the InputField 'username'. */
        public      static          LibInputFieldHandler        handlerLostPasswordUsername                     = new LibInputFieldHandler();
        /** The InputFieldHandler for the InputField 'username'. */
        public      static          LibInputFieldHandler        handlerLoginUsername                            = new LibInputFieldHandler();
        /** The InputFieldHandler for the InputField 'password'. */
        public      static          LibInputFieldHandler        handlerLoginPassword                            = new LibInputFieldHandler();

        /**********************************************************************************************
        *   Handles the 'logout'-task.
        *
        *   @param  state       The according state.
        **********************************************************************************************/
        public static final void logout( LibState state )
        {
            try
            {
                //commit logout
                JSONObject  response    = PicFoodJsonRPCUser.logout( state.getActivity() );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //do NOT show 'technical error' but report this exception
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                        break;
                    }

                    default:
                    {
                        //ignore different status codes ( session expired etc. )
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //ignore this no network! The logout-connection is NOT mandatory!
                }
                else
                {
                    //do NOT show 'technical error' but report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Logout threw an Exception" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }

            //perform logout
            PicFoodIDM.logout( state );
        }

        /**********************************************************************************************
        *   Handles the 'sendFeedback'-task.
        *
        *   @param  state       The according state.
        **********************************************************************************************/
        public static final void sendFeedback( LibState state )
        {
            //pick feedback from InputHandler
            String feedback = inputHandlerFeedback.getText();

            //check if feedback is empty or too short
            if ( LibString.isEmpty( feedback ) || LibString.isShorterThan( feedback, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'username required'
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                PicFoodActionDialog.EDialogFeedbackInvalid.run();

                //abort feedback
                return;
            }

            try
            {
                //commit logout
                JSONObject  response    = PicFoodJsonRPCUser.sendFeedback( state.getActivity(), feedback );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog and show 'thanks for your feedback'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        PicFoodActionDialog.EDialogFeedbackSent.run();
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
                        PicFoodActionDialog.EDialogFeedbackTechnicalError.run();

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
                    PicFoodActionDialog.EDialogFeedbackNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    PicFoodActionDialog.EDialogFeedbackTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Handles the 'removeUser'-task.
        *
        *   @param  state       The according state.
        **********************************************************************************************/
        public static final void removeUser( LibState state )
        {
            //pick password from InputHandler
            String password = inputHandlerDeleteUserPasswordConfirmation.getText();

            //check if feedback is empty
            if ( LibString.isEmpty( password ) )
            {
                //hide please wait dialog and show 'password required'
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                PicFoodActionDialog.EDialogDeleteAccountEmptyPassword.run();

                //abort removal
                return;
            }

            try
            {
                //commit logout
                JSONObject  response    = PicFoodJsonRPCUser.removeUser( state.getActivity(), password );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //the user has been deleted!
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //perform logout
                        PicFoodIDM.logout( state );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_WRONG_PASSWORD:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //show 'wrong password'
                        PicFoodActionDialog.EDialogDeleteAccountWrongPassword.run();

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
                        PicFoodActionDialog.EDialogDeleteAccountTechnicalError.run();

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
                    PicFoodActionDialog.EDialogDeleteAccountNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    PicFoodActionDialog.EDialogDeleteAccountTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Handles the 'changePassword'-task.
        *
        *   @param  state       The according state.
        **********************************************************************************************/
        public static final void changePassword( LibState state )
        {
            //pick password from InputHandler
            String oldPassword       = inputHandlerChangePasswordOldPassword.getText();
            String newPassword       = inputHandlerChangePasswordNewPassword.getText();
            String newPasswordRepeat = inputHandlerChangePasswordNewPasswordRepeat.getText();

            //check if old password is empty
            if ( LibString.isEmpty( oldPassword ) )
            {
                //hide please wait dialog, show 'empty old password' and abort
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                PicFoodActionDialog.EDialogChangePasswordEmptyOldPassword.run();
                return;
            }

            //check if new password is empty or too short
            if ( LibString.isEmpty( newPassword ) || LibString.isShorterThan( newPassword, InputFields.MIN_PASSWORD_LENGTH ) )
            {
                //hide please wait dialog, show 'empty new password' and abort
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                PicFoodActionDialog.EDialogChangePasswordInvalidNewPassword.run();
                return;
            }

            //check if new password repeat is empty
            if ( LibString.isEmpty( newPasswordRepeat ) )
            {
                //hide please wait dialog, show 'empty new password repeat' and abort
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                PicFoodActionDialog.EDialogChangePasswordEmptyNewPasswordRepeat.run();
                return;
            }

            //check if new password and new password repeat do NOT match
            if ( !newPassword.equals( newPasswordRepeat ) )
            {
                //hide please wait dialog, show 'empty new password repeat' and abort
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                PicFoodActionDialog.EDialogChangePasswordMismatchingNewPasswordRepeat.run();
                return;
            }

            try
            {
                //commit password-change
                JSONObject  response    = PicFoodJsonRPCUser.changePassword( state.getActivity(), oldPassword, newPassword );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //the password has been changed - dismiss the progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //change the saved password
                        String newPasswordMD5 = LibIO.getMD5( newPassword );
                        PicFoodSave.saveSetting( state.getActivity(), SaveKey.ELastLoginPasswordMD5, newPasswordMD5 );

                        //show dialog
                        PicFoodActionDialog.EDialogChangePasswordOk.run();

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_WRONG_PASSWORD:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //show 'wrong password'
                        PicFoodActionDialog.EDialogChangePasswordWrongPassword.run();

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
                        PicFoodActionDialog.EDialogChangePasswordTechnicalError.run();

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
                    PicFoodActionDialog.EDialogChangePasswordNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    PicFoodActionDialog.EDialogChangePasswordTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }
    }
