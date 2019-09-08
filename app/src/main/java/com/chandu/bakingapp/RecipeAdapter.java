package com.chandu.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chandu.bakingapp.cooking.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private List<Recipe> recipeList;

    private final RecipeAdapterOnClickHandler mClickHandler;

    public RecipeAdapter(RecipeAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.cooking_list_main;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        Recipe recipeSummary = recipeList.get(position);
        //recipeAdapterViewHolder.recipeName.setText(recipeSummary.getName());
        int imageID = getImageid(recipeSummary.getName());
        if (imageID != -1) {
            recipeAdapterViewHolder.recipeName.setBackgroundResource(imageID);
        }
    }

    @Override
    public int getItemCount() {
        return (recipeList == null) ? 0: recipeList.size();
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipeDetails);
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final Button recipeName;

        public RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = (Button) itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipeDetails = recipeList.get(adapterPosition);
            mClickHandler.onClick(recipeDetails);
        }
    }

    public void setRecipeListDetails(List<Recipe> recipeListDetails) {
        recipeList = recipeListDetails;
        notifyDataSetChanged();
    }

    public int getImageid(String itemName) {
        if (itemName.equals(("Nutella Pie"))) {
            return R.drawable.nutellapie;
        } else if (itemName.equals(("Brownies"))) {
            return R.drawable.brownies;
        } else if (itemName.equals(("Yellow Cake"))) {
            return R.drawable.yellowcake;
        } else if (itemName.equals(("Cheesecake"))) {
            return R.drawable.cheesecake;
        } else {
            return -1;
        }

    }
}
