package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ayush.gsim.Fragments.DateWiseAttendanceTodayFragment;
import com.ayush.gsim.Fragments.LibraryFragment;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;
import com.ayush.gsim.Utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ayush on 29/1/17.
 */

public class DateWiseAttendace {
    private Activity activity;
    private String userName, passWord, sessionCookie;
    private ProgressDialog mProgressDialog;
    private FragmentManager fragmentManager;


    private List<String> dateWiseTodayDate;
    private List<String> dateWiseTodayName;
    private List<String> dateWiseTodayTime;
    private List<String> dateWiseTodayType;
    private List<String> dateWiseTodayStatus;
    private String fromDate = null;
    private String toDate = null;

    public DateWiseAttendace(Activity activity, String userName, String passWord, String cookie, FragmentManager fragmentManager) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.sessionCookie = cookie;
        this.fragmentManager = fragmentManager;
    }

    public void getDateFunction() {

        final Dialog myDialog = new Dialog(activity);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        myDialog.setContentView(R.layout.datelayout);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);

        ImageButton b1 = (ImageButton) myDialog.findViewById(R.id.calc_clear_txt_Prise);
        ImageButton b2 = (ImageButton) myDialog.findViewById(R.id.calc_clear_txt_Prise1);
        Button b3 = (Button) myDialog.findViewById(R.id.bno);
        Button b4 = (Button) myDialog.findViewById(R.id.byes);
        b1.setBackgroundColor(Color.TRANSPARENT);
        b2.setBackgroundColor(Color.TRANSPARENT);

        final EditText e1 = (EditText) myDialog.findViewById(R.id.calc_txt_Prise);
        final EditText e2 = (EditText) myDialog.findViewById(R.id.calc_txt_Prise1);
        e1.setTypeface(Typeface
                .createFromAsset(activity.getAssets(), Constants.FONT_ROBOTO_THIN));
        e2.setTypeface(Typeface
                .createFromAsset(activity.getAssets(), Constants.FONT_ROBOTO_THIN));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePicker input = new DatePicker(activity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setMaxDate(System.currentTimeMillis());
                input.setBackgroundColor(activity.getResources().getColor(R.color.newgreen));
                input.setLayoutParams(lp);
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alert;
                builder.setView(input).setMessage("PICK DATE").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int day = input.getDayOfMonth();
                        int month = input.getMonth() + 1;
                        int year = input.getYear();
                        e1.setText(day + "/" + month + "/" + year);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                alert = builder.create();
                if (!alert.isShowing()) {
                    alert.show();
                }

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePicker input = new DatePicker(activity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setMaxDate(System.currentTimeMillis());
                input.setBackgroundColor(activity.getResources().getColor(R.color.newgreen));
                input.setLayoutParams(lp);
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final AlertDialog alert;
                builder.setView(input).setMessage("PICK DATE").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int day = input.getDayOfMonth();
                        int month = input.getMonth() + 1;
                        int year = input.getYear();
                        e2.setText(day + "/" + month + "/" + year);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                alert = builder.create();
                if (!alert.isShowing()) {
                    alert.show();
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
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                if (Utils.isDateValid(s1) && Utils.isDateValid(s2)) {
                    if (!dateCount(e1.getText().toString(), e2.getText().toString())) {
                        Toast.makeText(activity.getApplicationContext(), "Difference Between Two Days Can't more than 8 days", Toast.LENGTH_LONG).show();
                    } else {
                        fromDate = s1;
                        toDate = s2;
                        myDialog.cancel();
                        new TodayAttendanceDateAsyncTask().execute(fromDate, toDate);
                    }
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Dates Not Valid", Toast.LENGTH_LONG).show();
                }
            }
        });

        myDialog.show();


    }

    public void getTodayDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        new TodayAttendanceDateAsyncTask().execute(formattedDate, formattedDate);

    }

    private boolean dateCount(String date1, String date2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String inputString1 = date1;
        String inputString2 = date2;

        try {
            Date dat1 = myFormat.parse(inputString1);
            Date dat2 = myFormat.parse(inputString2);
            long diff = dat2.getTime() - dat1.getTime();
            if ((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 8) && (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 0)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private class TodayAttendanceDateAsyncTask extends AsyncTask<String, Void, String> {

        String title = "";

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setTitle("Today Attendance(Date)");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

            dateWiseTodayDate = new ArrayList<>();
            dateWiseTodayName = new ArrayList<>();
            dateWiseTodayTime = new ArrayList<>();
            dateWiseTodayType = new ArrayList<>();
            dateWiseTodayStatus = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
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
                LogWrapper.out(activity.getApplicationContext(), todayAttendancePage.html());


                response1 = Jsoup.connect(urlAttendance)
                        .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                        .data(Constants.STRING_LAST_FOCUS, "")
                        .data(Constants.STRING_EVENT_TARGET, "")
                        .data(Constants.STRING_EVENT_ARGUMENT, "")
                        .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                        .data(Constants.STRING_VIEW_STATE, "")
                        .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$txtFrom", params[0])
                        .data("ctl00$ctl00$MCPH1$SCPH$txtTo", params[1])
                        .data("ctl00$ctl00$MCPH1$SCPH$btnShowAtt", "Show")
                        .userAgent(Constants.USER_AGENT)
                        .method(Connection.Method.POST).timeout(10000)
                        .execute();


                Document doc4 = response1.parse();

                Elements count = doc4.select("tr.GrdAltRow");
                Element dateWiseTodayDateElement, dateWiseTodayNameElement, dateWiseTodayTimeElement, dateWiseTodayTypeElement, dateWiseTodayStatusElement;
                int count1 = 0;
                for (Element element : count) {
                    count1++;
                }
                // attendance_subject_name=new String[count1];
                //attendance_subject_percentage=new String[count1];

                for (int i = 0; i < count1; i++) {
                    //attendance_subject_name_el=count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_"+i+"]").first();
                    dateWiseTodayDateElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblDate_" + i + "]").first();
                    dateWiseTodayNameElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblNAME_" + i + "]").first();
                    dateWiseTodayTimeElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblTimeSlot_" + i + "]").first();
                    dateWiseTodayTypeElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblAttType_" + i + "]").first();
                    dateWiseTodayStatusElement = count.select("span[id=MCPH1_SCPH_gvDateWise_lblProgram_" + i + "]").first();
                    //attendance_subject_name[i]=attendance_subject_name_el.text();
                    //attendance_subject_percentage[i]=attendance_subject_percentdemo[i];
                    dateWiseTodayDate.add(dateWiseTodayDateElement.text());
                    dateWiseTodayName.add(dateWiseTodayNameElement.text());
                    dateWiseTodayTime.add(dateWiseTodayTimeElement.text());
                    dateWiseTodayType.add(dateWiseTodayTypeElement.text());
                    dateWiseTodayStatus.add(dateWiseTodayStatusElement.text());

                }
            } catch (Exception e) {
                LogWrapper.printStackTrace(activity.getApplicationContext(), e);
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//
            try {
                mProgressDialog.dismiss();

                Bundle arg = new Bundle();
                arg.putString(Constants.USERNAME_INTENT, userName);
                arg.putString(Constants.PASSWORD_INTENT, passWord);
                arg.putString(Constants.COOKIE_INTENT, sessionCookie);

                arg.putStringArray("Array", (dateWiseTodayDate != null) ? dateWiseTodayDate.toArray(new String[dateWiseTodayDate.size()]) : null);
                arg.putStringArray("Array1", (dateWiseTodayName != null) ? dateWiseTodayName.toArray(new String[dateWiseTodayName.size()]) : null);
                arg.putStringArray("Array2", (dateWiseTodayTime != null) ? dateWiseTodayTime.toArray(new String[dateWiseTodayTime.size()]) : null);
                arg.putStringArray("Array3", (dateWiseTodayType != null) ? dateWiseTodayType.toArray(new String[dateWiseTodayType.size()]) : null);
                arg.putStringArray("Array4", (dateWiseTodayStatus != null) ? dateWiseTodayStatus.toArray(new String[dateWiseTodayStatus.size()]) : null);

                DateWiseAttendanceTodayFragment tb1 = new DateWiseAttendanceTodayFragment();
                tb1.setArguments(arg);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, tb1, LibraryFragment.TAG).commit();

            } catch (Exception e) {
                LogWrapper.printStackTrace(activity.getApplicationContext(), e);
            }
        }
    }
}
