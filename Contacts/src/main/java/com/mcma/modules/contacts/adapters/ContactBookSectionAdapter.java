package com.mcma.modules.contacts.adapters;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mcma.app.BaseViewHolder;
import com.mcma.app.constants.AppConstants;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.modules.contacts.presenter.ContactPresenterImpl;

/**
 * Created by anil on 2/8/2017.
 */

public class ContactBookSectionAdapter extends SectionCursorRecyclerViewAdapter<BaseViewHolder> {

    private final int layoutId;
    private ContactPresenterImpl contactPresenter;

    public ContactBookSectionAdapter(Context context, int layoutId, Cursor cursor, ContactPresenterImpl contactPresenter,boolean isFavotiteAdapter) {
        super(context, cursor, contactPresenter,isFavotiteAdapter);
        this.contactPresenter = contactPresenter;
        this.layoutId = layoutId;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, Cursor cursor) {
        Object obj = getObjForPosition(cursor);
        viewHolder.bind(obj, contactPresenter);
    }

    @Override
    protected Object getSectionFromCursor(Cursor cursor) {
        Object obj =  getObjForPosition(cursor);
        String Index = ((ContactEntityModel)obj).getFirst_name().substring(0, 1);
       // Log.i(AppConstants.APP_TAG, "Index :"+Index);
        return Index;
    }

    @Override
    protected Object getObjForPosition(Cursor cursor) {
        ContactEntityModel contactEntityModel = new ContactEntityModel();
        contactEntityModel.load(cursor);
        return contactEntityModel;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return layoutId;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SECTION) {
            Log.i(AppConstants.APP_TAG, "VIEW_TYPE_SECTION true :");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false);
            return new BaseViewHolder(binding,true);
        } else {
            Log.i(AppConstants.APP_TAG, "VIEW_TYPE_NORMAL true :");
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false);
            return new BaseViewHolder(binding,false);
        }
    }
}
