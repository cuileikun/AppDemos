package com.qk.applibrary.util;

import android.app.Activity;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.qk.applibrary.listener.VideoCompressionInitializationListener;
import com.qk.applibrary.listener.VideoCompressionProgressListener;


/**
 * 作者：zhoubenhua
 * 时间：2017-3-22 14:49
 * 功能:视频压缩工具
 */
public class VideoCompressionUtil {
    public FFmpeg ffmpeg;//用ffmpeg命令压缩视频
    public VideoCompressionUtil(Activity activity){
        ffmpeg = FFmpeg.getInstance(activity);
    }

    /**
     * 视频压缩初始化
     * @param initListener 初始化接口
     */
    public void videoCompressionInitialization(final VideoCompressionInitializationListener initListener) {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {}

                @Override
                public void onFailure() {
                    initListener.onInitFail("incompatible with this device");
                }

                @Override
                public void onSuccess() {
                    initListener.oninitSuccess();
                }
                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param compressVideoFilePath 要压缩视频文件路径
     * @param compressVideoFilePath 压缩后视频文件路径
     * @param progressListener 压缩回调接口
     */
    public void videoCompressionProgress(String compressVideoFilePath,String compressAfterVideoFilePath,final VideoCompressionProgressListener progressListener) {
        try {
            String cmd =   "-y -i "+compressVideoFilePath +" -strict -2 -vcodec libx264 -preset ultrafast -crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x352 -aspect 16:9 "+compressAfterVideoFilePath;
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {
                    progressListener.onCompressionProgress(message);}

                @Override
                public void onFailure(String message) {
                    progressListener.onCompressionFail(message); }

                @Override
                public void onSuccess(String message) {
                    progressListener.onCompressionSuccess(message);
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

}
