package com.easypush.push.lib;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class PushHelper{

    protected final String AUTHENTICATION = "Authentication";
    private RequestListener requestListener;
    private Activity activity;
    private boolean isHaveAppId = false;
    private AsyncHttpClient client;
    private boolean isFinish = true;
    private RequestParams params;
    private String deviceId = "";
    private static String APP_ID = "app_id";
    private String baseUrl = "";
    SharedPreferences sharedPreferences;

    public PushHelper(Activity activity, String baseUrl, String KEY){
        init(activity, baseUrl, KEY, null);
    }

    @SuppressWarnings("unused")
    public PushHelper (Activity activity, String baseUrl, String KEY, RequestListener requestListener){

        init(activity, baseUrl, KEY, requestListener);
    }

    private void init(Activity activity, String baseUrl, String KEY, RequestListener requestListener){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.requestListener = requestListener;
        this.baseUrl = baseUrl;
        this.activity = activity;
        client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
        client.setResponseTimeout(10000);
        client.setConnectTimeout(10000);
        client.setMaxRetriesAndTimeout(0, 10000);
        client.addHeader(AUTHENTICATION, KEY);

        deviceId = getUniquePsuedoID();
        params = new RequestParams();

    }

    @SuppressWarnings("unused")
    public void subscribe(String email, String name){
        submit(email, name, "", null);
    }

    @SuppressWarnings("unused")
    public void subscribe(String email, String name, String imagePath){
        submit(email, name, imagePath, null);
    }

    public void subscribe(String email, String name, RequestListener requestListener){
        submit(email, name, "", requestListener);
    }

    @SuppressWarnings("unused")
    public void subscribe(String email, String name, String imagePath, RequestListener requestListener){
        submit(email, name, imagePath, requestListener);
    }

    private void submit (String email, String name, String imagePath, RequestListener requestListener){

        this.params = new RequestParams();
        this.requestListener = requestListener;
        this.params.put("email", email);
        this.params.put("name", name);
        this.params.put("image_path", imagePath);
        this.params.put("device_id", deviceId);
        this.params.put("device", "android");

        String TOKEN = "";
        if (!sharedPreferences.getString("TOKEN", "").equals("")){
            TOKEN = sharedPreferences.getString("TOKEN","");
        }else {
            Log.w("App Id Error", "App Id cannot be null");
            return;
        }
        this.params.put(APP_ID, TOKEN);
        if(isFinish)
            sendPostRequest(baseUrl+"/api/user/login");
    }

    @SuppressWarnings("unused")
    public void unSubscribe(){
        unregister(null);
    }

    public void unSubscribe(RequestListener requestListener) {
        unregister(requestListener);
    }

    private void unregister(RequestListener requestListener){

        if (sharedPreferences.getString("TOKEN", "").equals("")){
            Log.w("App Id Error", "App Id cannot be null");
            return;
        }

        this.requestListener = requestListener;
        if (isFinish)
            sendPostRequest(baseUrl + "/api/user/logout");
    }

    private void sendPostRequest(String URL) {

        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                isFinish = true;

                if (requestListener != null)
                    requestListener.onSuccess(response);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                super.onSuccess(statusCode, headers, response);
                isFinish = true;
                if (requestListener != null)
                    requestListener.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                isFinish = true;
                String message;
                if (statusCode == 400) {
                    message = "Invalid EASYPUSH KEY";
                } else if (statusCode == 401) {
                    Log.d("message", "You need valid credentials for me to respond to this request");
                    message = "You need valid credentials for me to respond to this request";
                } else if (statusCode == 403) {
                    Log.d("message", "I understood your credentials, but so sorry, you're not allowed");
                    message = "I understood your credentials, but so sorry, you're not allowed";
                } else if (statusCode == 404) {
                    Log.d("message", "Resource error");
                    message = "Resource error";
                } else if (statusCode == 500) {
                    Log.d("message", "Server error");
                    message = "Server error";
                } else {
                    Log.d("message", "Connection error");
                    message = "Connection error";
                }

                if (requestListener != null)
                    requestListener.onFailed(message, statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                isFinish = true;
                String message;

                if (statusCode == 400) {
                    message = "Invalid EASYPUSH KEY";
                } else if (statusCode == 401) {
                    Log.d("message", "You need valid credentials for me to respond to this request");
                    message = "You need valid credentials for me to respond to this request";
                } else if (statusCode == 403) {
                    Log.d("message", "I understood your credentials, but so sorry, you're not allowed");
                    message = "I understood your credentials, but so sorry, you're not allowed";
                } else if (statusCode == 404) {
                    Log.d("message", "Resource error");
                    message = "Resource error";
                } else if (statusCode == 500) {
                    Log.d("message", "Server error");
                    message = "Server error";
                } else {
                    Log.d("message", "Connection error");
                    message = "Connection error";
                }

                if (requestListener != null)
                    requestListener.onFailed(message, statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                isFinish = true;
                String message;

                if (statusCode == 400) {
                    message = "Invalid EASYPUSH KEY";
                } else if (statusCode == 401) {
                    Log.d("message", "You need valid credentials for me to respond to this request");
                    message = "You need valid credentials for me to respond to this request";
                } else if (statusCode == 403) {
                    Log.d("message", "I understood your credentials, but so sorry, you're not allowed");
                    message = "I understood your credentials, but so sorry, you're not allowed";
                } else if (statusCode == 404) {
                    Log.d("message", "Resource error");
                    message = "Resource error";
                } else if (statusCode == 500) {
                    Log.d("message", "Server error");
                    message = "Server error";
                } else {
                    Log.d("message", "Connection error");
                    message = "Connection error";
                }

                if (requestListener != null)
                    requestListener.onFailed(message, statusCode);
            }
        });
    }

    private String getUniquePsuedoID() {

        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "apiqueserial"; // some value
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
