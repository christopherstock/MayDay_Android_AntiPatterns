
    package de.mayflower.antipatterns.ui.adapter;

    import android.content.Context;
    import android.os.Bundle;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;

    import de.mayflower.antipatterns.AntiPatternsViewPagerFragment;

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
            Fragment fragment = new AntiPatternsViewPagerFragment();

            Bundle args = new Bundle();
            args.putInt( "page_position", position + 1 );

            fragment.setArguments( args );

            return fragment;
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
