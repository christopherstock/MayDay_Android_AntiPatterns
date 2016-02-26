/*  $Id: LibModernAPI3.java 49251 2013-05-22 09:51:24Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.api;

    import  android.annotation.*;
    import  android.content.*;
    import  android.provider.*;
    import  android.provider.Settings.Secure;
    import  android.provider.Settings.SettingNotFoundException;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 3 or higher.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 49251 $ $Date: 2013-05-22 11:51:24 +0200 (Mi, 22 Mai 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/api/LibModernAPI3.java $"
    *********************************************************************************/
    @SuppressLint("NewApi")
    public abstract class LibModernAPI3
    {
        /*********************************************************************************
        *   Checks if the system's setting 'unknown sources' is enabled.
        *   If this setting is disabled, only packages from the AndroidMarket can be installed.
        *
        *   @param  context     The current application context.
        *   @return             <code>true</code> if the 'unknown sources' setting is enabled.
        *                       Otherwise <code>false</code>.
        *********************************************************************************/
        @SuppressWarnings( "deprecation" )
        public static final boolean isSettingUnknownSourcesEnabled( Context context )
        {
            try
            {
                return ( Settings.Secure.getInt( context.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS ) == 1 );
            }
            catch ( SettingNotFoundException snfe )
            {
                return true;
            }
        }

        /*********************************************************************************
        *   Switches to the system's setting-menu 'application settings'
        *   where the user can alter his setting for 'unknown sources'.
        *
        *   @param  context     The current application context.
        *********************************************************************************/
        public static final void launchSettingsUnknownSources( Context context )
        {
            //lead user to the application settings
            Intent intent = new Intent();
            intent.setAction( Settings.ACTION_APPLICATION_SETTINGS );
            context.startActivity( intent );
        }

        /*********************************************************************************
        *   Delivers the device's android-id. This is a 64-bit number (as a hex string)
        *   being randomly generated on the device's first boot.
        *   It should remain constant for the lifetime of the device.
        *   The value may change if a factory reset is performed on the device.
        *
        *   @param  context     The current application context.
        *   @return             The device's android-id.
        *********************************************************************************/
        public static final String getAndroidID( Context context )
        {
            return Secure.getString( context.getContentResolver(), Secure.ANDROID_ID );
        }
    }
