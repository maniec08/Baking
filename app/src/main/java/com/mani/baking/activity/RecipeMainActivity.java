package com.mani.baking.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.mani.baking.R;
import com.mani.baking.adapters.RecipeRecyclerAdapter;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.datastruct.RecipeDetails;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeMainActivity extends AppCompatActivity {

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;

    @BindView(R.id.error_loading_data_tv)
    TextView errorMessageTextView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);
        ButterKnife.bind(this);
        isTablet = getResources().getBoolean(R.bool.istablet);
        setSupportActionBar(toolbar);

        new ParseJson(this).execute();
    }

    private void populateRecyclerView(Context context, List<RecipeDetails> recipes) {
        RecipeRecyclerAdapter recipeRecyclerAdapter =
                new RecipeRecyclerAdapter(context, getLayoutInflater(), recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if(isTablet){
            layoutManager = new GridLayoutManager(context, 3);
        }
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setAdapter(recipeRecyclerAdapter);
    }

    public class ParseJson extends AsyncTask<Void,Void, List<RecipeDetails>> {
        Context context;

        public ParseJson(Context context) {
            this.context = context;
        }

        @Override
        protected List<RecipeDetails> doInBackground(Void... voids) {
            Recipe recipe = new Recipe(context);
            return recipe.getList();
        }
        @Override
        protected void onPostExecute(List<RecipeDetails> recipes){
            populateRecyclerView(context, recipes);
        }
    }

}
