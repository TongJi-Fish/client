package com.app.ipinle.base;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
<<<<<<< HEAD
import android.util.Log;
import android.widget.Toast;

public class BaseUi extends Activity {
=======
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ipingle.R;

public class BaseUi<loadBar> extends Activity {
>>>>>>> ea8e1d28021be3ee08bb239a2c4bb3fc553be0c2

	protected BaseApp app;
	protected BaseHandler handler;
	protected BaseTaskPool taskPool;
<<<<<<< HEAD
	protected boolean showLoadBar = false;
	protected boolean showDebugMsg = true;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// debug memory
		//debugMemory("onCreate");
=======
	protected View loadBar;
	protected boolean showLoadBar = false;
	protected boolean showDebugMsg = true;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// debug memory
		// debugMemory("onCreate");
>>>>>>> ea8e1d28021be3ee08bb239a2c4bb3fc553be0c2
		// async task handler
		this.handler = new BaseHandler(this);
		// init task pool
		this.taskPool = new BaseTaskPool(this);
		// init application
<<<<<<< HEAD
		//this.app = (BaseApp) this.getApplicationContext();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// util method
	
	public Context getContext () {
		return this;
	}
	
	public void toast(String msg){
		Toast.makeText(this, "msg", Toast.LENGTH_LONG).show();
	}
	
	public void hideLoadBar(){
	}
	
	public void showLoadBar(){
	}
	
	public void onNetworkError(int taskId){
		Log.i(C.debug.login, "login baseUi error");
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// logic method
	
	public void sendMessage (int what, int taskId, String data) {
=======
		// this.app = (BaseApp) this.getApplicationContext();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// util method

	public Context getContext() {
		return this;
	}

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showLoadBar () {
		loadBar = this.findViewById(R.id.main_load_bar);
		if(loadBar != null){
			loadBar.setVisibility(View.VISIBLE);
			loadBar.bringToFront();	
		}else{
			this.toast("load bar null show");
		}
	}

	public void hideLoadBar() {
		loadBar = this.findViewById(R.id.main_load_bar);
		if(loadBar != null){
			loadBar.setVisibility(View.GONE);	
		}else{
			this.toast("load bar null hide");
		}

	}

	public void onNetworkError(int taskId) {
		this.toast(C.err.network);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// logic method

	public void sendMessage(int what, int taskId, String data) {
>>>>>>> ea8e1d28021be3ee08bb239a2c4bb3fc553be0c2
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
<<<<<<< HEAD
		if(what==BaseTask.NETWORK_ERROR)
		Log.i(C.debug.login, "what = NETWORK_ERROR baseUi error");
	}
	
	public void doTaskAsync (int taskId, String taskUrl, HashMap<String, String> taskArgs) {
		showLoadBar();
		taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask(){
			@Override
			public void onComplete (String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
				Log.i(C.debug.login, error+"SHOWERROR");
			}
		}, 0);
	}
	
	public void forward(Class<?> classObject){
=======
	}

	public void doTaskAsync(int taskId, String taskUrl,
			HashMap<String, String> taskArgs) {
		showLoadBar();
		taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask() {
			@Override
			public void onComplete(String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}

			@Override
			public void onError(String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
				//Log.i(C.debug.login, error + "SHOWERROR");
			}
		}, 0);
	}

	public void forward(Class<?> classObject) {
>>>>>>> ea8e1d28021be3ee08bb239a2c4bb3fc553be0c2
		Intent intent = new Intent();
		intent.setClass(this, classObject);
		startActivity(intent);
	}
<<<<<<< HEAD
	
	public void onTaskComplete(int taskId){
	}
	
	public void onTaskComplete(int taskId, BaseMessage message){
	}
	
	public void onTaskError(){	
		Log.i(C.debug.login,"base ui error");
=======

	public void onTaskComplete(int taskId) {
	}

	public void onTaskComplete(int taskId, BaseMessage message) {
	}

	public void onTaskError() {
		//Log.i(C.debug.login, "base ui error");
>>>>>>> ea8e1d28021be3ee08bb239a2c4bb3fc553be0c2
	}
}