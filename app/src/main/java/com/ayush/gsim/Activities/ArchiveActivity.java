package com.ayush.gsim.Activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;

import com.ayush.gsim.ArchivePad.ArchivePadFragment;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArchiveActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private InterstitialAd mInterstitialAd;
    private int adsCountLimit = 5;
    private int currentAdsCount;
    private SharedPreferences sharedPreferencesAds;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
        try {
            loadInterstitialAds();
        } catch (Exception e) {
            LogWrapper.printStackTrace(getApplicationContext(), e);
        }

        ArchivePadFragment archivePadFragment = new ArchivePadFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, archivePadFragment).commit();

    }

    private void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.GENERAL);
        toolbar = (Toolbar) findViewById(R.id.appbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(Typeface
                .createFromAsset(getAssets(), Constants.FONT_ROBOTO_THIN));
        TypedValue tv = new TypedValue();
        float actionBarHeight;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            mTitle.setTextSize(actionBarHeight / 5);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();
        overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
    }

    private void loadInterstitialAds() {

        sharedPreferencesAds = getSharedPreferences(Constants.SHARED_PREF_ADS_COUNT_TABLE, 0);
        String adSavedOccurrence = (sharedPreferencesAds.getString(Constants.SHARED_PREF_ARCHIVE_ADS_COUNT, "0"));
        currentAdsCount = Integer.parseInt(adSavedOccurrence);
        currentAdsCount++;

        databaseReferenceGsim.child(FirebaseConstants.ADS_COUNT_ARCHIVE_PAD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                adsCountLimit = Integer.parseInt(snapshot.getValue().toString());

                if (currentAdsCount >= adsCountLimit) {
                    currentAdsCount = 0;
                    mInterstitialAd = new InterstitialAd(ArchiveActivity.this);
                    mInterstitialAd.setAdUnitId(getResources().getString(R.string.archive_ad));
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
                editor.putString(Constants.SHARED_PREF_ARCHIVE_ADS_COUNT, currentAdsCount + "");
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogWrapper.out(getApplicationContext(), databaseError.getMessage());
            }
        });
    }
}

