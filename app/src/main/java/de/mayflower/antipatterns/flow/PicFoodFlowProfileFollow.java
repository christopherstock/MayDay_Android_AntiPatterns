
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.flow.ui.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.GridViews;
    import  org.json.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Manages the followers / followings data.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodFlowProfileFollow
    {
        /** The user that orderes followings or followers data. */
        public      static      PicFoodDataUser                     lastUser                        = null;

        /** The last activity where detailed follow data has been ordered. */
        public      static      LibState                            lastState                       = null;

        /** Encapsulates the GridView flow according to the Strategy pattern. */
        public      static      PicFoodGridViewFlow                 flow                            = null;

        /**********************************************************************************************
        *   Shows all people that follow the current selected user.
        *
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        **********************************************************************************************/
        public static final void orderNextFollowers
        (
            Runnable    actionOnNoNetwork,
            Runnable    actionOnTechnicalError
        )
        {
            PicFoodDebug.followship.out( PicFoodFlowProfile.class + "::orderNextFollowers() [" + PicFoodFlowProfile.profileDetailsEnteredVia + "]" );

            //order followers
            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering followers\n" + flow.toString() );

                //order detailed image data
                JSONObject  response    = PicFoodJsonRPCUser.getFollowers
                (
                    PicFoodState.EFollowDetails.getActivity(),
                    lastUser.iUserID,
                    flow.getOffset()
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse followers from response
                        PicFoodDataUser[]   newUsers        = PicFoodDataUser.parse( response );
                        int                 count           = LibJSON.getJSONIntegerSecure( response, "count" );

                        PicFoodDebug.followUsers.out( "Parsed [" + newUsers.length + "] users from response, count is always 0: [" + count + "]" );

                        //let the flow handle the parsed data
                        flow.handleParsedData
                        (
                            PicFoodState.EFollowDetails,
                            newUsers,
                            count,
                            PicFoodActionPush.EPushListEntryFollowDetails,
                            GridViews.EFollowDetails,
                            PicFoodActionUpdate.EUpdateFollowersDetailsNextOffset,
                            false
                        );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.EFollowDetails );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EFollowDetails.getActivity() );
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
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EFollowDetails.getActivity() );
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EFollowDetails.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "Throwable caught on showing followers", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Shows all people that follow the current selected user.
        *
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        **********************************************************************************************/
        public static final void orderNextFollowings
        (
            Runnable    actionOnNoNetwork,
            Runnable    actionOnTechnicalError
        )
        {
            PicFoodDebug.followship.out( PicFoodFlowProfile.class + "::orderNextFollowings() [" + PicFoodFlowProfile.profileDetailsEnteredVia + "]" );

            //order followings
            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out( "BEFORE ordering followings\n" + flow.toString());

                //order detailed image data
                JSONObject  response    = PicFoodJsonRPCUser.getFollowing
                (
                    PicFoodState.EFollowDetails.getActivity(),
                    lastUser.iUserID,
                    flow.getOffset()
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse followings from response
                        PicFoodDataUser[]   newUsers    = PicFoodDataUser.parse( response );
                        int                 count       = LibJSON.getJSONIntegerSecure( response, "count" );

                        PicFoodDebug.followUsers.out( "Parsed [" + newUsers.length + "] users from response, count is always 0: [" + count + "]" );

                        //let the flow handle the parsed data
                        flow.handleParsedData
                        (
                            PicFoodState.EFollowDetails,
                            newUsers,
                            count,
                            PicFoodActionPush.EPushListEntryFollowDetails,
                            GridViews.EFollowDetails,
                            PicFoodActionUpdate.EUpdateFollowingsDetailsNextOffset,
                            false
                        );
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodState.EFollowDetails );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EFollowDetails.getActivity() );
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
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EFollowDetails.getActivity() );
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EFollowDetails.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    PicFoodDebug.DEBUG_THROWABLE( t, "Throwable caught on showing followings", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data for the followings.
        *   Should be invoked before a clean update of the image properties 'followings' is performed.
        *
        *   @param  newCount    The total count of followers / followings to initialize the current flow with.
        **********************************************************************************************/
        public static final void reset( int newCount )
        {
            flow = new PicFoodGridViewFlow( newCount );
        }
    }
