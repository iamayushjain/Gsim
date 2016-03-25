package com.ayush.gsim;



import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class imagecustom_subjectattendance extends ArrayAdapter<String>{
private final Activity context;
private final String[] web;
private final String[] web1;
private final String[] web2;
private final String[] web3;
    private final String[] web4;
//private final Integer[] imageId;
public imagecustom_subjectattendance(Activity context,
                                     String[] web, String[] web1, String[] web2, String[] web3, String[] web4) {
super(context, R.layout.imagelayout, web);
this.context = context;
this.web = web;
this.web1 = web1;
this.web2 = web2;
this.web3 = web3;//this.imageId = imageId;
    this.web4 = web4;
}
@Override
public View getView(int position, View view, ViewGroup parent) {
    LayoutInflater inflater = context.getLayoutInflater();
    View rowView = inflater.inflate(R.layout.imagelayout_sub, null, true);
    int color_id[] = {Color.CYAN, Color.DKGRAY, Color.WHITE, Color.MAGENTA, Color.YELLOW};
    TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
    ImageView iv = (ImageView) rowView.findViewById(R.id.listImage);
    int pos = randomWithRange(0, 4);
//iv.setBackgroundColor(color_id[pos]);
    iv.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
    TextView txtTitle1 = (TextView) rowView.findViewById(R.id.textView2);
//ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
    TextView txtTitle2 = (TextView) rowView.findViewById(R.id.textView3);
    TextView txtTitle3 = (TextView) rowView.findViewById(R.id.textView4);
  //  TextView txtTitle4 = (TextView) rowView.findViewById(R.id.textView5);
    txtTitle.setText(web[position]);
    txtTitle1.setText("Present Classes="+web2[position]);
    txtTitle3.setText("Total Classes="+web4[position]);
    txtTitle2.setText(web1[position]+"%");
    //txtTitle4.setText("Absent Classes="+web3[position]);
   if ((Float.parseFloat(web1[position]))<75)
    {
        txtTitle2.setTextColor(context.getResources().getColor(R.color.primaryAbsent));
    }

   txtTitle.setTypeface(Typeface
           .createFromAsset(context.getAssets(), "robotothin.ttf"));
   txtTitle1.setTypeface(Typeface
           .createFromAsset(context.getAssets(), "robotothin.ttf"));
   txtTitle2.setTypeface(Typeface
           .createFromAsset(context.getAssets(), "robotothin.ttf"));
   txtTitle3.setTypeface(Typeface
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
txtTitle2.setSelected(true);
//tv.setSelected(true);
txtTitle2.setEllipsize(TruncateAt.MARQUEE);
txtTitle2.setSingleLine(true);txtTitle3.setSelected(true);
//tv.setSelected(true);
txtTitle3.setEllipsize(TruncateAt.MARQUEE);
txtTitle3.setSingleLine(true);//imageView.setImageResource(imageId[0]);
    //txtTitle4.setEllipsize(TruncateAt.MARQUEE);
   // txtTitle4.setSingleLine(true);//imageView.setImageResource(imageId[0]);
    return rowView;
}
int randomWithRange(int min, int max)
{
   int range = (max - min) + 1;     
   return (int)(Math.random() * range) + min;
}
}

