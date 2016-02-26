/*  $Id: LibIO.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.io;

    import  java.io.*;
    import  java.security.*;
    import  java.util.*;
    import  java.util.zip.*;
    import  android.content.*;
    import  android.graphics.*;

    /************************************************************************
    *   Simple static functions for I/O-Operations.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/io/LibIO.java $"
    ************************************************************************/
    public class LibIO
    {
        /** The number of bytes to read from any InputStream in one operation. */
        public      static      final   int             MAX_INPUT_STREAM_CHUNK_SIZE     = 65536;

        /*********************************************************************************************
        *   Reads the given {@link InputStream} buffered and returns all read bytes as a byte-array.
        *   Bytes must be streamed chunked in android, the application will slow down horribly otherwise.
        *
        *   @param  is                  The InputStream to read buffered.
        *   @return                     All bytes of the given InputStream as a byte-array.
        *********************************************************************************************/
        public static final byte[] readStreamBuffered( InputStream is )
        {
            byte[] ba = readStreamBuffered( is, null, false, null );
            return ba;
        }

        /*********************************************************************************************
        *   Reads the given {@link InputStream} buffered and returns all read bytes as a byte-array.
        *   Bytes must be streamed chunked in android, the application will slow down horribly otherwise.
        *
        *   @param  is                  The InputStream to read buffered.
        *   @param  aOs                 The output stream to write the read bytes to.
        *   @param  useStreamCallback   Flags if callback functionality to the given streamCallback shall appear.
        *   @param  streamCallback      The stream-callback-object to notify if desired.
        *   @return                     All bytes of the given InputStream as a byte-array.
        *********************************************************************************************/
        public static final byte[] readStreamBuffered
        (
            InputStream         is,
            OutputStream        aOs,
            boolean             useStreamCallback,
            LibStreamCallback   streamCallback
        )
        {
            OutputStream            os              = ( aOs == null ? new ByteArrayOutputStream() : aOs );
            int                     bytesRead       = 0;
            byte[]                  bytes           = null;
            int                     bytesDownloaded = 0;

            try
            {
                //read chunks of available bytes
                while ( bytesRead != -1 )
                {
                    bytes       = new byte[ MAX_INPUT_STREAM_CHUNK_SIZE ];
                    bytesRead   = is.read( bytes );

                    if ( bytesRead != -1 )
                    {
                        os.write( bytes, 0, bytesRead );

                        bytesDownloaded += bytesRead;
                    }

                    //DebugSystem.downloadProgressBar.out( ">>>> read stream buffered! bytesread is [" + bytesRead + "] useStreamCallback [" + useStreamCallback + "] streamCallback [" + streamCallback + "]" );

                    if ( useStreamCallback )
                    {
                        if ( streamCallback != null ) streamCallback.setCurrentProgress( bytesDownloaded );
                    }
                }
                is.close();
                os.close();

                //return an empty byte-array if the bytes have been streamed to disk
                if ( aOs != null )
                {
                    return new byte[] {};
                }

                //return the output-stream as a byte-array
                byte[] ret = ( (ByteArrayOutputStream)os ).toByteArray();
                return ret;
            }
            catch ( IOException e )
            {
                //AppsDebugSystem.bugfix.out( "STREAMING FAILED WITH EXCEPTION: " + e );

                try
                {
                    is.close();
                }
                catch ( IOException u ) {}

                try
                {
                    os.close();
                }
                catch ( IOException u ) {}

                return null;
            }
        }

        /*************************************************************************************************
        *   Creates the SHA256-Digest and returns it as a hex string.
        *
        *   @param      file    The file to get the SHA256-checksum for.
        *   @return             The SHA256-digest for the given file as a hex-string.
        **************************************************************************************************/
        public static final String getSha256Hex( File file )
        {
            try
            {
                //create checksum and FileInputStream
                MessageDigest   md  = MessageDigest.getInstance( "SHA-256" );
                FileInputStream fis = new FileInputStream( file );

                //read chunks of available bytes
                byte[] readBuffer = new byte[ MAX_INPUT_STREAM_CHUNK_SIZE ];
                while ( fis.read( readBuffer ) != -1 )
                {
                    //update digest
                    md.update( readBuffer );
                }
                fis.close();

                //generate checksum
                byte[] mdBytes = md.digest();

                //convert the byte to hex format
                StringBuffer hexString = new StringBuffer();
                for ( int i=0; i < mdBytes.length; i++ )
                {
                    hexString.append( Integer.toHexString( 0xff & mdBytes[ i ] ) );
                }
                return hexString.toString();

                //2nd solution seems more discreet - but it is INCORRECT!! do NOT use it!!
                //return new BigInteger( mdBytes ).toString( 16 );
            }
            catch ( Throwable t )
            {
                t.printStackTrace();
            }

            return null;
        }

        /*************************************************************************************************
        *   Creates the SHA256-Digest of a String and returns it as a hex string.
        *
        *   @param      plain   The String to get the SHA256-checksum for.
        *   @return             The SHA256-digest for the given String as a hex-string.
        **************************************************************************************************/
        public static final String getMD5( String plain )
        {
            try
            {
                MessageDigest hash = null;
                try
                {
                    hash = MessageDigest.getInstance( "MD5" );
                }
                catch ( NoSuchAlgorithmException e )
                {
                    return null;
                }

                hash.update( plain.getBytes() );
                byte[] digest = hash.digest();
                StringBuilder builder = new StringBuilder();
                for ( int b : digest )
                {
                    builder.append( Integer.toHexString( ( b >> 4 ) & 0xf ) );
                    builder.append( Integer.toHexString( ( b >> 0 ) & 0xf ) );
                }
                return builder.toString();
            }
            catch ( Throwable t )
            {
                t.printStackTrace();
            }

            return null;
        }

        /************************************************************************
        *   Streams and returns all bytes of the given File.
        *
        *   @param  f   The file to stream.
        *   @return     All streamed bytes of the given File.
        ************************************************************************/
        public static final byte[] streamFile( File f )
        {
            try
            {
                FileInputStream fis = new FileInputStream( f );
                byte[]          ret = LibIO.readStreamBuffered( fis );
                fis.close();

                return ret;
            }
            catch ( Exception e )
            {
                return null;
            }
        }

        /************************************************************************
        *   Sets the file's last modified date to the current date.
        *
        *   @param  context     The current system context.
        *   @param  fileName    The filename of the file to touch.
        ************************************************************************/
        protected static final void touchFile( Context context, String fileName )
        {
            try
            {
                File f = context.getFileStreamPath( fileName );

                //alter last modified if file exists
                if ( f.exists() )
                {
                    f.setLastModified( System.currentTimeMillis() );
                }
            }
            catch ( Exception e )
            {
            }
        }

        /************************************************************************
        *   Streams all bytes from the specified file.
        *
        *   @param  file        The file to read all bytes from.
        *   @return             A byte-array with all bytes from the specified file
        *                       or <code>null</code> if the file doesn't exist.
        ************************************************************************/
        public static final byte[] readFile( File file )
        {
            try
            {
                FileInputStream fis = new FileInputStream( file );

                byte[] ret = LibIO.readStreamBuffered( fis );
                fis.close();

                return ret;
            }
            catch ( Exception e )
            {
                return null;
            }
        }

        /************************************************************************
        *   Writes all bytes to the specified location.
        *
        *   @param  file        The file to write all bytes into.
        *   @param  data        The bytes to write into the file.
        ************************************************************************/
        public static final void writeFile( File file, byte[] data )
        {
            try
            {
                FileOutputStream fos = new FileOutputStream( file );

                fos.write( data );
                fos.flush();
                fos.close();
            }
            catch ( IOException e )
            {
                //e.printStackTrace();
            }
        }

        /************************************************************************
        *   Deletes the file with the specified name.
        *
        *   @param  file        The file to delete.
        ************************************************************************/
        public static final void deleteFile( File file )
        {
            try
            {
                file.delete();
            }
            catch ( Exception e )
            {
            }
        }

        /************************************************************************
        *   Checks, if the specified file is existent.
        *
        *   @param  file    The file to check existence for.
        *   @return         <code>true</code> if the given file exists.
        *                   Otherwise <code>false</code>.
        ************************************************************************/
        public static final boolean isExistent( File file )
        {
            try
            {
                //check existence
                return file.exists();
            }
            catch ( Exception e )
            {
                return false;
            }
        }

        /************************************************************************
        *   Turns the specified Serializable into a base64-encoded String
        *   from the bytes of an ObjectOutputStream.
        *
        *   @param  obj     The Serializable object to turn into a base64-encoded String.
        *   @return         The serialized object as a base64-encoded String.
        ************************************************************************/
        public static String serializableToString( Serializable obj )
        {
            try
            {
                ByteArrayOutputStream   baos    = new ByteArrayOutputStream();
                ObjectOutputStream      oos     = new ObjectOutputStream( baos );
                oos.writeObject( obj );
                oos.close();

                return LibBase64.encodeToString( baos.toByteArray() );
            }
            catch ( IOException e )
            {
            }

            return null;
        }

        /************************************************************************
        *   Reads the Serializable-object from the base64-encoded String.
        *
        *   @param  str     The base64-encoded String to read the Serializable object from.
        *   @return         The read object from the base64-encoded String
        *                   or <code>null</code> if an error occurs.
        ************************************************************************/
        public static Object stringToSerializable( String str )
        {
            try
            {
                if ( str == null ) return null;
                return new ObjectInputStream( new ByteArrayInputStream( LibBase64.decode( str ) ) ).readObject();
            }
            catch ( Exception e )
            {
                return null;
            }
        }

        /************************************************************************
        *   Returns the Typeface with the specified resource name from the 'asset'-folder.
        *
        *   @param  context     The current system context.
        *   @param  assetName   The resource name of the font-file in the 'asset'-folder.
        *   @return             The read Typeface.
        ************************************************************************/
        public static final Typeface createTypefaceFromAsset( Context context, String assetName )
        {
            return Typeface.createFromAsset( context.getAssets(), assetName );
        }

        /************************************************************************
        *   Fills the given Vector with all file-entries ( NOT containing any directory-entries )
        *   that are RECURSIVELY picked in the given directory.
        *
        *   @param  dir         The directory to pick all file-entries from.
        *   @param  ret         The Vector to store all results.
        ************************************************************************/
        public static final void getAllFileEntries( File dir, Vector<File> ret )
        {
            //browse all entries
            if ( dir != null && dir.listFiles() != null )
            {
                for ( File file : dir.listFiles() )
                {
                    //descend to directories
                    if ( file.isDirectory() )
                    {
                        getAllFileEntries( file, ret );
                    }
                    else
                    {
                        ret.addElement( file );
                    }
                }
            }
        }

        /************************************************************************
        *   Fills the given Vector with all directory-entries ( NOT containing any filename-entries )
        *   that are RECURSIVELY picked in the given directory.
        *
        *   @param  dir         The directory to pick all directory-entries from.
        *   @param  ret         The Vector to store all results.
        ************************************************************************/
        public static final void getAllDirectories( File dir, Vector<File> ret )
        {
            //browse all entries
            if ( dir != null && dir.listFiles() != null )
            {
                for ( File file : dir.listFiles() )
                {
                    //descend to directories
                    if ( file.isDirectory() )
                    {
                        //append on 1st position - deeper paths will be listed first this way, simplifying deletion
                        ret.add( 0, file );
                        getAllDirectories( file, ret );
                    }
                }
            }
        }

        /************************************************************************
        *   Reads the specified entry from the zip.
        *
        *   @param  zip         The zip-filename to read an entry from.
        *   @param  entryName   The relative name of the entry to pick from the zip.
        *   @return             The bytes of the specified entry.
        *                       <code>null</code> if an error occured.
        ************************************************************************/
        public static final byte[] readFromZip( File zip, String entryName )
        {
            try
            {
                ZipFile zf = new ZipFile( zip );

                Enumeration<? extends ZipEntry> entries = zf.entries();
                while ( entries.hasMoreElements() )
                {
                    ZipEntry ze = entries.nextElement();

                    String name = ze.getName();

                    //pick the desired entry
                    if ( name.equals( entryName ) )
                    {
                        return LibIO.readStreamBuffered( zf.getInputStream( ze ) );
                    }
                }
            }
            catch ( Exception e )
            {
            }

            return null;
        }

        /************************************************************************
        *   Reads the specified entry from the zip.
        *
        *   @param  zip         The zip-filename to read an entry from.
        *   @param  filter      If this value is not <code>null</code>,
        *                       only files are returned that contain this value.
        *   @return             The bytes of the specified entry.
        *                       <code>null</code> if an error occured.
        ************************************************************************/
        public static final String[] readZipEntries( File zip, String filter )
        {
            try
            {
                Vector<String>                  ret     = new Vector<String>();
                ZipFile                         zf      = new ZipFile( zip );
                Enumeration<? extends ZipEntry> entries = zf.entries();
                while ( entries.hasMoreElements() )
                {
                    ZipEntry    ze      = entries.nextElement();
                    String      name    = ze.getName();

                    //pick the desired entry
                    if ( filter == null || name.contains( filter ) )
                    {
                        ret.addElement( name );
                    }
                }

                return ret.toArray( new String[] {} );
            }
            catch ( Exception e )
            {
                //AppsDebugSystem.DEBUG_THROWABLE( e );
            }

            return new String[] {};
        }
    }
