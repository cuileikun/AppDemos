package com.qk.applibrary.api;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.qk.applibrary.bean.UploadFileBean;
import com.qk.applibrary.listener.ResponseResultListener;
import com.qk.applibrary.util.CommonUtil;
import com.qk.applibrary.util.LogUtil;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 作者：zhoubenhua
 * 时间：2016-10-31 13:23
 * 功能:调用上传文件抽象类,子类要实现requestSucessed和requestFailed方法
 */
public abstract class BaseUploadFileApiAsyncTask {
    protected ExecutorService pool;
    private Context mContext;
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    private UploadFileBean uploadPicBean;
    protected ResponseResultListener callPicListener;//回调图片接口
    public String mApiLogFileDirectory;//api 日志文件目录
    public String mApiLogFileName;//api 日志文件名字
    public String warnUploadPicFailed;//提醒上传图片失败

    public BaseUploadFileApiAsyncTask(Context context) {
        mContext = context;
        pool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
    }

    /**
     * 上传图片
     * @param callPicListener  回调给前端接口
     * @param mApiLogFileDirectory 请求api日志目录
     * @param mApiLogFileName 请求api日志文件名
     * @param uploadPicBean 上传图片参数
     * @param warnUploadPicFailed 因网络问题上传文件失败提示语
     */
    public void post(ResponseResultListener callPicListener, String mApiLogFileDirectory, String mApiLogFileName,
                     UploadFileBean uploadPicBean, String warnUploadPicFailed) {
        this.callPicListener = callPicListener;
        this.mApiLogFileDirectory = mApiLogFileDirectory;
        this.mApiLogFileName = mApiLogFileName;
        this.uploadPicBean = uploadPicBean;
        this.warnUploadPicFailed = warnUploadPicFailed;
        pool.submit(new UploadFileTask());
    }

    /**
     * 请求接口失败
     */
    protected abstract void requestFailed( String content) ;

    /**
     * 请求接口成功
     */
    protected abstract void requestSucessed(String result);

