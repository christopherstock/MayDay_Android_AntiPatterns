/*  $Id: LibDebugConsole.java 50541 2013-08-09 12:37:45Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui.widget;

    import  java.util.*;
    import  android.app.*;
    import  android.os.*;
    import  android.text.*;
    import  android.widget.*;

    /************************************************************************
    *   Represents a debug-console that can be connected to a {@link TextView}
    *   and be used to show output on the screen of a running application.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50541 $ $Date: 2013-08-09 14:37:45 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/widget/LibDebugConsole.java $"
    ************************************************************************/
    public class LibDebugConsole
    {
        /************************************************************************
        *   Specified all colors that one console output line can have.
        ************************************************************************/
        public static enum ConsoleColor
        {
            /** The console output color 'green bright'. */
            EGreenBright(   "#66ff66" ),

            /** The console output color 'green'. */
            EGreen(         "#1bbd1b" ),

            /** The console output color 'yellow'. */
            EYellow(        "#ffff00" ),

            /** The console output color 'orange'. */
            EOrange(        "#ff7700" ),

            /** The console output color 'blue bright'. */
            EBlueBright(    "#8bd0f8" ),

            /** The console output color 'blue'. */
            EBlue(          "#3575f8" ),

            /** The console output color 'white'. */
            EWhite(         "#ffffff" ),

            /** The console output color 'red'. */
            ERed(           "#ec2121" ),

            ;

            /************************************************************************
            *   The stored color value as a HTML-color-assignment.
            ************************************************************************/
            public              String          iCol            = null;

            /************************************************************************
            *   Creates a new color for one output in the debug console.
            *
            *   @param  aCol    The color value as a HTML-color-notation.
            ************************************************************************/
            private ConsoleColor( String aCol )
            {
                iCol = aCol;
            }
        }

        /** The singleton instance of this class. */
        private     static              LibDebugConsole                 singleton                   = null;

        /** The connected TextView that displays all debug lines. */
        private                         TextView                        iDebugConsole               = null;
        /** All output items this debug console displays. */
        public                          Vector<Spanned>                 iOutputBuffer               = new Vector<Spanned>();

        /************************************************************************
        *   Returns the singleton instance of this class.
        *
        *   @return The singleton instance of this class.
        ************************************************************************/
        public static final LibDebugConsole getSingleton()
        {
            if ( singleton == null ) singleton = new LibDebugConsole();
            return singleton;
        }

        /************************************************************************
        *   Connects the debug-console to the specified TextView.
        *
        *   @param  activity    The according activity context.
        *   @param  console     The TextView to display the debug output.
        ************************************************************************/
        public final void connect( final Activity activity, TextView console )
        {
            //assign debug console
            iDebugConsole = console;

            //assign current output threaded
            new Thread()
            {
                @Override
                public final void run()
                {
                    //update the text view in an ui-thread
                    updateTextViewUiThreaded( activity );
                }
            }.start();
        }

        /************************************************************************
        *   Prunes all text of the connected TextView and sets up all lines
        *   of debug output. This method is performed on the UI-Thread.
        *
        *   @param  activity    The according activity context.
        ************************************************************************/
        protected final void updateTextViewUiThreaded( Activity activity )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        updateTextView();
                    }
                }
            );
        }

        /************************************************************************
        *   Prunes all text of the connected TextView and sets up all lines
        *   of debug output. This method must be invoked from the UI-Thread.
        ************************************************************************/
        protected final void updateTextView()
        {
            //only if a debug console is assigned
            if ( iDebugConsole != null )
            {
                //prune text view
                iDebugConsole.setText( "" );

                //add all outputs
                for ( int i = 0; i < iOutputBuffer.size(); ++i )
                {
                    iDebugConsole.append( iOutputBuffer.elementAt( i ) );

                    //linebreak for all elements except the last one
                    if ( i < iOutputBuffer.size() - 1 )
                    {
                        iDebugConsole.append( Html.fromHtml( "<br>" ) );
                    }
                }
            }
        }

        /************************************************************************
        *   Clears all output lines of the console.
        ************************************************************************/
        public final void clearOutput()
        {
            //prune output stack
            iOutputBuffer.removeAllElements();

            //update text view
            updateTextView();
        }

        /************************************************************************
        *   Appends a line of output to the console.
        *   This method is being performed on the UI-Thread.
        *
        *   @param  col     The color for this output line.
        *   @param  text    The message to append to the console.
        ************************************************************************/
        public final void appendOutputUIThreaded( ConsoleColor col, String text )
        {
            appendOutputUIThreaded( Html.fromHtml( "<font color=\"" + col.iCol + "\">" + text + "</font>" ) );
        }

        /************************************************************************
        *   Appends a line of output to the console.
        *   This method is being performed on the UI-Thread.
        *
        *   @param  outputToAppend  The spanned HTML-text to append to the console.
        ************************************************************************/
        private final void appendOutputUIThreaded( final Spanned outputToAppend )
        {
            Handler handler = new Handler( Looper.getMainLooper() );
            handler.post
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        appendOutput( SpannedString.valueOf( outputToAppend ) );
                    }
                }
            );
        }

        /************************************************************************
        *   Appends a line of output to the console.
        *
        *   @param  outputToAppend  The spanned HTML-text to append to the console.
        ************************************************************************/
        public final void appendOutput( Spanned outputToAppend )
        {
            //add to output stack
            iOutputBuffer.addElement( outputToAppend );

            //update text view
            updateTextView();
        }
    }
