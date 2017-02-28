package com.mcma.callbacks;

import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.utils.NetworkError;

import java.util.List;

/**
 * Created by anil on 2/6/2017.
 */

public interface GetContactListCallback {
    void onSuccess(List<ContactEntityModel> contactEntities);

    void onError(NetworkError networkError);
}
