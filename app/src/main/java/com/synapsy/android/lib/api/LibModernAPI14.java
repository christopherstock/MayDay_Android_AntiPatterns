/*  $Id: LibModernAPI14.java 49251 2013-05-22 09:51:24Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.api;

    import  android.annotation.*;
    import  android.content.*;
    import  android.provider.*;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 14 or higher.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 49251 $ $Date: 2013-05-22 11:51:24 +0200 (Mi, 22 Mai 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/api/LibModernAPI14.java $"
    *********************************************************************************/
    @SuppressLint("NewApi")
    public abstract class LibModernAPI14
    {
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
            intent.setAction( Settings.ACTION_SECURITY_SETTINGS );
            context.startActivity( intent );
        }
    }
