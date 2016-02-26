
    package de.mayflower.antipatterns.state.pivotal;

    import  de.mayflower.antipatterns.*;
    import  android.os.*;
    import  android.support.v4.app.Fragment;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   The third fragment.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStatePivotalTabNews extends Fragment
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
            LibUI.setupTextView( getActivity(), iTitleNews, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, R.string.state_news_headline );

            //connect and HIDE debugConsole
            TextView console = (TextView)newsView.findViewById( R.id.text_console );
            LibDebugConsole.getSingleton().connect( getActivity(), console );
            console.setVisibility( View.GONE );

            //return inflated layout
            return newsView;
        }
    }
