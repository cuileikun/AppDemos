package com.appdemo.widget;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 作者：popular cui
 * 时间：2017/6/7 13:40
 * 功能:app更新进度框
 */
public class SongUpdateDialog {
    ProgressBar progressBar;//下载进度条
    /* 记录进度条数量 */
    private int progress;
    private Dialog mDownloadDialog;
    private String fileName;
    private String downloadUrl;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    private static final int NETWORK_ERROR = 3;
    private Context context;
    private String mSaveFilePath;//保存apk路径

    private UpDataListener upDataListener;

    /**
     * 更新进度框
     *
     * @param context      上下文
     * @param downloadUrl  下载地址
     * @param saveFilePath 保存apk路径
     */
    public SongUpdateDialog(Context context, String downloadUrl, String saveFilePath, String saveFileName) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        mSaveFilePath = saveFilePath;
        this.fileName = saveFileName;
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(com.qk.applibrary.R.layout.update_progress, null);
        progressBar = (ProgressBar) v.findViewById(com.qk.applibrary.R.id.update_progress_pb);
        // 构造软件下载对话框
        Builder builder = new Builder(context);
        builder.setTitle("正在更新歌曲");
        // 给下载对话框增加进度条
        builder.setView(v);
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        mDownloadDialog.setCancelable(false);

    }

    public void beginDown() {
        new DownloadApkThread().start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    progressBar.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    //歌曲下载成功
                    if (upDataListener != null) {
                        upDataListener.UpDataComplete();
                    }
                    mDownloadDialog.dismiss();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    if (mDownloadDialog != null)
                        mDownloadDialog.dismiss();
                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class DownloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1,
//                            downloadUrl.length());
                    URL url = new URL(downloadUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSaveFilePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }else{//存在目录 也有可能存在文件
                        File mFile = new File(fileName);
                        mFile.delete();
                    }
                    File mFile = new File(fileName);
                    if (mFile.exists()) {
                        Log.e("yan", mFile.getPath() + "/" + mFile.getName() + "=======file size====" + file.length());
//                        Toast.makeText(context, "已经存在了", Toast.LENGTH_SHORT).show();
                    }

                    FileOutputStream fos = new FileOutputStream(mFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    int numread;
                    do {
                        // 写入到文件中

                        numread = is.read(buf);
                        if(numread<0){
                            continue;
                        }
//                        Log.e("yan", "numread" + numread + "");
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);

                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (numread > 0);
                    if (numread <= 0) {
                        // 下载完成
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                    }
                    fos.close();
                    is.close();
                } else {
                    Message msg = mHandler.obtainMessage(NETWORK_ERROR);
                    msg.obj = "没有SD卡";
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = mHandler.obtainMessage(NETWORK_ERROR);
                msg.obj = "下载失败";
                mHandler.sendMessage(msg);
            }
        }
    }

    public interface UpDataListener {
        void UpDataComplete();
    }


    public void setUpDataListener(UpDataListener upDataListener) {
        this.upDataListener = upDataListener;
    }
}
