
    package com.synapsy.android.lib.util;

    /************************************************************************
    *   An Exception that is launched intentionally.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50336 $ $Date: 2013-08-01 17:07:50 +0200 (Do, 01 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/util/LibInternalError.java $"
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
