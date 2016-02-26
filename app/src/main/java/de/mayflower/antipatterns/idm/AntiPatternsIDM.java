
    package de.mayflower.antipatterns.idm;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.io.*;
    import de.mayflower.antipatterns.io.AntiPatternsSave.*;
    import  de.mayflower.antipatterns.state.acclaim.*;
    import  de.mayflower.antipatterns.state.auth.*;
    import  android.app.*;
    import  android.content.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.dialog.*;

    /*****************************************************************************************
    *   The pivotal class for identity management.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsIDM
    {
        /** The last activity class that has been assigned when a 'session expired' response was received. */
        public      static          LibState                    lastStateBeforeSessionExpired           = null;

        /*****************************************************************************************
        *   Assigns the data that has been received after a successful login:
        *   The session-ID and the user-ID.
        *
        *   @param  activity        The according activity context.
        *   @param  newSessionID    The currently received session id of the currently logged in user.
        *   @param  newUserID       The currently received user id of the currently logged in user.
        *   @param  userName        The currently used username.
        *   @param  passwordMD5     The currently used password as an md5 encrypted String.
        *****************************************************************************************/
        public static final void assignLogin( Activity activity, String newSessionID, String newUserID, String userName, String passwordMD5 )
        {
            //save sessionID and userID
            AntiPatternsSave.saveSetting(activity, SaveKey.ESessionID, newSessionID);
            AntiPatternsSave.saveSetting(activity, SaveKey.EUserID, newUserID);

            AntiPatternsSave.saveSetting(activity, SaveKey.ELastLoginUsername, userName);
            AntiPatternsSave.saveSetting(activity, SaveKey.ELastLoginPasswordMD5, passwordMD5);
        }

        /*****************************************************************************************
        *   Prunes all current session and user data that is managed by the IDM system.
        *   This method is invoked, when the user has successfully logged out or
        *   if he was disconnected from the backend system.
        *
        *   @param  activity        The according activity context.
        *****************************************************************************************/
        public static final void assignLogout( Activity activity )
        {
            //strip off all IDs
            AntiPatternsSave.saveSetting(activity, SaveKey.ESessionID, null);
            AntiPatternsSave.saveSetting(activity, SaveKey.EUserID, null);
            AntiPatternsSave.saveSetting(activity, SaveKey.EFacebookUID, null);

            AntiPatternsSave.saveSetting(activity, SaveKey.ELastLoginUsername, null);
            AntiPatternsSave.saveSetting(activity, SaveKey.ELastLoginPasswordMD5, null);
        }

        /*****************************************************************************************
        *   Being invoked, when the user's Facebook-UID becomes known.
        *
        *   @param  activity    The according activity context.
        *   @param  uid         The Facebook UID of the current logged in user.
        *****************************************************************************************/
        public static final void assignFacebookUID( Activity activity, String uid )
        {
            AntiPatternsSave.saveSetting(activity, SaveKey.EFacebookUID, uid);
        }

        /*****************************************************************************************
        *   Delivers the current sessionID of the current active user.
        *
        *   @param  context     The current system context.
        *   @return             The session ID that is used by the current active user.
        *****************************************************************************************/
        public static final String getSessionID( Context context )
        {
            return AntiPatternsSave.loadSetting(context, SaveKey.ESessionID);
        }

        /*****************************************************************************************
        *   Delivers the current userID of the current active user.
        *
        *   @param  context     The current system context.
        *   @return             The user ID that is used by the current active user.
        *****************************************************************************************/
        public static final String getUserID( Context context )
        {
            return AntiPatternsSave.loadSetting(context, SaveKey.EUserID);
        }

        /*****************************************************************************************
        *   Delivers the current Facebook UID of the current active user.
        *
        *   @param  context     The current system context.
        *   @return             The Facebook UID that is used by the current active user.
        *****************************************************************************************/
        public static final String getFacebookUID( Context context )
        {
            return AntiPatternsSave.loadSetting(context, SaveKey.EFacebookUID);
        }

        /*****************************************************************************************
        *   Logs out the current user. All session data is stripped off and the user
        *   will be delegated to the Login state.
        *
        *   @param  state           The according state.
        *****************************************************************************************/
        public static final void logout( LibState state )
        {
            //strip off sessionID and userID
            assignLogout( state.getActivity() );

            //prune register- and login-fields if the states are still available
            AntiPatternsStateRegister.clearAllFieldsUIThreaded();
            AntiPatternsStateLogin.clearAllFieldsUIThreaded();

            //prune the input-handler for 'delete user'
            AntiPatternsFlowUser.inputHandlerDeleteUserPasswordConfirmation.destroy();

            //dismiss progress dialog
            LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

            //set acclaim-state to last page and show 'acclaim' state
            AntiPatternsStateAcclaim.setLastPageIndexToLastPage();
            LibLauncher.launchActivity( state.getActivity(), AntiPatternsStateAcclaim.class, R.anim.fade_in, R.anim.fade_out );
        }

        /*****************************************************************************************
        *   Being invoked, when the status code {@link de.mayflower.antipatterns.io.jsonrpc.AntiPatternsJsonRPC#ERROR_CODE_SESSION_EXPIRED}
        *   is received, this method assigns the last activity before session expiry and launches
        *   the dialog 'session expired'.
        *
        *   @param  state           The according state.
        *****************************************************************************************/
        public static final void sessionExpired( LibState state )
        {
            //AntiPatternsDebug.major.out( "The session has EXPIRED! The user will be redirected to the login-page!" );

            lastStateBeforeSessionExpired = state;

            //show dialog and perform a logout!
            AntiPatternsActionDialog.EDialogSessionExpired.run();
        }
    }
