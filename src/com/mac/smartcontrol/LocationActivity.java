package com.mac.smartcontrol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.mac.smartcontrol.util.SaveLocationUtil;

public class LocationActivity extends Activity {

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	int location_distance = 0;
	String location_longitude = "0";
	String location_latitude = "0";
	String location_address = "";
	BDLocation bdLocation = null;
	boolean location_isOpen = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("进来了");
		setContentView(R.layout.activity_location);
		ImageView postion_Iv = (ImageView) findViewById(R.id.position_iv);
		ImageView save_Iv = (ImageView) findViewById(R.id.save_iv);
		final TextView position_name_Tv = (TextView) findViewById(R.id.position_name_et);
		final TextView position_distance_Et = (TextView) findViewById(R.id.distance_et);
		sharedPreferences = getSharedPreferences("location", 0);
		if (sharedPreferences != null) {
			editor = sharedPreferences.edit();
			location_distance = sharedPreferences.getInt("location_distance",
					1000);
			location_longitude = sharedPreferences.getString(
					"location_longitude", "0");
			location_latitude = sharedPreferences.getString(
					"location_latitude", "0");
			location_address = sharedPreferences.getString("location_address",
					"");
			location_isOpen = sharedPreferences.getBoolean("location_isOpen",
					true);
			position_name_Tv.setText(location_address);
			position_distance_Et.setText(location_distance + "");
		}
		final ImageView remeber_Iv = (ImageView) findViewById(R.id.remeber_iv);
		LinearLayout remeber_ll = (LinearLayout) findViewById(R.id.remeber_ll);
		remeber_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				location_isOpen = (!location_isOpen);
				if (location_isOpen) {
					remeber_Iv.setImageResource(R.drawable.checkbox_bg_focus);
				} else {
					remeber_Iv.setImageResource(R.drawable.checkbox_bg);
				}
			}
		});

		postion_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bdLocation = SaveLocationUtil.getBdLocation();
				if (bdLocation == null) {
					position_name_Tv.setText("正在获取定位信息……");
				} else {
					position_name_Tv.setText(bdLocation.getAddrStr());
					location_longitude = bdLocation.getLongitude() + "";
					location_latitude = bdLocation.getLatitude() + "";
					location_address = bdLocation.getAddrStr();
				}
			}
		});

		save_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editor.putInt("location_distance", location_distance);
				editor.putString("location_longitude", location_longitude);
				editor.putString("location_latitude", location_latitude);
				editor.putString("location_address", location_address);
				editor.putBoolean("location_isOpen", location_isOpen);

				editor.commit();
			}
		});

	}

}
