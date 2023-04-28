package edu.brown.cs.student.main.csv.creators;

import edu.brown.cs.student.main.csv.objectTypes.Star;
import java.util.List;

/** StarCreator class with a create() method to turn rows into Star objects. */
public class StarCreator implements CreatorFromRow<Star> {

  /** StarCreator constructor. */
  public StarCreator() {}

  /**
   * Creates a Star object from a list of strings.
   *
   * @param row - the list of strings form Star object from
   * @return - a Star object
   * @throws FactoryFailureException - if the input row is invalid (too many or few elements)
   */
  @Override
  public Star create(List<String> row) throws FactoryFailureException {
    // check row size
    if (row.size() < 5) {
      throw new FactoryFailureException(
          "Too few columns in the dataset to create Star object.", row);
    } else if (row.size() > 5) {
      throw new FactoryFailureException(
          "Too many columns in the dataset to create Star object.", row);
    }

    // parse variables
    int starID;
    double x;
    double y;
    double z;
    String properName = row.get(1);

    try {
      starID = Integer.parseInt(row.get(0));
      x = Double.parseDouble(row.get(2));
      y = Double.parseDouble(row.get(3));
      z = Double.parseDouble(row.get(4));
    } catch (NullPointerException | NumberFormatException e) {
      throw new FactoryFailureException("Row contains incorrect datatype for Star object.", row);
    }

    // return new Star object
    return new Star(starID, properName, x, y, z);
  }
}
