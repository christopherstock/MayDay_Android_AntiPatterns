
    package de.mayflower.antipatterns.action;

    import  android.net.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Debug;
    import  de.mayflower.antipatterns.AntiPatternsProject.Urls;
    import  de.mayflower.antipatterns.AntiPatternsSettings.Gps;
    import  de.mayflower.antipatterns.AntiPatternsSettings.Images;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.AntiPatternsSave.SaveKey;
    import  de.mayflower.antipatterns.state.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public enum AntiPatternsAction implements Runnable
    {
        /** */  ENone,

        /** */  EDownloadUpdate,
        /** */  EGooglePlacesLoadAfterGPSSuccessClean,
        /** */  EGooglePlacesLoadAfterGPSSuccessNext,
        /** */  EGooglePlacesLoadBySearchTermClean,
        /** */  EGooglePlacesLoadBySearchTermNext,
        /** */  EGooglePlacesLoadNextResultsViaGPSNoNetwork,
        /** */  EGooglePlacesLoadNextResultsViaSearchTermNoNetwork,
        /** */  EGooglePlacesScanGPSAndLoadAfterProgressDialog,
        /** */  EGooglePlacesShowGPSDisabled,
        /** */  EGooglePlacesShowGPSFailed,
        /** */  ELaunchGpsSettingsFromGooglePlaces,
        /** */  ELaunchGpsSettingsFromSearchDialog,
        /** */  ELaunchImageCropperNewEntry,
        /** */  ELaunchImageCropperRegister,
        /** */  ELaunchImageCropperSettings,
        /** */  ESearchImagesAfterGPSDisabled,
        /** */  ESearchImagesAfterGPSFailed,
        /** */  ESearchImagesAfterGPSSuccess,

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
                case ENone:
                {
                    //do nothing
                    break;
                }

                case EGooglePlacesScanGPSAndLoadAfterProgressDialog:
                {
                    //get fine gps-location
                    LibGPS.startNewThread
                    (
                        AntiPatternsState.ENewEntry.getActivity(),
                        EGooglePlacesLoadAfterGPSSuccessClean,
                        EGooglePlacesShowGPSFailed,
                        EGooglePlacesShowGPSDisabled,
                        AntiPatternsSettings.Gps.GPS_CACHE_TIME_MILLIS,
                        Gps.GPS_LOCATION_SEARCH_TIMEOUT_MILLIS,
                        AntiPatternsDebug.gps,
                        Debug.DEBUG_SIMULATE_GPS
                    );
                    break;
                }

                case EGooglePlacesLoadAfterGPSSuccessClean:
                {
                    //change progress dialog
                    LibDialogProgress.changeProgressDialogUIThreaded
                    (
                        AntiPatternsState.EGooglePlaces.getActivity(),
                        LibResource.getResourceString( AntiPatternsState.EGooglePlaces.getActivity(), R.string.dialog_wait_google_places_title ),
                        LibResource.getResourceString( AntiPatternsState.EGooglePlaces.getActivity(), R.string.dialog_wait_google_places_body  )
                    );

                    //reset the google places
                    AntiPatternsFlowGooglePlaces.reset();

                    //order google-places-data for current location
                    AntiPatternsFlowGooglePlaces.loadNextResultsFromLastGPSLocation
                            (
                                    LibGPS.currentLocation,
                                    AntiPatternsActionDialog.EDialogGooglePlacesSurroundingsNoResults,
                                    AntiPatternsActionDialog.EDialogGooglePlacesNoNetwork,
                                    AntiPatternsActionDialog.EDialogGooglePlacesTechnicalError
                            );
                    break;
                }

                case EGooglePlacesLoadAfterGPSSuccessNext:
                {
                    //order google-places-data for current location
                    AntiPatternsFlowGooglePlaces.loadNextResultsFromLastGPSLocation
                            (
                                    LibGPS.currentLocation,
                                    AntiPatternsAction.ENone,
                                    AntiPatternsAction.EGooglePlacesLoadNextResultsViaGPSNoNetwork,
                                    AntiPatternsAction.EGooglePlacesLoadNextResultsViaGPSNoNetwork
                            );
                    break;
                }

                case EGooglePlacesLoadBySearchTermClean:
                {
                    //reset the google places
                    AntiPatternsFlowGooglePlaces.reset();

                    //order google-places-data for the specified searchterm
                    AntiPatternsFlowGooglePlaces.loadNextResultsFromSearchTerm
                            (
                                    AntiPatternsFlowSearchImages.inputHandlerSearchImagesLocation.getText(),
                                    AntiPatternsActionDialog.EDialogGooglePlacesTermNoResults,
                                    AntiPatternsActionDialog.EDialogGooglePlacesNoNetwork,
                                    AntiPatternsActionDialog.EDialogGooglePlacesTechnicalError
                            );
                    break;
                }

                case EGooglePlacesLoadBySearchTermNext:
                {
                    //order google-places-data for the specified searchterm
                    AntiPatternsFlowGooglePlaces.loadNextResultsFromSearchTerm
                            (
                                    AntiPatternsFlowSearchImages.inputHandlerSearchImagesLocation.getText(),
                                    AntiPatternsAction.ENone,
                                    AntiPatternsAction.EGooglePlacesLoadNextResultsViaSearchTermNoNetwork,
                                    AntiPatternsAction.EGooglePlacesLoadNextResultsViaSearchTermNoNetwork
                            );
                    break;
                }

                case EGooglePlacesShowGPSFailed:
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );

                    //show dialog 'gps low'
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsState.EGooglePlaces.getActivity(),
                        R.string.dialog_gps_low_title,
                        R.string.dialog_gps_low_body,
                        R.string.dialog_gps_low_button_ok,
                        AntiPatternsActionState.ELeaveGooglePlaces,
                        0,
                        null,
                        true,
                        AntiPatternsActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case EGooglePlacesShowGPSDisabled:
                {
                    //hide progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );

                    //show dialog 'GPS disabled' - offer to change setting
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsState.EGooglePlaces.getActivity(),
                        R.string.dialog_gps_disabled_title,
                        R.string.dialog_gps_disabled_body,
                        R.string.dialog_gps_disabled_button_enable,
                        ELaunchGpsSettingsFromGooglePlaces,
                        R.string.dialog_gps_disabled_button_cancel,
                        AntiPatternsActionState.ELeaveGooglePlaces,
                        true,
                        AntiPatternsActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case ELaunchGpsSettingsFromGooglePlaces:
                {
                    //flag new gps-connection wanted
                    AntiPatternsSave.saveSetting(AntiPatternsState.EGooglePlaces.getActivity(), SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart, "true");

                    //show gps settings
                    LibLauncher.launchSettingsGPS( AntiPatternsState.EGooglePlaces.getActivity() );

                    break;
                }

                case ELaunchGpsSettingsFromSearchDialog:
                {
                    //flag to submit the search dialog again
                    AntiPatternsSave.saveSetting(AntiPatternsState.EPivotalMenu.getActivity(), SaveKey.EStatePivotalShowSearchDialogNextOnStart, "true");

                    //show gps settings
                    LibLauncher.launchSettingsGPS( AntiPatternsState.EPivotalMenu.getActivity() );

                    break;
                }

                case ELaunchImageCropperNewEntry:
                {
                    //read last saved uri
                    String uriString = AntiPatternsSave.loadSetting(AntiPatternsState.ENewEntry.getActivity(), SaveKey.EStateNewEntryLastPickedURI);

                    //launch the image cropper from the saved uri
                    LibLauncher.launchImageCrop( Uri.parse( uriString ), Uri.fromFile( AntiPatternsSD.getFileLastCroppedImageNewEntry() ), AntiPatternsState.ENewEntry.getActivity(), AntiPatternsSettings.ActivityRequestID.STATE_NEW_ENTRY_CROP_IMAGE, Images.IMAGE_ASPECT_X, Images.IMAGE_ASPECT_Y );
                    break;
                }

                case ELaunchImageCropperRegister:
                {
                    //read last saved uri
                    String uriString = AntiPatternsSave.loadSetting(AntiPatternsState.ERegister.getActivity(), SaveKey.EStateRegisterLastPickedURI);

                    //launch the image cropper from the saved uri
                    LibLauncher.launchImageCrop( Uri.parse( uriString ), Uri.fromFile( AntiPatternsSD.getFileLastCroppedImageRegister() ), AntiPatternsState.ERegister.getActivity(), AntiPatternsSettings.ActivityRequestID.STATE_REGISTER_CROP_IMAGE, Images.ICON_ASPECT_X, Images.ICON_ASPECT_Y );
                    break;
                }

                case ELaunchImageCropperSettings:
                {
                    //read last saved uri
                    String uriString = AntiPatternsSave.loadSetting(AntiPatternsState.ESettings.getActivity(), SaveKey.EStateSettingsLastPickedURI);

                    //launch the image cropper from the saved uri
                    LibLauncher.launchImageCrop( Uri.parse( uriString ), Uri.fromFile( AntiPatternsSD.getFileLastCroppedImageSettings() ), AntiPatternsState.ESettings.getActivity(), AntiPatternsSettings.ActivityRequestID.STATE_SETTINGS_CROP_IMAGE, Images.ICON_ASPECT_X, Images.ICON_ASPECT_Y );
                    break;
                }

                case ESearchImagesAfterGPSSuccess:
                {
                    //change 'please wait'-message
                    LibDialogProgress.changeProgressDialogUIThreaded
                    (
                        AntiPatternsState.EPivotalMenu.getActivity(),
                        LibResource.getResourceString( AntiPatternsState.EPivotalMenu.getActivity(), R.string.dialog_search_images_searching_title ),
                        LibResource.getResourceString( AntiPatternsState.EPivotalMenu.getActivity(), R.string.dialog_search_images_searching_body  )
                    );

                    //assign last activity
                    AntiPatternsFlowSearchImages.lastSearchTerm              = AntiPatternsFlowSearchImages.inputHandlerSearchImagesTerm.getText();
                    AntiPatternsFlowSearchImages.lastStateBeforeSearch       = AntiPatternsState.EPivotalMenu;

                    //assign coordinates according to searchterm
                    if ( AntiPatternsFlowSearchImages.useGPS )
                    {
                        AntiPatternsFlowSearchImages.lastLatitude            = String.valueOf( LibGPS.currentLocation.getLatitude()  );
                        AntiPatternsFlowSearchImages.lastLongitude           = String.valueOf( LibGPS.currentLocation.getLongitude() );
                    }
                    else
                    {
                        AntiPatternsFlowSearchImages.lastLatitude            = "";
                        AntiPatternsFlowSearchImages.lastLongitude           = "";
                    }

                    //assign last actions
                    AntiPatternsFlowSearchImages.lastActionOnNoNetwork       = AntiPatternsActionDialog.EDialogSearchImagesPivotalNoNetwork;
                    AntiPatternsFlowSearchImages.lastActionOnTechnicalError  = AntiPatternsActionDialog.EDialogSearchImagesPivotalTechnicalError;
                    AntiPatternsFlowSearchImages.lastActionOnNoResults       = AntiPatternsActionDialog.EDialogSearchImagesPivotalNoResults;

                    //enter state 'search images'
                    AntiPatternsActionState.EEnterSearchImagesResults.run();
                    break;
                }

                case ESearchImagesAfterGPSFailed:
                {
                    AntiPatternsDebug.imageSearch.out( "Receiving location via GPS failed. Show dialog!" );

                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EPivotalMenu.getActivity() );

                    //show dialog 'gps low'
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsState.EPivotalMenu.getActivity(),
                        R.string.dialog_search_images_gps_low_title,
                        R.string.dialog_search_images_gps_low_body,
                        R.string.dialog_search_images_gps_low_button_ok,
                        AntiPatternsActionDialog.EDialogSearchImages,
                        0,
                        null,
                        true,
                        AntiPatternsActionDialog.EDialogSearchImages
                    );

                    break;
                }

                case ESearchImagesAfterGPSDisabled:
                {
                    AntiPatternsDebug.imageSearch.out( "GPS is disabled. Show dialog!" );

                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EPivotalMenu.getActivity() );

                    //show dialog 'GPS disabled' - offer to change setting
                    LibDialogDefault.showUIThreaded
                    (
                        AntiPatternsState.EPivotalMenu.getActivity(),
                        R.string.dialog_search_images_gps_disabled_title,
                        R.string.dialog_search_images_gps_disabled_body,
                        R.string.dialog_search_images_gps_disabled_button_enable,
                        ELaunchGpsSettingsFromSearchDialog,
                        R.string.dialog_search_images_gps_disabled_button_cancel,
                        AntiPatternsActionDialog.EDialogSearchImages,
                        true,
                        AntiPatternsActionDialog.EDialogSearchImages
                    );

                    break;
                }

                case EGooglePlacesLoadNextResultsViaGPSNoNetwork:
                {
                    AntiPatternsFlowGooglePlaces.changeNextLoadingItemToNoNetwork(AntiPatternsState.EGooglePlaces, EGooglePlacesLoadAfterGPSSuccessNext);
                    break;
                }

                case EGooglePlacesLoadNextResultsViaSearchTermNoNetwork:
                {
                    AntiPatternsFlowGooglePlaces.changeNextLoadingItemToNoNetwork(AntiPatternsState.EGooglePlaces, EGooglePlacesLoadBySearchTermNext);
                    break;
                }

                case EDownloadUpdate:
                {
                    //unselect settings buttons
                    AntiPatternsActionUnselect.EUnselectButtonsSettings.run();

                    //let the system open the download link
                    LibLauncher.launchPlatformRequest( AntiPatternsState.ESettings.getActivity(), Urls.getUrlDownloadUpdate(), AntiPatternsDebug.major );

                    break;
                }
            }
        }
    }
