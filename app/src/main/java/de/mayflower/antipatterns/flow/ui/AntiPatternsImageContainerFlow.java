
    package de.mayflower.antipatterns.flow.ui;

    import  java.util.*;
    import  org.json.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.data.AntiPatternsDataImage.ContentImageStyle;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;
    import  de.mayflower.antipatterns.ui.*;
    import  android.app.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;

    /*****************************************************************************
    *   Represents a container that contains detailed image views.
    *   Unfortunately, GridView makes problems on updating itself.
    *   This class contains the encapsulates image-container flow
    *   according to the Strategy pattern.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class AntiPatternsImageContainerFlow
    {
        /** The current offset for the images data to order. */
        protected                   int                                         iImagesOffset                    = 0;
        /** The total number of images that can be ordered. */
        protected                   int                                         iImagesCount                     = 0;
        /** All images-data and their according containers. */
        protected                   Vector<AntiPatternsImageContainerFlowContent>    iImagesData                      = new Vector<AntiPatternsImageContainerFlowContent>();

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
        public AntiPatternsImageContainerFlow(Activity activity, ViewGroup imagesContainer, LibScrollView scrollView, int aImageCount)
        {
            iImagesOffset    = 0;
            iImagesCount     = aImageCount;
            iImagesData      = new Vector<AntiPatternsImageContainerFlowContent>();

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
            final   AntiPatternsDataImage[]      newImages       = AntiPatternsDataImage.parse
                    (
                            json,
                            ContentImageStyle.EDetailed,
                            (
                                    (state == AntiPatternsState.EPivotalMenu && AntiPatternsStatePivotal.isTabSelected(AntiPatternsStatePivotal.TAB_TAG_WALL))
                                            || (state == AntiPatternsState.ESearchImagesResults)
                            )
                    );

            //AntiPatternsDebug.updateAfterImageAction.out( "parsed [" + newImages.size() + "] images from JSON." );

            //add new user-images to stack and increase the offset
            iImagesOffset += newImages.length;

            //create all image-views
            for ( AntiPatternsDataImage newUserImage : newImages )
            {
                //add to imagesData
                iImagesData.addElement
                (
                    new AntiPatternsImageContainerFlowContent
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
                    AntiPatternsLoadingCircle.showAndStartLoadingCircleUIThreaded(activity, iBottomLoadingItem);

                    //order next contents again
                    actionOnPushingNoNetwork.run();
                }
            };

            //change the symbol to 'no network'
            AntiPatternsLoadingCircle.turnLoadingCircleToNoNetworkUIThreaded(activity, iBottomLoadingItem, runnable);
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
            AntiPatternsLoadingCircle.showAndStartLoadingCircle(activity, iBottomLoadingItem);

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
                        for ( AntiPatternsImageContainerFlowContent imageView : iImagesData )
                        {
                            //add the image view to the container
                            iContainer.addView( imageView.iView );
                        }

                        //enable visibility callback for image views
                        iScrollView.setTracingVisibilityViews( iImagesData.toArray( new LibScrollView.VisibilityCallback[] {} ) );

                        //add loading-circle if more profile-images can be loaded
                        if ( iImagesOffset < iImagesCount )
                        {
                            AntiPatternsDebug.limitOffset.out( "Create loading item on bottom" );

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
            AntiPatternsDataImage imageToRemove,
            Runnable            nextLoadAction,
            View                viewToShow
        )
        {
            //replace the image data and view
            for ( int i = 0; i < iImagesData.size(); ++i )
            {
                AntiPatternsDataImage image           = iImagesData.elementAt( i ).iImg;

                AntiPatternsDebug.updateAfterImageAction.out( "image id [" + image.iImageID + "]" );

                if ( image.iImageID.equals( imageToRemove.iImageID ) )
                {
                    AntiPatternsDebug.updateAfterImageAction.out( "This index [" + i + "] is the image to update!" );

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
            AntiPatternsDataImage imageToUpdate,
            Runnable            nextLoadAction,
            View                viewToShow
        )
        {
            //replace the image data and view
            out:
            for ( int i = 0; i < iImagesData.size(); ++i )
            {
                AntiPatternsImageContainerFlowContent image           = iImagesData.elementAt( i );

                AntiPatternsDebug.updateAfterImageAction.out( "image id [" + image.iImg.iImageID + "]" );

                if ( image.iImg.iImageID.equals( imageToUpdate.iImageID ) )
                {
                    AntiPatternsDebug.updateAfterImageAction.out( "This index [" + i + "] is the image to update!" );

                    AntiPatternsDataImage newImageToAssign = new AntiPatternsDataImage( imageToUpdate );

                    //update image data and view
                    iImagesData.setElementAt
                    (
                        new AntiPatternsImageContainerFlowContent
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
