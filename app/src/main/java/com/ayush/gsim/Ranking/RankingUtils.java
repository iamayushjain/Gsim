package com.ayush.gsim.Ranking;

import android.content.Context;

import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ayush on 25/2/17.
 */

public class RankingUtils {

    public static void pushRank(Context context, String userName, String studentName, String percentage) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference(FirebaseConstants.PROFILE)
                    .child(userName);
            databaseReference.child(FirebaseConstants.PROFILE_STUDENT_NAME).setValue(studentName);
//            databaseReference.child(FirebaseConstants.).setValue(studentName);
            databaseReference.child(FirebaseConstants.PROFILE_PERCENTAGE_VALUE).setValue(Float.parseFloat(percentage));
        } catch (Exception e) {
            LogWrapper.printStackTrace(context, e);
        }
    }
}
