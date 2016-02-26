
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
        *   Checks if this device's API-Level is lower than API-Level 3.
        *   The field {@link android.os.Build.VERSION#SDK_INT} is read to determine this.
        *
        *   @return     <code>true</code> if this device uses an API-Level lower than 3.
        *               Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSdkLevelLower3()
        {
            return ( android.os.Build.VERSION.SDK_INT < 3 );
        }

        /*********************************************************************************
        *   Checks if this device's API-Level is lower than API-Level 9.
        *   The field {@link android.os.Build.VERSION#SDK_INT} is read to determine this.
        *
        *   @return     <code>true</code> if this device uses an API-Level lower than 9.
        *               Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSdkLevelLower9()
        {
            return ( android.os.Build.VERSION.SDK_INT < 9 );
        }

        /*********************************************************************************
        *   Checks if this device's API-Level is lower than API-Level 4.
        *   The field {@link android.os.Build.VERSION#SDK_INT} is read to determine this.
        *
        *   @return     <code>true</code> if this device uses an API-Level lower than 4.
        *               Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSdkLevelLower4()
        {
            return ( android.os.Build.VERSION.SDK_INT < 4 );
        }

        /*********************************************************************************
        *   Checks if this device's API-Level is lower than API-Level 5.
        *   The field {@link android.os.Build.VERSION#SDK_INT} is read to determine this.
        *
        *   @return     <code>true</code> if this device uses an API-Level lower than 5.
        *               Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSdkLevelLower5()
        {
            return ( android.os.Build.VERSION.SDK_INT < 5 );
        }

        /*********************************************************************************
        *   Checks if this device's API-Level is lower than API-Level 14.
        *   The field {@link android.os.Build.VERSION#SDK_INT} is read to determine this.
        *
        *   @return     <code>true</code> if this device uses an API-Level lower than 14.
        *               Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSdkLevelLower14()
        {
            return ( android.os.Build.VERSION.SDK_INT < 14 );
        }
    }
