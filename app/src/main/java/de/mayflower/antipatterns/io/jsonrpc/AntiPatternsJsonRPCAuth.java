
    package de.mayflower.antipatterns.io.jsonrpc;

    import  de.mayflower.antipatterns.*;
    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  org.alexd.jsonrpc.*;
    import  org.json.*;
    import  de.mayflower.lib.io.*;

    /*****************************************************************************************
    *   1. Handles all Json-RPC-requests of the group 'auth'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsJsonRPCAuth
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
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'login' [" + username + "][" + password + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCAuth());

            String passwordMD5 = ( changePasswortToMD5 ? LibIO.getMD5( password ) : password );
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'login' - md5-password is [" + passwordMD5 + "]" );

            //execute and return the response
            JSONObject response = client.callJSONObject
            (
                "login",
                username,
                passwordMD5
            );
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'login' - object received [" + response + "]" );
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
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'restorePassword' [" + username + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCAuth());

            //execute and return the response
            JSONObject response = client.callJSONObject
            (
                "restorePassword",
                username
            );
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'restorePassword' - object received [" + response + "]" );
            return response;
        }
    }
