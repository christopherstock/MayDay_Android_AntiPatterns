/*  $Id: PicFoodStateAcclaimPage5.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state.acclaim;

    import  net.picfood.*;
    import  android.content.*;
    import  android.view.*;
    import  android.widget.*;

    import  de.mayflower.lib.ui.*;

    /**********************************************************************************************
    *   The 5th page of the 'acclaim' ViewPager.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/acclaim/PicFoodStateAcclaimPage5.java $"
    **********************************************************************************************/
    public class PicFoodStateAcclaimPage5
    {
        /** The inflated view that represents the 5th page. */
        protected                   View                iView                           = null;
        /** A reference to the acclaim text that is displayed on this page. */
        protected                   TextView            iAcclaimText                    = null;

        /**********************************************************************************************
        *   Creates the 5th page of the 'acclaim' ViewPager.
        *
        *   @param  context     The current system context.
        **********************************************************************************************/
        public PicFoodStateAcclaimPage5( Context context )
        {
            iView        = LibUI.getInflatedLayoutById( context, R.layout.state_content_acclaim_page_5 );
            iAcclaimText = (TextView)iView.findViewById( R.id.acclaim_text );

            //setup text
            LibUI.setupTextView( context, iAcclaimText, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.state_acclaim_page5 );
        }
    }
