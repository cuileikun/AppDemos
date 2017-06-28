package com.qk.applibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 避免scrollview与listview冲突
 * Created by acer on 2016-8-31.
 */
public class MyListView extends ListView {
    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        setMeasuredDimension(widthMeasureSpec, expandSpec);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
