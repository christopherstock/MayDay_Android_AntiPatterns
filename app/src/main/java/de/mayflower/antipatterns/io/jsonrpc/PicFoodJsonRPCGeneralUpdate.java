
    package de.mayflower.antipatterns.io.jsonrpc;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.*;
    import  de.mayflower.antipatterns.io.*;
    import  org.apache.http.*;
    import  org.apache.http.client.*;
    import  org.apache.http.client.methods.*;
    import  org.apache.http.util.*;

    /*****************************************************************************************
    *   7. Handles all Download-requests.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
