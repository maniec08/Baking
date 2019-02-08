package com.mani.baking.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class IngredientListViewService extends RemoteViewsService {

    private static final String TAG = IngredientListViewService.class.getName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewFactory(this);
    }

}
