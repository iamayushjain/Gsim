package com.ayush.gsim.Service;

import android.app.Activity;
import android.content.Intent;

import com.ayush.gsim.Utils.FirebaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ayush on 28/1/17.
 */

public class ServiceActivation {


    public void firebaseServiceCondition(final Activity activity, DatabaseReference databaseReferenceGsim) {


        databaseReferenceGsim.child(FirebaseConstants.GENERAL_IS_SERVICE_ACTIVE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String string = snapshot.getValue().toString();
                Intent intent = new Intent(activity, NotificationService.class);
                if (string.equals(FirebaseConstants.GENERAL_IS_SERVICE_ACTIVE_TRUE)) {
                    activity.startService(intent);
                    System.out.println("service begins");
                } else {
                    try {
                        activity.stopService(intent);
                        System.out.println("service ends");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Firebase Error");
            }
        });
    }
}
