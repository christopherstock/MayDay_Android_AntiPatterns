
    package de.mayflower.lib.api;

    import  android.annotation.SuppressLint;
    import  android.app.*;
    import  android.content.*;
    import  android.database.*;
    import  android.provider.*;
    import  android.view.*;
    import  de.mayflower.lib.*;

    /*********************************************************************************
    *   Holds functionality exclusively for devices that use API Level 5 or higher.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************/
    @SuppressLint( "NewApi" )
    public abstract class LibModernAPI5
    {
        /*********************************************************************************
        *   Enables or disabled scrollbar fading for the specified view.
        *
        *   @param  view        The view to disable or enable scrollbar fading.
        *   @param  enable      The new setting to set.
        *********************************************************************************/
        public static final void setScrollbarFadingEnabled( View view, boolean enable )
        {
            view.setScrollbarFadingEnabled( enable );
        }

        /************************************************************************
        *   Invokes the external phonebook activity from the Android system
        *   and lets the user select an entry. The result is transfered
        *   in the Activity's onActivityResult-method.
        *
        *   @param  act         The activity that performs this request.
        *   @param  requestID   The number that identifies this request.
        *                       This number is returned in onActivityResult.
        *   @param  aDebug      A debug flag to trace this operation.
        ************************************************************************/
        public static final void openPhoneBookForActivityResult( Activity act, int requestID, LibDebug aDebug )
        {
            //create intent to open the phonebook ( from API Level 5 ! )
            Intent intent = new Intent( Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI );

            aDebug.out( "launch modern phonebook intent" );

            //launch the intent as a sub-activity
            act.startActivityForResult( intent, requestID );
        }

        /************************************************************************
        *   Handles the result intent that comes from the external phonebook activity.
        *   This method must be invoked at the top of the calling activity's onActivityResult()
        *   function in order to deliver the correct result.
        *   This function performs different actions for devices with an API-level lower than 5.
        *
        *   @param  act             The activity that has received the callback.
        *   @param  resultIntent    The result intent that has been propagated to
        *                           activity's onActivityResult()-method.
        *   @return                 The picked phonenumber from the phonebook
        *                           or <code>null</code> if an error occured.
        ************************************************************************/
        @SuppressWarnings( "deprecation" )
        public static final String handlePhoneBookActivityResult( Activity act, Intent resultIntent )
        {
            try
            {
                //specify the cursor and move to the first tuple
                Cursor cursor = act.managedQuery( resultIntent.getData(), null, null, null, null );
                cursor.moveToFirst();

                //return the phonenumber
                String phonenumber = cursor.getString( cursor.getColumnIndexOrThrow( ContactsContract.CommonDataKinds.Phone.NUMBER ) );

                //close resources
                cursor.close();

                return phonenumber;
            }
            catch ( Exception e )
            {
                return null;
            }
        }

        /*********************************************************************************
        *   Sets up a transition animation from the current to a different activity.
        *   This method must be invoked AFTER the different activity has been started!
        *
        *   @param  activity    The according activity context.
        *   @param  animIn      The resource-ID of the animation for the new activity to appear.
        *   @param  animOut     The resource-ID of the animation for the old activity to disappear.
        *********************************************************************************/
        public static final void overridePendingTransition( Activity activity, int animIn, int animOut )
        {
            activity.overridePendingTransition( animIn, animOut );
        }
    }
