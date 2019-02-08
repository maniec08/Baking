package com.mani.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.mani.baking.R;
import com.mani.baking.activity.WidgetMainActivity;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.utils.ExtractJson;

public class WidgetProvider extends AppWidgetProvider {

    public static int id = 0;
    private static int intentId = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        new Recipe(context).getList();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        //Intent to be set when 'Next' button is clicked
        Intent nextIntent = WidgetService.getActionNextRecipeIntent(context);
        PendingIntent nextPendingIntent = PendingIntent.getService(context, intentId++, nextIntent, 0);
        views.setOnClickPendingIntent(R.id.next_step_button, nextPendingIntent);

        //Intent to be set when 'Previous' button is clicked
        Intent previousIntent = WidgetService.getActionPreviousRecipeIntent(context);
        PendingIntent previousPendingIntent = PendingIntent.getService(context, intentId++, previousIntent , 0);
        views.setOnClickPendingIntent(R.id.previous_step_button, previousPendingIntent);
        views.setTextViewText(R.id.widget_tv, Recipe.getRecipeDetails(id).getName());
        Intent intent = new Intent(context, IngredientListViewService.class);
        views.setRemoteAdapter(R.id.widget_lv, intent);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context, AppWidgetManager widgetManager, int appWidgetIds[]) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, widgetManager, appWidgetId);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAllWidgets(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}
