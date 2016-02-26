
    package de.mayflower.lib.io.http;

    import  java.util.*;
    import  android.os.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.api.*;

    /************************************************************************
    *   Delivers the device-browser's UserAgent. The user is picked from a temporary {@link android.webkit.WebView}.
    *   This is the superclass of two classes:
    *
    *   {@link LibHttpUserAgentBuildConstantsModern}   to get the User-Agent for devices with SDK-Level 4 or higher.
    *   {@link LibHttpUserAgentBuildConstantsObsolete} to get the User-Agent for devices with SDK-Level 3 or lower.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public abstract class LibHttpUserAgent
    {
        /** The resolved UserAgent-String. */
        protected                   String      iUserAgentString        = null;

        /** The debug flag for tracing this system. */
        protected                   LibDebug    iDebug                  = null;

        /************************************************************************
        *   Creates a new http user agent detection system.
        *
        *   @param  aDebug  The debug system for tracing this system.
        ************************************************************************/
        protected LibHttpUserAgent( LibDebug aDebug )
        {
            iDebug = aDebug;
        }

        /************************************************************************
        *   Delivers the user-agent-string that has been resolved by this system.
        *
        *   @return     The resolved user-agent-string.
        ************************************************************************/
        public String getUserAgentString()
        {
            return iUserAgentString;
        }

        /************************************************************************
        *   Creates a UserAgent-resolver from build constants.
        *
        *   @param  debug       The debug system for tracing this operation.
        *   @param  appCaption  The caption of the application. Will be included in the UserAgent-String.
        *   @return             The UserAgent resolver that contains the UserAgent-String.
        ************************************************************************/
        private static final LibHttpUserAgent getFromBuildConstants( LibDebug debug, String appCaption )
        {
            LibHttpUserAgent ret = null;

            //switch sdk level
            if ( LibAPI.isSdkLevelLower4() )
            {
                ret = new LibHttpUserAgentBuildConstantsObsolete( debug, appCaption );
            }
            else
            {
                ret = new LibHttpUserAgentBuildConstantsModern( debug, appCaption );
            }

            return ret;
        }

        /************************************************************************
        *   Holds functionality that is operative on api levels lower than 4.
        *   This functionality is enclosed into a single class to prevent a {@link NoClassDefFoundError}.
        ************************************************************************/
        protected static class LibHttpUserAgentBuildConstantsObsolete extends LibHttpUserAgent
        {
            /************************************************************************
            *   Creates the User-Agent-String from build-constants that are available
            *   on API-Levels lower than 4.
            *
            *   @param  aDebug      The debug system for tracing this operation.
            *   @param  appCaption  The caption of this app. Will be included in the UserAgent-string.
            ************************************************************************/
            public LibHttpUserAgentBuildConstantsObsolete( LibDebug aDebug, String appCaption )
            {
                super( aDebug );

                //assign UserAgent-string from constants
                iUserAgentString =
                (
                        appCaption
                    +   " "
                    +   "(Linux"                                                                    + "; "
                    +   "U"                                                                         + "; "
                    +   "Android " + Build.VERSION.RELEASE                                          + "; "
                    +   Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry()  + "; "
                    +   Build.MODEL
                    +   ")"
                );

                //ProjectName (Android {Build.VERSION.RELEASE}; {locale}; {Build.MANUFACTURER}; {Build.MODEL}/{Build.VERSION_CODES})

                iDebug.out( "assigned UserAgent: [" + iUserAgentString + "]" );
              //iDebug.logAppend( "UserAgent: [" + iUserAgentString + "]" );
            }
        }

        /************************************************************************
        *   Holds functionality that is operative on all api levels and
        *   that does not need a webview. This functionality is enclosed
        *   into a single class to prevent a {@link NoClassDefFoundError}.
        ************************************************************************/
        protected static class LibHttpUserAgentBuildConstantsModern extends LibHttpUserAgent
        {
            /************************************************************************
            *   Creates the User-Agent-String from build-constants that are available
            *   on API-Levels 4 or higher.
            *
            *   @param  aDebug      The debug system for tracing this operation.
            *   @param  appCaption  The caption of this app. Will be included in the UserAgent-string.
            ************************************************************************/
            public LibHttpUserAgentBuildConstantsModern( LibDebug aDebug, String appCaption )
            {
                super( aDebug );

                //assign UserAgent-string from constants
                iUserAgentString =
                (
                        appCaption
                    +   " "
                    +   "(Linux"                                                                    + "; "
                    +   "U"                                                                         + "; "
                    +   "Android " + Build.VERSION.RELEASE                                          + "; "
                    +   Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry()  + "; "
                    +   Build.MANUFACTURER + " " + Build.MODEL
                    +   ")"
                );

                iDebug.out( "assigned UserAgent: [" + iUserAgentString + "]" );
              //iDebug.logAppend( "UserAgent: [" + iUserAgentString + "]" );
            }
        }

        /************************************************************************
        *   Delivers a user-agent-resolver.
        *
        *   @param  debug               The debug system for tracing this operation.
        *   @param  appNameInUserAgent  The name of the application. Will be included
        *                               in the User-Agent string.
        *   @return                     The user-agent-resolver.
        ************************************************************************/
        public static final LibHttpUserAgent create( LibDebug debug, String appNameInUserAgent )
        {
            return LibHttpUserAgent.getFromBuildConstants( debug, appNameInUserAgent );
        }
    }
