package com.mjakop.lib.widget.inside_menu;

import java.util.ArrayList;

import com.mjakop.lib.widget.ObjectArrayListAdapter;
import com.mjakop.lib.widget.ObjectArrayListAdapterListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class InsideMenu extends ListView implements OnItemClickListener{
	
	private InsideMenuListener listener;
	private Context context;
	private InsideMenuItems items;
	
	public InsideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOnItemClickListener(this);
	}


	public InsideMenu(Context context) {
		super(context);
		setOnItemClickListener(this);
	}
	
	public InsideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnItemClickListener(this);
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (this.items != null) {
			InsideMenuItem item = this.items.get(position);
			if (this.listener != null) {
				listener.onItemSelected(item);
			}
		}
	}
	
	public void setMenuItems(int layoutResourceId, int viewId, InsideMenuItems items, InsideMenuListener listener){
		this.listener = listener;
		
		final int finalViewId = viewId;
		ArrayList<Object> data = new ArrayList<Object>();
		data.addAll(items);
		ObjectArrayListAdapter ladapter = new ObjectArrayListAdapter(this.context, layoutResourceId, data, new ObjectArrayListAdapterListener() {
			
			public void setRowData(View row, Object item) {
				TextView title = (TextView)row.findViewById(finalViewId);
				InsideMenuItem mitem = (InsideMenuItem)item;
				title.setText(mitem.getTitle());
			}
			
		});
		this.items = items;
		this.setAdapter(ladapter);
		this.listener = listener;
	}
	
	
	
}
