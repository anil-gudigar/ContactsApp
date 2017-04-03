package com.mcma.dagger;

import com.mcma.network.manager.RetrofitManager;
import com.mcma.test.ContactCreateInstrumentedTest;
import com.mcma.test.ContactDetailsInstrumentedTest;
import com.mcma.test.ContactListInstrumentedTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anil on 2/13/2017.
 */
@Singleton
@Component(modules = {TestModule.class, RetrofitManager.class})
public interface TestComponent {
    void inject(ContactListInstrumentedTest test1);
    void inject(ContactCreateInstrumentedTest test2);
    void inject(ContactDetailsInstrumentedTest test3);
}
