/*  $Id: PicFoodImageContainerFlowContent.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow.ui;

    import  net.picfood.data.*;
    import  android.view.*;

    import de.mayflower.lib.*;
    import  de.mayflower.lib.ui.widget.LibScrollView.*;

    /*****************************************************************************
    *   Manages the container View and the contained image data
    *   for displaying one image in a scrolling View.
    *   Unfortunately, GridView is not suitable for mutable data!
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/ui/PicFoodImageContainerFlowContent.java $"
    *****************************************************************************/
    public class PicFoodImageContainerFlowContent implements VisibilityCallback
    {
        /** The state of the flow this content's represented in. */
        public                          LibState            iState                                  = null;
        /** The container view. */
        public                          View                iView                                   = null;
        /** The image data. */
        public                          PicFoodDataImage    iImg                                    = null;
        /** Flags if this item is currently visible. */
        private                         boolean             iCurrentlyVisible                       = false;

        /*****************************************************************************
        *   Creates a new image container view.
        *
        *   @param  aState      The state that holds the flow where this image shall be shown.
        *   @param  aView       The view.
        *   @param  aImg        The image data.
        *****************************************************************************/
        public PicFoodImageContainerFlowContent( LibState aState, View aView, PicFoodDataImage aImg )
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
                    //PicFoodDebug.bitmapRecycling.out( "View becomes visible - ordering bitmaps" );

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
                    //PicFoodDebug.bitmapRecycling.out( "View becomes hidden - recycling bitmaps" );

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
