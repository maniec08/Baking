package com.mani.baking.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IngredientListViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewFactory(this);
    }

}
