
    package de.mayflower.antipatterns.data;

    public class Pattern
    {
        private             Integer         id                  = null;
        private             String          name                = null;
        private             String[]        problems            = null;
        private             String[]        remedies            = null;
        private             Integer         counter             = null;

        public Pattern( Integer id, String name, String[] problems, String[] remedies, Integer counter )
        {
            this.id       = id;
            this.name     = name;
            this.problems = problems;
            this.remedies = remedies;
            this.counter  = counter;
        }

        public String getName()
        {
            return name;
        }

        public String[] getProblems()
        {
            return problems;
        }

        public String[] getRemedies()
        {
            return remedies;
        }

        public Integer getCounter()
        {
            return counter;
        }
    }
