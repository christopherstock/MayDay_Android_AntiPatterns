
    package de.mayflower.antipatterns;

    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.acclaim.*;
    import  android.os.*;
    import  android.app.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.widget.LibDebugConsole.*;

    /**********************************************************************************************
    *   The old startup activity class.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ***********************************************************************************************/
    public class AntiPatternsOld extends Activity
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
            AntiPatternsSystems.init(this);
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
