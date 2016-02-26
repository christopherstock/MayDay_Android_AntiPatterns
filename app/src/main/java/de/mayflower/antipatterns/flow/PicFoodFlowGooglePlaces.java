
    package de.mayflower.antipatterns.flow;

    import  java.io.*;
    import  java.net.*;
    import  java.util.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.API.GooglePlaces;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.io.PicFoodSave.SaveKey;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.*;
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
    public class PicFoodFlowGooglePlaces
    {
        /**********************************************************************************************
        *   The last received token from a Google places response that must be set in
        *   order to receive the next 20 results of the same request.
        **********************************************************************************************/
        private             static          String                          placesNextPageToken         = null;

        /** All adapter data that is currently displayed in the google places state. */
        private             static          Vector<LibAdapterData>          placesData                  = new Vector<LibAdapterData>();

        /** The loading content item that is currently displayed on the bottom of the scrolling list. */
        private             static          PicFoodGridViewContentLoading   lastBottomLoadingItem       = null;

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
            PicFoodDebug.googlePlaces.out( "Searching via GooglePlaces-API from last scanned GPS location" );

            //get Location from GPS
            PicFoodDataLocation lastUserLocation = new PicFoodDataLocation( currentLocation );
            PicFoodDebug.googlePlaces.out( "Using current location: [" + lastUserLocation.iLatitude + "," + lastUserLocation.iLongitude + "]" );

            //save location persistent
            PicFoodSave.saveSetting( PicFoodState.EGooglePlaces.getActivity(), SaveKey.EStateNewEntryLastUserLocation, LibIO.serializableToString( lastUserLocation ) );

            try
            {
                //connect via get
                HttpClient  httpClient  = PicFoodHttp.getEnrichedHttpClient();
                HttpGet     httpGet     = new HttpGet
                (
                                                                PicFoodProject.API.GooglePlaces.GOOGLE_PLACES_API_URL_SEARCH_NEARBY
                    +   "?" +   "key"           +   "="     +   PicFoodProject.API.GooglePlaces.GOOGLE_PLACES_API_BROWSER_KEY

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
                PicFoodDebug.googlePlaces.out( "Connecting Google places API [" + httpGet.getURI() + "]" );
                HttpResponse    httpResponse    = httpClient.execute( httpGet );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                PicFoodDebug.googlePlaces.out( "Received StatusCode [" + statusCode + "]" );

                //get body
                HttpEntity      entity          = httpResponse.getEntity();
                String          body            = EntityUtils.toString( entity );

                PicFoodDebug.googlePlaces.out( "Received ResponseBody [" + body + "]" );

                //assign nextPageToken
                JSONObject      obj             = new JSONObject( body );
                placesNextPageToken = LibJSON.getJSONStringSecure( obj, "next_page_token" );
                PicFoodDebug.googlePlaces.out( "Picked nextPageToken [" + placesNextPageToken + "]" );

                //parse body
                Vector<PicFoodDataGooglePlace> googlePlaces = PicFoodDataGooglePlace.parse
                (
                    body,
                    currentLocation
                );

                PicFoodDebug.googlePlaces.out( "Parsed [" + googlePlaces.size() + "] results" );

                //prune existent loading-items
                PicFoodUI.pruneLoadingItems( placesData );

                //create adapter data
                for ( PicFoodDataGooglePlace googlePlace : googlePlaces )
                {
                    //create new item
                    LibAdapterData newItem = new PicFoodGridViewContent
                    (
                        PicFoodState.EGooglePlaces,
                        googlePlace,
                        PicFoodActionPush.EPushListEntryGooglePlaceNewEntry,
                        GridViews.EGooglePlaces
                    );

                    //order image threaded
                    googlePlace.orderImageThreaded( PicFoodState.EGooglePlaces );

                    //add new item to stack
                    placesData.addElement( newItem );
                }

                //check if more places can be loaded
                Runnable actionOnBottomReach = null;
                if ( placesNextPageToken != null && !LibString.isEmpty( placesNextPageToken ) )
                {
                    PicFoodDebug.googlePlaces.out( "Set loading item on last position" );

                    //add loading icon
                    lastBottomLoadingItem = new PicFoodGridViewContentLoading( PicFoodState.EGooglePlaces, true, true );
                    placesData.addElement( lastBottomLoadingItem );

                    //set action to perform on scrolling to the bottom
                    actionOnBottomReach = PicFoodAction.EGooglePlacesLoadAfterGPSSuccessNext;
                }

                //assign results and dismiss the progress dialog
                PicFoodAdapterManager.getSingleton( PicFoodState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces ).changeDataUIThreaded( PicFoodState.EGooglePlaces, placesData, actionOnBottomReach );
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );

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
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );
                actionOnNoNetwork.run();
            }
            catch ( Throwable t )
            {
                PicFoodDebug.googlePlaces.out( "Using the GooglePlaces-API threw an Exception!" );
                //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Usage of Google-Places-API threw an Exception" ), "", UncaughtException.ENo );
                PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );

                //dismiss progress dialog
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );
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
            PicFoodDebug.googlePlaces.out( "Invoking the GooglePlaces-API with searchTerm [" + term + "]" );

            try
            {
                //connect via get
                HttpClient  httpClient  = PicFoodHttp.getEnrichedHttpClient();
                HttpGet     httpGet     = new HttpGet
                (
                                                                PicFoodProject.API.GooglePlaces.GOOGLE_PLACES_API_URL_SEARCH_TERM
                    +   "?" +   "key"           +   "="     +   PicFoodProject.API.GooglePlaces.GOOGLE_PLACES_API_BROWSER_KEY
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
                PicFoodDebug.googlePlaces.out( "Connecting Google places API [" + httpGet.getURI() + "]" );
                HttpResponse    httpResponse    = httpClient.execute( httpGet );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                PicFoodDebug.googlePlaces.out( "Received StatusCode [" + statusCode + "]" );

                //get body
                HttpEntity      entity          = httpResponse.getEntity();
                String          body            = EntityUtils.toString( entity );

                PicFoodDebug.googlePlaces.out( "Received ResponseBody [" + body + "]" );

                //assign nextPageToken
                JSONObject      obj             = new JSONObject( body );
                placesNextPageToken = LibJSON.getJSONStringSecure( obj, "next_page_token" );
                PicFoodDebug.googlePlaces.out( "Picked nextPageToken [" + placesNextPageToken + "]" );

                //parse body
                Vector<PicFoodDataGooglePlace> googlePlaces = PicFoodDataGooglePlace.parse
                (
                    body,
                    null
                );

                PicFoodDebug.googlePlaces.out( "Parsed [" + googlePlaces.size() + "] results - GooglePlaces.singleton is [" + PicFoodState.EGooglePlaces.getActivity() + "]" );

                //prune existent loading-items
                PicFoodUI.pruneLoadingItems( placesData );

                //create adapter data
                for ( PicFoodDataGooglePlace googlePlace : googlePlaces )
                {
                    //create new item
                    LibAdapterData newItem = new PicFoodGridViewContent
                    (
                        PicFoodState.EGooglePlaces,
                        googlePlace,
                        PicFoodActionPush.EPushListEntryGooglePlaceImageSearch,
                        GridViews.EGooglePlaces
                    );

                    //order image threaded
                    googlePlace.orderImageThreaded( PicFoodState.EGooglePlaces );

                    //add new item to adapter
                    placesData.addElement( newItem );
                }

                //check if more places can be loaded
                Runnable actionOnBottomReach = null;
                if ( placesNextPageToken != null && !LibString.isEmpty( placesNextPageToken ) )
                {
                    PicFoodDebug.googlePlaces.out( "Set loading item on last position" );

                    //add loading icon
                    lastBottomLoadingItem = new PicFoodGridViewContentLoading( PicFoodState.EGooglePlaces, true, true );
                    placesData.addElement( lastBottomLoadingItem );

                    //set action to perform on scrolling to the bottom
                    actionOnBottomReach = PicFoodAction.EGooglePlacesLoadBySearchTermNext;
                }

                //assign results and dismiss the progress dialog
                PicFoodAdapterManager.getSingleton( PicFoodState.EGooglePlaces.getActivity(), GridViews.EGooglePlaces ).changeDataUIThreaded( PicFoodState.EGooglePlaces, placesData, actionOnBottomReach );
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );

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
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );
                actionOnNoNetwork.run();
            }
            catch ( Throwable t )
            {
                PicFoodDebug.googlePlaces.out( "Using the GooglePlaces-API threw an Exception!" );
                //PicFoodDebug.DEBUG_THROWABLE( new PicFoodInternalError( "Usage of Google-Places-API threw an Exception" ), "", UncaughtException.ENo );
                PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );

                //dismiss progress dialog
                LibDialogProgress.dismissProgressDialogUIThreaded( PicFoodState.EGooglePlaces.getActivity() );
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
