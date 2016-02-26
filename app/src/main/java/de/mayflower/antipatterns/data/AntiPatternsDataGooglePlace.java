
    package de.mayflower.antipatterns.data;

    import  java.io.*;
    import  java.util.*;
    import  org.json.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.AntiPatternsUI.ImageSize;
    import  android.graphics.drawable.*;
    import  android.location.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /*****************************************************************************
    *   One parsed entry from the Google Places API
    *   containing the pure data and therefore being serializable.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class AntiPatternsDataGooglePlace implements Serializable, AntiPatternsData
    {
        /************************************************************************
        *   Every class that implements {@link java.io.Serializable} must specify this
        *   final static field with a long serial version UID.
        ************************************************************************/
        private     static  final   long                serialVersionUID                = 0L;

        /** The latitude coorinate of this Google Place. */
        public                      double              iLatitude                       = 0;
        /** The longitude coorinate of this Google Place. */
        public                      double              iLongitude                      = 0;
        /** The url of the according icon for this Google place. */
        public                      String              iIconUrl                        = null;
        /** This place's ID. This is a foreign ID from Google. */
        public                      String              iID                             = null;
        /** The caption of this Google place. */
        public                      String              iName                           = null;
        /** The place's address. */
        public                      String              iVicinity                       = null;

        /** The view that holds the Google-icon for this location. */
        private                     LibImageView        iIconView                       = null;
        /** The Google-icon for this location. */
        private                     BitmapDrawable      iIconDrawable                   = null;
        /** The rating for this Google place. */
        private                     String              iRating                         = null;
        /** The distance in KM to the user's corrent location. */
        private                     float               iDistance                       = 0.0f;

        /**********************************************************************************************
        *   Creates a Google place from the given json object.
        *
        *   @param  json            The json object to parse this Google place from.
        *   @param  srcLocation     The user's current location or <code>null</code>
        *                           if this information is not available.
        **********************************************************************************************/
        public AntiPatternsDataGooglePlace(JSONObject json, Location srcLocation)
        {
            iID        = LibJSON.getJSONStringSecure(  json,     "id"            );
            iName      = LibJSON.getJSONStringSecure(  json,     "name"          );
            iIconUrl   = LibJSON.getJSONStringSecure(  json,     "icon"          );
            iVicinity  = LibJSON.getJSONStringSecure(  json,     "vicinity"      );
            iRating    = LibJSON.getJSONStringSecure(  json,     "rating"        );

            //get lat/long
            JSONObject  geometry    = LibJSON.getJSONObjectSecure( json,        "geometry"      );
            JSONObject  location    = LibJSON.getJSONObjectSecure( geometry,    "location"      );
            double      latitude    = Double.parseDouble( LibJSON.getJSONStringSecure(  location,  "lat" ) );
            double      longitude   = Double.parseDouble( LibJSON.getJSONStringSecure(  location,  "lng" ) );
            Location    dstLocation = new Location( "none" );
            dstLocation.setLatitude(  latitude  );
            dstLocation.setLongitude( longitude );

            //assign lat/lon and distance
            iLatitude   = dstLocation.getLatitude();
            iLongitude  = dstLocation.getLongitude();
            iDistance   = ( srcLocation == null ? 0.0f : srcLocation.distanceTo( dstLocation ) );
        }

        /*****************************************************************************
        *   Parses a Google Places response.
        *
        *   @param  jsonBody    The received response body of the http request.
        *   @param  srcLocation The user's current location.
        *                       May be <code>null</code> if this information is unavailable.
        *   @return             All GooglePlaces that have been parsed from the jsonBody.
        *****************************************************************************/
        public static final Vector<AntiPatternsDataGooglePlace> parse( String jsonBody, Location srcLocation )
        {
            AntiPatternsDebug.googlePlacesParser.out( "Parsing .." );

            //prepare results
            Vector<AntiPatternsDataGooglePlace> ret = new Vector<AntiPatternsDataGooglePlace>();

            try
            {
                JSONObject  obj = new JSONObject( jsonBody );
                JSONArray   arr = LibJSON.getJSONArraySecure( obj, "results" );

                AntiPatternsDebug.googlePlacesParser.out( "Results count: [" + arr.length() + "]" );

                //browse all results
                for ( int i = 0; i < arr.length(); ++ i )
                {
                    JSONObject  entry       = arr.getJSONObject( i );

                    AntiPatternsDebug.googlePlacesParser.out( "Entry # [" + i + "]: [" + entry + "]" );

                    //assign to return array
                    ret.addElement
                    (
                        new AntiPatternsDataGooglePlace( entry, srcLocation )
                    );
                }
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.googlePlacesParser.out( "Parsing GooglePlaces-Results raised an Exception:" );
                //AntiPatternsDebug.DEBUG_THROWABLE( new AntiPatternsInternalError( "Parsing Google-Places-Result raised an Exception" ), "", UncaughtException.ENo );
                AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
            }

            //return results
            return ret;
        }

        /*****************************************************************************
        *   Writes this {@link Serializable} to the specified OutputStream.
        *
        *   @param  out         The {@link ObjectOutputStream} to write this {@link Serializable} to.
        *   @throws IOException If an error occurs on writing this object to the stream.
        *****************************************************************************/
        private synchronized void writeObject( ObjectOutputStream out ) throws IOException
        {
            out.writeUTF(       iID         );
            out.writeUTF(       iName       );
            out.writeUTF(       iIconUrl    );
            out.writeUTF(       iVicinity   );
            out.writeUTF(       iRating     );
            out.writeDouble(    iLatitude   );
            out.writeDouble(    iLongitude  );
            out.writeFloat(     iDistance   );
        }

        /*****************************************************************************
        *   Being invoked by the system, this method fills all fields of this object
        *   with data from the given {@link ObjectInputStream}.
        *
        *   @param  in                      The {@link ObjectInputStream} to read this {@link Serializable} from.
        *   @throws IOException             If an error occurs on reading the object-fields to the stream.
        *****************************************************************************/
        private synchronized void readObject( ObjectInputStream in ) throws IOException
        {
            iID        = in.readUTF();
            iName      = in.readUTF();
            iIconUrl   = in.readUTF();
            iVicinity  = in.readUTF();
            iRating    = in.readUTF();
            iLatitude  = in.readDouble();
            iLongitude = in.readDouble();
            iDistance  = in.readFloat();
        }

        @Override
        public void orderImageThreaded( LibState state )
        {
            //only order if this icon is not already present
            if ( iIconDrawable == null )
            {
                AntiPatternsImage.orderImageThreaded
                        (
                                state,
                                iIconUrl,
                                ImageSize.EIcon,
                                this
                        );
            }
        }

        @Override
        public void assignLoadedImage( LibState state, ImageSize imageSize, BitmapDrawable bd )
        {
            //assign new icon
            iIconDrawable = bd;

            //update ImageView
            AntiPatternsUI.setNewImageUIThreaded(state.getActivity(), bd, iIconView, R.drawable.de_mayflower_antipattern_loading_image);
        }

        @Override
        public View createItemView( final LibState state )
        {
            ViewGroup ret = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_grid_view_simple );

            //title
            {
                TextView title = null;
                title = (TextView)ret.findViewById( R.id.title );
                title.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_title ) );
                LibUI.setupTextView( title, AntiPatternsSystems.getFonts().TYPEFACE_BOLD, iName );
            }

            //description
            {
                TextView description = (TextView)ret.findViewById( R.id.description );

                //hide if empty
                if ( LibString.isEmpty( iVicinity ) && iDistance == 0.0 )
                {
                    description.setVisibility( View.GONE );
                }
                else
                {
                    String text = "";

                    //append vicinity
                    if ( !LibString.isEmpty( iVicinity ) )
                    {
                        text += iVicinity + "\n";
                    }

                    //append distance
                    if ( iDistance != 0.0 )
                    {
                        String distance = LibResource.getResourceString
                        (
                            state.getActivity(),
                            (
                                    iDistance < 1.0 || iDistance >= 2.0
                                ?   R.string.state_google_places_distance_plural
                                :   R.string.state_google_places_distance_singular
                            )
                        );
                        distance = distance.replace( "{distance}", String.valueOf( LibStringFormat.getSingleton().formatNumber( Math.round( iDistance ) ) ) );
                        text += distance;
                    }

                    description.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_description ) );
                    LibUI.setupTextView( description, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, text );
                }
            }

            //icon
            iIconView = (LibImageView)ret.findViewById( R.id.icon );
            //check if loaded icon is present
            if ( iIconDrawable == null )
            {
                iIconView.setImageResource( R.drawable.de_mayflower_antipattern_loading_icon_place);
            }
            else
            {
                AntiPatternsUI.setNewImage(state.getActivity(), iIconDrawable, iIconView, R.drawable.de_mayflower_antipattern_loading_icon_place);

                //iIconView.setImageDrawable( iIconDrawable );
            }

            //bg
            ret.setBackgroundResource( R.drawable.de_mayflower_antipattern_bg_grid_item);

            return ret;
        }

        @Override
        public void recycleBitmaps()
        {
            //recycle the icon for this wall-image
            if ( iIconDrawable != null && iIconDrawable.getBitmap() != null && !iIconDrawable.getBitmap().isRecycled() )
            {
                //change the image 1st
                iIconView.setImageDrawable( null );
                iIconView.setImageBitmap(   null );
                iIconView.setImageResource( R.drawable.de_mayflower_antipattern_loading_icon_place);

                //recycle and ditch old image
                iIconDrawable.getBitmap().recycle();
                iIconDrawable = null;

                //AntiPatternsDebug.bitmapRecycling.out( " recycled icon successfully" );
            }
        }

        @Override
        public String toString()
        {
            return new String
            (
                    " id            [" + iID        + "]:"
                +   " name          [" + iName      + "]:"
                +   " iconURL       [" + iIconUrl   + "]:"
                +   " vicinity      [" + iVicinity  + "]:"
                +   " rating        [" + iRating    + "]:"
                +   " latitude      [" + iLatitude  + "]:"
                +   " longitude     [" + iLongitude + "]:"
            );
        }
    }
