
    package de.mayflower.antipatterns;

    import  android.os.Bundle;
    import  android.support.v4.app.Fragment;
    import  android.view.LayoutInflater;
    import  android.view.View;
    import  android.view.ViewGroup;

    public class AntiPatternsMainScreenViewPagerFragment extends Fragment
    {
        private         int     position                        = 0;


        public AntiPatternsMainScreenViewPagerFragment( int position )
        {
            this.position = position;





        }

        @Override
        public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
        {
            //setContentView ??

            // Inflate the layout resource that'll be returned
            View rootView = inflater.inflate( R.layout.antipatterns_main_screen_view_pager_fragment, container, false );

            // Get the arguments that was supplied when
            // the fragment was instantiated in the
            // CustomPagerAdapter
            //Bundle args = getArguments();
            //((TextView) rootView.findViewById(R.id.textView)).setText("Page " + args.getInt("page_position"));

            return rootView;
        }
    }
