package com.ayush.gsim.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayush.gsim.Activities.NewsFeedActivity;
import com.ayush.gsim.AsyncTasks.Attendance;
import com.ayush.gsim.HowItDone.HowItsDoneActivity;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//import android.app.Fragment;

public class AttendanceFragment_Database extends Fragment {
    TextView text;
    public static TextView view1;
    ImageView img;
    public static ImageView ivm;
    public static ListView list;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer = false;
    public static ShapeDrawable footerBackground;
    private boolean mFromSavedInstanceState = false;
    public static final String TAG = AttendanceFragment_Database.class.getSimpleName();
    SQLiteDatabase db;
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private String userName, passWord, sessionCookie;
    String presnt[];
    String absent[];
    String total[], total_string;
    public static TextView detailsItemActivityText;
    public static ImageButton refreshButton;
    public static WebView brower;
    TextView attendanceText;
    public static LinearLayout newsfeedLayout;
    public static CardView attendanceTextCardView;
    private FragmentManager fragmentManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;

    public static AttendanceFragment_Database newInstance() {
        return new AttendanceFragment_Database();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        userName = getArguments().getString(Constants.USERNAME_INTENT);
        passWord = getArguments().getString(Constants.PASSWORD_INTENT);
        sessionCookie = getArguments().getString(Constants.COOKIE_INTENT);

        View view;
        view = inflater.inflate(R.layout.attendancetotal, container, false);
        ivm = (ImageView) view.findViewById(R.id.imageviewC);
        refreshButton = (ImageButton) view.findViewById(R.id.imageButton3);
        brower = (WebView) view.findViewById(R.id.browser);
        brower.loadUrl(Constants.ADVERTISE_URL);
        detailsItemActivityText = (TextView) view.findViewById(R.id.details);
        footerBackground = new ShapeDrawable();
        newsfeedLayout = (LinearLayout) view.findViewById(R.id.newsInAttendance);
        newsfeedLayout.setVisibility(View.INVISIBLE);
        attendanceText = (TextView) view.findViewById(R.id.loginText);
        attendanceText.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), Constants.FONT_ROBOTO_MEDIUM));
        attendanceTextCardView = (CardView) view.findViewById(R.id.card_view_login_text);
        detailsItemActivityText.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), Constants.FONT_ROBOTO_THIN));
        detailsItemActivityText.setText(Html.fromHtml("<u><b>Click Here to know how it is done.</u></b>"));
        detailsItemActivityText.setVisibility(View.GONE);
        detailsItemActivityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HowItsDoneActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

            }
        });
        latestNewsOnFeed();
        float[] radii = new float[8];
        radii[0] = 16;
        radii[1] = 16;
        radii[2] = 16;
        radii[3] = 16;
        footerBackground.setShape(new OvalShape());

        // String menu = getArguments().getString("Menu");
        //text= (TextView) view.findViewById(R.id.detail);
        //text.setText(menu);
        view1 = (TextView) view.findViewById(R.id.textView1);
        view1.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), Constants.FONT_ROBOTO_THIN));
        list = (ListView) view.findViewById(R.id.listView1);
        refreshButton.setBackgroundColor(Color.TRANSPARENT);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFunction();
            }
        });

        Attendance attandence = new Attendance(getActivity(), userName, passWord,sessionCookie, null, 0);
        attandence.databaseFunction();

        return view;

    }

    ArrayList<String> databaseNewEntrySubjectName;
    ArrayList<String> databaseNewEntryAttendanceStatus;

    public ProgressDialog mProgressDialog;

    //
