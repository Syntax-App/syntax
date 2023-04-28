package edu.brown.cs.student.main.server;

import com.squareup.moshi.Moshi;

import java.util.Map;

/**
 * Class with a static helper method, which helps with serialization.
 */
public class SerializeHelper {

    /**
     * Serializes a Map into a JSON-formatted String.
     * @param toSerialize - A map of String to Objects to serialize, i.e.,
     *                    transform into a JSON formatted String
     * @return a JSON-formatted String representing the Map.
     */
    public static String helpSerialize(Map<String, Object> toSerialize) {
        try {
            Moshi moshi = new Moshi.Builder()
                    .build();
            return moshi.adapter(Map.class).toJson(toSerialize);
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
