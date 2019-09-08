package com.chandu.bakingapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {IngredientsEntry.class, RecipeEntry.class, StepsEntry.class}, version = 1,
        exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    private static final String LOG_TAG = RecipeDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "faveRecipeList";
    private static RecipeDatabase sInstance;

    public static RecipeDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new DB instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeDatabase.class, RecipeDatabase.DATABASE_NAME)

                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the DB instance");
        return sInstance;
    }

    public abstract RecipeDAO recipeDAO();
}
