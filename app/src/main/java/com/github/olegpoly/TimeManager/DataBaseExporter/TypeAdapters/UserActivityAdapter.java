package com.github.olegpoly.TimeManager.DataBaseExporter.TypeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.UserActivityDBEntry;

/**
 * Converts UserActivityDBTableEntry objects to and from JSON.
 */
public class UserActivityAdapter extends TypeAdapter<UserActivityDBEntry> {
    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, UserActivityDBEntry value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        String json = value.getActivityName();
        out.value(json);
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     * @param in
     * @return the converted Java object. May be null.
     */
    @Override
    public UserActivityDBEntry read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        String activityName = in.nextString();
        return new UserActivityDBEntry(activityName);
    }
}
