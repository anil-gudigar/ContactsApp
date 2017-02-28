package com.mcma.app;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mcma.app.constants.AppConstants;

/**
 * Created by anil on 2/6/2017.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Object mPresenter;

    public BaseAdapter(Object presenter) {
        mPresenter = presenter;
    }

    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(AppConstants.APP_TAG, "onCreateViewHolder :" + viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new BaseViewHolder(binding,false);
    }

    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Log.i(AppConstants.APP_TAG, "onBindViewHolder :" + position);
        Object obj = getObjForPosition(position);
        holder.bind(obj,mPresenter);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);
}