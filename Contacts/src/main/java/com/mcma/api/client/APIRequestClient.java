package com.mcma.api.client;

import com.mcma.api.interfaces.APIRequestInterface;
import com.mcma.callbacks.CreateContactCallback;
import com.mcma.callbacks.GetContactCallback;
import com.mcma.callbacks.GetContactListCallback;
import com.mcma.callbacks.UpdateContactCallback;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.utils.NetworkError;

import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by anil on 2/2/2017.
 */

public class APIRequestClient {
    private final APIRequestInterface mAPIRequestInterface;

    /**
     * Retrofit Request Client
     * @param apiRequestInterface @see {@link APIRequestInterface}
     */
    @Inject
    public APIRequestClient(APIRequestInterface apiRequestInterface) {
        mAPIRequestInterface = apiRequestInterface;
    }

    /**
     * API call to get all contact i.e GET /contacts.json
     * @param callback callback for success / failure @see {@link GetContactListCallback}
     * @return Subscription @see {@link Subscription}
     */
    public Subscription requestContactList(final GetContactListCallback callback) {

        return mAPIRequestInterface.requestContactList()
                .subscribeOn(rx.schedulers.Schedulers.io())
                .onErrorResumeNext(new Func1<Throwable, rx.Observable<? extends List<ContactEntityModel>>>() {
                    @Override
                    public rx.Observable<? extends List<ContactEntityModel>> call(Throwable throwable) {
                        return rx.Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<List<ContactEntityModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(List<ContactEntityModel> contactListResponse) {
                        callback.onSuccess(contactListResponse);

                    }
                });
    }

    /**
     * API call to get individual contact detail i.e GET /contacts/{id}.json
     * @param id identifier for contact
     * @param callback callback for success / failure  @see {@link GetContactCallback}
     * @return Subscription @see {@link Subscription}
     */
    public Subscription requestContact(final String id, final GetContactCallback callback) {

        return mAPIRequestInterface.requestContact(id)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .onErrorResumeNext(new Func1<Throwable, rx.Observable<? extends ContactEntityModel>>() {
                    @Override
                    public rx.Observable<? extends ContactEntityModel> call(Throwable throwable) {
                        return rx.Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ContactEntityModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ContactEntityModel contactEntityModel) {
                        callback.onSuccess(contactEntityModel);

                    }
                });
    }

    /**
     * API call to create the new contacts i.e POST /contacts.json
     * @param contactEntityModel contact details  @see {@link ContactEntityModel}
     * @param callback callback for success / failure  @see {@link CreateContactCallback}
     * @return Subscription @see {@link Subscription}
     */
    public Subscription createContact(ContactEntityModel contactEntityModel, final CreateContactCallback callback) {

        return mAPIRequestInterface.createContact(contactEntityModel)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .onErrorResumeNext(new Func1<Throwable, rx.Observable<? extends Response<ResponseBody>>>() {
                    @Override
                    public rx.Observable<? extends Response<ResponseBody>> call(Throwable throwable) {
                        return rx.Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    /**
     * API call to update the contacts i.e PUT /contacts/{id}.json
     * @param contactEntityModel contact details @see {@link ContactEntityModel}
     * @param callback callback for success / failure
     * @return Subscription @see {@link Subscription}
     */
    public Subscription updateContact(ContactEntityModel contactEntityModel, final UpdateContactCallback callback) {

        return mAPIRequestInterface.updateContact(contactEntityModel.getContact_id(),contactEntityModel)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .onErrorResumeNext(new Func1<Throwable, rx.Observable<? extends Response<ResponseBody>>>() {
                    @Override
                    public rx.Observable<? extends Response<ResponseBody>> call(Throwable throwable) {
                        return rx.Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        callback.onSuccess(response);
                    }
                });
    }

}
