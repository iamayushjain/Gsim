package com.ayush.gsim.Service;

/**
 * Created by user on 8/30/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.ayush.gsim.Activities.LauncherActivity;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {

    MyCount counter = new MyCount(120000, 20000);
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        counter.start();
        //  long l=
        //Toast.makeText(this, "Create", Toast.LENGTH_LONG).show();
    }


    public void onDestroy() {
        try {
//			mTimer.cancel();
//			timerTask.cancel();
            counter.cancel();
            Log.e("Log", "Destroy");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("com.android.techtrainner");
        intent.putExtra("yourvalue", "torestore");
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
    }

    void firebaseNewFeature() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.GENERAL);
        databaseReferenceGsim.child("NotificationHandle").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                //Toast.makeText(getApplicationContext(),snapshot.getValue().toString(),3000).show();
                String message = snapshot.child("Message").getValue().toString();
                String link = snapshot.child("Link").getValue().toString();
                try {
                    System.out.println(message);
                    if (!message.equals("1")) {
                        //  Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
                        System.out.println(message);
                        if (!checkLastMessage(message))
                            sendNotification(message, link);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Firebase error");
            }
        });

    }


    private void sendNotification(String messageBody, String link) {
        Intent intent;
        if (link.equals("1")) {
            intent = new Intent(this, LauncherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            System.out.println(link);

        } else {
            System.out.println(link);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {

                getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                intent.setData(Uri.parse("fb://facewebmodal/f?href=" + link));
                System.out.println("facebook link");
            } catch (Exception e) {
                intent.setData(Uri.parse(link));
                System.out.println("normal link");
            }

            //intent.setData(Uri.parse(link));

        }
        // activity.startActivity(browserIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("GSIM")
                .setTicker(messageBody)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.BigTextStyle(notificationBuilder)
                .bigText(messageBody).build();

        notificationManager.notify(0 /* ID of notification */, notification);
    }

    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
//          sendNotification();
            firebaseNewFeature();
            counter.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //System.out.println("Finish");
        }
    }

    boolean checkLastMessage(String s) {
        SharedPreferences sp = getSharedPreferences("LastMessege", 0);
        String s1 = (sp.getString("Messege", ""));
        System.out.println(s1 + "-----" + s);
        if (s1.equals(s)) {
            return true;
        } else {

            SharedPreferences sp1 = getSharedPreferences("LastMessege", 0);
            SharedPreferences.Editor ed1 = sp1.edit();
            ed1.putString("Messege", s);
            ed1.commit();
            return false;
        }

    }

}

