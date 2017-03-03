package com.ayush.gsim.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.gsim.Activities.LauncherActivity;
import com.ayush.gsim.Activities.LoginActivity;
import com.ayush.gsim.FragmentDrawer;
import com.ayush.gsim.ImageCustoms.ImageCustomDetailsHomeScreenRecycler;
import com.ayush.gsim.R;
import com.ayush.gsim.UploadImage;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;
import com.ayush.gsim.Utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailFragment extends Fragment {
    public static TextView view1;
    String title;
    public static final String TAG = DetailFragment.class.getSimpleName();
    public static TextView welcome;


    private String userName, passWord, sessionCookie, menu;
    GridView gridLayout;
    private List<Integer> homeImageList = new ArrayList<>();
    private RecyclerView recyclerViewAttendance, recyclerViewErp, recyclerViewAdditional;
    private ImageCustomDetailsHomeScreenRecycler mAdapter;
    private View view;
    public static ImageView imageViewProfile;
    private int PICK_IMAGE_REQUEST = 1, CROP_IMAGE_REQUEST = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private TextView attendanceMoreTextView;
    private String studentName;


    public static DetailFragment newInstance() {
        return new DetailFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {


        userName = getArguments().getString(Constants.USERNAME_INTENT);
        passWord = getArguments().getString(Constants.PASSWORD_INTENT);
        sessionCookie = getArguments().getString(Constants.COOKIE_INTENT);
        studentName = getArguments().getString(Constants.STUDENT_NAME_INTENT);

        view = inflater.inflate(R.layout.details_fragment_new, container, false);

        view1 = (TextView) view.findViewById(R.id.textView1);
        gridLayout = (GridView) view.findViewById(R.id.gridView);
        welcome = (TextView) view.findViewById(R.id.welcome);
        view1.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), Constants.FONT_ROBOTO_THIN));
        imageViewProfile = (ImageView) view.findViewById(R.id.profileImage);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForPermission();
            }
        });
        attendanceMoreTextView = (TextView) view.findViewById(R.id.attendanceMoreTextView);
//        UploadImage uploadImage = new UploadImage();
//        uploadImage.getFromFireBase(getActivity().getApplicationContext(), userName);
        Integer[] homeImages = {R.drawable.house
                , R.drawable.newspaper
                , R.drawable.lecture
                , R.drawable.logout
                , R.drawable.chat
                , R.drawable.archive
        };


        recyclerViewAttendance = (RecyclerView) view.findViewById(R.id.recycler_view_attendance);
        mAdapter = new ImageCustomDetailsHomeScreenRecycler(getActivity(),
                getFragmentManager(), Arrays.asList(Constants.attendanceImages),
                Arrays.asList(Constants.attendanceValues),
                userName, passWord, sessionCookie,
                Constants.HOME_PAGE_LIST_TYPE_ATTENDANCE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAttendance.setLayoutManager(mLayoutManager);
        recyclerViewAttendance.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAttendance.setAdapter(mAdapter);

        recyclerViewErp = (RecyclerView) view.findViewById(R.id.recycler_view_erp);
        mAdapter = new ImageCustomDetailsHomeScreenRecycler(getActivity(), getFragmentManager(), Arrays.asList(Constants.erpImages),
                Arrays.asList(Constants.erpValues),
                userName, passWord, sessionCookie,
                Constants.HOME_PAGE_LIST_TYPE_ERP);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewErp.setLayoutManager(mLayoutManager1);
        recyclerViewErp.setItemAnimator(new DefaultItemAnimator());
        recyclerViewErp.setAdapter(mAdapter);

        recyclerViewAdditional = (RecyclerView) view.findViewById(R.id.recycler_view_additional);
        mAdapter = new ImageCustomDetailsHomeScreenRecycler(getActivity(), getFragmentManager(), Arrays.asList(Constants.optionImages),
                Arrays.asList(Constants.optionsValues),
                userName, passWord, sessionCookie,
                Constants.HOME_PAGE_LIST_TYPE_OPTIONS);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAdditional.setLayoutManager(mLayoutManager2);
        recyclerViewAdditional.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdditional.setAdapter(mAdapter);
        attendanceMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attendanceMoreTextView.getText().equals(getActivity().getString(R.string.more))) {
                    mAdapter = new ImageCustomDetailsHomeScreenRecycler(getActivity(),
                            getFragmentManager(), Arrays.asList(Constants.attendanceImages),
                            Arrays.asList(Constants.attendanceValues),
                            userName, passWord, sessionCookie,
                            Constants.HOME_PAGE_LIST_TYPE_ATTENDANCE);
                    GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                    recyclerViewAttendance.setLayoutManager(mLayoutManager);
                    recyclerViewAttendance.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewAttendance.setAdapter(mAdapter);
                    attendanceMoreTextView.setText(getString(R.string.less));
                } else {
                    mAdapter = new ImageCustomDetailsHomeScreenRecycler(getActivity(),
                            getFragmentManager(), Arrays.asList(Constants.attendanceImages),
                            Arrays.asList(Constants.attendanceValues),
                            userName, passWord, sessionCookie,
                            Constants.HOME_PAGE_LIST_TYPE_ATTENDANCE);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewAttendance.setLayoutManager(mLayoutManager);
                    recyclerViewAttendance.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewAttendance.setAdapter(mAdapter);
                    attendanceMoreTextView.setText(getString(R.string.more));

                }
            }
        });
        try {
            view1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_slide_in));
            view1.setText(studentName);
            view1.setGravity(Gravity.CENTER);
            welcome.setVisibility(View.VISIBLE);
            FragmentDrawer.studentNameTextView.setText(studentName);
            FragmentDrawer.userNameTextView.setText(userName);
        } catch (Exception e) {
            LogWrapper.printStackTrace(getActivity().getApplicationContext(), e);
            Utils.sessionExpired(getActivity());
        }
