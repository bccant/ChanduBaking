package com.chandu.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.chandu.bakingapp.cooking.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsDetail extends ArrayAdapter<RecipeSteps>{
    private final Context context;
    private final List<RecipeSteps> recipeStepsList;
    private AdapterView.OnItemClickListener onItemClickListener;

    public RecipeStepsDetail(@NonNull Context context, ArrayList<RecipeSteps> objects) {
        super(context, R.layout.recipe_steps, objects);
        this.context = context;
        this.recipeStepsList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.recipe_steps,parent,false);


        Button childName = (Button)rowView.findViewById(R.id.childButton);
        final String buttonName = recipeStepsList.get(position).getShortDesc();
        childName.setText(buttonName);

        return rowView;
    }
}
