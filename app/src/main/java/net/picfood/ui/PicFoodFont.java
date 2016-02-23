/*  $Id: PicFoodFont.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.ui;

    import  net.picfood.PicFoodProject.Assets;
    import  android.content.*;
    import  android.graphics.*;

    import  com.synapsy.android.lib.io.*;

    /*****************************************************************************
    *   Holds all fonts the application is using.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/ui/PicFoodFont.java $"
    *****************************************************************************/
    public class PicFoodFont
    {
        /** A default typeface. A regular TrueType-font should be assigned here. */
        public                          Typeface            TYPEFACE_REGULAR                        = null;

        /** A bold typeface. A bold TrueType-font should be assigned here. */
        public                          Typeface            TYPEFACE_BOLD                           = null;

        /*****************************************************************************
        *   Initializes all fonts that are used in the application.
        *   A default typeface can be created using
        *   e.g.: Typeface.create( "Droid Sans", Typeface.NORMAL );
        *
        *   @param  context     The current application context.
        *****************************************************************************/
        public PicFoodFont( Context context )
        {
            //init global ( foreign ) typeface
            TYPEFACE_REGULAR     = LibIO.createTypefaceFromAsset( context, Assets.FILENAME_ASSET_FONT_REGULAR     );
            TYPEFACE_BOLD        = LibIO.createTypefaceFromAsset( context, Assets.FILENAME_ASSET_FONT_BOLD        );
        }
    }
