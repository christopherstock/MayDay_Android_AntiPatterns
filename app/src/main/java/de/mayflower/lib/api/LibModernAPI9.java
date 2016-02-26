
    package de.mayflower.lib.api;

    import  android.annotation.SuppressLint;
    import  android.view.*;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 9 or higher.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************/
    @SuppressLint("NewApi")
    public abstract class LibModernAPI9
    {
        /*********************************************************************************
        *   Enables the OverScrollMode for the specified view.
        *   This means that the content of a view can be scrolled outside of it's bounds
        *   and that it will bounce back afterwards, giving the user a cool user-experience.
        *
        *   @param  view        The view to set OverScrollMode for.
        *   @param  enable      The new value to set.
        *********************************************************************************/
        public static final void setOverScrollModeEnabled( View view, boolean enable )
        {
            view.setOverScrollMode( enable ? View.OVER_SCROLL_IF_CONTENT_SCROLLS : View.OVER_SCROLL_NEVER );
        }
    }
