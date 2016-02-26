/*  $Id: PicFoodFacebook.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.ext.facebook;

    import  java.security.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  android.annotation.SuppressLint;
    import  android.app.*;
    import  android.content.*;
    import  android.content.pm.*;
    import  android.util.*;
    import  com.facebook.*;
    import de.mayflower.lib.*;

    /*****************************************************************************
    *   Performs requests to the Facebook API.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/ext/facebook/PicFoodFacebook.java $"
    *****************************************************************************/
    public class PicFoodFacebook
    {
        /*****************************************************************************
        *   All facebook-tasks that can be launched after the Facebook session
        *   has been successfully established.
        *****************************************************************************/
        protected static enum FacebookTaskAfterSessionOpen
        {
            /*****************************************************************************
            *   Means that nothing shall be performed
            *   after the Facebook session has been established.
            *****************************************************************************/
            ENone,

            /*****************************************************************************
            *   The general data shall be read from the Facebook API and be assigned
            *   to the fields in state 'register'.
            *****************************************************************************/
            EFillRegisterData,

            /*****************************************************************************
            *   All friends of the Facebook user shall be read and transferred to the search request.
            *****************************************************************************/
            ETransferFriendsIDs,

            ;
        }

        /*****************************************************************************
        *   Fills some register-fields with data that has been received by the Facebook-API.
        *
        *   @param  state       The according state.
        *****************************************************************************/
        public static final void fillRegisterData( LibState state )
        {
            //propagade action to facebook session
            PicFoodFacebookSession.performAction( state, FacebookTaskAfterSessionOpen.EFillRegisterData );
        }

        /*****************************************************************************
        *   Picks all friend-ids from facebook
        *   and performs a 'find friend'-request afterwards.
        *
        *   @param  state       The according state.
        *****************************************************************************/
        public static final void findFriendsViaFacebook( LibState state )
        {
            //propagade action to facebook session
            PicFoodFacebookSession.performAction( state, FacebookTaskAfterSessionOpen.ETransferFriendsIDs );
        }

        /*****************************************************************************
        *   Being invoked from the activity's onActivityResult function,
        *   calling this function from any Activity that receives this callback from the Facebook SDK
        *   is mandatory!
        *
        *   @param  currentActivity The Activity that is forwarding the onActivityResult call.
        *   @param  requestCode     The requestCode parameter from the forwarded call. When this
        *                           onActivityResult occurs as part of Facebook authorization
        *                           flow, this value is the activityCode passed to open or authorize.
        *   @param  responseCode    An int containing the resultCode parameter from the forwarded call.
        *   @param  data            The Intent passed as the data parameter from the forwarded call.
        *****************************************************************************/
        public static final void onActivityResult( Activity currentActivity, int requestCode, int responseCode, Intent data )
        {
            //propagade to facebook SDK - this is mandatory!
            if ( Session.getActiveSession() != null )
            {
                Session.getActiveSession().onActivityResult( currentActivity, requestCode, responseCode, data );
            }
        }

        /*****************************************************************************
        *   Unselects all buttons that invoke a Facebook action.
        *****************************************************************************/
        public static final void unselectAllFacebookButtons()
        {
            //disable all states with facebook buttons
            {
                PicFoodActionUnselect.EUnselectButtonsRegister.run();
                PicFoodActionUnselect.EUnselectButtonsSettings.run();
            }
        }

        /*****************************************************************************
        *   This method shows the string that you must add
        *   to the serversided 'Key Hashes' of your serversided Facebook app
        *   in order to connect to the Facebook API properly.
        *
        *   @param      a   The current Activity context.
        *   @deprecated     This is for testing purposes only.
        *****************************************************************************/
        @SuppressLint( "NewApi" )
        @Deprecated
        public static final void printFacebookHash( Activity a )
        {
            try
            {
                PackageInfo info = a.getPackageManager().getPackageInfo( "de.mayflower.picfood", PackageManager.GET_SIGNATURES );
                for ( android.content.pm.Signature signature : info.signatures )
                {
                    MessageDigest md = MessageDigest.getInstance( "SHA" );
                    md.update( signature.toByteArray() );
                    PicFoodDebug.facebook.out( " KeyHash: [" + Base64.encodeToString( md.digest(), Base64.DEFAULT ) + "]" );
                }
            }
            catch ( Throwable e )
            {
                PicFoodDebug.facebook.trace( e );
            }
        }
    }
