package com.ljlVink.services;

import android.service.quicksettings.TileService;

import com.ljlVink.core.hackmdm.v2.HackMdm;

public class QuickStartSvc_backtolsp extends TileService {
    @Override
    public void onClick() {
        HackMdm.DeviceMDM.backToLSPDesktop();
    }
}
