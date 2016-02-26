/*  $Id: PicFoodActionDialog.java 50571 2013-08-13 09:11:29Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.action;

    import  android.view.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50571 $ $Date: 2013-08-13 11:11:29 +0200 (Di, 13 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/action/PicFoodActionDialog.java $"
    **********************************************************************************************/
    public enum PicFoodActionDialog implements Runnable
    {
        /** */  EDialogChangePasswordEmptyNewPasswordRepeat,
        /** */  EDialogChangePasswordEmptyOldPassword,
        /** */  EDialogChangePasswordInvalidNewPassword,
        /** */  EDialogChangePasswordMismatchingNewPasswordRepeat,
        /** */  EDialogChangePasswordNoNetwork,
        /** */  EDialogChangePasswordOk,
        /** */  EDialogChangePasswordTechnicalError,
        /** */  EDialogChangePasswordWrongPassword,
        /** */  EDialogChangeProfile,
        /** */  EDialogChangeProfileNoNetwork,
        /** */  EDialogChangeProfileTechnicalError,
        /** */  EDialogChangeProfileSucceeded,
        /** */  EDialogChangeProfileBiographyInvalid,
        /** */  EDialogChangeProfileRealNameInvalid,
        /** */  EDialogChangeProfilePhoneNumberInvalid,
        /** */  EDialogChangeProfileWebsiteInvalid,
        /** */  EDialogChangeProfilePickProfileNoNetwork,
        /** */  EDialogChangeProfilePickProfileTechnicalError,
        /** */  EDialogChangeProfileImageSucceeded,
        /** */  EDialogChangeProfileImageNoNetwork,
        /** */  EDialogChangeProfileImageTechnicalError,
        /** */  EDialogCommentInvalid,
        /** */  EDialogCommentNoNetwork,
        /** */  EDialogCommentSent,
        /** */  EDialogCommentTechnicalError,
        /** */  EDialogDeleteAccountEmptyPassword,
        /** */  EDialogDeleteAccountNoNetwork,
        /** */  EDialogDeleteAccountTechnicalError,
        /** */  EDialogDeleteAccountWrongPassword,
        /** */  EDialogDeleteImageCacheSuccess,
        /** */  EDialogDetailedImageNoNetwork,
        /** */  EDialogDetailedImageTechnicalError,
        /** */  EDialogFeedbackInvalid,
        /** */  EDialogFeedbackNoNetwork,
        /** */  EDialogFeedbackSent,
        /** */  EDialogFeedbackTechnicalError,
        /** */  EDialogFindFriendsViaFacebookNoNetwork,
        /** */  EDialogFindFriendsViaFacebookNoResults,
        /** */  EDialogFindFriendsViaFacebookTechnicalError,
        /** */  EDialogFindFriendsViaPhonenumberNoNetwork,
        /** */  EDialogFindFriendsViaPhonenumberNoResults,
        /** */  EDialogFindFriendsViaPhonenumberTechnicalError,
        /** */  EDialogFindFriendsViaSearchTermInvalidTerm,
        /** */  EDialogFindFriendsViaSearchTermNoNetwork,
        /** */  EDialogFindFriendsViaSearchTermNoResults,
        /** */  EDialogFindFriendsViaSearchTermTechnicalError,
        /** */  EDialogFollowDetailsNoNetwork,
        /** */  EDialogFollowDetailsTechnicalError,
        /** */  EDialogGooglePlacesNoNetwork,
        /** */  EDialogGooglePlacesSurroundingsNoResults,
        /** */  EDialogGooglePlacesTechnicalError,
        /** */  EDialogGooglePlacesTermNoResults,
        /** */  EDialogImagePropertiesNoNetwork,
        /** */  EDialogImagePropertiesTechnicalError,
        /** */  EDialogLikeUnlikeNoNetwork,
        /** */  EDialogLikeUnlikeTechnicalError,
        /** */  EDialogLoginFailed,
        /** */  EDialogLoginNoNetwork,
        /** */  EDialogLoginPasswordRequired,
        /** */  EDialogLoginTechnicalError,
        /** */  EDialogLoginUsernameRequired,
        /** */  EDialogLostPasswordNoNetwork,
        /** */  EDialogLostPasswordOk,
        /** */  EDialogLostPasswordTechnicalError,
        /** */  EDialogLostPasswordUsernameNotFound,
        /** */  EDialogLostPasswordUsernameRequired,
        /** */  EDialogNoUpdate,
        /** */  EDialogRateFoodNoNetwork,
        /** */  EDialogRateFoodTechnicalError,
        /** */  EDialogRegisterEMailExists,
        /** */  EDialogRegisterEMailInvalid,
        /** */  EDialogRegisterEMailMalformed,
        /** */  EDialogRegisterFailed,
        /** */  EDialogRegisterNoNetwork,
        /** */  EDialogRegisterPasswordInvalid,
        /** */  EDialogRegisterPasswordsDiffer,
        /** */  EDialogRegisterTechnicalError,
        /** */  EDialogRegisterUsernameExists,
        /** */  EDialogRegisterUsernameInvalid,
        /** */  EDialogRegisterUsernameMalformed,
        /** */  EDialogRemoveImageNoNetwork,
        /** */  EDialogRemoveImageTechnicalError,
        /** */  EDialogReportAbuseInvalid,
        /** */  EDialogReportAbuseNoNetwork,
        /** */  EDialogReportAbuseSent,
        /** */  EDialogReportAbuseTechnicalError,
        /** */  EDialogSearchImages,
        /** */  EDialogSearchImagesGooglePlacesNoNetwork,
        /** */  EDialogSearchImagesGooglePlacesNoResults,
        /** */  EDialogSearchImagesGooglePlacesTechnicalError,
        /** */  //EDialogSearchImagesLocationRequired,
        /** */  EDialogSearchImagesPivotalNoNetwork,
        /** */  EDialogSearchImagesPivotalNoResults,
        /** */  EDialogSearchImagesPivotalTechnicalError,
        /** */  EDialogSessionExpired,
        /** */  EDialogSetBlockNoNetwork,
        /** */  EDialogSetBlockTechnicalError,
        /** */  EDialogShareImageNoNetwork,
        /** */  EDialogShareImageTechnicalError,
        /** */  EDialogShowFacebookPleaseWait,
        /** */  EDialogShowFacebookPleaseWaitAfterProgressDialog,
        /** */  EDialogToggleFollowshipNoNetwork,
        /** */  EDialogToggleFollowshipTechnicalError,
        /** */  EDialogUpdateCheckNoNetwork,
        /** */  EDialogUpdateCheckTechnicalError,
        /** */  EDialogUpdateOptional,
        /** */  EDialogUpdateRequired,
        /** */  EDialogUploadNewEntryFailed,
        /** */  EDialogUploadNewEntryNoNetwork,
        /** */  EDialogUploadNewEntrySucceeded,
        /** */  EDialogUploadNewEntryTechnicalError,

        ;

        /*****************************************************************************
        *   Every action is being performed in a separate thread.
        *****************************************************************************/
        @Override
        public final void run()
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        execute();
                    }
                    catch ( Throwable t )
                    {
                        PicFoodDebug.DEBUG_THROWABLE( t, "This throwable was caught in the Action system", UncaughtException.ENo );
                    }
                }
            }.start();
        }

        /*****************************************************************************
        *   Performs this action.
        *****************************************************************************/
        protected final void execute()
        {
            PicFoodDebug.major.out( "EXECUTE Action: [" + this + "]" );
          //PicFoodDebug.major.mem();

            switch ( this )
            {
                case EDialogChangePasswordEmptyOldPassword:
                {
                    //show dialog 'old password empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_password_empty_old_password_title,
                        R.string.dialog_change_password_empty_old_password_body,
                        R.string.dialog_change_password_empty_old_password_button_ok,
                        PicFoodActionPush.EPushChangePassword,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushChangePassword
                    );
                    break;
                }

                case EDialogChangePasswordInvalidNewPassword:
                {
                    //show dialog 'new password empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_password_invalid_new_password_title,
                        R.string.dialog_change_password_invalid_new_password_body,
                        R.string.dialog_change_password_invalid_new_password_button_ok,
                        PicFoodActionPush.EPushChangePassword,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushChangePassword
                    );
                    break;
                }

                case EDialogChangeProfileBiographyInvalid:
                {
                    //show dialog 'bio invalid'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_profile_invalid_biography_title,
                        R.string.dialog_change_profile_invalid_biography_body,
                        R.string.dialog_change_profile_invalid_biography_button_ok,
                        PicFoodActionDialog.EDialogChangeProfile,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogChangeProfile
                    );
                    break;
                }

                case EDialogChangeProfileRealNameInvalid:
                {
                    //show dialog 'real name invalid'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_profile_invalid_real_name_title,
                        R.string.dialog_change_profile_invalid_real_name_body,
                        R.string.dialog_change_profile_invalid_real_name_button_ok,
                        PicFoodActionDialog.EDialogChangeProfile,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogChangeProfile
                    );
                    break;
                }

                case EDialogChangeProfileWebsiteInvalid:
                {
                    //show dialog 'website invalid'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_profile_invalid_website_title,
                        R.string.dialog_change_profile_invalid_website_body,
                        R.string.dialog_change_profile_invalid_website_button_ok,
                        PicFoodActionDialog.EDialogChangeProfile,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogChangeProfile
                    );
                    break;
                }

                case EDialogChangeProfilePhoneNumberInvalid:
                {
                    //show dialog 'phone number invalid'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_profile_invalid_phone_number_title,
                        R.string.dialog_change_profile_invalid_phone_number_body,
                        R.string.dialog_change_profile_invalid_phone_number_button_ok,
                        PicFoodActionDialog.EDialogChangeProfile,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogChangeProfile
                    );
                    break;
                }

                case EDialogChangeProfileNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionDialog.EDialogChangeProfile,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogChangeProfile
                    );
                    break;
                }

                case EDialogChangeProfileTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionDialog.EDialogChangeProfile,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogChangeProfile
                    );
                    break;
                }

                case EDialogChangeProfileSucceeded:
                {
                    //show dialog 'change profile succeeded'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_profile_succeeded_title,
                        R.string.dialog_change_profile_succeeded_body,
                        R.string.dialog_change_profile_succeeded_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogChangePasswordEmptyNewPasswordRepeat:
                {
                    //show dialog 'new password repeat empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_password_empty_new_password_repeat_title,
                        R.string.dialog_change_password_empty_new_password_repeat_body,
                        R.string.dialog_change_password_empty_new_password_repeat_button_ok,
                        PicFoodActionPush.EPushChangePassword,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushChangePassword
                    );
                    break;
                }

                case EDialogChangePasswordMismatchingNewPasswordRepeat:
                {
                    //show dialog 'new password repeat empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_password_mismatching_new_password_repeat_title,
                        R.string.dialog_change_password_mismatching_new_password_repeat_body,
                        R.string.dialog_change_password_mismatching_new_password_repeat_button_ok,
                        PicFoodActionPush.EPushChangePassword,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushChangePassword
                    );
                    break;
                }

                case EDialogChangePasswordOk:
                {
                    //prune references to the InputFields
                    PicFoodFlowUser.inputHandlerChangePasswordNewPassword.destroy();
                    PicFoodFlowUser.inputHandlerChangePasswordNewPasswordRepeat.destroy();
                    PicFoodFlowUser.inputHandlerChangePasswordOldPassword.destroy();

                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_password_success_title,
                        R.string.dialog_change_password_success_body,
                        R.string.dialog_change_password_success_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogChangePasswordWrongPassword:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_password_wrong_password_title,
                        R.string.dialog_change_password_wrong_password_body,
                        R.string.dialog_change_password_wrong_password_button_ok,
                        PicFoodActionPush.EPushChangePassword,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushChangePassword
                    );
                    break;
                }

                case EDialogChangePasswordNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionPush.EPushChangePassword,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushChangePassword
                    );
                    break;
                }

                case EDialogChangePasswordTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionPush.EPushChangePassword,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushChangePassword
                    );
                    break;
                }

                case EDialogImagePropertiesTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EImageProperties.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionState.ELeaveImageProperties,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveImageProperties
                    );
                    break;
                }

                case EDialogImagePropertiesNoNetwork:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EImageProperties.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionState.ELeaveImageProperties,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveImageProperties
                    );
                    break;
                }

                case EDialogLoginNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELogin.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLogin,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLogin
                    );
                    break;
                }

                case EDialogLostPasswordNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELostPassword.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword
                    );
                    break;
                }

                case EDialogLoginFailed:
                {
                    //show dialog 'login failed'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELogin.getActivity(),
                        R.string.dialog_login_failed_title,
                        R.string.dialog_login_failed_body,
                        R.string.dialog_login_failed_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLogin,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLogin
                    );
                    break;
                }

                case EDialogLoginTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELogin.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLogin,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLogin
                    );
                    break;
                }

                case EDialogLoginUsernameRequired:
                {
                    //show dialog 'username required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELogin.getActivity(),
                        R.string.dialog_username_empty_title,
                        R.string.dialog_username_empty_body,
                        R.string.dialog_username_empty_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLogin,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLogin
                    );
                    break;
                }

                case EDialogLostPasswordUsernameRequired:
                {
                    //show dialog 'username required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELostPassword.getActivity(),
                        R.string.dialog_username_empty_title,
                        R.string.dialog_username_empty_body,
                        R.string.dialog_username_empty_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword
                    );
                    break;
                }

                case EDialogLoginPasswordRequired:
                {
                    //show dialog 'password required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELogin.getActivity(),
                        R.string.dialog_password_empty_title,
                        R.string.dialog_password_empty_body,
                        R.string.dialog_password_empty_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLogin,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLogin
                    );
                    break;
                }

                case EDialogLostPasswordOk:
                {
                    //show dialog 'password required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELostPassword.getActivity(),
                        R.string.dialog_lost_password_ok_title,
                        R.string.dialog_lost_password_ok_body,
                        R.string.dialog_lost_password_ok_button_ok,
                        PicFoodActionState.ELeaveLostPassword,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveLostPassword
                    );
                    break;
                }

                case EDialogLostPasswordUsernameNotFound:
                {
                    //show dialog 'username not found'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELostPassword.getActivity(),
                        R.string.dialog_lost_password_username_not_found_title,
                        R.string.dialog_lost_password_username_not_found_body,
                        R.string.dialog_lost_password_username_not_found_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword
                    );
                    break;
                }

                case EDialogLostPasswordTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ELostPassword.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsLostPassword
                    );
                    break;
                }

                case EDialogRegisterUsernameInvalid:
                {
                    //show dialog 'username required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_username_invalid_title,
                        R.string.dialog_username_invalid_body,
                        R.string.dialog_username_invalid_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterUsernameMalformed:
                {
                    //show dialog 'username malformed'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_username_malformed_title,
                        R.string.dialog_username_malformed_body,
                        R.string.dialog_username_malformed_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterPasswordsDiffer:
                {
                    //show dialog 'passwords differ'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_passwords_differ_title,
                        R.string.dialog_passwords_differ_body,
                        R.string.dialog_passwords_differ_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterUsernameExists:
                {
                    //show dialog 'username exists'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_username_exists_title,
                        R.string.dialog_username_exists_body,
                        R.string.dialog_username_exists_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterPasswordInvalid:
                {
                    //show dialog 'password required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_password_invalid_title,
                        R.string.dialog_password_invalid_body,
                        R.string.dialog_password_invalid_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterEMailExists:
                {
                    //show dialog 'email exists'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_email_exists_title,
                        R.string.dialog_email_exists_body,
                        R.string.dialog_email_exists_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterEMailMalformed:
                {
                    //show dialog 'email malformed'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_email_malformed_title,
                        R.string.dialog_email_malformed_body,
                        R.string.dialog_email_malformed_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterEMailInvalid:
                {
                    //show dialog 'email required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_email_invalid_title,
                        R.string.dialog_email_invalid_body,
                        R.string.dialog_email_invalid_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogRegisterFailed:
                {
                    //show dialog 'register failed'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_register_failed_title,
                        R.string.dialog_register_failed_body,
                        R.string.dialog_register_failed_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsRegister,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsRegister
                    );
                    break;
                }

                case EDialogFeedbackTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogFeedbackSent:
                {
                    //prune reference to the InputField
                    PicFoodFlowUser.inputHandlerFeedback.destroy();

                    //show dialog 'feedback sent'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_feedback_sent_title,
                        R.string.dialog_feedback_sent_body,
                        R.string.dialog_feedback_sent_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogFeedbackNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogFeedbackInvalid:
                {
                    //show dialog 'feedback empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_feedback_invalid_title,
                        R.string.dialog_feedback_invalid_body,
                        R.string.dialog_feedback_invalid_button_ok,
                        PicFoodActionPush.EPushSendFeedback,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushSendFeedback
                    );
                    break;
                }

                case EDialogDeleteAccountWrongPassword:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_delete_account_wrong_password_title,
                        R.string.dialog_delete_account_wrong_password_body,
                        R.string.dialog_delete_account_wrong_password_button_ok,
                        PicFoodActionPush.EPushDeleteAccountShowPasswordConfirmation,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushDeleteAccountShowPasswordConfirmation
                    );
                    break;
                }

                case EDialogDeleteImageCacheSuccess:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_delete_image_cache_success_title,
                        R.string.dialog_delete_image_cache_success_body,
                        R.string.dialog_delete_image_cache_success_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogDeleteAccountNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogDeleteAccountEmptyPassword:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_delete_account_empty_password_title,
                        R.string.dialog_delete_account_empty_password_body,
                        R.string.dialog_delete_account_empty_password_button_ok,
                        PicFoodActionPush.EPushDeleteAccountShowPasswordConfirmation,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushDeleteAccountShowPasswordConfirmation
                    );
                    break;
                }

                case EDialogDeleteAccountTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogUploadNewEntrySucceeded:
                {
                    //prune comment-inputfield-handler
                    PicFoodFlowNewEntry.handlerComment.pruneUIThreaded( PicFoodState.ENewEntry.getActivity() );

                    //reset timeouts for wall and user profile
                    PicFoodStatePivotalTabWall.resetLastUpdate();
                    PicFoodStatePivotalTabProfile.resetLastUpdate();

                    //show 'image uploaded'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ENewEntry.getActivity(),
                        R.string.dialog_upload_new_entry_succeeded_title,
                        R.string.dialog_upload_new_entry_succeeded_body,
                        R.string.dialog_upload_new_entry_succeeded_button_ok,
                        PicFoodActionState.ELeaveNewEntry,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveNewEntry
                    );
                    break;
                }

                case EDialogUploadNewEntryTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ENewEntry.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry
                    );
                    break;
                }

                case EDialogUploadNewEntryFailed:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ENewEntry.getActivity(),
                        R.string.dialog_upload_new_entry_failed_title,
                        R.string.dialog_upload_new_entry_failed_body,
                        R.string.dialog_upload_new_entry_failed_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry
                    );
                    break;
                }

                case EDialogUploadNewEntryNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ENewEntry.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry
                    );
                    break;
                }

                case EDialogGooglePlacesNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionState.ELeaveGooglePlaces,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case EDialogGooglePlacesTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionState.ELeaveGooglePlaces,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case EDialogGooglePlacesSurroundingsNoResults:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
                        R.string.dialog_google_places_no_surrounding_results_title,
                        R.string.dialog_google_places_no_surrounding_results_body,
                        R.string.dialog_google_places_no_surrounding_results_button_back,
                        PicFoodActionState.ELeaveGooglePlaces,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case EDialogGooglePlacesTermNoResults:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
                        R.string.dialog_google_places_no_term_results_title,
                        R.string.dialog_google_places_no_term_results_body,
                        R.string.dialog_google_places_no_term_results_button_back,
                        PicFoodActionState.ELeaveGooglePlaces,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case EDialogLikeUnlikeTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogLikeUnlikeNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogReportAbuseInvalid:
                {
                    //show dialog 'feedback empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_report_abuse_invalid_title,
                        R.string.dialog_report_abuse_invalid_body,
                        R.string.dialog_report_abuse_invalid_button_ok,
                        PicFoodActionPush.EPushImageReportAbuse,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushImageReportAbuse
                    );
                    break;
                }

                case EDialogRemoveImageNoNetwork:
                case EDialogReportAbuseNoNetwork:
                case EDialogRateFoodNoNetwork:
                case EDialogCommentNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogReportAbuseSent:
                {
                    //prune reference to the InputField
                    PicFoodFlowImage.inputHandlerReportAbuse.destroy();

                    //show dialog 'report abuse sent'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_report_abuse_sent_title,
                        R.string.dialog_report_abuse_sent_body,
                        R.string.dialog_report_abuse_sent_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogRemoveImageTechnicalError:
                case EDialogReportAbuseTechnicalError:
                case EDialogRateFoodTechnicalError:
                case EDialogCommentTechnicalError:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogCommentInvalid:
                {
                    //show dialog 'comment empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_comment_invalid_title,
                        R.string.dialog_comment_invalid_body,
                        R.string.dialog_comment_invalid_button_ok,
                        PicFoodActionPush.EPushImageComment,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushImageComment
                    );
                    break;
                }

                case EDialogCommentSent:
                {
                    //prune reference to the InputField
                    PicFoodFlowImageComment.inputHandlerComment.destroy();

                    //show dialog 'comment sent'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_comment_sent_title,
                        R.string.dialog_comment_sent_body,
                        R.string.dialog_comment_sent_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogDetailedImageNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EDetailedImage.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionState.ELeaveDetailedImage,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveDetailedImage
                    );
                    break;
                }

                case EDialogDetailedImageTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EDetailedImage.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionState.ELeaveDetailedImage,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveDetailedImage
                    );
                    break;
                }

                case EDialogToggleFollowshipNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EForeignProfile.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile
                    );
                    break;
                }

                case EDialogToggleFollowshipTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EForeignProfile.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile
                    );
                    break;
                }

                case EDialogSetBlockNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EForeignProfile.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile
                    );
                    break;
                }

                case EDialogSetBlockTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EForeignProfile.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsForeignProfile
                    );
                    break;
                }

                case EDialogFindFriendsViaSearchTermInvalidTerm:
                {
                    //show dialog 'old password empty'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_search_friends_via_searchterm_invalid_title,
                        R.string.dialog_search_friends_via_searchterm_invalid_body,
                        R.string.dialog_search_friends_via_searchterm_invalid_button_ok,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm
                    );
                    break;
                }

                case EDialogFindFriendsViaSearchTermNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm
                    );
                    break;
                }

                case EDialogFindFriendsViaSearchTermTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm
                    );
                    break;
                }

                case EDialogFindFriendsViaFacebookNoNetwork:
                case EDialogFindFriendsViaPhonenumberNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogFindFriendsViaFacebookTechnicalError:
                case EDialogFindFriendsViaPhonenumberTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogFindFriendsViaSearchTermNoResults:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_find_friends_searchterm_no_results_title,
                        R.string.dialog_find_friends_searchterm_no_results_body,
                        R.string.dialog_find_friends_searchterm_no_results_button_ok,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushFindFriendsViaSearchTerm
                    );
                    break;
                }

                case EDialogFindFriendsViaFacebookNoResults:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_find_friends_facebook_no_results_title,
                        R.string.dialog_find_friends_facebook_no_results_body,
                        R.string.dialog_find_friends_facebook_no_results_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogFindFriendsViaPhonenumberNoResults:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_find_friends_phonenumber_no_results_title,
                        R.string.dialog_find_friends_phonenumber_no_results_body,
                        R.string.dialog_find_friends_phonenumber_no_results_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogSearchImages:
                {
                    //create checkbox
                    View checkbox = PicFoodFlowSearchImages.createDialogCheckbox();

                    //show dialog 'search images'
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodState.EPivotalMenu.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowSearchImages.inputHandlerSearchImagesTerm,             PicFoodFlowSearchImages.inputHandlerSearchImagesLocation            },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_singleline,                            R.layout.dialog_view_input_singleline,                              },
                        new int[]                   { R.id.dialog_input_field,                                          R.id.dialog_input_field,                                            },
                        new int[]                   { R.string.dialog_search_images_inputfield_term_hint,               R.string.dialog_search_images_inputfield_location_hint,             },
                        new int[]                   { R.integer.inputfield_max_length_search_images_term,               R.integer.inputfield_max_length_search_images_location,             },
                        R.string.dialog_search_images_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_search_images_body,                               },
                        R.string.dialog_search_images_button_submit,
                        PicFoodActionPush.EPushSearchImagesSubmit,
                        R.string.dialog_search_images_button_cancel,
                        PicFoodActionUnselect.EUnselectButtonsPivotalHeader,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsPivotalHeader,
                        checkbox
                    );
                    break;
                }

                case EDialogSearchImagesPivotalNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionState.ELeaveSearchImagesResults,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveSearchImagesResults
                    );
                    break;
                }

                case EDialogSearchImagesPivotalTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionState.ELeaveSearchImagesResults,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveSearchImagesResults
                    );
                    break;
                }

                case EDialogSearchImagesPivotalNoResults:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        R.string.dialog_search_images_no_results_title,
                        R.string.dialog_search_images_no_results_body,
                        R.string.dialog_search_images_no_results_button_ok,
                        PicFoodActionState.ELeaveSearchImagesResults,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveSearchImagesResults
                    );
                    break;
                }

                case EDialogSearchImagesGooglePlacesNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionState.ELeaveSearchImagesResults,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveSearchImagesResults
                    );
                    break;
                }

                case EDialogSearchImagesGooglePlacesTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionState.ELeaveSearchImagesResults,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveSearchImagesResults
                    );
                    break;
                }
