
    package de.mayflower.antipatterns.data;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.io.*;
    import  de.mayflower.antipatterns.ui.*;
    import  de.mayflower.antipatterns.ui.PicFoodUI.ImageSize;
    import  org.json.*;
    import  android.graphics.drawable.*;
    import  android.view.*;
    import  android.widget.*;
    import de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;

    /**********************************************************************************************
    *   Represents one food rating.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodDataRating implements PicFoodData
    {
        /** The owner who submitted this rating. */
        public                          PicFoodDataUser     iOwner                      = null;

        /** The ImageView that displays the icon for the owner of the rating. */
        private                         ImageView           iIconView                   = null;
        /** The icon of this rating's owner. */
        private                         BitmapDrawable      iIconDrawable               = null;
        /** The timestamp when this rating has been posted. */
        @SuppressWarnings( "unused" )
        private                         long                iPostedAt                   = 0;
        /** The vote value of this rating. */
        private                         int                 iVote                       = 0;

        /**********************************************************************************************
        *   Creates a rating from the specified json object.
        *
        *   @param  json    The json object to parse this rating from.
        **********************************************************************************************/
        public PicFoodDataRating( JSONObject json )
        {
            iPostedAt   = Lib.MILLIS_PER_SECOND * LibJSON.getJSONLongSecure(    json, "postedAt"  );
            iVote       = LibJSON.getJSONIntegerSecure( json, "vote"      );
            iOwner      = new PicFoodDataUser(  LibJSON.getJSONObjectSecure( json, "owner"     ) );
        }

        /**********************************************************************************************
        *   Parses all ratings from the given json object.
        *
        *   @param  json    The json object to parse all ratings from.
        *   @return         All ratings that have been parsed from the json object.
        **********************************************************************************************/
        public static final PicFoodDataRating[] parse( JSONObject json )
        {
            JSONObject obj = LibJSON.getJSONObjectSecure( json, "foodRatings" );
            JSONArray  arr = LibJSON.getJSONArraySecure(  obj,  "ratings"     );

            PicFoodDataRating[] ret = new PicFoodDataRating[ arr.length() ];

            for ( int i = 0; i < arr.length(); ++i )
            {
                ret[ i ] = new PicFoodDataRating( LibJSON.getJSONObjectSecure( arr, i ) );
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
            LibUI.setupTextView( title, PicFoodSystems.getFonts().TYPEFACE_REGULAR, iOwner.iUserName );

            //description
            TextView description = (TextView)ret.findViewById( R.id.description );
            description.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_description ) );
            LibUI.setupTextView( description, PicFoodSystems.getFonts().TYPEFACE_REGULAR, String.valueOf( iVote ) );

            //show date?

            //icon
            iOwner.iIconView = (ImageView)ret.findViewById( R.id.icon );
            //check if loaded icon is present
            if ( iOwner.iIconDrawable == null )
            {
                iOwner.iIconView.setImageResource( R.drawable.net_picfood_loading_icon_user );
            }
            else
            {
                PicFoodUI.setNewImage( state.getActivity(), iOwner.iIconDrawable, iOwner.iIconView, R.drawable.net_picfood_loading_icon_user );

                //iOwner.iIconView.setImageDrawable( iOwner.iIconDrawable );
            }

            //bg
            ret.setBackgroundResource( R.drawable.net_picfood_bg_grid_item );

            return ret;
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
