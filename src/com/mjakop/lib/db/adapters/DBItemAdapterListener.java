package com.mjakop.lib.db.adapters;

import com.mjakop.lib.db.DBItem;

import android.view.View;

public interface DBItemAdapterListener {
	
	public void setRowData(View row, DBItem item, int position);
	
}
