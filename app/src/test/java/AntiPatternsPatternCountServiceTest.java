import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.*;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import de.mayflower.antipatterns.AntiPatternsPatternCountService;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AntiPatternsPatternCountServiceTest
{
    @Mock
    Activity activityMock;

    @Mock
    SharedPreferences sharedPreferencesMock;

    AntiPatternsPatternCountService antiPatternsPatternCountService;

    @Test
    public void readCounter_ReturnsExpectedValue()
    {
        Integer actualCounter = new Integer(10);

        initActivityMock();
        initSharedPreferencesMock("2", actualCounter);

        antiPatternsPatternCountService = new AntiPatternsPatternCountService();
        antiPatternsPatternCountService.init(activityMock);

        Integer expectedCounter = antiPatternsPatternCountService.readCounter(2);

        assertEquals(expectedCounter, actualCounter);
    }


    private void initSharedPreferencesMock(String key, Integer returnedCounter)
    {
        when(sharedPreferencesMock.getInt(key, 0)).thenReturn(returnedCounter);
    }

    private void initActivityMock()
    {
        when(activityMock.getPreferences(Context.MODE_PRIVATE)).thenReturn(this.sharedPreferencesMock);
    }

}
