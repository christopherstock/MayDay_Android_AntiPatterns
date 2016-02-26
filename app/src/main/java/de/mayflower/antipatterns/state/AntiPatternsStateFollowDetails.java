
    package de.mayflower.antipatterns.state;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import de.mayflower.antipatterns.state.AntiPatternsState.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.*;
    import  android.app.*;
    import  android.os.*;
    import  android.view.*;
    import  android.widget.*;

    import de.mayflower.lib.*;

    /**********************************************************************************************
    *   The state 'Follow details'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateFollowDetails extends Activity
    {
        /** The singleton instance of this state. */
        protected   static AntiPatternsStateFollowDetails singleton                       = null;

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
            ViewGroup container = AntiPatternsState.initStateActivity
            (
                    AntiPatternsState.EFollowDetails,
                    R.layout.state_content_follow_details,
                    R.string.empty_string,
                    HideHeaderBar.ENo,
                    ShowHeaderBackButton.EYes,
                    ShowHeaderSettingsButton.ENo,
                    ShowHeaderSearchButton.ENo,
                    ShowHeaderAppLogo.ENo,
                    AntiPatternsActionState.ELeaveFollowDetails
            );

            //connect to GridView
            AntiPatternsAdapterManager.getSingleton(this, GridViews.EFollowDetails).connect( container );
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
                String      headlineString  = LibResource.getResourceString( this, ( AntiPatternsFlowProfileFollow.lastUser.iFollowerCount == 1 ? R.string.state_follow_details_header_followers_singular : R.string.state_follow_details_header_followers_plural ) );
                            headlineString  = headlineString.replace( "{user}",  String.valueOf( AntiPatternsFlowProfileFollow.lastUser.iUserName      ) );
                            headlineString  = headlineString.replace( "{count}", String.valueOf( AntiPatternsFlowProfileFollow.lastUser.iFollowerCount ) );
                headline.setText( headlineString );

                //ditch the GridView
                AntiPatternsAdapterManager.getSingleton(this, GridViews.EFollowDetails).clearData();

                //update the GridView
                AntiPatternsActionUpdate.EUpdateFollowersDetailsClean.run();
            }
            else if ( setupFollowingsNextOnStart )
            {
                //unflag
                setupFollowingsNextOnStart = false;

                //setup header with 'followings'
                TextView headline = (TextView)findViewById( R.id.state_headline );
                String      headlineString  = LibResource.getResourceString( this, ( AntiPatternsFlowProfileFollow.lastUser.iFollowingsCount == 1 ? R.string.state_follow_details_header_followings_singular : R.string.state_follow_details_header_followings_plural ) );
                            headlineString  = headlineString.replace( "{user}",  String.valueOf( AntiPatternsFlowProfileFollow.lastUser.iUserName      ) );
                            headlineString  = headlineString.replace( "{count}", String.valueOf( AntiPatternsFlowProfileFollow.lastUser.iFollowingsCount ) );
                headline.setText( headlineString );

                //ditch the GridView
                AntiPatternsAdapterManager.getSingleton(this, GridViews.EFollowDetails).clearData();

                //update the GridView
                AntiPatternsActionUpdate.EUpdateFollowingsDetailsClean.run();
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
                    AntiPatternsActionState.ELeaveFollowDetails.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
