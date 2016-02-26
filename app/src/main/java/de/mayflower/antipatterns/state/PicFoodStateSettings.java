
    package de.mayflower.antipatterns.state;

    import  de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.action.*;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.PicFoodState.*;
    import  de.mayflower.lib.ui.*;
    import  android.os.Bundle;
    import  android.view.*;
    import  android.app.Activity;
    import  android.content.*;

    /**********************************************************************************************
    *   The state 'settings'.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public class PicFoodStateSettings extends Activity
    {
        /** The singleton instance of this state. */
        protected       static          PicFoodStateSettings    singleton                   = null;

        /** All displayed button/itemsof the settings state. */
        protected                       ViewGroup[]             iItems                      = null;

        @Override
        protected void onCreate( Bundle bundle )
        {
            //invoke super method
            super.onCreate( bundle );

            //assign singleton
            singleton = this;

            //init state activity
            PicFoodState.initStateActivity
            (
                PicFoodState.ESettings,
                R.layout.state_content_settings,
                R.string.state_settings_headline,
                HideHeaderBar.ENo,
                ShowHeaderBackButton.EYes,
                ShowHeaderSettingsButton.ENo,
                ShowHeaderSearchButton.ENo,
                ShowHeaderAppLogo.ENo,
                PicFoodActionState.ELeaveSettings
            );

            //setup items
            iItems = new ViewGroup[]
            {
                //find friends
                LibUI.setupItem(  this,     R.id.item_find_friends,         R.id.text_find_friends,             R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_find_friends,          PicFoodActionPush.EPushFindFriends,                 PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //send feedback
                LibUI.setupItem(  this,     R.id.item_send_feedback,        R.id.text_send_feedback,            R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_send_feedback,         PicFoodActionPush.EPushSendFeedback,                PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //privacy policy
                LibUI.setupItem(  this,     R.id.item_privacy_policy,       R.id.text_privacy_policy,           R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_privacy_policy,        PicFoodActionPush.EPushSettingsPrivacyPolicy,       PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //terms and conditions
                LibUI.setupItem(  this,     R.id.item_terms_and_conditions, R.id.text_terms_and_conditions,     R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_terms_and_conditions,  PicFoodActionPush.EPushSettingsTermsAndConditions,  PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //change profile
                LibUI.setupItem(  this,     R.id.item_change_profile,       R.id.text_change_profile,           R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_change_profile,        PicFoodActionPush.EPushChangeProfile,               PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //change profile image
                LibUI.setupItem(  this,     R.id.item_change_profile_image, R.id.text_change_profile_image,     R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_change_profile_image,  PicFoodActionPush.EPushChangeProfileImage,          PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //logout
                LibUI.setupItem(  this,     R.id.item_logout,               R.id.text_logout,                   R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_logout,                PicFoodActionPush.EPushLogout,                      PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //change password
                LibUI.setupItem(  this,     R.id.item_change_password,      R.id.text_change_password,          R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_change_password,       PicFoodActionPush.EPushChangePassword,              PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //delete image cache
                LibUI.setupItem(  this,     R.id.item_delete_image_cache,   R.id.text_delete_image_cache,       R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_delete_image_cache,    PicFoodActionPush.EPushDeleteImageCache,            PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //delete account
                LibUI.setupItem(  this,     R.id.item_delete_account,       R.id.text_delete_account,           R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_delete_account,        PicFoodActionPush.EPushDeleteAccount,               PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
                //check for updates
                LibUI.setupItem(  this,     R.id.item_check_update,         R.id.text_check_update,             R.drawable.net_picfood_bg_item,                 R.string.state_settings_item_check_update,          PicFoodActionPush.EPushSettingsCheckUpdate,         PicFoodSystems.getFonts().TYPEFACE_BOLD   ),
            };
        }

        @Override
        public boolean onKeyDown( int keyCode, KeyEvent event )
        {
            switch ( keyCode )
            {
                case KeyEvent.KEYCODE_BACK:
                {
                    //leave state 'settings'
                    PicFoodActionState.ELeaveSettings.run();

                    //prevent this event from being propagated further
                    return true;
                }
            }

            //let the system handle this event
            return false;
        }

        /**********************************************************************************************
        *   Unselects all buttons/items being shown in this state.
        *   This method is performed on the UI-Thread.
        **********************************************************************************************/
        public static final void unselectAllButtonsUIThreaded()
        {
            if ( singleton != null )
            {
                singleton.runOnUiThread
                (
                    new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for ( ViewGroup vg : singleton.iItems )
                            {
                                vg.setSelected( false );
                            }
                        }
                    }
                );
            }
        }

        @Override
        public void onActivityResult( int requestCode, int responseCode, Intent data )
        {
            //invoke super method
            super.onActivityResult( requestCode, responseCode, data );

            //switch requestCode
            switch ( requestCode )
            {
                case PicFoodSettings.ActivityRequestID.STATE_SETTINGS_PICK_IMAGE_FROM_GALLERY:
                {
                    //unselect buttons
                    PicFoodActionUnselect.EUnselectButtonsSettings.run();

                    //handle selected image data if response code is successful
                    if ( responseCode == RESULT_OK )
                    {
                        PicFoodFlowProfileChange.handlePickedImage( this, data );
                    }
                    break;
                }

                case PicFoodSettings.ActivityRequestID.STATE_SETTINGS_CROP_IMAGE:
                {
                    //handle selected image data if response code is successful
                    if ( ( responseCode == RESULT_OK && data != null ) )
                    {
                        //handle the new cropped image data
                        PicFoodFlowProfileChange.handleCroppedImage( this );
                    }
                    break;
                }
            }
        }
    }
