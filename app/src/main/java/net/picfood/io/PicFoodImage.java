/*  $Id: PicFoodImage.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.io;

    import  java.io.*;
    import  net.picfood.*;
    import  net.picfood.PicFoodProject.Features;
    import  net.picfood.PicFoodSettings.Images;
    import  net.picfood.data.*;
    import  net.picfood.ui.*;
    import  net.picfood.ui.PicFoodUI.ImageSize;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.graphics.drawable.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;

    /**********************************************************************************************
    *   Manages loading EXTERNAL Bitmaps.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/io/PicFoodImage.java $"
    **********************************************************************************************/
    public abstract class PicFoodImage
    {
        /** The cached calculated size for a detailed image. */
        private     static          Integer             sizeDetailedImage                       = null;

        /** The cached calculated size for a tiled image. */
        private     static          Integer             sizeTiledImage                          = null;

        /** The cached calculated size for an icon. */
        private     static          Integer             sizeIcon                                = null;

        /** The displayed size for an image. */
        private     static          Integer             displayedSizeDetailedImage              = null;

        /*****************************************************************************
        *   Orders one image in a background thread.
        *   When loaded, it will be assigned to the specified ImageView.
        *
        *   @param  state                   The according state.
        *   @param  url                     The base URL for this image to order.
        *                                   This URL contains the {size} token.
        *   @param  imageSize               One of the image formats that are used in this project.
        *   @param  accordingAdapterData    The according data that shall be informed when this image is loaded.
        *****************************************************************************/
        public static final void orderImageThreaded
        (
            final   LibState            state,
            final   String              url,
            final   ImageSize           imageSize,
            final   PicFoodData         accordingAdapterData
        )
        {
            //only perform the order if the image-url is set
            if ( url != null && !LibString.isEmpty( url ) )
            {
                PicFoodDebug.imageOrder.out( "Ordering image for base url [" + url + "]" );

                //check if this ImageView already has a loaded image
                {
                    //create a thread for each request
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                //replace size in url
                                        int     imageSizeInPixel    = PicFoodImage.getImageSizeToOrder( state.getActivity(), imageSize );

                                final   String  MAGIC_POINT         = ".";
                                final   String  MAGIC_X             = "x";

                                        String  urlReplaced         = url.replace( "{size}", MAGIC_POINT + imageSizeInPixel + MAGIC_X + imageSizeInPixel );

                                //order this image
                                PicFoodDebug.imageOrder.out( " Streaming image from url [" + urlReplaced + "]" );

                                //stream or cache BitmapDrawable
                                BitmapDrawable  bd  = PicFoodCache.getBitmapDrawableFromHttpOrCache( state.getActivity(), urlReplaced );

                                //cut corners for images ?!
                                if ( Features.CUT_CORNERS_FOR_IMAGES )
                                {
                                    bd = PicFoodImage.cutCorners( state.getActivity(), bd, imageSize );
                                }

                                PicFoodDebug.imageOrder.out( " Streamed image is [" + bd + "]" );

                                //callback if successful
                                if ( bd != null && bd.getBitmap() != null )
                                {
                                    //PicFoodDebug.imageOrder.out( "Received image [" + urlReplaced + "], desired [" + imageSizeInPixel + "] recveived [" + bd.getBitmap().getWidth() + "]x[" + bd.getBitmap().getHeight() + "]" );

                                    PicFoodDebug.imageOrder.out( " Assigning streamed image.." );
                                    PicFoodDebug.imageOrder.out( "" );

                                    //assign loaded image
                                    accordingAdapterData.assignLoadedImage( state, imageSize, bd );
                                }
                            }
                            catch ( Throwable t )
                            {
                                //ignore Throwable
                                PicFoodDebug.imageOrder.out( " Ordering image threw an EXCEPTION [" + t.getClass() + "]" );
                                PicFoodDebug.imageOrder.trace( t );
                            }
                        }
                    }.start();
                }
            }
        }

        /*****************************************************************************
        *   Delivers the size for displaying detailed images.
        *
        *   @param  activity    The according activity context.
        *   @return             The size in which detailed images shall be displayed on the screen.
        *****************************************************************************/
        public static final int getDisplayedDetailedImageSize( Activity activity )
        {
            //init lazy
            if ( displayedSizeDetailedImage == null )
            {
                //get screen width and substract horz borders
                int smallerScreenDimension = LibUI.getSmallerPaintableScreenDimension( activity );
                smallerScreenDimension -= 4 * LibResource.getResourceDimensionInPixel( activity, R.dimen.content_distance_normal );

                //assign cached value
                displayedSizeDetailedImage = Integer.valueOf( smallerScreenDimension );
            }

            return displayedSizeDetailedImage.intValue();
        }

        /*****************************************************************************
        *   Delivers the size of the specified SQUARE image to order.
        *
        *   @param  context     The current system context.
        *   @param  imageSize   One of the image formats that are used in this project.
        *   @return             The dimension of the width or height
        *                       for the desired image size.
        *****************************************************************************/
        public static final int getImageSizeToOrder( Context context, PicFoodUI.ImageSize imageSize )
        {
            switch ( imageSize )
            {
                case EDetailedImage:
                {
                    //init lazy
                    if ( sizeDetailedImage == null )
                    {
                        //get screen width and substract horz borders
                        int smallerScreenDimension = LibUI.getSmallerPaintableScreenDimension( context );
                        smallerScreenDimension -= 4 * LibResource.getResourceDimensionInPixel( context, R.dimen.content_distance_normal );

                        //clip to maximum icon size
                        if ( smallerScreenDimension > Images.MAX_IMAGE_SIZE ) smallerScreenDimension = Images.MAX_IMAGE_SIZE;

                        //cache value
                        sizeDetailedImage = Integer.valueOf( smallerScreenDimension );
                    }
                    return sizeDetailedImage.intValue();
                }

                case EIcon:
                {
                    //init lazy
                    if ( sizeIcon == null )
                    {
                        int iconSize = LibResource.getResourceDimensionInPixel( context, R.dimen.icon_size );

                        //clip to maximum icon size
                        if ( iconSize > Images.MAX_ICON_SIZE ) iconSize = Images.MAX_ICON_SIZE;

                        //cache value
                        sizeIcon = Integer.valueOf( iconSize );
                    }

                    return sizeIcon.intValue();
                }

                case ETiledImage:
                {
                    //init lazy
                    if ( sizeTiledImage == null )
                    {
                        PicFoodDebug.tiledColumnWidth.out( "Initializing tiled image size - All sizes are in px" );

                        //get screen width and substract horz borders
                        int availableWidth = LibUI.getSmallerPaintableScreenDimension( context );
                        availableWidth -= 2 * LibResource.getResourceDimensionInPixel( context, R.dimen.content_distance_normal );

                        PicFoodDebug.tiledColumnWidth.out( " Available screen width: [" + availableWidth + "]" );

                        //pick minimum column width
                        int minTiledImageSize = LibResource.getResourceDimensionInPixel( context, R.dimen.explore_image_size_min );
                        //clip to maximum tiled image size
                        if ( minTiledImageSize > Images.MAX_IMAGE_SIZE ) minTiledImageSize = Images.MAX_IMAGE_SIZE;

                        PicFoodDebug.tiledColumnWidth.out( " Minimum tile width: [" + minTiledImageSize + "]" );

                        //specify the number of columns
                        int numberOfColumns = ( availableWidth / ( minTiledImageSize + LibResource.getResourceDimensionInPixel( context, R.dimen.content_distance_normal ) ) );
                        if ( numberOfColumns < 1 ) numberOfColumns = 1;
                        PicFoodDebug.tiledColumnWidth.out( " Number of columns: [" + numberOfColumns + "]" );

                        //get space to distribute ( screen width substracting all horizontal distances between columns )
                        int spaceToDistribute = ( availableWidth - ( ( numberOfColumns - 1 ) * LibResource.getResourceDimensionInPixel( context, R.dimen.content_distance_normal ) ) );
                        PicFoodDebug.tiledColumnWidth.out( " Space to distribute: [" + spaceToDistribute + "]" );

                        //pick actual column width
                        int finalColumnWidth = spaceToDistribute / numberOfColumns;
                        PicFoodDebug.tiledColumnWidth.out( " Final column width: [" + finalColumnWidth + "]" );

                        //cache value
                        sizeTiledImage = Integer.valueOf( finalColumnWidth );
                    }
                    return sizeTiledImage.intValue();
                }
            }

            return 0;
        }

        /**********************************************************************************************
        *   Resizes the specified image file to a maximum size and overwrites it afterwards.
        *   Performs nothing if the given image file doesn't exceed this size.
        *
        *   @param  activity        The according activity context.
        *   @param  maxImageSize    The maximum image size that the image file may have.
        *                           The file will be scaled down to this size if it has a higher size.
        *   @param  file            The image file.
        *   @throws Throwable       If an Exception occured.
        **********************************************************************************************/
        public static final void resizeImageToMaxSize( Activity activity, int maxImageSize, File file ) throws Throwable
        {
            //pick bitmap from cropped url and assigned as last-cropped-bitmap
            Bitmap lastCroppedBitmap = LibImage.createBitmapFromFile( activity, file );
            //lastCroppedBitmap = (Bitmap)data.getExtras().get( "data" );

            PicFoodDebug.resizeImage.out( "  >>> PICKED cropped bitmap, w: [" + lastCroppedBitmap.getWidth() + "] h: [" + lastCroppedBitmap.getHeight() + "]" );

            //resize bitmap if it exceeds the MaxSize
            if ( lastCroppedBitmap.getWidth() > maxImageSize )
            {
                //resize
                lastCroppedBitmap = LibImage.resizeBitmapToWidth( lastCroppedBitmap, maxImageSize );
                PicFoodDebug.resizeImage.out( "  >>> RESIZED cropped bitmap, w: [" + lastCroppedBitmap.getWidth() + "] h: [" + lastCroppedBitmap.getHeight() + "]" );

                //save again
                byte[] bytes = LibImage.getBytesFromBitmapAsJPEG( PicFoodSettings.Image.DEFAULT_JPEG_QUALITY, lastCroppedBitmap );
                LibIO.writeFile( file, bytes );
            }

            //recycle this bitmap
            lastCroppedBitmap.recycle();
        }

        /**********************************************************************************************
        *   Cuts the corners off from a specified Bitmap with a specified imageSize.
        *   The new Bitmap is returned afterwards.
        *
        *   @param  activity        The according activity context.
        *   @param  bd              The BitmapDrawable holding the Bitmap to cut corners for.
        *   @param  imageSize       The according imageSize for this image.
        *                           Tiled images will not be cut.
        *                           Icons get a small corner radius.
        *                           Detailed images get a big corner radius.
        *   @return                 The cut Bitmap as a BitmapDrawable.
        **********************************************************************************************/
        public static final BitmapDrawable cutCorners( Activity activity, BitmapDrawable bd, PicFoodUI.ImageSize imageSize )
        {
            switch ( imageSize )
            {
                case ETiledImage:
                {
                    return bd;
                }

                case EIcon:
                {
                    return new BitmapDrawable
                    (
                        activity.getResources(),
                        LibImage.getRoundedCornerBitmap( bd.getBitmap(), LibResource.getResourceDimensionInPixel( activity, R.dimen.corner_radius_normal ) )
                    );
                }

                case EDetailedImage:
                {
                    return new BitmapDrawable
                    (
                        activity.getResources(),
                        LibImage.getRoundedCornerBitmap( bd.getBitmap(), LibResource.getResourceDimensionInPixel( activity, R.dimen.corner_radius_big ) )
                    );
                }
            }

            return bd;
        }
    }
