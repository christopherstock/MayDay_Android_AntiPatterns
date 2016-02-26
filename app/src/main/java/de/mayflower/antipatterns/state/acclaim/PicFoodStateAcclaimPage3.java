
    package de.mayflower.antipatterns.state.acclaim;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  android.content.*;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.*;

    /**********************************************************************************************
    *   The 3rd page of the 'acclaim' ViewPager.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodStateAcclaimPage3
    {
        /** The inflated view that represents the 3rd page. */
        protected                   View                iView                           = null;
        /** A reference to the acclaim text that is displayed on this page. */
        protected                   TextView            iAcclaimText                    = null;

        /**********************************************************************************************
        *   Creates the 3rd page of the 'acclaim' ViewPager.
        *
        *   @param  context     The current system context.
        **********************************************************************************************/
        public PicFoodStateAcclaimPage3( Context context )
        {
            iView        = LibUI.getInflatedLayoutById( context, R.layout.state_content_acclaim_page_3 );
            iAcclaimText = (TextView)iView.findViewById( R.id.acclaim_text );

            //setup text
            LibUI.setupTextView( context, iAcclaimText, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.state_acclaim_page3 );

            //setup click for outer container
            LinearLayout ll = (LinearLayout)iView.findViewById( R.id.acclaim_page_outer_container );
            LibUI.setOnClickAction( ll, PicFoodActionPush.EPushAcclaimNextPage );
        }
    }
