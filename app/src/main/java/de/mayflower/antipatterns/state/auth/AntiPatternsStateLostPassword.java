
    package de.mayflower.antipatterns.state.auth;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Features;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.*;
    import de.mayflower.antipatterns.state.AntiPatternsState.*;
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
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateLostPassword extends Activity
    {
        /** The singleton instance of this state. */
        public          static AntiPatternsStateLostPassword singleton                       = null;

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
            AntiPatternsState.initStateActivity
            (
                    AntiPatternsState.ELostPassword,
                    R.layout.state_content_lost_password,
                    R.string.state_lost_password_headline,
                    HideHeaderBar.ENo,
                    ShowHeaderBackButton.EYes,
                    ShowHeaderSettingsButton.ENo,
                    ShowHeaderSearchButton.ENo,
                    ShowHeaderAppLogo.ENo,
                    AntiPatternsActionState.ELeaveLostPassword
            );

            //button 'request password'
            {
                iButtonLostPassword = (Button)findViewById( R.id.button_request_password );
                LibUI.setupButton( this, iButtonLostPassword, R.string.state_lost_password_button, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsActionPush.EPushLostPassword );
            }

            //set InputFieldHandler for InputFields
            {
                AntiPatternsFlowUser.handlerLostPasswordUsername.assignField( (EditText)findViewById( R.id.input_username ) );
            }

            //try auto-login
            if ( Features.ENABLE_AUTO_LOGIN ) AntiPatternsFlowAutoLogin.checkAutoLogin(AntiPatternsState.ELostPassword);
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //move to acclaim state
                    AntiPatternsActionState.ELeaveLostPassword.run();

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
