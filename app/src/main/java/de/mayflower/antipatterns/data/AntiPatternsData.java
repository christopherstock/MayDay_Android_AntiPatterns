
    package de.mayflower.antipatterns.data;

    import  de.mayflower.antipatterns.ui.AntiPatternsUI.ImageSize;
    import  android.graphics.drawable.*;
    import  android.view.*;

    import de.mayflower.lib.*;

    /************************************************************************
    *   Represents data that can be assigned into a {@link de.mayflower.antipatterns.ui.adapter.AntiPatternsGridViewContent}.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public interface AntiPatternsData
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
