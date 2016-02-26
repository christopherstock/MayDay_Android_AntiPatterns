/*  $Id: PicFoodStatePivotalTabNews.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state.pivotal;

    import  net.picfood.*;
    import  android.os.*;
    import  android.support.v4.app.Fragment;
    import  android.view.*;
    import  android.widget.*;

    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   The third fragment.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/pivotal/PicFoodStatePivotalTabNews.java $"
    **********************************************************************************************/
    public class PicFoodStatePivotalTabNews extends Fragment
    {
        @Override
        public View onCreateView( LayoutInflater li, ViewGroup vg, Bundle bundle )
        {
            //invoke super method
            super.onCreateView( li, vg, bundle );

            //inflate layout
            View newsView = li.inflate( R.layout.state_content_pivotal_tab_news, null );

            //setup headline
            TextView iTitleNews = (TextView)newsView.findViewById( R.id.title_news );
            LibUI.setupTextView( getActivity(), iTitleNews, PicFoodSystems.getFonts().TYPEFACE_REGULAR, R.string.state_news_headline );

            //connect and HIDE debugConsole
            TextView console = (TextView)newsView.findViewById( R.id.text_console );
            LibDebugConsole.getSingleton().connect( getActivity(), console );
            console.setVisibility( View.GONE );

            //return inflated layout
            return newsView;
        }
    }
