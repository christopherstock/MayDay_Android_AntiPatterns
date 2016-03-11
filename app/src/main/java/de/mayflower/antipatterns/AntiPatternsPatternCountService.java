    package de.mayflower.antipatterns;

    import android.app.Activity;
    import android.content.Context;
    import android.content.SharedPreferences;

    import java.util.Iterator;
    import java.util.LinkedHashMap;
    import java.util.Map;

    import de.mayflower.antipatterns.data.Pattern;

    public class AntiPatternsPatternCountService
    {
        public final static Integer TOP_10_CATEGORY_ID = 5;

        private SharedPreferences sharedPreferences = null;

        public void init(Activity activity)
        {
            this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        }

        public void incrementCounter(Integer patternId)
        {
            int patternCount = this.readCounter(patternId);

            patternCount += 1;

            this.editIntegerValue(patternId.toString(), patternCount);
        }

        public void resetCounter(Integer patternId)
        {
            this.editIntegerValue(patternId.toString(), 0);
        }

        public void resetAllCounters()
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }

        public int readCounter(Integer patternId)
        {
            return sharedPreferences.getInt(patternId.toString(), 0);
        }

        public Integer[] getSortedTopPatternIdList(int top, Pattern[] patternList)
        {
            Map counters = sharedPreferences.getAll();

            counters = AntiPatternsMapUtil.sortByValue(counters, AntiPatternsMapUtil.SORT_ORDER.DESC);

            if (top > counters.size()) {
                top = counters.size();
            }

            Integer[] sortedPatternIdList = new Integer[top];

            Iterator entries = counters.entrySet().iterator();

            int topCounter = 0;

            while (entries.hasNext()) {
                LinkedHashMap.Entry entry = (LinkedHashMap.Entry) entries.next();
                
                int key = Integer.parseInt(entry.getKey().toString());
                
                sortedPatternIdList[topCounter] = Integer.valueOf(key);

                topCounter += 1;

                if (topCounter == top) {
                    break;
                }
            }

            return sortedPatternIdList;
        }

        private void editIntegerValue(String key, int value)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }
