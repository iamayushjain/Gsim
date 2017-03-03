package com.ayush.gsim.BranchWiseChat;


import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BranchWiseChatFragment extends Fragment {
    public ListView listView;
    View view1;
    String username, pass, sessionCookie, branch, name;
    EditText editText;
    FloatingActionButton sendButton;
    CardView cardView;
    SharedPreferences prefs;
    private String mUsername, mId;
    private ChatListAdapter mChatListAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGsim;
    private ValueEventListener valueEventListener;

    public static BranchWiseChatFragment newInstance() {
        return new BranchWiseChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        //setContentView(R.layout.login_activity);
        view1 = inflater.inflate(R.layout.chatapp_mainlayout, container, false);
        username = getArguments().getString(Constants.USERNAME_INTENT);
        pass = getArguments().getString(Constants.PASSWORD_INTENT);
        sessionCookie = getArguments().getString(Constants.COOKIE_INTENT);
        name = getArguments().getString(Constants.STUDENT_NAME_INTENT);
        branch = getArguments().getString(Constants.BRANCH_INTENT);
        mId = username;
        mUsername = name;
        init();
        // Make sure we have a mUsername

//		setupUsername();
        // init();


        // Setup our Firebase mFirebaseRef

        //sendMessage();
        // Setup our input methods. Enter key on the keyboard or pushing the send button


        return view1;
    }

    void init() {
//        mRecyclerView = (RecyclerView) view1.findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
        cardView = (CardView) view1.findViewById(R.id.card_view);
        editText = (EditText) view1.findViewById(R.id.messageInput);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceGsim = firebaseDatabase.getReference(FirebaseConstants.BRANCH_WISE_CHAT).child("chat").child(branch);

        editText.bringToFront();
        editText.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s != null) {

                    cardView.setAlpha(1);
                } else {
                    cardView.setAlpha(0.7f);
                }
            }
        });

        ((TextView) view1.findViewById(R.id.textView10)).setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), "robotothin.ttf"));

        ((TextView) view1.findViewById(R.id.textView11)).setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
        sendButton = (FloatingActionButton) view1.findViewById(R.id.sendButton);
        ((LinearLayout) view1.findViewById(R.id.listFooter)).setBackgroundColor(Color.TRANSPARENT);
        ((LinearLayout) view1.findViewById(R.id.listFooter)).bringToFront();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString().trim();
                if (message.trim() != null && !message.equals(""))
                    sendMessage(message.trim(), mId, mUsername);
            }
        });
        sendButton.setBackgroundColor(Color.TRANSPARENT);
        //  editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        listView = (ListView) view1.findViewById(R.id.list);
        try { // Tell our list adapter that we only want 50 messages at a time
            mChatListAdapter = new ChatListAdapter(databaseReferenceGsim.orderByChild("time" +
                    ""), getActivity(), R.layout.imagecustom_branch_chat, mId);
            listView.setAdapter(mChatListAdapter);
            System.out.print("Output:" + mChatListAdapter + "knj" + mChatListAdapter.getCount());


            // listView.setSelection(0);

//        mAdapter = new MyRecyclerViewAdapter(mFirebaseRef.limit(50).orderByChild("time" +
//                "");
//        mRecyclerView.setAdapter(mAdapter);

            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    //   listView.setSelection(0);
                    if (mChatListAdapter.getCount() > 3) {
                        ((RelativeLayout) view1.findViewById(R.id.archieveIntroRelativeLayout)).setVisibility(View.GONE);
                    }
                    listView.setSelection(mChatListAdapter.getCount() - 1);
                }
            });

            // Finally, a little indication of connection status
            valueEventListener = databaseReferenceGsim.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
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
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    System.out.println("Firebase Error");

                    // ...
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReferenceGsim.getRoot().child(".info/connected").removeEventListener(valueEventListener);
        mChatListAdapter.cleanup();
    }

//    private void setupUsername() {
//        prefs = getActivity().getApplication().getSharedPreferences("ChatPrefs", 0);
//        mUsername = prefs.getString("username", null);
//
////            Random r = new Random();
////            // Assign a random user name if we don't have one saved.
////
////            prefs.edit().putString("username", mUsername).commit();
////        }
//        Firebase myFirebaseRef1 = new Firebase(FIREBASE_URL).child("name");
//
//        myFirebaseRef1.child("drama").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                mUsername = snapshot.getValue().toString();
//                prefs.edit().putString("username", mUsername).commit();
//                //prints "Do you have data? You'll love Firebase."
//            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {
//            }
//
//        });
//    }

    private void sendMessage(String message, String id, String username) {
        //EditText inputText = (EditText) findViewById(R.id.messageInput);
        long time = System.currentTimeMillis();
        if (!message.equals("") && !id.equals("") && !username.equals("")) {
            // Create our 'model', a ArchivePad object
            Chat chat = new Chat(message, mId, mUsername, time);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            databaseReferenceGsim.push().setValue(chat);
            editText.setText("");
            cardView.setAlpha(0.7f);
        }
    }
}
