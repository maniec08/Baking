package com.mani.baking.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.baking.R;
import com.mani.baking.activity.ItemDetailActivity;
import com.mani.baking.activity.ItemDetailFragment;
import com.mani.baking.activity.ItemListActivity;
import com.mani.baking.utils.SessionData;
import com.mani.baking.utils.SelectionSesionVar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsRecyclerAdapter extends RecyclerView.Adapter<StepsRecyclerAdapter.ViewHolder> {

    private final ItemListActivity parentActivity;
    private final boolean twoPane;

    private void startFragment(int position) {
        SelectionSesionVar.step =position;
        if (twoPane) {
            ItemDetailFragment fragment = new ItemDetailFragment();
            //Updating the selection is used to update player seek position
            ItemDetailFragment.currentSelection = position;
            parentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(parentActivity, ItemDetailActivity.class);
            parentActivity.startActivity(intent);
        }
    }

    public StepsRecyclerAdapter(ItemListActivity parent, boolean twoPane) {
        this.parentActivity = parent;
        this.twoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(
                SessionData.getStepDetails(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return SessionData.getRecipeDetails().getStepDetailsList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.steps_content_tv)
        TextView textView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView );
            textView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            startFragment(getAdapterPosition());
        }
    }
}
