package com.ayush.gsim;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;





import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MyMainActivity extends Activity {
EditText e1,e2;
Button b1;
CheckBox box,box1;
ProgressDialog mProgressDialog;
//private InterstitialAd mInterstitialAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
				builder.setMessage("Error Connecting to The Server")
						.setCancelable(false).setNegativeButton("Retry",new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i=new Intent(MyMainActivity.this,MyMainActivity.class);
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
	    setContentView(R.layout.activity_my_main);
		
		e1=(EditText)findViewById(R.id.editText1);
		e2=(EditText)findViewById(R.id.editText2);
		b1=(Button)findViewById(R.id.button1);
		box=(CheckBox)findViewById(R.id.checkBox1);
		box1=(CheckBox)findViewById(R.id.checkBox2);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(e1, InputMethodManager.SHOW_IMPLICIT);
		e1.setTypeface(Typeface
				.createFromAsset(getAssets(), "robotothin.ttf"));


		SharedPreferences sp=getSharedPreferences("MM",0);
		e1.setText(sp.getString("KK", ""));
		SharedPreferences sp1=getSharedPreferences("NN",0);
		e2.setText(sp1.getString("LL",""));
		SharedPreferences sp2=getSharedPreferences("QPQ",0);
		String qw=(sp2.getString("WPW",""));

		//mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-5666077083567257/2174168522");
        //AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		//		.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();
        //mInterstitialAd.loadAd(adRequest1);
        //mInterstitialAd.setAdListener(new AdListener(){
//            public void onAdLoaded(){
//
//                                    // if (mInterstitialAd.isLoaded()) {
//                       //  mInterstitialAd.show();
//                     //}
//
//            }
//  //});
//
		//Toast.makeText(getApplicationContext(),qw,3000).show();
		if(qw.equals("1"))
		{
			if(isNetworkAvailable())
			{
				new MyTask().execute();
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
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

		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(box.isChecked())
				{
					//Toast.makeText(getApplicationContext(), "TRUE", 3000).show();
					SharedPreferences sp1=getSharedPreferences("MM",0);
					SharedPreferences.Editor ed1=sp1.edit();
					ed1.putString("KK",e1.getText().toString());
					ed1.commit();
					SharedPreferences sp2=getSharedPreferences("NN",0);
					SharedPreferences.Editor ed2=sp2.edit();
					ed2.putString("LL",e2.getText().toString());
					ed2.commit();
				
				
				}
				if(box1.isChecked())
				{
					SharedPreferences sp1=getSharedPreferences("MM",0);
					SharedPreferences.Editor ed1=sp1.edit();
					ed1.putString("KK",e1.getText().toString());
					ed1.commit();
					SharedPreferences sp2=getSharedPreferences("NN",0);
					SharedPreferences.Editor ed2=sp2.edit();
					ed2.putString("LL",e2.getText().toString());
					ed2.commit();
					SharedPreferences sp3=getSharedPreferences("QPQ",0);
					SharedPreferences.Editor ed3=sp3.edit();
					ed3.putString("WPW","1");
					ed3.commit();

				}
				else
				{

					SharedPreferences sp3=getSharedPreferences("QPQ",0);
					SharedPreferences.Editor ed3=sp3.edit();
					ed3.putString("WPW","0");
					ed3.commit();

				}
				if(isNetworkAvailable())
				{
				new MyTask().execute();
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
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
		});
		
				
	
		
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	    
	}


private class MyTask extends AsyncTask<Void, Void, String> {

	String title ="";
	 String sessionId="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(MyMainActivity.this);
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
	 
	    String username=e1.getText().toString();
		String pass=e2.getText().toString();

	
	
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
	        	Intent i=new Intent(MyMainActivity.this,MyActivity.class);
				i.putExtra("User",e1.getText().toString());
				i.putExtra("Password", e2.getText().toString());
				i.putExtra("Cookie", sessionId);



				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

				finish();
			
	        }
	        else
	        {
				mProgressDialog.dismiss();

				SharedPreferences sp3=getSharedPreferences("QPQ",0);
				SharedPreferences.Editor ed3=sp3.edit();
				ed3.putString("WPW","0");
				ed3.commit();

				AlertDialog.Builder builder = new AlertDialog.Builder(MyMainActivity.this);
				builder.setMessage("Incorrect UserName or Password")
				       .setCancelable(false)
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                e2.setText("");//do things
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			
	        }
	 //view.setText(title);
		  
	  }
}

}

	
	
