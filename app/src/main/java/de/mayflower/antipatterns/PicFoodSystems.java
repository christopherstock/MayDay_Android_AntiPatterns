
    package de.mayflower.antipatterns;

    import  de.mayflower.antipatterns.PicFoodProject.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.ui.*;
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
    public class PicFoodSystems
    {
        /** The UserAgent system constructs and delivers the user agent for this app. */
        protected   static      LibHttpUserAgent                    userAgent               = null;

        /** The UncaughtExceptionHandler systems catches uncaught Exceptions and reports them to the developer team. */
        private     static      LibUncaughtExceptionHandler         exceptionHandler        = null;

        /** The font systems delivers custom Typefaces from the 'assets'-resource folder. */
        private     static      PicFoodFont                         fonts                   = null;

        /** The instance of the cache system, that cares for caching Bitmap data. */
        private     static      LibCache                            cache                   = null;

        /*****************************************************************************
        *   Inits all systems lazy. Being invoked on entering every activity,
        *   the initialization of all systems is asserted.
        *
        *   @param  state       The according state.
        *****************************************************************************/
        public static final void init( LibState state )
        {
            //hide soft-keyboard if popped up
            LibUI.hideSoftKeyboard( state.getActivity() );

            //init UncaughtExceptionHandler
            initUncaughtExceptionHandler();

            //init UserAgent
            initUserAgent();

            //inits external fonts
            initFonts( state.getActivity() );

            //init cache system
            initCache();

            //perform implicit update check
            PicFoodFlowGeneral.checkAppUpdateThreaded( state );
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
                userAgent = LibHttpUserAgent.create( PicFoodDebug.userAgent, Paramounts.USER_AGENT_PREFIX );
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
                            PicFoodDebug.DEBUG_THROWABLE( throwable, "This has been an UNCAUGHT EXCEPTION being caught by the UncaughtExceptionHandler !", UncaughtException.EYes );
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
                fonts = new PicFoodFont( context );
            }
        }

        /*****************************************************************************
        *   Returns the font system.
        *
        *   @return     The initialized font system.
        *               This offers access to different Typefaces that have been read
        *               from the 'assets' folder.
        *****************************************************************************/
        public static final PicFoodFont getFonts()
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
                cache   = new LibCache( PicFoodSD.getCacheDir() );
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
