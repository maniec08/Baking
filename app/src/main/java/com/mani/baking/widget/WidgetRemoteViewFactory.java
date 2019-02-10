package com.mani.baking.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mani.baking.R;
import com.mani.baking.datastruct.IngredientDetails;
import com.mani.baking.utils.SessionData;

import java.util.List;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<IngredientDetails> ingredientDetails;

    public WidgetRemoteViewFactory(Context applicationContext) {
        mContext = applicationContext;
        ingredientDetails = SessionData.getRecipeDetails(WidgetProvider.id).getIngredientDetailsList();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
     ingredientDetails = SessionData.getRecipeDetails(WidgetProvider.id).getIngredientDetailsList();
    }

    @Override
    public void onDestroy() {

    }
    @Override
    public int getCount() {
        return ingredientDetails.size();
    }
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingrdient_row_widget);
        rv.setTextViewText(R.id.quantity_tv,ingredientDetails.get(position).getQuantity());
        rv.setTextViewText(R.id.measure_tv,ingredientDetails.get(position).getMeasure());
        rv.setTextViewText(R.id.ingredient_tv,ingredientDetails.get(position).getIngredient());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}