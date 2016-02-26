
    package de.mayflower.antipatterns.action;

    import de.mayflower.antipatterns.*;

    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.AntiPatternsSave.SaveKey;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.acclaim.*;
    import  de.mayflower.antipatterns.state.auth.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public enum AntiPatternsActionState implements Runnable
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
                case EEnterPivotalFromLogin:
                {
                    //reset pivotal cache
                    AntiPatternsStatePivotalTabWall.resetLastUpdate();
                    AntiPatternsStatePivotalTabExplore.resetLastUpdate();
                    AntiPatternsStatePivotalTabProfile.resetLastUpdate();

                    //reset pivotal page
                    AntiPatternsStatePivotal.setLastSelectedTab(AntiPatternsStatePivotal.TAB_TAG_WALL);

                    //show pivotal menu
                    LibLauncher.launchActivity( AntiPatternsState.ELogin.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterPivotalFromAutoLogin:
                {
                    //reset pivotal cache
                    AntiPatternsStatePivotalTabWall.resetLastUpdate();
                    AntiPatternsStatePivotalTabExplore.resetLastUpdate();
                    AntiPatternsStatePivotalTabProfile.resetLastUpdate();

                    //reset pivotal page
                    AntiPatternsStatePivotal.setLastSelectedTab(AntiPatternsStatePivotal.TAB_TAG_WALL);

                    //show pivotal menu
                    LibLauncher.launchActivity( AntiPatternsFlowAutoLogin.lastState.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterPivotalFromRegister:
                {
                    //reset pivotal cache
                    AntiPatternsStatePivotalTabWall.resetLastUpdate();
                    AntiPatternsStatePivotalTabExplore.resetLastUpdate();
                    AntiPatternsStatePivotalTabProfile.resetLastUpdate();

                    //show pivotal menu
                    LibLauncher.launchActivity( AntiPatternsState.ERegister.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterGooglePlacesAndScanGPS:
                {
                    //flag activity GooglePlaces to update the data on next start
                    AntiPatternsSave.saveSetting(AntiPatternsState.ENewEntry.getActivity(), SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart, "true");

                    //start activity 'Google Places'
                    LibLauncher.launchActivity( AntiPatternsState.ENewEntry.getActivity(), AntiPatternsStateGooglePlaces.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterGooglePlacesAndSearchByTerm:
                {
                    //flag activity GooglePlaces to update the data on next start
                    AntiPatternsSave.saveSetting(AntiPatternsState.EPivotalMenu.getActivity(), SaveKey.EStateGooglePlacesSeachByTermAndUpdateDataNextOnStart, "true");

                    //start activity 'Google Places'
                    LibLauncher.launchActivity( AntiPatternsState.EPivotalMenu.getActivity(), AntiPatternsStateGooglePlaces.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveGooglePlaces:
                {
                    //check how the state 'google places' has been entered
                    String googlePlacesEnteredVia = AntiPatternsSave.loadSetting(AntiPatternsState.EGooglePlaces.getActivity(), SaveKey.EStateGooglePlacesEnteredVia);
                    if ( googlePlacesEnteredVia == null || googlePlacesEnteredVia.equals( AntiPatternsStateGooglePlaces.GOOGLE_PLACES_ENTERED_VIA_IMAGE_SEARCH ) )
                    {
                        //return to state 'pivotal menu'
                        LibLauncher.launchActivity( AntiPatternsState.EGooglePlaces.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    }
                    else if ( googlePlacesEnteredVia.equals( AntiPatternsStateGooglePlaces.GOOGLE_PLACES_ENTERED_VIA_NEW_ENTRY ) )
                    {
                        //flag state 'google-places' as shown
                        AntiPatternsSave.saveSetting(AntiPatternsState.EGooglePlaces.getActivity(), SaveKey.EStateNewEntryShowedGooglePlacesPicker, Boolean.TRUE.toString());

                        //return to state 'new entry'
                        LibLauncher.launchActivity( AntiPatternsState.EGooglePlaces.getActivity(), AntiPatternsStateNewEntry.class, R.anim.fade_in, R.anim.fade_out );
                    }
                    break;
                }

                case EEnterSettings:
                {
                    LibLauncher.launchActivity( AntiPatternsState.EPivotalMenu.getActivity(), AntiPatternsStateSettings.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case ELeaveSettings:
                {
                    LibLauncher.launchActivity( AntiPatternsState.ESettings.getActivity(), AntiPatternsStatePivotal.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveNewEntryShowConfirmDialog:
                {
                    //show dialog 'Discard entry?'
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsState.ENewEntry.getActivity(),
                        R.string.dialog_new_entry_cancel_title,
                        R.string.dialog_new_entry_cancel_body,
                        R.string.dialog_new_entry_cancel_button_yes,
                        ELeaveNewEntry,
                        R.string.dialog_new_entry_cancel_button_no,
                        AntiPatternsActionUnselect.EUnselectButtonsNewEntry,
                        true,
                        AntiPatternsActionUnselect.EUnselectButtonsNewEntry
                    );
                    break;
                }

                case ELeaveNewEntry:
                {
                    //set last pivotal menu state to 'upload new entry'
                    AntiPatternsStatePivotal.setLastSelectedTab(AntiPatternsStatePivotal.TAB_TAG_UPLOAD);

                    //launch pivotal menu
                    LibLauncher.launchActivity( AntiPatternsState.ENewEntry.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterRegister:
                {
                    LibLauncher.launchActivity( AntiPatternsState.EAcclaim.getActivity(), AntiPatternsStateRegister.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case EEnterLogin:
                {
                    LibLauncher.launchActivity( AntiPatternsState.EAcclaim.getActivity(), AntiPatternsStateLogin.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case ELeaveRegister:
                {
                    LibLauncher.launchActivity( AntiPatternsState.ERegister.getActivity(), AntiPatternsStateAcclaim.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveLogin:
                {
                    LibLauncher.launchActivity( AntiPatternsState.ELogin.getActivity(), AntiPatternsStateAcclaim.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case EEnterLostPassword:
                {
                    LibLauncher.launchActivity( AntiPatternsState.ELogin.getActivity(), AntiPatternsStateLostPassword.class, R.anim.push_left_in, R.anim.push_left_out );
                    break;
                }

                case ELeaveLostPassword:
                {
                    //unselect button
                    AntiPatternsActionUnselect.EUnselectButtonsLostPassword.run();

                    //leave state 'lost password'
                    LibLauncher.launchActivity( AntiPatternsState.ELostPassword.getActivity(), AntiPatternsStateLogin.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveForeignProfile:
                {
                    //return to state 'pivotal menu'
                    LibLauncher.launchActivity( AntiPatternsState.EForeignProfile.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveDetailedImage:
                {
                    //return to state 'pivotal menu'
                    LibLauncher.launchActivity( AntiPatternsState.EDetailedImage.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveImageProperties:
                {
                    //return to state 'pivotal menu'
                    LibLauncher.launchActivity( AntiPatternsState.EImageProperties.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case EEnterDetailedImage:
                {
                    AntiPatternsDebug.detailedImage.out( ">>>>>>> Enter state detailed image" );

                    //flag activity DetailedImage to load the image details on next start
                    AntiPatternsStateDetailedImage.loadImageDataNextOnStart = true;

                    //start activity 'Detailed Image'
                    LibLauncher.launchActivity( AntiPatternsState.EPivotalMenu.getActivity(), AntiPatternsStateDetailedImage.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }

                case EEnterFollowDetails:
                {
                    //show state 'follow details'
                    LibLauncher.launchActivity( AntiPatternsFlowProfileFollow.lastState.getActivity(), AntiPatternsStateFollowDetails.class, R.anim.fade_in, R.anim.fade_out );
                    break;
                }

                case ELeaveFollowDetails:
                {
                    //update foreign profile if returning to it
                    AntiPatternsStateForeignProfile.setupProfileNextOnStart = true;

                    if ( AntiPatternsFlowProfileFollow.lastState == null )
                    {
                        LibLauncher.launchActivity( AntiPatternsState.EFollowDetails.getActivity(), AntiPatternsStatePivotal.class, R.anim.fade_in, R.anim.fade_out );
                    }
                    else
                    {
                        LibLauncher.launchActivity( AntiPatternsState.EFollowDetails.getActivity(), AntiPatternsFlowProfileFollow.lastState.getActivity().getClass(), R.anim.fade_in, R.anim.fade_out );
                    }
                    break;
                }

                case ELeaveFindFriendsResults:
                {
                    //return to state 'settings'
                    LibLauncher.launchActivity( AntiPatternsState.EFindFriendsResults.getActivity(), AntiPatternsStateSettings.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case ELeaveSearchImagesResults:
                {
                    //return to state 'settings'
                    LibLauncher.launchActivity( AntiPatternsState.ESearchImagesResults.getActivity(), AntiPatternsStatePivotal.class, R.anim.push_right_in, R.anim.push_right_out );
                    break;
                }

                case EEnterSearchImagesResults:
                {
                    //clear the InputHandlers
                    AntiPatternsFlowSearchImages.inputHandlerSearchImagesLocation.destroy();
                    AntiPatternsFlowSearchImages.inputHandlerSearchImagesTerm.destroy();

                    //show state 'search images results' that displays these results
                    AntiPatternsStateSearchImagesResults.updateNextOnStart = true;
                    LibLauncher.launchActivity( AntiPatternsFlowSearchImages.lastStateBeforeSearch.getActivity(), AntiPatternsStateSearchImagesResults.class, R.anim.push_left_in, R.anim.push_left_out );

                    break;
                }
            }
        }
    }
