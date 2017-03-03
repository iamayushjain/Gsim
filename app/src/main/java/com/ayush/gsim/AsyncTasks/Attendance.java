package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;

import com.ayush.gsim.Activities.MyActivity;
import com.ayush.gsim.Fragments.AttendanceFragment_Database;
import com.ayush.gsim.Fragments.LibraryFragment;
import com.ayush.gsim.Fragments.SubjectWiseAttendanceExactFragment;
import com.ayush.gsim.ImageCustoms.ImageCustomAttendanceTotal;
import com.ayush.gsim.R;
import com.ayush.gsim.Ranking.RankingUtils;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by ayush on 29/1/17.
 */

public class Attendance {

    private Activity activity;
    private String userName, passWord, sessionCookie, studentName;
    private ProgressDialog mProgressDialog;
    private FragmentManager fragmentManager;

    private SQLiteDatabase db;
    private ArrayList<String> databaseNewEntrySubjectName;
    private ArrayList<String> databaseNewEntryAttendanceStatus;
    private String startDateMonday;
    private String startDateSaturday;
    private String current;
    private ArrayList<String> subjectName = new ArrayList<String>();
    private ArrayList<String> attendanceStatus = new ArrayList<String>();
    private ArrayList<String> mondays = new ArrayList<String>();
    private ArrayList<String> saturdays = new ArrayList<String>();
    private int flag;
    private String present[];
    private String absent[];
    private String total[];
    private boolean isSuccessFul;

