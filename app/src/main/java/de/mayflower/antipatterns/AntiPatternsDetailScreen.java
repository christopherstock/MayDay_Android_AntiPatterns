
    package de.mayflower.antipatterns;

    import android.app.Activity;
    import android.graphics.Canvas;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import de.mayflower.antipatterns.data.Pattern;
    import de.mayflower.lib.api.LibAPI;
    import de.mayflower.lib.api.LibModernAPI5;

    /**********************************************************************************************
    *   The details screen that contains the detailed anti pattern description.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ***********************************************************************************************/
    public class AntiPatternsDetailScreen extends Activity {
        static final int MIN_SWIPE_DISTANCE = 150;

        private float touchStartX           = 0;
        private float touchEndX             = 0;
        private static final String ARG_ITEM_ID = "pattern_id";


        /*****************************************************************************
         * Being invoked when this activity is being created.
         *****************************************************************************/
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //invoke super method
            super.onCreate(savedInstanceState);

            AntiPatternsDebug.major.out(AntiPatternsDetailScreen.class + "::onCreate()");

            LayoutInflater inflater = this.getLayoutInflater();
            View rootView = inflater.inflate(R.layout.antipatterns_detail_screen, null, false);
            TextView titleview = (TextView)rootView.findViewById(R.id.detail_view_title);

            Pattern pattern = AntiPatternsHydrator.getCurrentPattern();
            titleview.setText(pattern.getName());
            
            this.populateList(
                    pattern.getSymptomps(),
                    (LinearLayout) rootView.findViewById(R.id.detail_view_symptoms_list),
                    inflater
            );

            this.populateList(
                    pattern.getRemedies(),
                    (LinearLayout) rootView.findViewById(R.id.detail_view_remedies_list),
                    inflater
            );

            setContentView(rootView);
        }

        /*****************************************************************************
         * Being invoked after this activity has been created and on returning.
         *****************************************************************************/
        @Override
        public void onStart()
        {
            //invoke super method
            super.onStart();

            AntiPatternsDebug.major.out( AntiPatternsDetailScreen.class + "::onStart()" );
        }

        @Override
        public void onBackPressed()
        {
            super.onBackPressed();
            this.animateTransition();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    this.touchStartX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    this.touchEndX = event.getX();

                    float deltaX = touchEndX - touchStartX;

                    if (deltaX > MIN_SWIPE_DISTANCE) {
                        super.onBackPressed();
                        this.animateTransition();
                    }

                    break;

            }
            return super.onTouchEvent(event);
        }

        private void populateList(
                String[] listItems,
                LinearLayout listContainer,
                LayoutInflater inflater
        ) {
            for (String listItem: listItems) {
                TextView listItemTextView = (TextView) inflater.inflate(R.layout.detail_view_list_item, listContainer, false);
                listItemTextView.setText(listItem);

                listContainer.addView(listItemTextView);
            }
        }


        private void animateTransition()
        {
            //only operative since API-level 5
            if (!LibAPI.isSdkLevelLowerThan(5)) {
                LibModernAPI5.overridePendingTransition
                        (
                                this,
                                R.anim.push_right_in,
                                R.anim.push_right_out
                        );
            }
        }
    }