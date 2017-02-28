package com.mcma.models.contacts;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcma.provider.ContactContract;

import java.util.Objects;

/**
 * Created by anil on 2/8/2017.
 */
@Table(name = ContactContract.ContactEntity.TABLE_NAME, id = ContactContract.ContactEntity._ID)
public class ContactEntityModel extends Model implements Parcelable {


    public void load(ContactEntityModel contactEntityModel) {
        contact_id = contactEntityModel.getContact_id();
        first_name = contactEntityModel.getFirst_name();
        last_name = contactEntityModel.getLast_name();
        profile_pic = contactEntityModel.getProfile_pic();
        url = contactEntityModel.getUrl();
        phone_number = contactEntityModel.getPhone_number();
        email = contactEntityModel.getEmail();
        created_at = contactEntityModel.getCreated_at();
        updated_at = contactEntityModel.getUpdated_at();
        favorite = contactEntityModel.getFavorite();
    }

    public void load(Cursor in) {
        if (null != in) {
            contact_id = in.getString(in.getColumnIndex(ContactContract.ContactEntity.CONTACT_ID));
            first_name = in.getString(in.getColumnIndex(ContactContract.ContactEntity.FIRST_NAME));
            last_name = in.getString(in.getColumnIndex(ContactContract.ContactEntity.LAST_NAME));
            profile_pic = in.getString(in.getColumnIndex(ContactContract.ContactEntity.PROFILE_PIC));
            url = in.getString(in.getColumnIndex(ContactContract.ContactEntity.URL));
            phone_number = in.getString(in.getColumnIndex(ContactContract.ContactEntity.PHONE_NUMBER));
            email = in.getString(in.getColumnIndex(ContactContract.ContactEntity.EMAIL));
            created_at = in.getString(in.getColumnIndex(ContactContract.ContactEntity.CREATED_TIME));
            updated_at = in.getString(in.getColumnIndex(ContactContract.ContactEntity.UPDATE_TIME));
            favorite = (in.getInt(in.getColumnIndex(ContactContract.ContactEntity.FAVORITE)) == 1);
        }
    }

    @SerializedName("id")
    @Column(name = ContactContract.ContactEntity.CONTACT_ID, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    @Expose
    public String contact_id;
    @SerializedName(ContactContract.ContactEntity.FIRST_NAME)
    @Column(name = ContactContract.ContactEntity.FIRST_NAME, index = true)
    @Expose
    public String first_name;
    @SerializedName(ContactContract.ContactEntity.LAST_NAME)
    @Column(name = ContactContract.ContactEntity.LAST_NAME)
    @Expose
    public String last_name;
    @SerializedName(ContactContract.ContactEntity.PROFILE_PIC)
    @Column(name = ContactContract.ContactEntity.PROFILE_PIC)
    @Expose
    public String profile_pic;
    @SerializedName(ContactContract.ContactEntity.URL)
    @Column(name = ContactContract.ContactEntity.URL)
    @Expose
    public String url;
    @SerializedName(ContactContract.ContactEntity.PHONE_NUMBER)
    @Column(name = ContactContract.ContactEntity.PHONE_NUMBER)
    @Expose
    public String phone_number;
    @SerializedName(ContactContract.ContactEntity.EMAIL)
    @Column(name = ContactContract.ContactEntity.EMAIL)
    @Expose
    public String email;
    @SerializedName(ContactContract.ContactEntity.CREATED_TIME)
    @Column(name = ContactContract.ContactEntity.CREATED_TIME)
    @Expose
    public String created_at;
    @SerializedName(ContactContract.ContactEntity.UPDATE_TIME)
    @Column(name = ContactContract.ContactEntity.UPDATE_TIME)
    @Expose
    public String updated_at;
    @SerializedName("favorite")
    @Column(name = ContactContract.ContactEntity.FAVORITE)
    @Expose
    public boolean favorite;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ContactEntityModel> CREATOR
            = new Parcelable.Creator<ContactEntityModel>() {
        public ContactEntityModel createFromParcel(Parcel in) {
            return new ContactEntityModel(in);
        }

        public ContactEntityModel[] newArray(int size) {
            return new ContactEntityModel[size];
        }
    };

    public ContactEntityModel() {
        super();
    }

    private ContactEntityModel(Parcel in) {
        super();
        if (null != in) {
            contact_id = in.readString();
            first_name = in.readString();
            last_name = in.readString();
            profile_pic = in.readString();
            url = in.readString();
            phone_number = in.readString();
            email = in.readString();
            created_at = in.readString();
            updated_at = in.readString();
            firstValidation = in.readString();
            emailValidation = in.readString();
            phoneValidation = in.readString();
            favorite = in.readByte() != 0;
        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contact_id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(profile_pic);
        parcel.writeString(url);
        parcel.writeString(phone_number);
        parcel.writeString(email);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(firstValidation);
        parcel.writeString(emailValidation);
        parcel.writeString(phoneValidation);
        parcel.writeByte((byte) (favorite ? 1 : 0));
    }

    public String firstValidation;
    public String emailValidation;
    public String phoneValidation;


    public String getEmailValidation() {
        return emailValidation;
    }

    public void setEmailValidation(String emailValidation) {
        this.emailValidation = emailValidation;
    }

    public String getPhoneValidation() {
        return phoneValidation;
    }

    public void setPhoneValidation(String phoneValidation) {
        this.phoneValidation = phoneValidation;
    }

    public String getFirstValidation() {
        return firstValidation;
    }

    public void setFirstValidation(String firstValidation) {
        this.firstValidation = firstValidation;
    }

}
