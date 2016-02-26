/*  $Id: PicFoodActionState.java 50806 2013-09-02 12:28:08Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.action;

    import  net.picfood.*;

    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  net.picfood.flow.*;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.SaveKey;
    import  net.picfood.state.*;
    import  net.picfood.state.acclaim.*;
    import  net.picfood.state.auth.*;
    import  net.picfood.state.pivotal.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50806 $ $Date: 2013-09-02 14:28:08 +0200 (Mo, 02 Sep 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/action/PicFoodActionState.java $"
    **********************************************************************************************/
    public enum PicFoodActionState implements Runnable
    {

        /** */  EEnterDetailedImage,
        /** */  EEnterFollowDetails,
        /** */  EEnterGooglePlacesAndScanGPS,
        /** */  EEnterGooglePlacesAndSearchByTerm,
        /** */  EEnterLogin,
        /** */  EEnterLostPassword,
        /** */  EEnterPivotalFromAutoLogin,
        /** */  EEnterPivotalFromLogin,
        /** */  EEnterPivotalFromRegister,
        /** */  EEnterRegister,
        /** */  EEnterSearchImagesResults,
        /** */  EEnterSettings,

        /** */  ELeaveDetailedImage,
        /** */  ELeaveFindFriendsResults,
        /** */  ELeaveFollowDetails,
        /** */  ELeaveForeignProfile,
        /** */  ELeaveGooglePlaces,
        /** */  ELeaveImageProperties,
        /** */  ELeaveLogin,
        /** */  ELeaveLostPassword,
        /** */  ELeaveNewEntry,
        /** */  ELeaveNewEntryShowConfirmDialog,
        /** */  ELeaveRegister,
        /** */  ELeaveSearchImagesResults,
        /** */  ELeaveSettings,

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
                case EEnterPivotalFromLogin:
                {
                    //reset pivotal cache
                    PicFoodStatePivotalTabWall.resetLastUpdate();
                    PicFoodStatePivotalTabExplore.resetLastUpdate();
                    PicFoodStatePivotalTabProfile.resetLastUpdate();

                    //reset pivotal page
                    PicFoodStatePivotal.setLastSelectedTab( PicFoodStatePivotal.TAB_TAG_WALL );

                    //show pivotal menu
                    LibLauncher.launchActivity( PicFoodState.ELogin.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterPivotalFromAutoLogin:
                {
                    //reset pivotal cache
                    PicFoodStatePivotalTabWall.resetLastUpdate();
                    PicFoodStatePivotalTabExplore.resetLastUpdate();
                    PicFoodStatePivotalTabProfile.resetLastUpdate();

                    //reset pivotal page
                    PicFoodStatePivotal.setLastSelectedTab( PicFoodStatePivotal.TAB_TAG_WALL );

                    //show pivotal menu
                    LibLauncher.launchActivity( PicFoodFlowAutoLogin.lastState.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterPivotalFromRegister:
                {
                    //reset pivotal cache
                    PicFoodStatePivotalTabWall.resetLastUpdate();
                    PicFoodStatePivotalTabExplore.resetLastUpdate();
                    PicFoodStatePivotalTabProfile.resetLastUpdate();

                    //show pivotal menu
                    LibLauncher.launchActivity( PicFoodState.ERegister.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterGooglePlacesAndScanGPS:
                {
                    //flag activity GooglePlaces to update the data on next start
                    PicFoodSave.saveSetting( PicFoodState.ENewEntry.getActivity(), SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart, "true" );

                    //start activity 'Google Places'
                    LibLauncher.launchActivity( PicFoodState.ENewEntry.getActivity(), PicFoodStateGooglePlaces.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterGooglePlacesAndSearchByTerm:
                {
                    //flag activity GooglePlaces to update the data on next start
                    PicFoodSave.saveSetting( PicFoodState.EPivotalMenu.getActivity(), SaveKey.EStateGooglePlacesSeachByTermAndUpdateDataNextOnStart, "true" );

                    //start activity 'Google Places'
                    LibLauncher.launchActivity( PicFoodState.EPivotalMenu.getActivity(), PicFoodStateGooglePlaces.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveGooglePlaces:
                {
                    //check how the state 'google places' has been entered
                    String googlePlacesEnteredVia = PicFoodSave.loadSetting( PicFoodState.EGooglePlaces.getActivity(), SaveKey.EStateGooglePlacesEnteredVia );
                    if ( googlePlacesEnteredVia == null || googlePlacesEnteredVia.equals( PicFoodStateGooglePlaces.GOOGLE_PLACES_ENTERED_VIA_IMAGE_SEARCH ) )
                    {
                        //return to state 'pivotal menu'
                        LibLauncher.launchActivity( PicFoodState.EGooglePlaces.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    }
                    else if ( googlePlacesEnteredVia.equals( PicFoodStateGooglePlaces.GOOGLE_PLACES_ENTERED_VIA_NEW_ENTRY ) )
                    {
                        //flag state 'google-places' as shown
                        PicFoodSave.saveSetting( PicFoodState.EGooglePlaces.getActivity(), SaveKey.EStateNewEntryShowedGooglePlacesPicker, Boolean.TRUE.toString() );

                        //return to state 'new entry'
                        LibLauncher.launchActivity( PicFoodState.EGooglePlaces.getActivity(), PicFoodStateNewEntry.class, R.anim.fade_in, R.anim.fade_out );
                    }
                    break;
                }

                case EEnterSettings:
                {
                    LibLauncher.launchActivity( PicFoodState.EPivotalMenu.getActivity(), PicFoodStateSettings.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case ELeaveSettings:
                {
                    LibLauncher.launchActivity( PicFoodState.ESettings.getActivity(), PicFoodStatePivotal.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveNewEntryShowConfirmDialog:
                {
                    //show dialog 'Discard entry?'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.ENewEntry.getActivity(),
                        R.string.dialog_new_entry_cancel_title,
                        R.string.dialog_new_entry_cancel_body,
                        R.string.dialog_new_entry_cancel_button_yes,
                        ELeaveNewEntry,
                        R.string.dialog_new_entry_cancel_button_no,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry,
                        true,
                        PicFoodActionUnselect.EUnselectButtonsNewEntry
                    );
                    break;
                }

                case ELeaveNewEntry:
                {
                    //set last pivotal menu state to 'upload new entry'
                    PicFoodStatePivotal.setLastSelectedTab( PicFoodStatePivotal.TAB_TAG_UPLOAD );

                    //launch pivotal menu
                    LibLauncher.launchActivity( PicFoodState.ENewEntry.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterRegister:
                {
                    LibLauncher.launchActivity( PicFoodState.EAcclaim.getActivity(), PicFoodStateRegister.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case EEnterLogin:
                {
                    LibLauncher.launchActivity( PicFoodState.EAcclaim.getActivity(), PicFoodStateLogin.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case ELeaveRegister:
                {
                    LibLauncher.launchActivity( PicFoodState.ERegister.getActivity(), PicFoodStateAcclaim.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveLogin:
                {
                    LibLauncher.launchActivity( PicFoodState.ELogin.getActivity(), PicFoodStateAcclaim.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case EEnterLostPassword:
                {
                    LibLauncher.launchActivity( PicFoodState.ELogin.getActivity(), PicFoodStateLostPassword.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case ELeaveLostPassword:
                {
                    //unselect button
                    PicFoodActionUnselect.EUnselectButtonsLostPassword.run();

                    //leave state 'lost password'
                    LibLauncher.launchActivity( PicFoodState.ELostPassword.getActivity(), PicFoodStateLogin.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveForeignProfile:
                {
                    //return to state 'pivotal menu'
                    LibLauncher.launchActivity( PicFoodState.EForeignProfile.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveDetailedImage:
                {
                    //return to state 'pivotal menu'
                    LibLauncher.launchActivity( PicFoodState.EDetailedImage.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveImageProperties:
                {
                    //return to state 'pivotal menu'
                    LibLauncher.launchActivity( PicFoodState.EImageProperties.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterDetailedImage:
                {
                    PicFoodDebug.detailedImage.out( ">>>>>>> Enter state detailed image" );

                    //flag activity DetailedImage to load the image details on next start
                    PicFoodStateDetailedImage.loadImageDataNextOnStart = true;

                    //start activity 'Detailed Image'
                    LibLauncher.launchActivity( PicFoodState.EPivotalMenu.getActivity(), PicFoodStateDetailedImage.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EEnterFollowDetails:
                {
                    //show state 'follow details'
                    LibLauncher.launchActivity( PicFoodFlowProfileFollow.lastState.getActivity(), PicFoodStateFollowDetails.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveFollowDetails:
                {
                    //update foreign profile if returning to it
                    PicFoodStateForeignProfile.setupProfileNextOnStart = true;

                    if ( PicFoodFlowProfileFollow.lastState == null )
                    {
                        LibLauncher.launchActivity( PicFoodState.EFollowDetails.getActivity(), PicFoodStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    }
                    else
                    {
                        LibLauncher.launchActivity( PicFoodState.EFollowDetails.getActivity(), PicFoodFlowProfileFollow.lastState.getActivity().getClass(), R.anim.fade_in, R.anim.fade_out );
                    }
                    break;
                }

                case ELeaveFindFriendsResults:
                {
                    //return to state 'settings'
                    LibLauncher.launchActivity( PicFoodState.EFindFriendsResults.getActivity(), PicFoodStateSettings.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveSearchImagesResults:
                {
                    //return to state 'settings'
                    LibLauncher.launchActivity( PicFoodState.ESearchImagesResults.getActivity(), PicFoodStatePivotal.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case EEnterSearchImagesResults:
                {
                    //clear the InputHandlers
                    PicFoodFlowSearchImages.inputHandlerSearchImagesLocation.destroy();
                    PicFoodFlowSearchImages.inputHandlerSearchImagesTerm.destroy();

                    //show state 'search images results' that displays these results
                    PicFoodStateSearchImagesResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( PicFoodFlowSearchImages.lastStateBeforeSearch.getActivity(), PicFoodStateSearchImagesResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }
            }
        }
    }
