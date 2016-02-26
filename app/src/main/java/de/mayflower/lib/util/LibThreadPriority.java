
    package de.mayflower.lib.util;

    /*********************************************************************************
    *   Holds all suitable values for the thread-priority setting.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
