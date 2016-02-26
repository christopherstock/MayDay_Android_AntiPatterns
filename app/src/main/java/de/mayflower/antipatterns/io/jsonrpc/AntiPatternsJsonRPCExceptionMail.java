
    package de.mayflower.antipatterns.io.jsonrpc;

    import  java.util.*;
    import de.mayflower.antipatterns.*;
    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  de.mayflower.antipatterns.io.*;
    import  org.apache.http.*;
    import  org.apache.http.client.*;
    import  org.apache.http.client.entity.*;
    import  org.apache.http.client.methods.*;
    import  org.apache.http.message.*;
    import  de.mayflower.lib.ui.*;

    /*****************************************************************************************
    *   8. Handles Exception-mails.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsJsonRPCExceptionMail
    {
        /*****************************************************************************************
        *   8.1 - Requests to handle an Exception-E-Mail.
        *****************************************************************************************/
        public static final void handleExceptionEmail()
        {
            try
            {
                //create new HttpClient and post method
                HttpClient          httpClient      = AntiPatternsHttp.getEnrichedHttpClient();
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
                            new BasicNameValuePair( "UserAgent",                    AntiPatternsSystems.getUserAgent().getUserAgentString()          ),
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

                            new BasicNameValuePair( "stackTrace",                   AntiPatternsDebug.stackTraceString                               ),
                        }
                    )
                );

                //exclusively for API Level 3 or higher!
                httpPost.setEntity( new UrlEncodedFormEntity( nameValuePairs ) );

                //Execute HTTP Post Request
                HttpResponse httpResponse = httpClient.execute( httpPost );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                AntiPatternsDebug.major.out( "Received StatusCode from sending ExceptionMail [" + statusCode + "]" );
            }
            catch ( Exception e )
            {
                AntiPatternsDebug.DEBUG_OUT_THROWABLE(e);
            }
        }
    }
