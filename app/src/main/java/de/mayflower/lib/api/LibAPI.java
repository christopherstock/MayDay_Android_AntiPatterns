
    package de.mayflower.lib.api;

    /*********************************************************************************
    *   Determines device api.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************/
    public abstract class LibAPI
    {
        /*********************************************************************************
        *   Checks if this device's API-Level is lower than given API-Level.
        *   The field {@link android.os.Build.VERSION#SDK_INT} is read to determine this.
        *
        *   @return     <code>true</code> if this device uses an API-Level lower than 3.
        *               Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSdkLevelLowerThen(int apiLevel)
        {
            return ( android.os.Build.VERSION.SDK_INT < apiLevel );
        }
    }
