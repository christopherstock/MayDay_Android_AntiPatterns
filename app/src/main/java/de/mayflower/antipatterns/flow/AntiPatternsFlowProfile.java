
    package de.mayflower.antipatterns.flow;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import  org.json.*;
    import  android.app.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Manages the users' profiles.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowProfile
    {
        /**********************************************************************************************
        *   The different cases for ordering profile data for.
        **********************************************************************************************/
        public static enum ProfileDetailsEnteredVia
        {
            /**********************************************************************************************
            *   Profile data has previously been ordered for the active user's profile.
            **********************************************************************************************/
            EOwnProfile,

            /**********************************************************************************************
            *   Profile data has previously been ordered for a foreign user's profile.
            **********************************************************************************************/
            EForeignProfile,

            ;
        }

        /**********************************************************************************************
        *   Specifies if user profile details have previously been ordered for the own
        *   or for a foreign user's profile.
        **********************************************************************************************/
        public          static          ProfileDetailsEnteredVia    profileDetailsEnteredVia    = null;

        /** A reference to the current active user. */
        private         static AntiPatternsDataUser ownUser                     = null;

        /** A reference to the last foreign user where user-profile-data has been ordered. */
        private         static AntiPatternsDataUser lastForeignUser             = null;

        /**********************************************************************************************
        *   Returns the profile of the current logged in user.
        *
        *   @return     The last user that has been shown in state 'own profile'.
        **********************************************************************************************/
        public static final AntiPatternsDataUser getOwnUser()
        {
            return ownUser;
        }

        /**********************************************************************************************
        *   Returns the profile of the last foreign user.
        *
        *   @return     The last user that has been shown in state 'foreign user'.
        **********************************************************************************************/
        public static final AntiPatternsDataUser getLastForeignUser()
        {
            return lastForeignUser;
        }

        /**********************************************************************************************
        *   Updates the reference to the user that is currently logged in.
        *
        *   @param  user    The user to assign as the current active user.
        **********************************************************************************************/
        public static final void setOwnUser( AntiPatternsDataUser user )
        {
            ownUser = user;
        }

        /**********************************************************************************************
        *   Updates the reference to the foreign user that's profile shall currently be displayed.
        *
        *   @param  user    The user to assign as the current foreign user.
        **********************************************************************************************/
        public static final void setForeignUser( AntiPatternsDataUser user )
        {
            lastForeignUser = user;
        }

        /**********************************************************************************************
        *   Being invoked when the user clicks on a profile link.
        *
        *   @param  activity    The according activity context.
        *   @param  user        The user data to show the profile for.
        **********************************************************************************************/
        public static final void showUserProfile( Activity activity, AntiPatternsDataUser user )
        {
            AntiPatternsDebug.profile.out( "Show selected user profile userID [" + user.iUserID + "]" );

            //check if user to show is current logged in user
            if ( AntiPatternsIDM.getUserID(activity).equals( user.iUserID ) )
            {
                AntiPatternsDebug.profile.out( "This user is yourself! Change to 'pivotal menu' and show 'profile' tab!" );

                //reset own profile scrolling if already set up
                if ( AntiPatternsStatePivotalTabProfile.singleton != null )
                {
                    LibUI.resetScrollViewScrollingUIThreaded( activity, AntiPatternsStatePivotalTabProfile.singleton.iScrollView );
                }

                //show state 'own profile'
                AntiPatternsStatePivotal.changeToTabUIThreaded(activity, AntiPatternsStatePivotal.TAB_TAG_PROFILE);
                LibLauncher.launchActivity( activity, AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
            }
            else
            {
                AntiPatternsDebug.profile.out( "This is a foreign user! Show him in a separate state!" );

                //remember foreign user-id
                lastForeignUser = user;

                //show state 'foreign profile'
                AntiPatternsStateForeignProfile.setupProfileNextOnStart = true;
                LibLauncher.launchActivity( activity, AntiPatternsStateForeignProfile.class, R.anim.fade_in, R.anim.fade_out );
            }
        }

        /**********************************************************************************************
        *   Updates the user's profile by fetching current data.
        *
        *   @param  updateImageArea     Determines, if the profile-images shall be updated too,
        *                               after the profile-data has been updated.
        **********************************************************************************************/
        public static final void updateOwnUserProfile( boolean updateImageArea )
        {
            AntiPatternsDebug.profile.out( AntiPatternsFlowProfile.class + "::updateUserProfile() [" + AntiPatternsStatePivotalTabProfile.singleton + "]" );

            //update own user's profile container
            AntiPatternsFlowProfileData.updateProfileDataContainer
                    (
                            AntiPatternsIDM.getUserID(AntiPatternsState.EPivotalMenu.getActivity()),
                            AntiPatternsState.EPivotalMenu,
                            AntiPatternsStatePivotalTabProfile.singleton.iContainerProfileData,
                            AntiPatternsStatePivotalTabProfile.singleton.iContainerProfileImages,
                            AntiPatternsStatePivotalTabProfile.singleton.iScrollView,
                            AntiPatternsStatePivotalTabProfile.singleton.iTitleProfile,
                            AntiPatternsStatePivotalTabProfile.singleton.iTitleImages,
                            AntiPatternsStatePivotalTabProfile.singleton.iOverlayIcon,
                            AntiPatternsActionUpdate.EUpdateOwnUserProfileFull,
                            updateImageArea
                    );
        }

        /**********************************************************************************************
        *   Updates the foreign user's profile by fetching current data.
        **********************************************************************************************/
        public static final void updateForeignUserProfile()
        {
            AntiPatternsDebug.profile.out( AntiPatternsFlowProfile.class + "::updateForeignUserProfile() [" + AntiPatternsState.EForeignProfile.getActivity() + "]" );

            //update profile container with the foreign user's profile
            AntiPatternsFlowProfileData.updateProfileDataContainer
                    (
                            lastForeignUser.iUserID,
                            AntiPatternsState.EForeignProfile,
                            AntiPatternsStateForeignProfile.singleton.iContainerProfileData,
                            AntiPatternsStateForeignProfile.singleton.iContainerProfileImages,
                            AntiPatternsStateForeignProfile.singleton.iScrollView,
                            AntiPatternsStateForeignProfile.singleton.iTitleProfile,
                            AntiPatternsStateForeignProfile.singleton.iTitleImages,
                            AntiPatternsStateForeignProfile.singleton.iOverlayIcon,
                            AntiPatternsActionUpdate.EUpdateForeignProfile,
                            true
                    );
        }

        /**********************************************************************************************
        *   Toggles the followship for the current foreign user profile.
        **********************************************************************************************/
        public static final void toggleFollowship()
        {
            AntiPatternsDebug.followship.out( AntiPatternsFlowProfile.class + "::toggleFollowship() [" + AntiPatternsState.EForeignProfile.getActivity() + "] [" + lastForeignUser.iFollow + "]" );

            try
            {
                //get latest profile information
                boolean     newFollowValue  = !( lastForeignUser.iFollow );
                String      foreignUserID   = lastForeignUser.iUserID;
                JSONObject  response        = AntiPatternsJsonRPCUser.setFollowing
                        (
                                AntiPatternsState.EForeignProfile.getActivity(),
                                foreignUserID,
                                newFollowValue
                        );

                //only update on correct status code
                int statusCode = Integer.parseInt( response.getString( "status" ) );
                switch ( statusCode )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        AntiPatternsDebug.followship.out( "Toggled follow status for foreign user successfully!" );

                        //delete wall- and profile-cache
                        AntiPatternsStatePivotalTabWall.resetLastUpdate();
                        AntiPatternsStatePivotalTabProfile.resetLastUpdate();

                        //dismiss progress dialog and update foreign user's profile container
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                        AntiPatternsActionUpdate.EUpdateForeignProfile.run();

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(AntiPatternsState.EForeignProfile);
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                        AntiPatternsActionDialog.EDialogToggleFollowshipTechnicalError.run();

                        //report this exception
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                    AntiPatternsActionDialog.EDialogToggleFollowshipNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                    AntiPatternsActionDialog.EDialogToggleFollowshipTechnicalError.run();

                    //report this exception
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "Throwable raised on toggling followship.", UncaughtException.ENo);
                }
            }
        }

        /**********************************************************************************************
        *   Sets the block for the requested user.
        *   Blocks for other users can only be set and not be unset so far.
        **********************************************************************************************/
        public static final void setBlock()
        {
            AntiPatternsDebug.followship.out( AntiPatternsFlowProfile.class + "::setBlock() [" + AntiPatternsState.EForeignProfile.getActivity() + "] [" + lastForeignUser + "]" );

            try
            {
                String      foreignUserID   = lastForeignUser.iUserID;
                JSONObject  response        = AntiPatternsJsonRPCUser.setBlockUser
                        (
                                AntiPatternsState.EForeignProfile.getActivity(),
                                foreignUserID,
                                true
                        );

                //only update on correct status code
                int statusCode = Integer.parseInt( response.getString( "status" ) );
                switch ( statusCode )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog and return to pivotal menu
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                        AntiPatternsActionState.ELeaveForeignProfile.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(AntiPatternsState.EForeignProfile);
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                        AntiPatternsActionDialog.EDialogSetBlockTechnicalError.run();

                        //report this exception
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                    AntiPatternsActionDialog.EDialogSetBlockNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                    AntiPatternsActionDialog.EDialogSetBlockTechnicalError.run();

                    //report this exception
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "Throwable raised on toggling followship.", UncaughtException.ENo);
                }
            }
        }
    }
