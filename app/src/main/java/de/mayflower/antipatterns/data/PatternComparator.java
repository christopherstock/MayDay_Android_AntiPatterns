package de.mayflower.antipatterns.data;

import java.util.Comparator;

public class PatternComparator implements Comparator<Pattern>
{
    @Override
    public int compare(Pattern pattern, Pattern t1) {

        int counterDiff = pattern.getCounter() - t1.getCounter();

        if (counterDiff == 0) {
            return pattern.getId() - t1.getId();
        } else {
            return counterDiff;
        }
    }
}
