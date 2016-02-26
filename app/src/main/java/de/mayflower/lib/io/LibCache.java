
    package de.mayflower.lib.io;

    import  java.io.*;

    /*********************************************************************************************
    *   A cache system for storing and caching bytes. The filename is the md5-string of the according url.
    *   Simple but efficient.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************************/
    public class LibCache
    {
        /** The folder where the cache data shall be saved. */
        private                             File                    iFolderCache                    = null;

        /*********************************************************************************************
        *   Creates a new cache system with the specified cache folder.
        *
        *   @param  aFolderCache    The folder where the cache data shall be saved.
        *********************************************************************************************/
        public LibCache( File aFolderCache )
        {
            //assign and create cache folder
            iFolderCache = aFolderCache;
        }

        /*********************************************************************************************
        *   Stores bytes with the associated url specifier.
        *
        *   @param  url     The url where these bytes have been streamed from.
        *   @param  bytes   The bytes that have been streamed from this url.
        *********************************************************************************************/
        public final void put( String url, byte[] bytes )
        {
            File targetLocation = getFilenameFromUrl( url );

            //write this file
            LibIO.writeFile( targetLocation, bytes );
        }

        /*********************************************************************************************
        *   Reads bytes with the associated url specifier from the cache.
        *   If no cached bytes for this stored url are saved, <code>null</code> will be returned.
        *
        *   @param  url The url to read the cached bytes for.
        *   @return     The bytes or <code>null</code> if no bytes are cached for this url.
        *********************************************************************************************/
        public final byte[] get( String url )
        {
            File targetLocation  = getFilenameFromUrl( url );

            //check if cached file exists
            if ( targetLocation.exists() )
            {
                return LibIO.readFile( targetLocation );
            }

            return null;
        }

        /*********************************************************************************************
        *   Creates the local filename for the file that shall be saved for the given url.
        *   The filename is a md5-digest of the given url, descended in five subfolders
        *   that are named by the first characters of this md5-checksum.
        *
        *   @param  url The url to receive the filename for.
        *   @return     The file to store for the given url.
        *********************************************************************************************/
        private final File getFilenameFromUrl( String url )
        {
            //turn url to md5
            String md5      = LibIO.getMD5( url );

            //descend five directories
            File filename   = new File( iFolderCache,   String.valueOf( md5.charAt( 0 ) ) );
                 filename   = new File( filename,       String.valueOf( md5.charAt( 1 ) ) );
                 filename   = new File( filename,       String.valueOf( md5.charAt( 2 ) ) );
                 filename   = new File( filename,       String.valueOf( md5.charAt( 3 ) ) );
                 filename   = new File( filename,       String.valueOf( md5.charAt( 4 ) ) );

            //create directory structure
                 filename.mkdirs();

            //append md5 filename
                 filename   = new File( filename,       md5 );

            return filename;
        }
    }
