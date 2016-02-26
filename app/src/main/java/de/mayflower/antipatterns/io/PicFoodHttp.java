/*  $Id: PicFoodHttp.java 50336 2013-08-01 15:07:50Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.io;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodSettings.ApacheHttpClient;
    import  org.apache.http.client.*;
    import  org.apache.http.impl.client.*;
    import  org.apache.http.params.*;

    /**********************************************************************************************
    *   Manages the Apache HttpClient that is implemented in Android.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50336 $ $Date: 2013-08-01 17:07:50 +0200 (Do, 01 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/io/PicFoodHttp.java $"
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
