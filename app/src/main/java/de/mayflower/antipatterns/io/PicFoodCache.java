
    package de.mayflower.antipatterns.io;

    import  java.io.*;
    import  java.util.*;
    import de.mayflower.antipatterns.*;
    import  de.mayflower.antipatterns.PicFoodProject.Debug;
    import  de.mayflower.antipatterns.action.*;
    import  org.apache.http.*;
    import  org.apache.http.client.*;
    import  org.apache.http.client.methods.*;
    import  org.apache.http.util.*;
    import  android.app.*;
    import  android.content.*;
    import  android.graphics.*;
    import  android.graphics.drawable.*;
    import  android.text.*;
    import  de.mayflower.lib.*;
    import  de.mayflower.lib.io.*;
    import  de.mayflower.lib.ui.*;
    import  de.mayflower.lib.ui.dialog.*;

    /**********************************************************************************************
    *   Accesses the external cache system.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    **********************************************************************************************/
    public abstract class PicFoodCache
    {
        /*********************************************************************************
        *   Delivers a Bitmap from the specified url.
        *   If an entry for this url is present in the cache,
        *   the according Bitmap will be streamed from disk.
        *
        *   If no cached entry is available, the Bitmap will be streamed via http
        *   and saved as a cached entry afterwards.
        *
        *   @param  context     The current system context.
        *   @param  url         The url that offers this Bitmap.
        *   @return             The cached or streamed Bitmap as a BitmapDrawable.
        *   @throws Throwable   If an error on streaming or encoding occurs.
        *********************************************************************************/
        public static final BitmapDrawable getBitmapDrawableFromHttpOrCache( Context context, String url ) throws Throwable
        {
            //PicFoodDebug.imageCache.out( "Fetching bitmap from http OR cache [" + url + "]" );

            //icon target object
            byte[]                  cachedBytes         = null;
            Bitmap                  bitmap              = null;

            //check if cache is enabled and if this icon can be cached
            boolean                 cacheEnabled    = !Debug.DEBUG_DISABLE_CACHE;
            if ( cacheEnabled )
            {
                //pick bytes from cache
                cachedBytes = PicFoodSystems.getCache().get( url );

                //check if bytes are cached
                if ( cachedBytes != null )
                {
                    //create Bitmap from cached bytes
                    bitmap = LibImage.createBitmapFromByteArray( cachedBytes );
                }
            }

            //check if Bitmap has been created from cache
            if ( bitmap == null )
            {
                //order via http
                HttpClient      httpClient      = PicFoodHttp.getEnrichedHttpClient();
                HttpGet         httpGet         = new HttpGet( url );

                //connect
                HttpResponse    httpResponse    = httpClient.execute( httpGet );

                //show StatusCode
                int statusCode = httpResponse.getStatusLine().getStatusCode();
              //PicFoodDebug.googlePlaces.out( "Received StatusCode [" + statusCode + "]" );

                //get body
                HttpEntity      entity          = httpResponse.getEntity();
                final byte[]    bytes           = EntityUtils.toByteArray( entity );

                PicFoodDebug.imageCache.out( "http [" + statusCode + "] streamed [" + bytes.length + "] bytes from url [" + url + "]" );

                //save image bytes to cache system if enabled - catch errors on saving Bitmap!
                if ( cacheEnabled )
                {
                    try
                    {
                        PicFoodSystems.getCache().put( url, bytes );
                    }
                    catch ( Throwable t )
                    {
                    }
                }

                //create icon from streamed bytes
                bitmap = LibImage.createBitmapFromByteArray( bytes );
            }
            else
            {
                PicFoodDebug.imageCache.out( "cached [" + url + "] created Bitmap from [" + ( cachedBytes == null ? -1 : cachedBytes.length ) + "] bytes" );
            }

            //create BitmapDrawable from Bitmap
            BitmapDrawable ret  = new BitmapDrawable( context.getResources(), bitmap );

            return ret;
        }

        /*********************************************************************************
        *   Deletes ALL files and folders that are contained in the cache-directory.
        *   The cache-directory can be obtained using {@link PicFoodSD#getCacheDir()}.
        *
        *   @param  activity    The according activity context.
        *********************************************************************************/
        public static final void deleteCache( Activity activity )
        {
            //files
            {
                //scan all files
                Vector<File> allFiles = new Vector<File>();
                LibIO.getAllFileEntries( PicFoodSD.getCacheDir(), allFiles );

                //browse all files
                int lastPercentDisplay = -1;
                for ( int i = 0; i < allFiles.size(); ++i )
                {
                    //change progress dialog
                    int percent = ( 100 * ( i + 1 ) / allFiles.size() );
                    if ( lastPercentDisplay < percent )
                    {
                        lastPercentDisplay = percent;
                        String progressBody = LibResource.getResourceString( activity, R.string.dialog_please_wait_delete_cache_body_2 );
                        progressBody = progressBody.replace( "{progress}", lastPercentDisplay + " %" );
                        LibDialogProgress.changeProgressDialogUIThreaded( activity, null, Html.fromHtml( progressBody ) );

                        PicFoodDebug.imageCache.out( "Updated progress bar" );
                    }

                    PicFoodDebug.imageCache.out( "Delete cache entry [" + allFiles.elementAt( i ).getAbsolutePath() + "]" );
                    LibIO.deleteFile( allFiles.elementAt( i ) );
                }

                PicFoodDebug.imageCache.out( "" );

                //change progress dialog
                {
                    Spanned progressBody = LibResource.getResourceSpannedString( activity, R.string.dialog_please_wait_delete_cache_body_3 );
                    LibDialogProgress.changeProgressDialogUIThreaded( activity, null, progressBody );
                }
            }

            //dirs
            {
                //scan all dirs
                Vector<File> allDirs = new Vector<File>();
                LibIO.getAllDirectories( PicFoodSD.getCacheDir(), allDirs );

                //browse all dirs
                int lastPercentDisplay = -1;
                for ( int i = 0; i < allDirs.size(); ++i )
                {
                    //change progress dialog
                    int percent = ( 100 * ( i + 1 ) / allDirs.size() );
                    if ( lastPercentDisplay < percent )
                    {
                        lastPercentDisplay = percent;
                        String progressBody = LibResource.getResourceString( activity, R.string.dialog_please_wait_delete_cache_body_4 );
                        progressBody = progressBody.replace( "{progress}", lastPercentDisplay + " %" );
                        LibDialogProgress.changeProgressDialogUIThreaded( activity, null, Html.fromHtml( progressBody ) );

                        PicFoodDebug.imageCache.out( "Updated progress bar" );
                    }

                    PicFoodDebug.imageCache.out( "Delete cache dir [" + allDirs.elementAt( i ).getAbsolutePath() + "]" );
                    LibIO.deleteFile( allDirs.elementAt( i ) );
                }

                //dismiss progress dialog and show 'success'
                LibDialogProgress.dismissProgressDialogUIThreaded( activity );
                PicFoodActionDialog.EDialogDeleteImageCacheSuccess.run();
            }
        }
    }
