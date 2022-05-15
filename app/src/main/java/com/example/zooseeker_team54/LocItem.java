package com.example.zooseeker_team54;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Class to represent our Location Items and to be able to store and modify them in containers of
 * our choosing.
 */
@Entity(tableName = "loc_items")
public class LocItem implements Serializable {

    // TODO: change the type of kind to Kind
    public static enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION
    }

    @PrimaryKey(autoGenerate = true)
    public long pk_id;

    // member variables of LocItem
    @NonNull
    public String name, kind, id;
    public boolean planned, visited;
    public List<String> tags;
    public double currDist;

    /**
     * Gets the representation of the LocItem as a string
     * @return a String representation of the LocItem with its member variables
     */
    @NonNull
    @Override
    public String toString() {
        return "Loc {" +
                "id=" + id +
                ", name='" + name +
                "', planned=" + planned +
                ", visited=" + visited +
                ", current distance=" + currDist +
                '}';
    }

    /**
     * Constructor for the LocItem
     * @param name  name of the LocItem
     * @param id    id for the LocItem
     * @param kind  the kind of LocItem it is
     * @param tags  List of tags for the LocItem
     */
    LocItem(@NonNull String name, String id, String kind, List<String> tags) {
        this.name = name;
        this.kind = kind;
        this.id = id;
        this.tags = tags;
        this.planned = false;
        this.visited = false;
        this.currDist = 0;
    }

    /**
     * Getter method for the current distance as a string
     * @return  current distance of the LocItem
     */
    public String getCurrDist() {
        return String.format("- %.0f meters", currDist);
    }

    /**
     * Return a list of LocItems loaded from a JSON file
     * @param context   given context
     * @param path      path to JSON file to be loaded
     * @return          a list of LocItems
     */
    public static List<LocItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<LocItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
