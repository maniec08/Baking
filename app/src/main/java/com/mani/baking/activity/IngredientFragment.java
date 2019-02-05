package com.mani.baking.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.adapters.IngredientsRecyclerAdapter;
import com.mani.baking.datastruct.IngredientDetails;
import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.utils.KeyConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragment extends Fragment {

    private RecipeDetails recipeDetails;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDetails = getActivity().getIntent().getParcelableExtra(KeyConstants.RECIPE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredient, container, false);
        recyclerView = rootView.findViewById(R.id.ingredient_recycler_view);
        IngredientsRecyclerAdapter ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(recipeDetails.getIngredientDetailsList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(ingredientsRecyclerAdapter);
        return rootView;
    }
}
