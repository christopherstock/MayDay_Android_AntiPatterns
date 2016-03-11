
    package de.mayflower.antipatterns.data;

    public class Category
    {
        private Integer id;
        private String name;
        private Integer[] patterns;

        public Category(Integer Id, String name, Integer[] patterns)
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
    
