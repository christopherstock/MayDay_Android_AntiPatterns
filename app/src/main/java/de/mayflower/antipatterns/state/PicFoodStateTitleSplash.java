/*  $Id: PicFoodStateTitleSplash.java 50538 2013-08-09 09:04:24Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.state;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  de.mayflower.antipatterns.ui.*;
    import  android.app.*;
    import  android.os.Bundle;
    import  android.view.*;
    import  android.widget.*;

    /**********************************************************************************************
    *   The state 'TitleSplash' - It displays the logo and the loading circle.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50538 $ $Date: 2013-08-09 11:04:24 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/PicFoodStateTitleSplash.java $"
    **********************************************************************************************/
    public class PicFoodStateTitleSplash extends Activity
    {
        /** The singleton instance of this state. */
        protected       static              PicFoodStateTitleSplash         singleton                   = null;

        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            //reference singleton instance
            singleton = this;

            //init state activity
            PicFoodState.initStateActivity
            (
                PicFoodState.ETitleSplash,
                R.layout.state_content_title_splash,
                R.string.empty_string,
                HideHeaderBar.EYes,
                ShowHeaderBackButton.ENo,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodAction.ENone
            );

            //start loading circle animation
            {
                ImageView loadingCircle = (ImageView)findViewById( R.id.loading_circle );
                PicFoodLoadingCircle.showAndStartLoadingCircleUIThreaded( this, loadingCircle );

              //viewCircle.startAnimation( LibResource.getResourceAnimation( this, R.anim.rotation_ccw ) );
              //viewCircle.startAnimation( Lib.getBitmapCenteredRotateAnimationAbsolute( Performance.LOADING_CIRCLE_ROTATION_DURATION ) );
            }
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
