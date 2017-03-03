package com.ayush.gsim;
import java.io.IOException;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import android.graphics.Color;
import android.graphics.Typeface;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import android.util.*;
//import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ayush.gsim.ImageCustoms.ImageCustomAttendanceTotal;

public class AttendanceFragment extends Fragment {
    TextView text;
    TextView view1;

	ImageView img;
	ImageView ivm;
    ListView list;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer=false;
	ShapeDrawable footerBackground;
	private boolean mFromSavedInstanceState=false;
	public static final String TAG =AttendanceFragment.class.getSimpleName();
	
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	String username;
	String pass;
	String cokie;
	String presnt[];
	String absent[];
	String total[],total_string;
	public static AttendanceFragment newInstance() {
		return new AttendanceFragment();
		
	}
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
    	 String menu = getArguments().getString("Menu");
    	 username = getArguments().getString("User");
    	 pass = getArguments().getString("Pass");
    	 cokie = getArguments().getString("Cook");
			View view;
			view=inflater.inflate(R.layout.attendancetotal,container,false);
		ivm=(ImageView)view.findViewById(R.id.imageviewC);
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
			view1=(TextView)view.findViewById(R.id.textView1);
		view1.setTypeface(Typeface
				.createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
		list=(ListView)view.findViewById(R.id.listView1);

		new MyTask().execute();

			return view;

		}
		private class MyTask extends AsyncTask<Void, Void, String> {

		String title ="";
		
		@Override
		  protected String doInBackground(Void... params) {
		    
		    Document doc;
		 
		   // String username=e1.getText().toString();
			//String pass=e2.getText().toString();

		
		
			//Document doc;
			try
			{
			    String urlLogin = "http://122.160.168.157/isimgc/login";
		        String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
		        String urlHome = "http://122.160.168.157/isimgc/home";
		        String urlAttendance="http://122.160.168.157/iSIMGC/Student/TodayAttendence";
		        
		        Connection.Response response1=Jsoup.connect(urlAttendance)
		        		.cookie("ASP.NET_SessionId", cokie)
		        		       
		        			//.data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt","Subject Wise Attendance")
		        		//.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
				         .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
		        	    .method(Connection.Method.GET).timeout(10000)
		        	    .execute();
		        
		        Document doc3=response1.parse();
		        
		        Element eventValidation = 	doc3.select("input[name=__EVENTVALIDATION]").first();
		        Element viewStateGenerator =doc3.select("input[name=__VIEWSTATEGENERATOR]").first();
		        Element viewState = 		doc3.select("input[name=__VIEWSTATE]").first();

		        response1=Jsoup.connect(urlAttendance)
		        		.cookie("ASP.NET_SessionId", cokie)
		        		 .data("__VIEWSTATE", viewState.attr("value"))
				                .data("__EVENTVALIDATION", eventValidation.attr("value"))
				                .data("__VIEWSTATEGENERATOR", viewStateGenerator.attr("value"))		                
				               
		        			.data("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt","Subject Wise Attendance")
		        		//.data("MCPH1_SCPH_btnSubjectWiseAtt","Subject Wise Attendance")
				         .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
		        	    .method(Connection.Method.GET).timeout(10000)
		        	    .execute();
		        

		Document doc4=response1.parse();
		        
		        eventValidation = 	doc4.select("input[name=__EVENTVALIDATION]").first();
		        viewStateGenerator =doc4.select("input[name=__VIEWSTATEGENERATOR]").first();
		        viewState = 		doc4.select("input[name=__VIEWSTATE]").first();


		        
		        
		        //System.out.println(doc.html());
		       Element viewState1 = 		doc4.select("input[name=__VIEWSTATE]").first();
		//for(Element element:viewState1)

		        String encode=viewState1.attr("value");
		        System.out.println("Ans="+encode);
		   String stringFromBase = new String(Base64.decode(encode,Base64.DEFAULT));//.getBytes(Charset.forName("UTF-8")).toString();
		        System.out.println(stringFromBase);
		        String result[] = stringFromBase.split("Total" 
						);
				String res1[]=result[1].split("progress");
				
				//System.out.println("Ans"+res1[0]);
				String res2=res1[0].replaceAll("", "");
				String attendance=res2.substring(res2.length()-10,res2.length()-2);
				title=attendance;
				total_string=res2.substring(res2.length()-90,res2.length()-10);


			}catch(IOException e)
			{
				e.printStackTrace();
			}
			return title;
			
		}

		  @Override
		  protected void onPostExecute(String result) {//
			  int string_count = title.length();
			  String title1 = "", title2 = "";
			  for (int i = 0; i < string_count; i++) {
				  if ((title.charAt(i) >= 48 && title.charAt(i) <= 57) || (title.charAt(i) == '.'))
					  title1 = title1 + title.charAt(i);
			  }
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

				  footerBackground.getPaint().setColor(color);

				  ivm.setBackground(footerBackground);

				  for (int i = 0; i < total_string.length(); i++) {
					  if ((total_string.charAt(i) >= 48 && total_string.charAt(i) <= 57) || (total_string.charAt(i) == 'd'))
						  title2 = title2 + total_string.charAt(i);
				  }

				  String atten_demo[] = title2.split("d");
				  int i = 0;


				  String d[] = new String[3];
				  int j = 0;
				  for (String str : atten_demo) {
					  if (!(str.equals(""))) {
						  d[j] = str;
						  //		  System.out.print("a"+str);
						  j++;
					  }
				  }
				  presnt = new String[1];
				  absent = new String[1];
				  total = new String[1];
				  presnt[0] = d[0];
				  absent[0] = d[1];
				  total[0] = d[2];
			  }
			  catch(Exception e)
			  {
				  presnt[0]="--";
				  absent[0]="--";
				  total[0]="--";


			  }
	  ImageCustomAttendanceTotal ic=new ImageCustomAttendanceTotal(getActivity(), presnt,absent,total);
//			  //list.setAdapter(ic);
list.setAdapter(ic);

//			  Toast.makeText(getActivity().getApplicationContext(),presnt[0]+"/"+absent[0]+"/"+total[0],3000).show();
			  view1.setText(title1+"%");
			view1.setGravity(Gravity.CENTER);
			//Toast.makeText(getActivity(), "EDECUTED", 3000).show();
			 
		  }
	}
}