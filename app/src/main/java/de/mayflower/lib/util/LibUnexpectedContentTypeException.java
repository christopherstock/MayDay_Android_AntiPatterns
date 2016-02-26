/*  $Id: LibUnexpectedContentTypeException.java 50257 2013-07-30 10:57:31Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.util;

    /*************************************************************************************************
    *   An intentional exception being launched for debug purposes.
    *   This exception shall be thrown if a http response delivers a content-type that is not expected.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50257 $ $Date: 2013-07-30 12:57:31 +0200 (Di, 30 Jul 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/util/LibUnexpectedContentTypeException.java $"
    *************************************************************************************************/
    public class LibUnexpectedContentTypeException extends RuntimeException
    {
        /************************************************************************
        *   Every class that implements {@link java.io.Serializable} must specify this
        *   final static field with a long serial version UID.
        ************************************************************************/
        private     static  final   long    serialVersionUID        = -3430232697319947549L;

        /*************************************************************************************************
        *   Creates a new {@link LibUnexpectedContentTypeException} by overriding
        *   the error message of a {@link RuntimeException}.
        *
        *   @param  expectedContentType     The content-type that has been expected.
        *   @param  receivedContentType     The actual content-type that has been received.
        *   @param  itemToPerform           The job that describes the http request.
        *   @param  url                     The url to insert into the description.
        *   @param  responseHeaders         Additional information to append.
        *   @param  caption                 The internal caption of the module where this Exception occured.
        *   @param  version                 The project or module version.
        *************************************************************************************************/
        public LibUnexpectedContentTypeException( String expectedContentType, String receivedContentType, String itemToPerform, String url, String responseHeaders, String caption, String version )
        {
            super
            (
                    "This Exception has been raised INTENTIONALLY from [" + caption + "][" + version + "]" + "\n\n"
                +   "Expected content type: [" + expectedContentType    + "]\n"
                +   "received was:          [" + receivedContentType    + "]\n"
                +   "The job was:           [" + itemToPerform          + "]\n"
                +   "The url was:           [" + url                    + "]\n"
                +   "Response-Headers:\n\n"
                +   responseHeaders
                +   "\n"
            );
        }
    }
