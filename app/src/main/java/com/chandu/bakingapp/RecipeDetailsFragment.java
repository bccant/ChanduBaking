package com.chandu.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.chandu.bakingapp.cooking.Ingredients;
import com.chandu.bakingapp.cooking.Recipe;
import com.chandu.bakingapp.cooking.RecipeSteps;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeDetailsFragment extends Fragment implements RecipeStepsFragment.navigateRecipeStep {
    Recipe newRecipe;
    RecipeSteps recipeSteps;
    ArrayList<RecipeSteps> recipeStepsList;
    Ingredients[] ingredientsList;
    RecipeStepsDetail recipeAdapter;
    final String recipeObject = "RecipeObject";
    final String ingredientsObject = "IngredientsObject";
    final String recipeStepsObject = "RecipeStepsObject";
    final String recipeStepsNumber = "RecipeStepNumber";
    final String recipeStepsMax = "RecipeSteps";
    final String dishObject = "DishObject";
    private int currentRecipeStepPos = 0;
    private int maxRecipeSteps;
    private String dishName;
    private boolean mTwoPane = false;

    public RecipeDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients, container, false);

        final Intent i = getActivity().getIntent();
        newRecipe = (Recipe) i.getSerializableExtra(recipeObject);

        if (getActivity().findViewById(R.id.recipe_steps_frag) != null) {
            mTwoPane = true;
        }

        Button recipe_ingredient = (Button) rootView.findViewById(R.id.recipe_ingredient);
        ListView listView = (ListView) rootView.findViewById(R.id.recipe_steps_list);

        recipeStepsList = new ArrayList<>(Arrays.asList(newRecipe.getRecipeSeq()));
        ingredientsList = newRecipe.getRecipeIngredients();
        recipeAdapter = new RecipeStepsDetail(getActivity().getApplicationContext(), recipeStepsList);
        dishName = newRecipe.getName();
        listView.setAdapter(recipeAdapter);
        i.putExtra(ingredientsObject, ingredientsList);
        i.putExtra(dishObject, dishName);
        maxRecipeSteps = ingredientsList.length;

        recipe_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
//                    i.putExtra(ingredientsObject, ingredientsList);
//                    i.putExtra(dishObject, dishName);

                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_steps_frag, ingredientsFragment)
                            .commit();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), IngredientsActivity.class);
                    intent.putExtra(ingredientsObject, ingredientsList);
                    intent.putExtra(dishObject, dishName);
                    startActivity(intent);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recipeSteps = newRecipe.RecipeSeq[position];
                if (mTwoPane) {
                    i.putExtra(recipeStepsNumber, position);
                    i.putExtra(recipeStepsMax, newRecipe.getRecipeSeq().length);
                    recipeFragmentGenerate(position);
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), RecipeStepsActivity.class);
                    intent.putExtra(recipeStepsNumber, position);
                    intent.putExtra(recipeObject, newRecipe);
                    intent.putExtra(recipeStepsMax, newRecipe.getRecipeSeq().length);
                    startActivity(intent);
                }

            }
        });

        return rootView;
    }

    private void recipeFragmentGenerate(int position) {
        RecipeStepsFragment rSF = new RecipeStepsFragment();
        rSF.seqMax = newRecipe.getRecipeSeq().length;
        rSF.seq = position;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_steps_frag, rSF)
                .commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void previousStep() {
        if (currentRecipeStepPos != 0) {
            currentRecipeStepPos = currentRecipeStepPos - 1;
            recipeFragmentGenerate(currentRecipeStepPos);
        }
    }

    @Override
    public void nextStep() {
        if (currentRecipeStepPos != (maxRecipeSteps - 1)) {
            currentRecipeStepPos = currentRecipeStepPos + 1;
            recipeFragmentGenerate(currentRecipeStepPos);
        }
    }
}
