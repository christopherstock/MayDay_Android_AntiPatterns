
    package de.mayflower.antipatterns.state.pivotal;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  android.os.*;
    import  android.support.v4.app.Fragment;
    import  android.view.*;
    import  android.widget.*;
    import  de.mayflower.lib.ui.*;

    /**********************************************************************************************
    *   The third fragment.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodStatePivotalTabUpload extends Fragment
    {
        /** The singleton instance of this fragment. */
        public      static          PicFoodStatePivotalTabUpload    singleton                       = null;

        /** A reference to the Button 'upload from camera'. */
        protected                   Button                          iButtonUploadFromCamera         = null;
        /** A reference to the Button 'upload from gallery'. */
        protected                   Button                          iButtonUploadFromGallery        = null;

        @Override
        public View onCreateView( LayoutInflater li, ViewGroup vg, Bundle b )
        {
            //invoke super method
            super.onCreateView( li, vg, b );

            //assign singleton instance
            singleton = this;

            //inflate layout
            View uploadView = li.inflate( R.layout.state_content_pivotal_tab_upload, null );

            //setup headline
            {
                //headline
                TextView headline       = (TextView)uploadView.findViewById( R.id.headline );
                LibUI.setupTextView( getActivity(), headline, PicFoodSystems.getFonts().TYPEFACE_REGULAR, R.string.state_upload_headline );
            }

            //setup buttons
            {
                //button 'upload from camera'
                iButtonUploadFromCamera     = (Button)uploadView.findViewById( R.id.upload_from_camera );
                LibUI.setupButton( getActivity(), iButtonUploadFromCamera, R.string.state_upload_from_camera, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionPush.EPushUploadImageFromCamera );

                //button 'upload from gallery'
                iButtonUploadFromGallery    = (Button)uploadView.findViewById( R.id.upload_from_gallery );
                LibUI.setupButton( getActivity(), iButtonUploadFromGallery, R.string.state_upload_from_gallery, PicFoodSystems.getFonts().TYPEFACE_REGULAR, PicFoodActionPush.EPushUploadImageFromGallery );
            }

            //return inflated layout
            return uploadView;
        }

        /**********************************************************************************************
        *   Unselects all buttons/items being shown in this state.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public final void unselectAllButtonsUIThreaded()
        {
            getActivity().runOnUiThread
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        iButtonUploadFromCamera.setSelected(  false );
                        iButtonUploadFromGallery.setSelected( false );
                    }
                }
            );
        }
    }
