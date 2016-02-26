
    package de.mayflower.antipatterns;

    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.ui.*;

    import android.app.Activity;
    import  android.content.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.io.http.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtExceptionCallback;

    /*****************************************************************************
    *   All independent systems the application is using.
    *   All systems use lazy-initialization and only once instance is hold per system
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class AntiPatternsSystems
    {
        /** The UserAgent system constructs and delivers the user agent for this app. */
        protected   static      LibHttpUserAgent                    userAgent               = null;

        /** The UncaughtExceptionHandler systems catches uncaught Exceptions and reports them to the developer team. */
        private     static      LibUncaughtExceptionHandler         exceptionHandler        = null;

        /** The font systems delivers custom Typefaces from the 'assets'-resource folder. */
        private     static AntiPatternsFont fonts                   = null;

        /** The instance of the cache system, that cares for caching Bitmap data. */
        private     static      LibCache                            cache                   = null;

        /*****************************************************************************
        *   Inits all systems lazy. Being invoked on entering every activity,
        *   the initialization of all systems is asserted.
        *
        *   @param  activity            The according activity context.
        *****************************************************************************/
        public static final void init( Activity activity )
        {
            //hide soft-keyboard if popped up
            //LibUI.hideSoftKeyboard( activity );

            //init UncaughtExceptionHandler
            //initUncaughtExceptionHandler();

            //inits external fonts
            //initFonts( activity );

            //init UserAgent
            //initUserAgent();

            //init cache system
            //initCache();

            //perform implicit update check
            //AntiPatternsFlowGeneral.checkAppUpdateThreaded(state);
        }

        /*****************************************************************************
        *   Lazy inits the UserAgent-system.
        *****************************************************************************/
        public static final void initUserAgent()
        {
            //init lazy
            if ( userAgent == null )
            {
                //specify user agent
                userAgent = LibHttpUserAgent.create( AntiPatternsDebug.userAgent, Paramounts.USER_AGENT_PREFIX );
            }
        }

        /*****************************************************************************
        *   Returns the UserAgent-system.
        *
        *   @return     The UserAgent-system that can be used to retreive the
        *               constructed UserName.
        *****************************************************************************/
        public static final LibHttpUserAgent getUserAgent()
        {
            return userAgent;
        }

        /*****************************************************************************
        *   Lazy inits the UncaughtExceptionHandler-system.
        *****************************************************************************/
        public static final void initUncaughtExceptionHandler()
        {
            //init lazy
            if ( exceptionHandler == null )
            {
                exceptionHandler = new LibUncaughtExceptionHandler
                (
                    new UncaughtExceptionCallback()
                    {
                        @Override
                        public void uncaughtException( Thread thread, Throwable throwable )
                        {
                            AntiPatternsDebug.DEBUG_THROWABLE(throwable, "This has been an UNCAUGHT EXCEPTION being caught by the UncaughtExceptionHandler !", UncaughtException.EYes);
                        }
                    }
                );
            }
        }

        /*****************************************************************************
        *   Lazy inits the font system.
        *
        *   @param  context     The current system context.
        *****************************************************************************/
        public static final void initFonts( Context context )
        {
            //init lazy
            if ( fonts == null )
            {
                fonts = new AntiPatternsFont( context );
            }
        }

        /*****************************************************************************
        *   Returns the font system.
        *
        *   @return     The initialized font system.
        *               This offers access to different Typefaces that have been read
        *               from the 'assets' folder.
        *****************************************************************************/
        public static final AntiPatternsFont getFonts()
        {
            return fonts;
        }

        /*****************************************************************************
        *   Lazy inits the cache system.
        *****************************************************************************/
        public static final void initCache()
        {
            //init lazy
            if ( cache == null )
            {
                cache   = new LibCache( AntiPatternsSD.getCacheDir() );
            }
        }

        /*****************************************************************************
        *   Returns the cache system.
        *
        *   @return     The initialized cache system.
        *****************************************************************************/
        public static final LibCache getCache()
        {
            return cache;
        }
    }
