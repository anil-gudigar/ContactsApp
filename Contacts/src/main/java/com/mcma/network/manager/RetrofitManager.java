package com.mcma.network.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcma.network.client.APIRequestClient;
import com.mcma.network.repository.ContactDataRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * RetrofitManager to handle all API calls of RESTFull webservice.
 * Created by anil on 2/2/2017.
 */
@Module
public class RetrofitManager {
    public static final int TIMEOUT = 60;
    public static String mBaseUrl;

    public RetrofitManager(String SERVICE_API_BASE_URL) {
        mBaseUrl = SERVICE_API_BASE_URL;
    }

    /**
     * Retrofit API Service
     *
     * @return @see {@link Retrofit}
     */
    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, ScalarsConverterFactory scalarsConverterFactory, RxJava2CallAdapterFactory rxJava2CallAdapterFactory, OkHttpClient okHttpClient) {
        Retrofit mServiceRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(scalarsConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build();

        return mServiceRetrofit;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    ScalarsConverterFactory provideScalars() {
        return ScalarsConverterFactory.create();
    }


    @Provides
    @Singleton
    RxJava2CallAdapterFactory provideRxJava2CallAdapter() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // setting log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    /**
     * @return
     */
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor logging) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .addInterceptor(logging)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS);

        return client.build();
    }

    @Provides
    @Singleton
    public ContactDataRepository providesAPIRequestInterface(Retrofit retrofit) {
        return retrofit.create(ContactDataRepository.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public APIRequestClient providesAPIRequestClient(ContactDataRepository contactDataRepository) {
        return new APIRequestClient(contactDataRepository);
    }
}
