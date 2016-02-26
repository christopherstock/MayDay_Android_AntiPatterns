/*  $Id: LibDialogProgress.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui.dialog;

    import  android.app.*;
    import  android.content.*;
    import de.mayflower.lib.*;

    /************************************************************************
    *   Creates and shows a progress dialog.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/dialog/LibDialogProgress.java $"
    ************************************************************************/
    public abstract class LibDialogProgress
    {
        /** References the last shown instance of this dialog. */
        protected       static          ProgressDialog      progressDialog              = null;

        /*****************************************************************************
        *   Orders to shows a progress dialog UI-threaded.
        *
        *   @param  activity                The according activity context.
        *   @param  titleID                 The string-resource-ID for this dialog's title.
        *   @param  messageID               The string-resource-ID for this dialog's message body.
        *   @param  actionOnDialogVisible   The action to perform when this dialog is visible.
        *                                   CAUTION! This action is performed on the UI-Thread!
        *   @param  cancelable              Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction            The action to perform if this dialog is canceled.
        *****************************************************************************/
        public static final void showProgressDialogUIThreaded
        (
            final   Activity        activity,
            final   int             titleID,
            final   int             messageID,
            final   Runnable        actionOnDialogVisible,
            final   boolean         cancelable,
            final   Runnable        cancelAction
        )
        {
            showProgressDialogUIThreaded
            (
                activity,
                LibResource.getResourceSpannedString( activity, titleID ),
                LibResource.getResourceSpannedString( activity, messageID ),
                actionOnDialogVisible,
                cancelable,
                cancelAction
            );
        }

        /*****************************************************************************
        *   Orders to shows a progress dialog UI-threaded.
        *
        *   @param  activity                The according activity context.
        *   @param  title                   The string for this dialog's title.
        *   @param  message                 The string for this dialog's message body.
        *   @param  actionOnDialogVisible   The action to perform when this dialog is visible.
        *   @param  cancelable              Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction            The action to perform if this dialog is canceled.
        *****************************************************************************/
        private static final void showProgressDialogUIThreaded
        (
            final   Activity        activity,
            final   CharSequence    title,
            final   CharSequence    message,
            final   Runnable        actionOnDialogVisible,
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
                        //only if the progress-dialog is not shown
                        if ( progressDialog == null || !progressDialog.isShowing() )
                        {
                            //show the progress dialog
                            progressDialog = new ProgressDialog( activity );
                            progressDialog.setTitle(        title       );
                            progressDialog.setMessage(      message     );
                            progressDialog.setCancelable(   cancelable  );
                            progressDialog.show();

                            //assign cancel-listener if desired
                            if ( cancelable )
                            {
                                progressDialog.setOnCancelListener
                                (
                                    new DialogInterface.OnCancelListener()
                                    {
                                        @Override
                                        public void onCancel( DialogInterface aDialog )
                                        {
                                            dismissProgressDialogUIThreaded( activity );
                                            cancelAction.run();
                                        }
                                    }
                                );
                            }

                            //perform runnable when the dialog is shown - this is invoked on the UI-Thread!!
                            actionOnDialogVisible.run();
                        }
                    }
                }
            );
        }

        /*****************************************************************************
        *   Hides the current displayed progress-dialog, if any.
        *
        *   @param  activity                The according activity context.
        *****************************************************************************/
        public static final void dismissProgressDialogUIThreaded( Activity activity )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( progressDialog != null )
                        {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                }
            );
        }

        /*****************************************************************************
        *   Alters the title and the message for the current shown progress-dialog, if any.
        *
        *   @param  activity        The according activity context.
        *   @param  title           The new title to set.
        *   @param  message         The new message-body to set.
        *****************************************************************************/
        public static final void changeProgressDialogUIThreaded( Activity activity, final CharSequence title, final CharSequence message )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //only if the progress-dialog is shown
                        if ( progressDialog != null && progressDialog.isShowing() )
                        {
                            //show the progress dialog
                            if ( title   != null ) progressDialog.setTitle(        title   );
                            if ( message != null ) progressDialog.setMessage(      message );
                        }
                    }
                }
            );
        }
    }
