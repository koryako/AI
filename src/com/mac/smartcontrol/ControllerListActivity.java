package com.mac.smartcontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mac.smartcontrol.adapter.ControllerListAdapter;
import com.mac.smartcontrol.broadcast.ControllerBroadcastReceiver;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Ctrl_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgQryReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ControllerListActivity extends Activity {

	public List<Ctrl_S> ctrlList=null;
	public ControllerListAdapter controllerListAdapter=null;
	public ListView ctrlListView=null;
	public int del_Idx=-1;
	public int mod_Idx=-1;
	ControllerBroadcastReceiver controllerBroadcastReceiver;
	TextView ctrl_title_tv;
	public Ctrl_S ctrl_S=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ctrl_title_tv=(TextView) findViewById(R.id.userlist_title);
        ctrl_title_tv.setText(R.string.controller_list);
        ctrl_S=new Ctrl_S();
        ctrlListView=(ListView) findViewById(R.id.userlist);
        ctrlList=new ArrayList<Ctrl_S>();
        controllerListAdapter=new ControllerListAdapter(ControllerListActivity.this, ctrlList);
        ctrlListView.setAdapter(controllerListAdapter);
        ImageView back_Iv=(ImageView) findViewById(R.id.back);
        back_Iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        ImageView add_Iv=(ImageView) findViewById(R.id.add);
        add_Iv.setVisibility(View.INVISIBLE);
        controllerBroadcastReceiver=new ControllerBroadcastReceiver(ControllerListActivity.this);
		IntentFilter filter=new IntentFilter();
		filter.addAction("2_2");
		filter.addAction("2_3");
		filter.addAction("2_4");
		filter.addAction("IOException");
		registerReceiver(controllerBroadcastReceiver, filter);
		
		  try {
				WriteUtil.write(MsgId_E.MSGID_CTRL.getVal(),0,MsgType_E.MSGTYPE_REQ.getVal(),MsgOper_E.MSGOPER_QRY.getVal(),(short)2, new MsgQryReq_S((short) 0).getMsgQryReq_S());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(ControllerListActivity.this, "获取列表失败", Toast.LENGTH_LONG).show();
			}
      
    }
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		 unregisterReceiver(controllerBroadcastReceiver);
		super.finish();
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
	               finish();
	     } 
	               return true; 
	    }
	
}
