
    package de.mayflower.lib;

    import  de.mayflower.lib.ui.widget.LibDebugConsole.ConsoleColor;

    /*********************************************************************************
    *   This class templates a debug system.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50324 $ $Date: 2013-08-01 10:18:12 +0200 (Do, 01 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/LibDebug.java $"
    *********************************************************************************/
    public interface LibDebug
    {
        /*********************************************************************************
        *   Writes this message to the console output.
        *
        *   @param  msg     The debug-message.
        *********************************************************************************/
        public abstract void out( Object msg );

        /*********************************************************************************
        *   Writes this error message with severity 'error'.
        *
        *   @param  msg     The error-message.
        *********************************************************************************/
        public abstract void err( Object msg );

        /*********************************************************************************
        *   Writes the stacktrace and message of the given Throwable to the console output.
        *
        *   @param  t       The Throwable to output.
        *********************************************************************************/
        public abstract void trace( Throwable t );

        /*********************************************************************************
        *   Logs a message to an on-screen console.
        *
        *   @param  msg     The message to log.
        *   @param  col     The console color to use for logging the given message.
        *********************************************************************************/
        public abstract void log( String msg, ConsoleColor col );

        /*********************************************************************************
        *   Performs an output of memory information.
        *********************************************************************************/
        public abstract void mem();
    }
