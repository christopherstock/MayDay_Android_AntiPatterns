
    package de.mayflower.antipatterns.io;

    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  android.content.*;

    /**********************************************************************************************
    *   Accesses the internal and persistent Setting system.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public abstract class AntiPatternsSave
    {
        /**********************************************************************************************
        *   Specifies all save-keys. Each setting needs an own key.
        *   Each key must specify a different key-value.
        **********************************************************************************************/
        public static enum SaveKey
        {
            /** Saves the received Facebook UID. */
            EFacebookUID(                                           "facebookUID"                                                       ),
            /** Saves the active user's current session ID. */
            ESessionID(                                             "sessionID"                                                         ),
            /** Saves the active user's current user ID. */
            EUserID(                                                "userID"                                                            ),
            /** Saves the active user's current user ID. */
            EGoogleCloudMessagingRegistrationID(                    "googleCloudMessagingRegistrationID" + Paramounts.PROJECT_VERSION   ),
            /** The last username the user has entered. */
            ELastLoginUsername(                                     "lastLoginUsername"                                                 ),
            /** The md5-string of the last password the user has entered. */
            ELastLoginPasswordMD5(                                  "lastLoginPasswordMD5"                                              ),

            /** Saves the information, how the state 'GooglePlaces' have been entered. */
            EStateGooglePlacesEnteredVia(                           "stateGooglePlacesEnteredVia"                                       ),
            /** Saves the information, if a GPS-scan shall be performed when state 'GooglePlaces' is entered. */
            EStateGooglePlacesScanGPSAndUpdateDataNextOnStart(      "stateGooglePlacesScanGPSAndUpdateDataNextOnStart"                  ),
            /** Saves the information, if a search-by-term shall be performed when state 'GooglePlaces' is entered. */
            EStateGooglePlacesSeachByTermAndUpdateDataNextOnStart(  "stateGooglePlacesSeachByTermAndUpdateDataNextOnStart"              ),
            /** Saves the last selected GooglePlace that has been picked by the user for state 'new entry'. */
            EStateNewEntryLastPickedGooglePlace(                    "stateNewEntryLastPickedGooglePlace"                                ),
            /** Saves the last selected image-URI that the user has selected for state 'new entry'. */
            EStateNewEntryLastPickedURI(                            "stateNewEntryLastPickedURI"                                        ),
            /** Saves the last user location that has been scanned via GPS for state 'new entry'. */
            EStateNewEntryLastUserLocation(                         "stateNewEntryLastUserLocation"                                     ),
            /** Saves, if the state 'GooglePlaces' has already been shown for the user to create a new entry in state 'new entry'. */
            EStateNewEntryShowedGooglePlacesPicker(                 "stateNewEntryShowedGooglePlacesPicker"                             ),
            /** Saves, if the dialog 'search images' shall be shown in state 'pivotal''s next onStart method. */
            EStatePivotalShowSearchDialogNextOnStart(               "statePivotalShowSearchDialogNextOnStart"                           ),
            /** Saves the last selected image-URI that the user has selected for state 'register'. */
            EStateRegisterLastPickedURI(                            "stateRegisterLastPickedURI"                                        ),
            /** Saves the last selected image-URI that the user has selected for state 'settings'. */
            EStateSettingsLastPickedURI(                            "stateSettingsLastPickedURI"                                        ),

            /** Saves, when the last update check has been performed. */
            ELastUpdateCheck(                                       "lastUpdateCheck"                                                   ),

            ;

            /**********************************************************************************************
            *   This is the key that identifies the setting. Do NOT use the name of the enum constant here,
            *   because this may cause trouble if resources are refactored!
            **********************************************************************************************/
            public              String              iString                     = null;

            /**********************************************************************************************
            *   Creates a new save key.
            *
            *    @param     aStringRepresentation   The string value for this key.
            *                                       This must be a unique value.
            **********************************************************************************************/
            private SaveKey( String aStringRepresentation )
            {
                iString = aStringRepresentation;
            }
        }

        /**********************************************************************************************
        *   Loads the value for the specified setting key.
        *
        *   @param  context     The current system context.
        *   @param  key         The setting key to load the saved value for.
        *   @return             The stored value that is associated with the specified key
        *                       or <code>null</code> if no value is stored for the specified key.
        **********************************************************************************************/
        public static final String loadSetting( Context context, SaveKey key )
        {
            SharedPreferences   settings    = context.getSharedPreferences( Paramounts.PROJECT_SPECIFIER, Context.MODE_PRIVATE );
            String              ret         = settings.getString( key.iString, null );

            return ret;
        }

        /**********************************************************************************************
        *   Saves a value for the specified setting key.
        *
        *   @param  context     The current system context.
        *   @param  key         The setting key to save the specified value with.
        *   @param  value       The value to save persistent under the specified key.
        **********************************************************************************************/
        public static final void saveSetting( Context context, SaveKey key, String value)
        {
            SharedPreferences           settings    = context.getSharedPreferences( Paramounts.PROJECT_SPECIFIER, Context.MODE_PRIVATE );
            SharedPreferences.Editor    editor      = settings.edit();

            editor.putString( key.iString, value );
            editor.commit();
        }
    }
