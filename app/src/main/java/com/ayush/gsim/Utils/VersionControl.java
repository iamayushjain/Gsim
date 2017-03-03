package com.ayush.gsim.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ayush on 27/1/17.
 */

public class VersionControl {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;


    private Activity activity;

    public VersionControl(Activity activity) {
        this.activity = activity;
    }

    public void fireBaseEachVersion() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.GENERAL);


        databaseReferenceGsim.child(FirebaseConstants.GENERAL_VERSION_ALERT)
                .child(Utils.getVersionCode(activity.getApplicationContext()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        notificationAnalysis(
                                snapshot.child("Button").getValue().toString(),
                                snapshot.child("Count").getValue().toString(),
                                snapshot.child("IsCompulsory").getValue().toString(),
                                snapshot.child("Link").getValue().toString(),
                                snapshot.child("Message").getValue().toString(),
                                snapshot.child("Title").getValue().toString()
                        );
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        LogWrapper.out(activity.getApplicationContext(), databaseError.getMessage());
                    }
                });


    }

    public void notificationAnalysis(String button, String count, String isCompulsory, final String link, String message, String title) {
        try {

            System.out.println("here");
            SharedPreferences sp = activity.getSharedPreferences(Constants.SHARED_PREF_ADS_COUNT_TABLE, 0);
            String currentVersionAlertCountString = (sp.getString(Constants.SHARED_PREF_VERSION_ALERT_ADS_COUNT, "0"));
            int currentVersionAlertCount = Integer.parseInt(currentVersionAlertCountString);
            currentVersionAlertCount++;


            System.out.println(currentVersionAlertCount);
            if (currentVersionAlertCount >= Integer.parseInt(count)) {
                currentVersionAlertCount = 0;
                System.out.println(currentVersionAlertCount);
                currentVersionAlertCount = 0;
                if (!title.equals("1")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(title).setMessage(message)
                            .setPositiveButton(button, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        if (!link.equals("1")) {
                                            try {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                                activity.startActivity(browserIntent);
                                            } catch (Exception e) {

                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                    if (isCompulsory.equals("1")) {
                        builder.setCancelable(false);
                        //alert.setCanceledOnTouchOutside(false);

                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                    SharedPreferences.Editor editor = sp.edit();
                    System.out.println(currentVersionAlertCount);
                    editor.putString(Constants.SHARED_PREF_VERSION_ALERT_ADS_COUNT, currentVersionAlertCount + "");
                    editor.commit();
                }
            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
    }
}
