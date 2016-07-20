package com.ayush.gsim;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MyMainActivity extends Activity {
    EditText e1, e2;
    Button b1;
    CheckBox box, box1;
    ProgressDialog mProgressDialog;
    ImageView iv;
    AdView mAdView;
//private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Intent i = new Intent(MyMainActivity.this, ErrorActivity.class);
                startActivity(i);

            }
        });

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_main);
        firebaseDialogCondition();
        firebaseNewFeature();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice("CE8A765D73C169864D8222080FCC9DA7")
                // .addTestDevice("A2DA3C272CCDBBE0F0EC842FBF9BFC8B")
                .build();
        mAdView.loadAd(adRequest1);

        e1 = (EditText) findViewById(R.id.editText1);
        e2 = (EditText) findViewById(R.id.editText2);
        b1 = (Button) findViewById(R.id.button1);
        box = (CheckBox) findViewById(R.id.checkBox1);
        box1 = (CheckBox) findViewById(R.id.checkBox2);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(e1, InputMethodManager.SHOW_IMPLICIT);
        e1.setTypeface(Typeface
                .createFromAsset(getAssets(), "robotothin.ttf"));


        SharedPreferences sp = getSharedPreferences("MM", 0);
        e1.setText(sp.getString("KK", ""));
        SharedPreferences sp1 = getSharedPreferences("NN", 0);
        e2.setText(sp1.getString("LL", ""));
        SharedPreferences sp2 = getSharedPreferences("QPQ", 0);
        String qw = (sp2.getString("WPW", ""));

        //mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-5666077083567257/2174168522");
        //AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //		.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();
        //mInterstitialAd.loadAd(adRequest1);
        //mInterstitialAd.setAdListener(new AdListener(){
//            public void onAdLoaded(){
//
//                                    // if (mInterstitialAd.isLoaded()) {
//                       //  mInterstitialAd.show();
//                     //}
//
//            }
//  //});
//
        //Toast.makeText(getApplicationContext(),qw,3000).show();
        if (qw.equals("1")) {
            if (isNetworkAvailable()) {
                //new MyTask().execute();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
                builder.setMessage("Internet Connection Not Available")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        }

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (box.isChecked()) {
                    //Toast.makeText(getApplicationContext(), "TRUE", 3000).show();
                    SharedPreferences sp1 = getSharedPreferences("MM", 0);
                    SharedPreferences.Editor ed1 = sp1.edit();
                    ed1.putString("KK", e1.getText().toString());
                    ed1.commit();
                    SharedPreferences sp2 = getSharedPreferences("NN", 0);
                    SharedPreferences.Editor ed2 = sp2.edit();
                    ed2.putString("LL", e2.getText().toString());
                    ed2.commit();


                }
                if (box1.isChecked()) {
                    SharedPreferences sp1 = getSharedPreferences("MM", 0);
                    SharedPreferences.Editor ed1 = sp1.edit();
                    ed1.putString("KK", e1.getText().toString());
                    ed1.commit();
                    SharedPreferences sp2 = getSharedPreferences("NN", 0);
                    SharedPreferences.Editor ed2 = sp2.edit();
                    ed2.putString("LL", e2.getText().toString());
                    ed2.commit();
                    SharedPreferences sp3 = getSharedPreferences("QPQ", 0);
                    SharedPreferences.Editor ed3 = sp3.edit();
                    ed3.putString("WPW", "1");
                    ed3.commit();

                } else {

                    SharedPreferences sp3 = getSharedPreferences("QPQ", 0);
                    SharedPreferences.Editor ed3 = sp3.edit();
                    ed3.putString("WPW", "0");
                    ed3.commit();

                }
                if (isNetworkAvailable()) {
                    new MyTask_New().execute();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
                    builder.setMessage("Internet Connection Not Available")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            }
        });


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    Element lastFocus, eventTarget, eventArgument, vKey, viewState, eventValidation, capcha_txt, capcha_txt2, capcha_txt3;
    Bitmap bitmap;
    String capcha;

    String cookString = "";

    private class MyTask_New extends AsyncTask<Void, Void, String> {

        String title = "";
        String sessionId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"I know you are about to copy my code",Toast.LENGTH_LONG);
            mProgressDialog = new ProgressDialog(MyMainActivity.this);
            mProgressDialog.setTitle("Loading Capcha");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
//			mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Document doc;

