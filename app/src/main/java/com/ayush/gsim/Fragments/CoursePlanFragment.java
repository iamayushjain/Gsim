package com.ayush.gsim.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayush.gsim.Activities.LauncherActivity;
import com.ayush.gsim.AsyncTasks.CoursePlanOnSubjectSelected;
import com.ayush.gsim.R;
import com.ayush.gsim.RecyclerViewsFragments.CoursePlanRecyclerView;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;

public class CoursePlanFragment extends Fragment {
    private String userName;
    private String passWord;
    private String sessionCookie;
    private Button subjectButton;

    private String[] subjectIDs;
    private String[] subjectsName;
    public AlertDialog alertDialog1;
    public static RecyclerView recyclerView;
    public CoursePlanRecyclerView coursePlanRecyclerView;
    public static ProgressBar progressBar;
    private RelativeLayout courseRelativeLayout;
    public static TextView centerTextView;

    public static CoursePlanFragment newInstance() {
        return new CoursePlanFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

        userName = getArguments().getString(Constants.USERNAME_INTENT);
        passWord = getArguments().getString(Constants.PASSWORD_INTENT);
        sessionCookie = getArguments().getString(Constants.COOKIE_INTENT);

        subjectsName = getArguments().getStringArray(Constants.COURSE_PLAN_SUBJECTS_INTENT);
        subjectIDs = getArguments().getStringArray(Constants.COURSE_PLAN_SUBJECT_IDS_INTENT);

        View view;
        view = inflater.inflate(R.layout.course_plan_layout, container, false);

        subjectButton = (Button) view.findViewById(R.id.subjectButton);

        courseRelativeLayout = (RelativeLayout) view.findViewById(R.id.coursePlanIntroRelativeLayout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        centerTextView=(TextView)view.findViewById(R.id.textView10);
        subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogWithRadioButtonGroupSubject(subjectsName, subjectIDs);
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        return view;

    }

    public void createAlertDialogWithRadioButtonGroupSubject(final String[] values, final String[] ids) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Subject");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                subjectButton.setAlpha(1);
                subjectButton.setText("Subject: " + values[item]);
                alertDialog1.dismiss();
                LogWrapper.d(getActivity().getApplicationContext(), "page", "id" + ids[item]);
                courseRelativeLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                CoursePlanOnSubjectSelected coursePlanOnSubjectSelected = new CoursePlanOnSubjectSelected(getActivity(), userName, passWord, sessionCookie, getFragmentManager());
                coursePlanOnSubjectSelected.execute(ids[item]);
                //getSubjectsFromBranches(values[item]);
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    void ErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Error Connecting to The Server")
                .setCancelable(false).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), LauncherActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

                getActivity().finish();

            }
        })
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getActivity().finish();//do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}