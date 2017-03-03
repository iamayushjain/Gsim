package com.ayush.gsim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ayush.gsim.Fragments.DetailFragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ayush on 4/2/17.
 */
public class DownloadingProfileImage extends AsyncTask<String, Void, Bitmap> {


    String userName;
    Context context;

    public DownloadingProfileImage(Context context, String userName) {
        this.context = context;
        this.userName = userName;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {

        String urldisplay = "https://firebasestorage.googleapis.com/v0/b/gsim-f0a4e.appspot.com/o/profile_pics%2F" + userName + ".jpeg?alt=media";
        Log.d("Uploading Profile", urldisplay);
        Bitmap mIcon11 = null;
        try {
            File dir = context.getExternalFilesDir(".myprivDir");
            File dp = new File(dir, "/dp.jpeg");
            OutputStream output;

            URL url = new URL(urldisplay);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            long downloaded = 0;
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            connection.connect();

            InputStream input = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fos;

            fos = new FileOutputStream(dp);
            output = new BufferedOutputStream(fos, 1024);

            byte[] data = new byte[1024];
            int x = 0;

            long total = downloaded;

            while ((x = input.read(data, 0, 1024)) >= 0) {
                output.write(data, 0, x);
                downloaded += x;
                //publishProgress("" + (int) ((downloaded * 100) / (total)));

            }
            // closing streams
            output.flush();
            output.close();
            input.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.d("Uploading Profile", "Upload Success");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.ayush.gsim/files/.myprivDir/dp.jpeg";
        File imgFile = new File(path);

        if (imgFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            int height = bitmap.getHeight(), width = bitmap.getWidth();

            if (height > 1280 && width > 960) {
                Bitmap imgbitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                DetailFragment.imageViewProfile.setImageBitmap(getCircleBitmap(imgbitmap));
            } else {
                DetailFragment.imageViewProfile.setImageBitmap(getCircleBitmap(bitmap));
            }
        }
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF
                rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
