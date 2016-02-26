
    package de.mayflower.antipatterns.flow;

    import  org.json.*;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.net.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodSettings.Images;
    import  de.mayflower.antipatterns.PicFoodSettings.InputFields;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.PicFoodSave.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Manages changing the user's profile.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodFlowProfileChange
    {
        /** The InputHandler for the dialog InputField 'phone number' for the action 'change profile'. */
        public      static          LibDialogInputHandler               inputHandlerPhone               = new LibDialogInputHandler();
        /** The InputHandler for the dialog InputField 'real name' for the action 'change profile'. */
        public      static          LibDialogInputHandler               inputHandlerRealName            = new LibDialogInputHandler();
        /** The InputHandler for the dialog InputField 'biography' for the action 'change profile'. */
        public      static          LibDialogInputHandler               inputHandlerBiography           = new LibDialogInputHandler();
        /** The InputHandler for the dialog InputField 'website' for the action 'change profile'. */
        public      static          LibDialogInputHandler               inputHandlerWebsite             = new LibDialogInputHandler();

        /** The last Bitmap the user has picked for changing his profile image. */
        public      static          Bitmap                              lastPickedBitmap                = null;

        /**********************************************************************************************
        *   The flow that performs the action 'change user profile'.
        **********************************************************************************************/
        public static final void changeProfile()
        {
            //pick all required fields
            String  phone                   = inputHandlerPhone.getText();
            String  realName                = inputHandlerRealName.getText();
            String  biography               = inputHandlerBiography.getText();
            String  website                 = inputHandlerWebsite.getText();

            //check if phone is not empty but too short
            if ( !LibString.isEmpty( phone ) && LibString.isShorterThan( phone, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'phone number too short'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                PicFoodActionDialog.EDialogChangeProfilePhoneNumberInvalid.run();

                //abort profile change
                return;
            }

            //check if realName is not empty but too short
            if ( !LibString.isEmpty( realName ) && LibString.isShorterThan( realName, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'realName number too short'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                PicFoodActionDialog.EDialogChangeProfileRealNameInvalid.run();

                //abort profile change
                return;
            }

            //check if bio is not empty but too short
            if ( !LibString.isEmpty( biography ) && LibString.isShorterThan( biography, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'bio too short'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                PicFoodActionDialog.EDialogChangeProfileBiographyInvalid.run();

                //abort profile change
                return;
            }

            //check if website is not empty but too short
            if ( !LibString.isEmpty( website ) && LibString.isShorterThan( website, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'website too short'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                PicFoodActionDialog.EDialogChangeProfileWebsiteInvalid.run();

                //abort profile change
                return;
            }

            try
            {
                //connect profile-change and pick result
                JSONObject  response    = PicFoodJsonRPCUser.changeProfile
                (
                    PicFoodState.ESettings.getActivity(),
                    realName,
                    phone,
                    website,
                    biography
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //prune cache time for own profile
                        PicFoodStatePivotalTabProfile.resetLastUpdate();

                        //pruning input handlers is unnecessary ( will be assigned on opening the dialog )
                        //inputHandlerBiography.destroy();
                        //inputHandlerRealName.destroy();
                        //inputHandlerWebsite.destroy();
                        //inputHandlerPhone.destroy();

                        //hide please wait dialog and show 'success'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                        PicFoodActionDialog.EDialogChangeProfileSucceeded.run();
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                        PicFoodActionDialog.EDialogChangeProfileTechnicalError.run();

                        //report this exception
                        PicFoodDebug.DEBUG_THROWABLE( new LibIntentionalException( "Being invoked on changing profile data." ), "", UncaughtException.ENo );
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                    PicFoodActionDialog.EDialogChangeProfileNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                    PicFoodActionDialog.EDialogChangeProfileTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Handles the picked user's profile image on changing profile image.
        *
        *   @param  activity    The according activity context.
        *   @param  data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
        **********************************************************************************************/
        public static final void handlePickedImage( Activity activity, Intent data )
        {
            try
            {
                //pick image URL
                Uri         uri             = data.getData();
                PicFoodDebug.pickImage.out( "URL to bitmap data is: [" + uri + "]" );

                //save url
                PicFoodSave.saveSetting( activity, SaveKey.EStateSettingsLastPickedURI, uri.toString() );

                //launch image-cropper
                PicFoodAction.ELaunchImageCropperSettings.run();
            }
            catch ( Throwable t )
            {
                PicFoodDebug.pickImage.out( "Throwable being raised on receiving the picked image data:" );
                PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
            }
        }

        /**********************************************************************************************
        *   Handles the cropped user's profile image after the image cropper completed.
        *
        *   @param  activity    The according activity context.
        **********************************************************************************************/
        public static final void handleCroppedImage( Activity activity )
        {
            try
            {
                //resize the cropped image
                PicFoodImage.resizeImageToMaxSize
                (
                    activity,
                    Images.MAX_ICON_SIZE,
                    PicFoodSD.getFileLastCroppedImageSettings()
                );

                //pick bitmap from file
                Bitmap pickedBitmap = LibImage.createBitmapFromFile( activity, PicFoodSD.getFileLastCroppedImageSettings() );
                PicFoodDebug.pickImage.out( "Picking image succeeded: [" + pickedBitmap.getWidth() + "][" + pickedBitmap.getHeight() + "]" );

                //assign image
                lastPickedBitmap = pickedBitmap;

                //show 'please wait'
                LibDialogProgress.showProgressDialogUIThreaded
                (
                    PicFoodState.ESettings.getActivity(),
                    R.string.dialog_please_wait_title,
                    R.string.dialog_please_wait_body,
                    PicFoodActionUpdate.EUpdateSubmitChangedProfileImage,
                    false,
                    null
                );
            }
            catch ( Throwable t )
            {
                PicFoodDebug.pickImage.out( "Throwable being raised on receiving cropped image data:" );
                PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
            }
        }

        /**********************************************************************************************
        *   The flow that performs the action 'change user profile image'.
        **********************************************************************************************/
        public static final void changeProfileImage()
        {
            try
            {
                //connect profile-change and pick result
                JSONObject  response    = PicFoodJsonRPCUser.setProfileImage
                (
                    PicFoodState.ESettings.getActivity(),
                    lastPickedBitmap
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //prune cache time for own profile and wall
                        PicFoodStatePivotalTabWall.resetLastUpdate();
                        PicFoodStatePivotalTabProfile.resetLastUpdate();

                        //hide please wait dialog and show 'success'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                        PicFoodActionDialog.EDialogChangeProfileImageSucceeded.run();
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                        PicFoodActionDialog.EDialogChangeProfileImageTechnicalError.run();

                        //report this exception
                        PicFoodDebug.DEBUG_THROWABLE( new LibIntentionalException( "Being invoked on changing profile data." ), "", UncaughtException.ENo );
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                    PicFoodActionDialog.EDialogChangeProfileImageNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ESettings.getActivity() );
                    PicFoodActionDialog.EDialogChangeProfileImageTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }
    }
