package com.ayush.gsim.Ranking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yash on 8/24/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<String> generatedSubjects = new ArrayList<>();
    private String path, standardName;
    private int subject;

    public PagerAdapter(FragmentManager fm, List<String> generatedSubjects) {
        super(fm);
        this.generatedSubjects = generatedSubjects;
//        this.path = path;
//        this.standardName = standardName;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        frag = SubjectFragment.getNewInstance(generatedSubjects.get(position), path, standardName);
        return frag;
    }

    @Override
    public int getCount() {
        return generatedSubjects.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        title = generatedSubjects.get(position);
        return title;
    }
}

