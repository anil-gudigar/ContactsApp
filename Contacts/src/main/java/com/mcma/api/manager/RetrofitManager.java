package com.mcma.api.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcma.api.client.APIRequestClient;
import com.mcma.api.interfaces.APIRequestInterface;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
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
    Retrofit provideRetrofit(Gson gson, ScalarsConverterFactory scalarsConverterFactory, RxJavaCallAdapterFactory rxJavaCallAdapterFactory, OkHttpClient okHttpClient) {
        Retrofit mServiceRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(scalarsConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
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
    RxJavaCallAdapterFactory provideRxJavaCallAdapter() {
        return RxJavaCallAdapterFactory.create();
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
    public APIRequestInterface providesAPIRequestInterface(Retrofit retrofit) {
        return retrofit.create(APIRequestInterface.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public APIRequestClient providesAPIRequestClient(APIRequestInterface apiRequestInterface) {
        return new APIRequestClient(apiRequestInterface);
    }
}
