package edu.brown.cs.student.main.csv.objectTypes;

/** Person object class. */
public class Person {
  String name;
  String major;
  String age;

  /**
   * Person constructor. Takes in a name, major, and age.
   *
   * @param name - name of the Person object
   * @param major - major of the Person object
   * @param age - age of the Person object
   */
  public Person(String name, String major, String age) {
    this.name = name;
    this.major = major;
    this.age = age;
  }

  /**
   * Getter for the name field. For testing purposes.
   *
   * @return - the name field
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter for the major field. For testing purposes.
   *
   * @return - the major field
   */
  public String getMajor() {
    return this.major;
  }

  /**
   * Getter for the age field. For testing purposes.
   *
   * @return - the age field
   */
  public String getAge() {
    return this.age;
  }
}
