package com.ayush.gsim.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ayush.gsim.ImageCustoms.ImageCustomSubjectAttendance;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;

import java.util.Locale;

public class SubjectWiseAttendanceExactFragment extends Fragment {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static final String TAG = SubjectWiseAttendanceExactFragment.class.getSimpleName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    public static SubjectWiseAttendanceExactFragment newInstance() {
        return new SubjectWiseAttendanceExactFragment();
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

            String singleSubjectName[] = getArguments().getStringArray("Array");
            String singleAttendancePercent[] = getArguments().getStringArray("Array1");
            String singleAttendanceDetails[] = getArguments().getStringArray("Array2");


            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            args.putString(Constants.USERNAME_INTENT, userName);
            args.putString(Constants.PASSWORD_INTENT, passWord);
            args.putString(Constants.COOKIE_INTENT, sessionCookie);
            args.putInt("POSS", position);
            args.putStringArray("Array", singleSubjectName);
            args.putStringArray("Array1", singleAttendancePercent);
            args.putStringArray("Array2", singleAttendanceDetails);

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
         */
        View rootView;
        ListView list;
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            try {
                rootView = inflater.inflate(R.layout.listview,
                        container, false);
                String singleSubjectName[] = getArguments().getStringArray("Array");
                String singleAttendancePercent[] = getArguments().getStringArray("Array1");
                String singleAttendanceDetails[] = getArguments().getStringArray("Array2");
                String atten_total[] = new String[singleAttendanceDetails.length];
                String atten_pre[] = new String[singleAttendanceDetails.length];
                String atten_abse[] = new String[singleAttendanceDetails.length];
                String atten_demo[];
                int obi = 0;
                String d[] = new String[3];

                for (String string : singleAttendanceDetails) {
                    string = string.trim();
                    int j = 0;

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
                        atten_abse[obi] = (Integer.parseInt(d[1]) - Integer.parseInt(d[0])) + "";
                        atten_total[obi] = d[1];

                    } catch (Exception e) {
                        atten_pre[obi] = "--";
                        atten_abse[obi] = "--";
                        atten_total[obi] = "--";


                    }
                    obi++;
                }


                ImageCustomSubjectAttendance ic = new ImageCustomSubjectAttendance(getActivity(), singleSubjectName, singleAttendancePercent, atten_pre, atten_abse, atten_total);
                list = (ListView) rootView.findViewById(R.id.listView1);
                list.setAdapter(ic);

            } catch (Exception e) {
                ErrorDialog();
            }
            return rootView;

        }

        void ErrorDialog() {
            Toast.makeText(getActivity(), "Error Dialog", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Error Connecting to The Server")
                    .setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(MyActivity.this, LauncherActivity.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
//                    args.putString("Menu", menu[0]);
//                    args.putString("User", s);
//                    args.putString("Pass", p);
//                    args.putString("Cook", ck);
//                    DetailFragment detailFragment = DetailFragment.newInstance();
//                    detailFragment.setArguments(args);
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.content_frame, detailFragment).commit();
//
//                    //finish();

                }
            })
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            //      finish();//do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

}
