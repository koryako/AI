package com.mac.smartcontrol.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mac.smartcontrol.ModifyUserActivity;
import com.mac.smartcontrol.R;
import com.mac.smartcontrol.UserListActivity;
import com.mac.smartcontrol.util.WriteUtil;

import define.entity.User_S;
import define.oper.MsgOper_E;
import define.oper.body.req.MsgDelReq_S;
import define.type.MsgId_E;
import define.type.MsgType_E;
import define.type.UserType_E;

public class UserListAdapter extends BaseAdapter {
	private Context context;
	private List<User_S> userList;

	public UserListAdapter(Context context, List<User_S> userList) {
		super();
		this.context = context;
		this.userList = userList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userList.get(position);
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
			convertView = localinflater.inflate(R.layout.user_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);

			holder.userName_Tv = (TextView) convertView
					.findViewById(R.id.userName);
			holder.userType_Tv = (TextView) convertView
					.findViewById(R.id.userType);
			holder.delete_Iv = (ImageView) convertView
					.findViewById(R.id.delete_btn);
			holder.modify_Iv = (ImageView) convertView
					.findViewById(R.id.modify_btn);

		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}
		final User_S user_S = userList.get(position);
		holder.userName_Tv.setText(user_S.getSzName());
		if (user_S.getUcType() == UserType_E.USER_TYPE_USER.getVal()) {
			holder.userType_Tv.setText("��ͨ");
		} else if (user_S.getUcType() == UserType_E.USER_TYPE_ADMIN.getVal()) {
			holder.userType_Tv.setText("ϵͳ");
		}
		if (user_S.getSzName().equals("admin")) {
			holder.delete_Iv.setVisibility(View.INVISIBLE);
			holder.modify_Iv.setVisibility(View.INVISIBLE);
		} else {

			holder.delete_Iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// try {
					WriteUtil.write(
							MsgId_E.MSGID_USER.getVal(),
							0,
							MsgType_E.MSGTYPE_REQ.getVal(),
							MsgOper_E.MSGOPER_DEL.getVal(),
							MsgDelReq_S.getSize(),
							new MsgDelReq_S(user_S.getUsIdx()).getMsgDelReq_S(),
							context);
					((UserListActivity) context).del_Idx = position;
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
					Intent intent = new Intent();
					intent.setClass(context, ModifyUserActivity.class);
					intent.putExtra("user", user_S.getUser_S());
					// context.startActivity(intent);
					// ((Activity)context).finish();
					((UserListActivity) context).mod_Idx = position;
					// ��ʼһ���µ� Activity�Ⱥ򷵻ؽ��
					((Activity) context).startActivityForResult(intent, 1);
				}
			});
		}
		return convertView;
	}

	class ViewHolder {

		TextView userName_Tv;
		TextView userType_Tv;
		ImageView delete_Iv;
		ImageView modify_Iv;
	}

	void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.userName_Tv.setText(null);
		viewHolder.userType_Tv.setText(null);
	}

}
