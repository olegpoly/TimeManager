package com.github.olegpoly.TimeManager.DataBaseExporter.TypeAdapters;

import com.github.olegpoly.TimeManager.TImeManagerDataBase.TableEntry.ActionDBEntry;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Converts UserActivityDBTableEntry objects to and from JSON.
 */
public class ActionAdapter extends TypeAdapter<ActionDBEntry> {
    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, ActionDBEntry value) throws IOException {
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
    public ActionDBEntry read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        String activityName = in.nextString();
        return new ActionDBEntry(activityName);
    }
}