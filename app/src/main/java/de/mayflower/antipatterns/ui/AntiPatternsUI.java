
    package de.mayflower.antipatterns.ui;

    import  java.util.*;
    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Features;
    import  de.mayflower.antipatterns.AntiPatternsSettings.Image;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.drawable.*;
    import  android.widget.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.adapter.*;

    /*****************************************************************************
    *   Delivers ui information that is specific for the PicFood project.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public abstract class AntiPatternsUI
    {
        /*****************************************************************************
        *   All available image sizes this project uses.
        *****************************************************************************/
        public static enum ImageSize
        {
            /** The size for the detailed image representation. */
            EDetailedImage,

            /** The size for the tiled image representation. */
            ETiledImage,

            /** The size for profile or Google location icons. */
            EIcon,

            ;
        }

        /*****************************************************************************
        *   Prunes all items from the given stack that are an instance of class
        *   {@link de.mayflower.antipatterns.ui.adapter.AntiPatternsGridViewContentLoading}.
        *
        *   @param  adapterData     The stack of items that shall be freed of
        *                           loading items.
        *****************************************************************************/
        public static void pruneLoadingItems( Vector<LibAdapterData> adapterData )
        {
            for ( int i = adapterData.size() - 1; i >= 0; --i )
            {
                //check if this is a loading item
                if ( adapterData.elementAt( i ) instanceof AntiPatternsGridViewContentLoading)
                {
                    //prune it!
                    adapterData.removeElementAt( i );
                }
            }
        }

        /*****************************************************************************
        *   Sets a new image to the specified ImageView. This method is performed on the UI-Thread.
        *
        *   @param  activity            The according activity context.
        *   @param  newImage            The new image to assign to this ImageView.
        *   @param  imageView           The ImageView to assign the image to.
        *   @param  loadingDrawableID   The resource-id of the loading-image.
        *                               This is only used if {@link Features#ENABLE_IMAGE_ALPHA_ANIMATIONS}
        *                               is enabled and a transition shall therefore be performed -
        *                               from the loading drawable to the newImage.
        *****************************************************************************/
        public static void setNewImageUIThreaded
        (
            final   Activity        activity,
            final   Drawable        newImage,
            final   ImageView       imageView,
            final   int             loadingDrawableID
        )
        {
            //run on ui-Thread
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setNewImage
                        (
                            activity,
                            newImage,
                            imageView,
                            loadingDrawableID
                        );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Sets a new image to the specified ImageView.
        *
        *   @param  activity            The according activity context.
        *   @param  newImage            The new image to assign to this ImageView.
        *   @param  imageView           The ImageView to assign the image to.
        *   @param  loadingDrawableID   The resource-id of the loading-image.
        *                               This is only used if {@link Features#ENABLE_IMAGE_ALPHA_ANIMATIONS}
        *                               is enabled and a transition shall therefore be performed -
        *                               from the loading drawable to the newImage.
        *****************************************************************************/
        public static void setNewImage
        (
            Activity        activity,
            Drawable        newImage,
            ImageView       imageView,
            int             loadingDrawableID
        )
        {
            //only assign if view and image are available
            if ( imageView != null && newImage != null )
            {
                //fancy or not?
                if ( Features.ENABLE_IMAGE_ALPHA_ANIMATIONS )
                {
                    //transitions are fancy - but they sometimes hang on entering a state :(
                    LibUI.startImageViewTransition
                    (
                        imageView,
                        LibResource.getResourceDrawable( activity, loadingDrawableID ),
                        newImage,
                        AntiPatternsSettings.Performance.TRANSITION_ITEMS_FADING_MILLIS
                    );
                }
                else
                {
                    //direct assignment is lame - but it works
                    imageView.setImageDrawable( newImage );
                }
            }
        }

        /*****************************************************************************
        *   Creates a time distance String for the time distance from now till the specified timestamp.
        *
        *   @param  context     The current system context.
        *   @param  timestamp   The timestamp of the time to get the distance for.
        *   @return             A String that describes the time distance from now till
        *                       the given timestamp. e.g. "2 Hours 15 Minutes ago".
        *****************************************************************************/
        public static final String formatTimeDistance( Context context, long timestamp )
        {
            //specify result
            StringBuffer    ret             = new StringBuffer();

            //get chunk setup
            final   int     numberOfChunks  = Image.MAX_NUMBER_OF_TIME_DISTANCE_CHUNKS;
            int             chunkCount      = 0;

            //get time distance
            long            now         = System.currentTimeMillis();
            long            difference  = now - timestamp;

            //clip lower than one minute
            if ( difference < Lib.MILLIS_PER_MINUTE )
            {
                return LibResource.getResourceString( context, R.string.time_just_now );
            }

            //specify years
            if ( chunkCount < numberOfChunks )
            {
                int years = 0;
                while ( difference > Lib.MILLIS_PER_YEAR )
                {
                    difference -= Lib.MILLIS_PER_YEAR;
                    ++years;
                }
                if ( years > 0 )
                {
                    ++chunkCount;
                    ret.append( years + " " + ( years == 1 ? LibResource.getResourceString( context, R.string.time_year_singular ) : LibResource.getResourceString( context, R.string.time_year_plural ) ) + " " );
                }
            }

            //specify months
            if ( chunkCount < numberOfChunks )
            {
                int months = 0;
                while ( difference > Lib.MILLIS_PER_MONTH )
                {
                    difference -= Lib.MILLIS_PER_MONTH;
                    ++months;
                }
                if ( months > 0 )
                {
                    ++chunkCount;
                    ret.append( months + " " + ( months == 1 ? LibResource.getResourceString( context, R.string.time_month_singular ) : LibResource.getResourceString( context, R.string.time_month_plural ) ) + " " );
                }
            }

            //specify weeks
            if ( chunkCount < numberOfChunks )
            {
                int weeks = 0;
                while ( difference > Lib.MILLIS_PER_WEEK )
                {
                    difference -= Lib.MILLIS_PER_WEEK;
                    ++weeks;
                }
                if ( weeks > 0 )
                {
                    ++chunkCount;
                    ret.append( weeks + " " + ( weeks == 1 ? LibResource.getResourceString( context, R.string.time_week_singular ) : LibResource.getResourceString( context, R.string.time_week_plural ) ) + " " );
                }
            }

            //specify days
            if ( chunkCount < numberOfChunks )
            {
                int days = 0;
                while ( difference > Lib.MILLIS_PER_DAY )
                {
                    difference -= Lib.MILLIS_PER_DAY;
                    ++days;
                }
                if ( days > 0 )
                {
                    ++chunkCount;
                    ret.append( days + " " + ( days == 1 ? LibResource.getResourceString( context, R.string.time_day_singular ) : LibResource.getResourceString( context, R.string.time_day_plural ) ) + " " );
                }
            }

            //specify hours
            if ( chunkCount < numberOfChunks )
            {
                int hours = 0;
                while ( difference > Lib.MILLIS_PER_HOUR )
                {
                    difference -= Lib.MILLIS_PER_HOUR;
                    ++hours;
                }
                if ( hours > 0 )
                {
                    ++chunkCount;
                    ret.append( hours + " " + ( hours == 1 ? LibResource.getResourceString( context, R.string.time_hour_singular ) : LibResource.getResourceString( context, R.string.time_hour_plural ) ) + " " );
                }
            }

            //specify minutes
            if ( chunkCount < numberOfChunks )
            {
                int minutes = 0;
                while ( difference > Lib.MILLIS_PER_MINUTE )
                {
                    difference -= Lib.MILLIS_PER_MINUTE;
                    ++minutes;
                }
                if ( minutes > 0 )
                {
                    ++chunkCount;
                    ret.append( minutes + " " + ( minutes == 1 ? LibResource.getResourceString( context, R.string.time_minute_singular ) : LibResource.getResourceString( context, R.string.time_minute_plural ) ) + " " );
                }
            }

            //build distance sentence
            String sentence = LibResource.getResourceString( context, R.string.time_distance );
                   sentence = sentence.replace( "{time}", ret.toString().trim() );
            return sentence;
        }
    }
