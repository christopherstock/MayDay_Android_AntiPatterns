
    package de.mayflower.lib.ui;

    import  java.io.*;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.graphics.Bitmap.Config;
    import  android.graphics.Paint.Style;
    import  android.graphics.PorterDuff.Mode;
    import  android.graphics.Region.Op;
    import  android.graphics.drawable.*;
    import  android.net.*;
    import  de.mayflower.lib.ui.LibDrawing.*;

    /************************************************************************
    *   Offers extended image functionality.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public abstract class LibImage
    {
        /************************************************************************
        *   Specifies if the ratio of the bitmap's proportions shall be
        *   kept on performing resize operations.
        ************************************************************************/
        public static enum ConstrainProportions
        {
            /** Yes - The proportions of the image must be constrained on being resized. */
            EYes,

            /** No - The image may be distorted on being resized. */
            ENo,

            ;
        }

        /************************************************************************
        *   The implementing class must define a fill-style for bitmap-backgrounds.
        *   The definition of 'bitmap-background' is:
        *   The <code>BitmapBg</code> lies behind every bitmap.
        *   It becomes visible if the bitmap contains translucent pixels
        *   or if the canvas size is enhanced but the bitmap shall keep it's original size.
        ************************************************************************/
        public static interface BitmapBgFill
        {
        }

        /************************************************************************
        *   Specifies a filler for the <code>BitmapBg</code> that fills it with a solid color.
        ************************************************************************/
        public static class BitmapBgFillSolid implements BitmapBgFill
        {
            /** The solid color to fill the <code>BitmapBg</code> with. */
            public          int     iFillColor              = 0;

            /************************************************************************
            *   Creates a new filler for the <code>BitmapBg</code>
            *   that fills it with the specified solid color.
            *
            *   @param  aFillColor  The color to fill the <code>BitmapBg</code> with.
            ************************************************************************/
            public BitmapBgFillSolid( int aFillColor )
            {
                iFillColor = aFillColor;
            }
        }

        /************************************************************************
        *   Specifies a filler for the <code>BitmapBg</code> that fills it with a gradient.
        ************************************************************************/
        public static class BitmapBgFillGradient implements BitmapBgFill
        {
            /** The color of the gradient's top. */
            public          int     iGradientTopColor       = 0;

            /** The color of the gradient's bottom. */
            public          int     iGradientBottomColor    = 0;

            /************************************************************************
            *   Creates a new filler for the <code>BitmapBg</code>
            *   that fills it with the specified gradient.
            *
            *   @param  aGradientTopColor       The top color of the gradient to fill the <code>BitmapBg</code> with.
            *   @param  aGradientBottomColor    The bottom color of the gradient to fill the <code>BitmapBg</code> with.
            ************************************************************************/
            public BitmapBgFillGradient( int aGradientTopColor, int aGradientBottomColor )
            {
                iGradientTopColor       = aGradientTopColor;
                iGradientBottomColor    = aGradientBottomColor;
            }
        }

        /************************************************************************
        *   Resizes the given bitmap. If proportions shall be constrained and both,
        *   <code>newWidth</code> and <code>newHeight</code> are set, the width and
        *   height of the resized bitmap will not exceed these values.
        *
        *   @param  bitmap                  The bitmap to resize.
        *   @param  newWidth                The target width to resize to. <b>-1</b> if the width shall be
        *                                   calculated if proportions are constrained.
        *   @param  newHeight               The target height to resize to. <b>-1</b> if the height shall be
        *                                   calculated if proportions are constrained.
        *   @param  constrainProportions    Specifies if proportions shall be constrained.
        *   @return                         The resized bitmap.
        ************************************************************************/
        public static final Bitmap resizeBitmapToSize( Bitmap bitmap, int newWidth, int newHeight, ConstrainProportions constrainProportions )
        {
            return resizeBitmap( bitmap, newWidth, newHeight, constrainProportions );
        }

        /************************************************************************
        *   Resizes the given bitmap to the specified target height by
        *   constraining it's proportions.
        *
        *   @param  bitmap                  The bitmap to resize.
        *   @param  newHeight               The target height to resize to.
        *   @return                         The resized bitmap.
        ************************************************************************/
        public static final Bitmap resizeBitmapToHeight( Bitmap bitmap, int newHeight )
        {
            return resizeBitmap( bitmap, -1, newHeight, ConstrainProportions.EYes );
        }

        /************************************************************************
        *   Resizes the given bitmap to the specified target width by
        *   constraining it's proportions.
        *
        *   @param  bitmap                  The bitmap to resize.
        *   @param  newWidth                The target width to resize to.
        *   @return                         The resized bitmap.
        ************************************************************************/
        public static final Bitmap resizeBitmapToWidth( Bitmap bitmap, int newWidth )
        {
            return resizeBitmap( bitmap, newWidth, -1, ConstrainProportions.EYes );
        }

        /************************************************************************
        *   Resizes the specified bitmap.
        *
        *   @param      originalBitmap  The original bitmap
        *   @param      newWidth        the new maximum width to scale to.
        *   @param      newHeight       the new maximum height to scale to.
        *   @param      keepRatio       specifies if to keep the ratio.
        *                               If true, the bitmap is scaled till is reaches the new
        *                               width or height, keeping its proportions.
        *   @return                     The resized bitmap.
        ************************************************************************/
        private static final Bitmap resizeBitmap( Bitmap originalBitmap, int newWidth, int newHeight, ConstrainProportions keepRatio )
        {
            float scaleRatioX = ((float) newWidth  ) / originalBitmap.getWidth();
            float scaleRatioY = ((float) newHeight ) / originalBitmap.getHeight();

            //check if ratio should be kept
            switch ( keepRatio )
            {
                case EYes:
                {
                    if ( newWidth  == -1 )
                    {
                        scaleRatioX = scaleRatioY;
                    }
                    else if ( newHeight == -1 )
                    {
                        scaleRatioY = scaleRatioX;
                    }
                    else if ( scaleRatioX > scaleRatioY )
                    {
                        scaleRatioX = scaleRatioY;
                    }
                    else if ( scaleRatioY > scaleRatioX )
                    {
                        scaleRatioY = scaleRatioX;
                    }
                    break;
                }

                case ENo:
                {
                    break;
                }
            }

            //scale by scaling factor x and y
            newWidth    = (int)( originalBitmap.getWidth()  * scaleRatioX );
            newHeight   = (int)( originalBitmap.getHeight() * scaleRatioY );

            //resize the bitmap and return as drawable
            Bitmap resizedBitmap = Bitmap.createScaledBitmap( originalBitmap, newWidth, newHeight, true );
            return resizedBitmap;
        }

        /************************************************************************
        *   Creates a BitmapDrawable from the specified image bytes.
        *
        *   @param  context     The current system context.
        *   @param  bytes       The bytes of the image-file.
        *   @return             The created bitmap.
        ************************************************************************/
        public static final BitmapDrawable createBitmapDrawableFromByteArray( Context context, byte[] bytes )
        {
            //using the bitmapFactory saves the vm heap?
            Bitmap bitmap = createBitmapFromByteArray( bytes );

            //throw an exception if the bitmap could not be converted
            if ( bitmap == null ) return null;

            //return as a BitmapDrawable
            return new BitmapDrawable( context.getResources(), bitmap );
        }

        /************************************************************************
        *   Creates a Bitmap from the specified file.
        *
        *   @param  activity    The according activity context.
        *   @param  file        The file to create a bitmap from.
        *   @return             The created bitmap.
        *   @throws Throwable   If an error occurs.
        ************************************************************************/
        public static final Bitmap createBitmapFromFile( Activity activity, File file ) throws Throwable
        {
            return createBitmapFromUri( activity, Uri.fromFile( file ) );
        }

        /************************************************************************
        *   Creates a Bitmap from the specified URI.
        *
        *   @param  activity    The according activity context.
        *   @param  uri         The uri to create the bitmap from.
        *   @return             The created bitmap.
        *   @throws Throwable   If an error occurs.
        ************************************************************************/
        public static final Bitmap createBitmapFromUri( Activity activity, Uri uri ) throws Throwable
        {
            //Media.getBitmap( activity.getContentResolver(), uri );
            Bitmap bitmap = BitmapFactory.decodeStream( activity.getContentResolver().openInputStream( uri ) );
            return bitmap;
        }

        /************************************************************************
        *   Creates a Bitmap from the specified image bytes.
        *
        *   @param  bytes       The bytes of the image-file.
        *   @return             The created bitmap.
        ************************************************************************/
        public static final Bitmap createBitmapFromByteArray( byte[] bytes )
        {
            try
            {
                //using the bitmapFactory saves the vm heap?
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );

                //throw an exception if the bitmap could not be converted
                if ( bitmap == null ) throw new Exception();

                //return as a drawable
                return bitmap;
            }
            catch ( Exception e )
            {
                return null;
            }
        }

        /************************************************************************
        *   Suitable for all sdk levels, this function alters the bitmap's alpha value.
        *
        *   @param  context     The current system context.
        *   @param  bitmap      The bitmap to change the alpha value for.
        *   @param  newAlpha    The new alpha value for the bitmap.
        *   @return             The given bitmap being processed with the given alpha value.
        ************************************************************************/
        public static final BitmapDrawable alterBitmapAlpha( Context context, BitmapDrawable bitmap, Integer newAlpha )
        {
            Bitmap      bmp         = Bitmap.createBitmap( bitmap.getBitmap().getWidth(), bitmap.getBitmap().getHeight(), Bitmap.Config.ARGB_8888 );
            Canvas      bmpCanvas   = new Canvas( bmp );

            //center the alpha bitmap on the canvas
            bmpCanvas.clipRect( 0, 0, bitmap.getBitmap().getWidth(), bitmap.getBitmap().getHeight() );
            LibDrawing.drawBitmap( bmpCanvas, bitmap, 0, 0, newAlpha, LibAnchor.ELeftTop );

            return new BitmapDrawable( context.getResources(), bmp );
        }

        /************************************************************************
        *   Flips the given bitmap vertical and adds an alpha gradient.
        *
        *   @param  context                 The current system context.
        *   @param  bitmapDrawable          The bitmap to modify.
        *   @param  gradientVisibleRatio    The height ratio where the alpha gradient shall start.
        *   @param  alphaTopRow             The alpha-value of the top row.
        *                                   This will be the most opaque row.
        *   @return                         The vertical flipped bitmap containing an alpha gradient.
        ************************************************************************/
        public static final BitmapDrawable createMirroredGradientFadedImage( Context context, BitmapDrawable bitmapDrawable, int gradientVisibleRatio, int alphaTopRow )
        {
            //pick the bitmap
            Bitmap bitmap = bitmapDrawable.getBitmap();

            //read the data from the given img
            int[] imgData  = new int[ bitmap.getWidth() * bitmap.getHeight() ];
            bitmap.getPixels( imgData, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight() );

            //flip the data horizontal
            int[]   flippedImgData  = new int[ bitmap.getWidth() * bitmap.getHeight() ];
            int     k               = 0;
            int     currentRow      = 0;
            //browse all rows of the original image
            for ( int i = imgData.length - bitmap.getWidth(); i >= 0; i -= bitmap.getWidth()  )
            {
                //write the rows flipped
                for ( int j = 0; j < bitmap.getWidth(); ++j )
                {
                    flippedImgData[ k ] = imgData[ i + j ];

                    int currentAlpha  = alphaTopRow - ( alphaTopRow * currentRow / ( bitmap.getHeight() * gradientVisibleRatio / 100 ) );
                    if ( currentAlpha < 0 ) currentAlpha = 0;

                    int oldAlphaValue = ( flippedImgData[ k ] >> 24 ) & 0xff;
                    int newAlphaValue = ( 0xff & currentAlpha ) * oldAlphaValue / 255;

                    //debug.bugfix.out( "new alpha value: [" + newAlphaValue + "]" );
                    flippedImgData[ k ] = ( ( flippedImgData[ k ] & 0x00ffffff ) | ( newAlphaValue << 24 ) );

                    ++k;
                }
                ++currentRow;
            }

            //create and return a new image from the data
            return new BitmapDrawable( context.getResources(), Bitmap.createBitmap( flippedImgData, bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888 ) );
        }

        /************************************************************************
        *   Desaturates the given bitmap.
        *
        *   @param  context The current system context.
        *   @param  bitmap  The bitmap to desaturate.
        *   @return         The given bitmap in monochrome colors.
        ************************************************************************/
        public static final BitmapDrawable desaturate( Context context, BitmapDrawable bitmap )
        {
            Bitmap      bmp         = Bitmap.createBitmap( bitmap.getBitmap().getWidth(), bitmap.getBitmap().getHeight(), Bitmap.Config.ARGB_8888 );
            Canvas      bmpCanvas   = new Canvas( bmp );

            ColorMatrix greymatrix = new ColorMatrix();
            greymatrix.setSaturation( 0.0f );
            ColorFilter filter = new ColorMatrixColorFilter( greymatrix );
            Paint painter = new Paint();
            painter.setColorFilter( filter );

            bmpCanvas.drawBitmap( bitmap.getBitmap(), 0, 0, painter );

            return new BitmapDrawable( context.getResources(), bmp );
        }

        /************************************************************************
        *   Combines the two bitmaps into one bitmap by using the left horizontal half
        *   of the first bitmap and the right horizontal half if the second bitmap.
        *
        *   @param  context The current system context.
        *   @param  img1    The 1st BitmapDrawable.
        *   @param  img2    The 2nd BitmapDrawable.
        *   @return         The combined Bitmap. The left half is picked from the 1st bitmap.
        *                   The right half is picked from the 2nd bitmap.
        ************************************************************************/
        public static final BitmapDrawable createHalfClipped( Context context, BitmapDrawable img1, BitmapDrawable img2 )
        {
            Bitmap      bmp         = Bitmap.createBitmap( img1.getBitmap().getWidth(), img1.getBitmap().getHeight(), Bitmap.Config.ARGB_8888 );
            Canvas      bmpCanvas   = new Canvas( bmp );
            Paint       painter     = new Paint();

            bmpCanvas.clipRect(   0, 0, bmp.getWidth() / 2, bmp.getHeight(), Op.REPLACE );
            bmpCanvas.drawBitmap( img1.getBitmap(), 0, 0, painter );
            bmpCanvas.clipRect(   bmp.getWidth() / 2, 0, bmp.getWidth(), bmp.getHeight(), Op.REPLACE );
            bmpCanvas.drawBitmap( img2.getBitmap(), 0, 0, painter );

            return new BitmapDrawable( context.getResources(), bmp );
        }

        /************************************************************************
        *   Creates a BitmapDrawable with the specified dimensions
        *   and all pixels filled with the specified fill color.
        *
        *   @param  context The current system context.
        *   @param  width   The desired width of the Bitmap.
        *   @param  height  The desired height of the Bitmap.
        *   @param  fillCol The fill color to use for filling all pixels of the generated Bitmap.
        *   @return         A new Bitmap with the specified dimension and fill color.
        ************************************************************************/
        public static final BitmapDrawable createBitmap( Context context, int width, int height, int fillCol )
        {
            Bitmap      bmp         = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
            Canvas      bmpCanvas   = new Canvas( bmp );
            Paint       painter     = new Paint();

            painter.setStyle( Style.FILL );
            painter.setColor( fillCol );

            bmpCanvas.drawRect( 0, 0, width, height, painter );

            return new BitmapDrawable( context.getResources(), bmp );
        }

        /************************************************************************
        *   Centers a Bitmap into another Bitmap and returns the combined Bitmap.
        *
        *   @param  context     The current system context.
        *   @param  base        The base Bitmap to center the other Bitmap in.
        *   @param  toCenter    The Bitmap to horizontal and vertical center in the base Bitmap.
        *   @return             A new Bitmap with the base Bitmap in the background
        *                       and the horizontal and vertical centered Bitmap 'toCenter' in the foreground.
        ************************************************************************/
        public static final BitmapDrawable centerBitmapInBitmap( Context context, Bitmap base, Bitmap toCenter )
        {
            Canvas      bmpCanvas   = new Canvas( base );
            Paint       painter     = new Paint();

            bmpCanvas.drawBitmap( toCenter, ( base.getWidth() - toCenter.getWidth() ) / 2, ( base.getHeight() - toCenter.getHeight() ) / 2, painter );

            return new BitmapDrawable( context.getResources(), base );
        }

        /************************************************************************
        *   Delivers the JPEG-bytes of the specified BitmapDrawable.
        *
        *   @param  quality     The desired JPEG-quality from 0 to 100.
        *   @param  bd          The BitmapDrawable to get the JPEG-bytes from.
        *   @return             The JPEG-bytes from the given BitmapDrawable,
        *                       processes with the specified quality.
        ************************************************************************/
        public static final byte[] getBytesFromBitmapDrawableAsJPEG( int quality, BitmapDrawable bd )
        {
            Bitmap bitmap = bd.getBitmap();
            return getBytesFromBitmapAsJPEG( quality, bitmap );
        }

        /************************************************************************
        *   Delivers the JPEG-bytes of the specified Bitmap.
        *
        *   @param  quality     The desired JPEG-quality from 0 to 100.
        *   @param  bitmap      The Bitmap to get the JPEG-bytes from.
        *   @return             The JPEG-bytes from the given Bitmap,
        *                       processes with the specified quality.
        ************************************************************************/
        public static final byte[] getBytesFromBitmapAsJPEG( int quality, Bitmap bitmap )
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.JPEG, quality, stream );
            byte[] bytes = stream.toByteArray();
            return bytes;
        }

        /************************************************************************
        *   Delivers the PNG24-bytes of the specified BitmapDrawable.
        *
        *   @param  quality     The desired PNG24-quality from 0 to 100.
        *   @param  bd          The BitmapDrawable to get the PNG24-bytes from.
        *   @return             The PNG24-bytes from the given BitmapDrawable,
        *                       processes with the specified quality.
        ************************************************************************/
        public static final byte[] getBytesFromBitmapDrawableAsPNG24( int quality, BitmapDrawable bd )
        {
            Bitmap bitmap = bd.getBitmap();
            return getBytesFromBitmapAsPNG24( quality, bitmap );
        }

        /************************************************************************
        *   Delivers the PNG24-bytes of the specified Bitmap.
        *
        *   @param  quality     The desired PNG24-quality from 0 to 100.
        *   @param  bitmap      The Bitmap to get the PNG24-bytes from.
        *   @return             The PNG24-bytes from the given Bitmap,
        *                       processes with the specified quality.
        ************************************************************************/
        public static final byte[] getBytesFromBitmapAsPNG24( int quality, Bitmap bitmap )
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.PNG, quality, stream );
            byte[] bytes = stream.toByteArray();
            return bytes;
        }

        /************************************************************************
        *   Returns a Bitmap with rounded corners.
        *
        *   @param  bitmap      The Bitmap to round the corners from.
        *   @param  radius      The radius of the rounded corners.
        *   @return             A new bitmap with rounded corners and transparent bg.
        ************************************************************************/
        public static Bitmap getRoundedCornerBitmap( Bitmap bitmap, int radius )
        {
            Bitmap output = Bitmap.createBitmap( bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888 );
            Canvas canvas = new Canvas( output );

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect( 0, 0, bitmap.getWidth(), bitmap.getHeight() );
            final RectF rectF = new RectF( rect );
            final float roundPx = radius;

            paint.setAntiAlias(true);
            canvas.drawARGB( 0, 0, 0, 0 );
            paint.setColor( color );
            canvas.drawRoundRect( rectF, roundPx, roundPx, paint );

            paint.setXfermode( new PorterDuffXfermode( Mode.SRC_IN ) );
            canvas.drawBitmap( bitmap, rect, rect, paint );

            return output;
        }
    }
