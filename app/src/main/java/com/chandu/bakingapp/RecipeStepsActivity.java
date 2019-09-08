package com.chandu.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.chandu.bakingapp.cooking.Recipe;

public class RecipeStepsActivity extends AppCompatActivity implements RecipeStepsFragment.navigateRecipeStep{
    Intent intent;
    final String recipeObject = "RecipeObject";
    final String recipeStepsObject = "RecipeStepsObject";
    final String recipeStepsNumber = "RecipeStepNumber";
    final String recipeSteps = "RecipeSteps";
    final String dishObject = "DishObject";
    private int seq = 0;
    private int seqMax;
    Recipe myRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
        }

        intent = getIntent();
        if (intent != null && intent.hasExtra(recipeObject)) {
            myRecipe = (Recipe) intent.getSerializableExtra(recipeObject);
            setTitle(myRecipe.getName());
        }

        seq = intent.getIntExtra(recipeStepsNumber, 0);
        seqMax = intent.getIntExtra(recipeSteps, 0);

        setContentView(R.layout.activity_recipe_steps);

        if (savedInstanceState == null) {

            recipeStepsFragment(seq);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void recipeStepsFragment(int position) {
        RecipeStepsFragment rSF = new RecipeStepsFragment();
        rSF.seqMax = myRecipe.getRecipeSeq().length;
        rSF.seq = position;

        Intent intent = getIntent();

        intent.putExtra(recipeObject, myRecipe);
        intent.putExtra(recipeStepsNumber, rSF.seq);
        intent.putExtra(recipeSteps, rSF.seqMax);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_steps_frag, rSF)
                .commit();
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

    @Override
    public void previousStep() {
        seq = intent.getIntExtra(recipeStepsNumber, 0);
        seqMax = intent.getIntExtra(recipeSteps, 0);
        if (seq != 0) {
            seq = seq - 1;
            recipeStepsFragment(seq);
        }
    }

    @Override
    public void nextStep() {
        seq = intent.getIntExtra(recipeStepsNumber, 0);
        seqMax = intent.getIntExtra(recipeSteps, 0);
        if (seq != (seqMax - 1)) {
            seq = seq + 1;
            recipeStepsFragment(seq);
        }
    }
}
