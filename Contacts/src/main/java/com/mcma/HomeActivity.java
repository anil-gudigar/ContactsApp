package com.mcma;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcma.app.base.BaseActivity;
import com.mcma.app.MCMAContactsBookApplication;
import com.mcma.app.constants.AppConstants;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.modules.contacts.fragment.ContactDetailFragment;
import com.mcma.modules.contacts.fragment.ContactListFragment;
import com.mcma.modules.contacts.fragment.NewContactFragment;
import com.mcma.modules.contacts.presenter.ContactPresenterImpl;
import com.mcma.modules.contacts.presenter.ContactsPresenterView;
import com.mcma.utils.GenericUtilities;

import java.io.File;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class HomeActivity extends BaseActivity implements ContactsPresenterView, ContactListFragment.OnFragmentInteractionListener, NewContactFragment.OnFragmentInteractionListener, ContactDetailFragment.OnFragmentInteractionListener {
    private static final int LOAD_DATA = 1;
    public static final String MESSAGE_RFC822 = "message/rfc822";
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_PHONE = 111;
    public static final String SEND_MAIL = "Send mail...";
    public static final String TEXT_X_VCARD = "text/x-vcard";
    public static final String TEXT_PLAIN = "text/plain";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragTransaction;
    private boolean isContactsLoadedtoBD = false;
    @Inject
    ContactPresenterImpl contactPresenter;
    private ContactListFragment mContactListFragment = null;
    private NewContactFragment mNewContactFragment = null;
    private ContactDetailFragment mContactDetailFragment = null;
    private boolean isContactListShown = true, isContactDetailsShown = false, isNewContactShown = false;
    private String tempPhonenumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((MCMAContactsBookApplication) getApplication()).getNetComponent().inject(this);
        loadFragments();
    }

    /**
     * Load ContactList Fragment
     */
    private void loadFragments() {
        Log.i(AppConstants.APP_TAG, "loadFragments");
        isContactDetailsShown = false;
        isContactListShown = true;
        isNewContactShown = false;

        invalidateOptionsMenu();
        if (null == mContactListFragment) {
            mContactListFragment = ContactListFragment.newInstance();
        }
        fragmentManager = getSupportFragmentManager();
        if (null != fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.frag_container)).commit();
        }
        fragTransaction = fragmentManager.beginTransaction();
        fragTransaction.add(R.id.frag_container, mContactListFragment);
        fragTransaction.commit();
    }

    /**
     * Load Create Contact Fragment
     */
    public void addNewContactFragment() {
        isContactDetailsShown = false;
        isContactListShown = false;
        isNewContactShown = true;
        mNewContactFragment = NewContactFragment.newInstance();
        invalidateOptionsMenu();
        fragmentManager = getSupportFragmentManager();
        fragTransaction = fragmentManager.beginTransaction();
        fragTransaction.add(R.id.frag_container, mNewContactFragment);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
        Log.i(AppConstants.APP_TAG, "getFragmentManager().getBackStackEntryCount() :" + getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem action_person = menu.findItem(R.id.action_person);
        action_person.setVisible(isContactListShown);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_person) {
            return true;
        }

        if (id == android.R.id.home) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                isContactDetailsShown = false;
                isContactListShown = true;
                isNewContactShown = false;
                invalidateOptionsMenu();
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ContactPresenterImpl getContactPresenter() {
        return contactPresenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactPresenter.detachView();
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isContactListShown && null != mContactListFragment) {
                    mContactListFragment.showProgress();
                }
                if (isNewContactShown && null != mNewContactFragment) {
                    mNewContactFragment.showProgress();
                }
                if (isContactDetailsShown && null != mContactDetailFragment) {
                    mContactDetailFragment.showProgress();
                }
            }
        });
    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isContactListShown && null != mContactListFragment) {
                    mContactListFragment.hideProgress();
                }

                if (isNewContactShown && null != mNewContactFragment) {
                    mNewContactFragment.hideProgress();
                }
                if (isContactDetailsShown && null != mContactDetailFragment) {
                    mContactDetailFragment.showProgress();
                }
            }
        });
    }

    /**
     * to handle all Contact List loaded
     *
     * @param contactEntities
     */
    @Override
    public void onContactListSuccess(final List<ContactEntityModel> contactEntities) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(AppConstants.APP_TAG, "onContactListSuccess :" + contactEntities.size() + " isContactsLoadedtoBD : " + isContactsLoadedtoBD);
                if (contactEntities.size() > 0) {
                    executeTask(contactEntities, LOAD_DATA);
                    if (isContactListShown && null != mContactListFragment)
                        mContactListFragment.onSucces();
                } else {
                    if (isContactListShown && null != mContactListFragment)
                        mContactListFragment.onNoContacts();
                }
            }
        });
    }

    /**
     * Contact Create Success
     *
     * @param contactEntityModel
     */
    @Override
    public void onContactSuccess(ContactEntityModel contactEntityModel) {
        contactEntityModel.save();
    }

    @Override
    public void onCreateContactSuccess(final Response<ResponseBody> response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isNewContactShown && null != mNewContactFragment) {
                    try {
                        if (null != response && response.isSuccessful()) {
                            if (null != response.raw()) {
                                URI uri = new URI(response.raw().networkResponse().request().url().toString());
                                String[] segments = uri.getPath().split("/");
                                String idStr = segments[segments.length - 1];
                                int id = Integer.parseInt(idStr);
                                Log.i(AppConstants.APP_TAG, "HomeActivity -> ResponseBody :" + response.code() + " id:" + id);
                                contactPresenter.getContact(String.valueOf(id));
                            }
                        }
                    } catch (Exception e) {
                        GenericUtilities.handleException(e);
                    }
                    hideProgress();
                    mNewContactFragment.onSucces();
                    Log.i(AppConstants.APP_TAG, "HomeActivity -> onCreateContactSuccess :" + getFragmentManager().getBackStackEntryCount());
                    isContactDetailsShown = false;
                    isContactListShown = true;
                    isNewContactShown = false;
                    invalidateOptionsMenu();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.remove(mNewContactFragment);
                    trans.commit();
                    manager.popBackStack();
                }
            }
        });
    }

    @Override
    public void onCreateContactValidationError() {
        if (isNewContactShown && null != mNewContactFragment) {
            mNewContactFragment.onError();
        }
    }


    @Override
    public void onUpdateContactSuccess(Response<ResponseBody> response) {

    }


    @Override
    public void onFailure(final String message) {
        Log.i(AppConstants.APP_TAG, "HomeActivity -> onFailure :" + message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isNewContactShown && null != mNewContactFragment)
                    mNewContactFragment.onFailure(message);
                if (isContactListShown && null != mContactListFragment)
                    mContactListFragment.onFailure(message);
                if (isContactDetailsShown && null != mContactDetailFragment)
                    mContactDetailFragment.onFailure(message);
            }
        });
    }

    /**
     * to handle Contact List Item Selected
     *
     * @param contactEntityModel
     */
    @Override
    public void onItemSelected(final ContactEntityModel contactEntityModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isContactDetailsShown = true;
                isContactListShown = false;
                isNewContactShown = false;
                invalidateOptionsMenu();
                Bundle args = new Bundle();
                args.putParcelable(ContactDetailFragment.ARG_CONTACT_ENTITY, contactEntityModel);
                fragmentManager = getSupportFragmentManager();
                fragTransaction = fragmentManager.beginTransaction();
                mContactDetailFragment = ContactDetailFragment.newInstance();
                mContactDetailFragment.setArguments(args);
                fragTransaction.add(R.id.frag_container, mContactDetailFragment);
                fragTransaction.addToBackStack(null);
                fragTransaction.commit();
                Log.i(AppConstants.APP_TAG, "getFragmentManager().getBackStackEntryCount() :" + getFragmentManager().getBackStackEntryCount());
            }
        });
    }

    @Override
    public void onSendMessage(String phonenumber) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(AppConstants.SMS, phonenumber, null)));
    }

    @Override
    public void onShareContact(final ContactEntityModel contactEntityModel) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        final AlertDialog sharingDialog = new AlertDialog.Builder(this).
                setTitle(R.string.sharing)
                .setView(dialogView)
                .create();

        TextView shareviaVCF = (TextView) dialogView.findViewById(R.id.vcardshare);
        shareviaVCF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharingDialog.dismiss();
                shareContact(true, contactEntityModel);
            }
        });

        TextView shareviaTEXT = (TextView) dialogView.findViewById(R.id.textshare);
        shareviaTEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharingDialog.dismiss();
                shareContact(false, contactEntityModel);
            }
        });
        sharingDialog.show();
    }

    private void shareContact(boolean isVCF, ContactEntityModel contactEntityModel) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        if (isVCF) {
            File vdfFile = GenericUtilities.createVCF(this, contactEntityModel);
            sendIntent.setType(TEXT_X_VCARD);
            sendIntent.putExtra(Intent.EXTRA_STREAM,
                    Uri.fromFile(vdfFile));
        } else {
            sendIntent.putExtra(Intent.EXTRA_TEXT, GenericUtilities.createText(contactEntityModel).toString());
            sendIntent.setType(TEXT_PLAIN);
        }
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.sharing)));
    }

    @Override
    public void onPlaceCall(String phonenumber) {
        placeCall(phonenumber);
    }

    protected void placeCall(String phonenumber) {
        PackageManager pm = getPackageManager();
        int hasPerm = pm.checkPermission(
                android.Manifest.permission.CALL_PHONE, getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            Log.d(AppConstants.APP_TAG, "User has not permitted.");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_ACCESS_PHONE);
            tempPhonenumber = phonenumber;
        } else {
            Log.d(AppConstants.APP_TAG, "User has permitted.");
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenumber)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(AppConstants.APP_TAG, " yay!  User has permitted.");
                    // permission was granted, yay! Do the
                    if (null != tempPhonenumber)
                        placeCall(tempPhonenumber);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(AppConstants.APP_TAG, " oOPs! User has not permitted.");
                }
                return;
                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }

    @Override
    public void onSendEmail(String email) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType(MESSAGE_RFC822);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        try {
            startActivity(Intent.createChooser(i, SEND_MAIL));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, AppConstants.THERE_ARE_NO_EMAIL_CLIENTS_INSTALLED, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            isContactDetailsShown = false;
            isContactListShown = true;
            isNewContactShown = false;
            invalidateOptionsMenu();
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void update(Object data, int event) {
        switch (event) {
            case LOAD_DATA:
                if (isContactListShown && null != mContactListFragment) {
                    mContactListFragment.hideProgress();
                }
                Log.i(AppConstants.APP_TAG, "----------Load Data Completed----------");
                break;
        }
    }

    @Override
    public int backgroundJob(Object data, int event) {
        try {
            switch (event) {
                case LOAD_DATA:
                    loadData((List<ContactEntityModel>) data);
                    break;
            }
            return 0;
        } catch (Exception e) {
            GenericUtilities.handleException(e);
            return 0;
        }

    }

    /**
     * Load All Contacts by querying the Contact full details and insert into Database.
     *
     * @param contactEntities
     */
    private void loadData(final List<ContactEntityModel> contactEntities) {
        Log.i(AppConstants.APP_TAG, "ContactEntityList :" + contactEntities.size());
        if (!isContactsLoadedtoBD) {
            for (ContactEntityModel contactEntityModel : contactEntities) {
                Log.i(AppConstants.APP_TAG, "Contact id:" + contactEntityModel.getContact_id());
                contactPresenter.getContact(contactEntityModel.getContact_id());
            }
            isContactsLoadedtoBD = true;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Add new Contact callback
     */
    @Override
    public void addNewContact() {
        addNewContactFragment();
    }

    @Override
    public void onLoadComplete() {
        if (isContactListShown && null != mContactListFragment) {
            mContactListFragment.hideProgress();
        }
    }
}
