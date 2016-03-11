    package de.mayflower.antipatterns;

    import android.app.Activity;
    import android.content.Context;
    import android.content.SharedPreferences;

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

        private void editIntegerValue(String key, int value)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }
