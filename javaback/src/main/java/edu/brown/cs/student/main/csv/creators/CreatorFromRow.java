package edu.brown.cs.student.main.csv.creators;

import java.util.List;

/**
 * Interface for developers to implement in their creator classes. Should be passed in the return
 * type they desire as output of Parser as parameter T.
 *
 * @param <T> - the desired return type from Parser
 */
public interface CreatorFromRow<T> {

  /**
   * Creates the desired return object from a single row of strings.
   *
   * @param row - list of strings to create an object from
   * @return - output object of desired type
   * @throws FactoryFailureException - if the input row is invalid in any way
   */
  T create(List<String> row) throws FactoryFailureException;
}