//			String username=e1.getText().toString();
//			String pass=e2.getText().toString();


            //Document doc;
            try {

                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlTimeTable = "http://122.160.168.157/iSIMGC/Student/TimeTable";
                String urlAttendance = "http://122.160.168.157/iSIMGC/Student/todayAttendance";
                Connection.Response response = Jsoup.connect(urlLogin)
                        .method(Connection.Method.GET)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .timeout(0)
                        .execute();


                Document loginPage = response.parse();


                System.out.println(loginPage.title());
                //System.out.println(viewState.attr("value"));
                lastFocus = loginPage.select("input[name=__LASTFOCUS]").first();
                eventTarget = loginPage.select("input[name=__EVENTTARGET]").first();
                eventArgument = loginPage.select("input[name=__EVENTARGUMENT]").first();
                vKey = loginPage.select("input[name=__VSKEY]").first();
                viewState = loginPage.select("input[name=__VIEWSTATE]").first();
                eventValidation = loginPage.select("input[name=__EVENTVALIDATION]").first();
                cookString = response.cookie("ASP.NET_SessionId");
                Element captcha = loginPage.select("img[id=Image1]").first();
                //	System.out.println(capcha_txt.attr("id"));	//Element img = loginPage.select("Image1").first();
//				// Locate the src attribute
                //String imgSrc = img.attr("src");
//				// Download image from URL
                //Toast.makeText(getApplicationContext(),imgSrc,3000).show();
                //InputStream input = new java.net.URL("http://122.160.168.157/isimgc/Student/capimage").openStream();//http://122.160.168.157/isimgc/Student/capimage
//				// Decode Bitmap
                Elements capcha_txt1 = loginPage.select("input[type=text]");
                int me_count = 0;
                capcha_txt = loginPage.select("input[type=text]").last();

                capcha_txt2 = loginPage.select("input[type=text]").last();
                capcha_txt3 = loginPage.select("input[type=text]").last();
                for (Element e : capcha_txt1) {
                    if (me_count == 0) {
                        capcha_txt2 = e;
                    }
                    if (me_count == 1) {
                        capcha_txt = e;
                    }
                    if (me_count == 2) {
                        capcha_txt3 = e;
                    }
                    me_count++;
                }


                response = Jsoup //
                        .connect(captcha.absUrl("src")) // Extract image absolute URL
                        .cookie("ASP.NET_SessionId", cookString) // Grab cookies
                        .ignoreContentType(true) // Needed for fetching image
                        .execute();
                bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(response.bodyAsBytes()));
                //System.out.println(eventValidation.attr("value"));

                //System.out.println(document.title());

//				response = Jsoup.connect(urlLogin)
//						.data("__VIEWSTATE", viewState.attr("value"))
//						.data("__EVENTVALIDATION", eventValidation.attr("value"))
//						.data("__VIEWSTATEGENERATOR", viewStateGenerator.attr("value"))
//						.data("txtUserId", username)
//						.data("txtPass", pass)
//						.data("btnValue", "")
//						.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
//						.method(Connection.Method.POST)
//						.followRedirects(true)
//						.timeout(0)
//						.execute();
                //    String sessionId = response.cookie("ASP.NET_SessionId");

//				Document document = response.parse();
                //Element liElement = document.select("").first();
                //      System.out.println(viewState.attr("value"));

                //    System.out.println(eventValidation.attr("value"));

                //System.out.println(document.html());
                // System.out.println(document.title())
                title = loginPage.title();
//				sessionId = response.cookie("ASP.NET_SessionId");
                //  System.out.println(sessionId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//
            String title_correct = title;
            String sim = "SIM Login";
            mProgressDialog.dismiss();

            if (title_correct.equals(sim)) {
                //view.setText(view.getText().toString()+title+"Correct");
//				Intent i=new Intent(MyMainActivity.this,MyActivity.class);
//				i.putExtra("User",e1.getText().toString());
//				i.putExtra("Password", e2.getText().toString());
//				i.putExtra("Cookie", sessionId);


//				startActivity(i);
//				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
//
//				finish();
                final Dialog dialog = new Dialog(MyMainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.captcha_custom_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setTitle("Captcha");
                dialog.setCanceledOnTouchOutside(false);
                // set the custom dialog components - text, image and button
//				TextView text = (TextView) dialog.findViewById(R.id.text);
//				text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                final EditText editText_cap = (EditText) dialog.findViewById(R.id.editText1);
                image.setImageBitmap(bitmap);
                editText_cap.setTypeface(Typeface
                        .createFromAsset(getAssets(), "robotothin.ttf"));

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        capcha = editText_cap.getText().toString();
                        dialog.dismiss();
                        new MyTask().execute();
                    }
                });

                dialog.show();


            } else {
                mProgressDialog.dismiss();

//				SharedPreferences sp3=getSharedPreferences("QPQ",0);
//				SharedPreferences.Editor ed3=sp3.edit();
//				ed3.putString("WPW","0");
//				ed3.commit();

                AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
                builder.setMessage("Error Loading Captcha")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                e2.setText("");//do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
            //view.setText(title);

        }
    }

    private class MyTask extends AsyncTask<Void, Void, String> {

        String title = "";
        String sessionId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyMainActivity.this);
            mProgressDialog.setTitle("LogIn");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            //	Toast.makeText(getApplicationContext(),capcha,3000).show();
            //	Toast.makeText()
