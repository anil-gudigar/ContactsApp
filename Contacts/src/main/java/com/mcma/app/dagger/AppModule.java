package com.mcma.app.dagger;

import com.mcma.app.MCMAContactsBookApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Anil on 2/12/2017.
 */
@Module
public class AppModule {

    MCMAContactsBookApplication mApplication;

    public AppModule(MCMAContactsBookApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    MCMAContactsBookApplication providesApplication() {
        return mApplication;
    }
}
