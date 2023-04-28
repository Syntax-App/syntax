package edu.brown.cs.student.main.csv.creators;

import java.util.ArrayList;
import java.util.List;

/** ListCreator class. Creates a list of strings from a row. */
public class ListCreator implements CreatorFromRow<List<String>> {

  /** ListCreator constructor. */
  public ListCreator() {}

  /**
   * Simply returns the list of strings as list of strings.
   *
   * @param row - list of strings to be returned
   * @return - the given row
   * @throws FactoryFailureException - not thrown since any String row is valid
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return new ArrayList<>(row);
  }
}
