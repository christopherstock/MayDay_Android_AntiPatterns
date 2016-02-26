/*  $Id: LibDialogInputHandler.java 50578 2013-08-13 12:46:00Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui.dialog;

    import  android.content.*;
    import  android.text.*;
    import  android.view.*;
    import  android.view.inputmethod.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.*;

    /*****************************************************************************
    *   Handles the InputField in input dialogs.
    *   Manages to set the same input string again ( in case of a connection problem ).
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50578 $ $Date: 2013-08-13 14:46:00 +0200 (Di, 13 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/dialog/LibDialogInputHandler.java $"
    *****************************************************************************/
    public final class LibDialogInputHandler
    {
        /** The InputField that shall be handled. */
        private             EditText                iField                  = null;
        /** The last text that has been entered into this InputField. Used to assign this text when the {@link #iField} is reassigned. */
        private             String                  iLastText               = null;

        /*****************************************************************************
        *   Creates a new Dialog-InputField-handler.
        *****************************************************************************/
        public LibDialogInputHandler()
        {
        }

        /*****************************************************************************
        *   Constructs one single input-field for a dialog.
        *
        *   @param  context                 The current system context.
        *   @param  dialogInputLayoutID     The layout-resource-ID of this input-field.
        *   @param  inputFieldID            The layout-IDs for this input-fields.
        *   @param  hintStringID            The resource-ID for this input-field's hint.
        *   @param  maxFieldLengthID        The resource-ID for the maximum length of this input-fields.
        *   @param  lastItem                Specifies, if this is the last InputField in the dialog.
        *                                   The {@link EditorInfo#IME_ACTION_DONE} is assigned to
        *                                   the last InputField. Action {@link EditorInfo#IME_ACTION_NEXT}
        *                                   is assigned otherwise. The user can jump to the next InputField
        *                                   using the ENTER-key this way.
        *   @return                         The constructed View that holds the InputField.
        *****************************************************************************/
        public final View getDialogInputView
        (
            Context     context,
            int         dialogInputLayoutID,
            int         inputFieldID,
            int         hintStringID,
            int         maxFieldLengthID,
            boolean     lastItem
        )
        {
            //check if the feedback has already been assigned
            String existentFeedback = "";
            if ( iField != null )
            {
                existentFeedback = iField.getText().toString().trim();
            }
            else if ( iLastText != null )
            {
                existentFeedback = iLastText;
            }

            //get dialog view
            View dialogInputView = LibUI.getInflatedLayoutById( context, dialogInputLayoutID );

            iField = (EditText)dialogInputView.findViewById( inputFieldID );
            iField.setHint( hintStringID );
            iField.setText( existentFeedback );
            iField.setFilters( new InputFilter[] { new InputFilter.LengthFilter( context.getResources().getInteger( maxFieldLengthID ) ) } );
            iField.setImeOptions( iField.getImeOptions() | ( lastItem ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_NEXT ) );

            return dialogInputView;
        }

        /*****************************************************************************
        *   Pruned the InputField of this handler, destroying all inserted data.
        *****************************************************************************/
        public final void destroy()
        {
            iField      = null;
            iLastText   = null;
        }

        /*****************************************************************************
        *   Receives the text of this handler's InputField.
        *   The returned text is trimmed.
        *
        *   @return     The inserted String of this InputField.
        *               The String is trimmed before it is returned.
        *               <code>null</code> is NEVER returned!
        *****************************************************************************/
        public final String getText()
        {
            return iField.getText().toString().trim();
        }

        /*****************************************************************************
        *   Replaces the content of this InputField with the given String.
        *
        *   @param  text    The text to assign to this InputField.
        *                   This text will be trimmed before it is assigned.
        *                   This operation will be ignored if the given String is <code>null</code>.
        *****************************************************************************/
        public final void setText( String text )
        {
            if ( iField != null )
            {
                iField.setText( text.trim() );
            }
            iLastText = text;
        }

        /*****************************************************************************
        *   Alters this InputField's visibility.
        *
        *   @param  visibility  The visibility constant to set.
        *****************************************************************************/
        public final void setVisibility( int visibility )
        {
            if ( iField != null )
            {
                iField.setVisibility( visibility );
            }
        }
    }
