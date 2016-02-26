
    package de.mayflower.antipatterns.state.pivotal;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.AntiPatternsSettings.ContentUpdatePeriods;
    import de.mayflower.antipatterns.AntiPatternsProject.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.ui.adapter.*;
    import de.mayflower.antipatterns.ui.adapter.AntiPatternsAdapterManager.*;
    import  android.os.*;
    import  android.support.v4.app.Fragment;
    import  android.view.*;
    import  android.widget.*;

    /**********************************************************************************************
    *   The third fragment.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class AntiPatternsStatePivotalTabExplore extends Fragment
    {
        /** The singleton instance of this fragment. */
        public          static AntiPatternsStatePivotalTabExplore singleton                   = null;

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
            AntiPatternsAdapterManager.getSingleton(getActivity(), GridViews.EExplore).connect( iContentContainer );

            //order explore area update
            if ( Features.LOAD_EXPLORE )
            {
                //check if next update period is reached
                if ( lastUpdate < System.currentTimeMillis() - ContentUpdatePeriods.PERIOD_UPDATE_EXPLORE )
                {
                    lastUpdate = System.currentTimeMillis();

                    //clear and update explore
                    AntiPatternsAdapterManager.getSingleton(getActivity(), GridViews.EExplore).clearData();
                    AntiPatternsActionUpdate.EUpdateExploreAreaClean.run();
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
