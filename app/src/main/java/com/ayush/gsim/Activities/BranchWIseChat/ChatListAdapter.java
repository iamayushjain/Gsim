package com.ayush.gsim.Activities.BranchWIseChat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayush.gsim.R;
import com.firebase.client.Query;

/**
 * @author greg
 * @since 6/21/13
 * <p/>
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    Activity activity;
    ImageView urlImageView;
    View view1;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


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
        view1 = view;
        TextView authorText = (TextView) view.findViewById(R.id.textView);
        urlImageView = (ImageView) view.findViewById(R.id.imageView1);
//        authorText.setTypeface(Typeface
//                .createFromAsset(activity.getAssets(), "robotothin.ttf"));

        authorText.setText(Html.fromHtml(author) + ": ");
        final String authorUrl = chat.getAuthorUrl();
        authorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = getOpenFacebookIntent(authorUrl);
                activity.startActivity(browserIntent);

            }
        });
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(activity.getResources().getColor(R.color.primaryColor));
        } else {
            authorText.setTextColor(activity.getResources().getColor(R.color.primaryColor));
        }
        //   Url = chat.getAuthor().toString();

        TextView messageText = ((TextView) view.findViewById(R.id.textView2));
        messageText.setTypeface(Typeface
                .createFromAsset(activity.getAssets(), "robotothin.ttf"));
        try {
            messageText.setText(Html.fromHtml(checkFacebookIntent(chat.getMessage())));
            messageText.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {

        }
        //  MainActivity m=new MainActivity();
        ImageButton imageButton1 = (ImageButton) view.findViewById(R.id.imageButton);
        ImageButton imageButton2 = (ImageButton) view.findViewById(R.id.imageButton2);
        imageButton1.setBackgroundColor(Color.TRANSPARENT);
        imageButton2.setBackgroundColor(Color.TRANSPARENT);
        final String url = chat.getUrl();
        final String phone = chat.getPhone();
        imageButton1.setVisibility(View.VISIBLE);
        imageButton2.setVisibility(View.VISIBLE);
        imageButton1.setEnabled(true);
        imageButton2.setEnabled(true);
        imageButton1.setImageResource(R.drawable.web);
        imageButton1.setColorFilter(activity.getResources().getColor(R.color.primaryColor));
        imageButton2.setImageResource(R.drawable.telephone);
        imageButton2.setColorFilter(activity.getResources().getColor(R.color.primaryColor));
        if (url.equals("null")) {
            imageButton1.setEnabled(false);
            imageButton1.setVisibility(View.GONE);
            Log.e("Visibility Gone url", url + messageText.getText().toString());
            //imageButton1.setColorFilter(Color.LTGRAY);
        }
        if (phone.equals("null")) {
            imageButton2.setEnabled(false);
            Log.e("Visibility Gone phone", phone + messageText.getText().toString());
            imageButton2.setVisibility(View.GONE);
            //imageButton2.setColorFilter(Color.LTGRAY);
        }


        System.out.println(url + phone);

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = getOpenFacebookIntent(url);
                activity.startActivity(browserIntent);

            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("calling", phone);

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                activity.startActivity(intent);
            }
        });
        String imageUrl = chat.getImageUrl();
        final Bitmap decodedByte;
        if (!imageUrl.equals("1")) {

            try {
                System.out.println("imageUrl" + imageUrl);
                byte[] decodedString = Base64.decode(imageUrl, Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                urlImageView.setVisibility(View.VISIBLE);
                urlImageView.setImageBitmap(decodedByte);
//                ImageView expandedImageView = (ImageView) view.findViewById(
//                        R.id.zoomimageView1);
//                expandedImageView.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        mShortAnimationDuration = activity.getResources().getInteger(
//                android.R.integer.config_shortAnimTime);


    }

    public Intent getOpenFacebookIntent(String url) {

        try {

            activity.getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + url));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
    }

    public String checkFacebookIntent(String url) {

        try {

            activity.getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            System.out.print(url);
            return url;//new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        } catch (Exception e) {
            return url.replace("fb://facewebmodal/f?href=", "");//new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
    }

    public void zoomImageFromThumb(final View thumbView, Bitmap imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) view1.findViewById(
                R.id.zoomimageView1);
        expandedImageView.setImageBitmap(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        view1.findViewById(R.id.relativeLayout)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


}
