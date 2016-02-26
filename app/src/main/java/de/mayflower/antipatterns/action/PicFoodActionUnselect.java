
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
    *   @author     Christopher Stock
    *   @version    1.0
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
