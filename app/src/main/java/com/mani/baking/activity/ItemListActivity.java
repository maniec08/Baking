package com.mani.baking.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.adapters.StepsRecyclerAdapter;
import com.mani.baking.utils.SessionData;
import com.mani.baking.utils.SelectionSesionVar;

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
            if (SelectionSesionVar.step >= 0) {
                startStepTransaction(savedInstanceState);
            } else {
                startIngredientTransaction();
            }
        }
    }

    private void setUpToolBar() {
        toolbar.setTitle(SessionData.getRecipeDetails().getName());
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
                SelectionSesionVar.step = -1;
                startIngredientTransaction();
            } else {
                Intent intent = new Intent(context, IngredientActivity.class);
                context.startActivity(intent);
            }
        });
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
