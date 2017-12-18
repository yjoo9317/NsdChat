package com.youngjoo.nsdchat;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private  static final String TAG = "MainActivity";

    private NSDHelper mNSDHelper;
    private Handler mUpdateHandler;
    private TextView mStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUpdateHandler = new Handler(){

            @Override
            public void handleMessage(Message msg){

            }
        };
        mNSDHelper = new NSDHelper(this);
        mNSDHelper.initNSD();

    }

    public void onRegister(View view){

    }

    public void onDiscover(View view){
        mNSDHelper.discoverService();
    }

    public void onConnect(View view){

    }

    public void onSend(View view){

    }
}
