
    package de.mayflower.antipatterns;

    import  android.os.Bundle;
    import  android.support.v4.app.Fragment;
    import  android.view.LayoutInflater;
    import  android.view.View;
    import  android.view.ViewGroup;
    import  android.widget.LinearLayout;
    import  de.mayflower.lib.LibLauncher;

    public class AntiPatternsMainScreenViewPagerFragment extends Fragment
    {
        private                             int         index                   = 0;
        private                             String      title                   = null;

        public void init( int index, String title )
        {
            this.index = index;
            this.title = title;
        }

        @Override
        public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
        {
            super.onCreateView( inflater, container, savedInstanceState );

            // TODO refactor!

            AntiPatternsDebug.major.out("onCreateView for fragment [" + index + "]");

            View      rootView = inflater.inflate( R.layout.antipatterns_main_screen_view_pager_fragment, container, false );
            ViewGroup sv       = (ViewGroup)rootView.findViewById( R.id.view_pager_scrollview_content );

            int itemsToCreate = 10;
            for ( int i = 0; i < itemsToCreate; ++i )
            {
                LinearLayout item = (LinearLayout)inflater.inflate( R.layout.antipatterns_list_item, container, false );

                final int index = i;

                AntiPatternsItemClickListener clickListener = new AntiPatternsItemClickListener(
                    index,
                    AntiPatternsMainScreenViewPagerFragment.this.getActivity()
                );

                item.setOnClickListener( clickListener );

                sv.addView( item );
            }

            return rootView;
        }

        public String getTitle()
        {
            return title;
        }
    }
