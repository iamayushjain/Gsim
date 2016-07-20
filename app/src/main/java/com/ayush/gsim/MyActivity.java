package com.ayush.gsim;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.gsim.NewsFeed.Main_Page;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextView mTitle;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] menu = {"Name","News Feed", "Attendance", "Date Wise Attendance(Today)", "Subject Attendance", "Date Wise", "Today Attendance", "Time Table", "Library", "Developer"};

    String s, p, ck;
    ProgressDialog mProgressDialog;
    private static final String TAG = MyActivity.class.getSimpleName();
    String sub_name[] = null;
    String day[] = null;
    String cl_time[] = null;
    String sub_fac[] = null;
    String batch[] = null;
    String block[] = null;
    String book_issue_title[] = null;
    String book_issue_date[] = null;
    String book_due_date[] = null;
    String book_return_date[] = null;
    String total_fine = null;
    String attendance_subject_name[] = null;
    String attendance_subject_percentage[] = null;
    String attendance_subject_percentdemo[] = null;
    String attendance_subject_values[] = null;
    String attendance_subject_valuesdemo[] = null;


    String date_wise_today_date[] = null;
    String date_wise_today_name[] = null;
    String date_wise_today_time[] = null;
    String date_wise_today_type[] = null;
    String date_wise_today_status[] = null;
    String date_from = null;
    String date_to = null;
    String total_paid = null;

    String total_balance = null;

    String total_waived = null;
    //private Toolbar toolbar;


    int rpc[] = {20, 20, 20, 20, 20, 20};
    String offline = null;
    Bundle args = new Bundle();
    AdView mAdView;
    private InterstitialAd mInterstitialAd;
    int ads_count = 15;
    int myIntValue;
    SharedPreferences sp;
    Bundle bundSaved;
    SweetSheet mSweetSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundSaved = savedInstanceState;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
//                SharedPreferences sp3=getSharedPreferences("QPQ",0);
//                SharedPreferences.Editor ed3=sp3.edit();
//                ed3.putString("WPW","0");
//                ed3.commit();

                //finish();
//                Bundle arg=new Bundle();
//
//                arg.putString("Menu", "1");
//                arg.putString("User", s);
//                arg.putString("Pass", p);
//                arg.putString("Cook", ck);
//
//                arg.putStringArray("Array", null);
//                arg.putStringArray("Array1", null);
//
//                arg.putStringArray("Array2",null);
//                arg.putStringArray("Array3", null);
//                //arg.putStringArray("Array5", block);
//                //detail.setArguments(args);
//                ErrorFragment_MyActivity tb1=new ErrorFragment_MyActivity();
//                //tb1.setArguments(arg);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, tb1, LibraryActivity.TAG).commit();
                if(mProgressDialog!=null && mProgressDialog.isShowing())
                {
                    mProgressDialog.cancel();
                }
                Intent i = new Intent(MyActivity.this, ErrorActivity.class);
                startActivity(i);


            }
        });

        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_my);
