/*  $Id: PicFoodStateLostPassword.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.state.auth;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.Features;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  de.mayflower.antipatterns.R;
    import  de.mayflower.lib.ui.*;
    import  android.app.*;
    import  android.os.Bundle;
    import  android.view.*;
    import  android.widget.Button;
    import  android.widget.EditText;

    /**********************************************************************************************
    *   The state 'lostPassword'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/auth/PicFoodStateLostPassword.java $"
    **********************************************************************************************/
    public class PicFoodStateLostPassword extends Activity
    {
        /** The singleton instance of this state. */
        public          static      PicFoodStateLostPassword    singleton                       = null;

        /** A reference to the button 'Submit'. */
        public                      Button                      iButtonLostPassword             = null;

        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            //assign singleton instance
            singleton = this;

            //init state activity
            PicFoodState.initStateActivity
            (
                PicFoodState.ELostPassword,
                R.layout.state_content_lost_password,
                R.string.state_lost_password_headline,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveLostPassword
            );

            //button 'request password'
            {
                iButtonLostPassword = (Button)findViewById( R.id.button_request_password );
                LibUI.setupButton( this, iButtonLostPassword, R.string.state_lost_password_button, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionPush.EPushLostPassword );
            }

            //set InputFieldHandler for InputFields
            {
                PicFoodFlowUser.handlerLostPasswordUsername.assignField( (EditText)findViewById( R.id.input_username ) );
            }

            //try auto-login
            if ( Features.ENABLE_AUTO_LOGIN ) PicFoodFlowAutoLogin.checkAutoLogin( PicFoodState.ELostPassword );
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //move to acclaim state
                    PicFoodActionState.ELeaveLostPassword.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }

        /**********************************************************************************************
        *   Unselects all buttons/items being shown in this state.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void unselectAllButtonsUIThreaded()
        {
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        singleton.iButtonLostPassword.setSelected( false );
                    }
                }
            );
        }
    }
