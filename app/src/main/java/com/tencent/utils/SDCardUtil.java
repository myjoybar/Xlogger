package com.tencent.utils;

import android.annotation.TargetApi;
import android.os.Environment;

import com.tencent.mars.xlog.BuildConfig;


/**
 * Created by joybar on 2019/1/11.
 */

public class SDCardUtil {

	/** 存储卡 */
	public static String DIR_NAME = "Dingtone11111";
	public static String SdCard = getSdCard();
	public static final String RootDirPathOnSdcard = SdCard + "/" + DIR_NAME +
			(BuildConfig.DEBUG ? "Debug" : "") + "/";
	public static String LogPath = RootDirPathOnSdcard + "log/";

	public static String getSdCard()
	{
		try{
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
					!isExternalStorageRemovable()) {
				return Environment.getExternalStorageDirectory().toString();

			} else {
				return Environment.getDataDirectory().toString();
			}
		}catch (Exception e){
			return Environment.getDataDirectory().toString();
		}
	}


	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		return Environment.isExternalStorageRemovable();
	}

}
