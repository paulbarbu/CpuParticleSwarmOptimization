package main;

/**
 * Add the Scala Library (org.scala-lang.scala-library_2.11.8.v20160304-115712-1706a37eb8)
 * to Build Path as External JAR (see the eclipse/plugins) directory for the scala-library.jar file
 * File - Export
 * Choose this file as Main
 * 
 * Then in the CLI:
 * java -jar cpuPso.jar
 * or
 * java -jar cpuPso.jar --psatsim-name psatsim_con --psatsim-path $(pwd)/psatsim/ --swarm-size 11 --max-iterations 11 --archive-size 100 
 * 
 * http://stackoverflow.com/questions/11413888/exporting-scala-project-as-jar-from-eclipse
 */
public class Main { 
    public static void main (String[] args) {    	
    	App.main(args);
    }
}
