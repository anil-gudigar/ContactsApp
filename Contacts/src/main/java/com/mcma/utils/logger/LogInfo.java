package com.mcma.utils.logger;

import com.mcma.BuildConfig;
import com.mcma.app.MCMAContactsBookApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by anil on 2/8/2017.
 */

public class LogInfo {
    public static final String FILENAME = "LoggingInfo.txt";

    public static boolean saveToLocalLogFile(Throwable throwable) {
        return saveToLocalLogFile(getStackTrace(throwable), FILENAME);
    }

    public static boolean saveToLocalLogFile(String msg, String logFileName) {
        /* try {
             File log = new File(MCMAContactsBookApplication.getInstance().getApplicationContext().getExternalFilesDir(null), logFileName);
             if (!log.exists())
                 log.createNewFile();
             BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
             out.write(msg);
             out.write("\n");
             out.close();
             return true;
         } catch (IOException e) {
             e.printStackTrace();
             return false;
         } catch (Exception e) {
             e.printStackTrace();
             return false;
         }*/
        return BuildConfig.DEBUG;
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
