
    package de.mayflower.lib.io;

    /************************************************************************
    *   A callback from a stream to get callbacks when the current or total size has changed.
    *   Useful for ui-components like progress bars.
    *
    *   @author     Christopher Stock
    *   @version    1.0
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
