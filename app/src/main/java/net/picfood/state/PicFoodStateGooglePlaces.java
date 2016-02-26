/*  $Id: PicFoodStateGooglePlaces.java 50538 2013-08-09 09:04:24Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.*;
    import  net.picfood.state.PicFoodState.*;
    import  net.picfood.ui.adapter.*;
    import  net.picfood.ui.adapter.PicFoodAdapterManager.*;
    import  android.os.Bundle;
    import  android.app.Activity;
    import  android.view.*;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   The state 'Google places'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50538 $ $Date: 2013-08-09 11:04:24 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/PicFoodStateGooglePlaces.java $"
    **********************************************************************************************/
    public class PicFoodStateGooglePlaces extends Activity
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
            ViewGroup googlePlacesView = PicFoodState.initStateActivity
            (
                PicFoodState.EGooglePlaces,
                R.layout.state_content_google_places,
                R.string.state_google_places_headline,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveGooglePlaces
            );

            //connect GridView-Adapter to Container
            PicFoodAdapterManager.getSingleton( this, GridViews.EGooglePlaces ).connect( googlePlacesView );
        }

        @Override
        public void onStart()
        {
            //invoke super-method
            super.onStart();

            //PicFoodDebug.googlePlaces.out( "GooglePlaces::onStart() - singleton is [" + singleton + "]" );

            //check if data shall be updated
            String scanGPSAndUpdateDataNextOnStartSetting = PicFoodSave.loadSetting( this, SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart );
            if ( scanGPSAndUpdateDataNextOnStartSetting != null && scanGPSAndUpdateDataNextOnStartSetting.equals( "true" ) )
            {
                //unflag and remember entry
                PicFoodSave.saveSetting( this, SaveKey.EStateGooglePlacesScanGPSAndUpdateDataNextOnStart, "false" );
                PicFoodSave.saveSetting( this, SaveKey.EStateGooglePlacesEnteredVia, GOOGLE_PLACES_ENTERED_VIA_NEW_ENTRY );

                //clear current google-places-adapter data
                PicFoodAdapterManager.getSingleton( singleton, GridViews.EGooglePlaces ).clearData();

                //show progress dialog and load new data
                LibDialogProgress.showProgressDialogUIThreaded
                (
                    this,
                    R.string.dialog_search_images_awaiting_gps_title,
                    R.string.dialog_search_images_awaiting_gps_body,
                    PicFoodAction.EGooglePlacesScanGPSAndLoadAfterProgressDialog,
                    true,
                    PicFoodActionPush.EPushCancelGPSNewEntry
                );
            }

            //check if term-search shall be performed
            String seachByTermAndUpdateDataNextOnStartSetting = PicFoodSave.loadSetting( this, SaveKey.EStateGooglePlacesSeachByTermAndUpdateDataNextOnStart );
            if ( seachByTermAndUpdateDataNextOnStartSetting != null && seachByTermAndUpdateDataNextOnStartSetting.equals( "true" ) )
            {
                //unflag and remember entry
                PicFoodSave.saveSetting( this, SaveKey.EStateGooglePlacesSeachByTermAndUpdateDataNextOnStart, "false" );
                PicFoodSave.saveSetting( this, SaveKey.EStateGooglePlacesEnteredVia, GOOGLE_PLACES_ENTERED_VIA_IMAGE_SEARCH );

                //clear current google-places-adapter data
                PicFoodAdapterManager.getSingleton( singleton, GridViews.EGooglePlaces ).clearData();

                PicFoodDebug.imageSearch.out( "seachByTermAndUpdateDataNextOnStart" );

                //show 'please wait'
                LibDialogProgress.showProgressDialogUIThreaded
                (
                    this,
                    R.string.dialog_wait_google_places_title,
                    R.string.dialog_wait_google_places_body,
                    PicFoodAction.EGooglePlacesLoadBySearchTermClean,
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
                    PicFoodActionState.ELeaveGooglePlaces.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
