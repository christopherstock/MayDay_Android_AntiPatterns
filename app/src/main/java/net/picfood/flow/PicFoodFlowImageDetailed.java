/*  $Id: PicFoodFlowImageDetailed.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  org.json.*;
    import  android.view.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.data.*;
    import  net.picfood.data.PicFoodDataImage.ContentImageStyle;
    import  net.picfood.idm.*;
    import  net.picfood.io.jsonrpc.*;

    /**********************************************************************************************
    *   Specifies the flow for image tasks.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowImageDetailed.java $"
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
