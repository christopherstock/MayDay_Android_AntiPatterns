/*  $Id: PicFoodStatePivotal.java 50578 2013-08-13 12:46:00Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.state.pivotal;

    import  java.util.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.PicFoodSave.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  android.app.*;
    import  android.os.Bundle;
    import  android.support.v4.app.Fragment;
    import  android.support.v4.app.FragmentActivity;
    import  android.support.v4.view.ViewPager;
    import  android.view.*;
    import  android.widget.*;
    import  android.widget.ImageView.ScaleType;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.adapter.*;

    /**********************************************************************************************
    *   This class implements the Fragment activity that maintains a TabHost using a ViewPager
    *   and represents the pivotal menu.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50578 $ $Date: 2013-08-13 14:46:00 +0200 (Di, 13 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/pivotal/PicFoodStatePivotal.java $"
    **********************************************************************************************/
    public class PicFoodStatePivotal extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
    {
        /** The tag for the tab 'wall'. */
        public      static  final   String                              TAB_TAG_WALL                    = "tab_wall";
        /** The tag for the tab 'explore'. */
        public      static  final   String                              TAB_TAG_EXPLORE                 = "tab_explore";
        /** The tag for the tab 'uplpoad'. */
        public      static  final   String                              TAB_TAG_UPLOAD                  = "tab_upload";
        /** The tag for the tab 'profile'. */
        public      static  final   String                              TAB_TAG_PROFILE                 = "tab_profile";

        /** The singleton instance of this state. */
        public      static          PicFoodStatePivotal                 singleton                       = null;

        /** The last tab that has been selected by the user. */
        protected   static          String                              lastSelectedTab                 = TAB_TAG_WALL;

        /** The TabHost that is displayed on the bottom of this state. */
        protected                   TabHost                             iTabHost                        = null;
        /** The ViewPager that is bound to the TabHost in this {@link FragmentActivity}. */
        private                     ViewPager                           iViewPager                      = null;
        /** The adapter that serves the ViewPager with Fragments. */
        public                      LibFragmentTabPagerAdapter          iPagerAdapter                   = null;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //assign singleton
            singleton = this;

            //init pivotal state
            initPivotal();
        }

        @Override
        public void onStart()
        {
            //invoke super method
            super.onStart();

            //check if returning from GPS-settings
            String submitSearchDialogNextOnStart = PicFoodSave.loadSetting( this, SaveKey.EStatePivotalShowSearchDialogNextOnStart );
            PicFoodDebug.imageSearch.out( "PicFoodStatePivotal::onStart() - submitSearchDialogNextOnStart is [" + submitSearchDialogNextOnStart + "]" );
            if ( submitSearchDialogNextOnStart != null && submitSearchDialogNextOnStart.equals( "true" ) )
            {
                //unflag
                PicFoodSave.saveSetting( this, SaveKey.EStatePivotalShowSearchDialogNextOnStart, "false" );

                //do it
                PicFoodActionDialog.EDialogSearchImages.run();
            }
        }

        /**********************************************************************************************
        *   Inits the pivotal state.
        **********************************************************************************************/
        private void initPivotal()
        {
            //init state activity
            iTabHost = (TabHost)PicFoodState.initStateActivity
            (
                PicFoodState.EPivotalMenu,
                R.layout.state_content_pivotal,
                0,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.ENo,
                ShowHeaderSettingsButton.EYes,
                ShowHeaderSearchButton.EYes,
                ShowHeaderAppLogo.EYes,
                PicFoodActionPush.EPushEclipseApp
            );

            //init TabHost
            initialiseTabHost();

            //intialise ViewPager
            intialiseViewPager();

            //init TabWidget-bgs
            initTabWidgetBgs();

            //switch to last selected tab
            iTabHost.setCurrentTabByTag( lastSelectedTab );
        }

        @Override
        public void onTabChanged( String tag )
        {
            //TabInfo newTab = mapTabInfo.get(tag);
            int pos = iTabHost.getCurrentTab();
            iViewPager.setCurrentItem( pos );

            //remember this tab as the last selected one
            setLastSelectedTab( tag );
        }

        @Override
        public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels )
        {
        }

        @Override
        public void onPageScrollStateChanged( int state )
        {
        }

        @Override
        public void onPageSelected( int position )
        {
            iTabHost.setCurrentTab( position );
        }

        /**********************************************************************************************
        *   Initialise the Tab Host
        **********************************************************************************************/
        private void initialiseTabHost()
        {
            iTabHost.setup();
            addTab( iTabHost, iTabHost.newTabSpec( TAB_TAG_WALL    ).setIndicator( LibUI.createImageView( this, R.drawable.net_picfood_pivotal_item_wall_fg,      ScaleType.CENTER_INSIDE ) ) );
            addTab( iTabHost, iTabHost.newTabSpec( TAB_TAG_EXPLORE ).setIndicator( LibUI.createImageView( this, R.drawable.net_picfood_pivotal_item_explore_fg,   ScaleType.CENTER_INSIDE ) ) );
            addTab( iTabHost, iTabHost.newTabSpec( TAB_TAG_UPLOAD  ).setIndicator( LibUI.createImageView( this, R.drawable.net_picfood_pivotal_item_upload_fg,    ScaleType.CENTER_INSIDE ) ) );
          //addTab( iTabHost, iTabHost.newTabSpec( TAB_TAG_NEWS    ).setIndicator( LibUI.createImageView( this, R.drawable.net_picfood_pivotal_item_news_fg,      ScaleType.CENTER_INSIDE ) ) );
            addTab( iTabHost, iTabHost.newTabSpec( TAB_TAG_PROFILE ).setIndicator( LibUI.createImageView( this, R.drawable.net_picfood_pivotal_item_profile_fg,   ScaleType.CENTER_INSIDE ) ) );

            // Default to first tab
            //onTabChanged("Tab1");
            iTabHost.setOnTabChangedListener( this );
        }

        /**********************************************************************************************
        *   Initialise ViewPager
        **********************************************************************************************/
        private void intialiseViewPager()
        {
            List<Fragment> fragments = new Vector<Fragment>();

            fragments.add( Fragment.instantiate( this, PicFoodStatePivotalTabWall.class.getName()       ) );
            fragments.add( Fragment.instantiate( this, PicFoodStatePivotalTabExplore.class.getName()    ) );
            fragments.add( Fragment.instantiate( this, PicFoodStatePivotalTabUpload.class.getName()     ) );
          //fragments.add( Fragment.instantiate( this, PicFoodStatePivotalTabNews.class.getName()       ) );
            fragments.add( Fragment.instantiate( this, PicFoodStatePivotalTabProfile.class.getName()    ) );

            //setup PagerAdapter
            iPagerAdapter  = new LibFragmentTabPagerAdapter( super.getSupportFragmentManager(), fragments );

            //init ViewPager
            iViewPager = (ViewPager)super.findViewById( R.id.viewpager );
            iViewPager.setAdapter( iPagerAdapter );
            iViewPager.setOnPageChangeListener( this );
        }

        /**********************************************************************************************
        *   Init the backgrounds for all tabs
        **********************************************************************************************/
        private void initTabWidgetBgs()
        {
            TabWidget tabWidget = iTabHost.getTabWidget();
            for ( int i = 0; i < tabWidget.getChildCount(); ++i )
            {
                tabWidget.getChildAt( i ).setBackgroundResource( R.drawable.net_picfood_bg_pivotal_tab );
            }
        }

        /**********************************************************************************************
        *   Adds a tab to the Tabhost
        *
        *   @param  tabHost     The TabHost to add a tab to.
        *   @param  tabSpec     The specification of the new tab to add.
        **********************************************************************************************/
        private void addTab( TabHost tabHost, TabHost.TabSpec tabSpec )
        {
            //create anonymous content-factory class
            tabSpec.setContent
            (
                new TabHost.TabContentFactory()
                {
                    @Override
                    public View createTabContent( String tag )
                    {
                        //return empty view
                        View v = new View( PicFoodStatePivotal.this );
                        //v.setMinimumWidth(  0 );
                        //v.setMinimumHeight( 0 );
                        return v;
                    }
                }
            );

            //add tabSpec to host
            tabHost.addTab( tabSpec );
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //eclipse the app
                    PicFoodActionPush.EPushEclipseApp.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }

        /**********************************************************************************************
        *   Sets the specified tab as the last selected tab.
        *
        *   @param  tag     The tag of the tab to set as the last selected tab.
        **********************************************************************************************/
        public static final void setLastSelectedTab( String tag )
        {
            lastSelectedTab = tag;
        }

        /**********************************************************************************************
        *   Switches to a specified tab.
        *
        *   @param  activity    The according activity context.
        *   @param  tag         The tag of the tab to switch to.
        **********************************************************************************************/
        public static final void changeToTabUIThreaded( Activity activity, String tag )
        {
            lastSelectedTab = tag;

            //switch to this tab
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        singleton.iTabHost.setCurrentTabByTag( lastSelectedTab );
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Unselects all buttons in the header.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void unselectHeaderButtonsUIThreaded()
        {
            //switch to this tab
            singleton.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //unselect header buttons
                        singleton.findViewById( R.id.settings_icon ).setSelected( false );
                        singleton.findViewById( R.id.search_icon   ).setSelected( false );
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Checks, if the specified tab is currently selected in the pivotal menu.
        *   This method is performed on the UI-Thread.
        *
        *   @param  tagToCheck  The tag of the tab that shall be checked for selection.
        *   @return             <code>true</code> if the specified tag is currently selected.
        *                       Otherwise <code>false</code>.
        **********************************************************************************************/
        public static final boolean isTabSelected( String tagToCheck )
        {
            return ( lastSelectedTab.equals( tagToCheck ) );
        }
    }
