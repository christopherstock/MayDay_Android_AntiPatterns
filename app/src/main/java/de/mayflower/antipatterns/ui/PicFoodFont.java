
    package de.mayflower.antipatterns.ui;

    import  de.mayflower.antipatterns.PicFoodProject.Assets;
    import  android.content.*;
    import  android.graphics.*;
    import  de.mayflower.lib.io.*;

    /*****************************************************************************
    *   Holds all fonts the application is using.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
