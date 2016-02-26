/*  $Id: PicFoodStateLogin.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.state.auth;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.Debug;
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
    import  android.widget.TextView;

    /**********************************************************************************************
    *   The state 'login'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/auth/PicFoodStateLogin.java $"
    **********************************************************************************************/
    public class PicFoodStateLogin extends Activity
    {
        /** The singleton instance of this state. */
        public          static      PicFoodStateLogin           singleton               = null;

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
            PicFoodState.initStateActivity
            (
                PicFoodState.ELogin,
                R.layout.state_content_login,
                R.string.state_login_headline,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveLogin
            );

            //set lost password text
            {
                TextView iLostPasswordText = (TextView)findViewById( R.id.lost_password );
                LibUI.setupTextView( this, iLostPasswordText, PicFoodSystems.getFonts().TYPEFACE_REGULAR, R.string.state_login_lost_password );
                LibUI.setOnClickAction( iLostPasswordText, PicFoodActionState.EEnterLostPassword );
            }

            //button 'login'
            {
                iButtonLogin = (Button)findViewById( R.id.button_login );
                LibUI.setupButton( this, iButtonLogin, R.string.state_login_button, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionPush.EPushLogin );
            }

            //set InputFieldHandler for InputFields
            {
                PicFoodFlowUser.handlerLoginUsername.assignField( (EditText)findViewById( R.id.input_username ) );
                PicFoodFlowUser.handlerLoginPassword.assignField( (EditText)findViewById( R.id.input_password ) );
            }

            //set default input
            if ( Debug.DEBUG_PRESET_INPUTFIELDS )
            {
                PicFoodFlowUser.handlerLoginUsername.setTextUIThreaded( this, "chris_stock" );
                PicFoodFlowUser.handlerLoginPassword.setTextUIThreaded( this, "008156"      );
            }

            //try auto-login
            if ( Features.ENABLE_AUTO_LOGIN ) PicFoodFlowAutoLogin.checkAutoLogin( PicFoodState.ELogin );
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //move to acclaim state
                    PicFoodActionState.ELeaveLogin.run();

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
                PicFoodFlowUser.handlerLoginUsername.pruneUIThreaded( singleton );
                PicFoodFlowUser.handlerLoginPassword.pruneUIThreaded( singleton );
            }
        }
    }
