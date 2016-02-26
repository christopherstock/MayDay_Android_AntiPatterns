/*  $Id: LibDialogDefault.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui.dialog;

    import  android.app.*;
    import  android.content.*;

    import de.mayflower.lib.*;

    /*****************************************************************************
    *   All dialogs the application makes use of. Dialogs must be shown on the ui-Thread.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/dialog/LibDialogDefault.java $"
    *****************************************************************************/
    public abstract class LibDialogDefault
    {
        /*****************************************************************************
        *   Orders to shows a default dialog UI-threaded.
        *
        *   @param  activity            The according activity context.
        *   @param  titleID             The string-resource-ID for this dialog's title.
        *   @param  messageID           The string-resource-ID for this dialog's message body.
        *   @param  buttonCaption1ID    The string-resource-ID for the 1st button's caption.
        *   @param  buttonAction1       The action to perform if the 1st button is pressed.
        *   @param  buttonCaption2ID    The string-resource-ID for the 2nd button's caption.
        *   @param  buttonAction2       The action to perform if the 2nd button is pressed.
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
            final   int             buttonCaption2ID,
            final   Runnable        buttonAction2,
            final   boolean         cancelable,
            final   Runnable        cancelAction
        )
        {
            showUIThreaded
            (
                activity,
                titleID,
                LibResource.getResourceSpannedString( activity, messageID ).toString(),
                buttonCaption1ID,
                buttonAction1,
                buttonCaption2ID,
                buttonAction2,
                cancelable,
                cancelAction
            );
        }

        /*****************************************************************************
        *   Orders to shows a default dialog UI-threaded.
        *
        *   @param  activity            The according activity context.
        *   @param  titleID             The string-resource-ID for this dialog's title.
        *   @param  message             The string to set for this dialog's message body.
        *   @param  buttonCaption1ID    The string-resource-ID for the 1st button's caption.
        *   @param  buttonAction1       The action to perform if the 1st button is pressed.
        *   @param  buttonCaption2ID    The string-resource-ID for the 2nd button's caption.
        *   @param  buttonAction2       The action to perform if the 2nd button is pressed.
        *   @param  cancelable          Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction        The action to perform if this dialog is canceled.
        *****************************************************************************/
        public static final void showUIThreaded
        (
            final   Activity        activity,
            final   int             titleID,
            final   CharSequence    message,
            final   int             buttonCaption1ID,
            final   Runnable        buttonAction1,
            final   int             buttonCaption2ID,
            final   Runnable        buttonAction2,
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
                            message,
                            buttonCaption1ID,
                            buttonAction1,
                            buttonCaption2ID,
                            buttonAction2,
                            cancelable,
                            cancelAction
                        );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Shows a default dialog.
        *
        *   @param  context             The current system context.
        *   @param  titleID             The string-resource-ID for this dialog's title.
        *   @param  message             The string to set for this dialog's message body.
        *   @param  buttonCaption1      The string for the 1st button's caption.
        *   @param  buttonAction1       The action to perform if the 1st button is pressed.
        *   @param  buttonCaption2      The string for the 2nd button's caption.
        *   @param  buttonAction2       The action to perform if the 2nd button is pressed.
        *   @param  cancelable          Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction        The action to perform if this dialog is canceled.
        *****************************************************************************/
        protected static final void show
        (
                    Context         context,
                    int             titleID,
                    CharSequence    message,
                    int             buttonCaption1,
            final   Runnable        buttonAction1,
                    int             buttonCaption2,
            final   Runnable        buttonAction2,
            final   boolean         cancelable,
            final   Runnable        cancelAction
        )
        {
            //create dialog builder
            AlertDialog.Builder iBuilder = new AlertDialog.Builder( context );
            iBuilder.setCancelable( cancelable );
            iBuilder.setTitle( titleID );

            //set message
            iBuilder.setMessage( message );

            //set positive button if available
            if ( buttonAction1 != null )
            {
                iBuilder.setPositiveButton
                (
                    buttonCaption1,
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
                    buttonCaption2,
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
