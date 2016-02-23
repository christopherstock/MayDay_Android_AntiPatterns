/*  $Id: LibUncaughtExceptionHandler.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.util;

    /*****************************************************************************
    *   The UncaughtExceptionHandler is invoked when an Exception is thrown and is not caught.
    *   These exceptions get caught by the UncaughtExceptionHandler and the
    *   associated callback-method is invoked.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/util/LibUncaughtExceptionHandler.java $"
    *****************************************************************************/
    public class LibUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
    {
        /*****************************************************************************
        *   Specifies a callback-object for the uncaught exception handler.
        *
        *   @author     $Author: schristopher $
        *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
        *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/util/LibUncaughtExceptionHandler.java $"
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
