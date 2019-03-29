package com.tencent.utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Environment;

import com.tencent.mars.xlog.BuildConfig;
import com.tencent.mars.xlog.XLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by joybar on 2019/1/14.
 */

public class LogUtils {

	/**
	 *
	 */
	private static final String tag = "LogUtils";
	private static String DIR_NAME = "/marssample";
	private static String DEBUG_DIR = "Debug";
	private static String DIR_LOG = "/log";
	private static String DIR_LOG_CACHE = "/cache";

	private static String SdCard = getSdCard();
	private static final String RootDirPathOnSdcard = SdCard + DIR_NAME + (BuildConfig.DEBUG ? DEBUG_DIR : "");

	public static String LOG_PATH = RootDirPathOnSdcard + DIR_LOG;
	// this is necessary, or may crash for SIGBUS
	public static String LOG_CACHE_PATH = LOG_PATH + DIR_LOG_CACHE;
	public static boolean CONSOLE_LOG_OPEN = BuildConfig.DEBUG ? true : false;

//	public static String PUB_KEY =
//			"572d1e2710ae5fbca54c76a382fdd44050b3a675cb2bf39feebe85ef63d947aff0fa4943f1112e8b6af34bebebbaefa1a0aae055d9259b89a1858f7cc9af9df1";

	public static String PUB_KEY =
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdSSJVaywlDvdMaqhzeBAFRk0E4AfzkMKwNQ5jXZYPxQohI1Gw4xXX5W1nmeNozaYk7rICHYkPyIwZa9rdQ3LAXoZiIG8t6WEjAUjluEI7q1pZ1R+7MszWSJeLC4+ECTRwuA2OxuZdiJKBsSu8GQfs/8XeQ72a8+LJrMc7iOXmxwIDAQAB";


	public static String logFileName() {
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HH:mm");
		return df.format(day);
	}

	private static String getSdCard() {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !isExternalStorageRemovable()) {
				return Environment.getExternalStorageDirectory().toString();

			} else {
				return Environment.getDataDirectory().toString();
			}
		} catch (Exception e) {
			return Environment.getDataDirectory().toString();
		}
	}


	public static void zipPreviousLogs(String logPath, boolean removeAfterZip, boolean isAsyncHandle) {
		File file = new File(logPath);
		if (!file.exists() || !file.isDirectory()) {
			return;
		}
		File[] logFiles = LogUtils.findSDCardFile(logPath, "log");
		if (logFiles == null) {
			return;
		}
		ArrayList<File> legalFiles = new ArrayList<File>();


		for (File logFile : logFiles) {
			legalFiles.add(logFile);
		}

		if (isAsyncHandle) {
			ZipLogTask task = new ZipLogTask(legalFiles, removeAfterZip);
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			zipFile(legalFiles, removeAfterZip);
		}

	}

	private static class ZipLogTask extends AsyncTask<Void, Void, Void> {

		private ArrayList<File> files;
		private boolean removeAfterZip;

		public ZipLogTask(ArrayList<File> files, boolean removeAfterZip) {
			this.files = files;
			this.removeAfterZip = removeAfterZip;
		}

		@Override
		protected Void doInBackground(Void... params) {
			zipFile(files, removeAfterZip);
			return null;
		}
	}

	private static void zipFile(ArrayList<File> files, boolean removeAfterZip) {
		if (files == null) {
			return;
		}
		for (File f : files) {
			String fileName = f.getName();
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length()))) {
				String pureFileName = fileName.substring(0, dot);
				zipFiles(f.getParent() + File.separator + pureFileName + ".zip", new File[]{f});
				if (removeAfterZip) {
					f.delete();
				}
			}
		}
	}

	private static void zipFiles(String zipFile, File[] files) {
		File f = new File(zipFile);
		if (f.exists() && f.isFile()) {
			f.delete();
		}

		FileOutputStream fout = null;
		ZipOutputStream zout = null;
		FileInputStream fin = null;
		try {
			byte[] buffer = new byte[1024];
			fout = new FileOutputStream(zipFile);
			zout = new ZipOutputStream(fout);

			for (int i = 0; i < files.length; i++) {
				fin = new FileInputStream(files[i]);
				String entryName = files[i].getName();
				ZipEntry ze = new ZipEntry(entryName);
				ze.setTime(files[i].lastModified());
				zout.putNextEntry(ze);
				int length;
				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}
				zout.closeEntry();
				fin.close();
			}

			zout.close();

		} catch (IOException ioe) {
			XLogger.e(tag, ioe.getStackTrace().toString());
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static File[] findSDCardFile(String path, final String fileType) {
		File dir = new File(path);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					return (filename.endsWith(fileType));
				}
			});
			if (files == null) {
				return null;
			}
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			try {
				Arrays.sort(files, new Comparator<File>() {
					@Override
					public int compare(File str1, File str2) {
						if (str1.lastModified() - str2.lastModified() <= 0) {
							return 1;
						} else {
							return -1;
						}
					}
				});
			} catch (Exception e) {
			}
			return files;
		}
		return null;
	}

	@TargetApi(9)
	private static boolean isExternalStorageRemovable() {
		return Environment.isExternalStorageRemovable();
	}
}
