package com.munir.myintentservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by munirul.hoque on 3/12/2017.
 */

public class MyIntentServices extends IntentService {
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
    private static final String TAG = "MyIntentServices";
    public static final String ACTION_RESP = "com.munir.myintentservice.MESSAGE_PROCESSED";
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public Bundle bundle;

    public MyIntentServices() {
        super(MyIntentServices.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");
        final ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
        bundle = new Bundle();

        /* Update UI: Download Service is Running */
        resultReceiver.send(STATUS_RUNNING, Bundle.EMPTY);



        String msg = intent.getStringExtra(PARAM_IN_MSG);
        SystemClock.sleep(6000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String resultTxt = msg + "  "+ currentDateandTime;

        Log.d("Current Time  ", resultTxt);
        bundle.putString("result",resultTxt);
        resultReceiver.send(STATUS_FINISHED,bundle);

        //Use this code if you use BroadcastReceiver instead of ResultReceiver
        /*Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponeReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(PARAM_OUT_MSG, resultTxt);
        sendBroadcast(broadcastIntent); */

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }



    public class MyIntentServicesException extends Exception {
        public MyIntentServicesException(String message) {
            super(message);
        }

        public MyIntentServicesException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
