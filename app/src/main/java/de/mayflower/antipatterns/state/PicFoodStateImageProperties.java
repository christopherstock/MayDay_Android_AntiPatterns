
    package de.mayflower.antipatterns.state;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.*;
    import  android.app.*;
    import  android.os.*;
    import  android.view.*;
    import  android.widget.*;

    /**********************************************************************************************
    *   The state 'Image properties'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodStateImageProperties extends Activity
    {
        /** The singleton instance of this state. */
        protected   static      PicFoodStateImageProperties         singleton                       = null;

        /** Flags, if this state shall perform a clean update for 'image likes', next time the {@link #onStart()} method is invoked. */
        public      static      boolean                             setupLikesNextOnStart           = false;
        /** Flags, if this state shall perform a clean update for 'image comments', next time the {@link #onStart()} method is invoked. */
        public      static      boolean                             setupCommentsNextOnStart        = false;
        /** Flags, if this state shall perform a clean update for 'image ratings', next time the {@link #onStart()} method is invoked. */
        public      static      boolean                             setupFoodRatingsNextOnStart     = false;

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
                PicFoodState.EImageProperties,
                R.layout.state_content_image_properties,
                R.string.empty_string,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveImageProperties
            );

            //connect to GridView
            PicFoodAdapterManager.getSingleton( this, GridViews.EImageProperties ).connect( container );

            //iContainerProfileData   = (ViewGroup)findViewById( R.id.container_profile_data   );
            //iContainerProfileImages = (ViewGroup)findViewById( R.id.container_profile_images );
        }

        @Override
        protected void onStart()
        {
            //invoke super method
            super.onStart();

            //check if the profile shall be set up this onStart
            if ( setupLikesNextOnStart )
            {
                //unflag
                setupLikesNextOnStart = false;

                //setup header with 'image likes'
                TextView headline = (TextView)findViewById( R.id.state_headline );
                headline.setText( R.string.state_image_properties_likes_header );

                //ditch the GridView
                PicFoodAdapterManager.getSingleton( this, GridViews.EImageProperties ).clearData();

                //update the image's likes
                PicFoodActionUpdate.EUpdateImagePropertiesLikesClean.run();
            }
            else if ( setupCommentsNextOnStart )
            {
                //unflag
                setupCommentsNextOnStart = false;

                //setup header with 'image likes'
                TextView headline = (TextView)findViewById( R.id.state_headline );
                headline.setText( R.string.state_image_properties_comments_header );

                //ditch the GridView
                PicFoodAdapterManager.getSingleton( this, GridViews.EImageProperties ).clearData();

                //update the image's comments
                PicFoodActionUpdate.EUpdateImagePropertiesCommentsClean.run();
            }
            else if ( setupFoodRatingsNextOnStart )
            {
                //unflag
                setupFoodRatingsNextOnStart = false;

                //setup header with 'image likes'
                TextView headline = (TextView)findViewById( R.id.state_headline );
                headline.setText( R.string.state_image_properties_food_ratings_header );

                //ditch the GridView
                PicFoodAdapterManager.getSingleton( this, GridViews.EImageProperties ).clearData();

                //update the image's ratings
                PicFoodActionUpdate.EUpdateImagePropertiesFoodRatingsClean.run();
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
                    PicFoodActionState.ELeaveImageProperties.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
