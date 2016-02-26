
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.ui.*;
    import  org.json.*;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.ui.widget.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Manages the users' profile data area.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowProfileData
    {
        /**********************************************************************************************
        *   Prunes the specified targetContainer and fills it with the specified user's profile.
        *
        *   @param  userID                  The ID of the user the profile data container shall be enriched with.
        *   @param  state                   The according state.
        *   @param  profileDataContainer    The container that holds the profile data.
        *   @param  profileImagesContainer  The container that holds the profile images.
        *   @param  parentScrollView        The parent ScrollView that holds both, the data- and the images-containers.
        *   @param  titleProfile            The TextView that holds the title for the profile-data-container.
        *   @param  titleImages             The TextView that holds the title for the profile-images-container.
        *   @param  overlayIcon             The icon ( usually a loading circle ) that's arranged above the ScrollView
        *                                   and that will display loading or no-network-information.
        *                                   May be <code>null</code> if no overlay icon shall be displayed
        *                                   while this operation is running.
        *   @param  reloadAction            The action to perform when the 'no network' icon is pushed,
        *                                   after the network connection has failed.
        *   @param  updateDataAndImageArea  Determines, if both areas ( profile data and profile images )
        *                                   shall be updated or if only the profile data shall be updated.
        **********************************************************************************************/
        public static final void updateProfileDataContainer
        (
            final   String                  userID,
            final   LibState                state,
            final   ViewGroup               profileDataContainer,
            final   ViewGroup               profileImagesContainer,
            final   LibScrollView           parentScrollView,
            final   TextView                titleProfile,
            final   TextView                titleImages,
            final   ImageView               overlayIcon,
            final   Runnable                reloadAction,
            final   boolean                 updateDataAndImageArea
        )
        {
            try
            {
                //prune bottom-reach-action to prevent it from being launched on clearing the view!
                parentScrollView.setOnVisibleAction( null, null );

                //truncate data container
                LibUI.removeAllViewsUIThreaded( state.getActivity(), profileDataContainer  );

                //truncate image container if desired
                if ( updateDataAndImageArea )
                {
                    LibUI.removeAllViewsUIThreaded( state.getActivity(), profileImagesContainer );
                }

                //hide both headlines
                LibUI.setVisibilityUIThreaded( state.getActivity(), titleProfile, View.GONE );
                LibUI.setVisibilityUIThreaded( state.getActivity(), titleImages,  View.GONE );

                //show loading circle if both areas shall be updated
                if ( updateDataAndImageArea )
                {
                    AntiPatternsLoadingCircle.showAndStartLoadingCircleUIThreaded(state.getActivity(), overlayIcon);
                }

                //get user profile information
                JSONObject response = AntiPatternsJsonRPCUser.getProfile
                        (
                                state.getActivity(),
                                userID
                        );

                //only update on correct status code
                int statusCode = Integer.parseInt( response.getString( "status" ) );
                switch ( statusCode )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //update own or foreign user's profile
                        final AntiPatternsDataUser userToDisplay = new AntiPatternsDataUser( response );
                        if ( AntiPatternsIDM.getUserID(state.getActivity()).equals( userToDisplay.iUserID ) )
                        {
                            AntiPatternsFlowProfile.setOwnUser(userToDisplay);
                        }
                        else
                        {
                            AntiPatternsFlowProfile.setForeignUser(userToDisplay);
                        }

                        //pick profile container from newUser
                        final View profileView = userToDisplay.createProfileView( state.getActivity() );

                        //remove loading-circle and show profile-view and title
                        AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(state.getActivity(), overlayIcon);
                        LibUI.addViewUIThreaded(        state.getActivity(), profileDataContainer, profileView );
                        LibUI.setVisibilityUIThreaded(  state.getActivity(), titleProfile, View.VISIBLE );

                        //order current profile-image
                        userToDisplay.orderImageThreaded( state );

                        //check if this user has uploaded images
                        if ( userToDisplay.iImageCount > 0 && updateDataAndImageArea )
                        {
                            //order profile's uploaded images in a separate thread
                            new Thread()
                            {
                                @Override
                                public void run()
                                {
                                    //assign initial data for user's profile images
                                    AntiPatternsFlowProfileImages.lastState                   = state;
                                    AntiPatternsFlowProfileImages.lastUserID                     = userToDisplay.iUserID;
                                    AntiPatternsFlowProfileImages.lastImageCount                 = userToDisplay.iImageCount;
                                    AntiPatternsFlowProfileImages.lastProfileImagesContainer     = profileImagesContainer;
                                    AntiPatternsFlowProfileImages.lastParentScrollView           = parentScrollView;
                                    AntiPatternsFlowProfileImages.lastTitleImages                = titleImages;

                                    //order profile-images clean
                                    AntiPatternsFlowProfileImages.orderProfileImagesClean();
                                }
                            }.start();
                        }
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //remove loading circle
                        AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(state.getActivity(), overlayIcon);

                        //report this error
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);

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
                        //remove loading-circle
                        AntiPatternsLoadingCircle.removeLoadingCircleUIThreaded(state.getActivity(), overlayIcon);

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //show 'no network' icon and offer reload
                    AntiPatternsLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded(state.getActivity(), overlayIcon, reloadAction);
                }
                else
                {
                    //show 'no network' icon and offer reload
                    AntiPatternsLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded(state.getActivity(), overlayIcon, reloadAction);

                    //remove loading-circle
                    //PicFoodLoadingCircle.removeLoadingCircleUIThreaded( activity, overlayIcon );

                    //ignore this error - The profile will just not get updated!
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "Caught on updating user profile", UncaughtException.ENo);
                }
            }
        }

        /**********************************************************************************************
        *   Requests the current active user's profile data and reassigns it.
        *   This is used in order to update the current user's profile after it has been changed.
        *
        *   @param  state                   The according state.
        **********************************************************************************************/
        public static final void updateOwnProfileDataOnly( AntiPatternsState state )
        {
            try
            {
                //get user profile information
                JSONObject response = AntiPatternsJsonRPCUser.getProfile
                        (
                                state.getActivity(),
                                AntiPatternsIDM.getUserID(state.getActivity())
                        );

                //only update on correct status code
                int statusCode = Integer.parseInt( response.getString( "status" ) );
                switch ( statusCode )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //update own user's profile
                        final AntiPatternsDataUser user = new AntiPatternsDataUser( response );
                        AntiPatternsFlowProfile.setOwnUser(user);

                        //setup dialog-fields in dialog 'change profile'
                        AntiPatternsFlowProfileChange.inputHandlerPhone.setText(     user.iPhone         );
                        AntiPatternsFlowProfileChange.inputHandlerRealName.setText(  user.iRealName      );
                        AntiPatternsFlowProfileChange.inputHandlerBiography.setText( user.iBiography     );
                        AntiPatternsFlowProfileChange.inputHandlerWebsite.setText(   user.iWebsite       );

                        //dismiss progress dialog and show 'change profile'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        AntiPatternsActionDialog.EDialogChangeProfile.run();

                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        AntiPatternsActionDialog.EDialogChangeProfilePickProfileTechnicalError.run();
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
                    AntiPatternsActionDialog.EDialogChangeProfilePickProfileNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogChangeProfilePickProfileTechnicalError.run();
                }
            }
        }
    }
