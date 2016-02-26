
    package de.mayflower.antipatterns;

    import  android.app.Activity;
    import  android.os.Bundle;
    import  android.view.Menu;
    import android.view.MenuInflater;

    /**********************************************************************************************
    *   The startup activity class.
    *
    *   TODO HIGH   Try PagerTabStrip.
    *   TODO HIGH   Remove ALL inspection issues AND/OR warnings and confectionate Inspection profile!
    *   TODO ASAP   Support latest API Level.
    *
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
    public class AntiPatterns extends Activity
    {
        /*****************************************************************************
        *   Being invoked when this activity is being created.
        *****************************************************************************/
        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            AntiPatternsDebug.major.out( "AntiPatterns::onCreate()" );

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

            AntiPatternsDebug.major.out( "AntiPatterns::onStart()" );

            //submit acclaim message in output and console
            String acclaim = "Welcome to [ " + AntiPatternsVersion.getVersion() + " ]";
            AntiPatternsDebug.major.out(acclaim);


            setContentView( R.layout.antipatterns_main_screen);




            //launch initial state 'acclaim'
            //LibLauncher.launchActivity( this, AntiPatternsStateAcclaim.class, R.anim.fade_in, R.anim.fade_out );

        }

        /*****************************************************************************
        *   Being invoked when the menu key is pressed.
        *****************************************************************************/
        @Override
        public boolean onCreateOptionsMenu( Menu menu )
        {
            AntiPatternsDebug.major.out( "AntiPatterns::onCreateOptionsMenu()" );

            MenuInflater inflater = getMenuInflater();
            inflater.inflate( R.menu.menu_main, menu );
            return true;
        }

    }
