package com.mani.baking.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.activity.ItemDetailActivity;
import com.mani.baking.activity.ItemDetailFragment;
import com.mani.baking.activity.ItemListActivity;
import com.mani.baking.datastruct.StepDetails;
import com.mani.baking.utils.KeyConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepsRecyclerAdapter extends RecyclerView.Adapter<StepsRecyclerAdapter.ViewHolder> {

    private final ItemListActivity parentActivity;
    private final ArrayList<StepDetails> stepDetailsList;
    private final boolean twoPane;

    private void startFragment(int position) {
       // Bundle arguments = new Bundle();
       // arguments.putParcelable(KeyConstants.RECIPE, stepDetailsList.get(position));
        if (twoPane) {
            ItemDetailFragment fragment = new ItemDetailFragment();
            //fragment.setArguments(arguments);
            parentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(parentActivity, ItemDetailActivity.class);
          //  intent.putExtra(KeyConstants.RECIPE, arguments);
            parentActivity.startActivity(intent);
        }
    }

    public StepsRecyclerAdapter(ItemListActivity parent, List<StepDetails> stepDetails, boolean twoPane) {
        this.stepDetailsList = (ArrayList) stepDetails;
        this.parentActivity = parent;
        this.twoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(stepDetailsList.get(position).getShortDescribtion());
    }

    @Override
    public int getItemCount() {
        return stepDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.content);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            startFragment(getAdapterPosition());
        }
    }


}