//       sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
//         myIntValue = sp.getInt("your_int_key", -1);
//        myIntValue++;
//        Firebase myFirebaseRef = new Firebase("https://gsim.firebaseio.com/");
//        myFirebaseRef.child("Ads_Count").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
//                //Toast.makeText(getApplicationContext(),snapshot.getValue().toString(),3000).show();
//                ads_count = Integer.parseInt(snapshot.getValue().toString());
//
//                if (myIntValue >= ads_count) {
//                    myIntValue = 0;
//                    mInterstitialAd = new InterstitialAd(MyActivity.this);
//                    mInterstitialAd.setAdUnitId("ca-app-pub-5666077083567257/2174168522");
//                    AdRequest adRequest = new AdRequest.Builder()
//                            .addTestDevice("479BACC5AB5371AC9BDCEC2DE7A45F28")
//                                    //.addTestDevice("A2DA3C272CCDBBE0F0EC842FBF9BFC8B")
//                            .build();
//
//                    mInterstitialAd.loadAd(adRequest);
//                    mInterstitialAd.setAdListener(new AdListener() {
//                        public void onAdLoaded() {
//                            while(mInterstitialAd.isLoading())
//                            {
//                                continue;
//                            }
//                            if (mInterstitialAd.isLoaded()) {
//                                mInterstitialAd.show();
//                            }
//
//                        }
//                    });
//                }
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putInt("your_int_key", myIntValue);
//                editor.commit();
//
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {
//            }
//        });
//
//
        sp = getSharedPreferences("ads_count1", 0);
        String myIntValue1 = (sp.getString("ads_count_no1", "0"));
        myIntValue = Integer.parseInt(myIntValue1);
        myIntValue++;

        //Toast.makeText(getApplicationContext(),myIntValue1,3000).show();
        Firebase myFirebaseRef = new Firebase("https://gsim.firebaseio.com/");
        myFirebaseRef.child("Ads_Count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                //Toast.makeText(getApplicationContext(),snapshot.getValue().toString(),3000).show();
                ads_count = Integer.parseInt(snapshot.getValue().toString());

                if (myIntValue >= ads_count) {
                    myIntValue = 0;
                    mInterstitialAd = new InterstitialAd(MyActivity.this);
                    mInterstitialAd.setAdUnitId("ca-app-pub-5666077083567257/9750820928");
                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice("CE8A765D73C169864D8222080FCC9DA7")
                            //.addTestDevice("A2DA3C272CCDBBE0F0EC842FBF9BFC8B")
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

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ads_count_no1", myIntValue + "");
                editor.commit();


            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest1 = new AdRequest.Builder()
                .addTestDevice("CE8A765D73C169864D8222080FCC9DA7")
                // .addTestDevice("A2DA3C272CCDBBE0F0EC842FBF9BFC8B")
                .build();
        mAdView.loadAd(adRequest1);

        Bundle bund = getIntent().getExtras();
        s = bund.getString("User");
        p = bund.getString("Password");
        ck = bund.getString("Cookie");
        //dList.setSelector(android.R.color.holo_blue_dark);
        args.putString("Menu", menu[0]);
        args.putString("User", s);
        args.putString("Pass", p);
        args.putString("Cook", ck);

        nitView();
//        SharedPreferences sp2=getSharedPreferences("QPQ",0);
//        offline=(sp2.getString("WPW",""));

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //    displayView();
            navigateTo(0);

        }
        if (toolbar != null) {
            toolbar.setTitle("");

            setSupportActionBar(toolbar);
        }
        initDrawer();
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {


                navigateTo(position);

            }

        });
        mAdView.bringToFront();

    }

    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter = new ArrayAdapter<String>(MyActivity.this, android.R.layout.simple_list_item_1, menu);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(Typeface
                .createFromAsset(getAssets(), "robotothin.ttf"));
        TypedValue tv = new TypedValue();
        float actionBarHeight;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            mTitle.setTextSize(actionBarHeight / 5);

        }

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        rl.setVisibility(View.VISIBLE);
        mSweetSheet = new SweetSheet(rl);

    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     MenuInflater ifg=getMenuInflater();
        ifg.inflate(R.menu.menu_main, menu);

        //getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if (!mSweetSheet.isShow()) {


            mSweetSheet.setMenuList(R.menu.menu_settings);
            mSweetSheet.setDelegate(new RecyclerViewDelegate(true));
            mSweetSheet.setBackgroundEffect(new DimEffect(0.5f));
            mSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
                @Override
                public boolean onItemClick(int position, MenuEntity menuEntity1) {

                    if (position == 0)
                    {
                        Intent i = new Intent(MyActivity.this, MyMainActivity.class);

                        SharedPreferences sp3 = getSharedPreferences("QPQ", 0);
                        SharedPreferences.Editor ed3 = sp3.edit();
                        ed3.putString("WPW", "0");
                        ed3.commit();

                        startActivity(i);
                        //overridePendingTransition(R.anim.right_slide_out, R.anim.right_slide_in);
                        overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);

                        finish();

                    }
                    else if (position == 1)
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ayush.gsim"));
                        startActivity(browserIntent);
                    }
                    return true;
                }
            });
            mSweetSheet.toggle();


        }

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void navigateTo(int position) {
//		Log.v(TAG, "List View Item: " + position);
        //	Fragment detail =new DetailFragment();
        Log.v(TAG, "List View Item: " + position);

        //	Fragment tbb =new TabbedActivity();
        //Bundle args = new Bundle();
        mAdView.bringToFront();

        switch (position) {
            case 0:
                drawerLayout.closeDrawers();
                args.putString("Menu", menu[position]);
                args.putString("User", s);
                args.putString("Pass", p);
                args.putString("Cook", ck);
                DetailFragment detailFragment = DetailFragment.newInstance();
                detailFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, detailFragment).commit();

                break;
            case 2:

                drawerLayout.closeDrawers();
                //Bundle args = new Bundle();
                args.putString("Menu", menu[position]);
                args.putString("User", s);
                args.putString("Pass", p);
                args.putString("Cook", ck);

//                    AttendanceFragment attendaceFragment = AttendanceFragment.newInstance();
//
//                    attendaceFragment.setArguments(args);
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.content_frame, attendaceFragment).commit();
//
                AttendanceFragment_COLOR attendaceFragment = AttendanceFragment_COLOR.newInstance();

                attendaceFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, attendaceFragment).commit();

                break;
            case 7:

                drawerLayout.closeDrawers();
                new MyTask().execute();

                break;
            case 8:
                drawerLayout.closeDrawers();
                new MyTask2().execute();
                break;
            case 9:
                drawerLayout.closeDrawers();
                Contact_page_Fact_dev tb13 = new Contact_page_Fact_dev();
                //tb12.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, tb13).commit();


                break;
            case 6:

                drawerLayout.closeDrawers();
                new MyTask3().execute();

                break;
            case 4:

                drawerLayout.closeDrawers();
                new MyTask4().execute();

                break;
            case 3:
                drawerLayout.closeDrawers();
                new MyTask5().execute();

                break;
            case 5:
                drawerLayout.closeDrawers();
                getDateFunction();


                break;
            case 1:
                drawerLayout.closeDrawers();
                Main_Page main_page = new Main_Page();
                //tb12.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, main_page).commit();


                break;


        }
        // mAdView.bringToFront();

    }

    public void getDateFunction() {

        final Dialog myDialog = new Dialog(MyActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        myDialog.setContentView(R.layout.datelayout);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  myDialog.setTitle("PICK DATE");
        //myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(true);
        //relativeLayout.setBackgroundColor(Color.TRANSPARENT);

        ImageButton b1 = (ImageButton) myDialog.findViewById(R.id.calc_clear_txt_Prise);
        ImageButton b2 = (ImageButton) myDialog.findViewById(R.id.calc_clear_txt_Prise1);
        Button b3 = (Button) myDialog.findViewById(R.id.bno);
        Button b4 = (Button) myDialog.findViewById(R.id.byes);
        b1.setBackgroundColor(Color.TRANSPARENT);
        b2.setBackgroundColor(Color.TRANSPARENT);

        final EditText e1 = (EditText) myDialog.findViewById(R.id.calc_txt_Prise);
        final EditText e2 = (EditText) myDialog.findViewById(R.id.calc_txt_Prise1);
        e1.setTypeface(Typeface
                .createFromAsset(getAssets(), "robotothin.ttf"));
        e2.setTypeface(Typeface
                .createFromAsset(getAssets(), "robotothin.ttf"));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Dialog myDialog1 = new Dialog(MyActivity.this);
                //myDialog.setContentView(R.layout.datelayout);
                final DatePicker input = new DatePicker(MyActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setMaxDate(System.currentTimeMillis());
                // input.setBackgroundColor(Color.parseColor("#000000"));
                input.setBackgroundColor(getResources().getColor(R.color.newgreen));
                //input.setMinDate(System.currentTimeMillis()-2678400);
                input.setLayoutParams(lp);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                final AlertDialog alert;
                builder.setView(input).setMessage("PICK DATE").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        //  finish();
                        int day = input.getDayOfMonth();
                        int month = input.getMonth() + 1;
                        int year = input.getYear();
                        //Toast.makeText(getApplicationContext(),day+""+month+""+year,3000).show();
                        e1.setText(day + "/" + month + "/" + year);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        //  finish();
                    }
                });

                alert = builder.create();
                if (!alert.isShowing()) {
                    alert.show();
//input.OnDateChangedListener();
                    // builder.show();
                }

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Dialog myDialog1 = new Dialog(MyActivity.this);
                //myDialog.setContentView(R.layout.datelayout);
                final DatePicker input = new DatePicker(MyActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setBackgroundColor(getResources().getColor(R.color.newgreen));

                input.setMaxDate(System.currentTimeMillis());
                //input.setMinDate(System.currentTimeMillis()-2678400);
                input.setLayoutParams(lp);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                final AlertDialog alert;
                builder.setView(input).setMessage("PICK DATE").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        //  finish();
                        int day = input.getDayOfMonth();
                        int month = input.getMonth() + 1;
                        int year = input.getYear();
                        // Toast.makeText(getApplicationContext(), day + "" + month + "" + year, Toast.LENGTH_LONG).show();
                        e2.setText(day + "/" + month + "/" + year);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        //  finish();
                    }
                });

                alert = builder.create();
                if (!alert.isShowing()) {
                    alert.show();
//input.OnDateChangedListener();
                    // builder.show();
                }

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // myDialog.cancel();
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                if (isDateValid(s1) && isDateValid(s2)) {
                    if (!datecount(e1.getText().toString(), e2.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Difference Between Two Days Can't more than 30 days", Toast.LENGTH_LONG).show();
                    } else {
                        date_from = s1;
                        date_to = s2;
                        myDialog.cancel();
                        new MyTask6().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Dates Not Valid", Toast.LENGTH_LONG).show();
                }
            }
        });

        myDialog.show();


    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean datecount(String date1, String date2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String inputString1 = date1;
        String inputString2 = date2;

        try {
            Date dat1 = myFormat.parse(inputString1);
            Date dat2 = myFormat.parse(inputString2);
            long diff = dat2.getTime() - dat1.getTime();
            // Toast.makeText(getApplicationContext(), TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+"",3000).show();
            if ((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 30) && (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 0)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private class MyTask extends AsyncTask<Void, Void, String> {

        String title = "he";
        String name[] = new String[]{"Ayush", "Jain", "Java"};

        int totaa = 0;

        // ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,name);
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyActivity.this);
            mProgressDialog.setTitle("TimeTable");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
//				mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlTimeTable = "http://122.160.168.157/iSIMGC/Student/TimeTable";
                Document doc2 = Jsoup.connect(urlHome)
                        .cookie("ASP.NET_SessionId", ck)
                        .get();

                doc2 = Jsoup.connect(urlTimeTable)
                        .cookie("ASP.NET_SessionId", ck)
                        .get();

                Element eventTarget = doc2.select("input[name=__EVENTTARGET]").first();
                Element eventArgument = doc2.select("input[name=__EVENTARGUMENT]").first();
                Element vKey = doc2.select("input[name=__VSKEY]").first();

                Element viewState = doc2.select("input[name=__VIEWSTATE]").first();
                Element eventValidation = doc2.select("input[name=__EVENTVALIDATION]").first();

                Connection.Response rp = Jsoup.connect(urlTimeTable)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$Button1", "Weekly")
                        .method(Connection.Method.POST)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .followRedirects(true)
                        .timeout(100000)
                        .execute();
                doc2 = rp.parse();

                Elements imports = doc2.select("td");
                Elements rowsp = doc2.select("td[rowspan]");
                Log.v(TAG, "HElooooo");
                int rps[] = {8, 6, 6, 4, 3, 9};
                //int rpc[]={8,6,6,4,3};
                int j = 0;
                //      adapter.add("RAT");
                //    adapter.notifyDataSetChanged();
                for (Element element : rowsp) {
                    //System.out.println(element.attr("rowspan"));
                    totaa = totaa + Integer.parseInt(element.attr("rowspan"));
                    rps[j] = (Integer.parseInt(element.attr("rowspan")) * 6) + 1;
                    rpc[j] = Integer.parseInt(element.attr("rowspan"));
                    j++;
                }

                //System.out.println("Coming");
                //String day;
                int se = 1;
                int sw = 1;
                int ml = 0, hi = 0, gt = 0;
                String weekdays[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                day = new String[totaa];
                for (String string : day) {
                    day[hi] = weekdays[ml];
                    hi++;
                    gt++;
                    if (gt == rpc[ml]) {
                        if (ml != 5)
                            ml++;
                        gt = 0;
                    }
                }

                String[] sub_code = {};
                sub_name = new String[totaa];
                cl_time = new String[totaa];
                sub_fac = new String[totaa];
                block = new String[totaa];
                int i = 0;
                int i1 = 0;
                int i2 = 0;
                int i3 = 0;
                int l = 0;
                int m = 0;
                for (Element element : imports) {
                    if (se == 3) {
                        sub_name[i] = element.text();
                        System.out.print(element.text());
                        System.out.println(sw);
                        i++;
                    }
                    if (se == 6) {
                        cl_time[i1] = element.text();
                        //System.out.print(element.text());
                        //System.out.println(sw);
                        i1++;

                    }
                    if (se == 7) {
                        block[i2] = element.text();
                        //System.out.print(element.text());
                        //System.out.println(sw);
                        i2++;

                    }
                    if (se == 4) {
                        sub_fac[i3] = element.text();
                        //System.out.print(element.text());
                        //System.out.println(sw);
                        i3++;

                    }
                    /* se++;
                     if(se==8)
		      	   {
		      		   se=2;
		      	   }
		      	   sw++;
		      	   if(sw==rps[l])
		      	   {
		      		   se--;
		      		   if(l!=4)
		      			   l++;

		      		   sw=1;
		      	   }*/
                    if (se == 7) {
                        se = 1;
                    }
                    if (sw == rps[l]) {
                        sw = 0;
                        se = 0;
                        if (l != 4)
                            l++;

                    }
                    se++;
                    sw++;
                    //Sy
                }
                name = new String[]{"HArsh", "LOL"};
                System.out.println("TRY BLOCK");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Catch");
                ErrorDialog();

            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//
            //view1.setText(title);
            //Toast.makeText(getActivity(), "EDECUTED", 3000).show();
            //	ArrayAdapter<String> adapter=new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1,sub_name);
            //  String mon_nme[]=new String[totaa];
            //  int ui=0,op=0;
            try {
                mProgressDialog.dismiss();
                mAdView.bringToFront();

                Bundle arg = new Bundle();
                arg.putString("Menu", "1");
                arg.putString("User", s);
                arg.putString("Pass", p);
                arg.putString("Cook", ck);
                arg.putStringArray("Array", sub_name);
                arg.putStringArray("Array1", day);
                arg.putIntArray("Array2", rpc);
                arg.putStringArray("Array3", cl_time);
                arg.putStringArray("Array4", sub_fac);
                arg.putStringArray("Array5", block);
                //detail.setArguments(args);

                TabbedActivity tb1 = new TabbedActivity();
                tb1.setArguments(arg);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, tb1, TabbedActivity.TAG).commit();

			/*
             for (String string : day) {

			 if(divs==0)
			  {
				  if(string.equals("Monday"))
				  {
					  mon_nme[op]=sub_name[ui];
					  op++;
				  }
				//  ui++;
			  }
			  if(divs==1)
			  {
				  if(string.equals("Tuesday"))
				  {
					  mon_nme[op]=sub_name[ui];
					  op++;
				  }
				  //ui++;
			  }
			 if(divs==2)
			  {
				  if(string.equals("Wednesday"))
				  {
					  mon_nme[op]=sub_name[ui];
					  op++;
				  }
				  //ui++;
			  }
			 if(divs==3)
			  {
				  if(string.equals("Thursday"))
				  {
					  mon_nme[op]=sub_name[ui];
					  op++;
				  }
				  //ui++;
			  }
			 if(divs==4)
			  {
				  if(string.equals("Friday"))
				  {
					  mon_nme[op]=sub_name[ui];
					  op++;
				  }
				  //ui++;
			  }
			 ui++;

			 }
			 ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mon_nme);

				list.setAdapter(adapter1);
			//  list.setAdapter(new ArrayAdapter<String>(getActivity()
				//	  , android.R.layout.simple_list_item_1 , sub_name));
			  //List<String> forecastList = new ArrayList<>(Arrays.asList(sub_name));
	          //  forecastList.removeAll(Collections.singleton(null));
	            //forecastAdapter.clear();
	            //forecastAdapter.addAll(forecastList);
		  */
                mAdView.bringToFront();
            } catch (Exception e) {
                ErrorDialog();
            }
        }
    }

    private class MyTask2 extends AsyncTask<Void, Void, String> {

        String title = "he";
        String name[] = new String[]{"Ayush", "Jain", "Java"};

        int totaa = 0;

        // ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,name);
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyActivity.this);
            mProgressDialog.setTitle("Library");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
//				mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlTimeTable = "http://122.160.168.157/iSIMGC/Student/TimeTable";
                String urlLibrary = "http://122.160.168.157/iSIMGC/Library/LibIssueReturn";
                Document doc2 = Jsoup.connect(urlHome)
                        .cookie("ASP.NET_SessionId", ck)
                        .get();

                doc2 = Jsoup.connect(urlLibrary)
                        .cookie("ASP.NET_SessionId", ck)
                        .get();

                Element eventTarget = doc2.select("input[name=__EVENTTARGET]").first();
                Element eventArgument = doc2.select("input[name=__EVENTARGUMENT]").first();
                Element vKey = doc2.select("input[name=__VSKEY]").first();

                Element viewState = doc2.select("input[name=__VIEWSTATE]").first();
                Element eventValidation = doc2.select("input[name=__EVENTVALIDATION]").first();

                Connection.Response rp = Jsoup.connect(urlLibrary)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$btnIssue", "Issued")
                        .method(Connection.Method.POST)
                        .followRedirects(true).timeout(100000)
                        .execute();
                doc2 = rp.parse();

                //  Elements imports = doc2.select("td");
                //Elements rowsp=doc2.select("td[rowspan]");
                //Log.v(TAG, "HElooooo");
                Elements impo = doc2.select("table.top-heading");
                Elements count = impo.select("tr.GrdAltRow");
                Element book_issue_title_element;
                Element book_issue_date_element;
                Element book_due_date_element;
                Element book_return_date_element;

                // Elements book_issue_title_element=impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_0]");
                Element total_fine_elemnt = doc2.select("label[id=MCPH1_SCPH_lblttlfine]").first();
                total_fine = total_fine_elemnt.text();
                Element total_paid_elemnt = doc2.select("label[id=MCPH1_SCPH_lblpaid]").first();
                total_paid = total_paid_elemnt.text();
                Element total_balance_elemnt = doc2.select("label[id=MCPH1_SCPH_lblbal]").first();
                total_balance = total_balance_elemnt.text();
                Element total_waived_elemnt = doc2.select("label[id=MCPH1_SCPH_lblWaived]").first();
                total_waived = total_waived_elemnt.text();
                int count1 = 0;
                for (Element element : count) {
                    count1++;
                }
                book_issue_title = new String[count1];
                book_issue_date = new String[count1];
                book_due_date = new String[count1];
                book_return_date = new String[count1];

                for (int i = 0; i < count1; i++) {
                    book_issue_title_element = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_" + i + "]").first();
                    book_issue_date_element = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtIssueDate_" + i + "]").first();
                    book_due_date_element = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtDueDate_" + i + "]").first();
                    book_return_date_element = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtReturnDate_" + i + "]").first();

                    book_issue_title[i] = book_issue_title_element.text();
                    book_issue_date[i] = book_issue_date_element.text();
                    book_due_date[i] = book_due_date_element.text();
                    book_return_date[i] = book_return_date_element.text();

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Catch");
                ErrorDialog();

            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//
            //view1.setText(title);
            //Toast.makeText(getActivity(), "EDECUTED", 3000).show();
            //	ArrayAdapter<String> adapter=new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1,sub_name);
            //  String mon_nme[]=new String[totaa];
            //  int ui=0,op=0;
            try {
                mProgressDialog.dismiss();
                Bundle arg = new Bundle();

                arg.putString("Menu", "1");
                arg.putString("User", s);
                arg.putString("Pass", p);
                arg.putString("Cook", ck);
                arg.putString("Total_fine", total_fine);
                arg.putString("Total_due", total_balance);
                arg.putString("Total_paided", total_paid);
                arg.putString("Total_waived", total_waived);

                arg.putStringArray("Array", book_issue_title);
                arg.putStringArray("Array1", book_issue_date);

                arg.putStringArray("Array2", book_due_date);
                arg.putStringArray("Array3", book_return_date);
                //arg.putStringArray("Array5", block);
                //detail.setArguments(args);
                LibraryActivity tb1 = new LibraryActivity();
                tb1.setArguments(arg);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, tb1, LibraryActivity.TAG).commit();

            } catch (Exception e) {
                ErrorDialog();
            }
        }


    }


    private class MyTask3 extends AsyncTask<Void, Void, String> {

        String title = "";

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyActivity.this);
            mProgressDialog.setTitle("Today Attendance");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
