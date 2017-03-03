package com.ayush.gsim.HowItDone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayush.gsim.Activities.LauncherActivity;
import com.ayush.gsim.R;

public class HowItsDoneDetailsFragment extends Fragment {
    TextView text;
    TextView view1;
    String title;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer = false;
    private boolean mFromSavedInstanceState = false;
    public static final String TAG = HowItsDoneDetailsFragment.class.getSimpleName();


    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    String username;
    String pass;
    String cokie;

    public static HowItsDoneDetailsFragment newInstance() {
        return new HowItsDoneDetailsFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        //Toast.makeText(getActivity(),cokie,3000).show();
        View view;
        view = inflater.inflate(R.layout.how_it_done_fragment, container, false);

        view1 = (TextView) view.findViewById(R.id.textView1);
        view1.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), "robotothin.ttf"));

view1.setText(Html.fromHtml("Although the Decoding done previously from the source code is hidden but still there exists few browsers where the decoding is possible such as Safari 1.0\n" +
        "Chrome 1.0 - 2.0 etc. Visit <a href=\"122.160.168.157/isimgc/login\">122.160.168.157/isimgc/login</a>\" for any doubts" ));
        return view;

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