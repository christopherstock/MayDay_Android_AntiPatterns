
    package de.mayflower.lib;

    import  java.util.*;
    import  java.util.regex.*;

    /************************************************************************
    *   Handles string-drawing and measuring-systems.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class LibString
    {
        /** These characters will break lines. */
        private         static  final   char[]              LINEBREAK_CHARS                 =
        {
            '\n', ' ', ',', '?', '-', ':', ';', '/', '.', '!'
        };

        /*********************************************************************************
        *   Determines where to display the currency.
        *********************************************************************************/
        public static enum CurrencyDisplay
        {
            /** Display currencry before the amount. */
            EBeforeAmount,

            /** Display currencry after the amount. */
            EAfterAmount,
            ;
        }

        /*********************************************************************************
        *   Removes all special chars ( regex ^[0-9A-Za-z] ) from the given string.
        *
        *   @param  in  The String to prune all special chars from.
        *   @return     The condensed string where only chars 0-9, A-Z and a-z are left.
        *********************************************************************************/
        public static String pruneSpecialChars( String in )
        {
            StringBuffer inb = new StringBuffer( in );

            //browse all chars reversed
            for ( int i = inb.length() - 1; i >= 0; --i )
            {
                //check if this is a suitable character
                if
                (
                        ( inb.charAt( i ) >= 48 && inb.charAt( i ) <= 57  )   // 0 - 9
                    ||  ( inb.charAt( i ) >= 65 && inb.charAt( i ) <= 90  )   // A - Z
                    ||  ( inb.charAt( i ) >= 97 && inb.charAt( i ) <= 122 )   // a - z
                )
                {
                    //character is suitable and will not be pruned
                }
                else
                {
                    //prune
                    inb.deleteCharAt( i );
                }
            }

            return inb.toString();
        }


        /***************************************************************************************
        *   Formats an integer-value to a price-string.
        *
        *   @param  intPrice                    The price as an integer.  ( e.g. 99 )
        *   @param  floatingPointLiteral        The literal being used to separate the last two digits.
        *   @param  currency                    The currency to display after the string.
        *   @param  cutPrices                   Specifies if the last two digits of the price shall be cropped.
        *   @param  freeString                  The string to return if the price is 0.
        *   @param  cipherLiteralIntegerPoint   The cipher-literal to use if the integer-point-value amounts to 0.
        *   @param  cipherLiteralDecimalPoint   The cipher-literal to use if the decimal-point-value amounts to 0.
        *   @param  currencyDisplay             Decides if the currency shall be displayed in front or behind the amount.
        *   @param  separator                   The String literal to use as the separator between integer and decimal place.
        *   @return                             A formatted price-string. ( e.g. 2,- )
        ***************************************************************************************/
        public static final String toFormattedPrice
        (
            int             intPrice,
            String          floatingPointLiteral,
            String          currency,
            boolean         cutPrices,
            String          freeString,
            String          cipherLiteralIntegerPoint,
            String          cipherLiteralDecimalPoint,
            CurrencyDisplay currencyDisplay,
            String          separator
        )
        {
            String  price   = Integer.valueOf( intPrice ).toString();
            int     len     = price.length();

            //return 'free' if the price is empty
            if ( intPrice == 0 )
            {
                return freeString;
            }

            String currencyStringBefore = null;
            String currencyStringAfter  = null;

            switch ( currencyDisplay )
            {
                case EAfterAmount:
                {
                    currencyStringBefore = "";
                    currencyStringAfter  = ( currency.length() > 0 ? separator + currency : "" );
                    break;
                }
                case EBeforeAmount:
                {
                    currencyStringBefore = ( currency.length() > 0 ? currency + separator : "" );
                    currencyStringAfter  = "";
                    break;
                }
            }

            switch ( len )
            {
                case 0:
                {
                    return "";
                }
                case 1:
                {
                    if ( cutPrices )
                    {
                        return currencyStringBefore + "0" + currencyStringAfter;
                    }

                    return currencyStringBefore + cipherLiteralIntegerPoint + floatingPointLiteral + "0" + price + currencyStringAfter;
                }
                case 2:
                {
                    if ( cutPrices )
                    {
                        return currencyStringBefore + "0" + currencyStringAfter;
                    }

                    return currencyStringBefore + cipherLiteralIntegerPoint + floatingPointLiteral + price + currencyStringAfter;
                }
                default:
                {
                    if ( cutPrices )
                    {
                        return currencyStringBefore + price.substring( 0, len - 2 ) + currencyStringAfter;
                    }

                    String lastTwoDigits = price.substring( len - 2 );
                    if ( lastTwoDigits.equals( "00" ) ) lastTwoDigits = cipherLiteralDecimalPoint;
                    return currencyStringBefore + price.substring( 0, len - 2 ) + floatingPointLiteral + lastTwoDigits + currencyStringAfter;
                }
            }
        }

        /*********************************************************************************
        *   Checks for equality of two String arrays. Two String arrays equal if both arrays
        *   contain the same number of elements and if elements with the same index equal.
        *
        *   @param  haystack1   The first  array to check for equality.
        *   @param  haystack2   The second array to check for equality.
        *   @return             <code>true</code> if both arrays have the same number of elements
        *                       and all elements with the same index equal. Otherwise <code>false</code>.
        *********************************************************************************/
        public static final boolean equals( String[] haystack1, String[] haystack2 )
        {
            if ( haystack1.length != haystack2.length ) return false;

            for ( int i = 0; i < haystack1.length; ++i )
            {
                if ( !haystack1[ i ].equals( haystack2[ i ] ) )
                {
                    return false;
                }
            }

            return true;
        }

        /*************************************************************************************************
        *   Return results from regEx search.
        *
        *   @param  haystack            The search subject.
        *   @param  regex               The regular expression to search.
        *   @param  groups              Number of capture groups in the regex.
        *   @param  searchSubstring     true  - search regex as substring in subject.
        *                               false - regex has to match the subject.
        *   @return                     A 2d String-array. First dimension is the number of
        *                               found places. The second dimension is the number of
        *                               capture groups.
        **************************************************************************************************/
        public static final String[][] searchRegEx( String haystack, String regex, int groups, boolean searchSubstring )
        {
            Vector<String[]>    results     = new Vector<String[]>();
            String[]            tmp         = null;
            Pattern             pat         = null;
            Matcher             mat         = null;

            //assign pattern
            pat = Pattern.compile( regex, Pattern.CASE_INSENSITIVE|Pattern.DOTALL );
            mat = pat.matcher( haystack );

            //find or match regex
            while ( ( searchSubstring ? mat.find() : mat.matches() ) )
            {
                tmp = new String[ groups ];

                for ( int i = 0; i < groups; ++i )
                {
                    tmp[ i ] = mat.group( ( i + 1 ) );
                }

                results.add( tmp );

                //break if not searching for substrings
                if ( !searchSubstring )
                {
                    break;
                }
            }

            return results.toArray( new String[][] {} );
        }

        /*************************************************************************************************
        *   Returns the index of the next linebreak.
        *
        *   @param  aString     The string to search for a linebreak.
        *   @param  aStartIndex The start index to search for linebreaks.
        *   @return             The index of the nearest linebreak from the specified start index on
        *                       or <code>-1</code> if no linebreak has been found beyond this index.
        **************************************************************************************************/
        private static final int getNearestLinebreakIndexOptimized( String aString, int aStartIndex )
        {
            //start a new end-index-search
            int endIndex = -1;

            //skip leading whitespaces
            while( aStartIndex < aString.length() && aString.charAt( aStartIndex ) == ' ' ) ++aStartIndex;

            //browse all linebreak-chars
            for ( int i = 0; i < LibString.LINEBREAK_CHARS.length; ++i )
            {
                //check if this linebreak-char was found
                int foundIndex = aString.substring( aStartIndex ).indexOf( LibString.LINEBREAK_CHARS[ i ] );
                if ( foundIndex == -1 )
                {
                }
                else if ( endIndex == -1 || aStartIndex + foundIndex < endIndex )
                {
                    endIndex = aStartIndex + foundIndex;
                }
            }

            return endIndex;
        }

        /*************************************************************************************************
        *   Converts a byte-array to a UTF-8-encoded String.
        *
        *   @param  ba      The byte array to convert.
        *   @return         The byte array as a UTF-8-encoded String
        *                   or <code>null</code> if conversion failed.
        **************************************************************************************************/
        public static final String byteArrayToUtfString( byte[] ba )
        {
            try
            {
                return new String( ba, "UTF-8" );
            }
            catch ( Exception e )
            {
                return null;
            }
        }

        /*************************************************************************************************
        *   Converts a UTF-8-encoded String to a byte-array.
        *
        *   @param  str     The UTF-8-encoded String to convert.
        *   @return         The String as a byte-array
        *                   or <code>null</code> if conversion failed.
        **************************************************************************************************/
        public static final byte[] utfStringToByteArray( String str )
        {
            try
            {
                return str.getBytes( "UTF-8" );
            }
            catch ( Exception e )
            {
                return null;
            }
        }

        /*********************************************************************************************
        *   Splits a haystack around all occurences of the splitter and returns all chunks in an array.
        *   Empty elements are not pruned.
        *
        *   @param  haystack            The original string to split.
        *   @param  splitter            A string that works as a delimiter for the chunks.
        *   @param  pruneEmptyElements  Specifies if empty chunks shall be pruned from the result array.
        *   @return                     An array of chunks. Empty chunks are pruned if desired.
        *********************************************************************************************/
        public static final String[] split( String haystack, String splitter, boolean pruneEmptyElements )
        {
            if ( haystack == null || splitter == null ) return new String[] {};

            Vector<String>  vec         = new Vector<String>();
            String[]        ret         = null;
            int             lastIndex   = -1;

            while ( ( lastIndex = haystack.indexOf( splitter ) ) != -1 )
            {
                //add this element
                if ( pruneEmptyElements && haystack.substring( 0, lastIndex ).length() == 0 )
                {
                }
                else
                {
                    vec.addElement( haystack.substring( 0, lastIndex ) );
                }
                haystack = haystack.substring( lastIndex + splitter.length() );
            }

            //add last element directly
            if ( pruneEmptyElements && haystack.length() == 0 )
            {
            }
            else
            {
                vec.addElement( haystack );
            }

            //copy all elements from vector to array
            ret = new String[ vec.size() ];
            for ( int i = 0; i < vec.size(); ++i )
            {
                ret[ i ] = vec.elementAt( i ).toString();
            }

            return ret;
        }

        /*********************************************************************************************
        *   Splits a haystack around all occurences of the splitter and returns all chunks in an array.
        *   Empty elements are not pruned.
        *
        *   @param  haystack    The original string to split.
        *   @param  splitter    A string that works as a delimiter for the chunks.
        *   @return             An array of chunks. It contains one element more than
        *                       the number of occurences of splitter in the haystack.
        *********************************************************************************************/
        public static final String[] split( String haystack, String splitter )
        {
            return split( haystack, splitter, false );
        }

        /*********************************************************************************************
        *   Replaces all occurences of oldString with newString in the given haystack.
        *
        *   @param  haystack    The string to perform the replacements.
        *   @param  oldString   The string to replace.
        *   @param  newString   The replacement.
        *   @return             The haystack with all occurences of oldString being replaced by newString.
        *********************************************************************************************/
        public static final String replace( String haystack, String oldString, String newString )
        {
            String[] chunks = split( haystack, oldString );
            if ( chunks.length <= 1 ) return haystack;

            StringBuffer ret = new StringBuffer( chunks[ 0 ] );
            for ( int i = 1; i < chunks.length; ++i )
            {
                ret.append( newString ).append( chunks[ i ] );
            }

            return ret.toString();
        }

        /*******************************************************************************************
        *   Cuts the specified characters from the end of the specified String
        *   if they could be found at the end of the String.
        *
        *   @param  str     The String to cut the specified characters from its end.
        *   @param  chr     The characters to cut from the end of the String.
        *   @return         The String with the characters cut from its end.
        *                   If the characters have not been found at the end of the String,
        *                   the String is returned unchanged.
        *******************************************************************************************/
        public static final String deleteRight( String str, char chr )
        {
            while ( str.charAt( str.length() - 1 ) == chr ) str = str.substring( 0, str.length() - 1 );
            return str;
        }

        /*******************************************************************************************
        *   Slices the given string into a String-array where each element has the specified amount
        *   of chars. The last Element is filled with spaces if it is lower than the specified amount of chars.
        *
        *   @param  str         The String to slice.
        *   @param  blockSize   The target length for one slice.
        *   @return             A String-Array of the sliced String. Each element has blockSize characters.
        *******************************************************************************************/
        public static final String[] sliceFilled( String str, int blockSize )
        {
            String[] slices = new String[ ( str.length() % blockSize == 0 ? str.length() / blockSize : str.length() / blockSize + 1 ) ];
            for ( int i = 0; i < slices.length; ++i )
            {
                if ( i == slices.length - 1 )
                {
                    //the last slice is filled with spaces
                    slices[ i ] = str.substring( i * blockSize );

                    int spacesToAppend = blockSize - slices[ i ].length();
                    for ( int j = 0; j < spacesToAppend; ++j )
                    {
                        slices[ i ] += " ";
                    }
                }
                else
                {
                    slices[ i ] = str.substring( i * blockSize, i * blockSize + blockSize );
                }
            }

            return slices;
        }

        /*******************************************************************************************
        *   Slices the given string into a String-array where each element has the specified amount
        *   of chars. The last Element is NOT filled with spaces and may contain less characters than
        *   the specified amount.
        *
        *   @param  str         The String to slice.
        *   @param  chunkLength The target length for one slice.
        *   @return             A String-Array of the sliced String. Each element has blockSize characters.
        *******************************************************************************************/
        public static final String[] sliceUnfilled( String str, int chunkLength )
        {
            String[]    chunks  = new String[ str.length() / chunkLength ];

            for ( int i = 0; i < chunks.length; ++i )
            {
                chunks[ i ] = str.substring( i * chunkLength, i * chunkLength + chunkLength );
            }

            return chunks;
        }

        /*******************************************************************************************
        *   Checks, if the given String is empty.
        *
        *   @param  str     The String to check emptiness for.
        *   @return         <code>true</code> if the specified String has a length of 0.
        *                   Otherwise <code>false</code>.
        *******************************************************************************************/
        public static final boolean isEmpty( String str )
        {
            return ( str.length() == 0 );
        }

        /*******************************************************************************************
        *   Checks, if the length of the given String is shorter than the given length.
        *
        *   @param  str     The String to check length for.
        *   @param  length  The length to check.
        *   @return         <code>true</code> if the specified String has a shorter length than the given length.
        *                   Otherwise <code>false</code>.
        *******************************************************************************************/
        public static final boolean isShorterThan( String str, int length )
        {
            return ( str.length() < length );
        }

        /*******************************************************************************************
        *   Returns the given String or an empty String if the given String is <code>null</code>.
        *
        *   @param  str     The String to secure.
        *   @return         The given String or an empty String if the given String is <code>null</code>.
        *******************************************************************************************/
        public static final String secureNullValue( String str )
        {
            return ( str == null ? "" : str );
        }
    }
