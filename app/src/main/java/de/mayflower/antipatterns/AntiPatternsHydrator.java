
    package de.mayflower.antipatterns;

    import  android.content.Context;
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


                //categoryItems[i] = new String[];
            }




        }


        private static void hydrateCategories( Context context )
        {


        }

        private static void hydratePatterns( Context context )
        {


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