/*
                case EDialogSearchImagesLocationRequired:
                {
                    //show dialog 'location required'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EPivotalMenu.getActivity(),
                        R.string.dialog_search_images_location_required_title,
                        R.string.dialog_search_images_location_required__body,
                        R.string.dialog_search_images_location_required_button_ok,
                        PicFoodActionDialog.EDialogSearchImages,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogSearchImages,
                    );
                    break;
                }
*/
                case EDialogSearchImagesGooglePlacesNoResults:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        R.string.dialog_search_images_no_results_title,
                        R.string.dialog_search_images_no_results_body,
                        R.string.dialog_search_images_no_results_button_ok,
                        PicFoodActionState.ELeaveSearchImagesResults,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveSearchImagesResults
                    );
                    break;
                }

                case EDialogShareImageNoNetwork:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_share_image_no_network_title,
                        R.string.dialog_share_image_no_network_body,
                        R.string.dialog_share_image_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogShareImageTechnicalError:
                {
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_share_image_technical_error_title,
                        R.string.dialog_share_image_technical_error_body,
                        R.string.dialog_share_image_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EDialogShowFacebookPleaseWait:
                {
                    break;
                }

                case EDialogShowFacebookPleaseWaitAfterProgressDialog:
                {
                    break;
                }

                case EDialogFollowDetailsNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EFollowDetails.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionState.ELeaveFollowDetails,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveFollowDetails
                    );
                    break;
                }

                case EDialogFollowDetailsTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EImageProperties.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionState.ELeaveFollowDetails,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveFollowDetails
                    );
                    break;
                }

                case EDialogSessionExpired:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodIDM.lastStateBeforeSessionExpired.getActivity(),
                        R.string.dialog_session_expired_title,
                        R.string.dialog_session_expired_body,
                        R.string.dialog_session_expired_button_ok,
                        PicFoodActionPush.EPushSessionExpiredOk,
                        0,
                        null,
                        true,
                        PicFoodActionPush.EPushSessionExpiredOk
                    );
                    break;
                }

                case EDialogNoUpdate:
                {
                    //show dialog 'no update available'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowGeneral.lastStateWhoCheckedUpdate.getActivity(),
                        R.string.dialog_no_update_title,
                        R.string.dialog_no_update_body,
                        R.string.dialog_no_update_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogUpdateOptional:
                {
                    //show dialog 'update optional'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowGeneral.lastStateWhoCheckedUpdate.getActivity(),
                        R.string.dialog_update_optional_title,
                        R.string.dialog_update_optional_body,
                        R.string.dialog_update_optional_button_ok,
                        PicFoodAction.EDownloadUpdate,
                        R.string.dialog_update_optional_button_cancel,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogUpdateRequired:
                {
                    //show dialog 'update optional'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowGeneral.lastStateWhoCheckedUpdate.getActivity(),
                        R.string.dialog_update_required_title,
                        R.string.dialog_update_required_body,
                        R.string.dialog_update_required_button_ok,
                        PicFoodAction.EDownloadUpdate,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogUpdateCheckNoNetwork:
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowGeneral.lastStateWhoCheckedUpdate.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogUpdateCheckTechnicalError:
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowGeneral.lastStateWhoCheckedUpdate.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogChangeProfile:
                {
                    //show change-profile-dialog
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowProfileChange.inputHandlerRealName,    PicFoodFlowProfileChange.inputHandlerPhone,         PicFoodFlowProfileChange.inputHandlerWebsite,       PicFoodFlowProfileChange.inputHandlerBiography,     },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_singleline,            R.layout.dialog_view_input_singleline,              R.layout.dialog_view_input_singleline,              R.layout.dialog_view_input_multiline,               },
                        new int[]                   { R.id.dialog_input_field,                          R.id.dialog_input_field,                            R.id.dialog_input_field,                            R.id.dialog_input_field,                            },
                        new int[]                   { R.string.state_register_inputfield_realname,      R.string.state_register_inputfield_phone,           R.string.state_register_inputfield_website,         R.string.state_register_inputfield_biography,       },
                        new int[]                   { R.integer.inputfield_max_length_real_name,        R.integer.inputfield_max_length_phone_number,       R.integer.inputfield_max_length_website,            R.integer.inputfield_max_length_biography,          },
                        R.string.dialog_change_profile_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_change_profile_label_realname,    R.string.dialog_change_profile_label_phonenumber,   R.string.dialog_change_profile_label_website,       R.string.dialog_change_profile_label_biography,     },
                        R.string.dialog_change_profile_button_submit,
                        PicFoodActionPush.EPushChangeProfileSubmit,
                        R.string.dialog_change_profile_button_cancel,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        null
                    );
                    break;
                }

                case EDialogChangeProfilePickProfileNoNetwork:
                {
                    //show dialog 'picking profile failed'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogChangeProfilePickProfileTechnicalError:
                {
                    //show dialog 'picking profile failed'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogChangeProfileImageSucceeded:
                {
                    //show dialog 'change profile image succeeded'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_change_profile_image_succeeded_title,
                        R.string.dialog_change_profile_image_succeeded_body,
                        R.string.dialog_change_profile_image_succeeded_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogChangeProfileImageNoNetwork:
                {
                    //show dialog 'no network'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EDialogChangeProfileImageTechnicalError:
                {
                    //show dialog 'technical error'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        0,
                        null,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }
            }
        }
    }
