
    package de.mayflower.antipatterns.state.auth;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.Debug;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
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
    public class PicFoodStateRegister extends Activity implements TextWatcher
    {
        /** The singleton instance of this state. */
        public          static          PicFoodStateRegister            singleton                       = null;

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
            PicFoodState.initStateActivity
            (
                PicFoodState.ERegister,
                R.layout.state_content_register,
                R.string.state_register_headline,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveRegister
            );

            //set legal text
            {
                TextView iLegalText = (TextView)findViewById( R.id.legal_text );
                LibUI.setupTextView( this, iLegalText, PicFoodSystems.getFonts().TYPEFACE_REGULAR, R.string.state_register_legal_text );
                LibUI.setOnClickAction( iLegalText, PicFoodActionPush.EPushRegisterTermsAndConditions );
            }

            //button 'register'
            {
                iButtonRegister = (Button)findViewById( R.id.button_register );
                LibUI.setupButton( this, iButtonRegister, R.string.state_register_button, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionPush.EPushRegister );
            }

            //button 'import facebook data'
            {
                iButtonImportFacebookData = (Button)findViewById( R.id.button_import_facebook_data );
                LibUI.setupButton( this, iButtonImportFacebookData, R.string.state_register_button_import_facebook_data, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionPush.EPushRegisterImportFacebookData );
            }

            //reference username field
            {
                iEditTextUsername = (EditText)findViewById( R.id.input_username );
            }

            //set InputFieldHandler for InputFields
            {
                PicFoodFlowRegister.handlerUsername.assignField(        iEditTextUsername                                           );
                PicFoodFlowRegister.handlerEMail.assignField(           (EditText)findViewById( R.id.input_email                    ) );
                PicFoodFlowRegister.handlerPassword.assignField(        (EditText)findViewById( R.id.input_password                 ) );
                PicFoodFlowRegister.handlerPasswordConfirm.assignField( (EditText)findViewById( R.id.input_password_confirmation    ) );
                PicFoodFlowRegister.handlerPhone.assignField(           (EditText)findViewById( R.id.input_phone                    ) );
                PicFoodFlowRegister.handlerRealName.assignField(        (EditText)findViewById( R.id.input_realname                 ) );
            }

            //set onChangeListener for inputField 'username'
            LibUI.setOnTextChangeListener( iEditTextUsername, this );

            //'choose profile image'
            {
                //reference image container - assign bitmap if available
                iContainerProfileImage = (ImageView)findViewById( R.id.profile_image );
                if ( PicFoodFlowRegister.lastPickedBitmap != null ) iContainerProfileImage.setImageBitmap( PicFoodFlowRegister.lastPickedBitmap );

                //setup item 'choose profile image'
                iItemChooseProfileImage = LibUI.setupItem( this, R.id.item_choose_profile_image, R.id.text_choose_profile_image, R.drawable.net_picfood_bg_item, R.string.state_register_choose_profile_image, PicFoodActionPush.EPushRegisterChooseProfileImage, PicFoodSystems.getFonts().TYPEFACE_BOLD );
            }

            //reference and hide username check icon
            iContainerCheckUsernameIcon = (ImageView)findViewById( R.id.username_check_icon );
            iContainerCheckUsernameIcon.setVisibility( View.INVISIBLE );

            //set test values
            if ( Debug.DEBUG_PRESET_INPUTFIELDS )
            {
                PicFoodFlowRegister.handlerUsername.setTextUIThreaded(          this, "testuser" + System.currentTimeMillis()              );
                PicFoodFlowRegister.handlerEMail.setTextUIThreaded(             this, "email" + System.currentTimeMillis() + "@jenetic.de"    );
                PicFoodFlowRegister.handlerPassword.setTextUIThreaded(          this, "008156"                                                );
                PicFoodFlowRegister.handlerPasswordConfirm.setTextUIThreaded(   this, "008156"                                                );
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
                        PicFoodFlowRegister.lastPickedBitmap = bmp;
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
                    PicFoodActionState.ELeaveRegister.run();

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
                case PicFoodSettings.ActivityRequestID.STATE_REGISTER_PICK_IMAGE_FROM_GALLERY:
                {
                    //unselect buttons
                    PicFoodActionUnselect.EUnselectButtonsRegister.run();

                    //handle selected image data if response code is successful
                    if ( responseCode == RESULT_OK )
                    {
                        PicFoodFlowRegister.handlePickedImage( this, data );
                    }
                    break;
                }

                case PicFoodSettings.ActivityRequestID.STATE_REGISTER_CROP_IMAGE:
                {
                    //handle selected image data if response code is successful
                    if ( ( responseCode == RESULT_OK && data != null ) )
                    {
                        //handle the new cropped image data
                        PicFoodFlowRegister.handleCroppedImage( this );
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
            if ( aCharSequence.length() >= PicFoodSettings.InputFields.MIN_USERNAME_LENGTH_FOR_CHECKING )
            {
                //check if this has already been requested
                if ( lastInsertedUsername == null || !lastInsertedUsername.equals( aCharSequence.toString() ) )
                {
                    //assign and request
                    lastInsertedUsername = aCharSequence.toString();
                    PicFoodFlowRegister.requestUsernameCheck();
                }
            }
            else
            {
                //hide icon 'check-user'
                setCheckUsernameIconUIThreaded( false, R.drawable.net_picfood_user_check );
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
                PicFoodFlowRegister.handlerEMail.pruneUIThreaded(           singleton );
                PicFoodFlowRegister.handlerUsername.pruneUIThreaded(        singleton );
                PicFoodFlowRegister.handlerPassword.pruneUIThreaded(        singleton );
                PicFoodFlowRegister.handlerPasswordConfirm.pruneUIThreaded( singleton );
                PicFoodFlowRegister.handlerPhone.pruneUIThreaded(           singleton );
                PicFoodFlowRegister.handlerRealName.pruneUIThreaded(        singleton );

                //drop image
                assignPickedImageUIThreaded( null );
            }
        }
    }
