
    package de.mayflower.antipatterns;

    import android.content.Context;
    import android.util.Log;

    import java.util.Vector;

    import de.mayflower.antipatterns.AntiPatternsProject.Debug;
    import de.mayflower.antipatterns.AntiPatternsProject.Features;
    import de.mayflower.lib.LibResource;
    import de.mayflower.lib.LibStringFormat;

    /*****************************************************************************
    *   Represents the debug system consisting of switchable debug groups
    *   formed by the enum constants. Grouped debug outs can be toggled.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public final class AntiPatternsHydrator
    {
        public static void test( Context context )
        {
            String test1 = LibResource.getResourceString(context, R.string.app_name);
            AntiPatternsDebug.major.out(">> app_name 1: [" + test1 + "]");

            String[] test2 = LibResource.getResourceStringArray( context, R.array.antipattern_category_ids);
            for (String s : test2)
            {
                AntiPatternsDebug.major.out(">> app_array 2: [" + s + "]");
            }

            String test3 = LibResource.getResourceString( context, "app_name" );
            AntiPatternsDebug.major.out(">> app_name 3: [" + test3 + "]");

            String[] test4 = LibResource.getResourceStringArray( context, "antipattern_category_ids" );
            for (String s : test4)
            {
                AntiPatternsDebug.major.out(">> app_array 4: [" + s + "]");
            }
        }




    }
