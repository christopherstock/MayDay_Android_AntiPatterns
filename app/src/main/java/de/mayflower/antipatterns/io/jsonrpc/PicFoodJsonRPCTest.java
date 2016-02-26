
    package de.mayflower.antipatterns.io.jsonrpc;

    import  org.json.*;
    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodSettings.Image;
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
    public class PicFoodJsonRPCTest
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
                    PicFoodJsonRPCAuth.login( "chris_stock", "008156", true );

                    //1.2
                    boolean testRestorePassword = false;    // enabling this will change the password!
                    if ( testRestorePassword ) PicFoodJsonRPCAuth.restorePassword( "chris_stock" );
                }

                //2. join
                {
                    //2.1
                    PicFoodJsonRPCRegister.register( "chris_stock", "chris@jenetic.de", "008156", "09316668998", "Christopher Stock", null, null );

                    //2.2
                    PicFoodJsonRPCRegister.checkUsername( "chris_stock" );
                }

                //3. user
                {
                    //requires sessionID
                    if ( PicFoodIDM.getSessionID( activity ) != null )
                    {
                        //3.1
                        PicFoodJsonRPCUser.logout( activity );
                        JSONObject response = PicFoodJsonRPCAuth.login( "chris_stock", "008156", true );

                        //assign session-id and user-id
                        PicFoodIDM.assignLogin
                        (
                            activity,
                            LibJSON.getJSONStringSecure( response, "sessionId" ),
                            LibJSON.getJSONStringSecure( response, "userId"    ),
                            "chris_stock",
                            LibIO.getMD5( "008156" )
                        );

                        //3.2
                        PicFoodJsonRPCUser.changeProfile( activity, "Christopher Stack", "0179 6872286", "www.christopherstock.de", "Now reeducated to a restaurant tester." );

                        //3.3
                        PicFoodJsonRPCUser.sendFeedback( activity, "Here be dragons." );

                        //3.4
                        PicFoodJsonRPCUser.getProfile( activity, PicFoodIDM.getUserID( activity ) );

                        //3.5
                        PicFoodJsonRPCUser.setFollowing( activity, "243", true );

                        //3.6
                        PicFoodJsonRPCUser.setBlockUser( activity, "243", true );

                        //3.7
                        PicFoodJsonRPCUser.getFollowers( activity, "243", 0 );

                        //3.8
                        PicFoodJsonRPCUser.getFollowing( activity, "243", 0 );

                        //3.9
                        boolean testUserRemove = false;
                        if ( testUserRemove ) PicFoodJsonRPCUser.removeUser( activity, "008156" );
                        PicFoodJsonRPCRegister.register( "chris_stock", "chris@jenetic.de", "008156", "09316668998", "Christopher Stock", null, null );
                        PicFoodJsonRPCAuth.login( "chris_stock", "008156", true );

                        //3.10
                        PicFoodJsonRPCUser.changePassword( activity, "008156", "008156" );

                        //3.11
                        PicFoodJsonRPCUser.setProfileImage( activity, LibResource.getResourceBitmap( activity, R.drawable.app_icon ) );
                    }
                }

                //4. image
                {
                    //4.1
                    PicFoodJsonRPCImage.postImage
                    (
                        activity,
                        null,
                        LibResource.getResourceBitmapDrawable( activity, R.drawable.net_picfood_settings_select ).getBitmap(),
                        "Best seafood ever! #seafood",
                        null
                    );

                    //4.2
                    PicFoodJsonRPCImage.removeImage( activity, "008156" );

                    //4.3
                    PicFoodJsonRPCImage.postComment( activity, "709", "Fine shot! Yummy!" );

                    //4.4
                    PicFoodJsonRPCImage.likeImage( activity, true, "709" );

                    //4.5
                    PicFoodJsonRPCImage.getImageLikes( activity, "709", 0 );

                    //4.6
                    PicFoodJsonRPCImage.rateFood( activity, "709", Image.FOOD_RATING_MEDIUM );

                    //4.7
                    PicFoodJsonRPCImage.getFoodRatings( activity, "709", 0 );

                    //4.8
                    PicFoodJsonRPCImage.getComments( activity, "709", 0 );

                    //4.9
                    PicFoodJsonRPCImage.getImage( activity, "709" );

                    //4.10
                    PicFoodJsonRPCImage.getUserImages( activity, "280", 0 );

                    //4.11
                    PicFoodJsonRPCImage.getUserWall( activity, 0 );

                    //4.12
                    PicFoodJsonRPCImage.sendImageFeedback( activity, "myCategory", "709", "Test image report." );

                    //4.13
                    PicFoodJsonRPCImage.getExplore( activity, 0 );
                }

                //5. search
                {
                    //5.1
                    PicFoodJsonRPCSearch.searchUsersByPhonenumber( activity, new String[] { "08215489412", "09316668998", "01796872286", "01739234824", }, 0 );

                    //5.2
                    PicFoodJsonRPCSearch.searchUsersByFacebookID( activity, new String[] { "008156", "81649852", "100000562026555", }, "100000562026555", 0 );

                    //5.3
                    PicFoodJsonRPCSearch.searchImagesByTagsAndOrLocationCoordinates( activity, "steak", "49.79398046633147", "9.922252777050744", 0 );

                    //5.4
                    PicFoodJsonRPCSearch.searchUsersByTerm( activity, "chris_stock", 0 );
                }

                //6. general
                {
                    //6.1
                    PicFoodJsonRPCGeneral.getTerms( activity );

                    //6.2
                    PicFoodJsonRPCGeneral.getPrivacyPolicy( activity );

                    //6.3
                    PicFoodJsonRPCGeneral.checkUpdate();
                }

                //7. app download
                {
                    //7.1
                    PicFoodJsonRPCGeneralUpdate.downloadLatestVersion();
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
                        PicFoodDebug.jsonRpc.trace( t );
                    }
                }
            }
            catch ( Throwable t )
            {
                PicFoodDebug.jsonRpc.out( t );
            }
        }
    }
