
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
    public enum AntiPatternsActionUnselect implements Runnable
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
                        AntiPatternsDebug.DEBUG_THROWABLE(t, "This throwable was caught in the Action system", UncaughtException.ENo);
                    }
                }
            }.start();
        }

        /*****************************************************************************
        *   Performs this action.
        *****************************************************************************/
        protected final void execute()
        {
            AntiPatternsDebug.major.out( "EXECUTE Action: [" + this + "]" );

            switch ( this )
            {
                case EUnselectButtonsPivotalUpload:
                {
                    AntiPatternsStatePivotalTabUpload.singleton.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsPivotalHeader:
                {
                    AntiPatternsStatePivotal.unselectHeaderButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsNewEntry:
                {
                    AntiPatternsStateNewEntry.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsLogin:
                {
                    //unselect login button
                    AntiPatternsStateLogin.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsLostPassword:
                {
                    //unselect request-password button
                    AntiPatternsStateLostPassword.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsRegister:
                {
                    //unselect all buttons in state 'register'
                    AntiPatternsStateRegister.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsSettings:
                {
                    //unselect all buttons in state 'settings'
                    AntiPatternsStateSettings.unselectAllButtonsUIThreaded();
                    break;
                }

                case EUnselectButtonsImage:
                {
                    //unselect image buttons
                    AntiPatternsFlowImage.lastImage.unselectAllButtonsUIThreaded( AntiPatternsFlowImage.lastState.getActivity() );
                    break;
                }

                case EUnselectButtonsForeignProfile:
                {
                    AntiPatternsFlowProfile.getLastForeignUser().unselectAllButtonsUIThreaded( AntiPatternsState.EForeignProfile.getActivity() );
                    break;
                }
            }
        }
    }
