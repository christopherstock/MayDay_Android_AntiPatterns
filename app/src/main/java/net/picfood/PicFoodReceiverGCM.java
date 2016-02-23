/*  $Id: PicFoodReceiverGCM.java 50546 2013-08-09 14:19:00Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood;

    import  org.json.*;
    import  net.picfood.PicFoodSettings.Notification;
    import  net.picfood.ext.gcm.*;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.SaveKey;
    import  net.picfood.io.jsonrpc.*;
    import  android.app.*;
    import  android.content.*;
    import  android.net.*;
    import  com.google.android.gms.gcm.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.*;

    /**********************************************************************************************
    *   The Google Cloud Messaging receiver.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50546 $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/PicFoodReceiverGCM.java $"
    ***********************************************************************************************/
    public class PicFoodReceiverGCM extends BroadcastReceiver
    {
        @Override
        public void onReceive( final Context context, final Intent intent )
        {
            PicFoodDebug.gcm.out( "PicFoodReceiverGCM::onReceive()" );

            //init UncaughtExceptionHandler
            PicFoodSystems.initUncaughtExceptionHandler();

            //check GCM message
            GoogleCloudMessaging    gcm         = GoogleCloudMessaging.getInstance( context );
            String                  messageType = gcm.getMessageType( intent );

            if ( GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals( messageType ) )
            {
                PicFoodDebug.gcm.out( "PicFoodReceiverGCM::onReceive() - Send error occured!" );
            }
            else if ( GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals( messageType ) )
            {
                PicFoodDebug.gcm.out( "PicFoodReceiverGCM::onReceive() - Message is already deleted from server" );
            }
            else
            {
                PicFoodDebug.gcm.out( "PicFoodReceiverGCM::onReceive() - Action [" + intent.getAction() + "] Received: [" + intent.getExtras().toString() + "]" );

                //handle the action in a new thread
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        if ( intent.getAction().equals( "com.google.android.c2dm.intent.REGISTRATION" ) )
                        {
                            handleRegistration( context, intent );
                        }
                        else if ( intent.getAction().equals( "com.google.android.c2dm.intent.RECEIVE" ) )
                        {
                            handleReceivedMessage( context, intent );
                        }
                    }
                }.start();

                //set positive result code
                setResultCode( Activity.RESULT_OK );
            }
        }

        /**********************************************************************************************
        *   Being invoked when a registration broadcast has been received.
        *
        *   @param      context     The current system context.
        *   @param      intent      The transfered system intent.
        ***********************************************************************************************/
        protected void handleRegistration( Context context, Intent intent )
        {
            PicFoodDebug.gcm.out( "PicFoodReceiverGCM::handleRegistration() - Handle registration" );

            //pick registration id and assign into RAM
            String regId = intent.getStringExtra( "registration_id" );

            //show registration id
            PicFoodDebug.gcm.out( "PicFoodReceiverGCM::handleRegistration() - registrationID: [" + regId + "]" );

            //transfer the registration-ID to the backend
            try
            {
                JSONObject  response    = PicFoodJsonRPCUser.setGCMRegistrationID( context, regId );
                int         statusCode  = Integer.parseInt( LibJSON.getJSONStringSecure( response, "status" ) );
                switch ( statusCode )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //transmit registration id to RAM
                        PicFoodGoogleCloudMessaging.registrationID = regId;

                        //save registration id to persistent storage
                        PicFoodSave.saveSetting( context, SaveKey.EGoogleCloudMessagingRegistrationID, regId );

                        PicFoodDebug.gcm.out( "SUCCESSFULLY SAVED registration ID [" + PicFoodGoogleCloudMessaging.registrationID + "] to RAM and PERSISTENT STORAGE!" );

                        break;
                    }

                    //ignore an expired session when the callback arrives - re-register in the next session!
                }
            }
            catch ( Throwable t )
            {
                //committing registration failed! Ignore this issue!
                PicFoodDebug.gcm.trace( t );
            }
        }

        /**********************************************************************************************
        *   Being invoked when a message has been received.
        *
        *   @param      context     The current system context.
        *   @param      intent      The transfered system intent.
        ***********************************************************************************************/
        protected void handleReceivedMessage( Context context, Intent intent )
        {
            PicFoodDebug.gcm.out( "PicFoodReceiverGCM::handleReceivedMessage() - Handle received message" );

            String action       = intent.getExtras().getString( "action"        );
            String body         = intent.getExtras().getString( "body"          );
            String title        = intent.getExtras().getString( "title"         );
            String collapseKey  = intent.getExtras().getString( "collapse_key"  );

            //check if collapse-key can be converted into an integer
            int collapseKeyInt  = Notification.DEFAULT_PUSH_NOTIFICATION_ID;
            try
            {
                collapseKeyInt  = Integer.parseInt( collapseKey );
            }
            catch ( Throwable t )
            {
            }

            PicFoodDebug.gcm.out( "    action:         [" + action         + "]" );
            PicFoodDebug.gcm.out( "    body:           [" + body           + "]" );
            PicFoodDebug.gcm.out( "    title:          [" + title          + "]" );
            PicFoodDebug.gcm.out( "    collapse_key:   [" + collapseKey    + "]" );
            PicFoodDebug.gcm.out( "    collapseKeyInt: [" + collapseKeyInt + "]" );

            LibNotification.showCustomNotification
            (
                context,
                title,
                body,
                PicFood.class,
                collapseKeyInt,
                R.drawable.net_picfood_icon_notification_bar,
                LibResource.getResourceBitmap( context, R.drawable.net_picfood_icon_in_notification ),
                Uri.parse( "test" )
            );
        }
    }
