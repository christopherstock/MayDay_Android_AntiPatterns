/*  $Id: PicFoodState.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.state;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.state.acclaim.*;
    import  de.mayflower.antipatterns.state.auth.*;
    import  de.mayflower.antipatterns.state.pivotal.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;

    import  android.app.Activity;
    import  android.view.*;
    import  android.widget.*;

    /**********************************************************************************************
    *   This class sets up every Actvity, which represent one different state of the application.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/PicFoodState.java $"
    **********************************************************************************************/
    public enum PicFoodState implements LibState
    {
        /** This is the state that is referenced in the manifest as the application's startup activity. */
        EStartup,
        /** The state 'title splash'. This state is not implemented. */
        ETitleSplash,
        /** The state 'acclaim', displaying the acclaim ViewPager. */
        EAcclaim,
        /** The state 'pivotal menu', being represented by a Tabbed ViewPager. */
        EPivotalMenu,
        /** The state 'detailed image', displaying detailed information for one image of the 'explore' state. */
        EDetailedImage,
        /** The state 'foreign profile', displaying other user's profiles. */
        EForeignProfile,
        /** The state 'image results' that displays all results for an image-search. */
        ESearchImagesResults,
        /** The state 'settings' that displays all settings-items. */
        ESettings,
        /** The state 'google places' that displays all results of a Google places search. */
        EGooglePlaces,
        /** The state 'find friends results' that displays all results of a friend search. */
        EFindFriendsResults,
        /** The state 'follow details' that displays all detailed followers or followings for a profile. */
        EFollowDetails,
        /** The state 'new entry' that lets the user confect a new entry. */
        ENewEntry,
        /** The state 'register' that lets the user create a new profile. */
        ERegister,
        /** The state 'image properties' that displays all comments, likes or ratings for a detailed image. */
        EImageProperties,
        /** The state 'lost password' that gives the user the option to reset his password. */
        ELostPassword,
        /** The state 'login' that lets the user log in to the pivotal menu. */
        ELogin,

        ;

        @Override
        public final Activity getActivity()
        {
            switch ( this )
            {
                case EStartup:              return PicFood.singleton;
                case ETitleSplash:          return PicFoodStateTitleSplash.singleton;
                case EAcclaim:              return PicFoodStateAcclaim.singleton;
                case EPivotalMenu:          return PicFoodStatePivotal.singleton;
                case EDetailedImage:        return PicFoodStateDetailedImage.singleton;
                case EGooglePlaces:         return PicFoodStateGooglePlaces.singleton;
                case ESettings:             return PicFoodStateSettings.singleton;
                case EForeignProfile:       return PicFoodStateForeignProfile.singleton;
                case ESearchImagesResults:  return PicFoodStateSearchImagesResults.singleton;
                case EFindFriendsResults:   return PicFoodStateFindFriendsResults.singleton;
                case EFollowDetails:        return PicFoodStateFollowDetails.singleton;
                case ENewEntry:             return PicFoodStateNewEntry.singleton;
                case ERegister:             return PicFoodStateRegister.singleton;
                case EImageProperties:      return PicFoodStateImageProperties.singleton;
                case ELostPassword:         return PicFoodStateLostPassword.singleton;
                case ELogin:                return PicFoodStateLogin.singleton;
            }
            return null;
        }

        /** Determines, if the state's header bar shall be hidden. */
        public static enum HideHeaderBar{           /** Yes. */ EYes, /** No. */ ENo, ; }
        /** Determines, if the 'back'-button shall be available in the state's header bar. */
        public static enum ShowHeaderBackButton{    /** Yes. */ EYes, /** No. */ ENo, ; }
        /** Determines, if the 'settings'-button shall be available in the state's header bar. */
        public static enum ShowHeaderSettingsButton{/** Yes. */ EYes, /** No. */ ENo, ; }
        /** Determines, if the 'search'-button shall be available in the state's header bar. */
        public static enum ShowHeaderSearchButton{  /** Yes. */ EYes, /** No. */ ENo, ; }
        /** Determines, if the logo shall be displayed in the state's header bar. */
        public static enum ShowHeaderAppLogo{       /** Yes. */ EYes, /** No. */ ENo, ; }

        /**********************************************************************************************
        *   Sets up the activity for a state.
        *
        *   @param  state            The activity to set up.
        *   @param  contentID           The layout-ID of the content view to inflate
        *                               and connect to the state content container.
        *   @param  headlineID          The string-resource-id of the string to display in the header headline.
        *   @param  hideHeaderBar       Specifies if the header shall completely be hidden.
        *   @param  showBackButton      Specifies if the header shall display the 'back' button.
        *   @param  showSettingsButton  Specifies if the header shall display the 'settings' button.
        *   @param  showSearchButton    Specifies if the header shall display the 'search' button.
        *   @param  showLogo            Specifies if the header shall display the PicFood logo.
        *   @param  backAction          The action to perform on pressing the back-button.
        *   @return                     A reference to the content view, so specific ids of the layout
        *                               can be adjusted furthermore.
        **********************************************************************************************/
        public static final ViewGroup initStateActivity
        (
            LibState                    state,
            int                         contentID,
            int                         headlineID,
            HideHeaderBar               hideHeaderBar,
            ShowHeaderBackButton        showBackButton,
            ShowHeaderSettingsButton    showSettingsButton,
            ShowHeaderSearchButton      showSearchButton,
            ShowHeaderAppLogo           showLogo,
            Runnable                    backAction
        )
        {
            //init all systems
            PicFoodSystems.init( state );

            //reference state layout
            state.getActivity().setContentView( R.layout.state );

            //reference container
            ViewGroup contentContainer = (ViewGroup)state.getActivity().findViewById( R.id.state_content_container );
            ViewGroup contentView      = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), contentID );

            //add login content to container
            contentContainer.addView( contentView );

            //setup header
            setupHeader
            (
                state.getActivity(),
                hideHeaderBar,
                showBackButton,
                showSettingsButton,
                showSearchButton,
                showLogo,
                backAction
            );

            //set headline if desired
            if ( headlineID != 0 )
            {
                TextView headline = (TextView)state.getActivity().findViewById( R.id.state_headline );
                LibUI.setupTextView( state.getActivity(), headline, PicFoodSystems.getFonts().TYPEFACE_REGULAR, headlineID );
            }

            return contentView;
        }

        /**********************************************************************************************
        *   Sets up the header for this state.
        *
        *   @param  activity            The activity to set up.
        *   @param  hideHeaderBar       Specifies if the header shall completely be hidden.
        *   @param  showBackButton      Specifies if the header shall display the 'back' button.
        *   @param  showSettingsButton  Specifies if the header shall display the 'settings' button.
        *   @param  showSearchButton    Specifies if the header shall display the 'search' button.
        *   @param  showLogo            Specifies if the header shall display the PicFood logo.
        *   @param  backAction          The action to perform on pressing the back-button.
        **********************************************************************************************/
        public static final void setupHeader
        (
            Activity                    activity,
            HideHeaderBar               hideHeaderBar,
            ShowHeaderBackButton        showBackButton,
            ShowHeaderSettingsButton    showSettingsButton,
            ShowHeaderSearchButton      showSearchButton,
            ShowHeaderAppLogo           showLogo,
            Runnable                    backAction
        )
        {
            //setup back icon
            {
                View backIcon = activity.findViewById( R.id.back_icon );
                switch ( showBackButton )
                {
                    case EYes:
                    {
                        backIcon.setVisibility( View.VISIBLE );
                        LibUI.setOnClickAction( backIcon, backAction );
                        break;
                    }

                    case ENo:
                    {
                        backIcon.setVisibility( View.GONE );
                        break;
                    }
                }
            }

            //setup settings icon
            {
                View settingsIcon = activity.findViewById( R.id.settings_icon );
                switch ( showSettingsButton )
                {
                    case EYes:
                    {
                        settingsIcon.setVisibility( View.VISIBLE );
                        LibUI.setOnClickAction( settingsIcon, PicFoodActionState.EEnterSettings );
                        break;
                    }

                    case ENo:
                    {
                        settingsIcon.setVisibility( View.GONE );
                        break;
                    }
                }
            }

            //setup search icon
            {
                View searchIcon = activity.findViewById( R.id.search_icon );
                switch ( showSearchButton )
                {
                    case EYes:
                    {
                        searchIcon.setVisibility( View.VISIBLE );
                        LibUI.setOnClickAction( searchIcon, PicFoodActionDialog.EDialogSearchImages );
                        break;
                    }

                    case ENo:
                    {
                        searchIcon.setVisibility( View.GONE );
                        break;
                    }
                }
            }

            //hide logo if desired
            {
                if ( showLogo == ShowHeaderAppLogo.ENo )
                {
                    View logo = activity.findViewById( R.id.app_logo );
                    logo.setVisibility( View.GONE );
                }
                else
                {
                    boolean centerLogo = true;
                    if ( centerLogo )
                    {
                        //only if the settings icon is NOT present
                        if ( showSettingsButton == ShowHeaderSettingsButton.ENo )
                        {
                            //hide the fill spacer
                            View fillSpacer = activity.findViewById( R.id.fill_spacer );
                            fillSpacer.setVisibility( View.GONE );
                        }
                    }
                }
            }

            //hide the whole bar if desired
            {
                if ( hideHeaderBar == HideHeaderBar.EYes )
                {
                    View header = activity.findViewById( R.id.state_header );
                    header.setVisibility( View.GONE );
                }
            }
        }
    }
