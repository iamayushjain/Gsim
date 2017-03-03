package com.ayush.gsim.NewsFeed;


import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ayush.gsim.R;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewsFeedFragment extends Fragment {
    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    View view1;
    private static final String FIREBASE_URL = "https://news-gcet.firebaseio.com";

    private String mUsername;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;
    private ValueEventListener valueEventListener;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

        view1 = inflater.inflate(R.layout.newsfeed_mainlayout, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.NEWS_FEED).child("chat");

        return view1;
    }

    void init() {
        mRecyclerView = (RecyclerView) view1.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    public ListView listView;

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        listView = (ListView) view1.findViewById(R.id.list);
        try { // Tell our list adapter that we only want 50 messages at a time
            mChatListAdapter = new ChatListAdapter(databaseReferenceGsim.limitToFirst(50).orderByChild("time" +
                    ""), getActivity(), R.layout.imagecustom_news_feed, mUsername);
            listView.setAdapter(mChatListAdapter);
            //listView.setSelection(0);

//        mAdapter = new MyRecyclerViewAdapter(mFirebaseRef.limit(50).orderByChild("time" +
//                "");
//        mRecyclerView.setAdapter(mAdapter);

            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(0);
                }
            });

            // Finally, a little indication of connection status
            mConnectedListener = databaseReferenceGsim.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
                        System.out.println("Output" + dataSnapshot.getValue());
                    } else {
                        Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    // No-op
                }
            });
        } catch (Exception e) {
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        databaseReferenceGsim.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    SharedPreferences prefs;

}
