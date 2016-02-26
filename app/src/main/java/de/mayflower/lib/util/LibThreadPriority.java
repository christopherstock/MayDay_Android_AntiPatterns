/*  $Id: LibThreadPriority.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.util;

    /*********************************************************************************
    *   Holds all suitable values for the thread-priority setting.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/util/LibThreadPriority.java $"
    *********************************************************************************/
    public enum LibThreadPriority
    {
        /** The highest thread priority. */
        MAX(    Thread.MAX_PRIORITY     ),

        /** A high thread priority. */
        HIGHER( 7                       ),

        /** A normal thread priority. */
        NORM(   Thread.NORM_PRIORITY    ),

        /** A low thread priority. */
        LOWER(  3                       ),

        /** The lowest thread priority. */
        MIN(    Thread.MIN_PRIORITY     ),

        ;

        /** The thread priority as an integer number reaching from 1 to 10. */
        private     int     number      = 0;

        /*********************************************************************************
        *   Creates a new thread priority with the specified priority.
        *
        *   @param  aNumber     The priority number for this Thread reaching from 1 to 10.
        *********************************************************************************/
        private LibThreadPriority( int aNumber )
        {
            number = aNumber;
        }

        /*********************************************************************************
        *   Returns the thread priority integer.
        *
        *   @return     The priority for this enum constant reaching from 1 to 10.
        *********************************************************************************/
        public int getNumber()
        {
            return number;
        }
    }
