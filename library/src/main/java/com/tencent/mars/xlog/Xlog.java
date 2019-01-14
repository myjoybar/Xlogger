package com.tencent.mars.xlog;

import android.os.Environment;

/**
 * Created by joybar on 2019/1/14.
 */

class Xlog implements XLogService {


	public void init(boolean consoleLogOpen, String cacheDir, String logDir, String namePrefix, String pubKey) {

		System.loadLibrary("c++_shared");
		System.loadLibrary("marsxlog");
		setConsoleLogOpen(consoleLogOpen);
		appenderOpen(L.Config.kLevelVerbose, L.Config.AppednerModeAsync, cacheDir, logDir, namePrefix,0, pubKey);
	}


	@Override
	public void v(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(L.Config.kLevelDebug, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}

	@Override
	public void d(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(L.Config.kLevelDebug, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}

	@Override
	public void i(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(L.Config.kLevelInfo, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}



	@Override
	public void w(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(L.Config.kLevelWarn, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}

	@Override
	public void e(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg) {
		logWrite2(L.Config.kLevelError, tag, filename, funcName, line, pid, tid, mainThreadId, msg);
	}


	public static native void setConsoleLogOpen(boolean isOpen);    //set whether the console prints log

	public static native void setLogLevel(int logLevel);

	public static native void setAppenderMode(int mode);

	public static native void appenderOpen(int level, int mode, String cacheDir, String logDir, String namePrefix,int cachelogdays, String pubKey);

	// method:appenderOpen, sig:(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V
	//public static native void appenderOpen(int level, int mode, String cacheDir, String logDir, String namePrefix, String pubKey);

	public static native void logWrite(XLoggerInfo logInfo, String log);
 //method:logWrite, sig:(Lcom/tencent/mars/xlog/Xlog$XLoggerInfo;Ljava/lang/String;)V
	public static native void logWrite2(int level, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String
			log);


	public native void appenderClose();

	public native void appenderFlush(boolean isSync);
	public static native void setMaxAliveTime(long aliveTime);
	public static native void setMaxFileSize(long fileSize);
	//method:setMaxAliveTime, sig:(J)V


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
