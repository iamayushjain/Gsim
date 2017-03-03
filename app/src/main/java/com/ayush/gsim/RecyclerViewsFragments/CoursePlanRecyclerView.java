package com.ayush.gsim.RecyclerViewsFragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayush.gsim.R;

import java.util.List;

public class CoursePlanRecyclerView extends RecyclerView.Adapter<CoursePlanRecyclerView.MyViewHolder> {

    private String type;
    private String userName, passWord, sessionCookie;
    private Activity activity;
    private FragmentManager fragmentManager;
    private List<String> coursePlanUnits, coursePlanTopics, coursePlanDocumentName, coursePlanUploadedBy, coursePlanUploadedOn, coursePlanContent;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTopic, textViewBy, textViewDocumentName;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);

            textViewTopic = (TextView) view.findViewById(R.id.topicTextView);
            textViewBy = (TextView) view.findViewById(R.id.uploadedBy);
            textViewDocumentName = (TextView) view.findViewById(R.id.documentNameTextView);
            cardView = (CardView) view.findViewById(R.id.card_view2);
        }
    }


    public CoursePlanRecyclerView(Activity activity,
                                  List<String> coursePlanUnits, List<String> coursePlanTopics, List<String> coursePlanDocumentName,
                                  List<String> coursePlanUploadedBy, List<String> coursePlanUploadedOn, List<String> coursePlanContent) {
        this.activity = activity;
        this.coursePlanUnits = coursePlanUnits;
        this.coursePlanTopics = coursePlanTopics;
        this.coursePlanDocumentName = coursePlanDocumentName;
        this.coursePlanUploadedBy = coursePlanUploadedBy;
        this.coursePlanUploadedOn = coursePlanUploadedOn;
        this.coursePlanContent = coursePlanContent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_plan_custom_recycler, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textViewTopic.setText(coursePlanUnits.get(position) + ": " + coursePlanTopics.get(position));
        if (coursePlanUploadedBy.get(position) == null || coursePlanUploadedBy.get(position).equals("")) {
            holder.textViewBy.setVisibility(View.GONE);
        } else {
            holder.textViewBy.setVisibility(View.VISIBLE);
            holder.textViewBy.setText(coursePlanUploadedBy.get(position) + " on " + coursePlanUploadedOn.get(position));
        }
        if (coursePlanDocumentName.get(position) == null || coursePlanDocumentName.get(position).equals("")) {
            holder.cardView.setVisibility(View.GONE);
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.textViewDocumentName.setText(coursePlanDocumentName.get(position).toUpperCase());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://122.160.168.157" + coursePlanContent.get(position)));
                    activity.startActivity(browserIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return coursePlanUnits.size();
    }

}