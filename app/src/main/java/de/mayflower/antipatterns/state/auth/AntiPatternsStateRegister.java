
    package de.mayflower.antipatterns.state.auth;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Debug;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.*;
    import de.mayflower.antipatterns.state.AntiPatternsState.*;
    import  de.mayflower.lib.ui.*;
    import  android.os.Bundle;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.text.*;
    import  android.view.*;
    import  android.widget.*;

    /**********************************************************************************************
    *   The state 'register'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateRegister extends Activity implements TextWatcher
    {
        /** The singleton instance of this state. */
        public          static AntiPatternsStateRegister singleton                       = null;

        /** The last username that has been inserted into the username InputField. */
        public          static          String                          lastInsertedUsername            = null;

        /** A reference to the 'register' button. */
        protected                       Button                          iButtonRegister                 = null;
        /** A reference to the 'import Facebook data' button. */
        protected                       Button                          iButtonImportFacebookData       = null;
        /** A reference to the 'choose profile image' item/button. */
        protected                       ViewGroup                       iItemChooseProfileImage         = null;

        /** The ImageView that displays the selected profile image. */
        public                          ImageView                       iContainerProfileImage          = null;
        /** The ImageView that displays the icon to indicate the result and activity of a username check. */
        public                          ImageView                       iContainerCheckUsernameIcon     = null;
        /** The InputField for the username. This one gets a ChangeListener assigned in order to perform actions when the text is changed. */
        public                          EditText                        iEditTextUsername               = null;

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
                    AntiPatternsState.ERegister,
                    R.layout.state_content_register,
                    R.string.state_register_headline,
                    HideHeaderBar.ENo,
                    ShowHeaderBackButton.EYes,
                    ShowHeaderSettingsButton.ENo,
                    ShowHeaderSearchButton.ENo,
                    ShowHeaderAppLogo.ENo,
                    AntiPatternsActionState.ELeaveRegister
            );

            //set legal text
            {
                TextView iLegalText = (TextView)findViewById( R.id.legal_text );
                LibUI.setupTextView( this, iLegalText, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, R.string.state_register_legal_text );
                LibUI.setOnClickAction( iLegalText, AntiPatternsActionPush.EPushRegisterTermsAndConditions );
            }

            //button 'register'
            {
                iButtonRegister = (Button)findViewById( R.id.button_register );
                LibUI.setupButton( this, iButtonRegister, R.string.state_register_button, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsActionPush.EPushRegister );
            }

            //button 'import facebook data'
            {
                iButtonImportFacebookData = (Button)findViewById( R.id.button_import_facebook_data );
                LibUI.setupButton( this, iButtonImportFacebookData, R.string.state_register_button_import_facebook_data, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsActionPush.EPushRegisterImportFacebookData );
            }

            //reference username field
            {
                iEditTextUsername = (EditText)findViewById( R.id.input_username );
            }

            //set InputFieldHandler for InputFields
            {
                AntiPatternsFlowRegister.handlerUsername.assignField(        iEditTextUsername                                           );
                AntiPatternsFlowRegister.handlerEMail.assignField(           (EditText)findViewById( R.id.input_email                    ) );
                AntiPatternsFlowRegister.handlerPassword.assignField(        (EditText)findViewById( R.id.input_password                 ) );
                AntiPatternsFlowRegister.handlerPasswordConfirm.assignField( (EditText)findViewById( R.id.input_password_confirmation    ) );
                AntiPatternsFlowRegister.handlerPhone.assignField(           (EditText)findViewById( R.id.input_phone                    ) );
                AntiPatternsFlowRegister.handlerRealName.assignField(        (EditText)findViewById( R.id.input_realname                 ) );
            }

            //set onChangeListener for inputField 'username'
            LibUI.setOnTextChangeListener( iEditTextUsername, this );

            //'choose profile image'
            {
                //reference image container - assign bitmap if available
                iContainerProfileImage = (ImageView)findViewById( R.id.profile_image );
                if ( AntiPatternsFlowRegister.lastPickedBitmap != null ) iContainerProfileImage.setImageBitmap( AntiPatternsFlowRegister.lastPickedBitmap );

                //setup item 'choose profile image'
                iItemChooseProfileImage = LibUI.setupItem( this, R.id.item_choose_profile_image, R.id.text_choose_profile_image, R.drawable.de_mayflower_antipattern_bg_item, R.string.state_register_choose_profile_image, AntiPatternsActionPush.EPushRegisterChooseProfileImage, AntiPatternsSystems.getFonts().TYPEFACE_BOLD );
            }

            //reference and hide username check icon
            iContainerCheckUsernameIcon = (ImageView)findViewById( R.id.username_check_icon );
            iContainerCheckUsernameIcon.setVisibility( View.INVISIBLE );

            //set test values
            if ( Debug.DEBUG_PRESET_INPUTFIELDS )
            {
                AntiPatternsFlowRegister.handlerUsername.setTextUIThreaded(          this, "testuser" + System.currentTimeMillis()              );
                AntiPatternsFlowRegister.handlerEMail.setTextUIThreaded(             this, "email" + System.currentTimeMillis() + "@jenetic.de"    );
                AntiPatternsFlowRegister.handlerPassword.setTextUIThreaded(          this, "008156"                                                );
                AntiPatternsFlowRegister.handlerPasswordConfirm.setTextUIThreaded(   this, "008156"                                                );
            }

            //try auto-login
        }

        /**********************************************************************************************
        *   Assigns the selected image from the Gallery to the profile-image-container in the 'register' state.
        *   This method is performed on the UI-Thread.
        *
        *   @param  bmp     The Bitmap to remember and to assign to the profile-image-container.
        **********************************************************************************************/
        public static final void assignPickedImageUIThreaded( final Bitmap bmp )
        {
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AntiPatternsFlowRegister.lastPickedBitmap = bmp;
                        singleton.iContainerProfileImage.setImageBitmap( bmp );   // may be null!
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
                    //move to acclaim state
                    AntiPatternsActionState.ELeaveRegister.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }

        @Override
        public void onActivityResult( int requestCode, int responseCode, Intent data )
        {
            //invoke super method
            super.onActivityResult( requestCode, responseCode, data );

            //switch requestCode
            switch ( requestCode )
            {
                case AntiPatternsSettings.ActivityRequestID.STATE_REGISTER_PICK_IMAGE_FROM_GALLERY:
                {
                    //unselect buttons
                    AntiPatternsActionUnselect.EUnselectButtonsRegister.run();

                    //handle selected image data if response code is successful
                    if ( responseCode == RESULT_OK )
                    {
                        AntiPatternsFlowRegister.handlePickedImage(this, data);
                    }
                    break;
                }

                case AntiPatternsSettings.ActivityRequestID.STATE_REGISTER_CROP_IMAGE:
                {
                    //handle selected image data if response code is successful
                    if ( ( responseCode == RESULT_OK && data != null ) )
                    {
                        //handle the new cropped image data
                        AntiPatternsFlowRegister.handleCroppedImage(this);
                    }
                    break;
                }
            }
        }

        /**********************************************************************************************
        *   Unselects all buttons/items being shown in this state.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void unselectAllButtonsUIThreaded()
        {
            if ( singleton != null )
            {
                singleton.runOnUiThread
                (
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            singleton.iButtonRegister.setSelected(            false );
                            singleton.iButtonImportFacebookData.setSelected(  false );
                            singleton.iItemChooseProfileImage.setSelected(    false );
                        }
                    }
                );
            }
        }

        @Override
        public void afterTextChanged( Editable aS )
        {
        }

        @Override
        public void beforeTextChanged( CharSequence aS, int aStart, int aCount, int aAfter )
        {
        }

        @Override
        public void onTextChanged( CharSequence aCharSequence, int aStart, int aBefore, int aCount )
        {
            //only assign if the input sequence has more than X chars
            if ( aCharSequence.length() >= AntiPatternsSettings.InputFields.MIN_USERNAME_LENGTH_FOR_CHECKING )
            {
                //check if this has already been requested
                if ( lastInsertedUsername == null || !lastInsertedUsername.equals( aCharSequence.toString() ) )
                {
                    //assign and request
                    lastInsertedUsername = aCharSequence.toString();
                    AntiPatternsFlowRegister.requestUsernameCheck();
                }
            }
            else
            {
                //hide icon 'check-user'
                setCheckUsernameIconUIThreaded( false, R.drawable.de_mayflower_antipattern_user_check);
            }
        }

        /**********************************************************************************************
        *   Changes the indicator for the 'username check' result.
        *
        *   @param  show        Specifies if the indicator shall be shown or hidden.
        *   @param  resourceID  The resource-UD of the image resource to display in the indicator.
        **********************************************************************************************/
        public static void setCheckUsernameIconUIThreaded( final boolean show, final int resourceID )
        {
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        singleton.iContainerCheckUsernameIcon.setVisibility( ( show ? View.VISIBLE : View.INVISIBLE ) );
                        singleton.iContainerCheckUsernameIcon.setImageResource( resourceID );
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
                //prune all fields
                AntiPatternsFlowRegister.handlerEMail.pruneUIThreaded(           singleton );
                AntiPatternsFlowRegister.handlerUsername.pruneUIThreaded(        singleton );
                AntiPatternsFlowRegister.handlerPassword.pruneUIThreaded(        singleton );
                AntiPatternsFlowRegister.handlerPasswordConfirm.pruneUIThreaded( singleton );
                AntiPatternsFlowRegister.handlerPhone.pruneUIThreaded(           singleton );
                AntiPatternsFlowRegister.handlerRealName.pruneUIThreaded(        singleton );

                //drop image
                assignPickedImageUIThreaded( null );
            }
        }
    }
