package com.mcma.app;

import android.support.v4.app.Fragment;

import com.mcma.callbacks.ITask;
import com.mcma.utils.GenericUtilities;

/**
 * Created by Anil on 2/14/2017.
 */

public abstract class BaseFragment extends Fragment implements ITask {
    @Override
    public int backgroundJob(Object data, int event) {
        return 0;
    }

    protected void executeTask(Object data, int event) {
        try {
            Task task = new Task(this, data, event);
            task.execute();
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }

    @Override
    public void onTaskStarted(int event) {

    }

    @Override
    public void onTaskFinished(int event, int result) {

    }

    @Override
    public void onTaskProgress(int event, int progress) {

    }

    protected abstract void update(Object data, int event);

    public void updateUi(final Object data, final int event) {
        try {
            if (null != getActivity()) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            update(data, event);
                        } catch (Exception e) {
                            GenericUtilities.handleException(e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
