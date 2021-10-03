package com.niteshb.mywardrobe.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.niteshb.mywardrobe.models.ItemColorModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ProjectUtils {

    public static ArrayList<ItemColorModel>  getColorList(Context activity){
        String json = null;
        ArrayList<ItemColorModel> itemColorModels = new ArrayList<>();
        try {
            InputStream is = activity.getAssets().open("colors.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int status = is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(json);
            Gson gson = new Gson();
            Type userListType = new TypeToken<ArrayList<ItemColorModel>>() {
            }.getType();
            itemColorModels = gson.fromJson(jsonElement, userListType);
        } catch (JsonParseException | IOException e) {
           System.out.println(e.getLocalizedMessage());
        }
        return itemColorModels;
    }
}
