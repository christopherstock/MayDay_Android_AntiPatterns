
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsSettings.Images;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import de.mayflower.antipatterns.io.AntiPatternsSave.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  org.json.*;
    import  android.app.*;
    import  android.content.*;
    import  android.net.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds data for the state 'new entry'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowNewEntry
    {
        /** The InputFieldHandler for the InputField 'comment'. */
        public          static          LibInputFieldHandler                                handlerComment                  = new LibInputFieldHandler();

        /**********************************************************************************************
        *   Prunes old data for the 'new entry' state ( e.g. the last cropped image file ).
        *   Should be invoked when a new entry wants to be composed.
        *
        *   @param  activity    The according activity context.
        **********************************************************************************************/
        public static final void ditchOldData( Activity activity )
        {
            //ditch persistent 'show google places picker'
            AntiPatternsSave.saveSetting(activity, SaveKey.EStateNewEntryShowedGooglePlacesPicker, Boolean.FALSE.toString());

            //ditch cropped bitmap
            LibIO.deleteFile( AntiPatternsSD.getFileLastCroppedImageNewEntry() );

            //ditch persistent 'last user location'
            AntiPatternsSave.saveSetting(activity, SaveKey.EStateNewEntryLastUserLocation, null);

            //ditch persistent 'last picked google place'
            AntiPatternsSave.saveSetting(activity, SaveKey.EStateNewEntryLastPickedGooglePlace, null);

            //ditch persistent 'last picked uri'
            AntiPatternsSave.saveSetting(activity, SaveKey.EStateNewEntryLastPickedURI, null);
        }

        /**********************************************************************************************
        *   Handles the picked new-entry-image.
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
                AntiPatternsSave.saveSetting(activity, SaveKey.EStateNewEntryLastPickedURI, uri.toString());

                //launch image-cropper
                AntiPatternsAction.ELaunchImageCropperNewEntry.run();
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.pickImage.out( "Throwable being raised on receiving the picked image data:" );
                AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);

                //leave to state 'pivotal menu'
                AntiPatternsActionState.ELeaveNewEntry.run();
            }
        }

        /**********************************************************************************************
        *   Handles the cropped image after the image-cropper has completed it's job.
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
                                Images.MAX_IMAGE_SIZE,
                                AntiPatternsSD.getFileLastCroppedImageNewEntry()
                        );

                //update state 'new entry'
                //PicFoodStateNewEntry.singleton.

                //check if a location has already been picked
                String googlePlacesShown = AntiPatternsSave.loadSetting(activity, SaveKey.EStateNewEntryShowedGooglePlacesPicker);
                if ( googlePlacesShown == null || googlePlacesShown.equals( Boolean.FALSE.toString() ) )
                {
                    //show state 'google places'
                    AntiPatternsActionState.EEnterGooglePlacesAndScanGPS.run();
                }
                else
                {
                    //show state 'new entry' - no operation required to do so!
                }
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.pickImage.out( "Throwable being raised on receiving cropped image data:" );
                //AntiPatternsDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Receiving cropped image data threw an Exception" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);

                //leave to state 'pivotal menu'
                AntiPatternsActionState.ELeaveNewEntry.run();
            }
        }

        /**********************************************************************************************
        *   Handles the flow when the user clicks on 'submit' in state 'new entry'.
        *
        *   @param  state       The according state.
        **********************************************************************************************/
        public static final void uploadEntry( LibState state )
        {
            //pick comment from box
            String comment = handlerComment.getText();

            try
            {
                //get last google place
                AntiPatternsDataLocation currentUserLocation = (AntiPatternsDataLocation)LibIO.stringToSerializable(    AntiPatternsSave.loadSetting(state.getActivity(), SaveKey.EStateNewEntryLastUserLocation) );
                AntiPatternsDataGooglePlace pickedGooglePlace   = (AntiPatternsDataGooglePlace)LibIO.stringToSerializable( AntiPatternsSave.loadSetting(state.getActivity(), SaveKey.EStateNewEntryLastPickedGooglePlace) );

                //connect postImage and pick result
                JSONObject  response    = AntiPatternsJsonRPCImage.postImage
                        (
                                state.getActivity(),
                                currentUserLocation,
                                LibImage.createBitmapFromFile(state.getActivity(), AntiPatternsSD.getFileLastCroppedImageNewEntry()),
                                comment,
                                pickedGooglePlace
                        );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //hide please wait dialog and show 'image uploaded'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        AntiPatternsActionDialog.EDialogUploadNewEntrySucceeded.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        AntiPatternsActionDialog.EDialogUploadNewEntryTechnicalError.run();

                        //report this exception!
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(state);
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'upload new entry failed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        AntiPatternsActionDialog.EDialogUploadNewEntryFailed.run();
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
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogUploadNewEntryNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogUploadNewEntryTechnicalError.run();

                    //report this exception!
                    //AntiPatternsDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Technical error on uploading new entry" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }
    }