//    private class MyTask extends AsyncTask<Void, Void, String> {
//
//        String title = "";
//        String checkSession = "";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mProgressDialog = new ProgressDialog(getActivity());
//            mProgressDialog.setTitle("Attendance");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mProgressDialog.setMax(100);
//            mProgressDialog.setProgress(0);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();
////
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            Document doc;
//
//            // String username=e1.getText().toString();
//            //String pass=e2.getText().toString();
//
//            databaseNewEntrySubjectName = new ArrayList<String>();
//            databaseNewEntryAttendanceStatus = new ArrayList<String>();
//            //Document doc;
//            try {
//                String urlLogin = "http://122.160.168.157/isimgc/login";
//                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
//                String urlHome = "http://122.160.168.157/isimgc/home";
//                String urlAttendance = "http://122.160.168.157/iSIMGC/Student/TodayAttendence";
//
//                Connection.Response response1 = Jsoup.connect(urlAttendance)
//                        .cookie("ASP.NET_SessionId", cokie)
//
//                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
//                        .method(Connection.Method.GET).timeout(0)
//                        .execute();
//
//                Document document = response1.parse();
//                checkSession = document.title();
//
//                Element eventTarget = document.select("input[name=__EVENTTARGET]").first();
//                Element eventArgument = document.select("input[name=__EVENTARGUMENT]").first();
//                Element vKey = document.select("input[name=__VSKEY]").first();
//
//                Element viewState = document.select("input[name=__VIEWSTATE]").first();
//                Element eventValidation = document.select("input[name=__EVENTVALIDATION]").first();
//
//                response1 = Jsoup.connect(urlAttendance)
//                        .cookie("ASP.NET_SessionId", cokie)
//                        .data("__EVENTTARGET", "")
//                        .data("__EVENTARGUMENT", "")
//                        .data("__VSKEY", vKey.attr("value") + "")
//                        .data("__VIEWSTATE", "")
//                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
//                        .data("ctl00$ctl00$MCPH1$SCPH$btnDateWiseAtt", "Date Wise")
//                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
//                        .method(Connection.Method.POST)
//                        .execute();
//
//
//                document = response1.parse();
//
//
//                //System.out.println(doc.html());
//                //for(Element element:viewState1)
//                String[] stringMon = mondays.toArray(new String[0]);
//                String[] stringSat = saturdays.toArray(new String[0]);
//
//                for (int i = 0; i < stringMon.length; i++) {
//                    response1 = Jsoup
//                            .connect(urlAttendance)
//                            .cookie("ASP.NET_SessionId", cokie)
//                            .data("__EVENTTARGET", "")
//                            .data("__EVENTARGUMENT", "")
//                            .data("__VSKEY", vKey.attr("value") + "")
//                            .data("__VIEWSTATE", "")
//                            .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
//                            .data("ctl00$ctl00$MCPH1$SCPH$txtFrom", stringMon[i])
//                            .data("ctl00$ctl00$MCPH1$SCPH$txtTo", stringSat[i])
//                            .data("ctl00$ctl00$MCPH1$SCPH$btnShowAtt", "Show")
//                            // .data("ctl00$ctl00$MCPH1$SCPH$btnDateWiseAtt","Date Wise")
//                            // .data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
//                            .userAgent(
//                                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
//                            .method(Connection.Method.POST).timeout(0).execute();
//
//                    System.out.println(stringMon[i]);
//                    System.out.println(stringSat[i]);
//                    System.out.println(datecount(stringMon[i], current));
//                    mProgressDialog.setProgress(100 - getDateProgress(stringMon[i], stringMon.length));
//                    document = response1.parse();
//                    checkSession = document.title();
//
//                    // System.out.println("Att" + doc2.html());
//                    System.out.println("Att" + document.title());
//                    Elements count = document.select("tr.GrdAltRow");
//                    Element date_wise_today_date_el, date_wise_today_name_el, date_wise_today_time_el, date_wise_today_type_el, date_wise_today_status_el;
//                    // Elements
//                    // book_issue_title_element=impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_0]");
//                    int count1 = 0;
//                    for (Element element : count) {
//                        count1++;
//                    }
//                    // attendance_subject_name=new String[count1];
//                    // attendance_subject_percentage=new String[count1];
//
//                    for (int i1 = 0; i1 < count1; i1++) {
//                        // attendance_subject_name_el=count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_"+i+"]").first();
//                        date_wise_today_date_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblDate_" + i1 + "]").first();
//                        date_wise_today_name_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_" + i1 + "]").first();
//                        date_wise_today_time_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblTimeSlot_" + i1 + "]")
//                                .first();
//                        date_wise_today_type_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblAttType_" + i1 + "]")
//                                .first();
//                        date_wise_today_status_el = count.select("span[id=MCPH1_SCPH_gvDateWise_lblProgram_" + i1 + "]")
//                                .first();
//                        // attendance_subject_name[i]=attendance_subject_name_el.text();
//                        // attendance_subject_percentage[i]=attendance_subject_percentdemo[i];
//                        subjectName.add(date_wise_today_name_el.text());
//                        attendanceStatus.add(date_wise_today_status_el.text());
//                        if (i != stringMon.length - 1) {
//                            databaseNewEntrySubjectName.add(date_wise_today_name_el.text());
//                            databaseNewEntryAttendanceStatus.add(date_wise_today_status_el.text());
//                            //System.out.println(date_wise_today_name_el.text());
//                        }
//                        // date_wise_today_name[i] = date_wise_today_name_el.text();
//                        // date_wise_today_time[i] = date_wise_today_time_el.text();
//                        // date_wise_today_type[i] = date_wise_today_type_el.text();
//                        // date_wise_today_status[i] =
//                        // date_wise_today_status_el.text();
//
//                    }
//
//                }
//
//
//            } catch (Throwable e) {
//                //ErrorDialog();
//
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        ErrorDialog();
//                        mProgressDialog.dismiss();
//                    }
//                });
//            }
//            return title;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {//
//
//            try {
//                mProgressDialog.dismiss();
//
//                if (checkSession.equals("SIM Login")) {
//                    Toast.makeText(getActivity(), "Session Expired", Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(i);
//                    //overridePendingTransition(R.anim.right_slide_out, R.anim.right_slide_in);
//                    getActivity().overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
//
//                    getActivity().finish();
//
//                }
//            } catch (Exception e) {
//
//            }
//            detailsItemActivityText.setVisibility(View.VISIBLE);
//            String[] arraySubjectName = subjectName.toArray(new String[0]);
//            String[] arrayAttendanceStatus = attendanceStatus.toArray(new String[0]);
//
//            int string_count = title.length();
//            String title1 = "", title2 = "";
//            int count_att = 0;
//            for (String d : arrayAttendanceStatus) {
//                if (d.equals("P")) {
//                    count_att++;
//                }
//            }
//            title1 = ((float) count_att / arrayAttendanceStatus.length) * 100 + "";
//            int color;
//            try {
//                if (Float.parseFloat(title1) >= 90) {
//                    color = Color.parseColor("#00BFA5");
//                } else if (Float.parseFloat(title1) >= 75) {
//                    color = Color.parseColor("#AED581");
//                } else if (Float.parseFloat(title1) >= 60) {
//                    color = Color.parseColor("#00B8D4");
//                } else if (Float.parseFloat(title1) >= 45) {
//                    color = Color.parseColor("#F48FB1");
//                } else {
//                    color = Color.parseColor("#F44336");
//                }
//
//                footerBackground.getPaint().setColor(color);
//
//                ivm.setBackground(footerBackground);
////
////                for (int i = 0; i < total_string.length(); i++) {
////                    if ((total_string.charAt(i) >= 48 && total_string.charAt(i) <= 57) || (total_string.charAt(i) == 'd'))
////                        title2 = title2 + total_string.charAt(i);
////                }
////
////                String atten_demo[] = title2.split("d");
////                int i = 0;
////
////
////                String d[] = new String[3];
////                int j = 0;
////                for (String str : atten_demo) {
////                    if (!(str.equals(""))) {
////                        d[j] = str;
////                        //		  System.out.print("a"+str);
////                        j++;
////                    }
////                }
//                presnt = new String[1];
//                absent = new String[1];
//                total = new String[1];
//                presnt[0] = count_att + "";
//                absent[0] = arrayAttendanceStatus.length - count_att + "";
//                total[0] = arrayAttendanceStatus.length + "";
//                entryToDatabase();
//                String formatpercentage = String.format("%.2f", Float.parseFloat(title1));
//                view1.setText(formatpercentage + "%");
//                view1.setGravity(Gravity.CENTER);
//                // db.close();
//            } catch (Exception e) {
//                presnt[0] = "--";
//                absent[0] = "--";
//                total[0] = "--";
//                e.printStackTrace();
//
//                view1.setText("--");
//                view1.setGravity(Gravity.CENTER);
//
//
//            }
//            ImageCustomAttendanceTotal ic = new ImageCustomAttendanceTotal(getActivity(), presnt, absent, total);
////			  //list.setAdapter(ic);
//
//            list.setAdapter(ic);
//            refreshButton.setVisibility(View.VISIBLE);
//            refreshButton.setBackgroundColor(Color.TRANSPARENT);
//            brower.setVisibility(View.VISIBLE);
//            newsfeedLayout.setVisibility(View.VISIBLE);
//            attendanceTextCardView.setVisibility(View.VISIBLE);
//            // refreshButton.setColorFilter(getActivity().getResources().getColor(R.color.primaryColor));
//
//            refreshButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    refreshFunction();
////                    getSupportFragmentManager()
////                            .beginTransaction()
////                            .replace(R.id.content_frame, attendaceFragment).commit();
//                }
//            });
//
////			  Toast.makeText(getActivity().getApplicationContext(),presnt[0]+"/"+absent[0]+"/"+total[0],3000).show();
//            //Toast.makeText(getActivity(), "EDECUTED", 3000).show();
//
//        }
//    }
//
    private void refreshFunction() {
        AttendanceFragment_Database attendaceFragment = AttendanceFragment_Database.newInstance();

        Bundle args = new Bundle();
        args.putString(Constants.USERNAME_INTENT, userName);
        args.putString(Constants.PASSWORD_INTENT, passWord);
        args.putString(Constants.COOKIE_INTENT, sessionCookie);
        deleteData(getActivity());
        attendaceFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, attendaceFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    public static void deleteData(Activity activity) {
        Context context = activity.getApplicationContext();
        context.deleteDatabase(Constants.ATTENDANCE_DATABASE);
        SharedPreferences sp3 = activity.getSharedPreferences(Constants.SHARED_PREF_ATTENDANCE_TABLE, 0);
        SharedPreferences.Editor ed3 = sp3.edit();
        ed3.clear();
        ed3.commit();

    }

    //
//    String startDate_Monday;
//    String startDate_Saturday;
//    String current;
//    ArrayList<String> subjectName = new ArrayList<String>();
//    ArrayList<String> attendanceStatus = new ArrayList<String>();
//    ArrayList<String> mondays = new ArrayList<String>();
//    ArrayList<String> saturdays = new ArrayList<String>();
//
//    void entryToDatabase() {
//        String[] data1 = databaseNewEntrySubjectName.toArray(new String[0]);
//        String[] data2 = databaseNewEntryAttendanceStatus.toArray(new String[0]);
//        for (int j = 0; j < data1.length; j++) {
//            db.execSQL("insert into '" + "s" + username + "' values('" + data1[j] + "','" + data2[j] + "')");
//        }
//        SharedPreferences sp3 = getActivity().getSharedPreferences("AttendanceCount", 0);
//        SharedPreferences.Editor ed3 = sp3.edit();
//
//        ed3.putString(username, getLastSaturday(current));
//        ed3.commit();
//        db.close();
//
//    }
//
//    void databaseFunction() {
//        db = getActivity().openOrCreateDatabase("DemoDatabase1", getActivity().MODE_APPEND, null);
//        db.execSQL("create table if not exists " + "s" + username + "( subject varchar , status varchar ) ");
//        SharedPreferences sp = getActivity().getSharedPreferences("AttendanceCount", 0);
//        String lastupdated = (sp.getString(username, ""));
//        System.out.println(lastupdated);
//        Calendar calendar = Calendar.getInstance();
//        System.out.println("Current time => " + calendar.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        current = df.format(calendar.getTime());
//        if (lastupdated.equals("")) {
//            SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("Dates", 0);
//            startDate_Monday = sharedPreferences1.getString("Monday", "");
//            startDate_Saturday = sharedPreferences1.getString("Saturday", "");
//
//        } else {
//            startDate_Monday = getNextMonday(lastupdated);
//            startDate_Saturday = getNextSaturday(lastupdated);
//
//
//            String q = "select*from " + "s" + username;
//            Cursor c = db.rawQuery(q, null);
//
//            while (c.moveToNext()) {
//                subjectName.add(c.getString(0));
//                attendanceStatus.add(c.getString(1));
//            }
//
//        }
//        setAllArray();
//
//    }
//
//    public String getNextMonday(String date1) {
//        String[] splitString = date1.split("/");
//        GregorianCalendar d = new GregorianCalendar(Integer.parseInt(splitString[2]), Integer.parseInt(splitString[1]) - 1, Integer.parseInt(splitString[0]));//(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
//
//        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
//            d.add(Calendar.DATE, 1);
//        System.out.println("now " + formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));
//        return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
//    }
//
//    public String getNextSaturday(String date1) {
//        String[] splitString = date1.split("/");
//        GregorianCalendar d = new GregorianCalendar(Integer.parseInt(splitString[2]), Integer.parseInt(splitString[1]) - 1, Integer.parseInt(splitString[0]));//(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
//        d.add(Calendar.DATE, 1);
//        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
//            d.add(Calendar.DATE, 1);
//        System.out.println("now " + formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));
//        return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
//    }
//
//    public String formatDate(int d, int m, int year) {
//
//        String format = String.format("%02d/%02d/%d", d, m + 1, year);
//        return format;
//
//    }
//
//    void setAllArray() {
//        String[] splitString1 = startDate_Monday.split("/");
//        String[] splitString2 = startDate_Saturday.split("/");
//        String[] splitString3 = current.split("/");
//
//        GregorianCalendar mon_start = new GregorianCalendar(Integer.parseInt(splitString1[2]), Integer.parseInt(splitString1[1]) - 1, Integer.parseInt(splitString1[0]));
//        GregorianCalendar sat_start = new GregorianCalendar(Integer.parseInt(splitString2[2]), Integer.parseInt(splitString2[1]) - 1, Integer.parseInt(splitString2[0]));//(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
//        GregorianCalendar current = new GregorianCalendar(Integer.parseInt(splitString3[2]), Integer.parseInt(splitString3[1]) - 1, Integer.parseInt(splitString3[0]));
//        getmondays(mon_start, getWeekAnalysis(current), sat_start);
//        lastWeek(current);
//    }
//
//    void getmondays(GregorianCalendar s, GregorianCalendar e, GregorianCalendar t) {
//
//        GregorianCalendar mon = s;
//        GregorianCalendar current = e;
//        GregorianCalendar sat = t;
//
////		List<LocalDate> totalDates_Mondays = new ArrayList();
//        while (!sat.after(current)) {
//            //	totalDates_Mondays.add(sat);
//            saturdays.add(formatDate(sat.get(Calendar.DATE), sat.get(Calendar.MONTH), sat.get(Calendar.YEAR)));
//            sat.add(Calendar.WEEK_OF_YEAR, 1);
//        }
//        sat.add(Calendar.WEEK_OF_YEAR, -1);
//        while (!mon.after(current) && !mon.after(sat)) {
//            mondays.add(formatDate(mon.get(Calendar.DATE), mon.get(Calendar.MONTH), mon.get(Calendar.YEAR)));
//            mon.add(Calendar.WEEK_OF_YEAR, 1);
//        }
////
////		return (ArrayList<LocalDate>) totalDates_Mondays;
//    }
//
//    GregorianCalendar getWeekAnalysis(GregorianCalendar date) {
//
//        GregorianCalendar d = date;
//        System.out.println(d.get(Calendar.DAY_OF_WEEK));
//
//        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SATURDAY))) {
//
//            d.add(Calendar.DAY_OF_MONTH, -1);
//            //	System.out.println("Sat" + formatDate(d.get(Calendar.DATE),d.get(Calendar.MONTH),d.get(Calendar.YEAR)));
//            return d;
//        }
//        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SUNDAY))) {
//
//            d.add(Calendar.DAY_OF_MONTH, -2);
//            return d;
//        }
//        return d;
//
//
//    }
//
//    String getLastSaturday(String date) {
//        String[] splitString1 = date.split("/");
//
//        GregorianCalendar d = new GregorianCalendar(Integer.parseInt(splitString1[2]), Integer.parseInt(splitString1[1]) - 1, Integer.parseInt(splitString1[0]));
//        System.out.println(d.get(Calendar.DAY_OF_WEEK));
//
//        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SATURDAY))) {
//
//            d.add(Calendar.DAY_OF_MONTH, -7);
//            //	System.out.println("Sat" + formatDate(d.get(Calendar.DATE),d.get(Calendar.MONTH),d.get(Calendar.YEAR)));
//            return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
//        }
//        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SUNDAY))) {
//
//            d.add(Calendar.DAY_OF_MONTH, -8);
//            return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
//        }
//
//
//        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
//            d.add(Calendar.DATE, -1);
//        System.out.println("now " + formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));
//
//        return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
//    }
//
//
//    void lastWeek(GregorianCalendar date) {
//        GregorianCalendar d = new GregorianCalendar();//(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
////	d.set((Calendar.YEAR), date.get(Calendar.YEAR));
////	d.set((Calendar.MONTH), date.get(Calendar.MONTH));
////	d.set((Calendar.DATE), date.get(Calendar.DATE));
//        d = (GregorianCalendar
//                ) date.clone();
//        saturdays.add(formatDate(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR)));
//        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
//            d.add(Calendar.DATE, -1);
//        mondays.add(formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));
//        //System.out.println("Final " + formatDate(date.get(Calendar.DATE),date.get(Calendar.MONTH),date.get(Calendar.YEAR)));
//    }
//
//    void ErrorDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Error Connecting to Server")
//                .setCancelable(false).setNegativeButton("Home", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////				Intent i = new Intent(getActivity(), LauncherActivity.class);
////				startActivity(i);
////				getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
//                Bundle args = new Bundle();
//                args.putString("Menu", "Name");
//                args.putString("User", username);
//                args.putString("Pass", pass);
//                args.putString("Cook", cokie);
//                DetailFragment detailFragment = DetailFragment.newInstance();
//                detailFragment.setArguments(args);
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content_frame, detailFragment).commit();
//
//                //getActivity().finish();
//
//            }
//        })
//                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Bundle args = new Bundle();
//                        args.putString("Menu", "Name");
//                        args.putString("User", username);
//                        args.putString("Pass", pass);
//                        args.putString("Cook", cokie);
//                        AttendanceFragment_Database attendanceFragment_database = AttendanceFragment_Database.newInstance();
//
//                        attendanceFragment_database.setArguments(args);
//                        getActivity().getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.content_frame, attendanceFragment_database).commit();
//
//
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }
//
//    public int datecount(String date1, String date2) {
//        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
//        String inputString1 = date1;
//        String inputString2 = date2;
//
//        try {
//            Date dat1 = myFormat.parse(inputString1);
//            Date dat2 = myFormat.parse(inputString2);
//            long diff = dat2.getTime() - dat1.getTime();
//            // Toast.makeText(getApplicationContext(), TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+"",3000).show();
//            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return 0;
//        }
//
//    }
//
//    int getDateProgress(String mondayDate, int length) {
//        int a = (Math.abs(100 * datecount(mondayDate, current) / (length * 10)));
//        System.out.println(a);
//        a = randomWithRange(a - 5, a + 5);
//        System.out.println(a);
//        return a;
//    }
//
//    int randomWithRange(int min, int max) {
//        int range = (max - min) + 1;
//        return (int) (Math.random() * range) + min;
//    }
//
    private void latestNewsOnFeed() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.NEWS_FEED);


        databaseReferenceGsim.child(FirebaseConstants.LATEST_NEWS_FEED)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        latestNewsOnFeedLayout(snapshot.child("message").getValue().toString(),
                                snapshot.child("author").getValue().toString(),
                                snapshot.child("authorUrl").getValue().toString(),
                                snapshot.child("url").getValue().toString(),
                                snapshot.child("phone").getValue().toString(),
                                snapshot.child("imageUrl").getValue().toString()
                        );
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        LogWrapper.out(getActivity().getApplicationContext(), databaseError.getMessage());
                    }
                });
    }


