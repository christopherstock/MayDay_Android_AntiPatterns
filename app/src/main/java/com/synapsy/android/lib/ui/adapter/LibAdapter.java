/*  $Id: LibAdapter.java 50546 2013-08-09 14:19:00Z schristopher $
 *  ==============================================================================================================
 */
    package com.synapsy.android.lib.ui.adapter;

    import  android.view.*;
    import  android.widget.*;

    /************************************************************************
    *   Manages menu lists via native UI.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50546 $ $Date: 2013-08-09 16:19:00 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/adapter/LibAdapter.java $"
    ************************************************************************/
    public class LibAdapter extends BaseAdapter
    {
        /** All items this adapter provides. */
        public              LibAdapterData[]            iItems              = null;

        /************************************************************************
        *   Creates a new adapter with the given data.
        *
        *   @param  aData   The initial data for this adapter to provide.
        ************************************************************************/
        public LibAdapter( LibAdapterData[] aData )
        {
            iItems = aData;
        }

        /************************************************************************
        *   Returns all data items.
        *
        *   @return     All item data this adapter provides.
        ************************************************************************/
        public LibAdapterData[] getData()
        {
            return iItems;
        }

        @Override
        public int getCount()
        {
            return iItems.length;
        }

        @Override
        public LibAdapterData getItem( int position )
        {
            return iItems[ position ];
        }

        @Override
        public long getItemId( int position )
        {
            return 0;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            return iItems[ position ].getView();
        }

        /************************************************************************
        *   Replaces all data this adapter provides with the specified data.
        *
        *   @param  newData     The new data to set for this adapter.
        ************************************************************************/
        public void changeData( LibAdapterData[] newData )
        {
            iItems = newData;
            notifyDataSetChanged();
        }

        /************************************************************************
        *   Replaces one item of the data this adapter provides with the specified data item.
        *
        *   @param  index       The index of the data item to replace.
        *   @param  newData     The new data to set on the given index.
        ************************************************************************/
        public void changeData( int index, LibAdapterData newData )
        {
            iItems[ index ] = newData;
            notifyDataSetChanged();
        }

        /************************************************************************
        *   Replaces the data this adapter with an empty data array.
        ************************************************************************/
        public final void clearData()
        {
            iItems = new LibAdapterData[] {};
            notifyDataSetChanged();
        }
    }
