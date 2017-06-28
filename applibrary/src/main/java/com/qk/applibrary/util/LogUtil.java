package com.qk.applibrary.util;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志工具
 */
public class LogUtil {
	private static String TAG = "LogUtil";
	static boolean logControl = true;
	/**
	 * 过滤敏感关键字 不要将敏感关键字数据写到日志文件中
	 */
	public static Map<String,String> filterSensitiveKeywordMap;

	static {
		filterSensitiveKeywordMap = new HashMap<String,String>();
	}

	/**
	 * 添加过滤敏感关键字
	 * @param filterKeys 添加要过滤敏感关键字数组
	 */
	public static void addFilterSensitiveKeywords(String[] filterKeys) {
		for(String key:filterKeys) {
			filterSensitiveKeywordMap.put(key,key);
		}
	}

	/**
	 * 要删除过滤敏感关键字
	 * @param filterKeys 删除要过滤敏感关键字数组
	 */
	public static void removeSensitiveKeywords(String[] filterKeys) {
		if(filterSensitiveKeywordMap.size()>0) {
			for(String key:filterKeys) {
				filterSensitiveKeywordMap.remove(key);
			}
		}	
	}

	/**
	 * 保存日志
	 * @param savePathStr 目录路径
	 * @param saveFileNameS 文件名
	 * @param saveDataStr 日期时间
	 * @param saveTypeStr
	 */
	public static void saveLog(String savePathStr, String saveFileNameS, String saveDataStr, boolean saveTypeStr) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			File folder = new File(savePathStr);
			if(folder.exists() == false || folder.isDirectory() == false){
				folder.mkdirs();
			}
			File f = new File(folder, saveFileNameS);
			fw = new FileWriter(f, saveTypeStr);

			pw = new PrintWriter(fw);
			pw.print(saveDataStr+"\r\n");
			pw.flush();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {}
			}
			if(pw != null){
				pw.close();
			}
		}
	}

	/**
	 *
	 * @param log 日志信息
	 * @param directoryPath 目录路径
	 * @param fileName 文件名
	 */
	public static void log(String log,String directoryPath,String fileName) {
		if (logControl) {
			Log.d(TAG, log);
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File path = new File(directoryPath);
				String url = null;//请求地址
				if(log.indexOf("{")!=-1 && log.indexOf("}")!=-1) {
					url = log.substring(0,log.indexOf("{"));
				}
				if(!CommonUtil.isEmpty(url)) {
					try {
						log = log.substring(url.length(),log.length());
						JSONObject jsonObject = JSONObject.parseObject(log);
						/**
						 * 判断要不要过滤敏感关键字
						 */
						if(filterSensitiveKeywordMap.size()>0) {
							for(String key:filterSensitiveKeywordMap.keySet()) {
								if(jsonObject.containsKey(key)) {
									jsonObject.remove(key);
								}
							}
						}
						log = url + jsonObject.toString();
					} catch (Exception e) {
						e.printStackTrace();
						log = url+log;
					}
				}
  			   saveLog(path.getPath(), fileName,CommonUtil.getCurrentDate()+":"+log, true);
			}
		}
	}

}