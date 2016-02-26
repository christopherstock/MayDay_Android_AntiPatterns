/*  $Id: PicFoodDataUser.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.data;

    import  java.io.*;
    import  org.json.*;
    import  net.picfood.*;
    import  net.picfood.action.*;
    import  net.picfood.idm.*;
    import  net.picfood.io.*;
    import  net.picfood.ui.*;
    import  net.picfood.ui.PicFoodUI.ImageSize;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.drawable.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;

    /**********************************************************************************************
    *   Represents one PicFood user.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/data/PicFoodDataUser.java $"
    **********************************************************************************************/
    public class PicFoodDataUser implements Serializable, PicFoodData
    {
        /************************************************************************
        *   Every class that implements {@link java.io.Serializable} must specify this
        *   final static field with a long serial version UID.
        ************************************************************************/
        private     static  final       long                serialVersionUID            = 0L;

        /** The user's biography. */
        public                          String              iBiography                  = null;
        /** Determines, if the current logged in user follows this user. */
        public                          boolean             iFollow                     = false;
        /** The number of users that follow this user. */
        public                          int                 iFollowerCount              = 0;
        /** The number of users this user follows. */
        public                          int                 iFollowingsCount            = 0;
        /** The ImageView that displays the icon for this user. */
        public                          ImageView           iIconView                   = null;
        /** The icon of this user. */
        public                          BitmapDrawable      iIconDrawable               = null;
        /** The number of images this user posted. */
        public                          int                 iImageCount                 = 0;
        /** The user's phone number. */
        public                          String              iPhone                      = null;
        /** The URL to the user's profile icon. */
        public                          String              iProfileImageUrl            = null;
        /** The unique ID of this user. */
        public                          String              iUserID                     = null;
        /** The user's real name. */
        public                          String              iRealName                   = null;
        /** The username of this user. */
        public                          String              iUserName                   = null;
        /** The user's website. */
        public                          String              iWebsite                    = null;

        /** The link 'username'. */
        private                         TextView            iUsernameLink               = null;
        /** The user's email address. */
        private                         String              iEmail                      = null;
        /** The foreign facebook-ID for this user. */
        private                         String              iFacebookId                 = null;
        /** The button 'follow' that is displayed near this user's profile representation. */
        private                         Button              iButtonFollow               = null;
        /** The button 'block' that is displayed near this user's profile representation. */
        private                         Button              iButtonBlock                = null;
        /** The link 'followers count'. */
        private                         TextView            iFollowersLink              = null;
        /** The link 'followings count'. */
        private                         TextView            iFollowingsLink             = null;

        /**********************************************************************************************
        *   Creates a user from the given json object.
        *
        *   @param  json    The json object to parse the user from.
        **********************************************************************************************/
        public PicFoodDataUser( JSONObject json )
        {
            iUserName           = LibJSON.getJSONStringSecure(  json, "userName"            );
            iUserID             = LibJSON.getJSONStringSecure(  json, "userId"              );
            iRealName           = LibJSON.getJSONStringSecure(  json, "realName"            );
            iEmail              = LibJSON.getJSONStringSecure(  json, "email"               );
            iPhone              = LibJSON.getJSONStringSecure(  json, "phone"               );
            iWebsite            = LibJSON.getJSONStringSecure(  json, "website"             );
            iBiography          = LibJSON.getJSONStringSecure(  json, "biography"           );
            iProfileImageUrl    = LibJSON.getJSONStringSecure(  json, "profileImageUrl"     );
            iFacebookId         = LibJSON.getJSONStringSecure(  json, "facebookId"          );
            iFollowerCount      = LibJSON.getJSONIntegerSecure( json, "followerCount"       );
            iFollowingsCount     = LibJSON.getJSONIntegerSecure( json, "followingCount"      );
            iImageCount         = LibJSON.getJSONIntegerSecure( json, "imageCount"          );
            iFollow             = LibJSON.getJSONBooleanSecure( json, "iFollow"             );

            //debug
            //iBiography += "debug biography, last update: [" + LibStringFormat.getSingleton().formatDateTimeMedium() + "]";
        }

        /**********************************************************************************************
        *   Parses all users from the given json object.
        *
        *   @param  obj     The json object to parse all users from.
        *   @return         All users that have been parsed from the json object.
        **********************************************************************************************/
        public static final PicFoodDataUser[] parse( JSONObject obj )
        {
            JSONArray arr = LibJSON.getJSONArraySecure( obj, "users"  );
            PicFoodDataUser[] ret = new PicFoodDataUser[ arr.length() ];
            for ( int i = 0; i < arr.length(); ++i )
            {
                ret[ i ] = new PicFoodDataUser( LibJSON.getJSONObjectSecure( arr, i ) );
            }

            return ret;
        }

        /**********************************************************************************************
        *   Creates the profile view representation for this user.
        *
        *   @param  context     The current system context.
        *   @return             This user's view represenation.
        **********************************************************************************************/
        public View createProfileView( final Context context )
        {
            View ret = LibUI.getInflatedLayoutById( context, R.layout.content_profile );

            //setup loading image
            iIconView = (ImageView)ret.findViewById( R.id.profile_image );
            iIconView.setImageResource( R.drawable.net_picfood_loading_icon_user );

            //setup 'you follow' text
            TextView youFollow = (TextView)ret.findViewById( R.id.you_follow );
            if ( iFollow )
            {
                LibUI.setupTextView( context, youFollow, PicFoodSystems.getFonts().TYPEFACE_BOLD, R.string.state_foreign_profile_you_follow );
            }
            else
            {
                youFollow.setVisibility( View.GONE );
            }

            //setup username
            iUsernameLink = (TextView)ret.findViewById( R.id.username );
            LibUI.setupTextView( iUsernameLink, PicFoodSystems.getFonts().TYPEFACE_BOLD, iUserName );
            if ( LibString.isEmpty( iUserName ) ) iUsernameLink.setVisibility( View.GONE );

            //setup photo count
            TextView photoCount = (TextView)ret.findViewById( R.id.photo_count );
            String captionPhotoCount = LibResource.getResourceString( context, R.string.state_profile_photo_count ).replace( "{count}", String.valueOf( iImageCount ) );
            LibUI.setupTextView( photoCount, PicFoodSystems.getFonts().TYPEFACE_BOLD, captionPhotoCount );

            //setup followers text
            {
                iFollowersLink = (TextView)ret.findViewById( R.id.followers );
                String captionFollowers = LibResource.getResourceString( context, R.string.state_profile_followers ).replace( "{count}", String.valueOf( iFollowerCount ) );
                if ( iFollowerCount == 0 )
                {
                    LibUI.setupTextView( iFollowersLink, PicFoodSystems.getFonts().TYPEFACE_REGULAR, captionFollowers );
                }
                else
                {
                    LibUI.setupTextView( iFollowersLink, PicFoodSystems.getFonts().TYPEFACE_BOLD, captionFollowers );
                    LibUI.setupTextViewColorStateList( context, iFollowersLink, R.color.net_picfood_link_text );

                    LibUI.setOnClickAction
                    (
                        iFollowersLink,
                        new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //show all followers
                                if ( iUserID.equals( PicFoodIDM.getUserID( context ) ) )
                                {
                                    PicFoodActionPush.EPushShowOwnUserFollowers.run();
                                }
                                else
                                {
                                    PicFoodActionPush.EPushShowForeignUserFollowers.run();
                                }
                            }
                        }
                    );
                }
            }

            //setup following text
            {
                iFollowingsLink = (TextView)ret.findViewById( R.id.following );
                String captionFollowing = LibResource.getResourceString( context, R.string.state_profile_following ).replace( "{count}", String.valueOf( iFollowingsCount ) );
                if ( iFollowingsCount == 0 )
                {
                    LibUI.setupTextView( iFollowingsLink, PicFoodSystems.getFonts().TYPEFACE_REGULAR, captionFollowing );
                }
                else
                {
                    LibUI.setupTextView( iFollowingsLink, PicFoodSystems.getFonts().TYPEFACE_BOLD, captionFollowing );
                    LibUI.setupTextViewColorStateList( context, iFollowingsLink, R.color.net_picfood_link_text );

                    LibUI.setOnClickAction
                    (
                        iFollowingsLink,
                        new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //show all followings
                                if ( iUserID.equals( PicFoodIDM.getUserID( context ) ) )
                                {
                                    PicFoodActionPush.EPushShowOwnUserFollowings.run();
                                }
                                else
                                {
                                    PicFoodActionPush.EPushShowForeignUserFollowings.run();
                                }
                            }
                        }
                    );
                }
            }

            //setup realname
            TextView dataRealname = (TextView)ret.findViewById( R.id.realname );
            LibUI.setupTextView( dataRealname, PicFoodSystems.getFonts().TYPEFACE_REGULAR, iRealName );
            if ( LibString.isEmpty( iRealName ) ) dataRealname.setVisibility( View.GONE );

            //setup email
            TextView labelEmail  = (TextView)ret.findViewById( R.id.label_email );
            TextView dataEmail   = (TextView)ret.findViewById( R.id.data_email );
            String   sLabelEMail = LibResource.getResourceString( context, R.string.state_profile_label_email );
            LibUI.setupTextView( labelEmail, PicFoodSystems.getFonts().TYPEFACE_BOLD, sLabelEMail );
            LibUI.setupTextView( dataEmail,  PicFoodSystems.getFonts().TYPEFACE_REGULAR, iEmail );
            if ( LibString.isEmpty( iEmail ) )
            {
                dataEmail.setVisibility(  View.GONE );
                labelEmail.setVisibility( View.GONE );
            }

            //setup phone
            TextView labelPhone  = (TextView)ret.findViewById( R.id.label_phone );
            TextView dataPhone   = (TextView)ret.findViewById( R.id.data_phone );
            String   sLabelPhone = LibResource.getResourceString( context, R.string.state_profile_label_phone );
            LibUI.setupTextView( labelPhone, PicFoodSystems.getFonts().TYPEFACE_BOLD, sLabelPhone );
            LibUI.setupTextView( dataPhone,  PicFoodSystems.getFonts().TYPEFACE_REGULAR, iPhone );
            if ( LibString.isEmpty( iPhone ) )
            {
                dataPhone.setVisibility(  View.GONE );
                labelPhone.setVisibility( View.GONE );
            }

            //setup website
            TextView labelWebsite  = (TextView)ret.findViewById( R.id.label_website );
            TextView dataWebsite   = (TextView)ret.findViewById( R.id.data_website );
            String   sLabelWebsite = LibResource.getResourceString( context, R.string.state_profile_label_website );
            LibUI.setupTextView( labelWebsite, PicFoodSystems.getFonts().TYPEFACE_BOLD, sLabelWebsite );
            LibUI.setupTextView( dataWebsite,  PicFoodSystems.getFonts().TYPEFACE_REGULAR, iWebsite );
            if ( LibString.isEmpty( iWebsite ) )
            {
                dataWebsite.setVisibility(  View.GONE );
                labelWebsite.setVisibility( View.GONE );
            }

            //setup biography
            TextView labelBiography  = (TextView)ret.findViewById( R.id.label_biography );
            TextView dataBiography   = (TextView)ret.findViewById( R.id.data_biography );
            String   sLabelBiography = LibResource.getResourceString( context, R.string.state_profile_label_biography );
            LibUI.setupTextView( labelBiography, PicFoodSystems.getFonts().TYPEFACE_BOLD, sLabelBiography );
            LibUI.setupTextView( dataBiography,  PicFoodSystems.getFonts().TYPEFACE_REGULAR, iBiography );
            if ( LibString.isEmpty( iBiography ) )
            {
                dataBiography.setVisibility(  View.GONE );
                labelBiography.setVisibility( View.GONE );
            }

            //setup 'follow'- and 'block'-buttons - hide these button for the own profile
            iButtonFollow = (Button)ret.findViewById( R.id.button_follow );
            iButtonBlock  = (Button)ret.findViewById( R.id.button_block  );
            if ( PicFoodIDM.getUserID( context ).equals( iUserID ) )
            {
                iButtonFollow.setVisibility( View.GONE );
                iButtonBlock.setVisibility(  View.GONE );
            }
            else
            {
                LibUI.setupButton
                (
                    context,
                    iButtonFollow,
                    ( iFollow ? R.string.state_foreign_profile_button_unfollow : R.string.state_foreign_profile_button_follow ),
                    PicFoodSystems.getFonts().TYPEFACE_REGULAR,
                    PicFoodActionPush.EPushUserToggleFollowship
                );

                //hide block button for now ( will be released in v.2.0 )
                iButtonBlock.setVisibility(  View.GONE );
/*
                LibUI.setupButton
                (
                    context,
                    iButtonBlock,
                    R.string.state_foreign_profile_button_block,
                    PicFoodSystems.getFonts().TYPEFACE_REGULAR,
                    PicFoodActionPush.EPushUserBlock
                );
*/
            }

            return ret;
        }

        @Override
        public View createItemView( final LibState state )
        {
            ViewGroup ret = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_grid_view_simple );

            //title
            TextView title = null;
            title = (TextView)ret.findViewById( R.id.title );
            title.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_title ) );
            LibUI.setupTextView( title, PicFoodSystems.getFonts().TYPEFACE_BOLD, iUserName );
