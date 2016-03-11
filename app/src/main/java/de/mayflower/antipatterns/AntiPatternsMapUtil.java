    package de.mayflower.antipatterns;

    import java.util.Collections;
    import java.util.Comparator;
    import java.util.LinkedHashMap;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.Map;

    public class AntiPatternsMapUtil
    {
        public final static int SORT_ASC = 0;
        public final static int SORT_DESC = 1;

        public static enum SORT_ORDER {
            ASC,
            DESC
        }

        public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map, final SORT_ORDER sortOrder)
        {
            List<Map.Entry<K, V>> list =
                    new LinkedList<Map.Entry<K, V>>( map.entrySet() );

            Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    if (sortOrder == SORT_ORDER.ASC) {
                        return (o1.getValue()).compareTo(o2.getValue());
                    } else {
                        return (o2.getValue()).compareTo(o1.getValue());
                    }
                }
            });

            Map<K, V> result = new LinkedHashMap<K, V>();

            for (Map.Entry<K, V> entry : list) {
                result.put( entry.getKey(), entry.getValue() );
            }

            return result;
        }
    }
