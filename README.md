# android-easypush
  This is a simple example project how to use easypus library. You can modify as you need depends on your requirements. Please feel free if you have questions about this project
  
# requirements
  1. Easypush server
  2. easypushlib.jar as library

# how to use
  Initialize pushhelper class into your activity
  
  ```
  pushHelper = new PushHelper(this, "http://yourdomain.com", "YOUR_EASYPUSHSERVER_KEY");
  ```
  
  Extends **EasypushInstanceIDListenerService** into your listenerService class
  
  ```
  public class MyInstanceIDListenerService extends EasypushInstanceIDListenerService {

  }
  ```
  
  Add GCMListener class.
  
  ```
  public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);


        if (from.startsWith("/topics/")) {

        } else {

        }

        sendNotification(message);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, EasypushActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
  }
  ```
  
  Edit your manifest file. Add some permissions
  
  ```
  <uses-permission android:name="android.permission.WAKE_LOCK"/>
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

  <permission android:name="<YOUR_PACKAGE>.permission.C2D_MESSAGE" android:protectionLevel="signature"/>
  <uses-permission android:name="<YOUR_PACKAGE>.permission.C2D_MESSAGE"/>
  ```
  
  add meta data. 
  
  ```
  <meta-data android:name="GOOGLE_APP_ID" android:value="YOUR_GOOGLE_APP_ID"/>
  ```
  
  Add receiver and listeners inside application tag
  
  ```
  <receiver
    android:name="com.google.android.gms.gcm.GcmReceiver"
    android:exported="true"
    android:permission="com.google.android.c2dm.permission.SEND" >
      <intent-filter>
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        <category android:name="<YOUR_PACKAGE>" />
      </intent-filter>
  </receiver>
  <service
    android:name=".Service.MyGcmListenerService"
    android:exported="true" >
      <intent-filter>
        <action android:name="com.google.android.c2dm.intent.RECEIVE" />
      </intent-filter>
  </service>
  <service
    android:name=".Service.MyInstanceIDListenerService"
    android:exported="true">
      <intent-filter>
        <action android:name="com.google.android.gms.iid.InstanceID" />
      </intent-filter>
  </service>
  ```
  
# subscribe

  ```
  pushHelper.subscribe("youremail_android@company.com", "yourname_android", new RequestListener() {
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
  ```

# unsubscribe

  ```
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
  ```

# license
  This project is completely free to use.

# credits
  This project using [android-async-http](http://loopj.com/android-async-http/ "android-async-http") as the third party library to handle Http Request connection.
