
    package de.mayflower.antipatterns.flow;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.GridViews;
    import  org.json.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Specifies the flow for image-ratings tasks.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowImageRating
    {
        /**********************************************************************************************
        *   Handles the 'rateFood'-task.
        **********************************************************************************************/
        public static final void rateFood()
        {
            try
            {
                //commit rate request
                JSONObject  response    = AntiPatternsJsonRPCImage.rateFood(AntiPatternsFlowImage.lastState.getActivity(), AntiPatternsFlowImage.lastImage.iImageID, AntiPatternsFlowImage.lastFoodRating);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );

                        //unselect image buttons
                        AntiPatternsActionUnselect.EUnselectButtonsImage.run();

                        //update according image list after image has been changed
                        AntiPatternsActionUpdate.EUpdateImageListAfterImageChange.run();

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(AntiPatternsFlowImage.lastState);
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    default:
                    {
                        //unexpected return code - show 'technical error' too

                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );
                        AntiPatternsActionDialog.EDialogRateFoodTechnicalError.run();

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
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );
                    AntiPatternsActionDialog.EDialogRateFoodNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog
                    LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );

                    //unselect image buttons
                    AntiPatternsActionUnselect.EUnselectButtonsImage.run();

                    //do NOT show 'technical error' but report this exception
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
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
                AntiPatternsDebug.limitOffset.out( "BEFORE ordering ratings\n" + AntiPatternsFlowImage.flow.toString() );

                //order detailed image data
                JSONObject  response    = AntiPatternsJsonRPCImage.getFoodRatings
                        (
                                state.getActivity(),
                                AntiPatternsFlowImage.lastImage.iImageID,
                                AntiPatternsFlowImage.flow.getOffset()
                        );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //parse ImageFoodRatings from response
                        JSONObject          imageFoodRatings    = LibJSON.getJSONObjectSecure(  response,           "foodRatings"   );
                        AntiPatternsDataRating[] newFoodRatings      = AntiPatternsDataRating.parse(response);
                        int                 ratingCount         = LibJSON.getJSONIntegerSecure( imageFoodRatings,   "count"         );

                        AntiPatternsDebug.imageProperties.out( "Parsed [" + newFoodRatings.length + "] new imageFoodRatings from response" );

                        //let the flow handle the parsed data
                        AntiPatternsFlowImage.flow.handleParsedData
                        (
                            state,
                            newFoodRatings,
                            ratingCount,
                            AntiPatternsActionPush.EPushListEntryImagePropertiesResults,
                            GridViews.EImageProperties,
                            AntiPatternsActionUpdate.EUpdateImagePropertiesFoodRatingsNextOffset,
                            false
                        );
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
                        actionOnTechnicalError.run();

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
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    //AntiPatternsDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Error on sending feedback" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "Throwable caught on loading food ratings", UncaughtException.ENo);
                }
            }
        }
    }
