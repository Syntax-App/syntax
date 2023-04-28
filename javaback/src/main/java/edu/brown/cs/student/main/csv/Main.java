package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.csv.creators.CreatorFromRow;
import edu.brown.cs.student.main.csv.creators.ListCreator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/** The Main class of our project. This is where execution begins. */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    // check number of arguments
    if (args.length < 3) {
      System.err.println("Too few arguments. Usage: ./run filename value hasHeader [columnID]");
      System.exit(1);
    } else if (args.length > 4) {
      System.err.println("Too many arguments. Usage: ./run filename value hasHeader [columnID]");
      System.exit(1);
    }

    try {
      // instantiate parser and parse the given csv file
      CreatorFromRow<List<String>> rowCreator = new ListCreator();
      Parser<List<String>> parser =
          new Parser<>(new FileReader(args[0]), rowCreator, Boolean.parseBoolean(args[2]));
      List<List<String>> content = parser.getContent();

      // instantiate searcher and search through the returned content for given value
      Searcher searcher;
      if (args.length == 4) {
        searcher = new Searcher(content, args[1], parser.getHeader(), args[3]);
      } else {
        searcher = new Searcher(content, args[1], parser.getHeader());
      }
      System.out.println(searcher.search());

    } catch (FileNotFoundException e) {
      // handle FileNotFound
      System.err.println("File was not found. Please input a valid file path.");
      System.exit(1);
    }
  }
}
