
    package de.mayflower.antipatterns.state.auth;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Debug;
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
    import  android.widget.TextView;

    /**********************************************************************************************
    *   The state 'login'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateLogin extends Activity
    {
        /** The singleton instance of this state. */
        public          static AntiPatternsStateLogin singleton               = null;

        /** A reference to the button 'login'. */
        protected                   Button                      iButtonLogin            = null;

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
                    AntiPatternsState.ELogin,
                    R.layout.state_content_login,
                    R.string.state_login_headline,
                    HideHeaderBar.ENo,
                    ShowHeaderBackButton.EYes,
                    ShowHeaderSettingsButton.ENo,
                    ShowHeaderSearchButton.ENo,
                    ShowHeaderAppLogo.ENo,
                    AntiPatternsActionState.ELeaveLogin
            );

            //set lost password text
            {
                TextView iLostPasswordText = (TextView)findViewById( R.id.lost_password );
                LibUI.setupTextView( this, iLostPasswordText, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, R.string.state_login_lost_password );
                LibUI.setOnClickAction( iLostPasswordText, AntiPatternsActionState.EEnterLostPassword );
            }

            //button 'login'
            {
                iButtonLogin = (Button)findViewById( R.id.button_login );
                LibUI.setupButton( this, iButtonLogin, R.string.state_login_button, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsActionPush.EPushLogin );
            }

            //set InputFieldHandler for InputFields
            {
                AntiPatternsFlowUser.handlerLoginUsername.assignField( (EditText)findViewById( R.id.input_username ) );
                AntiPatternsFlowUser.handlerLoginPassword.assignField( (EditText)findViewById( R.id.input_password ) );
            }

            //set default input
            if ( Debug.DEBUG_PRESET_INPUTFIELDS )
            {
                AntiPatternsFlowUser.handlerLoginUsername.setTextUIThreaded( this, "chris_stock" );
                AntiPatternsFlowUser.handlerLoginPassword.setTextUIThreaded( this, "008156"      );
            }

            //try auto-login
            if ( Features.ENABLE_AUTO_LOGIN ) AntiPatternsFlowAutoLogin.checkAutoLogin(AntiPatternsState.ELogin);
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //move to acclaim state
                    AntiPatternsActionState.ELeaveLogin.run();

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
                        singleton.iButtonLogin.setSelected( false );
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Resets all form fields for the 'register' state.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void clearAllFieldsUIThreaded()
        {
            if ( singleton != null )
            {
                AntiPatternsFlowUser.handlerLoginUsername.pruneUIThreaded( singleton );
                AntiPatternsFlowUser.handlerLoginPassword.pruneUIThreaded( singleton );
            }
        }
    }
