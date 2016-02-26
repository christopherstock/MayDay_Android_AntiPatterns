/*  $Id: PicFoodFacebookJob.java 50537 2013-08-08 15:35:28Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.ext.facebook;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.flow.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.*;
    import  net.picfood.state.*;
    import  net.picfood.state.auth.*;
    import  org.json.*;
    import  android.graphics.drawable.*;
    import  android.os.*;
    import  com.facebook.*;
    import  com.facebook.model.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /*****************************************************************************
    *   Performs all facebook jobs.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50537 $ $Date: 2013-08-08 17:35:28 +0200 (Do, 08 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/ext/facebook/PicFoodFacebookJob.java $"
    *****************************************************************************/
    public class PicFoodFacebookJob
    {
        /*****************************************************************************
        *   Requests all information about the current logged in user.
        *
        *   @param  state       The according state.
        *   @throws Throwable   If any error occurs in this flow.
        *****************************************************************************/
        public static final void requestUserInfo( final LibState state ) throws Throwable
        {
            //create request query - query user info
            String fqlQuery = "SELECT uid, name, pic_square FROM user WHERE uid = me();";

            //query all friends
            //String fqlQuery = "SELECT uid, name, pic_square FROM user WHERE uid IN ( SELECT uid2 FROM friend WHERE uid1 = me() )"; // LIMIT 25
            //query logged in user
            //String fqlQuery = "SELECT uid, name, pic_square FROM user WHERE uid = me()";
            //String fqlQuery = "SELECT uid,name,first_name,middle_name,last_name,gender,locale,languages,link,username,age_range,third_party_id,installed,timezone,updated_time,verified,bio,birthday,cover,currency,devices,education,email,hometown,interested_in,location,political,payment_pricepoints,favorite_athletes,favorite_teams,picture,quotes,relationship_status,religion,security_settings,significant_other,video_upload_limits,website,work FROM user WHERE uid = me()";
            //String fqlQuery = "SELECT uid,name,about,address,age_range,bio,birthday,cover,currency,devices,education,email,favorite_athletes,favorite_teams,first_name,gender,hometown,inspirational_people,install_type,installed,interested_in,languages,last_name,link,locale,location,meeting_for,middle_name,name_format,political,quotes,religion,security_settings,relationship_status,significant_other,sports,third_party_id,timezone,updated_time,username,verified,video_upload_limits,viewer_can_send_gift,website,work FROM user WHERE uid = me()";

            //create request params
            final Bundle params = new Bundle();
            params.putString( "q", fqlQuery );

            //execute request
            Request.executeBatchAsync
            (
                new Request
                (
                    Session.getActiveSession(),
                    "/fql",
                    params,
                    HttpMethod.GET,
                    new Request.Callback()
                    {
                        @Override
                        public void onCompleted( final Response response )
                        {
                            try
                            {
                                PicFoodDebug.facebook.out( "GraphUserCallback: [" + response + "]" );

                                      GraphObject   go      = response.getGraphObject();
                                      JSONObject    jso     = go.getInnerJSONObject();
                                      JSONArray     arr     = LibJSON.getJSONArraySecure( jso, "data" );
                                      JSONObject    me      = arr.getJSONObject( 0 );

                                      String        uid     = LibJSON.getJSONStringSecure( me, "uid"         );
                                      String        name    = LibJSON.getJSONStringSecure( me, "name"        );
                                final String        iconURL = LibJSON.getJSONStringSecure( me, "pic_square"  );

                                PicFoodDebug.facebook.out( "Read data [" + uid + "][" + name + "][" + iconURL + "]" );

                                //assign facebook UID
                                PicFoodIDM.assignFacebookUID( state.getActivity(), uid );

                                //fill the fields in the register-state
                                PicFoodFlowRegister.handlerRealName.setTextUIThreaded(  state.getActivity(), name  );
                              //PicFoodFlowRegister.handlerPhone.setTextUIThreaded(     state.getActivity(), phone );

                                //order the icon in a new thread
                                new Thread()
                                {
                                    @Override
                                    public void run()
                                    {
                                        try
                                        {
                                            //stream or cache icon
                                            BitmapDrawable bd = PicFoodCache.getBitmapDrawableFromHttpOrCache( PicFoodState.ERegister.getActivity(), iconURL );
                                            PicFoodStateRegister.assignPickedImageUIThreaded( bd.getBitmap() );

                                            //dismiss progress dialog
                                            LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                                        }
                                        catch ( Throwable t )
                                        {
                                            //dismiss progress dialog
                                            LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                                        }
                                    }
                                }.start();
                            }
                            catch ( Throwable t )
                            {
                                //dismiss progress dialog
                                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                                PicFoodDebug.DEBUG_THROWABLE( t, "Being raised on requesting facebook request [requestUserInfo]!", UncaughtException.ENo );
                            }
                        }
                    }
                )
            );
        }

        /*****************************************************************************
        *   Requests the ID of the currently logged in user.
        *
        *   @param  state       The according state.
        *   @throws Throwable   If any error occurs in this flow.
        *****************************************************************************/
        public static final void requestOwnID( final LibState state ) throws Throwable
        {
            //create request query - query all friends
            String fqlQuery = "SELECT uid, name, pic_square FROM user WHERE uid = me();";

            //create request params
            final Bundle params = new Bundle();
            params.putString( "q", fqlQuery );

            //execute request
            Request.executeBatchAsync
            (
                new Request
                (
                    Session.getActiveSession(),
                    "/fql",
                    params,
                    HttpMethod.GET,
                    new Request.Callback()
                    {
                        @Override
                        public void onCompleted( final Response response )
                        {
                            try
                            {
                                PicFoodDebug.facebook.out( "GraphUserCallback: [" + response + "]" );

                                GraphObject     go      = response.getGraphObject();
                                JSONObject      jso     = go.getInnerJSONObject();
                                JSONArray       arr     = LibJSON.getJSONArraySecure( jso, "data" );
                                JSONObject      me      = arr.getJSONObject( 0 );
                                String          uid     = LibJSON.getJSONStringSecure( me, "uid"         );

                                PicFoodDebug.facebook.out( "Read own uid: [" + uid + "]" );

                                //assign facebook UID
                                PicFoodIDM.assignFacebookUID( state.getActivity(), uid );

                                //order all friend-IDs
                                requestFriendsIDs( state, uid );
                            }
                            catch ( Throwable t )
                            {
                                //dismiss progress dialog
                                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                                PicFoodDebug.DEBUG_THROWABLE( t, "Being raised on requesting facebook request [requestFriendsIDs]!", UncaughtException.ENo );
                            }
                        }
                    }
                )
            );
        }

        /*****************************************************************************
        *   Requests all ids from all friends.
        *
        *   @param  state           The according state.
        *   @param  ownFacebookID   The user's facebook ID.
        *   @throws Throwable       If any error occurs in this flow.
        *****************************************************************************/
        public static final void requestFriendsIDs( final LibState state, final String ownFacebookID ) throws Throwable
        {
            //create request query - query all friends
            String fqlQuery = "SELECT uid FROM user WHERE uid IN ( SELECT uid2 FROM friend WHERE uid1 = me() )";

            //create request params
            final Bundle params = new Bundle();
            params.putString( "q", fqlQuery );

            //execute request
            Request.executeBatchAsync
            (
                new Request
                (
                    Session.getActiveSession(),
                    "/fql",
                    params,
                    HttpMethod.GET,
                    new Request.Callback()
                    {
                        @Override
                        public void onCompleted( final Response response )
                        {
                            try
                            {
                                PicFoodDebug.facebook.out( "GraphUserCallback: [" + response + "]" );

                                GraphObject     go      = response.getGraphObject();
                                JSONObject      jso     = go.getInnerJSONObject();
                                JSONArray       arr     = LibJSON.getJSONArraySecure( jso, "data" );
                                final String[]  uids    = new String[ arr.length() ];
                                for ( int i = 0; i < arr.length(); ++i )
                                {
                                    JSONObject  me          = arr.getJSONObject( i );
                                    String      uid         = LibJSON.getJSONStringSecure( me, "uid"         );
                                                uids[ i ]   = uid;

                                    PicFoodDebug.facebook.out( "Read data #[" + i + "] [" + uid + "]" );
                                }

                                //order the search in a new thread
                                new Thread()
                                {
                                    @Override
                                    public void run()
                                    {
                                        //assign the last facebook-uids
                                        PicFoodFlowSearchUsers.lastFacebookUIDs = uids;

                                        //clean search data
                                        PicFoodFlowSearchUsers.reset();

                                        PicFoodFlowSearchUsers.nextReloadAction     = PicFoodActionUpdate.EUpdateFindFriendsViaFacebookNextOffset;
                                        PicFoodFlowSearchUsers.lastOwnFacebookID    = ownFacebookID;

                                        //perform 1st friends search
                                        PicFoodFlowSearchUsers.orderNextFriendsSearchViaFacebookIDs
                                        (
                                            PicFoodState.ESettings,
                                            PicFoodActionDialog.EDialogFindFriendsViaFacebookNoNetwork,
                                            PicFoodActionDialog.EDialogFindFriendsViaFacebookTechnicalError,
                                            PicFoodActionDialog.EDialogFindFriendsViaFacebookNoResults,
                                            PicFoodActionUpdate.EUpdateFindFriendsViaFacebookShowResults
                                        );
                                    }
                                }.start();
                            }
                            catch ( Throwable t )
                            {
                                //dismiss progress dialog
                                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                                PicFoodDebug.DEBUG_THROWABLE( t, "Being raised on requesting facebook request [requestFriendsIDs]!", UncaughtException.ENo );
                            }
                        }
                    }
                )
            );
        }
    }
