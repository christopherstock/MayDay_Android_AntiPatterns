
    package de.mayflower.antipatterns.state;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.io.*;
    import de.mayflower.antipatterns.io.AntiPatternsSave.*;
    import de.mayflower.antipatterns.state.AntiPatternsState.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.*;
    import  android.os.Bundle;
    import  android.app.Activity;
    import  android.view.*;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   The state 'Google places'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateGooglePlaces extends Activity
    {
        /** This value is used for the SaveKey {@link SaveKey#EStateGooglePlacesEnteredVia} and indicates that the state 'Google Places' has last been entered via state 'new entry'. */
        public      static  final   String          GOOGLE_PLACES_ENTERED_VIA_NEW_ENTRY     = "newEntry";
        /** This value is used for the SaveKey {@link SaveKey#EStateGooglePlacesEnteredVia} and indicates that the state 'Google Places' has last been entered via state 'image search'. */
        public      static  final   String          GOOGLE_PLACES_ENTERED_VIA_IMAGE_SEARCH  = "imageSearch";

        /** The singleton instance of this state. */
        protected   static  Activity                singleton                               = null;

        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            //PicFoodDebug.googlePlaces.out( "GooglePlaces::onCreate() - singleton is [" + singleton + "]" );

            //assign singleton
            singleton = this;

            //init state activity
            ViewGroup googlePlacesView = AntiPatternsState.initStateActivity
            (
                    AntiPatternsState.EGooglePlaces,
                    R.layout.state_content_google_places,
                    R.string.state_google_places_headline,
                    HideHeaderBar.ENo,
                    ShowHeaderBackButton.EYes,
                    ShowHeaderSettingsButton.ENo,
                    ShowHeaderSearchButton.ENo,
                    ShowHeaderAppLogo.ENo,
                    AntiPatternsActionState.ELeaveGooglePlaces
            );

            //connect GridView-Adapter to Container
            AntiPatternsAdapterManager.getSingleton(this, GridViews.EGooglePlaces).connect( googlePlacesView );
        }

        @Override
        public void onStart()
        {
            //invoke super-method
            super.onStart();

            //PicFoodDebug.googlePlaces.out( "GooglePlaces::onStart() - singleton is [" + singleton + "]" );

            //check if data shall be updated
            String scanGPSAndUpdateDataNextOnStartSetting = AntiPatternsSave.loadSetting(this, SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart);
            if ( scanGPSAndUpdateDataNextOnStartSetting != null && scanGPSAndUpdateDataNextOnStartSetting.equals( "true" ) )
            {
                //unflag and remember entry
                AntiPatternsSave.saveSetting(this, SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart, "false");
                AntiPatternsSave.saveSetting(this, SaveKey.EStateGooglePlacesEnteredVia, GOOGLE_PLACES_ENTERED_VIA_NEW_ENTRY);

                //clear current google-places-adapter data
                AntiPatternsAdapterManager.getSingleton(singleton, GridViews.EGooglePlaces).clearData();

                //show progress dialog and load new data
                LibDialogProgress.showProgressDialogUIThreaded
                (
                    this,
                    R.string.dialog_search_images_awaiting_gps_title,
                    R.string.dialog_search_images_awaiting_gps_body,
                    AntiPatternsAction.EGooglePlacesScanGPSAndLoadAfterProgressDialog,
                    true,
                    AntiPatternsActionPush.EPushCancelGPSNewEntry
                );
            }

            //check if term-search shall be performed
            String seachByTermAndUpdateDataNextOnStartSetting = AntiPatternsSave.loadSetting(this, SaveKey.EStateGooglePlacesSeachByTermAndUpdateDataNextOnStart);
            if ( seachByTermAndUpdateDataNextOnStartSetting != null && seachByTermAndUpdateDataNextOnStartSetting.equals( "true" ) )
            {
                //unflag and remember entry
                AntiPatternsSave.saveSetting(this, SaveKey.EStateGooglePlacesSeachByTermAndUpdateDataNextOnStart, "false");
                AntiPatternsSave.saveSetting(this, SaveKey.EStateGooglePlacesEnteredVia, GOOGLE_PLACES_ENTERED_VIA_IMAGE_SEARCH);

                //clear current google-places-adapter data
                AntiPatternsAdapterManager.getSingleton(singleton, GridViews.EGooglePlaces).clearData();

                AntiPatternsDebug.imageSearch.out( "seachByTermAndUpdateDataNextOnStart" );

                //show 'please wait'
                LibDialogProgress.showProgressDialogUIThreaded
                (
                    this,
                    R.string.dialog_wait_google_places_title,
                    R.string.dialog_wait_google_places_body,
                    AntiPatternsAction.EGooglePlacesLoadBySearchTermClean,
                    false,
                    null
                );
            }
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //leave state 'google places'
                    AntiPatternsActionState.ELeaveGooglePlaces.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
