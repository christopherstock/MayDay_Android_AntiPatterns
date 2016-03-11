
    package de.mayflower.lib;

    /*********************************************************************************
    *   This class templates a debug system.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
        *   Performs an output of memory information.
        *********************************************************************************/
        public abstract void mem();
    }
