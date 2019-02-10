package com.mani.baking.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.adapters.RecipeRecyclerAdapter;
import com.mani.baking.adapters.StepsRecyclerAdapter;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.utils.KeyConstants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListActivity extends AppCompatActivity {

    private static final String TAG = ItemListActivity.class.getSimpleName();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean twoPane;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.steps_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ingredients_header)
    TextView ingredientTextView;
    ItemDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        ButterKnife.bind(this);
        setUpToolBar();
        twoPane = getResources().getBoolean(R.bool.istablet);
        addClickListener();
        setupRecyclerView();
        if(twoPane) {
            if (Recipe.selectedStep >= 0) {
                startStepTransaction(savedInstanceState);
            } else {
                startIngredientTransaction();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setUpToolBar() {
        toolbar.setTitle(Recipe.getRecipeDetails().getName());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            try {
                onBackPressed();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });
    }

    private void addClickListener() {
        final Context context = this;
        ingredientTextView.setOnClickListener(v -> {
            if (twoPane) {
                Recipe.selectedStep = -1;
                startIngredientTransaction();
            } else {
                Intent intent = new Intent(context, IngredientActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
       // outState.putLong();
        super.onSaveInstanceState(outState);
    }

    private void  startStepTransaction(Bundle savedInstance){
        if (twoPane && savedInstance == null)  {
            fragment = new ItemDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }
    }
    private void startIngredientTransaction() {
        IngredientFragment ingredientFragment = new IngredientFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, ingredientFragment)
                .commit();
    }

    private void setupRecyclerView() {
        StepsRecyclerAdapter stepsRecyclerAdapter = new StepsRecyclerAdapter(this, twoPane);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(stepsRecyclerAdapter);
    }
}
