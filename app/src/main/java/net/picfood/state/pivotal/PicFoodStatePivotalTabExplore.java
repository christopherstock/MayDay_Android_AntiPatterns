/*  $Id: PicFoodStatePivotalTabExplore.java 50542 2013-08-09 13:12:27Z schristopher $
 *  ==============================================================================================================
 */
    package net.picfood.state.pivotal;

    import  net.picfood.*;
    import  net.picfood.PicFoodSettings.ContentUpdatePeriods;
    import  net.picfood.PicFoodProject.*;
    import  net.picfood.action.*;
    import  net.picfood.ui.adapter.*;
    import  net.picfood.ui.adapter.PicFoodAdapterManager.*;
    import  android.os.*;
    import  android.support.v4.app.Fragment;
    import  android.view.*;
    import  android.widget.*;

    /**********************************************************************************************
    *   The third fragment.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50542 $ $Date: 2013-08-09 15:12:27 +0200 (Fr, 09 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/state/pivotal/PicFoodStatePivotalTabExplore.java $"
    **********************************************************************************************/
    public class PicFoodStatePivotalTabExplore extends Fragment
    {
        /** The singleton instance of this fragment. */
        public          static      PicFoodStatePivotalTabExplore       singleton                   = null;

        /** Specifies, when the explore area has last been updated. */
        private         static      long                                lastUpdate                  = 0;

        /** The overlay ImageView that can display any images. Used to represent the loading-icon and the 'no network'-symbol. */
        public                      ImageView                           iOverlayIcon                = null;
        /** A reference to the content container that connects to the 'explore'-GridView. */
        public                      LinearLayout                        iContentContainer           = null;

        @Override
        public View onCreateView( LayoutInflater li, ViewGroup vg, Bundle b )
        {
            //invoke super method
            super.onCreateView( li, vg, b );

            //assign singleton instance
            singleton = this;

            //inflate layout
            FrameLayout fl      = (FrameLayout)li.inflate(          R.layout.state_content_pivotal_tab_explore, null    );
            iOverlayIcon        = (ImageView)fl.findViewById(       R.id.overlay_icon                                   );
            iContentContainer   = (LinearLayout)fl.findViewById(    R.id.content_container                              );

            //connect GridView-Adapter to Container
            PicFoodAdapterManager.getSingleton( getActivity(), GridViews.EExplore ).connect( iContentContainer );

            //order explore area update
            if ( Features.LOAD_EXPLORE )
            {
                //check if next update period is reached
                if ( lastUpdate < System.currentTimeMillis() - ContentUpdatePeriods.PERIOD_UPDATE_EXPLORE )
                {
                    lastUpdate = System.currentTimeMillis();

                    //clear and update explore
                    PicFoodAdapterManager.getSingleton( getActivity(), GridViews.EExplore ).clearData();
                    PicFoodActionUpdate.EUpdateExploreAreaClean.run();
                }
            }

            //return layout
            return fl;
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
