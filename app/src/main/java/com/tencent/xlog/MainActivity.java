package com.tencent.xlog;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mars.xlog.*;
import com.tencent.utils.LogUtils;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		L.init(LogUtils.LOG_CACHE_PATH, LogUtils.LOG_PATH, LogUtils.logFileName(), LogUtils.PUB_KEY, LogUtils.CONSOLE_LOG_OPEN);
		L.setLevel(L.Config.kLevelVerbose);
		//L.CONFIG_NEED_JUMP_SOURCE = com.tencent.mars.xlog.BuildConfig.DEBUG ? true : false;
		L.CONFIG_NEED_JUMP_SOURCE = false;

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				writeLog1();
			}
		}, 2000);


		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				LogUtils.zipPreviousLogs(LogUtils.LOG_PATH, true, true);
			}
		}, 5000);

	}


	public void writeLog1() {
		for (int i = 0; i < 100; i++) {
			L.i("MainActivity", "onCreate-----iiiiiii---" + i);
			L.d("MainActivity", "onCreate----dddddd----" + i);
			L.w("MainActivity", "onCreate----wwwwwww----" + i);
			L.e("MainActivity", "onCreate-----eeeeeee--" + i);
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		L.appenderClose();
	}
}
