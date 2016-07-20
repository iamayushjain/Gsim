package com.ayush.gsim.NewsFeed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ayush.gsim.R;
import com.firebase.client.Query;

/**
 * @author greg
 * @since 6/21/13
 * <p>
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    Activity activity;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
        this.activity = activity;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    String Url;

    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.textView);
//        authorText.setTypeface(Typeface
//                .createFromAsset(activity.getAssets(), "robotothin.ttf"));

        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            authorText.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
     //   Url = chat.getAuthor().toString();

        TextView messageText = ((TextView) view.findViewById(R.id.textView2));
        messageText.setTypeface(Typeface
                .createFromAsset(activity.getAssets(), "robotothin.ttf"));

        messageText.setText(chat.getMessage());
        //  MainActivity m=new MainActivity();
        ImageButton imageButton1 = (ImageButton) view.findViewById(R.id.imageButton);
        ImageButton imageButton2 = (ImageButton) view.findViewById(R.id.imageButton2);
        imageButton1.setBackgroundColor(Color.TRANSPARENT);
        imageButton2.setBackgroundColor(Color.TRANSPARENT);
        final String url = chat.getUrl();
        final String phone = chat.getPhone();
        imageButton1.setVisibility(View.VISIBLE);
        imageButton2.setVisibility(View.VISIBLE);
        imageButton1.setImageResource(R.drawable.web);
        imageButton2.setImageResource(R.drawable.telephone);
        if (url.equals("null")) {
            imageButton1.setEnabled(false);
            imageButton1.setVisibility(View.GONE);
            Log.e("Visibility Gone url",url+messageText.getText().toString());
            //imageButton1.setColorFilter(Color.LTGRAY);
        }
        if (phone.equals("null")) {
            imageButton2.setEnabled(false);
            Log.e("Visibility Gone phone",phone+messageText.getText().toString());
            imageButton2.setVisibility(View.GONE);
            //imageButton2.setColorFilter(Color.LTGRAY);
        }
        System.out.println(url + phone);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(browserIntent);

            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                activity.startActivity(intent);
            }
        });
    }
}