//			mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            String username = e1.getText().toString();
            String pass = e2.getText().toString();


            //Document doc;
            try {

                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlTimeTable = "http://122.160.168.157/iSIMGC/Student/TimeTable";
                String urlAttendance = "http://122.160.168.157/iSIMGC/Student/todayAttendance";
//	        Connection.Response response = Jsoup.connect(urlLogin)
//	                .method(Connection.Method.GET)
//	                .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
//	                .timeout(0)
//	                .execute();
//
//
//	        Document loginPage = response.parse();
//
//
//	System.out.println(loginPage.title());
//	//System.out.println(viewState.attr("value"));
//	Element eventValidation = 	loginPage.select("input[name=__EVENTVALIDATION]").first();
//	Element viewStateGenerator =loginPage.select("input[name=__VIEWSTATEGENERATOR]").first();
//	Element viewState = 		loginPage.select("input[name=__VIEWSTATE]").first();

                //System.out.println(eventValidation.attr("value"));

                //System.out.println(document.title());

                Connection.Response response = Jsoup.connect(urlLogin)
                        .cookie("ASP.NET_SessionId", cookString)
                        .data("__LASTFOCUS", "")// lastFocus.attr("value")+"")
                        .data("__EVENTTARGET", "")// eventTarget.attr("value")+"")
                        .data("__EVENTARGUMENT", "")//eventArgument.attr("value")+"")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")// viewState.attr("value")+"")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        //.data("__VIEWSTATEGENERATOR", viewStateGenerator.attr("value"))

                        .data(capcha_txt2.attr("id"), username)
                        .data("txtPass", pass)
                        .data(capcha_txt.attr("id"), capcha)
                        .data(capcha_txt3.attr("id"), username)
                        .data("btnLogin__", "")
                        .data("btnValue", "Button")
                        //.cookie("ASP.NET_SessionId",response.cookie("ASP.NET_SessionId").to)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .timeout(0)
                        .execute();

                //    String sessionId = response.cookie("ASP.NET_SessionId");

                Document document = response.parse();
                //Element liElement = document.select("").first();
                //      System.out.println(viewState.attr("value"));

                //    System.out.println(eventValidation.attr("value"));

                //System.out.println(document.html());
                // System.out.println(document.title());
                title = document.title();
                sessionId = cookString;
                //  System.out.println(sessionId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//
            String title_correct = title;
            String sim = "SIM";
            mProgressDialog.dismiss();
//Toast.makeText(getApplicationContext(),title_correct,3000).show();
            if (title_correct.equals(sim)) {
                //view.setText(view.getText().toString()+title+"Correct");
                Intent i = new Intent(MyMainActivity.this, MyActivity.class);
                i.putExtra("User", e1.getText().toString());
                i.putExtra("Password", e2.getText().toString());
                i.putExtra("Cookie", sessionId);


                startActivity(i);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

                finish();

            } else {
                mProgressDialog.dismiss();

                SharedPreferences sp3 = getSharedPreferences("QPQ", 0);
                SharedPreferences.Editor ed3 = sp3.edit();
                ed3.putString("WPW", "0");
                ed3.commit();

                AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
                builder.setMessage("Incorrect UserName or Password")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                e2.setText("");//do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
            //view.setText(title);

        }
    }

    void firebaseDialogCondition() {
        if(!CheckLatestVersion()) {
            Firebase myFirebaseRef = new Firebase("https://gsim.firebaseio.com/");
            myFirebaseRef.child("Error").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    //Toast.makeText(getApplicationContext(),snapshot.getValue().toString(),3000).show();
                    String string = snapshot.getValue().toString();
                    if (!string.equals("1")) {
                        final String[] errorString = (string).split("-");
                        for (String s : errorString) System.out.println(s);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
                        builder.setMessage(errorString[0])

                                .setPositiveButton(errorString[1], new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (errorString[2].equals("link")) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ayush.gsim"));
                                            startActivity(browserIntent);
                                        }

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }


                @Override
                public void onCancelled(FirebaseError error) {
                }
            });

        }
    }

    void firebaseNewFeature() {
        Firebase myFirebaseRef = new Firebase("https://gsim.firebaseio.com/");
        myFirebaseRef.child("Notify").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                //Toast.makeText(getApplicationContext(),snapshot.getValue().toString(),3000).show();
                String string = snapshot.getValue().toString();
                if (!string.equals("1")) {
                    final String[] errorString = (snapshot.getValue().toString()).split("-");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
                    builder.setMessage(errorString[0])
                            .setPositiveButton(errorString[1], new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (errorString[2].equals("link")) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ayush.gsim"));
                                        startActivity(browserIntent);
                                    }

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });


    }
    boolean CheckLatestVersion()
    {
        if(getVersionCode().equals(checkActiveVersion()))
        {
            return true;
        }
        return  false;
    }
    String getVersionCode()
    {
        String ver="1.0";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            ver = pInfo.versionName;
            System.out.print(version);
        }catch (Exception e)
        {

        }
        return  ver;
    }
    String version;
    String checkActiveVersion()
    {
        Firebase myFirebaseRef = new Firebase("https://gsim.firebaseio.com/");
        version="1.0";
        myFirebaseRef.child("Notify").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                //Toast.makeText(getApplicationContext(),snapshot.getValue().toString(),3000).show();
                version = snapshot.getValue().toString();

                }


            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
return version;
    }
}

	
	
