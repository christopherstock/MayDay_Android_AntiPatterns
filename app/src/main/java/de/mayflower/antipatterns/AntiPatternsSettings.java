
    package de.mayflower.antipatterns;

    import de.mayflower.lib.*;

    /***************************************************************************************************
    *   Holds version information.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ***************************************************************************************************/
    public class AntiPatternsSettings
    {
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
        *   All adjustments for the images.
        *********************************************************************************/
        public static final class Image
        {
            /** The default quality of jpegs that are loaded, saved or transfered. */
            public      static  final   int         DEFAULT_JPEG_QUALITY                        = 90;
            /** The default quality of pngs that are loaded, saved or transfered. @deprecated PNG bitmap format should not be used! */
            @Deprecated
            public      static  final   int         DEFAULT_PNG_QUALITY                         = 100;
            /** The default category that is sent for the {@link de.mayflower.antipatterns.io.jsonrpc.AntiPatternsJsonRPCUser#sendFeedback(android.app.Activity, String)} request. */
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
