
    package de.mayflower.antipatterns.action;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Debug;
    import  de.mayflower.antipatterns.AntiPatternsProject.Debug.Simulations;
    import  de.mayflower.antipatterns.AntiPatternsProject.Features;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import de.mayflower.antipatterns.AntiPatternsSettings.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.flow.*;
    import de.mayflower.antipatterns.flow.AntiPatternsFlowProfile.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.AntiPatternsSave.SaveKey;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.acclaim.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public enum AntiPatternsActionPush implements Runnable
    {
        /** */  EPushAcclaimNextPage,
        /** */  EPushCancelGPSNewEntry,
        /** */  EPushCancelGPSSearchImages,
        /** */  EPushChangePassword,
        /** */  EPushChangePasswordSubmit,
        /** */  EPushChangePasswordSubmitAfterProgressDialog,
        /** */  EPushChangeProfile,
        /** */  EPushChangeProfileAfterProgressDialog,
        /** */  EPushChangeProfileImage,
        /** */  EPushChangeProfileSubmit,
        /** */  EPushChangeProfileSubmitAfterProgressDialog,
        /** */  EPushDeleteAccount,
        /** */  EPushDeleteAccountPasswordConfirmationYes,
        /** */  EPushDeleteAccountPasswordConfirmationYesAfterProgressDialog,
        /** */  EPushDeleteAccountShowPasswordConfirmation,
        /** */  EPushDeleteImageCache,
        /** */  EPushImageCacheAfterProgressDialog,
        /** */  EPushEclipseApp,
        /** */  EPushFindFriends,
        /** */  EPushFindFriendsViaFacebook,
        /** */  EPushFindFriendsViaPhonenumber,
        /** */  EPushFindFriendsViaPhonenumberAfterProgressDialog,
        /** */  EPushFindFriendsViaSearchTerm,
        /** */  EPushFindFriendsViaSearchTermCleanSubmit,
        /** */  EPushFindFriendsViaSearchTermCleanSubmitAfterProgressDialog,
        /** */  EPushImageComment,
        /** */  EPushImageCommentSubmit,
        /** */  EPushImageCommentSubmitAfterProgressDialog,
        /** */  EPushImageLikeUnlike,
        /** */  EPushImageLikeUnlikeAfterProgressDialog,
        /** */  EPushImageOptions,
        /** */  EPushImageRate,
        /** */  EPushImageRateAfterProgressDialog,
        /** */  EPushImageRateFood,
        /** */  EPushImageRateHighest,
        /** */  EPushImageRateLowest,
        /** */  EPushImageRateMedium,
        /** */  EPushImageRemove,
        /** */  EPushImageRemoveYes,
        /** */  EPushImageRemoveYesAfterProgressDialog,
        /** */  EPushImageReportAbuse,
        /** */  EPushImageReportAbuseYes,
        /** */  EPushImageReportAbuseYesAfterProgressDialog,
        /** */  EPushImageShare,
        /** */  EPushImageShareAfterProgressDialog,
        /** */  EPushImageShowComments,
        /** */  EPushImageShowFoodRatings,
        /** */  EPushImageShowLikes,
        /** */  EPushListEntryFindFriendsResults,
        /** */  EPushListEntryFollowDetails,
        /** */  EPushListEntryGooglePlaceImageSearch,
        /** */  EPushListEntryGooglePlaceImageSearchAfterProgressDialog,
        /** */  EPushListEntryGooglePlaceNewEntry,
        /** */  EPushListEntryImagePropertiesResults,
        /** */  EPushLogin,
        /** */  EPushLoginAfterProgressDialog,
        /** */  EPushLogout,
        /** */  EPushLogoutConfirmationYes,
        /** */  EPushLogoutConfirmationYesAfterProgressDialog,
        /** */  EPushLostPassword,
        /** */  EPushLostPasswordAfterProgressDialog,
        /** */  EPushRegister,
        /** */  EPushRegisterAfterProgressDialog,
        /** */  EPushRegisterChooseProfileImage,
        /** */  EPushRegisterImportFacebookData,
        /** */  EPushRegisterTermsAndConditions,
        /** */  EPushRegisterTermsAndConditionsAfterProgressDialog,
        /** */  EPushSearchImagesSubmit,
        /** */  EPushSearchImagesSubmitNoLocationAfterProgressDialog,
        /** */  EPushSendFeedback,
        /** */  EPushSendFeedbackSubmit,
        /** */  EPushSendFeedbackYesAfterProgressDialog,
        /** */  EPushSessionExpiredOk,
        /** */  EPushSettingsCheckUpdate,
        /** */  EPushSettingsCheckUpdateAfterProgressDialog,
        /** */  EPushSettingsPrivacyPolicy,
        /** */  EPushSettingsPrivacyPolicyAfterProgressDialog,
        /** */  EPushSettingsTermsAndConditions,
        /** */  EPushSettingsTermsAndConditionsAfterProgressDialog,
        /** */  EPushShowForeignUserFollowers,
        /** */  EPushShowForeignUserFollowings,
        /** */  EPushShowOwnUserFollowers,
        /** */  EPushShowOwnUserFollowings,
        /** */  EPushSkipAcclaim,
        /** */  EPushSubmitNewEntry,
        /** */  EPushSubmitNewEntryAfterProgressDialog,
        /** */  EPushUploadImageFromCamera,
        /** */  EPushUploadImageFromGallery,
        /** */  EPushUserBlock,
        /** */  EPushUserBlockAfterProgressDialog,
        /** */  EPushUserToggleFollowship,
        /** */  EPushUserToggleFollowshipAfterProgressDialog,

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
                        AntiPatternsDebug.DEBUG_THROWABLE(t, "This throwable was caught in the Action system", UncaughtException.ENo);
                    }
                }
            }.start();
        }

        /*****************************************************************************
        *   Performs this action.
        *****************************************************************************/
        protected final void execute()
        {
            AntiPatternsDebug.major.out( "EXECUTE Action: [" + this + "]" );
          //PicFoodDebug.major.mem();

            switch ( this )
            {
                case EPushAcclaimNextPage:
                {
                    //unselect all buttons
                    AntiPatternsStateAcclaim.unselectAllButtonsUIThreaded();

                    //switch ViewPager to the next page
                    AntiPatternsStateAcclaim.switchViewPagerToNextPageUIThreaded();
                    break;
                }

                case EPushSkipAcclaim:
                {
                    //unselect all buttons
                    AntiPatternsStateAcclaim.unselectAllButtonsUIThreaded();

                    //switch ViewPager to the last page
                    AntiPatternsStateAcclaim.switchViewPagerToLastPageUIThreaded();
                    break;
                }

                case EPushUploadImageFromCamera:
                {
                    //ditch old data
                    AntiPatternsFlowNewEntry.ditchOldData(AntiPatternsState.EPivotalMenu.getActivity());

                    //order state 'new entry' to launch the camera
                    AntiPatternsStateNewEntry.launchCameraNextOnStart = true;

                    //show state 'new entry'
                    LibLauncher.launchActivity( AntiPatternsState.EPivotalMenu.getActivity(), AntiPatternsStateNewEntry.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushUploadImageFromGallery:
                {
                    //ditch old data
                    AntiPatternsFlowNewEntry.ditchOldData(AntiPatternsState.EPivotalMenu.getActivity());

                    //order state 'new entry' to launch the image picker
                    AntiPatternsStateNewEntry.launchGalleryNextOnStart = true;

                    //show state 'new entry'
                    LibLauncher.launchActivity( AntiPatternsState.EPivotalMenu.getActivity(), AntiPatternsStateNewEntry.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushEclipseApp:
                {
                    Lib.showHomescreenActivity( AntiPatternsState.EPivotalMenu.getActivity() );
                    break;
                }

                case EPushListEntryGooglePlaceNewEntry:
                {
                    //get picked entry
                    AntiPatternsGridViewContent pickedGooglePlaceEntry  = (AntiPatternsGridViewContent) AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces).getLastSelectedItem();
                    AntiPatternsDataGooglePlace pickedGooglePlace       = (AntiPatternsDataGooglePlace)pickedGooglePlaceEntry.getData();

                    //save persistent
                    AntiPatternsSave.saveSetting(AntiPatternsState.EGooglePlaces.getActivity(), SaveKey.EStateNewEntryLastPickedGooglePlace, LibIO.serializableToString(pickedGooglePlace));

                    //leave state 'google places'
                    AntiPatternsActionState.ELeaveGooglePlaces.run();

                    break;
                }

                case EPushListEntryGooglePlaceImageSearch:
                {
                    //show 'please wait'-message
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EGooglePlaces.getActivity(),
                        R.string.dialog_search_images_searching_title,
                        R.string.dialog_search_images_searching_body,
                        EPushListEntryGooglePlaceImageSearchAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushListEntryGooglePlaceImageSearchAfterProgressDialog:
                {
                    //get picked entry
                    AntiPatternsGridViewContent pickedGooglePlaceEntry  = (AntiPatternsGridViewContent) AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces).getLastSelectedItem();
                    AntiPatternsDataGooglePlace pickedGooglePlace       = (AntiPatternsDataGooglePlace)pickedGooglePlaceEntry.getData();

                    //perform search with this location
                    AntiPatternsDebug.imageSearch.out( "Picked Google Place - Searching images for location [" + pickedGooglePlace.iName + "]" );

                    //assign last activity
                    AntiPatternsFlowSearchImages.lastStateBeforeSearch       = AntiPatternsState.EGooglePlaces;
                    AntiPatternsFlowSearchImages.lastLatitude                = String.valueOf( pickedGooglePlace.iLatitude  );
                    AntiPatternsFlowSearchImages.lastLongitude               = String.valueOf( pickedGooglePlace.iLongitude );
                    AntiPatternsFlowSearchImages.lastSearchTerm              = AntiPatternsFlowSearchImages.inputHandlerSearchImagesTerm.getText();

                    //assign last actions
                    AntiPatternsFlowSearchImages.lastActionOnNoNetwork       = AntiPatternsActionDialog.EDialogSearchImagesGooglePlacesNoNetwork;
                    AntiPatternsFlowSearchImages.lastActionOnTechnicalError  = AntiPatternsActionDialog.EDialogSearchImagesGooglePlacesTechnicalError;
                    AntiPatternsFlowSearchImages.lastActionOnNoResults       = AntiPatternsActionDialog.EDialogSearchImagesGooglePlacesNoResults;

                    //enter state 'search images'
                    AntiPatternsActionState.EEnterSearchImagesResults.run();
                    break;
                }

                case EPushRegisterTermsAndConditions:
                {
                    //show 'please wait'-dialog and order terms and conditions
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ERegister.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushRegisterTermsAndConditionsAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushRegisterTermsAndConditionsAfterProgressDialog:
                {
                    //show latest terms and conditions
                    AntiPatternsFlowGeneral.showTermsAndConditions(AntiPatternsState.ERegister, AntiPatternsAction.ENone);
                    break;
                }

                case EPushSettingsPrivacyPolicy:
                {
                    //show 'please wait'-dialog and order privacy policy
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushSettingsPrivacyPolicyAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSettingsPrivacyPolicyAfterProgressDialog:
                {
                    //show latest privacy policy
                    AntiPatternsFlowGeneral.showPrivacyPolicy(AntiPatternsState.ESettings, AntiPatternsActionUnselect.EUnselectButtonsSettings);
                    break;
                }

                case EPushSendFeedback:
                {
                    //show feedback-dialog
                    LibDialogInput.showUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { AntiPatternsFlowUser.inputHandlerFeedback,         },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_multiline,         },
                        new int[]                   { R.id.dialog_input_field,                      },
                        new int[]                   { R.string.dialog_feedback_inputfield_hint,     },
                        new int[]                   { R.integer.inputfield_max_length_feedback,     },
                        R.string.dialog_feedback_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_feedback_body,                },
                        R.string.dialog_feedback_button_submit,
                        EPushSendFeedbackSubmit,
                        R.string.dialog_feedback_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        null
                    );
                    break;
                }

                case EPushChangeProfile:
                {
                    //show dialog 'picking your current profile'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_picking_profile_title,
                        R.string.dialog_please_wait_picking_profile_body,
                        AntiPatternsActionPush.EPushChangeProfileAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushChangeProfileAfterProgressDialog:
                {
                    AntiPatternsFlowProfileData.updateOwnProfileDataOnly(AntiPatternsState.ESettings);
                    break;
                }

                case EPushChangeProfileImage:
                {
                    //show image picker
                    LibLauncher.launchImagePicker( AntiPatternsState.ESettings.getActivity(), ActivityRequestID.STATE_SETTINGS_PICK_IMAGE_FROM_GALLERY );
                    break;
                }

                case EPushChangeProfileSubmit:
                {
                    //show 'please wait' dialog and commit the feedback
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushChangeProfileSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushChangeProfileSubmitAfterProgressDialog:
                {
                    //submit the profile change
                    AntiPatternsFlowProfileChange.changeProfile();
                    break;
                }

                case EPushSendFeedbackSubmit:
                {
                    //show 'please wait' dialog and commit the feedback
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushSendFeedbackYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSendFeedbackYesAfterProgressDialog:
                {
                    //send feedback
                    AntiPatternsFlowUser.sendFeedback(AntiPatternsState.ESettings);
                    break;
                }

                case EPushSettingsTermsAndConditions:
                {
                    //show 'please wait'-dialog and order terms and conditions
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushSettingsTermsAndConditionsAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSettingsTermsAndConditionsAfterProgressDialog:
                {
                    //show latest terms and conditions
                    AntiPatternsFlowGeneral.showTermsAndConditions(AntiPatternsState.ESettings, AntiPatternsActionUnselect.EUnselectButtonsSettings);
                    break;
                }

                case EPushRegisterImportFacebookData:
                {
                    break;
                }

                case EPushLogin:
                {
                    //show 'please wait'-dialog and order login
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ELogin.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushLoginAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushLoginAfterProgressDialog:
                {
                    //perform login
                    AntiPatternsFlowAuth.login();
                    break;
                }

                case EPushLostPassword:
                {
                    //show 'please wait'-dialog and order passwort-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ELostPassword.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushLostPasswordAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushLostPasswordAfterProgressDialog:
                {
                    //perform request-password
                    AntiPatternsFlowAuth.lostPassword();
                    break;
                }

                case EPushRegister:
                {
                    //show 'please wait'-dialog and order register
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ERegister.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushRegisterAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushRegisterAfterProgressDialog:
                {
                    //perform register
                    AntiPatternsFlowRegister.register();
                    break;
                }

                case EPushLogout:
                {
                    //show dialog 'Logout confirmation'
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_logout_confirmation_title,
                        R.string.dialog_logout_confirmation_body,
                        R.string.dialog_logout_confirmation_button_yes,
                        EPushLogoutConfirmationYes,
                        R.string.dialog_logout_confirmation_button_no,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EPushLogoutConfirmationYes:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushLogoutConfirmationYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushLogoutConfirmationYesAfterProgressDialog:
                {
                    //commit logout
                    AntiPatternsFlowUser.logout(AntiPatternsState.ESettings);
                    break;
                }

                case EPushRegisterChooseProfileImage:
                {
                    //show image picker
                    LibLauncher.launchImagePicker( AntiPatternsState.ERegister.getActivity(), ActivityRequestID.STATE_REGISTER_PICK_IMAGE_FROM_GALLERY );
                    break;
                }

                case EPushDeleteAccount:
                {
                    //show 'delete account confirm'-dialog
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_delete_account_confirm_delete_title,
                        R.string.dialog_delete_account_confirm_delete_body,
                        R.string.dialog_delete_account_confirm_delete_button_ok,
                        EPushDeleteAccountShowPasswordConfirmation,
                        R.string.dialog_delete_account_confirm_delete_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EPushDeleteAccountShowPasswordConfirmation:
                {
                    //show 'delete account'-dialog
                    LibDialogInput.showUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { AntiPatternsFlowUser.inputHandlerDeleteUserPasswordConfirmation,       },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_password,                              },
                        new int[]                   { R.id.dialog_input_field,                                          },
                        new int[]                   { R.string.dialog_delete_account_confirm_password_inputfield_hint,  },
                        new int[]                   { R.integer.inputfield_max_length_password,                         },
                        R.string.dialog_delete_account_confirm_password_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_delete_account_confirm_password_body,             },
                        R.string.dialog_delete_account_confirm_password_button_submit,
                        EPushDeleteAccountPasswordConfirmationYes,
                        R.string.dialog_delete_account_confirm_password_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        null
                    );
                    break;
                }

                case EPushDeleteAccountPasswordConfirmationYes:
                {
                    //show 'please wait' dialog and send 'delete user'-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushDeleteAccountPasswordConfirmationYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushDeleteAccountPasswordConfirmationYesAfterProgressDialog:
                {
                    //commit user-deletion
                    AntiPatternsFlowUser.removeUser(AntiPatternsState.ESettings);
                    break;
                }

                case EPushDeleteImageCache:
                {
                    //show 'please wait' dialog and delete the cache
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_delete_cache_title,
                        R.string.dialog_please_wait_delete_cache_body_1,
                        AntiPatternsActionPush.EPushImageCacheAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageCacheAfterProgressDialog:
                {
                    AntiPatternsCache.deleteCache(AntiPatternsState.ESettings.getActivity());
                    break;
                }

                case EPushSubmitNewEntry:
                {
                    //show 'please wait while your image is uploaded'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ENewEntry.getActivity(),
                        R.string.dialog_upload_new_entry_title,
                        R.string.dialog_upload_new_entry_body,
                        AntiPatternsActionPush.EPushSubmitNewEntryAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSubmitNewEntryAfterProgressDialog:
                {
                    //upload the new entry - last cropped bitmap must exist - but we handle this for sure!
                    if ( LibIO.isExistent( AntiPatternsSD.getFileLastCroppedImageNewEntry() ) )
                    {
                        AntiPatternsFlowNewEntry.uploadEntry(AntiPatternsState.ENewEntry);
                    }
                    else
                    {
                        //dismiss the progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ENewEntry.getActivity() );

                        //leave state 'new entry'
                        AntiPatternsActionState.ELeaveNewEntry.run();
                    }
                    break;
                }

                case EPushImageLikeUnlike:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EPushImageLikeUnlikeAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageLikeUnlikeAfterProgressDialog:
                {
                    AntiPatternsFlowImageLike.likeOrUnlike();
                    break;
                }

                case EPushImageOptions:
                {
                    //check if the user owns this image
                    int[]       captions = null;
                    Runnable[]  actions  = null;
                    if ( AntiPatternsIDM.getUserID(AntiPatternsFlowImage.lastState.getActivity()).equals( AntiPatternsFlowImage.lastImage.iOwner.iUserID ) )
                    {
                        captions = new int[]        { R.string.dialog_image_options_share_image,    R.string.dialog_image_options_report_abuse, R.string.dialog_image_options_remove_image, };
                        actions  = new Runnable[]   { EPushImageShare,                              EPushImageReportAbuse,                      EPushImageRemove,                           };
                    }
                    else
                    {
                        captions = new int[]        { R.string.dialog_image_options_share_image,    R.string.dialog_image_options_report_abuse,                                             };
                        actions  = new Runnable[]   { EPushImageShare,                              EPushImageReportAbuse,                                                                  };
                    }

                    //show items-dialog
                    LibDialogItems.showUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_image_options_title,
                        captions,
                        actions,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EPushImageShare:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EPushImageShareAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageShareAfterProgressDialog:
                {
                    //share last image
                    AntiPatternsFlowImage.shareImage();
                    break;
                }

                case EPushImageReportAbuse:
                {
                    //show 'report abuse'-dialog
                    LibDialogInput.showUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        new LibDialogInputHandler[] { AntiPatternsFlowImage.inputHandlerReportAbuse,                 },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_multiline,                     },
                        new int[]                   { R.id.dialog_input_field,                                  },
                        new int[]                   { R.string.dialog_report_abuse_inputfield_hint,             },
                        new int[]                   { R.integer.inputfield_max_length_report_abuse_text,        },
                        R.string.dialog_report_abuse_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_report_abuse_body,                        },
                        R.string.dialog_report_abuse_button_submit,
                        EPushImageReportAbuseYes,
                        R.string.dialog_report_abuse_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsImage,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsImage,
                        null
                    );
                    break;
                }

                case EPushImageReportAbuseYes:
                {
                    //show 'please wait' dialog and commit the feedback
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushImageReportAbuseYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageReportAbuseYesAfterProgressDialog:
                {
                    //send feedback
                    AntiPatternsFlowImage.reportAbuse(AntiPatternsFlowImage.inputHandlerReportAbuse.getText());
                    break;
                }

                case EPushImageRemove:
                {
                    //show dialog 'remove image'
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_remove_image_title,
                        R.string.dialog_remove_image_body,
                        R.string.dialog_remove_image_button_yes,
                        EPushImageRemoveYes,
                        R.string.dialog_remove_image_button_no,
                        AntiPatternsActionUnselect.EUnselectButtonsImage,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EPushImageRemoveYes:
                {
                    //show 'please wait' dialog and commit the remove-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushImageRemoveYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageRemoveYesAfterProgressDialog:
                {
                    //send remove-request
                    AntiPatternsFlowImage.removeImage();
                    break;
                }

                case EPushImageRateFood:
                {
                    //show dialog 'no network'
                    LibDialogImageButtons.showUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_rate_food_title,
                        R.string.dialog_rate_food_body,
                        R.string.dialog_rate_food_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsImage,
                        new int[]       { R.drawable.net_picfood_rating_big_green,  R.drawable.net_picfood_rating_big_yellow,   R.drawable.net_picfood_rating_big_red,  },
                        new Runnable[]  { EPushImageRateHighest,                    EPushImageRateMedium,                       EPushImageRateLowest,                   },
                        R.dimen.button_smiley,
                        R.layout.dialog_layout_image_buttons,
                        R.id.text_container,
                        R.id.button_container,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EPushImageRateHighest:
                {
                    //assign rating
                    AntiPatternsFlowImage.lastFoodRating = AntiPatternsSettings.Image.FOOD_RATING_HIGHEST;

                    //launch rate action
                    EPushImageRate.run();

                    break;
                }

                case EPushImageRateMedium:
                {
                    //assign rating
                    AntiPatternsFlowImage.lastFoodRating = AntiPatternsSettings.Image.FOOD_RATING_MEDIUM;

                    //launch rate action
                    EPushImageRate.run();

                    break;
                }

                case EPushImageRateLowest:
                {
                    //assign rating
                    AntiPatternsFlowImage.lastFoodRating = AntiPatternsSettings.Image.FOOD_RATING_LOWEST;

                    //launch rate action
                    EPushImageRate.run();

                    break;
                }

                case EPushImageRate:
                {
                    //hide rate dialog
                    LibDialogImageButtons.dismissLastDialogImageButtonsUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );

                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushImageRateAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageRateAfterProgressDialog:
                {
                    //commit the image-food-rate
                    AntiPatternsFlowImageRating.rateFood();
                    break;
                }

                case EPushImageComment:
                {
                    //show comment-dialog
                    LibDialogInput.showUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        new LibDialogInputHandler[] { AntiPatternsFlowImageComment.inputHandlerComment,      },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_multiline,             },
                        new int[]                   { R.id.dialog_input_field,                          },
                        new int[]                   { R.string.dialog_comment_inputfield_hint,          },
                        new int[]                   { R.integer.inputfield_max_length_image_comment,    },
                        R.string.dialog_comment_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_comment_body,                     },
                        R.string.dialog_comment_button_submit,
                        EPushImageCommentSubmit,
                        R.string.dialog_comment_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsImage,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsImage,
                        null
                    );
                    break;
                }

                case EPushImageCommentSubmit:
                {
                    //show 'please wait' dialog and commit the remove-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushImageCommentSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageCommentSubmitAfterProgressDialog:
                {
                    //send comment-submission
                    AntiPatternsFlowImageComment.commentImage(AntiPatternsFlowImageComment.inputHandlerComment.getText().toString());
                    break;
                }

                case EPushImageShowLikes:
                {
                    //show all likes in state 'image details'
                    AntiPatternsStateImageProperties.setupLikesNextOnStart = true;
                    LibLauncher.launchActivity( AntiPatternsFlowImage.lastState.getActivity(), AntiPatternsStateImageProperties.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushImageShowComments:
                {
                    //show all comments in state 'image details'
                    AntiPatternsStateImageProperties.setupCommentsNextOnStart = true;
                    LibLauncher.launchActivity( AntiPatternsFlowImage.lastState.getActivity(), AntiPatternsStateImageProperties.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushImageShowFoodRatings:
                {
                    //show all food-ratings in state 'image details'
                    AntiPatternsStateImageProperties.setupFoodRatingsNextOnStart = true;
                    LibLauncher.launchActivity( AntiPatternsFlowImage.lastState.getActivity(), AntiPatternsStateImageProperties.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushChangePassword:
                {
                    //show 'change password'-dialog
                    LibDialogInput.showUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { AntiPatternsFlowUser.inputHandlerChangePasswordOldPassword,            AntiPatternsFlowUser.inputHandlerChangePasswordNewPassword,          AntiPatternsFlowUser.inputHandlerChangePasswordNewPasswordRepeat,            },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_password,                              R.layout.dialog_view_input_password,                            R.layout.dialog_view_input_password,                                    },
                        new int[]                   { R.id.dialog_input_field,                                          R.id.dialog_input_field,                                        R.id.dialog_input_field,                                                },
                        new int[]                   { R.string.dialog_change_password_inputfield_old_password_hint,     R.string.dialog_change_password_inputfield_new_password_hint,   R.string.dialog_change_password_inputfield_new_password_repeat_hint,    },
                        new int[]                   { R.integer.inputfield_max_length_password,                         R.integer.inputfield_max_length_password,                       R.integer.inputfield_max_length_password,                               },
                        R.string.dialog_change_password_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_change_password_body,                             },
                        R.string.dialog_change_password_button_submit,
                        EPushChangePasswordSubmit,
                        R.string.dialog_change_password_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        null
                    );
                    break;
                }

                case EPushChangePasswordSubmit:
                {
                    //show 'please wait' dialog and send 'change password'-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushChangePasswordSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushChangePasswordSubmitAfterProgressDialog:
                {
                    //commit password-change
                    AntiPatternsFlowUser.changePassword(AntiPatternsState.ESettings);
                    break;
                }

                case EPushUserToggleFollowship:
                {
                    //show 'please wait' dialog and toggle followship
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EForeignProfile.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushUserToggleFollowshipAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushUserToggleFollowshipAfterProgressDialog:
                {
                    //toggle followship
                    AntiPatternsFlowProfile.toggleFollowship();
                    break;
                }

                case EPushUserBlock:
                {
                    //show 'please wait' dialog and request block
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EForeignProfile.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushUserBlockAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushUserBlockAfterProgressDialog:
                {
                    //set block
                    AntiPatternsFlowProfile.setBlock();
                    break;
                }

                case EPushShowOwnUserFollowers:
                {
                    //flag state content and launch activity 'follow details'
                    AntiPatternsFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EOwnProfile;
                    AntiPatternsStateFollowDetails.setupFollowersNextOnStart     = true;
                    AntiPatternsFlowProfileFollow.lastUser                       = AntiPatternsFlowProfile.getOwnUser();
                    AntiPatternsFlowProfileFollow.lastState                      = AntiPatternsState.EPivotalMenu;

                    AntiPatternsActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushShowForeignUserFollowers:
                {
                    //flag state content and launch activity 'follow details'
                    AntiPatternsFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EForeignProfile;
                    AntiPatternsStateFollowDetails.setupFollowersNextOnStart     = true;
                    AntiPatternsFlowProfileFollow.lastUser                       = AntiPatternsFlowProfile.getLastForeignUser();
                    AntiPatternsFlowProfileFollow.lastState                      = AntiPatternsState.EForeignProfile;

                    AntiPatternsActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushShowOwnUserFollowings:
                {
                    //flag state content and launch activity 'follow details'
                    AntiPatternsFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EOwnProfile;
                    AntiPatternsStateFollowDetails.setupFollowingsNextOnStart    = true;
                    AntiPatternsFlowProfileFollow.lastUser                       = AntiPatternsFlowProfile.getOwnUser();
                    AntiPatternsFlowProfileFollow.lastState                      = AntiPatternsState.EPivotalMenu;

                    AntiPatternsActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushShowForeignUserFollowings:
                {
                    //flag state content and launch activity 'follow details'
                    AntiPatternsFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EForeignProfile;
                    AntiPatternsStateFollowDetails.setupFollowingsNextOnStart    = true;
                    AntiPatternsFlowProfileFollow.lastUser                       = AntiPatternsFlowProfile.getLastForeignUser();
                    AntiPatternsFlowProfileFollow.lastState                      = AntiPatternsState.EForeignProfile;

                    AntiPatternsActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushFindFriends:
                {
                    if ( Features.ENABLE_FIND_FRIENDS_VIA_PHONEBOOK )
                    {
                        //show selection dialog
                        LibDialogItems.showUIThreaded
                        (
                            AntiPatternsState.ESettings.getActivity(),
                            R.string.dialog_find_friends_title,
                            new int[]      {  R.string.dialog_find_friends_item_facebook, R.string.dialog_find_friends_item_phonenumber, R.string.dialog_find_friends_item_seachterm, },
                            new Runnable[] { EPushFindFriendsViaFacebook, EPushFindFriendsViaPhonenumber, EPushFindFriendsViaSearchTerm, },
                            true,
                            AntiPatternsActionUnselect.EUnselectButtonsSettings
                        );
                    }
                    else
                    {
                        //show selection dialog
                        LibDialogItems.showUIThreaded
                        (
                            AntiPatternsState.ESettings.getActivity(),
                            R.string.dialog_find_friends_title,
                            new int[]      {  R.string.dialog_find_friends_item_facebook, R.string.dialog_find_friends_item_seachterm, },
                            new Runnable[] { EPushFindFriendsViaFacebook, EPushFindFriendsViaSearchTerm, },
                            true,
                            AntiPatternsActionUnselect.EUnselectButtonsSettings
                        );
                    }
                    break;
                }

                case EPushFindFriendsViaSearchTerm:
                {
                    //show 'find friends search term' dialog
                    LibDialogInput.showUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { AntiPatternsFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm,      },
                        R.layout.dialog_layout_input_dialog,
                        R.id.dialog_container,
                        new int[]                   { R.layout.dialog_view_input_singleline,                            },
                        new int[]                   { R.id.dialog_input_field,                                          },
                        new int[]                   { R.string.dialog_find_friends_via_searchterm_inputfield_hint,      },
                        new int[]                   { R.integer.inputfield_max_length_search_term_find_friends,         },
                        R.string.dialog_find_friends_via_searchterm_title,
                        R.dimen.content_distance_big,
                        new int[]                   { R.string.dialog_find_friends_via_searchterm_body,                 },
                        R.string.dialog_find_friends_via_searchterm_button_submit,
                        EPushFindFriendsViaSearchTermCleanSubmit,
                        R.string.dialog_find_friends_via_searchterm_button_cancel,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsSettings,
                        null
                    );

                    //preset value if desired
                    if ( Debug.DEBUG_PRESET_INPUTFIELDS )
                    {
                        AntiPatternsFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm.setText( "chris" );
                    }
                    break;
                }

                case EPushFindFriendsViaSearchTermCleanSubmit:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushFindFriendsViaSearchTermCleanSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushFindFriendsViaSearchTermCleanSubmitAfterProgressDialog:
                {
                    //assign last user-search-term
                    AntiPatternsFlowSearchUsers.lastUserSearchTerm   = AntiPatternsFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm.getText();
                    AntiPatternsFlowSearchUsers.nextReloadAction     = AntiPatternsActionUpdate.EUpdateFindFriendsViaSearchTermNextOffset;

                    //clean search data
                    AntiPatternsFlowSearchUsers.reset();

                    //search via searchterm
                    AntiPatternsFlowSearchUsers.orderNextFriendsSearchViaSearchTerm
                            (
                                    AntiPatternsState.ESettings,
                                    AntiPatternsActionDialog.EDialogFindFriendsViaSearchTermNoNetwork,
                                    AntiPatternsActionDialog.EDialogFindFriendsViaSearchTermTechnicalError,
                                    AntiPatternsActionDialog.EDialogFindFriendsViaSearchTermNoResults,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaSearchTermShowResults
                            );
                    break;
                }

                case EPushFindFriendsViaFacebook:
                {
                    break;
                }

                case EPushFindFriendsViaPhonenumber:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushFindFriendsViaPhonenumberAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushFindFriendsViaPhonenumberAfterProgressDialog:
                {
                    //read and assign user's phonenumbers from phonebook
                    AntiPatternsFlowSearchUsers.lastUserPhoneNumbers = LibPhoneBook.readAllPhonenumbersFromAllContacts( AntiPatternsState.ESettings.getActivity() );
                    AntiPatternsFlowSearchUsers.nextReloadAction     = AntiPatternsActionUpdate.EUpdateFindFriendsViaPhonenumberNextOffset;

                    //clean search data
                    AntiPatternsFlowSearchUsers.reset();

                    //simulate phone-numbers if desired
                    if ( Simulations.SIMULATE_FIND_FRIENDS_VIA_PHONENUMBERS )
                    {
                        AntiPatternsFlowSearchUsers.lastUserPhoneNumbers = new String[] { "08215489412", "09316668998", "01796872286", "01739234824", };
                    }

                    //search via searchterm
                    AntiPatternsFlowSearchUsers.orderNextFriendSearchViaPhonenumber
                            (
                                    AntiPatternsState.ESettings,
                                    AntiPatternsActionDialog.EDialogFindFriendsViaPhonenumberNoNetwork,
                                    AntiPatternsActionDialog.EDialogFindFriendsViaPhonenumberTechnicalError,
                                    AntiPatternsActionDialog.EDialogFindFriendsViaPhonenumberNoResults,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaPhonenumberShowResults
                            );
                    break;
                }

                case EPushListEntryFindFriendsResults:
                {
                    //get picked entry
                    AntiPatternsGridViewContent pickedListEntry = (AntiPatternsGridViewContent) AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EFindFriendsResults.getActivity(), GridViews.EFindFriends).getLastSelectedItem();
                    AntiPatternsDataUser pickedUser      = (AntiPatternsDataUser)pickedListEntry.getData();

                    //show this profile
                    AntiPatternsFlowProfile.showUserProfile(AntiPatternsState.EFindFriendsResults.getActivity(), pickedUser);

                    break;
                }

                case EPushListEntryImagePropertiesResults:
                {
                    //get picked user
                    AntiPatternsDataUser pickedUser      = null;
                    Object          pickedListEntry = AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EImageProperties.getActivity(), GridViews.EImageProperties).getLastSelectedItem();
                    Object          data            = ( ( (AntiPatternsGridViewContent)pickedListEntry ).getData() );

                    if ( data instanceof AntiPatternsDataLike)
                    {
                        pickedUser = ( (AntiPatternsDataLike)data ).iOwner;
                    }
                    else if ( data instanceof AntiPatternsDataComment)
                    {
                        pickedUser = ( (AntiPatternsDataComment)data ).iOwner;
                    }
                    else if ( data instanceof AntiPatternsDataRating)
                    {
                        pickedUser = ( (AntiPatternsDataRating)data ).iOwner;
                    }

                    //show this profile
                    AntiPatternsFlowProfile.showUserProfile(AntiPatternsState.EImageProperties.getActivity(), pickedUser);

                    break;
                }

                case EPushListEntryFollowDetails:
                {
                    //get picked entry
                    AntiPatternsGridViewContent pickedListEntry     = (AntiPatternsGridViewContent) AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EFollowDetails.getActivity(), GridViews.EFollowDetails).getLastSelectedItem();
                    AntiPatternsDataUser pickedUser          = (AntiPatternsDataUser)pickedListEntry.getData();

                    //show this profile
                    AntiPatternsFlowProfile.showUserProfile(AntiPatternsState.EFollowDetails.getActivity(), pickedUser);

                    break;
                }

                case EPushSearchImagesSubmit:
                {
                    //check, if 'use gps' is checked
                    AntiPatternsFlowSearchImages.useGPS = AntiPatternsFlowSearchImages.checkboxSearchImages.isChecked();

                    if ( AntiPatternsFlowSearchImages.useGPS )
                    {
                        //show 'please wait'
                        LibDialogProgress.showProgressDialogUIThreaded
                        (
                            AntiPatternsState.EPivotalMenu.getActivity(),
                            R.string.dialog_search_images_awaiting_gps_title,
                            R.string.dialog_search_images_awaiting_gps_body,
                            AntiPatternsActionPush.EPushSearchImagesSubmitNoLocationAfterProgressDialog,
                            true,
                            AntiPatternsActionPush.EPushCancelGPSSearchImages
                        );
                    }
                    else
                    {

                        //pick location from InputField
                        String location = AntiPatternsFlowSearchImages.inputHandlerSearchImagesLocation.getText();
                        AntiPatternsDebug.imageSearch.out( "PICKING LOCATION from search images dialog [" + location + "]" );

                        //check if 'location' is empty
                        if ( LibString.isEmpty( location ) )
                        {
                            //launch without a location
                            LibDialogProgress.showProgressDialogUIThreaded
                            (
                                AntiPatternsState.EPivotalMenu.getActivity(),
                                R.string.dialog_search_images_searching_title,
                                R.string.dialog_search_images_searching_body,
                                AntiPatternsAction.ESearchImagesAfterGPSSuccess,
                                false,
                                null
                            );
                        }
                        else
                        {
                            //enter 'google places'
                            AntiPatternsActionState.EEnterGooglePlacesAndSearchByTerm.run();
                        }
                    }
                    break;
                }

                case EPushSearchImagesSubmitNoLocationAfterProgressDialog:
                {
                    //pick current fine gps-location
                    LibGPS.startNewThread
                    (
                        AntiPatternsState.EPivotalMenu.getActivity(),
                        AntiPatternsAction.ESearchImagesAfterGPSSuccess,
                        AntiPatternsAction.ESearchImagesAfterGPSFailed,
                        AntiPatternsAction.ESearchImagesAfterGPSDisabled,
                        AntiPatternsSettings.Gps.GPS_CACHE_TIME_MILLIS,
                        Gps.GPS_LOCATION_SEARCH_TIMEOUT_MILLIS,
                        AntiPatternsDebug.gps,
                        Debug.DEBUG_SIMULATE_GPS
                    );
                    break;
                }

                case EPushCancelGPSNewEntry:
                {
                    //cancel the GPS-Thread and leave 'GooglePlaces'
                    LibGPS.cancelCurrentThread();
                    AntiPatternsActionState.ELeaveGooglePlaces.run();
                    break;
                }

                case EPushCancelGPSSearchImages:
                {
                    //cancel the GPS-Thread and unselect the search button
                    LibGPS.cancelCurrentThread();
                    AntiPatternsActionUnselect.EUnselectButtonsPivotalHeader.run();
                    break;
                }

                case EPushSessionExpiredOk:
                {
                    //logout the current user
                    AntiPatternsFlowUser.logout(AntiPatternsIDM.lastStateBeforeSessionExpired);
                    break;
                }

                case EPushSettingsCheckUpdate:
                {
                    //show 'please wait'-dialog and order update check
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        AntiPatternsActionPush.EPushSettingsCheckUpdateAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSettingsCheckUpdateAfterProgressDialog:
                {
                    //perform an update check
                    AntiPatternsFlowGeneral.checkAppUpdate
                            (
                                    AntiPatternsState.ESettings,
                                    AntiPatternsActionDialog.EDialogNoUpdate,
                                    AntiPatternsActionDialog.EDialogUpdateOptional,
                                    AntiPatternsActionDialog.EDialogUpdateRequired,
                                    AntiPatternsActionDialog.EDialogUpdateCheckNoNetwork,
                                    AntiPatternsActionDialog.EDialogUpdateCheckTechnicalError,
                                    true
                            );
                    break;
                }
            }
        }
    }
