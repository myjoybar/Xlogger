package com.tencent.mars.xlog;

/**
 * Created by joybar on 2019/1/14.
 */

class Xlog implements IXLogService {


	public void init(boolean consoleLogOpen, int logLevelControl, String cacheDir, String logDir, String LogFileName, String pubKey) {

		System.loadLibrary("c++_shared_log");
		System.loadLibrary("marsxlog");
		setConsoleLogOpen(consoleLogOpen);
		appenderOpen(logLevelControl, XLogger.Config.AppednerModeAsync, cacheDir, logDir, LogFileName, 0, pubKey);
	}


	@Override
	public void v(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(XLogger.Config.LEVEL_VERBOSE, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}

	@Override
	public void d(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(XLogger.Config.LEVEL_DEBUG, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}

	@Override
	public void i(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(XLogger.Config.LEVEL_INFO, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}


	@Override
	public void w(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(XLogger.Config.LEVEL_WARN, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}

	@Override
	public void e(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(XLogger.Config.LEVEL_ERROR, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}

	/**
	 * 设置log保留时间（每次启动时会删除过期文件，默认只保留十天内的日志文件）
	 *
	 * @param aliveTime
	 */
	public void setLogMaxAliveTime(long aliveTime) {
		setMaxAliveTime(aliveTime);
	}


	public static native void setConsoleLogOpen(boolean isOpen);    //set whether the console prints log

	public static native void setLogLevel(int logLevel);

	public static native void setAppenderMode(int mode);

	public static native void appenderOpen(int level, int mode, String cacheDir, String logDir, String namePrefix, int cachelogdays, String pubKey);


	public static native void logWrite(XLoggerInfo logInfo, String log);

	public static native void logWrite2(int level, String tag, String filename, String funcname, int line, int pid, long tid, long maintid,
										String log);


	public native void appenderClose();

	public native void appenderFlush(boolean isSync);

	public static native void setMaxAliveTime(long aliveTime);

	public static native void setMaxFileSize(long fileSize);

	static class XLoggerInfo {
		public int level;
		public String tag;
		public String filename;
		public String funcname;
		public int line;
		public long pid;
		public long tid;
		public long maintid;
	}


}
