
    package de.mayflower.lib.util;

    /*************************************************************************************************
    *   An intentional exception being launched for debug purposes.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/util/LibIntentionalException.java $"
    *************************************************************************************************/
    public class LibIntentionalException extends RuntimeException
    {
        /************************************************************************
        *   Every class that implements {@link java.io.Serializable} must specify this
        *   final static field with a long serial version UID.
        ************************************************************************/
        private     static  final   long    serialVersionUID    = -1958355403959521245L;

        /*************************************************************************************************
        *   Creates an intentional exception with the default error message.
        *************************************************************************************************/
        public LibIntentionalException()
        {
            this( "THIS EXCEPTION HAS BEEN RAISED INTENTIONALLY FROM A DEBUG VERSION!" );
        }

        /*************************************************************************************************
        *   Creates an intentional exception with the specified error message.
        *
        *   @param  aMsg    The error message to associate with this exception.
        *************************************************************************************************/
        public LibIntentionalException( String aMsg )
        {
            super( aMsg );
        }
    }
