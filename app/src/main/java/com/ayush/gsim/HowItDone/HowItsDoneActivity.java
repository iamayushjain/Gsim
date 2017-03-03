package com.ayush.gsim.HowItDone;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.ayush.gsim.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class HowItsDoneActivity extends AppCompatActivity {

  //  DrawerLayout mDrawerLayout;
   // ViewPager viewPager;
    ListView listView;
    private Toolbar toolbar;
    String s,s1;
    int navi=0;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.appbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(Typeface
                .createFromAsset(getAssets(), "robotothin.ttf"));
        TypedValue tv = new TypedValue();
        float actionBarHeight;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            mTitle.setTextSize(actionBarHeight / 5);

        }
        mInterstitialAd = new InterstitialAd(HowItsDoneActivity.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5666077083567257/9936882129");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("CE8A765D73C169864D8222080FCC9DA7")
                //.addTestDevice("A2DA3C272CCDBBE0F0EC842FBF9BFC8B")
                .build();

        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

            }
        });


//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Description_page frag=new Description_page();

                HowItsDoneDetailsFragment frag1 = HowItsDoneDetailsFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, frag1).commit();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivityForResult(myIntent, 0);finish();
        finish();
        mInterstitialAd.show();

        overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);


        return true;

    }

    @Override
    public void onBackPressed() {
        finish();
        mInterstitialAd.show();

        overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
    }
}

