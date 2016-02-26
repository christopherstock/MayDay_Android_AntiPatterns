
    package de.mayflower.lib.util;

    /************************************************************************
    *   An Exception that is launched intentionally.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class LibInternalError extends RuntimeException
    {
        /************************************************************************
        *   Every class that implements {@link java.io.Serializable} must specify this
        *   final static field with a long serial version UID.
        ************************************************************************/
        private         static      final       long        serialVersionUID            = 0L;

        /************************************************************************
        *   Creates an intentional Exception with the specified message body.
        *
        *   @param  msg The message body to assign to this Exception.
        ************************************************************************/
        public LibInternalError( String msg )
        {
            super( msg );
        }
    }
