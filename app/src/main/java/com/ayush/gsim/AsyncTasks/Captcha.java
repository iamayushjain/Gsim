package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ayush.gsim.Activities.LoginActivity;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;

/**
 * Created by ayush on 27/1/17.
 */
public class Captcha extends AsyncTask<Void, Void, String> {


    private ProgressDialog mProgressDialog;
    private ProgressBar progressBar;
    private String userName, password;
    private TextView welcome, powered;

    private Element lastFocus, eventTarget, eventArgument, vsKey, viewState, eventValidation, textField1, textField2, textField3;
    private Bitmap bitmap;
    private String captcha;
    private Document loginPage;
    private Connection.Response response;
    private String sessionCookie = "";
    private String loginLocation;
    private SharedPreferences sharedPreferencesLoginTable;
    private Activity activity;
    private boolean isSuccessFull;

    public Captcha(Activity activity, String userName, String passWord, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.userName = userName;
        this.password = passWord;
        this.sharedPreferencesLoginTable = sharedPreferences;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle("Loading Captcha");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        isSuccessFull = false;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {


            response = Jsoup.connect(Constants.URL_LOGIN)
                    .method(Connection.Method.GET)
                    .userAgent(Constants.USER_AGENT)
                    .timeout(0)
                    .execute();


            loginPage = response.parse();


            LogWrapper.d(activity.getApplicationContext(), "Title:", loginPage.title());

            lastFocus = loginPage.select(Constants.INPUT_LAST_FOCUS).first();
            eventTarget = loginPage.select(Constants.INPUT_EVENT_TARGET).first();
            eventArgument = loginPage.select(Constants.INPUT_EVENT_ARGUMENT).first();
            vsKey = loginPage.select(Constants.INPUT_VS_KEY).first();
            viewState = loginPage.select(Constants.INPUT_VIEW_STATE).first();
            eventValidation = loginPage.select(Constants.INPUT_EVENT_VALIDATION).first();

            sessionCookie = response.cookie(Constants.COOKIE_ASP_NET);
            Element captcha = loginPage.select(Constants.INPUT_CAPTCHA_ID).first();

            Elements elementsTextField = loginPage.select(Constants.INPUT_TYPE_TEXT);
            int me_count = 0;

            textField1 = loginPage.select(Constants.INPUT_TYPE_TEXT).last();
            textField2 = loginPage.select(Constants.INPUT_TYPE_TEXT).last();
            textField3 = loginPage.select(Constants.INPUT_TYPE_TEXT).last();
            for (Element e : elementsTextField) {
                if (me_count == 0) {
                    textField1 = e;
                }
                if (me_count == 1) {
                    textField2 = e;
                }
                if (me_count == 2) {
                    textField3 = e;
                }
                me_count++;
            }


            response = Jsoup //
                    .connect(captcha.absUrl("src")) // Extract image absolute URL
                    .cookie(Constants.COOKIE_ASP_NET, sessionCookie) // Grab cookies
                    .ignoreContentType(true)
                    .timeout(0) // Needed for fetching image
                    .execute();
            bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(response.bodyAsBytes()));
            loginLocation = loginPage.location();
            isSuccessFull = true;
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
            isSuccessFull = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {//
        String obtainedURL = loginLocation;
        mProgressDialog.dismiss();
        if (isSuccessFull) {
            if (obtainedURL.equalsIgnoreCase(Constants.URL_LOGIN)) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.captcha_custom_layout);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setTitle(activity.getResources().getString(R.string.captcha_dialog_title));
                dialog.setCanceledOnTouchOutside(false);

                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                final EditText editText_cap = (EditText) dialog.findViewById(R.id.editText1);
                editText_cap.setTypeface(Typeface
                        .createFromAsset(activity.getAssets(), Constants.FONT_ROBOTO_MEDIUM));

                image.setImageBitmap(bitmap);
                editText_cap.setTypeface(Typeface
                        .createFromAsset(activity.getAssets(), Constants.FONT_ROBOTO_MEDIUM));

                TextView dialogButton = (TextView) dialog.findViewById(R.id.dialogButtonOK);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        captcha = editText_cap.getText().toString();
                        dialog.dismiss();
                        new AutoLoginAsyncTask().execute();
                    }
                });

                dialog.show();


            } else {
                mProgressDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Error Loading Captcha")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LoginActivity.passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                LoginActivity.passwordEditText.setTypeface(Typeface
                                        .createFromAsset(activity.getAssets(), Constants.FONT_ROBOTO_THIN));

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }


    private class AutoLoginAsyncTask extends AsyncTask<Void, Void, String> {

        String sessionId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setTitle(activity.getResources().getString(R.string.login_progress_dialog));
            mProgressDialog.setMessage(activity.getResources().getString(R.string.loading_progress_dialog));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            loginLocation = "";
            isSuccessFull = false;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                Connection.Response response = Jsoup.connect(Constants.URL_LOGIN)
                        .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                        .data(Constants.STRING_LAST_FOCUS, "")
                        .data(Constants.STRING_EVENT_TARGET, "")
                        .data(Constants.STRING_EVENT_ARGUMENT, "")
                        .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                        .data(Constants.STRING_VIEW_STATE, "")
                        .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                        .data(textField1.attr("id"), userName)
                        .data(Constants.LOGIN_TEXT_PASSWORD, password)
                        .data(textField2.attr("id"), captcha)
                        .data(textField3.attr("id"), userName)
                        .data(Constants.LOGIN_BUTTON_ID, "")
                        .data("btnValue", "Button")
                        .data(Constants.LOGIN_TEXT_USERNAME, "")
                        .data(Constants.LOGIN_TEXT_BIRTH, "")
                        .data(Constants.LOGIN_HID_SET_NAME, "ok")


                        .userAgent(Constants.USER_AGENT)
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .timeout(10000)
                        .execute();


                Document document = response.parse();

                loginLocation = document.location();
                sessionId = sessionCookie;

                LogWrapper.d(activity.getApplicationContext(), "Page:", loginLocation);
                isSuccessFull = true;
            } catch (Exception e) {
                LogWrapper.printStackTrace(activity.getApplicationContext(), e);
                isSuccessFull = false;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {//
            String obtainedURL = loginLocation;
            mProgressDialog.dismiss();
            if (isSuccessFull) {
                if (obtainedURL.equalsIgnoreCase(Constants.URL_HOME)) {

                    Details details = new Details(activity, userName, password, sessionCookie);
                    details.execute();

//                    Intent i = new Intent(activity, MyActivity.class);
//                    i.putExtra(Constants.USERNAME_INTENT, userName.toUpperCase().trim());
//                    i.putExtra(Constants.PASSWORD_INTENT, password);
//                    i.putExtra(Constants.COOKIE_INTENT, sessionId);
//                    i.putExtra(Constants.BRANCH_INTENT, userName.toUpperCase().trim().substring(5, 7));
//
//                    activity.startActivity(i);
//                    activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
//
//                    activity.finish();

                } else {
                    SharedPreferences.Editor ed1 = sharedPreferencesLoginTable.edit();
                    ed1.putString(Constants.SHARED_PREF_AUTO_LOGIN, Constants.AUTO_LOGIN_FALSE_CASE);
                    ed1.commit();

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Incorrect UserName or Password")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    LoginActivity.passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    LoginActivity.passwordEditText.setTypeface(Typeface
                                            .createFromAsset(activity.getAssets(), Constants.FONT_ROBOTO_THIN));

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }
        }
    }
}

