
    package de.mayflower.antipatterns;

    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.acclaim.*;
    import  android.os.*;
    import  android.app.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.widget.LibDebugConsole.*;

    /**********************************************************************************************
    *   The startup activity class.
    *
    *   TODO ASAP   Rename Library classes package names.
    *   TODO ASAP   Remove facebook API.
    *   TODO ASAP   Prune class header in all files.
    *   TODO HIGH   Remove ALL inspection issues AND/OR warnings and confectionate Inspection profile!
    *   TODO ASAP   Facebook button in state 'register' is fuzzy.
    *   TODO ASAP   Import Facebook-data: Try to pick email and username!
    *
    *   DONE        Setup IDE and code inspection.
    *
    *   @author     Christopher Stock
    *   @version    0.1
    ***********************************************************************************************/
    public class PicFood extends Activity
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

            PicFoodDebug.major.out( "PicFood::onCreate()" );

            //init all systems
            PicFoodSystems.init( PicFoodState.EStartup );
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
            String acclaim = "Welcome to [ " + PicFoodVersion.getVersion() + " ]";
            PicFoodDebug.major.out( acclaim );
            PicFoodDebug.major.log( acclaim, ConsoleColor.EGreenBright );

            //launch initial state 'acclaim'
            LibLauncher.launchActivity( singleton, PicFoodStateAcclaim.class, R.anim.fade_in, R.anim.fade_out );

            //perform initial debug
            {
                //test all json-RPC connections on the new server
                //PicFoodJsonRPCTest.test( this );
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
