/*  $Id: LibNotification.java 50238 2013-07-29 15:23:21Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui;

    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.net.*;
    import  android.support.v4.app.*;

    /************************************************************************
    *   Manages notifications.
    *
    *   @author         $Author: schristopher $
    *   @version        $Rev: 50238 $ $Date: 2013-07-29 17:23:21 +0200 (Mo, 29 Jul 2013) $
    *   @see            "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/LibNotification.java $"
    ************************************************************************/
    public class LibNotification
    {
        /************************************************************************
        *   Put the GCM message into a notification and post it.
        *
        *   @param  context             The current system context.
        *   @param  title               The notification's title text.
        *   @param  msg                 The notification's content text.
        *   @param  activityToLaunch    The class to launch when the item is pressed by the user.
        *   @param  notificationID      The internal group ID for this notification icon.
        *                               Newer notification items with the same group ID
        *                               will override older notification items.
        *   @param  notificationIconID  The resource-ID of the icon to display for this notification.
        *   @param  largeIcon           The icon to display in the notification body.
        *   @param  uri                 The data-uri to invoke when the item is pressed by the user.
        ************************************************************************/
        public static final void showCustomNotification
        (
            Context     context,
            String      title,
            String      msg,
            Class<?>    activityToLaunch,
            int         notificationID,
            int         notificationIconID,
            Bitmap      largeIcon,
            Uri         uri
        )
        {
            NotificationManager mNotificationManager    = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
            Intent              intent                  = new Intent( context, activityToLaunch );
            intent.setData( uri );
            PendingIntent       contentIntent           = PendingIntent.getActivity( context, 0, intent, 0 );

            //create Builder
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( context );
            mBuilder.setSmallIcon( notificationIconID );
            mBuilder.setContentTitle( title );
            mBuilder.setStyle( new NotificationCompat.BigTextStyle().bigText( msg ) );
            mBuilder.setContentText(msg);
            mBuilder.setLargeIcon( largeIcon );
            mBuilder.setContentIntent( contentIntent );

            //create Notification
            Notification notification = mBuilder.build();

            //show Notification
            mNotificationManager.notify( notificationID, notification );
        }

        /*****************************************************************************************
        *   Confects and shows the "welcome notification" in the notification bar.
        *
        *   @param  notificationID          The internal group ID for this notification icon.
        *                                   Newer notification items with the same group ID
        *                                   will override older notification items.
        *   @param  context                 The application context to use.
        *   @param  iconID                  The resource-ID of the icon to display for this notification.
        *   @param  activityClassToLaunch   The class to launch when the item is pressed by the user.
        *   @param  textTicker              The text for the notification-ticker in the status-line.
        *   @param  textTitle               The notification's title text.
        *   @param  textBody                The notification's content text.
        *   @param  uri                     The data-uri to invoke when the item is pressed by the user.
        *****************************************************************************************/
/*
        public static final Notification showCustomNotification
        (
            int         notificationID,
            Context     context,
            int         iconID,
            Class<?>    activityClassToLaunch,
            String      textTicker,
            String      textTitle,
            String      textBody,
            Uri         uri,
            int         imageID
        )
        {
            long            when                = System.currentTimeMillis();
            Intent          notificationIntent  = new Intent( context, activityClassToLaunch );
                            if ( uri != null ) notificationIntent.setData( uri );
            PendingIntent   pendingIntent       = PendingIntent.getActivity( context, 0, notificationIntent, 0 );
            Notification    notification        = new Notification( iconID, textTicker, when );

            //get ids for layout and its components
            int layoutID    = context.getResources().getIdentifier( "synapsy_odp_odp3_notificationwelcome",     "layout",   context.getPackageName() );
            int titleViewID = context.getResources().getIdentifier( "synapsy_odp_notificationwelcometitle",     "id",       context.getPackageName() );
            int textViewID  = context.getResources().getIdentifier( "synapsy_odp_notificationwelcometext",      "id",       context.getPackageName() );
            int imageViewID = context.getResources().getIdentifier( "synapsy_odp_notificationwelcomeappicon",   "id",       context.getPackageName() );

            RemoteViews contentView = new RemoteViews( context.getPackageName(), layoutID );
            contentView.setTextViewText(        titleViewID, Html.fromHtml( textTitle ) );
            contentView.setTextViewText(        textViewID,  Html.fromHtml( textBody  ) );
            contentView.setImageViewResource(   imageViewID, imageID                    );

            notification.contentView    = contentView;
            notification.contentIntent  = pendingIntent;

            notification.flags    |= Notification.FLAG_AUTO_CANCEL;     //clear notification after being selected
          //notification.defaults |= Notification.DEFAULT_SOUND;        //play user's default notify sound
          //notification.defaults |= Notification.DEFAULT_VIBRATE;      //default vibration behaviour ( requires permission 'vibrate' )
          //notification.defaults |= Notification.DEFAULT_LIGHTS;       //default light behaviour

            //show the notification via the notification manager
            NotificationManager notificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.notify( notificationID, notification );

            return notification;
        }
*/
    }
