package com.chandu.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandu.bakingapp.cooking.Ingredients;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {
    @BindView(R.id.recyclerview_ingredientsList)
    RecyclerView mRecyclerview;
    IngredientsAdapter ingredientsAdapter;
    final String recipeObject = "RecipeObject";
    final String ingredientsObject = "IngredientsObject";
    final String dishObject = "DishObject";
    Ingredients[] ingredientsList;
    List<Ingredients> ingredientsResult;
    String dishName;

    public IngredientsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_details, container, false);

        ButterKnife.bind(this, rootView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerview.setLayoutManager(layoutManager);

        ingredientsAdapter = new IngredientsAdapter();
        mRecyclerview.setAdapter(ingredientsAdapter);

        final Intent i = getActivity().getIntent();
        ingredientsList = (Ingredients[]) i.getSerializableExtra(ingredientsObject);
        //ingredientsResult = new ArrayList<>();
        ingredientsResult = Arrays.asList(ingredientsList);

        ingredientsAdapter.setRecipeListDetails(ingredientsResult);
        dishName = i.getStringExtra(dishObject);
        getActivity().setTitle(dishName + " Ingredients");

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
