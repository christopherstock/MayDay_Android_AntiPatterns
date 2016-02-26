/*  $Id: PicFoodJsonRPCExceptionMail.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.io.jsonrpc;

    import  java.util.*;
    import  net.picfood.*;
    import  net.picfood.PicFoodProject.*;
    import  net.picfood.io.*;
    import  org.apache.http.*;
    import  org.apache.http.client.*;
    import  org.apache.http.client.entity.*;
    import  org.apache.http.client.methods.*;
    import  org.apache.http.message.*;

    import  de.mayflower.lib.ui.*;

    /*****************************************************************************************
    *   8. Handles Exception-mails.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/io/jsonrpc/PicFoodJsonRPCExceptionMail.java $"
    *****************************************************************************************/
    public class PicFoodJsonRPCExceptionMail
    {
        /*****************************************************************************************
        *   8.1 - Requests to handle an Exception-E-Mail.
        *****************************************************************************************/
        public static final void handleExceptionEmail()
        {
            try
            {
                //create new HttpClient and post method
                HttpClient          httpClient      = PicFoodHttp.getEnrichedHttpClient();
                HttpPost            httpPost        = new HttpPost( Urls.getUrlExceptionMailer() );
                List<NameValuePair> nameValuePairs  = new ArrayList<NameValuePair>();

                //requires permission 'READ_PHONE_STATE'
                //LibNetworkInfo      networkInfo     = new LibNetworkInfo( PicFoodApplication.getAppContext(), null );

                //add post body
                nameValuePairs.addAll
                (
                    Arrays.asList
                    (
                        new BasicNameValuePair[]
                        {
                            new BasicNameValuePair( "projectName",                  Paramounts.PROJECT_NAME                                     ),
                            new BasicNameValuePair( "projectVersion",               Paramounts.PROJECT_VERSION                                  ),

                            new BasicNameValuePair( "timestamp",                    LibStringFormat.getSingleton().formatDateTimeMedium()       ),
                            new BasicNameValuePair( "UserAgent",                    PicFoodSystems.getUserAgent().getUserAgentString()          ),
                            new BasicNameValuePair( "device",                       android.os.Build.DEVICE                                     ),
                            new BasicNameValuePair( "board",                        android.os.Build.BOARD                                      ),
                            new BasicNameValuePair( "brand",                        android.os.Build.BRAND                                      ),
                            new BasicNameValuePair( "fingerprint",                  android.os.Build.FINGERPRINT                                ),
                            new BasicNameValuePair( "sdk-level (integer)",          String.valueOf( android.os.Build.VERSION.SDK_INT )          ),
                            new BasicNameValuePair( "id",                           android.os.Build.ID                                         ),
                            new BasicNameValuePair( "user",                         android.os.Build.USER                                       ),
                            new BasicNameValuePair( "model",                        android.os.Build.MODEL                                      ),
                            new BasicNameValuePair( "product",                      android.os.Build.PRODUCT                                    ),
                            new BasicNameValuePair( "tags",                         android.os.Build.TAGS                                       ),

                            new BasicNameValuePair( "cpu abi",                      android.os.Build.CPU_ABI                                    ),
                            new BasicNameValuePair( "display",                      android.os.Build.DISPLAY                                    ),
                            new BasicNameValuePair( "manufacturer",                 android.os.Build.MANUFACTURER                               ),

                            new BasicNameValuePair( "stackTrace",                   PicFoodDebug.stackTraceString                               ),
                        }
                    )
                );

                //exclusively for API Level 3 or higher!
                httpPost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );

                //Execute HTTP Post Request
                HttpResponse httpResponse = httpClient.execute( httpPost );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                PicFoodDebug.major.out( "Received StatusCode from sending ExceptionMail [" + statusCode + "]" );
            }
            catch ( Exception e )
            {
                PicFoodDebug.DEBUG_OUT_THROWABLE( e );
            }
        }
    }
