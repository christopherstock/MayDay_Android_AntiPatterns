
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsSettings.InputFields;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.AntiPatternsSave.SaveKey;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  org.json.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds all authentification tasks.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowUser
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
                JSONObject  response    = AntiPatternsJsonRPCUser.logout(state.getActivity());
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //do NOT show 'technical error' but report this exception
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);
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
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //ignore this no network! The logout-connection is NOT mandatory!
                }
                else
                {
                    //do NOT show 'technical error' but report this exception
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Logout threw an Exception" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }

            //perform logout
            AntiPatternsIDM.logout(state);
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
                AntiPatternsActionDialog.EDialogFeedbackInvalid.run();

                //abort feedback
                return;
            }

            try
            {
                //commit logout
                JSONObject  response    = AntiPatternsJsonRPCUser.sendFeedback(state.getActivity(), feedback);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog and show 'thanks for your feedback'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        AntiPatternsActionDialog.EDialogFeedbackSent.run();
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
                        AntiPatternsActionDialog.EDialogFeedbackTechnicalError.run();

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
                    AntiPatternsActionDialog.EDialogFeedbackNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogFeedbackTechnicalError.run();

                    //report this exception
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
                AntiPatternsActionDialog.EDialogDeleteAccountEmptyPassword.run();

                //abort removal
                return;
            }

            try
            {
                //commit logout
                JSONObject  response    = AntiPatternsJsonRPCUser.removeUser(state.getActivity(), password);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //the user has been deleted!
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //perform logout
                        AntiPatternsIDM.logout(state);
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_WRONG_PASSWORD:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //show 'wrong password'
                        AntiPatternsActionDialog.EDialogDeleteAccountWrongPassword.run();

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
                        AntiPatternsActionDialog.EDialogDeleteAccountTechnicalError.run();

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
                    AntiPatternsActionDialog.EDialogDeleteAccountNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogDeleteAccountTechnicalError.run();

                    //report this exception
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
                AntiPatternsActionDialog.EDialogChangePasswordEmptyOldPassword.run();
                return;
            }

            //check if new password is empty or too short
            if ( LibString.isEmpty( newPassword ) || LibString.isShorterThan( newPassword, InputFields.MIN_PASSWORD_LENGTH ) )
            {
                //hide please wait dialog, show 'empty new password' and abort
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                AntiPatternsActionDialog.EDialogChangePasswordInvalidNewPassword.run();
                return;
            }

            //check if new password repeat is empty
            if ( LibString.isEmpty( newPasswordRepeat ) )
            {
                //hide please wait dialog, show 'empty new password repeat' and abort
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                AntiPatternsActionDialog.EDialogChangePasswordEmptyNewPasswordRepeat.run();
                return;
            }

            //check if new password and new password repeat do NOT match
            if ( !newPassword.equals( newPasswordRepeat ) )
            {
                //hide please wait dialog, show 'empty new password repeat' and abort
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                AntiPatternsActionDialog.EDialogChangePasswordMismatchingNewPasswordRepeat.run();
                return;
            }

            try
            {
                //commit password-change
                JSONObject  response    = AntiPatternsJsonRPCUser.changePassword(state.getActivity(), oldPassword, newPassword);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //the password has been changed - dismiss the progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //change the saved password
                        String newPasswordMD5 = LibIO.getMD5( newPassword );
                        AntiPatternsSave.saveSetting(state.getActivity(), SaveKey.ELastLoginPasswordMD5, newPasswordMD5);

                        //show dialog
                        AntiPatternsActionDialog.EDialogChangePasswordOk.run();

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_WRONG_PASSWORD:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //show 'wrong password'
                        AntiPatternsActionDialog.EDialogChangePasswordWrongPassword.run();

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
                        AntiPatternsActionDialog.EDialogChangePasswordTechnicalError.run();

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
                    AntiPatternsActionDialog.EDialogChangePasswordNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogChangePasswordTechnicalError.run();

                    //report this exception
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }
    }
