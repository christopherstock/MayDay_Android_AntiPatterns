
    package de.mayflower.antipatterns.action;

    import de.mayflower.antipatterns.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import  de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.GridViews;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public enum AntiPatternsActionUpdate implements Runnable
    {
        /** */  EUpdateDetailedImage,
        /** */  EUpdateDetailedImageAfterProgressDialog,
        /** */  EUpdateExploreAreaClean,
        /** */  EUpdateExploreAreaNextLoadingNoNetwork,
        /** */  EUpdateExploreAreaNextOffset,
        /** */  EUpdateFindFriendsUpdateResults,
        /** */  EUpdateFindFriendsViaFacebookNextOffset,
        /** */  EUpdateFindFriendsViaFacebookNextOffsetNoNetwork,
        /** */  EUpdateFindFriendsViaFacebookShowResults,
        /** */  EUpdateFindFriendsViaPhonenumberNextOffset,
        /** */  EUpdateFindFriendsViaPhonenumberNextOffsetNoNetwork,
        /** */  EUpdateFindFriendsViaPhonenumberShowResults,
        /** */  EUpdateFindFriendsViaSearchTermNextOffset,
        /** */  EUpdateFindFriendsViaSearchTermNextOffsetNoNetwork,
        /** */  EUpdateFindFriendsViaSearchTermShowResults,
        /** */  EUpdateFollowersDetailsClean,
        /** */  EUpdateFollowersDetailsCleanAfterProgressDialog,
        /** */  EUpdateFollowersDetailsNextLoadingNoNetwork,
        /** */  EUpdateFollowersDetailsNextOffset,
        /** */  EUpdateFollowingsDetailsClean,
        /** */  EUpdateFollowingsDetailsCleanAfterProgressDialog,
        /** */  EUpdateFollowingsDetailsNextLoadingNoNetwork,
        /** */  EUpdateFollowingsDetailsNextOffset,
        /** */  EUpdateForeignProfile,
        /** */  EUpdateImageListAfterImageChange,
        /** */  EUpdateImageListAfterImageRemove,
        /** */  EUpdateImagePropertiesCommentsClean,
        /** */  EUpdateImagePropertiesCommentsCleanAfterProgressDialog,
        /** */  EUpdateImagePropertiesCommentsNextLoadingNoNetwork,
        /** */  EUpdateImagePropertiesCommentsNextOffset,
        /** */  EUpdateImagePropertiesFoodRatingsClean,
        /** */  EUpdateImagePropertiesFoodRatingsCleanAfterProgressDialog,
        /** */  EUpdateImagePropertiesFoodRatingsNextOffset,
        /** */  EUpdateImagePropertiesLikesClean,
        /** */  EUpdateImagePropertiesLikesCleanAfterProgressDialog,
        /** */  EUpdateImagePropertiesLikesNextLoadingNoNetwork,
        /** */  EUpdateImagePropertiesLikesNextOffset,
        /** */  EUpdateImagePropertiesRatingsNextLoadingNoNetwork,
        /** */  EUpdateLastUserProfileClean,
        /** */  EUpdateLastUserProfileImagesClean,
        /** */  EUpdateOwnUserProfileDataOnly,
        /** */  EUpdateOwnUserProfileFull,
        /** */  EUpdateSearchImagesResultsClean,
        /** */  EUpdateSearchImagesResultsNext,
        /** */  EUpdateSearchImagesResultsNextLoadingNoNetwork,
        /** */  EUpdateUserProfileImagesNext,
        /** */  EUpdateUserProfileImagesNextLoadingNoNetwork,
        /** */  EUpdateUserWallClean,
        /** */  EUpdateUserWallNextLoadingNoNetwork,
        /** */  EUpdateUserWallNext,
        /** */  EUpdateSubmitChangedProfileImage,

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

            switch ( this )
            {
                case EUpdateUserWallClean:
                {
                    //reset offset and wall-entries
                    AntiPatternsFlowWall.reset();

                    //update user's wall
                    AntiPatternsFlowWall.orderNextUserWallImages
                    (
                        AntiPatternsState.EPivotalMenu,
                        AntiPatternsStatePivotalTabWall.singleton.iOverlayIcon,
                        AntiPatternsAction.ENone,
                        AntiPatternsAction.ENone
                    );
                    break;
                }

                case EUpdateUserWallNext:
                {
                    AntiPatternsDebug.limitOffset.out( "Order NEXT wall-images offset" );

                    AntiPatternsFlowWall.orderNextUserWallImages
                            (
                                    AntiPatternsState.EPivotalMenu,
                                    null,
                                    EUpdateUserWallNextLoadingNoNetwork,
                                    EUpdateUserWallNextLoadingNoNetwork
                            );
                    break;
                }

                case EUpdateExploreAreaClean:
                {
                    //reset offset and wall-entries
                    AntiPatternsFlowExplore.reset();

                    //clear explore area
                    AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EPivotalMenu.getActivity(), GridViews.EExplore).clearDataUIThreaded( AntiPatternsState.EPivotalMenu.getActivity() );

                    //update explore area
                    AntiPatternsFlowExplore.updateNextExploreImages
                            (
                                    AntiPatternsState.EPivotalMenu,
                                    AntiPatternsStatePivotalTabExplore.singleton.iOverlayIcon,
                                    AntiPatternsAction.ENone,
                                    AntiPatternsAction.ENone
                            );
                    break;
                }

                case EUpdateExploreAreaNextOffset:
                {
                    //load next explore-data
                    AntiPatternsFlowExplore.updateNextExploreImages
                            (
                                    AntiPatternsState.EPivotalMenu,
                                    null,
                                    EUpdateExploreAreaNextLoadingNoNetwork,
                                    EUpdateExploreAreaNextLoadingNoNetwork
                            );

                    break;
                }

                case EUpdateDetailedImage:
                {
                    //ditch detailed image container
                    AntiPatternsStateDetailedImage.ditchContainerUIThreaded();

                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EDetailedImage.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EUpdateDetailedImageAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EUpdateDetailedImageAfterProgressDialog:
                {
                    //update image container with the detailed image data
                    AntiPatternsFlowImageDetailed.orderDetailedImageAndUpdateDetailedImageContainer
                            (
                                    AntiPatternsState.EDetailedImage,
                                    AntiPatternsStateDetailedImage.singleton.iDetailedImageContainer
                            );
                    break;
                }

                case EUpdateOwnUserProfileFull:
                {
                    //update own user's profile
                    AntiPatternsFlowProfile.updateOwnUserProfile(true);
                    break;
                }

                case EUpdateOwnUserProfileDataOnly:
                {
                    //update own user's profile
                    AntiPatternsFlowProfile.updateOwnUserProfile(false);
                    break;
                }

                case EUpdateForeignProfile:
                {
                    //load the foreign user profile
                    AntiPatternsFlowProfile.updateForeignUserProfile();
                    break;
                }

                case EUpdateLastUserProfileClean:
                {
                    //check if the own or if a foreign user has been shown
                    if ( AntiPatternsIDM.getUserID(AntiPatternsFlowImage.lastState.getActivity()).equals( AntiPatternsFlowProfileImages.lastUserID ) )
                    {
                        //update own user
                        EUpdateOwnUserProfileFull.run();
                    }
                    else
                    {
                        //update foreign user
                        EUpdateForeignProfile.run();
                    }
                    break;
                }

                case EUpdateLastUserProfileImagesClean:
                {
                    //clear the image container
                    AntiPatternsFlowProfileImages.orderProfileImagesClean();
                    break;
                }

                case EUpdateUserProfileImagesNext:
                {
                    //load next user profile images
                    AntiPatternsFlowProfileImages.orderUsersNextProfileImages
                            (
                                    EUpdateUserProfileImagesNextLoadingNoNetwork,
                                    EUpdateUserProfileImagesNextLoadingNoNetwork
                            );
                    break;
                }

                case EUpdateImagePropertiesLikesClean:
                {
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EImageProperties.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EUpdateImagePropertiesLikesCleanAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EUpdateImagePropertiesLikesCleanAfterProgressDialog:
                {
                    AntiPatternsDebug.limitOffset.out( "Order FIRST like offset" );

                    //reset the image's likes
                    AntiPatternsFlowImage.reset();

                    //order next offset
                    AntiPatternsFlowImageLike.orderNextImageLikes
                            (
                                    AntiPatternsState.EImageProperties,
                                    AntiPatternsActionDialog.EDialogImagePropertiesNoNetwork,
                                    AntiPatternsActionDialog.EDialogImagePropertiesTechnicalError
                            );
                    break;
                }

                case EUpdateImagePropertiesLikesNextOffset:
                {
                    AntiPatternsDebug.limitOffset.out( "Order NEXT like offset" );

                    AntiPatternsFlowImageLike.orderNextImageLikes
                            (
                                    AntiPatternsState.EImageProperties,
                                    EUpdateImagePropertiesLikesNextLoadingNoNetwork,
                                    EUpdateImagePropertiesLikesNextLoadingNoNetwork
                            );
                    break;
                }

                case EUpdateImagePropertiesCommentsClean:
                {
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EImageProperties.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EUpdateImagePropertiesCommentsCleanAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EUpdateImagePropertiesCommentsCleanAfterProgressDialog:
                {
                    AntiPatternsDebug.limitOffset.out( "Order FIRST comment offset" );

                    //reset the image's comments
                    AntiPatternsFlowImage.reset();

                    //order next offset
                    AntiPatternsFlowImageComment.orderNextImageComments
                            (
                                    AntiPatternsState.EImageProperties,
                                    AntiPatternsActionDialog.EDialogImagePropertiesNoNetwork,
                                    AntiPatternsActionDialog.EDialogImagePropertiesTechnicalError
                            );
                    break;
                }

                case EUpdateImagePropertiesCommentsNextOffset:
                {
                    AntiPatternsDebug.limitOffset.out( "Order NEXT comment offset" );

                    AntiPatternsFlowImageComment.orderNextImageComments
                            (
                                    AntiPatternsState.EImageProperties,
                                    EUpdateImagePropertiesCommentsNextLoadingNoNetwork,
                                    EUpdateImagePropertiesCommentsNextLoadingNoNetwork
                            );
                    break;
                }

                case EUpdateImagePropertiesFoodRatingsClean:
                {
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EImageProperties.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EUpdateImagePropertiesFoodRatingsCleanAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EUpdateImagePropertiesFoodRatingsCleanAfterProgressDialog:
                {
                    AntiPatternsDebug.limitOffset.out( "Order FIRST ratings offset" );

                    //reset the image's ratings
                    AntiPatternsFlowImage.reset();

                    //order next offset
                    AntiPatternsFlowImageRating.orderNextImageRatings
                            (
                                    AntiPatternsState.EImageProperties,
                                    AntiPatternsActionDialog.EDialogImagePropertiesNoNetwork,
                                    AntiPatternsActionDialog.EDialogImagePropertiesTechnicalError
                            );
                    break;
                }

                case EUpdateImagePropertiesFoodRatingsNextOffset:
                {
                    AntiPatternsDebug.limitOffset.out( "Order NEXT ratings offset" );

                    AntiPatternsFlowImageRating.orderNextImageRatings
                            (
                                    AntiPatternsState.EImageProperties,
                                    EUpdateImagePropertiesRatingsNextLoadingNoNetwork,
                                    EUpdateImagePropertiesRatingsNextLoadingNoNetwork
                            );
                    break;
                }

                case EUpdateFollowersDetailsClean:
                {
                    //show 'please wait'-dialog
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EFollowDetails.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EUpdateFollowersDetailsCleanAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EUpdateFollowingsDetailsClean:
                {
                    //show 'please wait'-dialog
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        AntiPatternsState.EFollowDetails.getActivity(),
                        R.string.dialog_please_wait_title,
                        R.string.dialog_please_wait_body,
                        EUpdateFollowingsDetailsCleanAfterProgressDialog,
                        false,
                        null
                    );
                    break;
                }

                case EUpdateFollowersDetailsCleanAfterProgressDialog:
                {
                    //reset values
                    AntiPatternsFlowProfileFollow.reset(AntiPatternsFlowProfileFollow.lastUser.iFollowerCount);

                    //order initial followers
                    AntiPatternsFlowProfileFollow.orderNextFollowers
                            (
                                    AntiPatternsActionDialog.EDialogFollowDetailsNoNetwork,
                                    AntiPatternsActionDialog.EDialogFollowDetailsTechnicalError
                            );
                    break;
                }

                case EUpdateFollowersDetailsNextOffset:
                {
                    //order next followers
                    AntiPatternsFlowProfileFollow.orderNextFollowers
                            (
                                    AntiPatternsActionUpdate.EUpdateFollowersDetailsNextLoadingNoNetwork,
                                    AntiPatternsActionUpdate.EUpdateFollowersDetailsNextLoadingNoNetwork
                            );
                    break;
                }

                case EUpdateFollowingsDetailsCleanAfterProgressDialog:
                {
                    //reset values
                    AntiPatternsFlowProfileFollow.reset(AntiPatternsFlowProfileFollow.lastUser.iFollowingsCount);

                    //order initial followers
                    AntiPatternsFlowProfileFollow.orderNextFollowings
                            (
                                    AntiPatternsActionDialog.EDialogFollowDetailsNoNetwork,
                                    AntiPatternsActionDialog.EDialogFollowDetailsTechnicalError
                            );
                    break;
                }

                case EUpdateFollowingsDetailsNextOffset:
                {
                    //order next followers
                    AntiPatternsFlowProfileFollow.orderNextFollowings
                            (
                                    AntiPatternsActionUpdate.EUpdateFollowingsDetailsNextLoadingNoNetwork,
                                    AntiPatternsActionUpdate.EUpdateFollowingsDetailsNextLoadingNoNetwork
                            );
                    break;
                }

                case EUpdateImagePropertiesCommentsNextLoadingNoNetwork:
                {
                    AntiPatternsFlowImage.flow.changeNextLoadingItemToNoNetwork( AntiPatternsState.EImageProperties, GridViews.EImageProperties, EUpdateImagePropertiesCommentsNextOffset );
                    break;
                }

                case EUpdateImagePropertiesLikesNextLoadingNoNetwork:
                {
                    AntiPatternsFlowImage.flow.changeNextLoadingItemToNoNetwork( AntiPatternsState.EImageProperties, GridViews.EImageProperties, EUpdateImagePropertiesLikesNextOffset );
                    break;
                }

                case EUpdateImagePropertiesRatingsNextLoadingNoNetwork:
                {
                    AntiPatternsFlowImage.flow.changeNextLoadingItemToNoNetwork( AntiPatternsState.EImageProperties, GridViews.EImageProperties, EUpdateImagePropertiesFoodRatingsNextOffset );
                    break;
                }

                case EUpdateUserWallNextLoadingNoNetwork:
                {
                    AntiPatternsFlowWall.changeNextLoadingItemToNoNetwork(AntiPatternsState.EPivotalMenu.getActivity(), EUpdateUserWallNext);
                    break;
                }

                case EUpdateUserProfileImagesNextLoadingNoNetwork:
                {
                    AntiPatternsFlowProfileImages.changeNextLoadingItemToNoNetwork();
                    break;
                }

                case EUpdateExploreAreaNextLoadingNoNetwork:
                {
                    AntiPatternsFlowExplore.flow.changeNextLoadingItemToNoNetwork
                    (
                        AntiPatternsState.EPivotalMenu,
                        GridViews.EExplore,
                        EUpdateExploreAreaNextOffset
                    );
                    break;
                }

                case EUpdateSearchImagesResultsNext:
                {
                    //assign last actions
                    AntiPatternsFlowSearchImages.lastActionOnNoNetwork       = EUpdateSearchImagesResultsNextLoadingNoNetwork;
                    AntiPatternsFlowSearchImages.lastActionOnTechnicalError  = EUpdateSearchImagesResultsNextLoadingNoNetwork;
                    AntiPatternsFlowSearchImages.lastActionOnNoResults       = AntiPatternsAction.ENone;

                    //search images
                    AntiPatternsFlowSearchImages.orderNextSearchImages
                            (
                                    AntiPatternsState.ESearchImagesResults.getActivity(),
                                    null
                            );
                    break;
                }

                case EUpdateSearchImagesResultsClean:
                {
                    //reset existent results
                    AntiPatternsFlowSearchImages.reset();

                    //order to search images
                    AntiPatternsFlowSearchImages.orderNextSearchImages
                            (
                                    AntiPatternsState.ESearchImagesResults.getActivity(),
                                    AntiPatternsStateSearchImagesResults.singleton.iOverlayIcon
                            );
                    break;
                }

                case EUpdateSearchImagesResultsNextLoadingNoNetwork:
                {
                    AntiPatternsFlowSearchImages.changeNextLoadingItemToNoNetwork(EUpdateSearchImagesResultsNext);
                    break;
                }

                case EUpdateFollowersDetailsNextLoadingNoNetwork:
                {
                    AntiPatternsFlowProfileFollow.flow.changeNextLoadingItemToNoNetwork( AntiPatternsState.EFollowDetails, GridViews.EFollowDetails, EUpdateFollowersDetailsNextOffset );
                    break;
                }

                case EUpdateFollowingsDetailsNextLoadingNoNetwork:
                {
                    AntiPatternsFlowProfileFollow.flow.changeNextLoadingItemToNoNetwork( AntiPatternsState.EFollowDetails, GridViews.EFollowDetails, EUpdateFollowingsDetailsNextOffset );
                    break;
                }

                case EUpdateFindFriendsViaSearchTermShowResults:
                {
                    //prune the InputHandler
                    AntiPatternsFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm.destroy();

                    //show state 'find friends results' that displays these results
                    AntiPatternsStateFindFriendsResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( AntiPatternsState.ESettings.getActivity(), AntiPatternsStateFindFriendsResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EUpdateFindFriendsViaPhonenumberShowResults:
                {
                    //show state 'find friends results' that displays these results
                    AntiPatternsStateFindFriendsResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( AntiPatternsState.ESettings.getActivity(), AntiPatternsStateFindFriendsResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EUpdateFindFriendsViaFacebookShowResults:
                {
                    //show state 'find friends results' that displays these results
                    AntiPatternsStateFindFriendsResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( AntiPatternsState.ESettings.getActivity(), AntiPatternsStateFindFriendsResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EUpdateFindFriendsViaSearchTermNextOffset:
                {
                    //search via searchterm
                    AntiPatternsFlowSearchUsers.orderNextFriendsSearchViaSearchTerm
                            (
                                    AntiPatternsState.ESettings,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaSearchTermNextOffsetNoNetwork,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaSearchTermNextOffsetNoNetwork,
                                    AntiPatternsAction.ENone,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsUpdateResults
                            );
                    break;
                }

                case EUpdateFindFriendsViaSearchTermNextOffsetNoNetwork:
                {
                    AntiPatternsFlowSearchUsers.changeNextLoadingItemToNoNetwork(EUpdateFindFriendsViaSearchTermNextOffset);
                    break;
                }

                case EUpdateFindFriendsViaPhonenumberNextOffset:
                {
                    //search via searchterm
                    AntiPatternsFlowSearchUsers.orderNextFriendSearchViaPhonenumber
                            (
                                    AntiPatternsState.ESettings,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaPhonenumberNextOffsetNoNetwork,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaPhonenumberNextOffsetNoNetwork,
                                    AntiPatternsAction.ENone,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsUpdateResults
                            );
                    break;
                }

                case EUpdateFindFriendsViaPhonenumberNextOffsetNoNetwork:
                {
                    AntiPatternsFlowSearchUsers.changeNextLoadingItemToNoNetwork(EUpdateFindFriendsViaPhonenumberNextOffset);
                    break;
                }

                case EUpdateFindFriendsViaFacebookNextOffset:
                {
                    //search via searchterm
                    AntiPatternsFlowSearchUsers.orderNextFriendsSearchViaFacebookIDs
                            (
                                    AntiPatternsState.ESettings,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaFacebookNextOffsetNoNetwork,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsViaFacebookNextOffsetNoNetwork,
                                    AntiPatternsAction.ENone,
                                    AntiPatternsActionUpdate.EUpdateFindFriendsUpdateResults
                            );
                    break;
                }

                case EUpdateFindFriendsViaFacebookNextOffsetNoNetwork:
                {
                    AntiPatternsFlowSearchUsers.changeNextLoadingItemToNoNetwork(EUpdateFindFriendsViaFacebookNextOffset);
                    break;
                }

                case EUpdateFindFriendsUpdateResults:
                {
                    AntiPatternsFlowSearchUsers.updateSearchUsersResultsThreaded(null);
                    break;
                }

                case EUpdateImageListAfterImageChange:
                {
                    AntiPatternsFlowImageUpdate.updateImageListAfterImageChange();
                    break;
                }

                case EUpdateImageListAfterImageRemove:
                {
                    AntiPatternsFlowImageRemove.updateImageListAfterImageRemove();
                    break;
                }

                case EUpdateSubmitChangedProfileImage:
                {
                    //submit image to the server in order to change the profile image
                    AntiPatternsFlowProfileChange.changeProfileImage();
                    break;
                }
            }
        }
    }
