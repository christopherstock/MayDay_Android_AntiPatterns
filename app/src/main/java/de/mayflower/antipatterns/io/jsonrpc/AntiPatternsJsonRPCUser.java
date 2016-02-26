
    package de.mayflower.antipatterns.io.jsonrpc;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsSettings.JsonRPC;
    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  de.mayflower.antipatterns.idm.*;
    import  org.alexd.jsonrpc.*;
    import  org.json.*;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;

    /*****************************************************************************************
    *   3. Handles all Json-RPC-requests of the group 'user'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsJsonRPCUser
    {
        /*****************************************************************************************
        *   3.1 - Requests a logout.
        *
        *   @param  activity    The according activity context.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject logout( Activity activity ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'logout'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            JSONObject response = client.callJSONObject( "logout", AntiPatternsIDM.getSessionID(activity) );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'logout' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'logout' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'logout' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.2 - Requests to change the current active user's profile.
        *
        *   @param      activity        The according activity context.
        *   @param      newRealName     The real name to set.
        *   @param      newPhoneNumber  The phone number to set.
        *   @param      newWebsite      The website address to set.
        *   @param      newBiography    The biography to set.
        *   @return                     The response JSONObject.
        *   @throws     Throwable       If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject changeProfile
        (
            Activity    activity,
            String      newRealName,
            String      newPhoneNumber,
            String      newWebsite,
            String      newBiography
        )
        throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'changeProfile'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            JSONObject response = client.callJSONObject
            (
                "changeProfile",
                AntiPatternsIDM.getSessionID(activity),
                newRealName,
                newPhoneNumber,
                newWebsite,
                newBiography
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'changeProfile' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'changeProfile' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'changeProfile' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.3 - Requests to send feedback.
        *
        *   @param  activity    The according activity context.
        *   @param  feedback    The general feedback to send to our company.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject sendFeedback( Activity activity, String feedback ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'sendFeedback' [" + feedback + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            JSONObject response = client.callJSONObject
            (
                "sendFeedback",
                AntiPatternsIDM.getSessionID(activity),
                feedback
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'sendFeedback' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'sendFeedback' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'sendFeedback' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.4 - Requests to receive all of the user's profile data.
        *
        *   @param  activity    The according activity context.
        *   @param  userID      The ID of the user to get the profile data for.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getProfile( Activity activity, String userID ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserProfile'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            JSONObject response = client.callJSONObject
            (
                "getProfile",
                AntiPatternsIDM.getSessionID(activity),
                userID
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserProfile' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getUserProfile' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getUserProfile' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.5 - Requests to add or remove a following ( a user to follow ).
        *
        *   @param  activity        The according activity context.
        *   @param  userID          The ID of the user that shall be followed.
        *   @param  newFollowValue  <code>true</code> if this user shall be followed.
        *                           <code>false</code> if this user shall be unfollowed.
        *   @return                 The response JSONObject.
        *   @throws Throwable       If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject setFollowing( Activity activity, String userID, boolean newFollowValue ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setFollowing' [" + newFollowValue + "]" );

            JSONRPCClient   client      = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());
            JSONObject      response    = client.callJSONObject
            (
                "setFollowing",
                AntiPatternsIDM.getSessionID(activity),
                userID,
                ( newFollowValue ? AntiPatternsJsonRPC.TRUE : AntiPatternsJsonRPC.FALSE )
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setFollowing' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setFollowing' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setFollowing' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.6 - Requests to block or unblock a user.
        *
        *   @param  activity    The according activity context.
        *   @param  userID      The ID of the user to block.
        *   @param  setBlock    Specifies, if the user shall be blocked or unblocked.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject setBlockUser( Activity activity, String userID, boolean setBlock ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setBlockUser'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            JSONObject response = client.callJSONObject
            (
                "setBlockUser",
                AntiPatternsIDM.getSessionID(activity),
                userID,
                ( setBlock ? AntiPatternsJsonRPC.TRUE : AntiPatternsJsonRPC.FALSE )
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setBlockUser' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setBlockUser' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setBlockUser' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.7 - Requests to get all of the user's followers.
        *
        *   @param  activity    The according activity context.
        *   @param  userID      The ID of the user to get all followers for.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getFollowers( Activity activity, String userID, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getFollowers'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            JSONObject response = client.callJSONObject
            (
                "getFollowers",
                AntiPatternsIDM.getSessionID(activity),
                userID,
                Integer.valueOf( JsonRPC.LIMIT_RELOAD_FOLLOWERS ),  //LIMIT
                Integer.valueOf( offset                         )   //OFFSET
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getFollowers' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getFollowers' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getFollowers' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.8 - Requests to get all users the user is following.
        *
        *   @param  activity    The according activity context.
        *   @param  userID      The ID of the user to get all followings for.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getFollowing( Activity activity, String userID, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getFollowing'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            JSONObject response = client.callJSONObject
            (
                "getFollowing",
                AntiPatternsIDM.getSessionID(activity),
                userID,
                Integer.valueOf( JsonRPC.LIMIT_RELOAD_FOLLOWINGS    ),          //LIMIT
                Integer.valueOf( offset                             )           //OFFSET
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getFollowing' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getFollowing' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getFollowing' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.9 - Requests to remove the current user's account.
        *
        *   @param  activity    The according activity context.
        *   @param  password    The user's current password as a plain String.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject removeUser( Activity activity, String password ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'removeUser' [" + password + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            //password must be md5
            String passwordMD5 = LibIO.getMD5( password );

            JSONObject response = client.callJSONObject
            (
                "removeUser",
                AntiPatternsIDM.getSessionID(activity),
                passwordMD5
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'removeUser' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'removeUser' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'removeUser' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.10 - Requests to change the user's password.
        *
        *   @param  activity    The according activity context.
        *   @param  oldPassword The user's old password as a plain String.
        *   @param  newPassword The user's new password as a plain String.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject changePassword( Activity activity, String oldPassword, String newPassword ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'changeUserPassword'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            //password must be md5
            String oldPasswordMD5 = LibIO.getMD5( oldPassword );
            String newPasswordMD5 = LibIO.getMD5( newPassword );

            JSONObject response = client.callJSONObject
            (
                "changePassword",
                AntiPatternsIDM.getSessionID(activity),
                oldPasswordMD5,
                newPasswordMD5
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'changeUserPassword' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'changeUserPassword' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'changeUserPassword' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.11 - Requests to set the user's profile image.
        *
        *   @param  activity    The according activity context.
        *   @param  bitmap      The Bitmap to set as the user's new profile image.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject setProfileImage( Activity activity, Bitmap bitmap ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setProfileImage'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());

            //read image bytes
            String bitmapBase64 = "";
            if ( bitmap != null )
            {
                byte[] bytes = LibImage.getBytesFromBitmapAsJPEG( AntiPatternsSettings.Image.DEFAULT_JPEG_QUALITY, bitmap );
                bitmapBase64 = LibBase64.encodeToString( bytes );
            }

            JSONObject response = client.callJSONObject
            (
                "setProfileImage",
                AntiPatternsIDM.getSessionID(activity),
                bitmapBase64
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setProfileImage' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setProfileImage' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setProfileImage' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   3.12 - Requests to enrich the user's GCM registrationID.
        *
        *   @param  activity    The according activity context.
        *   @param  gcmRegID    The registration ID of the GCM system as received from GCM before.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject setGCMRegistrationID( Context activity, String gcmRegID ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setGCMRegistrationID'" );

            JSONRPCClient   client   = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCUser());
            JSONObject      response = client.callJSONObject
            (
                "setGCMRegistrationID",
                AntiPatternsIDM.getSessionID(activity),
                gcmRegID
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'setGCMRegistrationID' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setGCMRegistrationID' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'setGCMRegistrationID' - message:   [" + message                + "]" );

            return response;
        }
    }
