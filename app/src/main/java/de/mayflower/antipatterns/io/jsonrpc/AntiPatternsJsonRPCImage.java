
    package de.mayflower.antipatterns.io.jsonrpc;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Debug.Simulations;
    import  de.mayflower.antipatterns.AntiPatternsSettings.JsonRPC;
    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  org.alexd.jsonrpc.*;
    import  org.json.*;
    import  android.app.*;
    import  android.graphics.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;

    /*****************************************************************************************
    *   4. Handles all Json-RPC-requests of the group 'image'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsJsonRPCImage
    {
        /*****************************************************************************************
        *   4.1 - Requests to submit an image.
        *
        *   @param  activity    The according activity context.
        *   @param  location    The location where this image has been taken.
        *   @param  bitmap      The bitmap data that shall be submitted for this image.
        *   @param  comment     An initial comment that has been entered for this image.
        *   @param  googlePlace The picked according GooglePlace-entry for this image.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject postImage
        (
            Activity                activity,
            AntiPatternsDataLocation location,
            Bitmap                  bitmap,
            String                  comment,
            AntiPatternsDataGooglePlace googlePlace
        )
        throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'postImage'" ); // - Set location [" + userLocation + "][" + ( userLocation != null ? userLocation.iLatitude : 0.0 ) + "][" + ( userLocation != null ? userLocation.iLongitude : 0.0 ) + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //read image bytes
            byte[] bytes = LibImage.getBytesFromBitmapAsJPEG( AntiPatternsSettings.Image.DEFAULT_JPEG_QUALITY, bitmap );
            String base64 = LibBase64.encodeToString( bytes );

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "postImage",
                AntiPatternsIDM.getSessionID(activity),
                base64,
                comment,
                ( location != null ? String.valueOf( location.iLatitude         ) : "" ),
                ( location != null ? String.valueOf( location.iLongitude        ) : "" ),
                ( googlePlace  != null ? String.valueOf( googlePlace.iID                ) : "" ),
                ( googlePlace  != null ? String.valueOf( googlePlace.iName              ) : "" ),
                ( googlePlace  != null ? String.valueOf( googlePlace.iVicinity          ) : "" ),
                ( googlePlace  != null ? String.valueOf( googlePlace.iIconUrl           ) : "" ),
                ( googlePlace  != null ? String.valueOf( googlePlace.iLatitude          ) : "" ),
                ( googlePlace  != null ? String.valueOf( googlePlace.iLongitude         ) : "" )
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'postImage' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"        );
            String  message     = LibJSON.getJSONStringSecure( response, "message"       );
            String  imageId     = LibJSON.getJSONStringSecure( response, "imageId"       );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'postImage' - status:    [" + status           + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'postImage' - message:   [" + message          + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'postImage' - imageId:   [" + imageId          + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.2 - Requests to remove a user's image via Json-RPC.
        *
        *   @param  activity    The according activity context.
        *   @param  imageID     The ID of the image to remove.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject removeImage( Activity activity, String imageID ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'removeImage'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "removeImage",
                AntiPatternsIDM.getSessionID(activity),
                imageID
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'removeImage' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'removeImage' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'removeImage' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.3 - Requests to post a comment for an image.
        *
        *   @param  activity    The according activity context.
        *   @param  imageID     The ID of the image to post a comment for.
        *   @param  comment     The comment to submit for the given image-ID.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject postComment( Activity activity, String imageID, String comment ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'postComment'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "postComment",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                comment
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'postComment' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"        );
            String  message     = LibJSON.getJSONStringSecure( response, "message"       );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'postComment' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'postComment' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.4 - Requests to post a like or an unlike for an image.
        *
        *   @param  activity        The according activity context.
        *   @param  newLikeStatus   <code>true</code> to commit a like
        *                           and <code>false</code> to commit an unlike,
        *   @param  imageID         The ID of the image to like or unlike.
        *   @return                 The response JSONObject.
        *   @throws Throwable       If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject likeImage( Activity activity, boolean newLikeStatus, String imageID ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'likeImage' [" + imageID + "][" + newLikeStatus + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "likeImage",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                ( newLikeStatus ? AntiPatternsJsonRPC.TRUE : AntiPatternsJsonRPC.FALSE )
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'likeImage' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'likeImage' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'likeImage' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.5 - Requests to get the image likes.
        *
        *   @param  activity    The according activity context.
        *   @param  imageID     The ID of the image to return all likes for.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getImageLikes( Activity activity, String imageID, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getImageLikes'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "getImageLikes",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                Integer.valueOf( JsonRPC.LIMIT_RELOAD_LIKES ),   // LIMIT
                Integer.valueOf( offset                     )    // OFFSET
            );

            if ( Simulations.SIMULATE_GET_IMAGE_LIKES )
            {
                response = new JSONObject( "{\"message\":\"success\",\"imageLikes\":{\"count\":24,\"likes\":[{\"owner\":{\"userName\":\"simulated1\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"3\"},{\"owner\":{\"userName\":\"simulated2\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"4\"},{\"owner\":{\"userName\":\"simulated3\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"5\"},{\"owner\":{\"userName\":\"simulated4\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"2\"},{\"owner\":{\"userName\":\"simulated5\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"3\"},{\"owner\":{\"userName\":\"simulated6\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"4\"},{\"owner\":{\"userName\":\"simulated7\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"5\"},{\"owner\":{\"userName\":\"simulated8\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"2\"},{\"owner\":{\"userName\":\"simulated9\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"1\"},{\"owner\":{\"userName\":\"simulated10\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"4\"}]},\"status\":\"100\"}" );
            }

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getImageLikes' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getImageLikes' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getImageLikes' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.6 - Requests to rate the food being shown on an image.
        *
        *   @param  activity    The according activity context.
        *   @param  imageID     The ID of the image to rate food.
        *   @param  rating      The rating value to submit for this food.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject rateFood( Activity activity, String imageID, Integer rating ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'rateFood'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "rateFood",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                rating
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'rateFood' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'rateFood' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'rateFood' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.7 - Requests to get food ratings for one image.
        *
        *   @param  activity    The according activity context.
        *   @param  imageID     The ID of the image to get food ratings for.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getFoodRatings( Activity activity, String imageID, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getFoodRatings'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "getFoodRatings",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                Integer.valueOf( JsonRPC.LIMIT_RELOAD_FOOD_RATINGS  ),      // LIMIT
                Integer.valueOf( offset                             )       // OFFSET
            );

            if ( Simulations.SIMULATE_GET_IMAGE_FOOD_RATINGS )
            {
                response = new JSONObject( "{\"message\":\"success\",\"foodRatings\":{\"count\":24,\"ratings\":[{\"owner\":{\"userName\":\"simulated1\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"1\"},{\"owner\":{\"userName\":\"simulated2\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"2\"},{\"owner\":{\"userName\":\"simulated3\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"3\"},{\"owner\":{\"userName\":\"simulated4\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"4\"},{\"owner\":{\"userName\":\"simulated5\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"5\"},{\"owner\":{\"userName\":\"simulated6\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"4\"},{\"owner\":{\"userName\":\"simulated7\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"3\"},{\"owner\":{\"userName\":\"simulated8\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"2\"},{\"owner\":{\"userName\":\"simulated9\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"1\"},{\"owner\":{\"userName\":\"simulated10\",\"profileImageUrl\":\"\",\"realName\":null,\"userId\":470},\"postedAt\":\"1372276067\",\"vote\":\"2\"}]},\"status\":\"100\"}" );
            }

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getFoodRatings' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getFoodRatings' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getFoodRatings' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.8 - Requests to get comments for one image.
        *
        *   @param  activity    The according activity context.
        *   @param  imageID     The ID of the image to get comments for.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getComments( Activity activity, String imageID, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getComments'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "getComments",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                Integer.valueOf( JsonRPC.LIMIT_RELOAD_COMMENTS  ),   // LIMIT
                Integer.valueOf( offset                         )    // OFFSET
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getComments' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getComments' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getComments' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.9 - Requests to get all DETAILED information for one image.
        *
        *   @param  activity    The according activity context.
        *   @param  imageID     The ID of the image to get all information for.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getImage( Activity activity, String imageID ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getImage'" );

            JSONRPCClient   client              = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getImage'" );

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "getImage",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_COMMENTS             ),      // COMMENT-LIMIT
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_FOOD_RATINGS         ),      // RATINGS-LIMIT
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_LIKES                )       // LIKES-LIMIT
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getImage' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getImage' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getImage' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.10 - Requests to get all images for one user.
        *
        *   @param  activity    The according activity context.
        *   @param  userID      The ID of the user to get all images for.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getUserImages( Activity activity, String userID, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserImages'" );

            JSONRPCClient   client              = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserImages'" );

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "getUserImages",
                AntiPatternsIDM.getSessionID(activity),
                userID,
                Integer.valueOf( JsonRPC.LIMIT_IMAGES_USER                  ),      // LIMIT IMAGE
                Integer.valueOf( offset                                     ),      // OFFSET IMAGE
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_COMMENTS             ),      // LIMIT COMMENT
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_FOOD_RATINGS         ),      // LIMIT FOOD-RATINGS
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_LIKES                )       // LIMIT LIKES
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserImages' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getUserImages' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getUserImages' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.11 - Requests to get all startup information for one image.
        *
        *   @param  activity    The according activity context.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getUserWall( Activity activity, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserWall'" );

            JSONRPCClient   client              = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserWall'" );

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "getUserWall",
                AntiPatternsIDM.getSessionID(activity),
                Integer.valueOf( JsonRPC.LIMIT_IMAGES_WALL          ),          // LIMIT IMAGE
                Integer.valueOf( offset                             ),          // OFFSET IMAGE
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_COMMENTS     ),          // LIMIT COMMENT
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_FOOD_RATINGS ),          // LIMIT RATINGS
                Integer.valueOf( JsonRPC.LIMIT_INITIAL_LIKES        )           // LIMIT LIKES
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getUserWall' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getUserWall' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getUserWall' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.12 - Requests to send feedback for one image to our company. ( Abuse, Spam etc. ).
        *
        *   @param  activity    The according activity context.
        *   @param  category    The category group for this feedback.
        *   @param  imageID     The ID of the image to submit this feedback for.
        *   @param  report      The user's feedback to submit for this image.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject sendImageFeedback( Activity activity, String category, String imageID, String report ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'sendImageFeedback'" );

            JSONRPCClient   client              = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'sendImageFeedback'" );

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "sendImageFeedback",
                AntiPatternsIDM.getSessionID(activity),
                imageID,
                category,               // category
                report                  // text
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'sendImageFeedback' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'sendImageFeedback' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'sendImageFeedback' - message:   [" + message                      + "]" );

            return response;
        }

        /*****************************************************************************************
        *   4.13 - Requests to get the explore data for the current user.
        *
        *   @param  activity    The according activity context.
        *   @param  offset      The database offset of the results to pick.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getExplore( Activity activity, int offset ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getExplore'" );

            JSONRPCClient   client              = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCImage());

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getExplore'" );

            //execute the response
            JSONObject response = client.callJSONObject
            (
                "getExplore",
                AntiPatternsIDM.getSessionID(activity),
                Integer.valueOf( JsonRPC.LIMIT_IMAGES_EXPLORE   ),   // LIMIT
                Integer.valueOf( offset                         )    // OFFSET
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'getExplore' - object received [" + response + "]" );

            String  status      = LibJSON.getJSONStringSecure( response, "status"      );
            String  message     = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getExplore' - status:    [" + status                       + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'getExplore' - message:   [" + message                      + "]" );

            return response;
        }
    }
