package com.ayush.gsim.ImageCustoms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayush.gsim.Activities.ArchiveActivity;
import com.ayush.gsim.Activities.BranchWiseChatActivity;
import com.ayush.gsim.Activities.NewsFeedActivity;
import com.ayush.gsim.AsyncTasks.Attendance;
import com.ayush.gsim.AsyncTasks.CoursePlan;
import com.ayush.gsim.AsyncTasks.DateWiseAttendace;
import com.ayush.gsim.AsyncTasks.Library;
import com.ayush.gsim.AsyncTasks.TimeTable;
import com.ayush.gsim.Fragments.AttendanceFragment_Database;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;

import java.util.List;

public class ImageCustomDetailsHomeScreenRecycler extends RecyclerView.Adapter<ImageCustomDetailsHomeScreenRecycler.MyViewHolder> {

    private List<Integer> homeImages;
    private List<String> imagesString;
    private String type;
    private String userName, passWord, sessionCookie;
    private Activity activity;
    private FragmentManager fragmentManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.textView);
        }
    }


    public ImageCustomDetailsHomeScreenRecycler(Activity activity, FragmentManager fragmentManager, List<Integer> imagesList, List<String> imagesString, String userName, String passWord, String sessionCookie, String type) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.homeImages = imagesList;
        this.imagesString = imagesString;
        this.userName = userName;
        this.passWord = passWord;
        this.sessionCookie = sessionCookie;
        this.type = type;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imagecustom_details_home_screen, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.imageView.setImageResource(homeImages.get(position));
        holder.textView.setText(imagesString.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseList(type, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeImages.size();
    }

    private void chooseList(String type, int position) {
        if (type.equals(Constants.HOME_PAGE_LIST_TYPE_ATTENDANCE)) {
            navigateToAttendance(position);
        }
        if (type.equals(Constants.HOME_PAGE_LIST_TYPE_ERP)) {
            navigateToERP(position);
        }
        if (type.equals(Constants.HOME_PAGE_LIST_TYPE_OPTIONS)) {
            navigateToOptions(position);
        }
    }

    private void navigateToAttendance(int position) {
        Bundle args = new Bundle();
        args.putString(Constants.USERNAME_INTENT, userName);
        args.putString(Constants.PASSWORD_INTENT, passWord);
        args.putString(Constants.COOKIE_INTENT, sessionCookie);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent i;
        switch (position) {
            case 0:
                AttendanceFragment_Database attendaceFragment = AttendanceFragment_Database.newInstance();
                attendaceFragment.setArguments(args);
                fragmentTransaction.replace(R.id.content_frame, attendaceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                Attendance attendance = new Attendance(activity, userName, passWord, sessionCookie, fragmentManager, 1);
                attendance.databaseFunction();
                break;
            case 2:
                DateWiseAttendace dateWiseAttendace = new DateWiseAttendace(activity, userName, passWord, sessionCookie, fragmentManager);
                dateWiseAttendace.getTodayDate();
                break;
            case 3:

                DateWiseAttendace dateWiseAttendace1 = new DateWiseAttendace(activity, userName, passWord, sessionCookie, fragmentManager);
                dateWiseAttendace1.getDateFunction();
                break;
        }
    }

    private void navigateToERP(int position) {

        Bundle args = new Bundle();
        args.putString(Constants.USERNAME_INTENT, userName);
        args.putString(Constants.PASSWORD_INTENT, passWord);
        args.putString(Constants.COOKIE_INTENT, sessionCookie);


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent i;
        switch (position) {
            case 0:
                TimeTable timeTable = new TimeTable(activity, userName, passWord, sessionCookie, fragmentManager);
                timeTable.execute();
                break;
            case 1:
                CoursePlan coursePlan = new CoursePlan(activity, userName, passWord, sessionCookie, fragmentManager);
                coursePlan.execute();
                break;
            case 2:
                Library library = new Library(activity, userName, passWord, sessionCookie, fragmentManager);
                library.execute();
                break;

        }
    }

    private void navigateToOptions(int position) {
        Bundle args = new Bundle();
        args.putString(Constants.USERNAME_INTENT, userName);
        args.putString(Constants.PASSWORD_INTENT, passWord);
        args.putString(Constants.COOKIE_INTENT, sessionCookie);


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent i;
        switch (position) {
            case 0:
                Intent newsFeedIntent = new Intent(activity, NewsFeedActivity.class);
                activity.startActivity(newsFeedIntent);
                activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                break;
            case 1:
                Intent branchWiseChat = new Intent(activity, BranchWiseChatActivity.class);
                branchWiseChat.putExtra(Constants.USERNAME_INTENT, userName);
                branchWiseChat.putExtra(Constants.PASSWORD_INTENT, passWord);
                branchWiseChat.putExtra(Constants.COOKIE_INTENT, sessionCookie);

                activity.startActivity(branchWiseChat);
                activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                break;
            case 2:
                Intent achieveIntent = new Intent(activity, ArchiveActivity.class);
                activity.startActivity(achieveIntent);
                activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                break;
        }
    }
}