package com.mcma.test;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.mcma.*;
import com.mcma.R;
import com.mcma.api.client.APIRequestClient;
import com.mcma.api.manager.RetrofitManager;
import com.mcma.app.constants.AppConstants;
import com.mcma.app.dagger.NetComponent;
import com.mcma.callbacks.GetContactListCallback;
import com.mcma.dagger.DaggerTestComponent;
import com.mcma.dagger.TestComponent;
import com.mcma.dagger.TestModule;
import com.mcma.data.TestData;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.modules.contacts.presenter.ContactPresenterImpl;
import com.mcma.utils.NetworkError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.subscribers.TestSubscriber;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ContactListInstrumentedTest {
    private MockWebServer mMockWebServer;
    @Rule
    public ActivityTestRule<HomeActivity> mContactListIntentRule =
            new ActivityTestRule<>(HomeActivity.class, true, false);
    @Inject
    ContactPresenterImpl contactPresenter;

    @Inject
    APIRequestClient mApiRequestClient;


    @Before
    public void setUp() throws Exception {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        TestComponent component = DaggerTestComponent.builder()
                .testModule(new TestModule())
                .retrofitManager(new RetrofitManager(mMockWebServer.url("/").toString()))
                .build();

        component.inject(this);
    }

    @Test
    public void onNetworkError() throws Exception {
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_LIST_JSON_NETWORK_ERROR));
        mContactListIntentRule.launchActivity(null);
        mApiRequestClient.requestContactList(new GetContactListCallback() {
            @Override
            public void onSuccess(List<ContactEntityModel> contactEntities) {
                Log.i(AppConstants.APP_TAG, "OnSucess :" + contactEntities.size());
                mContactListIntentRule.getActivity().onContactListSuccess(contactEntities);
            }

            @Override
            public void onError(NetworkError networkError) {
                Log.i(AppConstants.APP_TAG, "Failure :");
                mContactListIntentRule.getActivity().onFailure(networkError.getAppErrorMessage());
            }
        });
        // Ask the server for its URL. You'll need this to make HTTP requests.
        RecordedRequest request = mMockWebServer.takeRequest();
        assertEquals(AppConstants.API_CONSTANTS.REQUEST_CONTACTS, request.getPath());
        assertEquals("GET", request.getMethod());
        assertEquals("", request.getBody().readUtf8());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(NetworkError.NETWORK_ERROR_MESSAGE))).check(matches(isDisplayed()));
    }

    @Test
    public void showContactsNoData() throws Exception {
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_LIST_JSON_NODATA));
        mContactListIntentRule.launchActivity(null);
        mApiRequestClient.requestContactList(new GetContactListCallback() {
            @Override
            public void onSuccess(List<ContactEntityModel> contactEntities) {
                Log.i(AppConstants.APP_TAG, "OnSucess :" + contactEntities.size());
                mContactListIntentRule.getActivity().onContactListSuccess(contactEntities);
            }

            @Override
            public void onError(NetworkError networkError) {
                Log.i(AppConstants.APP_TAG, "Failure :");
                mContactListIntentRule.getActivity().onFailure(networkError.getAppErrorMessage());
            }
        });
        // Ask the server for its URL. You'll need this to make HTTP requests.
        RecordedRequest request = mMockWebServer.takeRequest();
        assertEquals(AppConstants.API_CONSTANTS.REQUEST_CONTACTS, request.getPath());
        assertEquals("GET", request.getMethod());
        assertEquals("", request.getBody().readUtf8());
        onView(withId(R.id.no_contacts_view)).check(matches(isDisplayed()));
    }


    @Test
    public void showContactsSuccess() throws Exception {
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_LIST_SMALL_SET_JSON));
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_SMALL_SET_22));
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_SMALL_SET_222));
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_SMALL_SET_14));
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_SMALL_SET_74));
        mMockWebServer.enqueue(new MockResponse().setBody(TestData.CONTACT_SMALL_SET_28));
        mContactListIntentRule.launchActivity(null);
        mApiRequestClient.requestContactList(new GetContactListCallback() {
            @Override
            public void onSuccess(List<ContactEntityModel> contactEntities) {
                Log.i(AppConstants.APP_TAG, "OnSucess :" + contactEntities.size());
                mContactListIntentRule.getActivity().onContactListSuccess(contactEntities);
            }

            @Override
            public void onError(NetworkError networkError) {
                mContactListIntentRule.getActivity().onFailure(networkError.getAppErrorMessage());
            }
        });
        // Ask the server for its URL. You'll need this to make HTTP requests.
        RecordedRequest request = mMockWebServer.takeRequest();
        assertEquals(AppConstants.API_CONSTANTS.REQUEST_CONTACTS, request.getPath());
        assertEquals("GET", request.getMethod());
        assertEquals("", request.getBody().readUtf8());
        onView(withId(com.mcma.R.id.favorite_contact_list_view)).check(matches(isDisplayed()));
        onView(withId(com.mcma.R.id.contact_list_view)).check(matches(isDisplayed()));
    }


    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }


}
