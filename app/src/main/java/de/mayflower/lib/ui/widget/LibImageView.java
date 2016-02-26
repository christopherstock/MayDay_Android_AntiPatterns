
    package de.mayflower.lib.ui.widget;

    import  android.content.*;
    import  android.graphics.*;
    import  android.graphics.drawable.*;
    import  android.util.*;
    import  android.widget.*;

    /************************************************************************
    *   Represents a custom ImageView that avoids drawing recycled Bitmaps.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class LibImageView extends ImageView
    {
        /************************************************************************
        *   This constructor is automatically called from the system
        *   when the according xml-class is instanciated.
        *   The application will crash if the ImageView is constructed
        *   and this method is missing.
        *
        *   @param  context     The current context.
        *   @param  as          The attribute set that is passed by the system.
        ************************************************************************/
        public LibImageView( Context context, AttributeSet as )
        {
            super( context, as );
        }

        @Override
        public final void onDraw( Canvas c )
        {
            //only propagate super call if the Bitmap
            if
            (
                    getDrawable() != null
                &&  getDrawable() instanceof BitmapDrawable
                &&  ( (BitmapDrawable)getDrawable() ).getBitmap() != null
                &&  ( (BitmapDrawable)getDrawable() ).getBitmap().isRecycled()
            )
            {
                //PicFoodDebug.bitmapRecycling.out( ">> DENIED onDraw FROM DRAWING A RECYCLED BITMAP!" );
            }
            else
            {
                super.onDraw( c );
            }
        }
    }
