
    package de.mayflower.antipatterns;

    /*****************************************************************************************
    *   Specifies all project settings.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsProject
    {
        /*****************************************************************************************
        *   The paramount settings. Make sure to check these before each release build.
        *****************************************************************************************/
        public static final class Paramounts
        {
            /** The internal name of this project. This value is used in the exception emails. */
            public      static  final   String              PROJECT_NAME                                        = "AntiPatterns";
            /** The prefix for the composed user-agent. */
            public      static  final   String              USER_AGENT_PREFIX                                   = "AntiPatterns/" + Paramounts.PROJECT_VERSION;
            /** The project specifier. This value must not specify special chars because it is used as the global settings group for the persistent storage and for the root-folder on the SD-card. */
            public      static  final   String              PROJECT_SPECIFIER                                   = "AntiPatterns";
            /** The current project version. */
            public      static  final   String              PROJECT_VERSION                                     = AntiPatternsVersion.values()[ 0 ].getClientVersionNumber();
        }

        /*****************************************************************************************
        *   All switches made for debug purposes.
        *****************************************************************************************/
        @SuppressWarnings( "unused" )
        public static final class Debug
        {
            /** Enables or disabled the debug mode. */
            protected   static  final   boolean             DEBUG_MODE                                          = true;

            /** Presets inputfields for facilitating the testing process. */
            public      static  final   boolean             DEBUG_PRESET_INPUTFIELDS                            = ( true    && DEBUG_MODE );
            /** Simulate a constant location for GPS. GPS scan will always succeed this way. */
            public      static  final   boolean             DEBUG_SIMULATE_GPS                                  = ( false   && DEBUG_MODE );

            /** Use the developement backend. Only active in debug mode. */
            protected   static  final   boolean             DEBUG_USE_DEV_BACKEND                               = ( true    && DEBUG_MODE );
            /** Disables the cache system. Only active in debug mode. */
            public      static  final   boolean             DEBUG_DISABLE_CACHE                                 = ( false   && DEBUG_MODE );

            /*****************************************************************************************
            *   Specified debug simulations for development.
            *****************************************************************************************/
            public static final class Simulations
            {
                /** Simulate a response for the 'getImageLike' json rpc request. {@link de.mayflower.antipatterns.io.jsonrpc.AntiPatternsJsonRPCImage#getImageLikes(android.app.Activity, String, int)} */
                public  static  final   boolean             SIMULATE_GET_IMAGE_LIKES                            = ( false   && Debug.DEBUG_MODE );
                /** Simulate a response for the 'getImageFoodRatings' json rpc request. {@link de.mayflower.antipatterns.io.jsonrpc.AntiPatternsJsonRPCImage#getFoodRatings(android.app.Activity, String, int)} */
                public  static  final   boolean             SIMULATE_GET_IMAGE_FOOD_RATINGS                     = ( false   && Debug.DEBUG_MODE );
                /** Simulate the received phonenumbers from a phonebook scan. */
                public  static  final   boolean             SIMULATE_FIND_FRIENDS_VIA_PHONENUMBERS              = ( false   && Debug.DEBUG_MODE );
                /** Simulate the received facebook-IDs from a friend request with the Facebook API. */
                public  static  final   boolean             SIMULATE_FIND_FRIENDS_VIA_FACEBOOK                  = ( false   && Debug.DEBUG_MODE );
                /** Simulate the response 'update required' for the update check. */
                public  static  final   boolean             SIMULATE_APP_UPDATE_REQUIRED                        = ( false   && Debug.DEBUG_MODE );
            }
        }

        /*****************************************************************************************
        *   All features this app implements. Some experimental features may be disabled by now.
        *****************************************************************************************/
        public static final class Features
        {
            /** Load items for the wall. Elementary. */
            public      static  final   boolean             LOAD_WALL                                           = true;
            /** Load items for the explore area. Elementary. */
            public      static  final   boolean             LOAD_EXPLORE                                        = true;
            /** Load items for the own user profile. Elementary. */
            public      static  final   boolean             LOAD_USER_PROFILE                                   = true;

            /** Process all streamed Bitmaps and round their corners. May cause memory problems. */
            public      static  final   boolean             CUT_CORNERS_FOR_IMAGES                              = false;

            /*****************************************************************************************
            *   Link 'rating count' so the user can see the detailed list of ratings.
            *   Senseless since each image can only have one rating by now.
            *****************************************************************************************/
            public      static  final   boolean             SHOW_DETAILED_IMAGE_RATINGS                         = false;

            /*****************************************************************************************
            *   Enables alpha transition animations for appearing images after they have been loaded.
            *   These animations always hang if the appear while the image is on the screen.
            *   No solution found so far.
            *****************************************************************************************/
            public      static  final   boolean             ENABLE_IMAGE_ALPHA_ANIMATIONS                       = false;

            /** Enables auto-login in states 'login', 'acclaim' and 'forgot password'. */
            public      static  final   boolean             ENABLE_AUTO_LOGIN                                   = true;

            /** Recycle Bitmaps when they're out of the canvas and reload them if they're visible. */
            public      static  final   boolean             ENABLE_BITMAP_RECYCLING                             = true;

            /** Enables or disabled sending exception mails. */
            public      static  final   boolean             ENABLE_DEBUG_THROWABLE_MAILS                        = false;

            /** Enables feature 'c2d messaging'. */
            public      static  final   boolean             ENABLE_C2D                                          = false;

            /** Enables feature 'find friends via phonebook'. */
            public      static  final   boolean             ENABLE_FIND_FRIENDS_VIA_PHONEBOOK                   = true;

            /** Enables optional email on registering. */
            public      static  final   boolean             EMAIL_OPTIONAL_ON_REGISTERING                       = false;
        }

        /*****************************************************************************************
        *   All URLs this app connects to.
        *****************************************************************************************/
        public static final class Urls
        {
            /*****************************************************************************************
            *   The startup url in the local network.
            *
            *   @deprecated     Maintenance for this address is not supplied anymore.
            *****************************************************************************************/
            @Deprecated
            protected   static  final   String              URL_LOCAL                                           = "http://192.168.2.20/picfood";

            /*****************************************************************************************
            *   The development URL.
            *****************************************************************************************/
            protected   static  final   String              URL_DEV                                             = "http://picfood.hz1.synapsy.net/v1";

            /*****************************************************************************************
            *   The live URL used for release builds.
            *****************************************************************************************/
            protected   static  final   String              URL_LIVE                                            = "http://picfood.net/v1";

            /*****************************************************************************************
            *   Switches the desired URL according to the debug settings.
            *****************************************************************************************/
            private     static  final   String              URL_BASE                                            =
            (
                    Debug.DEBUG_USE_DEV_BACKEND
                ?   URL_DEV
                :   URL_LIVE
            );

            /** The URL path for RPC-requests of the 'authentification' group. */
            private     static  final   String              URL_RPC_AUTH                                        = URL_BASE + "/auth/";
            /** The URL path for RPC-requests of the 'user' group. */
            private     static  final   String              URL_RPC_USER                                        = URL_BASE + "/user/";
            /** The URL path for RPC-requests of the 'register' group. */
            private     static  final   String              URL_RPC_REGISTER                                    = URL_BASE + "/join/";
            /** The URL path for RPC-requests of the 'general' group. */
            private     static  final   String              URL_RPC_GENERAL                                     = URL_BASE + "/general/";
            /** The URL path for RPC-requests of the 'app' group. */
            private     static  final   String              URL_RPC_APP                                         = URL_BASE + "/app/";
            /** The URL path for RPC-requests of the 'image' group. */
            private     static  final   String              URL_RPC_IMAGE                                       = URL_BASE + "/image/";
            /** The URL path for RPC-requests of the 'search' group. */
            private     static  final   String              URL_RPC_SEARCH                                      = URL_BASE + "/search/";
            /** The URL path for RPC-requests of the 'update' group. */
            private     static  final   String              URL_DOWNLOAD_UPDATE                                 = URL_BASE + "/general/update";
            /** The URL path for exception mails to be posted to. */
            private     static  final   String              URL_DEBUG_EXCEPTION_MAILER                          = URL_BASE + "/debug/exceptionmailer";

            /** @return The URL path for RPC-requests of the 'authentification' group. */
            public static final String getUrlRPCAuth()          {   return URL_RPC_AUTH;                    }
            /** @return The URL path for RPC-requests of the 'user' group. */
            public static final String getUrlRPCUser()          {   return URL_RPC_USER;                    }
            /** @return The URL path for RPC-requests of the 'register' group. */
            public static final String getUrlRPCRegister()      {   return URL_RPC_REGISTER;                }
            /** @return The URL path for RPC-requests of the 'general' group. */
            public static final String getUrlRPCGeneral()       {   return URL_RPC_GENERAL;                 }
            /** @return The URL path for RPC-requests of the 'app' group. */
            public static final String getUrlRPCApp()           {   return URL_RPC_APP;                     }
            /** @return The URL path for RPC-requests of the 'image' group. */
            public static final String getUrlRPCImage()         {   return URL_RPC_IMAGE;                   }
            /** @return The URL path for RPC-requests of the 'search' group. */
            public static final String getUrlRPCSearch()        {   return URL_RPC_SEARCH;                  }
            /** @return The URL path for RPC-requests of the 'update' group. */
            public static final String getUrlDownloadUpdate()   {   return URL_DOWNLOAD_UPDATE;             }
            /** @return The URL path for exception mails to be posted to. */
            public static final String getUrlExceptionMailer()  {   return URL_DEBUG_EXCEPTION_MAILER;      }
        }

        /*****************************************************************************************
        *   Settings for external APIs.
        *****************************************************************************************/
        public static final class API
        {
            /*****************************************************************************************
            *   The Google Mail API.
            *****************************************************************************************/
            public static final class GoogleMail
            {
            }

            /*****************************************************************************************
            *   The Facebook API.
            *****************************************************************************************/
            public static final class Facebook
            {
                /** This is the current version number of the Facebook API. This value is not linked anywhere. */
                public  static  final   String              FACEBOOK_API_VERSION                                = "v3.0.1";

                /** The Facebook API permissions our requests require. */
                public  static  final   String[]            FACEBOOK_APP_PERMISSIONS_READ                       = new String[] { "user_status", };  /* "user_likes" */
            }

            /*****************************************************************************************
            *   The Google Places API.
            *****************************************************************************************/
            public static final class GooglePlaces
            {
                /** The Google Places URL to perform a nearby search, using JSON requests. */
                public  static  final   String              GOOGLE_PLACES_API_URL_SEARCH_NEARBY                 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
                /** The Google Places URL to perform a search by term, using JSON requests. */
                public  static  final   String              GOOGLE_PLACES_API_URL_SEARCH_TERM                   = "https://maps.googleapis.com/maps/api/place/textsearch/json";

                /** The API-key for 'browser' requests. Must be specified in all requests. */
                public  static  final   String              GOOGLE_PLACES_API_BROWSER_KEY                       = "AIzaSyD3l2ustIclBbTtbB2MaMfqVuYlxU0GBIM";
                /** Location types to find with nearby searches via the Google Places API. */
                public  static  final   String              GOOGLE_PLACES_TYPES                                 = "food%7Cbar%7Ccafe%7Ccampground%7Cmeal_delivery%7Crestaurant%7Cnight_club";       // %7C == |
            }
        }
    }
