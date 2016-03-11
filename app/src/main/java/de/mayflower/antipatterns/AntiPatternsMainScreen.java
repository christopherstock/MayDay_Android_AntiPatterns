
    package de.mayflower.antipatterns;

    import  android.os.Bundle;
    import  android.support.v4.app.FragmentActivity;
    import  android.support.v4.view.ViewPager;
    import  android.view.Menu;
    import  android.view.MenuInflater;
    import  android.view.MenuItem;

    import de.mayflower.lib.LibResource;

    /**********************************************************************************************
    *   The startup activity class.
    *
    *   TODO ASAP   Move TODO list to trello board.
    *   TODO ASAP   Rename 'problems' to 'symptoms'.
    *   TODO ASAP   Rename remove 'AntiPatterns' from all classes.
    *   TODO ASAP   Move classes to appropriate subpackages.
    *   TODO ASAP   Button in detailed view for returning.
    *   TODO WEAK   Hold and pass all data in instance fields!
    *   TODO WEAK   Support latest API Level.
    *   TODO WEAK   Remove all old classes and references.
    *   TODO WEAK   Remove ALL inspection issues AND/OR warnings and confectionate Inspection profile!
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

            AntiPatternsDebug.major.out("Welcome to [" + AntiPatternsVersion.getVersion() + "]");

            setContentView(R.layout.antipatterns_main_screen);

            AntiPatternsHydrator.hydrateData( this );

            setupPagerAdapter();
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

        /*****************************************************************************
        *   Sets up the pager-adapter for the pager view.
        *****************************************************************************/
        private void setupPagerAdapter()
        {
            AntiPatternsMainScreenViewPagerAdapter pagerAdapter = new AntiPatternsMainScreenViewPagerAdapter
            (
                this.getSupportFragmentManager()
            );

            pagerAdapter.init();

            ViewPager viewPager = (ViewPager)findViewById( R.id.main_screen_pager );
            viewPager.setAdapter(pagerAdapter);
        }
    }
