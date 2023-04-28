package edu.brown.cs.student.main.csv.objectTypes;

/** Star object class. */
public class Star {
  int starID;
  String properName;
  double x;
  double y;
  double z;

  /**
   * Star constructor. Takes in a star ID, name, and x,y,z of the Star object.
   *
   * @param starID - star ID
   * @param properName - name of star
   * @param x - x coord
   * @param y - y coord
   * @param z - z coord
   */
  public Star(int starID, String properName, double x, double y, double z) {
    this.starID = starID;
    this.properName = properName;
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
