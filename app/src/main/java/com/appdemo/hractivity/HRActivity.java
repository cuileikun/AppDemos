package com.appdemo.hractivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appdemo.R;
import com.appdemo.api.BuildConfigDemo;
import com.appdemo.api.HouseResourceAPIAsyncTask;
import com.appdemo.api.HouseResourceResponseResult;
import com.appdemo.api.Protocol;
import com.appdemo.base.HouseResourceBaseActivity;
import com.appdemo.constants.Constants;
import com.appdemo.hractivity.rank.RealTimeActivity;
import com.appdemo.utils.ZipUtils;
import com.appdemo.widget.SongUpdateDialog;
import com.qk.applibrary.listener.ResponseResultListener;
import com.qk.applibrary.util.CommonUtil;
import com.qk.applibrary.util.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class HRActivity extends HouseResourceBaseActivity implements View.OnClickListener {
    public static HRActivity mInstance = null;
    private Context context;
    private Button btn_rank;

    @Override
    public void initViews() {
        mInstance=this;
        context=this;
        btn_rank = (Button) findViewById(R.id.btn_rank);
        //获得当前服务器的歌曲信息
        getSongsList();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {
        btn_rank.setOnClickListener(HRActivity.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hr;
    }

    /**
     * 得到歌曲列表
     */
    private void getSongsList() {
        if (CommonUtil.checkNetwork(context)) {
            showProgressDialog(null, "加载中");
            //歌曲下载 接口1
            String testUrl = BuildConfigDemo.getInstance().getConnect().getApiSongUrl() + Protocol.GET_SONG_VERSION;
            HouseResourceAPIAsyncTask asyncTask = new HouseResourceAPIAsyncTask(context);

            HashMap<String, Object> params = new HashMap<String, Object>();

            String apiLogFileDirectory = Constants.Log_Def.LogDirectory;
            String apiLogFileName = Constants.Log_Def.Api_Log_File;

            asyncTask.post(apiLogFileDirectory, apiLogFileName, testUrl, params, new ResponseResultListener() {
                @Override
                public void onResult(Object object) {
                    //歌曲的下载链接
                    dissmissProgressDialog();
                    HouseResourceResponseResult responseResult = (HouseResourceResponseResult) object;
                    if (responseResult.code == HouseResourceResponseResult.SUCESS_CODE) {
                        try {
                            /**
                             * 请求成功
                             */
                            JSONObject json = new JSONObject(responseResult.data);

                            if (json.has("version") && json.has("url") && json.has("fileSize")) {
                                String version = (String) json.get("version");
                                Long fileSize = json.getLong("fileSize");
                                String downLoadUrl = (String) json.get("url");
                                checkOrDownLoad(version, fileSize, downLoadUrl);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!com.qk.applibrary.util.CommonUtil.isEmpty(responseResult.message)) {
                        /**
                         * 请求失败
                         */
                        com.qk.applibrary.util.CommonUtil.sendToast(context, responseResult.message);
                        dissmissProgressDialog();
                    }

                }
            });
        }
    }

    /**
     * 判断本地是否有歌曲，版本不一致 ：删除 zip和缓存后 下载  版本一致zip包大小不一致 删除zip 后下载 ;没有歌曲：下载
     *
     * @param version
     * @param fileSize
     * @param url
     */
    private void checkOrDownLoad(String version, Long fileSize, String url) {
        String saveSongPath = Constants.Song_Def.DOWN_SONGS_SAVE_PATH;
        String saveSongName = Constants.Song_Def.DOWN_SONGS_SAVE_PATH + File.separator + Constants.Song_Def.DOWN_SONGS_SAVE_NAME;


        if (hasSongVersionChanged(version, saveSongPath, saveSongName)) {//本地没有当前版本或者本地是第一次使用版本
            SharedPreferencesUtil.setSetting("songs", context, "songListVersion", version);//储存当前的版本信息
        }
        //        是否存在解压后的歌曲的路径
        File downSongsUpzipPath = new File(Constants.Song_Def.DOWN_SONGS_UPZIP_PATH);
        File zipFile = new File(Constants.Song_Def.DOWN_SONGS_SAVE_PATH + File.separator + Constants.Song_Def.DOWN_SONGS_SAVE_NAME);
        if (hasSongZip(saveSongPath, saveSongName, fileSize)) {//有zip文件
            if (hasUpSongZip(downSongsUpzipPath)) {//已经存在解压后的文件
                //判断
                File[] files = downSongsUpzipPath.listFiles();
                if (files == null || files.length <= 10) {
                    try {
                        ZipUtils.upZipFile(zipFile, Constants.Song_Def.DOWN_SONGS_UPZIP_PATH);
                    } catch (IOException e) {
                        CommonUtil.sendToast(context,"解压失败");
                    }
                } else {

                }
            } else {//去解压
                //去解压
                try {
                    ZipUtils.upZipFile(zipFile, Constants.Song_Def.DOWN_SONGS_UPZIP_PATH);
                } catch (IOException e) {
                    CommonUtil.sendToast(context,"解压失败");
                }
            }
        } else {//没有zip文件，去下载zip
            SongUpdateDialog songUpdateDialog = new SongUpdateDialog(context, url, saveSongPath, saveSongName);
            songUpdateDialog.setUpDataListener(new SongUpdateDialog.UpDataListener() {
                @Override
                public void UpDataComplete() {
                    Toast.makeText(context, "更新歌曲成功", Toast.LENGTH_SHORT).show();

                    //去解压
                    try {
                        File mZipFile = new File(Constants.Song_Def.DOWN_SONGS_SAVE_PATH + File.separator + Constants.Song_Def.DOWN_SONGS_SAVE_NAME);
                        ZipUtils.upZipFile(mZipFile, Constants.Song_Def.DOWN_SONGS_UPZIP_PATH);
                    } catch (IOException e) {
                        Toast.makeText(context, "解压失败", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            songUpdateDialog.beginDown();
        }


    }


    /**
     * 判断本地是否有歌曲，和目录 有歌曲和目录就不需要去服务器下载了
     */
    private boolean hasSongZip(String saveSongPath, String saveName, Long fileSize) {
        //下载的歌曲zop
        File file = new File(saveSongPath);

        if (file.exists()) {
            Log.e("cui", "存在路径");
            File zipFile = new File(saveName);
            if (zipFile.exists()) {//存在
                if (fileSize == zipFile.length()) {
                    Log.e("cui", "完整的zip");// 能否做判断 如果当前版本zip 解压后的播放曾经失败 可以在这里清理缓存 进行重新解压？？
                    return true;
                } else {
                    clearSongCache(saveSongPath, saveName);
                    return false;
                }
            } else {
                Log.e("cui", "不存在zip");
                return false;
            }
        } else {
            Log.e("cui", "不存在路径");
            return false;
        }
    }

    /**
     * 删除缓存的上一版本音乐
     */
    private void clearSongCache(String saveSongPath, String saveName) {
        //下载的歌曲路径
        File file = new File(saveSongPath);
        //下载的歌曲zip
        File fileZip = new File(saveName);
        if (file.exists()) {
            if (fileZip.exists()) {
                fileZip.delete();
            }
        }
        //删除解压目录的歌曲缓存
        File downSongsUpzipPath = new File(Constants.Song_Def.DOWN_SONGS_UPZIP_PATH);
        if (downSongsUpzipPath.exists()) {
            File[] files = downSongsUpzipPath.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    /**
     * 判断本地是否有解压后的歌曲
     */
    private boolean hasUpSongZip(File downSongsUpzipPath) {

        //如果是文件夹的话
        if (downSongsUpzipPath.isDirectory()) {
            //返回文件夹中有的数据
            File[] files = downSongsUpzipPath.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if (null == files) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 本地版本歌曲版本是否发生改变
     *
     * @return
     */
    private boolean hasSongVersionChanged(String songVersion, String saveSongPath, String saveName) {
        String lcaSongVersion = SharedPreferencesUtil.getSetting("songs", context, "songListVersion");
        if ("".equals(lcaSongVersion)) {//第一次使用 发生变化
            clearSongCache(saveSongPath, saveName);
            return true;
        }
        if (lcaSongVersion.equals(songVersion)) {//版本一致 不需要更新
            return false;
        } else {
            //版本不一致 删除本地的zip缓存和歌曲缓存
            clearSongCache(saveSongPath, saveName);
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_rank:
                startActivity(new Intent(HRActivity.this, RealTimeActivity.class));
            break;
        }

    }



}
