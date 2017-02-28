package com.mcma.app;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import com.mcma.BR;

/**
 * Created by anil on 2/6/2017.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding binding;
    private Boolean isIndex = false;
    public BaseViewHolder(ViewDataBinding binding,Boolean isIndex) {
        super(binding.getRoot());
        this.binding = binding;
        this.isIndex = isIndex;
    }

    public void bind(Object obj,Object presenter) {
        binding.setVariable(BR.obj, obj);
        binding.setVariable(BR.presenter,presenter);
        binding.setVariable(BR.isIndex,isIndex);
        binding.executePendingBindings();
    }
}