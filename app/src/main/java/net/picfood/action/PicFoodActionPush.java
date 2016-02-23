/*  $Id: PicFoodActionPush.java 50667 2013-08-22 07:22:59Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.action;

    import  net.picfood.*;
    import  net.picfood.PicFoodProject.Debug;
    import  net.picfood.PicFoodProject.Debug.Simulations;
    import  net.picfood.PicFoodProject.Features;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  net.picfood.PicFoodSettings.*;
    import  net.picfood.data.*;
    import  net.picfood.ext.facebook.*;
    import  net.picfood.flow.*;
    import  net.picfood.flow.PicFoodFlowProfile.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.SaveKey;
    import  net.picfood.state.*;
    import  net.picfood.state.acclaim.*;
    import  net.picfood.ui.adapter.*;
    import  net.picfood.ui.adapter.PicFoodAdapterManager.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.*;
    import  com.synapsy.android.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50667 $ $Date: 2013-08-22 09:22:59 +0200 (Do, 22 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/action/PicFoodActionPush.java $"
    **********************************************************************************************/
    public enum PicFoodActionPush implements Runnable
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
                case EPushAcclaimNextPage:
                {
                    //unselect all buttons
                    PicFoodStateAcclaim.unselectAllButtonsUIThreaded();

                    //switch ViewPager to the next page
                    PicFoodStateAcclaim.switchViewPagerToNextPageUIThreaded();
                    break;
                }

                case EPushSkipAcclaim:
                {
                    //unselect all buttons
                    PicFoodStateAcclaim.unselectAllButtonsUIThreaded();

                    //switch ViewPager to the last page
                    PicFoodStateAcclaim.switchViewPagerToLastPageUIThreaded();
                    break;
                }

                case EPushUploadImageFromCamera:
                {
                    //ditch old data
                    PicFoodFlowNewEntry.ditchOldData( PicFoodState.EPivotalMenu.getActivity() );

                    //order state 'new entry' to launch the camera
                    PicFoodStateNewEntry.launchCameraNextOnStart = true;

                    //show state 'new entry'
                    LibLauncher.launchActivity( PicFoodState.EPivotalMenu.getActivity(), PicFoodStateNewEntry.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushUploadImageFromGallery:
                {
                    //ditch old data
                    PicFoodFlowNewEntry.ditchOldData( PicFoodState.EPivotalMenu.getActivity() );

                    //order state 'new entry' to launch the image picker
                    PicFoodStateNewEntry.launchGalleryNextOnStart = true;

                    //show state 'new entry'
                    LibLauncher.launchActivity( PicFoodState.EPivotalMenu.getActivity(), PicFoodStateNewEntry.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushEclipseApp:
                {
                    Lib.showHomescreenActivity( PicFoodState.EPivotalMenu.getActivity() );
                    break;
                }

                case EPushListEntryGooglePlaceNewEntry:
                {
                    //get picked entry
                    PicFoodGridViewContent  pickedGooglePlaceEntry  = (PicFoodGridViewContent)PicFoodAdapterManager.getSingleton( PicFoodState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces ).getLastSelectedItem();
                    PicFoodDataGooglePlace  pickedGooglePlace       = (PicFoodDataGooglePlace)pickedGooglePlaceEntry.getData();

                    //save persistent
                    PicFoodSave.saveSetting( PicFoodState.EGooglePlaces.getActivity(), SaveKey.EStateNewEntryLastPickedGooglePlace, LibIO.serializableToString( pickedGooglePlace ) );

                    //leave state 'google places'
                    PicFoodActionState.ELeaveGooglePlaces.run();

                    break;
                }

                case EPushListEntryGooglePlaceImageSearch:
                {
                    //show 'please wait'-message
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
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
                    PicFoodGridViewContent      pickedGooglePlaceEntry  = (PicFoodGridViewContent)PicFoodAdapterManager.getSingleton( PicFoodState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces ).getLastSelectedItem();
                    PicFoodDataGooglePlace      pickedGooglePlace       = (PicFoodDataGooglePlace)pickedGooglePlaceEntry.getData();

                    //perform search with this location
                    PicFoodDebug.imageSearch.out( "Picked Google Place - Searching images for location [" + pickedGooglePlace.iName + "]" );

                    //assign last activity
                    PicFoodFlowSearchImages.lastStateBeforeSearch       = PicFoodState.EGooglePlaces;
                    PicFoodFlowSearchImages.lastLatitude                = String.valueOf( pickedGooglePlace.iLatitude  );
                    PicFoodFlowSearchImages.lastLongitude               = String.valueOf( pickedGooglePlace.iLongitude );
                    PicFoodFlowSearchImages.lastSearchTerm              = PicFoodFlowSearchImages.inputHandlerSearchImagesTerm.getText();

                    //assign last actions
                    PicFoodFlowSearchImages.lastActionOnNoNetwork       = PicFoodActionDialog.EDialogSearchImagesGooglePlacesNoNetwork;
                    PicFoodFlowSearchImages.lastActionOnTechnicalError  = PicFoodActionDialog.EDialogSearchImagesGooglePlacesTechnicalError;
                    PicFoodFlowSearchImages.lastActionOnNoResults       = PicFoodActionDialog.EDialogSearchImagesGooglePlacesNoResults;

                    //enter state 'search images'
                    PicFoodActionState.EEnterSearchImagesResults.run();
                    break;
                }

                case EPushRegisterTermsAndConditions:
                {
                    //show 'please wait'-dialog and order terms and conditions
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushRegisterTermsAndConditionsAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushRegisterTermsAndConditionsAfterProgressDialog:
                {
                    //show latest terms and conditions
                    PicFoodFlowGeneral.showTermsAndConditions( PicFoodState.ERegister, PicFoodAction.ENone );
                    break;
                }

                case EPushSettingsPrivacyPolicy:
                {
                    //show 'please wait'-dialog and order privacy policy
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushSettingsPrivacyPolicyAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSettingsPrivacyPolicyAfterProgressDialog:
                {
                    //show latest privacy policy
                    PicFoodFlowGeneral.showPrivacyPolicy( PicFoodState.ESettings, PicFoodActionUnselect.EUnselectButtonsSettings );
                    break;
                }

                case EPushSendFeedback:
                {
                    //show feedback-dialog
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowUser.inputHandlerFeedback,         },
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
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        null
                    );
                    break;
                }

                case EPushChangeProfile:
                {
                    //show dialog 'picking your current profile'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_picking_profile_title,
                        R.string.dialog_please_wait_picking_profile_body,
                        PicFoodActionPush.EPushChangeProfileAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushChangeProfileAfterProgressDialog:
                {
                    PicFoodFlowProfileData.updateOwnProfileDataOnly( PicFoodState.ESettings );
                    break;
                }

                case EPushChangeProfileImage:
                {
                    //show image picker
                    LibLauncher.launchImagePicker( PicFoodState.ESettings.getActivity(), ActivityRequestID.STATE_SETTINGS_PICK_IMAGE_FROM_GALLERY );
                    break;
                }

                case EPushChangeProfileSubmit:
                {
                    //show 'please wait' dialog and commit the feedback
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushChangeProfileSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushChangeProfileSubmitAfterProgressDialog:
                {
                    //submit the profile change
                    PicFoodFlowProfileChange.changeProfile();
                    break;
                }

                case EPushSendFeedbackSubmit:
                {
                    //show 'please wait' dialog and commit the feedback
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushSendFeedbackYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSendFeedbackYesAfterProgressDialog:
                {
                    //send feedback
                    PicFoodFlowUser.sendFeedback( PicFoodState.ESettings );
                    break;
                }

                case EPushSettingsTermsAndConditions:
                {
                    //show 'please wait'-dialog and order terms and conditions
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushSettingsTermsAndConditionsAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSettingsTermsAndConditionsAfterProgressDialog:
                {
                    //show latest terms and conditions
                    PicFoodFlowGeneral.showTermsAndConditions( PicFoodState.ESettings, PicFoodActionUnselect.EUnselectButtonsSettings );
                    break;
                }

                case EPushRegisterImportFacebookData:
                {
                    //import register data from facebook
                    PicFoodFacebook.fillRegisterData( PicFoodState.ERegister );
                    break;
                }

                case EPushLogin:
                {
                    //show 'please wait'-dialog and order login
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ELogin.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushLoginAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushLoginAfterProgressDialog:
                {
                    //perform login
                    PicFoodFlowAuth.login();
                    break;
                }

                case EPushLostPassword:
                {
                    //show 'please wait'-dialog and order passwort-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ELostPassword.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushLostPasswordAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushLostPasswordAfterProgressDialog:
                {
                    //perform request-password
                    PicFoodFlowAuth.lostPassword();
                    break;
                }

                case EPushRegister:
                {
                    //show 'please wait'-dialog and order register
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ERegister.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushRegisterAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushRegisterAfterProgressDialog:
                {
                    //perform register
                    PicFoodFlowRegister.register();
                    break;
                }

                case EPushLogout:
                {
                    //show dialog 'Logout confirmation'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_logout_confirmation_title,
                        R.string.dialog_logout_confirmation_body,
                        R.string.dialog_logout_confirmation_button_yes,
                        EPushLogoutConfirmationYes,
                        R.string.dialog_logout_confirmation_button_no,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EPushLogoutConfirmationYes:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushLogoutConfirmationYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushLogoutConfirmationYesAfterProgressDialog:
                {
                    //commit logout
                    PicFoodFlowUser.logout( PicFoodState.ESettings );
                    break;
                }

                case EPushRegisterChooseProfileImage:
                {
                    //show image picker
                    LibLauncher.launchImagePicker( PicFoodState.ERegister.getActivity(), ActivityRequestID.STATE_REGISTER_PICK_IMAGE_FROM_GALLERY );
                    break;
                }

                case EPushDeleteAccount:
                {
                    //show 'delete account confirm'-dialog
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_delete_account_confirm_delete_title,
                        R.string.dialog_delete_account_confirm_delete_body,
                        R.string.dialog_delete_account_confirm_delete_button_ok,
                        EPushDeleteAccountShowPasswordConfirmation,
                        R.string.dialog_delete_account_confirm_delete_button_cancel,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings
                    );
                    break;
                }

                case EPushDeleteAccountShowPasswordConfirmation:
                {
                    //show 'delete account'-dialog
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowUser.inputHandlerDeleteUserPasswordConfirmation,       },
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
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        null
                    );
                    break;
                }

                case EPushDeleteAccountPasswordConfirmationYes:
                {
                    //show 'please wait' dialog and send 'delete user'-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushDeleteAccountPasswordConfirmationYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushDeleteAccountPasswordConfirmationYesAfterProgressDialog:
                {
                    //commit user-deletion
                    PicFoodFlowUser.removeUser( PicFoodState.ESettings );
                    break;
                }

                case EPushDeleteImageCache:
                {
                    //show 'please wait' dialog and delete the cache
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_delete_cache_title,
                        R.string.dialog_please_wait_delete_cache_body_1,
                        PicFoodActionPush.EPushImageCacheAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageCacheAfterProgressDialog:
                {
                    PicFoodCache.deleteCache( PicFoodState.ESettings.getActivity() );
                    break;
                }

                case EPushSubmitNewEntry:
                {
                    //show 'please wait while your image is uploaded'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ENewEntry.getActivity(),
                        R.string.dialog_upload_new_entry_title,
                        R.string.dialog_upload_new_entry_body,
                        PicFoodActionPush.EPushSubmitNewEntryAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSubmitNewEntryAfterProgressDialog:
                {
                    //upload the new entry - last cropped bitmap must exist - but we handle this for sure!
                    if ( LibIO.isExistent( PicFoodSD.getFileLastCroppedImageNewEntry() ) )
                    {
                        PicFoodFlowNewEntry.uploadEntry( PicFoodState.ENewEntry );
                    }
                    else
                    {
                        //dismiss the progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ENewEntry.getActivity() );

                        //leave state 'new entry'
                        PicFoodActionState.ELeaveNewEntry.run();
                    }
                    break;
                }

                case EPushImageLikeUnlike:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
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
                    PicFoodFlowImageLike.likeOrUnlike();
                    break;
                }

                case EPushImageOptions:
                {
                    //check if the user owns this image
                    int[]       captions = null;
                    Runnable[]  actions  = null;
                    if ( PicFoodIDM.getUserID( PicFoodFlowImage.lastState.getActivity() ).equals( PicFoodFlowImage.lastImage.iOwner.iUserID ) )
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
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_image_options_title,
                        captions,
                        actions,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EPushImageShare:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
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
                    PicFoodFlowImage.shareImage();
                    break;
                }

                case EPushImageReportAbuse:
                {
                    //show 'report abuse'-dialog
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowImage.inputHandlerReportAbuse,                 },
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
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        null
                    );
                    break;
                }

                case EPushImageReportAbuseYes:
                {
                    //show 'please wait' dialog and commit the feedback
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushImageReportAbuseYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageReportAbuseYesAfterProgressDialog:
                {
                    //send feedback
                    PicFoodFlowImage.reportAbuse( PicFoodFlowImage.inputHandlerReportAbuse.getText() );
                    break;
                }

                case EPushImageRemove:
                {
                    //show dialog 'remove image'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_remove_image_title,
                        R.string.dialog_remove_image_body,
                        R.string.dialog_remove_image_button_yes,
                        EPushImageRemoveYes,
                        R.string.dialog_remove_image_button_no,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EPushImageRemoveYes:
                {
                    //show 'please wait' dialog and commit the remove-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushImageRemoveYesAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageRemoveYesAfterProgressDialog:
                {
                    //send remove-request
                    PicFoodFlowImage.removeImage();
                    break;
                }

                case EPushImageRateFood:
                {
                    //show dialog 'no network'
                    LibDialogImageButtons.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_rate_food_title,
                        R.string.dialog_rate_food_body,
                        R.string.dialog_rate_food_button_cancel,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        new int[]       { R.drawable.net_picfood_rating_big_green,  R.drawable.net_picfood_rating_big_yellow,   R.drawable.net_picfood_rating_big_red,  },
                        new Runnable[]  { EPushImageRateHighest,                    EPushImageRateMedium,                       EPushImageRateLowest,                   },
                        R.dimen.button_smiley,
                        R.layout.dialog_layout_image_buttons,
                        R.id.text_container,
                        R.id.button_container,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage
                    );
                    break;
                }

                case EPushImageRateHighest:
                {
                    //assign rating
                    PicFoodFlowImage.lastFoodRating = PicFoodSettings.Image.FOOD_RATING_HIGHEST;

                    //launch rate action
                    EPushImageRate.run();

                    break;
                }

                case EPushImageRateMedium:
                {
                    //assign rating
                    PicFoodFlowImage.lastFoodRating = PicFoodSettings.Image.FOOD_RATING_MEDIUM;

                    //launch rate action
                    EPushImageRate.run();

                    break;
                }

                case EPushImageRateLowest:
                {
                    //assign rating
                    PicFoodFlowImage.lastFoodRating = PicFoodSettings.Image.FOOD_RATING_LOWEST;

                    //launch rate action
                    EPushImageRate.run();

                    break;
                }

                case EPushImageRate:
                {
                    //hide rate dialog
                    LibDialogImageButtons.dismissLastDialogImageButtonsUIThreaded( PicFoodFlowImage.lastState.getActivity() );

                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushImageRateAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageRateAfterProgressDialog:
                {
                    //commit the image-food-rate
                    PicFoodFlowImageRating.rateFood();
                    break;
                }

                case EPushImageComment:
                {
                    //show comment-dialog
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowImageComment.inputHandlerComment,      },
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
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsImage,
                        null
                    );
                    break;
                }

                case EPushImageCommentSubmit:
                {
                    //show 'please wait' dialog and commit the remove-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodFlowImage.lastState.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushImageCommentSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushImageCommentSubmitAfterProgressDialog:
                {
                    //send comment-submission
                    PicFoodFlowImageComment.commentImage( PicFoodFlowImageComment.inputHandlerComment.getText().toString() );
                    break;
                }

                case EPushImageShowLikes:
                {
                    //show all likes in state 'image details'
                    PicFoodStateImageProperties.setupLikesNextOnStart = true;
                    LibLauncher.launchActivity( PicFoodFlowImage.lastState.getActivity(), PicFoodStateImageProperties.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushImageShowComments:
                {
                    //show all comments in state 'image details'
                    PicFoodStateImageProperties.setupCommentsNextOnStart = true;
                    LibLauncher.launchActivity( PicFoodFlowImage.lastState.getActivity(), PicFoodStateImageProperties.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushImageShowFoodRatings:
                {
                    //show all food-ratings in state 'image details'
                    PicFoodStateImageProperties.setupFoodRatingsNextOnStart = true;
                    LibLauncher.launchActivity( PicFoodFlowImage.lastState.getActivity(), PicFoodStateImageProperties.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EPushChangePassword:
                {
                    //show 'change password'-dialog
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowUser.inputHandlerChangePasswordOldPassword,            PicFoodFlowUser.inputHandlerChangePasswordNewPassword,          PicFoodFlowUser.inputHandlerChangePasswordNewPasswordRepeat,            },
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
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        null
                    );
                    break;
                }

                case EPushChangePasswordSubmit:
                {
                    //show 'please wait' dialog and send 'change password'-request
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushChangePasswordSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushChangePasswordSubmitAfterProgressDialog:
                {
                    //commit password-change
                    PicFoodFlowUser.changePassword( PicFoodState.ESettings );
                    break;
                }

                case EPushUserToggleFollowship:
                {
                    //show 'please wait' dialog and toggle followship
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.EForeignProfile.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushUserToggleFollowshipAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushUserToggleFollowshipAfterProgressDialog:
                {
                    //toggle followship
                    PicFoodFlowProfile.toggleFollowship();
                    break;
                }

                case EPushUserBlock:
                {
                    //show 'please wait' dialog and request block
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.EForeignProfile.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushUserBlockAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushUserBlockAfterProgressDialog:
                {
                    //set block
                    PicFoodFlowProfile.setBlock();
                    break;
                }

                case EPushShowOwnUserFollowers:
                {
                    //flag state content and launch activity 'follow details'
                    PicFoodFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EOwnProfile;
                    PicFoodStateFollowDetails.setupFollowersNextOnStart     = true;
                    PicFoodFlowProfileFollow.lastUser                       = PicFoodFlowProfile.getOwnUser();
                    PicFoodFlowProfileFollow.lastState                      = PicFoodState.EPivotalMenu;

                    PicFoodActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushShowForeignUserFollowers:
                {
                    //flag state content and launch activity 'follow details'
                    PicFoodFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EForeignProfile;
                    PicFoodStateFollowDetails.setupFollowersNextOnStart     = true;
                    PicFoodFlowProfileFollow.lastUser                       = PicFoodFlowProfile.getLastForeignUser();
                    PicFoodFlowProfileFollow.lastState                      = PicFoodState.EForeignProfile;

                    PicFoodActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushShowOwnUserFollowings:
                {
                    //flag state content and launch activity 'follow details'
                    PicFoodFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EOwnProfile;
                    PicFoodStateFollowDetails.setupFollowingsNextOnStart    = true;
                    PicFoodFlowProfileFollow.lastUser                       = PicFoodFlowProfile.getOwnUser();
                    PicFoodFlowProfileFollow.lastState                      = PicFoodState.EPivotalMenu;

                    PicFoodActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushShowForeignUserFollowings:
                {
                    //flag state content and launch activity 'follow details'
                    PicFoodFlowProfile.profileDetailsEnteredVia             = ProfileDetailsEnteredVia.EForeignProfile;
                    PicFoodStateFollowDetails.setupFollowingsNextOnStart    = true;
                    PicFoodFlowProfileFollow.lastUser                       = PicFoodFlowProfile.getLastForeignUser();
                    PicFoodFlowProfileFollow.lastState                      = PicFoodState.EForeignProfile;

                    PicFoodActionState.EEnterFollowDetails.run();
                    break;
                }

                case EPushFindFriends:
                {
                    if ( Features.ENABLE_FIND_FRIENDS_VIA_PHONEBOOK )
                    {
                        //show selection dialog
                        LibDialogItems.showUIThreaded
                        (
                            PicFoodState.ESettings.getActivity(),
                            R.string.dialog_find_friends_title,
                            new int[]      {  R.string.dialog_find_friends_item_facebook, R.string.dialog_find_friends_item_phonenumber, R.string.dialog_find_friends_item_seachterm, },
                            new Runnable[] { EPushFindFriendsViaFacebook, EPushFindFriendsViaPhonenumber, EPushFindFriendsViaSearchTerm, },
                            true,
                            PicFoodActionUnselect.EUnselectButtonsSettings
                        );
                    }
                    else
                    {
                        //show selection dialog
                        LibDialogItems.showUIThreaded
                        (
                            PicFoodState.ESettings.getActivity(),
                            R.string.dialog_find_friends_title,
                            new int[]      {  R.string.dialog_find_friends_item_facebook, R.string.dialog_find_friends_item_seachterm, },
                            new Runnable[] { EPushFindFriendsViaFacebook, EPushFindFriendsViaSearchTerm, },
                            true,
                            PicFoodActionUnselect.EUnselectButtonsSettings
                        );
                    }
                    break;
                }

                case EPushFindFriendsViaSearchTerm:
                {
                    //show 'find friends search term' dialog
                    LibDialogInput.showUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        new LibDialogInputHandler[] { PicFoodFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm,      },
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
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsSettings,
                        null
                    );

                    //preset value if desired
                    if ( Debug.DEBUG_PRESET_INPUTFIELDS )
                    {
                        PicFoodFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm.setText( "chris" );
                    }
                    break;
                }

                case EPushFindFriendsViaSearchTermCleanSubmit:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushFindFriendsViaSearchTermCleanSubmitAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushFindFriendsViaSearchTermCleanSubmitAfterProgressDialog:
                {
                    //assign last user-search-term
                    PicFoodFlowSearchUsers.lastUserSearchTerm   = PicFoodFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm.getText();
                    PicFoodFlowSearchUsers.nextReloadAction     = PicFoodActionUpdate.EUpdateFindFriendsViaSearchTermNextOffset;

                    //clean search data
                    PicFoodFlowSearchUsers.reset();

                    //search via searchterm
                    PicFoodFlowSearchUsers.orderNextFriendsSearchViaSearchTerm
                    (
                        PicFoodState.ESettings,
                        PicFoodActionDialog.EDialogFindFriendsViaSearchTermNoNetwork,
                        PicFoodActionDialog.EDialogFindFriendsViaSearchTermTechnicalError,
                        PicFoodActionDialog.EDialogFindFriendsViaSearchTermNoResults,
                        PicFoodActionUpdate.EUpdateFindFriendsViaSearchTermShowResults
                    );
                    break;
                }

                case EPushFindFriendsViaFacebook:
                {
                    if ( Simulations.SIMULATE_FIND_FRIENDS_VIA_FACEBOOK )
                    {
                        //assign the last facebook-uids
                        PicFoodFlowSearchUsers.lastFacebookUIDs     = new String[] { "1660999065", };
                        PicFoodFlowSearchUsers.nextReloadAction     = PicFoodActionUpdate.EUpdateFindFriendsViaFacebookNextOffset;
                        PicFoodFlowSearchUsers.lastOwnFacebookID    = "";

                        //clean search data
                        PicFoodFlowSearchUsers.reset();

                        //perform 1st friends search
                        PicFoodFlowSearchUsers.orderNextFriendsSearchViaFacebookIDs
                        (
                            PicFoodState.ESettings,
                            PicFoodActionDialog.EDialogFindFriendsViaFacebookNoNetwork,
                            PicFoodActionDialog.EDialogFindFriendsViaFacebookTechnicalError,
                            PicFoodActionDialog.EDialogFindFriendsViaFacebookNoResults,
                            PicFoodActionUpdate.EUpdateFindFriendsViaFacebookShowResults
                        );
                    }
                    else
                    {
                        //pick facebook-friend-ids from facebook
                        PicFoodFacebook.findFriendsViaFacebook( PicFoodState.ESettings );
                    }

                    break;
                }

                case EPushFindFriendsViaPhonenumber:
                {
                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushFindFriendsViaPhonenumberAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushFindFriendsViaPhonenumberAfterProgressDialog:
                {
                    //read and assign user's phonenumbers from phonebook
                    PicFoodFlowSearchUsers.lastUserPhoneNumbers = LibPhoneBook.readAllPhonenumbersFromAllContacts( PicFoodState.ESettings.getActivity() );
                    PicFoodFlowSearchUsers.nextReloadAction     = PicFoodActionUpdate.EUpdateFindFriendsViaPhonenumberNextOffset;

                    //clean search data
                    PicFoodFlowSearchUsers.reset();

                    //simulate phone-numbers if desired
                    if ( Simulations.SIMULATE_FIND_FRIENDS_VIA_PHONENUMBERS )
                    {
                        PicFoodFlowSearchUsers.lastUserPhoneNumbers = new String[] { "08215489412", "09316668998", "01796872286", "01739234824", };
                    }

                    //search via searchterm
                    PicFoodFlowSearchUsers.orderNextFriendSearchViaPhonenumber
                    (
                        PicFoodState.ESettings,
                        PicFoodActionDialog.EDialogFindFriendsViaPhonenumberNoNetwork,
                        PicFoodActionDialog.EDialogFindFriendsViaPhonenumberTechnicalError,
                        PicFoodActionDialog.EDialogFindFriendsViaPhonenumberNoResults,
                        PicFoodActionUpdate.EUpdateFindFriendsViaPhonenumberShowResults
                    );
                    break;
                }

                case EPushListEntryFindFriendsResults:
                {
                    //get picked entry
                    PicFoodGridViewContent  pickedListEntry = (PicFoodGridViewContent)PicFoodAdapterManager.getSingleton( PicFoodState.EFindFriendsResults.getActivity(), GridViews.EFindFriends ).getLastSelectedItem();
                    PicFoodDataUser         pickedUser      = (PicFoodDataUser)pickedListEntry.getData();

                    //show this profile
                    PicFoodFlowProfile.showUserProfile( PicFoodState.EFindFriendsResults.getActivity(), pickedUser );

                    break;
                }

                case EPushListEntryImagePropertiesResults:
                {
                    //get picked user
                    PicFoodDataUser pickedUser      = null;
                    Object          pickedListEntry = PicFoodAdapterManager.getSingleton( PicFoodState.EImageProperties.getActivity(), GridViews.EImageProperties ).getLastSelectedItem();
                    Object          data            = ( ( (PicFoodGridViewContent)pickedListEntry ).getData() );

                    if ( data instanceof PicFoodDataLike )
                    {
                        pickedUser = ( (PicFoodDataLike)data ).iOwner;
                    }
                    else if ( data instanceof PicFoodDataComment )
                    {
                        pickedUser = ( (PicFoodDataComment)data ).iOwner;
                    }
                    else if ( data instanceof PicFoodDataRating )
                    {
                        pickedUser = ( (PicFoodDataRating)data ).iOwner;
                    }

                    //show this profile
                    PicFoodFlowProfile.showUserProfile( PicFoodState.EImageProperties.getActivity(), pickedUser );

                    break;
                }

                case EPushListEntryFollowDetails:
                {
                    //get picked entry
                    PicFoodGridViewContent  pickedListEntry     = (PicFoodGridViewContent)PicFoodAdapterManager.getSingleton( PicFoodState.EFollowDetails.getActivity(), GridViews.EFollowDetails ).getLastSelectedItem();
                    PicFoodDataUser         pickedUser          = (PicFoodDataUser)pickedListEntry.getData();

                    //show this profile
                    PicFoodFlowProfile.showUserProfile( PicFoodState.EFollowDetails.getActivity(), pickedUser );

                    break;
                }

                case EPushSearchImagesSubmit:
                {
                    //check, if 'use gps' is checked
                    PicFoodFlowSearchImages.useGPS = PicFoodFlowSearchImages.checkboxSearchImages.isChecked();

                    if ( PicFoodFlowSearchImages.useGPS )
                    {
                        //show 'please wait'
                        LibDialogProgress.showProgressDialogUIThreaded
                        (
                            PicFoodState.EPivotalMenu.getActivity(),
                            R.string.dialog_search_images_awaiting_gps_title,
                            R.string.dialog_search_images_awaiting_gps_body,
                            PicFoodActionPush.EPushSearchImagesSubmitNoLocationAfterProgressDialog,
                            true,
                            PicFoodActionPush.EPushCancelGPSSearchImages
                        );
                    }
                    else
                    {

                        //pick location from InputField
                        String location = PicFoodFlowSearchImages.inputHandlerSearchImagesLocation.getText();
                        PicFoodDebug.imageSearch.out( "PICKING LOCATION from search images dialog [" + location + "]" );

                        //check if 'location' is empty
                        if ( LibString.isEmpty( location ) )
                        {
                            //launch without a location
                            LibDialogProgress.showProgressDialogUIThreaded
                            (
                                PicFoodState.EPivotalMenu.getActivity(),
                                R.string.dialog_search_images_searching_title,
                                R.string.dialog_search_images_searching_body,
                                PicFoodAction.ESearchImagesAfterGPSSuccess,
                                false,
                                null
                            );
                        }
                        else
                        {
                            //enter 'google places'
                            PicFoodActionState.EEnterGooglePlacesAndSearchByTerm.run();
                        }
                    }
                    break;
                }

                case EPushSearchImagesSubmitNoLocationAfterProgressDialog:
                {
                    //pick current fine gps-location
                    LibGPS.startNewThread
                    (
                        PicFoodState.EPivotalMenu.getActivity(),
                        PicFoodAction.ESearchImagesAfterGPSSuccess,
                        PicFoodAction.ESearchImagesAfterGPSFailed,
                        PicFoodAction.ESearchImagesAfterGPSDisabled,
                        PicFoodSettings.Gps.GPS_CACHE_TIME_MILLIS,
                        Gps.GPS_LOCATION_SEARCH_TIMEOUT_MILLIS,
                        PicFoodDebug.gps,
                        Debug.DEBUG_SIMULATE_GPS
                    );
                    break;
                }

                case EPushCancelGPSNewEntry:
                {
                    //cancel the GPS-Thread and leave 'GooglePlaces'
                    LibGPS.cancelCurrentThread();
                    PicFoodActionState.ELeaveGooglePlaces.run();
                    break;
                }

                case EPushCancelGPSSearchImages:
                {
                    //cancel the GPS-Thread and unselect the search button
                    LibGPS.cancelCurrentThread();
                    PicFoodActionUnselect.EUnselectButtonsPivotalHeader.run();
                    break;
                }

                case EPushSessionExpiredOk:
                {
                    //logout the current user
                    PicFoodFlowUser.logout( PicFoodIDM.lastStateBeforeSessionExpired );
                    break;
                }

                case EPushSettingsCheckUpdate:
                {
                    //show 'please wait'-dialog and order update check
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.ESettings.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        PicFoodActionPush.EPushSettingsCheckUpdateAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EPushSettingsCheckUpdateAfterProgressDialog:
                {
                    //perform an update check
                    PicFoodFlowGeneral.checkAppUpdate
                    (
                        PicFoodState.ESettings,
                        PicFoodActionDialog.EDialogNoUpdate,
                        PicFoodActionDialog.EDialogUpdateOptional,
                        PicFoodActionDialog.EDialogUpdateRequired,
                        PicFoodActionDialog.EDialogUpdateCheckNoNetwork,
                        PicFoodActionDialog.EDialogUpdateCheckTechnicalError,
                        true
                    );
                    break;
                }
            }
        }
    }
