
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
        private     static          Integer             current             = 0;

        public static void hydrate(Context context, AntiPatternsPatternCountService countService)
        {
            hydrateCategories(context);
            hydratePatterns(context, countService);
        }

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
                Integer id = Integer.parseInt( categoryIds[i] );
                categories[ i ] = new Category
                (
                    id,
                    categoryTitles[i],
                    categoryItems[i]
                );
            }

        }

        public static void hydratePatterns( Context context, AntiPatternsPatternCountService countService )
        {
            Integer[] simulatedPatternIds = getPatternIds();

            Hashtable<Integer, String>   patternTitles   = new Hashtable<Integer, String>();
            Hashtable<Integer, String[]> patternSymptoms = new Hashtable<Integer, String[]>();
            Hashtable<Integer, String[]> patternRemedies = new Hashtable<Integer, String[]>();

            patterns = new Pattern[simulatedPatternIds.length];
            for ( Integer simulatedParentId : simulatedPatternIds )
            {
                String title = LibResource.getResourceString(
                    context,
                    "antipattern_" + simulatedParentId + "_title"
                );

                String[] symptoms = LibResource.getResourceStringArray(
                        context,
                        "antipattern_" + simulatedParentId + "_symptoms"
                );

                String[] remedies = LibResource.getResourceStringArray(
                        context,
                        "antipattern_" + simulatedParentId + "_remedies"
                );
                patterns[simulatedParentId] = new Pattern(
                        simulatedParentId,
                        title,
                        symptoms,
                        remedies,
                        countService.readCounter(simulatedParentId)
                );

                AntiPatternsDebug.major.out(" >> AP title [" + title + "]");
                for ( String s : symptoms )
                {
                    AntiPatternsDebug.major.out(" >> AP problem [" + s + "]");
                }
                for ( String s : remedies )
                {
                    AntiPatternsDebug.major.out(" >> AP remedy [" + s + "]");
                }
            }
        }

        private static Integer[] getPatternIds()
        {
            Hashtable<Integer, Integer> patternIds = new Hashtable<Integer, Integer>();
            for ( int i=0; i < categories.length; i++ ) {
                Integer[] pats = categories[i].getPatterns();
                for ( int j=0; j < pats.length; j++ ) {
                    patternIds.put(pats[j], pats[j]);
                }
            }

            return patternIds.values().toArray(new Integer[]{});
        }

        public static Pattern getCurrentPattern()
        {
            if ( null == current ) {
                current = 0;
            }
            if ( null != patterns) {
                return patterns[current];
            } else {
                return null;
            }

        }
        public static void setCurrent(Integer current)
        {
            AntiPatternsHydrator.current = current;
        }
    }
