
    package de.mayflower.lib.util;

    /*****************************************************************************
    *   The UncaughtExceptionHandler is invoked when an Exception is thrown and is not caught.
    *   These exceptions get caught by the UncaughtExceptionHandler and the
    *   associated callback-method is invoked.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class LibUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
    {
        /*****************************************************************************
        *   Specifies a callback-object for the uncaught exception handler.
        *
        *   @author     Christopher Stock
        *   @version    1.0
        *****************************************************************************/
        public static interface UncaughtExceptionCallback
        {
            /*****************************************************************************
            *   This callback is invoked if an UncaughtException has been caught by the
            *   uncaught exception handler.
            *
            *   @param  thread  The thread in which the exception occured.
            *   @param  t       The Throwable that has been raised.
            *****************************************************************************/
            public void uncaughtException( Thread thread, Throwable t );
        }

        /*****************************************************************************
        *   The callback object this UncaughtExceptionHandler uses.
        *****************************************************************************/
        public                              UncaughtExceptionCallback           iCallback                   = null;

        /*****************************************************************************
        *   Specifies the answers to the question if an exception has been caught.
        *****************************************************************************/
        public static enum UncaughtException
        {
            /** Means that this is an uncaught exception. */
            EYes,

            /** Means that this exception has been caught. */
            ENo,
            ;
        }

        /*****************************************************************************
        *   Creates a new instance that informs the given callback about uncaught exceptions.s
        *
        *   @param  aCallback   The callback-object to inform when an uncaught exception appears.
        *****************************************************************************/
        public LibUncaughtExceptionHandler( UncaughtExceptionCallback aCallback )
        {
            iCallback = aCallback;
            Thread.setDefaultUncaughtExceptionHandler( this );
        }

        @Override
        public final void uncaughtException( Thread thread, Throwable t )
        {
            iCallback.uncaughtException( thread, t );
        }
    }
