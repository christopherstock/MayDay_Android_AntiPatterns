/*  $Id: LibAPI.java 50182 2013-07-25 09:44:38Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.api;

    /*********************************************************************************
    *   Determines device api.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50182 $ $Date: 2013-07-25 11:44:38 +0200 (Do, 25 Jul 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/api/LibAPI.java $"
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
