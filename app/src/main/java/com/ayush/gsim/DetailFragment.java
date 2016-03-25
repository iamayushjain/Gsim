package com.ayush.gsim;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {
    TextView text;
    TextView view1;
    
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer=false;
	private boolean mFromSavedInstanceState=false;
	public static final String TAG =DetailFragment.class.getSimpleName();
	

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	String username;
	String pass;
	String cokie;

	public static DetailFragment newInstance() {
		return new DetailFragment();
		
	}
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
    	 String menu = getArguments().getString("Menu");
    	 username = getArguments().getString("User");
    	 pass = getArguments().getString("Pass");
    	 cokie = getArguments().getString("Cook");
    	 View view;
         	   view = inflater.inflate(R.layout.layout2, container, false);
              // String menu = getArguments().getString("Menu");
               //text= (TextView) view.findViewById(R.id.detail);
               //text.setText(menu);
        	   view1=(TextView)view.findViewById(R.id.textView1);
		view1.setTypeface(Typeface
				.createFromAsset(getActivity().getAssets(), "robotothin.ttf"));

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
		         
		        /*Connection.Response response = Jsoup.connect(urlLogin)
		                .method(Connection.Method.GET)
		                .execute();


		        Document loginPage = response.parse();
	title=loginPage.title();
		   Element eventValidation = loginPage.select("input[name=__EVENTVALIDATION]").first();
		        Element viewState = loginPage.select("input[name=__VIEWSTATE]").first();

				//System.out.println(loginPage.title());
		        response = Jsoup.connect(urlLogin)
		                .data("__VIEWSTATE", viewState.attr("value"))
		                .data("__EVENTVALIDATION", eventValidation.attr("value"))
		                .data("txtUserId",username)
		                .data("txtPass", pass)
		                .data("btnLogin", "")
		                .method(Connection.Method.POST)
		                .followRedirects(true)
		                .timeout(100000)
		                .execute();

		        Document document = response.parse();
		        
		/*        view=(TextView)findViewById(R.id.textView1);
		        //Element liElement = document.select("").first();
		        if(document.title()=="SIM")
		        {
		        	view.setText("Correct");
		        }
		        else
		        {
		        	view.setText("Incorrect");
		        }
		  */     // System.out.println(document.title());
		        //String sessionId = response.cookie("ASP.NET_SessionId"); 
		        Document doc2 = Jsoup.connect(urlCourse)
		        	    .cookie("ASP.NET_SessionId", cokie)
		        	    .get();
		        title=doc2.title();
		        Element name=doc2.select("label[id=MCPH1_SCPH_lblName]").first();
		        title=name.text();
		       // dochtml=doc2.html();
		        //System.out.println(doc2.html());
			
		        }catch(IOException e)
			{
				e.printStackTrace();
			}
			return title;
			
		}

		  @Override
		  protected void onPostExecute(String result){//
			view1.setText(title);
			view1.setGravity(Gravity.CENTER);
			//Toast.makeText(getActivity(), "EDECUTED", 3000).show();
			 
		  }
	}
}