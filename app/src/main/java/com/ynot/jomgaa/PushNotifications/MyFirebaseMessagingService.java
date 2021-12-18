package com.ynot.jomgaa.PushNotifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ynot.jomgaa.View.Login;
import com.ynot.jomgaa.View.MainActivity;
import com.ynot.jomgaa.Web.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    Intent resultIntent;
    int pushID = 0;
    String notification, sound;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*getApplicationContext().startActivity(new Intent(getApplicationContext(), HomeActivity.class));*/
        Log.e("Message Received", "From: " + remoteMessage.getFrom());
        notification = "on";
        sound = "on";
        SharedPreferences sh = getSharedPreferences("push", MODE_PRIVATE);
        pushID = sh.getInt("push", 0);
        if (remoteMessage == null) {
            return;
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("Notification", "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("Notification", "Data Payload: " + remoteMessage.getData().toString());
            try {
               /* JSONObject json = new JSONObject(remoteMessage.getData().toString());
                JSONObject data = json.getJSONObject("data");
                Log.e("push data", data+"");*/
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("message");
                String image = remoteMessage.getData().get("image");
               /* String type = remoteMessage.getData().get("type");
                String chat_type = remoteMessage.getData().get("chat_type");*/

               // String image = "";
                String type = "";
                String chat_type = "";
                String senderId = "";
               /* if(mode.equals("chat")){
                    Gson gson = new Gson();
                    ChatMessageModel apiResponse=gson.fromJson(message, ChatMessageModel.class);
                    message=apiResponse.getMessage();
                    senderId=apiResponse.getSenderable_id();
                }*/
                handleDataMessage(title, message, image, type, chat_type);
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            setNotification(message);
            Log.e("App location", "On");
            Intent pushNotification = new Intent("pushNotification");
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            Log.e("App location", "Off");
        }
    }

    private void setNotification(String message) {
        pushID++;
        SharedPreferences.Editor edit = getSharedPreferences("push", MODE_PRIVATE).edit();
        edit.putInt("push", pushID);
        edit.apply();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        resultIntent = new Intent(getApplicationContext(), Login.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        showNotificationMessage(getApplicationContext(), "Jomgaa", message, format, resultIntent, pushID, "");

    }


    private void handleDataMessage(String title, String message, String image, String type, String chat_type) {
        try {
            String notificationType = "";
            pushID++;
            SharedPreferences.Editor edit = getSharedPreferences("push", MODE_PRIVATE).edit();
            edit.putInt("push", pushID);
            edit.apply();
            NotificationUtils not = new NotificationUtils(getApplicationContext());
            not.playNotificationSound();
            boolean isLoggedIn = SharedPrefManager.getInstatnce(getApplicationContext()).isLoggedIn();
            if (isLoggedIn) {

                resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();


            } else {
                resultIntent = new Intent(getApplicationContext(), Login.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }
            if (image != null && !image.equals("")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, format, resultIntent, image, pushID, notificationType);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                showNotificationMessage(getApplicationContext(), title, message, format, resultIntent, pushID, notificationType);
            }

        } catch (
                Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }

    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, int pushID, String type) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, pushID, sound, type);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl, int pushID, String type) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl, pushID, sound, type);
    }
}
