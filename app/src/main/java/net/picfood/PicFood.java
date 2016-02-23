/*  $Id: PicFood.java 50822 2013-09-04 10:38:07Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood;

    import  net.picfood.state.*;
    import  net.picfood.state.acclaim.*;
    import  android.os.Bundle;
    import  android.app.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.ui.widget.LibDebugConsole.ConsoleColor;

    /**********************************************************************************************
    *   The startup activity class.
    *
    *   ASK     Allow non-square images?
    *
    *   TODO ASAP   Facebook button in state 'register' is fuzzy.
    *   TODO ASAP   Import Facebook-data: Try to pick email and username!
    *
    *   DONE    Avoided NullPointerException in PicFoodActionPush.ELeaveFollowDetails.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50822 $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/PicFood.java $"
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

            //PicFoodDebug.major.out( "PicFood::onCreate()" );

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
            LibLauncher.launchActivity( PicFood.singleton, PicFoodStateAcclaim.class, R.anim.fade_in, R.anim.fade_out );

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
