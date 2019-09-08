package com.chandu.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chandu.bakingapp.Database.IngredientsEntry;
import com.chandu.bakingapp.Database.RecipeDatabase;
import com.chandu.bakingapp.Database.RecipeEntry;
import com.chandu.bakingapp.Database.StepsEntry;
import com.chandu.bakingapp.Utilities.AppExecutors;
import com.chandu.bakingapp.Utilities.NetworkUtils;
import com.chandu.bakingapp.Utilities.RecipeJasonUtils;
import com.chandu.bakingapp.widgets.IngredientWidgetService;
import com.chandu.bakingapp.cooking.Ingredients;
import com.chandu.bakingapp.cooking.Recipe;
import com.chandu.bakingapp.cooking.RecipeSteps;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Recipe>>{
    @BindView(R.id.recyclerview_recipelist)
    RecyclerView mRecyclerView;
    @BindView(R.id.recipe_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    private RecipeAdapter recipeAdapter;
    private static final int RECIPE_LIST_LOADER = 22;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    final String recipeURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    final String recipeObject = "RecipeObject";
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Parcelable savedRecyclerLayoutState;
    private RecipeDatabase rDB;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, calculateNoOfColumns(this));

        mRecyclerView.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(recipeAdapter);

        rDB = RecipeDatabase.getsInstance(getApplicationContext());

        //mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        loadRecipeList();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //int itemID = prefs.getInt("ingredient", -1);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns <= 2)
            noOfColumns = 1;
        return noOfColumns;
    }

    private void loadRecipeList() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, recipeURL);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> movieLoader = loaderManager.getLoader(RECIPE_LIST_LOADER);

        if (movieLoader == null) {
            loaderManager.initLoader(RECIPE_LIST_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(RECIPE_LIST_LOADER, queryBundle, this);
        }
    }

    private void showRecipeView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the recipe list is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int i, final Bundle args) {

        return new AsyncTaskLoader<List<Recipe>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);

                forceLoad();
            }

            @Nullable
            @Override
            public List<Recipe> loadInBackground() {
                String recipeListURL = args.getString(SEARCH_QUERY_URL_EXTRA);

                if (recipeListURL == null || TextUtils.isEmpty(recipeListURL)) {
                    return null;
                }

                URL newURL = null;
                try {
                    newURL = new URL(recipeListURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    String jsonRecipeListResponse = NetworkUtils
                            .getResponseFromHttpUrl2(newURL);

                    List<Recipe> simpleJsonRecipeListData = RecipeJasonUtils.getRecipesDetailFromJson(MainActivity.this, jsonRecipeListResponse);

                    return simpleJsonRecipeListData;

                } catch (Exception e) {
                    System.out.println("EXCEPTION ISNIDE ASYNCTASK: " + e);
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, final List<Recipe> recipes) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (recipes != null) {
            Throwable throwable = new Throwable();
            throwable.printStackTrace();
            showRecipeView();
            recipeAdapter.setRecipeListDetails(recipes);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            updateRecipeDB(recipes);
            //IngredientWidgetService.startIngredientsAction(this);
        }
    }

    private void updateRecipeDB(final List<Recipe> recipes) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                deleteDB();
                for (int i = 0; i < recipes.size(); i++) {
                    int recipeID = Integer.parseInt(recipes.get(i).getId());
                    final RecipeEntry rEntry = new RecipeEntry(recipes.get(i).getId(),
                            recipes.get(i).getName(), recipes.get(i).getServings(),
                            recipes.get(i).getImageUrl());
                    rDB.recipeDAO().insertRecipe(rEntry);

                    Ingredients[] ingredientsList = recipes.get(i).getRecipeIngredients();
                    RecipeSteps[] recipeSteps = recipes.get(i).getRecipeSeq();

                    for (int j = 0; j < ingredientsList.length; j++) {
                        final IngredientsEntry iEntry = new IngredientsEntry(
                                ingredientsList[j].getQuantity(),
                                ingredientsList[j].getMeasure(),
                                ingredientsList[j].getIngredient(), recipeID);
                        rDB.recipeDAO().insertIngredients(iEntry);
                    }

                    for (int k = 0; k < recipeSteps.length; k++) {
                        final StepsEntry sEntry = new StepsEntry(recipeSteps[k].getId(),
                                recipeSteps[k].getShortDesc(), recipeSteps[k].getDesc(),
                                recipeSteps[k].getVideoUrl(), recipeSteps[k].getThumbUrl(),
                                recipeID);
                        rDB.recipeDAO().insertRecipeStep(sEntry);
                    }
                }
            }
        });

        System.out.println("Test DB");
    }

    private void deleteDB() {
        rDB.recipeDAO().nukeRecipe();
        rDB.recipeDAO().nukeRecipeSteps();
        rDB.recipeDAO().nukeIngredients();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {
        getLoaderManager().destroyLoader(RECIPE_LIST_LOADER);
    }

    @Override
    public void onClick(Recipe recipeDetails) {
        Context context = this;
        IngredientWidgetService.startIngredientsAction(this, Integer.parseInt(recipeDetails.getId()) -1);
        Intent intent = new Intent(context, RecipeDetailActivity.class);

        intent.putExtra(recipeObject, recipeDetails);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadRecipeList();
    }
}
