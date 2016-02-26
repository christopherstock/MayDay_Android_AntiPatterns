
    package de.mayflower.antipatterns.flow.ui;

    import  de.mayflower.antipatterns.data.*;
    import  android.view.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.widget.LibScrollView.*;

    /*****************************************************************************
    *   Manages the container View and the contained image data
    *   for displaying one image in a scrolling View.
    *   Unfortunately, GridView is not suitable for mutable data!
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class AntiPatternsImageContainerFlowContent implements VisibilityCallback
    {
        /** The state of the flow this content's represented in. */
        public                          LibState            iState                                  = null;
        /** The container view. */
        public                          View                iView                                   = null;
        /** The image data. */
        public AntiPatternsDataImage iImg                                    = null;
        /** Flags if this item is currently visible. */
        private                         boolean             iCurrentlyVisible                       = false;

        /*****************************************************************************
        *   Creates a new image container view.
        *
        *   @param  aState      The state that holds the flow where this image shall be shown.
        *   @param  aView       The view.
        *   @param  aImg        The image data.
        *****************************************************************************/
        public AntiPatternsImageContainerFlowContent(LibState aState, View aView, AntiPatternsDataImage aImg)
        {
            iState  = aState;
            iView   = aView;
            iImg    = aImg;
        }

        @Override
        public void setVisibility( boolean visible )
        {
            //check visibility
            if ( visible )
            {
                //check if view shows up
                if ( !iCurrentlyVisible )
                {
                    iCurrentlyVisible = true;
                    //AntiPatternsDebug.bitmapRecycling.out( "View becomes visible - ordering bitmaps" );

                    //order the bitmaps for this wall-image
                    iImg.orderImageThreaded( iState );
                }
            }
            else
            {
                //check if view hides
                if ( iCurrentlyVisible )
                {
                    iCurrentlyVisible = false;
                    //AntiPatternsDebug.bitmapRecycling.out( "View becomes hidden - recycling bitmaps" );

                    //recycle all Bitmap
                    iImg.recycleBitmaps();
                }
            }
        }

        @Override
        public View getView()
        {
            return iView;
        }
    }
