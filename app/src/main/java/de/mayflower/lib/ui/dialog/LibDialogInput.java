/*  $Id: LibDialogInput.java 50571 2013-08-13 09:11:29Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui.dialog;

    import  android.app.*;
    import  android.content.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;

    /*****************************************************************************
    *   Handles input dialogs.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50571 $ $Date: 2013-08-13 11:11:29 +0200 (Di, 13 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/dialog/LibDialogInput.java $"
    *****************************************************************************/
    public abstract class LibDialogInput
    {
        /*****************************************************************************
        *   Orders to shows an input-dialog UI-threaded.
        *
        *   @param  activity                The according activity context.
        *   @param  handlers                The input-handlers for all input-fields.
        *   @param  dialogLayoutID          The layout-resource-ID of the layout to use for this dialog.
        *   @param  dialogContainerID       The resource-ID of the container in the dialog-layout.
        *   @param  dialogInputLayoutIDs    The resource-IDs of the layouts for the InputFields.
        *   @param  inputFieldIDs           The layout-IDs for all input-fields.
        *   @param  hintStringIDs           The resource-IDs for all input-field-hints.
        *   @param  maxFieldLengthIDs       The resource-IDs for the maximum lengths for all input-fields.
        *   @param  titleID                 The string-resource-ID for this dialog's title.
        *   @param  verticalSpacerID        The resource-ID for the height of the vertical padding between texts and input-fields.
        *   @param  messageIDs              The string-resource-ID for the labels above the input-fields.
        *                                   This array's length doesn't have to equal the length of the inputFieldIDs.
        *   @param  buttonCaption1ID        The string-resource-ID for the 1st button's caption.
        *   @param  buttonAction1           The action to perform if the 1st button is pressed.
        *   @param  buttonCaption2ID        The string-resource-ID for the 2nd button's caption.
        *   @param  buttonAction2           The action to perform if the 2nd button is pressed.
        *   @param  cancelable              Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction            The action to perform if this dialog is canceled.
        *   @param  additionalView          An additional view to append after the InputFields.
        *****************************************************************************/
        public static final void showUIThreaded
        (
            final   Activity                activity,
            final   LibDialogInputHandler[] handlers,
                    int                     dialogLayoutID,
                    int                     dialogContainerID,
                    int[]                   dialogInputLayoutIDs,
                    int[]                   inputFieldIDs,
                    int[]                   hintStringIDs,
                    int[]                   maxFieldLengthIDs,
            final   int                     titleID,
            final   int                     verticalSpacerID,
            final   int[]                   messageIDs,
            final   int                     buttonCaption1ID,
            final   Runnable                buttonAction1,
            final   int                     buttonCaption2ID,
            final   Runnable                buttonAction2,
            final   boolean                 cancelable,
            final   Runnable                cancelAction,
            final   View                    additionalView
        )
        {
            showUIThreaded
            (
                activity,
                handlers,
                dialogLayoutID,
                dialogContainerID,
                dialogInputLayoutIDs,
                inputFieldIDs,
                hintStringIDs,
                maxFieldLengthIDs,
                titleID,
                verticalSpacerID,
                LibResource.getResourceStrings( activity, messageIDs ),
                buttonCaption1ID,
                buttonAction1,
                buttonCaption2ID,
                buttonAction2,
                cancelable,
                cancelAction,
                additionalView
            );
        }

        /*****************************************************************************
        *   Orders to show an input-dialog UI-threaded.
        *
        *   @param  activity                The according activity context.
        *   @param  handlers                The input-handlers for all input-fields.
        *   @param  dialogLayoutID          The layout-resource-ID of the layout to use for this dialog.
        *   @param  dialogContainerID       The resource-ID of the container in the dialog-layout.
        *   @param  dialogInputLayoutIDs    The layout-resource-IDs for all input-fields.
        *   @param  inputFieldIDs           The layout-IDs for all input-fields.
        *   @param  hintStringIDs           The resource-IDs for all input-field-hints.
        *   @param  maxFieldLengthIDs       The resource-IDs for the maximum lengths for all input-fields.
        *   @param  titleID                 The string-resource-ID for this dialog's title.
        *   @param  verticalSpacerID        The resource-ID for the height of the vertical padding between texts and input-fields.
        *   @param  messages                The Strings for the labels above the input-fields.
        *                                   This array's length doesn't have to equal the length of the inputFieldIDs.
        *   @param  buttonCaption1ID        The string-resource-ID for the 1st button's caption.
        *   @param  buttonAction1           The action to perform if the 1st button is pressed.
        *   @param  buttonCaption2ID        The string-resource-ID for the 2nd button's caption.
        *   @param  buttonAction2           The action to perform if the 2nd button is pressed.
        *   @param  cancelable              Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction            The action to perform if this dialog is canceled.
        *   @param  additionalView          An additional view to append after the InputFields.
        *****************************************************************************/
        private static final void showUIThreaded
        (
            final   Activity                activity,
            final   LibDialogInputHandler[] handlers,
            final   int                     dialogLayoutID,
            final   int                     dialogContainerID,
            final   int[]                   dialogInputLayoutIDs,
            final   int[]                   inputFieldIDs,
            final   int[]                   hintStringIDs,
            final   int[]                   maxFieldLengthIDs,
            final   int                     titleID,
            final   int                     verticalSpacerID,
            final   CharSequence[]          messages,
            final   int                     buttonCaption1ID,
            final   Runnable                buttonAction1,
            final   int                     buttonCaption2ID,
            final   Runnable                buttonAction2,
            final   boolean                 cancelable,
            final   Runnable                cancelAction,
            final   View                    additionalView
        )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        show
                        (
                            activity,
                            handlers,
                            dialogLayoutID,
                            dialogContainerID,
                            dialogInputLayoutIDs,
                            inputFieldIDs,
                            hintStringIDs,
                            maxFieldLengthIDs,
                            titleID,
                            verticalSpacerID,
                            messages,
                            buttonCaption1ID,
                            buttonAction1,
                            buttonCaption2ID,
                            buttonAction2,
                            cancelable,
                            cancelAction,
                            additionalView
                        );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Shows an input dialog.
        *
        *   @param  context                 The current system context.
        *   @param  handlers                The input-handlers for all input-fields.
        *   @param  dialogLayoutID          The layout-resource-ID of the layout to use for this dialog.
        *   @param  dialogContainerID       The resource-ID of the container in the dialog-layout.
        *   @param  dialogInputLayoutIDs    The layout-resource-IDs for all input-fields.
        *   @param  inputFieldIDs           The layout-IDs for all input-fields.
        *   @param  hintStringIDs           The resource-IDs for all input-field-hints.
        *   @param  maxFieldLengthIDs       The resource-IDs for the maximum lengths for all input-fields.
        *   @param  titleID                 The string-resource-ID for this dialog's title.
        *   @param  verticalSpacerID        The resource-ID for the height of the vertical padding between texts and input-fields.
        *   @param  messages                The Strings for the labels above the input-fields.
        *                                   This array's length doesn't have to equal the length of the inputFieldIDs.
        *   @param  buttonCaption1ID        The string-resource-ID for the 1st button's caption.
        *   @param  buttonAction1           The action to perform if the 1st button is pressed.
        *   @param  buttonCaption2ID        The string-resource-ID for the 2nd button's caption.
        *   @param  buttonAction2           The action to perform if the 2nd button is pressed.
        *   @param  cancelable              Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction            The action to perform if this dialog is canceled.
        *   @param  additionalView          An additional view to append after the InputFields.
        *****************************************************************************/
        protected static final void show
        (
                    Context                 context,
                    LibDialogInputHandler[] handlers,
                    int                     dialogLayoutID,
                    int                     dialogContainerID,
                    int[]                   dialogInputLayoutIDs,
                    int[]                   inputFieldIDs,
                    int[]                   hintStringIDs,
                    int[]                   maxFieldLengthIDs,
                    int                     titleID,
                    int                     verticalSpacerID,
                    CharSequence[]          messages,
                    int                     buttonCaption1ID,
            final   Runnable                buttonAction1,
                    int                     buttonCaption2ID,
            final   Runnable                buttonAction2,
            final   boolean                 cancelable,
            final   Runnable                cancelAction,
            final   View                    additionalView
        )
        {
            //reference layout
            ViewGroup layout    = (ViewGroup)LibUI.getInflatedLayoutById( context, dialogLayoutID );
            ViewGroup container = (ViewGroup)layout.findViewById( dialogContainerID );

            //create linear layout dynamically
            //LinearLayout ll = LibUI.createLinearLayout( context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, android.R.color.transparent, LinearLayout.VERTICAL, Gravity.CENTER_HORIZONTAL|Gravity.TOP );

            //get all views from all input-field-handlers
            //View[] dialogInputViews = new View[ handlers.length ];

            //browse all InputFields
            for ( int i = 0; i < handlers.length; ++i )
            {
                //check if a label is available
                if ( i < messages.length )
                {
                    //add TextView to view - style as a dialog text
                    TextView tv = new TextView( context );
                    tv.setText( messages[ i ] );
                    tv.setTextAppearance( context, android.R.style.TextAppearance_DialogWindowTitle );
                    container.addView( tv );

                    //add spacer after TextView
                    container.addView( LibUI.createRelativeLayout( context, ViewGroup.LayoutParams.MATCH_PARENT, LibResource.getResourceDimensionInPixel( context, verticalSpacerID ), android.R.color.transparent ) );
                }

                //create view and add it to the vertical LinearLayout
                container.addView
                (
                    handlers[ i ].getDialogInputView
                    (
                        context,
                        dialogInputLayoutIDs[ i ],
                        inputFieldIDs[        i ],
                        hintStringIDs[        i ],
                        maxFieldLengthIDs[    i ],
                        ( i == handlers.length - 1 )
                    )
                );

                //add spacer after InputField
                container.addView( LibUI.createRelativeLayout( context, ViewGroup.LayoutParams.MATCH_PARENT, LibResource.getResourceDimensionInPixel( context, verticalSpacerID ), android.R.color.transparent ) );
            }

            //add checkbox if desired
            if ( additionalView != null )
            {
                //add checkbox to container
                container.addView( additionalView );

                //add spacer after CheckBox
                container.addView( LibUI.createRelativeLayout( context, ViewGroup.LayoutParams.MATCH_PARENT, LibResource.getResourceDimensionInPixel( context, verticalSpacerID ), android.R.color.transparent ) );
            }

            //create dialog builder
            AlertDialog.Builder iBuilder = new AlertDialog.Builder( context );
            iBuilder.setCancelable( cancelable );
            iBuilder.setTitle( titleID );

            //inflate and assign view
            iBuilder.setView( layout );

            //set positive button if available
            if ( buttonAction1 != null )
            {
                iBuilder.setPositiveButton
                (
                    buttonCaption1ID,
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick( DialogInterface dialog, int whichButton )
                        {
                            //perform action on close
                            buttonAction1.run();

                            //dismiss dialog
                            dialog.dismiss();
                        }
                    }
                );
            }

            //set negative button if available
            if ( buttonAction2 != null )
            {
                iBuilder.setNegativeButton
                (
                    buttonCaption2ID,
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick( DialogInterface dialog, int whichButton )
                        {
                            //perform cancel action
                            buttonAction2.run();

                            //dismiss dialog
                            dialog.dismiss();
                        }
                    }
                );
            }

            //set cancel-action if available
            if ( cancelable )
            {
                iBuilder.setOnCancelListener
                (
                    new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel( DialogInterface aDialog )
                        {
                            //perform cancel-action
                            cancelAction.run();
                        }
                    }
                );
            }

            //create and show
            AlertDialog dialog = iBuilder.create();
            dialog.show();
        }
    }
