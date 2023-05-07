package edu.brown.cs.student.main.algo.snippets;

import com.squareup.moshi.Json;

/**
 * This class stores the record representing SnippetsJSONs and their Snippets
 */
public class Snippets {
    // top level; represents whole SnippetJSON, an array of Snippets
    public record SnippetsJSON(
        @Json(name = "array") Snippet[] array
        ){}

    // each snippet has its own text and difficulty
    public record Snippet(
        @Json(name = "text") String text,
        @Json(name = "difficulty") double difficulty
    ){}
}
