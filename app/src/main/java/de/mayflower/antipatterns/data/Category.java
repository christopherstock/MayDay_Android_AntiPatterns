
    package de.mayflower.antipatterns.data;

    public class Category
    {
        private     Integer         id                  = null;
        private     String          name                = null;
        private     Integer[]       patterns            = null;

        public Category( Integer id, String name, Integer[] patterns )
        {
            this.id       = id;
            this.name     = name;
            this.patterns = patterns;
        }

        public String getName()
        {
            return name;
        }

        public Integer[] getPatterns()
        {
            return patterns;
        }

        public Integer getId()
        {
            return id;
        }
    }
