
    package de.mayflower.lib.ui;

    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.net.*;
    import  android.support.v4.app.*;

    /************************************************************************
    *   Manages notifications.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
    }
