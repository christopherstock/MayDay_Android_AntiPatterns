
    package de.mayflower.antipatterns;

    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.acclaim.*;
    import  android.os.*;
    import  android.app.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.widget.LibDebugConsole.*;

    /**********************************************************************************************
    *   The startup activity class.
    *
    *   TODO ASAP   Create mayflower certification file.
    *   TODO ASAP   Test on device!
    *   TODO ASAP   Prune old specifier everywhere!
    *   TODO ASAP   Remove specifier 'synapsy'.
    *   TODO HIGH   Remove ALL inspection issues AND/OR warnings and confectionate Inspection profile!
    *   TODO ASAP   Support latest API Level.
    *   TODO ASAP   Create new icon.
    *
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
        *   The singleton instance of this activity.
        *****************************************************************************/
        public      static      Activity            singleton               = null;

        /*****************************************************************************
        *   Being invoked when this activity is being created.
        *****************************************************************************/
        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            //assign singleton context
            singleton = this;

            AntiPatternsDebug.major.out( "AntiPatterns::onCreate()" );

            //init all systems
            AntiPatternsSystems.init(AntiPatternsState.EStartup);
        }

        /*****************************************************************************
        *   Being invoked after this activity has been created and on returning.
        *****************************************************************************/
        @Override
        public void onStart()
        {
            //invoke super method
            super.onStart();

            //submit acclaim message in output and console
            String acclaim = "Welcome to [ " + AntiPatternsVersion.getVersion() + " ]";
            AntiPatternsDebug.major.out( acclaim );
            AntiPatternsDebug.major.log( acclaim, ConsoleColor.EGreenBright );

            //launch initial state 'acclaim'
            LibLauncher.launchActivity( singleton, AntiPatternsStateAcclaim.class, R.anim.fade_in, R.anim.fade_out );

            //perform initial debug
            {
                //test all json-RPC connections on the new server
                //AntiPatternsJsonRPCTest.test( this );
            }
        }
/*
        @Override public boolean onCreateOptionsMenu( Menu menu )
        {
            //inflate menu
            getMenuInflater().inflate( R.menu.pic_food, menu );
            return true;
        }
*/
    }
