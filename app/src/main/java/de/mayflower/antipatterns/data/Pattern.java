
    package de.mayflower.antipatterns.data;

    public class Pattern
    {
        private Integer id;
        private String name;
        private String[] problems;
        private String[] remedies;

        public Pattern(Integer id, String name, String[] problems, String[] remedies)
        {
            this.id       = id;
            this.name     = name;
            this.problems = problems;
            this.remedies = remedies;
        }

        public String getName() {
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
    }

