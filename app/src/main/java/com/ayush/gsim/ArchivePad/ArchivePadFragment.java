package com.ayush.gsim.ArchivePad;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArchivePadFragment extends Fragment {
    public static ArchivePadFragment newInstance() {
        return new ArchivePadFragment();
    }

    private View view1;

    private String mUsername;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;
    private ValueEventListener mConnectedListener;
    private ArchivePadListAdapter mChatListAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button branchButton, subjectButton;
    private TextView introText1, introText2;
    private AlertDialog alertDialog1;
    private String[] branchs, subjectsName;
    private String selectedBranch, selectedSubject;
    private ListView listView;
    private ProgressDialog mProgressDialog;
    private RelativeLayout introRelativeLayout;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

        view1 = inflater.inflate(R.layout.archievepad_layout, container, false);

        init();
        return view1;
    }

    public void init() {
        branchButton = (Button) view1.findViewById(R.id.branchButton);
        subjectButton = (Button) view1.findViewById(R.id.subjectButton);
        introText1 = (TextView) view1.findViewById(R.id.textView10);
        introText2 = (TextView) view1.findViewById(R.id.textView11);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.ARCHIEVE_PAD);

        introText1.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), Constants.FONT_ROBOTO_THIN));
        introText2.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), Constants.FONT_ROBOTO_THIN));
        introRelativeLayout = (RelativeLayout) view1.findViewById(R.id.archieveIntroRelativeLayout);
        branchButton.setAlpha(.6f);
        subjectButton.setAlpha(.6f);
        branchButton.setEnabled(true);
        branchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogWithRadioButtonGroupBranch(branchs);
            }
        });
        subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialogWithRadioButtonGroupSubject(subjectsName);
            }
        });
        listView = (ListView) view1.findViewById(R.id.list);
        mProgressBar = (ProgressBar) view1.findViewById(R.id.progressBar1);
    }

    @Override
    public void onStart() {
        super.onStart();
        getBranches();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void getBranches() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.archive_pad_progress_dialog));
        mProgressDialog.setMessage(getString(R.string.loading_progress_dialog));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        databaseReferenceGsim.child(FirebaseConstants.ARCHIVE_PAD_SHORTCUT_BRANCHES).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        branchs = snapshot.getValue().toString().split("\\|");
                        branchButton.setAlpha(.85f);
                        mProgressDialog.dismiss();
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        LogWrapper.out(getActivity().getApplicationContext(), databaseError.getMessage());
                    }
                });

    }

    private void createAlertDialogWithRadioButtonGroupBranch(final String[] values) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Branch");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                branchButton.setAlpha(1);
                branchButton.setText("Branch: " + values[item]);
                selectedBranch = values[item];
                alertDialog1.dismiss();
                getSubjectsFromBranches(values[item]);
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    private void getSubjectsFromBranches(String branch) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.archive_pad_progress_dialog));
        mProgressDialog.setMessage(getString(R.string.loading_progress_dialog));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        final ArrayList<String> subjectsNameList = new ArrayList<String>();


        databaseReferenceGsim.child(FirebaseConstants.ARCHIVE_PAD_BRANCHES).child(branch).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        subjectsNameList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            subjectsNameList.add(dataSnapshot.getKey().toString());
                        }
                        subjectsName = subjectsNameList.toArray(new String[0]);
                        subjectButton.setEnabled(true);
                        subjectButton.setAlpha(.85f);
                        createAlertDialogWithRadioButtonGroupSubject(subjectsName);
                        mProgressDialog.dismiss();
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        LogWrapper.out(getActivity().getApplicationContext(), databaseError.getMessage());
                    }
                });

    }

    private void createAlertDialogWithRadioButtonGroupSubject(final String[] values) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Subject");


        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                subjectButton.setAlpha(1);
                subjectButton.setText("Subject: " + values[item]);
                selectedSubject = values[item];
                getPapers();
                introRelativeLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                alertDialog1.dismiss();

            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    private void getPapers() {
        try {
            mChatListAdapter = new ArchivePadListAdapter(databaseReferenceGsim.child("branches").child(selectedBranch).child(selectedSubject).limitToFirst(50)
                    , getActivity(), R.layout.imagecustom_archive_pad);
            listView.setAdapter(mChatListAdapter);

            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(0);
                }
            });
            mConnectedListener = databaseReferenceGsim.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        listView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        LogWrapper.out(getActivity().getApplicationContext(), "Output" + dataSnapshot.getValue());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    LogWrapper.out(getActivity().getApplicationContext(), databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            LogWrapper.printStackTrace(getActivity().getApplicationContext(), e);
        }
    }
}
