
    package de.mayflower.antipatterns;

    import de.mayflower.lib.*;

    /***************************************************************************************************
    *   Holds version information.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ***************************************************************************************************/
    public class PicFoodSettings
    {
        /*********************************************************************************
        *   General adjustments that do not fit in one settings group.
        *********************************************************************************/
        public static final class General
        {
            /** The minimum time between two update checks. */
            public      static  final   long        UPDATE_CHECK_PERIOD                         = Lib.MILLIS_PER_DAY;

            /** The search radius that means, that the serversided search radius shall be used. */
            public      static  final   int         SEARCH_RADIUS_USE_SERVERSIDE                = -1;
        }

        /*********************************************************************************
        *   Optimization #2
        *
        *   Specifies time distance for updating contents of the wall, the explore area
        *   and the own profile. Reloading these states each time they are entered is history.
        *********************************************************************************/
        public static final class ContentUpdatePeriods
        {
            /** The minimum time between automatically updating the user wall on returning to the state. */
            public      static  final   long        PERIOD_UPDATE_WALL                          = 10 * Lib.MILLIS_PER_MINUTE;
            /** The minimum time between automatically updating the explore area on returning to the state. */
            public      static  final   long        PERIOD_UPDATE_EXPLORE                       = 10 * Lib.MILLIS_PER_MINUTE;
            /** The minimum time between automatically updating the own profile on returning to the state. */
            public      static  final   long        PERIOD_UPDATE_OWN_PROFILE                   = 10 * Lib.MILLIS_PER_MINUTE;
        }

        /*********************************************************************************
        *   Adjustments for all images.
        *********************************************************************************/
        public static final class Images
        {
            /** Maximum image size for uploaded images. */
            public      static  final   int         MAX_IMAGE_SIZE                              = 480;

            /** Maximum image size for uploaded icons. */
            public      static  final   int         MAX_ICON_SIZE                               = 160;

            /** The aspect ratio for the horizontal size of the image. */
            public      static  final   int         IMAGE_ASPECT_X                              = 1;

            /** The aspect ratio for the vertical size of the image. */
            public      static  final   int         IMAGE_ASPECT_Y                              = 1;

            /** The aspect ratio for the horizontal size of the icon. */
            public      static  final   int         ICON_ASPECT_X                               = 1;

            /** The aspect ratio for the vertical size of the icon. */
            public      static  final   int         ICON_ASPECT_Y                               = 1;
        }

        /*********************************************************************************
        *   GPS settings.
        *********************************************************************************/
        public static final class Gps
        {
            /** The timeout for the GPS location scan. */
            public      static  final   long        GPS_LOCATION_SEARCH_TIMEOUT_MILLIS          = 30 * Lib.MILLIS_PER_SECOND;
            /** The maximum cache time for the last scanned GPS location. */
            public      static  final   long        GPS_CACHE_TIME_MILLIS                       = 10 * Lib.MILLIS_PER_MINUTE;
        }

        /*********************************************************************************
        *   All request-id this app propagates to
        *   {@link android.app.Activity#startActivityForResult(android.content.Intent, int)}.
        *********************************************************************************/
        public static final class ActivityRequestID
        {
            /** Return from Activity 'gallery' that picked an image for state 'new entry'. */
            public      static  final   int         STATE_NEW_ENTRY_PICK_UPLOAD_IMAGE_GALLERY   = 1;
            /** Return from Activity 'camera' that picked an image for state 'new entry'. */
            public      static  final   int         STATE_NEW_ENTRY_PICK_UPLOAD_IMAGE_CAMERA    = 2;
            /** Return from Activity 'image cropper' that cropped an image for state 'new entry'. */
            public      static  final   int         STATE_NEW_ENTRY_CROP_IMAGE                  = 3;
            /** Return from Activity 'gallery' that picked an image for state 'register'. */
            public      static  final   int         STATE_REGISTER_PICK_IMAGE_FROM_GALLERY      = 4;
            /** Return from Activity 'image cropper' that cropped an image for state 'register'. */
            public      static  final   int         STATE_REGISTER_CROP_IMAGE                   = 5;
            /** Return from Activity 'gallery' that picked an image for state 'settings'. */
            public      static  final   int         STATE_SETTINGS_PICK_IMAGE_FROM_GALLERY      = 6;
            /** Return from Activity 'image cropper' that cropped an image for state 'settings'. */
            public      static  final   int         STATE_SETTINGS_CROP_IMAGE                   = 7;
        }

        /*********************************************************************************
        *   All settings for the Json-RPC-connection system.
        *********************************************************************************/
        public static final class JsonRPC
        {
            /** The default encoding for all JSON-RPC requests. */
            public      static  final   String      ENCODING                                    = "UTF-8";
            /** The connection timeout in ms for all JSON-RPC requests. */
            public      static  final   int         TIMEOUT_CONNECTION                          = (int)( 5  * Lib.MILLIS_PER_SECOND );
            /** The socket operation timeout in ms for all JSON-RPC requests. */
            public      static  final   int         TIMEOUT_S_O                                 = (int)( 30 * Lib.MILLIS_PER_SECOND );

            /** The number of images per request to load for the wall. */
            public      static  final   int         LIMIT_IMAGES_WALL                           = 5;
            /** The number of images per request to load for the user-profile. */
            public      static  final   int         LIMIT_IMAGES_USER                           = 5;
            /** The number of images per request to load for the image-search. */
            public      static  final   int         LIMIT_IMAGES_SEARCH                         = 5;
            /** The number of images per request to load for the explore-area. */
            public      static  final   int         LIMIT_IMAGES_EXPLORE                        = 6;

            /** The number of users per request to load for the user-search. */
            public      static  final   int         LIMIT_USERS_FIND                            = 10;

            /** The number of initial comments to load for detailed image requests. */
            public      static  final   int         LIMIT_INITIAL_COMMENTS                      = 5;
            /** The number of initial likes to load for detailed image requests. */
            public      static  final   int         LIMIT_INITIAL_LIKES                         = 5;
            /** The number of initial food ratings to load for detailed image requests. */
            public      static  final   int         LIMIT_INITIAL_FOOD_RATINGS                  = 5;

            /** The number of comments per request on loading image properties. */
            public      static  final   int         LIMIT_RELOAD_COMMENTS                       = 10;
            /** The number of likes per request on loading image properties. */
            public      static  final   int         LIMIT_RELOAD_LIKES                          = 10;
            /** The number of food ratings per request on loading image properties. */
            public      static  final   int         LIMIT_RELOAD_FOOD_RATINGS                   = 10;

            /** The number of users per request on loading followers. */
            public      static  final   int         LIMIT_RELOAD_FOLLOWERS                      = 10;
            /** The number of users per request on loading followings. */
            public      static  final   int         LIMIT_RELOAD_FOLLOWINGS                     = 10;

            /*********************************************************************************
            *   This would be the limit value that receives all requested items in one request.
            *
            *   @deprecated     Don't use it.
            *********************************************************************************/
            @Deprecated
            public      static  final   int         LIMIT_UNLIMITED                             = -1;
        }

        /*********************************************************************************
        *   All adjustments of the Apache HttpClient.
        *********************************************************************************/
        public static final class ApacheHttpClient
        {
            /** The connection timeout in ms for all Http requests with the Apache HttpClient API. */
            public      static  final   int         TIMEOUT_CONNECTION                          = JsonRPC.TIMEOUT_CONNECTION;
            /** The socket operation timeout in ms for all Http requests with the Apache HttpClient API. */
            public      static  final   int         TIMEOUT_S_O                                 = JsonRPC.TIMEOUT_S_O;
        }

        /*********************************************************************************
        *   All adjustments for the images.
        *********************************************************************************/
        public static final class Image
        {
            /** The default quality of jpegs that are loaded, saved or transfered. */
            public      static  final   int         DEFAULT_JPEG_QUALITY                        = 90;
            /** The default quality of pngs that are loaded, saved or transfered. @deprecated PNG bitmap format should not be used! */
            @Deprecated
            public      static  final   int         DEFAULT_PNG_QUALITY                         = 100;
            /** The default category that is sent for the {@link de.mayflower.antipatterns.io.jsonrpc.PicFoodJsonRPCUser#sendFeedback(android.app.Activity, String)} request. */
            public      static  final   String      DEFAULT_IMAGE_FEEDBACK_CATEGORY             = "ABUSE_REPORT";
            /** The JPEG quality to use for images that will be shared by the user. */
            public      static  final   int         SHARED_JPEG_QUALITY                         = 100;

            /** The integer value that represents the highest food rating. */
            public      static  final   Integer     FOOD_RATING_HIGHEST                         = Integer.valueOf( 5 );
            /** The integer value that represents the average food rating. */
            public      static  final   Integer     FOOD_RATING_MEDIUM                          = Integer.valueOf( 3 );
            /** The integer value that represents the lowest food rating. */
            public      static  final   Integer     FOOD_RATING_LOWEST                          = Integer.valueOf( 1 );

            /** The float value that represents the lower bound of the average smiley representation. This value is compared to the average food rating float value */
            public      static  final   float       BOUND_SMILEY_YELLOW                         = 2.33f;
            /** The float value that represents the lower bound of the best smiley representation. This value is compared to the average food rating float value. */
            public      static  final   float       BOUND_SMILEY_GREEN                          = 3.66f;

            /*********************************************************************************
            *   The maximum number of chunks for receiving time distance specifiers.
            *   E.g. 3 would result in "1 year 2 months 2 days" and cut off any pending hours,
            *   minutes etc. Only the highest units are picked!
            *********************************************************************************/
            public      static  final   int         MAX_NUMBER_OF_TIME_DISTANCE_CHUNKS          = 2;
        }

        /*********************************************************************************
        *   All settings for input-fields.
        *********************************************************************************/
        public static final class InputFields
        {
            /** The minimum numbers of characters to enter into the 'username' input field in the 'register' state to perform a check if the username is available. */
            public      static  final   int         MIN_USERNAME_LENGTH_FOR_CHECKING            = 3;
            /** The minimum accepted length for all password fields. */
            public      static  final   int         MIN_PASSWORD_LENGTH                         = 6;
            /** The minimum accepted length for all input fields that require user input. */
            public      static  final   int         MIN_INPUT_LENGTH                            = 3;
        }

        /*********************************************************************************
        *   Settings for system notifications.
        *********************************************************************************/
        public static final class Notification
        {
            /** The default notification-ID to use if no 'collapse_key' has been found in the received push message. */
            public      static  final   int         DEFAULT_PUSH_NOTIFICATION_ID                = 0;
        }

        /*********************************************************************************
        *   All performance settings.
        *********************************************************************************/
        public static final class Performance
        {
            /** The transition duration for alpha transition animations. */
            public      static  final   int         TRANSITION_ITEMS_FADING_MILLIS              = 500;
        }
    }