//				mProgressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            // String username=e1.getText().toString();
            //String pass=e2.getText().toString();


            //Document doc;
            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlAttendance = "http://122.160.168.157/iSIMGC/Student/TodayAttendence";

                Connection.Response response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)

                        //.data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt","Subject Wise Attendance")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.GET).timeout(10000)
                        .execute();

                Document doc3 = response1.parse();

                Element eventTarget = doc3.select("input[name=__EVENTTARGET]").first();
                Element eventArgument = doc3.select("input[name=__EVENTARGUMENT]").first();
                Element vKey = doc3.select("input[name=__VSKEY]").first();

                Element viewState = doc3.select("input[name=__VIEWSTATE]").first();
                Element eventValidation = doc3.select("input[name=__EVENTVALIDATION]").first();

                response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")

                        .data("ctl00$ctl00$MCPH1$SCPH$btntodayAtt", "Today Attendance")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST).timeout(10000)
                        .execute();


                Document doc4 = response1.parse();
                //Elements impo=doc4.select("table.top-heading");
                Elements count = doc4.select("tr.GrdAltRow");
                Element book_issue_title_element;
                Element book_issue_date_element;
                Element book_due_date_element;
                Element book_return_date_element;

                // Elements book_issue_title_element=impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_0]");
                int count1 = 0;
                for (Element element : count) {
                    count1++;
                }
                book_issue_title = new String[count1];
                book_issue_date = new String[count1];
                book_due_date = new String[count1];
                book_return_date = new String[count1];

                for (int i = 0; i < count1; i++) {
                    book_issue_title_element = count.select("span[id=MCPH1_SCPH_gvDailyAttendence1_lblNAME_" + i + "]").first();
                    book_issue_date_element = count.select("span[id=MCPH1_SCPH_gvDailyAttendence1_lblTimeSlot_" + i + "]").first();
                    book_due_date_element = count.select("span[id=MCPH1_SCPH_gvDailyAttendence1_lblProgram_" + i + "]").first();
                    book_return_date_element = count.select("span[id=MCPH1_SCPH_gvDailyAttendence1_lblAttType_" + i + "]").first();

                    book_issue_title[i] = book_issue_title_element.text();
                    book_issue_date[i] = book_issue_date_element.text();
                    book_due_date[i] = book_due_date_element.text();
                    book_return_date[i] = book_return_date_element.text();

                }
            } catch (Exception e) {
                ErrorDialog();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//

            try {
                mProgressDialog.dismiss();
                Bundle arg = new Bundle();

                arg.putString("Menu", "1");
                arg.putString("User", s);
                arg.putString("Pass", p);
                arg.putString("Cook", ck);

                arg.putStringArray("Array", book_issue_title);
                arg.putStringArray("Array1", book_issue_date);

                arg.putStringArray("Array2", book_due_date);
                arg.putStringArray("Array3", book_return_date);
                //arg.putStringArray("Array5", block);
                //detail.setArguments(args);
                TodayAttendance tb1 = new TodayAttendance();
                tb1.setArguments(arg);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, tb1, LibraryActivity.TAG).commit();

            } catch (Exception e) {
                ErrorDialog();
            }
        }
    }

    private class MyTask4 extends AsyncTask<Void, Void, String> {

        String title = "";

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyActivity.this);
            mProgressDialog.setTitle("Subject Attendance");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
