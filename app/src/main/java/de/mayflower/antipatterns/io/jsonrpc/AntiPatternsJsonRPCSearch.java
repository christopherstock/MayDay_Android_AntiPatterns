
    package de.mayflower.antipatterns.io.jsonrpc;

    import  de.mayflower.antipatterns.*;
    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import de.mayflower.antipatterns.AntiPatternsSettings.*;
    import  de.mayflower.antipatterns.idm.*;
    import  org.alexd.jsonrpc.*;
    import  org.json.*;
    import  android.app.*;
    import  de.mayflower.lib.io.*;

    /*****************************************************************************************
    *   5. Handles all Json-RPC-requests of the group 'search'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsJsonRPCSearch
    {
        /*****************************************************************************************
        *   5.1 - Requests to find users by their phone number.
        *
        *   @param  activity        The according activity context.
        *   @param  phoneNumbers    All phone numbers that shall be used to search for other users.
        *   @param  offset          The database offset of the results to pick.
        *   @return                 The response JSONObject.
        *   @throws Throwable       If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject searchUsersByPhonenumber
        (
            Activity        activity,
            String[]        phoneNumbers,
            int             offset
        )
        throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchUsersByPhonenumber'" );

            JSONRPCClient   client              = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCSearch());
            JSONArray       jsonPhoneNumbers    = LibJSON.toJSONArray( phoneNumbers );

            JSONObject response = client.callJSONObject
            (
                "searchUsersByPhonenumber",
                AntiPatternsIDM.getSessionID(activity),
                jsonPhoneNumbers,
                Integer.valueOf( JsonRPC.LIMIT_USERS_FIND   ),          // limit
                Integer.valueOf( offset                     )           // offset
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchUsersByPhonenumber' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchUsersByPhonenumber' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchUsersByPhonenumber' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   5.2 - Requests to find users by their facebook-ID.
        *
        *   @param  activity        The according activity context.
        *   @param  facebookIDs     All facebook-IDs of the user's facebook-friends.
        *   @param  ownFacebookID   The user's facebook ID.
        *   @param  offset          The database offset of the results to pick.
        *   @return                 The response JSONObject.
        *   @throws Throwable       If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject searchUsersByFacebookID( Activity activity, String[] facebookIDs, String ownFacebookID, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchUsersByFacebookID'" );

            JSONRPCClient   client          = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCSearch());
            JSONArray       jsonFacebookIDs = LibJSON.toJSONArray( facebookIDs );

            JSONObject response = client.callJSONObject
            (
                "searchUsersByFacebookId",
                AntiPatternsIDM.getSessionID(activity),
                jsonFacebookIDs,
                ownFacebookID,
                Integer.valueOf( JsonRPC.LIMIT_USERS_FIND   ),          // limit
                Integer.valueOf( offset                     )           // offset
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchUsersByFacebookID' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchUsersByFacebookID' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchUsersByFacebookID' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   5.3 - Requests to find images by tags and geo-location.
        *
        *   @param  activity    The according activity context.
        *   @param  term        The term to search images with.
        *   @param  latitude    The current latitude coordinate of the user's location.
        *   @param  longitude   The current longitude coordinate of the user's location.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject searchImagesByTagsAndOrLocationCoordinates
        (
            Activity    activity,
            String      term,
            String      latitude,
            String      longitude,
            int         offset
        )
        throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchImagesByTagsAndOrLocationCoordinates' lat [" + latitude + "] lon [" + longitude + "]" );

            JSONRPCClient   client      = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCSearch());
            JSONObject      response    = client.callJSONObject
            (
                "searchImagesByTagsAndOrLocationCoordinates",
                AntiPatternsIDM.getSessionID(activity),
                term,                                                                           // tag term
                latitude,
                longitude,
                Integer.valueOf( AntiPatternsSettings.General.SEARCH_RADIUS_USE_SERVERSIDE   ),
                Integer.valueOf( JsonRPC.LIMIT_IMAGES_SEARCH                            ),
                Integer.valueOf( offset                                                 ),      // images offset
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_COMMENTS                         ),
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_FOOD_RATINGS                     ),
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_LIKES                            )
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchImagesByTagsAndOrLocationCoordinates' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchImagesByTagsAndOrLocationCoordinates' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchImagesByTagsAndOrLocationCoordinates' - message:   [" + message                + "]" );

            return response;
        }

        /*****************************************************************************************
        *   5.4 - Requests to find users by name or real name.
        *
        *   @param  activity    The according activity context.
        *   @param  searchTerm  The term to find in usernames.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject searchUsersByTerm( Activity activity, String searchTerm, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchUsersByTerm' [" + searchTerm + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCSearch());

            JSONObject response = client.callJSONObject
            (
                "searchUsersByTerm",
                AntiPatternsIDM.getSessionID(activity),
                searchTerm,
                Integer.valueOf( JsonRPC.LIMIT_USERS_FIND   ),          // limit
                Integer.valueOf( offset                     )           // offset
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'searchUsersByTerm' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchUsersByTerm' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'searchUsersByTerm' - message:   [" + message                + "]" );

            return response;
        }
    }
