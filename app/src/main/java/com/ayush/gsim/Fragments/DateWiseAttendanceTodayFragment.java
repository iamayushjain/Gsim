package com.ayush.gsim.Fragments;

import android.graphics.Typeface;
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

import com.ayush.gsim.ImageCustoms.ImageCustomDateWise;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;

import java.util.Locale;

public class DateWiseAttendanceTodayFragment extends Fragment {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    public static final String TAG = DateWiseAttendanceTodayFragment.class.getSimpleName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    public static DateWiseAttendanceTodayFragment newInstance() {
        return new DateWiseAttendanceTodayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


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
            String userName = getArguments().getString(Constants.USERNAME_INTENT);
            String passWord = getArguments().getString(Constants.PASSWORD_INTENT);
            String sessionCookie = getArguments().getString(Constants.COOKIE_INTENT);

            String date_wise_today_date[] = getArguments().getStringArray("Array");
            String date_wise_today_name[] = getArguments().getStringArray("Array1");
            String date_wise_today_time[] = getArguments().getStringArray("Array2");
            String date_wise_today_type[] = getArguments().getStringArray("Array3");
            String date_wise_today_status[] = getArguments().getStringArray("Array4");


            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            args.putString("Menu", "1");
            args.putString(Constants.USERNAME_INTENT, userName);
            args.putString(Constants.PASSWORD_INTENT, passWord);
            args.putString(Constants.COOKIE_INTENT, sessionCookie);
            args.putInt("POSS", position);
            args.putStringArray("Array", date_wise_today_date);
            args.putStringArray("Array1", date_wise_today_name);
            args.putStringArray("Array2", date_wise_today_time);
            args.putStringArray("Array3", date_wise_today_type);
            args.putStringArray("Array4", date_wise_today_status);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Date Wise Attendance(Today)".toUpperCase(l);
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
            String date_wise_today_date[] = getArguments().getStringArray("Array");
            String date_wise_today_name[] = getArguments().getStringArray("Array1");
            String date_wise_today_time[] = getArguments().getStringArray("Array2");
            String date_wise_today_type[] = getArguments().getStringArray("Array3");
            String date_wise_today_status[] = getArguments().getStringArray("Array4");
            if (date_wise_today_name != null && date_wise_today_name.length > 0) {
                ImageCustomDateWise ic = new ImageCustomDateWise(getActivity(), date_wise_today_date, date_wise_today_name, date_wise_today_time, date_wise_today_type, date_wise_today_status);
                list = (ListView) rootView.findViewById(R.id.listView1);
                list.setAdapter(ic);
            } else {
                TextView tvvv = (TextView) rootView.findViewById(R.id.textView);
                tvvv.setText("NOTHING TO DISPLAY");
                tvvv.setTypeface(Typeface
                        .createFromAsset(getActivity().getAssets(), "robotothin.ttf"));

            }


            return rootView;

        }

    }

}
