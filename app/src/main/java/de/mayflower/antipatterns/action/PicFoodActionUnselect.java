/*  $Id: PicFoodActionUnselect.java 50505 2013-08-07 13:39:36Z schristopher $
 *  ==============================================================================================================
 */
    package de.mayflower.antipatterns.action;

    import de.mayflower.antipatterns.*;
    import  de.mayflower.lib.util.LibUncaughtExceptionHandler.UncaughtException;
    import  de.mayflower.antipatterns.flow.*;
    import  de.mayflower.antipatterns.state.*;
    import  de.mayflower.antipatterns.state.auth.*;
    import  de.mayflower.antipatterns.state.pivotal.*;

    /**********************************************************************************************
    *   Holds all actions the user can trigger.
    *
    *   @author     $Author: schristopher $
    *   @version    $Rev: 50505 $ $Date: 2013-08-07 15:39:36 +0200 (Mi, 07 Aug 2013) $
    *   @see        "$URL: http://svn.synapsy.net/svn/Synapsy/PicFood/android/PicFood_1_0/trunk/src/net/picfood/action/PicFoodActionUnselect.java $"
    **********************************************************************************************/
    public enum PicFoodActionUnselect implements Runnable
    {
        /** */  EUnselectButtonsForeignProfile,
        /** */  EUnselectButtonsImage,
        /** */  EUnselectButtonsLogin,
        /** */  EUnselectButtonsLostPassword,
        /** */  EUnselectButtonsNewEntry,
        /** */  EUnselectButtonsPivotalHeader,
        /** */  EUnselectButtonsPivotalUpload,
        /** */  EUnselectButtonsRegister,
        /** */  EUnselectButtonsSettings,

        ;

        /*****************************************************************************
        *   Every action is being performed in a separate thread.
        *****************************************************************************/
        @Override
        public final void run()
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        execute();
                    }
                    catch ( Throwable t )
                    {
                        PicFoodDebug.DEBUG_THROWABLE( t, "This throwable was caught in the Action system", UncaughtException.ENo );
                    }
                }
            }.start();
        }

        /*****************************************************************************
        *   Performs this action.
        *****************************************************************************/
        protected final void execute()
        {
            PicFoodDebug.major.out( "EXECUTE Action: [" + this + "]" );
          //PicFoodDebug.major.mem();

            switch ( this )
            {
                case EUnselectButtonsPivotalUpload:
                {
                    PicFoodStatePivotalTabUpload.singleton.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsPivotalHeader:
                {
                    PicFoodStatePivotal.unselectHeaderButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsNewEntry:
                {
                    PicFoodStateNewEntry.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsLogin:
                {
                    //unselect login button
                    PicFoodStateLogin.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsLostPassword:
                {
                    //unselect request-password button
                    PicFoodStateLostPassword.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsRegister:
                {
                    //unselect all buttons in state 'register'
                    PicFoodStateRegister.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsSettings:
                {
                    //unselect all buttons in state 'settings'
                    PicFoodStateSettings.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsImage:
                {
                    //unselect image buttons
                    PicFoodFlowImage.lastImage.unselectAllButtonsUIThreaded( PicFoodFlowImage.lastState.getActivity() );
                    break;
                }

                case EUnselectButtonsForeignProfile:
                {
                    PicFoodFlowProfile.getLastForeignUser().unselectAllButtonsUIThreaded( PicFoodState.EForeignProfile.getActivity() );
                    break;
                }
            }
        }
    }
