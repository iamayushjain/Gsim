package com.ayush.gsim;




import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class Contact_page_Fact_dev extends Fragment{
	 public static Contact_page_Fact_dev newInstance() {
			return new Contact_page_Fact_dev();
		}
		
	 View view1;
	   
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
	    	//setContentView(R.layout.activity_my_main);
		view1 = inflater.inflate(R.layout.tryimagelayoutofdev, container, false);
		TextView t1,t2,t3;
		t1=(TextView)view1.findViewById(R.id.textView1);
		t2=(TextView)view1.findViewById(R.id.textView2);
		t3=(TextView)view1.findViewById(R.id.textView3);
		t1.setTypeface(Typeface
				.createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
		t2.setTypeface(Typeface
				.createFromAsset(getActivity().getAssets(), "robotothin.ttf"));
		t3.setTypeface(Typeface
				.createFromAsset(getActivity().getAssets(), "robotothin.ttf"));

		//view1.setBackgroundColor(Color.BLACK);
  return view1;
	}
}