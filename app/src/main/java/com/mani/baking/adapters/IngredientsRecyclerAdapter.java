package com.mani.baking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.datastruct.IngredientDetails;
import com.mani.baking.utils.SessionData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> {

    private final List<IngredientDetails> ingredientDetails;
    private final Context context;
    public IngredientsRecyclerAdapter( Context context) {
        ingredientDetails = SessionData.getRecipeDetails().getIngredientDetailsList();
        this.context = context;
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
        holder.quantityTextView.setText(ingredientDetails.get(position).getQuantity());
        holder.measureTextView.setText(ingredientDetails.get(position).getMeasure());
        holder.ingredientTextView.setText(ingredientDetails.get(position).getIngredient());
        if(position%2 ==0){
            holder.linearLayout.setBackground(context.getDrawable(R.color.ingredients_row_background));
        }
    }

    @Override
    public int getItemCount() {
        return ingredientDetails.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.quantity_tv)
        TextView quantityTextView;
         @BindView(R.id.measure_tv) TextView measureTextView;
         @BindView(R.id.ingredient_tv)TextView ingredientTextView;
         @BindView(R.id.ingredient_row)LinearLayout linearLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
