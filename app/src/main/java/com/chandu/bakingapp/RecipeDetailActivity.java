package com.chandu.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.chandu.bakingapp.Database.RecipeDatabase;
import com.chandu.bakingapp.cooking.Recipe;

import butterknife.BindView;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.navigateRecipeStep {
    @BindView(R.id.recipe_ingredient)
    Button recipeIngredient;
    final String recipeObject = "RecipeObject";
    final String recipeStepsNumber = "RecipeStepNumber";
    final String recipeSteps = "RecipeSteps";
    private Recipe myRecipe;
    private boolean mTwoPane = false;
    private boolean landscapeMode = false;
    private RecipeDatabase rDB;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private Button mNext;
    private Button mPrev;
    private int prevStepSeq = 0;
    private int nextStepSeq = 1;
    private int seq = 0;
    private int seqMax = 0;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().hide();
//            }
            landscapeMode = true;
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().show();
//            }
        }

        setContentView(R.layout.activity_recipe_detail);

        if (findViewById(R.id.recipe_steps_frag) != null) {
            mTwoPane = true;
            mNext = findViewById(R.id.next);
            mPrev = findViewById(R.id.prev);
        }

        intent = getIntent();
        if (intent != null && intent.hasExtra(recipeObject)) {
            myRecipe = (Recipe) intent.getSerializableExtra(recipeObject);
            setTitle(myRecipe.getName());
        }

        seq = intent.getIntExtra(recipeStepsNumber, 0);
        seqMax = intent.getIntExtra(recipeSteps, 0);

        determineSteps(seq);

        intent.putExtra(recipeSteps, myRecipe.getRecipeSeq().length);

        if (savedInstanceState == null) {
            //handleRecipeDetailsFragment();

            if (mTwoPane) {
                intent.putExtra(recipeStepsNumber, 0);
                handleRecipeStepsIngredients(seq);
            }

            handleRecipeDetailsFragment();

        }

        int item = Integer.parseInt(myRecipe.getId()) - 1;
        //SharedPreferencesUtil.setRecipeid(this, item);

        //IngredientWidgetService.startIngredientsAction(this, item);
    }

    private void determineSteps(final int steps) {
        if (steps != 0) {
            prevStepSeq = steps - 1;
        }
        if (steps != (seqMax -1)) {
            nextStepSeq = steps + 1;
        }
    }

    private void handleRecipeDetailsFragment() {
        RecipeDetailsFragment ingredientsFragment = new RecipeDetailsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_frag, ingredientsFragment)
                .commit();
    }

    private void handleRecipeStepsIngredients(int position) {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void previousStep() {
        seq = intent.getIntExtra(recipeStepsNumber, 0);
        seqMax = intent.getIntExtra(recipeSteps, 0);
        if (seq != 0) {
            seq = seq - 1;
            handleRecipeStepsIngredients(seq);
        }
    }

    @Override
    public void nextStep() {
        seq = intent.getIntExtra(recipeStepsNumber, 0);
        seqMax = intent.getIntExtra(recipeSteps, 0);
        if (seq != (seqMax - 1)) {
            seq = seq + 1;
            handleRecipeStepsIngredients(seq);
        }
    }
}
