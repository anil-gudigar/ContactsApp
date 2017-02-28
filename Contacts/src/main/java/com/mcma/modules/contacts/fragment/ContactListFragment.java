package com.mcma.modules.contacts.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.content.ContentProvider;
import com.mcma.HomeActivity;
import com.mcma.R;
import com.mcma.app.BaseFragment;
import com.mcma.app.constants.AppConstants;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.modules.contacts.adapters.ContactBookSectionAdapter;
import com.mcma.provider.ContactContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ContactListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CONTACT_LIST_LOADER = 1;
    private static final int FAVORITE_LIST_LOADER = 2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.contact_list_view_referesh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.contact_list_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.favorite_contact_list_view)
    RecyclerView mFavoriteRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.contact_count)
    TextView contact_count;

    @BindView(R.id.no_contacts_view)
    RelativeLayout no_contacts_view;


    private ContactBookSectionAdapter contactBookSectionAdapter = null;
    private ContactBookSectionAdapter contactBookFavoriteSectionAdapter = null;


    public ContactListFragment() {
        // Required empty public constructor

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ContactDetailFragment.
     */
    public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.R.color.transparent);
        }
        setupRecyclerView();
        setupFavoriteRecyclerView();
        getActivity().getSupportLoaderManager().initLoader(CONTACT_LIST_LOADER, null, this);
        getActivity().getSupportLoaderManager().initLoader(FAVORITE_LIST_LOADER, null, this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.fab)
    public void addNewContact(View view) {
        mListener.addNewContact();
    }

    @Override
    protected void update(Object data, int event) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        // TODO: Update argument type and name
        void addNewContact();

        // TODO: Update argument type and name
        void onLoadComplete();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String orderBy = ContactContract.ContactEntity.FIRST_NAME + " ASC";
        switch (id) {
            case CONTACT_LIST_LOADER:
                Log.i(AppConstants.APP_TAG, "--------------------------- onCreateLoader -------------------------");
                return new CursorLoader(getActivity(), ContentProvider.createUri(ContactEntityModel.class, null), null, null, null, orderBy);
            case FAVORITE_LIST_LOADER:
                Log.i(AppConstants.APP_TAG, "--------------------------- onCreateLoader -------------------------");
                String[] value = {"1"};
                return new CursorLoader(getActivity(), ContentProvider.createUri(ContactEntityModel.class, null), null, ContactContract.ContactEntity.FAVORITE + "=?", value, orderBy);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListener.onLoadComplete();
        switch (loader.getId()) {
            case CONTACT_LIST_LOADER:
                Log.i(AppConstants.APP_TAG, "---------------------------CONTACT_LIST_LOADER onLoadFinished -------------------------");
                if (null != data) {
                    if (data.getCount() > 0) {
                        Log.i(AppConstants.APP_TAG, "--------------------------- CONTACT_LIST_LOADER onLoadFinished - data not null:" + data.getCount());
                        hideProgress();
                        contactBookSectionAdapter.swapCursor(data);
                        contact_count.setText(getResources().getString(R.string.contact_count, String.valueOf(data.getCount())));
                    } else {
                        Log.i(AppConstants.APP_TAG, "---------------------------  loading from server");
                        ((HomeActivity) getActivity()).getContactPresenter().attachedView(((HomeActivity) getActivity()), true);
                    }
                } else {
                    Log.i(AppConstants.APP_TAG, "--------------------------- CONTACT_LIST_LOADER onLoadFinished - data null");
                }
                break;
            case FAVORITE_LIST_LOADER:
                Log.i(AppConstants.APP_TAG, "--------------------------- FAVORITE_LIST_LOADER onLoadFinished -------------------------");
                if (null != data) {
                    if (data.getCount() > 0) {
                        Log.i(AppConstants.APP_TAG, "--------------------------- FAVORITE_LIST_LOADER onLoadFinished - data not null:" + data.getCount());
                        contactBookFavoriteSectionAdapter.swapCursor(data);
                    }
                } else {
                    Log.i(AppConstants.APP_TAG, "--------------------------- FAVORITE_LIST_LOADER onLoadFinished - data null");
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case CONTACT_LIST_LOADER:
                Log.i(AppConstants.APP_TAG, "---------------------------CONTACT_LIST_LOADER  onLoaderReset -------------------------");
                break;
            case FAVORITE_LIST_LOADER:
                Log.i(AppConstants.APP_TAG, "---------------------------FAVORITE_LIST_LOADER  onLoaderReset -------------------------");
                break;

        }
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((HomeActivity) getActivity()).getContactPresenter().attachedView(((HomeActivity) getActivity()), false);
        contactBookSectionAdapter = new ContactBookSectionAdapter(getActivity(), R.layout.layout_contact_item, null, ((HomeActivity) getActivity()).getContactPresenter(), false);
        mRecyclerView.setAdapter(contactBookSectionAdapter);
        contactBookSectionAdapter.notifyDataSetChanged();
        mRecyclerView.invalidate();
        mRecyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                ((HomeActivity) getActivity()).getContactPresenter().attachedView(((HomeActivity) getActivity()), true);
            }
        });
    }

    private void setupFavoriteRecyclerView() {
        mFavoriteRecyclerView.setHasFixedSize(true);
        mFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactBookFavoriteSectionAdapter = new ContactBookSectionAdapter(getActivity(), R.layout.layout_favorite_contact_item, null, ((HomeActivity) getActivity()).getContactPresenter(), true);
        mFavoriteRecyclerView.setAdapter(contactBookFavoriteSectionAdapter);
        contactBookFavoriteSectionAdapter.notifyDataSetChanged();
        mFavoriteRecyclerView.invalidate();
        mFavoriteRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    public void onSucces() {
        no_contacts_view.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mFavoriteRecyclerView.setVisibility(View.VISIBLE);
    }
    public void onNoContacts() {
        hideProgress();
        no_contacts_view.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mFavoriteRecyclerView.setVisibility(View.GONE);
    }

    public void onFailure(String message) {
        hideProgress();
        //TODO:make Duration to Snackbar.LENGTH_LONG to pass  the onNetworkError TestCase.
        Snackbar.make(fab,message,Snackbar.LENGTH_SHORT).show();
    }
}
