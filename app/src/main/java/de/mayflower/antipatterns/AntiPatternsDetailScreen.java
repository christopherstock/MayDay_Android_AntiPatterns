
    package de.mayflower.antipatterns;

    import android.app.Activity;
    import android.content.Context;
    import android.graphics.Canvas;
    import android.os.Bundle;
    import android.support.v4.view.ViewPager;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import java.util.Locale;

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

        private AntiPatternsPatternCountService counterService;


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

            this.counterService = new AntiPatternsPatternCountService();
            this.counterService.init(this);

            this.populateList(
                    pattern.getSymptoms(),
                    (LinearLayout) rootView.findViewById(R.id.detail_view_symptoms_list),
                    inflater
            );

            this.populateList(
                    pattern.getRemedies(),
                    (LinearLayout) rootView.findViewById(R.id.detail_view_remedies_list),
                    inflater
            );

            TextView counterDisplay = (TextView) rootView.findViewById(R.id.detail_view_display_counter);
            counterDisplay.setText(Integer.toString(this.counterService.readCounter(pattern.getId())));

            Button counterIncrementButton = (Button) rootView.findViewById(R.id.detail_view_increment_counter);

            final AntiPatternsPatternCountService finalCounterService = counterService;
            final Integer finalPatternId = pattern.getId();
            final TextView finalCounterDisplay = counterDisplay;
            final AntiPatternsDetailScreen detailScreen = this;

            counterIncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCounterService.incrementCounter(finalPatternId);
                    finalCounterDisplay.setText(
                            Integer.toString(finalCounterService.readCounter(finalPatternId))
                    );
                    AntiPatternsHydrator.updatePatternCount(
                            finalCounterService.readCounter(finalPatternId), detailScreen
                    );
                }
            });

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
