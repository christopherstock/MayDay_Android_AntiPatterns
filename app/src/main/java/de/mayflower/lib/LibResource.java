
    package de.mayflower.lib;

    import  android.content.*;
    import  android.content.res.*;

    /*********************************************************************************
    *   All functions for accessing system resources.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************/
    public abstract class LibResource
    {
        /*********************************************************************************
        *   Delivers a String resource.
        *
        *   @param  context     The current system context.
        *   @param  id          The resource-id of the String to pick.
        *   @return             The according String resource.
        *********************************************************************************/
        public static final String getResourceString( Context context, int id )
        {
            return context.getResources().getString( id );
        }

        /*********************************************************************************
        *   Delivers a String array resource.
        *
        *   @param  context     The current system context.
        *   @param  id          The resource-id of the String to pick.
        *   @return             The according String resource.
        *********************************************************************************/
        public static final String[] getResourceStringArray( Context context, int id )
        {
            return context.getResources().getStringArray( id );
        }

        /*********************************************************************************
        *   Delivers a String resource.
        *
        *   @param  context     The current system context.
        *   @param  id          The resource-id of the String to pick.
        *   @return             The according String resource.
        *********************************************************************************/
        public static final String getResourceString( Context context, String id )
        {
            Resources res   = context.getResources();
            int       resId = res.getIdentifier( id, "string", context.getPackageName() );

            return getResourceString( context, resId );
        }

        /*********************************************************************************
        *   Delivers a String resource.
        *
        *   @param  context     The current system context.
        *   @param  id          The resource-id of the String to pick.
        *   @return             The according String resource.
        *********************************************************************************/
        public static final String[] getResourceStringArray( Context context, String id )
        {
            Resources res   = context.getResources();
            int       resId = res.getIdentifier( id, "array", context.getPackageName() );

            return getResourceStringArray( context, resId );
        }
    }
