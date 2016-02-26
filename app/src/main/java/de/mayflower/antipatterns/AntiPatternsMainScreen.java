
    package de.mayflower.antipatterns;

    import  android.os.Bundle;
    import  android.support.v4.app.FragmentActivity;
    import  android.support.v4.view.ViewPager;
    import  android.view.Menu;
    import  android.view.MenuInflater;
    import  android.view.MenuItem;
    import  de.mayflower.antipatterns.ui.adapter.AntiPatternsMainScreenViewPagerAdapter;

    /**********************************************************************************************
    *   The startup activity class.
    *
    *   TODO HIGH   Button in detailed view for returning.
    *   TODO HIGH   Check if the ViewPager TabStrip can be clickable!
    *   TODO HIGH   Explicitly return to the last ViewPager page.
    *   TODO HIGH   Animate returning from detailed view via back button hardkey.
    *   TODO HIGH   Resolve the ViewPager- cached views problem.
    *   TODO HIGH   Remove ALL inspection issues AND/OR warnings and confectionate Inspection profile!
    *   TODO INIT   Hold and pass all data in instance fields!
    *   TODO WEAK   Support latest API Level.
    *
    *   DONE        Try animations between activity change.
    *   DONE        Removed item press event colliding with swipe touch.
    *   DONE        Remove iml (trunk.iml) and unused module files on project root?
    *   DONE        Try PagerTabStrip.
    *   DONE        Created new icon.
    *   DONE        Removed old company name.
    *   DONE        Pruned old specifier everywhere!
    *   DONE        Testd on device!
    *   DONE        Created mayflower certification file.
    *   DONE        Renamed all classes to 'AntiPatterns'.
    *   DONE        Fixed linebreaks in all files.
    *   DONE        Pruned class header in all files.
    *   DONE        Removed '$' everywhere.
    *   DONE        Altered doc-block in all classes (author/version).
    *   DONE        Setup IDE and code inspection.
    *   DONE        Rename Library classes and main app's package names.
    *   DONE        Removed external Facebook- and Google-Cloud-Messaging-API.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ***********************************************************************************************/
    public class AntiPatternsMainScreen extends FragmentActivity
    {
        /*****************************************************************************
        *   Being invoked when this activity is being created.
        *****************************************************************************/
        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            AntiPatternsDebug.major.out( AntiPatternsMainScreen.class + "::onCreate()" );

            //init all systems
            AntiPatternsSystems.init( this );
        }

        /*****************************************************************************
        *   Being invoked after this activity has been created and on returning.
        *****************************************************************************/
        @Override
        public void onStart()
        {
            //invoke super method
            super.onStart();

            AntiPatternsDebug.major.out( AntiPatternsMainScreen.class + "::onStart()" );

            //submit acclaim message in output and console
            String acclaim = "Welcome to [ " + AntiPatternsVersion.getVersion() + " ]";
            AntiPatternsDebug.major.out(acclaim);

            setContentView(R.layout.antipatterns_main_screen);

            AntiPatternsMainScreenViewPagerAdapter pagerAdapter = new AntiPatternsMainScreenViewPagerAdapter
            (
                this.getSupportFragmentManager(),
                this
            );

            ViewPager viewPager = (ViewPager)findViewById( R.id.main_screen_pager );
            viewPager.setAdapter(pagerAdapter);
        }

        /*****************************************************************************
        *   Being invoked when the menu key is pressed.
        *****************************************************************************/
        @Override
        public boolean onCreateOptionsMenu( Menu menu )
        {
            AntiPatternsDebug.major.out( AntiPatternsMainScreen.class + "::onCreateOptionsMenu()" );

            MenuInflater inflater = getMenuInflater();
            inflater.inflate( R.menu.menu_main, menu );

            return true;
        }
/*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.gesture, menu);
            return true;
        }
*/
        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            AntiPatternsDebug.major.out( AntiPatternsMainScreen.class + "::onOptionsItemSelected()" );

            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
/*
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
*/
            return super.onOptionsItemSelected(item);
        }
    }
