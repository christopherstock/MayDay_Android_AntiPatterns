
    package de.mayflower.lib.api;

    import  android.annotation.SuppressLint;
    import  android.app.*;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 5 or higher.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************/
    @SuppressLint( "NewApi" )
    public abstract class LibModernAPI5
    {
        /*********************************************************************************
        *   Sets up a transition animation from the current to a different activity.
        *   This method must be invoked AFTER the different activity has been started!
        *
        *   @param  activity    The according activity context.
        *   @param  animIn      The resource-ID of the animation for the new activity to appear.
        *   @param  animOut     The resource-ID of the animation for the old activity to disappear.
        *********************************************************************************/
        public static final void overridePendingTransition( Activity activity, int animIn, int animOut )
        {
            activity.overridePendingTransition( animIn, animOut );
        }
    }
