
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.state.*;

    /**********************************************************************************************
    *   Specify the update flow on removing an image from a GridView or a container.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodFlowImageRemove
    {
        /*****************************************************************************
        *   Being invoked after an image has been removed,
        *   this method updates the surrounding list or container.
        *****************************************************************************/
        public static final void updateImageListAfterImageRemove()
        {
            PicFoodDebug.updateAfterImageAction.out( "invoking updateActivityAfterImageDelete() .." );

            switch ( (PicFoodState)PicFoodFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //update wall AND profile because they both may contain this image!

                    //wall
                    //if ( PicFoodStatePivotal.lastSelectedTab.equals( PicFoodStatePivotal.TAB_TAG_WALL ) )
                    {
                        PicFoodDebug.updateAfterImageAction.out( "Removal has been performed on the WALL." );

                        //remove image from wall
                        if ( PicFoodFlowWall.flow != null )
                        {
                            PicFoodFlowWall.flow.removeImageFromContainer
                            (
                                PicFoodState.EPivotalMenu.getActivity(),
                                PicFoodFlowImage.lastImage,
                                PicFoodActionUpdate.EUpdateUserWallNext,
                                null
                            );
                        }
                    }

                    //profile
                    //else if ( PicFoodStatePivotal.lastSelectedTab.equals( PicFoodStatePivotal.TAB_TAG_PROFILE ) )
                    {
                        //remove image from container
                        if ( PicFoodFlowProfileImages.flow != null )
                        {
                            PicFoodFlowProfileImages.flow.removeImageFromContainer
                            (
                                PicFoodState.EPivotalMenu.getActivity(),
                                PicFoodFlowImage.lastImage,
                                PicFoodActionUpdate.EUpdateUserProfileImagesNext,
                                PicFoodFlowProfileImages.lastTitleImages
                            );
                        }

                        //update the profile data WITHOUT updating profile images
                        PicFoodActionUpdate.EUpdateOwnUserProfileDataOnly.run();
                    }
                    break;
                }

                case EDetailedImage:
                {
                    //detailed image

                    //return to pivotal menu
                    PicFoodActionState.ELeaveDetailedImage.run();
                    break;
                }

                case EForeignProfile:
                {
                    //foreign profile

                    //never passed! Foreign profile images can't be removed!
                    break;
                }

                case ESearchImagesResults:
                {
                    //image search results

                    //remove image from container
                    PicFoodFlowSearchImages.flow.removeImageFromContainer
                    (
                        PicFoodState.ESearchImagesResults.getActivity(),
                        PicFoodFlowImage.lastImage,
                        PicFoodActionUpdate.EUpdateSearchImagesResultsNext,
                        null
                    );
                    break;
                }

                default:
                {
                    break;
                }
            }
        }
    }
