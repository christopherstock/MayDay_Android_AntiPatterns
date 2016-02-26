
    package de.mayflower.antipatterns.data;

    import  org.json.*;
    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.*;
    import  de.mayflower.antipatterns.PicFoodSettings.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.idm.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.PicFoodUI.*;
    import  android.app.*;
    import  android.graphics.drawable.*;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   Represents all vital information for one PicFood image.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodDataImage implements PicFoodData
    {
        /**********************************************************************************************
        *   The different representation styles that exist for one image.
        **********************************************************************************************/
        public static enum ContentImageStyle
        {
            /** Displays the image with all available information. */
            EDetailed,

            /** Displays the image as a tile, without displaying any information. */
            ETiled,

            ;
        }

        /** The unique ID of this image. */
        public                  String                              iImageID                            = null;
        /** The timestamp when this image has been created. */
        public                  long                                iCreateDate                         = 0;
        /** The url where the Bitmap data of this image resides. */
        public                  String                              iURL                                = null;
        /** Specifies, if the current active user likes this image. */
        public                  boolean                             iILike                              = false;
        /** The image's owner. */
        public                  PicFoodDataUser                     iOwner                              = null;

        /** Specifies, if the current active user has already rated this food. */
        private                 boolean                             iIRatedFood                         = false;
        /** The RelativeLayout that contains the {@link #iImageView}. */
        private                 RelativeLayout                      iImageViewContainer                 = null;
        /** The ImageView that holds this image's bitmap. */
        private                 LibImageView                        iImageView                          = null;
        /** The ImageView that holds the icon of this image's owner. */
        private                 LibImageView                        iIconView                           = null;
        /** The Bitmap data for the image. */
        private                 BitmapDrawable                      iImageDrawable                      = null;
        /** The Bitmap data for the icon of this image's owner. */
        private                 BitmapDrawable                      iIconDrawable                       = null;
        /** All likes to show below the image in the detailed representation. */
        private                 PicFoodDataLike[]                   iInitialLikes                       = new PicFoodDataLike[] {};
        /** All comments to show below the image in the detailed representation. */
        private                 PicFoodDataComment[]                iInitialComments                    = new PicFoodDataComment[] {};
        /** All food ratings to show below the image in the detailed representation. */
        private                 PicFoodDataRating[]                 iInitialFoodRatings                 = new PicFoodDataRating[] {};
        /** The associated location where this image has been taken. */
        private                 PicFoodDataLocation                 iLocation                           = null;
        /** The 'like' button that is shown below the image in the detailed representation. */
        private                 Button                              iButtonLike                         = null;
        /** The 'comment' button that is shown below the image in the detailed representation. */
        private                 Button                              iButtonComment                      = null;
        /** The 'rate' button that is shown below the image in the detailed representation. */
        private                 Button                              iButtonRate                         = null;
        /** The 'options' button that is shown above the image in the detailed representation. */
        private                 ImageView                           iButtonOptions                      = null;
        /** The average food rating value for this image. */
        private                 float                               iAverageFoodRating                  = 0.0f;
        /** The number of likes that have been submitted for this image. */
        private                 int                                 iCountLikes                         = 0;
        /** The number of food ratings that have been submitted for this image. */
        private                 int                                 iCountFoodRatings                   = 0;
        /** The number of comments that have been submitted for this image. */
        private                 int                                 iCountComments                      = 0;
        /** The representation style of this image. */
        private                 ContentImageStyle                   iStyle                              = null;
        /** Determines, if the owner's username of this image shall be linked to it's profile page. */
        private                 boolean                             iLinkUsername                       = false;

        /**********************************************************************************************
        *   Creates an image data object from the specified json object.
        *
        *   @param  json            The json object to parse the image data from.
        *   @param  aStyle          The representation style to use for this data.
        *   @param  aLinkUsername   Determines, if the username of this image shall be linked to the according user profile.
        **********************************************************************************************/
        public PicFoodDataImage( JSONObject json, ContentImageStyle aStyle, boolean aLinkUsername )
        {
            iImageID                =                               LibJSON.getJSONStringSecure( json, "imageId"            );
            iURL                    =                               LibJSON.getJSONStringSecure( json, "imageURL"           );
            iILike                  = Boolean.parseBoolean(         LibJSON.getJSONStringSecure( json, "iLike"              ) );
            iIRatedFood             = Boolean.parseBoolean(         LibJSON.getJSONStringSecure( json, "iRatedFood"         ) );
            iOwner                  = new PicFoodDataUser(          LibJSON.getJSONObjectSecure( json, "owner"              ) );
            iCreateDate             = Lib.MILLIS_PER_SECOND * Long.parseLong( LibJSON.getJSONStringSecure( json, "createDate" ) );

            iLocation               = PicFoodDataLocation.parse(    LibJSON.getJSONObjectSecure( json, "location"           ) );

            iCountLikes             = LibJSON.getJSONIntegerSecure( LibJSON.getJSONObjectSecure( json, "imageLikes"         ), "count"      );
            iInitialLikes           = PicFoodDataLike.parse(        json );

            iCountFoodRatings       = LibJSON.getJSONIntegerSecure( LibJSON.getJSONObjectSecure( json, "foodRatings"        ), "count"      );
            iInitialFoodRatings     = PicFoodDataRating.parse(      json );

            iCountComments          = LibJSON.getJSONIntegerSecure( LibJSON.getJSONObjectSecure( json, "comments"           ), "count"      );
            iInitialComments        = PicFoodDataComment.parse(     json );

            iAverageFoodRating      =                               LibJSON.getJSONFloatSecure( json, "averageFoodRating"  );

            iStyle                  = aStyle;
            iLinkUsername           = aLinkUsername;

            PicFoodDebug.dataImage.out( "Created PicFoodImage:" );
            PicFoodDebug.dataImage.out( "imageId            [" + iImageID                                                                                   + "]"                       );
            PicFoodDebug.dataImage.out( "imageURL           [" + iURL                                                                                       + "]"                       );
            PicFoodDebug.dataImage.out( "iLike              [" + iILike                                                                                     + "]"                       );
            PicFoodDebug.dataImage.out( "createDate         [" + iCreateDate + "][" + LibStringFormat.getSingleton().formatDateTimeMedium( iCreateDate )    + "]"                       );
            PicFoodDebug.dataImage.out( "owner              [" + iOwner.iUserName                                                                           + "]"                       );
            PicFoodDebug.dataImage.out( "location           [" + ( iLocation != null ? iLocation.iLatitude + "," + iLocation.iLongitude : "" )              + "]"                       );
            PicFoodDebug.dataImage.out( "comments           [" + iInitialComments.length                                                                    + "] comments"              );
            PicFoodDebug.dataImage.out( "imageLikes         [" + iInitialLikes.length                                                                       + "] likes"                 );
            PicFoodDebug.dataImage.out( "foodRatings        [" + iInitialFoodRatings.length                                                                 + "] food-ratings"          );
            PicFoodDebug.dataImage.out( "averageFoodRating  [" + iAverageFoodRating                                                                         + "] average food rating"   );
            PicFoodDebug.dataImage.out( ""                                                                                                                                              );
        }

        /**********************************************************************************************
        *   Creates an image data object that is a copy of the specified image data object.
        *
        *   @param  template        The original image data object where this copy shall be created from.
        **********************************************************************************************/
        public PicFoodDataImage( PicFoodDataImage template )
        {
            //DANGER on adding fields to this class - they must be added here .. :/

            iImageID                = template.iImageID;
            iURL                    = template.iURL;
            iILike                  = template.iILike;
            iIRatedFood             = template.iIRatedFood;
            iOwner                  = template.iOwner;
            iCreateDate             = template.iCreateDate;

            iLocation               = template.iLocation;

            iCountLikes             = template.iCountLikes;
            iInitialLikes           = template.iInitialLikes;

            iCountFoodRatings       = template.iCountFoodRatings;
            iInitialFoodRatings     = template.iInitialFoodRatings;

            iCountComments          = template.iCountComments;
            iInitialComments        = template.iInitialComments;

            iAverageFoodRating      = template.iAverageFoodRating;

            iStyle                  = template.iStyle;
            iLinkUsername           = template.iLinkUsername;
        }

        /**********************************************************************************************
        *   Parses all data from the given json object.
        *
        *   @param  response        The json object to parse the image data from.
        *   @param  style           The representation style to use for this data.
        *   @param  linkUsername    Determines, if the username of this image shall be linked to the according user profile.
        *   @return                 All parsed images.
        *   @throws Throwable       If a parsing error occured.
        **********************************************************************************************/
        public static final PicFoodDataImage[] parse( JSONObject response, ContentImageStyle style, boolean linkUsername ) throws Throwable
        {
            JSONArray               images      = LibJSON.getJSONArraySecure( response, "images" );
            PicFoodDataImage[]      ret         = new PicFoodDataImage[ images.length() ];
            for ( int i = 0; i < images.length(); ++i )
            {
                //parse image from json
                JSONObject image = images.getJSONObject( i );
                ret[ i ] = new PicFoodDataImage( image, style, linkUsername );
            }

            return ret;
        }

        @Override
        public final View createItemView( final LibState state )
        {
            switch ( iStyle )
            {
                case EDetailed:
                {
                    return createDetailedView( state );
                }

                case ETiled:
                {
                    return createTiledView( state );
                }
            }

            return null;
        }

        /**********************************************************************************************
        *   Creates the detailed view of this image.
        *   This is the default image representation that displays all details for the image.
        *
        *   @param  state       The according state.
        *   @return             The detailed representation of this image.
        **********************************************************************************************/
        private final View createDetailedView( final LibState state )
        {
            //inflate layout
            ViewGroup ret = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_image_detailed );

            //disable selector
            ret.setBackgroundResource( R.drawable.net_picfood_selector_transparent );

            //setup clickable text 'username'
            setupUsername( ret, state.getActivity() );

            //setup text 'date'
            setupDate( ret, state.getActivity() );

            //setup user-icon with loading image
            setupUserIcon( ret, state.getActivity() );

            //setup image with loading image
            setupImage( ret, state, true );

            //setup the container that surrounds the image
            setupImageContainerDetailed( ret, state.getActivity() );

            //setup button 'like'
            setupButtonLike( ret, state );

            //setup button 'comment'
            setupButtonComment( ret, state );

            //setup button 'rate'
            setupButtonRate( ret, state );

            //setup button 'options'
            setupButtonOptions( ret, state );

            //setup text 'you like'
            setupTextYouLike( ret, state.getActivity() );

            //setup text 'like count'
            setupTextLikeCount( ret, state );

            //setup area 'likes preview'
            setupAreaLikesPreview( ret, state );

            //setup text 'comment count'
            setupTextCommentCount( ret, state );

            //setup area 'comments preview'
            setupAreaCommentsPreview( ret, state );

            //setup text 'average food rating'
            setupTextAverageFoodRating( ret, state.getActivity() );

            //setup text 'rating count' ( will be hidden in v. 1.0 )
            setupTextRatingCount( ret, state );

            //setup text 'location'
            setupTextLocation( ret, state.getActivity() );

            //rating preview area will be added in v. 2.0!

            return ret;
        }

        /**********************************************************************************************
        *   Creates the tiled view of this image. This is just a square representation of this image,
        *   without any detailed information. This view is prefered in tiled lists.
        *
        *   @param  state       The according state.
        *   @return             The tiled representation of this image.
        **********************************************************************************************/
        private final View createTiledView( final LibState state )
        {
            //inflate layout
            ViewGroup ret = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_image_tiled );

            //explicitly specify container size
            iImageViewContainer = (RelativeLayout)ret.findViewById( R.id.image_container );
            iImageViewContainer.getLayoutParams().width  = PicFoodImage.getImageSizeToOrder( state.getActivity(), ImageSize.ETiledImage );
            iImageViewContainer.getLayoutParams().height = PicFoodImage.getImageSizeToOrder( state.getActivity(), ImageSize.ETiledImage );

            //disable selector
            ret.setBackgroundResource( R.drawable.net_picfood_selector_transparent );

            //setup clickable image with loading image
            {
                //setup image
                setupImage( ret, state, false );

                //make image clickable
                LibUI.setOnClickAction
                (
                    iImageView,
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //assign this as the last pushed image
                            PicFoodFlowImage.lastState  = state;
                            PicFoodFlowImage.lastImage  = PicFoodDataImage.this;

                            //show the selected image in state 'detailed image'
                            PicFoodActionState.EEnterDetailedImage.run();
                        }
                    }
                );
            }

            return ret;
        }

        /**********************************************************************************************
        *   Requests to unselect all buttons in the composed view.
        *   This method is being performed on the UI-Thread.
        *
        *   @param  activity    The according activity context.
        **********************************************************************************************/
        public final void unselectAllButtonsUIThreaded( Activity activity )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        unselectAllButtons();
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Requests to unselect all buttons in the composed view.
        **********************************************************************************************/
        public final void unselectAllButtons()
        {
            //unselect buttons
            if ( iButtonLike    != null ) iButtonLike.setSelected(      false );
            if ( iButtonComment != null ) iButtonComment.setSelected(   false );
            if ( iButtonOptions != null ) iButtonOptions.setSelected(   false );
            if ( iButtonRate    != null ) iButtonRate.setSelected(      false );
/*
            //unselect links
            if ( commentCount    != null ) commentCount.setSelected(    false );
            if ( commentsViewAll != null ) commentsViewAll.setSelected( false );
            if ( ratingCount     != null ) ratingCount.setSelected(     false );
            if ( likeCount       != null ) likeCount.setSelected(       false );
            if ( username        != null ) username.setSelected(        false );

            if ( iCommentsLinks  != null )
            {
                for ( TextView tv : iCommentsLinks )
                {
                    tv.setSelected( false );
                }
            }

            if ( iLikesLinks  != null )
            {
                for ( TextView tv : iLikesLinks )
                {
                    tv.setSelected( false );
                }
            }
*/
        }

        /**********************************************************************************************
        *   Creates the 'username' in the detailed view.
        *
        *   @param  activity    The according activity context.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupUsername( View ret, Activity activity )
        {
            TextView username = (TextView)ret.findViewById( R.id.username );
            linkOrUnlinkUsername( activity, username, iOwner );
        }

        /**********************************************************************************************
        *   Creates the 'date' in the detailed view.
        *
        *   @param  activity    The according activity context.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupDate( View ret, Activity activity )
        {
            TextView date     = (TextView)ret.findViewById( R.id.date );
            String   dateTime = PicFoodUI.formatTimeDistance( activity, iCreateDate );
            LibUI.setupTextView( date, PicFoodSystems.getFonts().TYPEFACE_REGULAR, dateTime );
        }

        /**********************************************************************************************
        *   Creates the 'userIcon' in the detailed view.
        *
        *   @param  activity    The according activity context.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupUserIcon( View ret, Activity activity )
        {
            //reference image view
            iIconView = (LibImageView)ret.findViewById( R.id.icon );

            //check if loaded image is present
            if ( iIconDrawable == null )
            {
                iIconView.setImageResource( R.drawable.net_picfood_loading_icon_user );
            }
            else
            {
                //transit from loading-icon to new image
                PicFoodUI.setNewImage( activity, iIconDrawable, iIconView, R.drawable.net_picfood_loading_icon_user );

                //iIconView.setImageDrawable( iIconDrawable );
            }
        }

        /**********************************************************************************************
        *   Sets up the 'image' in the detailed view with the loading image.
        *
        *   @param  state           The according state.
        *   @param  ret             The parent detailed image view layout.
        *   @param  enableLongpress If longpressing this image should invoke the options menu.
        **********************************************************************************************/
        private final void setupImage( View ret, final LibState state, boolean enableLongpress )
        {
            //reference image view and image view container
            iImageView          = (LibImageView)ret.findViewById(      R.id.image );

            //check if loaded image is present
            if ( iImageDrawable == null )
            {
                iImageView.setImageResource( R.drawable.net_picfood_loading_image );
            }
            else
            {
                //transit from loading-icon to new image
                PicFoodUI.setNewImage( state.getActivity(), iImageDrawable, iImageView, R.drawable.net_picfood_loading_image );

                //iImageView.setImageDrawable( iImageDrawable );
            }

            //enable optionsmenu on longpress if desired
            if ( enableLongpress )
            {
                LibUI.setOnLongPressAction
                (
                    iImageView,
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //assign this as the last pushed image
                            PicFoodFlowImage.lastState  = state;
                            PicFoodFlowImage.lastImage  = PicFoodDataImage.this;

                            //open image's option menu
                            PicFoodActionPush.EPushImageOptions.run();
                        }
                    }
                );
            }
        }

        /**********************************************************************************************
        *   Sets up the image container that surrounds the 'image' for detailed images.
        *
        *   @param  activity    The according activity context.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupImageContainerDetailed( View ret, Activity activity )
        {
            //reference the ImageContainer
            iImageViewContainer = (RelativeLayout)ret.findViewById( R.id.image_container );

            //dimension image container
            iImageViewContainer.getLayoutParams().width  = PicFoodImage.getDisplayedDetailedImageSize( activity );
            iImageViewContainer.getLayoutParams().height = PicFoodImage.getDisplayedDetailedImageSize( activity );
        }

        /**********************************************************************************************
        *   Creates the 'like button' in the detailed view.
        *
        *   @param  state       The according state.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupButtonLike( View ret, final LibState state )
        {
            iButtonLike = (Button)ret.findViewById( R.id.button_like );
            LibUI.setupButton
            (
                state.getActivity(),
                iButtonLike,
                ( iILike ? R.string.image_button_unlike : R.string.image_button_like ),
                PicFoodSystems.getFonts().TYPEFACE_REGULAR,
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //assign this as the last pushed image
                        PicFoodFlowImage.lastState      = state;
                        PicFoodFlowImage.lastImage      = PicFoodDataImage.this;

                        //like or unlike the image
                        PicFoodActionPush.EPushImageLikeUnlike.run();
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Creates the 'comment button' in the detailed view.
        *
        *   @param  state       The according state.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupButtonComment( View ret, final LibState state )
        {
            iButtonComment = (Button)ret.findViewById( R.id.button_comment );
            LibUI.setupButton
            (
                state.getActivity(),
                iButtonComment,
                R.string.image_button_comment,
                PicFoodSystems.getFonts().TYPEFACE_REGULAR,
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //assign this as the last pushed image
                        PicFoodFlowImage.lastState      = state;
                        PicFoodFlowImage.lastImage      = PicFoodDataImage.this;

                        //like or unlike the image
                        PicFoodActionPush.EPushImageComment.run();
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Creates the 'rate button' in the detailed view.
        *
        *   @param  state       The according state.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupButtonRate( View ret, final LibState state )
        {
            iButtonRate = (Button)ret.findViewById( R.id.button_rate );
            LibUI.setupButton
            (
                state.getActivity(),
                iButtonRate,
                R.string.image_button_rate,
                PicFoodSystems.getFonts().TYPEFACE_REGULAR,
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //assign this as the last pushed image
                        PicFoodFlowImage.lastState      = state;
                        PicFoodFlowImage.lastImage      = PicFoodDataImage.this;

                        //show 'rate food' button
                        PicFoodActionPush.EPushImageRateFood.run();
                    }
                }
            );

            //show button 'rate' only if the user has not rated this image AND he's the owner
            boolean showRateButton = ( !iIRatedFood && PicFoodIDM.getUserID( state.getActivity() ).equals( iOwner.iUserID ) );
            iButtonRate.setVisibility( showRateButton ? View.VISIBLE : View.INVISIBLE );
        }

        /**********************************************************************************************
        *   Creates the 'options button' in the detailed view.
        *
        *   @param  state       The according state.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupButtonOptions( View ret, final LibState state )
        {
            iButtonOptions = (ImageView)ret.findViewById( R.id.button_options );

            LibUI.setOnClickAction
            (
                iButtonOptions,
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //assign this as the last pushed image
                        PicFoodFlowImage.lastState      = state;
                        PicFoodFlowImage.lastImage      = PicFoodDataImage.this;

                        //open image's option menu
                        PicFoodActionPush.EPushImageOptions.run();
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Creates the 'you like text' in the detailed view.
        *
        *   @param  ret         The parent detailed image view layout.
        *   @param  activity    The according activity context.
        **********************************************************************************************/
        private final void setupTextYouLike( View ret, Activity activity )
        {
            TextView youLike    = (TextView)ret.findViewById( R.id.you_like );
            LibUI.setupTextView( activity, youLike, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.image_you_like );
            youLike.setVisibility( iILike ? View.VISIBLE : View.GONE );
        }

        /**********************************************************************************************
        *   Creates the 'average food rating text' in the detailed view.
        *
        *   @param  ret         The parent detailed image view layout.
        *   @param  activity    The according activity context.
        **********************************************************************************************/
        private final void setupTextAverageFoodRating( View ret, Activity activity )
        {
            TextView    averageFoodRatingText = (TextView)ret.findViewById(  R.id.average_food_rating_text );
            ImageView   averageFoodRatingIcon = (ImageView)ret.findViewById( R.id.average_food_rating_icon );

            if ( iCountFoodRatings == 0 )
            {
                //show 'no food ratings yet' and hide the icon
                String      text                = LibResource.getResourceString( activity, R.string.image_average_food_rating_none );
                LibUI.setupTextView( averageFoodRatingText, PicFoodSystems.getFonts().TYPEFACE_BOLD, text );
                averageFoodRatingIcon.setVisibility( View.GONE );
            }
            else
            {
                //show average food rating
                String      text                = LibResource.getResourceString( activity, R.string.image_average_food_rating );
                //text = text.replace( "{value}", String.valueOf( iAverageFoodRating ) );
                LibUI.setupTextView( averageFoodRatingText, PicFoodSystems.getFonts().TYPEFACE_BOLD, text );

                //red smiley
                if ( iAverageFoodRating < Image.BOUND_SMILEY_YELLOW )
                {
                    averageFoodRatingIcon.setImageResource( R.drawable.net_picfood_rating_small_red );
                }
                else if ( iAverageFoodRating < Image.BOUND_SMILEY_GREEN )
                {
                    averageFoodRatingIcon.setImageResource( R.drawable.net_picfood_rating_small_yellow );
                }
                else
                {
                    averageFoodRatingIcon.setImageResource( R.drawable.net_picfood_rating_small_green );
                }
            }
        }

        /**********************************************************************************************
        *   Creates the 'like count text' in the detailed view.
        *
        *   @param  ret         The parent detailed image view layout.
        *   @param  state       The according state.
        **********************************************************************************************/
        private final void setupTextLikeCount( View ret, final LibState state )
        {
            TextView likeCount = (TextView)ret.findViewById( R.id.count_like );

            if ( iCountLikes == 0 )
            {
                String  likeCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_likes_none );

                LibUI.setupTextView( likeCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, likeCaption );
            }
            else if ( iCountLikes == 1 )
            {
                String  likeCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_likes_singular );
                        likeCaption = LibString.replace( likeCaption, "{count}", String.valueOf( iCountLikes ) );

                LibUI.setupTextView( likeCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, likeCaption );
            }
            else
            {
                String  likeCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_likes_plural );
                        likeCaption = LibString.replace( likeCaption, "{count}", String.valueOf( iCountLikes ) );

                LibUI.setupTextView( likeCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, likeCaption );
            }

            //add link to like count if desired
            if ( iCountLikes > JsonRPC.LIMIT_INITIAL_LIKES )
            {
                linkLikesCount( state, likeCount );
            }
        }

        /**********************************************************************************************
        *   Creates the 'comment count text' in the detailed view.
        *
        *   @param  state       The according state.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupTextCommentCount( View ret, final LibState state )
        {
            TextView commentCount = (TextView)ret.findViewById( R.id.count_comment );

            if ( iCountComments == 0 )
            {
                String  commentCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_comments_none );

                LibUI.setupTextView( commentCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, commentCaption );
            }
            else if ( iCountComments == 1 )
            {
                String  commentCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_comments_singular );
                        commentCaption = LibString.replace( commentCaption, "{count}", String.valueOf( iCountComments ) );

                LibUI.setupTextView( commentCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, commentCaption );
            }
            else
            {
                String  commentCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_comments_plural );
                        commentCaption = LibString.replace( commentCaption, "{count}", String.valueOf( iCountComments ) );

                LibUI.setupTextView( commentCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, commentCaption );
            }

            //add link to comment count if desired
            if ( iCountComments > JsonRPC.LIMIT_INITIAL_COMMENTS )
            {
                linkCommentCount( state, commentCount );
            }
        }

        /**********************************************************************************************
        *   Creates the 'rating count text' in the detailed view.
        *
        *   @param  state       The according state.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupTextRatingCount( View ret, final LibState state )
        {
            TextView ratingCount = (TextView)ret.findViewById( R.id.count_rating );

            PicFoodDebug.ratingsPreviews.out( "CountFoodRatings [" + iCountFoodRatings + "]" );

            if ( iCountFoodRatings == 0 )
            {
                String  ratingCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_food_ratings_none );

                LibUI.setupTextView( ratingCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, ratingCaption );
            }
            else if ( iCountFoodRatings == 1 )
            {
                String  ratingCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_food_ratings_singular );
                        ratingCaption = LibString.replace( ratingCaption, "{count}", String.valueOf( iCountFoodRatings ) );

                LibUI.setupTextView( ratingCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, ratingCaption );
            }
            else
            {
                String  ratingCaption = LibResource.getResourceString( state.getActivity(), R.string.image_count_food_ratings_plural );
                        ratingCaption = LibString.replace( ratingCaption, "{count}", String.valueOf( iCountFoodRatings ) );

                LibUI.setupTextView( ratingCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, ratingCaption );
            }

            //develop it now!
            if ( Features.SHOW_DETAILED_IMAGE_RATINGS )
            {
                //show it!
                ratingCount.setVisibility( View.VISIBLE );

                //link to "image properties ratings"
                if ( iCountFoodRatings > JsonRPC.LIMIT_INITIAL_FOOD_RATINGS )
                {
                    //make it bold and highlighted
                    LibUI.setupTextViewColorStateList( state.getActivity(), ratingCount, R.color.net_picfood_link_text );

                    //assign action
                    LibUI.setOnClickAction
                    (
                        ratingCount,
                        new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //assign this as the last pushed image
                                PicFoodFlowImage.lastState      = state;
                                PicFoodFlowImage.lastImage      = PicFoodDataImage.this;

                                //show all image likes
                                PicFoodActionPush.EPushImageShowFoodRatings.run();
                            }
                        }
                    );
                }
            }
        }

        /**********************************************************************************************
        *   Creates the 'comments preview area' in the detailed view.
        *
        *   @param  state       The according state.
        *   @param  ret         The parent detailed image view layout.
        **********************************************************************************************/
        private final void setupAreaCommentsPreview( View ret, final LibState state )
        {
            //find preview area
            TextView    commentsPreviewLabel        = (TextView)ret.findViewById(  R.id.comments_preview_label  );
            ViewGroup   commentsPreviewArea         = (ViewGroup)ret.findViewById( R.id.comments_preview_area   );
            View        commentsPreviewSeparator    =            ret.findViewById( R.id.separator_line_comments );
            TextView    commentsViewAll             = (TextView)ret.findViewById(  R.id.comments_view_all       );

            PicFoodDebug.commentsPreviews.out( "commentsCount is [" + iCountComments + "]" );

            //check if comments are available
            if ( iCountComments == 0 )
            {
                //hide the label and the area
                commentsPreviewLabel.setVisibility(     View.GONE );
                commentsPreviewArea.setVisibility(      View.GONE );
                commentsPreviewSeparator.setVisibility( View.GONE );
                commentsViewAll.setVisibility(          View.GONE );
            }
            else
            {
                //setup label
                LibUI.setupTextView( state.getActivity(), commentsPreviewLabel, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.image_comments );

                //browse initial comments
                for ( PicFoodDataComment comment : iInitialComments )
                {
                    PicFoodDebug.commentsPreviews.out( " comment [" + comment.iPostedAt + "][" + comment.iOwner.iUserName + "][" + comment.iText + "]" );

                    //pick comment-preview layout
                    ViewGroup commentLayoutHead = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_preview_comment_head );
                    ViewGroup commentLayoutBody = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_preview_comment_body );

                    //username
                    TextView usernameText = (TextView)commentLayoutHead.findViewById( R.id.username );
                    linkOrUnlinkUsername( state.getActivity(), usernameText, comment.iOwner );

                    //timestamp
                    TextView timestampText = (TextView)commentLayoutHead.findViewById( R.id.timestamp );
                    LibUI.setupTextView( timestampText, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodUI.formatTimeDistance( state.getActivity(), comment.iPostedAt ) );

                    //comment
                    TextView commentText = (TextView)commentLayoutBody.findViewById( R.id.comment );
                    LibUI.setupTextView( commentText, PicFoodSystems.getFonts().TYPEFACE_REGULAR, comment.iText );

                    //add layout to preview area
                    commentsPreviewArea.addView( commentLayoutHead );
                    commentsPreviewArea.addView( commentLayoutBody );
                }

                //add link to all comments if desired
                if ( iCountComments > JsonRPC.LIMIT_INITIAL_COMMENTS )
                {
                    LibUI.setupTextView( state.getActivity(), commentsViewAll, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.image_comments_view_all );
                    linkCommentCount( state, commentsViewAll );
                }
                else
                {
                    commentsViewAll.setVisibility( View.GONE );
                }
            }
        }

        /**********************************************************************************************
        *   Creates the 'likes preview area' in the detailed view.
        *
        *   @param  ret         The parent detailed image view layout.
        *   @param  state       The according state.
        **********************************************************************************************/
        private final void setupAreaLikesPreview( View ret, final LibState state )
        {
            //find preview area
            TextView            likesPreviewLabel       = (TextView)ret.findViewById(  R.id.likes_preview_label     );
            ViewGroup           likesPreviewArea        = (ViewGroup)ret.findViewById( R.id.likes_preview_area      );
            View                likesPreviewSeparator   =            ret.findViewById( R.id.separator_line_likes    );
            TextView            likesViewAll            = (TextView)ret.findViewById(  R.id.likes_view_all          );

            PicFoodDebug.likesPreviews.out( "likesCount is [" + iCountComments + "]" );

            //check if likes are available
            if ( iCountLikes == 0 )
            {
                //hide the label, the area and the separator line
                likesPreviewLabel.setVisibility(        View.GONE );
                likesPreviewArea.setVisibility(         View.GONE );
                likesPreviewSeparator.setVisibility(    View.GONE );
                likesViewAll.setVisibility(             View.GONE );
            }
            else
            {
                //setup label
                LibUI.setupTextView( state.getActivity(), likesPreviewLabel, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.image_likes );

                //browse initial likes
                for ( PicFoodDataLike like : iInitialLikes )
                {
                    PicFoodDebug.likesPreviews.out( " like [" + like.iPostedAt + "][" + like.iOwner.iUserName + "]" );

                    //pick text layout
                    TextView likeText = (TextView)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.text_image_default );
                    linkOrUnlinkUsername( state.getActivity(), likeText, like.iOwner );
                    likeText.setPadding( LibResource.getResourceDimensionInPixel( state.getActivity(), R.dimen.content_distance_normal ), 0, 0, 0 );

                    //add layout to preview area
                    likesPreviewArea.addView( likeText );
                }
            }

            //add link to all likes if desired
            if ( iCountLikes > JsonRPC.LIMIT_INITIAL_LIKES )
            {
                LibUI.setupTextView( state.getActivity(), likesViewAll, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.image_likes_view_all );
                linkLikesCount( state, likesViewAll );
            }
            else
            {
                likesViewAll.setVisibility( View.GONE );
            }
        }

        /**********************************************************************************************
        *   Creates the 'location text' in the detailed view.
        *
        *   @param  ret         The parent detailed image view layout.
        *   @param  activity    The according activity context.
        **********************************************************************************************/
        private final void setupTextLocation( View ret, Activity activity )
        {
            TextView locationCaption   = (TextView)ret.findViewById( R.id.location_caption   );
            TextView locationVicinity  = (TextView)ret.findViewById( R.id.location_vicinity );
            TextView locationDistance  = (TextView)ret.findViewById( R.id.location_distance  );

            //location name
            if ( iLocation == null || LibString.isEmpty( iLocation.iName ) )
            {
                locationCaption.setVisibility( View.GONE );
            }
            else
            {
                LibUI.setupTextView( locationCaption, PicFoodSystems.getFonts().TYPEFACE_REGULAR, String.valueOf( iLocation.iName ) );
            }

            //location vicinity
            if ( iLocation == null || LibString.isEmpty( iLocation.iVicinity ) )
            {
                locationVicinity.setVisibility( View.GONE );
            }
            else
            {
                LibUI.setupTextView( locationVicinity, PicFoodSystems.getFonts().TYPEFACE_REGULAR, String.valueOf( iLocation.iVicinity ) );
            }

            //location distance
            if ( iLocation == null || iLocation.iDistanceKM == 0 )
            {
                locationDistance.setVisibility( View.GONE );
            }
            else
            {
                int    distanceInMeters = (int)( iLocation.iDistanceKM * 1000 );
                String distanceText     = LibResource.getResourceString( activity, R.string.image_location_distance );
                       distanceText     = distanceText.replace( "{distance}", String.valueOf( distanceInMeters ) );
                LibUI.setupTextView( locationDistance, PicFoodSystems.getFonts().TYPEFACE_REGULAR, distanceText );
            }
        }

        @Override
        public void orderImageThreaded( LibState state )
        {
            PicFoodDebug.orderDetailedImageBitmaps.out( "Order detailed image for image from [" + iOwner.iUserName + "]" );

            //only load the image if not loaded yet
            if ( iImageDrawable == null )
            {
                PicFoodImage.orderImageThreaded
                (
                    state,
                    iURL,
                    ( iStyle == ContentImageStyle.EDetailed ? ImageSize.EDetailedImage : ImageSize.ETiledImage ),
                    this
                );
            }

            //only load the icon if not loaded yet AND only for detailed representation
            if ( iStyle == ContentImageStyle.EDetailed && iIconDrawable == null )
            {
                PicFoodImage.orderImageThreaded
                (
                    state,
                    iOwner.iProfileImageUrl,
                    ImageSize.EIcon,
                    this
                );
            }
        }

        @Override
        public void assignLoadedImage( LibState state, ImageSize imageSize, final BitmapDrawable bd )
        {
            switch ( imageSize )
            {
                case EDetailedImage:
                case ETiledImage:
                {
                    //assign new image
                    iImageDrawable = bd;

                    //assign directly if required
                    PicFoodUI.setNewImageUIThreaded( state.getActivity(), bd, iImageView, R.drawable.net_picfood_loading_image );

                    break;
                }

                case EIcon:
                {
                    //assign new icon
                    iIconDrawable = bd;

                    //assign directly if required
                    PicFoodUI.setNewImageUIThreaded( state.getActivity(), bd, iIconView, R.drawable.net_picfood_loading_icon_user );

                    break;
                }
            }
        }

        /**********************************************************************************************
        *   Sets up the given TextView according to the information, if the username shall be linked.
        *
        *   @param  activity    The according activity context.
        *   @param  textView    The TextView that represents the username.
        *   @param  user        The user that is represented in the username view.
        **********************************************************************************************/
        private void linkOrUnlinkUsername( final Activity activity, final TextView textView, final PicFoodDataUser user )
        {
            //link username if image's username shall be linked or if this username if different from the owner
            boolean linkUsername = ( iLinkUsername || !user.iUserID.equals( iOwner.iUserID ) );

            if ( linkUsername )
            {
                //bold font and OnClickAction for username
                LibUI.setupTextView( textView, PicFoodSystems.getFonts().TYPEFACE_BOLD, user.iUserName );
                LibUI.setupTextViewColorStateList( activity, textView, R.color.net_picfood_link_text );
                LibUI.setOnClickAction
                (
                    textView,
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //unselect all buttons for this image
                            PicFoodDataImage.this.unselectAllButtonsUIThreaded( activity );

                            //show the selected user profile
                            PicFoodFlowProfile.showUserProfile( activity, user );
                        }
                    }
                );
            }
            else
            {
                //regular font and default color for username
                LibUI.setupTextView( textView, PicFoodSystems.getFonts().TYPEFACE_BOLD, user.iUserName );
                textView.setTextColor( LibResource.getResourceColor( activity, R.color.text_image_default ) );
            }
        }

        /**********************************************************************************************
        *   Creates a link functionality to the comment count.
        *   This function shall only be invoked if the like count exceeds a specified number.
        *
        *   @param  state       The according state.
        *   @param  text        The TextView that represents the comment count.
        **********************************************************************************************/
        private void linkCommentCount( final LibState state, TextView text )
        {
            //make it bold and highlighted
            LibUI.setupTextViewColorStateList( state.getActivity(), text, R.color.net_picfood_link_text );

            //assign action
            LibUI.setOnClickAction
            (
                text,
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //assign this as the last pushed image
                        PicFoodFlowImage.lastState      = state;
                        PicFoodFlowImage.lastImage      = PicFoodDataImage.this;

                        //show all image likes
                        PicFoodActionPush.EPushImageShowComments.run();
                    }
                }
            );
        }

        /**********************************************************************************************
        *   Creates a link functionality to the like count.
        *   This function shall only be invoked if the like count exceeds a specified number.
        *
        *   @param  state       The according state.
        *   @param  text        The TextView that represents the link count.
        **********************************************************************************************/
        private void linkLikesCount( final LibState state, TextView text )
        {
            //make it bold and highlighted
            LibUI.setupTextViewColorStateList( state.getActivity(), text, R.color.net_picfood_link_text );

            //assign action
            LibUI.setOnClickAction
            (
                text,
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //assign this as the last pushed image
                        PicFoodFlowImage.lastState      = state;
                        PicFoodFlowImage.lastImage      = PicFoodDataImage.this;

                        //show all image likes
                        PicFoodActionPush.EPushImageShowLikes.run();
                    }
                }
            );
        }

        @Override
        public void recycleBitmaps()
        {
            //recycle the image for this wall-image
            if ( iImageDrawable != null && iImageDrawable.getBitmap() != null && !iImageDrawable.getBitmap().isRecycled() )
            {
                //change the image to the loading image
                iImageView.setImageDrawable( null );
                iImageView.setImageBitmap(   null );
                iImageView.setImageResource( R.drawable.net_picfood_loading_image );

                //recycle and ditch old image
                iImageDrawable.getBitmap().recycle();
                iImageDrawable = null;

                //PicFoodDebug.bitmapRecycling.out( " recycled image successfully" );
            }

            //recycle the icon for this wall-image
            if ( iIconDrawable != null && iIconDrawable.getBitmap() != null && !iIconDrawable.getBitmap().isRecycled() )
            {
                //change the image 1st
                iIconView.setImageDrawable( null );
                iIconView.setImageBitmap(   null );
                iIconView.setImageResource( R.drawable.net_picfood_loading_icon_user );

                //recycle and ditch old image
                iIconDrawable.getBitmap().recycle();
                iIconDrawable = null;

                //PicFoodDebug.bitmapRecycling.out( " recycled icon successfully" );
            }
        }
    }
