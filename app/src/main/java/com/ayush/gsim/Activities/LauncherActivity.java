package com.ayush.gsim.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ayush.gsim.AsyncTasks.Details;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;
import com.ayush.gsim.Utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;


public class LauncherActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private ProgressBar progressBar;
    private String username, password;
    private TextView welcome, powered;
    private Element lastFocus, eventTarget, eventArgument, vsKey, viewState, eventValidation, textField1, textField2, textField3;
    private Bitmap bitmap;
    private String captcha;
    private Document loginPage;
    private Connection.Response response;
    private String cookString = "";
    private String loginLocation;
    private SharedPreferences sharedPreferencesLoginTable;
    private boolean isSuccessFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.from_middle, R.anim.in_middle);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                LogWrapper.printStackTraceThrowable(getApplicationContext(), ex);
                errorDialog();
            }
        });

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.launchar_activity);

        init();
        Utils.deletePreviousData(LauncherActivity.this);
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            try {
                new CheckServerWorkingAsyncTask().execute();
            } catch (Exception e) {
                LogWrapper.printStackTrace(getApplicationContext(), e);
                errorDialog();
            }
        } else {
            Utils.noInternetDialog(LauncherActivity.this);
        }
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        welcome = (TextView) findViewById(R.id.welcome);
        powered = (TextView) findViewById(R.id.powered);

        welcome.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));
        powered.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));

        sharedPreferencesLoginTable = getSharedPreferences(Constants.SHARED_PREF_LOGIN_TABLE, 0);

    }

    private class CheckServerWorkingAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                response = Jsoup.connect(Constants.URL_LOGIN)
                        .method(Connection.Method.GET)
                        .timeout(0)
                        .userAgent(Constants.USER_AGENT)
                        .execute();


                loginPage = response.parse();

                lastFocus = loginPage.select(Constants.INPUT_LAST_FOCUS).first();
                eventTarget = loginPage.select(Constants.INPUT_EVENT_TARGET).first();
                eventArgument = loginPage.select(Constants.INPUT_EVENT_ARGUMENT).first();
                vsKey = loginPage.select(Constants.INPUT_VS_KEY).first();
                viewState = loginPage.select(Constants.INPUT_VIEW_STATE).first();
                eventValidation = loginPage.select(Constants.INPUT_EVENT_VALIDATION).first();

                loginLocation = loginPage.location();

            } catch (Exception e) {
                errorDialog();
                LogWrapper.printStackTrace(getApplicationContext(), e);
                loginLocation = "";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                String obtainedURL = loginLocation;

                if (obtainedURL.equalsIgnoreCase(Constants.URL_LOGIN)) {

                    String toAutoLogin = (sharedPreferencesLoginTable.getString(Constants.SHARED_PREF_AUTO_LOGIN, ""));
                    if (toAutoLogin.equals(Constants.AUTO_LOGIN_TRUE_CASE)) {
                        if (Utils.isNetworkAvailable(getApplicationContext())) {
                            new AutoLoginLoadCaptchaAsyncTask().execute();
                        } else {
                            Utils.noInternetDialog(LauncherActivity.this);
                        }
                    } else {
                        intentToLoginActivity();
                    }

                } else {
                    LogWrapper.d(getApplicationContext(), "No Internet access", "Wifi connected but not connected to Internet");
                    Utils.noInternetDialog(LauncherActivity.this);
                }

            } catch (Exception e) {
                LogWrapper.printStackTrace(getApplicationContext(), e);
                errorDialog();
            }
        }
    }

    //for login
    private class AutoLoginLoadCaptchaAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LauncherActivity.this);
            mProgressDialog.setTitle(getResources().getString(R.string.captcha_progress_dialog));
            mProgressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            loginLocation = "";
            isSuccessFull = false;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                cookString = response.cookie(Constants.COOKIE_ASP_NET);

                Element captcha = loginPage.select(Constants.INPUT_CAPTCHA_ID).first();

                Elements elementsTextField = loginPage.select(Constants.INPUT_TYPE_TEXT);
                int countTextFields = 0;
                textField1 = loginPage.select(Constants.INPUT_TYPE_TEXT).last();
                textField2 = loginPage.select(Constants.INPUT_TYPE_TEXT).last();
                textField3 = loginPage.select(Constants.INPUT_TYPE_TEXT).last();
                for (Element e : elementsTextField) {
                    if (countTextFields == 0) {
                        textField1 = e;
                    }
                    if (countTextFields == 1) {
                        textField2 = e;
                    }
                    if (countTextFields == 2) {
                        textField3 = e;
                    }
                    countTextFields++;
                }


                response = Jsoup //
                        .connect(captcha.absUrl("src")) // Extract image absolute URL
                        .cookie(Constants.COOKIE_ASP_NET, cookString) // Grab cookies
                        .ignoreContentType(true)
                        .timeout(0)// Needed for fetching image
                        .execute();
                bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(response.bodyAsBytes()));
                loginLocation = loginPage.location();
                isSuccessFull = true;
            } catch (Exception e) {
                LogWrapper.printStackTrace(getApplicationContext(), e);
            }
            return loginLocation;

        }

        @Override
        protected void onPostExecute(String result) {
            String obtainedURL = loginLocation;
            mProgressDialog.dismiss();
            if (isSuccessFull) {
                if (obtainedURL.equalsIgnoreCase(Constants.URL_LOGIN)) {

                    final Dialog dialogCaptcha = new Dialog(LauncherActivity.this);
                    dialogCaptcha.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialogCaptcha.setContentView(R.layout.captcha_custom_layout);

                    dialogCaptcha.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    dialogCaptcha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialogCaptcha.setTitle(getResources().getString(R.string.captcha_dialog_title));
                    dialogCaptcha.setCanceledOnTouchOutside(false);


                    ImageView image = (ImageView) dialogCaptcha.findViewById(R.id.image);
                    final EditText editTextCaptcha = (EditText) dialogCaptcha.findViewById(R.id.editText1);
                    image.setImageBitmap(bitmap);

                    editTextCaptcha.setTypeface(Typeface
                            .createFromAsset(getAssets(), Constants.FONT_ROBOTO_MEDIUM));

                    TextView dialogButton = (TextView) dialogCaptcha.findViewById(R.id.dialogButtonOK);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            captcha = editTextCaptcha.getText().toString();
                            dialogCaptcha.dismiss();
                            new AutoLoginAsyncTask().execute();
                        }
                    });

                    dialogCaptcha.show();


                } else {
                    mProgressDialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
                    builder.setMessage("Error loading Captcha")
                            .setCancelable(false)
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    intentToLoginActivity();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            } else {
                errorDialog();
            }
        }
    }

    //for direct login
    public class AutoLoginAsyncTask extends AsyncTask<Void, Void, String> {


        String sessionId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LauncherActivity.this);
            mProgressDialog.setTitle(getResources().getString(R.string.login_progress_dialog));
            mProgressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            loginLocation = "";
            username = (sharedPreferencesLoginTable.getString(Constants.SHARED_PREF_USERNAME, ""));
            password = (sharedPreferencesLoginTable.getString(Constants.SHARED_PREF_PASSWORD, ""));
            isSuccessFull = false;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                Connection.Response
                        response = Jsoup.connect(Constants.URL_LOGIN)
                        .cookie(Constants.COOKIE_ASP_NET, cookString)
                        .data(Constants.STRING_LAST_FOCUS, "")
                        .data(Constants.STRING_EVENT_TARGET, "")
                        .data(Constants.STRING_EVENT_ARGUMENT, "")
                        .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                        .data(Constants.STRING_VIEW_STATE, "")
                        .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                        .data(textField1.attr("id"), username)
                        .data(Constants.LOGIN_TEXT_PASSWORD, password)
                        .data(textField2.attr("id"), captcha)
                        .data(textField3.attr("id"), username)
                        .data(Constants.LOGIN_BUTTON_ID, "")
                        .data("btnValue", "Button")
                        .data(Constants.LOGIN_TEXT_USERNAME, "")
                        .data(Constants.LOGIN_TEXT_BIRTH, "")
                        .data(Constants.LOGIN_HID_SET_NAME, "ok")
                        .userAgent(Constants.USER_AGENT)
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .timeout(0)
                        .execute();

                Document document = response.parse();

                loginLocation = document.location();
                sessionId = cookString;
                isSuccessFull = true;

            } catch (Exception e) {
                LogWrapper.printStackTrace(getApplicationContext(), e);
            }
            return loginLocation;

        }

        @Override
        protected void onPostExecute(String result) {
            String obtainedURL = loginLocation;

            mProgressDialog.dismiss();
            if (isSuccessFull) {
                if (obtainedURL.equalsIgnoreCase(Constants.URL_HOME)) {
                    Details details = new Details(LauncherActivity.this, username, password, sessionId);
                    details.execute();

//                    Intent i = new Intent(LauncherActivity.this, MyActivity.class);
//                    i.putExtra(Constants.USERNAME_INTENT, username);
//                    i.putExtra(Constants.PASSWORD_INTENT, password);
//                    i.putExtra(Constants.COOKIE_INTENT, sessionId);
//                    i.putExtra(Constants.BRANCH_INTENT, username.toUpperCase().trim().substring(5, 7));
//                    startActivity(i);
//                    overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
//                    finish();

                } else {
                    mProgressDialog.dismiss();

                    SharedPreferences.Editor ed3 = sharedPreferencesLoginTable.edit();
                    ed3.putString(Constants.SHARED_PREF_AUTO_LOGIN, Constants.AUTO_LOGIN_FALSE_CASE);
                    ed3.apply();

                    AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
                    builder.setMessage("Incorrect UserName or Password")
                            .setCancelable(false)
                            .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            }).setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intentToLoginActivity();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else
                errorDialog();
        }
    }

    private void intentToLoginActivity() {
        Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        startActivity(intent);
        finish();
    }

    private void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
        builder.setMessage(getResources().getString(R.string.server_failed))
                .setCancelable(false).setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                intentToLoginActivity();
            }
        })
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}