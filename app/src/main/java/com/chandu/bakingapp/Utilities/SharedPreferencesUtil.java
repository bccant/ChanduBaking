package com.chandu.bakingapp.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtil {
    private static final String RECIPEID = "recipe";
    private static final int defaultID = -1;
    private static final String RECIPE_SP = "recipeSP";

//    public static int getRecipeID(Context context) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return sharedPreferences.getInt(RECIPEID, defaultID);
//    }
//
//    public static void setRecipeid(Context context, int recipeID) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(RECIPEID, recipeID);
//        editor.apply();
//    }

    public static int getRecipeID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECIPE_SP, MODE_PRIVATE);
        return sharedPreferences.getInt(RECIPEID, defaultID);
    }

    public static void setRecipeid(Context context, int recipeID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECIPE_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(RECIPEID, recipeID);
        editor.apply();
    }
}
