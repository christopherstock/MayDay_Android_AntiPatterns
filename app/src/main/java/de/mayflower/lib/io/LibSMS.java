
    package de.mayflower.lib.io;

    import  android.app.*;
    import  android.content.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.api.*;

    /************************************************************************
    *   Manages sending SMS.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class LibSMS
    {
        /*********************************************************************************
        *   Check the phone number for validity. Valid phone numbers contain
        *   only the digits 0 thru 9, and may contain a leading '+'.
        *
        *   @param  phoneNumberString   The phone number to check.
        *   @return                     <code>true</code> if number is a valid phone number,
        *                               otherwise <code>false</code>.
        *********************************************************************************/
        public static boolean isValidPhoneNumber( String phoneNumberString )
        {
            char[] chars = phoneNumberString.toCharArray();

            //check all chars
            for ( int i = 0; i < chars.length; ++i )
            {
                //allow '+' '-' and SPACE
                if ( chars[ i ] == '+' || chars[ i ] == '-' || chars[ i ] == ' ' ) continue;

                //return false if the current char is not a digit
                if ( !Character.isDigit( chars[ i ] ) )
                {
                    return false;
                }
            }

            return ( chars.length > 3 ? true : false );
        }

        /*********************************************************************************
        *   Sends an sms using the api-level-according functionality.
        *
        *   @param  context         The current application context.
        *   @param  phoneNr         The phone-number to send the sms to.
        *   @param  msg             The sms body to send.
        *   @param  debug           The debug system for tracing sms.
        *   @param  broadcastData   The name of the broadcast. This name is returned when the broadcast is received.
        *********************************************************************************/
        public static final void sendSMS( Context context, String phoneNr, String msg, LibDebug debug, String broadcastData )
        {
            PendingIntent sendIntent = Lib.getPendingIntentFromSpecifiedBroadcast( context, broadcastData );

            //switch api level
            if ( sendIntent != null )
            {
                if ( LibAPI.isSdkLevelLower4() )
                {
                    LibSMSDeprecated.sendSMS(   phoneNr, msg, debug, sendIntent );
                }
                else
                {
                    LibModernAPI4.sendSMS(       phoneNr, msg, debug, sendIntent );
                }
            }
        }

        /*********************************************************************************
        *   The SMS-sender for API-Levels lower than 4.
        *********************************************************************************/
        @SuppressWarnings( "deprecation" )
        public static final class LibSMSDeprecated
        {
            /*********************************************************************************
            *   Sends an sms for API-Levels lower than 4.
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
                    android.telephony.gsm.SmsManager smsm = android.telephony.gsm.SmsManager.getDefault();
                    smsm.sendTextMessage( /* "sms://" + */ phoneNr, null, msg, sentIntent, null );
                }
                catch ( Exception e )
                {
                    debug.out( "sendSMS() - Unable to send message\n" + e.toString() );
                    debug.trace( e );
                }
            }
        }
    }
