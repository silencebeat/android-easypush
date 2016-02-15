package com.easypush.push;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.easypush.push.lib.PushHelper;
import com.easypush.push.lib.RequestListener;

import org.json.JSONObject;

public class EasypushActivity extends AppCompatActivity implements View.OnClickListener{

    static String TAG = "SUBSCRIBE_SUCCESS";
    static String TAG_ERROR = "SUBSCRIBE_ERROR";
    PushHelper pushHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pushHelper = new PushHelper(this, "http://192.168.1.18/pushnotification", "3156df29bae2213d65e4b199e1a6f180ade81926");

        findViewById(R.id.btnSubscribe).setOnClickListener(this);
        findViewById(R.id.btnSubscribe).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubscribe:
                pushHelper.subscribe("youremail@company.com", "yourname", new RequestListener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                    }

                    @Override
                    public void onFailed(String message, int statusCode) {
                        Log.d(TAG_ERROR, message);
                    }

                    @Override
                    public void onSuccess(JSONObject jsonObject) {

                    }
                });
                break;
            case R.id.btnUnsubscribe:
                pushHelper.unSubscribe(new RequestListener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                    }

                    @Override
                    public void onFailed(String message, int statusCode) {
                        Log.d(TAG_ERROR, message);
                    }

                    @Override
                    public void onSuccess(JSONObject jsonObject) {

                    }
                });
                break;

            default:
                break;

        }
    }
}
