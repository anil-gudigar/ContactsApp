package com.mcma.database;

import android.provider.BaseColumns;

/**
 * Created by anil on 2/8/2017.
 */

public class ContactContract {

    public static abstract class ContactEntity implements BaseColumns {
        public static final String TABLE_NAME = "ContactEntity";
        public static final String CONTACT_ID = "contact_id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String PROFILE_PIC = "profile_pic";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String EMAIL = "email";
        public static final String CREATED_TIME = "created_at";
        public static final String URL = "url";
        public static final String UPDATE_TIME = "updated_at";
        public static final String FAVORITE = "favorite";
    }
}
