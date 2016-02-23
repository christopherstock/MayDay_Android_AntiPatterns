/*  $Id: PicFoodJsonRPCAuth.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.io.jsonrpc;

    import  net.picfood.*;
    import  net.picfood.PicFoodProject.*;
    import  org.alexd.jsonrpc.*;
    import  org.json.*;

    import  com.synapsy.android.lib.io.*;

    /*****************************************************************************************
    *   1. Handles all Json-RPC-requests of the group 'auth'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/io/jsonrpc/PicFoodJsonRPCAuth.java $"
    *****************************************************************************************/
    public class PicFoodJsonRPCAuth
    {
        /*****************************************************************************************
        *   1.1 - Requests a login via Json-RPC.
        *
        *   @param  username                The username to login with.
        *   @param  password                The plain password to login with.
        *   @param  changePasswortToMD5     If <code>true</code>, the given password will be encrypted
        *                                   to md5 before it gets submitted to the jsonRPC request.
        *   @return                         The JsonRPC-status code.
        *   @throws Throwable               If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject login( String username, String password, boolean changePasswortToMD5 ) throws Throwable
        {
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'login' [" + username + "][" + password + "]" );

            JSONRPCClient client = PicFoodJsonRPC.getJsonRPCClient( Urls.getUrlRPCAuth() );

            String passwordMD5 = ( changePasswortToMD5 ? LibIO.getMD5( password ) : password );
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'login' - md5-password is [" + passwordMD5 + "]" );

            //execute and return the response
            JSONObject response = client.callJSONObject
            (
                "login",
                username,
                passwordMD5
            );
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'login' - object received [" + response + "]" );
            return response;
        }

        /*****************************************************************************************
        *   1.2 - Requests to restore the user's password.
        *
        *   @param  username    The username to restore the password for.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject restorePassword( String username ) throws Throwable
        {
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'restorePassword' [" + username + "]" );

            JSONRPCClient client = PicFoodJsonRPC.getJsonRPCClient( Urls.getUrlRPCAuth() );

            //execute and return the response
            JSONObject response = client.callJSONObject
            (
                "restorePassword",
                username
            );
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'restorePassword' - object received [" + response + "]" );
            return response;
        }
    }
