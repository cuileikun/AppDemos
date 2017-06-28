package com.qk.applibrary.api;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qk.applibrary.http.AsyncHttpClient;
import com.qk.applibrary.http.AsyncHttpResponseHandler;
import com.qk.applibrary.http.RequestParams;
import com.qk.applibrary.listener.ResponseResultListener;
import com.qk.applibrary.util.CommonUtil;
import com.qk.applibrary.util.LogUtil;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 作者：zhoubenhua
 * 时间：2016-10-31 13:23
 * 功能:调用普通接口抽象类,子类要实现requestSucessed和requestFailed方法
 */
public abstract class BaseApiAsyncTask {
	protected Context context;
	private String mUrl;
	private int requestType;// 1为get  2为post 3为delete
	private AsyncHttpClient http;
	protected ResponseResultListener mListener;
	protected String apiLogFileDirectory;//api 日志文件目录
	protected String apiLogFileName;//api 日志文件名字

	public BaseApiAsyncTask(Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
	}

	/**
	 * get请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param listener            回调接口
	 */
	public void get(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		this.requestType = 1;
		mUrl = url;
		doApiRequest(objectParams, null);
	}

	/**
	 * get请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param headParamMap        http头部参数map
	 * @param listener            回调接口
	 */
	public void get(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, HashMap<String, Object> headParamMap, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		mUrl = url;
		this.requestType = 1;
		doApiRequest(objectParams, headParamMap);
	}

	/**
	 * post请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param listener            回调接口
	 */
	public void post(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		mUrl = url;
		this.requestType = 2;
		doApiRequest(objectParams, null);
	}


	/**
	 * 表单 post请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param listener            回调接口
	 */
	public void postByForm(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		mUrl = url;
		this.requestType = 5;
		doApiRequest(objectParams, null);
	}

	/**
	 * 表单 post请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param listener            回调接口
	 */
	public void postByForm(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, HashMap<String, Object> headParamMap, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		mUrl = url;
		this.requestType = 5;
		doApiRequest(objectParams, headParamMap);
	}

	/**
	 * post请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param headParamMap        http头部参数map
	 * @param listener            回调接口
	 */
	public void post(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, HashMap<String, Object> headParamMap, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		mUrl = url;
		this.requestType = 2;
		doApiRequest(objectParams, headParamMap);
	}

	/**
	 * put请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param headParamMap        http头部参数map
	 * @param listener            回调接口
	 */
	public void put(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, HashMap<String, Object> headParamMap, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		mUrl = url;
		this.requestType = 4;
		doApiRequest(objectParams, headParamMap);
	}

	/**
	 * delete请求
	 *
	 * @param apiLogFileDirectory api日志文件目录
	 * @param apiLogFileName      api日志文件名
	 * @param url                 接口地址
	 * @param objectParams            请求参数
	 * @param headParamMap        http头部参数map
	 * @param listener            回调接口
	 */
	public void delete(String apiLogFileDirectory, String apiLogFileName, String url, Object objectParams, HashMap<String, Object> headParamMap, ResponseResultListener listener) {
		mListener = listener;
		this.apiLogFileName = apiLogFileName;
		this.apiLogFileDirectory = apiLogFileDirectory;
		mUrl = url;
		this.requestType = 3;
		doApiRequest(objectParams, headParamMap);
	}


	public void execute() {

	}

