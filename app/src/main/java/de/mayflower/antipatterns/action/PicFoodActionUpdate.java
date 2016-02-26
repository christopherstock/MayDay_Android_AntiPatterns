
    package de.mayflower.antipatterns.action;

    import de.mayflower.antipatterns.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.GridViews;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public enum PicFoodActionUpdate implements Runnable
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
                case EUpdateUserWallClean:
                {
                    //reset offset and wall-entries
                    PicFoodFlowWall.reset();

                    //clear user wall
                    //PicFoodAdapterManager.getSingleton( PicFoodState.EPivotalMenu.getActivity(), GridViews.EUserWall ).clearDataUIThreaded( PicFoodState.EPivotalMenu.getActivity() );

                    //update user's wall
                    PicFoodFlowWall.orderNextUserWallImages
                    (
                        PicFoodState.EPivotalMenu,
                        PicFoodStatePivotalTabWall.singleton.iOverlayIcon,
                        PicFoodAction.ENone,
                        PicFoodAction.ENone
                    );
                    break;
                }

                case EUpdateUserWallNext:
                {
                    PicFoodDebug.limitOffset.out( "Order NEXT wall-images offset" );

                    PicFoodFlowWall.orderNextUserWallImages
                    (
                        PicFoodState.EPivotalMenu,
                        null,
                        EUpdateUserWallNextLoadingNoNetwork,
                        EUpdateUserWallNextLoadingNoNetwork
                    );
                    break;
                }

                case EUpdateExploreAreaClean:
                {
                    //reset offset and wall-entries
                    PicFoodFlowExplore.reset();

                    //clear explore area
                    PicFoodAdapterManager.getSingleton( PicFoodState.EPivotalMenu.getActivity(), GridViews.EExplore ).clearDataUIThreaded( PicFoodState.EPivotalMenu.getActivity() );

                    //update explore area
                    PicFoodFlowExplore.updateNextExploreImages
                    (
                        PicFoodState.EPivotalMenu,
                        PicFoodStatePivotalTabExplore.singleton.iOverlayIcon,
                        PicFoodAction.ENone,
                        PicFoodAction.ENone
                    );
                    break;
                }

                case EUpdateExploreAreaNextOffset:
                {
                    //load next explore-data
                    PicFoodFlowExplore.updateNextExploreImages
                    (
                        PicFoodState.EPivotalMenu,
                        null,
                        EUpdateExploreAreaNextLoadingNoNetwork,
                        EUpdateExploreAreaNextLoadingNoNetwork
                    );

                    break;
                }

                case EUpdateDetailedImage:
                {
                    //ditch detailed image container
                    PicFoodStateDetailedImage.ditchContainerUIThreaded();

                    //show 'please wait'
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.EDetailedImage.getActivity(),
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
                    PicFoodFlowImageDetailed.orderDetailedImageAndUpdateDetailedImageContainer
                    (
                        PicFoodState.EDetailedImage,
                        PicFoodStateDetailedImage.singleton.iDetailedImageContainer
                    );
                    break;
                }

                case EUpdateOwnUserProfileFull:
                {
                    //update own user's profile
                    PicFoodFlowProfile.updateOwnUserProfile( true );
                    break;
                }

                case EUpdateOwnUserProfileDataOnly:
                {
                    //update own user's profile
                    PicFoodFlowProfile.updateOwnUserProfile( false );
                    break;
                }

                case EUpdateForeignProfile:
                {
                    //load the foreign user profile
                    PicFoodFlowProfile.updateForeignUserProfile();
                    break;
                }

                case EUpdateLastUserProfileClean:
                {
                    //check if the own or if a foreign user has been shown
                    if ( PicFoodIDM.getUserID( PicFoodFlowImage.lastState.getActivity() ).equals( PicFoodFlowProfileImages.lastUserID ) )
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
                    PicFoodFlowProfileImages.orderProfileImagesClean();
                    break;
                }

                case EUpdateUserProfileImagesNext:
                {
                    //load next user profile images
                    PicFoodFlowProfileImages.orderUsersNextProfileImages
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
                        PicFoodState.EImageProperties.getActivity(),
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
                    PicFoodDebug.limitOffset.out( "Order FIRST like offset" );

                    //reset the image's likes
                    PicFoodFlowImage.reset();

                    //order next offset
                    PicFoodFlowImageLike.orderNextImageLikes
                    (
                        PicFoodState.EImageProperties,
                        PicFoodActionDialog.EDialogImagePropertiesNoNetwork,
                        PicFoodActionDialog.EDialogImagePropertiesTechnicalError
                    );
                    break;
                }

                case EUpdateImagePropertiesLikesNextOffset:
                {
                    PicFoodDebug.limitOffset.out( "Order NEXT like offset" );

                    PicFoodFlowImageLike.orderNextImageLikes
                    (
                        PicFoodState.EImageProperties,
                        EUpdateImagePropertiesLikesNextLoadingNoNetwork,
                        EUpdateImagePropertiesLikesNextLoadingNoNetwork
                    );
                    break;
                }

                case EUpdateImagePropertiesCommentsClean:
                {
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.EImageProperties.getActivity(),
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
                    PicFoodDebug.limitOffset.out( "Order FIRST comment offset" );

                    //reset the image's comments
                    PicFoodFlowImage.reset();

                    //order next offset
                    PicFoodFlowImageComment.orderNextImageComments
                    (
                        PicFoodState.EImageProperties,
                        PicFoodActionDialog.EDialogImagePropertiesNoNetwork,
                        PicFoodActionDialog.EDialogImagePropertiesTechnicalError
                    );
                    break;
                }

                case EUpdateImagePropertiesCommentsNextOffset:
                {
                    PicFoodDebug.limitOffset.out( "Order NEXT comment offset" );

                    PicFoodFlowImageComment.orderNextImageComments
                    (
                        PicFoodState.EImageProperties,
                        EUpdateImagePropertiesCommentsNextLoadingNoNetwork,
                        EUpdateImagePropertiesCommentsNextLoadingNoNetwork
                    );
                    break;
                }

                case EUpdateImagePropertiesFoodRatingsClean:
                {
                    LibDialogProgress.showProgressDialogUIThreaded
                    (
                        PicFoodState.EImageProperties.getActivity(),
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
                    PicFoodDebug.limitOffset.out( "Order FIRST ratings offset" );

                    //reset the image's ratings
                    PicFoodFlowImage.reset();

                    //order next offset
                    PicFoodFlowImageRating.orderNextImageRatings
                    (
                        PicFoodState.EImageProperties,
                        PicFoodActionDialog.EDialogImagePropertiesNoNetwork,
                        PicFoodActionDialog.EDialogImagePropertiesTechnicalError
                    );
                    break;
                }

                case EUpdateImagePropertiesFoodRatingsNextOffset:
                {
                    PicFoodDebug.limitOffset.out( "Order NEXT ratings offset" );

                    PicFoodFlowImageRating.orderNextImageRatings
                    (
                        PicFoodState.EImageProperties,
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
                        PicFoodState.EFollowDetails.getActivity(),
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
                        PicFoodState.EFollowDetails.getActivity(),
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
                    PicFoodFlowProfileFollow.reset( PicFoodFlowProfileFollow.lastUser.iFollowerCount );

                    //order initial followers
                    PicFoodFlowProfileFollow.orderNextFollowers
                    (
                        PicFoodActionDialog.EDialogFollowDetailsNoNetwork,
                        PicFoodActionDialog.EDialogFollowDetailsTechnicalError
                    );
                    break;
                }

                case EUpdateFollowersDetailsNextOffset:
                {
                    //order next followers
                    PicFoodFlowProfileFollow.orderNextFollowers
                    (
                        PicFoodActionUpdate.EUpdateFollowersDetailsNextLoadingNoNetwork,
                        PicFoodActionUpdate.EUpdateFollowersDetailsNextLoadingNoNetwork
                    );
                    break;
                }

                case EUpdateFollowingsDetailsCleanAfterProgressDialog:
                {
                    //reset values
                    PicFoodFlowProfileFollow.reset( PicFoodFlowProfileFollow.lastUser.iFollowingsCount );

                    //order initial followers
                    PicFoodFlowProfileFollow.orderNextFollowings
                    (
                        PicFoodActionDialog.EDialogFollowDetailsNoNetwork,
                        PicFoodActionDialog.EDialogFollowDetailsTechnicalError
                    );
                    break;
                }

                case EUpdateFollowingsDetailsNextOffset:
                {
                    //order next followers
                    PicFoodFlowProfileFollow.orderNextFollowings
                    (
                        PicFoodActionUpdate.EUpdateFollowingsDetailsNextLoadingNoNetwork,
                        PicFoodActionUpdate.EUpdateFollowingsDetailsNextLoadingNoNetwork
                    );
                    break;
                }

                case EUpdateImagePropertiesCommentsNextLoadingNoNetwork:
                {
                    PicFoodFlowImage.flow.changeNextLoadingItemToNoNetwork( PicFoodState.EImageProperties, GridViews.EImageProperties, EUpdateImagePropertiesCommentsNextOffset );
                    break;
                }

                case EUpdateImagePropertiesLikesNextLoadingNoNetwork:
                {
                    PicFoodFlowImage.flow.changeNextLoadingItemToNoNetwork( PicFoodState.EImageProperties, GridViews.EImageProperties, EUpdateImagePropertiesLikesNextOffset );
                    break;
                }

                case EUpdateImagePropertiesRatingsNextLoadingNoNetwork:
                {
                    PicFoodFlowImage.flow.changeNextLoadingItemToNoNetwork( PicFoodState.EImageProperties, GridViews.EImageProperties, EUpdateImagePropertiesFoodRatingsNextOffset );
                    break;
                }

                case EUpdateUserWallNextLoadingNoNetwork:
                {
                    PicFoodFlowWall.changeNextLoadingItemToNoNetwork( PicFoodState.EPivotalMenu.getActivity(), EUpdateUserWallNext );
                    break;
                }

                case EUpdateUserProfileImagesNextLoadingNoNetwork:
                {
                    PicFoodFlowProfileImages.changeNextLoadingItemToNoNetwork();
                    break;
                }

                case EUpdateExploreAreaNextLoadingNoNetwork:
                {
                    PicFoodFlowExplore.flow.changeNextLoadingItemToNoNetwork
                    (
                        PicFoodState.EPivotalMenu,
                        GridViews.EExplore,
                        EUpdateExploreAreaNextOffset
                    );
                    break;
                }

                case EUpdateSearchImagesResultsNext:
                {
                    //assign last actions
                    PicFoodFlowSearchImages.lastActionOnNoNetwork       = EUpdateSearchImagesResultsNextLoadingNoNetwork;
                    PicFoodFlowSearchImages.lastActionOnTechnicalError  = EUpdateSearchImagesResultsNextLoadingNoNetwork;
                    PicFoodFlowSearchImages.lastActionOnNoResults       = PicFoodAction.ENone;

                    //search images
                    PicFoodFlowSearchImages.orderNextSearchImages
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        null
                    );
                    break;
                }

                case EUpdateSearchImagesResultsClean:
                {
                    //reset existent results
                    PicFoodFlowSearchImages.reset();

                    //order to search images
                    PicFoodFlowSearchImages.orderNextSearchImages
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        PicFoodStateSearchImagesResults.singleton.iOverlayIcon
                    );
                    break;
                }

                case EUpdateSearchImagesResultsNextLoadingNoNetwork:
                {
                    PicFoodFlowSearchImages.changeNextLoadingItemToNoNetwork( EUpdateSearchImagesResultsNext );
                    break;
                }

                case EUpdateFollowersDetailsNextLoadingNoNetwork:
                {
                    PicFoodFlowProfileFollow.flow.changeNextLoadingItemToNoNetwork( PicFoodState.EFollowDetails, GridViews.EFollowDetails, EUpdateFollowersDetailsNextOffset );
                    break;
                }

                case EUpdateFollowingsDetailsNextLoadingNoNetwork:
                {
                    PicFoodFlowProfileFollow.flow.changeNextLoadingItemToNoNetwork( PicFoodState.EFollowDetails, GridViews.EFollowDetails, EUpdateFollowingsDetailsNextOffset );
                    break;
                }

                case EUpdateFindFriendsViaSearchTermShowResults:
                {
                    //prune the InputHandler
                    PicFoodFlowSearchUsers.inputHandlerFindFriendsViaSearchTerm.destroy();

                    //show state 'find friends results' that displays these results
                    PicFoodStateFindFriendsResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( PicFoodState.ESettings.getActivity(), PicFoodStateFindFriendsResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EUpdateFindFriendsViaPhonenumberShowResults:
                {
                    //show state 'find friends results' that displays these results
                    PicFoodStateFindFriendsResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( PicFoodState.ESettings.getActivity(), PicFoodStateFindFriendsResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EUpdateFindFriendsViaFacebookShowResults:
                {
                    //show state 'find friends results' that displays these results
                    PicFoodStateFindFriendsResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( PicFoodState.ESettings.getActivity(), PicFoodStateFindFriendsResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EUpdateFindFriendsViaSearchTermNextOffset:
                {
                    //search via searchterm
                    PicFoodFlowSearchUsers.orderNextFriendsSearchViaSearchTerm
                    (
                        PicFoodState.ESettings,
                        PicFoodActionUpdate.EUpdateFindFriendsViaSearchTermNextOffsetNoNetwork,
                        PicFoodActionUpdate.EUpdateFindFriendsViaSearchTermNextOffsetNoNetwork,
                        PicFoodAction.ENone,
                        PicFoodActionUpdate.EUpdateFindFriendsUpdateResults
                    );
                    break;
                }

                case EUpdateFindFriendsViaSearchTermNextOffsetNoNetwork:
                {
                    PicFoodFlowSearchUsers.changeNextLoadingItemToNoNetwork( EUpdateFindFriendsViaSearchTermNextOffset );
                    break;
                }

                case EUpdateFindFriendsViaPhonenumberNextOffset:
                {
                    //search via searchterm
                    PicFoodFlowSearchUsers.orderNextFriendSearchViaPhonenumber
                    (
                        PicFoodState.ESettings,
                        PicFoodActionUpdate.EUpdateFindFriendsViaPhonenumberNextOffsetNoNetwork,
                        PicFoodActionUpdate.EUpdateFindFriendsViaPhonenumberNextOffsetNoNetwork,
                        PicFoodAction.ENone,
                        PicFoodActionUpdate.EUpdateFindFriendsUpdateResults
                    );
                    break;
                }

                case EUpdateFindFriendsViaPhonenumberNextOffsetNoNetwork:
                {
                    PicFoodFlowSearchUsers.changeNextLoadingItemToNoNetwork( EUpdateFindFriendsViaPhonenumberNextOffset );
                    break;
                }

                case EUpdateFindFriendsViaFacebookNextOffset:
                {
                    //search via searchterm
                    PicFoodFlowSearchUsers.orderNextFriendsSearchViaFacebookIDs
                    (
                        PicFoodState.ESettings,
                        PicFoodActionUpdate.EUpdateFindFriendsViaFacebookNextOffsetNoNetwork,
                        PicFoodActionUpdate.EUpdateFindFriendsViaFacebookNextOffsetNoNetwork,
                        PicFoodAction.ENone,
                        PicFoodActionUpdate.EUpdateFindFriendsUpdateResults
                    );
                    break;
                }

                case EUpdateFindFriendsViaFacebookNextOffsetNoNetwork:
                {
                    PicFoodFlowSearchUsers.changeNextLoadingItemToNoNetwork( EUpdateFindFriendsViaFacebookNextOffset );
                    break;
                }

                case EUpdateFindFriendsUpdateResults:
                {
                    PicFoodFlowSearchUsers.updateSearchUsersResultsThreaded( null );
                    break;
                }

                case EUpdateImageListAfterImageChange:
                {
                    PicFoodFlowImageUpdate.updateImageListAfterImageChange();
                    break;
                }

                case EUpdateImageListAfterImageRemove:
                {
                    PicFoodFlowImageRemove.updateImageListAfterImageRemove();
                    break;
                }

                case EUpdateSubmitChangedProfileImage:
                {
                    //submit image to the server in order to change the profile image
                    PicFoodFlowProfileChange.changeProfileImage();
                    break;
                }
            }
        }
    }
