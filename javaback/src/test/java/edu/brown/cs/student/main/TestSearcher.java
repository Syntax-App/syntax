package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csv.creators.ListCreator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import edu.brown.cs.student.main.csv.Parser;
import edu.brown.cs.student.main.csv.Searcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Testing suite for Searcher */
public class TestSearcher {

  /**
   * Tests basic search on name data with header row and column ID
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testHeaderAndColID() throws FileNotFoundException {
    Reader file = new FileReader("data/persons/names-with-header.csv");
    Parser<List<String>> parser = new Parser<>(file, new ListCreator(), true);

    // with header & column name
    Searcher searcher =
        new Searcher(parser.getContent(), "computer science", parser.getHeader(), "1");
    List<List<String>> expected =
        List.of(
            List.of("ben", "computer science", "19"),
            List.of("ken", "computer science", "22"),
            List.of("andrew", "computer science", "18"));
    assertEquals(searcher.search(), expected);

    searcher = new Searcher(parser.getContent(), "computer science", parser.getHeader(), "0");
    expected = List.of(); // should be empty
    assertEquals(searcher.search(), expected);

    // with header & column index
    searcher =
        new Searcher(parser.getContent(), "computer science", parser.getHeader(), "concentration");
    expected =
        List.of(
            List.of("ben", "computer science", "19"),
            List.of("ken", "computer science", "22"),
            List.of("andrew", "computer science", "18"));
    assertEquals(searcher.search(), expected);

    searcher = new Searcher(parser.getContent(), "computer science", parser.getHeader(), "name");
    expected = List.of(); // should be empty
    assertEquals(searcher.search(), expected);
  }

  /**
   * Tests basic search on star data
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testStarSearch() throws FileNotFoundException {
    // search through stars (with header & column name)
    Reader file = new FileReader("data/stars/stardata.csv");
    Parser<List<String>> starParser = new Parser<>(file, new ListCreator(), true);

    Searcher searcher =
        new Searcher(starParser.getContent(), "Channie_10", starParser.getHeader(), "1");
    List<List<String>> result = searcher.search();
    List<List<String>> expected =
        List.of(List.of("67891", "Channie_10", "-119.99121", "-66.93475", "39.11535"));

    assertEquals(result, expected);
  }

  /**
   * Tests search on basic data with no header row
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testNoHeaderNoColID() throws FileNotFoundException {
    Reader file = new FileReader("data/persons/names-no-header.csv");
    Parser<List<String>> parser = new Parser<>(file, new ListCreator(), false);

    // with no header or column ID
    Searcher searcher = new Searcher(parser.getContent(), "computer science", parser.getHeader());
    List<List<String>> expected =
        List.of(
            List.of("ben", "computer science", "19"),
            List.of("ken", "computer science", "22"),
            List.of("andrew", "computer science", "18"));
    assertEquals(searcher.search(), expected);
  }

  /**
   * Tests search with no header row but with column ID
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testNoHeaderWithColID() throws FileNotFoundException {
    Reader file = new FileReader("data/persons/names-no-header.csv");
    Parser<List<String>> parser = new Parser<>(file, new ListCreator(), false);

    // with no header & with valid column index
    Searcher searcher =
        new Searcher(parser.getContent(), "computer science", parser.getHeader(), "1");
    List<List<String>> expected =
        List.of(
            List.of("ben", "computer science", "19"),
            List.of("ken", "computer science", "22"),
            List.of("andrew", "computer science", "18"));
    assertEquals(searcher.search(), expected);

    // value not in the specified column
    searcher = new Searcher(parser.getContent(), "computer science", parser.getHeader(), "0");
    expected = List.of(); // should be empty
    assertEquals(searcher.search(), expected);
  }

  /**
   * Tests search with invalid column ID
   *
   * @throws FileNotFoundException - if FileReader fails to find given file
   */
  @Test
  public void testInvalidColumn() throws FileNotFoundException {
    // with header and invalid column ID
    Reader file = new FileReader("data/persons/names-with-header.csv");
    Parser<List<String>> clientParser = new Parser<>(file, new ListCreator(), true);

    Searcher searcher =
        new Searcher(
            clientParser.getContent(), "computer science", clientParser.getHeader(), "pronouns");
    assertThrows(IllegalArgumentException.class, searcher::search);

    // without header & with invalid column index
    file = new FileReader("data/persons/names-no-header.csv");
    clientParser = new Parser<>(file, new ListCreator(), false);

    searcher =
        new Searcher(clientParser.getContent(), "computer science", clientParser.getHeader(), "3");
    assertThrows(IllegalArgumentException.class, searcher::search);

    // without header & with (nonexistent) column name
    searcher =
        new Searcher(
            clientParser.getContent(), "computer science", clientParser.getHeader(), "name");
    assertThrows(IllegalArgumentException.class, searcher::search);
  }
}