    public Attendance(Activity activity, String userName, String passWord, String cookie, FragmentManager fragmentManager, int flag) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.sessionCookie = cookie;
        this.fragmentManager = fragmentManager;
        this.flag = flag;
    }

    public void databaseFunction() {
        db = activity.openOrCreateDatabase(Constants.ATTENDANCE_DATABASE, activity.MODE_APPEND, null);

        db.execSQL("create table if not exists " + Constants.ATTENDANCE_DATABASE_TABLE_NAME + userName + "( subject varchar , status varchar ) ");

        SharedPreferences sp = activity.getSharedPreferences(Constants.SHARED_PREF_ATTENDANCE_TABLE, 0);
        String lastUpdated = (sp.getString(userName, ""));
        LogWrapper.out(activity.getApplicationContext(), lastUpdated);
        Calendar calendar = Calendar.getInstance();
        LogWrapper.out(activity.getApplicationContext(), "Current time => " + calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        current = df.format(calendar.getTime());
        if (lastUpdated.equals("")) {
            startDateMonday = sp.getString(Constants.SHARED_PREF_ATTENDANCE_MONDAY, Constants.ATTENDANCE_SEMESTER_STARTING_MONDAY);
            startDateSaturday = sp.getString(Constants.SHARED_PREF_ATTENDANCE_SATURADAY, Constants.ATTENDANCE_SEMESTER_STARTING_SATURDAY);
        } else {
            startDateMonday = getNextMonday(lastUpdated);
            startDateSaturday = getNextSaturday(lastUpdated);

            String query = "select*from " + Constants.ATTENDANCE_DATABASE_TABLE_NAME + userName;
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                subjectName.add(cursor.getString(0));
                attendanceStatus.add(cursor.getString(1));
            }
            cursor.close();
//            db.close();
        }
        setAllArray();

        new SubjectExactAttendanceNew().execute();
    }

    private String getNextMonday(String date1) {
        String[] splitString = date1.split("/");
        GregorianCalendar d = new GregorianCalendar(Integer.parseInt(splitString[2]), Integer.parseInt(splitString[1]) - 1, Integer.parseInt(splitString[0]));//(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));

        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            d.add(Calendar.DATE, 1);
        LogWrapper.out(activity.getApplicationContext(), "now " + formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));
        return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
    }

    private String getNextSaturday(String date1) {
        String[] splitString = date1.split("/");
        GregorianCalendar d = new GregorianCalendar(Integer.parseInt(splitString[2]), Integer.parseInt(splitString[1]) - 1, Integer.parseInt(splitString[0]));//(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
        d.add(Calendar.DATE, 1);
        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
            d.add(Calendar.DATE, 1);
        LogWrapper.out(activity.getApplicationContext(), "now " + formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));
        return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
    }

    private String formatDate(int d, int m, int year) {

        String format = String.format("%02d/%02d/%d", d, m + 1, year);
        return format;

    }

    private void setAllArray() {
        String[] splitString1 = startDateMonday.split("/");
        String[] splitString2 = startDateSaturday.split("/");
        String[] splitString3 = current.split("/");

        GregorianCalendar mon_start = new GregorianCalendar(Integer.parseInt(splitString1[2]), Integer.parseInt(splitString1[1]) - 1, Integer.parseInt(splitString1[0]));
        GregorianCalendar sat_start = new GregorianCalendar(Integer.parseInt(splitString2[2]), Integer.parseInt(splitString2[1]) - 1, Integer.parseInt(splitString2[0]));//(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
        GregorianCalendar current = new GregorianCalendar(Integer.parseInt(splitString3[2]), Integer.parseInt(splitString3[1]) - 1, Integer.parseInt(splitString3[0]));
        getMondays(mon_start, getWeekAnalysis(current), sat_start);
        lastWeek(current);
    }

    private void getMondays(GregorianCalendar s, GregorianCalendar e, GregorianCalendar t) {

        GregorianCalendar mon = s;
        GregorianCalendar current = e;
        GregorianCalendar sat = t;
        while (!sat.after(current)) {
            saturdays.add(formatDate(sat.get(Calendar.DATE), sat.get(Calendar.MONTH), sat.get(Calendar.YEAR)));
            sat.add(Calendar.WEEK_OF_YEAR, 1);
        }
        sat.add(Calendar.WEEK_OF_YEAR, -1);
        while (!mon.after(current) && !mon.after(sat)) {
            mondays.add(formatDate(mon.get(Calendar.DATE), mon.get(Calendar.MONTH), mon.get(Calendar.YEAR)));
            mon.add(Calendar.WEEK_OF_YEAR, 1);
        }
    }

    private GregorianCalendar getWeekAnalysis(GregorianCalendar date) {

        GregorianCalendar d = date;
        LogWrapper.out(activity.getApplicationContext(), d.get(Calendar.DAY_OF_WEEK));

        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SATURDAY))) {
            d.add(Calendar.DAY_OF_MONTH, -1);
            return d;
        }
        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SUNDAY))) {
            d.add(Calendar.DAY_OF_MONTH, -2);
            return d;
        }
        return d;


    }

    private String getLastSaturday(String date) {
        String[] splitString1 = date.split("/");

        GregorianCalendar d = new GregorianCalendar(Integer.parseInt(splitString1[2]), Integer.parseInt(splitString1[1]) - 1, Integer.parseInt(splitString1[0]));
        LogWrapper.out(activity.getApplicationContext(), d.get(Calendar.DAY_OF_WEEK));

        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SATURDAY))) {

            d.add(Calendar.DAY_OF_MONTH, -7);
            return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
        }
        if ((d.get(Calendar.DAY_OF_WEEK) == (Calendar.SUNDAY))) {

            d.add(Calendar.DAY_OF_MONTH, -8);
            return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
        }


        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
            d.add(Calendar.DATE, -1);
        LogWrapper.out(activity.getApplicationContext(), "now " + formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));

        return formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR));
    }

    private void lastWeek(GregorianCalendar date) {
        GregorianCalendar d = (GregorianCalendar
                ) date.clone();
        saturdays.add(formatDate(date.get(Calendar.DATE), date.get(Calendar.MONTH), date.get(Calendar.YEAR)));
        while (d.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            d.add(Calendar.DATE, -1);
        mondays.add(formatDate(d.get(Calendar.DATE), d.get(Calendar.MONTH), d.get(Calendar.YEAR)));
    }

    private class SubjectExactAttendanceNew extends AsyncTask<Void, Void, String> {

        String title = "";

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            if (flag == 0) {
                mProgressDialog.setTitle(activity.getString(R.string.total_attendance_progress_dialog));
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgress(0);
            } else {
                mProgressDialog.setTitle(activity.getString(R.string.subject_attendance_progress_dialog));
                mProgressDialog.setMessage(activity.getString(R.string.loading_progress_dialog));
                mProgressDialog.setIndeterminate(false);
            }
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            databaseNewEntrySubjectName = new ArrayList<String>();
            databaseNewEntryAttendanceStatus = new ArrayList<String>();
            isSuccessFul = false;
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                String urlAttendance = Constants.URL_ATTENDANCE;
                Connection.Response response1 = Jsoup.connect(urlAttendance)
                        .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                        .userAgent(Constants.USER_AGENT)
                        .method(Connection.Method.GET)
                        .timeout(0)
                        .execute();

                Document todayAttendancePage = response1.parse();

                Element lastFocus = todayAttendancePage.select(Constants.INPUT_LAST_FOCUS).first();
                Element eventTarget = todayAttendancePage.select(Constants.INPUT_EVENT_TARGET).first();
                Element eventArgument = todayAttendancePage.select(Constants.INPUT_EVENT_ARGUMENT).first();
                Element vsKey = todayAttendancePage.select(Constants.INPUT_VS_KEY).first();
                Element viewState = todayAttendancePage.select(Constants.INPUT_VIEW_STATE).first();
                Element eventValidation = todayAttendancePage.select(Constants.INPUT_EVENT_VALIDATION).first();
                Element studentID = todayAttendancePage.select("input[name=ctl00$ctl00$MCPH1$SCPH$hdnStudentId]").first();

                response1 = Jsoup.connect(urlAttendance)
                        .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                        .data(Constants.STRING_LAST_FOCUS, "")
                        .data(Constants.STRING_EVENT_TARGET, "")
                        .data(Constants.STRING_EVENT_ARGUMENT, "")
                        .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                        .data(Constants.STRING_VIEW_STATE, "")
                        .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$btnSemAtt", "Semester Attendance")
                        .userAgent(Constants.USER_AGENT)
                        .method(Connection.Method.POST)
                        .timeout(0)
                        .execute();
                todayAttendancePage = response1.parse();

                String[] stringMon = mondays.toArray(new String[0]);
                String[] stringSat = saturdays.toArray(new String[0]);
                for (int i = 0; i < stringMon.length; i++) {
                    response1 = Jsoup
                            .connect(urlAttendance).cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                            .data(Constants.STRING_LAST_FOCUS, "")
                            .data(Constants.STRING_EVENT_TARGET, "")
                            .data(Constants.STRING_EVENT_ARGUMENT, "")
                            .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                            .data(Constants.STRING_VIEW_STATE, "")
                            .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                            .data("ctl00$ctl00$MCPH1$SCPH$txtFrom", stringMon[i])
                            .data("ctl00$ctl00$MCPH1$SCPH$txtTo", stringSat[i])
                            .data("ctl00$ctl00$MCPH1$SCPH$btnShowAtt", "Show")
                            .userAgent(Constants.USER_AGENT)
                            .method(Connection.Method.POST).timeout(0)
                            .execute();

                    LogWrapper.out(activity.getApplicationContext(), stringMon[i]);
                    LogWrapper.out(activity.getApplicationContext(), stringSat[i]);
                    if (flag == 0) {
                        mProgressDialog.setProgress(100 - getDateProgress(stringMon[i], stringMon.length));
                    }
                    Document document = response1.parse();
                    Elements count = document.select("tr.GrdAltRow");
                    Element dateWiseTodayDateElement, dateWiseTodayNameElement, dateWiseTodayTimeElement, dateWiseTodayTypeElement, dateWiseTodayStatusElement;
                    int count1 = 0;
                    for (Element element : count) {
                        count1++;
                    }

                    for (int i1 = 0; i1 < count1; i1++) {
                        dateWiseTodayDateElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblDate_" + i1 + "]").first();
                        dateWiseTodayNameElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_" + i1 + "]").first();
                        dateWiseTodayTimeElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblTimeSlot_" + i1 + "]").first();
                        dateWiseTodayTypeElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblAttType_" + i1 + "]").first();
                        dateWiseTodayStatusElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblProgram_" + i1 + "]").first();

                        subjectName.add(dateWiseTodayNameElement.text());
                        attendanceStatus.add(dateWiseTodayStatusElement.text());
                        if (i != stringMon.length - 1) {
                            databaseNewEntrySubjectName.add(dateWiseTodayNameElement.text());
                            databaseNewEntryAttendanceStatus.add(dateWiseTodayStatusElement.text());
                        }
                    }

                }

                isSuccessFul = true;
            } catch (Exception e) {
                LogWrapper.printStackTrace(activity.getApplicationContext(), e);
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                });
                isSuccessFul = false;
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//

            mProgressDialog.dismiss();
            if (isSuccessFul) {

                String[] arraySubjectName = subjectName.toArray(new String[0]);
                String[] arrayAttendanceStatus = attendanceStatus.toArray(new String[0]);
                if (flag == 0) {
                    totalAttendance(arrayAttendanceStatus);
                } else {

                    try {
                        ArrayList<String> singleSubjectName = new ArrayList<String>();
                        ArrayList<String> singleAttendancePercent = new ArrayList<String>();
                        ArrayList<String> singleAttendanceDetails = new ArrayList<String>();


                        Set<String> uniqKeys = new TreeSet<>();
                        uniqKeys.addAll(Arrays.asList(arraySubjectName));
                        int m = 0, n = 0;
                        for (String s1 : uniqKeys) {
                            m = 0;
                            n = 0;
                            for (int j = 0; j < arraySubjectName.length; j++) {
                                if (s1.equals(arraySubjectName[j])) {
                                    m++;
                                    if (arrayAttendanceStatus[j].equals("P")) {
                                        n++;
                                    }
                                }
                            }
                            singleSubjectName.add(s1);
                            String formatpercentage = String.format("%.2f", Float.parseFloat(((float) n * 100 / m) + ""));

                            singleAttendancePercent.add(formatpercentage);
                            singleAttendanceDetails.add(n + "d" + m);

                        }


                        mProgressDialog.dismiss();

                        Bundle arg = new Bundle();
                        arg.putString(Constants.USERNAME_INTENT, userName);
                        arg.putString(Constants.PASSWORD_INTENT, passWord);
                        arg.putString(Constants.COOKIE_INTENT, sessionCookie);

                        arg.putStringArray("Array", singleSubjectName.toArray(new String[0]));
                        arg.putStringArray("Array1", singleAttendancePercent.toArray(new String[0]));
                        arg.putStringArray("Array2", singleAttendanceDetails.toArray(new String[0]));
                        SubjectWiseAttendanceExactFragment tb1 = new SubjectWiseAttendanceExactFragment();
                        tb1.setArguments(arg);
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, tb1, LibraryFragment.TAG).commit();

                        entryToDatabase();
                    } catch (Exception e) {
                        LogWrapper.printStackTrace(activity.getApplicationContext(), e);
                    }
                }
            }
        }
    }

    private void totalAttendance(String[] arrayAttendanceStatus) {

        AttendanceFragment_Database.detailsItemActivityText.setVisibility(View.VISIBLE);

        String title1 = "", title2 = "";
        int countAttendance = 0;
        for (String d : arrayAttendanceStatus) {
            if (d.equals("P")) {
                countAttendance++;
            }
        }
        title1 = ((float) countAttendance / arrayAttendanceStatus.length) * 100 + "";
        int color;
        try {
            if (Float.parseFloat(title1) >= 90) {
                color = Color.parseColor("#00BFA5");
            } else if (Float.parseFloat(title1) >= 75) {
                color = Color.parseColor("#AED581");
            } else if (Float.parseFloat(title1) >= 60) {
                color = Color.parseColor("#00B8D4");
            } else if (Float.parseFloat(title1) >= 45) {
                color = Color.parseColor("#F48FB1");
            } else {
                color = Color.parseColor("#F44336");
            }

            AttendanceFragment_Database.footerBackground.getPaint().setColor(color);

            AttendanceFragment_Database.ivm.setBackground(AttendanceFragment_Database.footerBackground);

            present = new String[1];
            absent = new String[1];
            total = new String[1];
            present[0] = countAttendance + "";
            absent[0] = arrayAttendanceStatus.length - countAttendance + "";
            total[0] = arrayAttendanceStatus.length + "";
            String formatpercentage = String.format("%.2f", Float.parseFloat(title1));
            AttendanceFragment_Database.view1.setText(formatpercentage + "%");
            AttendanceFragment_Database.view1.setGravity(Gravity.CENTER);
            studentName = MyActivity.studentName;
            RankingUtils.pushRank(activity.getApplicationContext(), userName, studentName, String.format("%.2f", Float.parseFloat(title1)));

            // db.close();
            entryToDatabase();
        } catch (Exception e) {
            present[0] = "--";
            absent[0] = "--";
            total[0] = "--";
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
            AttendanceFragment_Database.view1.setText("--");
            AttendanceFragment_Database.view1.setGravity(Gravity.CENTER);


        }
        ImageCustomAttendanceTotal ic = new ImageCustomAttendanceTotal(activity, present, absent, total);
        AttendanceFragment_Database.list.setAdapter(ic);
        AttendanceFragment_Database.refreshButton.setVisibility(View.VISIBLE);
        AttendanceFragment_Database.brower.setVisibility(View.VISIBLE);
        AttendanceFragment_Database.newsfeedLayout.setVisibility(View.VISIBLE);
        AttendanceFragment_Database.attendanceTextCardView.setVisibility(View.VISIBLE);

    }


    private int getDateProgress(String mondayDate, int length) {
        int a = (Math.abs(100 * datecount(mondayDate, current) / (length * 10)));
        System.out.println(a);
        a = randomWithRange(a - 5, a + 5);
        System.out.println(a);
        return a;
    }

    private int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    private int datecount(String date1, String date2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String inputString1 = date1;
        String inputString2 = date2;

        try {
            Date dat1 = myFormat.parse(inputString1);
            Date dat2 = myFormat.parse(inputString2);
            long diff = dat2.getTime() - dat1.getTime();
            // Toast.makeText(getApplicationContext(), TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+"",3000).show();
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    private void entryToDatabase() {
        String[] data1 = databaseNewEntrySubjectName.toArray(new String[0]);
        String[] data2 = databaseNewEntryAttendanceStatus.toArray(new String[0]);
        for (int j = 0; j < data1.length; j++) {
            db.execSQL("insert into '" + Constants.ATTENDANCE_DATABASE_TABLE_NAME + userName + "' values('" + data1[j] + "','" + data2[j] + "')");
        }
        SharedPreferences sp3 = activity.getSharedPreferences(Constants.SHARED_PREF_ATTENDANCE_TABLE, 0);
        SharedPreferences.Editor ed3 = sp3.edit();
        ed3.putString(userName, getLastSaturday(current));
        ed3.apply();
        db.close();

    }
}
