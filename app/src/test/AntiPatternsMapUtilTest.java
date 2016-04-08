package de.mayflower.antipatterns.test;

import org.junit.Test;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import java.util.LinkedHashMap;
import java.util.Map;
import de.mayflower.antipatterns.AntiPatternsMapUtil;
import de.mayflower.antipatterns.AntiPatternsMapUtil.SORT_ORDER;

public class AntiPatternsMapUtilTest {
    
    @Test
    public void MyTest_mapUtilTest_sortsIntegersAsc() {
        Map<Integer, Integer> result;
        Map<Integer, Integer> sorted =getSorted(SORT_ORDER.ASC);
        Map<Integer, Integer> unsorted =getUnsorted();

        result = AntiPatternsMapUtil.sortByValue(unsorted, SORT_ORDER.ASC);
        boolean expected = result.equals(sorted);
        assertTrue(expected);
    }
    @Test
    public void MyTest_mapUtilTest_sortsIntegersDesc() {
        Map<Integer, Integer> result;
        Map<Integer, Integer> sorted =getSorted(SORT_ORDER.DESC);
        Map<Integer, Integer> unsorted =getUnsorted();

        result = AntiPatternsMapUtil.sortByValue(unsorted, SORT_ORDER.DESC);
        boolean expected = result.equals(sorted);
        assertTrue(expected);
    }
    private Map<Integer, Integer> getSorted(SORT_ORDER order) {
        Map<Integer, Integer> sorted = new LinkedHashMap<>();
        switch(order) {
            case ASC:
                sorted.put(2,1);
                sorted.put(3,2);
                sorted.put(1,3);
                break;
            case DESC:
                sorted.put(1,3);
                sorted.put(3,2);
                sorted.put(2,1);
                break;
        }
        return sorted;
    }
    private Map<Integer, Integer> getUnsorted() {
        Map<Integer, Integer> unsorted = new LinkedHashMap<>();
        unsorted.put(1,3);
        unsorted.put(2,1);
        unsorted.put(3,2);
        return unsorted;
    }

}