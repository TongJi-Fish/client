package com.app.ipinle.base;

import android.app.Activity;

public class BaseTask extends Activity {

	public static final int TASK_COMPLETE = 0;
	public static final int NETWORK_ERROR = 1;
	public static final int SHOW_LOADBAR = 2;
	public static final int HIDE_LOADBAR = 3;
	public static final int SHOW_TOAST = 4;
	public static final int LOAD_IMAGE = 5;
	
	private int id = 0;
	private String name = "";
	
	public BaseTask() {}
	
	public int getId () {
		return this.id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getName () {
		return this.name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void onStart () {
<<<<<<< HEAD
//		Log.w("BaseTask", "onStart");
	}
	
	public void onComplete () {
//		Log.w("BaseTask", "onComplete");
	}
	
	public void onComplete (String httpResult) {
//		Log.w("BaseTask", "onComplete");
	}
	
	public void onError (String error) {
//		Log.w("BaseTask", "onError");
	}
	
	public void onStop () {
//		Log.w("BaseTask", "onStop");
=======
//		//Log.w("BaseTask", "onStart");
	}
	
	public void onComplete () {
//		//Log.w("BaseTask", "onComplete");
	}
	
	public void onComplete (String httpResult) {
//		//Log.w("BaseTask", "onComplete");
	}
	
	public void onError (String error) {
//		//Log.w("BaseTask", "onError");
	}
	
	public void onStop () {
//		//Log.w("BaseTask", "onStop");
>>>>>>> ea8e1d28021be3ee08bb239a2c4bb3fc553be0c2
	}
	
}
