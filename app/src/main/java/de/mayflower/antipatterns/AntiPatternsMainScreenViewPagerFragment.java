
    package de.mayflower.antipatterns;

    import  android.os.Bundle;
    import  android.support.v4.app.Fragment;
    import  android.view.LayoutInflater;
    import  android.view.View;
    import  android.view.ViewGroup;
    import  android.widget.LinearLayout;
    import  android.widget.TextView;

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

            AntiPatternsPatternCountService countService = new AntiPatternsPatternCountService();
            countService.init(this.getActivity());

            Integer[] patternIds;

            patternIds = AntiPatternsHydrator.categories[index].getPatterns();

            for ( int i = 0; i < patternIds.length; ++i )
            {
                LinearLayout item     = (LinearLayout)inflater.inflate(R.layout.antipatterns_list_item, container, false);
                TextView     textView = (TextView)item.findViewById(R.id.text_item_title);

                String patternLabel = AntiPatternsHydrator.patterns[i].getName();

                if ( Integer.valueOf(index).compareTo(AntiPatternsHydrator.TOP10_CATEGORY) == 0) {
                    patternLabel = AntiPatternsHydrator.patterns[i].getNameWithCounter();
                }

                textView.setText( patternLabel );

                AntiPatternsItemClickListener clickListener = new AntiPatternsItemClickListener(
                    i,
                    this.getActivity()
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
