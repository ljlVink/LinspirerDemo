package com.ljlVink.services;

import android.content.Intent;
import android.service.quicksettings.TileService;

import com.ljlVink.Activity.NewUI;
import com.ljlVink.core.HackMdm;

public class QuickStartSvc_backtolsp extends TileService {
    @Override
    public void onClick() {
        new HackMdm(this).backToLSP();
    }
}
