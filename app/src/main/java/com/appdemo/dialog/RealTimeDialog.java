package com.appdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.appdemo.R;
import com.appdemo.adapter.RealTimeDialogAdapter;
import com.appdemo.bean.PlayingSongEntity;
import com.appdemo.bean.RealTimeRankEntity;
import java.util.List;

/**
 * Created by popular_cui on 17/06/19.
 * 实时榜dialog
 */
public class RealTimeDialog extends Dialog {
    private Context context;
    private ListView listView;
    private RealTimeDialogAdapter adapter;
    private TextView close;
    private TextView tv_send_to_who;//送给谁的歌
    private TextView tv_send_to_song_name;//歌曲名
    List<RealTimeRankEntity> list;

    private RealTimeChangeListener realTimeChangeListener;
    private int mType;//区分是否是第一次进去  第一次 mType=0；mType=1；

    public RealTimeDialog(final Context context, List<RealTimeRankEntity> list,int type) {
        super(context, R.style.Dialog_Fullscreen);
        setContentView(R.layout.dialog_real_time);
        this.context=context;
        this.list=list;
        this.mType=type;
        listView = (ListView) findViewById(R.id.dialog_real_time_lv);
        close = (TextView) findViewById(R.id.close);
        tv_send_to_who = (TextView) findViewById(R.id.tv_send_to_who);
        tv_send_to_song_name = (TextView) findViewById(R.id.tv_send_to_song_name);

        adapter = new RealTimeDialogAdapter(context, list,mType);
        listView.setAdapter(adapter);
        if (null != list) {
            adapter.notifyDataSetChanged();
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realTimeChangeListener != null) {
                    realTimeChangeListener.close();
                }
                dismiss();
            }
        });


    }

    /**
     * 有弹框更新弹框 设置type值
     * @param type
     */
    public void setTypeValue(int type) {
        this.mType=type;
        listView = (ListView) findViewById(R.id.dialog_real_time_lv);
        close = (TextView) findViewById(R.id.close);
        tv_send_to_who = (TextView) findViewById(R.id.tv_send_to_who);
        tv_send_to_song_name = (TextView) findViewById(R.id.tv_send_to_song_name);

        adapter = new RealTimeDialogAdapter(context, list,mType);
        listView.setAdapter(adapter);
        if (null != list) {
            adapter.notifyDataSetChanged();
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realTimeChangeListener != null) {
                    realTimeChangeListener.close();
                }
                dismiss();
            }
        });



    }

    /**
     * 刷新视图
     */
    public void notifyDataSetChanged(List<RealTimeRankEntity> list) {

        if (realTimeChangeListener != null) {
            realTimeChangeListener.reStart(list);
        }
        adapter.notifyDataSetChanged(list);
    }


    public void setRealTimeChangeListener(RealTimeChangeListener realTimeChangeListener) {
        this.realTimeChangeListener = realTimeChangeListener;
    }


    @Override
    public void dismiss() {
        if (realTimeChangeListener != null) {
            realTimeChangeListener.close();
        }
        super.dismiss();
    }

    /**
     * 显示给谁的歌
     * @param entity
     */
    public void setSongTitleShow(PlayingSongEntity entity) {
        tv_send_to_who.setText("送给"+   entity.getPayingSongToWho()+"的歌");
        tv_send_to_song_name.setText(entity.getPayingSongName());
    }



}
