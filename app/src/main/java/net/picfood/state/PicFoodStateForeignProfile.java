/*  $Id: PicFoodStateForeignProfile.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.flow.*;
    import  net.picfood.state.PicFoodState.*;
    import  android.app.*;
    import  android.os.*;
    import  android.view.*;
    import  android.widget.*;

    import  com.synapsy.android.lib.ui.*;
    import  com.synapsy.android.lib.ui.widget.*;

    /**********************************************************************************************
    *   The state 'foreign profile'.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/PicFoodStateForeignProfile.java $"
    **********************************************************************************************/
    public class PicFoodStateForeignProfile extends Activity
    {
        /** The singleton instance of this state. */
        public      static      PicFoodStateForeignProfile          singleton                       = null;
        /** Flags, if the foreign profile data shall be updated, next time the {@link #onStart()} method is invoked. */
        public      static      boolean                             setupProfileNextOnStart         = false;

        /** A reference to the ScrollView in the state. */
        public                  LibScrollView                       iScrollView                     = null;

        /** The container that holds the foreign profile data. */
        public                  ViewGroup                           iContainerProfileData           = null;
        /** The container that holds the foreign profile-images. */
        public                  ViewGroup                           iContainerProfileImages         = null;

        /** The TextView that represents the title for the profile data area. */
        public                  TextView                            iTitleProfile                   = null;
        /** The TextView that represents the title for the profile-images area. */
        public                  TextView                            iTitleImages                    = null;

        /** The overlay ImageView that can display any images. Used to represent the loading-icon and the 'no network'-symbol. */
        public                  ImageView                           iOverlayIcon                    = null;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //assign singleton
            singleton = this;

            //init state activity
            PicFoodState.initStateActivity
            (
                PicFoodState.EForeignProfile,
                R.layout.state_content_foreign_profile,
                R.string.empty_string,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveForeignProfile
            );

            //reference scrollview and containers
            iScrollView             = (LibScrollView)findViewById( R.id.scroll_view );
            iContainerProfileData   = (ViewGroup)findViewById( R.id.container_profile_data   );
            iContainerProfileImages = (ViewGroup)findViewById( R.id.container_profile_images );

            //reference overlay icon
            iOverlayIcon            = (ImageView)findViewById( R.id.overlay_icon );

            //setup headlines
            iTitleProfile = (TextView)findViewById( R.id.title_profile );
            LibUI.setupTextView( this, iTitleProfile, PicFoodSystems.getFonts().TYPEFACE_REGULAR, R.string.state_foreign_profile_title_profile );
            iTitleProfile.setVisibility(   View.GONE );
            iTitleImages = (TextView)findViewById( R.id.title_images );
            LibUI.setupTextView( this, iTitleImages, PicFoodSystems.getFonts().TYPEFACE_REGULAR, R.string.state_foreign_profile_title_images );
            iTitleImages.setVisibility( View.GONE );
        }

        @Override
        protected void onStart()
        {
            //invoke super method
            super.onStart();

            //check if the profile shall be set up this onStart
            if ( setupProfileNextOnStart )
            {
                //unflag
                setupProfileNextOnStart = false;

                //setup header with the name of the foreign user
                TextView headline = (TextView)findViewById( R.id.state_headline );
                headline.setText( PicFoodFlowProfile.getLastForeignUser().iUserName );

                //update the foreign user's profile
                PicFoodActionUpdate.EUpdateForeignProfile.run();
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
                    PicFoodActionState.ELeaveForeignProfile.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }
    }
