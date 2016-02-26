
    package de.mayflower.lib.ui;

    import  android.graphics.*;
    import  android.graphics.Paint.Style;
    import  android.graphics.Path.FillType;
    import  android.graphics.drawable.*;
    import  de.mayflower.lib.util.*;

    /************************************************************************
    *   Offers independent drawing operations on a canvas.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class LibDrawing
    {
        /****************************************************************************
        *   All nine available anchors for drawable elements.
        ****************************************************************************/
        public static enum LibAnchor
        {
            /** The anchor is located on the element's left top corner. */
            ELeftTop,

            /** The anchor is located on the element's vertical middle of the left edge. */
            ELeftMiddle,

            /** The anchor is located on the element's left bottom corner. */
            ELeftBottom,

            /** The anchor is located on the element's horizontal center of the top edge. */
            ECenterTop,

            /** The anchor is located on the element's horizontal center and vertical middle. */
            ECenterMiddle,

            /** The anchor is located on the element's horizontal center of the bottom edge. */
            ECenterBottom,

            /** The anchor is located on the element's right top corner. */
            ERightTop,

            /** The anchor is located on the element's vertical middle of the right edge. */
            ERightMiddle,

            /** The anchor is located on the element's right bottom corner. */
            ERightBottom,

            ;

            /************************************************************************
            *   Returns the horizontal text offset for this anchor,
            *   being assigned on the specified text and font.
            *
            *   @param  str     The String to get the horizontal offset for
            *                   on assigning this anchor.
            *   @param  fnt     The font to use for drawing the given text.
            *   @return         The horizontal offset in px.
            ************************************************************************/
            public int getOffsetX( String str, LibFont fnt )
            {
                switch ( this )
                {
                    case ELeftTop:
                    case ELeftMiddle:
                    case ELeftBottom:   return 0;
                    case ECenterTop:
                    case ECenterMiddle:
                    case ECenterBottom: return ( fnt.getStringWidth( str ) / 2 );
                    case ERightTop:
                    case ERightMiddle:
                    case ERightBottom:  return ( fnt.getStringWidth( str ) );
                }
                return 0;
            }

            /************************************************************************
            *   Returns the vertical text offset for this anchor,
            *   being assigned on the specified font.
            *
            *   @param  fnt     The font to use for drawing the given text.
            *   @return         The vertical offset in px.
            ************************************************************************/
            public int getOffsetY( LibFont fnt )
            {
                switch ( this )
                {
                    case ELeftTop:
                    case ECenterTop:
                    case ERightTop:     return 0;
                    case ELeftMiddle:
                    case ECenterMiddle:
                    case ERightMiddle:  return ( fnt.getHeight() / 2 );
                    case ELeftBottom:
                    case ECenterBottom:
                    case ERightBottom:  return ( fnt.getHeight() );
                }
                return 0;
            }

            /************************************************************************
            *   Returns the horizontal offset for this anchor,
            *   being assigned on the specified bitmap to draw.
            *
            *   @param  bd      The BitmapDrawable to get the horizontal anchor offset for.
            *   @return         The horizontal offset in px.
            ************************************************************************/
            public int getOffsetX( BitmapDrawable bd )
            {
                return getOffsetX( bd.getBitmap().getWidth() );
            }

            /************************************************************************
            *   Returns the horizontal offset for this anchor,
            *   being assigned on the specified width.
            *
            *   @param  width   The width to get the horizontal anchor offset for.
            *   @return         The horizontal offset in px.
            ************************************************************************/
            public int getOffsetX( int width )
            {
                switch ( this )
                {
                    case ELeftTop:
                    case ELeftMiddle:
                    case ELeftBottom:   return 0;
                    case ECenterTop:
                    case ECenterMiddle:
                    case ECenterBottom: return ( width / 2 );
                    case ERightTop:
                    case ERightMiddle:
                    case ERightBottom:  return width;
                }
                return 0;
            }

            /************************************************************************
            *   Returns the vertical offset for this anchor,
            *   being assigned on the specified bitmap to draw.
            *
            *   @param  bd      The BitmapDrawable to get the vertical anchor offset for.
            *   @return         The vertical offset in px.
            ************************************************************************/
            public int getOffsetY( BitmapDrawable bd )
            {
                return getOffsetY( bd.getBitmap().getHeight() );
            }

            /************************************************************************
            *   Returns the vertical offset for this anchor,
            *   being assigned on the specified height.
            *
            *   @param  height  The height to get the vertical anchor offset for.
            *   @return         The vertical offset in px.
            ************************************************************************/
            public int getOffsetY( int height )
            {
                switch ( this )
                {
                    case ELeftTop:
                    case ECenterTop:
                    case ERightTop:     return 0;
                    case ELeftMiddle:
                    case ECenterMiddle:
                    case ERightMiddle:  return ( height / 2 );
                    case ELeftBottom:
                    case ECenterBottom:
                    case ERightBottom:  return height;
                }
                return 0;
            }
        }

        /****************************************************************************
        *   Fills a rect with a specified color.
        *   THe pike point is not handy as parameters - change it to the center?
        *
        *   @param  gc                  The Context to draw onto.
        *   @param  p                   The painter instance to draw with.
        *   @param  pa                  A path to use for this drawing operation.
        *                               The path does not have to be empty.
        *   @param  triangleDirection   The direction of the triangle to point to.
        *   @param  x                   The triangle's pike point x.
        *   @param  y                   The triangle's pike point y.
        *   @param  width               Destination rect's width.
        *   @param  height              Destination rect's height.
        *   @param  color               The color to fill the rect with.
        *   @param  lg                  The linear gradient to fill the rect with or <code>null</code>
        *                               if the rect shall be filled with a color.
        *   @param  flexedBackside      Specifies if the backside shall be flexed.
        *                               Will be drawed straight if <code>false</code>.
        ****************************************************************************/
        public static void fillTriangle( Canvas gc, Paint p, Path pa, LibUI.LibDirection triangleDirection, int x, int y, int width, int height, int color, LinearGradient lg, boolean flexedBackside )
        {
            switch ( triangleDirection )
            {
                case EWest:
                {
                    pa.rewind();

                    pa.moveTo( x, y );
                    pa.lineTo( x + width, y - height / 2 );
                    if ( flexedBackside ) pa.lineTo( x + width / 2, y );
                    pa.lineTo( x + width, y + height / 2 );
                    pa.lineTo( x, y );
                    break;
                }

                case EEast:
                {
                    pa.rewind();

                    pa.moveTo( x, y );
                    pa.lineTo( x - width, y - height / 2 );
                    if ( flexedBackside ) pa.lineTo( x - width / 2, y );
                    pa.lineTo( x - width, y + height / 2 );
                    pa.lineTo( x, y );
                    break;
                }

                case ENorth:
                {
                    pa.rewind();

                    pa.moveTo( x, y );
                    pa.lineTo( x - width / 2, y + height );
                    if ( flexedBackside ) pa.lineTo( x, y + height / 2 );
                    pa.lineTo( x + width / 2, y + height );
                    pa.lineTo( x, y );
                    break;
                }

                case ESouth:
                {
                    pa.rewind();

                    pa.moveTo( x, y );
                    pa.lineTo( x - width / 2, y - height );
                    if ( flexedBackside ) pa.lineTo( x, y - height / 2 );
                    pa.lineTo( x + width / 2, y - height );
                    pa.lineTo( x, y );
                    break;
                }
            }

            //draw path
            if ( lg != null )
            {
                fillPath( gc, p, pa, lg );
            }
            else
            {
                fillPath( gc, p, pa, color );
            }
        }

        /****************************************************************************
        *   Fills the specified path.
        *
        *   @param  gc      The graphics context to draw onto.
        *   @param  p       The painter instance to draw with.
        *   @param  pa      The path to fill.
        *   @param  color   The color to fill the path with.
        ****************************************************************************/
        public static void fillPath( Canvas gc, Paint p, Path pa, int color )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     color       );
            p.setAntiAlias( true        );

            gc.drawPath( pa, p );
        }

        /****************************************************************************
        *   Fills the specified path.
        *
        *   @param  gc  The graphics context to draw onto.
        *   @param  p   The painter instance to draw with.
        *   @param  pa  The path to fill.
        *   @param  lg  The linear gradient to fill the path with.
        ****************************************************************************/
        public static void fillPath( Canvas gc, Paint p, Path pa, LinearGradient lg )
        {
            p.setStyle(     Style.FILL  );
            p.setShader(    lg          );
            p.setAntiAlias( true        );
            gc.drawPath(    pa, p       );
            p.setShader(    null        );
        }

        /****************************************************************************
        *   Fills a rect with a specified color.
        *
        *   @param  gc      The graphics context to draw onto.
        *   @param  p       The painter instance to draw with.
        *   @param  x       destination x to draw to.
        *   @param  y       destination y to draw to.
        *   @param  width   destination rect's width.
        *   @param  height  destination rect's height.
        *   @param  color   The color to fill the rect with.
        ****************************************************************************/
        public static void fillRect( Canvas gc, Paint p, int x, int y, int width, int height, int color )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     color       );
            p.setAntiAlias( false       );

            gc.drawRect( x, y, x + width, y + height, p );
        }

        /****************************************************************************
        *   Fills a rect with a specified color.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  p           The painter instance to draw with.
        *   @param  x           Destination x to draw to.
        *   @param  y           Destination y to draw to.
        *   @param  width       Destination rect's width.
        *   @param  height      Destination rect's height.
        *   @param  colorBorder The color to draw the rect's border with.
        *   @param  colorFill   The color to fill the rect with.
        ****************************************************************************/
        public static void fillBorderedRect( Canvas gc, Paint p, int x, int y, int width, int height, int colorBorder, int colorFill )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     colorBorder );
            p.setAntiAlias( false       );

            gc.drawRect(    x, y, x + width, y + height, p );

            p.setColor(     colorFill   );
            gc.drawRect(    x + 1, y + 1, x + width - 1, y + height - 1, p );
        }

        /****************************************************************************
        *   Draws the border of a rectangle without filling it.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  p           The painter instance to draw with.
        *   @param  x           Destination x to draw to.
        *   @param  y           Destination y to draw to.
        *   @param  width       Destination rect's width.
        *   @param  height      Destination rect's height.
        *   @param  colorBorder The color to draw the rect's border with.
        ****************************************************************************/
        public static void drawRect( Canvas gc, Paint p, int x, int y, int width, int height, int colorBorder )
        {
            p.setStyle(         Style.STROKE    );
            p.setColor(         colorBorder     );
            p.setAntiAlias(     false           );

            gc.drawRect( x, y, x + width - 1, y + height - 1, p );
        }

        /****************************************************************************
        *   Fills a rect with a vertical color-gradient.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  x           destination x to draw to.
        *   @param  y           destination y to draw to.
        *   @param  width       destination rect's width.
        *   @param  height      destination rect's height.
        *   @param  gd          The gradient to use.
        ****************************************************************************/
        public static void fillGradientRect( Canvas gc, int x, int y, int width, int height, GradientDrawable gd )
        {
            gd.setBounds( x, y, x + width, y + height );
            gd.setSize( width, height );
            gd.draw( gc );
        }

        /****************************************************************************
        *   Fills a rect with rounded corners.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  p           The painter instance to draw with.
        *   @param  x           Destination x to draw to.
        *   @param  y           Destination y to draw to.
        *   @param  width       Destination rect's width.
        *   @param  height      Destination rect's height.
        *   @param  radius      The radius for the round corners.
        *   @param  col         The color to fill this rect with.
        ****************************************************************************/
        public static final void fillRoundRect( Canvas gc, Paint p, int x, int y, int width, int height, float radius, int col )
        {
            fillRoundRect( gc, p, x, y, width, height, radius, col, null );
        }

        /****************************************************************************
        *   Fills a translucent rect with rounded corners.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  p           The painter instance to draw with.
        *   @param  x           Destination x to draw to.
        *   @param  y           Destination y to draw to.
        *   @param  width       Destination rect's width.
        *   @param  height      Destination rect's height.
        *   @param  radius      The radius for the round corners.
        *   @param  col         The color to fill this rect with.
        *   @param  alpha       The alpha value for the color to draw.
        *                       <code>null</code> if the image shall be drawn opaque.
        ****************************************************************************/
        public static final void fillRoundRect( Canvas gc, Paint p, int x, int y, int width, int height, float radius, int col, Integer alpha )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     col         );
            p.setAntiAlias( true        );

            //set alpha if desired
            if ( alpha != null )
            {
                p.setAlpha( alpha.intValue() );
            }

            //draw rounded rect
            gc.drawRoundRect( new RectF( x, y, x + width, y + height ), radius, radius, p );
            p.setAlpha( 255 );
        }

        /****************************************************************************
        *   Fills a rect with rounded corners with a gradient.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  x           Destination x to draw to.
        *   @param  y           Destination y to draw to.
        *   @param  width       Destination rect's width.
        *   @param  height      Destination rect's height.
        *   @param  radius      The radius for the round corners.
        *   @param  gd          The gradient used to fill the rect.
        ****************************************************************************/
        public static final void fillGradientRoundRect( Canvas gc, int x, int y, int width, int height, float radius, GradientDrawable gd )
        {
            gd.setBounds( x, y, x + width, y + height );
            gd.setCornerRadius( radius );
            gd.setSize( width, height );
            gd.draw( gc );
        }

        /****************************************************************************
        *   Fills an ellipse.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  p           The painter instance to draw with.
        *   @param  x           The left edge of the ellipse's bounding rect.
        *   @param  y           The top edge of the ellipse's bounding rect.
        *   @param  width       The width of the ellipse's bounding rect.
        *   @param  height      The height of the ellipse's bounding rect.
        *   @param  col         The color to fill the ellipse with.
        ****************************************************************************/
        public static final void fillEllipse( Canvas gc, Paint p, int x, int y, int width, int height, int col )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     col         );
            p.setAntiAlias( true        );

            //draw ellipse
            gc.drawArc( new RectF( x, y, x + width, y + height ), 0, 360, true, p );
        }

        /****************************************************************************
        *   Fills a sector of an ellipse.
        *
        *   @param  gc          The Context to draw onto.
        *   @param  p           The painter instance to draw with.
        *   @param  x           The left edge of the ellipse's bounding rect.
        *   @param  y           The top edge of the ellipse's bounding rect.
        *   @param  width       The width of the ellipse's bounding rect.
        *   @param  height      The height of the ellipse's bounding rect.
        *   @param  col         The color to fill the ellipse with.
        *   @param  alpha       The alpha value to draw this object with.
        *   @param  angleStart  The start angle to draw the arc.
        *   @param  angleSize   The angle size for this arc to draw.
        ****************************************************************************/
        public static final void fillArk( Canvas gc, Paint p, int x, int y, int width, int height, int col, int alpha, float angleStart, float angleSize )
        {
            p.setStyle(     Style.FILL              );
            p.setColor(     col                     );
            p.setAntiAlias( true                    );
            p.setAlpha(     alpha                   );
          //p.setFlags(     Paint.ANTI_ALIAS_FLAG   );  // no effect ?

            //draw ellipse sector
            gc.drawArc( new RectF( x, y, x + width, y + height ), angleStart, angleSize, true, p  );
        }

        /****************************************************************************
        *   Draws a line.
        *
        *   @param  gc      The Context to draw onto.
        *   @param  p       The painter instance to draw with.
        *   @param  x1      line's source point x.
        *   @param  y1      line's source point y.
        *   @param  x2      line's destination point x.
        *   @param  y2      line's destination point y.
        *   @param  col     line's color.
        ****************************************************************************/
        public static void drawLine( Canvas gc, Paint p, int x1, int y1, int x2, int y2, int col )
        {
            p.setStyle(     Style.FILL  );
            p.setColor(     col         );
            p.setAntiAlias( false       );

            gc.drawLine( x1, y1, x2, y2, p );
        }

        /************************************************************************
        *   Draws a bitmap.
        *
        *   @param      gc      The context to draw onto.
        *   @param      img     The img to draw.
        *   @param      x       The destination x.
        *   @param      y       The destination y.
        *   @param      ank     The anchor for this drawing operation.
        ************************************************************************/
        public static void drawBitmap( Canvas gc, BitmapDrawable img, int x, int y, LibAnchor ank )
        {
            drawBitmap( gc, img, x, y, null, ank );
        }

        /************************************************************************
        *   Draws a translucent bitmap.
        *
        *   @param      gc      The context to draw onto.
        *   @param      img     The img to draw.
        *   @param      x       The destination x.
        *   @param      y       The destination y.
        *   @param      alpha   The alpha-value to draw the image with.
        *                       Use a range from 0 to 255 ( 0x00 till 0xff ).
        *                       <code>null</code> if the image shall be drawn opaque.
        *   @param      ank     The anchor for this drawing operation.
        ************************************************************************/
        public static void drawBitmap( Canvas gc, BitmapDrawable img, int x, int y, Integer alpha, LibAnchor ank )
        {
            //only draw if this drawable is not null and if it's bitmap is not recycled !
            if ( img != null && img.getBitmap() != null && !img.getBitmap().isRecycled() )
            {
                //translate x-point according to anchor
                if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ELeftMiddle   || ank == LibAnchor.ELeftBottom   )  x -= 0;
                if ( ank == LibAnchor.ECenterTop  || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ECenterBottom )  x -= img.getBitmap().getWidth() / 2;
                if ( ank == LibAnchor.ERightTop   || ank == LibAnchor.ERightMiddle  || ank == LibAnchor.ERightBottom  )  x -= img.getBitmap().getWidth();
                //translate y-point according to anchor
                if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ECenterTop    || ank == LibAnchor.ERightTop     )  y -= 0;
                if ( ank == LibAnchor.ELeftMiddle || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ERightMiddle  )  y -= img.getBitmap().getHeight() / 2;
                if ( ank == LibAnchor.ELeftBottom || ank == LibAnchor.ECenterBottom || ank == LibAnchor.ERightBottom  )  y -= img.getBitmap().getHeight();

                //draw the image
                img.setBounds( x, y, x + img.getBitmap().getWidth(), y + img.getBitmap().getHeight() );
                if ( alpha != null ) img.setAlpha( alpha.intValue() );
                img.draw( gc );
            }
        }

        /************************************************************************
        *   Draws a String.
        *
        *   @param  gc      The context to draw onto.
        *   @param  p       The painter instance to draw with.
        *   @param  str     The String to draw.
        *   @param  fnt     The font to draw the String with.
        *   @param  col     The color to draw the String with.
        *   @param  x       The destination x.
        *   @param  y       The destination y.
        *   @param  ank     The anchor for this drawing operation.
        ************************************************************************/
        public static void drawString( Canvas gc, Paint p, String str, LibFont fnt, int col, int x, int y, LibAnchor ank )
        {
            int drawX        = x;
            int drawY        = y;
            int stringWidth  = fnt.getStringWidth( str  );
            int stringHeight = fnt.getHeight();

            //translate x-point according to anchor
            if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ELeftMiddle   || ank == LibAnchor.ELeftBottom   )  drawX -= 0;
            if ( ank == LibAnchor.ECenterTop  || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ECenterBottom )  drawX -= stringWidth / 2;
            if ( ank == LibAnchor.ERightTop   || ank == LibAnchor.ERightMiddle  || ank == LibAnchor.ERightBottom  )  drawX -= stringWidth;

            //translate y-point according to anchor
            if ( ank == LibAnchor.ELeftTop    || ank == LibAnchor.ECenterTop    || ank == LibAnchor.ERightTop     )  drawY += fnt.getAscend();
            if ( ank == LibAnchor.ELeftMiddle || ank == LibAnchor.ECenterMiddle || ank == LibAnchor.ERightMiddle  )  drawY = drawY - fnt.getDescend() + stringHeight / 2;
            if ( ank == LibAnchor.ELeftBottom || ank == LibAnchor.ECenterBottom || ank == LibAnchor.ERightBottom  )  drawY -= fnt.getDescend();

            //set color, font, antialiasing and draw ( symbian draws all strings anchored LEFT|BOTTOM at the baseline ! )
            p.setStyle(     Style.FILL              );
            p.setColor(     col                     );
            p.setAntiAlias( true                    );

            p.setTypeface(  fnt.getTypeface()       );
            p.setTextSize(  fnt.getTextSize()       );

            gc.drawText( str, drawX, drawY, p );
