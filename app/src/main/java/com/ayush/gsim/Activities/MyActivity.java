package com.ayush.gsim.Activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.gsim.FragmentDrawer;
import com.ayush.gsim.Fragments.AttendanceFragment_Database;
import com.ayush.gsim.Fragments.DetailFragment;
import com.ayush.gsim.Fragments.DeveloperContactPage;
import com.ayush.gsim.R;
import com.ayush.gsim.Service.ServiceActivation;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.ayush.gsim.Utils.Utils;
import com.ayush.gsim.Utils.VersionControl;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;

public class MyActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final String TAG = MyActivity.class.getSimpleName();
    private TextView mTitle;
    private String userName, passWord, sessionCookie;
    private Bundle args;
    private AdView mAdView;
    private int adsCountLimit = 15;
    private int currentAdsCount;
    private SharedPreferences sharedPreferencesAds;
    private SweetSheet mSweetSheet;
    private Toolbar toolbar;
    private InterstitialAd mInterstitialAd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;
    private ImageButton toolbarChatButton, toolbarOptions;
    private FragmentDrawer drawerFragment;
    public static String studentName;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                try {
                    LogWrapper.e(getApplicationContext(), "Error in thread", ex.getMessage());
                    ex.printStackTrace();
                    LogWrapper.printStackTraceThrowable(getApplicationContext(), ex);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setContentView(R.layout.activity_my);
        init();


        Bundle bundle = getIntent().getExtras();

        userName = bundle.getString(Constants.USERNAME_INTENT);
        passWord = bundle.getString(Constants.PASSWORD_INTENT);
        sessionCookie = bundle.getString(Constants.COOKIE_INTENT);
        studentName = bundle.getString(Constants.STUDENT_NAME_INTENT);

        args.putString(Constants.USERNAME_INTENT, userName);
        args.putString(Constants.PASSWORD_INTENT, passWord);
        args.putString(Constants.COOKIE_INTENT, sessionCookie);
        args.putString(Constants.STUDENT_NAME_INTENT, studentName);

        if (savedInstanceState == null) {
            navigateTo(0);
        }
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        initDrawerNew();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarChatButton = (ImageButton) toolbar.findViewById(R.id.chatButton);
        toolbarOptions = (ImageButton) toolbar.findViewById(R.id.options);
        toolbarChatButton.setVisibility(View.VISIBLE);
        toolbarOptions.setVisibility(View.VISIBLE);
        mAdView = (AdView) findViewById(R.id.adView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.GENERAL);
        args = new Bundle();
        mTitle.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));
        TypedValue tv = new TypedValue();
        float actionBarHeight;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            mTitle.setTextSize(actionBarHeight / 5);
        }
        toolbarChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(0);
            }
        });
        toolbarOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionOnClick();
            }
        });
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        rl.setVisibility(View.VISIBLE);
        mSweetSheet = new SweetSheet(rl);
    }


    private void initDrawerNew() {
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), toolbar);
        drawerFragment.setDrawerListener(this);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        navigateTo(position);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void optionOnClick() {

        if (!mSweetSheet.isShow()) {

            mSweetSheet.setMenuList(R.menu.menu_settings);
            mSweetSheet.setDelegate(new RecyclerViewDelegate(true));
            mSweetSheet.setBackgroundEffect(new DimEffect(0.5f));
            mSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
                @Override
                public boolean onItemClick(int position, MenuEntity menuEntity1) {

                    if (position == 0) {
                        Utils.logout(MyActivity.this);
                    } else if (position == 1) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.APP_URL));
                        startActivity(browserIntent);
                    }
                    return true;
                }
            });
            mSweetSheet.toggle();
        }
    }

    private void loadBannerAds() {
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.testid))
                .build();
        mAdView.loadAd(adRequest1);
    }

    private void loadInterstitialAds() {

        sharedPreferencesAds = getSharedPreferences(Constants.SHARED_PREF_ADS_COUNT_TABLE, 0);
        String adSavedOccurrence = (sharedPreferencesAds.getString(Constants.SHARED_PREF_HOME_ADS_COUNT, "0"));
        currentAdsCount = Integer.parseInt(adSavedOccurrence);
        currentAdsCount++;

        databaseReferenceGsim.child(FirebaseConstants.ADS_COUNT_HOME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                adsCountLimit = Integer.parseInt(snapshot.getValue().toString());

                if (currentAdsCount >= adsCountLimit) {
                    currentAdsCount = 0;
                    mInterstitialAd = new InterstitialAd(MyActivity.this);
                    mInterstitialAd.setAdUnitId(getResources().getString(R.string.mainActivity_ad));
                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice(getResources().getString(R.string.testid))
                            .build();

                    mInterstitialAd.loadAd(adRequest);
                    mInterstitialAd.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            while (mInterstitialAd.isLoading()) {
                                continue;
                            }
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }

                        }
                    });
                }

                SharedPreferences.Editor editor = sharedPreferencesAds.edit();
                editor.putString(Constants.SHARED_PREF_HOME_ADS_COUNT, currentAdsCount + "");
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogWrapper.out(getApplicationContext(), databaseError.getMessage());
            }
        });

    }

    private void navigateTo(int position) {

        LogWrapper.d(getApplicationContext(), TAG, "Drawer List View Item: " + position);

        mAdView.bringToFront();

        switch (position) {
            case 0:
                DetailFragment detailFragment = DetailFragment.newInstance();
                detailFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, detailFragment, "Details").commit();
                break;
            case 1:
                DeveloperContactPage developerContactPage = DeveloperContactPage.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, developerContactPage).commit();

                break;
            case 2:
                AttendanceFragment_Database.deleteData(MyActivity.this);
                Toast.makeText(getApplicationContext(), "Data has been Refreshed", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Utils.logout(MyActivity.this);
                break;

        }

    }


    @Override
    public void onBackPressed() {

        if (mSweetSheet.isShow()) {
            mSweetSheet.dismiss();
        } else {
            if (getSupportFragmentManager().findFragmentByTag("Details") != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                builder.setMessage("Do you wanna exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog alert = builder.create();
                if (!alert.isShowing()) {
                    alert.show();
                }
            } else {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
                frameLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_slide_in));
                navigateTo(0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBannerAds();
        loadInterstitialAds();

        VersionControl versionControl = new VersionControl(this);
        versionControl.fireBaseEachVersion();

        ServiceActivation serviceActivation = new ServiceActivation();
        serviceActivation.firebaseServiceCondition(this, databaseReferenceGsim);

    }
}