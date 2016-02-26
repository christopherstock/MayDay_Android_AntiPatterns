
    package de.mayflower.antipatterns.state;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import de.mayflower.antipatterns.state.AntiPatternsState.*;
    import  android.app.*;
    import  android.os.*;
    import  android.view.*;

    /**********************************************************************************************
    *   The activity 'detailed image'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateDetailedImage extends Activity
    {
        /** The singleton instance of this state. */
        public      static AntiPatternsStateDetailedImage singleton                           = null;

        /** Flags, if the image data to display shall be updated, next time the {@link #onStart()} method is invoked. */
        public      static      boolean                         loadImageDataNextOnStart            = false;
        /** References the container that displays the detailed image. */
        public                  ViewGroup                       iDetailedImageContainer             = null;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //assign singleton
            singleton = this;

            //init state activity
            AntiPatternsState.initStateActivity
            (
                    AntiPatternsState.EDetailedImage,
                    R.layout.state_content_detailed_image,
                    R.string.state_detailed_image_headline,
                    HideHeaderBar.ENo,
                    ShowHeaderBackButton.EYes,
                    ShowHeaderSettingsButton.ENo,
                    ShowHeaderSearchButton.ENo,
                    ShowHeaderAppLogo.ENo,
                    AntiPatternsActionState.ELeaveDetailedImage
            );
        }

        @Override
        protected void onStart()
        {
            //invoke super method
            super.onStart();

            //check if the image shall be updated
            if ( loadImageDataNextOnStart )
            {
                //unflag
                loadImageDataNextOnStart = false;

                //reference image container
                iDetailedImageContainer = (ViewGroup)findViewById( R.id.image_container );

                //show 'please wait'
                AntiPatternsActionUpdate.EUpdateDetailedImage.run();
            }
        }

        /**********************************************************************************************
        *   Removes all views from the container that displays the detailed image.
        *   This method is invoked from the UI-Thread.
        **********************************************************************************************/
        public static void ditchContainerUIThreaded()
        {
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //ditch the image container
                        singleton.iDetailedImageContainer.removeAllViews();
                    }
                }
            );
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //leave this state
                    AntiPatternsActionState.ELeaveDetailedImage.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
