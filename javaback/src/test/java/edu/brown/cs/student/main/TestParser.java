package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csv.creators.ListCreator;
import edu.brown.cs.student.main.csv.creators.PersonCreator;
import edu.brown.cs.student.main.csv.creators.StarCreator;
import edu.brown.cs.student.main.csv.objectTypes.Person;
import edu.brown.cs.student.main.csv.objectTypes.Star;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.main.csv.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Test suite for Parser */
public class TestParser {

  /**
   * tests basic parsing functionality
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testParseLines() throws FileNotFoundException {
    Reader file = new FileReader("data/persons/names-with-header.csv");
    Parser<List<String>> parser = new Parser<>(file, new ListCreator(), true);

    List<List<String>> nameList =
        List.of(
            List.of("ben", "computer science", "19"),
            List.of("alice", "english", "20"),
            List.of("hannah", "apma", "18"),
            List.of("sawyer", "architecture", "22"),
            List.of("ken", "computer science", "22"),
            List.of("haya", "apma", "20"),
            List.of("andrew", "computer science", "18"));
    assertEquals(parser.getContent(), nameList);
  }

  /**
   * tests parsing empty csv files
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testParseEmpty() throws FileNotFoundException {
    Reader file = new FileReader("data/empty.csv");
    Parser<List<String>> parser = new Parser<>(file, new ListCreator(), false);

    List<List<String>> emptyList = List.of();
    assertEquals(parser.getContent(), emptyList);
  }

  /**
   * tests the getHeader() method in Parser.
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testGetHeader() throws FileNotFoundException {
    // with header row
    Reader file = new FileReader("data/persons/names-with-header.csv");
    Parser<List<String>> parser = new Parser<>(file, new ListCreator(), true);

    List<String> header = List.of("name", "concentration", "age");
    assertEquals(parser.getHeader(), header);

    // no header row
    parser = new Parser<>(file, new ListCreator(), false);
    assertEquals(new ArrayList<>(), parser.getHeader());
  }

  /** Tests parsing with a StringReader instead of FileReader */
  @Test
  public void testStringReader() {
    Reader string =
        new StringReader(
            "\nben,computer science,19\nalice,english,20\nhannah,apma,18\n"
                + "sawyer,architecture,22\nken,computer science,22\nhaya,apma,20\nandrew,computer science,18");
    Parser<List<String>> parser = new Parser<>(string, new ListCreator(), true);

    List<List<String>> nameList =
        List.of(
            List.of("ben", "computer science", "19"),
            List.of("alice", "english", "20"),
            List.of("hannah", "apma", "18"),
            List.of("sawyer", "architecture", "22"),
            List.of("ken", "computer science", "22"),
            List.of("haya", "apma", "20"),
            List.of("andrew", "computer science", "18"));

    assertEquals(parser.getContent(), nameList);
  }

  /**
   * parses a csv into Person objects
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testParsePerson() throws FileNotFoundException {
    Reader file = new FileReader("data/persons/names-with-header.csv");
    Parser<Person> parser = new Parser<>(file, new PersonCreator(), true);

    List<Person> expected =
        List.of(
            new Person("ben", "computer science", "19"),
            new Person("alice", "english", "20"),
            new Person("hannah", "apma", "18"),
            new Person("sawyer", "architecture", "22"),
            new Person("ken", "computer science", "22"),
            new Person("haya", "apma", "20"),
            new Person("andrew", "computer science", "18"));

    List<Person> result = parser.getContent();

    for (int i = 0; i < result.size(); i++) {
      assertEquals(result.get(i).getName(), expected.get(i).getName());
      assertEquals(result.get(i).getAge(), expected.get(i).getAge());
      assertEquals(result.get(i).getMajor(), expected.get(i).getMajor());
    }
  }

  /**
   * tests parsing a csv into Star objects.
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testParseStar() throws FileNotFoundException {
    Reader file = new FileReader("data/stars/star-part-invalid.csv");
    Parser<Star> parser = new Parser<>(file, new StarCreator(), true);

    List<Star> expected =
        List.of(
            new Star(1, "", 282.43485, 0.00449, 5.36884),
            new Star(3, "", 277.11358, 0.02422, 223.27753));

    List<Star> result = parser.getContent();
    assertEquals(result.size(), expected.size());
  }

  /**
   * tests FactoryFailureException on all rows.
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testFactoryFailure() throws FileNotFoundException {
    Reader file = new FileReader("data/persons/names-invalid.csv");
    Parser<Person> parser = new Parser<>(file, new PersonCreator(), true);
    List<List<String>> expected = List.of();

    assertEquals(parser.getContent(), expected);
  }

  /**
   * tests Factory Failure on some parts of the CSV. (Ill formated CSV)
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testPartialFactFailure() throws FileNotFoundException {
    Reader file = new FileReader("data/persons/names-part-invalid.csv");
    Parser<Person> parser = new Parser<>(file, new PersonCreator(), true);

    List<Person> expected =
        List.of(
            new Person("ben", "computer science", "19"),
            new Person("hannah", "apma", "18"),
            new Person("ken", "computer science", "22"),
            new Person("andrew", "computer science", "18"));

    List<Person> result = parser.getContent();

    assertEquals(result.size(), expected.size());

    for (int i = 0; i < result.size(); i++) {
      assertEquals(result.get(i).getName(), expected.get(i).getName());
      assertEquals(result.get(i).getAge(), expected.get(i).getAge());
      assertEquals(result.get(i).getMajor(), expected.get(i).getMajor());
    }
  }
}
