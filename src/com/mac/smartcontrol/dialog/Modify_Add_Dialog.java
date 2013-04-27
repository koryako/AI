package com.mac.smartcontrol.dialog;

import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.WriteUtil;

import define.type.MsgType_E;

public class Modify_Add_Dialog extends Dialog {

	private String titleName;
	private Context context;
	//type=1 Ìí¼Ó type=3 ÐÞ¸Ä
	private byte msgOper=1;
	private short msgId=3;
	private short size=0;
	private byte[] body;
	
	public Modify_Add_Dialog(Context context,String titleName,short msgId,byte msgOper,short size,byte[] body) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.titleName=titleName;
		this.msgOper=msgOper;
		this.msgId=msgId;
		this.size=size;
		this.body=body;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.area_add_dialog);
		TextView title_tv=(TextView) findViewById(R.id.dialog_title);
		title_tv.setText(titleName);
		ImageView submit_Iv=(ImageView) findViewById(R.id.submit_iv);
		ImageView cancel_Iv=(ImageView) findViewById(R.id.cancel_iv);
		cancel_Iv.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Modify_Add_Dialog.this.dismiss();	
			}
		});
		submit_Iv.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String toast_msg="Ìí¼ÓÊ§°Ü";
				 if(msgOper==3){
					 toast_msg="ÐÞ¸ÄÊ§°Ü";
				 }
				  try {
					WriteUtil.write(msgId,0,MsgType_E.MSGTYPE_REQ.getVal(),msgOper,size, body);
					Modify_Add_Dialog.this.dismiss();	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(context, toast_msg, Toast.LENGTH_LONG).show();
					}	
			}
		});
		
	}

}