//				mProgressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            // String username=e1.getText().toString();
            //String pass=e2.getText().toString();


            //Document doc;
            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlAttendance = "http://122.160.168.157/iSIMGC/Student/TodayAttendence";

                Connection.Response response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)

                        //.data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt","Subject Wise Attendance")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.GET).timeout(0)
                        .execute();

                Document doc3 = response1.parse();

                Element eventTarget = doc3.select("input[name=__EVENTTARGET]").first();
                Element eventArgument = doc3.select("input[name=__EVENTARGUMENT]").first();
                Element vKey = doc3.select("input[name=__VSKEY]").first();

                Element viewState = doc3.select("input[name=__VIEWSTATE]").first();
                Element eventValidation = doc3.select("input[name=__EVENTVALIDATION]").first();

                response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")

                        .data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt", "Subject Wise Attendance")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST).timeout(0)
                        .execute();


                Document doc4 = response1.parse();
                Element viewState1 = doc4.select("input[name=__VIEWSTATE]").first();
                //for(Element element:viewState1)

                String encode = viewState1.attr("value");
                System.out.println("Ans=" + encode);
                String stringFromBase = new String(Base64.decode(encode, Base64.DEFAULT));//.getBytes(Charset.forName("UTF-8")).toString();
                System.out.println(stringFromBase);
                String res1[] = stringFromBase.split("progress progress");
                String res2;

                String title1, title;
                int obi = 0;
                for (String string : res1) {
                    obi++;

                }
                attendance_subject_percentdemo = new String[obi];
                attendance_subject_valuesdemo = new String[obi];
                obi = 0;
                for (String string : res1) {
                    title1 = "";
                    res2 = string.replaceAll("", "");
                    title = res2.substring(res2.length() - 25, res2.length() - 2);

                    for (int i = 0; i < title.length(); i++) {
                        if ((title.charAt(i) >= 48 && title.charAt(i) <= 57) || (title.charAt(i) == '.'))
                            title1 = title1 + title.charAt(i);
                    }
                    attendance_subject_percentdemo[obi] = title1;
                    obi++;
                }
                obi = 0;
                for (String string : res1) {
                    title1 = "";
                    res2 = string;
                    res2 = string.replaceAll("	BackColor", "");
                    res2 = res2.replaceAll("_!SB", "");

                    //res2=string.replaceAll("", "");

                    title = res2.substring(res2.length() - 95, res2.length() - 25);
                    System.out.println(title);
                    for (int i = 0; i < title.length(); i++) {
                        if ((title.charAt(i) >= 48 && title.charAt(i) <= 57) || (title.charAt(i) == 'd'))
                            title1 = title1 + title.charAt(i);
                    }
                    attendance_subject_valuesdemo[obi] = title1;
                    obi++;

                }

                //Elements impo=doc4.select("table.top-heading");
                Elements count = doc4.select("tr.GrdAltRow");
                Element attendance_subject_name_el;
                // Elements book_issue_title_element=impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_0]");
                int count1 = 0;
                for (Element element : count) {
                    count1++;
                }
                attendance_subject_name = new String[count1];
                attendance_subject_percentage = new String[count1];
                attendance_subject_values = new String[count1];

                for (int i = 0; i < count1; i++) {
                    attendance_subject_name_el = count.select("span[id=MCPH1_SCPH_GVSubject_lblSubject_" + i + "]").first();

                    attendance_subject_name[i] = attendance_subject_name_el.text();
                    attendance_subject_percentage[i] = attendance_subject_percentdemo[i];
                    attendance_subject_values[i] = attendance_subject_valuesdemo[i];
                }
            } catch (Throwable e) {
                ErrorDialog();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//

            try {
                mProgressDialog.dismiss();

                Bundle arg = new Bundle();

                arg.putString("Menu", "1");
                arg.putString("User", s);
                arg.putString("Pass", p);
                arg.putString("Cook", ck);

                arg.putStringArray("Array", attendance_subject_name);
                arg.putStringArray("Array1", attendance_subject_percentage);
                arg.putStringArray("Array2", attendance_subject_values);
                //arg.putStringArray("Array5", block);
                //detail.setArguments(args);
                SubjectWiseAttendance tb1 = new SubjectWiseAttendance();
                tb1.setArguments(arg);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, tb1, LibraryActivity.TAG).commit();
            } catch (Throwable e) {
                ErrorDialog();
            }

        }
    }

    private class MyTask5 extends AsyncTask<Void, Void, String> {

        String title = "";

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyActivity.this);
            mProgressDialog.setTitle("Today Attendance(Date)");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
