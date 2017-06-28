package com.appdemo.hractivity.rank;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.appdemo.R;
import com.appdemo.adapter.RealTimeAdapter;
import com.appdemo.api.BuildConfigDemo;
import com.appdemo.api.HouseResourceAPIAsyncTask;
import com.appdemo.api.HouseResourceResponseResult;
import com.appdemo.api.Protocol;
import com.appdemo.base.HouseResourceBaseActivity;
import com.appdemo.bean.PlayingSongEntity;
import com.appdemo.bean.RealTimeRankEntity;
import com.appdemo.bean.RealTimeRankList;
import com.appdemo.constants.Constants;
import com.appdemo.dialog.RealTimeChangeListener;
import com.appdemo.dialog.RealTimeDialog;
import com.appdemo.utils.Util;
import com.appdemo.widget.MyTopbarView;
import com.qk.applibrary.listener.ResponseResultListener;
import com.qk.applibrary.listener.TopbarImplListener;
import com.qk.applibrary.util.CommonUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
public class RealTimeActivity extends HouseResourceBaseActivity {
    public static RealTimeActivity mInstance = null;
    private Context context;
    private MyTopbarView topbarView;
    private TextView currentTime;
    private ListView realTimeLv;
    private Button scanBt;
    private RealTimeAdapter adapter;
    private List<RealTimeRankEntity> list;
    static int i = 1;//listView自动滚动每次滚动的像素点
    private Timer autoUpdate;//listView自动向上
    private Timer autoGetNewData;//获得实施榜推送列表timer
    //    private Timer autoCloseDialog;
    private boolean isStartUpadte = false;//listView是否开始了自动滚动
    private RelativeLayout relativeLayout;
    RealTimeDialog realTimeDialog;
    private boolean ifComeNextActivity = false;//是否进入下一个页面
    private boolean ifStartTimer = false;//是否启动三分钟获取实时榜数据

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int type;//区分是否是第一次进去  第一次 type=0；type=1；

//    private Stack<String> songStack;


    private boolean isPause;
    private LinkedList<PlayingSongEntity> songQueueList;
    private String formatCurrentDay;
    private int ordinal;
    private boolean ifResumeLoadPushData;//保证从BuyerRankListActivity返回来的时候调用三分钟刷新的接口，不调用初期数据接口
    private String deviceName;//当前登录的设备名称

    @Override
    public void initViews() {
        mInstance = this;
        context = RealTimeActivity.this;
        autoUpdate = new Timer();
        autoGetNewData = new Timer();
//        autoCloseDialog = new Timer();
        topbarView = (MyTopbarView) findViewById(R.id.top_bar_view);
        currentTime = (TextView) findViewById(R.id.current_time);
        realTimeLv = (ListView) findViewById(R.id.real_time_lv);
        scanBt = (Button) findViewById(R.id.scan_paihangbang_bt);
        relativeLayout = (RelativeLayout) findViewById(R.id.re);
        list = new ArrayList<RealTimeRankEntity>();
        adapter = new RealTimeAdapter(context, list);

    }

    @Override
    public void initData() {
        //detele by clk on
        // SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
        //currentTime.setText((df.format(new Date())) + "实时榜");
        //detele by clk off
        topbarView.setTopbarTitle("采购实时榜");
        realTimeLv.setAdapter(adapter);

    }

    public void setIfLoadPushData() {
        ifResumeLoadPushData = true;
    }


    @Override
    public void addListeners() {
        topbarView.setTopBarClickListener(topListener);
        scanBt.setOnClickListener(rankListListener);
    }

