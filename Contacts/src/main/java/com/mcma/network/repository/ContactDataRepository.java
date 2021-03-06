package com.mcma.network.repository;

import com.mcma.app.constants.AppConstants;
import com.mcma.models.contacts.ContactEntityModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by anil on 2/2/2017.
 */

public interface ContactDataRepository {

    /**
     * API is used to get country list
     *
     * @return @see {@link Call <AllDocs>}
     */
    @Headers({AppConstants.API_CONSTANTS.CONTENT_TYPE_APPLICATION_JSON})
    @GET(AppConstants.API_CONSTANTS.REQUEST_CONTACTS)
    Observable<List<ContactEntityModel>> requestContactListObservable();

    @Headers({AppConstants.API_CONSTANTS.CONTENT_TYPE_APPLICATION_JSON})
    @GET(AppConstants.API_CONSTANTS.REQUEST_CONTACT)
    Observable<ContactEntityModel> requestContactObservable(@Path(value = AppConstants.API_CONSTANTS.CONTACT_ID) String id);

    @Headers({AppConstants.API_CONSTANTS.CONTENT_TYPE_APPLICATION_JSON})
    @PUT(AppConstants.API_CONSTANTS.REQUEST_CONTACT)
    Observable<Response<ResponseBody>> updateContactObservable(@Path(value = AppConstants.API_CONSTANTS.CONTACT_ID) String id, @Body ContactEntityModel contactEntityModel);

    @Headers({AppConstants.API_CONSTANTS.CONTENT_TYPE_APPLICATION_JSON})
    @POST(AppConstants.API_CONSTANTS.CREATE_CONTACTS)
    Observable<Response<ResponseBody>> createContactObservable(@Body ContactEntityModel contactEntityModel);
}
