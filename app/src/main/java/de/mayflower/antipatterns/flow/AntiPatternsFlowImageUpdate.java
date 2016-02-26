
    package de.mayflower.antipatterns.flow;

    import  org.json.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.data.*;
    import  de.mayflower.antipatterns.data.AntiPatternsDataImage.ContentImageStyle;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.pivotal.*;

    /**********************************************************************************************
    *   Optimization #1
    *
    *   Specifies the update flow after an image action ( like, rate, comment )
    *   has been performed. Changing the image will cause a different flow than removing it.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowImageUpdate
    {
        /*****************************************************************************
        *   Being invoked after an image has been changed,
        *   this method updates the surrounding list or container.
        *****************************************************************************/
        public static final void updateImageListAfterImageChange()
        {
            AntiPatternsDebug.updateAfterImageAction.out( "invoking updateActivityAfterImageChange() .." );

            //pivotal menu
            switch ( (AntiPatternsState) AntiPatternsFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //wall
                    if ( AntiPatternsStatePivotal.isTabSelected(AntiPatternsStatePivotal.TAB_TAG_WALL) )
                    {
                        AntiPatternsDebug.updateAfterImageAction.out( "Action has been performed on the WALL." );

                        //order single image data for this image
                        orderChangedImageData();
                    }
                    //profile
                    else if ( AntiPatternsStatePivotal.isTabSelected(AntiPatternsStatePivotal.TAB_TAG_PROFILE) )
                    {
                        //order single image data for this image
                        orderChangedImageData();
                    }
                    break;
                }

                case EDetailedImage:
                {
                    //detailed image

                    //update state 'detailed image'
                    orderChangedImageData();

                    break;
                }

                case EForeignProfile:
                {
                    //foreign profile

                    //order single image data for this image
                    orderChangedImageData();

                    break;
                }

                case ESearchImagesResults:
                {
                    //image search results

                    //order single image data for this image
                    orderChangedImageData();

                    break;
                }

                default:
                {
                    break;
                }
            }
        }

        /*****************************************************************************
        *   Being invoked if the reloading-operation of the changed image failed,
        *   this method updates the whole surrounding activity.
        *   This used to be the default flow after an image has been changed or removed,
        *   before this optimization has been implemented.
        *****************************************************************************/
        public static final void updateCompleteImageList()
        {
            AntiPatternsDebug.updateAfterImageAction.out( "invoking updateCompleteImageList() .." );

            //pivotal menu
            switch ( (AntiPatternsState) AntiPatternsFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //wall
                    if ( AntiPatternsStatePivotal.isTabSelected(AntiPatternsStatePivotal.TAB_TAG_WALL) )
                    {
                        AntiPatternsActionUpdate.EUpdateUserWallClean.run();
                    }
                    //profile
                    else if ( AntiPatternsStatePivotal.isTabSelected(AntiPatternsStatePivotal.TAB_TAG_PROFILE) )
                    {
                        AntiPatternsActionUpdate.EUpdateLastUserProfileImagesClean.run();
                    }
                    break;
                }

                case EDetailedImage:
                {
                    //detailed image
                    AntiPatternsActionUpdate.EUpdateDetailedImage.run();
                    break;
                }

                case EForeignProfile:
                {
                    //foreign profile
                    AntiPatternsActionUpdate.EUpdateLastUserProfileImagesClean.run();
                    break;
                }

                case ESearchImagesResults:
                {
                    //image search results
                    AntiPatternsActionUpdate.EUpdateSearchImagesResultsClean.run();
                    break;
                }

                default:
                {
                    break;
                }
            }
        }

        /*****************************************************************************
        *   Orders the updates image data for {@link AntiPatternsFlowImage#lastImage} from the server.
        *   If this order fails, the surrounding state is completely updated with a call
        *   to {@link #updateCompleteImageList()}. If the order succeeds, only the
        *   single image data entry is updated with a call to {@link #updateChangedImageData()}.
        *****************************************************************************/
        public static final void orderChangedImageData()
        {
            try
            {
                //order the detailed image data threaded
                JSONObject  response    = AntiPatternsJsonRPCImage.getImage(AntiPatternsFlowImage.lastState.getActivity(), AntiPatternsFlowImage.lastImage.iImageID);
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //update the enriched lastImage
                        AntiPatternsFlowImage.lastImage = new AntiPatternsDataImage( response, ContentImageStyle.EDetailed, true );

                        AntiPatternsDebug.updateAfterImageAction.out( "New image data for image [" + AntiPatternsFlowImage.lastImage.iImageID + "] is here" );

                        //update the according List or container
                        updateChangedImageData();

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(AntiPatternsFlowImage.lastState);
                        break;
                    }

                    default:
                    {
                        //reload the complete list in case of error
                        updateCompleteImageList();

                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //reload the complete list in case of error
                    updateCompleteImageList();
                }
                else
                {
                    //reload the complete list in case of error
                    updateCompleteImageList();

                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }

        /*****************************************************************************
        *   Updates the according View in the current state.
        *   Either a GridView is updated afterwards or the profile image container.
        *****************************************************************************/
        private static final void updateChangedImageData()
        {
            switch ( (AntiPatternsState) AntiPatternsFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //update wall AND profile because they both may contain this image!

                    //wall
                    //if ( AntiPatternsStatePivotal.lastSelectedTab.equals( AntiPatternsStatePivotal.TAB_TAG_WALL ) )
                    {
                        AntiPatternsDebug.updateAfterImageAction.out( "Update lastImage in wall .." );

                        //update image in wall
                        if ( AntiPatternsFlowWall.flow != null )
                        {
                            AntiPatternsFlowWall.flow.updateImageInContainer
                            (
                                AntiPatternsState.EPivotalMenu,
                                AntiPatternsFlowImage.lastImage,
                                AntiPatternsActionUpdate.EUpdateUserWallNext,
                                null
                            );
                        }
                    }

                    //profile
                    //else if ( AntiPatternsStatePivotal.lastSelectedTab.equals( AntiPatternsStatePivotal.TAB_TAG_PROFILE ) )
                    {
                        AntiPatternsDebug.updateAfterImageAction.out( "Update lastImage in own profile .." );

                        //update image in container
                        if ( AntiPatternsFlowProfileImages.flow != null )
                        {
                            AntiPatternsFlowProfileImages.flow.updateImageInContainer
                            (
                                AntiPatternsState.EPivotalMenu,
                                AntiPatternsFlowImage.lastImage,
                                AntiPatternsActionUpdate.EUpdateUserProfileImagesNext,
                                AntiPatternsFlowProfileImages.lastTitleImages
                            );
                        }
                    }
                    break;
                }

                case EDetailedImage:
                {
                    //detailed image
                    updateLastImageInDetailedImageContainer();
                    break;
                }

                case EForeignProfile:
                {
                    //foreign profile
                    AntiPatternsDebug.updateAfterImageAction.out( "Update lastImage in foreign profile .." );

                    //update image in profile-images
                    if ( AntiPatternsFlowProfileImages.flow != null )
                    {
                        AntiPatternsFlowProfileImages.flow.updateImageInContainer
                        (
                            AntiPatternsFlowProfileImages.lastState,
                            AntiPatternsFlowImage.lastImage,
                            AntiPatternsActionUpdate.EUpdateUserProfileImagesNext,
                            AntiPatternsFlowProfileImages.lastTitleImages
                        );
                    }
                    break;
                }

                case ESearchImagesResults:
                {
                    //image search results
                    AntiPatternsDebug.updateAfterImageAction.out( "Update lastImage in image-search-results .." );

                    //update image in profile-images
                    if ( AntiPatternsFlowSearchImages.flow != null )
                    {
                        AntiPatternsFlowSearchImages.flow.updateImageInContainer
                        (
                            AntiPatternsState.ESearchImagesResults,
                            AntiPatternsFlowImage.lastImage,
                            AntiPatternsActionUpdate.EUpdateSearchImagesResultsNext,
                            AntiPatternsFlowProfileImages.lastTitleImages
                        );
                    }
                    break;
                }

                default:
                {
                    break;
                }
            }
        }

        /*****************************************************************************
        *   Updates the updated View of the image that has been changed in the detailed
        *   image container of state 'detailed image'.
        *****************************************************************************/
        private static final void updateLastImageInDetailedImageContainer()
        {
            //replace the image data and view
            AntiPatternsFlowImage.lastState.getActivity().runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AntiPatternsStateDetailedImage.singleton.iDetailedImageContainer.removeAllViews();
                        AntiPatternsStateDetailedImage.singleton.iDetailedImageContainer.addView
                        (
                            AntiPatternsFlowImage.lastImage.createItemView( AntiPatternsFlowImage.lastState )
                        );

                        //order the bitmap for this image
                        AntiPatternsFlowImage.lastImage.orderImageThreaded( AntiPatternsFlowImage.lastState );
                    }
                }
            );
        }
    }
