package com.ayush.gsim;

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

public class SubjectWiseAttendance extends Fragment{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	public static final String TAG = SubjectWiseAttendance.class.getSimpleName();
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	
	public static SubjectWiseAttendance newInstance() {
		return new SubjectWiseAttendance();
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
		String book_issue_values[]=getArguments().getStringArray("Array2");


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
			String book_issue_values[]=getArguments().getStringArray("Array2");


		Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putString("Menu", "1");
			args.putString("User", username);
			args.putString("Pass", pass);
			args.putString("Cook", cokie);
			args.putInt("POSS", position);
			args.putStringArray("Array", book_issue_title);
			args.putStringArray("Array1", book_issue_date);
			args.putStringArray("Array2", book_issue_values);

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
				return "Subject Wise Attendance".toUpperCase(l);
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
		 * 
		 */
		TextView t1,t2,t3,t4;
		int poss;
		View rootView;
		ListView list;
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
				 rootView = inflater.inflate(R.layout.listview,
							container, false);
				 String book_issue_title[]=getArguments().getStringArray("Array");
			       String book_issue_date[]=getArguments().getStringArray("Array1");
			String book_issue_values[]=getArguments().getStringArray("Array2");
			String atten_total[]=new String[book_issue_values.length];
			String atten_pre[]=new String[book_issue_values.length];
			String atten_abse[]=new String[book_issue_values.length];
			String title1;
			String atten_demo[];
			int obi=0;
			String d[]=new String[3];

			for(String string:book_issue_values )
			{
				string=string.trim();
				int j=0;

				try {
					atten_demo = string.split("d");

					String t, p, a;
					for (String stin : atten_demo) {
						if (!(stin.equals(""))) {
							d[j] = stin;
							j++;
						}
					}
					atten_pre[obi] = d[0];
					atten_abse[obi] = d[1];
					atten_total[obi] = d[2];

				}
				catch(Exception e)
				{
					atten_pre[obi]="--";
					atten_abse[obi]="--";
					atten_total[obi]="--";


				}
				obi++;
			}


			imagecustom_subjectattendance ic=new imagecustom_subjectattendance(getActivity(), book_issue_title,book_issue_date,atten_pre,atten_abse,atten_total);
					//list.setAdapter(ic);
			       list=(ListView)rootView.findViewById(R.id.listView1);
					list.setAdapter(ic);
				
						

			return rootView;
		
		}
		
	}

}