//				mProgressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            // String username=e1.getText().toString();
            //String pass=e2.getText().toString();


            //Document doc;
            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlAttendance = "http://122.160.168.157/iSIMGC/Student/TodayAttendence";

                Connection.Response response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)

                        //.data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt","Subject Wise Attendance")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.GET).timeout(0)
                        .execute();

                Document doc3 = response1.parse();

                Element eventTarget = doc3.select("input[name=__EVENTTARGET]").first();
                Element eventArgument = doc3.select("input[name=__EVENTARGUMENT]").first();
                Element vKey = doc3.select("input[name=__VSKEY]").first();

                Element viewState = doc3.select("input[name=__VIEWSTATE]").first();
                Element eventValidation = doc3.select("input[name=__EVENTVALIDATION]").first();

                response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$btnDateWiseAtt", "Date Wise")

                        //.data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt","Subject Wise Attendance")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST).timeout(0)
                        .execute();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c.getTime());
//Toast.makeText(getActivity(),newDateStr,3000).show();
                //System.out.print("Date is"+newDateStr);
                title = formattedDate;
                response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$txtFrom", formattedDate)
                        .data("ctl00$ctl00$MCPH1$SCPH$txtTo", formattedDate)
                        .data("ctl00$ctl00$MCPH1$SCPH$btnShowAtt", "Show")
                        //.data("ctl00$ctl00$MCPH1$SCPH$btnDateWiseAtt","Date Wise")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST).timeout(10000)
                        .execute();


                Document doc4 = response1.parse();

                //Elements impo=doc4.select("table.top-heading");
                Elements count = doc4.select("tr.GrdAltRow");
                Element date_wise_today_date_el, date_wise_today_name_el, date_wise_today_time_el, date_wise_today_type_el, date_wise_today_status_el;
                // Elements book_issue_title_element=impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_0]");
                int count1 = 0;
                for (Element element : count) {
                    count1++;
                }
                // attendance_subject_name=new String[count1];
                //attendance_subject_percentage=new String[count1];
                date_wise_today_date = new String[count1];
                date_wise_today_name = new String[count1];
                date_wise_today_time = new String[count1];
                date_wise_today_type = new String[count1];
                date_wise_today_status = new String[count1];


                for (int i = 0; i < count1; i++) {
                    //attendance_subject_name_el=count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_"+i+"]").first();
                    date_wise_today_date_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblDate_" + i + "]").first();
                    date_wise_today_name_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_" + i + "]").first();
                    date_wise_today_time_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblTimeSlot_" + i + "]").first();
                    date_wise_today_type_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblAttType_" + i + "]").first();
                    date_wise_today_status_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblProgram_" + i + "]").first();
                    //attendance_subject_name[i]=attendance_subject_name_el.text();
                    //attendance_subject_percentage[i]=attendance_subject_percentdemo[i];
                    date_wise_today_date[i] = date_wise_today_date_el.text();
                    date_wise_today_name[i] = date_wise_today_name_el.text();
                    date_wise_today_time[i] = date_wise_today_time_el.text();
                    date_wise_today_type[i] = date_wise_today_type_el.text();
                    date_wise_today_status[i] = date_wise_today_status_el.text();

                }
            } catch (Exception e) {
                ErrorDialog();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//
            try {
                mProgressDialog.dismiss();

                Bundle arg = new Bundle();
///Toast.makeText(getApplicationContext(),title,3000).show();
                arg.putString("Menu", "1");
                arg.putString("User", s);
                arg.putString("Pass", p);
                arg.putString("Cook", ck);

                arg.putStringArray("Array", date_wise_today_date);
                arg.putStringArray("Array1", date_wise_today_name);
                arg.putStringArray("Array2", date_wise_today_time);
                arg.putStringArray("Array3", date_wise_today_type);
                arg.putStringArray("Array4", date_wise_today_status);//arg.putStringArray("Array5", block);
                //detail.setArguments(args);
                DateWiseAttendanceToday tb1 = new DateWiseAttendanceToday();
                tb1.setArguments(arg);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, tb1, LibraryActivity.TAG).commit();

            } catch (Exception e) {
                ErrorDialog();
            }
        }
    }

    private class MyTask6 extends AsyncTask<Void, Void, String> {

        String title = "";

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MyActivity.this);
            mProgressDialog.setTitle("Date Wise Attendance");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
