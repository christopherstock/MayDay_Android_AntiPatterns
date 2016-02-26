
    package de.mayflower.antipatterns.flow;

    import  org.json.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import de.mayflower.antipatterns.io.AntiPatternsSave.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;

    /**********************************************************************************************
    *   Manages automatic login.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowAutoLogin
    {
        /**********************************************************************************************
        *   The last created state that order automatic login.
        **********************************************************************************************/
        public          static AntiPatternsState lastState                   = null;

        /**********************************************************************************************
        *   Performs a check in order to perform an auto-login.
        *   The saved username and md5-password is read and a login is attempted.
        *   If the login fails, the saved username and md5-password will be deleted.
        *
        *   @param  state   The state that launched the auto-login-request.
        **********************************************************************************************/
        public static final void checkAutoLogin( final AntiPatternsState state )
        {
            //assign last state
            lastState = state;

            //pick settings for auto-login
            final String username    = AntiPatternsSave.loadSetting(state.getActivity(), SaveKey.ELastLoginUsername);
            final String passwordMD5 = AntiPatternsSave.loadSetting(state.getActivity(), SaveKey.ELastLoginPasswordMD5);

            //only perform an auto-login if username and md5-password are present
            if ( username != null && passwordMD5 != null )
            {
                AntiPatternsDebug.autoLogin.out( "tryAutoLogin with username [" + username + "] password [" + passwordMD5 + "]" );

                //show dialog 'auto login'
                LibDialogProgress.showProgressDialogUIThreaded
                (
                    state.getActivity(),
                    R.string.dialog_auto_login_title,
                    R.string.dialog_auto_login_body,
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //this will be performed on the UI-Thread!
                            AntiPatternsFlowAutoLogin.tryAutoLoginAfterProgressDialogThreaded
                                    (
                                            username,
                                            passwordMD5
                                    );
                        }
                    },
                    false,
                    null
                );
            }
        }

        /**********************************************************************************************
        *   Performs a check in order to perform an auto-login.
        *   The saved username and md5-password is read and a login is attempted.
        *   If the login fails, the saved username and md5-password will be deleted.
        *
        *   @param  username    The saved username.
        *   @param  passwordMD5 The saved password as an md5 encrypted String.
        **********************************************************************************************/
        protected static final void tryAutoLoginAfterProgressDialogThreaded
        (
            final   String          username,
            final   String          passwordMD5
        )
        {
            //run in a new thread
            new Thread()
            {
                @Override
                public void run()
                {
                    tryAutoLogin( username, passwordMD5 );
                }
            }.start();
        }

        /**********************************************************************************************
        *   Performs a check in order to perform an auto-login.
        *   The saved username and md5-password is read and a login is attempted.
        *   If the login fails, the saved username and md5-password will be deleted.
        *
        *   @param  username    The saved username.
        *   @param  passwordMD5 The saved password as an md5 encrypted String.
        **********************************************************************************************/
        protected static final void tryAutoLogin
        (
            final   String          username,
            final   String          passwordMD5
        )
        {
            AntiPatternsDebug.autoLogin.out( "tryAutoLoginAfterProgressDialog with username [" + username + "] password [" + passwordMD5 + "]" );

            try
            {
                //connect login and pick result
                JSONObject  response    = AntiPatternsJsonRPCAuth.login(username, passwordMD5, false);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //assign session-id and user-id
                        AntiPatternsIDM.assignLogin
                                (
                                        lastState.getActivity(),
                                        LibJSON.getJSONStringSecure(response, "sessionId"),
                                        LibJSON.getJSONStringSecure(response, "userId"),
                                        username,
                                        passwordMD5
                                );

                        //hide please wait dialog and show 'pivotal menu'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        AntiPatternsActionState.EEnterPivotalFromAutoLogin.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and ditch the data
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        AntiPatternsIDM.assignLogout(lastState.getActivity());

                        //report this exception!
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
                        //do nothing and ditch all data
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        AntiPatternsIDM.assignLogout(lastState.getActivity());
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //do nothing and ditch all data
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    AntiPatternsIDM.assignLogout(lastState.getActivity());
                }
                else
                {
                    //do nothing and ditch all data
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    AntiPatternsIDM.assignLogout(lastState.getActivity());
                }
            }
        }
    }
