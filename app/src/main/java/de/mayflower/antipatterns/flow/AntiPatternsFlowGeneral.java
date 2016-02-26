
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Debug.Simulations;
    import  de.mayflower.antipatterns.AntiPatternsSettings.General;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import de.mayflower.antipatterns.io.AntiPatternsSave.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.ui.*;
    import  org.json.*;
    import  android.text.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Holds data for general requests.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowGeneral
    {
        /** A reference to the activity that initiated the last update check. */
        public          static          LibState                lastStateWhoCheckedUpdate               = null;

        /**********************************************************************************************
        *   Handles the user's order to show the terms and conditions.
        *
        *   @param  state           The according state.
        *   @param  actionOnClose   The action to perform when the user closes the dialog.
        **********************************************************************************************/
        public static final void showTermsAndConditions( LibState state, Runnable actionOnClose )
        {
            try
            {
                JSONObject  response    = AntiPatternsJsonRPCGeneral.getTerms(state.getActivity());
                Spanned     terms       = Html.fromHtml( LibJSON.getJSONStringSecure( response, "terms" ) );

                //hide please wait dialog and show 'terms and conditions'-dialog
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                LibDialogDefault.showUIThreaded
                (
                    state.getActivity(),
                    R.string.dialog_terms_and_conditions_title,
                    terms,
                    R.string.dialog_terms_and_conditions_button_ok,
                    actionOnClose,
                    0,
                    null,
                    true,
                    actionOnClose
                );
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    LibDialogDefault.showUIThreaded
                    (
                        state.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        actionOnClose,
                        0,
                        null,
                        true,
                        actionOnClose
                    );
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    LibDialogDefault.showUIThreaded
                    (
                        state.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        actionOnClose,
                        0,
                        null,
                        true,
                        actionOnClose
                    );

                    //report this exception!
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Error on streaming terms" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }

        /**********************************************************************************************
        *   Handles the user's order to show the privacy policy.
        *
        *   @param  state           The according state.
        *   @param  actionOnClose   The action to perform when the user closes the dialog.
        **********************************************************************************************/
        public static final void showPrivacyPolicy( LibState state, Runnable actionOnClose )
        {
            try
            {
                JSONObject  response    = AntiPatternsJsonRPCGeneral.getPrivacyPolicy(state.getActivity());
                Spanned     pp          = Html.fromHtml( LibJSON.getJSONStringSecure( response, "privacyPolicy" ) );

                //hide please wait dialog and show 'terms and conditions'-dialog
                LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                LibDialogDefault.showUIThreaded
                (
                    state.getActivity(),
                    R.string.dialog_privacy_policy_title,
                    pp,
                    R.string.dialog_privacy_policy_button_ok,
                    actionOnClose,
                    0,
                    null,
                    true,
                    actionOnClose
                );
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //check no networks - hide please wait dialog and show 'no networks'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    LibDialogDefault.showUIThreaded
                    (
                        state.getActivity(),
                        R.string.dialog_no_network_title,
                        R.string.dialog_no_network_body,
                        R.string.dialog_no_network_button_ok,
                        actionOnClose,
                        0,
                        null,
                        true,
                        actionOnClose
                    );
                }
                else
                {
                    //hide please wait dialog and show 'technical error'
                    LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );
                    LibDialogDefault.showUIThreaded
                    (
                        state.getActivity(),
                        R.string.dialog_technical_error_title,
                        R.string.dialog_technical_error_body,
                        R.string.dialog_technical_error_button_ok,
                        actionOnClose,
                        0,
                        null,
                        true,
                        actionOnClose
                    );

                    //report this exception!
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Error on streaming privacy policy" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }

        /**********************************************************************************************
        *   Performs an implicit update check in a new Thread.
        *   This check if invoked each time, an Activity starts.
        *
        *   @param  state       The according state.
        **********************************************************************************************/
        public static final void checkAppUpdateThreaded( final LibState state )
        {
            //perform this in a separate Thread
            new Thread()
            {
                @Override
                public void run()
                {
                    //get last update timestamp
                    String  lastUpdateString = AntiPatternsSave.loadSetting(state.getActivity(), SaveKey.ELastUpdateCheck);
                    long    lastUpdateCheck  = 0;
                    long    timeDifference   = 0;

                    if ( lastUpdateString != null )
                    {
                        lastUpdateCheck = Long.parseLong( lastUpdateString );
                        timeDifference  = System.currentTimeMillis() - lastUpdateCheck;
                    }

                    boolean debugOutUpdateCheck = false;
                    if ( debugOutUpdateCheck )
                    {
                        AntiPatternsDebug.implicitUpdateCheck.out( "lastUpdateCheck   [" + lastUpdateString                      + "]" );
                        AntiPatternsDebug.implicitUpdateCheck.out( "lastUpdateCheck   [" + lastUpdateCheck                       + "]" );
                        AntiPatternsDebug.implicitUpdateCheck.out( "currentTimeMillis [" + System.currentTimeMillis()            + "]" );
                        AntiPatternsDebug.implicitUpdateCheck.out( "updateCheckPeriod [" + General.UPDATE_CHECK_PERIOD           + "]" );
                        AntiPatternsDebug.implicitUpdateCheck.out( "timeDifference    [" + timeDifference                        + "]" );
                    }

                    //check if lastUpdate is older than 1 day
                    if ( lastUpdateString == null || timeDifference > General.UPDATE_CHECK_PERIOD )
                    {
                        AntiPatternsDebug.implicitUpdateCheck.out( "Performing an app-update-check NOW!" );

                        //perform an update check
                        checkAppUpdate
                        (
                            state,
                            AntiPatternsAction.ENone,
                            AntiPatternsActionDialog.EDialogUpdateOptional,
                            AntiPatternsActionDialog.EDialogUpdateRequired,
                            AntiPatternsAction.ENone,
                            AntiPatternsAction.ENone,
                            false
                        );
                    }
                    else
                    {
                        AntiPatternsDebug.implicitUpdateCheck.out( "Next updateCheck will be performed in [" + AntiPatternsUI.formatTimeDistance(state.getActivity(), System.currentTimeMillis() - timeDifference) + "]" );
                    }
                }
            }.start();
        }

        /**********************************************************************************************
        *   Handles the user's order to check for an app update.
        *
        *   @param  state                   The according state.
        *   @param  actionOnNoUpdate        The action to perform if no update is available.
        *   @param  actionOnUpdateOptional  The action to perform if the response 'update optional' arrives.
        *   @param  actionOnUpdateRequired  The action to perform if the response 'update required' arrives.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *   @param  dismissProgressDialog   Specified, if any open progress dialog shall be closed after
        *                                   the update check has been performed.
        **********************************************************************************************/
        public static final void checkAppUpdate
        (
            LibState        state,
            Runnable        actionOnNoUpdate,
            Runnable        actionOnUpdateOptional,
            Runnable        actionOnUpdateRequired,
            Runnable        actionOnNoNetwork,
            Runnable        actionOnTechnicalError,
            boolean         dismissProgressDialog
        )
        {
            //remember last activity that performed the update-check
            lastStateWhoCheckedUpdate = state;

            //flag the update-check as performed with the current timestamp
            AntiPatternsSave.saveSetting(state.getActivity(), SaveKey.ELastUpdateCheck, String.valueOf(System.currentTimeMillis()));

            try
            {
                JSONObject  response    = AntiPatternsJsonRPCGeneral.checkUpdate();
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                AntiPatternsDebug.jsonRpc.out( "Response JSON-RPC 'checkUpdate' - status:     [" + status          + "]" );

                //simulate if desired
                if ( Simulations.SIMULATE_APP_UPDATE_REQUIRED )
                {
                    status = String.valueOf( AntiPatternsJsonRPC.ERROR_CODE_UPDATE_CHECK_UPDATE_REQUIRED );
                }

                //dismiss progress dialog
                if ( dismissProgressDialog ) LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //check OK means no update!
                        actionOnNoUpdate.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_UPDATE_CHECK_UPDATE_OPTIONAL:
                    {
                        //update is OPTIONAL
                        actionOnUpdateOptional.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_UPDATE_CHECK_UPDATE_REQUIRED:
                    {
                        //update is MANDATORY
                        actionOnUpdateRequired.run();
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(state);
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //dismiss the progress dialog
                    if ( dismissProgressDialog ) LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                    //perform action on no network
                    actionOnNoNetwork.run();
                }
                else
                {
                    //dismiss the progress dialog
                    if ( dismissProgressDialog ) LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

                    //perform action on technical error
                    actionOnTechnicalError.run();

                    //report this exception!
                    //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Error on streaming privacy policy" ), "", UncaughtException.ENo );
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }
    }
