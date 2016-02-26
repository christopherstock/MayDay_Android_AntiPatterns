
    package de.mayflower.lib.io;

    import  android.content.*;
    import  android.net.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.util.*;

    /*********************************************************************************************
    *   Performs a threaded installation of the local saved apk.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *********************************************************************************************/
    public class LibInstallationThread extends Thread
    {
        /** The uri of the apk stored in the local filesystem. */
        public                          Uri                             iUri            = null;

        /** The according activity context. */
        public                          Context                         iContext        = null;

        /** The debug system. */
        public                          LibDebug                        iDebug          = null;

        /*********************************************************************************************
        *   Creates a new threaded installation.
        *
        *   @param  aUri        The uri of the stored apk in the filesystem.
        *   @param  aContext    The according activity context.
        *   @param  aDebug      The debug system for this task.
        *********************************************************************************************/
        public LibInstallationThread( Uri aUri, Context aContext, LibDebug aDebug )
        {
            iUri        = aUri;
            iContext    = aContext;
            iDebug      = aDebug;
        }

        @Override
        public void run()
        {
            setPriority( LibThreadPriority.MAX.getNumber() );
            installApk();
        }

        /*********************************************************************************************
        *   Installs the apk threaded.
        *********************************************************************************************/
        public final void installApk()
        {
            iDebug.out( "APK URI to install is [" + iUri + "]" );

            Intent intent = new Intent();
            intent.setAction(       android.content.Intent.ACTION_VIEW                                      );
            intent.setFlags(        Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT     );  //bring task to front if already running
            intent.setDataAndType(  iUri, "application/vnd.android.package-archive"                         );
            iContext.startActivity( intent                                                                  );
        }
    }
