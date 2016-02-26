
    package de.mayflower.lib.api;

    import  android.annotation.*;
    import  android.app.*;
    import  android.telephony.*;
    import  de.mayflower.lib.*;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 4 or higher.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
