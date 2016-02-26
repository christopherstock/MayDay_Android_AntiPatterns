/*  $Id: PicFoodFacebookSession.java 50537 2013-08-08 15:35:28Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.ext.facebook;

    import  java.util.*;
    import  net.picfood.*;
    import  net.picfood.PicFoodProject.API.*;
    import  net.picfood.action.*;
    import  net.picfood.ext.facebook.PicFoodFacebook.*;
    import  com.facebook.*;
    import  com.facebook.Session.OpenRequest;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  com.facebook.Session.*;

    /*****************************************************************************
    *   Represents the session manager for Facebook sessions.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50537 $ $Date: 2013-08-08 17:35:28 +0200 (Do, 08 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/ext/facebook/PicFoodFacebookSession.java $"
    *****************************************************************************/
    public class PicFoodFacebookSession implements Session.StatusCallback
    {
        /** The singleton instance of this class. */
        private             static          PicFoodFacebookSession          singleton                   = null;

        /** The activity that invokes the Facebook API. */
        public              static          LibState                        callingState                = null;

        /** The Facebook task to perform after the Facebook session has been established successfully. */
        private             static          FacebookTaskAfterSessionOpen    taskAfterSessionOpen        = null;

        /**********************************************************************************************
        *   Delivers the singleton instance of the Facebook session manager.
        *
        *   @return     The singleton instance of this class.
        **********************************************************************************************/
        public static final PicFoodFacebookSession getSingleton()
        {
            if ( singleton == null ) singleton = new PicFoodFacebookSession();
            return singleton;
        }

        /**********************************************************************************************
        *   Launches an action that requires data from the Facebook API.
        *
        *   @param  state                   The according state.
        *   @param  aTaskAfterSessionOpen   The task that shall be performed after the Facebook session
        *                                   has been successfully established.
        **********************************************************************************************/
        public static final void performAction( LibState state, FacebookTaskAfterSessionOpen aTaskAfterSessionOpen )
        {
            //assign taks after session open
            callingState            = state;
            taskAfterSessionOpen    = aTaskAfterSessionOpen;

            //check if the current session is already open
            if ( Session.getActiveSession() != null && Session.getActiveSession().isOpened() )
            {
                PicFoodDebug.facebook.out( "session is already open!" );

                //unselect all facebook buttons
                PicFoodFacebook.unselectAllFacebookButtons();

                //launch 'please wait' dialog and perform the desired job
                PicFoodActionDialog.EDialogShowFacebookPleaseWait.run();
            }
            else
            {
                PicFoodDebug.facebook.out( "session is closed - active session will be opened" );

                //open a session
                openFacebookSession();
            }
        }

        /**********************************************************************************************
        *   Tries to open a Session to the Facebook system.
        **********************************************************************************************/
        private static void openFacebookSession()
        {
            OpenRequest openRequest = new OpenRequest( callingState.getActivity() ).setPermissions( Arrays.asList( Facebook.FACEBOOK_APP_PERMISSIONS_READ ) ).setCallback( PicFoodFacebookSession.getSingleton() );
            Session session = new Builder( callingState.getActivity() ).build();
            Session.setActiveSession( session );
            session.openForRead( openRequest );
        }

        /*****************************************************************************
        *   This callback is invoked when the session state changes.
        *****************************************************************************/
        @Override
        public void call( Session session, SessionState state, Exception exception )
        {
            PicFoodDebug.facebook.out( "callback call! " + session + " " + state );

            //unselect all facebook buttons
            PicFoodFacebook.unselectAllFacebookButtons();

            //check if the session is open now
            if ( session.isOpened() )
            {
                PicFoodDebug.facebook.out( "session is open now" );

                //perform job after session is open
                PicFoodActionDialog.EDialogShowFacebookPleaseWait.run();
            }
            else if ( session.isClosed() )
            {
                PicFoodDebug.facebook.out( "session is closed now!" );
            }
            else
            {
                PicFoodDebug.facebook.out( "session is neither opened nor closed ( opening )!" );
            }
        }

        /**********************************************************************************************
        *   Being invoked after the Facebook session has been successfully established
        *   and the request to the Facebook API is ready to be launched.
        **********************************************************************************************/
        public static final void performJobAfterSessionIsOpen()
        {
            PicFoodDebug.facebook.out( "performJobAfterSessionIsOpen() being invoked" );

            //copy and disable current job-token
            final FacebookTaskAfterSessionOpen taskToPerform = taskAfterSessionOpen;
            taskAfterSessionOpen = FacebookTaskAfterSessionOpen.ENone;

            //facebook-requests must be performed on the UI-Thread!
            callingState.getActivity().runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            //switch task
                            switch ( taskToPerform )
                            {
                                case EFillRegisterData:
                                {
                                    //request user info
                                    PicFoodFacebookJob.requestUserInfo( callingState );
                                    break;
                                }

                                case ETransferFriendsIDs:
                                {
                                    //request friends-ids
                                    PicFoodFacebookJob.requestOwnID( callingState );
                                    break;
                                }

                                case ENone:
                                {
                                    //do nothing
                                    break;
                                }
                            }
                        }
                        catch ( Throwable t )
                        {
                            //dismiss progress dialog
                            LibDialogProgress.dismissProgressDialogUIThreaded( callingState.getActivity() );

                            PicFoodDebug.DEBUG_THROWABLE( t, "Being raised on performing facebook-request [" + taskToPerform + "]!", UncaughtException.ENo );
                        }
                    }
                }
            );
        }
    }
