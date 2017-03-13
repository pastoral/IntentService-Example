package com.munir.myintentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MyResultReceiver.Receiver {

    EditText txtinput;
    TextView result;
    Button normalTask;
    private MyResultReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /* Allow activity to show indeterminate progressbar */
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        txtinput = (EditText)findViewById(R.id.txtinput);
        result = (TextView)findViewById(R.id.textView);
        normalTask = (Button)findViewById(R.id.normaltask);

        //**// use below code block if you choose to use BroadcastReceiver//
        // you need to define the Intent Filter and regiter the BroadcastReceiver

       /* IntentFilter filter = new IntentFilter(ResponeReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponeReceiver();
        registerReceiver(receiver,filter); */

        //**// set the ResultReceiver //
        receiver = new MyResultReceiver(new Handler());

    }



    @Override
    protected void onResume() {
        super.onResume();
        receiver.setmReceiver(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(receiver);
        receiver.setmReceiver(null);
    }

    public void normalOperation(View view){
        String strInput = txtinput.getText().toString();
        SystemClock.sleep(6000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        result.setText(strInput + "  "+ currentDateandTime);
    }

    public void serviceOperation(View view){
        String strInput = txtinput.getText().toString();
        //**// use below code block if you choose to use BroadcastReceiver//
        /*Intent intent = new Intent(this, MyIntentServices.class);
        intent.putExtra(MyIntentServices.PARAM_IN_MSG, strInput);
        startService(intent);*/

        //**// use below code block if you choose to use ResultReceiver//

        Intent intent = new Intent(this, MyIntentServices.class);
        intent.putExtra(MyIntentServices.PARAM_IN_MSG, strInput);
        intent.putExtra("receiver", receiver);
        intent.putExtra("requestId", 101);
        startService(intent);

    }

    public class ResponeReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "com.munir.myintentservice.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            result = (TextView)findViewById(R.id.textView);
            String text= intent.getStringExtra(MyIntentServices.PARAM_OUT_MSG);
            result.setText(text);
        }

    }

    //**// ResultReceiver results in onReceiveResult//


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch(resultCode){
            case MyIntentServices.STATUS_RUNNING:
                setProgressBarIndeterminateVisibility(true);
                break;
            case MyIntentServices.STATUS_FINISHED:
                setProgressBarIndeterminateVisibility(false);
                String text= resultData.getString("result");
                result.setText(text);
                break;
        }
    }
}
