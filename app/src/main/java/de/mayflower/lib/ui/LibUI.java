/*  $Id: LibUI.java 50546 2013-08-09 14:19:00Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.lib.ui;

    import  java.util.*;
    import  android.app.*;
    import  android.content.*;
    import  android.content.res.*;
    import  android.graphics.*;
    import  android.graphics.drawable.*;
    import  android.support.v4.view.*;
    import  android.support.v4.view.ViewPager.OnPageChangeListener;
    import  android.text.*;
    import  android.util.*;
    import  android.view.*;
    import  android.view.inputmethod.*;
    import  android.widget.*;
    import  android.widget.ImageView.ScaleType;
    import  android.widget.TabHost.TabSpec;

    import de.mayflower.lib.*;

    /************************************************************************
    *   All independent UI-functions.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50546 $ $Date: 2013-08-09 16:19:00 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src_lib/com/synapsy/android/lib/ui/LibUI.java $"
    ************************************************************************/
    public class LibUI
    {
        /*********************************************************************************
        *   Defines all states that a checkbox can have.
        *********************************************************************************/
        public static enum CheckboxState
        {
            /** The unchecked state for a checkbox. */
            EUnchecked,

            /** The checked state for a checkbox. */
            EChecked,

            ;
        }

        /*****************************************************************************
        *   Defines all possible canvas orientations.
        *****************************************************************************/
        public static enum CanvasOrientation
        {
            /*****************************************************************************
            *   Represents an upright canvas-orientation
            *   where the height is greater than the width.
            *****************************************************************************/
            EPortrait,

            /*****************************************************************************
            *   Represents a landscape canvas-orientation
            *   where the width is greater than the height.
            *****************************************************************************/
            ELandscape,

            ;
        }

        /*********************************************************************************
        *   Specifies the four cardinal points.
        *********************************************************************************/
        public static enum LibDirection
        {
            /** Points to the left. */
            EWest,

            /** Points to the right. */
            EEast,

            /** Points to the top. */
            ENorth,

            /** Points to the bottom. */
            ESouth,

            ;
        }

        /************************************************************************
        *   Converts the specified metric dimension to pixels using the
        *   given density. The result is scaled by the scaleFactor.
        *
        *   @param  scaleFactor     The scaleFactor to scale the results with.
        *                           1.0 means no scaling.
        *   @param  dpi             The density in dots-per-inch.
        *   @param  sizeInCm        The size in cm to convert to pixels.
        *   @return                 The scaled size in integer pixels.
        ************************************************************************/
        public static final float convertToPixels( float scaleFactor, int dpi, float sizeInCm )
        {
            return ( scaleFactor * dpi * sizeInCm / Lib.CM_PER_INCH );
        }

        /************************************************************************
        *   Removes all views of the specified ViewGroup.
        *   This method is performed on the UI-Thread.
        *
        *   @param  activity        The according activity context.
        *   @param  viewGroup       The ViewGroup to remove all views from.
        ************************************************************************/
        public static final void removeAllViewsUIThreaded( Activity activity, final ViewGroup viewGroup )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        viewGroup.removeAllViews();
                    }
                }
            );
        }

        /************************************************************************
        *   Sets up a {@link Button} with the major attributes.
        *
        *   @param  context         The current system context.
        *   @param  button          The Button to configure.
        *   @param  textID          The resource-ID for the caption of this button.
        *   @param  typeface        The typeface to use for the caption.
        *   @param  action          The OnClick-action.
        ************************************************************************/
        public static final void setupButton( Context context, Button button, int textID, Typeface typeface, Runnable action )
        {
            setupButton( button, LibResource.getResourceSpannedString( context, textID ), typeface, action, Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL );
        }

        /************************************************************************
        *   Sets up a {@link Button} with the major attributes.
        *
        *   @param  button          The Button to configure.
        *   @param  text            The caption for the button.
        *   @param  typeface        The typeface to use for the caption.
        *   @param  action          The OnClick-action.
        *   @param  gravity         The gravity for the displayed caption.
        ************************************************************************/
        private static final void setupButton( Button button, CharSequence text, Typeface typeface, Runnable action, int gravity )
        {
            button.setGravity(      gravity                                 );
            button.setText(         text, TextView.BufferType.SPANNABLE     );
            button.setTypeface(     typeface                                );

            setOnClickAction( button, action );
        }

        /************************************************************************
        *   Sets up a {@link TextView} with the major attributes.
        *
        *   @param  context         The current system context.
        *   @param  textView        The TextView to configure.
        *   @param  typeface        The typeface to use for the caption.
        *   @param  textID          The resource-ID for the caption of this button.
        ************************************************************************/
        public static final void setupTextView( Context context, TextView textView, Typeface typeface, int textID )
        {
            setupTextView( textView, typeface,  LibResource.getResourceSpannedString( context, textID ) );
        }

        /************************************************************************
        *   Sets up a {@link TextView} with the major attributes.
        *
        *   @param  textView        The TextView to configure.
        *   @param  typeface        The typeface to use for the caption.
        *   @param  text            The caption of this button.
        ************************************************************************/
        public static final void setupTextView( TextView textView, Typeface typeface, CharSequence text )
        {
            textView.setTypeface(   typeface    );
            textView.setText(       text        );
        }

        /************************************************************************
        *   Sets up a {@link TextView} with a color-state-list. Different colors
        *   for different states ( unselected, pressed, selected, hover )
        *   can be assigned this way.
        *
        *   @param  context             The current system context.
        *   @param  textView            The TextView to configure.
        *   @param  colorStateListID    The resource-ID of the color state list.
        ************************************************************************/
        public static final void setupTextViewColorStateList( Context context, TextView textView, int colorStateListID )
        {
            try
            {
                XmlResourceParser xrp = context.getResources().getXml( colorStateListID );
                ColorStateList    csl = ColorStateList.createFromXml( context.getResources(), xrp );
                textView.setTextColor( csl );
            }
            catch ( Throwable t )
            {
            }
        }

        /************************************************************************
        *   Sets up an item in The according activity context.
        *   This is represented by a TextView and a LinearLayout in the bg.
        *
        *   @param  activity            The activity where this item is specified.
        *   @param  itemID              The resource-layout-ID of the item's background layout.
        *   @param  textID              The resource-ID of the TextView component.
        *   @param  backgroundID        The resource-color-ID for the background of the bg layout.
        *   @param  captionID           The resource-ID of the caption to assign to the TextView.
        *   @param  action              The action to launch when this item is clicked.
        *   @param  typeface            The Typeface to use for the caption.
        *   @return                     The assembled item in a view container.
        ************************************************************************/
        public static final ViewGroup setupItem( Activity activity, int itemID, int textID, int backgroundID, int captionID, Runnable action, Typeface typeface )
        {
            return setupItem( activity, itemID, textID, backgroundID, LibResource.getResourceSpannedString( activity, captionID ), action, typeface );
        }

        /************************************************************************
        *   Sets up an item in The according activity context.
        *   This is represented by a TextView and a LinearLayout in the bg.
        *
        *   @param  activity            The activity where this item is specified.
        *   @param  itemID              The resource-layout-ID of the item's background layout.
        *   @param  textID              The resource-ID of the TextView component.
        *   @param  backgroundID        The resource-color-ID for the background of the bg layout.
        *   @param  caption             The caption to assign to the TextView.
        *   @param  action              The action to launch when this item is clicked.
        *   @param  typeface            The Typeface to use for the caption.
        *   @return                     The assembled item in a view container.
        ************************************************************************/
        public static final ViewGroup setupItem( Activity activity, int itemID, int textID, int backgroundID, CharSequence caption, Runnable action, Typeface typeface )
        {
            ViewGroup bgItem = (LinearLayout)activity.findViewById( itemID );
            setOnClickAction( bgItem, action );
            bgItem.setBackgroundResource( backgroundID );

            TextView textItem = (TextView)activity.findViewById( textID );
            setupTextView( textItem, typeface, caption );

            return bgItem;
        }

        /************************************************************************
        *   Creates a new tab specification for the given TabHost.
        *
        *   @param  context             The current system context.
        *   @param  host                The TabHost that wants to create a new tab specification.
        *   @param  tabID               This tab's identifier.
        *   @param  contentID           An integer tab content ID.
        *   @param  iconID              The resource-id of the icon to use for this Tab.
        *   @return                     The assembled tab specification.
        ************************************************************************/
        public static final TabSpec createTabSpec( Context context, TabHost host, String tabID, int contentID, int iconID )
        {
            TabSpec spec = host.newTabSpec( tabID );
            spec.setContent( contentID );

            ImageView iv = createImageView( context, iconID, ScaleType.CENTER_INSIDE );
            spec.setIndicator( iv );

            return spec;
        }

        /************************************************************************
        *   Creates a {@link ImageView} that holds an image.
        *
        *   @param  context     The current context.
        *   @param  imageID     The resource-ID of the image to set into the ImageView.
        *   @param  scaleType   The ScaleType to set for this ImageView.
        *   @return             The created ImageView holding the given image.
        ************************************************************************/
        public static final ImageView createImageView( Context context, int imageID, ScaleType scaleType )
        {
            ImageView ret = new ImageView( context );
            ret.setImageResource(   imageID         );
            ret.setScaleType(       scaleType       );

            return ret;
        }

        /************************************************************************
        *   Creates a {@link ImageView} that holds an image.
        *
        *   @param  context     The current context.
        *   @param  x           The left margin.
        *   @param  y           The top margin.
        *   @param  width       The width of the ImageView.
        *   @param  height      The height of the ImageView.
        *   @param  drawableID  The resource-ID of the Drawable to assign.
        *   @param  scaleType   The scaleType to assign.
        *   @return             The constructed ImageView.
        ************************************************************************/
        public static final ImageView createImageView( Context context, int x, int y, int width, int height, int drawableID, ScaleType scaleType )
        {
            ImageView                       ret    = new ImageView( context );
            RelativeLayout.LayoutParams     layout = new RelativeLayout.LayoutParams( width, height );

            layout.setMargins(      x, y, 0, 0  );
            ret.setImageResource(   drawableID  );
            ret.setLayoutParams(    layout      );
            ret.setFocusable(       false       );      //very important!

            //if ( rule != 0 ) layout.addRule( rule );

            ret.setScaleType( scaleType );

            return ret;
        }

        /************************************************************************
        *   Creates an ImageView with scaleType {@link ScaleType#CENTER_INSIDE}
        *   from the given BitmapDrawable.
        *
        *   @param  context     The current system context.
        *   @param  d           The BitmapDrawable to assign.
        *   @return             The constructed ImageView holding the specified BitmapDrawable.
        ************************************************************************/
        public static final ImageView createImageView( Context context, BitmapDrawable d )
        {
            ImageView iv  = new ImageView( context );

            iv.setImageDrawable(    d                       );
            iv.setFocusable(        false                   );      //very important!
            iv.setScaleType(        ScaleType.CENTER_INSIDE );      //prevents scaling on pushing menu list item?

            return iv;
        }

        /************************************************************************
        *   Creates a {@link LinearLayout} that acts as a group of views.
        *
        *   @param  context     The current system context.
        *   @param  width       The width of the layout.
        *   @param  height      The height of the layout.
        *   @param  colBgID     The resource-ID of the background-color to assign.
        *   @param  orientation The content orientation of this LinearLayout.
        *   @param  gravity     The gravity to set for this layout.
        *   @return             The assembled LinearLayout.
        ************************************************************************/
        public static final LinearLayout createLinearLayout( Context context, int width, int height, int colBgID, int orientation, int gravity )
        {
            LinearLayout            ret     = new LinearLayout( context );

            ViewGroup.LayoutParams  layout  = new LinearLayout.LayoutParams( width, height );
            ret.setLayoutParams(        layout          );

            ret.setBackgroundResource(  colBgID         );
            ret.setFocusable(           false           );
            ret.setOrientation(         orientation     );
            ret.setGravity(             gravity         );

            return ret;
        }

        /************************************************************************
        *   Assigns the specified action to the specified view.
        *   The action is invoked if the view's OnClick-event is invoked.
        *   The view is marked as selected in addition.
        *
        *   @param  view        The view to assign the action to.
        *   @param  action      The OnClick-action to assign.
        ************************************************************************/
        public static final void setOnClickAction( final View view, final Runnable action )
        {
            if ( action != null )
            {
                view.setOnClickListener
                (
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick( View aArg0 )
                        {
                            //mark this view as selected
                            view.setSelected( true );

                            //perform action if not null
                            action.run();
                        }
                    }
                );
            }
        }

        /************************************************************************
        *   Assigns the specified action to the specified view.
        *   The action is invoked if the view's OnLongClick-event is invoked.
        *   The view is marked as selected in addition.
        *
        *   @param  view        The view to assign the action to.
        *   @param  action      The OnLongClick-action to assign.
        ************************************************************************/
        public static final void setOnLongPressAction( final View view, final Runnable action )
        {
            if ( action != null )
            {
                view.setOnLongClickListener
                (
                    new View.OnLongClickListener()
                    {
                        @Override
                        public boolean onLongClick( View aArg0 )
                        {
                            //mark this view as selected
                            view.setSelected( true );

                            //perform action if not null
                            action.run();

                            //this callback definitely consumed the longClick
                            return true;
                        }
                    }
                );
            }
        }

        /************************************************************************
        *   Performs a transition between two drawables.
        *
        *   @param  imageView       The view where the transition takes place.
        *   @param  oldImage        The first image that is opaque before the transition starts.
        *   @param  newImage        The second image that is opaque after the transition ends.
        *   @param  durationMillis  The duration is ms for the transition to run.
        ************************************************************************/
        public static final void startImageViewTransition( ImageView imageView, Drawable oldImage, Drawable newImage, int durationMillis )
        {
            //create TransitionDrawable
            TransitionDrawable transitionDrawable = new TransitionDrawable( new Drawable[] { oldImage, newImage, } );
            transitionDrawable.setCrossFadeEnabled( true );

            //assign TransitionDrawable to ImageView
            imageView.setImageDrawable( transitionDrawable );

            //start TransitionDrawable
            transitionDrawable.startTransition( durationMillis );
        }

        /*********************************************************************************
        *   Sets up the specified ViewPager with the according OnPageChangeListener and
        *   the page adapter.
        *
        *   @param  pageChangeListener  The listener to set. This listener gets invoked
        *                               each time a page of the ViewPager is changed.
        *   @param  adapter             The adapter that shall provide the ViewPager with data.
        *   @param  viewPager           The ViewPager to setup.
        *********************************************************************************/
        public static final void setupViewPager( OnPageChangeListener pageChangeListener, PagerAdapter adapter, ViewPager viewPager )
        {
            //assign the adapter to the view pager
            viewPager.setAdapter( adapter );
            viewPager.setOnPageChangeListener( pageChangeListener );
        }

        /*********************************************************************************
        *   Removes this view from it's parent view.
        *   This method must be invoked from the UI-Thread since it changes view components.
        *   Performs nothing, if the specified view doesn't have a parent.
        *
        *   @param  view        The view to free from their parent.
        *********************************************************************************/
        public static final void freeFromParent( View view )
        {
            if ( view.getParent() != null )
            {
                ( (ViewGroup)view.getParent() ).removeView( view );
            }
        }

        /*********************************************************************************
        *   Removes all of the specified views from their parent view.
        *   This method must be invoked from the UI-Thread since it changes view components.
        *
        *   @param  views       The views to free from their parent.
        *********************************************************************************/
        public static final void freeFromParent( Vector<View> views )
        {
            for ( View v : views )
            {
                freeFromParent( v );
            }
        }

        /************************************************************************
        *   Resets the scrolling of the specified ScrollView, setting it's x and y-offsets to 0.
        *   This method is performed on the UI-Thread.
        *
        *   @param  activity    The according activity context.
        *   @param  scrollView  The ScrollView to reset scrolling for.
        ************************************************************************/
        public static final void resetScrollViewScrollingUIThreaded( Activity activity, final ScrollView scrollView )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        scrollView.scrollTo( 0, 0 );
                    }
                }
            );
        }

        /*********************************************************************************
        *   Assigns a TextWatcher to an EditText. The TextWatcher will be informed,
        *   each time the text inside of the EditText will be changed.
        *
        *   @param  editText    The EditText to assign the TextWatcher onto.
        *   @param  watcher     The callback that's being invoked each time the text is changed.
        *********************************************************************************/
        public static final void setOnTextChangeListener( EditText editText, TextWatcher watcher )
        {
            editText.addTextChangedListener( watcher );
        }

        /*********************************************************************************
        *   Delivers the smaller dimension of the paintable screen dimensions.
        *
        *   @param  context     The current system context.
        *   @return             The smaller dimension ( width or height ) of the paintable screen dimensions.
        *********************************************************************************/
        public static final int getSmallerPaintableScreenDimension( Context context )
        {
            Point p = getPaintableScreenDimensions( context );
            if ( p.x < p.y )
            {
                return p.x;
            }
            return p.y;
        }

        /*********************************************************************************
        *   Delivers the paintable screen dimensions for this device.
        *
        *   @param  context     The current system context.
        *   @return             A point, where x represents the width and y represents the height
        *                       of the paintable screen dimensions.
        *********************************************************************************/
        public static final Point getPaintableScreenDimensions( Context context )
        {
            WindowManager   wm      = (WindowManager)context.getSystemService( Context.WINDOW_SERVICE );
            Display         display = wm.getDefaultDisplay();
            DisplayMetrics  metrics = new DisplayMetrics();

            display.getMetrics( metrics );

            Point           ret     = new Point
            (
                metrics.widthPixels,
                metrics.heightPixels
            );

            return ret;
/*
            //this solution will NOT operate in some cases!! If activity is a FragmentActivity, it sometimes returns 0 ( in emu ) !!
            //use the safer way via the WindowManager !

            Rect   rect = new Rect();
            Window win  = activity.getWindow();
            win.getDecorView().getWindowVisibleDisplayFrame( rect );

            return new Point
            (
                rect.width(),
                rect.height()
            );
*/
        }

        /*********************************************************************************
        *   Inflates and returns a view by id
        *   even if the View is not set as the current activitie's content-view.
        *
        *   @param  context     The current system context.
        *   @param  id          The resource-layout-id of the layout to inflate.
        *   @return             The inflated View.
        *********************************************************************************/
        public static final View getInflatedLayoutById( Context context, int id )
        {
            LayoutInflater  inflator    = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View            ret         = inflator.inflate( id, null );

            return ret;
        }

        /*********************************************************************************
        *   Returns all children of the specified ViewGroup.
        *
        *   @param  vg      The ViewGroup to return all children from.
        *   @return         All children of the given ViewGroup.
        *********************************************************************************/
        public static final Vector<View> getAllChildren( ViewGroup vg )
        {
            Vector<View> ret = new Vector<View>();
            for ( int i = 0; i < vg.getChildCount(); ++i )
            {
                View child = vg.getChildAt( i );
                ret.addElement( child );
            }
            return ret;
        }

        /*********************************************************************************
        *   Inflates and returns a view by id even if the View is not set as the current
        *   activitie's content-view. The inflated View is add to the specified parent ViewGroup afterwards.
        *
        *   @param  context     The current system context.
        *   @param  id          The resource-layout-id of the layout to inflate.
        *   @param  parent      The parent ViewGroup to assign the inflated layout to.
        *   @return             The inflated View.
        *********************************************************************************/
        public static final View getInflatedViewByIdAndAssignToParentViewGroup( Context context, int id, ViewGroup parent )
        {
            LayoutInflater  inflator    = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View            ret         = inflator.inflate( id, parent, true );

            return ret;
        }

        /*********************************************************************************
        *   Shows an on-screen notification.
        *
        *   @param  activity    The according activity context.
        *   @param  message     The message to show in the on-screen notification.
        *   @param  showLong    If this toast shall be shown for a long time.
        *********************************************************************************/
        public static final void showToastUIThreaded( final Activity activity, final String message, final boolean showLong )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText( activity, message, ( showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT ) ).show();
                    }
                }
            );
        }

        /*********************************************************************************
        *   Lets the soft keyboard pop up.
        *
        *   @param  activity    The according activity context.
        *   @param  view        The EditText that is provided with input from this soft keyboards.
        *********************************************************************************/
        public static final void showSoftKeyboard( final Activity activity, final EditText view )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            //act.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE );
                            //act.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
                            //InputMethodManager inputManager = (InputMethodManager)act.getSystemService( Context.INPUT_METHOD_SERVICE );
                            //inputManager.showSoftInput( act.getCurrentFocus(), InputMethodManager.SHOW_FORCED );

                            //activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE );
                            //only will trigger it if no physical keyboard is open

                            InputMethodManager mgr = (InputMethodManager)activity.getSystemService( Context.INPUT_METHOD_SERVICE );
                          //mgr.showSoftInput( view, InputMethodManager.SHOW_IMPLICIT );

                            view.setImeOptions( EditorInfo.IME_ACTION_SEARCH|EditorInfo.IME_FLAG_NO_EXTRACT_UI );

                            mgr.showSoftInput( view, InputMethodManager.SHOW_FORCED );
                        }
                        catch ( Throwable t )
                        {
                            //thrown if the soft keyboard has never been shown before
                        }
                    }
                }
            );
        }

        /*********************************************************************************
        *   Lets the soft keyboard pop down.
        *
        *   @param  activity    The according activity context.
        *********************************************************************************/
        public static final void hideSoftKeyboard( final Activity activity )
        {
            activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
        }

        /************************************************************************
        *   Creates a <b>square</b> {@link ImageButton}.
        *
        *   @param  context     The current system context.
        *   @param  imageID     The image to set for this Button.
        *   @param  action      The action to perform on clicking this Button.
        *   @param  sizeID      The maximum width and height to set for this Button.
        *   @return             The assembled ImageButton.
        ************************************************************************/
        public static final ImageButton createImageButton( Context context, int imageID, Runnable action, int sizeID )
        {
            ImageButton ret = new ImageButton( context );

            ret.setImageResource(    imageID );
            ret.setAdjustViewBounds( true );
            ret.setScaleType(        ScaleType.CENTER_INSIDE );
            ret.setMaxWidth(         LibResource.getResourceDimensionInPixel( context, sizeID ) );
            ret.setMaxHeight(        LibResource.getResourceDimensionInPixel( context, sizeID ) );
            setOnClickAction(        ret, action );

            return ret;
        }

        /************************************************************************
        *   Adds a view to another view on the UI-Thread.
        *
        *   @param  activity    The according activity context.
        *   @param  base        The base view that shall gather the other view.
        *   @param  toAdd       The view to add to the base view.
        ************************************************************************/
        public static final void addViewUIThreaded( Activity activity, final ViewGroup base, final View toAdd )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        base.addView( toAdd );
                    }
                }
            );
        }

        /************************************************************************
        *   Changes the specified view's visibility on the UI-Thread.
        *
        *   @param  activity        The according activity context.
        *   @param  view            The view to alter visibility for.
        *   @param  newVisibility   The new visibility to assign to the view.
        ************************************************************************/
        public static final void setVisibilityUIThreaded( Activity activity, final View view, final int newVisibility )
        {
            activity.runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.setVisibility( newVisibility );
                    }
                }
            );
        }

        /************************************************************************
        *   Turns the current activity into fullscreen mode.
        *
        *   @param  activity        The activity to push into fullscreen mode.
        *   @param  showStatusBar   Determines, if the status ( notification- )
        *                           bar shall persist on the screen.
        ************************************************************************/
        public static final void requestFullscreen( Activity activity, boolean showStatusBar )
        {
            activity.requestWindowFeature( Window.FEATURE_NO_TITLE );
            if ( !showStatusBar ) activity.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        }

        /************************************************************************
        *   Returns the child index of the specified view in the specified viewGroup.
        *
        *   @param  viewGroup       The group to search the other view in.
        *   @param  view            The view to find in the viewGroup.
        *   @return                 The 0-based child-index of the view inside of
        *                           the viewGroup or <code>-1</code> if the view
        *                           if NOT contained in the viewGroup.
        ************************************************************************/
        public static final int getChildIndex( ViewGroup viewGroup, View view )
        {
            int ret = -1;

            for ( int i = 0; i < viewGroup.getChildCount(); ++i )
            {
                if ( viewGroup.getChildAt( i ) == view )
                {
                    return i;
                }
            }

            return ret;
        }

        /************************************************************************
        *   Creates a {@link RelativeLayout} with the specified dimensions.
        *
        *   @param  context     The current system context.
        *   @param  width       The desired width of the layout.
        *   @param  height      The desired height of the layout.
        *   @param  colBgID     The resource-ID of the background-color to assign.
        *   @return             The assembled RelativeLayout.
        ************************************************************************/
        public static final RelativeLayout createRelativeLayout( Context context, int width, int height, int colBgID )
        {
            return createRelativeLayout( context, 0, 0, width, height, colBgID );
        }

        /************************************************************************
        *   Creates a {@link RelativeLayout} with the specified dimensions.
        *
        *   @param  context     The current system context.
        *   @param  x           The left margin.
        *   @param  y           The top margin.
        *   @param  width       The desired width of the layout.
        *   @param  height      The desired height of the layout.
        *   @param  colBgID     The resource-ID of the background-color to assign.
        *   @return             The assembled RelativeLayout.
        ************************************************************************/
        public static final RelativeLayout createRelativeLayout( Context context, int x, int y, int width, int height, int colBgID )
        {
            RelativeLayout ret                 = new RelativeLayout( context );
            RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams( width, height );
            layout.setMargins(          x, y, 0, 0  );
            ret.setLayoutParams(        layout      );
            ret.setFocusable(           false       );
            ret.setBackgroundResource(  colBgID     );

            return ret;
        }

        /************************************************************************
        *   Recycles the Bitmap in the specified ImageView.
        *   This will only be performed if the ImageView contains a BitmapDrawable
        *   with an unrecycled Bitmap.
        *
        *   @param  iv          The ImageView that holds the Bitmap to recycle.
        ************************************************************************/
        public static final void recycleBitmap( ImageView iv )
        {
            Drawable  d  = iv.getDrawable();

            //check BitmapDrawable
            if ( d instanceof BitmapDrawable )
            {
                BitmapDrawable bd = (BitmapDrawable)d;
                Bitmap         b  = bd.getBitmap();

                //check bitmap presence
                if ( b != null && !b.isRecycled() )
                {
                    //recycle
                    b.recycle();
                }
            }
        }

        /************************************************************************
        *   Recycles all Bitmaps in all ImageViews that are RECURSIVELY
        *   contained in the specified View.
        *
        *   @param  v       The View to recycle all cascaded ImageView-Bitmaps.
        ************************************************************************/
        public static final void recycleAllChildBitmapViews( View v )
        {
            //check ViewGroup
            if ( v instanceof ViewGroup )
            {
                ViewGroup vg = (ViewGroup)v;

                //browse all children
                for ( int i = 0; i < vg.getChildCount(); ++i )
                {
                    //call recursive
                    View child = vg.getChildAt( i );
                    recycleAllChildBitmapViews( child );
                }
            }
            else
            {
                //check ImageView
                if ( v instanceof ImageView )
                {
                    //recycle Bitmap
                    recycleBitmap( (ImageView)v );
                }
            }
        }
    }
