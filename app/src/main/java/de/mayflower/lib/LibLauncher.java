
    package de.mayflower.lib;

    import  android.app.*;
    import  android.content.*;
    import  de.mayflower.lib.api.*;

    /*********************************************************************************
    *   Launches internal or external system activities.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************/
    public abstract class LibLauncher
    {
        /*********************************************************************************
        *   Launches another Activity of the current application package.
        *
        *   @param  context                 The according activity context.
        *   @param  activityClassToLaunch   The Activity class of the Activity to start.
        *   @param  animIn                  The resource-ID of the animation for the new Activity to appear.
        *   @param  animOut                 The resource-ID of the animation for the old Activity to disappear.
        *********************************************************************************/
        public static final void launchActivity( Activity context, Class<?> activityClassToLaunch, int animIn, int animOut )
        {
            //create activity launch intent and start it
            Intent launchActivity = new Intent( context, activityClassToLaunch );
            context.startActivity( launchActivity );

            //set animation if desired
            if ( animIn != -1 && animOut != -1 )
            {
                //only operative since API-level 5
                if ( !LibAPI.isSdkLevelLowerThan( 5 ) )
                {
                    LibModernAPI5.overridePendingTransition( context, animIn, animOut );
                }
            }
        }
    }
