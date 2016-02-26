
    package de.mayflower.antipatterns.flow;

    import  java.io.*;
    import  java.net.*;
    import  java.util.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.API.GooglePlaces;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.AntiPatternsSave.SaveKey;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.*;
    import  org.apache.http.*;
    import  org.apache.http.client.*;
    import  org.apache.http.client.methods.*;
    import  org.apache.http.util.*;
    import  org.json.*;
    import  android.location.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.adapter.*;
    import  de.mayflower.lib.ui.dialog.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /*****************************************************************************
    *   Performs requests to the Google Places API.
    *   {@link "https://developers.google.com/places/documentation/?hl=de"}
    *
    *   The maximum number of results that can be picked from the Google places API is 60.
    *   Each request will deliver up to 20 results.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class AntiPatternsFlowGooglePlaces
    {
        /**********************************************************************************************
        *   The last received token from a Google places response that must be set in
        *   order to receive the next 20 results of the same request.
        **********************************************************************************************/
        private             static          String                          placesNextPageToken         = null;

        /** All adapter data that is currently displayed in the google places state. */
        private             static          Vector<LibAdapterData>          placesData                  = new Vector<LibAdapterData>();

        /** The loading content item that is currently displayed on the bottom of the scrolling list. */
        private             static AntiPatternsGridViewContentLoading lastBottomLoadingItem       = null;

        /*****************************************************************************
        *   Performs a Google Places search with the last scanned GPS location.
        *
        *   @param  currentLocation         The location to perform a Google places location-search with.
        *   @param  actionOnNoResults       The action to perform if no results are returned.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *****************************************************************************/
        public static final void loadNextResultsFromLastGPSLocation
        (
            Location    currentLocation,
            Runnable    actionOnNoResults,
            Runnable    actionOnNoNetwork,
            Runnable    actionOnTechnicalError
        )
        {
            AntiPatternsDebug.googlePlaces.out( "Searching via GooglePlaces-API from last scanned GPS location" );

            //get Location from GPS
            AntiPatternsDataLocation lastUserLocation = new AntiPatternsDataLocation( currentLocation );
            AntiPatternsDebug.googlePlaces.out( "Using current location: [" + lastUserLocation.iLatitude + "," + lastUserLocation.iLongitude + "]" );

            //save location persistent
            AntiPatternsSave.saveSetting(AntiPatternsState.EGooglePlaces.getActivity(), SaveKey.EStateNewEntryLastUserLocation, LibIO.serializableToString(lastUserLocation));

            try
            {
                //connect via get
                HttpClient  httpClient  = AntiPatternsHttp.getEnrichedHttpClient();
                HttpGet     httpGet     = new HttpGet
                (
                                                                AntiPatternsProject.API.GooglePlaces.GOOGLE_PLACES_API_URL_SEARCH_NEARBY
                    +   "?" +   "key"           +   "="     +   AntiPatternsProject.API.GooglePlaces.GOOGLE_PLACES_API_BROWSER_KEY

                    +   "&" +   "location"      +   "="     +   lastUserLocation.iLatitude + "," + lastUserLocation.iLongitude
                    +   "&" +   "sensor"        +   "="     +   "true"
                    +   "&" +   "rankby"        +   "="     +   "distance"
                    +   "&" +   "types"         +   "="     +   GooglePlaces.GOOGLE_PLACES_TYPES                // 'types' does NOT seem optional - returns BAD REQUEST if missing

                        //set nextPageToken if available
                    +   (
                                placesNextPageToken == null || LibString.isEmpty( placesNextPageToken )
                            ?   ""
                            :   "&pagetoken=" + placesNextPageToken
                        )

                    //+   "&" +   "radius"        +   "="     +   "1000"
                    //+   "&" +   "name"          +   "="     +   "harbour"
                    //+   "&" +   "nextPageToken" +   "="     +   "{token-value}"
                );

                //connect
                AntiPatternsDebug.googlePlaces.out( "Connecting Google places API [" + httpGet.getURI() + "]" );
                HttpResponse    httpResponse    = httpClient.execute( httpGet );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                AntiPatternsDebug.googlePlaces.out( "Received StatusCode [" + statusCode + "]" );

                //get body
                HttpEntity      entity          = httpResponse.getEntity();
                String          body            = EntityUtils.toString( entity );

                AntiPatternsDebug.googlePlaces.out( "Received ResponseBody [" + body + "]" );

                //assign nextPageToken
                JSONObject      obj             = new JSONObject( body );
                placesNextPageToken = LibJSON.getJSONStringSecure( obj, "next_page_token" );
                AntiPatternsDebug.googlePlaces.out( "Picked nextPageToken [" + placesNextPageToken + "]" );

                //parse body
                Vector<AntiPatternsDataGooglePlace> googlePlaces = AntiPatternsDataGooglePlace.parse
                        (
                                body,
                                currentLocation
                        );

                AntiPatternsDebug.googlePlaces.out( "Parsed [" + googlePlaces.size() + "] results" );

                //prune existent loading-items
                AntiPatternsUI.pruneLoadingItems(placesData);

                //create adapter data
                for ( AntiPatternsDataGooglePlace googlePlace : googlePlaces )
                {
                    //create new item
                    LibAdapterData newItem = new AntiPatternsGridViewContent
                    (
                        AntiPatternsState.EGooglePlaces,
                        googlePlace,
                        AntiPatternsActionPush.EPushListEntryGooglePlaceNewEntry,
                        GridViews.EGooglePlaces
                    );

                    //order image threaded
                    googlePlace.orderImageThreaded( AntiPatternsState.EGooglePlaces );

                    //add new item to stack
                    placesData.addElement( newItem );
                }

                //check if more places can be loaded
                Runnable actionOnBottomReach = null;
                if ( placesNextPageToken != null && !LibString.isEmpty( placesNextPageToken ) )
                {
                    AntiPatternsDebug.googlePlaces.out( "Set loading item on last position" );

                    //add loading icon
                    lastBottomLoadingItem = new AntiPatternsGridViewContentLoading( AntiPatternsState.EGooglePlaces, true, true );
                    placesData.addElement( lastBottomLoadingItem );

                    //set action to perform on scrolling to the bottom
                    actionOnBottomReach = AntiPatternsAction.EGooglePlacesLoadAfterGPSSuccessNext;
                }

                //assign results and dismiss the progress dialog
                AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces).changeDataUIThreaded( AntiPatternsState.EGooglePlaces, placesData, actionOnBottomReach );
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );

                //show 'no results'-dialog if no results have been received
                if ( googlePlaces.isEmpty() )
                {
                    actionOnNoResults.run();
                }
            }
            catch ( IOException ioe )
            {
                //PicFoodDebug.googlePlaces.out( "IO-Exception occured on using GooglePlaces-API!" );
                //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Usage of Google-Places-API threw an Exception" ), "", UncaughtException.ENo );

                //dismiss progress dialog and show 'no network'
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );
                actionOnNoNetwork.run();
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.googlePlaces.out( "Using the GooglePlaces-API threw an Exception!" );
                //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Usage of Google-Places-API threw an Exception" ), "", UncaughtException.ENo );
                AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);

                //dismiss progress dialog
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );
                actionOnTechnicalError.run();
            }
        }

        /*****************************************************************************
        *   Performs a Google Places search with the last entered searchterm.
        *
        *   @param  term                    The searchterm to perform this search with.
        *   @param  actionOnNoResults       The action to perform if no results are returned.
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *****************************************************************************/
        public static final void loadNextResultsFromSearchTerm
        (
            String      term,
            Runnable    actionOnNoResults,
            Runnable    actionOnNoNetwork,
            Runnable    actionOnTechnicalError
        )
        {
            AntiPatternsDebug.googlePlaces.out( "Invoking the GooglePlaces-API with searchTerm [" + term + "]" );

            try
            {
                //connect via get
                HttpClient  httpClient  = AntiPatternsHttp.getEnrichedHttpClient();
                HttpGet     httpGet     = new HttpGet
                (
                                                                AntiPatternsProject.API.GooglePlaces.GOOGLE_PLACES_API_URL_SEARCH_TERM
                    +   "?" +   "key"           +   "="     +   AntiPatternsProject.API.GooglePlaces.GOOGLE_PLACES_API_BROWSER_KEY
                    +   "&" +   "query"         +   "="     +   URLEncoder.encode( term, "UTF-8" )
                    +   "&" +   "sensor"        +   "="     +   "false"

                        //set nextPageToken if available
                    +   (
                                placesNextPageToken == null || LibString.isEmpty( placesNextPageToken )
                            ?   ""
                            :   "&pagetoken=" + placesNextPageToken
                        )

                  //+   "&" +   "types"         +   "="     +   "food"
                  //+   "&" +   "name"          +   "="     +   "harbour"
                  //+   "&" +   "radius"        +   "="     +   "1000"
                  //+   "&" +   "nextPageToken" +   "="     +   "{token-value}"
                );

                //connect
                AntiPatternsDebug.googlePlaces.out( "Connecting Google places API [" + httpGet.getURI() + "]" );
                HttpResponse    httpResponse    = httpClient.execute( httpGet );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                AntiPatternsDebug.googlePlaces.out( "Received StatusCode [" + statusCode + "]" );

                //get body
                HttpEntity      entity          = httpResponse.getEntity();
                String          body            = EntityUtils.toString( entity );

                AntiPatternsDebug.googlePlaces.out( "Received ResponseBody [" + body + "]" );

                //assign nextPageToken
                JSONObject      obj             = new JSONObject( body );
                placesNextPageToken = LibJSON.getJSONStringSecure( obj, "next_page_token" );
                AntiPatternsDebug.googlePlaces.out( "Picked nextPageToken [" + placesNextPageToken + "]" );

                //parse body
                Vector<AntiPatternsDataGooglePlace> googlePlaces = AntiPatternsDataGooglePlace.parse
                        (
                                body,
                                null
                        );

                AntiPatternsDebug.googlePlaces.out( "Parsed [" + googlePlaces.size() + "] results - GooglePlaces.singleton is [" + AntiPatternsState.EGooglePlaces.getActivity() + "]" );

                //prune existent loading-items
                AntiPatternsUI.pruneLoadingItems(placesData);

                //create adapter data
                for ( AntiPatternsDataGooglePlace googlePlace : googlePlaces )
                {
                    //create new item
                    LibAdapterData newItem = new AntiPatternsGridViewContent
                    (
                        AntiPatternsState.EGooglePlaces,
                        googlePlace,
                        AntiPatternsActionPush.EPushListEntryGooglePlaceImageSearch,
                        GridViews.EGooglePlaces
                    );

                    //order image threaded
                    googlePlace.orderImageThreaded( AntiPatternsState.EGooglePlaces );

                    //add new item to adapter
                    placesData.addElement( newItem );
                }

                //check if more places can be loaded
                Runnable actionOnBottomReach = null;
                if ( placesNextPageToken != null && !LibString.isEmpty( placesNextPageToken ) )
                {
                    AntiPatternsDebug.googlePlaces.out( "Set loading item on last position" );

                    //add loading icon
                    lastBottomLoadingItem = new AntiPatternsGridViewContentLoading( AntiPatternsState.EGooglePlaces, true, true );
                    placesData.addElement( lastBottomLoadingItem );

                    //set action to perform on scrolling to the bottom
                    actionOnBottomReach = AntiPatternsAction.EGooglePlacesLoadBySearchTermNext;
                }

                //assign results and dismiss the progress dialog
                AntiPatternsAdapterManager.getSingleton(AntiPatternsState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces).changeDataUIThreaded( AntiPatternsState.EGooglePlaces, placesData, actionOnBottomReach );
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );

                //show 'no results'-dialog if no results have been received
                if ( googlePlaces.isEmpty() )
                {
                    actionOnNoResults.run();
                }
            }
            catch ( IOException ioe )
            {
                //PicFoodDebug.imageSearch.out( "IO-Exception occured on using GooglePlaces-API!" );
                //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Usage of Google-Places-API threw an Exception" ), "", UncaughtException.ENo );

                //dismiss progress dialog and show 'no network'
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );
                actionOnNoNetwork.run();
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.googlePlaces.out( "Using the GooglePlaces-API threw an Exception!" );
                //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Usage of Google-Places-API threw an Exception" ), "", UncaughtException.ENo );
                AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);

                //dismiss progress dialog
                LibDialogProgress.dismissProgressDialogUIThreaded( AntiPatternsState.EGooglePlaces.getActivity() );
                actionOnTechnicalError.run();
            }
        }

        /**********************************************************************************************
        *   Resets the offset and the GridView data.
        *   Should be invoked before a clean update of the google places state is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            placesNextPageToken = null;
            placesData.removeAllElements();
        }

        /**********************************************************************************************
        *   Changes the loading item {@link #lastBottomLoadingItem} on the bottom of the scrolling list
        *   to a 'no network' icon and assigns the specified OnClick-action.
        *
        *   @param  state           The according state.
        *   @param  actionOnClick   The action to perform when the 'no network' icon is clicked.
        **********************************************************************************************/
        public static final void changeNextLoadingItemToNoNetwork( final LibState state, final Runnable actionOnClick )
        {
            lastBottomLoadingItem.setupAndChangeToNoNetwork( state, GridViews.EGooglePlaces, actionOnClick );
        }
    }
