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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class startpage extends Activity {
    ProgressDialog mProgressDialog;
    ProgressBar pb;
    String username, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.from_middle, R.anim.in_middle);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                //finish();
                ErrorDialog();


            }
        });
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_page);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        boolean b = isNetworkAvailable();

        //System.out.println(DEVICE_I);
        if (b == true) {
            try {
                new Title().execute();
            } catch (Exception e) {

            }

        } else {
            //Toast.makeText(getApplicationContext(), "Yes", 3000).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Internet Connection Not Available")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            finish();//do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            //d1.set("Cancel ME");
            //d1.setContentView()
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private class Title extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Document doc;
            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";

                Connection.Response response = Jsoup.connect(urlLogin)
                        .method(Connection.Method.GET)
                        .timeout(0)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .execute();


                Document loginPage = response.parse();
                title = loginPage.title();
            } catch (Exception e) {
                ErrorDialog();
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            try {


                String title_correct = title;
                String sim_login = "SIM Login";
                if (title_correct.equals(sim_login)) {

                    Intent i = new Intent(startpage.this, MyMainActivity.class);
                    SharedPreferences sp2 = getSharedPreferences("QPQ", 0);
                    String qw = (sp2.getString("WPW", ""));
                    if (qw.equals("1")) {
                        if (isNetworkAvailable()) {
                            new MyTask_New().execute();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
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
                    } else {

                        startActivity(i);
                        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                        finish();
                    }

                } else {
                    //Toast.makeText(getApplicationContext(),"Unable to", 3000).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
                    builder.setMessage("Internet Connection Not Available")
                            .setCancelable(false)
                            .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
//				.setNegativeButton("GO OFFLINE",new DialogInterface.OnClickListener()
//				{
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Toast.makeText(getApplicationContext(),"Going Offline",Toast.LENGTH_LONG).show();
//
//					}
//				});
                    AlertDialog alert = builder.create();
                    alert.show();


                }

            } catch (Exception e) {
                ErrorDialog();
            }
        }
    }

    Element lastFocus, eventTarget, eventArgument, vKey, viewState, eventValidation, capcha_txt, capcha_txt2, capcha_txt3;
    Bitmap bitmap;
    String capcha;

    String cookString = "";

    //for login
    private class MyTask_New extends AsyncTask<Void, Void, String> {

        String title = "";
        String sessionId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(startpage.this);
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

//                eventValidation = loginPage.select("input[name=__EVENTVALIDATION]").first();
//                viewStateGenerator = loginPage.select("input[name=__VIEWSTATEGENERATOR]").first();
//                viewState = loginPage.select("input[name=__VIEWSTATE]").first();
//                capcha_txt = loginPage.select("input[type=text]").last();
                cookString = response.cookie("ASP.NET_SessionId");
                Element captcha = loginPage.select("img[id=Image1]").first();
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


                //	System.out.println(capcha_txt.attr("id"));	//Element img = loginPage.select("Image1").first();
//				// Locate the src attribute
                //String imgSrc = img.attr("src");
//				// Download image from URL
                //Toast.makeText(getApplicationContext(),imgSrc,3000).show();
                //InputStream input = new java.net.URL("http://122.160.168.157/isimgc/Student/capimage").openStream();//http://122.160.168.157/isimgc/Student/capimage
//				// Decode Bitmap

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
                final Dialog dialog = new Dialog(startpage.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                //myDialog.setContentView(R.layout.datelayout);
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
//				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//				imm.showSoftInput(editText_cap, InputMethodManager.SHOW_IMPLICIT);

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

                AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
                builder.setMessage("Error loading Captcha")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //e2.setText("");//do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
            //view.setText(title);

        }
    }

    //for direct login
    private class MyTask extends AsyncTask<Void, Void, String> {

        String title = "";
        String sessionId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(startpage.this);
            mProgressDialog.setTitle("LogIn");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            //Toast.makeText(getApplicationContext(),capcha,3000).show();
            //	Toast.makeText()
//			mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Document doc;
            SharedPreferences sp = getSharedPreferences("MM", 0);
            username = (sp.getString("KK", ""));
            SharedPreferences sp1 = getSharedPreferences("NN", 0);
            pass = (sp1.getString("LL", ""));


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

                Connection.Response

                        response = Jsoup.connect(urlLogin)
                        .cookie("ASP.NET_SessionId", cookString)
                        .data("__LASTFOCUS", "")// lastFocus.attr("value")+"")
                        .data("__EVENTTARGET", "")// eventTarget.attr("value")+"")
                        .data("__EVENTARGUMENT", "")//eventArgument.attr("value")+"")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")// viewState.attr("value")+"")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data(capcha_txt2.attr("id"), username)
                        .data("txtPass", pass)
                        .data(capcha_txt.attr("id"), capcha)
                        .data(capcha_txt3.attr("id"), username)
                        .data("btnLogin__", "")
                        .data("btnValue", "Button")
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
//                eventValidation = document.select("input[name=__EVENTVALIDATION]").first();
//                viewStateGenerator = document.select("input[name=__VIEWSTATEGENERATOR]").first();
//                viewState = document.select("input[name=__VIEWSTATE]").first();

                title = document.title();
                sessionId = cookString;
                System.out.println(sessionId);
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
                Intent i = new Intent(startpage.this, MyActivity.class);
                i.putExtra("User", username);
                i.putExtra("Password", pass);
                i.putExtra("Cookie", sessionId);


                startActivity(i);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

                finish();

            } else {
                mProgressDialog.dismiss();


                AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
                builder.setMessage("Incorrect UserName or Password")
                        .setCancelable(false)
                        .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //e2.setText("");//do things
                                SharedPreferences sp3 = getSharedPreferences("QPQ", 0);
                                SharedPreferences.Editor ed3 = sp3.edit();
                                ed3.putString("WPW", "0");
                                ed3.commit();

                                finish();
                            }
                        }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(startpage.this, startpage.class);
                        startActivity(i);
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
            //view.setText(title);

        }
    }

    void ErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
        builder.setMessage("Error Connecting to The Server")
                .setCancelable(false).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(startpage.this, startpage.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

                finish();

            }
        })
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();//do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}