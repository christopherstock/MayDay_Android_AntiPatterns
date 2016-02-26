/*  $Id: PicFoodDebug.java 50644 2013-08-20 13:37:58Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood;

    import  java.util.*;
    import  net.picfood.PicFoodProject.*;
    import  net.picfood.io.jsonrpc.*;
    import  android.util.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;
    import  de.mayflower.lib.ui.widget.LibDebugConsole.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.*;

    /*****************************************************************************
    *   Represents the debug system consisting of switchable debug groups
    *   formed by the enum constants. Grouped debug outs can be toggled.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50644 $ $Date: 2013-08-20 15:37:58 +0200 (Di, 20 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/PicFoodDebug.java $"
    *****************************************************************************/
    public enum PicFoodDebug implements LibDebug
    {
        /** Logs all requests being performed via JSON-RPC. */
        jsonRpc(                    true                    ),

        /** Logs displaying comments previews. */
        commentsPreviews(           false                   ),
        /** Logs optimization 'update image in GridView after imageAction'. */
        updateAfterImageAction(     false                   ),
        /** Logs search for images. */
        imageSearch(                false                   ),
        /** Handles the implementation of the Facebook API. */
        facebook(                   false                   ),
        /** Logs the handling of auto-login. */
        autoLogin(                  false                   ),
        /** Logs GPS handling. */
        gps(                        false                   ),
        /** Logs the flow 'follow details'. */
        followUsers(                false                   ),
        /** Logs fetching images via http or from cache. */
        imageCache(                 false                   ),
        /** Logs gridview callbacks. */
        gridView(                   false                   ),
        /** Logs, which detailed-images order data. */
        orderDetailedImageBitmaps(  false                   ),
        /** Logs the distribution of the screen width to the tiled columns. */
        tiledColumnWidth(           false                   ),
        /** Logs ordering images. */
        imageOrder(                 false                   ),
        /** Logs the implicit update check in bg. */
        implicitUpdateCheck(        false                   ),
        /** Logs limit/offset rigmarole. */
        limitOffset(                false                   ),
        /** Logs use of the Google Places API. */
        googlePlaces(               false                   ),
        /** Logs image cropping. */
        resizeImage(                false                   ),
        /** Logs picking image data from gallery or camera. */
        pickImage(                  false                   ),
        /** Logs finding friends. */
        findFriends(                false                   ),
        /** Handles logging the user wall. */
        wall(                       false                   ),
        /** Handles logging the explore area. */
        explore(                    false                   ),
        /** Handles profile updates and management. */
        profile(                    false                   ),
        /** Logs displaying ratings previews. */
        ratingsPreviews(            false                   ),
        /** Logs displaying likes previews. */
        likesPreviews(              false                   ),
        /** Logs showing and hiding loading circles. */
        loadingCircles(             false                   ),
        /** Logs memory problems. */
        memoryManager(              false                   ),
        /** Logs parsed image data. */
        dataImage(                  false                   ),
        /** Logs ordering and showing a detailed image. */
        followship(                 false                   ),
        /** Logs parsing the results from the Google Places API. */
        googlePlacesParser(         false                   ),
        /** Logs ordering and showing a detailed image. */
        imageProperties(            false                   ),
        /** Logs ordering and showing a detailed image. */
        detailedImage(              false                   ),
        /** Logs Google Cloud Messaging. */
        gcm(                        false                   ),
        /** Logs like/unlike-operation. */
        likeUnlike(                 false                   ),
        /** Logs information concerning the image upload. */
        imageUpload(                false                   ),
        /** Logs User-Agent creation. */
        userAgent(                  false                   ),
        /** Logs ViewPager-handling. */
        viewPager(                  false                   ),
        /** Logs the settings system. */
        settings(                   false                   ),

        /** A paramount debug group with lowest priority. */
        bugfix(                     Debug.DEBUG_MODE        ),
        /** A paramount debug group with moderate priority. */
        major(                      Debug.DEBUG_MODE        ),
        /** A paramount debug group with highest priority. */
        error(                      Debug.DEBUG_MODE        ),

        ;

        /** All lines for the device profiling to display on the screen. */
        public      static          Vector<String>      debugProfilingLines         = new Vector<String>();

        /** The single line for the profiling bar to display on the screen. */
        public      static          String              debugProfilingBar           = "";

        /** The String to display on the screen for debug purposes. */
        public      static          StringBuffer        debugOnScreenString         = new StringBuffer();

        /** The current angle of the performance-indicator ( spinning circle ). */
        public      static          int                 performanceTestAngle        = 0;

        /** The stacktrace-String to send via email. */
        public      static          String              stackTraceString            = "";

        /** The debug flag for this debug group. */
        private                     boolean             iDebug                      = false;

        /*****************************************************************************
        *   Creates a new debug group with the specified debug flag.
        *
        *   @param  aDebugOut   The debug flag indicates if debug-tasks for this group
        *                       shall be performed.
        *****************************************************************************/
        PicFoodDebug( boolean aDebugOut )
        {
            iDebug = aDebugOut;
        }

        /*****************************************************************************
        *   Checks if the debug flag for this debug group is set.
        *
        *   @return     <code>true</code> if the debug flag for this group is set.
        *               Otherwise <code>false</code>.
        *****************************************************************************/
        public final boolean isDebugged()
        {
            return iDebug;
        }

        @Override
        public final void out( Object obj )
        {
            if ( iDebug ) DEBUG_OUT( toString(), obj );
        }

        @Override
        public final void log( String msg, ConsoleColor col )
        {
            if ( iDebug ) LibDebugConsole.getSingleton().appendOutputUIThreaded( col, msg );
        }

        @Override
        public final void err( Object obj )
        {
            DEBUG_ERR( toString(), obj );
        }

        @Override
        public final void trace( Throwable t )
        {
            if ( iDebug ) DEBUG_THROWABLE( t );
        }

        @Override
        public final void mem()
        {
            if ( iDebug ) DEBUG_OUT_MEMORY();
        }

        /*****************************************************************************
        *   Shows the stack-trace of the specified throwable in a scrollable dialog.
        *
        *   @param  t   The throwable to display in the throwable dialog.
        *****************************************************************************/
        private static final void DEBUG_THROWABLE( Throwable t )
        {
            DEBUG_THROWABLE( t, null, UncaughtException.ENo );
        }

        /*****************************************************************************
        *   Shows the stack-trace of the specified throwable in a scrollable dialog.
        *
        *   @param  t               The throwable to display in the throwable dialog.
        *   @param  extraMessage    The extra string to display at the end of the
        *                           stack-trace. This can be any additional information
        *                           that helps to trace this error, e.g. a http-response-body.
        *   @param  type            Specifies if this throwable has been an uncaught one.
        *****************************************************************************/
        public static final void DEBUG_THROWABLE( Throwable t, String extraMessage, UncaughtException type )
        {
            //debug out the throwable
            DEBUG_OUT_THROWABLE( t );

            //pack stackTraceString
            stackTraceString =
            (
                    ( type == UncaughtException.EYes ? "this has been an UNCAUGHT exception caught by the UncaughtExceptionHandler!\n\n" : "" )
                +   "["
                +   t.getMessage()
                +   "]["
                +   t
                +   "]["
                +   Log.getStackTraceString( t )
                +   "]"
                +   ( extraMessage == null ? "" : "\n\nExtra Message:\n\n" + extraMessage )
            );

            //send throwable email
            if ( Features.ENABLE_DEBUG_THROWABLE_MAILS )
            {
                sendStackTraceViaEmail();
            }
        }

        /*****************************************************************************
        *   Delegates a message and a output-tag to the system's log system
        *   with a severity of 'info'.
        *
        *   @param      tag     The tag that represents the according debug group.
        *   @param      msg     The message to log for this output group.
        *   @see        Log#i(String, String)
        *****************************************************************************/
        private static final void DEBUG_OUT( String tag, Object msg )
        {
            if ( Debug.DEBUG_MODE )
            {
                Log.i( tag, "" + msg );
            }
        }

        /*****************************************************************************
        *   Delegates a message to the system's log system with a severity of 'error'.
        *
        *   @param      tag     The tag that represents the according debug group.
        *   @param      msg     The message to log for this output group.
        *   @see        Log#e(String, String)
        *****************************************************************************/
        private static final void DEBUG_ERR( String tag, Object msg )
        {
            if ( Debug.DEBUG_MODE )
            {
                Log.e( tag, "" + msg );
            }
        }

        /*****************************************************************************
        *   Outputs all available information concerning the Android heap status.
        *****************************************************************************/
        private static final void DEBUG_OUT_MEMORY()
        {
            if ( Debug.DEBUG_MODE )
            {
                Runtime r               = Runtime.getRuntime();

                String  memMax          = LibStringFormat.getSingleton().formatNumber( r.maxMemory()                                   );
                String  memFree         = LibStringFormat.getSingleton().formatNumber( r.freeMemory()                                  );
                String  memUsed         = LibStringFormat.getSingleton().formatNumber( r.totalMemory()                                 );
                String  freeNativeHeap  = LibStringFormat.getSingleton().formatNumber( android.os.Debug.getNativeHeapFreeSize()        );
                String  nativeHeap      = LibStringFormat.getSingleton().formatNumber( android.os.Debug.getNativeHeapSize()            );
                String  nativeHeapAlloc = LibStringFormat.getSingleton().formatNumber( android.os.Debug.getNativeHeapAllocatedSize()   );

                DEBUG_OUT( "memory", "==============================================" );
                DEBUG_OUT( "memory", "memMax:           [" + memMax             + "]" );
                DEBUG_OUT( "memory", "memFree:          [" + memFree            + "]" );
                DEBUG_OUT( "memory", "memUsed:          [" + memUsed            + "]" );
                DEBUG_OUT( "memory", "freeNativeHeap:   [" + freeNativeHeap     + "]" );
                DEBUG_OUT( "memory", "nativeHeapLimit:  [" + nativeHeap         + "]" );
                DEBUG_OUT( "memory", "nativeHeapAlloc:  [" + nativeHeapAlloc    + "]" );
            }
        }

        /*****************************************************************************
        *   Writes the stack-trace for the specified Throwable to the system's log system.
        *
        *   @param  t   The Throwable to print the stack-trace for.
        *****************************************************************************/
        public static final void DEBUG_OUT_THROWABLE( Throwable t )
        {
            if ( Debug.DEBUG_MODE )
            {
                DEBUG_OUT( "[throwable]", Log.getStackTraceString( t ) );
            }
        }

        /*****************************************************************************
        *   Sends the stacktrace via email to the specified exception-mailer-url.
        *****************************************************************************/
        private static final void sendStackTraceViaEmail()
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    PicFoodJsonRPCExceptionMail.handleExceptionEmail();
                }
            }.start();
        }
    }
