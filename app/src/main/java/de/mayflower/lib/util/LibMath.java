
    package de.mayflower.lib.util;

    import  android.graphics.*;

    /**************************************************************************************
    *   Extends arithmetic utility functions.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50257 $ $Date: 2013-07-30 12:57:31 +0200 (Di, 30 Jul 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/util/LibMath.java $"
    **************************************************************************************/
    public abstract class LibMath
    {
        /**************************************************************************************
        *   Calculates points for circles. Returns the translation for a point with x=0 and y=0
        *   for the specified angle and distance.
        *
        *   @param  angle       The angle for the null-point to translate.
        *   @param  distance    The distance for the null-point to translate.
        *   @return             The translated null-point.
        **************************************************************************************/
        public static final Point createRotatedPoint( int angle, int distance )
        {
            return createRotatedPoint( angle, distance, distance );
        }

        /**************************************************************************************
        *   Calculates points for ovals. Returns the translation for a point with x=0 and y=0
        *   for the specified angle and distances.
        *
        *   @param  angle       The angle for the null-point to translate.
        *   @param  distanceX   The x-distance for the null-point to translate.
        *   @param  distanceY   The y-distance for the null-point to translate.
        *   @return             The translated null-point.
        **************************************************************************************/
        public static final Point createRotatedPoint( int angle, int distanceX, int distanceY )
        {
            return new Point
            (
                (int)( cos( angle ) * distanceX ),
                (int)( sin( angle ) * distanceY )
            );
        }

        /**************************************************************************************
        *   Returns a random integer in the specified range.
        *
        *   @param  from        Start of the range.
        *   @param  to          End of the range.
        *   @return             A random integer between <code>from</code> and <code>to</code>.
        *                       The start and the end of the range are also possible results.
        **************************************************************************************/
        public static final int getRandom( int from, int to )
        {
            double rand = Math.random() * 1000;
            return ( ( (int)rand % ( to - from + 1 ) ) + from );
        }

        /**************************************************************************************
        *   Delivers the sin-value for a specified degree-value.
        *
        *   @param  degrees     The degrees to get the sin-value for.
        *   @return             The sin-value from -1.0f to 1.0f of the given degree value.
        **************************************************************************************/
        public static final float sin( float degrees )
        {
            return (float)Math.sin( degrees * Math.PI / 180.0f );
        }

        /**************************************************************************************
        *   Delivers the cos-value for a specified degree-value.
        *
        *   @param  degrees     The degrees to get the cos-value for.
        *   @return     The cos-value from -1.0f to 1.0f of the given degree value.
        **************************************************************************************/
        public static final float cos( float degrees )
        {
            return (float)Math.cos( degrees * Math.PI / 180.0f );
        }

        /**************************************************************************************
        *   Transforms an integer that represents an RGB-color value to a float-array with three elements.
        *
        *   @param  col     The RGB-color-value to transform.
        *   @return         A float-array with three elements that represent the R, G and B value.
        **************************************************************************************/
        public static final float[] colTof3( int col )
        {
            float r = ( col & 0xff0000 )  >>  16;
            float g = ( col & 0x00ff00 )  >>  8;
            float b = ( col & 0x0000ff )  >>  0;

            return new float[] { r / 255, g / 255, b / 255 };
        }

        /**************************************************************************************
        *   Transforms an integer that represents an ARGB-color value to a float-array with four elements.
        *
        *   @param  col     The RGB-color-value to transform.
        *   @return         A float-array with three elements that represent the R, G and B value.
        **************************************************************************************/
        public static final float[] colTof4( int col )
        {
            float a = ( col & 0xff000000 )  >>  24;
            float r = ( col & 0x00ff0000 )  >>  16;
            float g = ( col & 0x0000ff00 )  >>  8;
            float b = ( col & 0x000000ff )  >>  0;

            return new float[] { a / 255, r / 255, g / 255, b / 255 };
        }

        /**************************************************************************************
        *   Returns the angle from one point to another.
        *
        *   @param  x1      The x value of point one.
        *   @param  y1      The y value of point one.
        *   @param  x2      The x value of point two.
        *   @param  y2      The y value of point two.
        *   @return         The normalized angle in degrees from point one to point two.
        **************************************************************************************/
        public static final float getAngleCorrect( int x1, int y1, int x2, int y2 )
        {
            double  dx      = x2 - x1;
            double  dy      = y2 - y1;
            double  angle   = 0.0;

            if ( dx == 0.0 )
            {
                if ( dy == 0.0 )    angle = 0.0;
                else if( dy > 0.0 ) angle = Math.PI / 2.0;
                else                angle = ( Math.PI * 3.0 ) / 2.0;
            }
            else if ( dy == 0.0 )
            {
                if ( dx > 0.0 )     angle = 0.0;
                else                angle = Math.PI;
            }
            else
            {
                if ( dx < 0.0 )     angle = Math.atan( dy / dx ) + Math.PI;
                else if( dy < 0.0 ) angle = Math.atan( dy / dx ) + ( 2 * Math.PI );
                else                angle = Math.atan( dy / dx );
            }

            //to degree
            float ret = (float)( ( angle * 180 ) / Math.PI );
                  //ret += 90.0f; // if 0.0 degrees is north

            return normalizeAngle( ret );
        }

        /*********************************************************************************
        *   Normalizes the given angle and returns an equal angle
        *   clamped in the range of including 0.0 and excluding 360.0 degrees.
        *
        *   @param  angle   The angle to normalize.
        *   @return         The normalized angle in a range of including 0.0 and excluding 360.0.
        ***********************************************************************************/
        public static final float normalizeAngle( float angle )
        {
            while ( angle <    0.0f ) angle += 360.0f;
            while ( angle >= 360.0f ) angle -= 360.0f;
            return angle;
        }

        /*********************************************************************************
        *   Compute the length of the line from (x1,y1) to (x2,y2).
        *   Remember Pythagoras of Samos.
        *
        *   @param      x1      First  line end point x.
        *   @param      y1      First  line end point y.
        *   @param      x2      Second line end point x.
        *   @param      y2      Second line end point y.
        *   @return             Length of the line from (x1,y1) to (x2,y2).
        ***********************************************************************************/
        public static float getDistanceBetweenTwoPoints( float x1, float y1, float x2, float y2 )
        {
            float   distanceX = x2 - x1;
            float   distanceY = y2 - y1;

            return (float)Math.sqrt( distanceX * distanceX + distanceY * distanceY );
        }
    }
