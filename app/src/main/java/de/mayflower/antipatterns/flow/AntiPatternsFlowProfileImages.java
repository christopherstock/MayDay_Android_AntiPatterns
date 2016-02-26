
    package de.mayflower.antipatterns.flow;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.ui.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.jsonrpc.*;
    import  org.json.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;
    import  de.mayflower.lib.util.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;

    /**********************************************************************************************
    *   Manages the users' profiles.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsFlowProfileImages
    {
        /** The last activity where profile-images have been ordered. */
        public      static          LibState                                lastState                    = null;
        /** The ID of the last user profile where profile-images shall be ordered. */
        public      static          String                                  lastUserID                      = null;
        /** The TextView that represents the title of the last profile-image-container. */
        public      static          TextView                                lastTitleImages                 = null;
        /** The last total image count of the last user profile to display. */
        public      static          int                                     lastImageCount                  = 0;
        /** The parent ScrollView of the last representation for profile data. */
        public      static          LibScrollView                           lastParentScrollView            = null;
        /** The images-container of the last representation for profile images. */
        public      static          ViewGroup                               lastProfileImagesContainer      = null;

        /** Encapsulates the image-container flow according to the Strategy pattern. */
        protected   static AntiPatternsImageContainerFlow flow                            = null;

        /*****************************************************************************
        *   Orders all images of the given user
        *   and fills them into the specified container.
        *
        *   @param  actionOnNoNetwork       The action to perform if the network connection fails.
        *   @param  actionOnTechnicalError  The action to perform if a technical error occurs.
        *****************************************************************************/
        public static final void orderUsersNextProfileImages
        (
            Runnable    actionOnNoNetwork,
            Runnable    actionOnTechnicalError
        )
        {
            AntiPatternsDebug.profile.out( AntiPatternsFlowImage.class.getName() + "orderUsersUploadedImages()" );

            try
            {
                //output before ordering
                AntiPatternsDebug.limitOffset.out( "BEFORE ordering profile-images\n" + flow.toString() );

                //order the detailed image data threaded
                JSONObject          response    = AntiPatternsJsonRPCImage.getUserImages
                        (
                                lastState.getActivity(),
                                lastUserID,
                                flow.getOffset()
                        );
                String              status      = LibJSON.getJSONStringSecure( response, "status" );

                //switch status code
                switch ( Integer.parseInt( status ) )
                {
                    case AntiPatternsJsonRPC.ERROR_CODE_OK:
                    {
                        //parse new profileImages from response and handle them
                        JSONObject images = LibJSON.getJSONObjectSecure(  response, "images" );
                        flow.handleNewImages
                        (
                            lastState,
                            images,
                            AntiPatternsActionUpdate.EUpdateUserProfileImagesNext,
                            lastTitleImages
                        );

                        //output after ordering
                        AntiPatternsDebug.limitOffset.out( "AFTER ordering profile-images" + flow.toString() );

                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_SESSION_EXPIRED:
                    {
                        //handle an expired session
                        AntiPatternsIDM.sessionExpired(lastState);
                        break;
                    }

                    case AntiPatternsJsonRPC.ERROR_CODE_INTERNAL_ERROR:
                    default:
                    {
                        //launch action on technical error
                        actionOnTechnicalError.run();

                        //report this error
                        AntiPatternsDebug.DEBUG_THROWABLE(new LibInternalError("Invalid JsonRPC response [" + response + "]"), "Invalid JsonRPC-Response!", UncaughtException.ENo);
                        break;
                    }
                }
            }
            catch ( Throwable t )
            {
                //check no network
                if ( AntiPatternsJsonRPC.isIOError(t) )
                {
                    //launch action on no network
                    actionOnNoNetwork.run();

                    //ignore error 'no network' errors here!
                }
                else
                {
                    //launch action on technical error
                    actionOnTechnicalError.run();

                    //report this exception
                    AntiPatternsDebug.DEBUG_THROWABLE(t, "", UncaughtException.ENo);
                }
            }
        }

        /**********************************************************************************************
        *   Changes the loading item of the flow on the bottom of the scrolling list to a 'no network' icon.
        **********************************************************************************************/
        public static final void changeNextLoadingItemToNoNetwork()
        {
            flow.changeNextLoadingItemToNoNetwork( lastState.getActivity(), AntiPatternsActionUpdate.EUpdateUserProfileImagesNext );
        }

        /**********************************************************************************************
        *   Resets the offset and the profile-images-data.
        *   Should be invoked before a clean update of the profile-images is performed.
        **********************************************************************************************/
        public static final void reset()
        {
            flow = new AntiPatternsImageContainerFlow
            (
                lastState.getActivity(),
                lastProfileImagesContainer,
                lastParentScrollView,
                lastImageCount
            );
        }

        /**********************************************************************************************
        *   Orders to ditch all profile-images for the last user and reorder all profile-images.
        **********************************************************************************************/
        public static final void orderProfileImagesClean()
        {
            //hide images headline
            LibUI.setVisibilityUIThreaded( lastState.getActivity(), lastTitleImages,  View.GONE );

            //reset offset and images and assign count
            reset();

            //perform on UI-Thread
            lastState.getActivity().runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //show loading circle in image part UI-Threaded
                        flow.addAndShowLoadingCircle
                        (
                            lastState.getActivity(),
                            AntiPatternsActionUpdate.EUpdateUserProfileImagesNext,
                            true,
                            false
                        );

                        //load 1st user profile images
                        AntiPatternsActionUpdate.EUpdateUserProfileImagesNext.run();
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Unselects all buttons and links for all images.
        **********************************************************************************************/
/*
        public static final void unselectAllImages()
        {
            flow.unselectAllImages();
        }
*/
    }
