
    package de.mayflower.antipatterns.ui;

    import  de.mayflower.antipatterns.*;
    import  android.app.*;
    import  android.view.*;
    import  android.view.animation.*;
    import  android.widget.*;
    import  de.mayflower.lib.*;

    /*****************************************************************************
    *   Manages all tasks that are associated with displaying and changing loading circles.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public abstract class PicFoodLoadingCircle
    {
        /*****************************************************************************
        *   Shows the animated loading circle on the specified overlayIcon.
        *   This method runs on the UI-Thread.
        *
        *   @param  activity        The according activity context.
        *   @param  loadingCircle   The ImageView that holds the loading circle image.
        *****************************************************************************/
        public static final void showAndStartLoadingCircleUIThreaded( final Activity activity, final ImageView loadingCircle )
        {
            PicFoodDebug.loadingCircles.out( "showAndStartLoadingCircleInOverlayUIThreaded()" );

            //invoke on ui-thread
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showAndStartLoadingCircle( activity, loadingCircle );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Shows the animated loading circle on the specified overlayIcon.
        *
        *   @param  activity        The according activity context.
        *   @param  loadingCircle   The ImageView that holds the loading circle image.
        *****************************************************************************/
        public static final void showAndStartLoadingCircle( final Activity activity, final ImageView loadingCircle )
        {
            //assign image 'loading circle' overlay-icon
            loadingCircle.setImageResource( R.drawable.net_picfood_loading_circle );

            //disable clicks
            loadingCircle.setClickable( false );

            //start loading-circle animation
            Animation a = LibResource.getResourceAnimation( activity, R.anim.rotation_cw );
            loadingCircle.startAnimation( a );

            //show overlay-icon
            loadingCircle.setVisibility( View.VISIBLE );
        }

        /*****************************************************************************
        *   Hides the overlayIcon and clears any pending animation.
        *   This method runs on the UI-Thread.
        *
        *   @param  activity        The according activity context.
        *   @param  loadingCircle   The ImageView that holds the loading circle image.
        *****************************************************************************/
        public static final void removeLoadingCircleUIThreaded( final Activity activity, final ImageView loadingCircle )
        {
            PicFoodDebug.loadingCircles.out( "removeLoadingCircleUIThreaded()" );

            //invoke on ui-thread
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        removeLoadingCircle( loadingCircle );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Hides the overlayIcon and clears any pending animation.
        *
        *   @param  loadingCircle   The ImageView that holds the loading circle image.
        *****************************************************************************/
        public static final void removeLoadingCircle( final ImageView loadingCircle )
        {
            //clear rotation animation
            loadingCircle.clearAnimation();

            //disable clicks
            loadingCircle.setClickable( false );

            //hide icon
            loadingCircle.setVisibility( View.INVISIBLE );
        }

        /*****************************************************************************
        *   Stops the loading-circle animation and changes the icon to 'no network'.
        *   This method runs on the UI-Thread.
        *
        *   @param  activity        The according activity context.
        *   @param  loadingCircle   The ImageView that holds the loading circle image.
        *   @param  actionOnClick   The action to assign when this icon is clicked.
        *****************************************************************************/
        public static final void turnLoadingCircleToNoNetworkUIThreaded( final Activity activity, final ImageView loadingCircle, final Runnable actionOnClick )
        {
            PicFoodDebug.loadingCircles.out( "turnLoadingCircleToNoNetworkUIThreaded()" );

            //invoke on ui-thread
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        turnLoadingCircleToNoNetwork( loadingCircle, actionOnClick );
                    }
                }
            );
        }

        /*****************************************************************************
        *   Stops the loading-circle animation and changes the icon to 'no network'.
        *
        *   @param  loadingCircle   The ImageView that holds the loading circle image.
        *   @param  actionOnClick   The action to assign when this icon is clicked.
        *****************************************************************************/
        public static final void turnLoadingCircleToNoNetwork( final ImageView loadingCircle, final Runnable actionOnClick )
        {
            //clear rotation animation
            loadingCircle.clearAnimation();

            //change icon to 'no network'
            loadingCircle.setImageResource( R.drawable.net_picfood_no_network );

            //enable clicks and set onClick-action if this shall be supported
            if ( actionOnClick == null )
            {
                loadingCircle.setClickable( false );
            }
            else
            {
                loadingCircle.setClickable( true );
                loadingCircle.setOnClickListener
                (
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick( View aV )
                        {
                            //disable clickability immediately in order to prevent multiple presses
                            loadingCircle.setClickable( false );

                            //perform action
                            actionOnClick.run();
                        }
                    }
                );
            }
        }
    }
