package edu.brown.cs.student.main.csv.creators;

import java.util.ArrayList;
import java.util.List;

/** Exception thrown when a csv row is invalid. */
public class FactoryFailureException extends Exception {
  final List<String> row;

  /**
   * FactoryFailureException constructor. Prints error message and creates a new arraylist in place
   * of the invalid row.
   *
   * @param message - error message to be printed
   * @param row - invalid row
   */
  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }
}
