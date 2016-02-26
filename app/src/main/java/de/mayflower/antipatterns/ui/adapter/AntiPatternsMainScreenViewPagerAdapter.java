
    package de.mayflower.antipatterns.ui.adapter;

    import android.content.Context;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;

    import de.mayflower.antipatterns.AntiPatternsMainScreenViewPagerFragment;

    /************************************************************************
    *   The adapter for the main screen view pager.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class AntiPatternsMainScreenViewPagerAdapter extends FragmentPagerAdapter
    {
        Context context;

        public AntiPatternsMainScreenViewPagerAdapter( FragmentManager fm, Context context )
        {
            super( fm );
            this.context = context;
        }

        @Override
        public Fragment getItem( int position )
        {
            return new AntiPatternsMainScreenViewPagerFragment( position );
        }

        @Override
        public int getCount()
        {
            return 5;
        }

        @Override
        public CharSequence getPageTitle( int position ) {
            return "Page " + ( position + 1 );
        }
    }
