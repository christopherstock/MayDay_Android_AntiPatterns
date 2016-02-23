/*  $Id: LibFragmentTabPagerAdapter.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.adapter;

    import  java.util.List;
    import  android.support.v4.app.Fragment;
    import  android.support.v4.app.FragmentManager;
    import  android.support.v4.app.FragmentPagerAdapter;

    /**********************************************************************************************
    *   Serves the fragments when paging.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/adapter/LibFragmentTabPagerAdapter.java $"
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
