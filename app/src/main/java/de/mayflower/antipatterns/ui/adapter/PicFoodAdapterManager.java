
    package de.mayflower.antipatterns.ui.adapter;

    import  java.util.*;
    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.ui.PicFoodUI.*;
    import  android.app.*;
    import  android.content.*;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.adapter.*;

    /************************************************************************
    *   Manages all GridViews this application makes use of.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class PicFoodAdapterManager
    {
        /************************************************************************
        *   All GridViews that are managed by the adapter-manager.
        *
        *   GridViews are only suitable in states where data can't be changed!
        ************************************************************************/
        public static enum GridViews
        {
            /** The GridView in state 'explore'. */
            EExplore(           PicFoodState.EPivotalMenu,          GridView.AUTO_FIT,  R.dimen.content_distance_normal,    R.dimen.content_distance_normal,    R.dimen.content_distance_big        ),

            /** The GridView in state 'googlePlaces'. */
            EGooglePlaces(      PicFoodState.EGooglePlaces,         1,                  R.dimen.content_distance_normal,    R.dimen.content_distance_normal,    R.dimen.content_distance_normal     ),

            /** The GridView in state 'imageProperties'. */
            EImageProperties(   PicFoodState.EImageProperties,      1,                  R.dimen.content_distance_normal,    R.dimen.content_distance_normal,    R.dimen.content_distance_normal     ),

            /** The GridView in state 'followDetails'. */
            EFollowDetails(     PicFoodState.EFollowDetails,        1,                  R.dimen.content_distance_normal,    R.dimen.content_distance_normal,    R.dimen.content_distance_normal     ),

            /** The GridView in state 'findFriendsResults'. */
            EFindFriends(       PicFoodState.EFindFriendsResults,   1,                  R.dimen.content_distance_normal,    R.dimen.content_distance_normal,    R.dimen.content_distance_normal     ),

            ;

            /************************************************************************
            *   The according adapter manager. This one is lazily created once.
            ************************************************************************/
            protected       PicFoodAdapterManager       iManager                = null;

            /************************************************************************
            *   The desired number of columns in this GridView.
            ************************************************************************/
            protected       int                         iColumnCount            = 0;

            /************************************************************************
            *   The resource-id of the dimension that specifies the column width.
            ************************************************************************/
            protected       int                         iColumnWidthID          = 0;

            /************************************************************************
            *   The resource-id of the dimension that specifies the cell-spacing.
            ************************************************************************/
            protected       int                         iCellSpacingID          = 0;

            /************************************************************************
            *   The resource-id of the dimension that specifies the horizontal padding.
            ************************************************************************/
            protected       int                         iPaddingHorzID          = 0;

            /************************************************************************
            *   The resource-id of the dimension that specifies the vertical padding.
            ************************************************************************/
            protected       int                         iPaddingVertID          = 0;

            /************************************************************************
            *   The state that displays this GridView.
            ************************************************************************/
            protected       LibState                    iState                  = null;

            /************************************************************************
            *   Creates a new GridView-manager.
            *
            *   @param  aState          The state that displays this GridView.
            *   @param  aColumnCount    The desired number of columns in this GridView.
            *   @param  aCellSpacingID  The resource-id of the dimension that specifies the cell-spacing.
            *   @param  aPaddingHorzID  The resource-id of the dimension that specifies the horizontal padding.
            *   @param  aPaddingVertID  The resource-id of the dimension that specifies the vertical padding.
            ************************************************************************/
            private GridViews
            (
                LibState        aState,
                int             aColumnCount,
                int             aCellSpacingID,
                int             aPaddingHorzID,
                int             aPaddingVertID
            )
            {
                iState          = aState;
                iColumnCount    = aColumnCount;
                iCellSpacingID  = aCellSpacingID;
                iPaddingHorzID  = aPaddingHorzID;
                iPaddingVertID  = aPaddingVertID;
            }

            /************************************************************************
            *   Returns an Activity instance that holds thid GridView.
            *
            *   @return     An instance of an Activity that contains this GridView.
            ************************************************************************/
            protected LibState getAccordingState()
            {
                return iState;
            }
        }

        /************************************************************************
        *   The GridView that is managed by this manager.
        ************************************************************************/
        public          LibGridView                     iGridView                           = null;

        /************************************************************************
        *   Delivers the singleton manager for the specified GridView.
        *
        *   @param  context     The current system context.
        *   @param  gridView    The desired gridView to pick.
        *   @return             The manager that handles the desired GridView.
        ************************************************************************/
        public static final PicFoodAdapterManager getSingleton( Context context, GridViews gridView )
        {
            if ( gridView.iManager == null ) gridView.iManager = new PicFoodAdapterManager( context, gridView );
            return gridView.iManager;
        }

        /************************************************************************
        *   Creates the manager for the specified GridView.
        *
        *   @param  context     The current system context.
        *   @param  gridView    The gridView that shall be managed by this manager.
        ************************************************************************/
        private PicFoodAdapterManager( Context context, GridViews gridView )
        {
            //create GridView once
            iGridView = LibGridView.create
            (
                context,
                gridView.iCellSpacingID,
                gridView.iPaddingHorzID,
                gridView.iPaddingVertID,
                gridView.iColumnCount,
                android.R.color.transparent,
                R.drawable.net_picfood_selector_transparent,
                R.layout.grid_view,
                LibResource.getResourceDimensionInPixel( context, R.dimen.fading_edge_size ),
                ( gridView == GridViews.EExplore ? PicFoodImage.getImageSizeToOrder( context, ImageSize.ETiledImage ) : -1 ),
                Features.ENABLE_BITMAP_RECYCLING,
                PicFoodDebug.gridView
            );
        }

        /************************************************************************
        *   Connects the GridView this manager handles to the specified ViewGroup.
        *
        *   @param  parentContainer     The ViewGroup that shall display this GridView.
        ************************************************************************/
        public final void connect( ViewGroup parentContainer )
        {
            //free GridView
            LibUI.freeFromParent( iGridView );

            //add GridView to parent container
            parentContainer.addView( iGridView );
        }

        /************************************************************************
        *   Change the data, keeping the current bottom-reach-action.
        *
        *   @param  state       The according state.
        *   @param  newData     The new adapter data to set.
        ************************************************************************/
        public final void changeDataUIThreaded
        (
            final   LibState                state,
            final   Vector<LibAdapterData>  newData
        )
        {
            changeDataUIThreaded
            (
                state,
                newData,
                getActionOnBottomReach()
            );
        }

        /************************************************************************
        *   Change the data and the current bottom-reach-action.
        *   This method runs on the UI-Thread.
        *
        *   @param  state                   The according state.
        *   @param  newData                 The new adapter data to set.
        *   @param  newActionOnBottomReach  The new action to perform once the bottom
        *                                   of this GridView comes visible.
        ************************************************************************/
        public final void changeDataUIThreaded
        (
            final   LibState                state,
            final   Vector<LibAdapterData>  newData,
            final   Runnable                newActionOnBottomReach
        )
        {
            state.getActivity().runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //set new data
                        changeData( newData.toArray( new LibAdapterData[] {} ), newActionOnBottomReach );
                    }
                }
            );
        }

        /************************************************************************
        *   Change the data and the current bottom-reach-action.
        *
        *   @param  newData                 The new adapter data to set.
        *   @param  newActionOnBottomReach  The new action to perform once the bottom
        *                                   of this GridView comes visible.
        ************************************************************************/
        protected void changeData( final LibAdapterData[] newData, final Runnable newActionOnBottomReach )
        {
            //alter GridView data
            iGridView.changeData( newData );

            //set new action on bottom reach
            setActionOnBottomReach( newActionOnBottomReach );
        }

        /************************************************************************
        *   Returns the according GridView's action that shall be performed once
        *   the list has been scrolled to the bottom.
        *
        *   @return     The action that shall be performed when the scrolling content
        *               is scrolled to it's bottom or <code>null</code> if no
        *               action is assigned.
        ************************************************************************/
        protected Runnable getActionOnBottomReach()
        {
            return iGridView.getActionOnBottomReach();
        }

        /************************************************************************
        *   Sets the GridView's action that shall be performed once
        *   the list has been scrolled to the bottom.
        *
        *   @param  newActionOnBottomReach  The action to perform when the scrolling content
        *                                   is scrolled to it's bottom or <code>null</code>
        *                                   to clear any associated action.
        ************************************************************************/
        protected void setActionOnBottomReach( Runnable newActionOnBottomReach )
        {
            iGridView.setActionOnBottomReach( newActionOnBottomReach );
        }


        /************************************************************************
        *   Replaces the GridView's current data with an empty array of items.
        *   The action on reaching the bottom of this view will additionally be cleared.
        *   This method is invoked on the UI-Thread.
        *
        *   @param  activity    The according activity context.
        ************************************************************************/
        public void clearDataUIThreaded( Activity activity )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        clearData();
                    }
                }
            );
        }

        /************************************************************************
        *   Replaces the GridView's current data with an empty array of items.
        *   The action on reaching the bottom of this view will additionally be cleared.
        ************************************************************************/
        public void clearData()
        {
            //recycle all views
            //iGridView.recycleAllViews();

            //change data to an empty set - ditch action to perform on scrolling to the bottom
            changeData( new LibAdapterData[] {}, null );
        }

        /************************************************************************
        *   Delivers the item of the GridView that was last selected.
        *
        *   @return     The item that has been last selected
        *               or <code>null</code> if no item has been selected yet.
        ************************************************************************/
        public LibAdapterData getLastSelectedItem()
        {
            return iGridView.getLastSelectedItem();
        }

        /************************************************************************
        *   Invalidates all views of a GridView.
        *
        *   @param  state       The according state.
        ************************************************************************/
        public void invalidateGridViewUIThreaded( final LibState state )
        {
            state.getActivity().runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        invalidateGridView();
                    }
                }
            );
        }

        /************************************************************************
        *   Invalidates all items of the associated GridView.
        ************************************************************************/
        protected void invalidateGridView()
        {
            iGridView.notifyAdapterDataSetChanged();
        }
    }
