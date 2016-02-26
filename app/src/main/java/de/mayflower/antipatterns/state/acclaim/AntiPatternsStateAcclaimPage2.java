
    package de.mayflower.antipatterns.state.acclaim;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  android.content.*;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.*;

    /**********************************************************************************************
    *   The 2nd page of the 'acclaim' ViewPager.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStateAcclaimPage2
    {
        /** The inflated view that represents the 2nd page. */
        protected                   View                iView                           = null;
        /** A reference to the acclaim text that is displayed on this page. */
        protected                   TextView            iAcclaimText                    = null;

        /**********************************************************************************************
        *   Creates the 2nd page of the 'acclaim' ViewPager.
        *
        *   @param  context     The current system context.
        **********************************************************************************************/
        public AntiPatternsStateAcclaimPage2(Context context)
        {
            iView        = LibUI.getInflatedLayoutById( context, R.layout.state_content_acclaim_page_2 );
            iAcclaimText = (TextView)iView.findViewById( R.id.acclaim_text );

            //setup text
            LibUI.setupTextView( context, iAcclaimText, AntiPatternsSystems.getFonts().TYPEFACE_BOLD, R.string.state_acclaim_page2 );

            //setup click for outer container
            LinearLayout ll = (LinearLayout)iView.findViewById( R.id.acclaim_page_outer_container );
            LibUI.setOnClickAction( ll, AntiPatternsActionPush.EPushAcclaimNextPage );
        }
    }
