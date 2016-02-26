/*  $Id: PicFoodFlowImageUpdate.java 50591 2013-08-14 11:50:47Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.flow;

    import  org.json.*;

    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.data.*;
    import  net.picfood.data.PicFoodDataImage.ContentImageStyle;
    import  net.picfood.idm.*;
    import  net.picfood.io.jsonrpc.*;
    import  net.picfood.state.*;
    import  net.picfood.state.pivotal.*;

    /**********************************************************************************************
    *   Optimization #1
    *
    *   Specifies the update flow after an image action ( like, rate, comment )
    *   has been performed. Changing the image will cause a different flow than removing it.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50591 $ $Date: 2013-08-14 13:50:47 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/flow/PicFoodFlowImageUpdate.java $"
    **********************************************************************************************/
    public class PicFoodFlowImageUpdate
    {
        /*****************************************************************************
        *   Being invoked after an image has been changed,
        *   this method updates the surrounding list or container.
        *****************************************************************************/
        public static final void updateImageListAfterImageChange()
        {
            PicFoodDebug.updateAfterImageAction.out( "invoking updateActivityAfterImageChange() .." );

            //pivotal menu
            switch ( (PicFoodState)PicFoodFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //wall
                    if ( PicFoodStatePivotal.isTabSelected( PicFoodStatePivotal.TAB_TAG_WALL ) )
                    {
                        PicFoodDebug.updateAfterImageAction.out( "Action has been performed on the WALL." );

                        //order single image data for this image
                        orderChangedImageData();
                    }
                    //profile
                    else if ( PicFoodStatePivotal.isTabSelected( PicFoodStatePivotal.TAB_TAG_PROFILE ) )
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
            PicFoodDebug.updateAfterImageAction.out( "invoking updateCompleteImageList() .." );

            //pivotal menu
            switch ( (PicFoodState)PicFoodFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //wall
                    if ( PicFoodStatePivotal.isTabSelected( PicFoodStatePivotal.TAB_TAG_WALL ) )
                    {
                        PicFoodActionUpdate.EUpdateUserWallClean.run();
                    }
                    //profile
                    else if ( PicFoodStatePivotal.isTabSelected( PicFoodStatePivotal.TAB_TAG_PROFILE ) )
                    {
                        PicFoodActionUpdate.EUpdateLastUserProfileImagesClean.run();
                    }
                    break;
                }

                case EDetailedImage:
                {
                    //detailed image
                    PicFoodActionUpdate.EUpdateDetailedImage.run();
                    break;
                }

                case EForeignProfile:
                {
                    //foreign profile
                    PicFoodActionUpdate.EUpdateLastUserProfileImagesClean.run();
                    break;
                }

                case ESearchImagesResults:
                {
                    //image search results
                    PicFoodActionUpdate.EUpdateSearchImagesResultsClean.run();
                    break;
                }

                default:
                {
                    break;
                }
            }
        }

        /*****************************************************************************
        *   Orders the updates image data for {@link PicFoodFlowImage#lastImage} from the server.
        *   If this order fails, the surrounding state is completely updated with a call
        *   to {@link #updateCompleteImageList()}. If the order succeeds, only the
        *   single image data entry is updated with a call to {@link #updateChangedImageData()}.
        *****************************************************************************/
        public static final void orderChangedImageData()
        {
            try
            {
                //order the detailed image data threaded
                JSONObject  response    = PicFoodJsonRPCImage.getImage( PicFoodFlowImage.lastState.getActivity(), PicFoodFlowImage.lastImage.iImageID );
                String      status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case PicFoodJsonRPC.ERROR_CODE_OK:
                    {
                        //update the enriched lastImage
                        PicFoodFlowImage.lastImage = new PicFoodDataImage( response, ContentImageStyle.EDetailed, true );

                        PicFoodDebug.updateAfterImageAction.out( "New image data for image [" + PicFoodFlowImage.lastImage.iImageID + "] is here" );

                        //update the according List or container
                        updateChangedImageData();

                        break;
                    }

                    case PicFoodJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        PicFoodIDM.sessionExpired( PicFoodFlowImage.lastState );
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
                if ( PicFoodJsonRPC.isIOError( t ) )
                {
                    //reload the complete list in case of error
                    updateCompleteImageList();
                }
                else
                {
                    //reload the complete list in case of error
                    updateCompleteImageList();

                    PicFoodDebug.DEBUG_THROWABLE( t, "", UncaughtException.ENo );
                }
            }
        }

        /*****************************************************************************
        *   Updates the according View in the current state.
        *   Either a GridView is updated afterwards or the profile image container.
        *****************************************************************************/
        private static final void updateChangedImageData()
        {
            switch ( (PicFoodState)PicFoodFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //update wall AND profile because they both may contain this image!

                    //wall
                    //if ( PicFoodStatePivotal.lastSelectedTab.equals( PicFoodStatePivotal.TAB_TAG_WALL ) )
                    {
                        PicFoodDebug.updateAfterImageAction.out( "Update lastImage in wall .." );

                        //update image in wall
                        if ( PicFoodFlowWall.flow != null )
                        {
                            PicFoodFlowWall.flow.updateImageInContainer
                            (
                                PicFoodState.EPivotalMenu,
                                PicFoodFlowImage.lastImage,
                                PicFoodActionUpdate.EUpdateUserWallNext,
                                null
                            );
                        }
                    }

                    //profile
                    //else if ( PicFoodStatePivotal.lastSelectedTab.equals( PicFoodStatePivotal.TAB_TAG_PROFILE ) )
                    {
                        PicFoodDebug.updateAfterImageAction.out( "Update lastImage in own profile .." );

                        //update image in container
                        if ( PicFoodFlowProfileImages.flow != null )
                        {
                            PicFoodFlowProfileImages.flow.updateImageInContainer
                            (
                                PicFoodState.EPivotalMenu,
                                PicFoodFlowImage.lastImage,
                                PicFoodActionUpdate.EUpdateUserProfileImagesNext,
                                PicFoodFlowProfileImages.lastTitleImages
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
                    PicFoodDebug.updateAfterImageAction.out( "Update lastImage in foreign profile .." );

                    //update image in profile-images
                    if ( PicFoodFlowProfileImages.flow != null )
                    {
                        PicFoodFlowProfileImages.flow.updateImageInContainer
                        (
                            PicFoodFlowProfileImages.lastState,
                            PicFoodFlowImage.lastImage,
                            PicFoodActionUpdate.EUpdateUserProfileImagesNext,
                            PicFoodFlowProfileImages.lastTitleImages
                        );
                    }
                    break;
                }

                case ESearchImagesResults:
                {
                    //image search results
                    PicFoodDebug.updateAfterImageAction.out( "Update lastImage in image-search-results .." );

                    //update image in profile-images
                    if ( PicFoodFlowSearchImages.flow != null )
                    {
                        PicFoodFlowSearchImages.flow.updateImageInContainer
                        (
                            PicFoodState.ESearchImagesResults,
                            PicFoodFlowImage.lastImage,
                            PicFoodActionUpdate.EUpdateSearchImagesResultsNext,
                            PicFoodFlowProfileImages.lastTitleImages
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
            PicFoodFlowImage.lastState.getActivity().runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        PicFoodStateDetailedImage.singleton.iDetailedImageContainer.removeAllViews();
                        PicFoodStateDetailedImage.singleton.iDetailedImageContainer.addView
                        (
                            PicFoodFlowImage.lastImage.createItemView( PicFoodFlowImage.lastState )
                        );

                        //order the bitmap for this image
                        PicFoodFlowImage.lastImage.orderImageThreaded( PicFoodFlowImage.lastState );
                    }
                }
            );
        }
    }
