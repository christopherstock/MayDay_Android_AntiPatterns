
    package de.mayflower.antipatterns.data;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.AntiPatternsUI.ImageSize;
    import  org.json.*;
    import  android.graphics.drawable.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   Represents one comment. Each comment is assigned to one specific image.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsDataComment implements AntiPatternsData
    {
        /** This comment's ID. This value is assigned once but never read. */
        @SuppressWarnings( "unused" )
        private                         int                 iCommentID                  = 0;
        /** The view that holds the icon for the owner of this comment. */
        private                         LibImageView        iIconView                   = null;
        /** The icon of the owner of this comment. */
        private                         BitmapDrawable      iIconDrawable               = null;
        /** The creator of this comment. */
        public AntiPatternsDataUser iOwner                      = null;

        /** The timestamp in ms when this comment has been posted. */
        protected                       long                iPostedAt                   = 0;
        /** The comment's content. */
        protected                       String              iText                       = null;

        /**********************************************************************************************
        *   Parses a comment from the given json object.
        *
        *   @param  json    The json object to parse the comment from.
        **********************************************************************************************/
        public AntiPatternsDataComment(JSONObject json)
        {
            iPostedAt   = Lib.MILLIS_PER_SECOND * LibJSON.getJSONLongSecure(    json, "postedAt"  );
            iCommentID  = LibJSON.getJSONIntegerSecure( json, "commentId" );
            iText       = LibJSON.getJSONStringSecure(  json, "text"      );
            iOwner      = new AntiPatternsDataUser(  LibJSON.getJSONObjectSecure( json, "owner"     ) );
        }

        /**********************************************************************************************
        *   Parses all comments from the given json object.
        *
        *   @param  json    The json object to parse all comments from.
        *   @return         All comments that have been parsed from the json object.
        **********************************************************************************************/
        public static final AntiPatternsDataComment[] parse( JSONObject json )
        {
            JSONObject obj = LibJSON.getJSONObjectSecure( json, "comments" );
            JSONArray  arr = LibJSON.getJSONArraySecure(  obj,  "comments" );

            AntiPatternsDataComment[] ret = new AntiPatternsDataComment[ arr.length() ];
            for ( int i = 0; i < arr.length(); ++i )
            {
                ret[ i ] = new AntiPatternsDataComment( LibJSON.getJSONObjectSecure( arr, i ) );
            }

            return ret;
        }

        @Override
        public void orderImageThreaded( LibState state )
        {
            //only order if this icon is not already present
            if ( iIconDrawable == null )
            {
                AntiPatternsImage.orderImageThreaded
                        (
                                state,
                                iOwner.iProfileImageUrl,
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

            //update ImageView
            AntiPatternsUI.setNewImageUIThreaded(state.getActivity(), bd, iIconView, R.drawable.net_picfood_loading_image);
        }

        @Override
        public View createItemView( final LibState state )
        {
            ViewGroup ret = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_grid_view_simple );

            //title
            TextView title = null;
            title = (TextView)ret.findViewById( R.id.title );
            title.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_title ) );
            LibUI.setupTextView( title, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, iOwner.iUserName );

            //description
            TextView description = (TextView)ret.findViewById( R.id.description );
            description.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_description ) );
            LibUI.setupTextView( description, AntiPatternsSystems.getFonts().TYPEFACE_REGULAR, AntiPatternsUI.formatTimeDistance(state.getActivity(), iPostedAt) + "\n" + iText );

            //icon
            iIconView = (LibImageView)ret.findViewById( R.id.icon );
            //check if loaded icon is present
            if ( iIconDrawable == null )
            {
                iIconView.setImageResource( R.drawable.net_picfood_loading_icon_user );
            }
            else
            {
                AntiPatternsUI.setNewImage(state.getActivity(), iIconDrawable, iIconView, R.drawable.net_picfood_loading_icon_user);

                //iIconView.setImageDrawable( iIconDrawable );
            }

            //bg
            ret.setBackgroundResource( R.drawable.net_picfood_bg_grid_item );

            return ret;
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
