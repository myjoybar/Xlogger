package com.tencent.mars.xlog;

import android.os.Looper;
import android.os.Process;

/**
 * Created by joybar on 2019/1/11.
 */

public class L {

	private static Xlog xLog;

	public static void init(String cacheDir, String logDir, String fName,String pubKey) {
		if (null == xLog) {
			xLog = new Xlog();
		}
		xLog.init(true, cacheDir, logDir, fName, pubKey);
	}

	public static void init(String cacheDir, String logDir, String fName) {
		if (null == xLog) {
			xLog = new Xlog();
		}
		xLog.init(true, cacheDir, logDir, fName, "");
	}

	/**
	 * 程序退出时反初始化
	 */
	public static void appenderClose() {
		if (xLog != null) {
			xLog.appenderClose();
		}
	}

	/**
	 * 将缓存写到文件中，但是文件未关闭，此时文件还不完整，必须调用appenderClose才行。
	 *
	 * @param isSync 是否同步写
	 */
	public static void appenderFlush(boolean isSync) {
		if (xLog != null) {
			xLog.appenderFlush(isSync);
		}
	}

	public static void setLevel(final int level) {
		if (xLog != null) {
			xLog.setLogLevel(level);
		}
	}

	public static void v(String tag, String message) {
		if (xLog != null) {
			xLog.v(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}

	public static void i(String tag, String message) {
		if (xLog != null) {
			xLog.i(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}

	public static void d(String tag, String message) {
		if (xLog != null) {
			xLog.d(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}

	public static void w(String tag, String message) {
		if (xLog != null) {
			xLog.w(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}

	public static void e(String tag, String message) {
		if (xLog != null) {
			xLog.e(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}


	public static class Config {

		public static int kLevelAll = 0;
		public static int kLevelVerbose = 0;
		public static int kLevelDebug = 1;    // Detailed information on the flow through the system.
		public static int kLevelInfo = 2;   // Interesting runtime events (startup/shutdown), should be conservative and keep to a minimum.
		public static int kLevelWarn = 3;    // Other runtime situations that are undesirable or unexpected, but not necessarily "wrong".
		public static int kLevelError = 4;    // Other runtime errors or unexpected conditions.
		public static int kLevelFatal = 5;   // Severe errors that cause premature termination.
		public static int kLevelNone = 6;   // Special level used to disable all log messages.


		public static final int AppednerModeAsync = 0;
		public static final int AppednerModeSync = 1;
	}

}
