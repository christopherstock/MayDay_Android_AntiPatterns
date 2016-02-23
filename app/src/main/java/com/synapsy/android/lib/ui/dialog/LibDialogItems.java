/*  $Id: LibDialogItems.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.dialog;

    import  android.app.*;
    import  android.content.*;
    import  android.widget.*;

    import  com.synapsy.android.lib.*;

    /*****************************************************************************
    *   Shows a dialog with multiple items but without buttons.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/dialog/LibDialogItems.java $"
    *****************************************************************************/
    public abstract class LibDialogItems
    {
        /*****************************************************************************
        *   Orders to show an items-dialog. This method will run on the UI-Thread.
        *   
        *   @param  activity            The current system context.
        *   @param  titleID             The string-resource-ID for this dialog's title.
        *   @param  itemCaptionIDs      The string-resource-IDs for all items.
        *   @param  itemActions         The onClick-actions to assign for all items.
        *   @param  cancelable          Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction        The action to perform if this dialog is canceled.
        *****************************************************************************/
        public static final void showUIThreaded
        (
            final   Activity        activity,
            final   int             titleID,
            final   int[]           itemCaptionIDs,
            final   Runnable[]      itemActions,
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
                        show( activity, titleID, itemCaptionIDs, itemActions, cancelable, cancelAction );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Orders to show an items-dialog. Must be invoked on the UI-Thread.
        *   
        *   @param  context             The current system context.
        *   @param  titleID             The string-resource-ID for this dialog's title.
        *   @param  itemCaptionIDs      The string-resource-IDs for all items.
        *   @param  itemActions         The onClick-actions to assign for all items.
        *   @param  cancelable          Specifies if this dialog can be canceled via the back button.
        *   @param  cancelAction        The action to perform if this dialog is canceled.
        *****************************************************************************/
        protected static final void show
        (
                    Context         context,
                    int             titleID,
                    int[]           itemCaptionIDs,
            final   Runnable[]      itemActions,
                    boolean         cancelable,
            final   Runnable        cancelAction
        )
        {
            //convert IDs to captions
            String[] itemCaptions = LibResource.getResourceStrings( context, itemCaptionIDs );

            //show multiple item dialog
            ArrayAdapter<String>    adapter     = new ArrayAdapter<String> ( context, android.R.layout.select_dialog_item, itemCaptions );
            AlertDialog.Builder     builder     = new AlertDialog.Builder( context );
            builder.setTitle( titleID );
            builder.setAdapter
            (
                adapter,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int selectedIndex )
                    {
                        //perform associated action
                        itemActions[ selectedIndex ].run();
                    }
                }
            );

            //set cancelAction if cancalable
            builder.setCancelable( cancelable );
            if ( cancelable )
            {
                builder.setOnCancelListener
                (
                    new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel( DialogInterface aDialog )
                        {
                            //perform cancel action
                            cancelAction.run();
                        }
                    }
                );
            }

            //create and show
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
