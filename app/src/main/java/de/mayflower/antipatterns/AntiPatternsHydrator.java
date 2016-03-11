
    package de.mayflower.antipatterns;

    import  android.content.Context;
    import  java.util.*;
    import  de.mayflower.antipatterns.data.*;
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
        public      static          Category[]          categories          = null;
        public      static          Pattern[]           patterns            = null;

        public static Category[] categories;
        public static Pattern[] patterns;

        public static void hydrateCategories(Context context)
        {
            String[] categoryIds = LibResource.getResourceStringArray( context, "antipattern_category_ids" );
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

            Integer[][] categoryItems = new Integer[ categoryIds.length ][];
            String[] items;
            for ( int i = 0; i < categoryItems.length; ++i )
            {
                items = LibResource.getResourceStringArray(
                    context,
                    "antipattern_category_" + i + "_items"
                );

                categoryItems[ i ] = new Integer[ items.length ];
                for ( int j = 0; j < items.length; j++ )
                {
                    AntiPatternsDebug.major.out(" >> category id [" + i + "] item [" + items[j] + "]");
                    categoryItems[i][j] = Integer.parseInt(items[j]);
                }
            }

            categories = new Category[ categoryIds.length ];
            for ( int i = 0; i < categoryIds.length; i++)
            {
                categories[ i ] = new Category
                (
                    Integer.parseInt( categoryIds[i] ),
                    categoryTitles[i],
                    categoryItems[i]
                );
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
    }
