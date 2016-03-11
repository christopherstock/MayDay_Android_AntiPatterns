
    package de.mayflower.antipatterns;

    import de.mayflower.antipatterns.Settings.*;

    /***************************************************************************************************
    *   Logs all versions of this app.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ***************************************************************************************************/
    public enum AntiPatternsVersion
    {
        /** */  VERSION_1_0_0(  "1.0.0",    1,      "1.0.0",        "26.02.2016 11:29:28",      "BASE",                     "" ),

        ;

        /** The version string of the client version. */
        private         String              iClientVersionNumber        = null;
        /** The integer version code for the Android Manifest. */
        @SuppressWarnings( "unused" )
        private         int                 iVersionCode                = 0;
        /** The version string of the backend version. */
        private         String              iBackendVersionNumber       = null;
        /** The completion time of this version. */
        private         String              iReleaseDate                = null;
        /** The internal codename of this version. */
        private         String              iCodeName                   = null;
        /** The log for this version contains latest changes. */
        private         String              iLog                        = null;

        /******************************************************************************************
        *   Creates one app version enum constant.
        *
        *   @param  aClientVersionNumber    The version string for this client.
        *   @param  aVersionCode            The integer version code for the Android Manifest.
        *   @param  aBackendVersionNumber   The version string of the according backend.
        *   @param  aReleaseDate            The last compilation time of this version.
        *   @param  aCodename               The internal codename for this version.
        *   @param  aLog                    A log-text with latest changes for this version.
        ******************************************************************************************/
        private AntiPatternsVersion(String aClientVersionNumber, int aVersionCode, String aBackendVersionNumber, String aReleaseDate, String aCodename, String aLog)
        {
            iClientVersionNumber    = aClientVersionNumber;
            iVersionCode            = aVersionCode;
            iBackendVersionNumber   = aBackendVersionNumber;
            iReleaseDate            = aReleaseDate;
            iCodeName               = aCodename;
            iLog                    = aLog;
        }

        /******************************************************************************************
        *   Returns the release date.
        *
        *   @return     The last release date for this version.
        ******************************************************************************************/
        public final String getCompileTime()
        {
            return iReleaseDate;
        }

        /******************************************************************************************
        *   Returns the version string for the client version.
        *
        *   @return     The version string for the client.
        ******************************************************************************************/
        public final String getClientVersionNumber()
        {
            return iClientVersionNumber;
        }

        /******************************************************************************************
        *   Returns the version string for the backend version.
        *
        *   @return     The version string for the backend.
        ******************************************************************************************/
        public final String getBackendVersionNumber()
        {
            return iBackendVersionNumber;
        }

        /******************************************************************************************
        *   Shows the current version number.
        *
        *   @return     The version-number of the latest version.
        ******************************************************************************************/
        public static final String getVersion()
        {
            String ret = ( Paramounts.PROJECT_NAME + ", Version [" + values()[ 0 ].iClientVersionNumber + "] codename [" + values()[ 0 ].iCodeName + "] released on [" + values()[ 0 ].iReleaseDate + "]" );
            return ret;
        }

        /******************************************************************************************
        *   Returns a list of the project history.
        *
        *   @return     Returns a list of all versions with all version-numbers, codenames,
        *               release-dates and the according log.
        ******************************************************************************************/
        public static final String getVersionLog()
        {
            StringBuffer ret = new StringBuffer();
            for ( int i = 0; i < values().length; ++i )
            {
                ret.append( values()[ i ].iClientVersionNumber + " " + values()[ i ].iCodeName + " " + values()[ i ].iReleaseDate + " " + values()[ i ].iLog + "\n" );
            }
            ret.append( "\n" );
            return ret.toString();
        }
    }
