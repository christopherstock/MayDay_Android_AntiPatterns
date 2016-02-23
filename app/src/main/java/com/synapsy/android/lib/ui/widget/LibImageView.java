/*  $Id: LibImageView.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.widget;

    import  android.content.*;
    import  android.graphics.*;
    import  android.graphics.drawable.*;
    import  android.util.*;
    import  android.widget.*;

    /************************************************************************
    *   Represents a custom ImageView that avoids drawing recycled Bitmaps.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/widget/LibImageView.java $"
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
