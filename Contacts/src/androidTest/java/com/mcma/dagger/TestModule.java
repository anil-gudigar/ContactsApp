package com.mcma.dagger;

import com.mcma.network.client.APIRequestClient;
import com.mcma.network.repository.ContactDataRepository;

import org.mockito.Mockito;

import dagger.Module;

/**
 * Created by Anil on 2/13/2017.
 */
@Module
public class TestModule {

    public APIRequestClient providesAPIRequestClient(ContactDataRepository contactDataRepository) {
        return  Mockito.mock(APIRequestClient.class);
    }

}
