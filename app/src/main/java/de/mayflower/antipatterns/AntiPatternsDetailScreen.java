
    package de.mayflower.antipatterns;

    import android.app.Activity;
    import android.os.Bundle;
    import android.support.v4.app.FragmentActivity;
    import android.support.v4.view.ViewPager;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;

    import de.mayflower.antipatterns.ui.adapter.AntiPatternsMainScreenViewPagerAdapter;

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

            setContentView(R.layout.antipatterns_detail_screen);
        }
    }
