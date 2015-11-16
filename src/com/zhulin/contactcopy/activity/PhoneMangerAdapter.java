package com.zhulin.contactcopy.activity;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.paser.HXPhone;

public class PhoneMangerAdapter extends BaseAdapter {
	private boolean cancheck=false;
	private ArrayList<HXPhone> phones;
	
	public PhoneMangerAdapter(boolean cancheck, ArrayList<HXPhone> phones) {
		super();
		this.cancheck = cancheck;
		this.phones = phones;
	}

	@Override
	public int getCount() {
		return phones==null?0:phones.size();
	}

	@Override
	public Object getItem(int position) {
		return phones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder{
		TextView tv_name,tv_phone;
		ImageView iv_check;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView=View.inflate(parent.getContext(), R.layout.items_phone_manger, null);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_phone=(TextView) convertView.findViewById(R.id.tv_phone);
			holder.iv_check=(ImageView) convertView.findViewById(R.id.iv_check);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		HXPhone hxPhone=phones.get(position);
		if (hxPhone!=null) {
			holder.tv_name.setText(hxPhone.name);
			holder.tv_phone.setText(hxPhone.phone);
			holder.iv_check.setTag(hxPhone);
			if (cancheck) {
				holder.iv_check.setVisibility(View.VISIBLE);
				if (hxPhone.checked) {
					holder.iv_check.setImageResource(R.drawable.checkbox_on);
				}else {
					holder.iv_check.setImageResource(R.drawable.checkbox_off);
				}
			}else {
				holder.iv_check.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

}
