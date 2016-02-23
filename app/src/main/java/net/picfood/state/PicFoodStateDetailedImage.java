/*  $Id: PicFoodStateDetailedImage.java 50542 2013-08-09 13:12:27Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.state.PicFoodState.*;
    import  android.app.*;
    import  android.os.*;
    import  android.view.*;

    /**********************************************************************************************
    *   The activity 'detailed image'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50542 $ $Date: 2013-08-09 15:12:27 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/PicFoodStateDetailedImage.java $"
    **********************************************************************************************/
    public class PicFoodStateDetailedImage extends Activity
    {
        /** The singleton instance of this state. */
        public      static      PicFoodStateDetailedImage       singleton                           = null;

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
            PicFoodState.initStateActivity
            (
                PicFoodState.EDetailedImage,
                R.layout.state_content_detailed_image,
                R.string.state_detailed_image_headline,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveDetailedImage
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
                PicFoodActionUpdate.EUpdateDetailedImage.run();
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
                    PicFoodActionState.ELeaveDetailedImage.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
