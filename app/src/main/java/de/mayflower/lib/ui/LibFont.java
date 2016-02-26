
    package de.mayflower.lib.ui;

    import  android.graphics.*;
    import  android.graphics.Paint.FontMetricsInt;
    import  android.graphics.Paint.Style;

    /****************************************************************************
    *   The font system.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ****************************************************************************/
    public class LibFont
    {
        /** The native font that is measured. */
        private             Typeface    iTypeface                   = null;

        /** The paint instance used to measure fonts. */
        private             Paint       p                           = null;

        /** The text-size that must be set in order to receive the desired font-height. */
        private             int         iTextSize                   = 0;

        /** The actual measured height of this font. */
        private             int         iHeight                     = 0;

        /** The height of this font's ascender. This is the height above the baseline. */
        private             int         iAscend                     = 0;

        /** The height of this font's descender. This is the height below the baseline. */
        private             int         iDescend                    = 0;

        /****************************************************************************
        *   Creates a new font from the specified native font.
        *   The height of the font will be measured so it reaches the minimum height.
        *
        *   @param  aTypeface       The native font to use for this font.
        *   @param  aMinimumHeight  The minimum height for this font to set.
        ****************************************************************************/
        public LibFont( Typeface aTypeface, int aMinimumHeight )
        {
            iTypeface   = aTypeface;
            p           = new Paint();

            //set the physical height of this font
            setSizeByHeight( aMinimumHeight );
        }

        /****************************************************************************
        *   Returns the native font object.
        *
        *   @return     The native font this object uses.
        ****************************************************************************/
        public Typeface getTypeface()
        {
            return iTypeface;
        }

        /****************************************************************************
        *   The text-size that must be set when drawing with this font in order
        *   to draw with the desired height.
        *
        *   @return     The text-size that must be set for drawing operations.
        ****************************************************************************/
        public int getTextSize()
        {
            return iTextSize;
        }

        /****************************************************************************
        *   The actually measured height of this font.
        *
        *   @return     The height that one line of text in this font requires.
        ****************************************************************************/
        public int getHeight()
        {
            return iHeight;
        }

        /****************************************************************************
        *   Returns the font's ascender.
        *
        *   @return     Number of pixels this font ascends.
        ****************************************************************************/
        public int getAscend()
        {
            return iAscend;
        }

        /****************************************************************************
        *   Returns the font's descender.
        *
        *   @return     Number of pixels this font descends.
        ****************************************************************************/
        public int getDescend()
        {
            return iDescend;
        }

        /****************************************************************************
        *   Determines the desired textSize to set for receiving the desired font-height.
        *
        *   @param  desiredHeight   The target height for the font to set.
        ****************************************************************************/
        private void setSizeByHeight( int desiredHeight )
        {
            //set new size
            iTextSize = desiredHeight;

            //AppsDebugSystem.font.out( "set font size [" + desiredHeight + "] true size is [" + LibDrawing.getStringHeight( this ) + "]" );

            //increase the size if the meassured size is still lower than the desired size
            while ( getStringHeight() < desiredHeight )
            {
                ++iTextSize;
            }
            //AppsDebugSystem.font.out( "corrected font size is now [" + desiredHeight + "] meassured size [" + LibDrawing.getStringHeight( this ) + "]" );

            //update values
            iHeight     = getStringHeight();
            iAscend     = getStringAscend();
            iDescend    = getStringDescend();
/*
            if ( iDebug )
            {
                AppsDebugSystem.bugfix.out( "Debugging FONT: height  [" + iHeight  + "]" );
                AppsDebugSystem.bugfix.out( "Debugging FONT: ascend  [" + iAscend  + "]" );
                AppsDebugSystem.bugfix.out( "Debugging FONT: descend [" + iDescend + "]" );
            }
*/
        }

        /****************************************************************************
        *   Determines the desired textSize to set to fit the given string in the maximum width.
        *
        *   @param  availWidth  The available width for the String to fit in.
        *   @param  maxSize     The maximum accepted textsize for the String.
        *   @param  content     The string to fit the available width.
        ****************************************************************************/
        public void setSizeByWidth( int availWidth, int maxSize, String content )
        {
            //set new size
            iTextSize = maxSize;

            //increase the size if the meassured size is still lower than the desired size
            while ( getStringWidth( content ) > availWidth )
            {
                --iTextSize;
            }

            //update values
            iHeight     = getStringHeight();
            iAscend     = getStringAscend();
            iDescend    = getStringDescend();
        }

        /*********************************************************************************
        *   Calculates the length of the string by its discrete chars.
        *   ( Alternatively to {@link Paint#measureText(String)}. )
        *
        *   @param  str     The string to meassure.
        *   @return         The string-width of the given String.
        *********************************************************************************/
        public final int getStringWidth( String str )
        {
            int width = 0;
            if ( str == null )
            {
                return width;
            }

            p.setStyle(     Style.FILL  );
            p.setTypeface(  iTypeface   );
            p.setTextSize(  iTextSize   );

            //DEBUG_OUT( "get string width for ["+str+"]: ["+p.measureText( str )+"]" );
            return Math.round( p.measureText( str ) );
        }

        /*********************************************************************************
        *   Calculates the height of a string of the given font
        *
        *   @return     The height of this font. The text's ascender and descender are added.
        *********************************************************************************/
        private final int getStringHeight()
        {
            p.setStyle(     Style.FILL  );
            p.setTypeface(  iTypeface   );
            p.setTextSize(  iTextSize   );

            FontMetricsInt fmi = p.getFontMetricsInt();

            int ret = -fmi.top + fmi.bottom;
/*
            if ( iDebug )
            {
                AppsDebugSystem.bugfix.out( "CALCULATE FONT METRICS INT:" );
                AppsDebugSystem.bugfix.out( "top     [" + fmi.top     + "]" );
                AppsDebugSystem.bugfix.out( "ascent  [" + fmi.ascent  + "]" );
                AppsDebugSystem.bugfix.out( "leading [" + fmi.leading + "]" );
                AppsDebugSystem.bugfix.out( "descent [" + fmi.descent + "]" );
                AppsDebugSystem.bugfix.out( "bottom  [" + fmi.bottom  + "]" );

                AppsDebugSystem.bugfix.out( "FINAL HEIGHT IS: [" + ret + "]" );
            }
*/
            //this is the total height of the font
            return ret;
        }

        /****************************************************************************
        *   Returns the String's ascender.
        *
        *   @return     The number of pixels this String ascends.
        ****************************************************************************/
        private final int getStringAscend()
        {
            p.setStyle(     Style.FILL  );
            p.setTypeface(  iTypeface   );
            p.setTextSize(  iTextSize   );

            FontMetricsInt fmi = p.getFontMetricsInt();

            //DEBUG_OUT( "get string height for font [" + fnt + "]: [" + ( -p.ascent() + p.descent() ) + "]" );
            return -fmi.top;
        }

        /****************************************************************************
        *   Returns the String's descender.
        *
        *   @return     The number of pixels this String descends.
        ****************************************************************************/
        private final int getStringDescend()
        {
            p.setStyle(     Style.FILL  );
            p.setTypeface(  iTypeface   );
            p.setTextSize(  iTextSize   );

            FontMetricsInt fmi = p.getFontMetricsInt();

            //DEBUG_OUT( "get string height for font [" + fnt + "]: [" + ( -p.ascent() + p.descent() ) + "]" );
            return fmi.bottom;
        }
    }
