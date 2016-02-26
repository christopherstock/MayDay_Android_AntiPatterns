
    package de.mayflower.lib.io;

    /************************************************************************
    *   Represents a service-provider network with the assigned country-code
    *   and network-code.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
