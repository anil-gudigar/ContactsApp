package com.mcma.test;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.mcma.*;
import com.mcma.R;
import com.mcma.network.client.APIRequestClient;
import com.mcma.network.manager.RetrofitManager;
import com.mcma.app.constants.AppConstants;
import com.mcma.callbacks.GetContactListCallback;
import com.mcma.dagger.DaggerTestComponent;
import com.mcma.dagger.TestComponent;
import com.mcma.dagger.TestModule;
import com.mcma.data.TestData;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.modules.contacts.presenter.ContactPresenterImpl;
import com.mcma.utils.NetworkError;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactDetailsInstrumentedTest {
    private MockWebServer mMockWebServer;
    ContactEntityModel contactEntityModel;
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
    public void contactDetailsTest() throws Exception {

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
                contactEntityModel = contactEntities.get(2);
                mContactListIntentRule.getActivity().onContactListSuccess(contactEntities);
                onView(withId(com.mcma.R.id.favorite_contact_list_view)).check(matches(isDisplayed()));
                onView(withId(com.mcma.R.id.contact_list_view)).check(matches(isDisplayed()));
                ViewInteraction recyclerView = onView(
                        allOf(withId(com.mcma.R.id.contact_list_view), isDisplayed()));
                recyclerView.perform(actionOnItemAtPosition(2, click()));
                onView(allOf(ViewMatchers.withId(com.mcma.R.id.tvNumber1), isDisplayed()));
                onView(withId(com.mcma.R.id.tvNumber1)).check(matches(withText(contactEntityModel.getPhone_number())));
                onView(withId(R.id.tvEmail)).check(matches(withText(contactEntityModel.getEmail())));
                ViewInteraction toggleButton = onView(
                        allOf(withId(com.mcma.R.id.favorite),
                                withParent(allOf(withId(com.mcma.R.id.toolbar),
                                        withParent(withId(com.mcma.R.id.toolbar_layout)))),
                                isDisplayed()));
                toggleButton.perform(click());
                pressBack();
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
    }

    @Test
    public void contactDetailsFavoriteTest() throws Exception {

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
                contactEntityModel = contactEntities.get(2);
                mContactListIntentRule.getActivity().onContactListSuccess(contactEntities);
                onView(withId(com.mcma.R.id.favorite_contact_list_view)).check(matches(isDisplayed()));
                onView(withId(com.mcma.R.id.contact_list_view)).check(matches(isDisplayed()));
                ViewInteraction recyclerView = onView(
                        allOf(withId(com.mcma.R.id.contact_list_view), isDisplayed()));
                recyclerView.perform(actionOnItemAtPosition(2, click()));
                onView(allOf(ViewMatchers.withId(com.mcma.R.id.tvNumber1), isDisplayed()));
                onView(withId(com.mcma.R.id.tvNumber1)).check(matches(withText(contactEntityModel.getPhone_number())));
                onView(withId(R.id.tvEmail)).check(matches(withText(contactEntityModel.getEmail())));
                ViewInteraction toggleButton = onView(
                        allOf(withId(com.mcma.R.id.favorite),
                                withParent(allOf(withId(com.mcma.R.id.toolbar),
                                        withParent(withId(com.mcma.R.id.toolbar_layout)))),
                                isDisplayed()));
                toggleButton.perform(click());
                pressBack();
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
    }
}
