package de.mayflower.antipatterns;

import android.app.Application;
import android.util.Log;


public class AntipatternsApplication extends Application {
    private int currentPosition;

    public void onCreate ()
    {
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });
        super.onCreate();
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        Log.e("Topeer", "Uncaught " + e.getClass() + ": " + e.getMessage());

        System.exit(1); // kill off the crashed app
    }

    public int getCurrentPosition()
    {
        return currentPosition;
    }
    public void setCurrentPosition(int pos)
    {
        currentPosition = pos;
    }
}
