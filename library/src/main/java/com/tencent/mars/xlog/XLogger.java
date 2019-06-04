package com.tencent.mars.xlog;

import android.text.TextUtils;

/**
 * Created by joybar on 2019/1/11.
 */

public class XLogger {


	private static boolean CONFIG_NEED_JUMP_SOURCE = true;
	private static int CONFIG_LOG_LEVEL_CONTROL = 0;
	private static int STACK_TRACE_LEVEL = 5;
	private static final String AT = "at:";
	private static final String LEFT_BRACKET = "(";
	private static final String RIGHT_BRACKET = ")";
	private static final String DOT = ".";
	private static final String JAVA_FILE_NANE = ".java:";
	private static final String UNDER_LINE = "_";
	private static final String COLON = ":";


	private static Xlog xLog;

	/**
	 * 初始化，请在使用之前调用L.init方法
	 *
	 * @param cacheDir        this is necessary, or may crash for SIGBUS
	 * @param logDir          log dir
	 * @param LogFileName     log file name prefix
	 * @param pubKey          如果为空，不会加密，否则，log会加密
	 * @param consoleLogOpen  日志是否在控制台打印，建议debug模式置为true，release模式置为false
	 * @param logLevelControl log等级，详见L.Config 定义, 0-6,0打印所有，6不会打印
	 */
	public static void init(String cacheDir, String logDir, String LogFileName, String pubKey, boolean consoleLogOpen, int logLevelControl,
							boolean jumpToSource) {
		if (null == xLog) {
			xLog = new Xlog();
		}
		CONFIG_NEED_JUMP_SOURCE = jumpToSource;
		CONFIG_LOG_LEVEL_CONTROL = logLevelControl;
		xLog.init(consoleLogOpen, logLevelControl, cacheDir, logDir, LogFileName, pubKey);
	}

	public static void setStackTraceLevel(int stackTraceLevel) {
		STACK_TRACE_LEVEL = stackTraceLevel;
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
	 * @param level 参数定义见内部类Config
	 */
	public static void setLevel(final int level) {
		if (xLog != null) {
			xLog.setLogLevel(level);
		}
	}

	/**
	 * 设置log保留时间，默认10天
	 *
	 * @param aliveTime
	 */
	public void setLogMaxAliveTime(long aliveTime) {
		if (xLog != null) {
			xLog.setMaxAliveTime(aliveTime);
		}
	}


	public static void v(String tag, String message) {
		if (canPrintLog(Config.LEVEL_VERBOSE) && !TextUtils.isEmpty(message) && xLog != null) {
			//xLog.v(tag, "", "", 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), combineLogMsg(message));
			xLog.v(tag, "", "", 0, 0,0,0, combineLogMsg(message));
		}
	}


	public static void d(String tag, String message) {
		if (canPrintLog(Config.LEVEL_DEBUG) && !TextUtils.isEmpty(message) && xLog != null) {
			xLog.d(tag, "", "", 0, 0,0, 0, combineLogMsg(message));
		}
	}

	public static void i(String tag, String message) {
		if (canPrintLog(Config.LEVEL_INFO) && !TextUtils.isEmpty(message) && xLog != null) {
			xLog.i(tag, "", "", 0, 0,0, 0,combineLogMsg(message));
		}
	}


	public static void w(String tag, String message) {
		if (canPrintLog(Config.LEVEL_WARN) && !TextUtils.isEmpty(message) && xLog != null) {
			xLog.w(tag, "", "", 0, 0, 0, 0, combineLogMsg(message));
		}
	}

	public static void e(String tag, String message) {
		if (canPrintLog(Config.LEVEL_ERROR) && !TextUtils.isEmpty(message) && xLog != null) {
			xLog.e(tag, "", "", 0, 0, 0, 0, combineLogMsg(message));
		}
	}

	public static boolean canPrintLog(int currentLevel) {
		return CONFIG_LOG_LEVEL_CONTROL <= currentLevel;
	}

	private static String combineLogMsg(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(Thread.currentThread().getName()).append(UNDER_LINE);
		if (CONFIG_NEED_JUMP_SOURCE) {
			sb.append(getAutoJumpLogInfos());
		}
		sb.append(msg);
		return sb.toString();

	}

	private static String getAutoJumpLogInfos() {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length >= 5) {

			StackTraceElement stackTraceElement = elements[STACK_TRACE_LEVEL];
			sb.append(AT).append(stackTraceElement.getClassName()).append(DOT)
					.append(stackTraceElement.getMethodName())
					.append(LEFT_BRACKET)
					.append(stackTraceElement.getFileName())
					.append(COLON)
					.append(stackTraceElement.getLineNumber())
					.append(RIGHT_BRACKET);

			return sb.toString();
		}
		return "";
	}

	public static class Config {


		public static int LEVEL_ALL = 0;
		public static int LEVEL_VERBOSE = 0;
		public static int LEVEL_DEBUG = 1;    // Detailed information on the flow through the system.
		public static int LEVEL_INFO = 2;   // Interesting runtime events (startup/shutdown), should be conservative and keep to a minimum.
		public static int LEVEL_WARN = 3;    // Other runtime situations that are undesirable or unexpected, but not necessarily "wrong".
		public static int LEVEL_ERROR = 4;    // Other runtime errors or unexpected conditions.

		public static int LEVEL_FATAL = 5;   // Severe errors that cause premature termination.
		public static int LEVEL_NONE = 6;   // Special level used to disable all log messages.


		public static final int AppednerModeAsync = 0;
		public static final int AppednerModeSync = 1;
	}

}
