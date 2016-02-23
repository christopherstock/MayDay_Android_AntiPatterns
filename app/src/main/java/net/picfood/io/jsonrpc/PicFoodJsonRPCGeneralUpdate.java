/*  $Id: PicFoodJsonRPCGeneralUpdate.java 50393 2013-08-05 07:13:28Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.io.jsonrpc;

    import  net.picfood.*;
    import  net.picfood.PicFoodProject.*;
    import  net.picfood.io.*;
    import  org.apache.http.*;
    import  org.apache.http.client.*;
    import  org.apache.http.client.methods.*;
    import  org.apache.http.util.*;

    /*****************************************************************************************
    *   7. Handles all Download-requests.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50393 $ $Date: 2013-08-05 09:13:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/io/jsonrpc/PicFoodJsonRPCGeneralUpdate.java $"
    *****************************************************************************************/
    public class PicFoodJsonRPCGeneralUpdate
    {
        /*****************************************************************************************
        *   7.1 - Requests to download the latest app-version.
        *         NO Json-RPC is used for this connection!
        *
        *   @deprecated     The app shall not be downloaded internal!
        *                   Let the system open the download-link instead!
        *****************************************************************************************/
        @Deprecated
        public static final void downloadLatestVersion()
        {
            PicFoodDebug.jsonRpc.out( "Request 'downloadApp'" );

            try
            {
                //connect via get
                HttpClient   httpClient   = PicFoodHttp.getEnrichedHttpClient();
                HttpGet      httpGet      = new HttpGet( Urls.getUrlDownloadUpdate() );

                //connect
                HttpResponse httpResponse = httpClient.execute( httpGet );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                PicFoodDebug.jsonRpc.out( "Received StatusCode [" + statusCode + "]" );

                //get body
                HttpEntity      entity          = httpResponse.getEntity();
                byte[]          body            = EntityUtils.toByteArray( entity );

                PicFoodDebug.jsonRpc.out( "Received ResponseBody [" + body.length + "] bytes" );
            }
            catch ( Throwable t )
            {
                PicFoodDebug.jsonRpc.out( "Request 'downloadApp' - A Throwable has been raised!" );
                PicFoodDebug.jsonRpc.trace( t );
            }
        }
    }
