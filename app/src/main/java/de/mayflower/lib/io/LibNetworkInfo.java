
    package de.mayflower.lib.io;

    import  android.content.*;
    import  android.telephony.*;
    import  de.mayflower.lib.*;

    /************************************************************************
    *   The roaming system checks if the user is in roaming state.
    *   This can be determined by comparing the current mobile carrier's country-
    *   and network-code with a reference list.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class LibNetworkInfo
    {
        /** The current country code being assigned to the user's sim. */
        private                     String              currentCountryCode          = null;

        /** The current network code being assigned to the user's sim. */
        private                     String              currentNetworkCode          = null;

        /** The msisdn being assigned to the user's sim. */
        private                     String              msisdn                      = null;

        /************************************************************************
        *   A 64-bit number (as a hex string) being randomly generated
        *   on the device's first boot. It should remain constant for the lifetime of the device.
        *   (The value may change if a factory reset is performed on the device.)
        ************************************************************************/
        private                     String              androidId                   = null;

        /** Flags if the user is currently using the home network. */
        private                     boolean             userUsesHomeNetwork         = false;

        /** The reference home-networks required to determine if the user is in roaming. */
        private                     LibNetwork[]    iHomeNetworks               = null;

        /************************************************************************
        *   Creates a new roaming-system.
        *
        *   @param  aContext        The current application context.
        *   @param  aHomeNetworks   The reference list of networks that shall be
        *                           treated as home-networks.
        ************************************************************************/
        public LibNetworkInfo( Context aContext, LibNetwork[] aHomeNetworks )
        {
                                iHomeNetworks       = ( aHomeNetworks == null ? new LibNetwork[] {} : aHomeNetworks );
            TelephonyManager    telephonyManager    = (TelephonyManager)( aContext.getSystemService( Context.TELEPHONY_SERVICE ) );
            String              operator            = telephonyManager.getNetworkOperator();
                                msisdn              = telephonyManager.getLine1Number();
                                androidId           = Lib.getAndroidID( aContext );

            //check if the operator could be read
            if ( operator != null && operator.length() > 3 )
            {
                //read mcc and mnc ( static and current )
                currentCountryCode = operator.substring( 0, 3 );
                currentNetworkCode = operator.substring( 3 );

                //check if the user uses the home-network
                for ( int i = 0; i < iHomeNetworks.length; ++i )
                {
                    if
                    (
                            currentCountryCode.equals( iHomeNetworks[ i ].getCountryCode() )
                        &&  currentNetworkCode.equals( iHomeNetworks[ i ].getNetworkCode() )
                    )
                    {
                        userUsesHomeNetwork = true;
                        break;
                    }
                }
            }
        }

        /************************************************************************
        *   Returns the sim's current country code.
        *
        *   @return     The international mobile country-code the sim is using.
        ************************************************************************/
        public final String getCurrentCountryCode()
        {
            return currentCountryCode;
        }

        /************************************************************************
        *   Returns the sim's current network code.
        *
        *   @return     The international mobile network-code the sim is using.
        ************************************************************************/
        public final String getCurrentNetworkCode()
        {
            return currentNetworkCode;
        }

        /************************************************************************
        *   Determines if the sim is currently using the home-network.
        *
        *   @return     <code>true</code> if the sim is using a home-network
        *               from the reference list. Otherwise <code>false</code>.
        ************************************************************************/
        public final boolean getUserUsesHomeNetwork()
        {
            return userUsesHomeNetwork;
        }

        /************************************************************************
        *   Returns the sim's msisdn if it could be determined by the {@link TelephonyManager}.
        *   This is not possible on all devices so do NOT rely on this function.
        *   Only use it for additional information purposes!
        *
        *   @return     The msisdn of the sim or <code>null</code> if it could not be read.
        ************************************************************************/
        public final String getMsisdn()
        {
            return msisdn;
        }

        /************************************************************************
        *   Returns the device's android-ID if it could be determined.
        *   Devices with an API-Level lower than 3 will not receive an android-id
        *   so do NOT rely on this function.
        *   Only use it for additional information purposes!
        *
        *   @return     The android-id of the device or an empty string if it could not be read.
        ************************************************************************/
        public final String getAndroidId()
        {
            return androidId;
        }
    }
