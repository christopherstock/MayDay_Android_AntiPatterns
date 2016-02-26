/*  $Id: PicFoodFlowProfile.java 50564 2013-08-12 11:44:08Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.data.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.jsonrpc.*;
    import  net.picfood.state.*;
    import  net.picfood.state.pivotal.*;
    import  org.json.*;
    import  android.app.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Manages the users' profiles.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50564 $ $Date: 2013-08-12 13:44:08 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowProfile.java $"
    **********************************************************************************************/
    public class PicFoodFlowProfile
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
        private         static          PicFoodDataUser             ownUser                     = null;

        /** A reference to the last foreign user where user-profile-data has been ordered. */
        private         static          PicFoodDataUser             lastForeignUser             = null;

        /**********************************************************************************************
        *   Returns the profile of the current logged in user.
        *
        *   @return     The last user that has been shown in state 'own profile'.
        **********************************************************************************************/
        public static final PicFoodDataUser getOwnUser()
        {
            return ownUser;
        }

        /**********************************************************************************************
        *   Returns the profile of the last foreign user.
        *
        *   @return     The last user that has been shown in state 'foreign user'.
        **********************************************************************************************/
        public static final PicFoodDataUser getLastForeignUser()
        {
            return lastForeignUser;
        }

        /**********************************************************************************************
        *   Updates the reference to the user that is currently logged in.
        *
        *   @param  user    The user to assign as the current active user.
        **********************************************************************************************/
        public static final void setOwnUser( PicFoodDataUser user )
        {
            ownUser = user;
        }

        /**********************************************************************************************
        *   Updates the reference to the foreign user that's profile shall currently be displayed.
        *
        *   @param  user    The user to assign as the current foreign user.
        **********************************************************************************************/
        public static final void setForeignUser( PicFoodDataUser user )
        {
            lastForeignUser = user;
        }

        /**********************************************************************************************
        *   Being invoked when the user clicks on a profile link.
        *
        *   @param  activity    The according activity context.
        *   @param  user        The user data to show the profile for.
        **********************************************************************************************/
        public static final void showUserProfile( Activity activity, PicFoodDataUser user )
        {
            PicFoodDebug.profile.out( "Show selected user profile userID [" + user.iUserID + "]" );

            //check if user to show is current logged in user
            if ( PicFoodIDM.getUserID( activity ).equals( user.iUserID ) )
            {
                PicFoodDebug.profile.out( "This user is yourself! Change to 'pivotal menu' and show 'profile' tab!" );

                //reset own profile scrolling if already set up
                if ( PicFoodStatePivotalTabProfile.singleton != null )
                {
                    LibUI.resetScrollViewScrollingUIThreaded( activity, PicFoodStatePivotalTabProfile.singleton.iScrollView );
                }

                //show state 'own profile'
                PicFoodStatePivotal.changeToTabUIThreaded( activity, PicFoodStatePivotal.TAB_TAG_PROFILE );
                LibLauncher.launchActivity( activity, PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
            }
            else
            {
                PicFoodDebug.profile.out( "This is a foreign user! Show him in a separate state!" );

                //remember foreign user-id
                lastForeignUser = user;

                //show state 'foreign profile'
                PicFoodStateForeignProfile.setupProfileNextOnStart = true;
                LibLauncher.launchActivity( activity, PicFoodStateForeignProfile.class, R.anim.fade_in, R.anim.fade_out );
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
            PicFoodDebug.profile.out( PicFoodFlowProfile.class + "::updateUserProfile() [" + PicFoodStatePivotalTabProfile.singleton + "]" );

            //update own user's profile container
            PicFoodFlowProfileData.updateProfileDataContainer
            (
                PicFoodIDM.getUserID( PicFoodState.EPivotalMenu.getActivity() ),
                PicFoodState.EPivotalMenu,
                PicFoodStatePivotalTabProfile.singleton.iContainerProfileData,
                PicFoodStatePivotalTabProfile.singleton.iContainerProfileImages,
                PicFoodStatePivotalTabProfile.singleton.iScrollView,
                PicFoodStatePivotalTabProfile.singleton.iTitleProfile,
                PicFoodStatePivotalTabProfile.singleton.iTitleImages,
                PicFoodStatePivotalTabProfile.singleton.iOverlayIcon,
                PicFoodActionUpdate.EUpdateOwnUserProfileFull,
                updateImageArea
            );
        }

        /**********************************************************************************************
        *   Updates the foreign user's profile by fetching current data.
        **********************************************************************************************/
        public static final void updateForeignUserProfile()
        {
            PicFoodDebug.profile.out( PicFoodFlowProfile.class + "::updateForeignUserProfile() [" + PicFoodState.EForeignProfile.getActivity() + "]" );

            //update profile container with the foreign user's profile
            PicFoodFlowProfileData.updateProfileDataContainer
            (
                lastForeignUser.iUserID,
                PicFoodState.EForeignProfile,
                PicFoodStateForeignProfile.singleton.iContainerProfileData,
                PicFoodStateForeignProfile.singleton.iContainerProfileImages,
                PicFoodStateForeignProfile.singleton.iScrollView,
                PicFoodStateForeignProfile.singleton.iTitleProfile,
                PicFoodStateForeignProfile.singleton.iTitleImages,
                PicFoodStateForeignProfile.singleton.iOverlayIcon,
                PicFoodActionUpdate.EUpdateForeignProfile,
                true
            );
        }

        /**********************************************************************************************
        *   Toggles the followship for the current foreign user profile.
        **********************************************************************************************/
        public static final void toggleFollowship()
        {
            PicFoodDebug.followship.out( PicFoodFlowProfile.class + "::toggleFollowship() [" + PicFoodState.EForeignProfile.getActivity() + "] [" + lastForeignUser.iFollow + "]" );

            try
            {
                //get latest profile information
                boolean     newFollowValue  = !( lastForeignUser.iFollow );
                String      foreignUserID   = lastForeignUser.iUserID;
                JSONObject  response        = PicFoodJsonRPCUser.setFollowing
                (
                    PicFoodState.EForeignProfile.getActivity(),
                    foreignUserID,
                    newFollowValue
                );

                //only update on correct status code
                int statusCode = Integer.parseInt( response.getString( "status" ) );
                switch ( statusCode )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        PicFoodDebug.followship.out( "Toggled follow status for foreign user successfully!" );

                        //delete wall- and profile-cache
                        PicFoodStatePivotalTabWall.resetLastUpdate();
                        PicFoodStatePivotalTabProfile.resetLastUpdate();

                        //dismiss progress dialog and update foreign user's profile container
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                        PicFoodActionUpdate.EUpdateForeignProfile.run();

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.EForeignProfile );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                        PicFoodActionDialog.EDialogToggleFollowshipTechnicalError.run();

                        //report this exception
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                    PicFoodActionDialog.EDialogToggleFollowshipNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                    PicFoodActionDialog.EDialogToggleFollowshipTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "Throwable raised on toggling followship.", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Sets the block for the requested user.
        *   Blocks for other users can only be set and not be unset so far.
        **********************************************************************************************/
        public static final void setBlock()
        {
            PicFoodDebug.followship.out( PicFoodFlowProfile.class + "::setBlock() [" + PicFoodState.EForeignProfile.getActivity() + "] [" + lastForeignUser + "]" );

            try
            {
                String      foreignUserID   = lastForeignUser.iUserID;
                JSONObject  response        = PicFoodJsonRPCUser.setBlockUser
                (
                    PicFoodState.EForeignProfile.getActivity(),
                    foreignUserID,
                    true
                );

                //only update on correct status code
                int statusCode = Integer.parseInt( response.getString( "status" ) );
                switch ( statusCode )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog and return to pivotal menu
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                        PicFoodActionState.ELeaveForeignProfile.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.EForeignProfile );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                        PicFoodActionDialog.EDialogSetBlockTechnicalError.run();

                        //report this exception
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                    PicFoodActionDialog.EDialogSetBlockNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                    PicFoodActionDialog.EDialogSetBlockTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "Throwable raised on toggling followship.", UncaughtException.ENo );
                }
            }
        }
    }
