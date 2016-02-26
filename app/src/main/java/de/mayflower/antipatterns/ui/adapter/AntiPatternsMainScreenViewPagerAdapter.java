
    package de.mayflower.antipatterns.ui.adapter;

    import android.content.Context;
    import android.os.Bundle;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import de.mayflower.antipatterns.AntiPatternsMainScreenViewPagerFragment;

    /************************************************************************
    *   The adapter for the main screen view pager.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class AntiPatternsMainScreenViewPagerAdapter extends FragmentPagerAdapter
    {
        private         AntiPatternsMainScreenViewPagerFragment[]       fragments       = null;

        public AntiPatternsMainScreenViewPagerAdapter( FragmentManager fm )
        {
            super( fm );
        }

        public final void init()
        {
            fragments = new AntiPatternsMainScreenViewPagerFragment[]
            {
                new AntiPatternsMainScreenViewPagerFragment(),
                new AntiPatternsMainScreenViewPagerFragment(),
                new AntiPatternsMainScreenViewPagerFragment(),
                new AntiPatternsMainScreenViewPagerFragment(),
                new AntiPatternsMainScreenViewPagerFragment(),
            };

            fragments[ 0 ].init( 0, "Title 1" );
            fragments[ 1 ].init( 1, "Title 2" );
            fragments[ 2 ].init( 2, "Title 3" );
            fragments[ 3 ].init( 3, "Title 4" );
            fragments[ 4 ].init( 4, "Title 5" );
        }

        @Override
        public Fragment getItem( int position )
        {
            return fragments[ position ];
        }

        @Override
        public int getCount()
        {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle( int position )
        {
            return fragments[ position ].getTitle();
        }
    }
