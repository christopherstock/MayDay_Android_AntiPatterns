
    package de.mayflower.antipatterns.io.jsonrpc;

    import  org.json.*;
    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsSettings.Image;
    import  de.mayflower.antipatterns.idm.*;
    import  android.app.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;

    /*****************************************************************************************
    *   Tests all Json-RPC-requests.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************************/
    public class AntiPatternsJsonRPCTest
    {
        /*****************************************************************************************
        *   Tests ALL JSON-RPC-Requests.
        *
        *   @param      activity    The according activity context.
        *   @deprecated             For testing purposes only!
        *****************************************************************************************/
        @Deprecated
        @SuppressWarnings( "deprecation" )
        public static final void test( Activity activity )
        {
            try
            {
                //1. auth
                {
                    //1.1
                    AntiPatternsJsonRPCAuth.login("chris_stock", "008156", true);

                    //1.2
                    boolean testRestorePassword = false;    // enabling this will change the password!
                    if ( testRestorePassword ) AntiPatternsJsonRPCAuth.restorePassword("chris_stock");
                }

                //2. join
                {
                    //2.1
                    AntiPatternsJsonRPCRegister.register("chris_stock", "chris@jenetic.de", "008156", "09316668998", "Christopher Stock", null, null);

                    //2.2
                    AntiPatternsJsonRPCRegister.checkUsername("chris_stock");
                }

                //3. user
                {
                    //requires sessionID
                    if ( AntiPatternsIDM.getSessionID(activity) != null )
                    {
                        //3.1
                        AntiPatternsJsonRPCUser.logout(activity);
                        JSONObject response = AntiPatternsJsonRPCAuth.login("chris_stock", "008156", true);

                        //assign session-id and user-id
                        AntiPatternsIDM.assignLogin
                                (
                                        activity,
                                        LibJSON.getJSONStringSecure(response, "sessionId"),
                                        LibJSON.getJSONStringSecure(response, "userId"),
                                        "chris_stock",
                                        LibIO.getMD5("008156")
                                );

                        //3.2
                        AntiPatternsJsonRPCUser.changeProfile(activity, "Christopher Stack", "0179 6872286", "www.christopherstock.de", "Now reeducated to a restaurant tester.");

                        //3.3
                        AntiPatternsJsonRPCUser.sendFeedback(activity, "Here be dragons.");

                        //3.4
                        AntiPatternsJsonRPCUser.getProfile(activity, AntiPatternsIDM.getUserID(activity));

                        //3.5
                        AntiPatternsJsonRPCUser.setFollowing(activity, "243", true);

                        //3.6
                        AntiPatternsJsonRPCUser.setBlockUser(activity, "243", true);

                        //3.7
                        AntiPatternsJsonRPCUser.getFollowers(activity, "243", 0);

                        //3.8
                        AntiPatternsJsonRPCUser.getFollowing(activity, "243", 0);

                        //3.9
                        boolean testUserRemove = false;
                        if ( testUserRemove ) AntiPatternsJsonRPCUser.removeUser(activity, "008156");
                        AntiPatternsJsonRPCRegister.register("chris_stock", "chris@jenetic.de", "008156", "09316668998", "Christopher Stock", null, null);
                        AntiPatternsJsonRPCAuth.login("chris_stock", "008156", true);

                        //3.10
                        AntiPatternsJsonRPCUser.changePassword(activity, "008156", "008156");

                        //3.11
                        AntiPatternsJsonRPCUser.setProfileImage(activity, LibResource.getResourceBitmap(activity, R.drawable.app_icon));
                    }
                }

                //4. image
                {
                    //4.1
                    AntiPatternsJsonRPCImage.postImage
                            (
                                    activity,
                                    null,
                                    LibResource.getResourceBitmapDrawable(activity, R.drawable.net_picfood_settings_select).getBitmap(),
                                    "Best seafood ever! #seafood",
                                    null
                            );

                    //4.2
                    AntiPatternsJsonRPCImage.removeImage(activity, "008156");

                    //4.3
                    AntiPatternsJsonRPCImage.postComment(activity, "709", "Fine shot! Yummy!");

                    //4.4
                    AntiPatternsJsonRPCImage.likeImage(activity, true, "709");

                    //4.5
                    AntiPatternsJsonRPCImage.getImageLikes(activity, "709", 0);

                    //4.6
                    AntiPatternsJsonRPCImage.rateFood(activity, "709", Image.FOOD_RATING_MEDIUM);

                    //4.7
                    AntiPatternsJsonRPCImage.getFoodRatings(activity, "709", 0);

                    //4.8
                    AntiPatternsJsonRPCImage.getComments(activity, "709", 0);

                    //4.9
                    AntiPatternsJsonRPCImage.getImage(activity, "709");

                    //4.10
                    AntiPatternsJsonRPCImage.getUserImages(activity, "280", 0);

                    //4.11
                    AntiPatternsJsonRPCImage.getUserWall(activity, 0);

                    //4.12
                    AntiPatternsJsonRPCImage.sendImageFeedback(activity, "myCategory", "709", "Test image report.");

                    //4.13
                    AntiPatternsJsonRPCImage.getExplore(activity, 0);
                }

                //5. search
                {
                    //5.1
                    AntiPatternsJsonRPCSearch.searchUsersByPhonenumber(activity, new String[]{"08215489412", "09316668998", "01796872286", "01739234824",}, 0);

                    //5.2
                    AntiPatternsJsonRPCSearch.searchUsersByFacebookID(activity, new String[]{"008156", "81649852", "100000562026555",}, "100000562026555", 0);

                    //5.3
                    AntiPatternsJsonRPCSearch.searchImagesByTagsAndOrLocationCoordinates(activity, "steak", "49.79398046633147", "9.922252777050744", 0);

                    //5.4
                    AntiPatternsJsonRPCSearch.searchUsersByTerm(activity, "chris_stock", 0);
                }

                //6. general
                {
                    //6.1
                    AntiPatternsJsonRPCGeneral.getTerms(activity);

                    //6.2
                    AntiPatternsJsonRPCGeneral.getPrivacyPolicy(activity);

                    //6.3
                    AntiPatternsJsonRPCGeneral.checkUpdate();
                }

                //7. app download
                {
                    //7.1
                    AntiPatternsJsonRPCGeneralUpdate.downloadLatestVersion();
                }

                //8. exception mails
                {
                    //8.1
                    try
                    {
                        throw new Exception( "This Exception has been raised INTENTIONALLY in order to test the JsonRPC-System!" );
                    }
                    catch ( Throwable t )
                    {
                        AntiPatternsDebug.jsonRpc.trace( t );
                    }
                }
            }
            catch ( Throwable t )
            {
                AntiPatternsDebug.jsonRpc.out( t );
            }
        }
    }
