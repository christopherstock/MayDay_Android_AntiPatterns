/*  $Id: PicFoodGoogleCloudMessaging.java 50541 2013-08-09 12:37:45Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.ext.gcm;

    import  java.io.*;
    import  android.app.*;
    import  android.os.*;
    import  com.google.android.gms.gcm.*;
    import  net.picfood.*;
    import  net.picfood.PicFoodProject.API.GCM;
    import  net.picfood.io.*;
    import  net.picfood.io.PicFoodSave.SaveKey;

    /*****************************************************************************
    *   Performs requests to the Google Cloud Messaging API.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50541 $ $Date: 2013-08-09 14:37:45 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/ext/gcm/PicFoodGoogleCloudMessaging.java $"
    *****************************************************************************/
    public class PicFoodGoogleCloudMessaging
    {
        /** The user's registration-ID for the GCM system. */
        public                  static                  String              registrationID                      = null;

        /*****************************************************************************
        *   Registers the current active user to the GCM system.
        *   This method does NOT directly call the register-method from the GCM API
        *   but tries to read the GCM registrationID from RAM or from the persistent storage.
        *
        *   @param  activity    The according activity context.
        *****************************************************************************/
        public static final void registerGCM( final Activity activity )
        {
            try
            {
                //only perform if not active in RAM
                if ( registrationID == null )
                {
                    //only perform if the user is logged in because the GCM regID is bound to the user-profile
                    if ( PicFoodSave.loadSetting( activity, SaveKey.ESessionID ) != null )
                    {
                        PicFoodDebug.gcm.out( "GCM Test: Checking if a registrationID is available" );

                        //check if a registration ID is available
                        registrationID = PicFoodSave.loadSetting( activity, SaveKey.EGoogleCloudMessagingRegistrationID );
                        if ( registrationID == null )
                        {
                            PicFoodDebug.gcm.out( "GCM Test: RegistrationID is NULL - registering GCM" );

                            //register
                            registerGCMThreaded( activity );
                        }
                        else
                        {
                            PicFoodDebug.gcm.out( "GCM Test: LOADED RegistrationID is available: [" + registrationID + "] No operation required." );
                        }
                    }
                    else
                    {
                        PicFoodDebug.gcm.out( "GCM Test: User is NOT LOGGED IN. Registering GCM is ABORTED." );
                    }
                }
                else
                {
                    PicFoodDebug.gcm.out( "GCM Test: ACTIVE RAM RegistrationID is available: [" + registrationID + "] No operation required." );
                }
            }
            catch ( Throwable t )
            {
                PicFoodDebug.gcm.out( "GCM Test: Initializing GoogleCloudMessaging threw an Exception!" );
                PicFoodDebug.gcm.trace( t );
            }
        }

        /*****************************************************************************
        *   Registers the current active user to the GCM system.
        *   This method directly invokes the register-method from the GCM API.
        *
        *   @param  activity    The according activity context.
        *****************************************************************************/
        private static final void registerGCMThreaded( final Activity activity )
        {
            //perform this in a separate thread!
            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        //perform registration ( blocks )
                        GoogleCloudMessaging.getInstance( activity ).register( GCM.PROJECT_ID );
                    }
                    catch ( Throwable t )
                    {
                        PicFoodDebug.gcm.out( "GCM Test: Registering for GoogleCloudMessaging threw an Exception!" );
                        PicFoodDebug.gcm.trace( t );
                    }
                }
            }.start();
        }

        /*****************************************************************************
        *   Sends a test message to the GCM servers.
        *   This message should be pushed to all registered users.
        *
        *   @param  activity    The according activity context.
        *****************************************************************************/
        @Deprecated
        public static final void testSend( Activity activity )
        {
            PicFoodDebug.gcm.out( "GCM Test: testSend()" );

            try
            {
                //data is a key-value pair.
                Bundle data = new Bundle();
                data.putString( "hello", "world" );

                String timeToLive = Integer.toString( (int)System.currentTimeMillis() );
                GoogleCloudMessaging.getInstance( activity ).send( GCM.PROJECT_ID + "@gcm.googleapis.com", timeToLive, data );

                PicFoodDebug.gcm.out( "GCM Test: Message sent successfully" );
            }
            catch ( IOException ex )
            {
                PicFoodDebug.gcm.trace( ex );
            }
        }
    }
