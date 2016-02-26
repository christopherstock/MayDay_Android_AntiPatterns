/*  $Id: PicFoodStatePivotalTabWall.java 50564 2013-08-12 11:44:08Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state.pivotal;

    import  net.picfood.*;
    import  net.picfood.PicFoodProject.Features;
    import  net.picfood.PicFoodSettings.ContentUpdatePeriods;
    import  net.picfood.action.*;
    import  android.os.*;
    import  android.support.v4.app.Fragment;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.widget.*;

    /**********************************************************************************************
    *   The third fragment.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50564 $ $Date: 2013-08-12 13:44:08 +0200 (Mo, 12 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/pivotal/PicFoodStatePivotalTabWall.java $"
    **********************************************************************************************/
    public class PicFoodStatePivotalTabWall extends Fragment
    {
        /** The singleton instance of this fragment. */
        public      static      PicFoodStatePivotalTabWall          singleton                       = null;

        /** The cached View of the complete View Layout for this state. */
        private     static      ViewGroup                           lastLayout                      = null;
        /** Specifies, when the Layout View of this state has last been updated. */
        private     static      long                                lastUpdate                      = 0;

        /** A reference to the ScrollView in the state. */
        public                  LibScrollView                       iScrollView                     = null;
        /** The container that holds the wall-images. */
        public                  ViewGroup                           iContainerWallImages            = null;
        /** The overlay ImageView that can display any images. Used to represent the loading-icon and the 'no network'-symbol. */
        public                  ImageView                           iOverlayIcon                    = null;

        @Override
        public View onCreateView( LayoutInflater li, ViewGroup vg, Bundle b )
        {
            //invoke super method
            super.onCreateView( li, vg, b );

            //check if next update period is reached
            if ( lastLayout == null || lastUpdate < System.currentTimeMillis() - ContentUpdatePeriods.PERIOD_UPDATE_WALL )
            {
                //remember this update timestamp
                lastUpdate = System.currentTimeMillis();

                //assign singleton instance
                singleton = this;

                //inflate layout
                FrameLayout fl = (FrameLayout)li.inflate( R.layout.state_content_pivotal_tab_wall, null );

                //reference scrollview and containers
                iScrollView             = (LibScrollView)fl.findViewById( R.id.scroll_view );
                iContainerWallImages    = (ViewGroup)fl.findViewById( R.id.container_wall_images );

                //reference overlay icon
                iOverlayIcon            = (ImageView)fl.findViewById( R.id.overlay_icon );

                //order to update the own user profile
                if ( Features.LOAD_WALL )
                {
                    //clear and update all profile data
                    PicFoodActionUpdate.EUpdateUserWallClean.run();
                }

                //remember and return inflated layout
                lastLayout = fl;
                return fl;
            }

            //unselect all image buttons and links
            //PicFoodFlowWall.unselectAllImages();

            //free last layout and return it
            LibUI.freeFromParent( lastLayout );
            return lastLayout;
        }

        /**********************************************************************************************
        *   Resets the last update timestamp for this tab,
        *   forcing an immediate update when this tab is created next time.
        **********************************************************************************************/
        public static final void resetLastUpdate()
        {
            lastUpdate = 0;
        }
    }
