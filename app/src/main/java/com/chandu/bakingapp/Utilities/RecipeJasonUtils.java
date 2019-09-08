package com.chandu.bakingapp.Utilities;

import android.content.Context;
import android.util.Log;

import com.chandu.bakingapp.cooking.Ingredients;
import com.chandu.bakingapp.cooking.Recipe;
import com.chandu.bakingapp.cooking.RecipeSteps;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeJasonUtils {
    private static final String LOG_TAG = RecipeJasonUtils.class.getSimpleName();
    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param recipesJsonStr JSON response from server
     * @return Array of Strings describing weather data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<Recipe> getRecipesDetailFromJson(Context context, final String recipesJsonStr)
            throws JSONException {
        List<Recipe> recipesList = new ArrayList<>();
        final String API_RESULTS = "results";
        final String API_INGREDIENTS = "ingredients";
        final String API_STEPS = "steps";
        final String API_ID = "id";
        final String API_NAME = "name";
        final String API_SERVINGS = "servings";
        final String API_IMAGE = "image";

        Gson gson = new Gson();
        try {
            JSONArray resultsArray = new JSONArray(recipesJsonStr);

            if (resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    try {
                        Recipe recipeInner = new Recipe();
                        Ingredients[] ingredients;
                        RecipeSteps[] recipeSteps;
                        JSONObject recipeJson = resultsArray.getJSONObject(i);
                        recipeInner.setId(recipeJson.getString(API_ID));
                        recipeInner.setName(recipeJson.getString(API_NAME));
                        recipeInner.setImageUrl(recipeJson.getString(API_IMAGE));
                        recipeInner.setServings(recipeJson.getString(API_SERVINGS));
                        JSONArray ingredientJason = recipeJson.getJSONArray(API_INGREDIENTS);
                        ingredients = gson.fromJson(ingredientJason.toString(), Ingredients[].class);
                        recipeInner.setRecipeIngredients(ingredients);

                        JSONArray recipeStepsJson = recipeJson.getJSONArray(API_STEPS);
                        recipeSteps = gson.fromJson(recipeStepsJson.toString(), RecipeSteps[].class);
                        recipeInner.setRecipeSeq(recipeSteps);

                        recipesList.add(recipeInner);
                    } catch (Exception e) {
                        Log.d("Test", "Exception is " + e);
                        recipesList = null;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error Parsing results " +e);
            recipesList = null;
        }

        return recipesList;
    }
}
