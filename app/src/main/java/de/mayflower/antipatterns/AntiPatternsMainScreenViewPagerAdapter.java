
    package de.mayflower.antipatterns;

    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;

    import de.mayflower.antipatterns.AntiPatternsMainScreenViewPagerFragment;
    import de.mayflower.antipatterns.data.*;

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
            Category[] cats = AntiPatternsHydrator.categories;
            AntiPatternsMainScreenViewPagerFragment[] fragments = new AntiPatternsMainScreenViewPagerFragment[cats.length];
            for ( int i = 0; i < AntiPatternsHydrator.categories.length; i++ )
            {
                fragments[i] = new AntiPatternsMainScreenViewPagerFragment();
                fragments[i].init( AntiPatternsHydrator.categories[i].getId(), AntiPatternsHydrator.categories[i].getName());
            }
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
