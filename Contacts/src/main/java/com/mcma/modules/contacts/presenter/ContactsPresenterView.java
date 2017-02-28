package com.mcma.modules.contacts.presenter;

import com.mcma.models.contacts.ContactEntityModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by anil on 2/7/2017.
 */

public interface ContactsPresenterView {
    void showProgress();

    void hideProgress();

    void onContactListSuccess(List<ContactEntityModel> contactEntities);

    void onContactSuccess(ContactEntityModel contactEntityModel);

    void onCreateContactSuccess(Response<ResponseBody> response);

    void onCreateContactValidationError();

    void onUpdateContactSuccess(Response<ResponseBody> response);

    void onFailure(String message);

    void onItemSelected(ContactEntityModel contactEntityModel);

    void onSendMessage(String phonenumber);

    void onShareContact(ContactEntityModel contactEntityModel);

    void onPlaceCall(String phonenumber);

    void onSendEmail(String email);
}
