package com.tencent.mars.xlog;

/**
 * Created by joybar on 2019/1/14.
 */

 interface XLogService {


	void v(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg);

	void i(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg);

	void d(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg);

	void w(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg);

	void e(String tag, String filename, String funcName, int line, int pid, long tid, long mainThreadId, String msg);
}
