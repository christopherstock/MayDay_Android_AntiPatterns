
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
        private         AntiPatternsMainScreenViewPagerFragment[]       fragments       = null;

        public AntiPatternsMainScreenViewPagerAdapter( FragmentManager fm )
        {
            super( fm );
        }

        public final void init()
        {
            fragments = new AntiPatternsMainScreenViewPagerFragment[]
            {
                new AntiPatternsMainScreenViewPagerFragment( 0, "Title 1" ),
                new AntiPatternsMainScreenViewPagerFragment( 1, "Title 2" ),
                new AntiPatternsMainScreenViewPagerFragment( 2, "Title 3" ),
                new AntiPatternsMainScreenViewPagerFragment( 3, "Title 4" ),
                new AntiPatternsMainScreenViewPagerFragment( 4, "Title 5" ),
            };
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
