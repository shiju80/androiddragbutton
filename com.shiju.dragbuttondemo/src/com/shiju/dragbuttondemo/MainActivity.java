package com.shiju.dragbuttondemo;

import com.shiju.dragbuttondemo.dragbutton.DragButtonControl;
import com.shiju.dragbuttondemo.dragbutton.DragButtonControl.DragButtonControlActionListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity implements DragButtonControlActionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DragButtonControl control = (DragButtonControl) findViewById(R.id.drag_control);
		control.setDragButtonImage(R.drawable.ic_launcher);
		control.setOnDragButtonControlActionListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDragLeft(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDragRight(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDragUp(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDragDown(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDragStarted(boolean xAxis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDragEnded(boolean xAxis) {
		// TODO Auto-generated method stub
		
	}

}
