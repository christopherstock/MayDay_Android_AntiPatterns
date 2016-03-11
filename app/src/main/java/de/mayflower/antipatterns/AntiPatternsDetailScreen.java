
    package de.mayflower.antipatterns;

    import  android.app.Activity;
    import  android.os.Bundle;
    import android.view.MotionEvent;

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

        /*****************************************************************************
         * Being invoked when this activity is being created.
         *****************************************************************************/
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //invoke super method
            super.onCreate( savedInstanceState );

            AntiPatternsDebug.major.out( AntiPatternsDetailScreen.class + "::onCreate()" );

            setContentView( R.layout.antipatterns_detail_screen );
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

                    float deltaX = touchStartX - touchEndX;

                    if (deltaX > MIN_SWIPE_DISTANCE) {
                        super.onBackPressed();
                        this.animateTransition();
                    }

                    break;

            }
            return super.onTouchEvent(event);
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