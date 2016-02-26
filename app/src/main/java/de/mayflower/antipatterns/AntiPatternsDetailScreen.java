
    package de.mayflower.antipatterns;

    import android.app.Activity;
    import android.os.Bundle;

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

            setContentView( R.layout.antipatterns_detail_screen );
        }
    }
