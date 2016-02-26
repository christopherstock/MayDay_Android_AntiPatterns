
    package de.mayflower.antipatterns.io;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodSettings.ApacheHttpClient;
    import  org.apache.http.client.*;
    import  org.apache.http.impl.client.*;
    import  org.apache.http.params.*;

    /**********************************************************************************************
    *   Manages the Apache HttpClient that is implemented in Android.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public abstract class PicFoodHttp
    {
        /*****************************************************************************
        *   Returns a new HttpClient with a specified user-agent, connection-timeout and s/o-timeout.
        *
        *   @return     The configured HttpClient, ready to connect.
        *****************************************************************************/
        public static final HttpClient getEnrichedHttpClient()
        {
            HttpClient ret = new DefaultHttpClient();

            //set user-agent
            ret.getParams().setParameter( CoreProtocolPNames.USER_AGENT, PicFoodSystems.getUserAgent().getUserAgentString()    );

            //set connection- and s/o-timeout
            HttpConnectionParams.setConnectionTimeout(  ret.getParams(), ApacheHttpClient.TIMEOUT_CONNECTION    );
            HttpConnectionParams.setSoTimeout(          ret.getParams(), ApacheHttpClient.TIMEOUT_S_O           );

            return ret;
        }
    }
