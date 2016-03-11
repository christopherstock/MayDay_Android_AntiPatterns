
    package de.mayflower.antipatterns;

    import  android.content.Context;
    import  java.util.*;
    import  de.mayflower.lib.LibResource;

    /*****************************************************************************
    *   Represents the debug system consisting of switchable debug groups
    *   formed by the enum constants. Grouped debug outs can be toggled.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public final class AntiPatternsHydrator
    {


        public static void hydrateData( Context context )
        {
            String[] categoryIds    = LibResource.getResourceStringArray( context, "antipattern_category_ids" );
            for ( String s : categoryIds )
            {
                AntiPatternsDebug.major.out( ">> category ID [" + s + "]" );
            }

            String[] categoryTitles = new String[ categoryIds.length ];
            for ( int i = 0; i < categoryTitles.length; ++i )
            {
                categoryTitles[i] = LibResource.getResourceString(
                    context,
                    "antipattern_category_" + i + "_title"
                );
            }

            for ( String s : categoryTitles )
            {
                AntiPatternsDebug.major.out( ">> category Title [" + s + "]" );
            }

            String[][] categoryItems = new String[ categoryIds.length ][];
            for ( int i = 0; i < categoryItems.length; ++i )
            {
                categoryItems[i] = LibResource.getResourceStringArray(
                    context,
                    "antipattern_category_" + i + "_items"
                );

                for ( String s : categoryItems[i] )
                {
                    AntiPatternsDebug.major.out(" >> category id [" + i + "] item [" + s + "]");
                }






                //categoryItems[i] = new String[];
            }

        }

        public static void hydratePatterns( Context context )
        {
            String[] simulatedPatternIds = new String[] { "0", "1", };

            Hashtable<Integer, String>   patternTitles   = new Hashtable<Integer, String>();
            Hashtable<Integer, String[]> patternProblems = new Hashtable<Integer, String[]>();
            Hashtable<Integer, String[]> patternRemedies = new Hashtable<Integer, String[]>();

            for ( String simulatedParentId : simulatedPatternIds )
            {
                String title = LibResource.getResourceString(
                    context,
                    "antipattern_" + simulatedParentId + "_title"
                );

                String[] problems = LibResource.getResourceStringArray(
                    context,
                    "antipattern_" + simulatedParentId + "_problems"
                );

                String[] remedies = LibResource.getResourceStringArray(
                    context,
                    "antipattern_" + simulatedParentId + "_remedies"
                );

                AntiPatternsDebug.major.out(" >> AP title [" + title + "]");
                for ( String s : problems )
                {
                    AntiPatternsDebug.major.out(" >> AP problem [" + s + "]");
                }
                for ( String s : remedies )
                {
                    AntiPatternsDebug.major.out(" >> AP remedy [" + s + "]");
                }
            }
        }


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
