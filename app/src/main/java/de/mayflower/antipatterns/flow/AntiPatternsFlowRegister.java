
    package de.mayflower.antipatterns.flow;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Features;
    import de.mayflower.antipatterns.AntiPatternsSettings.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.AntiPatternsSave.SaveKey;
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
    public class AntiPatternsFlowRegister
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
                AntiPatternsDebug.pickImage.out( "URL to bitmap data is: [" + uri + "]" );

                //save url
                AntiPatternsSave.saveSetting(activity, SaveKey.EStateRegisterLastPickedURI, uri.toString());

                //launch image-cropper
                AntiPatternsAction.ELaunchImageCropperRegister.run();
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.pickImage.out( "Throwable being raised on receiving the picked image data:" );
                AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
                AntiPatternsImage.resizeImageToMaxSize
                        (
                                activity,
                                Images.MAX_ICON_SIZE,
                                AntiPatternsSD.getFileLastCroppedImageRegister()
                        );

                //pick bitmap from file
                Bitmap pickedBitmap = LibImage.createBitmapFromFile( activity, AntiPatternsSD.getFileLastCroppedImageRegister() );
                AntiPatternsDebug.pickImage.out( "Picking image succeeded: [" + pickedBitmap.getWidth() + "][" + pickedBitmap.getHeight() + "]" );

                //insert image in register ImageView
                AntiPatternsStateRegister.assignPickedImageUIThreaded(pickedBitmap);
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.pickImage.out( "Throwable being raised on receiving cropped image data:" );
                AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
            }
        }

        /**********************************************************************************************
        *   Handles the 'register'-button.
        **********************************************************************************************/
        public static final void register()
        {
            //pick all required fields
            String  username                = AntiPatternsFlowRegister.handlerUsername.getText();
            String  email                   = AntiPatternsFlowRegister.handlerEMail.getText();
            String  password                = AntiPatternsFlowRegister.handlerPassword.getText();
            String  passwordConfirmation    = AntiPatternsFlowRegister.handlerPasswordConfirm.getText();
            String  phone                   = AntiPatternsFlowRegister.handlerPhone.getText();
            String  realName                = AntiPatternsFlowRegister.handlerRealName.getText();
            Bitmap  bitmap                  = AntiPatternsFlowRegister.lastPickedBitmap;

            //check if username is empty or too short
            if ( LibString.isEmpty( username ) || LibString.isShorterThan( username, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'username required'
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                AntiPatternsActionDialog.EDialogRegisterUsernameInvalid.run();

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                    AntiPatternsActionDialog.EDialogRegisterEMailInvalid.run();

                    //abort register
                    return;
                }
            }

            //check if password is empty or too short
            if ( LibString.isEmpty( password ) || LibString.isShorterThan( password, InputFields.MIN_PASSWORD_LENGTH ) )
            {
                //hide please wait dialog and show 'password required'
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                AntiPatternsActionDialog.EDialogRegisterPasswordInvalid.run();

                //abort register
                return;
            }

            //check if passwords differ
            if ( !password.equals( passwordConfirmation ) )
            {
                //hide please wait dialog and show 'passwords differ'
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                AntiPatternsActionDialog.EDialogRegisterPasswordsDiffer.run();

                //abort register
                return;
            }

            try
            {
                //connect register and pick result
                JSONObject  response    = AntiPatternsJsonRPCRegister.register
                        (
                                username,
                                email,
                                password,
                                phone,
                                realName,
                                bitmap,
                                AntiPatternsIDM.getFacebookUID(AntiPatternsState.ERegister.getActivity())
                        );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //assign session-id and user-id
                        AntiPatternsIDM.assignLogin
                                (
                                        AntiPatternsState.ERegister.getActivity(),
                                        LibJSON.getJSONStringSecure(response, "sessionId"),
                                        LibJSON.getJSONStringSecure(response, "userId"),
                                        username,
                                        LibIO.getMD5(password)
                                );

                        //hide please wait dialog and show 'pivotal menu'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                        AntiPatternsActionState.EEnterPivotalFromRegister.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_WRONG_E_MAIL_FORMAT:
                    {
                        //hide please wait dialog and show 'email malformed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                        AntiPatternsActionDialog.EDialogRegisterEMailMalformed.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_REGISTER_E_MAIL_EXISTS:
                    {
                        //hide please wait dialog and show 'email exists'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                        AntiPatternsActionDialog.EDialogRegisterEMailExists.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_REGISTER_WRONG_USERNAME_FORMAT:
                    {
                        //hide please wait dialog and show 'username malformed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                        AntiPatternsActionDialog.EDialogRegisterUsernameMalformed.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_REGISTER_USERNAME_EXISTS:
                    {
                        //hide please wait dialog and show 'username exists'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                        AntiPatternsActionDialog.EDialogRegisterUsernameExists.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                        AntiPatternsActionDialog.EDialogRegisterTechnicalError.run();

                        //report this exception!
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(AntiPatternsState.ERegister);
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'register failed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                        AntiPatternsActionDialog.EDialogRegisterFailed.run();
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                    AntiPatternsActionDialog.EDialogRegisterNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.ERegister.getActivity() );
                    AntiPatternsActionDialog.EDialogRegisterTechnicalError.run();

                    //report this exception!
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Technical error on registering" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
            final String usernameToCheck = new String( AntiPatternsStateRegister.lastInsertedUsername );
            AntiPatternsDebug.pickImage.out( "Order Username-Check [" + usernameToCheck + "]" );

            //activate icon 'check-user'
            AntiPatternsStateRegister.setCheckUsernameIconUIThreaded(true, R.drawable.net_picfood_user_check);

            //order username-check in a new thread
            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //check via JSON-RPC ( blocks )
                        boolean usernameExists = AntiPatternsJsonRPCRegister.checkUsername(usernameToCheck);

                        //check if this checked username is still inserted
                        if ( usernameToCheck.equals( AntiPatternsStateRegister.lastInsertedUsername ) )
                        {
                            //check username existence
                            if (usernameExists )
                            {
                                //highlight 'free'
                                AntiPatternsStateRegister.setCheckUsernameIconUIThreaded(true, R.drawable.net_picfood_user_free);
                            }
                            else
                            {
                                //highlight 'existent'
                                AntiPatternsStateRegister.setCheckUsernameIconUIThreaded(true, R.drawable.net_picfood_user_existent);
                            }
                        }
                    }
                    catch ( Throwable t )
                    {
                        //abort this request if a Throwable has been raised
                        AntiPatternsStateRegister.setCheckUsernameIconUIThreaded(false, R.drawable.net_picfood_user_check);
                    }
                }
            }.start();
        }
    }
