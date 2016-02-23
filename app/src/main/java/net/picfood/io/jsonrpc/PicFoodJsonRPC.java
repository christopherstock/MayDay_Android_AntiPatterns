/*  $Id: PicFoodJsonRPC.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.io.jsonrpc;

    import  net.picfood.PicFoodSettings.*;
    import  org.alexd.jsonrpc.*;

    /*****************************************************************************************
    *   Handles all Json-RPC-requests.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/io/jsonrpc/PicFoodJsonRPC.java $"
    *****************************************************************************************/
    public class PicFoodJsonRPC
    {
        /** The error status code from our backend that indicates 'everything is ok'. */
        public          static          final       int         ERROR_CODE_OK                                       = 100;

        /** The error status code from our backend that indicates 'user not existent'. */
        public          static          final       int         ERROR_CODE_USER_NOT_FOUND                           = 202;
        /** The error status code from our backend that indicates 'session expired'. */
        public          static          final       int         ERROR_CODE_SESSION_EXPIRED                          = 203;
        /** The error status code from our backend that indicates 'email has an invalid format'. */
        public          static          final       int         ERROR_CODE_WRONG_E_MAIL_FORMAT                      = 211;
        /** The error status code from our backend that indicates 'username already exists' in state 'register'. */
        public          static          final       int         ERROR_CODE_REGISTER_USERNAME_EXISTS                 = 212;
        /** The error status code from our backend that indicates 'email already exists' in state 'register'. */
        public          static          final       int         ERROR_CODE_REGISTER_E_MAIL_EXISTS                   = 213;
        /** The error status code from our backend that indicates 'email has an invalid format' in state 'register'. */
        public          static          final       int         ERROR_CODE_REGISTER_WRONG_USERNAME_FORMAT           = 215;
        /** The error status code from our backend that indicates 'email does not exist'. */
        public          static          final       int         ERROR_CODE_E_MAIL_NOT_EXISTENT                      = 216;
        /** The error status code from our backend that indicates 'password invalid'. */
        public          static          final       int         ERROR_CODE_WRONG_PASSWORD                           = 217;
        /** The error status code from our backend that indicates 'image has not been found'. */
        public          static          final       int         ERROR_CODE_IMAGE_NOT_FOUND                          = 302;

        /** The error status code from our backend that indicates 'optional update available'. */
        public          static          final       int         ERROR_CODE_UPDATE_CHECK_UPDATE_OPTIONAL             = 400;
        /** The error status code from our backend that indicates 'mandatory update available'. */
        public          static          final       int         ERROR_CODE_UPDATE_CHECK_UPDATE_REQUIRED             = 401;

        /** The error status code from our backend that indicates 'internal error'. */
        public          static          final       int         ERROR_CODE_INTERNAL_ERROR                           = 500;

        /** Our definition for the integer representation of the boolean value {@link Boolean#FALSE}. */
        public          static                      Integer     FALSE                                               = Integer.valueOf( 0 );
        /** Our definition for the integer representation of the boolean value {@link Boolean#TRUE}. */
        public          static                      Integer     TRUE                                                = Integer.valueOf( 1 );

        /*****************************************************************************************
        *   Creates a new instance of the JsonRPC-client being configured with our project settings.
        *
        *   @param  url     The URL that shall be opened with this client instance.
        *   @return         An instance of the JSONRPC-client with specified connectionTimeout,
        *                   encoding and S/O-Timeout.
        *****************************************************************************************/
        public static final JSONRPCClient getJsonRPCClient( String url )
        {
            JSONRPCClient client = JSONRPCClient.create( url, JSONRPCParams.Versions.VERSION_2 );

            client.setConnectionTimeout(    JsonRPC.TIMEOUT_CONNECTION );
            client.setSoTimeout(            JsonRPC.TIMEOUT_S_O );
            client.setEncoding(             JsonRPC.ENCODING );

            return client;
        }

        /*****************************************************************************************
        *   Checks the specified Throwable that comes from the JSON-RPC-API,
        *   if the Throwable has been caused by a http connection error.
        *
        *   Unfortunately, the JSON-RPC-API does not launch an {@link java.io.IOException} when
        *   the http network fails. Instead, a Throwable with the message "IO error" is thrown.
        *   The Throwables from the JSON-RPC-API are all instances of {@link JSONRPCException}.
        *
        *   @param  t       The Throwable to examine for an IO error.
        *   @return         <code>true</code> if this Throwable has been raised by the JSON-RPC-API
        *                   because of a http network connection error. Otherwise <code>false</code>.
        *****************************************************************************************/
        public static final boolean isIOError( Throwable t )
        {
            //this is gnarly .. :/ But sadly, the JSONRPC-API does not throw IOErrors :(
            return ( t.getMessage() != null && t.getMessage().equals( "IO error" ) );
        }
    }
