package com.mcma.modules.contacts.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.mcma.network.client.APIRequestClient;
import com.mcma.app.constants.AppConstants;
import com.mcma.callbacks.CreateContactCallback;
import com.mcma.callbacks.GetContactCallback;
import com.mcma.callbacks.GetContactListCallback;
import com.mcma.callbacks.UpdateContactCallback;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.utils.NetworkError;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by anil on 2/6/2017.
 */

public class ContactPresenterImpl implements ContactPresenter<ContactsPresenterView> {
    private ContactsPresenterView mView = null;
    private CompositeDisposable compositeDisposable;
    private APIRequestClient apiRequestClient;

    @Inject
    public ContactPresenterImpl(APIRequestClient apiRequestClient) {
        this.apiRequestClient = apiRequestClient;
    }

    @Override
    public void attachedView(ContactsPresenterView contactsPresenterView, boolean loadContactFromServer) {
        mView = contactsPresenterView;
        this.compositeDisposable = new CompositeDisposable();
        if (loadContactFromServer) {
            getContactList();
        }
    }

    @Override
    public void detachView() {
        onStop();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onItemSelected(ContactEntityModel contactEntityModel) {
        Log.i(AppConstants.APP_TAG, "onItemSelected :" + contactEntityModel.getUrl());
        mView.onItemSelected(contactEntityModel);
    }

    @Override
    public void onContactSaveClicked(ContactEntityModel contactEntityModel) {
        Log.i(AppConstants.APP_TAG, "onContactSaveClicked -> contactEntityModel :" + contactEntityModel.toString());
        if (!isValidFirstname(contactEntityModel.getFirst_name())) {
            contactEntityModel.setFirstValidation("First Name not valid");
            onContactSaveError();
            return;
        }
        if (!isValidPhoneNumber(contactEntityModel.getPhone_number())) {
            contactEntityModel.setFirstValidation("");
            contactEntityModel.setPhoneValidation("Mobile Phone Number not valid");
            onContactSaveError();
            return;
        }
        if (!isValidMail(contactEntityModel.getEmail())) {
            contactEntityModel.setFirstValidation("");
            contactEntityModel.setPhoneValidation("");
            contactEntityModel.setEmailValidation("Email is not valid");
            onContactSaveError();
            return;
        } else {
            contactEntityModel.setFirstValidation("");
            contactEntityModel.setPhoneValidation("");
            contactEntityModel.setEmailValidation("");
            String firstname = new String(contactEntityModel.getFirst_name().trim());
            contactEntityModel.setFirst_name(firstname);
            onContactSaveError();
            mView.showProgress();
            contactEntityModel.setFavorite(false);
            contactEntityModel.setProfile_pic("/images/missing.png");
            createContact(contactEntityModel);
        }
    }

    @Override
    public void onContactSaveError() {
        mView.onCreateContactValidationError();
    }

    @Override
    public void onContactUpdateClicked(ContactEntityModel contactEntityModel) {
        mView.showProgress();
        updateContact(contactEntityModel);
    }

    @Override
    public void onContactSendMessageClick(String phonenumber) {
        mView.onSendMessage(phonenumber);
    }

    @Override
    public void onContactShareContactClick(ContactEntityModel contactEntityModel) {
        mView.onShareContact(contactEntityModel);
    }

    @Override
    public void onPlaceCallClick(String phonenumber) {
        mView.onPlaceCall(phonenumber);
    }

    @Override
    public void onContactEmailClick(String email) {
        mView.onSendEmail(email);
    }


    public void getContactList() {
        mView.showProgress();
        Disposable disposable = apiRequestClient.requestContactList(new GetContactListCallback() {
            @Override
            public void onSuccess(List<ContactEntityModel> contactEntities) {
                Log.i(AppConstants.APP_TAG, "OnSucess :" + contactEntities.size());
                //mView.hideProgress();
                mView.onContactListSuccess(contactEntities);
            }

            @Override
            public void onError(NetworkError networkError) {
                mView.onFailure(networkError.getAppErrorMessage());
            }
        });
        compositeDisposable.add(disposable);
    }

    public void getContact(String id) {
        mView.showProgress();
        Disposable disposable = apiRequestClient.requestContact(id, new GetContactCallback() {
            @Override
            public void onSuccess(ContactEntityModel contactEntity) {
                mView.onContactSuccess(contactEntity);
            }

            @Override
            public void onError(NetworkError networkError) {
                mView.onFailure(networkError.getAppErrorMessage());
            }
        });
        compositeDisposable.add(disposable);
    }

    public void createContact(final ContactEntityModel contactEntityModel) {
        mView.showProgress();
        Disposable disposable = apiRequestClient.createContact(contactEntityModel, new CreateContactCallback() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                mView.onCreateContactSuccess(response);
            }

            @Override
            public void onError(NetworkError networkError) {
                mView.onFailure(networkError.getAppErrorMessage());
            }
        });
        compositeDisposable.add(disposable);
    }

    public void updateContact(final ContactEntityModel contactEntityModel) {
        mView.showProgress();
        Disposable disposable = apiRequestClient.updateContact(contactEntityModel, new UpdateContactCallback() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                contactEntityModel.save();
                mView.onUpdateContactSuccess(response);
            }

            @Override
            public void onError(NetworkError networkError) {
                mView.onFailure(networkError.getAppErrorMessage());
            }
        });
        compositeDisposable.add(disposable);
    }

    public void onStop() {
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public boolean isValidFirstname(String firstname) {
        return (!TextUtils.isEmpty(firstname) && firstname.length() >= 3);
    }

    public boolean isValidMail(String email) {
        return (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isValidPhoneNumber(String phone) {
        String regexStr = "^[+]?[0-9]{12,15}$";
        return (!TextUtils.isEmpty(phone) && phone.matches(regexStr));
    }
}