//        Firebase myFirebaseRef = new Firebase("https://news-gcet.firebaseio.com");
//
//        myFirebaseRef.child("LatestNews").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                latestNewsOnFeedLayout(snapshot.child("message").getValue().toString(),
//                        snapshot.child("author").getValue().toString(),
//                        snapshot.child("authorUrl").getValue().toString(),
//                        snapshot.child("url").getValue().toString(),
//                        snapshot.child("phone").getValue().toString(),
//                        snapshot.child("imageUrl").getValue().toString()
//
//                );
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {
//            }
//        });


    //
    private void latestNewsOnFeedLayout(String message, String author, final String authorUrl, final String url, final String phone, final String imageUrl) {
        {

            TextView authorText = (TextView) newsfeedLayout.findViewById(R.id.textView);
            TextView clickText = (TextView) newsfeedLayout.findViewById(R.id.clickText);
            clickText.setVisibility(View.VISIBLE);
            clickText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), NewsFeedActivity.class);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);


                }
            });
            authorText.setText(Html.fromHtml(author) + ": ");
            authorText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = getOpenFacebookIntent(authorUrl);
                    getActivity().startActivity(browserIntent);

                }
            });
            // If the message was sent by this user, color it differently
            clickText.setTextColor(getActivity().getResources().getColor(R.color.primaryColor));
            clickText.setText(Html.fromHtml("<u>Visit News Feed>>></u>"));
            authorText.setTextColor(getActivity().getResources().getColor(R.color.primaryColor));
            //   Url = chat.getAuthor().toString();

            TextView messageText = ((TextView) newsfeedLayout.findViewById(R.id.textView2));
            messageText.setTypeface(Typeface
                    .createFromAsset(getActivity().getAssets(), Constants.FONT_ROBOTO_THIN));
            try {
                messageText.setText(Html.fromHtml(checkFacebookIntent(message)));
                messageText.setMovementMethod(LinkMovementMethod.getInstance());
            } catch (Exception e) {

            }

            ImageButton imageButton1 = (ImageButton) newsfeedLayout.findViewById(R.id.imageButton);
            ImageButton imageButton2 = (ImageButton) newsfeedLayout.findViewById(R.id.imageButton2);
            imageButton1.setBackgroundColor(Color.TRANSPARENT);
            imageButton2.setBackgroundColor(Color.TRANSPARENT);
            imageButton1.setVisibility(View.VISIBLE);
            imageButton2.setVisibility(View.VISIBLE);
            imageButton1.setEnabled(true);
            imageButton2.setEnabled(true);
            imageButton1.setImageResource(R.drawable.web);
            imageButton1.setColorFilter(getActivity().getResources().getColor(R.color.primaryColor));
            imageButton2.setImageResource(R.drawable.telephone);
            imageButton2.setColorFilter(newsfeedLayout.getResources().getColor(R.color.primaryColor));
            if (url.equals("null")) {
                imageButton1.setEnabled(false);
                imageButton1.setVisibility(View.GONE);

            }
            if (phone.equals("null")) {
                imageButton2.setEnabled(false);
                imageButton2.setVisibility(View.GONE);

            }


            LogWrapper.out(getActivity().getApplicationContext(), url + phone);

            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent browserIntent = getOpenFacebookIntent(url);
                    getActivity().startActivity(browserIntent);

                }
            });

            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    getActivity().startActivity(intent);
                }
            });


