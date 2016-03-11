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

        public Pattern[] getSortedTopPatternList(int top, Pattern[] patternList)
        {
            Map counters = sharedPreferences.getAll();

            counters = AntiPatternsMapUtil.sortByValue(counters, AntiPatternsMapUtil.SORT_ORDER.DESC);

            if (top > counters.size()) {
                top = counters.size();
            }

            Pattern[] sortedPatternList = new Pattern[top];

            Iterator entries = counters.entrySet().iterator();

            int topCounter = 0;

            while (entries.hasNext()) {
                LinkedHashMap.Entry entry = (LinkedHashMap.Entry) entries.next();

                for (Pattern pattern: patternList) {
                    int key = Integer.parseInt(entry.getKey().toString());

                    if (Integer.valueOf(key).compareTo(pattern.getId()) < 0) {
                        sortedPatternList[topCounter] = pattern;
                        break;
                    }
                }

                topCounter += 1;

                if (topCounter == top) {
                    break;
                }
            }

            return sortedPatternList;
        }

        private void editIntegerValue(String key, int value)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }
