/*  $Id: LibScrollView.java 50447 2013-08-06 14:15:19Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui.widget;

    import  android.content.*;
    import  android.graphics.*;
    import  android.util.*;
    import  android.view.*;
    import  android.widget.*;

    /******************************************************************************************************
    *   Represents a horizontal scrolling view.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50447 $ $Date: 2013-08-06 16:15:19 +0200 (Di, 06 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/widget/LibScrollView.java $"
    ******************************************************************************************************/
    public class LibScrollView extends ScrollView
    {
        /******************************************************************************************************
        *   Specifies a callback for a view inside a {@link LibScrollView}.
        ******************************************************************************************************/
        public static interface VisibilityCallback
        {
            /******************************************************************************************************
            *   Being invoked for updating the current visibility of a view.
            *
            *   @param  visible     If this view is currently visible or not.
            ******************************************************************************************************/
            public abstract void setVisibility( boolean visible );

            /******************************************************************************************************
            *   Delivers the according view.
            *
            *   @return     The view this callback checks.
            ******************************************************************************************************/
            public abstract View getView();
        }

        /** The callback to inform when the scrolling of this view changes. */
        private                         LibScrollCallback       iCallback                               = null;

        /** The view to launch the {@link #iVisibleAction} if it gets visible on the screen. */
        private                         View                    iVisibleView                            = null;

        /** The action to launch if the view {@link #iVisibleView} gets visible on the screen. */
        private                         Runnable                iVisibleAction                          = null;

        /** This rect caches the scroll bounds to detect, if the view {@link #iVisibleView} is currently visible. */
        private                         Rect                    iScrollBounds                           = new Rect();

        /** Contained views that shall be updated with visibility callbacks. */
        private                         VisibilityCallback[]    iTracingVisibilityViews                 = null;

        /******************************************************************************************************
        *   This constructor is automatically called from the system when the according xml-class is instanciated.
        *   The application will crash if the ScrollView is constructed and this method is missing.
        *
        *   @param  context     The current context.
        *   @param  as          The attribute set that is passed by the system.
        ******************************************************************************************************/
        public LibScrollView( Context context, AttributeSet as )
        {
            super( context, as );
        }

        /******************************************************************************************************
        *   Assign a scroll-callback to this ScrollView.
        *   The callback gets notified each time the scrolling changes.
        *
        *   @param  aCallback   The scroll callback to assign.
        ******************************************************************************************************/
        public void setScrollCallback( LibScrollCallback aCallback )
        {
            iCallback = aCallback;
        }

        /******************************************************************************************************
        *   Assign a view and an action to this ScrollView.
        *   The action gets invoked once the view becomes visible.
        *
        *   @param  aInsideView     The view to check for visibility.
        *                           This view must appear inside the ScrollView
        *                           for the action to be launched.
        *   @param  aVisibleAction  The action to launch if the view gets visible.
        ******************************************************************************************************/
        public void setOnVisibleAction( View aInsideView, Runnable aVisibleAction )
        {
            iVisibleView    = aInsideView;
            iVisibleAction  = aVisibleAction;
        }

        /******************************************************************************************************
        *   Assigns new visibility callback views.
        *
        *   @param  aTracingVisibilityCallback  All views where visibility callback shall be received for.
        ******************************************************************************************************/
        public void setTracingVisibilityViews( VisibilityCallback[] aTracingVisibilityCallback )
        {
            iTracingVisibilityViews  = aTracingVisibilityCallback;
        }



        @Override
        public void onScrollChanged( int i1, int i2, int i3, int i4 )
        {
            super.onScrollChanged( i1, i2, i3, i4 );

            //callback scroll change if callback is specified
            if ( iCallback != null )
            {
                iCallback.notifyScrollChange( i1, i2, i3, i4 );
            }
        }

        @Override
        public final void onDraw( Canvas c )
        {
            //check visible component
            if ( iVisibleView != null )
            {
                //check if the insideView is currently visible inside of the scrollView
                iScrollBounds = new Rect();
                getHitRect( iScrollBounds );
                if ( iVisibleView.getLocalVisibleRect( iScrollBounds ) )
                {
                    //launch action and remove it
                    iVisibleAction.run();

                    iVisibleView   = null;
                    iVisibleAction = null;
                }
            }

            //check tracing visibility views
            if ( iTracingVisibilityViews != null )
            {
                //PicFoodDebug.bitmapRecycling.out( "Calling back [" + iTracingVisibilityViews.length + "] views that changed scrolling" );

                //browse all views
                for ( int i = 0; i < iTracingVisibilityViews.length; ++i )
                {
                    //check if the insideView is currently visible inside of the scrollView
                    getHitRect( iScrollBounds );

                    //assign visibility for this item
                    boolean visible = ( iTracingVisibilityViews[ i ].getView().getLocalVisibleRect( iScrollBounds ) );

                    //notify visibility
                    iTracingVisibilityViews[ i ].setVisibility( visible );

                    //PicFoodDebug.bitmapRecycling.out( " View [] is visible [" + isVisible + "]" );
                }
            }
        }
    }
