
    package de.mayflower.antipatterns;

    import  android.app.Activity;
    import  android.view.View;
    import  de.mayflower.lib.LibLauncher;

    /**********************************************************************************************
    *   This class represents the item click listener for the antipattern item list.
    *
    *   @author     Christian Heldt.
    *   @version    1.0
    ***********************************************************************************************/
    public class AntiPatternsItemClickListener implements View.OnClickListener
    {
        private                 int                 index                       = 0;
        private                 Activity            context                     = null;

        public AntiPatternsItemClickListener( int index, Activity context )
        {
            this.index   = index;
            this.context = context;
        }

        @Override
        public void onClick( View view )
        {
            AntiPatternsDebug.major.out("Item [" + this.index + "] in page [" + this.index + "] touched!");

            AntiPatternsHydrator.setCurrent(this.index); // register pattern to show

            LibLauncher.launchActivity(
                    this.context,
                    AntiPatternsDetailScreen.class,
                    R.anim.push_left_in,
                    R.anim.push_left_out
            );
        }
    }
