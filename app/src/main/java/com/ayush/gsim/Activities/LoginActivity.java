package com.ayush.gsim.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayush.gsim.AsyncTasks.Captcha;
import com.ayush.gsim.R;
import com.ayush.gsim.Service.ServiceActivation;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.ayush.gsim.Utils.Utils;
import com.ayush.gsim.Utils.VersionControl;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {

    private EditText userNameEditText;
    public static EditText passwordEditText;
    private Button loginButton;
    private CheckBox rememberMeCheckBox, autoLoginCheckBox;
    private ImageView imageUser, imagePassword;
    private Button newsFeed;
    private RelativeLayout relativeLayoutMain;
    private ImageButton visiblePassword;
    private TextView loginText;
    private SharedPreferences sharedPreferencesLoginTable;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;
    private int adsCountLimit, currentAdsCount;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences sharedPreferencesAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            setContentView(R.layout.login_activity);
            init();
        } catch (Exception e) {
            LogWrapper.printStackTrace(getApplicationContext(), e);
        }
        VersionControl versionControl = new VersionControl(this);
        versionControl.fireBaseEachVersion();

        ServiceActivation serviceActivation = new ServiceActivation();
        serviceActivation.firebaseServiceCondition(this, databaseReferenceGsim);

        loadInterstitialAds();
    }

    private void init() {
        userNameEditText = (EditText) findViewById(R.id.editText1);
        passwordEditText = (EditText) findViewById(R.id.editText2);
        loginButton = (Button) findViewById(R.id.button1);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.checkBox1);
        autoLoginCheckBox = (CheckBox) findViewById(R.id.checkBox2);
        newsFeed = (Button) findViewById(R.id.newsfeed);
        visiblePassword = (ImageButton) findViewById(R.id.visiblePassword);
        relativeLayoutMain = (RelativeLayout) findViewById(R.id.relativeLayoutMain);
        imageUser = (ImageView) findViewById(R.id.imageUser);
        imagePassword = (ImageView) findViewById(R.id.imagePassword);

        imageUser.setBackgroundColor(Color.TRANSPARENT);
        imagePassword.setBackgroundColor(Color.TRANSPARENT);
        visiblePassword.setBackgroundColor(Color.TRANSPARENT);

        sharedPreferencesLoginTable = getSharedPreferences(Constants.SHARED_PREF_LOGIN_TABLE, 0);

        imageUser.setColorFilter(R.color.colorPrimary);
        imagePassword.setColorFilter(R.color.colorPrimary);
        visiblePassword.setColorFilter(R.color.colorPrimary);
        loginText = (TextView) findViewById(R.id.loginText);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.GENERAL);


        loginText.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_MEDIUM));
        rememberMeCheckBox.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_MEDIUM));
        autoLoginCheckBox.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_MEDIUM));
        newsFeed.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_MEDIUM));
        loginButton.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_MEDIUM));
        userNameEditText.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));
        passwordEditText.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));

        userNameEditText.setText(sharedPreferencesLoginTable.getString(Constants.SHARED_PREF_USERNAME, ""));

        passwordEditText.setText(sharedPreferencesLoginTable.getString(Constants.SHARED_PREF_PASSWORD, ""));

        relativeLayoutMain.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_slide_in));

        visiblePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getInputType() == 129) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordEditText.setTypeface(Typeface
                            .createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));

                } else {
                    passwordEditText.setInputType(129);
                    passwordEditText.setTypeface(Typeface
                            .createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));

                }
            }
        });
        newsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, NewsFeedActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

            }
        });
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userNameEditText, InputMethodManager.SHOW_IMPLICIT);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    onSubmit();
                return true;
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onSubmit();
            }
        });

    }


    private void onSubmit() {
        SharedPreferences.Editor ed1 = sharedPreferencesLoginTable.edit();

        if (rememberMeCheckBox.isChecked()) {
            ed1.putString(Constants.SHARED_PREF_USERNAME, userNameEditText.getText().toString().toUpperCase().trim());
            ed1.putString(Constants.SHARED_PREF_PASSWORD, passwordEditText.getText().toString());
            ed1.apply();
        }
        if (autoLoginCheckBox.isChecked()) {
            ed1.putString(Constants.SHARED_PREF_USERNAME, userNameEditText.getText().toString().toUpperCase().trim());
            ed1.putString(Constants.SHARED_PREF_PASSWORD, passwordEditText.getText().toString());
            ed1.putString(Constants.SHARED_PREF_AUTO_LOGIN, Constants.AUTO_LOGIN_TRUE_CASE);
            ed1.apply();
        } else {
            ed1.putString(Constants.SHARED_PREF_AUTO_LOGIN, Constants.AUTO_LOGIN_FALSE_CASE);
            ed1.apply();

        }
        if (Utils.isNetworkAvailable(getApplicationContext())) {

            Captcha captchaLoader = new Captcha(LoginActivity.this,
                    userNameEditText.getText().toString().toUpperCase().trim(),
                    passwordEditText.getText().toString(),
                    sharedPreferencesLoginTable);
            captchaLoader.execute();

        } else {
            Utils.noInternetDialog(LoginActivity.this);
        }
    }

    private void loadInterstitialAds() {

        sharedPreferencesAds = getSharedPreferences(Constants.SHARED_PREF_ADS_COUNT_TABLE, 0);
        String adSavedOccurrence = (sharedPreferencesAds.getString(Constants.SHARED_PREF_LOGIN_ADS_COUNT, "0"));
        currentAdsCount = Integer.parseInt(adSavedOccurrence);
        currentAdsCount++;


        databaseReferenceGsim.child(FirebaseConstants.ADS_COUNT_LOGIN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                adsCountLimit = Integer.parseInt(snapshot.getValue().toString());

                if (currentAdsCount >= adsCountLimit) {
                    currentAdsCount = 0;
                    mInterstitialAd = new InterstitialAd(LoginActivity.this);
                    mInterstitialAd.setAdUnitId(getResources().getString(R.string.login_ad));
                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice(getResources().getString(R.string.testid))
                            .build();
                    mInterstitialAd.loadAd(adRequest);

                    mInterstitialAd.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            while (mInterstitialAd.isLoading()) {
                            }
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }

                        }
                    });
                }

                SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                editor.putString(Constants.SHARED_PREF_LOGIN_ADS_COUNT, currentAdsCount + "");
                editor.apply();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogWrapper.out(getApplicationContext(), databaseError.getMessage());
            }
        });
    }
}

	
	