/*
            //draw anchor-crosshair if desired
            if ( AppsDebug.DEBUG_DRAW_STRING_ANCHOR_CROSSHAIR )
            {
                //save current canvas
                int canvasSave = gc.save();

                //release clipping
                gc.clipRect( Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Region.Op.REPLACE );

                p.setColor( ColorDebug.EStringAnchorCrosshair );
                gc.drawLine( x - 2, y,     x + 3, y,     p );
                gc.drawLine( x,     y - 2, x,     y + 3, p );

                //restore saved canvas
                gc.restoreToCount( canvasSave );
            }
*/
        }

        /************************************************************************
        *   Draws a String with a shadow.
        *
        *   @param  gc          The context to draw onto.
        *   @param  p           The painter instance to draw with.
        *   @param  str         The String to draw.
        *   @param  fnt         The font to draw the String with.
        *   @param  col         The color to draw the String with.
        *   @param  colShadow   The color to draw the shadow with.
        *   @param  x           The destination x.
        *   @param  y           The destination y.
        *   @param  ank         The anchor for this drawing operation.
        ************************************************************************/
        public static void drawShadedString( Canvas gc, Paint p, String str, LibFont fnt, int col, int colShadow, int x, int y, LibAnchor ank )
        {
            drawString( gc, p, str, fnt, colShadow, x + 1, y + 1, ank );
            drawString( gc, p, str, fnt, col,       x,     y,     ank );
        }

        /************************************************************************
        *   Creates the path for a star with five spikes.
        *
        *   @param  outerStarRadius     The outer radius of the star's corner points.
        *   @param  innerStarRadius     The inner radius of the star's corner points.
        *   @return                     The path for the star. The point 0|0 of the path
        *                               represents the star's central point.
        ************************************************************************/
        public static final Path createStarPath( int outerStarRadius, int innerStarRadius )
        {
            Path pa = new Path();
            pa.setFillType( FillType.EVEN_ODD );
            for ( int angle = -90; angle < 270; angle += 72 )
            {
                Point outerPoint = LibMath.createRotatedPoint( angle, outerStarRadius );
                if ( angle == -90 )
                {
                    pa.setLastPoint( outerPoint.x, outerPoint.y );
                }
                else
                {
                    pa.lineTo( outerPoint.x, outerPoint.y );
                }
                Point innerPoint = LibMath.createRotatedPoint( angle + 36, innerStarRadius );
                pa.lineTo( innerPoint.x, innerPoint.y );
            }
            return pa;
        }

        /************************************************************************
        *   Draws a boxed string.
        *
        *   @param  gc          The graphics context to draw onto.
        *   @param  p           The painter instance used for drawing operations.
        *   @param  x           x location if the rect to draw.
        *   @param  y           y location if the rect to draw.
        *   @param  width       The width for the rect to draw.
        *   @param  height      The height for the rect to draw.
        *   @param  colBg       The fill color for the rect.
        *   @param  string      The string to center in the rect.
        *   @param  font        The font to draw the string with.
        *   @param  colFontFg   The fg color of the string.
        *   @param  colFontBg   The shadow color of the string.
        ************************************************************************/
        public static final void drawBoxedShadedString( Canvas gc, Paint p, int x, int y, int width, int height, int colBg, String string, LibFont font, int colFontFg, int colFontBg )
        {
            LibDrawing.fillRect( gc, p, x, y, width, height, colBg );
            LibDrawing.drawShadedString( gc, p, string, font, colFontFg, colFontBg, x + width / 2, y + height / 2, LibAnchor.ECenterMiddle );
        }
/*
        public static final GradientDrawable getBgGradient()
        {
          //GradientDrawable ret = new GradientDrawable( Orientation.BL_TR, new int[] { ProjectColors.EBgScreenGradientLB, ProjectColors.EBgScreenGradientRT, } );
            GradientDrawable ret = new GradientDrawable( Orientation.LEFT_RIGHT, new int[] { ProjectColors.EBgScreenGradientLB, ProjectColors.EBgScreenGradientRT, } );
          //ret.setCornerRadius( 5.0f );
          //if ( false ) gd.setGradientCenter( 10, AppsNativeUI.CANVAS_HEIGHT - 10 );

            return ret;
        }
*/
    }
