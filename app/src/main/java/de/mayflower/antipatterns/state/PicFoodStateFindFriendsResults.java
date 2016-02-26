
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

    /**********************************************************************************************
    *   The state 'Find friends results'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodStateFindFriendsResults extends Activity
    {
        /** The singleton instance of this state. */
        protected   static          PicFoodStateFindFriendsResults      singleton                   = null;
        /** Flags, if the user data to display shall be updated, next time the {@link #onStart()} method is invoked. */
        public      static          boolean                             updateNextOnStart           = false;

        /** The overlay ImageView that can display any images. Used to represent the loading-icon and the 'no network'-symbol. */
        private                     ImageView                           iOverlayIcon                = null;
        /** A reference to the content container that holds the GridView with the results. */
        public                      LinearLayout                        iContentContainer           = null;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //assign singleton
            singleton = this;

            //init state activity
            RelativeLayout rl = (RelativeLayout)PicFoodState.initStateActivity
            (
                PicFoodState.EFindFriendsResults,
                R.layout.state_content_find_friends_results,
                R.string.state_find_friends_results_header,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveFindFriendsResults
            );

            iOverlayIcon        = (ImageView)rl.findViewById(       R.id.overlay_icon                                   );
            iContentContainer   = (LinearLayout)rl.findViewById(    R.id.content_container                              );

            //connect to GridView
            PicFoodAdapterManager.getSingleton( this, GridViews.EFindFriends ).connect( iContentContainer );
        }

        @Override
        protected void onStart()
        {
            //invoke super method
            super.onStart();

            if ( updateNextOnStart )
            {
                updateNextOnStart = false;

                //ditch the GridView
                PicFoodAdapterManager.getSingleton( this, GridViews.EFindFriends ).clearData();

                //update threaded
                PicFoodFlowSearchUsers.updateSearchUsersResultsThreaded( iOverlayIcon );
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
                    PicFoodActionState.ELeaveFindFriendsResults.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }

        @Override
        protected void onStop()
        {
            //invoke super method
            super.onStop();
        }
    }
