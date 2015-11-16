package com.zhulin.contactcopy.activity;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.paser.User;

public class MangerAdapter extends BaseAdapter{
	private boolean cancheck=false;
	private ArrayList<User> users;
	
	public MangerAdapter(boolean cancheck, ArrayList<User> users) {
		super();
		this.cancheck = cancheck;
		this.users = users;
	}

	@Override
	public int getCount() {
		return users==null?1:users.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	private class ViewHolder{
		TextView tv_name,tv_maxnum,tv_finshnum,tv_onenum,tv_time,tv_status;
		ImageView iv_check;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView=View.inflate(parent.getContext(), R.layout.items_manger_user, null);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_maxnum=(TextView) convertView.findViewById(R.id.tv_maxnum);
			holder.tv_finshnum=(TextView) convertView.findViewById(R.id.tv_finshnum);
			holder.tv_onenum=(TextView) convertView.findViewById(R.id.tv_onenum);
			holder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_status=(TextView) convertView.findViewById(R.id.tv_status);
			holder.iv_check=(ImageView) convertView.findViewById(R.id.iv_check);
			holder.tv_name.setTag("");
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position==0) {
			holder.tv_name.setText("姓名");
			holder.tv_maxnum.setText("最大\n下载量");
			holder.tv_finshnum.setText("已下载数");
			holder.tv_onenum.setText("单次\n下载数");
			holder.tv_time.setText("下载间隔\n（分钟）");
			holder.tv_maxnum.setTextSize(10);
			holder.tv_finshnum.setTextSize(10);
			holder.tv_onenum.setTextSize(10);
			holder.tv_time.setTextSize(10);
			if (cancheck) {
				holder.iv_check.setVisibility(View.INVISIBLE);
			}else {
				holder.iv_check.setVisibility(View.GONE);
			}
			holder.tv_status.setText("用户状态");
		} else {
			User user=users.get(position-1);
			if (user!=null) {
				holder.tv_name.setText(user.name==null?"":user.name);
				holder.tv_maxnum.setText(user.dayMaxNum==null?"0":user.dayMaxNum);
				holder.tv_finshnum.setText(user.dayDownMun==null?"0":user.dayDownMun);
				holder.tv_onenum.setText(user.perDownNum==null?"0":user.perDownNum);
				long time=Long.valueOf(user.perDownTime==null?"0":user.perDownTime);
				holder.tv_time.setText(String.valueOf(time/1000/60));
				holder.tv_maxnum.setTextSize(16);
				holder.tv_finshnum.setTextSize(16);
				holder.tv_onenum.setTextSize(16);
				holder.tv_time.setTextSize(16);
				String status=user.status==null?"0":user.status;
				if (status.equals("0")) {
					holder.tv_status.setText("禁用");
				}else {
					holder.tv_status.setText("正常");
				}
				if (cancheck) {
					holder.iv_check.setVisibility(View.VISIBLE);
					if (user.selced) {
						holder.iv_check.setImageResource(R.drawable.checkbox_on);
					}else {
						holder.iv_check.setImageResource(R.drawable.checkbox_off);
					}
				}else {
					holder.iv_check.setVisibility(View.GONE);
				}
				holder.tv_name.setTag(user);
			}
		}                                 
		return convertView;
	}

}
