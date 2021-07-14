package com.example.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.workmanager.SampleWorker.WORK_NUMBER_KEY;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWorker();
    }
    private void initWorker(){
        Data data=new Data.Builder()
                .putInt(WORK_NUMBER_KEY, 10)
                .build();

        Constraints constraints=new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        OneTimeWorkRequest downloadWorkRequest =new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("download")
                .build();


        WorkManager.getInstance(this).enqueue(downloadWorkRequest);
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                SampleWorker.class,
                1,
                TimeUnit.DAYS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("daily_notification")
                .build();
//        WorkManager.getInstance(this).enqueue(periodicWorkRequest);

//        WorkManager.getInstance(this).getWorkInfosByTagLiveData("download").observe(this,
//                new Observer<List<WorkInfo>>() {
//                    @Override
//                    public void onChanged(List<WorkInfo> workInfos) {
//                        for(WorkInfo w: workInfos){
//                            Log.d(TAG, "onChanged: Work status "+ w.getState());
//                        }
//                    }
//                });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                Log.d(TAG, "onChanged: work status:"+workInfo.getState());
            }
        });

        WorkManager.getInstance(this).cancelWorkById(downloadWorkRequest.getId());
    }
}