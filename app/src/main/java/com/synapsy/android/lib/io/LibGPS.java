/*  $Id: LibGPS.java 50667 2013-08-22 07:22:59Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.io;

    import  java.util.*;
    import  android.app.*;
    import  android.content.*;
    import  android.location.*;
    import  android.os.*;
    import  com.synapsy.android.lib.*;

    /**********************************************************************************************
    *   Delivers the user's current GPS coordinates.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50667 $ $Date: 2013-08-22 09:22:59 +0200 (Do, 22 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/io/LibGPS.java $"
    **********************************************************************************************/
    public class LibGPS implements LocationListener
    {
        /** The singleton instance of this class. */
        public      static          LibGPS                  singleton                       = null;

        /** The last location that has successfully been received via GPS. */
        public      static          Location                currentLocation                 = null;

        /** The activity that has been active when GPS coordinates have been requestes. */
        private                     Activity                iActivity                       = null;

        /** The LocationManager is required to receive GPS location data. */
        protected                   LocationManager         iLocationManager                = null;

        /** The action to perform after a GPS location has successfully been received. */
        private                     Runnable                iActionOnSuccess                = null;

        /** The action to perform after the GPS-scan-timeout has been reached. */
        protected                   Runnable                iActionOnFailed                 = null;

        /** The action to perform if the GPS-service on the user's device is disabled. */
        private                     Runnable                iActionOnDisabled               = null;

        /** The maximum time to scan for a GPS location. */
        private                     long                    iTimeout                        = 0;

        /** The timer to control the timeout with. */
        private                     Timer                   iTimeoutTimer                   = null;

        /**********************************************************************************************
        *   The time to cache the last received GPS location if another call to
        *   {@link #startNewThread(Activity, Runnable, Runnable, Runnable, long, long, LibDebug, boolean)}
        *   is invoked.
        **********************************************************************************************/
        private                     long                    iCacheTime                      = 0;

        /** The debug context. */
        private                     LibDebug                iDebug                          = null;

        /**********************************************************************************************
        *   Specifies, if GPS shall be simulated. A constant location will be assigned this way and
        *   {@link #iActionOnSuccess} will always be performed.
        **********************************************************************************************/
        private                     boolean                 iSimulateGPS                    = false;

        /**********************************************************************************************
        *   Returns the user's last known Location.
        *
        *   @param  aActivity               The Android context to pick the system service from.
        *   @param  aActionOnSuccess        The action to perform when a GPS location could be determined.
        *   @param  aActionOnFailed         The action to perform when no GPS location could be determined
        *                                   after the timeout has been reached.
        *   @param  aActionOnGpsDisabled    The action to perform when the GPS service is disabled.
        *   @param  aCacheTime              The time to cache the last received GPS location if another GPS-scan is ordered.
        *   @param  aTimeout                The maximum time to scan for a GPS location.
        *   @param  aDebug                  The debug context.
        *   @param  aSimulateGPS            Specifies, if GPS shall be simulated. A constant location will be assigned this way.
        **********************************************************************************************/
        private LibGPS
        (
            Activity    aActivity,
            Runnable    aActionOnSuccess,
            Runnable    aActionOnFailed,
            Runnable    aActionOnGpsDisabled,
            long        aCacheTime,
            long        aTimeout,
            LibDebug    aDebug,
            boolean     aSimulateGPS
        )
        {
            iActivity               = aActivity;
            iLocationManager        = (LocationManager)iActivity.getSystemService( Context.LOCATION_SERVICE );
            iActionOnSuccess        = aActionOnSuccess;
            iActionOnFailed         = aActionOnFailed;
            iActionOnDisabled       = aActionOnGpsDisabled;
            iCacheTime              = aCacheTime;
            iTimeout                = aTimeout;
            iDebug                  = aDebug;
            iSimulateGPS            = aSimulateGPS;
        }

        /**********************************************************************************************
        *   Starts scanning for a GPS location.
        **********************************************************************************************/
        public void start()
        {
            if ( iSimulateGPS )
            {
                //set dummy source location
                currentLocation = new Location( "none"      );          // Neydeckgasse 5, 97082 Würzburg
                currentLocation.setLatitude(    49.794      );          // 49.79398046633147
                currentLocation.setLongitude(   9.923       );          // 9.922252777050744

                //set dummy source location
                currentLocation = new Location( "none"      );          // Hracholusky, Czech Republic
                currentLocation.setLatitude(    49.058800   );          // 49.79398046633147
                currentLocation.setLongitude(   14.088537   );          // 9.922252777050744

                //launch action on success
                iActionOnSuccess.run();
                return;
            }

            //check if last location shall be cached
            if
            (
                    currentLocation != null
                &&  System.currentTimeMillis() - currentLocation.getTime() < iCacheTime
            )
            {
                //use current location and launch action on success
                iActionOnSuccess.run();
            }
            else
            {
                //iDebugBug.log( "LocationManager::isProviderEnabled: [" + isGpsProviderEnabled + "]", ConsoleColor.EBlueBright );

                //check if gps is enabled
                boolean isGpsProviderEnabled = iLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
                if ( isGpsProviderEnabled )
                {
                    //start timeout timer if gps is low
                    startTimeoutTimer();

                    //start GPS updates
                    startGPSUpdates();
                }
                else
                {
                    //perform gps-disabled-action
                    iActionOnDisabled.run();
                }
            }
        }

        /**********************************************************************************************
        *   Starts requesting location updates from the location manager.
        *   This will use this instance as the {@link LocationListener} for the {@link LocationManager}.
        **********************************************************************************************/
        private void startGPSUpdates()
        {
            //order location updates on ui-thread
            iActivity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //request location updates
                        iLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 100, 1, LibGPS.this );
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Stops requesting location updates from the location manager.
        *   The callbacks to {@link LocationListener} will stop immediately
        *   when this method is invoked.
        **********************************************************************************************/
        protected void stopGPSUpdates()
        {
            //must be invoked on the UI-Thread
            iActivity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //request location updates
                        iLocationManager.removeUpdates( LibGPS.this );
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Starts the timeout timer that will stop GPS updates and launch the {@link #iActionOnFailed} if it expires.
        **********************************************************************************************/
        protected void startTimeoutTimer()
        {
            iTimeoutTimer = new Timer( true );
            iTimeoutTimer.schedule
            (
                new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        //stop gps location updates
                        stopGPSUpdates();

                        //execute failed action
                        iActionOnFailed.run();
                    }
                },
                iTimeout
            );
        }

        /**********************************************************************************************
        *   Cancels the timeout timer.
        **********************************************************************************************/
        protected void stopTimeoutTimer()
        {
            if ( iTimeoutTimer != null )
            {
                iTimeoutTimer.cancel();
            }
        }

        @Override
        public void onLocationChanged( Location l )
        {
            iDebug.out( "PicFoodGPS::onLocationChanged() [" + l + "]" );

            //assign current fine location
            currentLocation = l;

            iDebug.out( "Stopping the requests and returning to task .." );

            //stop timeout timer
            stopTimeoutTimer();

            //stop GPS callbacks
            stopGPSUpdates();

            //launch action on success
            iActionOnSuccess.run();
        }

        @Override
        public void onProviderDisabled( String s )
        {
            iDebug.out( "PicFoodGPS::onProviderDisabled()" );
        }

        @Override
        public void onProviderEnabled( String s )
        {
            iDebug.out( "PicFoodGPS::onProviderEnabled()" );
        }

        @Override
        public void onStatusChanged( String s, int i, Bundle b )
        {
            iDebug.out( "PicFoodGPS::onStatusChanged()" );
        }

        /**********************************************************************************************
        *   This will start a new instance of this GPS-scanner.
        *   One of the specified actions will be launched automatically.
        *
        *   @param  aActivity               The according activity context.
        *   @param  aActionOnSuccess        The action to perform when a GPS location could be determined.
        *   @param  aActionOnFailed         The action to perform when no GPS location could be determined
        *                                   after the timeout has been reached.
        *   @param  aActionOnGpsDisabled    The action to perform when the GPS service is disabled.
        *   @param  aCacheTime              The time to cache the last received GPS location if another GPS-scan is ordered.
        *   @param  aTimeout                The maximum time to scan for a GPS location.
        *   @param  aDebug                  The debug context.
        *   @param  aSimulateGPS            Specifies, if GPS shall be simulated. A constant location will be assigned this way.
        **********************************************************************************************/
        public static final void startNewThread
        (
            Activity    aActivity,
            Runnable    aActionOnSuccess,
            Runnable    aActionOnFailed,
            Runnable    aActionOnGpsDisabled,
            long        aCacheTime,
            long        aTimeout,
            LibDebug    aDebug,
            boolean     aSimulateGPS
        )
        {
            singleton = new LibGPS
            (
                aActivity,
                aActionOnSuccess,
                aActionOnFailed,
                aActionOnGpsDisabled,
                aCacheTime,
                aTimeout,
                aDebug,
                aSimulateGPS
            );
            singleton.start();
        }

        /**********************************************************************************************
        *   Stop any currently running GPS activities.
        *   This could be invoked, if the user cancels the 'please wait - scanning GPS' dialog etc.
        **********************************************************************************************/
        public static final void cancelCurrentThread()
        {
            //stop timeout timer
            singleton.stopTimeoutTimer();

            //stop GPS callbacks
            singleton.stopGPSUpdates();
        }
    }
