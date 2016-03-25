package com.ayush.gsim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TabbedActivity extends Fragment{


	/**
	 * 
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	public static final String TAG = TabbedActivity.class.getSimpleName();
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	
	public static TabbedActivity newInstance() {
		return new TabbedActivity();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String menu = getArguments().getString("Menu");
   	 String username = getArguments().getString("User");
   	 String pass = getArguments().getString("Pass");
   	 String cokie = getArguments().getString("Cook");
   	 String sub_name[]=getArguments().getStringArray("Array");
   	String day[]=getArguments().getStringArray("Array1");
   	int rpc[]=getArguments().getIntArray("Array2");
    String cl_time[]=getArguments().getStringArray("Array3");
   	String sub_fac[]=getArguments().getStringArray("Array4");
   	String block[]=getArguments().getStringArray("Array5");
   	View v = inflater.inflate(R.layout.activity_item_one, container, false);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getChildFragmentManager());
		
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		return v;
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			String menu = getArguments().getString("Menu");
		   	String username = getArguments().getString("User");
		   	 String pass = getArguments().getString("Pass");
		   	 String cokie = getArguments().getString("Cook");
		   	String sub_name[]=getArguments().getStringArray("Array");
		   	String day[]=getArguments().getStringArray("Array1");
			int rpc[]=getArguments().getIntArray("Array2");
			String cl_time[]=getArguments().getStringArray("Array3");
			String sub_fac[]=getArguments().getStringArray("Array4");
		   	String block[]=getArguments().getStringArray("Array5");
		   	Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position +1);
			args.putString("Menu", "He");
			args.putString("User", username);
			args.putString("Pass", pass);
			args.putString("Cook", cokie);
			args.putString("POSS", position+"");
			args.putStringArray("Array", sub_name);
			args.putStringArray("Array1", day);
			args.putIntArray("Array2", rpc);
			args.putStringArray("Array3", cl_time);
			args.putStringArray("Array4", sub_fac);
			args.putStringArray("Array5", block);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Monday".toUpperCase(l);
			case 1:
				return "Tuesday".toUpperCase(l);
			case 2:
				return "Wednesday".toUpperCase(l);
			case 3:
				return "Thursday".toUpperCase(l);
			case 4:
				return "Friday".toUpperCase(l);
			
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		ListView list;
		//ProgressDialog mProgressDialog;

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}
		String username;
		String pass;
		String cokie;
		String sub_name[];
		String day[];
		String cl_time[];
		int rpc[];
		String sub_fac[];
	   	String block[];
	   	
		View rootView;
		int divs;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			String menu = getArguments().getString("Menu");
		   	 username = getArguments().getString("User");
		   	 pass = getArguments().getString("Pass");
		   	 cokie = getArguments().getString("Cook");
		   	 sub_name=getArguments().getStringArray("Array");
		   	day=getArguments().getStringArray("Array1");
		    rpc=getArguments().getIntArray("Array2");
			cl_time=getArguments().getStringArray("Array3");
			sub_fac=getArguments().getStringArray("Array4");
		   	block=getArguments().getStringArray("Array5");
		   	
		   	divs=Integer.parseInt(getArguments().getString("POSS"));
		   	rootView = inflater.inflate(R.layout.listview,
					container, false);
		   	
		//	TextView dummyTextView = (TextView) rootView
			//		.findViewById(R.id.section_label);
			//dummyTextView.setText(username);
	 list=(ListView)rootView.findViewById(R.id.listView1);
		  //new MyTask().execute();
	  String mon_nme[]=new String[rpc[divs]];
	  String mon_nme1[]=new String[rpc[divs]];
	  String mon_nme2[]=new String[rpc[divs]];
	  String mon_nme3[]=new String[rpc[divs]];
	  
	  int ui=0,op=0;
	 // mProgressDialog.dismiss();
		
	 for (String string : day) {
		
	 if(divs==0)
	  {
		  if(string.equals("Monday"))
		  {
			  mon_nme[op]=sub_name[ui];
			  mon_nme1[op]=cl_time[ui];
			  mon_nme2[op]=sub_fac[ui];
			  mon_nme3[op]=block[ui];
			  op++;
		  }
		//  ui++;
	  }
	  if(divs==1)
	  {
		  if(string.equals("Tuesday"))
		  {
			  mon_nme[op]=sub_name[ui];
			  mon_nme1[op]=cl_time[ui];
			  mon_nme2[op]=sub_fac[ui];
			  mon_nme3[op]=block[ui];
			  op++;
		  }
		  //ui++;
	  }
	 if(divs==2)
	  {
		  if(string.equals("Wednesday"))
		  {
			  mon_nme[op]=sub_name[ui];
			  mon_nme1[op]=cl_time[ui];
			  mon_nme2[op]=sub_fac[ui];
			  mon_nme3[op]=block[ui];
			  op++;
		  }
		  //ui++;
	  }
	 if(divs==3)
	  {
		  if(string.equals("Thursday"))
		  {
			  mon_nme[op]=sub_name[ui];
			  mon_nme1[op]=cl_time[ui];
			  mon_nme2[op]=sub_fac[ui];
			  mon_nme3[op]=block[ui];
			  
			  op++;
		  }
		  //ui++;
	  }
	 if(divs==4)
	  {
		  if(string.equals("Friday"))
		  {
			  mon_nme[op]=sub_name[ui];
			  mon_nme1[op]=cl_time[ui];
			  mon_nme2[op]=sub_fac[ui];
			  mon_nme3[op]=block[ui];
			  op++;
		  }
		  //ui++;
	  }
	 ui++;
	
	 }
	// ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mon_nme);
	 imagecustom ic=new imagecustom(getActivity(), mon_nme,mon_nme1,mon_nme2,mon_nme3);
		//list.setAdapter(ic);
	 
		list.setAdapter(ic);
	
          
			return rootView;
		///	SQLiteDatabase db=openOrCreateDatabase("MYDataBase", 0, null);
			
		}
	}
}
		/*private class MyTask extends AsyncTask<Void, Void, String> {

			String title ="he";
			   String sub_name[]=null;
			   String day[]=null;
			   String name[]=new String[]{"Ayush","Jain","Java"};
			   int rpc[]={8,8,8,8,8};
			  // ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,name);
				protected void onPreExecute() {
					super.onPreExecute();
					mProgressDialog = new ProgressDialog(getActivity());
					mProgressDialog.setTitle("LogIn");
					mProgressDialog.setMessage("Loading...");
					mProgressDialog.setIndeterminate(false);
					mProgressDialog.show();
//					mProgressDialog.show();
				}

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
			        String urlTimeTable="http://122.160.168.157/iSIMGC/Student/TimeTable";
			        //Toast.makeText(getActivity(), "HEllo", 3000).show();
			        Document doc2=Jsoup.connect(urlHome)
			        		.cookie("ASP.NET_SessionId", cokie)
			        	    .get();
			        
			        doc2=Jsoup.connect(urlTimeTable)
			        		.cookie("ASP.NET_SessionId", cokie)
			        	    .get();
			       
			       Element eventValidation = doc2.select("input[name=__EVENTVALIDATION]").first();
			         Element viewState = doc2.select("input[name=__VIEWSTATE]").first();

			        Connection.Response rp = Jsoup.connect(urlTimeTable)
			        	    .cookie("ASP.NET_SessionId", cokie)
			        	    .data("__VIEWSTATE", viewState.attr("value"))
			                .data("__EVENTVALIDATION", eventValidation.attr("value")).
			               data("ctl00$ctl00$MCPH1$SCPH$Button1","Weekly").method(Connection.Method.POST)
			               .followRedirects(true).timeout(100000)
			               .execute();
			        doc2=rp.parse();
			       /* Connection.Response response=Jsoup.connect(urlTimeTable)
			        		.cookie("ASP.NET_SessionId", cokie)
			        	    .method(Connection.Method.GET)
			                .execute();
			        Document doc2=response.parse();
			         Element eventValidation = doc2.select("input[name=__EVENTVALIDATION]").first();
			         Element viewState = doc2.select("input[name=__VIEWSTATE]").first();

			        response= Jsoup.connect(urlTimeTable)
			        	    .cookie("ASP.NET_SessionId", cokie)
			        	    .data("__VIEWSTATE", viewState.attr("value"))
			                .data("__EVENTVALIDATION", eventValidation.attr("value")).
			               data("ctl00$ctl00$MCPH1$SCPH$Button1","Weekly").method(Connection.Method.POST)
			                .followRedirects(true).timeout(100000)
			                .execute();
			        //Element 
			     doc2=response.parse();
//title=doc2.title();
			       // Element name=doc2.select("label[id=MCPH1_SCPH_lblName]").first();
			        //title=name.text();
			       // dochtml=doc2.html();
			        //System.out.println(doc2.html());
			         *\
			        Elements imports = doc2.select("td");
			        Elements rowsp=doc2.select("td[rowspan]");
			        int totaa=0;
			        //print("\nMedia: (%d)", media.size());
			        int rps[]={8,6,6,4,3};
			        //int rpc[]={8,6,6,4,3};
			        int j=0;
			  //      adapter.add("RAT");
			    //    adapter.notifyDataSetChanged();
			        for (Element element : rowsp) {
			 		//System.out.println(element.attr("rowspan"));
			 		totaa=totaa+Integer.parseInt(element.attr("rowspan"));
			 		rps[j]=(Integer.parseInt(element.attr("rowspan"))*6)+1;
			 		rpc[j]=Integer.parseInt(element.attr("rowspan"));
			 		j++;
			 	}
			        
			        //System.out.println("Coming");
			        //String day;
			        int se=1;
			        int sw=1;
			        int ml=0,hi=0,gt=0;
			        String weekdays[]={"Monday","Tuesday","Wednesday","Thursday","Friday"};
			        day=new String[totaa];
			        for (String string : day) {
			 		day[hi]=weekdays[ml];
			 				hi++;
			 				gt++;
			 				if(gt==rpc[ml])
			 				{	if(ml!=4)
			 					ml++;
			 					gt=0;
			 				}
			 	}
			        
			        String []sub_code={};
			        sub_name=new String[totaa];
			        String []sub_fac={};
			        String []batch={};
			        String []cl_time={};
			        String []block={};
			        int i=0;
			        int l=0;
			        int m=0;
			        for (Element element : imports) {
			        	 if(se==3)
			      	   {
			      		   sub_name[i]=element.text();
			      		   System.out.print(element.text());
			      		   System.out.println(sw);
			      		   i++;
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
			      	   }*]
			      	   if(se==7)
			      	   {
			      		   se=1;
			      	   }
			      	   if(sw==rps[l])
			      	   {
			      		   sw=0;
			      		   se=0;
			      		   if(l!=4)
			      			   l++;
			      		   
			      	   }
			      	   se++;
			      	   sw++;
			  		//Sy
			        }    
			        name=new String[]{"HArsh","LOL"};
					
			    }catch(IOException e)
				{
					e.printStackTrace();
				}
				return title;
				
			}
			   
			  @Override
			  protected void onPostExecute(String result){//
				//view1.setText(title);
				//Toast.makeText(getActivity(), "EDECUTED", 3000).show();
				//	ArrayAdapter<String> adapter=new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1,sub_name);
				  String mon_nme[]=new String[rpc[divs]];
				  int ui=0,op=0;
				  mProgressDialog.dismiss();
					
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
			  }
		}
	
		}
	
	
//	class data
	//{
	   
	//}

	
}
/*class tableview
{
public void main(){
	
	
	
String a1[]={"MONDAY","TUESDAY"};
String a2[]={"WED","THU"};
try {
	try {
		
	} catch (Exception e) {
		// TODO: handle exception
	}
} catch (Exception e) {
	// TODO: handle exception
}
}*/
//}