/*  $Id: PicFoodFlowAutoLogin.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  org.json.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.dialog.*;
    import  com.synapsy.android.lib.util.*;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.*;
    import  net.picfood.io.jsonrpc.*;
    import  net.picfood.state.*;

    /**********************************************************************************************
    *   Manages automatic login.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowAutoLogin.java $"
    **********************************************************************************************/
    public class PicFoodFlowAutoLogin
    {
        /**********************************************************************************************
        *   The last created state that order automatic login.
        **********************************************************************************************/
        public          static                  PicFoodState                    lastState                   = null;

        /**********************************************************************************************
        *   Performs a check in order to perform an auto-login.
        *   The saved username and md5-password is read and a login is attempted.
        *   If the login fails, the saved username and md5-password will be deleted.
        *
        *   @param  state   The state that launched the auto-login-request.
        **********************************************************************************************/
        public static final void checkAutoLogin( final PicFoodState state )
        {
            //assign last state
            lastState = state;

            //pick settings for auto-login
            final String username    = PicFoodSave.loadSetting( state.getActivity(), SaveKey.ELastLoginUsername    );
            final String passwordMD5 = PicFoodSave.loadSetting( state.getActivity(), SaveKey.ELastLoginPasswordMD5 );

            //only perform an auto-login if username and md5-password are present
            if ( username != null && passwordMD5 != null )
            {
                PicFoodDebug.autoLogin.out( "tryAutoLogin with username [" + username + "] password [" + passwordMD5 + "]" );

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
                            PicFoodFlowAutoLogin.tryAutoLoginAfterProgressDialogThreaded
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
            PicFoodDebug.autoLogin.out( "tryAutoLoginAfterProgressDialog with username [" + username + "] password [" + passwordMD5 + "]" );

            try
            {
                //connect login and pick result
                JSONObject  response    = PicFoodJsonRPCAuth.login( username, passwordMD5, false );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //assign session-id and user-id
                        PicFoodIDM.assignLogin
                        (
                            lastState.getActivity(),
                            LibJSON.getJSONStringSecure( response, "sessionId" ),
                            LibJSON.getJSONStringSecure( response, "userId"    ),
                            username,
                            passwordMD5
                        );

                        //hide please wait dialog and show 'pivotal menu'
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        PicFoodActionState.EEnterPivotalFromAutoLogin.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and ditch the data
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        PicFoodIDM.assignLogout( lastState.getActivity() );

                        //report this exception!
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
                        //do nothing and ditch all data
                        LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                        PicFoodIDM.assignLogout( lastState.getActivity() );
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //do nothing and ditch all data
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    PicFoodIDM.assignLogout( lastState.getActivity() );
                }
                else
                {
                    //do nothing and ditch all data
                    LibDialogProgress.dismissProgressDialogUIThreaded( lastState.getActivity() );
                    PicFoodIDM.assignLogout( lastState.getActivity() );
                }
            }
        }
    }
