package com.ayush.gsim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.ayush.gsim.Fragments.DetailFragment;
import com.ayush.gsim.Utils.LogWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by ayush on 4/2/17.
 */

public class UploadImage extends AsyncTask<Void, Void, String> {
    Activity activity;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String imageString, userName;
    private Uri uri;
    private byte[] b;

    public UploadImage(Activity activity, Uri uri, String userName) {
        this.activity = activity;
        this.uri = uri;
        this.userName = userName;
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public UploadImage() {

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
            b = baos.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String profilePics = "profile_pics/" + userName + ".jpeg";
        StorageReference storageReference = firebaseStorage.getReference(profilePics);
        LogWrapper.d(activity.getApplicationContext(), "uploading file", "uploading file");
        UploadTask uploadTask = storageReference.putBytes(b);
        uploadTask.addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("uploading file", "finish" + taskSnapshot.getDownloadUrl());
                getFromFireBase(activity.getApplicationContext(), userName);
            }
        });

    }

    public void getFromFireBase(Context context, String userName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.ayush.gsim/files/.myprivDir/dp.jpeg";
        File imgFile = new File(path);

        if (imgFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            int height = bitmap.getHeight(), width = bitmap.getWidth();

            if (height > 1280 && width > 960) {
                Bitmap imgbitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                DetailFragment.imageViewProfile.setImageBitmap(imgbitmap);
            } else {
                DetailFragment.imageViewProfile.setImageBitmap(bitmap);
            }
        } else {
            DownloadingProfileImage downloadingProfileImage = new DownloadingProfileImage(context, userName);
            downloadingProfileImage.execute();
        }
    }
}
