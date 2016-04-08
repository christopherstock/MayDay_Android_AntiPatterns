import org.junit.Test;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MyTest {

    @Test
    public void MyTest_simpleTest_ReturnsTrue() {
        assertThat(true, is(true));
    }
}