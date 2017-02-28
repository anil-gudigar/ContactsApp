package com.mcma.app.constants;

/**
 * Created by anil on 2/2/2017.
 */

public class AppConstants {
    public static final String THERE_ARE_NO_EMAIL_CLIENTS_INSTALLED = "There are no email clients installed.";
    public static final String SMS = "sms";
    public static final String VCF = ".vcf";
    public static String APP_TAG = "mcma";

    public static class API_CONSTANTS {
        public static final String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json;";
        public static final String CREATE_CONTACTS = "/contacts" ;
        public static final String REQUEST_CONTACTS = "/contacts.json" ;
        public static final String REQUEST_CONTACT = "/contacts/{id}.json" ;
        public static final String CONTACT_ID= "id" ;
    }
}
