
    package de.mayflower.antipatterns.flow;

    import  org.json.*;
    import  android.view.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.data.AntiPatternsDataImage.ContentImageStyle;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;

    /**********************************************************************************************
    *   Specifies the flow for image tasks.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowImageDetailed
    {
        /**********************************************************************************************
        *   Fills the specified image-container with the current selected image.
        *
        *   @param  state           The according state.
        *   @param  targetContainer The container to append the created detailed image view to.
        **********************************************************************************************/
        public static final void orderDetailedImageAndUpdateDetailedImageContainer
        (
            final   LibState        state,
            final   ViewGroup       targetContainer
        )
        {
            AntiPatternsDebug.detailedImage.out( "updateImageContainerUIThreaded()" );

            try
            {
                //order the detailed image data threaded
                JSONObject  response    = AntiPatternsJsonRPCImage.getImage(state.getActivity(), AntiPatternsFlowImage.lastImage.iImageID);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //update the enriched lastImage
                        AntiPatternsFlowImage.lastImage = new AntiPatternsDataImage( response, ContentImageStyle.EDetailed, true );

                        //create the image-view and add it to the specified container ( on the UI-Thread )
                        final View imageView = AntiPatternsFlowImage.lastImage.createItemView( state );
                        state.getActivity().runOnUiThread
                        (
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    targetContainer.addView( imageView );
                                }
                            }
                        );

                        //order the bitmaps
                        AntiPatternsFlowImage.lastImage.orderImageThreaded( state );
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
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        AntiPatternsActionDialog.EDialogDetailedImageTechnicalError.run();

                        //report this error
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogDetailedImageNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    AntiPatternsActionDialog.EDialogDetailedImageTechnicalError.run();

                    //report this exception
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }
    }
