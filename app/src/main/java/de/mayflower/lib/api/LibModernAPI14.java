
    package de.mayflower.lib.api;

    import  android.annotation.*;
    import  android.content.*;
    import  android.provider.*;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 14 or higher.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
