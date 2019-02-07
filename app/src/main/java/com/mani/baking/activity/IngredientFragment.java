package com.mani.baking.activity;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.adapters.IngredientsRecyclerAdapter;
import com.mani.baking.datastruct.IngredientDetails;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.utils.KeyConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragment extends Fragment {

    private static final String TAG = IngredientFragment.class.getSimpleName();

    @BindView(R.id.ingredient_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private boolean twoPane = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twoPane = getResources().getBoolean(R.bool.istablet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredient, container, false);
        ButterKnife.bind(this, rootView);

        setUpToolBar();
        setUpRecyclerView();
        return rootView;
    }

    private void setUpToolBar() {

        toolbar.setTitle(R.string.ingredients_header);
        if (!twoPane) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(v -> {
                try {
                    getActivity().onBackPressed();
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            });
        }
    }

    private void setUpRecyclerView() {
        IngredientsRecyclerAdapter ingredientsRecyclerAdapter =
                new IngredientsRecyclerAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(ingredientsRecyclerAdapter);
    }

}
