/*  $Id: PicFoodFlowNewEntry.java 50546 2013-08-09 14:19:00Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  net.picfood.*;
    import  net.picfood.PicFoodSettings.Images;
    import  net.picfood.action.*;
    import  net.picfood.data.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.*;
    import  net.picfood.io.jsonrpc.*;
    import  org.json.*;
    import  android.app.*;
    import  android.content.*;
    import  android.net.*;

    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.*;
    import  com.synapsy.android.lib.ui.dialog.*;
    import  com.synapsy.android.lib.util.*;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds data for the state 'new entry'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50546 $ $Date: 2013-08-09 16:19:00 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowNewEntry.java $"
    **********************************************************************************************/
    public class PicFoodFlowNewEntry
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
            PicFoodSave.saveSetting( activity, SaveKey.EStateNewEntryShowedGooglePlacesPicker, Boolean.FALSE.toString() );

            //ditch cropped bitmap
            LibIO.deleteFile( PicFoodSD.getFileLastCroppedImageNewEntry() );

            //ditch persistent 'last user location'
            PicFoodSave.saveSetting( activity, SaveKey.EStateNewEntryLastUserLocation, null );

            //ditch persistent 'last picked google place'
            PicFoodSave.saveSetting( activity, SaveKey.EStateNewEntryLastPickedGooglePlace, null );

            //ditch persistent 'last picked uri'
            PicFoodSave.saveSetting( activity, SaveKey.EStateNewEntryLastPickedURI, null );
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
                PicFoodDebug.pickImage.out( "URL to bitmap data is: [" + uri + "]" );

                //save url
                PicFoodSave.saveSetting( activity, SaveKey.EStateNewEntryLastPickedURI, uri.toString() );

                //launch image-cropper
                PicFoodAction.ELaunchImageCropperNewEntry.run();
            }
            catch ( Throwable t )
            {
                PicFoodDebug.pickImage.out( "Throwable being raised on receiving the picked image data:" );
                PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );

                //leave to state 'pivotal menu'
                PicFoodActionState.ELeaveNewEntry.run();
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
                PicFoodImage.resizeImageToMaxSize
                (
                    activity,
                    Images.MAX_IMAGE_SIZE,
                    PicFoodSD.getFileLastCroppedImageNewEntry()
                );

                //update state 'new entry'
                //PicFoodStateNewEntry.singleton.

                //check if a location has already been picked
                String googlePlacesShown = PicFoodSave.loadSetting( activity, SaveKey.EStateNewEntryShowedGooglePlacesPicker );
                if ( googlePlacesShown == null || googlePlacesShown.equals( Boolean.FALSE.toString() ) )
                {
                    //show state 'google places'
                    PicFoodActionState.EEnterGooglePlacesAndScanGPS.run();
                }
                else
                {
                    //show state 'new entry' - no operation required to do so!
                }
            }
            catch ( Throwable t )
            {
                PicFoodDebug.pickImage.out( "Throwable being raised on receiving cropped image data:" );
                //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Receiving cropped image data threw an Exception" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );

                //leave to state 'pivotal menu'
                PicFoodActionState.ELeaveNewEntry.run();
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
                PicFoodDataLocation     currentUserLocation = (PicFoodDataLocation)LibIO.stringToSerializable(    PicFoodSave.loadSetting( state.getActivity(), SaveKey.EStateNewEntryLastUserLocation      ) );
                PicFoodDataGooglePlace  pickedGooglePlace   = (PicFoodDataGooglePlace)LibIO.stringToSerializable( PicFoodSave.loadSetting( state.getActivity(), SaveKey.EStateNewEntryLastPickedGooglePlace ) );

                //connect postImage and pick result
                JSONObject  response    = PicFoodJsonRPCImage.postImage
                (
                    state.getActivity(),
                    currentUserLocation,
                    LibImage.createBitmapFromFile( state.getActivity(), PicFoodSD.getFileLastCroppedImageNewEntry() ),
                    comment,
                    pickedGooglePlace
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //hide please wait dialog and show 'image uploaded'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        PicFoodActionDialog.EDialogUploadNewEntrySucceeded.run();
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    {
                        //hide please wait dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        PicFoodActionDialog.EDialogUploadNewEntryTechnicalError.run();

                        //report this exception!
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( state );
                        break;
                    }

                    default:
                    {
                        //hide please wait dialog and show 'upload new entry failed'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        PicFoodActionDialog.EDialogUploadNewEntryFailed.run();
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
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    PicFoodActionDialog.EDialogUploadNewEntryNoNetwork.run();
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    PicFoodActionDialog.EDialogUploadNewEntryTechnicalError.run();

                    //report this exception!
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Technical error on uploading new entry" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }
    }
