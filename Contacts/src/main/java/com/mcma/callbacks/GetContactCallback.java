package com.mcma.callbacks;

import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.utils.NetworkError;

/**
 * Created by anil on 2/6/2017.
 */

public interface GetContactCallback {
    void onSuccess(ContactEntityModel contactEntities);

    void onError(NetworkError networkError);
}
