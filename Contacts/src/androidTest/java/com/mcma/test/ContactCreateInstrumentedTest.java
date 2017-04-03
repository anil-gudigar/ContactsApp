package com.mcma.test;


import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcma.HomeActivity;
import com.mcma.R;
import com.mcma.network.client.APIRequestClient;
import com.mcma.network.manager.RetrofitManager;
import com.mcma.app.constants.AppConstants;
import com.mcma.callbacks.CreateContactCallback;
import com.mcma.dagger.DaggerTestComponent;
import com.mcma.dagger.TestComponent;
import com.mcma.dagger.TestModule;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.modules.contacts.presenter.ContactPresenterImpl;
import com.mcma.utils.NetworkError;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactCreateInstrumentedTest {
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

        contactEntityModel = new ContactEntityModel();
        contactEntityModel.setFirst_name("aaaxxxxxx");
        contactEntityModel.setLast_name("bbbb");
        contactEntityModel.setFavorite(false);
        contactEntityModel.setPhone_number("99999999999999");
        contactEntityModel.setEmail("aa@aa.com");
    }


    @Test
    public void testFirstNameValidation() throws Exception {
        mContactListIntentRule.launchActivity(null);
        ViewInteraction floatingActionButton = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());
        ViewInteraction appCompatEditText = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.editText_fName), isDisplayed()));
        onView(withId(R.id.textinputlayout_fname)).check(matches(hasTextInputLayoutHintText(mContactListIntentRule.getActivity().getString(R.string.first_name))));
        appCompatEditText.perform(typeText("as"), closeSoftKeyboard());
        ViewInteraction appCompatButton = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.save_btn), withText("Save"), isDisplayed()));
        appCompatButton.perform(click());
        onView(withId(R.id.textinputlayout_fname)).check(matches(hasTextInputLayoutErrorText("First Name not valid")));
    }

    @Test
    public void testPhoneNumberValidation() throws Exception {
        mContactListIntentRule.launchActivity(null);
        ViewInteraction floatingActionButton = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());
        ViewInteraction appCompatEditText = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.editText_fName), isDisplayed()));
        appCompatEditText.perform(typeText("adsdds"), closeSoftKeyboard());
        ViewInteraction appCompatEditText3 = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.editText_phoneNumber), isDisplayed()));
        onView(withId(R.id.textinputlayout_phone)).check(matches(hasTextInputLayoutHintText(mContactListIntentRule.getActivity().getString(R.string.mobile_number))));
        appCompatEditText3.perform(replaceText("5555555"), closeSoftKeyboard());
        ViewInteraction appCompatButton = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.save_btn), withText("Save"), isDisplayed()));
        appCompatButton.perform(click());
        onView(withId(R.id.textinputlayout_phone)).check(matches(hasTextInputLayoutErrorText("Mobile Phone Number not valid")));
    }

    @Test
    public void testEmailValidation() throws Exception {
        mContactListIntentRule.launchActivity(null);
        ViewInteraction floatingActionButton = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());
        ViewInteraction appCompatEditText = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.editText_fName), isDisplayed()));
        appCompatEditText.perform(typeText("adsdds"), closeSoftKeyboard());
        ViewInteraction appCompatEditText3 = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.editText_phoneNumber), isDisplayed()));
        appCompatEditText3.perform(replaceText("555555555555"), closeSoftKeyboard());
        ViewInteraction appCompatEditText4 = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.editText_Email), isDisplayed()));
        onView(withId(R.id.textinputlayout_email)).check(matches(hasTextInputLayoutHintText(mContactListIntentRule.getActivity().getString(R.string.email_address))));
        appCompatEditText4.perform(replaceText("cffgggfyughgh.com"), closeSoftKeyboard());
        ViewInteraction appCompatButton = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.save_btn), withText("Save"), isDisplayed()));
        appCompatButton.perform(click());
        onView(withId(R.id.textinputlayout_email)).check(matches(hasTextInputLayoutErrorText("Email is not valid")));
    }

    @Test
    public void testCreateContactRequest() throws Exception {
        mContactListIntentRule.launchActivity(null);
        ViewInteraction floatingActionButton = onView(
                allOf(ViewMatchers.withId(com.mcma.R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());
        mMockWebServer.enqueue(new MockResponse());
        mApiRequestClient.createContact(contactEntityModel, new CreateContactCallback() {
            @Override
            public void onSuccess(Response<ResponseBody> response) {
                Log.i(AppConstants.APP_TAG, "OnSucess :" + response.message());
                mContactListIntentRule.getActivity().onContactSuccess(contactEntityModel);
            }

            @Override
            public void onError(NetworkError networkError) {
                Log.i(AppConstants.APP_TAG, "Failure :");
                mContactListIntentRule.getActivity().onFailure(networkError.getAppErrorMessage());
            }
        });
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        RecordedRequest request = this.mMockWebServer.takeRequest();
        assertEquals(AppConstants.API_CONSTANTS.CREATE_CONTACTS, request.getPath());
        assertEquals("POST", request.getMethod());
        assertEquals(gson.toJson(this.contactEntityModel).toString(), request.getBody().readUtf8());

    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }

    public Matcher<View> hasTextInputLayoutHintText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getHint();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String errorString = error.toString();

                return expectedErrorText.equals(errorString);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
