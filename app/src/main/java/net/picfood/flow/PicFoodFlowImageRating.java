/*  $Id: PicFoodFlowImageRating.java 50555 2013-08-12 09:22:12Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.data.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.jsonrpc.*;
    import  net.picfood.ui.adapter.PicFoodAdapterManager.GridViews;
    import  org.json.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.dialog.*;
    import  com.synapsy.android.lib.util.*;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Specifies the flow for image-ratings tasks.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50555 $ $Date: 2013-08-12 11:22:12 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowImageRating.java $"
    **********************************************************************************************/
    public class PicFoodFlowImageRating
    {
        /**********************************************************************************************
        *   Handles the 'rateFood'-task.
        **********************************************************************************************/
        public static final void rateFood()
        {
            try
            {
                //commit rate request
                JSONObject  response    = PicFoodJsonRPCImage.rateFood( PicFoodFlowImage.lastState.getActivity(), PicFoodFlowImage.lastImage.iImageID, PicFoodFlowImage.lastFoodRating );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );

                        //unselect image buttons
                        PicFoodActionUnselect.EUnselectButtonsImage.run();

                        //update according image list after image has been changed
                        PicFoodActionUpdate.EUpdateImageListAfterImageChange.run();

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodFlowImage.lastState );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    default:
                    {
                        //unexpected return code - show 'technical error' too

                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                        PicFoodActionDialog.EDialogRateFoodTechnicalError.run();

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                    PicFoodActionDialog.EDialogRateFoodNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodFlowImage.lastState.getActivity() );

                    //unselect image buttons
                    PicFoodActionUnselect.EUnselectButtonsImage.run();

                    //do NOT show 'technical error' but report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /*****************************************************************************
        *   Orders all 'food ratings' for one specific image
        *   and displays them in state 'image properties'.
        *
        *   @param  state                   The according state.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *****************************************************************************/
        public static final void orderNextImageRatings
        (
            LibState        state,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError
        )
        {
            //order image food-ratings
            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering ratings\n" + PicFoodFlowImage.flow.toString() );

                //order detailed image data
                JSONObject  response    = PicFoodJsonRPCImage.getFoodRatings
                (
                    state.getActivity(),
                    PicFoodFlowImage.lastImage.iImageID,
                    PicFoodFlowImage.flow.getOffset()
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse ImageFoodRatings from response
                        JSONObject          imageFoodRatings    = LibJSON.getJSONObjectSecure(  response,           "foodRatings"   );
                        PicFoodDataRating[] newFoodRatings      = PicFoodDataRating.parse(      response );
                        int                 ratingCount         = LibJSON.getJSONIntegerSecure( imageFoodRatings,   "count"         );

                        PicFoodDebug.imageProperties.out( "Parsed [" + newFoodRatings.length + "] new imageFoodRatings from response" );

                        //let the flow handle the parsed data
                        PicFoodFlowImage.flow.handleParsedData
                        (
                            state,
                            newFoodRatings,
                            ratingCount,
                            PicFoodActionPush.EPushListEntryImagePropertiesResults,
                            GridViews.EImageProperties,
                            PicFoodActionUpdate.EUpdateImagePropertiesFoodRatingsNextOffset,
                            false
                        );
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
                        actionOnTechnicalError.run();

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
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "Throwable caught on loading food ratings", UncaughtException.ENo );
                }
            }
        }
    }
