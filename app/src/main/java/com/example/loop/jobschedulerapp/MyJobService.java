package com.example.loop.jobschedulerapp;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by loop on 05/03/17.
 */

public class MyJobService extends JobService {


    private static final String TAG = MyJobService.class.getSimpleName();
    public static final String ACTION_MSG = "com.loop.action.msg";

    private AsyncTask<String, String, String> asyncTask;

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG, "onStartJob, thread " + Thread.currentThread().getName());
        asyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    Log.d(TAG, "Start work");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return SimpleDateFormat.getTimeInstance().format(new Date());
            }

            @Override
            protected void onPostExecute(String s) {
                sendMsg(s);
            }
        };
        asyncTask.execute();
        return true;
    }

    private void sendMsg(String msg) {
        Intent intent = new Intent(ACTION_MSG);
        intent.putExtra("time", msg);
        LocalBroadcastManager.getInstance(MyJobService.this).sendBroadcast(intent);
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "onStopJob, thread " + Thread.currentThread().getName());
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        return false;
    }
}
