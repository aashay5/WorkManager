package com.example.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SampleWorker extends Worker {
    private static final String TAG = "SampleWorker";
    public static final String WORK_NUMBER_KEY="number";
    public SampleWorker(@NonNull  Context context, @NonNull  WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData=getInputData();
        int number=inputData.getInt(WORK_NUMBER_KEY,-1);
        if (number!=-1) {
            for(int i=0;i<number;i++){
                Log.d(TAG, "doWork: i was "+i);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                    return Result.failure();
                }
            }
        }
        return Result.success();
    }
}
