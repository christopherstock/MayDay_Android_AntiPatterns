
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
    import  de.mayflower.antipatterns.data.PicFoodDataImage.ContentImageStyle;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;

    /**********************************************************************************************
    *   Specifies the flow for image tasks.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodFlowImageDetailed
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
            PicFoodDebug.detailedImage.out( "updateImageContainerUIThreaded()" );

            try
            {
                //order the detailed image data threaded
                JSONObject  response    = PicFoodJsonRPCImage.getImage( state.getActivity(), PicFoodFlowImage.lastImage.iImageID );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //update the enriched lastImage
                        PicFoodFlowImage.lastImage = new PicFoodDataImage( response, ContentImageStyle.EDetailed, true );

                        //create the image-view and add it to the specified container ( on the UI-Thread )
                        final View imageView = PicFoodFlowImage.lastImage.createItemView( state );
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
                        PicFoodFlowImage.lastImage.orderImageThreaded( state );
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
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        PicFoodActionDialog.EDialogDetailedImageTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    PicFoodActionDialog.EDialogDetailedImageNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    PicFoodActionDialog.EDialogDetailedImageTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }
    }
