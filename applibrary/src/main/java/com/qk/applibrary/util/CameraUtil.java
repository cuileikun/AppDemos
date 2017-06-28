package com.qk.applibrary.util;

import android.hardware.Camera;

import java.util.Comparator;
import java.util.List;

/**
 * 作者：zhoubenhua
 * 时间：2017-3-8 10:27
 * 功能:摄像头工具
 */
public class CameraUtil {

    /**
     * 获取摄像头预览尺寸
     * @param camera 摄像头
     * @return 预览尺寸
     */
    public static List<Camera.Size> getCameraSupportPreviewSizesList(Camera camera)
    {
        Camera.Parameters parameters = camera.getParameters();
        return parameters.getSupportedPreviewSizes();
    }


    public static class ResolutionComparator implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if(lhs.height!=rhs.height)
                return lhs.height-rhs.height;
            else
                return lhs.width-rhs.width;
        }

    }
}
