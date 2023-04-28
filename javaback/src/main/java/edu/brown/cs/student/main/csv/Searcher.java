package edu.brown.cs.student.main.csv;

import java.util.ArrayList;
import java.util.List;

/** Searcher class that handles searching through a list of list of strings for a given value. */
public class Searcher {
  List<List<String>> content;
  List<List<String>> matches;
  String value;
  List<String> header;
  String columnID;
  Integer colNum;

  /**
   * Searcher constructor.
   *
   * @param content - content to search through
   * @param value - value to search for
   * @param header - CSV header (null if there is no header)
   */
  public Searcher(List<List<String>> content, String value, List<String> header) {
    this(content, value, header, null);
  }

  /**
   * Searcher constructor with additional option 'columnID'.
   *
   * @param content - content to search through
   * @param value - value to search for
   * @param header - CSV header (null if there is no header)
   * @param columnID - column identification (either a column index or name)
   */
  public Searcher(List<List<String>> content, String value, List<String> header, String columnID) {
    this.content = content;
    this.value = value;
    this.header = header;
    this.columnID = columnID;
    this.matches = new ArrayList<>();
  }

  /**
   * helper to parse the column ID string input.
   *
   * @throws IllegalArgumentException if the input column ID is invalid
   */
  private void parseColID() {
    if (this.columnID != null) {
      if (this.isNumeric(this.columnID)) {
        this.colNum = Integer.parseInt(this.columnID);

        // check if column index is within range
        if (this.content.size() > 0 && this.colNum > this.content.get(0).size() - 1) {
          throw new IllegalArgumentException(
              "The input column does not exist. Please use indices between 0 and " + (this.header.size() - 1));
        }
      } else {
        // check if there is a header row
        if (this.header == null) {
          throw new IllegalArgumentException(
              "The dataset does not have a header row. Please input a column index instead.");
        }

        // find the column index
        this.colNum = this.findIndex(this.columnID);

        // check if column ID was valid
        if (this.colNum == null) {
          throw new IllegalArgumentException(
              "The input column does not exist. The valid headers are: " + this.header);
        }
      }
    }
  }

  /**
   * returns the index of the given column header by searching through the header row.
   *
   * @param colName - column name to find the index for
   * @return column index if found, otherwise null
   */
  private Integer findIndex(String colName) {
    // loop through the headers for a match
    for (int i = 0; i < this.header.size(); i++) {
      if (this.header.get(i).equals(colName)) {
        return i;
      }
    }
    // if there's no match
    return null;
  }

  /**
   * helper function to determine whether a string is numerical or not Code borrowed from
   * https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
   *
   * @param string - string to determine whether it is a number or not
   * @return - true if input string is numeric, false otherwise
   */
  public boolean isNumeric(String string) {
    try {
      Integer.parseInt(string);
    } catch (NumberFormatException | NullPointerException e) {
      return false;
    }

    return true;
  }

  /**
   * searches through the content for the search value.
   *
   * @return - a list of rows that contains the search value in the specified column
   */
  public List<List<String>> search() {
    // first parse the column ID (index or header)
    this.parseColID();

    // loop through the content's rows and check for search matches
    for (List<String> row : this.content) {
      for (int j = 0; j < row.size(); j++) {

        // check if the string matches search value and print row if true
        if (row.get(j).equals(this.value)) {
          // if the column is not the specified column
          if (this.colNum != null && this.colNum != j) {
            continue;
          }
          // if the row has a match and is in the specified column (or no col is specified)
          this.matches.add(row);
          break;
        }
      }
    }

    return this.matches;
  }
}