//        mShortAnimationDuration = activity.getResources().getInteger(
//                android.R.integer.config_shortAnimTime);
            WebView urlImageView = (WebView) newsfeedLayout.findViewById(R.id.webImageView);
            final Bitmap decodedByte;
            if (!imageUrl.equals("1")) {

                try {
                    System.out.println("imageUrl" + imageUrl);
                    urlImageView.loadUrl(imageUrl);
                    //urlImageView.loadData("<img src="+urlImageView+" width=100%/>","text/html","UTF-8");
                    urlImageView.setVisibility(View.VISIBLE);
                    ((RelativeLayout) newsfeedLayout.findViewById(R.id.relativeLayout)).setVisibility(View.VISIBLE);

//                ImageView expandedImageView = (ImageView) view.findViewById(
//                        R.id.zoomimageView1);
//                expandedImageView.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    LogWrapper.printStackTrace(getActivity().getApplicationContext(), e);
                }

            }

        }
    }

    public Intent getOpenFacebookIntent(String url) {

        try {

            getActivity().getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + url));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
    }

    public String checkFacebookIntent(String url) {

        try {

            getActivity().getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            System.out.print(url);
            return url;//new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        } catch (Exception e) {
            return url.replace("fb://facewebmodal/f?href=", "");//new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
    }

}