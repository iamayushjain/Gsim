package com.ayush.gsim;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class ErrorFragment_MyActivity extends Fragment{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	public static final String TAG = ErrorFragment_MyActivity.class.getSimpleName();
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	
	public static ErrorFragment_MyActivity newInstance() {
		return new ErrorFragment_MyActivity();
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

		String book_issue_title[]=getArguments().getStringArray("Array");
	       String book_issue_date[]=getArguments().getStringArray("Array1");
	       String book_due_date[]=getArguments().getStringArray("Array2");
	       String book_return_date[]=getArguments().getStringArray("Array3");
	       

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

			String book_issue_title[]=getArguments().getStringArray("Array");
		       String book_issue_date[]=getArguments().getStringArray("Array1");
		       String book_due_date[]=getArguments().getStringArray("Array2");
		       String book_return_date[]=getArguments().getStringArray("Array3");
		       

		Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putString("Menu", "1");
			args.putString("User", username);
			args.putString("Pass", pass);
			args.putString("Cook", cokie);
			args.putInt("POSS",position);
			args.putStringArray("Array", book_issue_title);
			args.putStringArray("Array1", book_issue_date);
			
			args.putStringArray("Array2", book_due_date);
			args.putStringArray("Array3", book_return_date);
			
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Error!!!!!".toUpperCase(l);
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
		TextView t1, t2, t3, t4;
		int poss;
		View rootView;
		ListView list;
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.listview,
					container, false);
			if (isNetworkAvailable()) {
				TextView tvvv = (TextView) rootView.findViewById(R.id.textView);
				tvvv.setText("Error Connecting to the Server");
				tvvv.setTypeface(Typeface
						.createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
			} else {
				TextView tvvv = (TextView) rootView.findViewById(R.id.textView);
				tvvv.setText("Internet Not Available");
				tvvv.setTypeface(Typeface
						.createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
			}


			return rootView;

		}


		public boolean isNetworkAvailable() {
			ConnectivityManager connectivityManager
					= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			return activeNetworkInfo != null && activeNetworkInfo.isConnected();

		}

	}
}
