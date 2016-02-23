/*  $Id: LibDialogImageButtons.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.dialog;

    import  android.app.*;
    import  android.content.*;
    import  android.view.*;
    import  android.widget.*;

    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.ui.*;

    /*****************************************************************************
    *   Handles a dialog that contains several image-buttons.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/dialog/LibDialogImageButtons.java $"
    *****************************************************************************/
    public abstract class LibDialogImageButtons
    {
        /** References the last shown instance of this dialog. */
        protected   static      AlertDialog             lastDialogImageButtons          = null;

        /*****************************************************************************
        *   Orders to shows a dialog with image-buttons UI-threaded.
        *
        *   @param  activity            The according activity context.
        *   @param  titleID             The string-resource-ID for this dialog's title.
        *   @param  messageID           The string-resource-ID for this dialog's message body.
        *   @param  buttonCaption1ID    The string-resource-ID for the 1st button's caption.
        *   @param  buttonAction1       The action to perform if the 1st button is pressed.
        *   @param  imageIDs            The resource-IDs of all images to use as the buttons.
        *   @param  actions             The actions for the buttons to set.
        *   @param  buttonSizeID        The resource-IDs for the dimensions of the buttons.
        *   @param  viewLayoutID        The resource-ID for the view-layout to use for this dialog.
        *   @param  textContainerID     The resource-ID of the text container inside the view-layout.
        *   @param  buttonContainerID   The resource-ID of the horizontal button container inside the view-layout.
        *   @param  cancelable          Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction        The action to perform if this dialog is canceled.
        *****************************************************************************/
        public static final void showUIThreaded
        (
            final   Activity        activity,
            final   int             titleID,
            final   int             messageID,
            final   int             buttonCaption1ID,
            final   Runnable        buttonAction1,
            final   int[]           imageIDs,
            final   Runnable[]      actions,
            final   int             buttonSizeID,
            final   int             viewLayoutID,
            final   int             textContainerID,
            final   int             buttonContainerID,
            final   boolean         cancelable,
            final   Runnable        cancelAction
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
                            titleID,
                            messageID,
                            buttonCaption1ID,
                            buttonAction1,
                            imageIDs,
                            actions,
                            buttonSizeID,
                            viewLayoutID,
                            textContainerID,
                            buttonContainerID,
                            cancelable,
                            cancelAction
                        );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Shows an image-button-dialog.
        *
        *   @param  context             The current system context.
        *   @param  titleID             The string-resource-ID for this dialog's title.
        *   @param  messageID           The string-resource-ID for this dialog's message body.
        *   @param  buttonCaption1ID    The string-resource-ID for the 1st button's caption.
        *   @param  buttonAction1       The action to perform if the 1st button is pressed.
        *   @param  imageIDs            The resource-IDs of all images to use as the buttons.
        *   @param  actions             The actions for the buttons to set.
        *   @param  buttonSizeID        The resource-IDs for the dimensions of the buttons.
        *   @param  viewLayoutID        The resource-ID for the view-layout to use for this dialog.
        *   @param  textContainerID     The resource-ID of the text container inside the view-layout.
        *   @param  buttonContainerID   The resource-ID of the horizontal button container inside the view-layout.
        *   @param  cancelable          Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction        The action to perform if this dialog is canceled.
        *****************************************************************************/
        protected static final void show
        (
                    Context         context,
                    int             titleID,
                    int             messageID,
                    int             buttonCaption1ID,
            final   Runnable        buttonAction1,
            final   int[]           imageIDs,
            final   Runnable[]      actions,
            final   int             buttonSizeID,
            final   int             viewLayoutID,
            final   int             textContainerID,
            final   int             buttonContainerID,
            final   boolean         cancelable,
            final   Runnable        cancelAction
        )
        {
            //create dialog builder
            AlertDialog.Builder iBuilder = new AlertDialog.Builder( context );
            iBuilder.setCancelable( cancelable );
            iBuilder.setTitle( titleID );

            //reference view
            ViewGroup layout        = (ViewGroup)LibUI.getInflatedLayoutById( context, viewLayoutID );
            ViewGroup textContainer = (ViewGroup)layout.findViewById( textContainerID );

            //add TextView to view - style as a dialog text
            TextView tv = new TextView( context );
            tv.setText( LibResource.getResourceString( context, messageID ) );
            tv.setTextAppearance( context, android.R.style.TextAppearance_DialogWindowTitle );
            textContainer.addView( tv );

            //add buttons
            {
                ViewGroup buttonView = (ViewGroup)layout.findViewById( buttonContainerID );
                for ( int i = 0; i < imageIDs.length; ++i )
                {
                    ImageButton ib = LibUI.createImageButton( context, imageIDs[ i ], actions[ i ], buttonSizeID );
                    buttonView.addView( ib );
                }
            }

            //assign view
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
            lastDialogImageButtons = iBuilder.create();
            lastDialogImageButtons.show();
        }

        /*****************************************************************************
        *   Dismisses the last instance of this dialog, if present.
        *
        *   @param  activity            The according activity context.
        *****************************************************************************/
        public static final void dismissLastDialogImageButtonsUIThreaded( Activity activity )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( lastDialogImageButtons != null )
                        {
                            lastDialogImageButtons.dismiss();
                            lastDialogImageButtons = null;
                        }
                    }
                }
            );
        }
    }
