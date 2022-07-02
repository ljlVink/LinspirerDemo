package com.ljlVink.services;

import android.service.quicksettings.TileService;

import com.ljlVink.core.core.HackMdm;

public class QuickStartSvc_backtolsp extends TileService {
    @Override
    public void onClick() {
        new HackMdm(this).backToLSP();
    }
}
