/*  $Id: LibState.java 50541 2013-08-09 12:37:45Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib;

    import  android.app.*;

    /************************************************************************
    *   Represents one state of the application
    *   that is represented by an activity.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50541 $ $Date: 2013-08-09 14:37:45 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/LibState.java $"
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
