package com.appdemo.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.qk.applibrary.api.BaseApiAsyncTask;
import com.qk.applibrary.util.CommonUtil;

/**
 * 作者：popular cui
 * 时间：2017/6/7 13:40
 * 功能:网络通讯类
 */
public class HouseResourceAPIAsyncTask extends BaseApiAsyncTask {

    public HouseResourceAPIAsyncTask(Context context) {
        super(context);

    }

    @Override
    public void requestSucessed(String result) {
        if (!CommonUtil.isEmpty(result)) {
            try {
                JSONObject jsons = JSON.parseObject(result);
                HouseResourceResponseResult data = new HouseResourceResponseResult();
                if (jsons.containsKey("message")) {
                    data.message = jsons.getString("message");
                }
                if (jsons.containsKey("result")) {
                    data.code = jsons.getInteger("result");
                }
                if (jsons.containsKey("code")) {
                    data.code = jsons.getInteger("code");
                }
                if (jsons.containsKey("data")) {
                    data.data = jsons.getString("data");
                } else {
                    data.data = result;
                }
              

                mListener.onResult(data);
            } catch (JSONException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    @Override
    public void requestFailed(Throwable error, String content) {

        HouseResourceResponseResult result = new HouseResourceResponseResult();
        result.code = -1;
        result.message = "连接失败";
        mListener.onResult(result);
    }


}