	private AsyncHttpClient getAsyncHttp(HashMap<String, Object> headParamMap) {
		if (http == null) {
			http = new AsyncHttpClient();
			http.setTimeout(1000 * 20);
		}
		if (headParamMap != null) {
			Iterator iterator = headParamMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				if(!CommonUtil.isEmpty((String) entry.getValue())) {
					http.addHeader((String) entry.getKey(), (String) entry.getValue());
				}
			}
		}
		return http;
	}

	/**
	 * 处理get请求
	 * @param client 网络请求工具
	 * @param params
	 * @param objectParams            请求参数
	 * @param headParamMap 头部参数
     */
	private void doGetRequest(AsyncHttpClient client, StringBuffer params, Object objectParams, HashMap<String, Object> headParamMap) throws Exception {
		params.append(copy(objectParams)).toString();
		String url = mUrl +"?"+params.toString();
		url = url.replaceAll(" ", "%20");
		LogUtil.log("[---------send-----" + "get" + "----]:" + url, apiLogFileDirectory, apiLogFileName);
		client.get(url, new AsyncHttpResponseHandler() {
			public void onSuccess(String response) {
				LogUtil.log("[---------result----GET-----]:" + response, apiLogFileDirectory, apiLogFileName);
				requestSucessed(response);
			}

			public void onFinish() {

			}

			public void onFailure(Throwable error, String content) {
				error.printStackTrace();
				requestFailed(error, content);
			}

		});
	}

	/**
	 * 处理post请求
	 * @param client 网络请求工具
	 * @param params
	 * @param objectParams            请求参数
	 * @param headParamMap 头部参数
	 */
	private void doPostRequest(AsyncHttpClient client, StringBuffer params, Object objectParams, HashMap<String, Object> headParamMap) throws Exception {
		Class<?> classType = objectParams.getClass();
		byte[] jsonBytes = null;
		if(classType.equals(com.alibaba.fastjson.JSONObject.class)) {
			com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject)objectParams;
			String jsonStr = json.toString();
			params.append(jsonStr);
			jsonBytes = jsonStr.getBytes();
		} else if(classType.equals(HashMap.class)) {
			HashMap<String,Object> paramMap = (HashMap<String,Object>)objectParams;
			Gson gson = new Gson();
			JsonObject jsonBody = new JsonObject();
			Iterator iterator = paramMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				jsonBody.add((String) entry.getKey(), gson.toJsonTree(entry.getValue()));
			}
			params.append(gson.toJson(jsonBody));
			jsonBytes = jsonBody.toString().getBytes();
		}
		HttpEntity entity = new ByteArrayEntity(jsonBytes);
		String contentType = "application/json;charset=UTF-8";
		LogUtil.log("[---------send-----" + "post" + "----]:" + mUrl + "?" + params.toString(), apiLogFileDirectory, apiLogFileName);
		client.post(null, mUrl, entity, contentType,
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						LogUtil.log("[---------result----POST-----]:"
								+ response, apiLogFileDirectory, apiLogFileName);
						requestSucessed(response);
					}

					public void onFinish() {

					}

					public void onFailure(Throwable error,
										  String content) {
						error.printStackTrace();
						requestFailed(error, content);
					}

				});
	}

	/**
	 * 检查value值类型
	 * @param entity
	 * @param params
	 */
    private void checkParamValueType(MultipartEntity entity, StringBuffer params, Map.Entry<String, Object> entry) throws Exception {
		if(params.length() > 0)
			params.append(",");
		params.append(entry.getKey());
		params.append(":");
		params.append(entry.getValue());
		if (entry.getValue() instanceof Bitmap) {
			Bitmap bit = (Bitmap) entry.getValue();
			ByteArrayOutputStream photoBao = new ByteArrayOutputStream();
			boolean successCompress = bit.compress(Bitmap.CompressFormat.JPEG, 100, photoBao);
			ByteArrayBody byteArrayBody = new ByteArrayBody(
					photoBao.toByteArray(), entry.getKey() + ".jpg");
			entity.addPart(entry.getKey(), byteArrayBody);
		} else {
			StringBody sb = new StringBody(entry.getValue()+"",
					Charset.forName(HTTP.UTF_8));
			entity.addPart(entry.getKey(), sb);
		}
	}

	/**
	 * 处理post请求
	 * @param client 网络请求工具
	 * @param params
	 * @param objectParams            请求参数
	 * @param headParamMap 头部参数
	 */
	private void doPostRequestByForm(AsyncHttpClient client, StringBuffer params, Object objectParams, HashMap<String, Object> headParamMap) throws Exception {
		Class<?> classType = objectParams.getClass();
		MultipartEntity entity = new MultipartEntity();
		if(classType.equals(com.alibaba.fastjson.JSONObject.class)) {
			com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject)objectParams;
			if(json.size()>0) {
				for (Map.Entry<String, Object> entry : json.entrySet()) {
					checkParamValueType(entity,params,entry);
				}
			}
		} else if(classType.equals(HashMap.class)) {
			HashMap<String,Object> paramMap = (HashMap<String,Object>)objectParams;
			Gson gson = new Gson();
			JsonObject jsonBody = new JsonObject();
			Iterator iterator = paramMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object>  entry = (Map.Entry<String, Object> ) iterator.next();
				checkParamValueType(entity,params,entry);
			}
		}
		LogUtil.log("[---------send-----" + "post" + "----]:" + mUrl + "?" + params.toString(), apiLogFileDirectory, apiLogFileName);
		client.post(null, mUrl, entity, null,
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						LogUtil.log("[---------result----POST-----]:"
								+ response, apiLogFileDirectory, apiLogFileName);
						requestSucessed(response);
					}

					public void onFinish() {

					}

					public void onFailure(Throwable error,
										  String content) {
						error.printStackTrace();
						requestFailed(error, content);
					}

				});
	}

	/**
	 * 处理put请求
	 * @param client 网络请求工具
	 * @param params
	 * @param objectParams            请求参数
	 * @param headParamMap 头部参数
	 */
	private void doPutRequest(AsyncHttpClient client, StringBuffer params, Object objectParams, HashMap<String, Object> headParamMap) throws Exception {
		Class<?> classType = objectParams.getClass();
		byte[] jsonBytes = null;
		if(classType.equals(com.alibaba.fastjson.JSONObject.class)) {
			com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject)objectParams;
			String jsonStr = json.toString();
			params.append(jsonStr);
			jsonBytes = jsonStr.getBytes();
		} else if(classType.equals(HashMap.class)) {
			HashMap<String,Object> paramMap = (HashMap<String,Object>)objectParams;
			Gson gson = new Gson();
			JsonObject jsonBody = new JsonObject();
			Iterator iterator = paramMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				jsonBody.add((String) entry.getKey(), gson.toJsonTree(entry.getValue()));
			}
			params.append(gson.toJson(jsonBody));
			jsonBytes = jsonBody.toString().getBytes();
		}
		HttpEntity entity = new ByteArrayEntity(jsonBytes);
		String contentType = "application/json;charset=UTF-8";
		LogUtil.log("[---------send-----" + "put" + "----]:" + mUrl + "?" + params.toString(), apiLogFileDirectory, apiLogFileName);
		client.put(null, mUrl, entity, contentType,
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						LogUtil.log("[---------result----put-----]:"
								+ response, apiLogFileDirectory, apiLogFileName);
						requestSucessed(response);
					}

					public void onFinish() {

					}

					public void onFailure(Throwable error,
										  String content) {
						error.printStackTrace();
						requestFailed(error, content);
					}

				});
	}

	/**
	 * 处理delete请求
	 * @param client 网络请求工具
	 * @param params
	 * @param objectParams            请求参数
	 * @param headParamMap 头部参数
	 */
	private void doDeleteRequest(AsyncHttpClient client, StringBuffer params, Object objectParams, HashMap<String, Object> headParamMap) throws Exception {
		params.append(copy(objectParams)).toString();
		String url = mUrl +"?"+params.toString();
		LogUtil.log("[---------send-----" + "delete" + "----]:" + url , apiLogFileDirectory, apiLogFileName);
		client.delete( url,new AsyncHttpResponseHandler() {
			public void onSuccess(String response) {
				LogUtil.log("[---------result----delete-----]:"
						+ response, apiLogFileDirectory, apiLogFileName);
				requestSucessed(response);
			}

			public void onFinish() {

			}

			public void onFailure(Throwable error,
								  String content) {
				error.printStackTrace();
				requestFailed(error, content);
			}

		});
	}

	/**
	 * 处理调用接口请求
	 * @param objectParams 请求参数
	 * @param headParamMap
     */
	private void doApiRequest(Object objectParams, HashMap<String, Object> headParamMap) {
		StringBuffer params = new StringBuffer();
		try {
			AsyncHttpClient client;
			if (headParamMap != null) {
				client = getAsyncHttp(headParamMap);
			} else {
				client = getAsyncHttp(null);
			}
			if (requestType == 1) {
				/**
				 * 处理get请求
				 */
				doGetRequest(client,params,objectParams,headParamMap);
			} else if (requestType == 2) {
				/**
				 * post请求
				 */
				doPostRequest(client,params,objectParams,headParamMap);

			} else if (requestType == 4) {
				/**
				 * put请求
				 */
				doPutRequest(client,params,objectParams,headParamMap);
			}else if (requestType == 3) {
				/**
				 * delete 请求
				 */
				doDeleteRequest(client,params,objectParams,headParamMap);
			}else if (requestType == 5) {
				/**
				 * post 表单 提交
				 */
				doPostRequestByForm(client,params,objectParams,headParamMap);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 请求接口成功
	 */
	public abstract void requestSucessed(String result);
	/**
	 * 请求接口失败
	 */
	public abstract  void requestFailed(Throwable error, String content) ;

	/**
	 * 封装请求参数
	 *
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private String copy(Object obj) throws IllegalArgumentException,
            SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
		Class<?> classType = obj.getClass();
		if(classType.equals(com.alibaba.fastjson.JSONObject.class)) {
			com.alibaba.fastjson.JSONObject json = (com.alibaba.fastjson.JSONObject)obj;
			StringBuilder result = new StringBuilder();
			if(json.size()>0) {
				for (Map.Entry<String, Object> entry : json.entrySet()) {
					if(result.length() > 0)
						result.append("&");
					result.append(entry.getKey());
					result.append("=");
					result.append(entry.getValue());
				}
			}
			return result.toString();
		} else if (classType.equals(RequestParams.class)) {
			return obj.toString();
		} else if(classType.equals(HashMap.class)) {
			StringBuilder result = new StringBuilder();
			HashMap<String,Object> map = (HashMap<String,Object>)obj;
			if(map.size()>0) {
				Iterator<Map.Entry<String,Object>> entrie = map.
						entrySet().iterator();
				while (entrie.hasNext()) {
					Map.Entry<String,Object> entry = entrie.next();
					if(result.length() > 0)
						result.append("&");
					result.append(entry.getKey());
					result.append("=");
					result.append(entry.getValue());
				}
			}
			return result.toString();
		} else {
			Object objectCopy = classType.getConstructor(new Class[]{})
					.newInstance(new Object[]{});
			Field[] fields = classType.getDeclaredFields();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				if (fieldName.equals("serialVersionUID")) {
					continue;
				}
				String stringLetter = fieldName.substring(0, 1).toUpperCase();
				String getName = "get" + stringLetter + fieldName.substring(1);
				String setName = "set" + stringLetter + fieldName.substring(1);
				Method getMethod = classType.getMethod(getName, new Class[]{});
				Method setMethod = classType.getMethod(setName,
						new Class[]{field.getType()});
				Object value = getMethod.invoke(obj, new Object[]{});
				if (value == null)
					value = "";
				sb.append("&" + fieldName + "=" + value);
				setMethod.invoke(objectCopy, new Object[]{value});
			}
			return sb.toString();

		}
	}
}
