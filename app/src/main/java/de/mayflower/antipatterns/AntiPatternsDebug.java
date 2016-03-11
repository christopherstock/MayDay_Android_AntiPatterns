
    package de.mayflower.antipatterns;

    import de.mayflower.antipatterns.Settings.*;
    import  android.util.*;
    import  de.mayflower.lib.*;

    /*****************************************************************************
    *   Represents the debug system consisting of switchable debug groups
    *   formed by the enum constants. Grouped debug outs can be toggled.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public enum AntiPatternsDebug implements LibDebug
    {
        /** A paramount debug group with lowest priority. */
        bugfix(                     Debug.DEBUG_MODE        ),
        /** A paramount debug group with moderate priority. */
        major(                      Debug.DEBUG_MODE        ),
        /** A paramount debug group with highest priority. */
        error(                      Debug.DEBUG_MODE        ),
        ;


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
        AntiPatternsDebug(boolean aDebugOut)
        {
            iDebug = aDebugOut;
        }

        @Override
        public final void out( Object obj )
        {
            if ( iDebug ) DEBUG_OUT( toString(), obj );
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


        /*****************************************************************************
        *   Shows the stack-trace of the specified throwable in a scrollable dialog.
        *
        *   @param  t   The throwable to display in the throwable dialog.
        *****************************************************************************/
        private static final void DEBUG_THROWABLE( Throwable t )
        {
            DEBUG_THROWABLE( t, null );
        }

        /*****************************************************************************
        *   Shows the stack-trace of the specified throwable in a scrollable dialog.
        *
        *   @param  t               The throwable to display in the throwable dialog.
        *   @param  extraMessage    The extra string to display at the end of the
        *                           stack-trace. This can be any additional information
        *                           that helps to trace this error, e.g. a http-response-body.
        *****************************************************************************/
        public static final void DEBUG_THROWABLE( Throwable t, String extraMessage )
        {
            //debug out the throwable
            DEBUG_OUT_THROWABLE( t );

            //pack stackTraceString
            stackTraceString =
            (
                    "["
                +   t.getMessage()
                +   "]["
                +   t
                +   "]["
                +   Log.getStackTraceString( t )
                +   "]"
                +   ( extraMessage == null ? "" : "\n\nExtra Message:\n\n" + extraMessage )
            );
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
    }
