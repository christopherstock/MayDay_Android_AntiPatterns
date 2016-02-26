
    package de.mayflower.lib;

    import  android.app.*;

    /************************************************************************
    *   Represents one state of the application
    *   that is represented by an activity.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public interface LibState
    {
        /************************************************************************
        *   Delivers the activity this state is bound to.
        *
        *   @return     The activity that represents this state.
        ************************************************************************/
        public abstract Activity getActivity();
    }
