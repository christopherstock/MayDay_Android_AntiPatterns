
    package de.mayflower.lib.ui.widget;

    /************************************************************************
    *   A callback that can be assign to a scrolling view.
    *   The callback causes a notify when the scroll value of the scrolling view has changed.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public interface LibScrollCallback
    {
        /************************************************************************
        *   Being invoked when the scroll-value changes,
        *   this method handles the details.
        *
        *   @param  newL    Current horizontal scroll origin.
        *   @param  newT    Current vertical scroll origin.
        *   @param  oldL    Previous horizontal scroll origin.
        *   @param  oldT    Previous vertical scroll origin.
        ************************************************************************/
        public void notifyScrollChange( int newL, int newT, int oldL, int oldT );
    }
