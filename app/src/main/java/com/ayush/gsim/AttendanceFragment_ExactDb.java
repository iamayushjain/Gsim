package com.ayush.gsim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ayush.gsim.Fragments.DetailFragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import android.app.Fragment;


public class AttendanceFragment_ExactDb extends Fragment {
    TextView text;
    TextView view1;

    ImageView img;
    ImageView ivm;
    ListView list;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer = false;
    ShapeDrawable footerBackground;
    private boolean mFromSavedInstanceState = false;
    public static final String TAG = AttendanceFragment_ExactDb.class.getSimpleName();


    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    String username;
    String pass;
    String cokie;
    String presnt[];
    String absent[];
    String total[], total_string;

    public static AttendanceFragment_ExactDb newInstance() {
        return new AttendanceFragment_ExactDb();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        String menu = getArguments().getString("Menu");
        username = getArguments().getString("User");
        pass = getArguments().getString("Pass");
        cokie = getArguments().getString("Cook");
        View view;
        view = inflater.inflate(R.layout.attendancetotal, container, false);
        ivm = (ImageView) view.findViewById(R.id.imageviewC);
        footerBackground = new ShapeDrawable();

// The corners are ordered top-left, top-right, bottom-right,
// bottom-left. For each corner, the array contains 2 values, [X_radius,
// Y_radius]
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
                .createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
        list = (ListView) view.findViewById(R.id.listView1);


        new MyTask().execute();

        return view;

    }

    private class MyTask extends AsyncTask<Void, Void, String> {

        String title = "";

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
                        .cookie("ASP.NET_SessionId", cokie)

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
                        .cookie("ASP.NET_SessionId", cokie)
                        .data("__EVENTTARGET", "")
                        .data("__EVENTARGUMENT", "")
                        .data("__VSKEY", vKey.attr("value") + "")
                        .data("__VIEWSTATE", "")
                        .data("__EVENTVALIDATION", eventValidation.attr("value") + "")
                        .data("ctl00$ctl00$MCPH1$SCPH$btnSemAtt", "Semester Attendance")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .method(Connection.Method.POST)
                        .timeout(10000)
                        .execute();


                Document doc4 = response1.parse();
                Elements count = doc4.select("tr.GrdAltRow");
                Element attendance_subject_name_el;
                // Elements book_issue_title_element=impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_0]");
                int count1 = 0;
                for (Element element : count) {
                    count1++;
                }
                count1 = count1 * 5;
                //attendance_subject_name=new String[count1];
                //attendance_subject_percentage=new String[count1];
                //attendance_subject_values=new String[count1];
                String attendance_subject_name = "";
                //for(int i=5;i<count1;i=i+6)
                {
                    attendance_subject_name_el = count.select("td[align=left]").last();

                    attendance_subject_name = attendance_subject_name_el.attr("style");
                }
                title = attendance_subject_name;
//
//		        eventValidation = 	doc4.select("input[name=__EVENTVALIDATION]").first();
//		        viewStateGenerator =doc4.select("input[name=__VIEWSTATEGENERATOR]").first();
//		        viewState = 		doc4.select("input[name=__VIEWSTATE]").first();
//
//
//
//
//		        //System.out.println(doc.html());
//		       Element viewState1 = 		doc4.select("input[name=__VIEWSTATE]").first();
//		//for(Element element:viewState1)
//
//		        String encode=viewState1.attr("value");
//		        System.out.println("Ans="+encode);
//		   String stringFromBase = new String(Base64.decode(encode,Base64.DEFAULT));//.getBytes(Charset.forName("UTF-8")).toString();
//		        System.out.println(stringFromBase);
//		        String result[] = stringFromBase.split("Total"
//						);
//				String res1[]=result[1].split("progress");
//
//				//System.out.println("Ans"+res1[0]);
//				String res2=res1[0].replaceAll("", "");
//				String attendance=res2.substring(res2.length()-10,res2.length()-2);
//				title=attendance;
//				total_string=res2.substring(res2.length()-90,res2.length()-10);


            } catch (Exception e) {
                e.printStackTrace();
                ErrorDialog();

            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {//
//			  int string_count = title.length();
//			  String title1 = "", title2 = "";
//			  for (int i = 0; i < string_count; i++) {
//				  if ((title.charAt(i) >= 48 && title.charAt(i) <= 57) || (title.charAt(i) == '.'))
//					  title1 = title1 + title.charAt(i);
//			  }
            try {


                int color;
                try {
                    if (title.contains("006600")) {
                        color = Color.parseColor("#00BFA5");
                    } else if (title.contains("00cc00") || title.contains("00ff00")) {
                        color = Color.parseColor("#AED581");
                    } else if (title.contains("0000ff") || title.contains("color:Blue")) {
                        color = Color.parseColor("#00B8D4");
                    } else if (title.contains("color:Pink") || title.contains("ffc0cb")) {
                        color = Color.parseColor("#F48FB1");
                    } else {
                        color = Color.parseColor("#F44336");
                    }

                    footerBackground.getPaint().setColor(color);

                    ivm.setBackground(footerBackground);


//
//
                } catch (Exception e) {
                    presnt[0] = "--";
                    absent[0] = "--";
                    total[0] = "--";


                }
                //ImageCustomAttendanceTotal ic=new ImageCustomAttendanceTotal(getActivity(), presnt,absent,total);
//			  //list.setAdapter(ic);
///list.setAdapter(ic);

//			  Toast.makeText(getActivity().getApplicationContext(),presnt[0]+"/"+absent[0]+"/"+total[0],3000).show();
                view1.setText("Color" + "");
                view1.setGravity(Gravity.CENTER);
                //Toast.makeText(getActivity(), "EDECUTED", 3000).show();

            } catch (Exception e) {
                e.printStackTrace();
                ErrorDialog();

            }
        }

    }

    void ErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Error Connecting to The Server")
                .setCancelable(false).setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//				Intent i = new Intent(getActivity(), LauncherActivity.class);
//				startActivity(i);
//				getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                Bundle args = new Bundle();
                args.putString("Menu", "Name");
                args.putString("User", username);
                args.putString("Pass", pass);
                args.putString("Cook", cokie);
                DetailFragment detailFragment = DetailFragment.newInstance();
                detailFragment.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, detailFragment).commit();

                //getActivity().finish();

            }
        })
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getActivity().finish();//do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}