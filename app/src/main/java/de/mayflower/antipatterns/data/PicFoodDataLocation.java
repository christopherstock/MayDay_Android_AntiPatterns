
    package de.mayflower.antipatterns.data;

    import  java.io.*;
    import  org.json.*;
    import  android.location.*;
    import  de.mayflower.lib.io.*;

    /*****************************************************************************
    *   A serializable class that represents a location.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class PicFoodDataLocation implements Serializable
    {
        /************************************************************************
        *   Every class that implements {@link java.io.Serializable} must specify this
        *   final static field with a long serial version UID.
        ************************************************************************/
        private     static  final   long                serialVersionUID                = 0L;

        /** The distance from the user's current location in KM. */
        public                      double              iDistanceKM                     = 0.0;
        /** The location's latitude. This value's origin is the Google places API. */
        public                      double              iLatitude                       = 0.0;
        /** The location's longitude. This value's origin is the Google places API. */
        public                      double              iLongitude                      = 0.0;
        /** The location name. This value's origin is the Google places API. */
        public                      String              iName                           = null;
        /** The vicinity. This value's origin is the Google places API. */
        public                      String              iVicinity                       = null;

        /** The location's ID. This value's origin is the Google places API. */
        private                     int                 iLocationID                     = 0;
        /** This location's icon url. This value's origin is the Google places API. This is why this value is never read. */
        @SuppressWarnings( "unused" )
        private                     String              iIconURL                        = null;

        /**********************************************************************************************
        *   Creates a location data object from the specified json object.
        *
        *   @param  json            The json object to parse the location data from.
        **********************************************************************************************/
        public PicFoodDataLocation( JSONObject json )
        {
            iLocationID = LibJSON.getJSONIntegerSecure(   json,   "locationId" );
            iName       = LibJSON.getJSONStringSecure(    json,   "name"       );
            iVicinity   = LibJSON.getJSONStringSecure(    json,   "vicinity"   );
            iLatitude   = LibJSON.getJSONDoubleSecure(    json,   "latitude"   );
            iLongitude  = LibJSON.getJSONDoubleSecure(    json,   "longitude"  );
            iDistanceKM = LibJSON.getJSONDoubleSecure(    json,   "distance"   );
            iIconURL    = LibJSON.getJSONStringSecure(    json,   "iconUrl"    );
        }

        /**********************************************************************************************
        *   Creates a location data that specifies the latitude and longitude only.
        *
        *   @param  l       The location that specified the latitude and longitude values for this location data object.
        **********************************************************************************************/
        public PicFoodDataLocation( Location l )
        {
            iLatitude  = l.getLatitude();
            iLongitude = l.getLongitude();
        }

        /**********************************************************************************************
        *   Parses one single locations from the given json object.
        *
        *   @param  json    The json object to parse the location from.
        *   @return         The locations that have been parsed from the json object.
        **********************************************************************************************/
        public static final PicFoodDataLocation parse( JSONObject json )
        {
            //return null if the object is empty
            if ( json.has( "locationId" ) )
            {
                return new PicFoodDataLocation( json );
            }

            return null;
        }

        /*****************************************************************************
        *   Writes this {@link Serializable} to the specified OutputStream.
        *
        *   @param  out         The {@link ObjectOutputStream} to write this {@link Serializable} to.
        *   @throws IOException If an error occurs on writing this object to the stream.
        *****************************************************************************/
        private synchronized void writeObject( ObjectOutputStream out ) throws IOException
        {
            out.writeInt(       iLocationID );
            out.writeDouble(    iLatitude   );
            out.writeDouble(    iLongitude  );
            out.writeDouble(    iDistanceKM );
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
            iLocationID = in.readInt();
            iLatitude   = in.readDouble();
            iLongitude  = in.readDouble();
            iDistanceKM   = in.readDouble();
        }
    }
