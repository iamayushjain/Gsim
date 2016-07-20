package com.ayush.gsim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by user on 7/19/2016.
 */
public class ErrorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ErrorDialog();

    }
    void ErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ErrorActivity.this);
        builder.setMessage("Error Connecting The Server")
                .setCancelable(false).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());


            }
        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();//do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
