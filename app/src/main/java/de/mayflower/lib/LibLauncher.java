
    package de.mayflower.lib;

    import  java.io.*;
    import  java.util.*;
    import  android.app.*;
    import  android.content.*;
    import  android.content.pm.*;
    import  android.net.*;
    import  android.provider.*;
    import  de.mayflower.lib.api.*;
    import  de.mayflower.lib.ui.*;

    /*********************************************************************************
    *   Launches internal or external system activities.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************/
    public abstract class LibLauncher
    {
        /*********************************************************************************
        *   Launches another Activity of the current application package.
        *
        *   @param  context                 The according activity context.
        *   @param  activityClassToLaunch   The Activity class of the Activity to start.
        *   @param  animIn                  The resource-ID of the animation for the new Activity to appear.
        *   @param  animOut                 The resource-ID of the animation for the old Activity to disappear.
        *********************************************************************************/
        public static final void launchActivity( Activity context, Class<?> activityClassToLaunch, int animIn, int animOut )
        {
            //create activity launch intent and start it
            Intent launchActivity = new Intent( context, activityClassToLaunch );
            context.startActivity(launchActivity);

if (true) return;

            //set animation if desired
            if ( animIn != -1 && animOut != -1 )
            {
                //only operative since API-level 5
                if ( !LibAPI.isSdkLevelLower5() )
                {
                    LibModernAPI5.overridePendingTransition( context, animIn, animOut );
                }
            }
        }

        /*********************************************************************************
        *   Opens the AndroidMarket on the device, pointing to a specified package.
        *
        *   @param  context                 The current application context.
        *   @param  packageNameToShowUp     The packagename of the application from the
        *                                   AndroidMarket to show.
        *   @param  aDebug                  The debug flag to trace this function.
        *********************************************************************************/
        public static final void launchAndroidMarket( Context context, String packageNameToShowUp, LibDebug aDebug )
        {
            launchPlatformRequest( context, "market://details?id=" + packageNameToShowUp, aDebug );
        }

        /*********************************************************************************
        *   Launches the system's camera application. This happens with a call to
        *   {@link Activity#startActivityForResult(Intent, int)}, so a requestCode
        *   must be specified.
        *
        *   @param  activity        The according activity context.
        *   @param  requestCode     The requestcode to propagate to the camera Activity.
        *********************************************************************************/
        public static final void launchCamera( Activity activity, int requestCode )
        {
            Intent intent = new Intent( "android.media.action.IMAGE_CAPTURE" ); //, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
            activity.startActivityForResult( intent, requestCode );
        }

        /*****************************************************************************************
        *   Launches an installed activity.
        *
        *   @param  context             The application context to use.
        *   @param  packageNameToStart  The package name of the activity to start.
        *   @param  classNameToStart    The class name of the activity to start.
        *                               The fully qualified classname is to be specified because
        *                               the class to start can be in a totally different package
        *                               than that being specified by <code>packageNameToStart</code>.
        *   @param  debug               The debug switch.
        *****************************************************************************************/
        public static final void launchExternalActivity( Context context, String packageNameToStart, String classNameToStart, LibDebug debug )
        {
            try
            {
                debug.out( "launch activity [" + packageNameToStart + "] [" + classNameToStart + "]" );

                //start the activity. setting the fully-qualified class-name is a MUST! .Class is NOT operative, although documented!
                Intent intent = new Intent();                   //Intent.ACTION_RUN is also operative
                intent.setAction( Intent.ACTION_MAIN );
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT );
                intent.setClassName( packageNameToStart, classNameToStart );

                context.startActivity( intent );
            }
            catch ( Throwable t )
            {
              //debug.logAppend( " THROWABLE THROWN ON STARTING ACTIVITY." );
                debug.out( " THROWABLE THROWN - ERROR STARTING ACTIVITY. [" + t + "]" );
            }
        }

        /*********************************************************************************
        *   Launches the system's image-picker ( gallery browser ).
        *   This happens with a call to {@link Activity#startActivityForResult(Intent, int)},
        *   so a requestCode must be specified.
        *
        *   @param  activity        The according activity context.
        *   @param  requestCode     The requestcode to propagate to the image-picker Activity.
        *********************************************************************************/
        public static final void launchImagePicker( Activity activity, int requestCode )
        {
            Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
            activity.startActivityForResult( intent, requestCode );
        }

        /*********************************************************************************
        *   Launches a phone call. This does not launch the phone call directly
        *   but opens the phone-activity with the preset phonenumber.
        *
        *   @param  context         The current system context.
        *   @param  phoneNumber     The phone number to transfer to the phone-activity.
        *   @param  aDebug          The debug context.
        *********************************************************************************/
        public static final void launchPhoneCall( Context context, String phoneNumber, LibDebug aDebug )
        {
            launchPlatformRequest( context, "tel:" + phoneNumber, aDebug );
        }

        /*********************************************************************************
        *   Launches an url and lets the system decide which program it handles. Http-urls are handled
        *   by the device-browser and Market-urls are handled by the AndroidMarket for instance.
        *
        *   @param  context The current application context.
        *   @param  url     The url to open.
        *   @param  debug   The debug flag for tracing this function.
        *********************************************************************************/
        public static final void launchPlatformRequest( Context context, String url, LibDebug debug )
        {
            try
            {
                Uri uri = Uri.parse( url );

                //start the activity. setting the fully-qualified class-name is a MUST! .Class is NOT operative, although documented!
                Intent intent = new Intent();                   //Intent.ACTION_RUN is also operative
                intent.setAction( Intent.ACTION_VIEW );
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT );
                intent.setData( uri );

                context.startActivity( intent );
            }
            catch ( Throwable t )
            {
              //debug.logAppend( " THROWABLE THROWN LAUNCHING PLATFORM REQUEST" );
                debug.trace( t );
            }
        }

        /*********************************************************************************
        *   Launches the GPS-Settings page.
        *
        *   @param  context         The current system context.
        *********************************************************************************/
        public static final void launchSettingsGPS( Context context )
        {
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
            context.startActivity( myIntent );
        }

        /*********************************************************************************
        *   Switches to the system's setting-menu 'application settings'
        *   where the user can alter his setting for 'unknown sources'.
        *   This function will not perform anything if the API-level of the device is lower 3.
        *
        *   @param  context     The current application context.
        *********************************************************************************/
        public static final void launchSettingsUnknownSources( Context context )
        {
            //only for api level 3 or higher!
            if ( LibAPI.isSdkLevelLower3() )
            {
                //do nothing!
            }
            else if ( LibAPI.isSdkLevelLower4() )
            {
                LibModernAPI3.launchSettingsUnknownSources( context );
            }
            else if ( LibAPI.isSdkLevelLower14() )
            {
                LibModernAPI3.launchSettingsUnknownSources( context );
            }
            else
            {
                LibModernAPI14.launchSettingsUnknownSources( context );
            }
        }

        /*********************************************************************************
        *   Launches the SMS-App with the preset phone number and body.
        *
        *   @param  context         The current system context.
        *   @param  phoneNumber     The phonenumber to preset in the SMS-form.
        *   @param  body            The SMS-body to preset in the SMS-form.
        *   @param  aDebug          The debug context.
        *********************************************************************************/
        public static final void launchSMS( Context context, String phoneNumber, String body, LibDebug aDebug )
        {
            launchPlatformRequest( context, "sms://" + phoneNumber + "?body=" + body, aDebug );
        }

        /*********************************************************************************
        *   Launches the system's image crop activity. This happens with a call to
        *   {@link Activity#startActivityForResult(Intent, int)}, so a requestCode must be specified.
        *
        *   @param  imageURI        The uri of the source image to crop resides.
        *   @param  cropURI         The uri of the destination where the cropped image is saved.
        *   @param  activity        The according activity context.
        *   @param  requestCode     The request code to propagate to the crop-activity.
        *   @param  aspectX         The desired horizontal aspect-ratio for the cropping rect.
        *   @param  aspectY         The desired vertical aspect-ratio for the cropping rect.
        *********************************************************************************/
        public static final void launchImageCrop( Uri imageURI, Uri cropURI, Activity activity, int requestCode, int aspectX, int aspectY )
        {
            Intent queryIntent = new Intent( "com.android.camera.action.CROP" );
            queryIntent.setType( "image/*" );

            //resolve all suitable activities
            List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities( queryIntent, 0 );

            //check if suitable activity has been found
            int size = list.size();
            if ( size == 0 )
            {
                LibUI.showToastUIThreaded( activity, "Unable to find image crop activity!", true );
                return;
            }

            //pick 1st resolve info and create new intent
            Intent      cropIntent   = new Intent( queryIntent );
            ResolveInfo res = list.get( 0 );

          //i.setComponent( new ComponentName( res.activityInfo.packageName, res.activityInfo.name ) );
            cropIntent.setClassName( res.activityInfo.packageName, res.activityInfo.name );

            //set image-url
            cropIntent.setData( imageURI );

            //put extra data
            cropIntent.putExtra( "aspectX",         aspectX                                     );
            cropIntent.putExtra( "aspectY",         aspectY                                     );
            cropIntent.putExtra( "return-data",     false                                       );
            cropIntent.putExtra( "crop",            true                                        );
            cropIntent.putExtra( "setWallpaper",    false                                       );
            cropIntent.putExtra( "output",          cropURI                                     );

            //launch external activity
            activity.startActivityForResult( cropIntent, requestCode );
        }

        /*********************************************************************************
        *   Launches the system's 'share'-activity in order to share the specified url.
        *
        *   @param  context         The current system context.
        *   @param  url             The url to share.
        *   @param  chooserTitle    The title for the 'share via'-dialog.
        *   @param  subject         The subject of the entity to share.
        *********************************************************************************/
        public static final void launchShareURL( Context context, String url, String chooserTitle, String subject )
        {
            Intent i = new Intent( Intent.ACTION_SEND               );

            //set plain text type
            i.setType(  "text/plain"                                );

            i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET  );
            i.putExtra( Intent.EXTRA_SUBJECT, subject               );
            i.putExtra( Intent.EXTRA_TEXT,    url                   );
            context.startActivity( Intent.createChooser( i, chooserTitle ) );
        }

        /*********************************************************************************
        *   Launches the system's 'share'-activity in order to share bitmap data.
        *
        *   @param  context         The current system context.
        *   @param  imagePath       The location of the file to share.
        *   @param  title           The title for the 'share via'-dialog.
        *   @param  subject         The subject of the entity to share.
        *   @param  body            The body of the entity to share.
        *********************************************************************************/
        public static final void launchShareImage( Context context, File imagePath, String title, String subject, String body )
        {
            Intent share = new Intent( Intent.ACTION_SEND );

            //set image type
            share.setType( "image/*" );                                 //image/png image/jpeg
          //share.setType( "*/*" );

            Uri uri = Uri.fromFile( imagePath );
            share.putExtra( Intent.EXTRA_STREAM,  uri                   );
            share.putExtra( Intent.EXTRA_SUBJECT, subject               );
            share.putExtra( Intent.EXTRA_TEXT,    body                  );
            context.startActivity( Intent.createChooser( share, title ) );
        }
    }
