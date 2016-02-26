
    package de.mayflower.antipatterns.state;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Features;
    import de.mayflower.antipatterns.AntiPatternsSettings.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.AntiPatternsSave.SaveKey;
    import de.mayflower.antipatterns.state.AntiPatternsState.*;
    import de.mayflower.antipatterns.ui.AntiPatternsUI.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.*;
    import  android.os.Bundle;
    import  android.view.*;
    import  android.widget.*;
    import  android.app.Activity;
    import  android.content.*;
    import  android.graphics.*;
    import  android.graphics.drawable.*;

    /**********************************************************************************************
    *   The state 'new entry'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateNewEntry extends Activity
    {
        /** The singleton instance of this state. */
        protected       static AntiPatternsStateNewEntry singleton                       = null;

        /** Flags, if the camera activity shall be launched, next time the {@link #onStart()} method is invoked. */
        public          static          boolean                                             launchCameraNextOnStart         = false;
        /** Flags, if the gallery activity shall be launched, next time the {@link #onStart()} method is invoked. */
        public          static          boolean                                             launchGalleryNextOnStart        = false;

        /** A reference to the image-container that displays the image thall shall be uploaded. */
        private                         ImageView                                           iImageContainer                 = null;
        /** A reference to the TextView that displayed the name of the picked location. */
        private                         TextView                                            iTextLocation                   = null;
        /** A reference to the TextView that displayed the vicinity of the picked location. */
        private                         TextView                                            iTextVicinity                   = null;

        /** A reference to the Button 'submit'. */
        protected                       Button                                              iButtonSubmit                   = null;
        /** A reference to the Button 'change location'. */
        protected                       Button                                              iButtonChangeLocation           = null;
        /** A reference to the Button 're-crop'. */
        protected                       Button                                              iButtonReCrop                   = null;
        /** A reference to the Button 'cancel'. */
        protected                       Button                                              iButtonCancel                   = null;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //PicFoodDebug.newEntryReturning.out( getClass().getName() + "::onCreate [" + this + "] [" + handlerComment + "]" );

            //assign singleton
            singleton = this;

            //init state activity
            AntiPatternsState.initStateActivity
            (
                    AntiPatternsState.ENewEntry,
                    R.layout.state_content_new_entry,
                    R.string.state_new_entry_headline,
                    HideHeaderBar.ENo,
                    ShowHeaderBackButton.EYes,
                    ShowHeaderSettingsButton.ENo,
                    ShowHeaderSearchButton.ENo,
                    ShowHeaderAppLogo.ENo,
                    AntiPatternsActionState.ELeaveNewEntryShowConfirmDialog
            );

            //reference Image-container
            iImageContainer = (ImageView)findViewById( R.id.cropped_image );

            //reference location text and hide it
            {
                iTextLocation = (TextView)findViewById( R.id.text_location );
                LibUI.setupTextView( iTextLocation, AntiPatternsSystems.getFonts().TYPEFACE_BOLD, "" );
                iTextLocation.setVisibility( View.GONE );
            }

            //reference vicinity text and hide it
            {
                iTextVicinity = (TextView)findViewById( R.id.text_vicinity );
                LibUI.setupTextView( iTextVicinity, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, "" );
                iTextVicinity.setVisibility( View.GONE );
            }

            //setup comment label
            {
                TextView textCommentExplanation = (TextView)findViewById( R.id.text_comment_explanation );
                LibUI.setupTextView( this, textCommentExplanation, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, R.string.state_new_entry_comment_explanation );
            }

            //reference comment-box and connect to handler
            {
                AntiPatternsFlowNewEntry.handlerComment.assignField( (EditText)findViewById( R.id.input_field_comment ) );
            }

            //reference button 'submit'
            {
                iButtonSubmit = (Button)findViewById( R.id.submit_entry );
                LibUI.setupButton( this, iButtonSubmit, R.string.state_new_entry_button_submit, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsActionPush.EPushSubmitNewEntry );
            }

            //reference button 'change location'
            {
                iButtonChangeLocation = (Button)findViewById( R.id.change_location );
                LibUI.setupButton( this, iButtonChangeLocation, R.string.state_new_entry_button_change_location, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsActionState.EEnterGooglePlacesAndScanGPS );
            }

            //reference button 'recrop image'
            {
                iButtonReCrop = (Button)findViewById( R.id.recrop_image );
                LibUI.setupButton( this, iButtonReCrop, R.string.state_new_entry_button_recrop_image, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsAction.ELaunchImageCropperNewEntry );
            }

            //reference button 'cancel'
            {
                iButtonCancel = (Button)findViewById( R.id.cancel );
                LibUI.setupButton( this, iButtonCancel, R.string.state_new_entry_button_cancel, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsActionState.ELeaveNewEntryShowConfirmDialog );
            }
        }

        @Override
        protected void onStart()
        {
            //invoke super method
            super.onStart();

            //PicFoodDebug.newEntryReturning.out( getClass().getName() + "::onStart" );

            //unselect all buttons
            unselectAllButtonsUIThreaded();

            //assign current gathered data
            assignCurrentGatheredData();

            //launch different task
            if ( launchCameraNextOnStart )
            {
                //unflag and launch camera
                launchCameraNextOnStart = false;
                LibLauncher.launchCamera( this, ActivityRequestID.STATE_NEW_ENTRY_PICK_UPLOAD_IMAGE_CAMERA );
            }
            //launch different task
            else if ( launchGalleryNextOnStart )
            {
                //unflag and launch camera
                launchGalleryNextOnStart = false;
                LibLauncher.launchImagePicker( this, ActivityRequestID.STATE_NEW_ENTRY_PICK_UPLOAD_IMAGE_GALLERY );
            }
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //show 'really dismiss entry ?'
                    AntiPatternsActionState.ELeaveNewEntryShowConfirmDialog.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }

        /**********************************************************************************************
        *   Assigns the picked and cropped Bitmap and the selected Google Places Location.
        *   Must be invoked on the UI-Thread.
        **********************************************************************************************/
        private void assignCurrentGatheredData()
        {
            //assign last cropped Bitmap
            if ( LibIO.isExistent( AntiPatternsSD.getFileLastCroppedImageNewEntry() ) )
            {
                try
                {
                    //pick cropped image from sd
                    Bitmap bmp = LibImage.createBitmapFromFile( this, AntiPatternsSD.getFileLastCroppedImageNewEntry() );

                    //cut corners and assign
                    if ( Features.CUT_CORNERS_FOR_IMAGES )
                    {
                        //resize to desired displayed width ( fits corner size )
                        bmp = LibImage.resizeBitmapToWidth( bmp, AntiPatternsImage.getImageSizeToOrder(this, ImageSize.EDetailedImage) );

                        //cut corners
                        BitmapDrawable bd = AntiPatternsImage.cutCorners(this, new BitmapDrawable(getResources(), bmp), ImageSize.EDetailedImage);
                        iImageContainer.setImageDrawable( bd );
                    }
                    else
                    {
                        iImageContainer.setImageBitmap( bmp );
                    }
                }
                catch ( Throwable t )
                {
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "Caught on creating and assigning bitmap from cropped image", UncaughtException.ENo);
                }
            }

            //assign last picked GoogleLocation
            AntiPatternsDataGooglePlace pickedGooglePlace  = (AntiPatternsDataGooglePlace)LibIO.stringToSerializable( AntiPatternsSave.loadSetting(this, SaveKey.EStateNewEntryLastPickedGooglePlace) );
            if ( pickedGooglePlace != null )
            {
                //assign and show location text
                iTextLocation.setText( pickedGooglePlace.iName );
                iTextLocation.setVisibility( View.VISIBLE );

                //assign and show vicinity if available
                if ( LibString.isEmpty( pickedGooglePlace.iVicinity ) )
                {
                    iTextVicinity.setVisibility( View.GONE );
                }
                else
                {
                    iTextVicinity.setText( pickedGooglePlace.iVicinity );
                    iTextVicinity.setVisibility( View.VISIBLE );
                }
            }
            else
            {
                //hide location and vicinity text
                iTextLocation.setVisibility( View.GONE );
                iTextVicinity.setVisibility( View.GONE );
            }
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
                        //buttons
                        singleton.iButtonSubmit.setSelected(          false );
                        singleton.iButtonChangeLocation.setSelected(  false );
                        singleton.iButtonReCrop.setSelected(          false );
                        singleton.iButtonCancel.setSelected(          false );

                        //back-button
                        singleton.findViewById( R.id.back_icon ).setSelected( false );
                    }
                }
            );
        }

        @Override
        public void onActivityResult( int requestCode, int responseCode, Intent data )
        {
            //invoke super method
            super.onActivityResult( requestCode, responseCode, data );

            //PicFoodDebug.newEntryReturning.out( getClass().getName() + "::onActivityResult( " + requestCode + ", " + responseCode + ", " + data + ")" );

            //switch requestCode
            switch ( requestCode )
            {
                case AntiPatternsSettings.ActivityRequestID.STATE_NEW_ENTRY_PICK_UPLOAD_IMAGE_GALLERY:
                {
                    //unselect buttons
                    //if ( false ) PicFoodActionUnselect.EUnselectButtonsPivotalUpload.run();

                    //handle selected image data if response code is successful
                    if ( responseCode == RESULT_OK && data != null )
                    {
                        //handle the picked image
                        AntiPatternsFlowNewEntry.handlePickedImage(this, data);
                    }
                    else
                    {
                        //leave to state 'pivotal menu'
                        AntiPatternsActionState.ELeaveNewEntry.run();
                    }
                    break;
                }

                case AntiPatternsSettings.ActivityRequestID.STATE_NEW_ENTRY_PICK_UPLOAD_IMAGE_CAMERA:
                {
                    //unselect buttons
                    //if ( false ) PicFoodActionUnselect.EUnselectButtonsPivotalUpload.run();

                    //handle selected image data if response code is successful
                    if ( responseCode == RESULT_OK && data != null )
                    {
                        //handle the picked image
                        AntiPatternsFlowNewEntry.handlePickedImage(this, data);
                    }
                    else
                    {
                        //leave to state 'pivotal menu'
                        AntiPatternsActionState.ELeaveNewEntry.run();
                    }
                    break;
                }

                case AntiPatternsSettings.ActivityRequestID.STATE_NEW_ENTRY_CROP_IMAGE:
                {
                    //handle selected image data if response code is successful
                    if ( ( responseCode == RESULT_OK && data != null ) )
                    {
                        //handle the new cropped image data
                        AntiPatternsFlowNewEntry.handleCroppedImage(this);
                    }
                    else
                    {
                        //check, if bitmap has been cropped before
                        if ( LibIO.isExistent( AntiPatternsSD.getFileLastCroppedImageNewEntry() ) )
                        {
                            //go back to 'new entry' - no operation is required to do so!
                        }
                        else
                        {
                            //leave to state 'pivotal menu' and discard this entry!
                            AntiPatternsActionState.ELeaveNewEntry.run();
                        }
                    }
                    break;
                }
            }
        }
    }
