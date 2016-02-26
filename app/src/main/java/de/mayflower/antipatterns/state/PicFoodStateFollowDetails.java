/*  $Id: PicFoodStateFollowDetails.java 50552 2013-08-12 08:56:31Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.state;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.*;
    import  android.app.*;
    import  android.os.*;
    import  android.view.*;
    import  android.widget.*;

    import de.mayflower.lib.*;

    /**********************************************************************************************
    *   The state 'Follow details'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50552 $ $Date: 2013-08-12 10:56:31 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/PicFoodStateFollowDetails.java $"
    **********************************************************************************************/
    public class PicFoodStateFollowDetails extends Activity
    {
        /** The singleton instance of this state. */
        protected   static      PicFoodStateFollowDetails           singleton                       = null;

        /** Flags, if the followers data shall be updated, next time the {@link #onStart()} method is invoked. */
        public      static      boolean                             setupFollowersNextOnStart       = false;
        /** Flags, if the followings data shall be updated, next time the {@link #onStart()} method is invoked. */
        public      static      boolean                             setupFollowingsNextOnStart      = false;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //assign singleton
            singleton = this;

            //init state activity
            ViewGroup container = PicFoodState.initStateActivity
            (
                PicFoodState.EFollowDetails,
                R.layout.state_content_follow_details,
                R.string.empty_string,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveFollowDetails
            );

            //connect to GridView
            PicFoodAdapterManager.getSingleton( this, GridViews.EFollowDetails ).connect( container );
        }

        @Override
        protected void onStart()
        {
            //invoke super method
            super.onStart();

            //check if the profile shall be set up this onStart
            if ( setupFollowersNextOnStart )
            {
                //unflag
                setupFollowersNextOnStart = false;

                //setup header with 'followers'
                TextView    headline        = (TextView)findViewById( R.id.state_headline );
                String      headlineString  = LibResource.getResourceString( this, ( PicFoodFlowProfileFollow.lastUser.iFollowerCount == 1 ? R.string.state_follow_details_header_followers_singular : R.string.state_follow_details_header_followers_plural ) );
                            headlineString  = headlineString.replace( "{user}",  String.valueOf( PicFoodFlowProfileFollow.lastUser.iUserName      ) );
                            headlineString  = headlineString.replace( "{count}", String.valueOf( PicFoodFlowProfileFollow.lastUser.iFollowerCount ) );
                headline.setText( headlineString );

                //ditch the GridView
                PicFoodAdapterManager.getSingleton( this, GridViews.EFollowDetails ).clearData();

                //update the GridView
                PicFoodActionUpdate.EUpdateFollowersDetailsClean.run();
            }
            else if ( setupFollowingsNextOnStart )
            {
                //unflag
                setupFollowingsNextOnStart = false;

                //setup header with 'followings'
                TextView headline = (TextView)findViewById( R.id.state_headline );
                String      headlineString  = LibResource.getResourceString( this, ( PicFoodFlowProfileFollow.lastUser.iFollowingsCount == 1 ? R.string.state_follow_details_header_followings_singular : R.string.state_follow_details_header_followings_plural ) );
                            headlineString  = headlineString.replace( "{user}",  String.valueOf( PicFoodFlowProfileFollow.lastUser.iUserName      ) );
                            headlineString  = headlineString.replace( "{count}", String.valueOf( PicFoodFlowProfileFollow.lastUser.iFollowingsCount ) );
                headline.setText( headlineString );

                //ditch the GridView
                PicFoodAdapterManager.getSingleton( this, GridViews.EFollowDetails ).clearData();

                //update the GridView
                PicFoodActionUpdate.EUpdateFollowingsDetailsClean.run();
            }
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //leave this state
                    PicFoodActionState.ELeaveFollowDetails.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
