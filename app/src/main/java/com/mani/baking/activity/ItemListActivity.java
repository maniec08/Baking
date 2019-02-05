package com.mani.baking.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.adapters.StepsRecyclerAdapter;
import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.utils.KeyConstants;

import java.security.Key;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean twoPane;
    private RecipeDetails recipeDetails;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    @BindView(R.id.ingredients_header)
    TextView ingredientTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);

        recipeDetails = getIntent().getParcelableExtra(KeyConstants.RECIPE);

        toolbar.setTitle(recipeDetails.getName());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
         setSupportActionBar(toolbar);

        twoPane = getResources().getBoolean(R.bool.istablet);
        addClickListener();

        setupRecyclerView();
    }

    private void addClickListener() {
        final Context context = this;
        ingredientTextView.setOnClickListener(v -> {
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(KeyConstants.RECIPE, (ArrayList<? extends Parcelable>) recipeDetails.getIngredientDetailsList());

            Intent intent = new Intent(context, IngredientActivity.class);
            intent.putExtra(KeyConstants.RECIPE, arguments);
            context.startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        StepsRecyclerAdapter stepsRecyclerAdapter = new StepsRecyclerAdapter(this, recipeDetails.getStepDetailsList(), twoPane);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(stepsRecyclerAdapter);
    }


}
