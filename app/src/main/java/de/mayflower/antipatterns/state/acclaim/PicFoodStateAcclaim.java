
    package de.mayflower.antipatterns.state.acclaim;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.adapter.*;
    import  com.viewpagerindicator.*;
    import  android.os.Bundle;
    import  android.app.*;
    import  android.support.v4.view.*;
    import  android.support.v4.view.ViewPager.*;
    import  android.view.*;
    import  android.widget.*;

    /**********************************************************************************************
    *   The state 'acclaim', displaying the 'acclaim'-ViewPager.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodStateAcclaim extends Activity implements OnPageChangeListener
    {
        /** The singleton instance of this state. */
        public      static              PicFoodStateAcclaim             singleton                   = null;

        /** The index of the last page that has been active to the user. */
        private     static              int                             lastPageIndex               = -1;

        /** A reference to the ViewPager. */
        protected                       ViewPager                       iViewPagerAcclaim           = null;
        /** The bg view container that shall display the map. */
        private                         RelativeLayout                  iIconView                   = null;

        /** The 1st page of the ViewPager. */
        private                         PicFoodStateAcclaimPage1        iPage1                      = null;
        /** The 2nd page of the ViewPager. */
        private                         PicFoodStateAcclaimPage2        iPage2                      = null;
        /** The 3rd page of the ViewPager. */
        private                         PicFoodStateAcclaimPage3        iPage3                      = null;
        /** The 4th page of the ViewPager. */
        private                         PicFoodStateAcclaimPage4        iPage4                      = null;
        /** The 5th page of the ViewPager. */
        private                         PicFoodStateAcclaimPage5        iPage5                      = null;

        /** A reference to the 'register' button. */
        protected                       Button                          iButtonRegister             = null;
        /** A reference to the 'login' button. */
        protected                       Button                          iButtonLogin                = null;

        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            //reference this instance
            singleton = this;

            //init state activity
            PicFoodState.initStateActivity
            (
                PicFoodState.EAcclaim,
                R.layout.state_content_acclaim,
                0,
                HideHeaderBar.EYes,
                ShowHeaderBackButton.ENo,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.EYes,
                PicFoodAction.ENone
            );

            //setup footer
            setupFooter();

            //setup bg
            setupBg();

            //create pages
            setupPages();

            //try auto-login
            if ( Features.ENABLE_AUTO_LOGIN ) PicFoodFlowAutoLogin.checkAutoLogin( PicFoodState.EAcclaim );
        }

        /**********************************************************************************************
        *   Sets up the footer of the acclaim-state, where the 'register' and 'login' buttons are displayed.
        **********************************************************************************************/
        private final void setupFooter()
        {
            //setup button 'register'
            iButtonRegister = (Button)findViewById( R.id.button_register );
            LibUI.setupButton( this, iButtonRegister, R.string.state_acclaim_register, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionState.EEnterRegister );

            //setup button 'login'
            iButtonLogin = (Button)findViewById( R.id.button_login );
            LibUI.setupButton( this, iButtonLogin, R.string.state_acclaim_login, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionState.EEnterLogin );
        }

        /**********************************************************************************************
        *   Sets up the background of the ViewPager, where the map is displayed.
        **********************************************************************************************/
        private final void setupBg()
        {
            //reference container for bg icons
            iIconView = (RelativeLayout)findViewById( R.id.bg_icons );

            //assign map image
            iIconView.setBackgroundResource( R.drawable.net_picfood_acclaim_bg );
        }

        /**********************************************************************************************
        *   Sets up all pages for the ViewPager and assigns them.
        **********************************************************************************************/
        private final void setupPages()
        {
            //create pages for view-pager
            iPage1 = new PicFoodStateAcclaimPage1( this );
            iPage2 = new PicFoodStateAcclaimPage2( this );
            iPage3 = new PicFoodStateAcclaimPage3( this );
            iPage4 = new PicFoodStateAcclaimPage4( this );
            iPage5 = new PicFoodStateAcclaimPage5( this );

            //reference featured list view pager - the title-page-indicator may be null!
            {
                                            iViewPagerAcclaim       = (ViewPager)findViewById(                  R.id.acclaim_view_pager             );
                CirclePageIndicator         titlePageIndicator      = (CirclePageIndicator)findViewById(  R.id.acclaim_view_pager_title_strip );
                LibViewPagerAdapter         adapter                 = new LibViewPagerAdapter
                (
                    new LibAdapterData[]
                    {
                        new LibViewPagerView( "Page 1", iPage1.iView ),
                        new LibViewPagerView( "Page 2", iPage2.iView ),
                        new LibViewPagerView( "Page 3", iPage3.iView ),
                        new LibViewPagerView( "Page 4", iPage4.iView ),
                        new LibViewPagerView( "Page 5", iPage5.iView ),
                    }
                );

                //setup ViewPager
                LibUI.setupViewPager( this, adapter, iViewPagerAcclaim );

                //setup TitlePageIndicator if existent
                if ( titlePageIndicator != null )
                {
                    //setup TitlePageIndicator
                    //titlePageIndicator.setup(  this, this, iViewPagerAcclaim, PicFoodSystems.getFonts().TYPEFACE_BOLD );
                    titlePageIndicator.setViewPager( iViewPagerAcclaim );

                    //hide ViewPagerIndicator ( sample usage )
                    final boolean HIDE_VIEW_PAGER_INDICATOR = false;
                    if ( HIDE_VIEW_PAGER_INDICATOR )
                    {
                        titlePageIndicator.setVisibility( View.GONE );
                    }
                }

                //set last viewed item if available
                if ( lastPageIndex != -1 )
                {
                    //set last viewed page
                    iViewPagerAcclaim.setCurrentItem(   lastPageIndex, false );
                    if ( titlePageIndicator != null )
                    {
                        titlePageIndicator.setCurrentItem(  lastPageIndex );
                    }
                }
            }
        }

        @Override
        public final void onPageScrolled( int i, float f, int i2 )
        {
        }

        @Override
        public final void onPageScrollStateChanged( int i )
        {
        }

        @Override
        public final void onPageSelected( int i )
        {
            //PicFoodDebug.bugfix.out( ">>>>>>>>>>> onPageSelected - " + i );

            //remember the last selected index
            lastPageIndex = i;
        }

        /**********************************************************************************************
        *   Switches to the 1st page.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public final void resetViewPagerUIThreaded()
        {
            //run on ui-thread
            runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        iViewPagerAcclaim.setCurrentItem( 0, false );
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Switches to the next page of the ViewPager.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void switchViewPagerToNextPageUIThreaded()
        {
            //run on ui-thread
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        singleton.iViewPagerAcclaim.setCurrentItem( singleton.iViewPagerAcclaim.getCurrentItem() + 1, true );
                    }
                }
            );
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }

        /**********************************************************************************************
        *   Switches to the last page.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void switchViewPagerToLastPageUIThreaded()
        {
            //run on ui-thread
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        singleton.iViewPagerAcclaim.setCurrentItem( 4 );
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Sets the last page as the next page to change to on returning to the ViewPager.
        **********************************************************************************************/
        public static final void setLastPageIndexToLastPage()
        {
            lastPageIndex = 4;
        }

        /**********************************************************************************************
        *   Unselects the buttons 'register' and 'login'.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void unselectAllButtonsUIThreaded()
        {
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                      //iButtonSkip.setSelected(        false );

                        singleton.iButtonRegister.setSelected(    false );
                        singleton.iButtonLogin.setSelected(       false );
                    }
                }
            );
        }
    }