    private TopbarImplListener topListener = new TopbarImplListener() {
        @Override
        public void leftClick() {
            RealTimeActivity.this.finish();
        }

    };
    private View.OnClickListener rankListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            //TODO
//            Intent intent = new Intent(RealTimeActivity.this, BuyerRankListActivity.class);
//            context.startActivity(intent);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_real_time;
    }


    @Override
    protected void onResume() {
        super.onResume();
        deviceName = Build.DEVICE;
        //请求实时榜数据
        getRealTimeList();
//        getRealTimePushList();
        //启动自动获取数据
        ifComeNextActivity = false;
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        } else {
            isPause = false;
        }
        ifComeNextActivity = true;
        try {
            if (null != realTimeDialog) {
//                if (realTimeDialog.isShowing()) {
//                    realTimeDialog.dismiss();
//                }
//                autoCloseDialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初期显示接口
     */
    public void getRealTimeList() {

        if (CommonUtil.checkNetwork2(context)) {
            showProgressDialog(null, getString(R.string.loading));
//            String testUrl = QkBuildConfig.getInstance().getConnect().getApiRankUrl() + Protocol.GETREALTIMELIST;
            String testUrl = BuildConfigDemo.getInstance().getConnect().getApiUrl() + Protocol.GETREALTIMELIST;
            HouseResourceAPIAsyncTask asyncTask = new HouseResourceAPIAsyncTask(context);
            HashMap<String, Object> params = new HashMap<String, Object>();

            params.put("ordinal", 0);

            String apiLogFileDirectory = Constants.Log_Def.LogDirectory;
            String apiLogFileName = Constants.Log_Def.Api_Log_File;
            asyncTask.post(apiLogFileDirectory, apiLogFileName, testUrl, params, new ResponseResultListener() {
                @Override
                public void onResult(Object object) {
                    dissmissProgressDialog();
                    HouseResourceResponseResult responseResult = (HouseResourceResponseResult) object;
                    if (responseResult.code == HouseResourceResponseResult.SUCESS_CODE) {
                        RealTimeRankList realTimeRankList = JSON.parseObject(responseResult.data, RealTimeRankList.class);
                        //add by clk on
                        formatCurrentDay = Util.formatNYR(realTimeRankList.getCurDate());
                        currentTime.setText(formatCurrentDay + "实时榜");
                        //add by clk off
                        List<RealTimeRankEntity> tempList = realTimeRankList.getList();
                        //add by clk 0623 on
                        //弹出实时榜dialog
                        if (!ifResumeLoadPushData) {
                            if (tempList.size() > 0) {
                                ordinal = tempList.get(0).getOrdinal();//后台数据倒序
                                if (realTimeDialog == null) {//第一次
                                    type = 0;
                                    realTimeDialog = new RealTimeDialog(context, tempList, type);
                                    realTimeDialog.show();
                                    realTimeDialog.setRealTimeChangeListener(realTimeChangeListener);
                                    realTimeChangeListener.start(tempList);
                                } else {
                                    //有弹窗 更新弹窗
                                    realTimeDialog.setRealTimeChangeListener(realTimeChangeListener);
                                    realTimeChangeListener.reStart(tempList);
                                    //有弹窗 更新弹窗
                                    realTimeDialog.notifyDataSetChanged(tempList);
                                    if (!realTimeDialog.isShowing()) {//上一个对话框已经消失 这个对话框已经来了
                                        realTimeDialog.show();
                                    }//上一个对话框还没消失 这个对话框已经来了
                                }
                            }
                        }
                        ifResumeLoadPushData = false;
                        //add by clk 0623 off


                        list.clear();
                        list.addAll(tempList);
                        adapter.notifyDataSetChanged();
                        //启动listView自动滚动
                        if (list.size() > 0) {
                            ordinal = tempList.get(0).getOrdinal();//后台数据倒序
                            //指定魅族3 魅族5 mx3 mx5手机满足四条以上的数据才可以滚动，否则滚动异常on
                            if (deviceName.equals("mx3") || deviceName.equals("mx5")) {
                                if (list.size() > 4) {
                                    setAutoScroll();
                                }
                            } else {
                                setAutoScroll();
                            }
                            //指定魅族3 魅族5 mx3 mx5手机满足四条以上的数据才可以滚动，否则滚动异常off
                            relativeLayout.setVisibility(View.GONE);
                        } else {
                            relativeLayout.setVisibility(View.VISIBLE);
                        }
                        if (!ifStartTimer) {
                            setAutoGetNewData();
//                            setAutoGetNewDataSecond();
                        }
                    } else if (!CommonUtil.isEmpty(responseResult.message)) {
                        /**
                         * 请求失败
                         */
                        CommonUtil.sendToast(context, responseResult.message);
                    }
                }
            });
        } else {
            CommonUtil.sendToast(mContext, "请检查网络后重新进入！");
        }

    }

    /**
     * 获得实时榜推送列表接口
     */
    public void getRealTimePushList() {

        if (CommonUtil.checkNetwork2(context)) {
            String testUrl = BuildConfigDemo.getInstance().getConnect().getApiUrl() + Protocol.GETREALTIMELIST;
            HouseResourceAPIAsyncTask asyncTask = new HouseResourceAPIAsyncTask(context);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("ordinal", ordinal);

            String apiLogFileDirectory = Constants.Log_Def.LogDirectory;
            String apiLogFileName = Constants.Log_Def.Api_Log_File;
            asyncTask.post(apiLogFileDirectory, apiLogFileName, testUrl, params, new ResponseResultListener() {
                @Override
                public void onResult(Object object) {
                    HouseResourceResponseResult responseResult = (HouseResourceResponseResult) object;
                    if (responseResult.code == HouseResourceResponseResult.SUCESS_CODE) {
                        final RealTimeRankList realTimeRankList = JSON.parseObject(responseResult.data, RealTimeRankList.class);
                        final List<RealTimeRankEntity> tempList = realTimeRankList.getList();
                        formatCurrentDay = Util.formatNYR(realTimeRankList.getCurDate());
                        currentTime.setText(formatCurrentDay + "实时榜");
                        //弹出实时榜dialog
                        if (tempList.size() > 0) {
                            ordinal = tempList.get(0).getOrdinal();//后台数据倒序
                            if (realTimeDialog == null) {//非第一次
                                type = 1;
                                realTimeDialog = new RealTimeDialog(context, tempList, type);
                                realTimeDialog.show();
                                realTimeDialog.setRealTimeChangeListener(realTimeChangeListener);
                                realTimeChangeListener.start(tempList);
                            } else {
                                //有弹窗 更新弹窗
                                realTimeDialog.setRealTimeChangeListener(realTimeChangeListener);
                                realTimeChangeListener.reStart(tempList);
                                //有弹窗 更新弹窗
                                type = 1;
                                realTimeDialog.setTypeValue(type);
                                realTimeDialog.notifyDataSetChanged(tempList);
                                if (!realTimeDialog.isShowing()) {//上一个对话框已经消失 这个对话框已经来了
                                    realTimeDialog.show();
                                }//上一个对话框还没消失 这个对话框已经来了
                            }


//                                    autoCloseDialog.schedule(new TimerTask() {
//                                        @Override
//                                        public void run() {
//                                            runOnUiThread(new Runnable() {
//                                                public void run() {
//                                                    realTimeDialog.dismiss();
//                                                }
//                                            });
//                                        }
//                                    }, 60 * 1000);
                        }
                        //====================非弹窗部分页面的修改=============================
                        if (tempList.size() > 0) {
                            relativeLayout.setVisibility(View.GONE);
                        }
                        list.addAll(0, tempList);
                        adapter.notifyDataSetChanged();
                        //指定魅族3 魅族5 mx3 mx5手机满足四条以上的数据才可以滚动，否则滚动异常on
                        if (!isStartUpadte) {
                            if (deviceName.equals("mx3") || deviceName.equals("mx5")) {
                                if (list.size() > 4) {
                                    setAutoScroll();
                                }
                            } else {
                                setAutoScroll();
                            }

                        }
                        //指定魅族3 魅族5 mx3 mx5手机满足四条以上的数据才可以滚动，否则滚动异常off
                    } else if (!CommonUtil.isEmpty(responseResult.message)) {
                        /**
                         * 请求失败
                         */
                        CommonUtil.sendToast(context, responseResult.message);
                    }
                }
            });
        } else {
            CommonUtil.sendToast(mContext, "请检查网络后重新进入！");
        }

    }


    /**
     * listView自动滚动
     */
    public void setAutoScroll() {
        int postDelay = 5;
//        String position = SharedPreferencesUtil.getSetting(QkConstant.SharedPreferencesKeyDef.FILE_NAME, context,
//                QkConstant.SharedPreferencesKeyDef.USER_POSITION);
//        if (Integer.parseInt(position) == 0) {
//            postDelay = 5;
//        } else {
//            postDelay = 8;
//        }
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        isStartUpadte = true;
                        realTimeLv.smoothScrollBy(i, 0);
                    }
                });
            }
        }, 0, postDelay);

        realTimeLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((totalItemCount > 0)
                        && (view.getLastVisiblePosition() == totalItemCount - 1)) {
                    if (view.getBottom() == view.getChildAt(
                            view.getChildCount() - 1).getBottom()) {
                        realTimeLv.setSelection(0);
                    }
                }
            }
        });
    }

    /**
     * 3分钟自动获取一次新数据
     */
    public void setAutoGetNewData() {
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                ifStartTimer = true;
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    public void run() {
                        if (!ifComeNextActivity) {
                            getRealTimePushList();
                        }
                    }
                });
            }
        }, 3 * 60 * 1000, 3 * 60 * 1000);
    }

    /**
     * 3分钟自动获取一次新数据
     */
    public void setAutoGetNewDataSecond() {
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                ifStartTimer = true;
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    public void run() {
                        if (!ifComeNextActivity) {
                            getRealTimePushList();
                        }
                    }
                });
            }
        }, 0, 3 * 60 * 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (null != autoUpdate) {
                autoUpdate.cancel();
            }
            if (null != autoGetNewData) {
                autoGetNewData.cancel();
            }
