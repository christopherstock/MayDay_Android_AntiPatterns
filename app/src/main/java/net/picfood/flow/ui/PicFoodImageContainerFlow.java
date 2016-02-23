/*  $Id: PicFoodImageContainerFlow.java 50564 2013-08-12 11:44:08Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow.ui;

    import  java.util.*;
    import  org.json.*;
    import  net.picfood.*;
    import  net.picfood.data.*;
    import  net.picfood.data.PicFoodDataImage.ContentImageStyle;
    import  net.picfood.state.*;
    import  net.picfood.state.pivotal.*;
    import  net.picfood.ui.*;
    import  android.app.*;
    import  android.view.*;
    import  android.widget.*;
    import  com.synapsy.android.lib.*;
    import  com.synapsy.android.lib.io.*;
    import  com.synapsy.android.lib.ui.*;
    import  com.synapsy.android.lib.ui.widget.*;

    /*****************************************************************************
    *   Represents a container that contains detailed image views.
    *   Unfortunately, GridView makes problems on updating itself.
    *   This class contains the encapsulates image-container flow
    *   according to the Strategy pattern.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50564 $ $Date: 2013-08-12 13:44:08 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/ui/PicFoodImageContainerFlow.java $"
    *****************************************************************************/
    public class PicFoodImageContainerFlow
    {
        /** The current offset for the images data to order. */
        protected                   int                                         iImagesOffset                    = 0;
        /** The total number of images that can be ordered. */
        protected                   int                                         iImagesCount                     = 0;
        /** All images-data and their according containers. */
        protected                   Vector<PicFoodImageContainerFlowContent>    iImagesData                      = new Vector<PicFoodImageContainerFlowContent>();

        /** The parent ScrollView of the last representation for profile data. */
        protected                   LibScrollView                               iScrollView                     = null;
        /** The images-container of the last representation for profile images. */
        protected                   ViewGroup                                   iContainer                      = null;
        /** The loading content item that is currently displayed on the bottom of the scrolling list. */
        protected                   ImageView                                   iBottomLoadingItem              = null;

        /*****************************************************************************
        *   Creates the flow for an image container.
        *
        *   @param  activity            The according activity context.
        *   @param  imagesContainer     The container that shall be filled with images.
        *   @param  scrollView          The ScrollView that contains the imagesContainer.
        *   @param  aImageCount         The initial count of images to load.
        *****************************************************************************/
        public PicFoodImageContainerFlow( Activity activity, ViewGroup imagesContainer, LibScrollView scrollView, int aImageCount )
        {
            iImagesOffset    = 0;
            iImagesCount     = aImageCount;
            iImagesData      = new Vector<PicFoodImageContainerFlowContent>();

            //assign and truncate image container
            iContainer = imagesContainer;
            LibUI.removeAllViewsUIThreaded( activity, iContainer );

            //assign scrollview and disable visibility callback for image views and prune bottom-reach-action to prevent it from being launched on clearing the view!
            iScrollView = scrollView;
            iScrollView.setTracingVisibilityViews( null        );
            iScrollView.setOnVisibleAction(        null, null  );
        }

        @Override
        public String toString()
        {
            return
            (
                    " CountImages [" + iImagesCount           + "] "
                +   " ImagesSize  [" + iImagesData.size()     + "] "
                +   " Offset      [" + iImagesOffset          + "] "
            );
        }

        /*****************************************************************************
        *   Being invoked, when a new image response arrives for this flow.
        *
        *   @param  state           The according state.
        *   @param  json            The json response that holds the images to parse.
        *   @param  nextLoadAction  The action to assign when the container is scrolled to it's bottom
        *                           AND more images can be loaded.
        *   @param  viewToShow      The view to change it's visibility to {@link View#VISIBLE}
        *                           when the content is recreated. This could be a headline view.
        *   @throws Throwable       If any error occurs in this method, e.g. on parsing the json response.
        *****************************************************************************/
        public void handleNewImages
        (
            LibState        state,
            JSONObject      json,
            Runnable        nextLoadAction,
            View            viewToShow
        )
        throws Throwable
        {
                                            iImagesCount    = LibJSON.getJSONIntegerSecure( json, "count" );
            final   PicFoodDataImage[]      newImages       = PicFoodDataImage.parse
            (
                json,
                ContentImageStyle.EDetailed,
                (
                        ( state == PicFoodState.EPivotalMenu && PicFoodStatePivotal.isTabSelected( PicFoodStatePivotal.TAB_TAG_WALL ) )
                    ||  ( state == PicFoodState.ESearchImagesResults )
                )
            );

            //PicFoodDebug.updateAfterImageAction.out( "parsed [" + newImages.size() + "] images from JSON." );

            //add new user-images to stack and increase the offset
            iImagesOffset += newImages.length;

            //create all image-views
            for ( PicFoodDataImage newUserImage : newImages )
            {
                //add to imagesData
                iImagesData.addElement
                (
                    new PicFoodImageContainerFlowContent
                    (
                        state,
                        newUserImage.createItemView( state ),
                        newUserImage
                    )
                );
            }

            //recreate all profile-images
            recreateProfileImagesContainerUIThreaded
            (
                state.getActivity(),
                nextLoadAction,
                viewToShow
            );
        }

        /*****************************************************************************
        *   Changes the 'loading'-item to a 'no network'-item.
        *
        *   @param  activity                    The according activity context.
        *   @param  actionOnPushingNoNetwork    The action to perform when the 'no network'-item is pushed.
        *****************************************************************************/
        public void changeNextLoadingItemToNoNetwork( final Activity activity, final Runnable actionOnPushingNoNetwork )
        {
            //construct action - prefix changing back to loading icon
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    //change the symbol back to 'loading'
                    PicFoodLoadingCircle.showAndStartLoadingCircleUIThreaded( activity, iBottomLoadingItem );

                    //order next contents again
                    actionOnPushingNoNetwork.run();
                }
            };

            //change the symbol to 'no network'
            PicFoodLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded( activity, iBottomLoadingItem, runnable );
        }

        /**********************************************************************************************
        *   Concats a loading circle to the bottom of the {@link #iContainer}.
        *
        *   @param  activity                            The according activity context.
        *   @param  nextLoadAction                      The action to assign when the container is scrolled to it's bottom
        *                                               AND more images can be loaded.
        *   @param  showTopSpacer                       Determines, if the loading circle shall show it's top spacer.
        *   @param  assignNextLoadActionOnVisibility    Determines, if this loading-circle shall launch the
        *                                               'load next profile images data' on getting visible.
        **********************************************************************************************/
        public final void addAndShowLoadingCircle
        (
            Activity    activity,
            Runnable    nextLoadAction,
            boolean     showTopSpacer,
            boolean     assignNextLoadActionOnVisibility
        )
        {
            //pick bottom view and add it to the profile-images-container
            View lastBottomLoadingView = LibUI.getInflatedLayoutById( activity, R.layout.content_loading );
            iContainer.addView( lastBottomLoadingView );

            //show top spacer?
            View topSpacer = lastBottomLoadingView.findViewById( R.id.top_spacer );
            if ( !showTopSpacer )
            {
                topSpacer.setVisibility( View.GONE );
            }

            //start animation
            iBottomLoadingItem = (ImageView)lastBottomLoadingView.findViewById( R.id.loading_circle );
            PicFoodLoadingCircle.showAndStartLoadingCircle( activity, iBottomLoadingItem );

            //assign action to perform if this view is visible
            if ( assignNextLoadActionOnVisibility )
            {
                iScrollView.setOnVisibleAction( lastBottomLoadingView, nextLoadAction );
            }
        }

        /**********************************************************************************************
        *   Prunes and recreates all views in the {@link #iContainer}.
        *
        *   @param  activity            The according activity context.
        *   @param  nextLoadAction      The action to assign when the container is scrolled to it's bottom
        *                               AND more images can be loaded.
        *   @param  viewToShow          The view to change it's visibility to {@link View#VISIBLE}
        *                               when the content is recreated. This could be a headline view.
        **********************************************************************************************/
        public final void recreateProfileImagesContainerUIThreaded
        (
            final   Activity    activity,
            final   Runnable    nextLoadAction,
            final   View        viewToShow
        )
        {
            //add all image-views to the specified container UI-Threaded
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //show view if available
                        if ( viewToShow != null ) viewToShow.setVisibility( View.VISIBLE );

                        //ditch the profile-images-container
                        iContainer.removeAllViews();

                        //add all image views
                        for ( PicFoodImageContainerFlowContent imageView : iImagesData )
                        {
                            //add the image view to the container
                            iContainer.addView( imageView.iView );
                        }

                        //enable visibility callback for image views
                        iScrollView.setTracingVisibilityViews( iImagesData.toArray( new LibScrollView.VisibilityCallback[] {} ) );

                        //add loading-circle if more profile-images can be loaded
                        if ( iImagesOffset < iImagesCount )
                        {
                            PicFoodDebug.limitOffset.out( "Create loading item on bottom" );

                            //add and show loading circle
                            addAndShowLoadingCircle( activity, nextLoadAction, false, true );
                        }
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Prunes and recreates all views in the {@link #iContainer}.
        *
        *   @param  activity            The according activity context.
        *   @param  imageToRemove       The image that shall be removed from this container.
        *   @param  nextLoadAction      The action to assign when the container is scrolled to it's bottom
        *                               AND more images can be loaded.
        *   @param  viewToShow          The view to change it's visibility to {@link View#VISIBLE}
        *                               when the content is recreated. This could be a headline view.
        **********************************************************************************************/
        public final void removeImageFromContainer
        (
            Activity            activity,
            PicFoodDataImage    imageToRemove,
            Runnable            nextLoadAction,
            View                viewToShow
        )
        {
            //replace the image data and view
            for ( int i = 0; i < iImagesData.size(); ++i )
            {
                PicFoodDataImage        image           = iImagesData.elementAt( i ).iImg;

                PicFoodDebug.updateAfterImageAction.out( "image id [" + image.iImageID + "]" );

                if ( image.iImageID.equals( imageToRemove.iImageID ) )
                {
                    PicFoodDebug.updateAfterImageAction.out( "This index [" + i + "] is the image to update!" );

                    //update image data and view
                    iImagesData.removeElementAt( i );

                    continue;
                }
            }

            //recreate profile image container
            recreateProfileImagesContainerUIThreaded
            (
                activity,
                nextLoadAction,
                viewToShow
            );
        }

        /**********************************************************************************************
        *   Updates the specified image in this container.
        *
        *   @param  state               The according state.
        *   @param  imageToUpdate       The image that shall be updated in this container.
        *   @param  nextLoadAction      The action to assign when the container is scrolled to it's bottom
        *                               AND more images can be loaded.
        *   @param  viewToShow          The view to change it's visibility to {@link View#VISIBLE}
        *                               when the content is recreated. This could be a headline view.
        **********************************************************************************************/
        public final void updateImageInContainer
        (
            LibState            state,
            PicFoodDataImage    imageToUpdate,
            Runnable            nextLoadAction,
            View                viewToShow
        )
        {
            //replace the image data and view
            out:
            for ( int i = 0; i < iImagesData.size(); ++i )
            {
                PicFoodImageContainerFlowContent       image           = iImagesData.elementAt( i );

                PicFoodDebug.updateAfterImageAction.out( "image id [" + image.iImg.iImageID + "]" );

                if ( image.iImg.iImageID.equals( imageToUpdate.iImageID ) )
                {
                    PicFoodDebug.updateAfterImageAction.out( "This index [" + i + "] is the image to update!" );

                    PicFoodDataImage newImageToAssign = new PicFoodDataImage( imageToUpdate );

                    //update image data and view
                    iImagesData.setElementAt
                    (
                        new PicFoodImageContainerFlowContent
                        (
                            state,
                            newImageToAssign.createItemView( state ),
                            newImageToAssign
                        ),
                        i
                    );

                    break out;
                }
            }

            //recreate profile image container
            recreateProfileImagesContainerUIThreaded
            (
                state.getActivity(),
                nextLoadAction,
                viewToShow
            );
        }

        /**********************************************************************************************
        *   Delivers the current image-offset for this flow.
        *   This is used to request image data via JSON-RPC.
        *
        *   @return     The current image offset for this flow.
        **********************************************************************************************/
        public int getOffset()
        {
            return iImagesOffset;
        }

        /**********************************************************************************************
        *   Unselects all buttons and links for all images.
        **********************************************************************************************/
/*
        public void unselectAllImages()
        {
            if ( iImagesData != null )
            {
                for ( PicFoodImageContainerFlowContent img : iImagesData )
                {
                    img.iImg.unselectAllButtons();
                }
            }
        }
*/
    }
