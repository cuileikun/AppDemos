package com.qk.applibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qk.applibrary.R;
import com.qk.applibrary.listener.TopbarImplListener;
import com.qk.applibrary.util.CommonUtil;


/**
 * 顶部头部条
 */
public class TopbarView extends RelativeLayout {
    private TextView topbarTitleTv;
    private Button topbarRightBt;
    private TextView topbarLeftTitelTv;
    private ImageView topbarLeftImgIv;
    private RelativeLayout topbarLeftRl;
    private View topView;

    public TopbarImplListener listener;

    public TopbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = LayoutInflater.from(context);
        topView = mInflater.inflate(R.layout.top_bar, this,
                false);
        topbarTitleTv = (TextView) topView.findViewById(R.id.top_bar_title_tv);
        topbarRightBt = (Button) topView.findViewById(R.id.top_bar_right_bt);
        topbarLeftImgIv = (ImageView) topView.findViewById(R.id.top_bar_left_img_iv);
        topbarLeftTitelTv = (TextView) topView.findViewById(R.id.top_bar_left_title_tv);
        topbarLeftRl = (RelativeLayout) topView.findViewById(R.id.top_bar_left_rl);
        /**
         * 点击左边按扭事件
         */
        topbarLeftRl.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.leftClick();
                }

            }
        });

        /**
         * 点击右边按扭事件
         */
        topbarRightBt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.rightClick();
                }
            }
        });

        addView(topView);
    }

    /**
     * 设置背景颜色
     * @param color
     */
    public void setTopbarColor(int color) {
        topView.setBackgroundColor(color);
    }

    public TextView getTopbarTitleTv() {
        return topbarTitleTv;
    }


    public TextView getTopbarLeftTitelTv() {
        return topbarLeftTitelTv;
    }

    public ImageView getTopbarBackIv() {
        return topbarLeftImgIv;
    }

    public Button getTopbarRightBt() {
        return topbarRightBt;
    }


    /**
     * 设置top中间文字
     *
     * @param title
     */
    public void setTopbarTitle(String title) {
        if (!CommonUtil.isEmpty(title)) {
            topbarTitleTv.setText(title);
        }
    }

    public RelativeLayout getTopbarLeftRl() {
        return topbarLeftRl;
    }

    public View getTopbarView() {
        return topView;
    }

    /**
     * 设置top bar事件
     *
     * @param listener
     */
    public void setTopBarClickListener(TopbarImplListener listener) {
        this.listener = listener;
    }


}
