package com.qk.applibrary.listener;

import com.qk.applibrary.api.BaseResponseResult;

/**
 * 服务器结果回调
 * @author benhua
 *
 */
public abstract class ResponseResultListener<T> {
	public abstract void onResult(T result);
}
