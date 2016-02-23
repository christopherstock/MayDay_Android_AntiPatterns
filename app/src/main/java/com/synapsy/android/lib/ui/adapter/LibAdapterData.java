/*  $Id: LibAdapterData.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.adapter;

    import  android.view.*;

    import  com.synapsy.android.lib.*;

    /************************************************************************
    *   Represents one item in an adapter.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/adapter/LibAdapterData.java $"
    ************************************************************************/
    public interface LibAdapterData
    {
        /************************************************************************
        *   Constructs the view and returns it.
        *
        *   @param  state       The according state.
        *   @return             The constructed view for this item.
        ************************************************************************/
        public View createView( LibState state );

        /************************************************************************
        *   Returns the created view. This method is being invoked by the system
        *   each time the view is ordered.
        *
        *   @return     The view for this item.
        ************************************************************************/
        public View getView();

        /************************************************************************
        *   Returns this item's title, if any.
        *
        *   @return     The title of this view. This may be used to represent
        *               the title of a ViewPager-page.
        ************************************************************************/
        public String getTitle();

        /************************************************************************
        *   Updates the view for this adapter item.
        *
        *   @param  state       The according state.
        ************************************************************************/
        public void updateView( LibState state );

        /************************************************************************
        *   Returns the associated click action.
        *
        *   @return     The action to be performed when this view is clicked.
        ************************************************************************/
        public Runnable getActionOnClick();

        /************************************************************************
        *   Updates this item's visibility.
        *
        *   @param  visible     <code>true</code> if this item is currently visible.
        *                       Otherwise <code>false</code>.
        ************************************************************************/
        public void changeVisibility( boolean visible );
    }