//				mProgressDialog.show();
        }


        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            // String username=e1.getText().toString();
            //String pass=e2.getText().toString();


            //Document doc;
            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlAttendance = "http://122.160.168.157/iSIMGC/Student/TodayAttendence";

                Connection.Response response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.GET).timeout(0)
                        .execute();

                Document doc3 = response1.parse();

                Element eventTarget = doc3.select("input[name=__EVENTTARGET]").first();
                Element eventArgument = doc3.select("input[name=__EVENTARGUMENT]").first();
                Element vKey = doc3.select("input[name=__VSKEY]").first();

                Element viewState = doc3.select("input[name=__VIEWSTATE]").first();
                Element eventValidation = doc3.select("input[name=__EVENTVALIDATION]").first();

                response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$btnDateWiseAtt", "Date Wise")

                        //.data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt","Subject Wise Attendance")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST).timeout(0)
                        .execute();

                /*Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c.getTime());
//Toast.makeText(getActivity(),newDateStr,3000).show();
                //System.out.print("Date is"+newDateStr);
                title=formattedDate;
                */
                response1 = Jsoup.connect(urlAttendance)
                        .cookie("ASP.NET_SessionId", ck)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$txtFrom", date_from)
                        .data("ctl00$ctl00$MCPH1$SCPH$txtTo", date_to)
                        .data("ctl00$ctl00$MCPH1$SCPH$btnShowAtt", "Show")
                        //.data("ctl00$ctl00$MCPH1$SCPH$btnDateWiseAtt","Date Wise")
                        //.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST).timeout(10000)
                        .execute();


                Document doc4 = response1.parse();

                //Elements impo=doc4.select("table.top-heading");
                Elements count = doc4.select("tr.GrdAltRow");
                Element date_wise_today_date_el, date_wise_today_name_el, date_wise_today_time_el, date_wise_today_type_el, date_wise_today_status_el;
                // Elements book_issue_title_element=impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_0]");
                int count1 = 0;
                for (Element element : count) {
                    count1++;
                }
                // attendance_subject_name=new String[count1];
                //attendance_subject_percentage=new String[count1];
                date_wise_today_date = new String[count1];
                date_wise_today_name = new String[count1];
                date_wise_today_time = new String[count1];
                date_wise_today_type = new String[count1];
                date_wise_today_status = new String[count1];


                for (int i = 0; i < count1; i++) {
                    //attendance_subject_name_el=count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_"+i+"]").first();
                    date_wise_today_date_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblDate_" + i + "]").first();
                    date_wise_today_name_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_" + i + "]").first();
                    date_wise_today_time_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblTimeSlot_" + i + "]").first();
                    date_wise_today_type_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblAttType_" + i + "]").first();
                    date_wise_today_status_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblProgram_" + i + "]").first();
                    //attendance_subject_name[i]=attendance_subject_name_el.text();
                    //attendance_subject_percentage[i]=attendance_subject_percentdemo[i];
                    date_wise_today_date[i] = date_wise_today_date_el.text();
                    date_wise_today_name[i] = date_wise_today_name_el.text();
                    date_wise_today_time[i] = date_wise_today_time_el.text();
                    date_wise_today_type[i] = date_wise_today_type_el.text();
                    date_wise_today_status[i] = date_wise_today_status_el.text();

                }
            } catch (Exception e) {
                ErrorDialog();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//

            try {
                mProgressDialog.dismiss();

                Bundle arg = new Bundle();

                arg.putString("Menu", "1");
                arg.putString("User", s);
                arg.putString("Pass", p);
                arg.putString("Cook", ck);

                arg.putStringArray("Array", date_wise_today_date);
                arg.putStringArray("Array1", date_wise_today_name);
                arg.putStringArray("Array2", date_wise_today_time);
                arg.putStringArray("Array3", date_wise_today_type);
                arg.putStringArray("Array4", date_wise_today_status);//arg.putStringArray("Array5", block);
                //detail.setArguments(args);
                DateWiseAttendanceAll tb1 = new DateWiseAttendanceAll();
                tb1.setArguments(arg);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, tb1, LibraryActivity.TAG).commit();

            } catch (Exception e) {
                ErrorDialog();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(getApplicationContext(), "gbgn", 3000).show();
//        Intent i = new Intent(MyActivity.this, MyMainActivity.class);
//        //  overridePendingTransition(R.anim.right_slide_out, R.anim.right_slide_in);
//        overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
//
//        startActivity(i);
//        finish();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mSweetSheet.isShow()) {
            mSweetSheet.dismiss();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
            builder.setMessage("Do you wanna exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            finish();
                        }
                    })
                    .setNegativeButton("No", null);
            AlertDialog alert = builder.create();
            if (!alert.isShowing()) {
                alert.show();

                // builder.show();
            }
        }
    }

    void ErrorDialog() {
        Toast.makeText(getApplicationContext(),"Error Dialog",3000).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
        builder.setMessage("Error Connecting to The Server")
                .setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(MyActivity.this, startpage.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                args.putString("Menu", menu[0]);
                args.putString("User", s);
                args.putString("Pass", p);
                args.putString("Cook", ck);
                DetailFragment detailFragment = DetailFragment.newInstance();
                detailFragment.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, detailFragment).commit();

                //finish();

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