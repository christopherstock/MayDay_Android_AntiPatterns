
    package de.mayflower.antipatterns.state.pivotal;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Features;
    import  de.mayflower.antipatterns.AntiPatternsSettings.ContentUpdatePeriods;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  android.os.*;
    import  android.support.v4.app.Fragment;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   The third fragment.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStatePivotalTabProfile extends Fragment
    {
        /** The singleton instance of this fragment. */
        public      static AntiPatternsStatePivotalTabProfile singleton                       = null;

        /** The cached View of the complete View Layout for this state. */
        private     static      ViewGroup                           lastLayout                      = null;
        /** Specifies, when the Layout View of this state has last been updated. */
        private     static      long                                lastUpdate                      = 0;

        /** A reference to the ScrollView in the state. */
        public                  LibScrollView                       iScrollView                     = null;

        /** The container that holds the own profile data. */
        public                  ViewGroup                           iContainerProfileData           = null;
        /** The container that holds the own profile-images. */
        public                  ViewGroup                           iContainerProfileImages         = null;

        /** The TextView that represents the title for the profile data area. */
        public                  TextView                            iTitleProfile                   = null;
        /** The TextView that represents the title for the profile-images area. */
        public                  TextView                            iTitleImages                    = null;

        /** The overlay ImageView that can display any images. Used to represent the loading-icon and the 'no network'-symbol. */
        public                  ImageView                           iOverlayIcon                    = null;

        @Override
        public View onCreateView( LayoutInflater li, ViewGroup vg, Bundle b )
        {
            //invoke super method
            super.onCreateView( li, vg, b );

            //check if next update period is reached
            if ( lastLayout == null || lastUpdate < System.currentTimeMillis() - ContentUpdatePeriods.PERIOD_UPDATE_OWN_PROFILE )
            {
                //remember this update timestamp
                lastUpdate = System.currentTimeMillis();

                //assign singleton instance
                singleton = this;

                //inflate layout
                FrameLayout fl = (FrameLayout)li.inflate( R.layout.state_content_pivotal_tab_profile, null );

                //reference scrollview and containers
                iScrollView             = (LibScrollView)fl.findViewById( R.id.scroll_view );
                iContainerProfileData   = (ViewGroup)fl.findViewById( R.id.container_profile_data   );
                iContainerProfileImages = (ViewGroup)fl.findViewById( R.id.container_profile_images );

                //reference overlay icon
                iOverlayIcon            = (ImageView)fl.findViewById( R.id.overlay_icon );

                //setup headlines
                iTitleProfile = (TextView)fl.findViewById( R.id.title_profile );
                LibUI.setupTextView( getActivity(), iTitleProfile, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, R.string.state_profile_title_profile );
                iTitleProfile.setVisibility(   View.GONE );
                iTitleImages = (TextView)fl.findViewById( R.id.title_images );
                LibUI.setupTextView( getActivity(), iTitleImages, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, R.string.state_profile_title_images );
                iTitleImages.setVisibility( View.GONE );

                //order to update the own user profile
                if ( Features.LOAD_USER_PROFILE )
                {
                    //clear and update all profile data
                    AntiPatternsActionUpdate.EUpdateOwnUserProfileFull.run();
                }

                //remember and return inflated layout
                lastLayout = fl;
                return fl;
            }

            //unselect links in profile view
            if ( AntiPatternsFlowProfile.getOwnUser() != null )
            {
                AntiPatternsFlowProfile.getOwnUser().unselectAllButtons();
            }

            //unselect all image buttons and links
            //PicFoodFlowProfileImages.unselectAllImages();

            //free last layout and return it
            LibUI.freeFromParent( lastLayout );
            return lastLayout;
        }

        /**********************************************************************************************
        *   Resets the last update timestamp for this tab,
        *   forcing an immediate update when this tab is created next time.
        **********************************************************************************************/
        public static final void resetLastUpdate()
        {
            lastUpdate = 0;
        }
    }
