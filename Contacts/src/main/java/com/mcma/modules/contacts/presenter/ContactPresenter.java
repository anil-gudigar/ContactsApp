package com.mcma.modules.contacts.presenter;

import com.mcma.models.contacts.ContactEntityModel;

/**
 * Created by anil on 2/6/2017.
 */

public interface ContactPresenter<V> {

    void attachedView(V view, boolean loadContactFromServer);

    void detachView();

    void onResume();

    void onItemSelected(ContactEntityModel contactEntityModel);

    void onContactSaveClicked(ContactEntityModel contactEntityModel);

    void onContactSaveError();

    void onContactUpdateClicked(ContactEntityModel contactEntityModel);

    void onContactSendMessageClick(String phonenumber);

    void onContactShareContactClick(ContactEntityModel contactEntityModel);

    void onPlaceCallClick(String phonenumber);

    void onContactEmailClick(String email);
}
