package com.mcma.app;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.mcma.BuildConfig;
import com.mcma.network.manager.RetrofitManager;
import com.mcma.app.dagger.AppModule;
import com.mcma.app.dagger.DaggerNetComponent;
import com.mcma.app.dagger.NetComponent;

/**
 * Created by anil on 2/2/2017.
 */

public class MCMAContactsBookApplication extends Application {
    private NetComponent mNetComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        mNetComponent = DaggerNetComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .retrofitManager(new RetrofitManager(BuildConfig.API_BASE_URL))
                .build();
        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
