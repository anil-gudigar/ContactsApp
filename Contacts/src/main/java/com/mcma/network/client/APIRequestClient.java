package com.mcma.network.client;


import com.mcma.network.repository.ContactDataRepository;
import com.mcma.callbacks.CreateContactCallback;
import com.mcma.callbacks.GetContactCallback;
import com.mcma.callbacks.GetContactListCallback;
import com.mcma.callbacks.UpdateContactCallback;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.utils.NetworkError;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by anil on 2/2/2017.
 */

public class APIRequestClient {
    private final ContactDataRepository mContactDataRepository;

    /**
     * Retrofit Request Client
     *
     * @param contactDataRepository @see {@link ContactDataRepository}
     */
    @Inject
    public APIRequestClient(ContactDataRepository contactDataRepository) {
        mContactDataRepository = contactDataRepository;
    }

    /**
     * API call to get all contact i.e GET /contacts.json
     *
     * @param callback callback for success / failure @see {@link GetContactListCallback}
     */
    public Disposable requestContactList(final GetContactListCallback callback) {
        return mContactDataRepository.requestContactListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ContactEntityModel>>() {
                                   @Override
                                   public void onNext(List<ContactEntityModel> contactListResponse) {
                                       callback.onSuccess(contactListResponse);
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       callback.onError(new NetworkError(e));
                                   }

                                   @Override
                                   public void onComplete() {

                                   }
                               }
                );
    }


    /**
     * API call to get individual contact detail i.e GET /contacts/{id}.json
     *
     * @param id       identifier for contact
     * @param callback callback for success / failure  @see {@link GetContactCallback}
     */
    public Disposable requestContact(final String id, final GetContactCallback callback) {

        return mContactDataRepository.requestContactObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ContactEntityModel>() {

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onNext(ContactEntityModel contactEntityModel) {
                        callback.onSuccess(contactEntityModel);

                    }
                });
    }

    /**
     * API call to create the new contacts i.e POST /contacts.json
     *
     * @param contactEntityModel contact details  @see {@link ContactEntityModel}
     * @param callback           callback for success / failure  @see {@link CreateContactCallback}
     */
    public Disposable createContact(ContactEntityModel contactEntityModel, final CreateContactCallback callback) {
        return mContactDataRepository.createContactObservable(contactEntityModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    /**
     * API call to update the contacts i.e PUT /contacts/{id}.json
     *
     * @param contactEntityModel contact details @see {@link ContactEntityModel}
     * @param callback           callback for success / failure
     */
    public Disposable updateContact(ContactEntityModel contactEntityModel, final UpdateContactCallback callback) {
        return mContactDataRepository.updateContactObservable(contactEntityModel.getContact_id(), contactEntityModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        callback.onSuccess(response);
                    }
                });
    }

}
