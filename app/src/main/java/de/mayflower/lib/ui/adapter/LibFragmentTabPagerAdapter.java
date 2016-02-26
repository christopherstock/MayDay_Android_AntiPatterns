
    package de.mayflower.lib.ui.adapter;

    import  java.util.List;
    import  android.support.v4.app.Fragment;
    import  android.support.v4.app.FragmentManager;
    import  android.support.v4.app.FragmentPagerAdapter;

    /**********************************************************************************************
    *   Serves the fragments when paging.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class LibFragmentTabPagerAdapter extends FragmentPagerAdapter
    {
        /** All fragments this TabPagerAdapter provides. */
        private                     List<Fragment>                          fragments               = null;

        /**********************************************************************************************
        *   Constructs a new TabPagerAdapter-manager.
        *
        *   @param  fm          The FragmentManager of a FragmentActivity.
        *   @param  aFragments  All Fragments to assign to this adapter.
        **********************************************************************************************/
        public LibFragmentTabPagerAdapter( FragmentManager fm, List<Fragment> aFragments )
        {
            super( fm );
            this.fragments = aFragments;
        }

        @Override
        public Fragment getItem( int position )
        {
            return this.fragments.get( position );
        }

        @Override
        public int getCount()
        {
            return this.fragments.size();
        }
    }
