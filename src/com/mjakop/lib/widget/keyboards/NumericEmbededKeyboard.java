package com.mjakop.lib.widget.keyboards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class NumericEmbededKeyboard extends LinearLayout  {
	
	private Object[][] keyboardLayout = {
			{"1", "2", "3"},
			{"4", "5", "6"},
			{"7", "8", "9"},
			{null, "0", null}
	};
	
	private Context context;
	private NumericEmbededKeyboardListener keyboardListener;
	private int buttonStyleReference;
	
	public NumericEmbededKeyboard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		buildDesign(-1);
	}

	public NumericEmbededKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		buildDesign(-1);
	}

	public NumericEmbededKeyboard(Context context) {
		super(context);
		this.context = context;
		buildDesign(-1);
	}
	
	public void setListener(NumericEmbededKeyboardListener listener){
		this.keyboardListener = listener;
	}
	
	public void setBottomLeftItem(View view){
		keyboardLayout[3][0] = view;
	}
	
	public void setBottomRightItem(View view) {
		keyboardLayout[3][2] = view;
	}
	
	private void handleKeyClick(Button button, Object item) {
		if (keyboardListener != null) {
			keyboardListener.onKeyPressed(""+button.getText());
		}
	}
	
	public void setButtonStyle(int buttonStyleReference){
		this.buttonStyleReference = buttonStyleReference;
	}
	
	public void buildView(){
		buildDesign(buttonStyleReference);
	}
	
	private void buildDesign(int buttonReference){
		removeAllViews();
		setOrientation(LinearLayout.VERTICAL);
		for(int i=0; i<keyboardLayout.length; i++) {
			LinearLayout hLayout = new LinearLayout(context);
			hLayout.setOrientation(LinearLayout.HORIZONTAL);
			hLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			double weight = 1.0 / (double)keyboardLayout[i].length;
			for(int j=0;j<keyboardLayout[i].length; j++) {
				final Object item = keyboardLayout[i][j];
				if (item != null) {
					if (item instanceof String) {
						String label = (String)item;
						//create button
						Button tmp;
						if (buttonReference > 0) {
							tmp = new Button(context, null, buttonStyleReference);
						} else {
							tmp = new Button(context);
						}
						final Button bK = tmp;
						bK.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, (float)weight));
						bK.setText(label);
						bK.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								handleKeyClick(bK, item);
							}
							
						});
						hLayout.addView(bK);
					} else if (item instanceof View) {
						View iView = (View)item;
						iView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, (float)weight));
						hLayout.addView(iView);
					}
				} else {
					//make empty space
				}
			}
			addView(hLayout);
		}
	}
	
	
}
