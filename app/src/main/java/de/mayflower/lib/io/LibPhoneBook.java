
    package de.mayflower.lib.io;

    import  java.util.*;
    import  android.app.*;
    import  android.content.*;
    import  android.database.*;
    import  android.provider.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.api.*;

    /************************************************************************
    *   Manages access to the phonebook. Includes deprecated fields though
    *   supporting all sdk levels.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class LibPhoneBook
    {
        /************************************************************************
        *   Reads and returns all phonenumber the user has stored in his phonebook.
        *
        *   @param  act     The current context.
        *   @return         A string-array containing all phonenumbers
        *                   that have been read from the user's phonebook.
        ************************************************************************/
        public static final String[] readAllPhonenumbersFromAllContacts( Activity act )
        {
            try
            {
                Vector<String>  ret     = new Vector<String>();
                Cursor          phones  = act.getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null );

                while ( phones.moveToNext() )
                {
                  //String name         = phones.getString( phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME ) );
                    String phoneNumber  = phones.getString( phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER ) );

                    ret.addElement( phoneNumber );
                }
                phones.close();

                return ret.toArray( new String[] {} );
            }
            catch ( Throwable t )
            {
                return new String[] {};
            }
        }

        /************************************************************************
        *   Opens the external 'phonebook' activity. The Activity's onActivityResult-method
        *   is invoked when the user has selected a phonebook-entry.
        *   This function performs different actions for devices with an API-level lower than 5.
        *
        *   @param  act         The activity that performs this request.
        *   @param  requestID   A result-id that is returned in the activity's
        *                       onActivityResult method and will determine the result
        *                       as a callback from the phonebook system.
        *   @param  aDebug      A debug flag to trace this function.
        ************************************************************************/
        public static final void openPhoneBookForActivityResult( Activity act, int requestID, LibDebug aDebug )
        {
            if ( LibAPI.isSdkLevelLower5() )
            {
                aDebug.out( "Open obsolete phonebook for sdk lower 5" );
                LibPhoneBookObsolete.openPhoneBookForActivityResult( act, requestID, aDebug );
            }
            else
            {
                aDebug.out( "Open modern phonebook for sdk 5 or higher" );
                LibModernAPI5.openPhoneBookForActivityResult( act, requestID, aDebug );
            }
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
        public static final String handlePhoneBookActivityResult( Activity act, Intent resultIntent )
        {
            String ret = null;

            if ( LibAPI.isSdkLevelLower5() )
            {
                ret = LibPhoneBookObsolete.handlePhoneBookActivityResult( act, resultIntent );
            }
            else
            {
                ret = LibModernAPI5.handlePhoneBookActivityResult( act, resultIntent );
            }

            return ret;
        }

        /************************************************************************
        *   Wraps the external phonebook-activity for devices with an API-level lower than 5.
        ************************************************************************/
        @SuppressWarnings( "deprecation" )
        private static final class LibPhoneBookObsolete
        {
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
            protected static final void openPhoneBookForActivityResult( Activity act, int requestID, LibDebug aDebug )
            {
                //create intent to open the phonebook
                Intent intent = new Intent( Intent.ACTION_PICK, Contacts.Phones.CONTENT_URI );

                aDebug.out( "launch obsolete phonebook intent" );

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
            protected static final String handlePhoneBookActivityResult( Activity act, Intent resultIntent )
            {
                try
                {
                    //specify the cursor and move to the first tuple
                    Cursor cursor = act.managedQuery( resultIntent.getData(), null, null, null, null );
                    cursor.moveToFirst();

                    //return the phonenumber
                    String phonenumber = cursor.getString( cursor.getColumnIndexOrThrow( Contacts.PhonesColumns.NUMBER ) );

                    //close resources
                    cursor.close();

                    return phonenumber;
                }
                catch ( Exception e )
                {
                    return null;
                }
            }
        }
/*
        public static final void readAllParametersOfAContact( Activity act, Intent intent )
        {
            Cursor cursor = act.managedQuery( intent.getData(), null, null, null, null );
            while ( cursor.moveToNext() )
            {
                String contactId = cursor.getString( cursor.getColumnIndex( ContactsContract.Contacts._ID ) );
                String name = cursor.getString( cursor.getColumnIndexOrThrow( ContactsContract.Contacts.DISPLAY_NAME ) );
                String hasPhone = cursor.getString( cursor.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER ) );

                if ( hasPhone.equalsIgnoreCase("1"))
                   hasPhone = "true";
                else
                   hasPhone = "false" ;

                if ( Boolean.parseBoolean( hasPhone ) )
                {
                    Cursor phones = act.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    while (phones.moveToNext())
                    {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        AppsDebugSystem.bugfix.out( "Phone Number: [" + phoneNumber + "]" );
                    }
                    phones.close();
                }

                //Find Email Addresses
                Cursor emails = act.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                while (emails.moveToNext())
                {
                    String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();

                Cursor address = act.getContentResolver().query
                (
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                    null, null
                );

                while ( address.moveToNext() )
                {
                    // These are all private class variables, don't forget to create them.
                    String poBox      = address.getString( address.getColumnIndex( ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                    String street     = address.getString( address.getColumnIndex( ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                    String city       = address.getString( address.getColumnIndex( ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                    String state      = address.getString( address.getColumnIndex( ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                    String postalCode = address.getString( address.getColumnIndex( ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                    String country    = address.getString( address.getColumnIndex( ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                    String type       = address.getString( address.getColumnIndex( ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                }
            }
            cursor.close();
        }
*/
    }
