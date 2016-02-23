/*  $Id: LibXmlParseAgent.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.io.xml;

    import  java.io.*;
    import  org.xmlpull.v1.*;
    import  com.synapsy.android.lib.util.*;

    /*************************************************************************
    *   The XML-Parse-Agent. A helper class that handles the {@link LibKXmlParser}.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/io/xml/LibXmlParseAgent.java $"
    *************************************************************************/
    public class LibXmlParseAgent
    {
        /** Specifies if empty children shall be created if the child node is empty. */
        private     static  final       boolean     IGNORE_WHITESPACES      = true;

        /** All bytes that build the xml-source. */
        public              byte[]                  iXmlSrc                 = null;

        /** The parser instance to use. */
        public              LibKXmlParser           parser                  = null;

        /*************************************************************************
        *   Creates a parse-agent from the specified xml-source.
        *
        *   @param  aXmlSrc     The xml-source to parse.
        *************************************************************************/
        public LibXmlParseAgent( byte[] aXmlSrc )
        {
            iXmlSrc = aXmlSrc;
        }

        /*************************************************************************
        *   Performs the complete parsing process and returns the root node
        *   of the parsed xml source.
        *
        *   @return             The top level node of the parsed xml source.
        *   @throws Exception   If parsing failed -
        *                       mostly due to an inconsistent xml structure.
        *************************************************************************/
        public LibXmlNode parseXML() throws Exception
        {
            parser = new LibKXmlParser();

            //throw intentional exception if the source is null
            if ( iXmlSrc == null )
            {
                throw new LibIntentionalException( "The XML parser has been instructed to parse [" + iXmlSrc + "] data." );
            }

            //set data
            parser.setInput( new ByteArrayInputStream( iXmlSrc ), "UTF-8" );

            //move to 1st tag
            parser.next();

            return _parse();
        }

        /*************************************************************************
        *   Starts the internal parsing process.
        *
        *   @return             The top level node of the parsed xml source.
        *   @throws Exception   If parsing failed -
        *                       mostly due to an inconsistent xml structure.
        *************************************************************************/
        private LibXmlNode _parse() throws Exception
        {
            LibXmlNode node = new LibXmlNode( LibXmlNode.ELEMENT_NODE );
            if ( parser.getEventType() != XmlPullParser.START_TAG )
            {
                throw new Exception( "Illegal XML state: " + parser.getName() + ", " + parser.getEventType() );
            }

            node.iNodeName = parser.getName();

            for ( int i = 0; i < parser.getAttributeCount(); i++ )
            {
                node.setAttribute( parser.getAttributeName( i ), parser.getAttributeValue( i ) );
            }

            parser.next();

            while ( parser.getEventType() != XmlPullParser.END_TAG )
            {
                if ( parser.getEventType() == XmlPullParser.START_TAG )
                {
                    node.addChild( _parse() );
                }
                else if ( parser.getEventType() == XmlPullParser.TEXT )
                {
                    if ( !IGNORE_WHITESPACES || !parser.isWhitespace() )
                    {
                        LibXmlNode child = new LibXmlNode( LibXmlNode.TEXT_NODE );
                        child.iNodeValue = parser.getText();
                        node.addChild( child );
                    }
                }
                parser.next();
            }

            return node;
        }
    }
