/*  $Id: PicFoodJsonRPCGeneral.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.io.jsonrpc;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.*;
    import  org.alexd.jsonrpc.*;
    import  org.json.*;
    import  android.app.*;
    import  de.mayflower.lib.io.*;

    /*****************************************************************************************
    *   6. Handles all Json-RPC-requests of the group 'general'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/io/jsonrpc/PicFoodJsonRPCGeneral.java $"
    *****************************************************************************************/
    public class PicFoodJsonRPCGeneral
    {
        /*****************************************************************************************
        *   6.1 - Requests to receive the terms and conditions.
        *
        *   @param  activity    The according activity context.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getTerms( Activity activity ) throws Throwable
        {
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'getTerms'" );

            String          language    = activity.getResources().getConfiguration().locale.getLanguage();
            PicFoodDebug.jsonRpc.out( "Setting language code [" + language + "]" );

            JSONRPCClient   client      = PicFoodJsonRPC.getJsonRPCClient( Urls.getUrlRPCGeneral() );
            JSONObject      response    = client.callJSONObject
            (
                "getTerms",
                language
            );

            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'getTerms' - object received [" + response + "]" );

            return response;
        }

        /*****************************************************************************************
        *   6.2 - Requests to receive the privacy policy.
        *
        *   @param  activity    The according activity context.
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject getPrivacyPolicy( Activity activity ) throws Throwable
        {
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'getPrivacyPolicy'" );

            String          language    = activity.getResources().getConfiguration().locale.getLanguage();
            PicFoodDebug.jsonRpc.out( "Setting language code [" + language + "]" );

            JSONRPCClient   client   = PicFoodJsonRPC.getJsonRPCClient( Urls.getUrlRPCGeneral() );
            JSONObject      response = client.callJSONObject
            (
                "getPrivacyPolicy",
                language
            );

            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'getPrivacyPolicy' - object received [" + response + "]" );

            String          privacyPolicy   = LibJSON.getJSONStringSecure( response, "privacyPolicy" );
            PicFoodDebug.jsonRpc.out( "Response JSON-RPC 'getPrivacyPolicy' - privacyPolicy:     [" + privacyPolicy                   + "]" );

            return response;
        }

        /*****************************************************************************************
        *   6.3 - Requests to receive the terms and conditions.
        *
        *   @return             The response JSONObject.
        *   @throws Throwable   If any error occurs on connecting or streaming JSON-RPC-data.
        *****************************************************************************************/
        public static final JSONObject checkUpdate() throws Throwable
        {
            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'checkUpdate'" );

            JSONRPCClient client = PicFoodJsonRPC.getJsonRPCClient( Urls.getUrlRPCGeneral() );
            JSONObject response = client.callJSONObject( "checkUpdate", Paramounts.PROJECT_VERSION );

            PicFoodDebug.jsonRpc.out( "Request JSON-RPC 'checkUpdate' - object received [" + response + "]" );

            return response;
        }
    }
