/*  $Id: LibNetwork.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.io;

    /************************************************************************
    *   Represents a service-provider network with the assigned country-code
    *   and network-code.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/io/LibNetwork.java $"
    ************************************************************************/
    public final class LibNetwork
    {
        /** The network's mobile country code. */
        private         String      iCountryCode    = null;

        /** The network's mobile network code. */
        private         String      iNetworkCode    = null;

        /************************************************************************
        *   Creates a phone network.
        *
        *   @param  aCountryCode    The network's mobile country code.
        *   @param  aNetworkCode    The network's mobile network code.
        ************************************************************************/
        public LibNetwork( String aCountryCode, String aNetworkCode )
        {
            iCountryCode = aCountryCode;
            iNetworkCode = aNetworkCode;
        }

        /************************************************************************
        *   Returns the network's mobile country code.
        *
        *   @return This network's mobile country code.
        ************************************************************************/
        public String getCountryCode()
        {
            return iCountryCode;
        }

        /************************************************************************
        *   Returns the network's mobile network code.
        *
        *   @return This network's mobile network code.
        ************************************************************************/
        public String getNetworkCode()
        {
            return iNetworkCode;
        }
    }
