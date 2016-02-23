/*  $Id: PicFoodStateAcclaimPage1.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state.acclaim;

    import  net.picfood.*;
    import  net.picfood.action.*;
    import  android.content.*;
    import  android.view.*;
    import  android.widget.*;

    import  com.synapsy.android.lib.ui.*;

    /**********************************************************************************************
    *   The 1st page of the 'acclaim' ViewPager.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/acclaim/PicFoodStateAcclaimPage1.java $"
    **********************************************************************************************/
    public class PicFoodStateAcclaimPage1
    {
        /** The inflated view that represents the 1st page. */
        protected                   View                iView                           = null;
        /** A reference to the acclaim text that is displayed on this page. */
        protected                   TextView            iAcclaimText                    = null;

        /**********************************************************************************************
        *   Creates the 1st page of the 'acclaim' ViewPager.
        *
        *   @param  context     The current system context.
        **********************************************************************************************/
        public PicFoodStateAcclaimPage1( Context context )
        {
            iView        = LibUI.getInflatedLayoutById( context, R.layout.state_content_acclaim_page_1 );
            iAcclaimText = (TextView)iView.findViewById( R.id.acclaim_text );

            //setup text
            LibUI.setupTextView( context, iAcclaimText, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.state_acclaim_page1 );

            //setup click for outer container
            LinearLayout ll = (LinearLayout)iView.findViewById( R.id.acclaim_page_outer_container );
            LibUI.setOnClickAction( ll, PicFoodActionPush.EPushAcclaimNextPage );
        }
    }
