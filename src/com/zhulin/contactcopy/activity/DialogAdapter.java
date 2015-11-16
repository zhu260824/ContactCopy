package com.zhulin.contactcopy.activity;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhulin.contactcopy.R;
import com.zhulin.contactcopy.paser.DialogAction;

public class DialogAdapter extends BaseAdapter{
	private ArrayList<DialogAction> actions;
                 	
	public DialogAdapter(ArrayList<DialogAction> actions) {
		super();
		this.actions = actions;
	}

	@Override
	public int getCount() {
		return actions==null?0:actions.size();
	}

	@Override
	public Object getItem(int position) {
		return actions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder{
		TextView tv_action_name;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView=View.inflate(parent.getContext(), R.layout.items_dialog_action, null);
			holder.tv_action_name=(TextView) convertView.findViewById(R.id.tv_action_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		DialogAction dAction=actions.get(position);
		if (dAction!=null) {
			holder.tv_action_name.setText(dAction.getActionname());
			holder.tv_action_name.setTag(dAction.getActionid());
		}
		return convertView;
	}

}
