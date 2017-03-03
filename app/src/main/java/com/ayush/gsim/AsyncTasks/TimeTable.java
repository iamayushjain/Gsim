package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ayush.gsim.Fragments.TimeTableFragment;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by ayush on 25/1/17.
 */
public class TimeTable extends AsyncTask<Void, Void, String> {

    Activity activity;
    String subjectNames[] = null;
    String day[] = null;
    String collegeTime[] = null;
    String subjectFaculty[] = null;
    String block[] = null;
    FragmentManager fragmentManager;


    public TimeTable(Activity activity, String userName, String passWord, String cookie, FragmentManager fragmentManager) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.cookie = cookie;
        this.fragmentManager = fragmentManager;
    }

    String title;
    int total = 0;
    String userName, passWord, cookie;
    ProgressDialog mProgressDialog;

    int[] rpc;

    // ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,name);
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle("TimeTable");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {


        try {
            String urlTimeTable = Constants.URL_TIMETABLE;

            Document timeTablePage = Jsoup.connect(urlTimeTable)
                    .cookie(Constants.COOKIE_ASP_NET, cookie)
                    .get();

            Element lastFocus = timeTablePage.select(Constants.INPUT_LAST_FOCUS).first();
            Element eventTarget = timeTablePage.select(Constants.INPUT_EVENT_TARGET).first();
            Element eventArgument = timeTablePage.select(Constants.INPUT_EVENT_ARGUMENT).first();
            Element vsKey = timeTablePage.select(Constants.INPUT_VS_KEY).first();
            Element viewState = timeTablePage.select(Constants.INPUT_VIEW_STATE).first();
            Element eventValidation = timeTablePage.select(Constants.INPUT_EVENT_VALIDATION).first();


            Connection.Response responceTimeTable = Jsoup.connect(urlTimeTable)
                    .cookie(Constants.COOKIE_ASP_NET, cookie)
                    .data(Constants.STRING_LAST_FOCUS, "")
                    .data(Constants.STRING_EVENT_TARGET, "")
                    .data(Constants.STRING_EVENT_ARGUMENT, "")
                    .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                    .data(Constants.STRING_VIEW_STATE, "")
                    .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                    .data("ctl00$ctl00$MCPH1$SCPH$Button1", "Weekly")
                    .method(Connection.Method.POST)
                    .userAgent(Constants.USER_AGENT)
                    .followRedirects(true)
                    .timeout(100000)
                    .execute();
            timeTablePage = responceTimeTable.parse();

            Elements imports = timeTablePage.select("td");
            Elements rowsp = timeTablePage.select("td[rowspan]");


            int[] rps = new int[6];
            rpc = new int[6];
            int j = 0;
            for (Element element : rowsp) {
                total = total + Integer.parseInt(element.attr("rowspan"));
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
            day = new String[total];
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


            subjectNames = new String[total];
            collegeTime = new String[total];
            subjectFaculty = new String[total];
            block = new String[total];
            int i = 0;
            int i1 = 0;
            int i2 = 0;
            int i3 = 0;
            int l = 0;
            int m = 0;
            for (Element element : imports) {
                if (se == 3) {
                    subjectNames[i] = element.text();
                    System.out.print(element.text());
                    System.out.println(sw);
                    i++;
                }
                if (se == 6) {
                    collegeTime[i1] = element.text();
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
                    subjectFaculty[i3] = element.text();
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
            System.out.println("TRY BLOCK");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Catch");
            //MyActivity.ErrorDialog();

        }
        return title;

    }

    @Override
    protected void onPostExecute(String result) {//
        //view1.setText(title);
        //Toast.makeText(getActivity(), "EDECUTED", 3000).show();
        //	ArrayAdapter<String> adapter=new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1,subjectNames);
        //  String mon_nme[]=new String[total];
        //  int ui=0,op=0;
        try {
            mProgressDialog.dismiss();

            Bundle arg = new Bundle();
            arg.putString("Menu", "1");
            arg.putString("User", userName);
            arg.putString("Pass", passWord);
            arg.putString("Cook", cookie);
            arg.putStringArray("Array", subjectNames);
            arg.putStringArray("Array1", day);
            arg.putIntArray("Array2", rpc);
            arg.putStringArray("Array3", collegeTime);
            arg.putStringArray("Array4", subjectFaculty);
            arg.putStringArray("Array5", block);

            TimeTableFragment tb1 = new TimeTableFragment();
            tb1.setArguments(arg);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, tb1, TimeTableFragment.TAG).commit();

        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
    }
}

