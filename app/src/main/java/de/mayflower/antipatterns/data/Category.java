
    package de.mayflower.antipatterns.data;

    public class Category
    {
        private String name;
        private Pattern[] patterns;

        public Category(String name, Pattern[] patterns)
        {
            this.name     = name;
            this.patterns = patterns;
        }

        public String getName() {
            return name;
        }
        public Pattern[] getPatterns() {
            return patterns;
        }
    }
    
