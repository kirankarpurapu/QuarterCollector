package com.example.kirank.quartercollector;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by kirank on 10/14/17.
 */

public class CoinCollector extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
        // add for verbose logging
         FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }
}
