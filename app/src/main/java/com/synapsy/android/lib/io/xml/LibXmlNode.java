/*  $Id: LibXmlNode.java 50398 2013-08-05 10:07:28Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.io.xml;

    import  java.util.*;

    /*************************************************************************
    *   Represents a single XML-Node.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50398 $ $Date: 2013-08-05 12:07:28 +0200 (Mo, 05 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/io/xml/LibXmlNode.java $"
    *************************************************************************/
    public class LibXmlNode
    {
        /** The id of a textnode that appears between an opening and a closing element node. */
        protected   static  final   int                         TEXT_NODE       = 0;

        /** The id of an element node. */
        protected   static  final   int                         ELEMENT_NODE    = 1;

        /** The node-type for this xml node. */
        private                     int                         iNodeType       = 0;

        /** All children this xml node owns. */
        private                     Vector<LibXmlNode>          iChildren       = null;

        /** All attributes this xml node defines. */
        private                     Hashtable<String, String>   iAttributes     = null;

        /** The name of this xml node. */
        protected                   String                      iNodeName       = null;

        /** The text-node-value for this node. Only specified if this is a textnode. */
        protected                   String                      iNodeValue      = null;

        /*************************************************************************
        *   Creates a new xml node with the specified node type.
        *
        *   @param  aNodeType   The node-type for this xml node.
        *************************************************************************/
        public LibXmlNode( int aNodeType )
        {
            iNodeType   = aNodeType;
            iChildren   = new Vector<LibXmlNode>();
            iAttributes = new Hashtable<String, String>();
        }

        /*************************************************************************
        *   Returns all attribute-specifiers this node defines.
        *
        *   @return     An Array containing all attribute names of this node.
        *************************************************************************/
        public String[] getAttributeNames()
        {
            String[] names = new String[ iAttributes.size() ];

            Enumeration<String> e = iAttributes.keys();

            int i = 0;

            while ( e.hasMoreElements() )
            {
                names[ i ] = e.nextElement();
                i++;
            }
            return names;
        }

        /*************************************************************************
        *   Sets an attribute for this node. This attribute will be overridden
        *   if it already exists.
        *
        *   @param  key     The new name of the attribute to set.
        *   @param  value   The new value for this attribute.
        *************************************************************************/
        public void setAttribute( String key, String value )
        {
            iAttributes.put( key, value );
        }

        /*************************************************************************
        *   Delivers the value for the specified attribute.
        *
        *   @param  key     The name of the attribute to get the value from.
        *   @return         The value of the specified attribute
        *                   or <code>null</code> if the specified attribute does not exist.
        *************************************************************************/
        public String getAttribute( String key )
        {
            return iAttributes.get( key );
        }

        /*************************************************************************
        *   Adds a new node to this element.
        *
        *   @param  child   The node to append as a child of this node.
        *************************************************************************/
        public void addChild( LibXmlNode child )
        {
            iChildren.addElement( child );
        }

        /*************************************************************************
        *   Returns the <b>first</b> child node for this element
        *   that carries the specified name.
        *
        *   @param  name    The name of the child-node to return.
        *   @return         The first child-node with this name or
        *                   <code>null</code> if this node does not define
        *                   a child node with the specified name.
        *************************************************************************/
        public final LibXmlNode getChildElementNode( String name )
        {
            LibXmlNode[] childElements = getChildElementNodes( name );

            if ( childElements.length > 0 )
            {
                return childElements[ 0 ];
            }
            return null;
        }

        /*************************************************************************
        *   Returns all child nodes for this element that carry the specified name.
        *
        *   @param  name    The name of the child-nodes to return.
        *   @return         A Vector that holds all child nodes for this node
        *                   that match the given name.
        *************************************************************************/
        public final LibXmlNode[] getChildElementNodes( String name )
        {
            Vector<LibXmlNode>  childrenToReturn = new Vector<LibXmlNode>();
            for ( int i = 0; i < iChildren.size(); ++i )
            {
                LibXmlNode child = iChildren.elementAt( i );

                //only pick ELEMENT-Nodes!
                if
                (
                        child.iNodeType == LibXmlNode.ELEMENT_NODE
                    &&  child.iNodeName.toLowerCase( Locale.getDefault() ).equals( name.toLowerCase() )
                )
                {
                    childrenToReturn.addElement( iChildren.elementAt( i ) );
                }
            }

            //toByteArray
            LibXmlNode[] ret = new LibXmlNode[ childrenToReturn.size() ];
            for ( int i = 0; i < ret.length; ++i )
            {
                ret[ i ] = childrenToReturn.elementAt( i );
            }

            return ret;
        }

        /*************************************************************************
        *   Returns the text-node-value for this node. If this is an element node,
        *
        *   @return     The value of the text-node for this node. If this node
        *               is an element node, an empty String is returned.
        *************************************************************************/
        public final String getTextNodeValue()
        {
            if ( iNodeType == ELEMENT_NODE )
            {
                if ( iChildren.size() == 0 )
                {
                    return "";
                }

                return ( iChildren.elementAt( 0 ) ).iNodeValue;
            }

            //if ( nodeType == TEXT_NODE )
            return iNodeValue;
        }
    }
