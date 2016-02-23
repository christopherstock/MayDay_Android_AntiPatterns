/*  $Id: PicFoodFlowProfileFollow.java 50565 2013-08-12 13:05:39Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.data.*;
    import  net.picfood.flow.ui.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.jsonrpc.*;
    import  net.picfood.state.*;
    import  net.picfood.ui.adapter.PicFoodAdapterManager.GridViews;
    import  org.json.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.dialog.*;
    import  com.synapsy.android.lib.util.*;
    import  com.synapsy.android.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Manages the followers / followings data.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50565 $ $Date: 2013-08-12 15:05:39 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowProfileFollow.java $"
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
