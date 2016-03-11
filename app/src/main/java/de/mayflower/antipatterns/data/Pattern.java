
    package de.mayflower.antipatterns.data;

    public class Pattern
    {
        private             Integer         id                  = null;
        private             String          name                = null;
        private             String[]        symptomps           = null;
        private             String[]        remedies            = null;
        private             Integer         counter             = null;

        public Pattern( Integer id, String name, String[] symptomps, String[] remedies, Integer counter )
        {
            this.id        = id;
            this.name      = name;
            this.symptomps = symptomps;
            this.remedies  = remedies;
            this.counter   = counter;
        }

        public Integer getId() { return id; }

        public String getName() { return name; }

        public String[] getSymptomps()
        {
            return symptomps;
        }

        public String[] getRemedies()
        {
            return remedies;
        }

        public Integer getCounter() { return counter; }

        public String getNameWithCounter() { return name +  " (" + counter  + ")"; }
    }