//        ImageCustomDetailsHomeScreen customList = new ImageCustomDetailsHomeScreen(getActivity(), homeImages, homeValues);
//        gridLayout.setAdapter(customList);
//        recyclerView.(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                navigateTo(position);
//            }
//        });
//        gridLayout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),homeValues[position].toUpperCase(),Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });

//        Details details = new Details(getActivity(), userName, passWord, sessionCookie);
//        details.execute();

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


    void checkForPermission() {

        int permissionReadExternalStorage = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissione = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissione != PackageManager.PERMISSION_GRANTED
                || permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            toDoTask();
        }

    }

    void toDoTask() {

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            // performCrop(uri);
            Uri uri = data.getData();
            UploadImage uploadImage = new UploadImage(getActivity(), uri, userName);
            uploadImage.execute();

        } else if (requestCode == CROP_IMAGE_REQUEST) {
            Uri uri = data.getData();
            UploadImage uploadImage = new UploadImage(getActivity(), uri, userName);
            uploadImage.execute();
        }

    }

    private void performCrop(Uri picUri) {
        try {
        }
        // respond to users whose devices do not support the crop action
        catch (Exception e) {
            // display an error message
            e.printStackTrace();
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toDoTask();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.app_name),
                            Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    //no use
    private class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        String checkSession = "";

        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            // String username=e1.getText().toString();
            //String pass=e2.getText().toString();


            //Document doc;
            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlTimeTable = "http://122.160.168.157/iSIMGC/Student/TimeTable";
                Connection.Response response = Jsoup.connect(urlCourse)
                        .cookie("ASP.NET_SessionId", sessionCookie)
                        .method(Connection.Method.GET).timeout(0)
                        .execute();


                Document loginPage = response.parse();

//               Element lastFocus = 		loginPage.select("input[name=__LASTFOCUS]").first();
//                Element eventTarget = 		loginPage.select("input[name=__EVENTTARGET]").first();
//                Element eventArgument = 		loginPage.select("input[name=__EVENTARGUMENT]").first();
//                Element vKey = 		loginPage.select("input[name=__VSKEY]").first();
//                Element viewState = 		loginPage.select("input[name=__VIEWSTATE]").first();
//                Element eventValidation = 	loginPage.select("input[name=__EVENTVALIDATION]").first();
//                response = Jsoup.connect(urlCourse)
//                        .cookie("ASP.NET_SessionId", sessionCookie)
//                        .data("__LASTFOCUS","")// lastFocus.attr("value")+"")
//                        .data("__EVENTTARGET","")// eventTarget.attr("value")+"")
//                        .data("__EVENTARGUMENT", "")//eventArgument.attr("value")+"")
//                        .data("__VSKEY", vKey.attr("value")+"")
//                        .data("__VIEWSTATE","")// viewState.attr("value")+"")
//                        .data("__EVENTVALIDATION", eventValidation.attr("value")+"")
//
//                        .method(Connection.Method.GET)
//                        .timeout(0)
//                        .execute();

                Document doc2 = response.parse();
                checkSession = doc2.title();
                title = doc2.title();
                Element name = doc2.select("label[id=MCPH1_SCPH_lblName]").first();
                title = name.text();
                System.out.println(title);
                return title;

            } catch (Exception e) {
                e.printStackTrace();
                title = "Error In Login";

                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {//

            try {
                if (checkSession.equals("SIM Login")) {
                    Toast.makeText(getActivity(), "Session Expired", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    //overridePendingTransition(R.anim.right_slide_out, R.anim.right_slide_in);
                    getActivity().overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);

                    getActivity().finish();

                }

//                gridLayout.setVisibility(View.VISIBLE);
//                gridLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_slide_in));
                view1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.right_slide_in));
                view1.setText(title);
                view1.setGravity(Gravity.CENTER);
                welcome.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                ErrorDialog();
            }

        }
    }

}