package com.tencent.mars.xlog;

import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

/**
 * Created by joybar on 2019/1/11.
 */

public class L {

	/**
	 * 初始化，请在使用之前调用L.init方法
	 * 静态变量CONFIG_DEBUG_MODE：false模式下，只打印i 和 e，否则，全打印
	 * 静态变量CONFIG_NEED_JUMP_SOURCE： true模式下，会自动console中自动加上跳转到代码的链接
	 */

	public static boolean CONFIG_DEBUG_MODE = true;
	public static boolean CONFIG_NEED_JUMP_SOURCE = true;

	private static Xlog xLog;
	private static int LOG_TRACE_LEVEL = 4;

	/**
	 *
	 * @param cacheDir  this is necessary, or may crash for SIGBUS
	 * @param logDir   log dir
	 * @param LogFileName   log file name prefix
	 * @param pubKey   如果为空，不会加密，否则，log会加密
	 * @param consoleLogOpen 日志是否在控制台打印，建议debug模式置为true，release模式置为false
	 */
	public static void init(String cacheDir, String logDir, String LogFileName, String pubKey, boolean consoleLogOpen) {
		if (null == xLog) {
			xLog = new Xlog();
		}
		xLog.init(consoleLogOpen, cacheDir, logDir, LogFileName, pubKey);
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

	/**
	 *
	 * @param level 参数定义见内部类Config
	 */
	public static void setLevel(final int level) {
		if (xLog != null) {
			xLog.setLogLevel(level);
		}
	}

	/**
	 * 设置log保留时间，默认10天
	 * @param aliveTime
	 */
	public void setLogMaxAliveTime(long aliveTime){
		if (xLog != null) {
			xLog.setMaxAliveTime(aliveTime);
		}
	}


	public static void i(String tag, String message) {
		if (!TextUtils.isEmpty(message) && xLog != null) {
			message = CONFIG_NEED_JUMP_SOURCE ? getAutoJumpLogInfos() + message : message;
			xLog.i(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}


	public static void v(String tag, String message) {
		if (CONFIG_DEBUG_MODE && !TextUtils.isEmpty(message) && xLog != null) {
			message = CONFIG_NEED_JUMP_SOURCE ? getAutoJumpLogInfos() + message : message;
			xLog.v(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}


	public static void d(String tag, String message) {
		if (CONFIG_DEBUG_MODE && !TextUtils.isEmpty(message) && xLog != null) {
			message = CONFIG_NEED_JUMP_SOURCE ? getAutoJumpLogInfos() + message : message;
			xLog.d(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}

	public static void w(String tag, String message) {
		if (CONFIG_DEBUG_MODE && !TextUtils.isEmpty(message) && xLog != null) {
			message = CONFIG_NEED_JUMP_SOURCE ? getAutoJumpLogInfos() + message : message;
			xLog.w(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}

	public static void e(String tag, String message) {
		if (!TextUtils.isEmpty(message) && xLog != null) {
			message = CONFIG_NEED_JUMP_SOURCE ? getAutoJumpLogInfos() + message : message;
			xLog.e(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), message);
		}
	}


	private static String getAutoJumpLogInfos() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length >= 5) {
			String s = "at " + elements[LOG_TRACE_LEVEL].getClassName() + "." + elements[LOG_TRACE_LEVEL].getMethodName() + "(" +
					elements[LOG_TRACE_LEVEL].getClassName().substring(elements[LOG_TRACE_LEVEL].getClassName().lastIndexOf(".") + 1,
							elements[LOG_TRACE_LEVEL].getClassName().length()) + ".java:" + elements[LOG_TRACE_LEVEL].getLineNumber() + ")";
			return s;
		}
		return "";
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
