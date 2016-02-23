/*  $Id: LibViewPagerView.java 50508 2013-08-07 14:05:16Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.adapter;

    import  android.view.*;
    import  com.synapsy.android.lib.LibState;

    /************************************************************************
    *   Represents one page in a ViewPager.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50508 $ $Date: 2013-08-07 16:05:16 +0200 (Mi, 07 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/adapter/LibViewPagerView.java $"
    ************************************************************************/
    public final class LibViewPagerView implements LibAdapterData
    {
        /** This view's title being possibly displayed in a PagerTitleStrip. */
        private         String          iTitle                  = null;

        /** The content view of this ViewPager. */
        private         View            iView                   = null;

        /************************************************************************
        *   Creates a page for a ViewPager.
        *
        *   @param  aTitle  This page's title.
        *   @param  aView   This page's content view.
        ************************************************************************/
        public LibViewPagerView( String aTitle, View aView )
        {
            iTitle  = aTitle;
            iView   = aView;
        }

        @Override
        public View createView( LibState state )
        {
            return iView;
        }

        @Override
        public View getView()
        {
            return iView;
        }

        @Override
        public String getTitle()
        {
            return iTitle;
        }

        @Override
        public void updateView( LibState state )
        {
            //do nothing - view is constant..!
        }

        @Override
        public Runnable getActionOnClick()
        {
            return null;
        }

        @Override
        public synchronized void changeVisibility( boolean visible )
        {
        }
    }
