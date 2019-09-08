package com.chandu.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.chandu.bakingapp.IngredientsActivity;
import com.chandu.bakingapp.MainActivity;
import com.chandu.bakingapp.R;
import com.chandu.bakingapp.Utilities.SharedPreferencesUtil;
import com.chandu.bakingapp.cooking.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int item,
                                List<Recipe> widgetRecipe, int appWidgetId) {
        final String dishObject = "DishObject";
        final String ingredientsObject = "IngredientsObject";
        RemoteViews views;

        // Construct the RemoteViews object
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_empty);

        //item = 1;
        SharedPreferencesUtil.setRecipeid(context, item);
        if (widgetRecipe == null || widgetRecipe.size() == 0 || item == -1) {
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setOnClickPendingIntent(R.id.appwidget_ingredient, pendingIntent);
            views = getEmptyView(context);
        } else {
//            Intent intent = new Intent(context, IngredientsActivity.class);
//            intent.putExtra(dishObject, widgetRecipe.get(item).getName());
//            Ingredients[] testIngredient = widgetRecipe.get(item).getRecipeIngredients();
//            intent.putExtra(ingredientsObject, testIngredient);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setOnClickPendingIntent(R.id.appwidget_ingredient, pendingIntent);

            views = getIngredientsListView(context, item, widgetRecipe);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        IngredientWidgetService.startIngredientsAction(context, SharedPreferencesUtil.getRecipeID(context));
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_list_view);
    }

    public static void updateIndegWidget(Context context, AppWidgetManager appWidgetManager, int item,
                                         List<Recipe> widgetRecipe, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, item, widgetRecipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static RemoteViews getEmptyView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.ingredient_widget_empty);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_ingredient, pendingIntent);
        views.setOnClickPendingIntent(R.id.appwidget_ingredient, pendingIntent);

        return views;
    }

    public static RemoteViews getIngredientsListView(Context context, int item,
                                                     List<Recipe> widgetRecipe) {
        final String widgetObject = "WidgetIngredient";
        final String dishObject = "DishObject";
        final String ingredientsObject = "IngredientsObject";
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_list);
        String widgetTitle = "Ingredients of " + widgetRecipe.get(item).getName() + " are:";
        views.setTextViewText(R.id.recipe_title, widgetTitle);

        Intent intent = new Intent(context, IngredientsListWidgetService.class);
//        Ingredients[] testIngredient = widgetRecipe.get(item).getRecipeIngredients();
//        intent.putExtra(widgetObject, testIngredient);
//        intent.putExtra(dishObject, widgetRecipe.get(item).getName());
        views.setRemoteAdapter(R.id.ingredient_list_view, intent);

        Intent detailactivity = new Intent(context, IngredientsActivity.class);
//        detailactivity.putExtra(dishObject, widgetRecipe.get(item).getName());
//        detailactivity.putExtra(ingredientsObject, testIngredient);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, detailactivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setOnClickPendingIntent(R.id.appwidget_ingredient, pendingIntent);
        views.setPendingIntentTemplate(R.id.ingredient_list_view, pendingIntent);

        views.setEmptyView(R.id.ingredient_list_view, R.id.widget_empty);

        return views;
    }
}

