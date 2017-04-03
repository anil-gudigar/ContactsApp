package com.mcma;

import android.text.TextUtils;
import android.util.Patterns;

import com.mcma.network.client.APIRequestClient;
import com.mcma.callbacks.CreateContactCallback;
import com.mcma.callbacks.GetContactCallback;
import com.mcma.callbacks.GetContactListCallback;
import com.mcma.callbacks.UpdateContactCallback;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.modules.contacts.presenter.ContactPresenterImpl;
import com.mcma.modules.contacts.presenter.ContactsPresenterView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ContactPresenterUnitTest {

    private ContactPresenterImpl contactPresenter;
    @Mock
    private ContactsPresenterView contactsPresenterView;
    @Mock
    private APIRequestClient apiRequestClient;
    @Mock
    TextUtils textUtils;
    @Mock
    Patterns patterns;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        contactPresenter = new ContactPresenterImpl(apiRequestClient);
        contactPresenter.attachedView(contactsPresenterView, false);
    }

    @Test
    public void onGetContactListSuccess() {
        when(apiRequestClient.requestContactList(any(GetContactListCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.getContactList();
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onContactListSuccess(anyList());
    }

    @Test
    public void onGetContactListFailure() {
        when(apiRequestClient.requestContactList(any(GetContactListCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.getContactList();
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onFailure(anyString());
    }

    @Test
    public void onGetContactSuccess() {
        when(apiRequestClient.requestContact(anyString(), any(GetContactCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.getContact("1");
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onContactSuccess(any(ContactEntityModel.class));
    }

    @Test
    public void onGetContactFailure() {
        when(apiRequestClient.requestContact(anyString(), any(GetContactCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.getContact("1");
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onFailure(anyString());
    }

    @Test
    public void onCreateContactSuccess() {
        when(apiRequestClient.createContact(any(ContactEntityModel.class), any(CreateContactCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.createContact(new ContactEntityModel());
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onCreateContactSuccess(any(Response.class));
    }

    @Test
    public void onCreateContactFailure() {
        when(apiRequestClient.createContact(any(ContactEntityModel.class), any(CreateContactCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.createContact(new ContactEntityModel());
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onFailure(anyString());
    }

    @Test
    public void onUpdateContactSuccess() {
        when(apiRequestClient.updateContact(any(ContactEntityModel.class), any(UpdateContactCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.updateContact(new ContactEntityModel());
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onUpdateContactSuccess(any(Response.class));
    }

    @Test
    public void onUpdateContactFailure() {
        when(apiRequestClient.updateContact(any(ContactEntityModel.class), any(UpdateContactCallback.class))).thenReturn(new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        });
        contactPresenter.updateContact(new ContactEntityModel());
        verify(contactsPresenterView).showProgress();
        verify(contactsPresenterView).onFailure(anyString());
    }


    @Test
    public void onCreateContactValidationTest() {
        assertEquals(contactPresenter.isValidFirstname(""), false);
        assertEquals(contactPresenter.isValidPhoneNumber(""), false);
        //assertEquals(contactPresenter.isValidMail(""), false);

        assertEquals(contactPresenter.isValidFirstname("aa"), false);
        assertEquals(contactPresenter.isValidPhoneNumber("999999"), false);
        //assertEquals(contactPresenter.isValidMail("aaaa.com"), false);

        assertEquals(contactPresenter.isValidFirstname("aaa"), true);
        assertEquals(contactPresenter.isValidPhoneNumber("+91998012341234"), true);
        assertEquals(contactPresenter.isValidPhoneNumber("998012341234"), true);
        assertEquals(contactPresenter.isValidPhoneNumber("0998012341234"), true);
        // assertEquals(contactPresenter.isValidMail("aaaa.@ccc.com"), true);
    }


}
