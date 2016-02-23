/*  $Id: LibModernAPI4.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.api;

    import  android.annotation.*;
    import  android.app.*;
    import  android.telephony.*;

    import  com.synapsy.android.lib.*;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 4 or higher.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/api/LibModernAPI4.java $"
    *********************************************************************************/
    @SuppressLint("NewApi")
    public abstract class LibModernAPI4
    {
        /*********************************************************************************
        *   Sends an sms for API-Levels higher or equal than 4.
        *
        *   @param  phoneNr     The phone-number to send the sms to.
        *   @param  msg         The sms body to send.
        *   @param  debug       The debug system for tracing sms.
        *   @param  sentIntent  A pending broadcast intent.
        *********************************************************************************/
        public static final void sendSMS( String phoneNr, String msg, LibDebug debug, PendingIntent sentIntent )
        {
            try
            {
                SmsManager smsm = SmsManager.getDefault();

                smsm.sendTextMessage( /* "sms://" + */ phoneNr, null, msg, sentIntent, null );
            }
            catch ( Exception e )
            {
                debug.out( "sendSMS() - Unable to send message\n" + e.toString() );
                debug.trace( e );
            }
        }
    }
