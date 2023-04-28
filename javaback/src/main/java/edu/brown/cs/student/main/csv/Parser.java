package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.csv.creators.CreatorFromRow;
import edu.brown.cs.student.main.csv.creators.FactoryFailureException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parser class that parses any Reader instances of CSV data.
 *
 * @param <T> - the return type of parsed CSV rows
 */
public class Parser<T> {
  Reader csv;
  CreatorFromRow<T> creator;
  boolean hasHeader;
  List<String> header;
  List<T> content;

  /**
   * Parser constructor.
   *
   * @param csv - Reader instance of a CSV file
   * @param creator - CreatorFromRow instance with the method to turn a row into desired return type
   * @param hasHeader - whether the CSV file has a header
   */
  public Parser(Reader csv, CreatorFromRow<T> creator, boolean hasHeader) {
    this.header = new ArrayList<>();
    this.content = new ArrayList<>();
    this.setUp(csv, creator, hasHeader);
    this.parseLines();
  }

  private void setUp(Reader csv, CreatorFromRow<T> creator, boolean hasHeader) {
    this.csv = csv;
    this.creator = creator;
    this.hasHeader = hasHeader;
  }

  /** helper that parses each row of the CSV file and stores them into this.content */
  private void parseLines() {
    try {
      BufferedReader br = new BufferedReader(this.csv);
      String line;
      while ((line = br.readLine()) != null) {
        List<String> row = Arrays.asList(line.split(","));

        // if this row is a header, store it in this.header and cont.
        if (this.hasHeader) {
          this.header = row;
          this.hasHeader = false;
          continue;
        }

        try {
          // otherwise, add to content
          T object = this.creator.create(row);
          this.content.add(object);
        } catch (FactoryFailureException e) {
          System.err.println(row + " was not a valid row: \n>> " + e + "\n");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * getter method for CSV content.
   *
   * @return - content of the CSV file as a list of given object type
   */
  public List<T> getContent() {
    return this.content;
  }


  /**
   * getter method for the header.
   *
   * @return - the header row of the CSV file as a list of Strings
   */
  public List<String> getHeader() {
    return this.header;
  }
}
