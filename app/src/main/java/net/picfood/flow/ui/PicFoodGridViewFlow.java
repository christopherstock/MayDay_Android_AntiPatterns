/*  $Id: PicFoodGridViewFlow.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow.ui;

    import  java.util.*;
    import  net.picfood.*;
    import  net.picfood.data.*;
    import  net.picfood.ui.*;
    import  net.picfood.ui.adapter.*;
    import  net.picfood.ui.adapter.PicFoodAdapterManager.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.adapter.*;
    import  de.mayflower.lib.ui.dialog.*;

    /*****************************************************************************
    *   Represents the flow of loading and reloading a GridView.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/ui/PicFoodGridViewFlow.java $"
    *****************************************************************************/
    public class PicFoodGridViewFlow
    {
        /** The loading content item that is currently displayed on the bottom of the scrolling list. */
        protected                           PicFoodGridViewContentLoading   iLastBottomLoadingItem  = null;

        /** All adapter data that is currently contained in the GridView. */
        private                             Vector<LibAdapterData>          iData                   = new Vector<LibAdapterData>();
        /** The current offset for the data to order. */
        private                             int                             iOffset                 = 0;
        /** The total number of items that can be ordered. */
        private                             int                             iCount                  = 0;

        /*****************************************************************************
        *   Creates a new flow to manage items in a GridView.
        *
        *   @param  aCount  The initial total count to assign.
        *****************************************************************************/
        public PicFoodGridViewFlow( int aCount )
        {
            iCount   = aCount;
            iOffset  = 0;
            iData.removeAllElements();
        }

        @Override
        public String toString()
        {
            return
            (
                    " count  [" + iCount                                   + "] "
                +   " size   [" + iData.size()                             + "] "
                +   " offset [" + iOffset                                  + "] "
            );
        }

        /*****************************************************************************
        *   Delivers the current offset for this flow.
        *
        *   @return     The current assigned offset for this flow.
        *****************************************************************************/
        public int getOffset()
        {
            return iOffset;
        }

        /*****************************************************************************
        *   Being invoked, when new data arrives for this flow.
        *
        *   @param  state               The according state.
        *   @param  newData             The new data to add to this flow.
        *   @param  newCount            The new parsed count to update the old count with.
        *                               Will only be assigned if this value is higher than 0.
        *   @param  actionOnClick       The action to perform when an item is clicked.
        *   @param  gridView            The according GridView.
        *   @param  actionNextLoad      The action to perform when the user scrolls to the bottom
        *                               of this GridView and more items can be loaded.
        *   @param  showNextLoadingItem A flag to force displaying the 'loading next items' item.
        *****************************************************************************/
        public void handleParsedData
        (
            LibState            state,
            PicFoodData[]       newData,
            int                 newCount,
            Runnable            actionOnClick,
            GridViews           gridView,
            Runnable            actionNextLoad,
            boolean             showNextLoadingItem
        )
        {
            //only assign counts higher 0
            if ( newCount > 0 )
            {
                iCount = newCount;
            }

            //increase the offset and add comments to lastImage
            iOffset += newData.length;

            //prune existent loading-items
            PicFoodUI.pruneLoadingItems( iData );

            //create new adapter data
            for ( PicFoodData data : newData )
            {
                //create new item
                LibAdapterData newItem = new PicFoodGridViewContent
                (
                    state,
                    data,
                    actionOnClick,
                    gridView
                );

                //order image threaded
                data.orderImageThreaded( state );

                //add new item to stack
                iData.addElement( newItem );
            }

            //output after ordering
            PicFoodDebug.limitOffset.out( "AFTER ordering comments\n" + toString() );

            //check if more data can be loaded
            Runnable actionOnBottomReach = null;
            if ( iOffset < iCount || showNextLoadingItem )
            {
                PicFoodDebug.limitOffset.out( "Set loading item on last position" );

                //add loading icon
                iLastBottomLoadingItem = new PicFoodGridViewContentLoading( state, true, true );
                iData.addElement( iLastBottomLoadingItem );

                //set action to perform on scrolling to the bottom
                actionOnBottomReach = actionNextLoad;
            }

            //dismiss progress dialog
            LibDialogProgress.dismissProgressDialogUIThreaded( state.getActivity() );

            //assign new adapter data to GridView
            PicFoodAdapterManager.getSingleton( state.getActivity(), gridView ).changeDataUIThreaded( state, iData, actionOnBottomReach );
        }

        /**********************************************************************************************
        *   Changes the loading item {@link #iLastBottomLoadingItem} on the bottom of the scrolling list
        *   to a 'no network' icon and assigns the specified OnClick-action.
        *
        *   @param  state           The according state.
        *   @param  gridView        The GridView that holds the item to change.
        *   @param  actionOnClick   The action to perform when the 'no network' icon is clicked.
        **********************************************************************************************/
        public final void changeNextLoadingItemToNoNetwork( LibState state, GridViews gridView, Runnable actionOnClick )
        {
            iLastBottomLoadingItem.setupAndChangeToNoNetwork( state, gridView, actionOnClick );
        }
    }
