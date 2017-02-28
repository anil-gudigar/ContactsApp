package com.mcma.modules.contacts.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcma.BR;
import com.mcma.HomeActivity;
import com.mcma.R;
import com.mcma.app.BaseFragment;
import com.mcma.models.contacts.ContactEntityModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ContactDetailFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    public static final String ARG_CONTACT_ENTITY = "contactentity";

    // TODO: Rename and change types of parameters
    private ContactEntityModel contactEntityModel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tvNumber1)
    TextView tvNumber1;

    private OnFragmentInteractionListener mListener;

    public ContactDetailFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ContactDetailFragment.
     */
    public static ContactDetailFragment newInstance() {
        ContactDetailFragment fragment = new ContactDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactEntityModel = (ContactEntityModel) getArguments().get(ARG_CONTACT_ENTITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_detail, container, false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (null != ((AppCompatActivity) getActivity()).getSupportActionBar()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (null != contactEntityModel) {
            if (null != contactEntityModel) {
                getActivity().setTitle(contactEntityModel.getFirst_name() + " " + contactEntityModel.getLast_name());
                binding.setVariable(BR.obj, contactEntityModel);
                binding.setVariable(BR.presenter, ((HomeActivity) getActivity()).getContactPresenter());
            }
        }
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
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public void onFailure(String message) {
        hideProgress();
    }
}
