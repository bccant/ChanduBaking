package com.chandu.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandu.bakingapp.cooking.Ingredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private List<Ingredients> ingredientsList;

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredients_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder ingredientsViewHolder, int position) {
        Ingredients ingredientSummary = ingredientsList.get(position);
        String  summaryText = ingredientSummary.getQuantity() + " " + ingredientSummary.getMeasure()
                 + "'s of " + ingredientSummary.getIngredient();
        ingredientsViewHolder.ingredientDetails.setText(summaryText);
    }

    @Override
    public int getItemCount() {
        return (ingredientsList == null) ? 0: ingredientsList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        public final TextView ingredientDetails;

        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientDetails = (TextView) itemView.findViewById(R.id.ingredients_list);
        }
    }

    public void setRecipeListDetails(List<Ingredients> ingredientDetails) {
        ingredientsList = ingredientDetails;
    }

}
