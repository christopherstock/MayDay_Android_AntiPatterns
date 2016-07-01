package de.mayflower.antipatterns.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.mayflower.antipatterns.AntiPatternsMainScreen;
import de.mayflower.antipatterns.AntiPatternsPatternCountService;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class AntiPatternsPatternCountServiceTest extends ActivityInstrumentationTestCase2<AntiPatternsMainScreen>
{
    private AntiPatternsMainScreen myActivity;
    private SharedPreferences mySharedPreferences;
    AntiPatternsPatternCountService antiPatternsPatternCountService;
    private Integer fixturePatternId = 313370815;
    private Integer fixtureSeedValue = 4711;

    public AntiPatternsPatternCountServiceTest()
    {
        super(AntiPatternsMainScreen.class);
    }

    @Before
    public void setUp() throws Exception{
        try {
            super.setUp();
        } catch(Exception e) {
            System.out.print("complain loudly");
            throw e;
        }
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        myActivity = getActivity();
        setUpSharedPreferences();
        antiPatternsPatternCountService = new AntiPatternsPatternCountService();
        antiPatternsPatternCountService.init(myActivity);
    }


    @Test
    public void readCounter_ReturnsExpectedValue()
    {
        Integer expectedCounter = antiPatternsPatternCountService.readCounter(fixturePatternId);

        assertEquals(expectedCounter, fixtureSeedValue);
    }

    @Test
    public void resetCounter_clearsValue()
    {
        antiPatternsPatternCountService.resetCounter(fixturePatternId);
        Integer expectedCounter = antiPatternsPatternCountService.readCounter(fixturePatternId);
        assertEquals(expectedCounter, (Integer) 0);

    }

    private void setUpSharedPreferences() {
        mySharedPreferences = myActivity.getSharedPreferences(AntiPatternsPatternCountService.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(fixturePatternId.toString(), fixtureSeedValue);
        editor.commit();
    }
}
