package com.ljlVink.services;

import android.content.Intent;
import android.service.quicksettings.TileService;

import com.ljlVink.Activity.NewUI;

public class QuickStartSvc_openMain extends TileService {
    @Override
    public void onClick() {
        Intent intent = new Intent(this, NewUI.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityAndCollapse(intent);
    }

}
