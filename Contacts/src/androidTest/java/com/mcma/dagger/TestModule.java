package com.mcma.dagger;

import com.mcma.api.client.APIRequestClient;
import com.mcma.api.interfaces.APIRequestInterface;

import org.mockito.Mockito;

import dagger.Module;

/**
 * Created by Anil on 2/13/2017.
 */
@Module
public class TestModule {

    public APIRequestClient providesAPIRequestClient(APIRequestInterface apiRequestInterface) {
        return  Mockito.mock(APIRequestClient.class);
    }

}
