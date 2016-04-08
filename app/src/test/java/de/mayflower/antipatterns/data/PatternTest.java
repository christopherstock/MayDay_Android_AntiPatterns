
    package de.mayflower.antipatterns.data;

    import org.junit.Assert;
    import org.junit.Test;

    /*****************************************************************************
    *   The test class for {@link de.mayflower.antipatterns.data.Pattern}.
    *
    *   @author     Christopher Stock
    *   @version    1.0
    *****************************************************************************/
    public class PatternTest
    {
        @Test
        public void createNewPattern()
        {
            Pattern p = new Pattern(
                new Integer( 17 ),
                "Chrisy",
                new String[] { "test1", "test2", "test3", },
                new String[] { "test4", "test5", "test6", },
                new Integer( 42 )
            );

            Assert.assertEquals( p.getId(),              new Integer( 17 ) );
            Assert.assertEquals( p.getName(),            "Chrisy" );
            Assert.assertEquals( p.getSymptomps(),       new String[] { "test1", "test2", "test3", } );
            Assert.assertEquals( p.getRemedies(),        new String[] { "test4", "test5", "test6", } );
            Assert.assertEquals( p.getCounter(),         new Integer( 42 ) );
            Assert.assertEquals( p.getNameWithCounter(), "Chrisy (42)" );
        }
    }
