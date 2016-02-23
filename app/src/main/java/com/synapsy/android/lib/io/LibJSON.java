/*  $Id: LibJSON.java 50204 2013-07-29 06:43:54Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.io;

    import  org.json.*;

    /************************************************************************
    *   Static functions to extend the system's JSON-parser.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50204 $ $Date: 2013-07-29 08:43:54 +0200 (Mo, 29 Jul 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/io/LibJSON.java $"
    ************************************************************************/
    public class LibJSON
    {
        /************************************************************************
        *   Picks the JSON-object with the specified key from the specified JSON-object.
        *   If the key does not exist, an empty JSON-object is returned!
        *
        *   @param  obj     The JSON-object to return the JSON-object from.
        *   @param  key     The key to pick the JSON-object from the given JSON-object.
        *   @return         The JSON-object with the specified key or an empty JSON-object
        *                   if the given key does not exist.
        ************************************************************************/
        public static final JSONObject getJSONObjectSecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return obj.getJSONObject( key );
            }
            catch ( Throwable t )
            {
                return new JSONObject();
            }
        }

        /************************************************************************
        *   Picks the String with the specified key from the specified JSON-object.
        *   If the key does not exist, an empty String is returned!
        *
        *   @param  obj     The JSON-object to return the String from.
        *   @param  key     The key to pick the String from the given JSON-object.
        *   @return         The String with the specified key or an empty String
        *                   if the given key does not exist.
        ************************************************************************/
        public static final String getJSONStringSecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return obj.getString( key );
            }
            catch ( Throwable t )
            {
                return "";
            }
        }

        /************************************************************************
        *   Picks the Integer with the specified key from the specified JSON-object.
        *   If the key does not exist, 0 is returned!
        *
        *   @param  obj     The JSON-object to return the Integer from.
        *   @param  key     The key to pick the Integer from the given JSON-object.
        *   @return         The Integer with the specified key or 0
        *                   if the given key does not exist.
        ************************************************************************/
        public static final int getJSONIntegerSecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return Integer.parseInt( obj.getString( key ) );
            }
            catch ( Throwable t )
            {
                return 0;
            }
        }

        /************************************************************************
        *   Picks the Float with the specified key from the specified JSON-object.
        *   If the key does not exist, 0.0f is returned!
        *
        *   @param  obj     The JSON-object to return the Float from.
        *   @param  key     The key to pick the Float from the given JSON-object.
        *   @return         The Float with the specified key or 0.0f
        *                   if the given key does not exist.
        ************************************************************************/
        public static final float getJSONFloatSecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return Float.parseFloat( obj.getString( key ) );
            }
            catch ( Throwable t )
            {
                return 0.0f;
            }
        }

        /************************************************************************
        *   Picks the Double with the specified key from the specified JSON-object.
        *   If the key does not exist, 0.0 is returned!
        *
        *   @param  obj     The JSON-object to return the Double from.
        *   @param  key     The key to pick the Double from the given JSON-object.
        *   @return         The Double with the specified key or 0.0
        *                   if the given key does not exist.
        ************************************************************************/
        public static final double getJSONDoubleSecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return Double.parseDouble( obj.getString( key ) );
            }
            catch ( Throwable t )
            {
                return 0.0;
            }
        }

        /************************************************************************
        *   Picks the Long with the specified key from the specified JSON-object.
        *   If the key does not exist, 0L is returned!
        *
        *   @param  obj     The JSON-object to return the Long from.
        *   @param  key     The key to pick the Long from the given JSON-object.
        *   @return         The Long with the specified key or 0L
        *                   if the given key does not exist.
        ************************************************************************/
        public static final long getJSONLongSecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return Long.parseLong( obj.getString( key ) );
            }
            catch ( Throwable t )
            {
                return 0L;
            }
        }

        /************************************************************************
        *   Picks the JSON-array with the specified key from the specified JSON-object.
        *   If the key does not exist, an empty JSON-array is returned!
        *
        *   @param  obj     The JSON-object to return the JSON-array from.
        *   @param  key     The key to pick the JSON-array from the given JSON-object.
        *   @return         The JSON-array with the specified key or an empty JSON-array
        *                   if the given key does not exist.
        ************************************************************************/
        public static final JSONArray getJSONArraySecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return obj.getJSONArray( key );
            }
            catch ( Throwable t )
            {
                return new JSONArray();
            }
        }

        /************************************************************************
        *   Picks the JSON-object with the specified index from the specified JSON-array.
        *   If the key does not exist, an empty JSON-object is returned!
        *
        *   @param  arr     The JSON-array to return the JSON-object from.
        *   @param  index   The index to pick the JSON-object from the given JSON-array.
        *   @return         The JSON-object with the specified key or an empty JSON-object
        *                   if the given index does not exist.
        ************************************************************************/
        public static final JSONObject getJSONObjectSecure( JSONArray arr, int index )
        {
            try
            {
                //prevents missing keys
                return arr.getJSONObject( index );
            }
            catch ( Throwable t )
            {
                return new JSONObject();
            }
        }

        /************************************************************************
        *   Picks the Boolean with the specified key from the specified JSON-object.
        *   If the key does not exist, <code>false</code> is returned!
        *
        *   @param  obj     The JSON-object to return the Boolean from.
        *   @param  key     The key to pick the Boolean from the given JSON-object.
        *   @return         The Boolean with the specified key or <code>false</code>
        *                   if the given key does not exist.
        ************************************************************************/
        public static final boolean getJSONBooleanSecure( JSONObject obj, String key )
        {
            try
            {
                //prevents null-values
                if ( obj.isNull( key ) ) throw new Throwable();

                //prevents missing keys
                return Boolean.parseBoolean( obj.getString( key ) );
            }
            catch ( Throwable t )
            {
                return false;
            }
        }

        /************************************************************************
        *   Converts the given String-array to a JSON-array.
        *
        *   @param  arr     The String-array to convert to a JSON-array.
        *   @return         The JSON-array that holds all String-elements
        *                   of the given String-array.
        ************************************************************************/
        public static final JSONArray toJSONArray( String[] arr )
        {
            JSONArray ret = new JSONArray();
            for ( String s : arr )
            {
                ret.put( s );
            }
            return ret;
        }
    }
