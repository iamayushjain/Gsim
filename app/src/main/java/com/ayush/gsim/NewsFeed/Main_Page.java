package com.ayush.gsim.NewsFeed;


import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.gsim.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Main_Page extends Fragment {
    public static Main_Page newInstance() {
        return new Main_Page();
    }

    View view1;
    private static final String FIREBASE_URL = "https://news-gcet.firebaseio.com";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        //setContentView(R.layout.activity_my_main);
        view1 = inflater.inflate(R.layout.newsfeed_mainlayout, container, false);


        // Make sure we have a mUsername

//		setupUsername();
        init();


        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");
        //sendMessage();
        // Setup our input methods. Enter key on the keyboard or pushing the send button


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
            mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50).orderByChild("time" +
                    ""), getActivity(), R.layout.card_view, mUsername);
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
            mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // No-op
                }
            });
        } catch (Exception e) {
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    SharedPreferences prefs;

    private void setupUsername() {
        prefs = getActivity().getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);

//            Random r = new Random();
//            // Assign a random user name if we don't have one saved.
//
//            prefs.edit().putString("username", mUsername).commit();
//        }
        Firebase myFirebaseRef1 = new Firebase(FIREBASE_URL).child("name");

        myFirebaseRef1.child("drama").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUsername = snapshot.getValue().toString();
                prefs.edit().putString("username", mUsername).commit();
                //prints "Do you have data? You'll love Firebase."
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }
}
