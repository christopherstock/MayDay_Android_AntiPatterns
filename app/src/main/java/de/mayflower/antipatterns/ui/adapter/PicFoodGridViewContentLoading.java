
    package de.mayflower.antipatterns.ui.adapter;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.adapter.PicFoodAdapterManager.GridViews;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.adapter.*;

    /************************************************************************
    *   Represents a GridView item that represents a spinning loading circle.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    ************************************************************************/
    public class PicFoodGridViewContentLoading implements LibAdapterData
    {
        /************************************************************************
        *   The two states that a loading icon can have.
        ************************************************************************/
        public static enum LoadingState
        {
            /************************************************************************
            *   A spinning loading circle indicates the user, that
            *   loading is in progress.
            ************************************************************************/
            ELoadingCircle,

            /************************************************************************
            *   A 'no network' symbol indicates the user, that loading failed
            *   and that he can repeat the loading operation by pressing the 'no network'
            *   symbol again.
            ************************************************************************/
            ENoNetwork,

            ;
        }

        /** The View that is created by this GridView item. */
        private                     View                        iView                   = null;

        /** Specifies, if the spacer above the loading circle shall be shown. */
        private                     boolean                     iShowTopSpacer          = false;
        /** Specifies, if the spacer below the loading circle shall be shown. */
        private                     boolean                     iShowBottomSpacer       = false;

        /** The action to perform when the loading item is clicked. */
        private                     Runnable                    iActionOnClick          = null;

        /** The current loading state of this item. */
        private                     LoadingState                iLoadingState           = null;

        /************************************************************************
        *   Creates a new loading item to use in a GridView.
        *
        *   @param  aActivity           The according activity context.
        *   @param  aShowTopSpacer      Determines, if the top spacer shall be shown.
        *   @param  aShowBottomSpacer   Determines, if the bottom spacer shall be shown.
        ************************************************************************/
        public PicFoodGridViewContentLoading( LibState aActivity, boolean aShowTopSpacer, boolean aShowBottomSpacer )
        {
            //assign bg
            iShowTopSpacer      = aShowTopSpacer;
            iShowBottomSpacer   = aShowBottomSpacer;

            //set loading-state as the initial state
            changeToLoading( aActivity );
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

        /************************************************************************
        *   Changes the item to a 'loading' symbol.
        *   This will flush the {@link #iActionOnClick}.
        *
        *   @param  state               The according state.
        ************************************************************************/
        public void changeToLoading( LibState state )
        {
            //flush onClickAction
            iActionOnClick = PicFoodAction.ENone;

            //change loading state
            iLoadingState = LoadingState.ELoadingCircle;

            //update the view
            updateView( state );
        }

        /************************************************************************
        *   Changes the item to a 'no network' symbol.
        *   This will re-assign the OnClick-action.
        *
        *   @param  state           The according state.
        *   @param  actionOnClick   The action to perform if this icon is clicked.
        ************************************************************************/
        public void changeToNoNetwork( LibState state, Runnable actionOnClick )
        {
            //assign onClickAction
            iActionOnClick = actionOnClick;

            //change loading state
            iLoadingState = LoadingState.ENoNetwork;

            //update the view
            updateView( state );
        }

        @Override
        public View createView( LibState state )
        {
            //create the View
            ViewGroup ret = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_grid_view_loading );

            //set unselectable bg
            ret.setBackgroundResource( R.drawable.net_picfood_selector_transparent );

            //hide top spacer if desired
            if ( !iShowTopSpacer )
            {
                ret.findViewById( R.id.top_spacer ).setVisibility( View.GONE );
            }

            //hide bottom spacer if desired
            if ( !iShowBottomSpacer )
            {
                ret.findViewById( R.id.bottom_spacer ).setVisibility( View.GONE );
            }

            //reference loading-circle
            ImageView loadingCircle = (ImageView)ret.findViewById( R.id.loading_circle );

            //set according loading icon
          //PicFoodDebug.bugfix.out( " create view with [" + iLoadingState + "]" );
            switch ( iLoadingState )
            {
                case ELoadingCircle:
                {
                    PicFoodLoadingCircle.showAndStartLoadingCircle( state.getActivity(), loadingCircle );
                    break;
                }

                case ENoNetwork:
                {
                    PicFoodLoadingCircle.turnLoadingCircleToNoNetwork( loadingCircle, null );
                    break;
                }
            }

            return ret;
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
            //return onClick-action
            return iActionOnClick;
        }

        /************************************************************************
        *   Changes this item to a clickable 'no network' symbol.
        *
        *   @param  state           The according state.
        *   @param  gridViewManager The according GridView to invalidate after the icon has been changed.
        *   @param  actionOnClick   The action to perform on clicking the 'no network'-item.
        ************************************************************************/
        public final void setupAndChangeToNoNetwork( final LibState state, final GridViews gridViewManager, final Runnable actionOnClick )
        {
            //construct action - prefix changing back to loading icon
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    //change the symbol back to 'loading' and invalidate the according GridView
                    changeToLoading( state );
                    PicFoodAdapterManager.getSingleton( state.getActivity(), gridViewManager ).invalidateGridViewUIThreaded( state );

                    //run the click-action
                    actionOnClick.run();
                }
            };

            //change the symbol to 'no network' and invalidate the according GridView - assign the specified action
            changeToNoNetwork( state, runnable );
            PicFoodAdapterManager.getSingleton( state.getActivity(), gridViewManager ).invalidateGridViewUIThreaded( state );
        }

        @Override
        public synchronized void changeVisibility( boolean visible )
        {
        }
    }
