package com.mcma.app.dagger;

import com.mcma.HomeActivity;
import com.mcma.network.manager.RetrofitManager;
import com.mcma.modules.contacts.fragment.ContactDetailFragment;
import com.mcma.modules.contacts.fragment.ContactListFragment;
import com.mcma.modules.contacts.fragment.NewContactFragment;
import com.mcma.modules.contacts.presenter.ContactPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anil on 2/12/2017.
 */
@Singleton
@Component(modules={AppModule.class, RetrofitManager.class})
public interface  NetComponent {
    void inject(HomeActivity activity);
    void inject(NewContactFragment fragment);
    void inject(ContactListFragment fragment);
    void inject(ContactDetailFragment fragment);
    ContactPresenterImpl getContactPresenterImpl();
}
