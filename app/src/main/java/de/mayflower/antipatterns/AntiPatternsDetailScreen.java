
    package de.mayflower.antipatterns;

    import  android.app.Activity;
    import  android.os.Bundle;
    import de.mayflower.lib.api.LibAPI;
    import de.mayflower.lib.api.LibModernAPI5;

    /**********************************************************************************************
    *   The details screen that contains the detailed anti pattern description.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ***********************************************************************************************/
    public class AntiPatternsDetailScreen extends Activity
    {
        /*****************************************************************************
        *   Being invoked when this activity is being created.
        *****************************************************************************/
        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            //invoke super method
            super.onCreate( savedInstanceState );

            AntiPatternsDebug.major.out( AntiPatternsDetailScreen.class + "::onCreate()" );

            setContentView( R.layout.antipatterns_detail_screen );
        }

        /*****************************************************************************
        *   Being invoked after this activity has been created and on returning.
        *****************************************************************************/
        @Override
        public void onStart()
        {
            //invoke super method
            super.onStart();

            AntiPatternsDebug.major.out( AntiPatternsDetailScreen.class + "::onStart()" );


        }

        @Override
        public void onBackPressed()
        {
            super.onBackPressed();

            //only operative since API-level 5
            if ( !LibAPI.isSdkLevelLowerThen(5) )
            {
                LibModernAPI5.overridePendingTransition
                (
                    this,
                    R.anim.push_right_in,
                    R.anim.push_right_out
                );
            }
        }
    }
