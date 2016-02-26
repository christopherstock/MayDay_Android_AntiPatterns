/*  $Id: PicFoodFlowSearchUsers.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.flow;

    import  java.util.*;
    import  org.json.*;
    import  android.widget.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodSettings.InputFields;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.GridViews;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.adapter.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds the flow for the search-users system.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowSearchUsers.java $"
    **********************************************************************************************/
    public class PicFoodFlowSearchUsers
    {
        /** The dialog input handler for the dialog input field 'find friends: searchterm'. */
        public      static          LibDialogInputHandler           inputHandlerFindFriendsViaSearchTerm                = new LibDialogInputHandler();

        /** The action to perform on scrolling the find-friends GridView to the bottom. */
        public      static          Runnable                        nextReloadAction                                    = null;

        /** The last entered searchterm to search users with. */
        public      static          String                          lastUserSearchTerm                                  = null;
        /** The last submitted phone numbers to search users with. */
        public      static          String[]                        lastUserPhoneNumbers                                = null;
        /** The last submitted facebook-IDs to search users with. */
        public      static          String[]                        lastFacebookUIDs                                    = null;
        /** The last scanned facebook-ID of the current active user. */
        public      static          String                          lastOwnFacebookID                                   = null;

        /** The total number of user-results that can be ordered. */
        protected   static          int                             searchCount                                         = 0;
        /** All user-data that is currently displayed in the find-friends-results state. */
        protected   static          Vector<LibAdapterData>          searchData                                          = new Vector<LibAdapterData>();
        /** The current offset for the user-search data to order. */
        protected   static          int                             searchOffset                                        = 0;

        /** The user-search-results that have been returned with the last user-search. */
        protected   static          PicFoodDataUser[]               newUsers                                            = new PicFoodDataUser[] {};

        /** The loading content item that is currently displayed on the bottom of the scrolling list. */
        protected   static          PicFoodGridViewContentLoading   lastBottomLoadingItem                               = null;

        /**********************************************************************************************
        *   Performs a search for users with a specified search term.
        *
        *   @param  state                   The according state.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *   @param  actionOnNoResults       The action to perform if no results are returned.
        *   @param  actionOnResults         The action to perform if results are returned.
        **********************************************************************************************/
        public static final void orderNextFriendsSearchViaSearchTerm
        (
            LibState        state,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError,
            Runnable        actionOnNoResults,
            Runnable        actionOnResults
        )
        {
            //check if term is empty or too short
            if ( LibString.isEmpty( lastUserSearchTerm ) || LibString.isShorterThan( lastUserSearchTerm, InputFields.MIN_INPUT_LENGTH ) )
            {
                //hide please wait dialog and show 'search term required'
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                PicFoodActionDialog.EDialogFindFriendsViaSearchTermInvalidTerm.run();

                //abort search
                return;
            }

            try
            {
                //output before ordering
                PicFoodDebug.limitOffset.out
                (
                        "BEFORE ordering friends-search via searchTerm"
                    +   " searchUsers       [" + searchData.size()  + "] "
                    +   " searchOffset      [" + searchOffset       + "] "
                    +   " searchCount       [" + searchCount        + "] "
                );

                //commit search
                JSONObject  response    = PicFoodJsonRPCSearch.searchUsersByTerm
                (
                    state.getActivity(),
                    lastUserSearchTerm,
                    searchOffset
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse users from response
                        JSONObject                  users       = LibJSON.getJSONObjectSecure( response,    "users"  );
                                                    newUsers    = PicFoodDataUser.parse( users );

                        PicFoodDebug.findFriends.out( "Parsed [" + newUsers.length + "] users from response" );

                        //increase offset, assign count and add to stack
                        searchOffset += newUsers.length;
                        searchCount  = LibJSON.getJSONIntegerSecure( users, "count" );

                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //check user size
                        if ( newUsers.length == 0 )
                        {
                            //show 'no results'
                            actionOnNoResults.run();
                        }
                        else
                        {
                            //show results
                            actionOnResults.run();
                        }
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( state );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        actionOnTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Performs a search for users with the specified Facebook-UIDs.
        *   The uid of the own user is transferred too.
        *
        *   @param  state                   The according state.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *   @param  actionOnNoResults       The action to perform if no results are returned.
        *   @param  actionOnResults         The action to perform if results are returned.
        **********************************************************************************************/
        public static final void orderNextFriendsSearchViaFacebookIDs
        (
            LibState        state,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError,
            Runnable        actionOnNoResults,
            Runnable        actionOnResults
        )
        {
            try
            {
                PicFoodDebug.findFriends.out( PicFoodFlowSearchImages.class + "::orderNextFriendsSearchViaFacebookIDs - ownFacebookID [" + lastOwnFacebookID + "] facebook-uids [" + lastFacebookUIDs.length + "] elements" );

                //output before ordering
                PicFoodDebug.limitOffset.out
                (
                        "BEFORE ordering friends-search via Facebook"
                    +   " searchUsers       [" + searchData.size()  + "] "
                    +   " searchOffset      [" + searchOffset       + "] "
                    +   " searchCount       [" + searchCount        + "] "
                );

                //commit search
                JSONObject  response    = PicFoodJsonRPCSearch.searchUsersByFacebookID
                (
                    state.getActivity(),
                    lastFacebookUIDs,
                    lastOwnFacebookID,
                    searchOffset
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse users from response
                        JSONObject                  users       = LibJSON.getJSONObjectSecure( response,    "users"  );
                                                    newUsers    = PicFoodDataUser.parse( response );

                        PicFoodDebug.findFriends.out( "Parsed [" + newUsers.length + "] users from response" );

                        //increase offset, assign count and add to stack
                        searchOffset += newUsers.length;
                        searchCount  = LibJSON.getJSONIntegerSecure( users, "count" );

                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //check user size
                        if ( newUsers.length == 0 )
                        {
                            //show 'no results'
                            actionOnNoResults.run();
                        }
                        else
                        {
                            //show results
                            actionOnResults.run();
                        }
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( state );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        actionOnTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Performs a search for users with all phonenumbers of his phonebook.
        *
        *   @param  state                   The according state.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *   @param  actionOnNoResults       The action to perform if no results are returned.
        *   @param  actionOnResults         The action to perform if results are returned.
        **********************************************************************************************/
        public static final void orderNextFriendSearchViaPhonenumber
        (
            LibState        state,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError,
            Runnable        actionOnNoResults,
            Runnable        actionOnResults
        )
        {
            try
            {
                PicFoodDebug.findFriends.out( "Requesting search with [" + lastUserPhoneNumbers.length + "] phone-numbers" );

                //output before ordering
                PicFoodDebug.limitOffset.out
                (
                        "BEFORE ordering friends-search via phoneNumber"
                    +   " searchUsers       [" + searchData.size()  + "] "
                    +   " searchOffset      [" + searchOffset       + "] "
                    +   " searchCount       [" + searchCount        + "] "
                );

                //commit search
                JSONObject  response    = PicFoodJsonRPCSearch.searchUsersByPhonenumber
                (
                    state.getActivity(),
                    lastUserPhoneNumbers,
                    searchOffset
                );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //parse users from response
                        JSONObject                  users       = LibJSON.getJSONObjectSecure( response,    "users"  );
                                                    newUsers    = PicFoodDataUser.parse( users );

                        PicFoodDebug.findFriends.out( "Parsed [" + newUsers.length + "] users from response" );

                        //increase offset, assign count and add to stack
                        searchOffset += newUsers.length;
                        searchCount  = LibJSON.getJSONIntegerSecure( users, "count" );

                        //dismiss progress dialog
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                        //check user size
                        if ( newUsers.length == 0 )
                        {
                            //show 'no results'
                            actionOnNoResults.run();
                        }
                        else
                        {
                            //show results
                            actionOnResults.run();
                        }
                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( state );
                        break;
                    }

                    default:
                    {
                        //dismiss progress dialog and show 'technical error'
                        LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                        actionOnTechnicalError.run();

                        //report this error
                        PicFoodDebug.DEBUG_THROWABLE( new LibInternalError( "Invalid JsonRPC response [" + response + "]" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //dismiss progress dialog and show 'no network'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss progress dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    actionOnTechnicalError.run();

                    //report this exception
                    //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Throwable being raised on removing user" ), "Invalid JsonRPC-Response!", UncaughtException.ENo );
                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /**********************************************************************************************
        *   Sets up the 'find-friends-results'-state with the resulting users.
        *
        *   @param  overlayIcon     The View that holds the state's overlay icon.
        *                           This ImageView can display a spinning loading circle,
        *                           a clickable 'no network' icon or nothing at all.
        **********************************************************************************************/
        public static final void updateSearchUsersResultsThreaded( final ImageView overlayIcon )
        {
            //show loading circle
            if ( overlayIcon != null ) PicFoodLoadingCircle.showAndStartLoadingCircleUIThreaded( PicFoodState.EFindFriendsResults.getActivity(), overlayIcon );

            //perform threaded!
            new Thread()
            {
                @Override
                public void run()
                {
                    //prune existent loading-items
                    PicFoodUI.pruneLoadingItems( searchData );

                    //create new adapter data
                    for ( PicFoodDataUser user : newUsers )
                    {
                        //create new item
                        LibAdapterData newItem = new PicFoodGridViewContent
                        (
                            PicFoodState.EFindFriendsResults,
                            user,
                            PicFoodActionPush.EPushListEntryFindFriendsResults,
                            GridViews.EFindFriends
                        );

                        //order image threaded
                        user.orderImageThreaded( PicFoodState.EFindFriendsResults );

                        //add new item to adapter
                        searchData.addElement( newItem );
                    }

                    //output after ordering
                    PicFoodDebug.limitOffset.out
                    (
                            "AFTER ordering friends-search via searchTerm"
                        +   " searchUsers       [" + searchData.size()   + "] "
                        +   " searchOffset      [" + searchOffset       + "] "
                        +   " searchCount       [" + searchCount        + "] "
                    );

                    //check if more users can be loaded
                    Runnable actionOnBottomReach = null;
                    if ( searchOffset < searchCount )
                    {
                        PicFoodDebug.limitOffset.out( "Set loading item on last position" );

                        //add loading icon
                        lastBottomLoadingItem = new PicFoodGridViewContentLoading( PicFoodState.EFindFriendsResults, true, true );
                        searchData.addElement( lastBottomLoadingItem );

                        //set action to perform on scrolling to the bottom
                        actionOnBottomReach = nextReloadAction;
                    }

                    //hide the loading circle and assign new adapter data to GridView 'FindFriends'
                    if ( overlayIcon != null ) PicFoodLoadingCircle.removeLoadingCircleUIThreaded( PicFoodState.EFindFriendsResults.getActivity(), overlayIcon );
                    PicFoodAdapterManager.getSingleton( PicFoodState.EFindFriendsResults.getActivity(), GridViews.EFindFriends ).changeDataUIThreaded( PicFoodState.EFindFriendsResults, searchData, actionOnBottomReach );
                }
            }.start();
        }

        /**********************************************************************************************
        *   Changes the loading item {@link #lastBottomLoadingItem} on the bottom of the scrolling list
        *   to a 'no network' icon and assigns the specified OnClick-action.
        *
        *   @param  actionOnClick   The action to perform when the 'no network' icon is clicked.
        **********************************************************************************************/
        public static final void changeNextLoadingItemToNoNetwork( final Runnable actionOnClick )
        {
            lastBottomLoadingItem.setupAndChangeToNoNetwork( PicFoodState.EFindFriendsResults, GridViews.EFindFriends, actionOnClick );
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the user-search-results is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            searchCount     = 0;
            searchOffset    = 0;
            searchData.removeAllElements();
        }
    }
