/*  $Id: LibTEA.java 50543 2013-08-09 13:46:59Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.io;

    import  java.io.*;

    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.ui.*;

    /*******************************************************************************************
    *   The Tiny Encryption Algorithm is a simple encryption and decryption-system.
    *   More information can be found on {@link "http://de.wikipedia.org/wiki/Tiny_Encryption_Algorithm"}.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50543 $ $Date: 2013-08-09 15:46:59 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/io/LibTEA.java $"
    *******************************************************************************************/
    public final class LibTEA
    {
        /** The delta. */
        private     static      final   long        DELTA                   = 0x9E3779B9;

        /** Holds the key. */
        private                         int[]       iKey                    = null;

        /** The debug flag. */
        private                         LibDebug    iDebug                  = null;

        /*******************************************************************************************
        *   Constructs a instance of the TEA-System with the specified private key.
        *
        *   @param  aTeaKey     The private key used for data encryption and decryption.
        *   @param  aDebug      The debug flag for tracing the TEA-system.
        *******************************************************************************************/
        public LibTEA( String aTeaKey, LibDebug aDebug )
        {
            iDebug   = aDebug;
            setKey( string2byteArray( aTeaKey ) );
        }

        /*******************************************************************************************
        *   Tests the TEA-system by encrypting a test-string and then decrypting and displaying the result.
        *
        *   @param  toTest      A test String to encrypt and decrypt.
        *******************************************************************************************/
        public final void testTEA( String toTest )
        {
            String  r   = encrypt( toTest   );
            String  a   = decrypt( r        );
            iDebug.out( "\n\nTEA TEST: [" + a + "]\n\n\n\n" );
            if ( a.equals( toTest ) )
            {
                iDebug.out( "success!" );
            }
            else
            {
                iDebug.out( "failed!" );
            }
        }

        /*******************************************************************************************
        *   Encrypts the given String into an encrypted base64-encoded String.
        *
        *   @param  stringToEncrypt     The String to encrypt
        *   @return                     An encrypted base64-encoded String.
        *******************************************************************************************/
        public final String encrypt( String stringToEncrypt )
        {
            iDebug.out( "The plain string to encrypt is [" + stringToEncrypt + "]" );

            //the stream holding the encrypted bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //split the stringToEncrypt in slices of 32 characters ( fill the last slice with trailing spaces )
            String[] slicesToEncrypt = LibString.sliceFilled( stringToEncrypt, 32 );

            //browse all slices
            for ( int i = 0; i < slicesToEncrypt.length; ++i )
            {
                iDebug.out( "slice [" + i + "] is [" + slicesToEncrypt[ i ] + "] containing [" + slicesToEncrypt[ i ].length() + "] bytes" );

                byte[] bytesToEncrypt = string2byteArray( slicesToEncrypt[ i ] );
                encrypt( bytesToEncrypt, 0, 32 );
                try
                {
                    baos.write( bytesToEncrypt );
                }
                catch ( IOException ioe ) {}
            }

            return LibBase64.encodeToString( baos.toByteArray() );
        }

        /*******************************************************************************************
        *   Decrypts a base64-encoded and encrypted String.
        *
        *   @param  encryptedBase64string   An encrypted and base64-encoded String.
        *   @return                         The decrypted and plain String.
        *******************************************************************************************/
        public final String decrypt( String encryptedBase64string )
        {
            byte[] bytes = decryptToBytes( encryptedBase64string );
            return byteArray2string( bytes );
        }

        /*******************************************************************************************
        *   Decrypts a base64-encoded and encrypted String.
        *
        *   @param  encryptedBase64string   An encrypted and base64-encoded String.
        *   @return                         The decrypted bytes.
        *******************************************************************************************/
        public final byte[] decryptToBytes( String encryptedBase64string )
        {
            if ( encryptedBase64string == null ) return null;

            //turn the received bytes to a UTF-8-string
            String encryptedString = byteArray2string( LibBase64.decode( encryptedBase64string ) );

            ByteArrayOutputStream   baos            = new ByteArrayOutputStream();
            String[]                encryptedSlices = LibString.sliceUnfilled( encryptedString, 32 );

            for ( int i = 0; i < encryptedSlices.length; ++i )
            {
                //decrypt this slice
                iDebug.out( "Decrypting encrypted slice # [" + i + "]" );

                byte[] encryptedBytes = string2byteArray( encryptedSlices[ i ] );

                //decrypt the encrypted bytes
                decrypt( encryptedBytes, 0, 32 );

                //right trim the last slice
                if ( i == encryptedSlices.length - 1 )
                {
                    encryptedBytes = string2byteArray( LibString.deleteRight( byteArray2string( encryptedBytes ), ' ' ) );
                }

                //add the decrypted bytes to the output-stream
                try
                {
                    baos.write( encryptedBytes );
                }
                catch ( IOException ioe ) {}
            }

            //return the complete decrypted byte-array
            return baos.toByteArray();
        }

        /*********************************************************************************************
        *   Converts the given string into a byte-array where each character is converted into  the
        *   equivalent byte- from 0-255.
        *
        *   @param  s   The String to convert to a byte-array.
        *   @return     The converted byte-array.
        *********************************************************************************************/
        private static final byte[] string2byteArray( String s )
        {
            if ( s == null ) return null;

            byte[] b = new byte[ s.length() ];

            for ( int i = 0; i < b.length; ++i )
            {
                b[ i ] = (byte)s.charAt( i );
            }

            return b;
        }

        /*********************************************************************************************
        *   Converts the given byte-array into a string where each character represents the byte-value
        *   from 0-255.
        *
        *   @param  ba  The byte-array to convert into a string.
        *   @return     The converted String.
        *********************************************************************************************/
        private static final String byteArray2string( byte[] ba )
        {
            if ( ba == null ) return null;

            StringBuffer s = new StringBuffer();

            for ( int i = 0; i < ba.length; ++i )
            {
                int b = ba[ i ];
                b = ( b < 0 ? 256 + b : b );

                //cast a value from 0-255 !
                s.append( (char)b );
            }

            return s.toString();
        }

        /*******************************************************************************************
        *   Assigns the private key for encryption and decryption.
        *
        *   @param  b   The private key consisting of 32 bytes.
        *******************************************************************************************/
        private final void setKey( byte[] b )
        {
            iKey = new int[ 32 ];

            int[] key = new int[4];

            for (int i = 0; i < 16;)
            {
                key[i / 4] = (b[i++] << 24) + ((b[i++] & 255) << 16) + ((b[i++] & 255) << 8) + (b[i++] & 255);
            }
            //for ( int i = 0; i < key.length; ++i ) debug( "[" + key[ i ] + "]" );

            int sum = 0;
            for (int i = 0; i < 32;)
            {
                iKey[ i++ ] = sum + key[ sum & 3 ];
                sum += DELTA;
                iKey[ i++ ] = sum + key[ ( sum >> 11 ) & 3];
            }
        }

        /*******************************************************************************************
        *   Encrypts the bytes according to the TEA algorithm.
        *
        *   @param  bytes   The bytes to encrypt.
        *   @param  off     The offset for the operation.
        *   @param  len     The length for the operation.
        *******************************************************************************************/
        private final void encrypt( byte[] bytes, int off, int len )
        {
            for (int i = off; i < off + len; i += 8)
            {
                encryptBlock(bytes, bytes, i);
            }
        }

        /*******************************************************************************************
        *   Encrypts the byte block according to the TEA algorithm.
        *
        *   @param  in      The bytes to encrypt.
        *   @param  out     The output to store the bytes to.
        *   @param  off     The offset for the operation.
        *******************************************************************************************/
        private final void encryptBlock( byte[] in, byte[] out, int off )
        {
            int y = (in[off] << 24) | ((in[off+1] & 255) << 16) | ((in[off+2] & 255) << 8) | (in[off+3] & 255);
            int z = (in[off+4] << 24) | ((in[off+5] & 255) << 16) | ((in[off+6] & 255) << 8) | (in[off+7] & 255);

            for ( int i = 0; i < 32; i += 2 )
            {
                y += (((z << 4) ^ (z >>> 5)) + z) ^ iKey[ i     ];
                z += (((y >>> 5) ^ (y << 4)) + y) ^ iKey[ i + 1 ];
            }

            out[off]   = (byte) (y >> 24);
            out[off+1] = (byte) (y >> 16);
            out[off+2] = (byte) (y >> 8);
            out[off+3] = (byte) y;
            out[off+4] = (byte) (z >> 24);
            out[off+5] = (byte) (z >> 16);
            out[off+6] = (byte) (z >> 8);
            out[off+7] = (byte) z;
        }

        /*******************************************************************************************
        *   Decrypts the bytes according to the TEA algorithm.
        *
        *   @param  bytes   The bytes to encrypt.
        *   @param  off     The offset for the operation.
        *   @param  len     The length for the operation.
        *******************************************************************************************/
        private final void decrypt( byte[] bytes, int off, int len )
        {
            for ( int i = off; i < off + len; i += 8 )
            {
                decryptBlock(bytes, bytes, i);
            }
        }

        /*******************************************************************************************
        *   Decrypts the byte block according to the TEA algorithm.
        *
        *   @param  in      The bytes to encrypt.
        *   @param  out     The output to store the bytes to.
        *   @param  off     The offset for the operation.
        *******************************************************************************************/
        private final void decryptBlock( byte[] in, byte[] out, int off )
        {
            int y = (in[off] << 24) | ((in[off+1] & 255) << 16) | ((in[off+2] & 255) << 8) | (in[off+3] & 255) ;
            int z = (in[off+4] << 24) | ((in[off+5] & 255) << 16) | ((in[off+6] & 255) << 8) | (in[off+7] & 255);

            for ( int i = 31; i >= 0; i -= 2 )
            {
                z -= (((y >>> 5) ^ (y << 4)) + y) ^ iKey[ i     ];
                y -= (((z << 4) ^ (z >>> 5)) + z) ^ iKey[ i - 1 ];
            }

            out[off]   = (byte) (y >> 24);
            out[off+1] = (byte) (y >> 16);
            out[off+2] = (byte) (y >> 8);
            out[off+3] = (byte) y;
            out[off+4] = (byte) (z >> 24);
            out[off+5] = (byte) (z >> 16);
            out[off+6] = (byte) (z >> 8);
            out[off+7] = (byte) z;
        }
    }
