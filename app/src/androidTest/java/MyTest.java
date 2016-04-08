import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MyTest {


    @Before
    public void createLogHistory() {
        System.out.print("Starting Tests...");
    }

    @Test
    public void MyTest_trueIsTrue()
    {
        System.out.print("True is true");
        assertThat(true, is(true));
    }

}