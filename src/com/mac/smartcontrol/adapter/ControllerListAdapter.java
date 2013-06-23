package com.mac.smartcontrol.adapter;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
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
import com.mac.smartcontrol.ControllerListActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.Ctrl_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;

public class ControllerListAdapter extends BaseAdapter {
	private Context context;
	private List<Ctrl_S> ctrlList;

	public ControllerListAdapter(Context context, List<Ctrl_S> ctrlList) {
		super();
		this.context = context;
		this.ctrlList = ctrlList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ctrlList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ctrlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater localinflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = localinflater.inflate(R.layout.controller_list_item,
					null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.controller_name_Tv = (TextView) convertView
					.findViewById(R.id.controller_name);
			holder.controller_address_Tv = (TextView) convertView
					.findViewById(R.id.controller_address);
			holder.controller_state_Tv = (TextView) convertView
					.findViewById(R.id.controller_state);
			holder.delete_Iv = (ImageView) convertView
					.findViewById(R.id.delete_btn);
			holder.modify_Iv = (ImageView) convertView
					.findViewById(R.id.modify_btn);

		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}

		final Ctrl_S ctrl_S = ctrlList.get(position);
		holder.controller_name_Tv.setText(ctrl_S.getSzName());
		holder.controller_address_Tv.setText(ctrl_S.getUlAddr() + "");
		if (ctrl_S.getUcStatus() == 1) {
			holder.controller_state_Tv.setText("在线");
			holder.delete_Iv.setVisibility(View.GONE);
		} else if (ctrl_S.getUcStatus() == 2) {
			holder.controller_state_Tv.setText("离线");
		}

		holder.delete_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// try {
				WriteUtil.write(MsgId_E.MSGID_CTRL.getVal(), 0,
						MsgType_E.MSGTYPE_REQ.getVal(),
						MsgOper_E.MSGOPER_DEL.getVal(), MsgDelReq_S.getSize(),
						new MsgDelReq_S(ctrl_S.getUsIdx()).getMsgDelReq_S(),
						context);
				((AreaListActivity) context).del_Idx = position;
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// Intent i = new Intent("IOException");
				// context.sendBroadcast(i);
				// }
			}
		});
		holder.modify_Iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(context, R.style.MyDialog);
				// 设置它的ContentView
				dialog.setContentView(R.layout.area_add_dialog);
				TextView dialog_title_tv = (TextView) dialog
						.findViewById(R.id.dialog_title);
				dialog_title_tv.setText(R.string.modify_controller);
				dialog.show();
				ImageView submit_Iv = (ImageView) dialog
						.findViewById(R.id.submit_iv);
				ImageView cancel_Iv = (ImageView) dialog
						.findViewById(R.id.cancel_iv);
				final EditText name_et = (EditText) dialog
						.findViewById(R.id.name_et);
				name_et.setText(ctrl_S.getSzName());

				cancel_Iv
						.setOnClickListener(new android.view.View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				submit_Iv
						.setOnClickListener(new android.view.View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String name = name_et.getText().toString()
										.trim();
								if (name == null || "".equals(name)) {
									Toast.makeText(context, "名字不能为空",
											Toast.LENGTH_LONG).show();
									return;
								}
								if (name.length() > 32) {
									Toast.makeText(context, "名字太长了",
											Toast.LENGTH_LONG).show();
									return;
								}
								if (ctrl_S.getSzName().equals(name)) {
									Toast.makeText(context, "名字不能一样",
											Toast.LENGTH_LONG).show();
									return;
								}

								// try {
								ctrl_S.setSzName(name);
								WriteUtil.write(MsgId_E.MSGID_CTRL.getVal(), 0,
										MsgType_E.MSGTYPE_REQ.getVal(),
										MsgOper_E.MSGOPER_MOD.getVal(),
										Ctrl_S.getSize(), ctrl_S.getCtrl_S(),
										context);
								((ControllerListActivity) context).mod_Idx = position;
								((ControllerListActivity) context).ctrl_S = ctrl_S;
								dialog.dismiss();
								// } catch (IOException e) {
								// // TODO Auto-generated catch block
								// Toast.makeText(context, "修改失败",
								// Toast.LENGTH_LONG).show();
								// }
							}
						});

			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView controller_name_Tv;
		TextView controller_address_Tv;
		TextView controller_state_Tv;
		ImageView delete_Iv;
		ImageView modify_Iv;

	}

	void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.controller_name_Tv.setText(null);
		viewHolder.controller_address_Tv.setText(null);
		viewHolder.controller_state_Tv.setText(null);

	}
}
