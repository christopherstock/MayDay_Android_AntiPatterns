/*  $Id: PicFoodAction.java 50578 2013-08-13 12:46:00Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.action;

    import  android.net.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.Debug;
    import  de.mayflower.antipatterns.PicFoodProject.Urls;
    import  de.mayflower.antipatterns.PicFoodSettings.Gps;
    import  de.mayflower.antipatterns.PicFoodSettings.Images;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.PicFoodSave.SaveKey;
    import  de.mayflower.antipatterns.state.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50578 $ $Date: 2013-08-13 14:46:00 +0200 (Di, 13 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/action/PicFoodAction.java $"
    **********************************************************************************************/
    public enum PicFoodAction implements Runnable
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
                        PicFoodState.ENewEntry.getActivity(),
                        EGooglePlacesLoadAfterGPSSuccessClean,
                        EGooglePlacesShowGPSFailed,
                        EGooglePlacesShowGPSDisabled,
                        PicFoodSettings.Gps.GPS_CACHE_TIME_MILLIS,
                        Gps.GPS_LOCATION_SEARCH_TIMEOUT_MILLIS,
                        PicFoodDebug.gps,
                        Debug.DEBUG_SIMULATE_GPS
                    );
                    break;
                }

                case EGooglePlacesLoadAfterGPSSuccessClean:
                {
                    //change progress dialog
                    LibDialogProgress.changeProgressDialogUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
                        LibResource.getResourceString( PicFoodState.EGooglePlaces.getActivity(), R.string.dialog_wait_google_places_title ),
                        LibResource.getResourceString( PicFoodState.EGooglePlaces.getActivity(), R.string.dialog_wait_google_places_body  )
                    );

                    //reset the google places
                    PicFoodFlowGooglePlaces.reset();

                    //order google-places-data for current location
                    PicFoodFlowGooglePlaces.loadNextResultsFromLastGPSLocation
                    (
                        LibGPS.currentLocation,
                        PicFoodActionDialog.EDialogGooglePlacesSurroundingsNoResults,
                        PicFoodActionDialog.EDialogGooglePlacesNoNetwork,
                        PicFoodActionDialog.EDialogGooglePlacesTechnicalError
                    );
                    break;
                }

                case EGooglePlacesLoadAfterGPSSuccessNext:
                {
                    //order google-places-data for current location
                    PicFoodFlowGooglePlaces.loadNextResultsFromLastGPSLocation
                    (
                        LibGPS.currentLocation,
                        PicFoodAction.ENone,
                        PicFoodAction.EGooglePlacesLoadNextResultsViaGPSNoNetwork,
                        PicFoodAction.EGooglePlacesLoadNextResultsViaGPSNoNetwork
                    );
                    break;
                }

                case EGooglePlacesLoadBySearchTermClean:
                {
                    //reset the google places
                    PicFoodFlowGooglePlaces.reset();

                    //order google-places-data for the specified searchterm
                    PicFoodFlowGooglePlaces.loadNextResultsFromSearchTerm
                    (
                        PicFoodFlowSearchImages.inputHandlerSearchImagesLocation.getText(),
                        PicFoodActionDialog.EDialogGooglePlacesTermNoResults,
                        PicFoodActionDialog.EDialogGooglePlacesNoNetwork,
                        PicFoodActionDialog.EDialogGooglePlacesTechnicalError
                    );
                    break;
                }

                case EGooglePlacesLoadBySearchTermNext:
                {
                    //order google-places-data for the specified searchterm
                    PicFoodFlowGooglePlaces.loadNextResultsFromSearchTerm
                    (
                        PicFoodFlowSearchImages.inputHandlerSearchImagesLocation.getText(),
                        PicFoodAction.ENone,
                        PicFoodAction.EGooglePlacesLoadNextResultsViaSearchTermNoNetwork,
                        PicFoodAction.EGooglePlacesLoadNextResultsViaSearchTermNoNetwork
                    );
                    break;
                }

                case EGooglePlacesShowGPSFailed:
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );

                    //show dialog 'gps low'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
                        R.string.dialog_gps_low_title,
                        R.string.dialog_gps_low_body,
                        R.string.dialog_gps_low_button_ok,
                        PicFoodActionState.ELeaveGooglePlaces,
                        0,
                        null,
                        true,
                        PicFoodActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case EGooglePlacesShowGPSDisabled:
                {
                    //hide progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );

                    //show dialog 'GPS disabled' - offer to change setting
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EGooglePlaces.getActivity(),
                        R.string.dialog_gps_disabled_title,
                        R.string.dialog_gps_disabled_body,
                        R.string.dialog_gps_disabled_button_enable,
                        ELaunchGpsSettingsFromGooglePlaces,
                        R.string.dialog_gps_disabled_button_cancel,
                        PicFoodActionState.ELeaveGooglePlaces,
                        true,
                        PicFoodActionState.ELeaveGooglePlaces
                    );
                    break;
                }

                case ELaunchGpsSettingsFromGooglePlaces:
                {
                    //flag new gps-connection wanted
                    PicFoodSave.saveSetting( PicFoodState.EGooglePlaces.getActivity(), SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart, "true" );

                    //show gps settings
                    LibLauncher.launchSettingsGPS( PicFoodState.EGooglePlaces.getActivity() );

                    break;
                }

                case ELaunchGpsSettingsFromSearchDialog:
                {
                    //flag to submit the search dialog again
                    PicFoodSave.saveSetting( PicFoodState.EPivotalMenu.getActivity(), SaveKey.EStatePivotalShowSearchDialogNextOnStart, "true" );

                    //show gps settings
                    LibLauncher.launchSettingsGPS( PicFoodState.EPivotalMenu.getActivity() );

                    break;
                }

                case ELaunchImageCropperNewEntry:
                {
                    //read last saved uri
                    String uriString = PicFoodSave.loadSetting( PicFoodState.ENewEntry.getActivity(), SaveKey.EStateNewEntryLastPickedURI );

                    //launch the image cropper from the saved uri
                    LibLauncher.launchImageCrop( Uri.parse( uriString ), Uri.fromFile( PicFoodSD.getFileLastCroppedImageNewEntry() ), PicFoodState.ENewEntry.getActivity(), PicFoodSettings.ActivityRequestID.STATE_NEW_ENTRY_CROP_IMAGE, Images.IMAGE_ASPECT_X, Images.IMAGE_ASPECT_Y );
                    break;
                }

                case ELaunchImageCropperRegister:
                {
                    //read last saved uri
                    String uriString = PicFoodSave.loadSetting( PicFoodState.ERegister.getActivity(), SaveKey.EStateRegisterLastPickedURI );

                    //launch the image cropper from the saved uri
                    LibLauncher.launchImageCrop( Uri.parse( uriString ), Uri.fromFile( PicFoodSD.getFileLastCroppedImageRegister() ), PicFoodState.ERegister.getActivity(), PicFoodSettings.ActivityRequestID.STATE_REGISTER_CROP_IMAGE, Images.ICON_ASPECT_X, Images.ICON_ASPECT_Y );
                    break;
                }

                case ELaunchImageCropperSettings:
                {
                    //read last saved uri
                    String uriString = PicFoodSave.loadSetting( PicFoodState.ESettings.getActivity(), SaveKey.EStateSettingsLastPickedURI );

                    //launch the image cropper from the saved uri
                    LibLauncher.launchImageCrop( Uri.parse( uriString ), Uri.fromFile( PicFoodSD.getFileLastCroppedImageSettings() ), PicFoodState.ESettings.getActivity(), PicFoodSettings.ActivityRequestID.STATE_SETTINGS_CROP_IMAGE, Images.ICON_ASPECT_X, Images.ICON_ASPECT_Y );
                    break;
                }

                case ESearchImagesAfterGPSSuccess:
                {
                    //change 'please wait'-message
                    LibDialogProgress.changeProgressDialogUIThreaded
                    (
                        PicFoodState.EPivotalMenu.getActivity(),
                        LibResource.getResourceString( PicFoodState.EPivotalMenu.getActivity(), R.string.dialog_search_images_searching_title ),
                        LibResource.getResourceString( PicFoodState.EPivotalMenu.getActivity(), R.string.dialog_search_images_searching_body  )
                    );

                    //assign last activity
                    PicFoodFlowSearchImages.lastSearchTerm              = PicFoodFlowSearchImages.inputHandlerSearchImagesTerm.getText();
                    PicFoodFlowSearchImages.lastStateBeforeSearch       = PicFoodState.EPivotalMenu;

                    //assign coordinates according to searchterm
                    if ( PicFoodFlowSearchImages.useGPS )
                    {
                        PicFoodFlowSearchImages.lastLatitude            = String.valueOf( LibGPS.currentLocation.getLatitude()  );
                        PicFoodFlowSearchImages.lastLongitude           = String.valueOf( LibGPS.currentLocation.getLongitude() );
                    }
                    else
                    {
                        PicFoodFlowSearchImages.lastLatitude            = "";
                        PicFoodFlowSearchImages.lastLongitude           = "";
                    }

                    //assign last actions
                    PicFoodFlowSearchImages.lastActionOnNoNetwork       = PicFoodActionDialog.EDialogSearchImagesPivotalNoNetwork;
                    PicFoodFlowSearchImages.lastActionOnTechnicalError  = PicFoodActionDialog.EDialogSearchImagesPivotalTechnicalError;
                    PicFoodFlowSearchImages.lastActionOnNoResults       = PicFoodActionDialog.EDialogSearchImagesPivotalNoResults;

                    //enter state 'search images'
                    PicFoodActionState.EEnterSearchImagesResults.run();
                    break;
                }

                case ESearchImagesAfterGPSFailed:
                {
                    PicFoodDebug.imageSearch.out( "Receiving location via GPS failed. Show dialog!" );

                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EPivotalMenu.getActivity() );

                    //show dialog 'gps low'
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EPivotalMenu.getActivity(),
                        R.string.dialog_search_images_gps_low_title,
                        R.string.dialog_search_images_gps_low_body,
                        R.string.dialog_search_images_gps_low_button_ok,
                        PicFoodActionDialog.EDialogSearchImages,
                        0,
                        null,
                        true,
                        PicFoodActionDialog.EDialogSearchImages
                    );

                    break;
                }

                case ESearchImagesAfterGPSDisabled:
                {
                    PicFoodDebug.imageSearch.out( "GPS is disabled. Show dialog!" );

                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EPivotalMenu.getActivity() );

                    //show dialog 'GPS disabled' - offer to change setting
                    LibDialogDefault.showUIThreaded
                    (
                        PicFoodState.EPivotalMenu.getActivity(),
                        R.string.dialog_search_images_gps_disabled_title,
                        R.string.dialog_search_images_gps_disabled_body,
                        R.string.dialog_search_images_gps_disabled_button_enable,
                        ELaunchGpsSettingsFromSearchDialog,
                        R.string.dialog_search_images_gps_disabled_button_cancel,
                        PicFoodActionDialog.EDialogSearchImages,
                        true,
                        PicFoodActionDialog.EDialogSearchImages
                    );

                    break;
                }

                case EGooglePlacesLoadNextResultsViaGPSNoNetwork:
                {
                    PicFoodFlowGooglePlaces.changeNextLoadingItemToNoNetwork( PicFoodState.EGooglePlaces, EGooglePlacesLoadAfterGPSSuccessNext );
                    break;
                }

                case EGooglePlacesLoadNextResultsViaSearchTermNoNetwork:
                {
                    PicFoodFlowGooglePlaces.changeNextLoadingItemToNoNetwork( PicFoodState.EGooglePlaces, EGooglePlacesLoadBySearchTermNext );
                    break;
                }

                case EDownloadUpdate:
                {
                    //unselect settings buttons
                    PicFoodActionUnselect.EUnselectButtonsSettings.run();

                    //let the system open the download link
                    LibLauncher.launchPlatformRequest( PicFoodState.ESettings.getActivity(), Urls.getUrlDownloadUpdate(), PicFoodDebug.major );

                    break;
                }
            }
        }
    }
