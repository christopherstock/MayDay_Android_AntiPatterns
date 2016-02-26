
    package de.mayflower.lib;

    import  java.io.*;
    import  java.math.*;
    import  java.security.*;
    import  java.util.*;
    import  android.app.*;
    import  android.content.*;
    import  android.content.pm.*;
    import android.content.pm.Signature;
    import  android.graphics.*;
    import  android.net.*;
    import  android.telephony.*;
    import  de.mayflower.lib.api.*;
    import  de.mayflower.lib.io.*;

    /*********************************************************************************
    *   Holds static utility functionality.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50546 $ $Date: 2013-08-09 16:19:00 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/Lib.java $"
    *********************************************************************************/
    public abstract class Lib
    {
        /** The amount of centimeters for one inch. */
        public  static  final   float                   CM_PER_INCH                     = 2.54f;

        /** The amount of milliseconds for one minute. */
        public  static  final   long                    MILLIS_PER_SECOND               = 1000;

        /** The amount of milliseconds for one minute. */
        public  static  final   long                    MILLIS_PER_MINUTE               = 60 * MILLIS_PER_SECOND;

        /** The amount of milliseconds for one hour. */
        public  static  final   long                    MILLIS_PER_HOUR                 = 60 * MILLIS_PER_MINUTE;

        /** The amount of milliseconds for one day. */
        public  static  final   long                    MILLIS_PER_DAY                  = 24 * MILLIS_PER_HOUR;

        /** The amount of milliseconds for one week. */
        public  static  final   long                    MILLIS_PER_WEEK                 = 7 * MILLIS_PER_DAY;

        /** The amount of milliseconds for an avarage month. */
        public  static  final   long                    MILLIS_PER_MONTH                = 30 * MILLIS_PER_DAY;

        /** The amount of milliseconds for an avarage year. */
        public  static  final   long                    MILLIS_PER_YEAR                 = 365 * MILLIS_PER_DAY;

        /** The number of bytes in 1 KB. */
        public  static  final   long                    BYTES_1_KB                      = 1024;

        /** The number of bytes in 256 KB. */
        public  static  final   long                    BYTES_256_KB                    = 256  * BYTES_1_KB;

        /** The number of bytes in 1 MegaByte. */
        public  static  final   long                    BYTES_1_MB                      = 1024 * BYTES_1_KB;

        /** The number of bytes in 1 GigaByte. */
        public  static  final   long                    BYTES_1_GB                      = 1024 * BYTES_1_MB;

        /** The number of bytes in 1 TerraByte. */
        public  static  final   long                    BYTES_1_TB                      = 1024 * BYTES_1_GB;

        /** The package-name of Google's AndroidMarket. */
        public  static  final   String                  PACKAGE_NAME_ANDROID_MARKET     = "com.android.vending";

        /*********************************************************************************
        *   Checks if an object is contained in a stack of objects.
        *
        *   @param  haystack    The stack to check presence of the needle.
        *   @param  needle      The object to search for in the haystack.
        *   @return             <code>true</code> if the needle is contained in the haystack.
        *                       Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean containsObject( Object[] haystack, Object needle )
        {
            for ( int i = 0; i < haystack.length; ++i )
            {
                if ( haystack[ i ] == needle ) return true;
            }

            return false;
        }

        /*********************************************************************************
        *   Checks if a String is contained in a stack of Strings.
        *
        *   @param  haystack    The stack to check presence of the needle.
        *   @param  needle      The String to search for in the haystack.
        *   @return             <code>true</code> if the needle is contained in the haystack.
        *                       Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean containsString( String[] haystack, String needle )
        {
            for ( int i = 0; i < haystack.length; ++i )
            {
                if ( haystack[ i ].equals( needle ) ) return true;
            }

            return false;
        }

        /*********************************************************************************
        *   Checks if one of many Strings is contained in a List of Strings.
        *
        *   @param  haystack    The stack to check presence of any needle.
        *   @param  needles     The Strings to search in the haystack.
        *   @return             <code>true</code> if any of the specified needles
        *                       is contained in the haystack. Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean containsStrings( List<String> haystack, String[] needles )
        {
            for ( String needle : needles )
            {
                if ( !haystack.contains( needle ) )
                {
                    return false;
                }
            }

            return true;
        }

        /*********************************************************************************
        *   Checks if an int is contained in a stack of ints.
        *
        *   @param  haystack    The stack to check presence of the needle.
        *   @param  needle      The int to search for in the haystack.
        *   @return             <code>true</code> if the needle is contained in the haystack.
        *                       Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean contains( int[] haystack, int needle )
        {
            for ( int i = 0; i < haystack.length; ++i )
            {
                if ( haystack[ i ] == needle ) return true;
            }

            return false;
        }

        /*********************************************************************************
        *   Constructs a {@link PendingIntent} from the specified {@link Uri}.
        *   This Intent targets the own application.
        *
        *   @param  context     The current application context.
        *   @param  uri         The data-uri for this intent.
        *   @return             A pending activity-intent that target the own application to startup
        *                       with a data-uri. This invokes Activity's method onNewIntent.
        *                       when the pending intent is activated by a user interaction.
        *********************************************************************************/
        public static final PendingIntent getPendingIntentFromUri( Context context, Uri uri )
        {
            try
            {
                PendingIntent pendingIntent = PendingIntent.getActivity( context, 0, new Intent( Intent.ACTION_VIEW, uri ), 0 );
                return pendingIntent;
            }
            catch ( Throwable t )
            {
                return null;
            }
        }

        /*********************************************************************************
        *   Constructs a {@link PendingIntent} with the specified broadcast-name.
        *
        *   @param  context         The current application context.
        *   @param  broadcastName   The name of the broadcast.
        *   @return                 A pending broadcast-intent with the specified broadcast name.
        *                           with a data-uri. The registered broadcast-listener with
        *                           the broadcastName will be informed when the pending intent delivers a result.
        *********************************************************************************/
        public static final PendingIntent getPendingIntentFromSpecifiedBroadcast( Context context, String broadcastName )
        {
            try
            {
                PendingIntent pendingIntent = PendingIntent.getBroadcast( context, 0, new Intent( broadcastName ), 0 );
                return pendingIntent;
            }
            catch ( Throwable t )
            {
                return null;
            }
        }

        /*********************************************************************************
        *   Creates a shortcut to the specified application on the user's homescreen.
        *
        *   @param  activity        The current application context.
        *   @param  packageName     The package name of the application to start when the shortcut is selected.
        *   @param  mainClassName   The main-class-name of the activity to start when the shortcut is selected.
        *   @param  caption         The caption for the shortcut.
        *   @param  iconResourceID  The resource id of the icon to set for this shortcut.
        *********************************************************************************/
        public static final void createShortcutIcon( final Activity activity, final String packageName, final String mainClassName, final String caption, final int iconResourceID )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Intent shortcutIntent = new Intent();
                        shortcutIntent.setClassName( packageName, mainClassName );

                        Intent intent = new Intent();
                        intent.putExtra(    Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent    );

                        //we disallow the creation of duplicate shortcuts - ( undocumented! )
                        intent.putExtra(    "duplicate", false                              );

                        intent.putExtra(    Intent.EXTRA_SHORTCUT_NAME, caption             );
                        intent.putExtra(    Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext( activity, iconResourceID ) );
                        intent.setAction(   "com.android.launcher.action.INSTALL_SHORTCUT"  );

                        activity.sendBroadcast( intent );
                    }
                }
            );
        }

        /*********************************************************************************
        *   Delays the current Thread for the specified amount of milliseconds.
        *
        *   @param  millis  The number of milliseconds to delay the current Thread for.
        *********************************************************************************/
        public static final void sleepCurrentThread( long millis )
        {
            try
            {
                Thread.sleep( millis );
            }
            catch ( InterruptedException ie ) {}
        }

        /*********************************************************************************
        *   Launches the Android Homescreen Activity. This will show the user's
        *   homescreen immediately and pushes this application in the background.
        *
        *   @param  context The current application context.
        *********************************************************************************/
        public static final void showHomescreenActivity( Context context )
        {
            Intent intent = new Intent();
            intent.setAction(   Intent.ACTION_MAIN      );
            intent.addCategory( Intent.CATEGORY_HOME    );

            //start this activity
            context.startActivity( intent );
        }

        /*********************************************************************************
        *   Checks if the system's setting 'unknown sources' is enabled.
        *   If this setting is disabled, only packages from the AndroidMarket can be installed.
        *   This function is only operative for devices with an API-level of 3 or higher.
        *
        *   @param  context     The current application context.
        *   @return             <code>true</code> if the 'unknown sources' setting is enabled
        *                       or if the API-level of the device is lower than 3.
        *                       Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSettingUnknownSourcesEnabled( Context context )
        {
            //only for api level 3 or higher!
            if ( LibAPI.isSdkLevelLower3() )
            {
                //always return 'setting is enabled' so the warning will not show up
                return true;
            }

            return LibModernAPI3.isSettingUnknownSourcesEnabled( context );
        }

        /*********************************************************************************
        *   Delivers the device's android-id for devices with an API-Level of 3 or higher.
        *   This is a 64-bit number (as a hex string)
        *   being randomly generated on the device's first boot.
        *   It should remain constant for the lifetime of the device.
        *   The value may change if a factory reset is performed on the device.
        *
        *   @param  context     The current application context.
        *   @return             The device's android-id or an empty String
        *                       if the device's API-Level is lower than 3.
        *********************************************************************************/
        public static String getAndroidID( Context context )
        {
            //only for api level 3 or higher!
            if ( LibAPI.isSdkLevelLower3() )
            {
                //return an empty android-id
                return "";
            }

            return LibModernAPI3.getAndroidID( context );
        }

        /*********************************************************************************
        *   Returns a big decimal of the given int.
        *
        *   @param  priceInt    The price to format as a big-decimal as an int. e.g. 99.
        *   @return             The given int as a big decimal. e.g. "0.99".
        *********************************************************************************/
        public static final BigDecimal getBigDecimal( int priceInt )
        {
            String  priceString = String.valueOf( priceInt );
            while ( priceString.length() < 3 ) priceString = "0" + priceString;
            priceString = priceString.substring( 0, priceString.length() - 2 ) + "." + priceString.substring( priceString.length() - 2, priceString.length() );

            return new BigDecimal( priceString );
        }

        /*********************************************************************************
        *   Creates a Rect with the specified location and dimension.
        *
        *   @param  x       Rect's location x.
        *   @param  y       Rect's location y.
        *   @param  width   The rect's width.
        *   @param  height  The rect's height.
        *   @return         The Rect with the specified location and size.
        *********************************************************************************/
        public static final Rect createRect( int x, int y, int width, int height )
        {
            return new Rect( x, y, x + width, y + height );
        }

        /*********************************************************************************
        *   Uninstalls the package with the given package name.
        *
        *   @param  context     The current context.
        *   @param  packageName The package name of the package to uninstall.
        *********************************************************************************/
        public static final void uninstallApp( Context context, String packageName )
        {
            Intent intent = new Intent( Intent.ACTION_DELETE, Uri.fromParts( "package", packageName, null ) );
            context.startActivity( intent );
        }

        /*********************************************************************************
        *   Checks if the SIM-card is active and online.
        *
        *   @param  context     The current context.
        *   @return             <code>true</code> if the SIM-card is operative.
        *                       Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSimCardReady( Context context )
        {
            TelephonyManager tm = (TelephonyManager)context.getSystemService( Context.TELEPHONY_SERVICE );
            return ( tm.getSimState() == TelephonyManager.SIM_STATE_READY );
        }

        /*********************************************************************************
        *   Checks if the operating device supports a SIM-card.
        *   Some devices ( Tablets like 'Nexus 7' ) don't have a SIM-card-slot.
        *
        *   @param  context     The current context.
        *   @return             <code>true</code> if this device has a SIM-card-slot.
        *                       Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean hasSimCardReader( Context context )
        {
            TelephonyManager tm = (TelephonyManager)context.getSystemService( Context.TELEPHONY_SERVICE );
            return ( tm.getSimState() != TelephonyManager.SIM_STATE_UNKNOWN );
        }

        /*********************************************************************************
        *   Installs the given file. The file must be a suitable Android-package.
        *
        *   @param  context     The current context.
        *   @param  file        The APK-file to install.
        *********************************************************************************/
        public static final void installApk( Context context, File file )
        {
            //bring task to front if already running
            Intent intent = new Intent();
            intent.setAction(       android.content.Intent.ACTION_VIEW                                      );
            intent.setFlags(        Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT     );
            intent.setDataAndType(  Uri.fromFile( file ), "application/vnd.android.package-archive"         );
            context.startActivity(  intent                                                                  );
        }

        /*********************************************************************************
        *   Checks if all elements of the given subset are contained in the specified superset.
        *
        *   @param  subset      The subset collection.
        *   @param  superset    The superset collection.
        *   @return             <code>true</code> if all elements of the subset are contained
        *                       in the superset. Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean isSubsetOf( Collection<String> subset, Collection<String> superset )
        {
            for ( String string : subset )
            {
                if ( !superset.contains( string ) )
                {
                    return false;
                }
            }
            return true;
        }

        /*********************************************************************************
        *   Prints the Base64-encoded SHA of all signatures of the given package.
        *
        *   @param  activity    The according activity context.
        *   @param  packageName The packagename of the package to print all signatures for.
        *********************************************************************************/
        public static final void printHashKey( Activity activity, String packageName )
        {
            try
            {
                PackageInfo info = activity.getPackageManager().getPackageInfo( packageName, PackageManager.GET_SIGNATURES );
                for ( Signature signature : info.signatures )
                {
                    MessageDigest md = MessageDigest.getInstance( "SHA" );
                    md.update( signature.toByteArray() );

                    System.out.println( "TEMPTAGHASH KEY: [" + LibBase64.encodeToString( md.digest() ) + "]" );
                }
            }
            catch ( Throwable t )
            {
            }
        }
    }
