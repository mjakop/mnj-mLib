package com.mjakop.lib.widget;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class ObjectArrayListAdapter extends ArrayAdapter<Object> {
	
    private Context context;
    private int layoutResourceId;
	private ObjectArrayListAdapterListener listener;
	
	private static ArrayList<Object> jsonArrayToArrayList(JSONArray data){
		ArrayList<Object> result = new ArrayList<Object>();
		for(int i=0; i<data.length(); i++) {
			try {
				result.add(data.get(i));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public ObjectArrayListAdapter(Context context, int layoutResourceId, JSONArray data, ObjectArrayListAdapterListener listener) {
		super(context, layoutResourceId, jsonArrayToArrayList(data));
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.listener = listener;
	}
	
	public ObjectArrayListAdapter(Context context, int layoutResourceId, ArrayList<Object> data, ObjectArrayListAdapterListener listener) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.listener = listener;
	}
	
	@Override
	public View getView(int position, View row, ViewGroup parent) {       
        if(row == null) {
        	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        try{
        	Object item = getItem(position);
        	listener.setRowData(row, item);
        }catch (Exception e) {
        	Log.e("StringArrayListAdapter", "Problem inside getView: ",e);
		}
        return row;
	}
	
}
