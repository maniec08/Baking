package com.mani.baking.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.activity.IngredientFragment;
import com.mani.baking.activity.ItemListActivity;
import com.mani.baking.datastruct.IngredientDetails;
import com.mani.baking.utils.KeyConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> {

    private final List<IngredientDetails> ingredientDetails;
    public IngredientsRecyclerAdapter( List<IngredientDetails> ingredientDetails) {
        this.ingredientDetails = ingredientDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingrdient_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.quantityTextView.setText(Integer.toString(ingredientDetails.get(position).getQuantity()));
        holder.measureTextView.setText(ingredientDetails.get(position).getMeasure());
        holder.ingredientTextView.setText(ingredientDetails.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredientDetails.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder  {
        TextView quantityTextView;
        TextView measureTextView;
        TextView ingredientTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            quantityTextView = itemView.findViewById(R.id.quantity_tv);
            measureTextView = itemView.findViewById(R.id.measure_tv);
            ingredientTextView = itemView.findViewById(R.id.ingredient_tv);
        }
    }


}
