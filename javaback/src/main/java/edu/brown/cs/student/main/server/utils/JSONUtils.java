package edu.brown.cs.student.main.server.utils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * This class represents JSONUtils, which encompasses various utilties and functions
 * for JSON purposes
 */
public class JSONUtils {
    public JSONUtils() {}

    /**
     * Gets Map<String, Object> from a Reader object with JSON-formatted data.
     * @param reader - Reader for JSON-formatted data.
     * @return - A Map<String, Object> representing the JSON data.
     * @throws IOException - in case readerToString throws it.
     */
    public Map<String, Object> getParsedJSON(Reader reader) throws IOException {
        String jsonString = this.readerToString(reader);
        return this.fromJson(jsonString);
    }

    /**
     * Gets Map<String, Object> from a Reader object with JSON-formatted data.
     * @param reader - Reader for JSON-formatted data.
     * @return - A Map<String, Object> representing the JSON data.
     * @throws IOException - in case readerToString throws it.
     */

     public <T> T getParsedJSON(Reader reader, Type classType) throws IOException {
        String jsonString = this.readerToString(reader);
        return this.fromJson(classType, jsonString);
    }

    /**
     * Converts a Reader object to a String.
     * *public for testing purposes*
     * @param reader - the Reader object to convert
     * @return - the String representation of the Reader object
     * @throws IOException - if an error occurs while converting the Reader object to a String
     */
    public String readerToString(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
        br.close();

        return stringBuilder.toString();
    }

    /**
     * Helper to convert a JSON-formatted String to a Map<String, Object>.
     * *public for testing purposes*
     * @param jsonString - string to be converted
     * @return - converted JSON
     * @throws IOException - if Moshi throws an error
     */
    public Map<String, Object> fromJson(String jsonString) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
        return adapter.fromJson(jsonString);
    }

    /**
     * Helper to convert a String to the specified class.
     * *public for testing purposes*
     * @param classType - class type to convert into
     * @param jsonString - string to be converted
     * @param <T> - class to convert into
     * @return - converted JSON
     * @throws IOException - if Moshi throws an error
     */
    public <T> T fromJson(Type classType, String jsonString) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> adapter = moshi.adapter(classType);
        return adapter.fromJson(jsonString);
    }
}
