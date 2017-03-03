package com.ayush.gsim.Activities.BranchWIseChat;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
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

public class MainActivity extends ListActivity {

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://news-gcet.firebaseio.com";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);


        setContentView(R.layout.newsfeed_mainlayout);

        // Make sure we have a mUsername

        setupUsername();
        init();

        setTitle("Chatting as " + mUsername);

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");
        //sendMessage();
        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    public ListView listView;

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        listView = getListView();
        try { // Tell our list adapter that we only want 50 messages at a time
            mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50).orderByChild("time" +
                    ""), this, R.layout.card_view, mUsername);
            listView.setAdapter(mChatListAdapter);

//        mAdapter = new MyRecyclerViewAdapter(mFirebaseRef.limit(50).orderByChild("time" +
//                "");
//        mRecyclerView.setAdapter(mAdapter);

            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(mChatListAdapter.getCount() - 1);
                }
            });

            // Finally, a little indication of connection status
            mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = (Boolean) dataSnapshot.getValue();
                    if (connected) {
                        Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
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
        prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
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

    private void sendMessage() {
        Bundle b = getIntent().getExtras();
        String message = b.getString("Message");
        String url = b.getString("URL");
        String phone = b.getString("Phone");
        String imageUrl = b.getString("imageUrl");
String authorUrl=b.getString("authorUrl");
        System.out.println(message+"."+url+","+phone);
        if (!message.equals("")) {
            // Create our 'model', a Chat object
            long time = -1 * System.currentTimeMillis();
            Chat chat = new Chat(message, mUsername, time, url, phone,imageUrl,authorUrl);
            // Create a new, auto-generated child of that chat location, and save our chat data there

            mFirebaseRef.push().setValue(chat);

            Toast.makeText(getApplicationContext(), "News Uploaded", Toast.LENGTH_LONG).show();
        }
    }
}
