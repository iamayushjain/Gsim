package com.ayush.gsim;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;


public class startpage extends Activity {
	ProgressDialog mProgressDialog;
	ProgressBar pb;
	String username,pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.from_middle, R.anim.in_middle);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				//finish();
				AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
				builder.setMessage("Error Connecting to The Server")
						.setCancelable(false).setNegativeButton("Retry",new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i=new Intent(startpage.this,startpage.class);
						startActivity(i);
						overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

						finish();

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
		});
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.start_page);
		pb=(ProgressBar)findViewById(R.id.progressBar1);
		boolean b=isNetworkAvailable();

		//System.out.println(DEVICE_I);
		if(b==true)
		{
			new Title().execute();
			
			
		}
		else
		{
			//Toast.makeText(getApplicationContext(), "Yes", 3000).show();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Internet Connection Not Available")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {

			                finish();//do things
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		
			//d1.set("Cancel ME");
			//d1.setContentView()
		}
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	    
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent i=new Intent(startpage.this,startpage.class);
		///i.putExtra("User",e1.getText().toString());
		//i.putExtra("Password",e2.getText().toString());
		//startActivity(i);
		//finish();
	}

	private class Title extends AsyncTask<Void, Void, Void> {
	String title;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//mProgressDialog = new ProgressDialog(startpage.this);
		//mProgressDialog.setTitle("Android Basic JSoup Tutorial");
		//mProgressDialog.setMessage("Loading...");
		//mProgressDialog.setIndeterminate(false);
		//mProgressDialog.show();
//		mProgressDialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
	    Document doc;
		//
	
	
		//Document doc;
		try
		{
		    String urlLogin = "http://122.160.168.157/isimgc/login";
	      //  String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
	        //String urlHome = "http://122.160.168.157/isimgc/home";
	         
	        Connection.Response response = Jsoup.connect(urlLogin)
	                .method(Connection.Method.GET)
	                .timeout(0)
	                .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
	                .execute();


	        Document loginPage = response.parse();
title=loginPage.title();
	    }catch(IOException e)
		{
			finish();
			e.printStackTrace();
		}
		return null;
				
	}

	@Override
	protected void onPostExecute(Void result) {
		// Set title into TextView
		//TextView txttitle = (TextView) findViewById(R.id.titletxt);
		//txttitle.setText(title);
		String title_correct=title;
		String sim_login="SIM Login";
		 if(title_correct.equals(sim_login))
	        {
	    
		//mProgressDialog.dismiss();
	  	Intent i=new Intent(startpage.this,MyMainActivity.class);
				SharedPreferences sp2=getSharedPreferences("QPQ",0);
				String qw=(sp2.getString("WPW",""));
				if(qw.equals("1"))
				{
					if(isNetworkAvailable())
					{
						new MyTask().execute();
					}
					else
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
						builder.setMessage("Internet Connection Not Available")
								.setCancelable(false)
								.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										//do things
									}
								});
						AlertDialog alert = builder.create();
						alert.show();


					}
				}
				else {

					startActivity(i);
					overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
					finish();
				}

	        }
		 else
		 {
			 //Toast.makeText(getApplicationContext(),"Unable to", 3000).show();
			 AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
				builder.setMessage("Internet Connection Not Available")
				       .setCancelable(false)
				       .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
						   public void onClick(DialogInterface dialog, int id) {
							   finish();
						   }
					   })
				.setNegativeButton("GO OFFLINE",new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getApplicationContext(),"Going Offline",Toast.LENGTH_LONG).show();

					}
				});
				AlertDialog alert = builder.create();
				alert.show();

			
		 }
	
	}
}
	private class MyTask extends AsyncTask<Void, Void, String> {

		String title ="";
		String sessionId="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(startpage.this);
			mProgressDialog.setTitle("LogIn");
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
//			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {

			Document doc;
			SharedPreferences sp=getSharedPreferences("MM",0);
			username=(sp.getString("KK", ""));
			SharedPreferences sp1=getSharedPreferences("NN",0);
			 pass=(sp1.getString("LL", ""));

//
//			String username=e1.getText().toString();
//			String pass=e2.getText().toString();



			//Document doc;
			try
			{

				String urlLogin = "http://122.160.168.157/isimgc/login";
				String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
				String urlHome = "http://122.160.168.157/isimgc/home";
				String urlTimeTable="http://122.160.168.157/iSIMGC/Student/TimeTable";
				String urlAttendance="http://122.160.168.157/iSIMGC/Student/todayAttendance";
				Connection.Response response = Jsoup.connect(urlLogin)
						.method(Connection.Method.GET)
						.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
						.timeout(0)
						.execute();


				Document loginPage = response.parse();


				System.out.println(loginPage.title());
				//System.out.println(viewState.attr("value"));
				Element eventValidation = 	loginPage.select("input[name=__EVENTVALIDATION]").first();
				Element viewStateGenerator =loginPage.select("input[name=__VIEWSTATEGENERATOR]").first();
				Element viewState = 		loginPage.select("input[name=__VIEWSTATE]").first();

				//System.out.println(eventValidation.attr("value"));

				//System.out.println(document.title());

				response = Jsoup.connect(urlLogin)
						.data("__VIEWSTATE", viewState.attr("value"))
						.data("__EVENTVALIDATION", eventValidation.attr("value"))
						.data("__VIEWSTATEGENERATOR", viewStateGenerator.attr("value"))
						.data("txtUserId", username)
						.data("txtPass", pass)
						.data("btnValue", "")
						.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
						.method(Connection.Method.POST)
						.followRedirects(true)
						.timeout(0)
						.execute();
				//    String sessionId = response.cookie("ASP.NET_SessionId");

				Document document = response.parse();
				//Element liElement = document.select("").first();
				//      System.out.println(viewState.attr("value"));

				//    System.out.println(eventValidation.attr("value"));

				//System.out.println(document.html());
				// System.out.println(document.title());
				title=document.title();
				sessionId = response.cookie("ASP.NET_SessionId");
				//  System.out.println(sessionId);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return title;

		}

		@Override
		protected void onPostExecute(String result){//
			String title_correct=title;
			String sim="SIM";
			mProgressDialog.dismiss();

			if(title_correct.equals(sim))
			{
				//view.setText(view.getText().toString()+title+"Correct");
				Intent i=new Intent(startpage.this,MyActivity.class);

				i.putExtra("User",username);
				i.putExtra("Password", pass);
				i.putExtra("Cookie", sessionId);



				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

				finish();

			}
			else
			{
				mProgressDialog.dismiss();
				Intent i=new Intent(startpage.this,MyMainActivity.class);

				SharedPreferences sp3=getSharedPreferences("QPQ",0);
				SharedPreferences.Editor ed3=sp3.edit();
				ed3.putString("WPW","0");
				ed3.commit();

				AlertDialog.Builder builder = new AlertDialog.Builder(startpage.this);
				builder.setMessage("Incorrect UserName or Password")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
				finish();


			}
			//view.setText(title);

		}
	}

}