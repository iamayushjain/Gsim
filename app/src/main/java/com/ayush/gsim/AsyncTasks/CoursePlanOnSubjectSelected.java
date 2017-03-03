package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ayush.gsim.Fragments.CoursePlanFragment;
import com.ayush.gsim.RecyclerViewsFragments.CoursePlanRecyclerView;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 25/1/17.
 */
public class CoursePlanOnSubjectSelected extends AsyncTask<String, Void, String> {

    private String userName, passWord, cookie;
    private ProgressDialog mProgressDialog;
    private Activity activity;
    private FragmentManager fragmentManager;

    private List<String> coursePlanUnits, coursePlanTopics, coursePlanDocumentName, coursePlanUploadedBy, coursePlanUploadedOn, coursePlanContent;

    public CoursePlanOnSubjectSelected(Activity activity, String userName, String passWord, String cookie, FragmentManager fragmentManager) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.cookie = cookie;
        this.fragmentManager = fragmentManager;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        coursePlanContent = new ArrayList<>();
        coursePlanDocumentName = new ArrayList<>();
        coursePlanUnits = new ArrayList<>();
        coursePlanTopics = new ArrayList<>();
        coursePlanUploadedBy = new ArrayList<>();
        coursePlanUploadedOn = new ArrayList<>();

    }


    @Override
    protected String doInBackground(String... params) {


        try {
            String urlCoursePlan = Constants.URL_COURSE_PLAN;

            Document libraryPage = Jsoup.connect(urlCoursePlan)
                    .cookie(Constants.COOKIE_ASP_NET, cookie)
                    .get();

            LogWrapper.d(activity.getApplicationContext(), "page", libraryPage.location());
            LogWrapper.d(activity.getApplicationContext(), "id", "id" + params[0]);

            Element lastFocus = libraryPage.select(Constants.INPUT_LAST_FOCUS).first();
            Element eventTarget = libraryPage.select(Constants.INPUT_EVENT_TARGET).first();
            Element eventArgument = libraryPage.select(Constants.INPUT_EVENT_ARGUMENT).first();
            Element vsKey = libraryPage.select(Constants.INPUT_VS_KEY).first();
            Element viewState = libraryPage.select(Constants.INPUT_VIEW_STATE).first();
            Element eventValidation = libraryPage.select(Constants.INPUT_EVENT_VALIDATION).first();

            Element viewStateEncrypted = libraryPage.select(Constants.INPUT_VIEW_STATE_ENCRYPTED).first();
            Element studentID = libraryPage.select("input[name=ctl00$ctl00$MCPH1$SCPH$hdnStudentId]").first();
            Connection.Response responseLibraryPage = Jsoup.connect(urlCoursePlan)
                    .cookie(Constants.COOKIE_ASP_NET, cookie)
                    .data(Constants.STRING_LAST_FOCUS, "")
                    .data(Constants.STRING_EVENT_TARGET, "")
                    .data(Constants.STRING_EVENT_ARGUMENT, "")
                    .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                    .data(Constants.STRING_VIEW_STATE, "")
                    .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                    .data(Constants.STRING_VIEW_STATE_ENCRYPTED, viewStateEncrypted.attr("value") + "")
                    .data("ctl00$ctl00$txtCaseCSS", "textDefault")
                    .data("ctl00$ctl00$MCPH1$SCPH$hdnStudentId", studentID.attr("value") + "")
                    .data("ctl00$ctl00$MCPH1$SCPH$ddlSubject", params[0])
                    .data("ctl00$ctl00$MCPH1$SCPH$btnShowTopic", "Show Topic")
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .timeout(0)
                    .execute();

            libraryPage = responseLibraryPage.parse();

            //  Elements imports = doc2.select("td");
            //Elements rowsp=doc2.select("td[rowspan]");
            //Log.v(TAG, "HElooooo");
            Elements impo = libraryPage.select("table");
            Elements count = impo.select("tr.GrdAltRow");
            Element coursePlanUnitsElement, coursePlanTopicsElement, coursePlanDocumentNameElement, coursePlanUploadedByElement,
                    coursePlanUploadedOnElement, coursePlanContentElement;

            for (int i = 0; i < count.size(); i++) {
                coursePlanUnitsElement = impo.select("span[id=MCPH1_SCPH_gvTopic_txtunit_" + i + "]").first();
                coursePlanTopicsElement = impo.select("span[id=MCPH1_SCPH_gvTopic_txtTopic_" + i + "]").first();
                coursePlanDocumentNameElement = impo.select("span[id=  MCPH1_SCPH_gvTopic_txtDocName_" + i + "]").first();
                coursePlanUploadedByElement = impo.select("span[id=MCPH1_SCPH_gvTopic_txtemp_name_" + i + "]").first();
                coursePlanUploadedOnElement = impo.select("span[id=MCPH1_SCPH_gvTopic_txtattDate_" + i + "]").first();
                coursePlanContentElement = impo.select("a[id=MCPH1_SCPH_gvTopic_hypDownload_" + i + "]").first();

                coursePlanUnits.add(coursePlanUnitsElement.text());
                coursePlanTopics.add(coursePlanTopicsElement.text());
                coursePlanDocumentName.add(coursePlanDocumentNameElement.text());
                coursePlanUploadedBy.add(coursePlanUploadedByElement.text());
                coursePlanUploadedOn.add(coursePlanUploadedOnElement.text());

                if (coursePlanContentElement != null)
                    coursePlanContent.add(coursePlanContentElement.attr("href"));
                else
                    coursePlanContent.add("");


            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);

        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {//

        try {
            CoursePlanFragment.progressBar.setVisibility(View.GONE);
            RecyclerView recyclerView = CoursePlanFragment.recyclerView;
            if (coursePlanUnits.size() > 0) {
                CoursePlanRecyclerView coursePlanRecyclerView = new CoursePlanRecyclerView(activity,
                        coursePlanUnits, coursePlanTopics, coursePlanDocumentName, coursePlanUploadedBy, coursePlanUploadedOn, coursePlanContent);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(coursePlanRecyclerView);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                CoursePlanFragment.centerTextView.setText("NOTHING TO DISPLAY");
            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);

        }
    }


}
