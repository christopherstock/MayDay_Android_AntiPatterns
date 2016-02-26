
    package de.mayflower.antipatterns.io;

    import  java.io.*;
    import  android.os.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.antipatterns.AntiPatternsProject.Paramounts;
    import  de.mayflower.antipatterns.data.*;

    /**********************************************************************************************
    *   Specifies the locations of all files on the SD-card.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public abstract class AntiPatternsSD
    {
        /**********************************************************************************************
        *   Delivers the abstract path descriptor of the cache directory on the SD card.
        *
        *   @return     The cache directory on the SD card. All cached images for the cache-system
        *               will be stored here.
        **********************************************************************************************/
        public static final File getCacheDir()
        {
            return new File( AntiPatternsSD.getSDCardProjectFolder(), "cache" );
        }

        /**********************************************************************************************
        *   Delivers the abstract path descriptor for the cropped file to create in the 'new entry' state.
        *
        *   @return     The file where to place the cropped image data for state 'new entry'.
        **********************************************************************************************/
        public static final File getFileLastCroppedImageNewEntry()
        {
            return new File( AntiPatternsSD.getSDCardProjectFolder(), "croppedNewEntry" );
        }

        /**********************************************************************************************
        *   Delivers the abstract path descriptor for the cropped file to create in the 'register' state.
        *
        *   @return     The file where to place the cropped image data for state 'register'.
        **********************************************************************************************/
        public static final File getFileLastCroppedImageRegister()
        {
            return new File( AntiPatternsSD.getSDCardProjectFolder(), "croppedRegister" );
        }

        /**********************************************************************************************
        *   Delivers the abstract path descriptor for the cropped file to create in the 'settings' state.
        *
        *   @return     The file where to place the cropped image data for state 'settings'.
        **********************************************************************************************/
        public static final File getFileLastCroppedImageSettings()
        {
            return new File( AntiPatternsSD.getSDCardProjectFolder(), "croppedSettings" );
        }

        /**********************************************************************************************
        *   Delivers the abstract path descriptor for the file where the 'share' action shall be performed with.
        *
        *   @param  image   The image data of the image that shall be shared.
        *                   These information influence the name of the file.
        *   @return         The file where to place the image to perform the share action on.
        **********************************************************************************************/
        public static final File getFileImageToShare( AntiPatternsDataImage image )
        {
            String username = LibString.pruneSpecialChars( image.iOwner.iUserName );
            String date     = LibString.pruneSpecialChars( LibStringFormat.getSingleton().formatDateTimeShort( image.iCreateDate ) );

            return new File( AntiPatternsSD.getSDCardProjectFolder(), username + "_" + date + ".jpg" );
        }

        /**********************************************************************************************
        *   Delivers the abstract path descriptor for the AntiPatterns project folder on the SD-card.
        *
        *   @return     The root folder on the SD card that shall contain all data for the AntiPatterns project.
        **********************************************************************************************/
        public static final File getSDCardProjectFolder()
        {
            final String PREFIX_TO_HIDE_THE_FOLDER = ".";

            File sdcard         = Environment.getExternalStorageDirectory();
            File folderAntiPatterns  = new File( sdcard, PREFIX_TO_HIDE_THE_FOLDER + Paramounts.PROJECT_SPECIFIER    );

            //create this SD-card project folder on the fly
            folderAntiPatterns.mkdirs();

            return folderAntiPatterns;
        }
    }
