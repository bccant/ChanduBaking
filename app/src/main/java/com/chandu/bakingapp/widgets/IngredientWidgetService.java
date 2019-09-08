package com.chandu.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.chandu.bakingapp.Database.IngredientsEntry;
import com.chandu.bakingapp.Database.RecipeDatabase;
import com.chandu.bakingapp.Database.RecipeEntry;
import com.chandu.bakingapp.Database.StepsEntry;
import com.chandu.bakingapp.R;
import com.chandu.bakingapp.Utilities.AppExecutors;
import com.chandu.bakingapp.Utilities.SharedPreferencesUtil;
import com.chandu.bakingapp.cooking.Ingredients;
import com.chandu.bakingapp.cooking.Recipe;
import com.chandu.bakingapp.cooking.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

public class IngredientWidgetService extends IntentService {
    private static RecipeDatabase rDB = null;
    public static List<Recipe> fullRecipe = null;
    public int item = -1;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public static final String ACTION_INGREDIENTS = "WIDGET_INGREDIENT";
    public static final String ACTION_DESCRIPTION = "WIDGET_DESCRIPTION";
    public static final String ACTION_RECIPE_ITEM = "WIDGET_RECIPE_ITEM";

    public IngredientWidgetService() {
        super("IngredientWidgetService");
    }

    public static void startIngredientsAction(Context context, int item1) {
        Intent intent = new Intent(context, IngredientWidgetService.class);
        intent.setAction(ACTION_INGREDIENTS);
        //intent.putExtra(ACTION_RECIPE_ITEM, item1);
        SharedPreferencesUtil.setRecipeid(context, item1);
        context.startService(intent);
    }

    public static void startIngredientsDesc(Context context) {
        Intent intent = new Intent(context, IngredientWidgetService.class);
        intent.setAction(ACTION_DESCRIPTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if(ACTION_INGREDIENTS.equals(action)) {
                item = SharedPreferencesUtil.getRecipeID(this);
                handleRecipeIngredients();
            } else if (ACTION_DESCRIPTION.equals(action)) {
                handleRecipeDesc();
            }
        }
    }

    private void handleRecipeIngredients() {
        if (rDB == null) {
            rDB = RecipeDatabase.getsInstance(getApplicationContext());
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                fullRecipe = extractRecipeFromDB(getApplicationContext());
                handleRecipeDesc();
            }
        });
    }

    public static List<Recipe> extractRecipeFromDB(Context context) {
        if (rDB == null) {
            rDB = RecipeDatabase.getsInstance(context);
        }

        List<RecipeEntry> recipeEntries = rDB.recipeDAO().loadAllRecipes();
        Ingredients[] defaultIngredients = {};
        List<Recipe> widgetRecipe = new ArrayList<>();

        if (fullRecipe != null) {
            fullRecipe.clear();
        }

        for (int i = 0; i < recipeEntries.size(); i++) {
            List<Ingredients> ingredientslist = new ArrayList<>();
            List<RecipeSteps> recipeStepsList = new ArrayList<>();
            Recipe recipe = new Recipe();
            int recipeID = Integer.parseInt(recipeEntries.get(i).getRecipeId());
            recipe.setId(recipeEntries.get(i).getRecipeId());
            recipe.setName(recipeEntries.get(i).getRecipeName());
            recipe.setImageUrl(recipeEntries.get(i).getRecipeImageUrl());
            recipe.setServings(recipeEntries.get(i).getRecipeServings());
            recipe.setRecipeIngredients(defaultIngredients);
            List<IngredientsEntry> ingredientsEntries = rDB.recipeDAO().loadIngredientsByID(recipeID);

            int z = ingredientsEntries.size();
            System.out.println("Size of ingredients for recipe-id:" + recipeID + "is " + z);
            for (int j = 0; j < ingredientsEntries.size(); j++) {
                Ingredients ingredients = new Ingredients();
                ingredients.setIngredient(ingredientsEntries.get(j).getRecipeIngredient());
                ingredients.setMeasure(ingredientsEntries.get(j).getIngMeasure());
                ingredients.setQuantity(ingredientsEntries.get(j).getIngQuantity());
                ingredientslist.add(ingredients);
            }
            recipe.setRecipeIngredients(ingredientslist.toArray(new Ingredients[ingredientslist.size()]));

            List<StepsEntry> stepsEntries = rDB.recipeDAO().loadRecipeStepByID(recipeID);

            for (int k =0; k < stepsEntries.size(); k++) {
                RecipeSteps recipeSteps = new RecipeSteps();
                recipeSteps.setId(stepsEntries.get(k).getStepsId());
                recipeSteps.setDesc(stepsEntries.get(k).getStepsDesc());
                recipeSteps.setShortDesc(stepsEntries.get(k).getStepsShortDesc());
                recipeSteps.setVideoUrl(stepsEntries.get(k).getStepsVideoUrl());
                recipeSteps.setThumbUrl(stepsEntries.get(k).getStepsThumbUrl());
                recipeStepsList.add(recipeSteps);
            }

            recipe.setRecipeSeq(recipeStepsList.toArray(new RecipeSteps[recipeStepsList.size()]));
            widgetRecipe.add(recipe);
        }

        //fullRecipe = widgetRecipe;
//        handleRecipeDesc();

        return widgetRecipe;
    }

    private void handleRecipeDesc() {
        item = SharedPreferencesUtil.getRecipeID(this);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        if (item != -1 && fullRecipe != null) {
            SharedPreferencesUtil.setRecipeid(this, item);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_list_view);
            IngredientWidget.updateIndegWidget(this, appWidgetManager, item, fullRecipe, appWidgetIds);
        } else {
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidget.class));
            IngredientWidget.updateIndegWidget(this, appWidgetManager, item, null, appWidgetIds);
        }
    }
}
