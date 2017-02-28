package com.mcma.app;

import android.os.AsyncTask;

import com.mcma.callbacks.ITask;
import com.mcma.utils.GenericUtilities;

/**
 * Created by anil on 2/8/2017.
 */

public class Task extends AsyncTask<Void, Integer, Integer> {
    public static final int RESULT_OK = 100;
    public static final int RESULT_FAILED = 101;

    private ITask listener;
    private int event;
    private Object mData;

    public Task(ITask listener, Object data, int event) {
        this.listener = listener;
        this.event = event;
        mData = data;
    }

    @Override
    protected void onPreExecute() {
        try {
            super.onPreExecute();
            listener.onTaskStarted(event);
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            return listener.backgroundJob(mData, event);
        } catch (Exception e) {
            GenericUtilities.handleException(e);
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        try {
            super.onPostExecute(result);
            listener.onTaskFinished(event, result);
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        try {
            listener.onTaskProgress(event, values[0]);
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }

    public void updateView(int progress) {
        try {
            publishProgress(progress);
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }
}
