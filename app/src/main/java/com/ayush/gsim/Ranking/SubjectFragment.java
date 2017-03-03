package com.ayush.gsim.Ranking;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayush.gsim.R;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment {

    private String pathToToppr, pathUpToSubject;
    private File[] directories;
    private String selectedSubject, standardName;
    private RecyclerView recyclerView;

    private List<String> chapterName = new ArrayList<String>();
    private List<Integer> chapterVideoCount = new ArrayList<>();//TODO select only those with .mp4 video count in case of subtitles

    public SubjectFragment() {
        // Required empty public constructor
    }

    public static SubjectFragment getNewInstance(String subjectName, String path, String standardName) {
        SubjectFragment subjectFragment = new SubjectFragment();
//        Bundle args = new Bundle();
//        args.putString(Constants.SELECTED_SUBJECT, subjectName);
//        args.putString(Constants.PATH_TO_TOPPR, path);
//        args.putString(Constants.STANDARD_NAME, standardName);
//        subjectFragment.setArguments(args);
        return subjectFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRank();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ranking_layout_fragment, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//        ChapterAdapter chapterAdapter = new ChapterAdapter(getActivity(), chapterName, chapterVideoCount, standardName, selectedSubject, pathToToppr, pathUpToSubject);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(chapterAdapter);
        return view;
    }

    private void getRank() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child(FirebaseConstants.PROFILE).orderByChild(FirebaseConstants.PROFILE_PERCENTAGE_VALUE).limitToFirst(10);//.child(FirebaseConstants.PROFILE_PERCENTAGE_VALUE).orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        LogWrapper.out(getActivity().getApplicationContext(),
                                issue.toString());
                    }
                    LogWrapper.out(getActivity().getApplicationContext(), "nothing1");
                }
                LogWrapper.out(getActivity().getApplicationContext(), "nothing");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                LogWrapper.out(getActivity().getApplicationContext(), databaseError.getMessage());

            }
        });
    }


}
