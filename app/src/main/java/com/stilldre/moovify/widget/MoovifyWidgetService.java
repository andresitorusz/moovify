package com.stilldre.moovify.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MoovifyWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MoovifyRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