    /**
     * 封装消息
     * @param what
     * @param info 消息内容
     * @return
     */
    protected Message obtainMessage(int what, Object info) {
        Message msg = null;
        msg = this.handler.obtainMessage(what,info);
        return msg;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String response;
            switch(msg.what) {
                case SUCCESS_MESSAGE:
                    response = (String)msg.obj;
                    requestSucessed(response);
                    break;
                case FAILURE_MESSAGE:
                    response = (String)msg.obj;
                    requestFailed(response);
                    break;
            }
        }
    };

    /**
     * 上传文件任务
     */
    private class UploadFileTask implements Runnable {
        @Override
        public void run() {
            HashMap<String, Object> hashMap = new HashMap<>();
            ArrayList<FormFile> formFiles = new ArrayList<>();
            Iterator iterator = uploadPicBean.getParamMap().entrySet().iterator();
            String fileParameterName = uploadPicBean.getFileParamterName();
            int i = 0;
            StringBuffer paramStr = new StringBuffer();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                paramStr.append(i == 0 ? "" : "&");
                paramStr.append(entry.getKey() + "=" + entry.getValue());
                i++;
                /**
                 * 判断那些参数值是文件路径，如果是文件路径需要用文件上传
                 */
                if (((String) entry.getKey()).equals(fileParameterName )) {
                    Uri uri = (Uri) entry.getValue();
                    File file = new File(uri.getPath());
                    formFiles.add(new FormFile(file.getName(), (String) entry.getKey(), "image/jpeg", file.getAbsolutePath()));
                } else {
                    hashMap.put((String) entry.getKey(), entry.getValue());
                }
            }
            LogUtil.log("[---------send-----" + "post" + "----]:" + uploadPicBean.getUrl()+ "?" + paramStr.toString(),
                    mApiLogFileDirectory,mApiLogFileName);
            String result = null;
            result = uploadPicByStream(uploadPicBean.getUrl(), hashMap,
                    uploadPicBean.getHeadParamMap(),formFiles.toArray(new FormFile[]{}));
            /**
             * 检查调用上传文件接口有没有成功
             */
            if(result != null) {
                /**
                 * 调用成功
                 */
                handler.sendMessage(obtainMessage(SUCCESS_MESSAGE, result));
            } else {
                /**
                 * 调用失败
                 */
                if(CommonUtil.isEmpty(warnUploadPicFailed)) {
                    handler.sendMessage(obtainMessage(FAILURE_MESSAGE, warnUploadPicFailed));
                } else {
                    handler.sendMessage(obtainMessage(FAILURE_MESSAGE, "当前网络不稳定,请在网络良好的情况下重试"));
                }

            }
        }


        /**
         * 通过流上传图片
         * @param mUrl 连接服务器地址
         * @param params 参数
         * @param headParamsMap 头部参数
         * @param files 上传图片文件
         * @return 上传结果
         */
        private String uploadPicByStream(String mUrl,Map<String, Object> params,Map<String, Object> headParamsMap,FormFile[] files) {
            final String BOUNDARY = "a1a2a3"; //数据分隔线
            final String MULTIPART_FORM_DATA = "multipart/form-data";
            final String TWO_HYPHENS = "--";
            final String LINE_FEED = "\r\n";
            StringBuilder stringBuilder = null;
            try {
                URL url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);//允许输入
                if(headParamsMap.size()>0) {
                    Iterator iterator = headParamsMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        conn.setRequestProperty((String)entry.getKey(),(String)entry.getValue());
                    }
                }
                conn.setDoOutput(true);//允许输出
                conn.setUseCaches(false);//不使用Cache
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(20000);//设置连接超时
                conn.setReadTimeout(30000);//设置请求超时
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY);
                DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
                //上传的表单参数部分，格式请参考文章
                for (Map.Entry<String, Object> entry : params.entrySet()) {//构建表单字段内容
                    outStream.write((TWO_HYPHENS + BOUNDARY + LINE_FEED).getBytes("UTF-8"));
                    outStream.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_FEED).getBytes("UTF-8"));
                    outStream.write(LINE_FEED.getBytes("UTF-8"));
                    try {
                        outStream.write(entry.getValue().toString().getBytes("UTF-8"));
                    } catch (Exception error) {
                        if(error instanceof SocketTimeoutException || error instanceof ConnectTimeoutException || error instanceof ConnectException
                                || error instanceof InterruptedIOException || error instanceof SocketException){
                            return null;
                        } else {
                            error.printStackTrace();
                        }
                    }
                    outStream.write(LINE_FEED.getBytes("UTF-8"));
                }
                //上传的文件部分，格式请参考文章
                for (FormFile file : files) {
                    StringBuilder split = new StringBuilder();
                    split.append(TWO_HYPHENS);
                    split.append(BOUNDARY);
                    split.append(LINE_FEED);
                    split.append("Content-Disposition: form-data; name=\"" + file.getFormName() + "\"; filename=\"" + file.getFileName() + "\"" + LINE_FEED);
                    split.append("Content-Type: " + file.getContentType() + LINE_FEED);
                    split.append(LINE_FEED);
                    outStream.write(split.toString().getBytes("UTF-8"));
                    byte[] cache = new byte[1024];
                    int length = 0;
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file.getPath());

                        while ((length = fileInputStream.read(cache)) != -1) {
                            outStream.write(cache, 0, length);
                        }
                        fileInputStream.close();
                    } catch (Exception error) {
                        if(error instanceof SocketTimeoutException || error instanceof ConnectTimeoutException || error instanceof ConnectException
                                || error instanceof InterruptedIOException || error instanceof SocketException){
                            return null;
                        } else {
                            error.printStackTrace();
                        }
                    }
                    outStream.write(LINE_FEED.getBytes("UTF-8"));
                }
                outStream.write((TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_FEED).getBytes("UTF-8"));//数据结束标志
                outStream.flush();
                outStream.close();
                int responseStatusCode = conn.getResponseCode();
                if (responseStatusCode != 200) {
                    conn.disconnect();
                    return null;
                }
                InputStream stream = conn.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream, "UTF-8");

                int ch;
                stringBuilder = new StringBuilder();
                while ((ch = reader.read()) != -1) {
                    stringBuilder.append((char) ch);
                }
                conn.disconnect();
            } catch (Exception error) {
                if(error instanceof SocketTimeoutException || error instanceof ConnectTimeoutException || error instanceof ConnectException
                        || error instanceof InterruptedIOException || error instanceof SocketException){
                    return null;
                } else {
                    error.printStackTrace();
                }
            }
            if (stringBuilder == null || TextUtils.isEmpty(stringBuilder.toString())) {
                return null;
            }
            String jsonString = stringBuilder.toString();
            LogUtil.log("[----result-----sucess----"+mUrl+"-----]:"+jsonString,mApiLogFileDirectory, mApiLogFileName);
            return jsonString;
        }
    }



}
