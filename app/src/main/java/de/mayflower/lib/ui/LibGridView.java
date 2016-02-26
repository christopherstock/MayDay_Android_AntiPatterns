/*  $Id: LibGridView.java 50546 2013-08-09 14:19:00Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui;

    import  android.content.*;
    import  android.util.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.api.*;
    import  de.mayflower.lib.ui.adapter.*;

    /************************************************************************
    *   Represents a custom GridView.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50546 $ $Date: 2013-08-09 16:19:00 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/LibGridView.java $"
    ************************************************************************/
    public class LibGridView extends GridView implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener
    {
        /** The debug context. */
        private                 LibDebug                    iDebug                  = null;

        /** The associated adapter that delivers this GridView with data. */
        private                 LibAdapter                  iAdapter                = null;

        /** The last adapter item that has been klicked. */
        private                 LibAdapterData              iLastSelectedItem       = null;

        /** The action to perform once the bottom of this GridView has been reached. */
        private                 Runnable                    iActionOnBottomReach    = null;

        /** The action to perform once the bottom of this GridView has been reached. */
        private                 boolean                     iCallbackVisibility     = false;

        /************************************************************************
        *   This constructor is automatically called from the system
        *   when the according xml-class is instanciated.
        *   The application will crash if the GridView is constructed
        *   and this method is missing.
        *
        *   @param  context     The current context.
        *   @param  as          The attribute set that is passed by the system.
        ************************************************************************/
        public LibGridView( Context context, AttributeSet as )
        {
            super( context, as );
        }

        /************************************************************************
        *   Creates a new {@link LibGridView} with the specified parameters.
        *
        *   @param  context             The current context.
        *   @param  cellSpacingID       The resource-ID of the dimension that specifies
        *                               the cell padding and the padding around the GridView.
        *                               <code>-1</code> if no spacing shall be specified.
        *   @param  paddingHorzID       The resource-ID of the dimension that specifies
        *                               the horizontal padding left and right of the grid.
        *                               <code>-1</code> if no padding shall be specified.
        *   @param  paddingVertID       The resource-ID of the dimension that specifies
        *                               the vertical padding top and bottom of the grid.
        *                               <code>-1</code> if no padding shall be specified.
        *   @param  columnCount         The number of columns to set.
        *   @param  colBgID             The resource-ID of the background-resource to set.
        *   @param  selectorID          The resource-ID of the selector drawable to set.
        *   @param  layoutID            The resource-ID of the layout-file
        *                               where this GridView layout resided.
        *   @param  fadingEdgeLength    The length of fading edges in px.
        *   @param  columnWidth         The width for one column. This value will not explicitly be set
        *                               if <code>-1</code> is passed.
        *   @param  callbackVisibility  Specifies, if this GridView should perform visibility callbacks to it's views.
        *   @param  debug               The debug context.
        *   @return                     The configured GridView.
        ************************************************************************/
        public static final LibGridView create
        (
            Context     context,
            int         cellSpacingID,
            int         paddingHorzID,
            int         paddingVertID,
            int         columnCount,
            int         colBgID,
            int         selectorID,
            int         layoutID,
            int         fadingEdgeLength,
            int         columnWidth,
            boolean     callbackVisibility,
            LibDebug    debug
        )
        {
            //inflate GridView
            LibGridView ret = (LibGridView)LibUI.getInflatedLayoutById( context, layoutID );

            //assign debug context
            ret.iDebug   = debug;

            //assign visibility callback
            ret.iCallbackVisibility = callbackVisibility;

            //set initial adapter
            ret.iAdapter = new LibAdapter( new LibAdapterData[] {} );
            ret.setAdapter( ret.iAdapter );

            //disable selector
            ret.setSelector( selectorID );

            //set properties
            ret.setOnItemClickListener(         ret                             );
            ret.setVerticalFadingEdgeEnabled(   true                            );
            ret.setFadingEdgeLength(            fadingEdgeLength                );
            ret.setBackgroundResource(          colBgID                         );
            ret.setNumColumns(                  columnCount                     );

            //set vertical spacing if specified
            if ( cellSpacingID  != -1 )
            {
                //set horizontal and vertical spacing
                int cellSpacing = LibResource.getResourceDimensionInPixel( context, cellSpacingID );
                ret.setHorizontalSpacing( cellSpacing );
                ret.setVerticalSpacing(   cellSpacing );
            }

            //set padding for the GridView
            int paddingHorz = ( paddingHorzID == -1 ? 0 : LibResource.getResourceDimensionInPixel( context, paddingHorzID ) );
            int paddingVert = ( paddingVertID == -1 ? 0 : LibResource.getResourceDimensionInPixel( context, paddingVertID ) );
            ret.setPadding( paddingHorz, paddingVert, paddingHorz, paddingVert );

            //set column width if specified
            if ( columnWidth != -1 )
            {
                //specify the column width
                ret.setColumnWidth( columnWidth );

                //ret.setColumnWidth( LibResource.getResourceDimensionInPixel( context, columnWidthID ) );

                //alter column stretch mode
                //ret.setStretchMode( GridView.STRETCH_SPACING_UNIFORM );
            }

            //set fading for scrollbars
            if ( !LibAPI.isSdkLevelLower5() )
            {
                LibModernAPI5.setScrollbarFadingEnabled( ret, true );
            }

            //set fading edges explicitly
            if ( !LibAPI.isSdkLevelLower9() )
            {
                LibModernAPI9.setOverScrollModeEnabled( ret, true );
            }

            //set scroll-listener
            ret.setOnScrollListener( ret );

            //return the GridView
            return ret;
        }

        /************************************************************************
        *   Returns the last item that has been clicked
        *   or <code>null</code> if no item has been clicked yet.
        *
        *   @return     The last selected item data or <code>null</code>
        *               if no item has been selected yet.
        ************************************************************************/
        public final LibAdapterData getLastSelectedItem()
        {
            return iLastSelectedItem;
        }

        /************************************************************************
        *   Alters all current items for this GridView.
        *
        *   @param  newData     The new data to assign to this view's adapter.
        ************************************************************************/
        public final void changeData( LibAdapterData[] newData )
        {
            //change adapter data
            iAdapter.changeData( newData );
        }

        /************************************************************************
        *   Assigns a new action to launch when the bottom of this GridView becomes visible.
        *
        *   @param  newActionOnBottomReach  The new bottom-reach-action to assign.
        ************************************************************************/
        public final void setActionOnBottomReach( Runnable newActionOnBottomReach )
        {
            //PicFoodDebug.major.out( "setActionOnBottomReach: [" + newActionOnBottomReach + "]" );

            iActionOnBottomReach = newActionOnBottomReach;
        }

        /************************************************************************
        *   Delivers the current action to launch when the bottom of this GridView becomes visible.
        *
        *   @return     The current assigned bottom-reach-action.
        ************************************************************************/
        public final Runnable getActionOnBottomReach()
        {
            return iActionOnBottomReach;
        }

        /************************************************************************
        *   Being invoked by the system when one item is clicked.
        ************************************************************************/
        @Override
        public void onItemClick( AdapterView<?> av, View v, int index, long l )
        {
            iDebug.out( "onItemClick [" + av + "][" + v + "][" + index + "][" + l + "]" );

            //assign clicked index
          //iLastSelectedIndex  = index;

            //assign clicked object
            iLastSelectedItem = iAdapter.getItem( index );

            //perform associated action
            if ( iLastSelectedItem.getActionOnClick() != null )
            {
                iLastSelectedItem.getActionOnClick().run();
            }
        }

        @Override
        public void onScroll( AbsListView alv, int firstVisibleItem, int visibleItemCount, int totalItemCount )
        {
            //PicFoodDebug.gridview.out( "onScroll [" + alv + "][" + firstVisibleItem + "][" + visibleItemCount + "] lastVisible: [" + lastVisibleItem + "] totalCount: [" + totalItemCount + "]" );

            //check if an action to perform on scrolling to bottom is available
            if ( iActionOnBottomReach != null )
            {
                //get last visible item index
                int lastVisibleItem = getLastVisiblePosition();

                //check if the bottom cell is visible
                if ( lastVisibleItem == totalItemCount - 1 )
                {
                    iDebug.out( "GridView-scrolling reached BOTTOM - Launch bottomReachedAction [" + iActionOnBottomReach + "]" );

                    //assign and destroy original action in order to prevent multiple invocation
                    Runnable actionToPerform = iActionOnBottomReach;
                    iActionOnBottomReach = null;

                    //run action to perform
                    actionToPerform.run();
                }
            }

            //callback visibility to every item
            if ( iCallbackVisibility )
            {
                int fvi = getFirstVisiblePosition();
                int lvi = getLastVisiblePosition();

                //iDebug.out( "GridView-scrolled - first [" + fvi + "] last [" + lvi + "]" );

                //pick and browse all adapter data
                LibAdapterData[] data = iAdapter.getData();
                if ( data != null )
                {
                    for ( int i = 0; i < data.length; ++i )
                    {
                        //callback visibility
                        boolean visible = ( i >= fvi && i <= lvi );
                        data[ i ].changeVisibility( visible );
                    }
                }
            }
        }

        /************************************************************************
        *   Notifies the associated adapter, that this GridView
        ************************************************************************/
        public void notifyAdapterDataSetChanged()
        {
            iAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScrollStateChanged( AbsListView alv, int i1 )
        {
            //implemented by OnScrollListener - ignore this event
            //PicFoodDebug.gridview.out( "onScrollStateChanged [" + alv + "][" + i1 + "]" );
        }
/*
        public void recycleAllViews()
        {
            //recycle all existent views
            LibAdapterData[] data = iAdapter.getData();
            for ( LibAdapterData d : data )
            {
                LibUI.recycleAllChildBitmapViews( d.getView() );
            }
        }
*/
        /************************************************************************
        *   Being invoked by the system when the height of this GridView is measured.
        ************************************************************************/
/*
        @Override
        public void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
        {
            //check if this GridView shall expand to the cumulative height of all it's items.
            if ( iExpdandToMaxHeight )
            {
                //calculate entire height by providing a very large height hint, but do not use the highest 2 bits of this integer, these are reserved for the MeasureSpec mode.
                int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST );
                super.onMeasure( widthMeasureSpec, expandSpec );

                //read measured content height
                int iContentHeight = getMeasuredHeight();

                //assign measured content height to layout params
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = iContentHeight;
            }
            else
            {
                //simply invoke the super method
                super.onMeasure( widthMeasureSpec, heightMeasureSpec );
            }
        }
*/
    }
