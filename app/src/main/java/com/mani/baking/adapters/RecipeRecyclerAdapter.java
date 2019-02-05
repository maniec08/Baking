package com.mani.baking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.activity.ItemDetailActivity;
import com.mani.baking.activity.ItemListActivity;
import com.mani.baking.datastruct.RecipeDetails;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeRecyclerAdapter  extends RecyclerView.Adapter<RecipeRecyclerAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    private List<RecipeDetails> recipes;
    Context context;

    public RecipeRecyclerAdapter(Context context, LayoutInflater layoutInflater, List<RecipeDetails> recipes) {
        this.layoutInflater = layoutInflater;
        this.recipes=recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recipeitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recipeTextView.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeTextView;
        ViewHolder(View itemView) {
            super(itemView);
            recipeTextView = itemView.findViewById(R.id.recipe_text_view);
            recipeTextView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
                launchIntent(getAdapterPosition());
        }
    }

    private void launchIntent(int adapterPosition) {
        Intent intent = new Intent(context, ItemListActivity.class);
        intent.putExtra("recipe", recipes.get(adapterPosition));
        context.startActivity(intent);
    }
}
