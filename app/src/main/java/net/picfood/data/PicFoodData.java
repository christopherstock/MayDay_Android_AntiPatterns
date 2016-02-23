/*  $Id: PicFoodData.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.data;

    import  net.picfood.ui.PicFoodUI.ImageSize;
    import  android.graphics.drawable.*;
    import  android.view.*;

    import  com.synapsy.android.lib.*;

    /************************************************************************
    *   Represents data that can be assigned into a {@link net.picfood.ui.adapter.PicFoodGridViewContent}.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/data/PicFoodData.java $"
    ************************************************************************/
    public interface PicFoodData
    {
        /************************************************************************
        *   Orders the required bitmap for this content.
        *
        *   @param  state                   The according state.
        ************************************************************************/
        public void orderImageThreaded( LibState state );

        /**********************************************************************************************
        *   Recycles all Bitmaps for this content.
        *   Must be invoked from the UI-Thread.
        **********************************************************************************************/
        public void recycleBitmaps();

        /************************************************************************
        *   Creates the View to propagate to the GridView item.
        *
        *   @param  state       The according state.
        *   @return             The constructed view from this adapter data.
        ************************************************************************/
        public View createItemView( final LibState state );

        /************************************************************************
        *   This callback method is invoked when the image is loaded.
        *   The adapter data should assign the loaded image to an according ImageView.
        *
        *   @param  state       The according state.
        *   @param  imageSize   The size of the ordered image.
        *   @param  bd          The BitmapDrawable that has been loaded.
        ************************************************************************/
        public void assignLoadedImage( LibState state, ImageSize imageSize, BitmapDrawable bd );
    }
