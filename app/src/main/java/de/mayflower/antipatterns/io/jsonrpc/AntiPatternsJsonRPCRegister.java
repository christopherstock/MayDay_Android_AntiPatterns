
    package de.mayflower.antipatterns.io.jsonrpc;

    import  de.mayflower.antipatterns.*;
    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  org.alexd.jsonrpc.*;
    import  org.json.*;
    import  android.graphics.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;

    /*****************************************************************************************
    *   2. Handles all Json-RPC-requests of the group 'join'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsJsonRPCRegister
    {
        /*****************************************************************************************
        *   2.1 - Requests to register a new user.
        *
        *   @param  username    The desired username.
        *   @param  email       The user's email address.
        *   @param  password    The password to use for this new account.
        *   @param  phone       The user's phone number.
        *   @param  realName    The user's real name.
        *   @param  bitmap      The user's profile icon.
        *   @param  facebookID  The user's facebookID. May be <code>null</code> if not present.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject register
        (
            String  username,
            String  email,
            String  password,
            String  phone,
            String  realName,
            Bitmap  bitmap,
            String  facebookID
        )
        throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'register'" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCRegister());

            //password must be md5
            String passwordMD5 = LibIO.getMD5( password );

            //read image bytes
            String bitmapBase64 = "";
            if ( bitmap != null )
            {
                byte[] bytes = LibImage.getBytesFromBitmapAsJPEG( AntiPatternsSettings.Image.DEFAULT_JPEG_QUALITY, bitmap );
                bitmapBase64 = LibBase64.encodeToString( bytes );
            }

            //secure facebookID
            if ( facebookID == null ) facebookID = "";

            //connect
            JSONObject response = client.callJSONObject
            (
                "join",
                username,
                passwordMD5,
                email,
                phone,
                realName,
                bitmapBase64,
                facebookID
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'register' - object received [" + response + "]" );

            return response;
        }

        /*****************************************************************************************
        *   2.2 - Requests to check the availability of a username.
        *
        *   @param  usernameToCheck     The username to check availability for.
        *   @return                     The response JSONObject.
        *   @throws Throwable           If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final boolean checkUsername( String usernameToCheck ) throws Throwable
        {
            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'checkUsername' [" + usernameToCheck + "]" );

            JSONRPCClient client = AntiPatternsJsonRPC.getJsonRPCClient(Urls.getUrlRPCRegister());

            JSONObject response = client.callJSONObject
            (
                "checkUsername",
                usernameToCheck
            );

            AntiPatternsDebug.jsonRpc.out( "Request JSON-RPC 'checkUsername' - object received [" + response + "]" );

            String  statusID     = LibJSON.getJSONStringSecure( response, "status"      );
            String  message      = LibJSON.getJSONStringSecure( response, "message"     );

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'checkUsername' - status:    [" + statusID               + "]" );
            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'checkUsername' - message:   [" + message                + "]" );

            //switch error-code
            switch ( Integer.parseInt( statusID ) )
            {
                case AntiPatternsJsonRPC.ERROR_CODE_OK:
                {
                    AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'checkUsername' - Username is AVAILABLE!" );
                    return true;
                }
            }

            AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'checkUsername' - Username is NOT AVAILABLE!" );
            return false;
        }
    }
