
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
    public class AntiPatternsFlowImageRemove
    {
        /*****************************************************************************
        *   Being invoked after an image has been removed,
        *   this method updates the surrounding list or container.
        *****************************************************************************/
        public static final void updateImageListAfterImageRemove()
        {
            AntiPatternsDebug.updateAfterImageAction.out( "invoking updateActivityAfterImageDelete() .." );

            switch ( (AntiPatternsState) AntiPatternsFlowImage.lastState )
            {
                case EPivotalMenu:
                {
                    //update wall AND profile because they both may contain this image!

                    //wall
                    //if ( AntiPatternsStatePivotal.lastSelectedTab.equals( AntiPatternsStatePivotal.TAB_TAG_WALL ) )
                    {
                        AntiPatternsDebug.updateAfterImageAction.out( "Removal has been performed on the WALL." );

                        //remove image from wall
                        if ( AntiPatternsFlowWall.flow != null )
                        {
                            AntiPatternsFlowWall.flow.removeImageFromContainer
                            (
                                AntiPatternsState.EPivotalMenu.getActivity(),
                                AntiPatternsFlowImage.lastImage,
                                AntiPatternsActionUpdate.EUpdateUserWallNext,
                                null
                            );
                        }
                    }

                    //profile
                    //else if ( AntiPatternsStatePivotal.lastSelectedTab.equals( AntiPatternsStatePivotal.TAB_TAG_PROFILE ) )
                    {
                        //remove image from container
                        if ( AntiPatternsFlowProfileImages.flow != null )
                        {
                            AntiPatternsFlowProfileImages.flow.removeImageFromContainer
                            (
                                AntiPatternsState.EPivotalMenu.getActivity(),
                                AntiPatternsFlowImage.lastImage,
                                AntiPatternsActionUpdate.EUpdateUserProfileImagesNext,
                                AntiPatternsFlowProfileImages.lastTitleImages
                            );
                        }

                        //update the profile data WITHOUT updating profile images
                        AntiPatternsActionUpdate.EUpdateOwnUserProfileDataOnly.run();
                    }
                    break;
                }

                case EDetailedImage:
                {
                    //detailed image

                    //return to pivotal menu
                    AntiPatternsActionState.ELeaveDetailedImage.run();
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
                    AntiPatternsFlowSearchImages.flow.removeImageFromContainer
                    (
                        AntiPatternsState.ESearchImagesResults.getActivity(),
                        AntiPatternsFlowImage.lastImage,
                        AntiPatternsActionUpdate.EUpdateSearchImagesResultsNext,
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
