package com.tencent.xlog;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mars.xlog.L;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

	String PUB_KEY =
			"572d1e2710ae5fbca54c76a382fdd44050b3a675cb2bf39feebe85ef63d947aff0fa4943f1112e8b6af34bebebbaefa1a0aae055d9259b89a1858f7cc9af9df1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
		final String logPath = SDCARD + "/marssample/log";
		// this is necessary, or may crash for SIGBUS
		final String cachePath = logPath + "/xlog";
		//L.init(cachePath,logPath,getLogFileName(),getLogFileName()),PUB_KEY);
		L.init(cachePath,logPath,getLogFileName(),getLogFileName());
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				L.setLevel(L.Config.kLevelVerbose);
				L.i("MainActivity","onCreate--------");
				writeLog1();
			}
		},2000);

	}


	public String getLogFileName(){
		Date day=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.format(day);
	}

	public void writeLog1(){
		for(int i =0;i<10000;i++){
			L.i("MainActivity","onCreate-----iiiiiii---"+i);
			L.d("MainActivity","onCreate----dddddd----"+i);
			L.w("MainActivity","onCreate----wwwwwww----"+i);
			L.e("MainActivity","onCreate-----eeeeeee--"+i);
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		L.appenderClose();
	}
}
