package com.mjakop.lib.db.adapters;

import java.util.ArrayList;

import com.mjakop.lib.db.DBArray;
import com.mjakop.lib.db.DBItem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class DBItemAdapter extends ArrayAdapter<DBItem> {
	
    private Context context;
    private int layoutResourceId;
	public DBArray data;
	private DBItemAdapterListener listener;
	
	protected static DBItem[] fromDBArrayToArray(DBArray input){
		DBItem[] output = new DBItem[input.length()];
		for(int i=0;i<output.length;i++){
			try{
				output[i] = input.getDBItem(i);
			}catch (Exception e) {
			}
		}
		return output;
	}
	
	public DBItemAdapter(Context context, int layoutResourceId, ArrayList<DBItem> data, DBItemAdapterListener listener) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.listener = listener;
		this.data = null;
	}
	
	public DBItemAdapter(Context context, int layoutResourceId, DBArray data, DBItemAdapterListener listener) {
		super(context, layoutResourceId, fromDBArrayToArray(data));
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		this.listener = listener;
	}
	
	@Override
	public View getDropDownView(int position, View row, ViewGroup parent) {
        if(row == null) {
        	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        try{
        	DBItem item = (DBItem)getItem(position);
        	listener.setRowData(row, item);
        }catch (Exception e) {
        	Log.e("DBItemAdapter", "Problem inside getView: ", e);
		}
        return row;
	}
	
	
	
	@Override
	public View getView(int position, View row, ViewGroup parent) {       
        if(row == null) {
        	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        try{
        	DBItem item = (DBItem)getItem(position);
        	listener.setRowData(row, item);
        }catch (Exception e) {
        	Log.e("DBItemAdapter", "Problem inside getView: ", e);
		}
        return row;
	}	
	
}
