
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
        public static final class Debug {
            /**
             * Enables or disabled the debug mode.
             */
            protected static final boolean DEBUG_MODE = true;

            /**
             * Presets inputfields for facilitating the testing process.
             */
            public static final boolean DEBUG_PRESET_INPUTFIELDS = (true && DEBUG_MODE);
            /**
             * Simulate a constant location for GPS. GPS scan will always succeed this way.
             */
            public static final boolean DEBUG_SIMULATE_GPS = (false && DEBUG_MODE);

            /**
             * Use the developement backend. Only active in debug mode.
             */
            protected static final boolean DEBUG_USE_DEV_BACKEND = (true && DEBUG_MODE);
            /**
             * Disables the cache system. Only active in debug mode.
             */
            public static final boolean DEBUG_DISABLE_CACHE = (false && DEBUG_MODE);
        }
    }
