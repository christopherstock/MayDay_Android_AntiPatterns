/*  $Id: LibScrollCallback.java 50257 2013-07-30 10:57:31Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui.widget;

    /************************************************************************
    *   A callback that can be assign to a scrolling view.
    *   The callback causes a notify when the scroll value of the scrolling view has changed.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50257 $ $Date: 2013-07-30 12:57:31 +0200 (Di, 30 Jul 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/widget/LibScrollCallback.java $"
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
