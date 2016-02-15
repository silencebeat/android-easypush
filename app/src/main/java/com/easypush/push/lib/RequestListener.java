package com.easypush.push.lib;

import org.json.JSONObject;

/**
 * Created by candra apiqueStudio on 12-Jan-16.
 */
public interface RequestListener {
    void onSuccess(String result);
    void onFailed(String message, int errorStatus);
    void onSuccess(JSONObject jsonObject);
}
