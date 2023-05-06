package edu.brown.cs.student.main.algo.snippets;

import com.squareup.moshi.Json;
import java.util.Map;

public class Snippets {
    // top level; represents whole GeoJSON
    public record SnippetsJSON(
        @Json(name = "array") Snippet[] array
        ){}

    // represents a Feature
    public record Snippet(
        @Json(name = "text") String text,
        @Json(name = "difficulty") double difficulty
    ){}
}
