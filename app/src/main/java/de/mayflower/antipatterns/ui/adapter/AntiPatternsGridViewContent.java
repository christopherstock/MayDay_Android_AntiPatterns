
    package de.mayflower.antipatterns.ui.adapter;

    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.GridViews;
    import  android.view.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.adapter.*;

    /************************************************************************
    *   Represents one item in a GridView.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class AntiPatternsGridViewContent implements LibAdapterData
    {
        /** The GridView this item belongs to. */
        private                     GridViews           iAccordingGridView          = null;

        /** The created View of this GridView item. */
        private                     View                iView                       = null;
        /** The data item that shall be represented by this GridView item. */
        private AntiPatternsData iData                       = null;

        /** The action that shall be performed if this GridView item is clicked. */
        private                     Runnable            iActionOnClick              = null;

        /** Flags if this item is currently visible. */
        private                     boolean             iCurrentlyVisible           = false;

        /************************************************************************
        *   Creates a new GridView content item that represents the specified data.
        *
        *   @param  aActivity           The according activity context.
        *   @param  aData               The data item that shall be represented by this GridView item.
        *   @param  aActionOnClick      The action that shall be performed if this GridView item is clicked.
        *   @param  aAccordingGridView  The GridView this item belongs to.
        ************************************************************************/
        public AntiPatternsGridViewContent(LibState aActivity, AntiPatternsData aData, Runnable aActionOnClick, GridViews aAccordingGridView)
        {
            //assign user
            iData               = aData;
            iActionOnClick      = aActionOnClick;
            iAccordingGridView  = aAccordingGridView;

            //update view
            updateView( aActivity );
        }

        @Override
        public View getView()
        {
            return iView;
        }

        @Override
        public void updateView( LibState state )
        {
            //recreate the view
            iView = createView( state );
        }

        @Override
        public View createView( LibState state )
        {
            //create the view from the data-user
            return iData.createItemView( state );
        }

        /************************************************************************
        *   Returns the data item that is wrapped by this GridView item.
        *
        *   @return     The data item that is represented by this GridView item.
        ************************************************************************/
        public AntiPatternsData getData()
        {
            return iData;
        }

        /************************************************************************
        *   Updates the associated data item and recreates the view.
        *
        *   @param  state       The according state.
        *   @param  newData     The new data to assign to this GridView item.
        ************************************************************************/
        public void updateDataAndView( LibState state, AntiPatternsData newData )
        {
            //assign the new data and update the view
            iData = newData;
            updateView( state );
        }

        @Override
        public String getTitle()
        {
            //unused
            return null;
        }

        @Override
        public Runnable getActionOnClick()
        {
            return iActionOnClick;
        }

        @Override
        public synchronized void changeVisibility( boolean visible )
        {
            //check visibility
            if ( visible )
            {
                //check if view shows up
                if ( !iCurrentlyVisible )
                {
                    iCurrentlyVisible = true;
                    //PicFoodDebug.bitmapRecycling.out( "View becomes visible - ordering bitmaps" );

                    //order the bitmaps for this wall-image
                    iData.orderImageThreaded( iAccordingGridView.getAccordingState() );
                }
            }
            else
            {
                //check if view hides
                if ( iCurrentlyVisible )
                {
                    iCurrentlyVisible = false;
                    //PicFoodDebug.bitmapRecycling.out( "View becomes hidden - recycling bitmaps" );

                    //recycle all Bitmap
                    iData.recycleBitmaps();
                }
            }
        }
    }
