/*  $Id: LibInputFieldHandler.java 50541 2013-08-09 12:37:45Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui;

    import  android.app.*;
    import  android.text.*;
    import  android.view.*;
    import  android.view.View.*;
    import  android.widget.*;

    /************************************************************************
    *   Sets the required handlers for an editable TextView and remembers
    *   the inserted text so no text clear will appear on screen orientation change
    *   or on entering the same state again.
    *
    *   Question is:
    *   Are these handlers now obsolete ??
    *   Why is this working now WITHOUT using this handler??
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50541 $ $Date: 2013-08-09 14:37:45 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/LibInputFieldHandler.java $"
    ************************************************************************/
    public class LibInputFieldHandler implements TextWatcher, OnFocusChangeListener
    {
        /** The InputField this handler makes use of. */
        protected       EditText                    iEditText           = null;

        /** The last text that has been entered into this InputField. */
        protected       String                      iLastText           = "";

        /** Determines, if the InputField is currently focused. */
        private         boolean                     iHasFocus           = false;

        /************************************************************************
        *   Creates a new InputField-handler.
        ************************************************************************/
        public LibInputFieldHandler()
        {
        }

        /************************************************************************
        *   Connects this InputField-handler with the specified EditText.
        *
        *   @param  aView   The EditText to connect to this InputField-handler.
        ************************************************************************/
        public final void assignField( EditText aView )
        {
            iEditText = aView;

            //set handlers
            iEditText.addTextChangedListener(   this );
            iEditText.setOnFocusChangeListener( this );

            //set last text and last focus
            iEditText.setText( iLastText.trim() );
            if ( iHasFocus ) iEditText.requestFocus();
        }

        @Override
        public void onTextChanged( CharSequence cs, int i1, int i2, int i3 )
        {
        }

        @Override
        public void beforeTextChanged( CharSequence cs, int i1, int i2, int i3 )
        {
        }

        @Override
        public void afterTextChanged( Editable e )
        {
            iLastText = e.toString().trim();
        }

        @Override
        public void onFocusChange( View view, boolean hasFocus )
        {
            //assign focus ownership
            iHasFocus = hasFocus;

            //make cursor visible according to focus ownership ( no effect! )
            //iEditText.setCursorVisible( hasFocus );
        }

        /************************************************************************
        *   Prunes the content of the InputField and clears the last stored text.
        *   This method is performed on the UI-Thread.
        *
        *   @param  activity    The according activity context.
        ************************************************************************/
        public void pruneUIThreaded( Activity activity )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( iEditText != null )
                        {
                            iEditText.setText( "" );
                        }
                        iLastText = "";
                    }
                }
            );
        }

        /************************************************************************
        *   Changes the content of the InputField and remembers this text.
        *   This method is performed on the UI-Thread.
        *
        *   @param  activity    The according activity context.
        *   @param  text        The text to assign to this InputField.
        ************************************************************************/
        public void setTextUIThreaded( Activity activity, final String text )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( iEditText != null )
                        {
                            iEditText.setText( text.trim() );
                        }
                        iLastText = text.trim();
                    }
                }
            );
        }

        /************************************************************************
        *   Returns the current trimmed content of this InputField.
        *
        *   @return     The text that is contained in the managed InputField.
        *               The text is trimmed before it is returned.
        ************************************************************************/
        public String getText()
        {
            return iEditText.getText().toString().trim();
        }
    }