/*
            //description
            TextView description = (TextView)ret.findViewById( R.id.description );
            description.setTextColor( LibResource.getResourceColor( activity, R.color.text_gridview_item_description ) );
            LibUI.setupTextView( description, PicFoodSystems.getFonts().TYPEFACE_REGULAR, desc );
*/
            //icon
            iIconView = (ImageView)ret.findViewById( R.id.icon );
            //check if loaded icon is present
            if ( iIconDrawable == null )
            {
                iIconView.setImageResource( R.drawable.net_picfood_loading_icon_user );
            }
            else
            {
                //iIconView.setImageDrawable( iIconDrawable );
                PicFoodUI.setNewImage( state.getActivity(), iIconDrawable, iIconView, R.drawable.net_picfood_loading_icon_user );
            }

            //bg
            ret.setBackgroundResource( R.drawable.net_picfood_bg_grid_item );

            return ret;
        }

        /*****************************************************************************
        *   Writes this {@link Serializable} to the specified OutputStream.
        *
        *   @param  out         The {@link ObjectOutputStream} to write this {@link Serializable} to.
        *   @throws IOException If an error occurs on writing this object to the stream.
        *****************************************************************************/
        private synchronized void writeObject( ObjectOutputStream out ) throws IOException
        {
            out.writeUTF(       iUserName           );
            out.writeUTF(       iUserID             );
            out.writeUTF(       iRealName           );
            out.writeUTF(       iEmail              );
            out.writeUTF(       iPhone              );
            out.writeUTF(       iWebsite            );
            out.writeUTF(       iBiography          );
            out.writeUTF(       iProfileImageUrl    );
            out.writeUTF(       iFacebookId         );
            out.writeInt(       iFollowerCount      );
            out.writeInt(       iFollowingsCount     );
            out.writeInt(       iImageCount         );
        }

        /*****************************************************************************
        *   Being invoked by the system, this method fills all fields of this object
        *   with data from the given {@link ObjectInputStream}.
        *
        *   @param  in                      The {@link ObjectInputStream} to read this {@link Serializable} from.
        *   @throws IOException             If an error occurs on reading the object-fields to the stream.
        *****************************************************************************/
        private synchronized void readObject( ObjectInputStream in ) throws IOException
        {
            iUserName           = in.readUTF();
            iUserID             = in.readUTF();
            iRealName           = in.readUTF();
            iEmail              = in.readUTF();
            iPhone              = in.readUTF();
            iWebsite            = in.readUTF();
            iBiography          = in.readUTF();
            iProfileImageUrl    = in.readUTF();
            iFacebookId         = in.readUTF();
            iFollowerCount      = in.readInt();
            iFollowingsCount     = in.readInt();
            iImageCount         = in.readInt();
        }

        /**********************************************************************************************
        *   Requests to unselect all buttons and links for this profile.
        *   This method is performed on the UI-Thread.
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
        *   Requests to unselect all buttons and links for this profile.
        **********************************************************************************************/
        public final void unselectAllButtons()
        {
            if ( iButtonFollow   != null ) iButtonFollow.setSelected(   false );
            if ( iButtonBlock    != null ) iButtonBlock.setSelected(    false );

            if ( iUsernameLink   != null ) iUsernameLink.setSelected(   false );
            if ( iFollowersLink  != null ) iFollowersLink.setSelected(  false );
            if ( iFollowingsLink != null ) iFollowingsLink.setSelected( false );
        }

        @Override
        public void orderImageThreaded( LibState state )
        {
            //only order if this icon is not already present
            if ( iIconDrawable == null )
            {
                PicFoodImage.orderImageThreaded
                (
                    state,
                    iProfileImageUrl,
                    ImageSize.EIcon,
                    this
                );
            }
        }

        @Override
        public void assignLoadedImage( LibState state, ImageSize imageSize, BitmapDrawable bd )
        {
            //assign new icon
            iIconDrawable = bd;

            //assign directly if required
            PicFoodUI.setNewImageUIThreaded( state.getActivity(), bd, iIconView, R.drawable.net_picfood_loading_icon_user );
        }

        @Override
        public void recycleBitmaps()
        {
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
