package com.ynot.jomgaa.PushNotifications;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import com.ynot.jomgaa.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NotificationUtils {
    private Context mContext;
    int pushID;
    int color;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent, int pushID, String sound, String type) {
        showNotificationMessage(title, message, timeStamp, intent, null, pushID, sound, type);

    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl, int pushID, String sound, String type) {
        this.pushID = pushID;
        color = ContextCompat.getColor(mContext, R.color.icon_color);
        if (TextUtils.isEmpty(message)) {
            return;
        }

        final int icon = R.mipmap.ic_launcher;
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, pushID, intent, PendingIntent.FLAG_ONE_SHOT);


        final Uri alarmSound;
        if (sound.equals("on")) {
            if (type.equals("NewOrder")) {
                alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/order_sound_new");
            } else {
                alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/vote_donate");
            }
        } else {
            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/silence");
        }

        if (!TextUtils.isEmpty(imageUrl)) {
            Bitmap b = getBitmapFromURL(imageUrl);
            if (b != null) {
                Log.e("Bitmap status", "bitmap  is present");
                showBigNotification(b, icon, title, message, timeStamp, resultPendingIntent, alarmSound, type);
            } else {
                Log.e("Bitmap status", "bitmap is empty");
                showSmallNotification(icon, title, message, timeStamp, resultPendingIntent, alarmSound, type);
            }
        } else {
            Log.e("Image Url", "Image Url is empty");
            showSmallNotification(icon, title, message, timeStamp, resultPendingIntent, alarmSound, type);
        }


    }


    private void showSmallNotification(int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, String type) {
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        inboxStyle.bigText(message);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "default")
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = null;
        if (type.equals("NewOrder")) {
            soundUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notification);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("com.mySellerApp.order");
                NotificationChannel channel = new NotificationChannel(
                        "com.mySellerApp.order",
                        "My Seller App Order",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(soundUri, audioAttributes);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }

        } else {
            soundUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notification);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("com.mySellerApp.other");
                NotificationChannel channel = new NotificationChannel(
                        "com.mySellerApp.other",
                        "My Seller App Other",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

                //.setUsage(AudioAttributes.USAGE_ALARM)

                channel.setSound(soundUri, audioAttributes);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
        notificationManager.notify(pushID, builder.build());

    }

    private void showBigNotification(Bitmap bitmap, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound, String type) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "default")
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(color)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = null;
        if (type.equals("NewOrder")) {
            soundUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notification);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("com.mySellerApp.order");
                NotificationChannel channel = new NotificationChannel(
                        "com.mySellerApp.order",
                        "My Seller App Order",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(soundUri, audioAttributes);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }

        } else {
            soundUri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notification);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("com.mySellerApp.other");
                NotificationChannel channel = new NotificationChannel(
                        "com.mySellerApp.other",
                        "My Seller App Other",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(soundUri, audioAttributes);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
        notificationManager.notify(pushID, builder.build());
    }


    // Playing notification sound
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.notification);
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            String newpath = src;
            URL url = new URL(newpath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
