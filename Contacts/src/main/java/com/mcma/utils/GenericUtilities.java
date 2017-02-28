package com.mcma.utils;

import android.content.Context;

import com.mcma.app.constants.AppConstants;
import com.mcma.models.contacts.ContactEntityModel;
import com.mcma.utils.logger.LogInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by anil on 2/8/2017.
 */

public class GenericUtilities {

    public static void handleException(Exception e) {
        e.printStackTrace();
        if (e.getLocalizedMessage() != null)
            LogInfo.saveToLocalLogFile(e);
    }

    public static File createVCF(Context context, ContactEntityModel contactEntityModel) {
        File vcfFile = new File(context.getExternalCacheDir(), contactEntityModel.getFirst_name() + AppConstants.VCF);
        FileWriter fw = null;
        try {
            fw = new FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");
            fw.write("N:" + contactEntityModel.getLast_name() + ";" + contactEntityModel.getFirst_name() + "\r\n");
            fw.write("FN:" + contactEntityModel.getFirst_name() + " " + contactEntityModel.getLast_name() + "\r\n");
            fw.write("ORG:" + "\r\n");
            fw.write("TITLE:" + "\r\n");
            fw.write("TEL;TYPE=WORK,VOICE:" + contactEntityModel.getPhone_number() + "\r\n");
            fw.write("TEL;TYPE=HOME,VOICE:" + "\r\n");
            fw.write("ADR;TYPE=WORK:;;" + ";" + ";" + ";" + ";" + "\r\n");
            fw.write("EMAIL;TYPE=PREF,INTERNET:" + contactEntityModel.getEmail() + "\r\n");
            fw.write("END:VCARD\r\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vcfFile;
    }

    public static StringBuffer createText(ContactEntityModel contactEntityModel) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Name:" + contactEntityModel.getFirst_name() + " " + contactEntityModel.getLast_name() + "\r\n");
        stringBuffer.append("Phone:" + contactEntityModel.getPhone_number() + "\r\n");
        stringBuffer.append("Email:" + contactEntityModel.getEmail() + "\r\n");
        return stringBuffer;
    }
}
