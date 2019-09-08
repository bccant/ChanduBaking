package com.chandu.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.chandu.bakingapp.cooking.Ingredients;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview_ingredientsList)
    RecyclerView mRecyclerview;
    IngredientsAdapter ingredientsAdapter;
    final String recipeObject = "RecipeObject";
    final String ingredientsObject = "IngredientsObject";
    final String dishObject = "DishObject";
    Ingredients[] ingredientsList;
    List<Ingredients> ingredientsResult;
    String dishName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_details);

        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutManager);

        ingredientsAdapter = new IngredientsAdapter();
        mRecyclerview.setAdapter(ingredientsAdapter);


        final Intent i = getIntent();
        ingredientsList = (Ingredients[]) i.getSerializableExtra(ingredientsObject);
        ingredientsResult = Arrays.asList(ingredientsList);
        //ingredientsList = (Ingredients[]) i.getSerializableExtra(ingredientsObject);

        ingredientsAdapter.setRecipeListDetails(ingredientsResult);
        dishName = i.getStringExtra(dishObject);
        setTitle(dishName + " Ingredients");

//        i.replaceExtras(new Bundle());
//        i.setAction("");
//        i.setData(null);
//        i.setFlags(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
