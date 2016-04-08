import org.junit.Test;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import de.mayflower.antipatterns.AntiPatternsMapUtil;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import de.mayflower.antipatterns.AntiPatternsMapUtil.SORT_ORDER;

public class MyTest {

    @Test
    public void MyTest_simpleTest_ReturnsTrue() {
        assertThat(true, is(true));
    }

    @Test
    public void MyTest_mapUtilTest_sortsIntegersAsc() {
        Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
        Map<Integer, Integer> sorted =getSorted(SORT_ORDER.ASC);
        Map<Integer, Integer> unsorted =getUnsorted();

        result = AntiPatternsMapUtil.sortByValue(unsorted, SORT_ORDER.ASC);
        boolean expected = result.equals(sorted);
        assertTrue(expected);
    }
    @Test
    public void MyTest_mapUtilTest_sortsIntegersDesc() {
        Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
        Map<Integer, Integer> sorted =getSorted(SORT_ORDER.DESC);
        Map<Integer, Integer> unsorted =getUnsorted();

        result = AntiPatternsMapUtil.sortByValue(unsorted, SORT_ORDER.DESC);
        boolean expected = result.equals(sorted);
        assertTrue(expected);
    }
    private Map<Integer, Integer> getSorted(SORT_ORDER order) {
        Map<Integer, Integer> sorted = new LinkedHashMap<Integer, Integer>();
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
        Map<Integer, Integer> unsorted = new LinkedHashMap<Integer, Integer>();
        unsorted.put(1, 3);
        unsorted.put(2, 1);
        unsorted.put(3,2);
        return unsorted;
    }

}