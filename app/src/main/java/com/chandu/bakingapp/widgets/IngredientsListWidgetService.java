package com.chandu.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.chandu.bakingapp.R;
import com.chandu.bakingapp.Utilities.SharedPreferencesUtil;
import com.chandu.bakingapp.cooking.Ingredients;
import com.chandu.bakingapp.cooking.Recipe;

import java.util.List;

public class IngredientsListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListViewFactory(this.getApplicationContext());
    }
}

class IngredientsListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    List<Recipe> recipeList = null;
    Ingredients[] ingredientsList = {};
    final String ingredientsObject = "IngredientsObject";

    public IngredientsListViewFactory(Context applicationContext) {
        context = applicationContext;
    }

//    public IngredientsListViewFactory(Context applicationContext, Intent intent) {
//        context = applicationContext;
//        ingredientsList = (Ingredients[]) intent.getSerializableExtra(ingredientsObject);
//    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        int item = SharedPreferencesUtil.getRecipeID(context);
        recipeList = IngredientWidgetService.extractRecipeFromDB(context);
        ingredientsList = recipeList.get(item).getRecipeIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientsList.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        int item = SharedPreferencesUtil.getRecipeID(context);
        final String dishObject = "DishObject";
        final String ingredientsObject = "IngredientsObject";
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.ingredients_list);
        Ingredients ingredientSummary = ingredientsList[position];
        String  summaryText = ingredientSummary.getQuantity() + " " + ingredientSummary.getMeasure()
                + "'s of " + ingredientSummary.getIngredient();
        String recipeName = recipeList.get(item).getName();
        //view.setTextViewText(R.id.recipe_title, "Chandu");
        view.setTextViewText(R.id.ingredients_list, summaryText);

        Intent detailactivity = new Intent();
        detailactivity.putExtra(dishObject, recipeName);
        detailactivity.putExtra(ingredientsObject, ingredientsList);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, detailactivity,
//                PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickFillInIntent(R.id.ingredients_list, detailactivity);
        //view.setPendingIntentTemplate(R.id.recipe_title, pendingIntent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
