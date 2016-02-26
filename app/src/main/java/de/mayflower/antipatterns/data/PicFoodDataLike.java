/*  $Id: PicFoodDataLike.java 50587 2013-08-14 09:04:26Z schristopher $
 *  ==============================================================================================================
 */
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
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   Represents one image like.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50587 $ $Date: 2013-08-14 11:04:26 +0200 (Mi, 14 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/data/PicFoodDataLike.java $"
    **********************************************************************************************/
    public class PicFoodDataLike implements PicFoodData
    {
        /** The timestamp when this like has been posted. */
        public                          long                    iPostedAt                   = 0;
        /** The creator of this like. */
        public                          PicFoodDataUser         iOwner                      = null;

        /** The ImageView that displays the icon for the owner of the like. */
        private                         LibImageView            iIconView                   = null;
        /** The icon of this like's owner. */
        private                         BitmapDrawable          iIconDrawable               = null;

        /**********************************************************************************************
        *   Parses a like from the given json object.
        *
        *   @param  json    The json object to parse the like from.
        **********************************************************************************************/
        public PicFoodDataLike( JSONObject json )
        {
            iPostedAt   = Lib.MILLIS_PER_SECOND * Long.parseLong(       LibJSON.getJSONStringSecure( json, "postedAt" ) );
            iOwner      = new PicFoodDataUser(  LibJSON.getJSONObjectSecure( json, "owner"    ) );
        }

        /**********************************************************************************************
        *   Parses all likes from the given json object.
        *
        *   @param  json    The json object to parse all likes from.
        *   @return         All likes that have been parsed from the json object.
        **********************************************************************************************/
        public static final PicFoodDataLike[] parse( JSONObject json )
        {
            JSONObject obj = LibJSON.getJSONObjectSecure( json, "imageLikes"    );
            JSONArray  arr = LibJSON.getJSONArraySecure(  obj,  "likes"         );
            PicFoodDataLike[] ret = new PicFoodDataLike[ arr.length() ];
            for ( int i = 0; i < arr.length(); ++i )
            {
                ret[ i ] =  new PicFoodDataLike( LibJSON.getJSONObjectSecure( arr, i ) );
            }

            return ret;
        }

        @Override
        public final View createItemView( LibState state )
        {
            ViewGroup ret = (ViewGroup)LibUI.getInflatedLayoutById( state.getActivity(), R.layout.content_grid_view_simple );

            //title
            TextView title = null;
            title = (TextView)ret.findViewById( R.id.title );
            title.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_title ) );
            LibUI.setupTextView( title, PicFoodSystems.getFonts().TYPEFACE_BOLD, iOwner.iUserName );

            //description
            TextView description = (TextView)ret.findViewById( R.id.description );
            description.setTextColor( LibResource.getResourceColor( state.getActivity(), R.color.text_gridview_item_description ) );
            LibUI.setupTextView( description, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodUI.formatTimeDistance( state.getActivity(), iPostedAt ) );

            //icon
            iIconView = (LibImageView)ret.findViewById( R.id.icon );
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
