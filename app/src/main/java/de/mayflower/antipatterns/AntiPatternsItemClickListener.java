package de.mayflower.antipatterns;

import android.app.Activity;
import android.view.View;

import de.mayflower.lib.LibLauncher;

public class AntiPatternsItemClickListener implements View.OnClickListener
{
    private int      index;
    private Activity context;

    public AntiPatternsItemClickListener(int index, Activity context)
    {
        this.index = index;
        this.context = context;
    }

    @Override
    public void onClick(View view)
    {
        AntiPatternsDebug.major.out("Item [" + this.index + "] in page [" + this.index + "] touched!");

        LibLauncher.launchActivity(
                this.context,
                AntiPatternsDetailScreen.class,
                R.anim.push_left_in,
                R.anim.push_left_out
        );
    }
}
