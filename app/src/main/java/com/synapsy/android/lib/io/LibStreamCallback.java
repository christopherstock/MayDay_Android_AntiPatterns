/*  $Id: LibStreamCallback.java 49044 2013-05-03 10:35:52Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.io;

    /************************************************************************
    *   A callback from a stream to get callbacks when the current or total size has changed.
    *   Useful for ui-components like progress bars.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 49044 $ $Date: 2013-05-03 12:35:52 +0200 (Fr, 03 Mai 2013) $
    *   @see        "URL: http://svn.synapsy.net/svn/Synapsy/odp/client/android/Lib/tags/v1.93/src/com/synapsy/odpandroid/lib/io/LibStreamCallback.java $"
    ************************************************************************/
    public interface LibStreamCallback
    {
        /************************************************************************
        *   The callback being invoked when the total stream size has changed.
        *
        *   @param  newTotalSize    The total size of bytes to stream.
        ************************************************************************/
        public abstract void setTotalProgress( long newTotalSize );

        /************************************************************************
        *   The callback being invoked when the current size of streamed bytes has changed.
        *
        *   @param  newCurrentSize  The new size of streamed bytes.
        ************************************************************************/
        public abstract void setCurrentProgress( int newCurrentSize );
    }
