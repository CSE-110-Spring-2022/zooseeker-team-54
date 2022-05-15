package com.example.zooseeker_team54;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to help convert JSONs to lists and vice versa
 */
public class Converters {

    /**
     * Convert a JSON to a list
     * @param jsonStr
     * @return a list from the given JSON
     */
    @TypeConverter
    public static List<String> fromJsonToList(String jsonStr) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(jsonStr, listType);
    }

    /**
     * Convert a list to JSON
     * @param list
     * @return a JSON from a given list
     */
    @TypeConverter
    public static String fromListToJson(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
