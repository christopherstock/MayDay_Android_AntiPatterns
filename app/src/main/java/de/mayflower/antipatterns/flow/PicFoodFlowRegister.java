
    package de.mayflower.antipatterns.flow;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.Features;
    import  de.mayflower.antipatterns.PicFoodSettings.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.PicFoodSave.SaveKey;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.auth.*;
    import  org.json.*;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.net.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds data for the state 'register'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodFlowRegister
    {
        /** The handler for the InputField 'email'. */
        public          static          LibInputFieldHandler            handlerEMail                    = new LibInputFieldHandler();
        /** The handler for the InputField 'username'. */
        public          static          LibInputFieldHandler            handlerUsername                 = new LibInputFieldHandler();
        /** The handler for the InputField 'password'. */
        public          static          LibInputFieldHandler            handlerPassword                 = new LibInputFieldHandler();
        /** The handler for the InputField 'confirm password'. */
        public          static          LibInputFieldHandler            handlerPasswordConfirm          = new LibInputFieldHandler();
        /** The handler for the InputField 'phone'. */
        public          static          LibInputFieldHandler            handlerPhone                    = new LibInputFieldHandler();
        /** The handler for the InputField 'real name'. */
        public          static          LibInputFieldHandler            handlerRealName                 = new LibInputFieldHandler();

        /** The last Bitmap that has been picked as the user profile image from the gallery. */
        public          static          Bitmap                          lastPickedBitmap                = null;

        /**********************************************************************************************
        *   Handles the picked user's profile image on registering.
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
                PicFoodSave.saveSetting( activity, SaveKey.EStateRegisterLastPickedURI, uri.toString() );

                //launch image-cropper
                PicFoodAction.ELaunchImageCropperRegister.run();
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
                    PicFoodSD.getFileLastCroppedImageRegister()
                );

                //pick bitmap from file
                Bitmap pickedBitmap = LibImage.createBitmapFromFile( activity, PicFoodSD.getFileLastCroppedImageRegister() );
                PicFoodDebug.pickImage.out( "Picking image succeeded: [" + pickedBitmap.getWidth() + "][" + pickedBitmap.getHeight() + "]" );

                //insert image in register ImageView
                PicFoodStateRegister.assignPickedImageUIThreaded( pickedBitmap );
            }
            catch ( Throwable t )
            {
                PicFoodDebug.pickImage.out( "Throwable being raised on receiving cropped image data:" );
                PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
            }
        }

        /**********************************************************************************************
        *   Handles the 'register'-button.
        **********************************************************************************************/
        public static final void register()
        {
            //pick all required fields
            String  username                = PicFoodFlowRegister.handlerUsername.getText();
            String  email                   = PicFoodFlowRegister.handlerEMail.getText();
            String  password                = PicFoodFlowRegister.handlerPassword.getText();
            String  passwordConfirmation    = PicFoodFlowRegister.handlerPasswordConfirm.getText();
            String  phone                   = PicFoodFlowRegister.handlerPhone.getText();
            String  realName                = PicFoodFlowRegister.handlerRealName.getText();
            Bitmap  bitmap                  = PicFoodFlowRegister.lastPickedBitmap;

            //check if username is empty or too short
            if ( LibString.isEmpty( username ) || LibString.isShorterThan( username, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'username required'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                PicFoodActionDialog.EDialogRegisterUsernameInvalid.run();

                //abort register
                return;
            }

            //check if email is required
            if ( !Features.EMAIL_OPTIONAL_ON_REGISTERING )
            {
                //check if email is empty or too short
                if ( LibString.isEmpty( email ) || LibString.isShorterThan( email, InputFields.MIN_INPUT_LENGTH ) )
                {
                    //hide please wait dialog and show 'email required'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                    PicFoodActionDialog.EDialogRegisterEMailInvalid.run();

                    //abort register
                    return;
                }
            }

            //check if password is empty or too short
            if ( LibString.isEmpty( password ) || LibString.isShorterThan( password, InputFields.MIN_PASSWORD_LENGTH ) )
            {
                //hide please wait dialog and show 'password required'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                PicFoodActionDialog.EDialogRegisterPasswordInvalid.run();

                //abort register
                return;
            }

            //check if passwords differ
            if ( !password.equals( passwordConfirmation ) )
            {
                //hide please wait dialog and show 'passwords differ'
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                PicFoodActionDialog.EDialogRegisterPasswordsDiffer.run();

                //abort register
                return;
            }

            try
            {
                //connect register and pick result
                JSONObject  response    = PicFoodJsonRPCRegister.register
                (
                    username,
                    email,
                    password,
                    phone,
                    realName,
                    bitmap,
                    PicFoodIDM.getFacebookUID( PicFoodState.ERegister.getActivity() )
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //assign session-id and user-id
                        PicFoodIDM.assignLogin
                        (
                            PicFoodState.ERegister.getActivity(),
                            LibJSON.getJSONStringSecure( response, "sessionId" ),
                            LibJSON.getJSONStringSecure( response, "userId"    ),
                            username,
                            LibIO.getMD5( password )
                        );

                        //hide please wait dialog and show 'pivotal menu'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                        PicFoodActionState.EEnterPivotalFromRegister.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_WRONG_E_MAIL_FORMAT:
                    {
                        //hide please wait dialog and show 'email malformed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                        PicFoodActionDialog.EDialogRegisterEMailMalformed.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_REGISTER_E_MAIL_EXISTS:
                    {
                        //hide please wait dialog and show 'email exists'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                        PicFoodActionDialog.EDialogRegisterEMailExists.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_REGISTER_WRONG_USERNAME_FORMAT:
                    {
                        //hide please wait dialog and show 'username malformed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                        PicFoodActionDialog.EDialogRegisterUsernameMalformed.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_REGISTER_USERNAME_EXISTS:
                    {
                        //hide please wait dialog and show 'username exists'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                        PicFoodActionDialog.EDialogRegisterUsernameExists.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                        PicFoodActionDialog.EDialogRegisterTechnicalError.run();

                        //report this exception!
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.ERegister );
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'register failed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                        PicFoodActionDialog.EDialogRegisterFailed.run();
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
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                    PicFoodActionDialog.EDialogRegisterNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.ERegister.getActivity() );
                    PicFoodActionDialog.EDialogRegisterTechnicalError.run();

                    //report this exception!
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Technical error on registering" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Performs a username check for a username that has been inserted into the 'username' field
        *   in state 'register'. This will setup an indicator to the user so he can immediately after
        *   changing the data in this InputField, if the username is available or not.
        **********************************************************************************************/
        public static final void requestUsernameCheck()
        {
            //copy last-inserted-username to a final String
            final String usernameToCheck = new String( PicFoodStateRegister.lastInsertedUsername );
            PicFoodDebug.pickImage.out( "Order Username-Check [" + usernameToCheck + "]" );

            //activate icon 'check-user'
            PicFoodStateRegister.setCheckUsernameIconUIThreaded( true, R.drawable.net_picfood_user_check );

            //order username-check in a new thread
            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //check via JSON-RPC ( blocks )
                        boolean usernameExists = PicFoodJsonRPCRegister.checkUsername( usernameToCheck );

                        //check if this checked username is still inserted
                        if ( usernameToCheck.equals( PicFoodStateRegister.lastInsertedUsername ) )
                        {
                            //check username existence
                            if (usernameExists )
                            {
                                //highlight 'free'
                                PicFoodStateRegister.setCheckUsernameIconUIThreaded( true, R.drawable.net_picfood_user_free );
                            }
                            else
                            {
                                //highlight 'existent'
                                PicFoodStateRegister.setCheckUsernameIconUIThreaded( true, R.drawable.net_picfood_user_existent );
                            }
                        }
                    }
                    catch ( Throwable t )
                    {
                        //abort this request if a Throwable has been raised
                        PicFoodStateRegister.setCheckUsernameIconUIThreaded( false, R.drawable.net_picfood_user_check );
                    }
                }
            }.start();
        }
    }
