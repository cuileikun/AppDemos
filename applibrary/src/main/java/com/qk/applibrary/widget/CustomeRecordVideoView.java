package com.qk.applibrary.widget;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.qk.applibrary.R;
import com.qk.applibrary.util.CommonUtil;
import java.io.File;

 public class CustomeRecordVideoView extends RelativeLayout implements OnErrorListener,Camera.AutoFocusCallback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private int mWidth;
    private int mHeight;
    private boolean isOpenCamera;
    private boolean isRecording = false;
    private Context mContext;



    public CustomeRecordVideoView(Context context) {
        this(context, null);
        mContext=context;
    }
 
    public CustomeRecordVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext=context;
    }
 
    public CustomeRecordVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext=context;
        mWidth= 320;
        mHeight=240;
        isOpenCamera=true;
        View moiveRecorderView = LayoutInflater.from(context).inflate(R.layout.custome_video_recorder_component, this);
        mSurfaceView = (SurfaceView)moiveRecorderView.findViewById(R.id.surface_view);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }


    private class CustomCallBack implements Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if(!CommonUtil.isSdcardExist()){
                Toast.makeText(mContext,"sdcard",Toast.LENGTH_SHORT).show();
            }
            if (!isOpenCamera)
                return;
            try {
                initCamera();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
 
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
 
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            realeaseCameraResource();
        }
 
    }


    private boolean initCamera()  {
        try {
            if (mCamera != null) {
                realeaseCameraResource();
            }
            try {
                mCamera = getCameraInstance();
            } catch (Exception e) {
                e.printStackTrace();
                realeaseCameraResource();
            }
            if (mCamera == null)
                return false;
            setCameraParams();
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            mCamera.autoFocus(this);
            mCamera.unlock();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

     @Override
     public void onAutoFocus(boolean success, Camera camera) {
         // TODO Auto-generated method stub
         if (success) {
             System.out.println(">>>>>>>>success");
         }else{
             camera.autoFocus(this);//如果失败，自动聚焦
         }
     }
/** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance() {
        Camera c = null;
        try
        {
//            if(isSupportCameraFacing(1)){
//                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
//            }
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e)
        {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    private void setCameraParams() {
        if (mCamera != null) {
            Parameters mParams = mCamera.getParameters();
            mParams.setFocusMode(Parameters.FOCUS_MODE_AUTO);
            mParams.set("orientation", "portrait");
            mCamera.setParameters(mParams);
        }
    }

    private boolean initRecord(File recordFile)  {
        try {
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
            }
            mMediaRecorder.reset();
            if(mCamera==null){
                initCamera();
                mMediaRecorder.setCamera(mCamera);
            }else  if (mCamera != null){
                mCamera.stopPreview();
                mMediaRecorder.setCamera(mCamera);
            }

            mMediaRecorder.setOnErrorListener(this);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setVideoSize(640, 480);//
            mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
            mMediaRecorder.setOutputFile(recordFile.getAbsolutePath());
            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            mMediaRecorder.prepare();
            //  mMediaRecorder.setOnInfoListener(this);
            mMediaRecorder.setOnErrorListener(this);
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void startRecordVideo(File recordFile) {
        if(!CommonUtil.isSdcardExist()){
            Toast.makeText(mContext,"sdcard",Toast.LENGTH_SHORT).show();
        }
         if(!isOpenCamera){
             initCamera();
         }
         initRecord(recordFile);
    }


    public void stop() {
        releaseRecordResource();
        realeaseCameraResource();
    }




    public void realeaseCameraResource() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public void releaseRecordResource() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
                isRecording=false ;
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }

    /**
     * 停止录制

     */
    public void stopRecord() {
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
                isRecording=false ;       //标记为false
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }



    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断当前手机是否支持前置摄像头还是后置摄像头
     * @param cameraFcingFlag 1是前置摄像头  2是后置摄像头
     * @return 是否支持
     */
    public static boolean isSupportCameraFacing(int cameraFcingFlag) {
        boolean flag = false;
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (cameraFcingFlag == info.facing) {
                flag = true;
                break;
            }
        }
        return flag;
    }


}

