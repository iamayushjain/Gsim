package com.ayush.gsim.ImageCustoms;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayush.gsim.R;

public class ImageCustomAttendanceTotal extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final String[] web1;
    private final String[] web2;
    public ImageButton refreshButton;
    public View rowView;

    public ImageCustomAttendanceTotal(Activity context,
                                      String[] web, String[] web1, String[] web2) {
        super(context, R.layout.imagecustom_attendancetotal, web);
        this.context = context;
        this.web = web;
        this.web1 = web1;
        this.web2 = web2;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.imagecustom_attendancetotal, null, true);
        int color_id[] = {Color.DKGRAY, Color.WHITE};
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
        ImageView iv = (ImageView) rowView.findViewById(R.id.listImage);
        int pos = randomWithRange(0, 1);
//iv.setBackgroundColor(color_id[pos]);

        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.textView2);
        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.textView4);
//ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
        txtTitle.append(web[position]);
        txtTitle1.append(web1[position]);
        txtTitle2.append(web2[position]);

        txtTitle.setTypeface(Typeface
                .createFromAsset(context.getAssets(), "robotothin.ttf"));
        txtTitle1.setTypeface(Typeface
                .createFromAsset(context.getAssets(), "robotothin.ttf"));
        txtTitle2.setTypeface(Typeface
                .createFromAsset(context.getAssets(), "robotothin.ttf"));


        txtTitle.setSelected(true);
//tv.setSelected(true);
        txtTitle.setEllipsize(TruncateAt.MARQUEE);
        txtTitle.setSingleLine(true);
//txtTitle1.setWidth(.getLayoutParams().width/2);
        txtTitle1.setSelected(true);
//tv.setSelected(true);
        txtTitle1.setEllipsize(TruncateAt.MARQUEE);
        txtTitle1.setSingleLine(true);
        txtTitle.setGravity(Gravity.CENTER);
        txtTitle1.setGravity(Gravity.CENTER);
        txtTitle2.setGravity(Gravity.CENTER);


        return rowView;
    }

    int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }


}

