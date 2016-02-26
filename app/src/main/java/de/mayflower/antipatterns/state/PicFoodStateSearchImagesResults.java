
    package de.mayflower.antipatterns.state;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  android.app.*;
    import  android.os.*;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   The state 'Search images results'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodStateSearchImagesResults extends Activity
    {
        /** The singleton instance of this state. */
        public      static      PicFoodStateSearchImagesResults     singleton                       = null;

        /** Flags, if the contents of this state shall be updated, next time the {@link #onStart()} method is invoked. */
        public      static      boolean                             updateNextOnStart               = false;

        /** A reference to the ScrollView in the state. */
        public                  LibScrollView                       iScrollView                     = null;
        /** The container that holds the wall-images. */
        public                  ViewGroup                           iContainerSearchImagesResults   = null;
        /** The overlay ImageView that can display any images. Used to represent the loading-icon and the 'no network'-symbol. */
        public                  ImageView                           iOverlayIcon                    = null;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //assign singleton
            singleton = this;

            //init state activity
            ViewGroup layout = PicFoodState.initStateActivity
            (
                PicFoodState.ESearchImagesResults,
                R.layout.state_content_search_images_results,
                R.string.state_search_images_results_header,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveSearchImagesResults
            );

            //reference scrollview and containers
            iScrollView                     = (LibScrollView)layout.findViewById( R.id.scroll_view );
            iContainerSearchImagesResults   = (ViewGroup)layout.findViewById( R.id.container_search_images_results );

            //reference overlay icon
            iOverlayIcon                    = (ImageView)layout.findViewById( R.id.overlay_icon );

            //connect to GridView
            //PicFoodAdapterManager.getSingleton( this, GridViews.ESearchImages ).connect( container );
        }

        @Override
        protected void onStart()
        {
            //invoke super method
            super.onStart();

            if ( updateNextOnStart )
            {
                updateNextOnStart = false;

                //ditch the GridView
                //PicFoodAdapterManager.getSingleton( this, GridViews.ESearchImages ).clearData();

                //update threaded!
                //PicFoodFlowSearchImages.addNewSearchImagesResultsThreaded();

                //update threaded!
                PicFoodActionUpdate.EUpdateSearchImagesResultsClean.run();
            }
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //leave this state
                    PicFoodActionState.ELeaveSearchImagesResults.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
