package de.mayflower.antipatterns.data;

import java.util.Comparator;

public class PatternComparator implements Comparator<Pattern>
{
    @Override
    public int compare(Pattern pattern, Pattern t1) {

        int counterDiff = t1.getCounter() - pattern.getCounter();

        if (counterDiff == 0) {
            return pattern.getId() - t1.getId();
        } else {
            return counterDiff;
        }
    }
}
