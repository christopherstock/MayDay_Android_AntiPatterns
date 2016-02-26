
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  org.json.*;
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
    public class PicFoodFlowAuth
    {
        /**********************************************************************************************
        *   Handles the login-connection.
        **********************************************************************************************/
        public static final void login()
        {
            //pick username and password
            String username = PicFoodFlowUser.handlerLoginUsername.getText();
            String password = PicFoodFlowUser.handlerLoginPassword.getText();

            //check if username is empty
            if ( LibString.isEmpty( username ) )
            {
                //hide please wait dialog and show 'username required'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELogin.getActivity() );
                PicFoodActionDialog.EDialogLoginUsernameRequired.run();

                //abort login
                return;
            }

            //check if password is empty
            if ( LibString.isEmpty( password ) )
            {
                //hide please wait dialog and show 'password required'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELogin.getActivity() );
                PicFoodActionDialog.EDialogLoginPasswordRequired.run();

                //abort login
                return;
            }

            try
            {
                //connect login and pick result
                JSONObject  response    = PicFoodJsonRPCAuth.login( username, password, true );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //assign session-id and user-id
                        PicFoodIDM.assignLogin
                        (
                            PicFoodState.ELogin.getActivity(),
                            LibJSON.getJSONStringSecure( response, "sessionId" ),
                            LibJSON.getJSONStringSecure( response, "userId"    ),
                            username,
                            LibIO.getMD5( password )
                        );

                        //hide please wait dialog and show 'pivotal menu'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELogin.getActivity() );
                        PicFoodActionState.EEnterPivotalFromLogin.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELogin.getActivity() );
                        PicFoodActionDialog.EDialogLoginTechnicalError.run();

                        //report this exception!
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.ELogin );
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'login failed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELogin.getActivity() );
                        PicFoodActionDialog.EDialogLoginFailed.run();
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELogin.getActivity() );
                    PicFoodActionDialog.EDialogLoginNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'internal error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELogin.getActivity() );
                    PicFoodActionDialog.EDialogLoginTechnicalError.run();

                    //report this exception!
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on logging in" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Handles the 'lost-password'-button.
        **********************************************************************************************/
        public static final void lostPassword()
        {
            //pick username
            String username = PicFoodFlowUser.handlerLostPasswordUsername.getText();

            //check if username is empty
            if ( LibString.isEmpty( username ) )
            {
                //hide please wait dialog and show 'username required'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELostPassword.getActivity() );
                PicFoodActionDialog.EDialogLostPasswordUsernameRequired.run();

                //abort request password
                return;
            }

            try
            {
                //connect request-password and pick result
                JSONObject  response    = PicFoodJsonRPCAuth.restorePassword( username );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //hide please wait dialog and show 'ok'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELostPassword.getActivity() );
                        PicFoodActionDialog.EDialogLostPasswordOk.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELostPassword.getActivity() );
                        PicFoodActionDialog.EDialogLostPasswordTechnicalError.run();

                        //report this exception!
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.ELostPassword );
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'username not existent'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELostPassword.getActivity() );
                        PicFoodActionDialog.EDialogLostPasswordUsernameNotFound.run();
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELostPassword.getActivity() );
                    PicFoodActionDialog.EDialogLostPasswordNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ELostPassword.getActivity() );
                    PicFoodActionDialog.EDialogLostPasswordTechnicalError.run();

                    //report this exception!
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on lost-password-streaming" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }
    }
