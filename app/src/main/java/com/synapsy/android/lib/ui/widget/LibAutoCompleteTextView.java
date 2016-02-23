/*  $Id: LibAutoCompleteTextView.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.widget;

    import  android.content.*;
    import  android.text.*;
    import  android.util.*;
    import  android.widget.*;

    /************************************************************************
    *   Creates a derived {@link AutoCompleteTextView} that performs
    *   an action after {@link #MIN_CHARS_TO_ENTER} characters have been entered.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/widget/LibAutoCompleteTextView.java $"
    ************************************************************************/
    public class LibAutoCompleteTextView extends AutoCompleteTextView implements TextWatcher
    {
        /** The minimum number of chars to be entered after the associated action is launched. */
        private     static  final   int                     MIN_CHARS_TO_ENTER          = 3;

        /** The last submitted search term. */
        public                      String                  iLastSearchTerm             = null;

        /** The action to perform each time the minimum number of chars if entered or exceeded. */
        public                      Runnable                iActionOnTextChange         = null;

        /************************************************************************
        *   Constructs a new {@link AutoCompleteTextView}. This method must be
        *   implemented and is invoked implicitly by the system.
        *
        *   @param  context     The current system context.
        *   @param  as          The associated attribute set.
        ************************************************************************/
        public LibAutoCompleteTextView( Context context, AttributeSet as )
        {
            super( context, as );

            //setImeOptions( AutoCompleteTextView. );

            //AppsDebugSystem.bugfix.out( ">>>>> create LibAutoCompleteTextView" );
            //addTextChangedListener( this );
        }
/*
        public final void updateAutoTerms( Context context, String[] newTerms )
        {
            iTerms   = newTerms;
            iAdapter = new ArrayAdapter<String>( context, android.R.layout.simple_dropdown_item_1line, newTerms );
            setAdapter( iAdapter );
            setThreshold( MIN_CHARS_TO_ENTER );
        }
*/
        @Override
        public final void beforeTextChanged( CharSequence txt, int i1, int i2, int i3 )
        {
        }

        @Override
        public final void afterTextChanged( Editable e )
        {
        }

        @Override
        public final void onTextChanged( CharSequence txt, int i1, int i2, int i3 )
        {
            boolean featureEnabled = true;
            if ( featureEnabled )
            {
                String s = txt.toString();

                //order results if charSequence reached min length
                if ( s.length() >= MIN_CHARS_TO_ENTER ) //&& ( lastSearchTerm == null || !lastSearchTerm.equalsIgnoreCase( s ) ) )
                {
                    //remember this term
                    iLastSearchTerm = s;

                    //notify auto-submit
                    iActionOnTextChange.run();
                }
            }
        }
    }
