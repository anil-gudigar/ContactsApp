package com.mcma.app;

import android.support.v7.app.AppCompatActivity;

import com.mcma.callbacks.ITask;
import com.mcma.utils.GenericUtilities;

/**
 * Created by anil on 2/8/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements ITask {


    protected void executeTask(Object data, int event) {
        try {
            Task task = new Task(this, data, event);
            task.execute();
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }

    @Override
    public int backgroundJob(Object data, int event) {
        return 0;
    }

    @Override
    public void onTaskStarted(int event) {

    }

    @Override
    public void onTaskFinished(int event, int result) {
        updateUi(null, event);
    }

    @Override
    public void onTaskProgress(int event, int progress) {

    }

    protected abstract void update(Object data, int event);

    public void updateUi(final Object data, final int event) {
        try {
            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        update(data, event);
                    } catch (Exception e) {
                        GenericUtilities.handleException(e);
                    }
                }
            });
        } catch (Exception e) {
            GenericUtilities.handleException(e);
        }
    }

}
