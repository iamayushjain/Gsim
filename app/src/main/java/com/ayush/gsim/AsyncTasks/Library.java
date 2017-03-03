package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ayush.gsim.Fragments.LibraryFragment;
import com.ayush.gsim.R;
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
public class Library extends AsyncTask<Void, Void, String> {

    private String userName, passWord, sessionCookie;
    private ProgressDialog mProgressDialog;
    private Activity activity;
    private FragmentManager fragmentManager;

    private String totalPaid = null;
    private String totalBalance = null;
    private String totalWaived = null;
    private String totalFine = null;
    private List<String> bookIssueTitle;
    private List<String> bookIssueDate;
    private List<String> bookDueDate;
    private List<String> bookReturnDate;

    public Library(Activity activity, String userName, String passWord, String cookie, FragmentManager fragmentManager) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.sessionCookie = cookie;
        this.fragmentManager = fragmentManager;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(activity.getResources().getString(R.string.library_progress_dialog));
        mProgressDialog.setMessage(activity.getResources().getString(R.string.loading_progress_dialog));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        bookIssueTitle = new ArrayList<>();
        bookIssueDate = new ArrayList<>();
        bookDueDate = new ArrayList<>();
        bookReturnDate = new ArrayList<>();
    }

    @Override
    protected String doInBackground(Void... params) {


        try {
            String urlLibrary = Constants.URL_LIBRARY;

            Document libraryPage = Jsoup.connect(urlLibrary)
                    .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                    .get();

            Element lastFocus = libraryPage.select(Constants.INPUT_LAST_FOCUS).first();
            Element eventTarget = libraryPage.select(Constants.INPUT_EVENT_TARGET).first();
            Element eventArgument = libraryPage.select(Constants.INPUT_EVENT_ARGUMENT).first();
            Element vsKey = libraryPage.select(Constants.INPUT_VS_KEY).first();
            Element viewState = libraryPage.select(Constants.INPUT_VIEW_STATE).first();
            Element eventValidation = libraryPage.select(Constants.INPUT_EVENT_VALIDATION).first();

            Connection.Response responseLibraryPage = Jsoup.connect(urlLibrary)
                    .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                    .data(Constants.STRING_LAST_FOCUS, "")
                    .data(Constants.STRING_EVENT_TARGET, "")
                    .data(Constants.STRING_EVENT_ARGUMENT, "")
                    .data(Constants.STRING_VS_KEY, vsKey.attr("value") + "")
                    .data(Constants.STRING_VIEW_STATE, "")
                    .data(Constants.STRING_EVENT_VALIDATION, eventValidation.attr("value") + "")
                    .data("ctl00$ctl00$MCPH1$SCPH$btnIssue", "Issued")
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .timeout(100000)
                    .execute();

            libraryPage = responseLibraryPage.parse();

            //  Elements imports = doc2.select("td");
            //Elements rowsp=doc2.select("td[rowspan]");
            //Log.v(TAG, "HElooooo");
            Elements impo = libraryPage.select("table.top-heading");
            Elements count = impo.select("tr.GrdAltRow");
            Element bookIssueTitleElement;
            Element bookIssueDateElement;
            Element bookDueDateElement;
            Element bookReturnDateElement;

            Element totalFineElement = libraryPage.select("label[id=MCPH1_SCPH_lblttlfine]").first();
            Element totalPaidElement = libraryPage.select("label[id=MCPH1_SCPH_lblpaid]").first();
            Element totalBalanceElement = libraryPage.select("label[id=MCPH1_SCPH_lblbal]").first();
            Element totalWaivedElement = libraryPage.select("label[id=MCPH1_SCPH_lblWaived]").first();
            totalFine = totalFineElement.text();
            totalPaid = totalPaidElement.text();
            totalBalance = totalBalanceElement.text();
            totalWaived = totalWaivedElement.text();

            for (int i = 0; i < count.size(); i++) {
                bookIssueTitleElement = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtTitle_" + i + "]").first();
                bookIssueDateElement = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtIssueDate_" + i + "]").first();
                bookDueDateElement = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtDueDate_" + i + "]").first();
                bookReturnDateElement = impo.select("span[id=MCPH1_SCPH_GVIssueReturn_txtReturnDate_" + i + "]").first();

                bookIssueTitle.add(bookIssueTitleElement.text());
                bookIssueDate.add(bookIssueDateElement.text());
                bookDueDate.add(bookDueDateElement.text());
                bookReturnDate.add(bookReturnDateElement.text());

            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {//
        try {
            mProgressDialog.dismiss();
            Bundle arg = new Bundle();
            arg.putString(Constants.USERNAME_INTENT, userName);
            arg.putString(Constants.PASSWORD_INTENT, passWord);
            arg.putString(Constants.COOKIE_INTENT, sessionCookie);
            arg.putString("Total_fine", totalFine);
            arg.putString("Total_due", totalBalance);
            arg.putString("Total_paided", totalPaid);
            arg.putString("Total_waived", totalWaived);

            arg.putStringArray("Array", bookIssueTitle.toArray(new String[bookIssueTitle.size()]));
            arg.putStringArray("Array1", bookIssueDate.toArray(new String[bookIssueDate.size()]));
            arg.putStringArray("Array2", bookDueDate.toArray(new String[bookDueDate.size()]));
            arg.putStringArray("Array3", bookReturnDate.toArray(new String[bookReturnDate.size()]));

            LibraryFragment tb1 = new LibraryFragment();
            tb1.setArguments(arg);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, tb1, LibraryFragment.TAG).commit();

        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
    }


}
