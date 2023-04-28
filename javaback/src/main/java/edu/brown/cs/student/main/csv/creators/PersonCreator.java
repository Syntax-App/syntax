package edu.brown.cs.student.main.csv.creators;

import edu.brown.cs.student.main.csv.objectTypes.Person;
import java.util.List;

/** PersonCreator class with a create() method to turn rows into Person objects. */
public class PersonCreator implements CreatorFromRow<Person> {

  /** PersonCreator constructor. */
  public PersonCreator() {}

  /**
   * Creates a Person object from a list of strings.
   *
   * @param row - the list of strings form Person object from
   * @return - a Person object
   * @throws FactoryFailureException - if the input row is invalid (too many or few elements)
   */
  @Override
  public Person create(List<String> row) throws FactoryFailureException {
    // check row size
    if (row.size() < 3) {
      throw new FactoryFailureException(
          "Too few columns in the dataset to create Person object.", row);
    } else if (row.size() > 3) {
      throw new FactoryFailureException(
          "Too many columns in the dataset to create Person object.", row);
    }

    // return new Person object
    return new Person(row.get(0), row.get(1), row.get(2));
  }
}