//            if (null != autoCloseDialog) {
//                autoCloseDialog.cancel();
//            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 一首一首播放
     */
    private void playSongs(final Stack<String> songList) {
        try {
            Log.e("yan", "mediaPlayer setDataSource");
            mediaPlayer.setDataSource(songList.pop());
            Log.e("yan", "mediaPlayer setDataSource");
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 一首一首播放
     */
    private void playSongs() {
        try {
            Log.e("yan", "mediaPlayer setDataSource");
            PlayingSongEntity entity = songQueueList.get(0);
            mediaPlayer.setDataSource(entity.getPayingSongPath());
            if (realTimeDialog != null) {
                realTimeDialog.setSongTitleShow(entity);
            }
            songQueueList.remove(0);
            songQueueList.add(entity);
            Log.e("yan", "mediaPlayer setDataSource");
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private RealTimeChangeListener realTimeChangeListener = new RealTimeChangeListener() {
        @Override
        public void close() {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        @Override
        public void start(List<RealTimeRankEntity> list) {
            songQueueList = getSongListSecond(list);
            if (songQueueList == null || songQueueList.size() <= 0) {
                return;
            }
            playSongs();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!songQueueList.isEmpty()) {
                        mp.reset();
                        playSongs();
                    }
                }
            });

        }

        @Override
        public void reStart(List<RealTimeRankEntity> list) {
            songQueueList = getSongListSecond(list);
            if (songQueueList == null || songQueueList.size() <= 0) {
                return;
            }
            mediaPlayer.stop();
            mediaPlayer.reset();
            playSongs();
        }

//        private Stack<String> getSongList(List<RealTimeRankEntity> list) {
//            Stack<String> songList = new Stack();
//            //        是否存在解压后的歌曲的路径
//            File downSongsUpzipPath = new File(QkConstant.DOWN_SONGS_UPZIP_PATH);
//            if (!downSongsUpzipPath.exists()) {
//                return null;
//            }
//            File[] files = downSongsUpzipPath.listFiles();
//            if (files == null || files.length <= 0) {
//                return null;
//            }
//            for (int i = list.size() - 1; i >= 0; i--) {
//                for (int j = 0; j < files.length; j++) {
//                    if (files[j].getAbsolutePath().endsWith(list.get(i).getSongName())) {
//                        songList.push(files[j].getAbsolutePath());
//                        continue;
//                    }
//                }
//            }
//            return songList;
//        }

        private LinkedList<PlayingSongEntity> getSongList(List<RealTimeRankEntity> list) {
            LinkedList<PlayingSongEntity> songList = new LinkedList<>();
            //        是否存在解压后的歌曲的路径
            File downSongsUpzipPath = new File(Constants.Song_Def.DOWN_SONGS_UPZIP_PATH);
            if (!downSongsUpzipPath.exists()) {
                return null;
            }
            File[] files = downSongsUpzipPath.listFiles();
            if (files == null || files.length <= 0) {
                return null;
            }
//            for (int i = list.size() - 1; i >= 0; i--) {
            for (int i = 0; i < list.size(); i++) {
                RealTimeRankEntity realTimeRankEntity = list.get(i);
                for (int j = 0; j < files.length; j++) {
                    String songName = realTimeRankEntity.getSongName();
                    if (CommonUtil.isEmpty(songName)) {
                        continue;
                    }
                    if (files[j].getAbsolutePath().endsWith(songName)) {
                        PlayingSongEntity entity = new PlayingSongEntity();
                        entity.setPayingSongPath(files[j].getAbsolutePath());
                        entity.setPayingSongName(songName.substring(0, songName.lastIndexOf(".")));
                        entity.setPayingSongToWho(realTimeRankEntity.getUserName());
                        songList.add(entity);
                        continue;
                    }
                }
            }
            return songList;
        }

    };

    /**
     * 房源播放歌曲逻辑
     *
     * @param list
     * @return
     */
    private LinkedList<PlayingSongEntity> getSongListSecond(List<RealTimeRankEntity> list) {
        LinkedList<PlayingSongEntity> songList = new LinkedList<>();
        //        是否存在解压后的歌曲的路径
        File downSongsUpzipPath = new File(Constants.Song_Def.DOWN_SONGS_UPZIP_PATH);
        if (!downSongsUpzipPath.exists()) {
            return null;
        }
        File[] files = downSongsUpzipPath.listFiles();
        if (files == null || files.length <= 0) {
            return null;
        }
        //   区分是否是第一次进去  第一次 type=0；type=1；
        if (type == 0) {
            int mOrdinal = (list.get(0).getOrdinal() - 1) % files.length + 1;

            if (mOrdinal > files.length) {
                CommonUtil.sendToast(context, "文件损坏");
            } else {
                PlayingSongEntity entity = new PlayingSongEntity();
                String absolutePath = files[mOrdinal].getAbsolutePath();
                int lastNum = absolutePath.lastIndexOf("/");
                int i = absolutePath.lastIndexOf(".");
                String substring = absolutePath.substring(lastNum + 1, i);
                entity.setPayingSongPath(files[mOrdinal].getAbsolutePath());
//        entity.setPayingSongName(songName.substring(0, songName.lastIndexOf(".")));
                entity.setPayingSongName(substring);
                entity.setPayingSongToWho(list.get(0).getPurchaserName());
                songList.add(entity);
            }
        } else {
            for (int j = 0; j < list.size(); j++) {
//  /storage/emulated/0/qk/upzip/blue.mp3
                int mOrdinal = (list.get(j).getOrdinal() - 1) % files.length + 1;

                if (mOrdinal > files.length) {
                    CommonUtil.sendToast(context, "文件损坏");
                } else {
                    PlayingSongEntity entity = new PlayingSongEntity();
                    String absolutePath = files[mOrdinal].getAbsolutePath();
                    int lastNum = absolutePath.lastIndexOf("/");
                    int i = absolutePath.lastIndexOf(".");
                    String substring = absolutePath.substring(lastNum + 1, i);
                    entity.setPayingSongPath(files[mOrdinal].getAbsolutePath());
//        entity.setPayingSongName(songName.substring(0, songName.lastIndexOf(".")));
                    entity.setPayingSongName(substring);
                    entity.setPayingSongToWho(list.get(j).getPurchaserName());
                    songList.add(entity);
                }
            }
        }

        return songList;
    }






}
