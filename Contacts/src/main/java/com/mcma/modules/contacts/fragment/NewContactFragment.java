package com.mcma.modules.contacts.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mcma.BR;
import com.mcma.HomeActivity;
import com.mcma.R;
import com.mcma.app.base.BaseFragment;
import com.mcma.app.constants.AppConstants;
import com.mcma.models.contacts.ContactEntityModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NewContactFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.editText_fName)
    EditText editText_fName;
    @BindView(R.id.editText_lName)
    EditText editText_lName;
    @BindView(R.id.editText_phoneNumber)
    EditText editText_phoneNumber;
    @BindView(R.id.editText_Email)
    EditText editText_Email;
    private ViewDataBinding mBinding;
    private OnFragmentInteractionListener mListener;
    private ContactEntityModel contactEntityModel;

    public NewContactFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ContactDetailFragment.
     */
    public static NewContactFragment newInstance() {
        NewContactFragment fragment = new NewContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_new_contact, container, false);
        View view = mBinding.getRoot();
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (null != ((AppCompatActivity) getActivity()).getSupportActionBar()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        contactEntityModel = new ContactEntityModel();
        mBinding.setVariable(BR.obj, contactEntityModel);
        mBinding.setVariable(BR.presenter, ((HomeActivity) getActivity()).getContactPresenter());
        getActivity().setTitle(getResources().getString(R.string.title_activity_new_contact));
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

    public void onError() {
        mBinding.setVariable(BR.obj, contactEntityModel);
    }

    public void onSucces() {
        Log.i(AppConstants.APP_TAG, "ContactEntityModel :" + contactEntityModel.toString());
        Toast.makeText(getActivity(), getActivity().getString(R.string.user_added), Toast.LENGTH_SHORT).show();
    }

    public void onFailure(String message) {
        hideProgress();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(getActivity().getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((HomeActivity) getActivity()).getContactPresenter().onContactSaveClicked(contactEntityModel);
            }
        });
        builder.show();
    }
}
