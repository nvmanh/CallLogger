package com.android.callrecord.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.android.callrecord.receiver.CallRecordReceiver;

/**
 * Created by aykutasil on 19.10.2016.
 */

public class CallRecordService extends Service {

    private static final String TAG = CallRecordService.class.getSimpleName();
    private CallRecordReceiver mCallRecordReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        startCallReceiver();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCallReceiver();
        Log.i(TAG, "onDestroy()");
    }

    private void startCallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CallRecordReceiver.ACTION_IN);
        intentFilter.addAction(CallRecordReceiver.ACTION_OUT);
        if (mCallRecordReceiver == null) {
            mCallRecordReceiver = new CallRecordReceiver();
        }
        registerReceiver(mCallRecordReceiver, intentFilter);
    }

    private void stopCallReceiver() {
        try {
            unregisterReceiver(mCallRecordReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}