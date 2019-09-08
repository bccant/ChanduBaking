package com.chandu.bakingapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Query("SELECT * FROM recipe")
    List<RecipeEntry> loadAllRecipes();

    @Insert()
    void insertRecipe(RecipeEntry recipeEntry);

    @Update()
    void updateRecipe(RecipeEntry recipeEntry);

    @Delete()
    void deleteRecipe(RecipeEntry recipeEntry);

    @Query("SELECT * FROM recipe where recipeId = :id")
    RecipeEntry loadRecipeByID(String id);

    @Query("DELETE FROM recipe")
    void nukeRecipe();

    @Query("SELECT * FROM recipesteps")
    List<StepsEntry> loadAllRecipeSteps();

    @Insert()
    void insertRecipeStep(StepsEntry stepsEntry);

    @Update()
    void updateRecipeStep(StepsEntry stepsEntry);

    @Delete()
    void deleteRecipeSteps(StepsEntry stepsEntry);

    @Query("SELECT * FROM recipesteps where recipeId = :id")
    List<StepsEntry> loadRecipeStepByID(int id);

    @Query("DELETE FROM recipesteps")
    void nukeRecipeSteps();

    @Query("SELECT * FROM ingredients")
    List<IngredientsEntry> loadAllIngredients();

    @Insert()
    void insertIngredients(IngredientsEntry ingredientsEntry);

    @Update()
    void updateIngredients(IngredientsEntry ingredientsEntry);

    @Delete()
    void deleteIngredients(IngredientsEntry ingredientsEntry);

    @Query("SELECT * FROM ingredients where recipeId = :id")
    List<IngredientsEntry> loadIngredientsByID(int id);

    @Query("DELETE FROM ingredients")
    void nukeIngredients();
}
