package com.mcma.callbacks;

/**
 * Created by anil on 2/8/2017.
 */

public interface ITask {
    int backgroundJob(Object data, int event);

    void onTaskStarted(int event);

    void onTaskFinished(int event, int result);

    void onTaskProgress(int event, int progress);
}
