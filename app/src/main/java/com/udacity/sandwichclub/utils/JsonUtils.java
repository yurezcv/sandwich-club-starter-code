package com.udacity.sandwichclub.utils;

import android.util.JsonReader;
import android.util.JsonToken;

import com.udacity.sandwichclub.model.Sandwich;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws IOException {
        Sandwich sandwich = new Sandwich();

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String jsonName = jsonReader.nextName();
            switch (jsonName) {
                case "name":
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String innerJsonName =jsonReader.nextName();
                        if (innerJsonName.equals("mainName")) {
                            sandwich.setMainName(jsonReader.nextString());
                        } else if (innerJsonName.equals("alsoKnownAs")
                                && jsonReader.peek() != JsonToken.NULL) {
                            sandwich.setAlsoKnownAs(parseJsonArrayIntoList(jsonReader));
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                    break;
                case "ingredients":
                    sandwich.setIngredients(parseJsonArrayIntoList(jsonReader));
                    break;
                case "placeOfOrigin":
                    sandwich.setPlaceOfOrigin(jsonReader.nextString());
                    break;
                case "description":
                    sandwich.setDescription(jsonReader.nextString());
                    break;
                case "image":
                    sandwich.setImage(jsonReader.nextString());
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }

        jsonReader.endObject();
        jsonReader.close();

        return sandwich;
    }

    /* Simple static helper method to parse JSON array of ingredients and alsoKnownAs */
    private static List<String> parseJsonArrayIntoList(JsonReader jsonReader) throws IOException {
        List<String> resultList = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            resultList.add(jsonReader.nextString());
        }
        jsonReader.endArray();

        return resultList;
    }
}
