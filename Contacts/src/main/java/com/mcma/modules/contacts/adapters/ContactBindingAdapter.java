package com.mcma.modules.contacts.adapters;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mcma.R;
import com.mcma.app.constants.AppConstants;

/**
 * Created by anil on 2/7/2017.
 */

public class ContactBindingAdapter {
    @BindingAdapter({"bind:profile_pic"})
    public static void loadImage(ImageView imageView, String url)
    {
        Log.i(AppConstants.APP_TAG," url :"+url);
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.default_dp)
                .into(imageView);
    }

    @BindingAdapter({"bind:banner_pic"})
    public static void loadBanner(ImageView imageView, String url)
    {
        Log.i(AppConstants.APP_TAG," url :"+url);
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter({"bind:errorText"})
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            view.setErrorEnabled(false);
        }else{
            view.setErrorEnabled(true);
            view.setError(errorMessage);
        }
    }
}
