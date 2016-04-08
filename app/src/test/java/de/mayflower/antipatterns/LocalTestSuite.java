package de.mayflower.antipatterns;
import  de.mayflower.antipatterns.data.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                AntiPatternsPatternCountServiceTest.class,
                AntiPatternsMapUtilTest.class,
                PatternTest.class
        })
public class LocalTestSuite {}
