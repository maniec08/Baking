package com.mani.baking.activity;

import android.os.Bundle;
import android.os.Parcelable;

import com.mani.baking.R;
import com.mani.baking.utils.KeyConstants;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.item_detail);
        ButterKnife.bind(this);
        IngredientFragment ingredientFragment = new IngredientFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.item_detail_container,ingredientFragment)
                .commit();
    }
}
