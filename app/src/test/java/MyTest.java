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

public class MyTest {

    @Test
    public void MyTest_simpleTest_ReturnsTrue() {
        assertThat(true, is(true));
    }

    @Test
    public void MyTest_mapUtilTest_sortsIntegers() {
        Map<Integer, Integer> unsorted = new LinkedHashMap<Integer, Integer>();
        Map<Integer, Integer> sorted = new LinkedHashMap<Integer, Integer>();
        Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
        unsorted.put(1,3);
        unsorted.put(2,1);
        unsorted.put(3,2);
        sorted.put(2,1);
        sorted.put(3,2);
        sorted.put(1,3);

        result = AntiPatternsMapUtil.sortByValue(unsorted, AntiPatternsMapUtil.SORT_ORDER.ASC);
        boolean foo = result.equals(sorted);
        // System.out.print("Result is "+result.toString());

        assertTrue(foo);

    }

}