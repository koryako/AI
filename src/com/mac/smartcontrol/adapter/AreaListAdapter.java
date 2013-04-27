package com.mac.smartcontrol.adapter;

import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.AreaListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Rgn_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class AreaListAdapter extends BaseAdapter {
	private Context context;
	private List<Rgn_S> areaList;
	
	public AreaListAdapter(Context context, List<Rgn_S> areaList) {
		super();
		this.context = context;
		this.areaList = areaList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return areaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return areaList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater localinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=localinflater.inflate(R.layout.area_list_item, null);
			TextView areaName_Tv=(TextView) convertView.findViewById(R.id.areaName);
			ImageView delete_Iv=(ImageView) convertView.findViewById(R.id.delete_btn);
			ImageView modify_Iv=(ImageView) convertView.findViewById(R.id.modify_btn);
			final Rgn_S rgn_S=areaList.get(position);
			areaName_Tv.setText(rgn_S.getSzName());

				delete_Iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						WriteUtil.write(MsgId_E.MSGID_RGN.getVal(),0,MsgType_E.MSGTYPE_REQ.getVal(),MsgOper_E.MSGOPER_DEL.getVal(),MsgDelReq_S.getSize(), new MsgDelReq_S(rgn_S.getUsIdx()).getMsgDelReq_S());
						((AreaListActivity)context).del_Idx=position;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Intent i=new Intent("IOException");
						context.sendBroadcast(i);	
					}
				}
			});
			modify_Iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 final Dialog dialog = new Dialog(context,R.style.MyDialog);
		                //设置它的ContentView
		            dialog.setContentView(R.layout.area_add_dialog);
		            TextView dialog_title_tv=(TextView) dialog.findViewById(R.id.dialog_title);
		            dialog_title_tv.setText(R.string.modify_area);
		            dialog.show();	
		            ImageView submit_Iv=(ImageView) dialog.findViewById(R.id.submit_iv);
		     		ImageView cancel_Iv=(ImageView)dialog. findViewById(R.id.cancel_iv);
		            final EditText name_et=(EditText) dialog.findViewById(R.id.name_et);
		            name_et.setText(rgn_S.getSzName());
		           
		     	  cancel_Iv.setOnClickListener(new android.view.View.OnClickListener() {
		    			
		    			@Override
		    			public void onClick(View v) {
		    				// TODO Auto-generated method stub
		    				dialog.dismiss();	
		    			}
		    		});
		    		submit_Iv.setOnClickListener(new android.view.View.OnClickListener() {
		    			
		    			@Override
		    			public void onClick(View v) {
		    				// TODO Auto-generated method stub
		    				String name=name_et.getText().toString().trim();
		    				if(name==null||"".equals(name)){
		    					Toast.makeText(context, "名字不能为空", Toast.LENGTH_LONG).show();
		    					return;
		    				}
		    				if(name.length()>32){
		    					Toast.makeText(context, "名字太长了", Toast.LENGTH_LONG).show();
		    					return;
		    				}
		    				if(rgn_S.getSzName().equals(name)){
		    					Toast.makeText(context, "名字不能一样", Toast.LENGTH_LONG).show();
		    					return;
		    				}
		    				  try {
		    					  rgn_S.setSzName(name);
		    					  WriteUtil.write(MsgId_E.MSGID_RGN.getVal(),0,MsgType_E.MSGTYPE_REQ.getVal(),MsgOper_E.MSGOPER_MOD.getVal(),Rgn_S.getSize(), rgn_S.getRgn_S());
		    					  ((AreaListActivity)context).mod_Idx=position;
		    					  ((AreaListActivity)context).rgn_S=rgn_S;
		    					dialog.dismiss();	
		    					} catch (IOException e) {
		    						// TODO Auto-generated catch block
		    						Toast.makeText(context, "修改失败", Toast.LENGTH_LONG).show();
		    					}	
		    			}
		    		});
		             
				}
			});
			
		}
		return convertView;
	}

}
