package com.appdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.appdemo.R;
import com.appdemo.bean.RealTimeRankEntity;
import java.util.List;
import java.util.Locale;

/**
 * Created by popular_cui on 17/06/19.
 * 实时榜初期显示adapter
 */
public class RealTimeAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<RealTimeRankEntity> realTimeRankEntityList;

    public RealTimeAdapter(Context mContext, List<RealTimeRankEntity> realTimeRankEntityList) {
        this.mContext = mContext;
        this.realTimeRankEntityList = realTimeRankEntityList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return realTimeRankEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return realTimeRankEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RealTimeRankEntity realTimeRankEntity = realTimeRankEntityList.get(position);
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.real_time_item_layout, null);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content = (TextView) convertView.findViewById(R.id.content);
//            holder.number = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(holder);
        }
        holder = (Holder) convertView.getTag();
//        holder.time.setText(realTimeRankEntity.getContractTime());
        holder.time.setText(realTimeRankEntity.getSignTime());
//        holder.number.setText(Html.fromHtml("<font color='#000000'>成交价:</font>" + "<font color='#ff0000'>" + realTimeRankEntity.getRental() + "</font>" + "<font color='#000000'>元/月</font>"));
//        String userName = realTimeRankEntity.getUserName();


        //  "恭喜房管苗（第七十七服务中心(电商中心)）成功签约了北翟路9*****号102室E室4N，是今天成交的第1单！"
        String purchaserName = realTimeRankEntity.getPurchaserName();//采购员姓名  方亮
        String departmentName = realTimeRankEntity.getDepartmentName();//部门名称  第一服务中心(徐汇1组)
        String houseAddress = realTimeRankEntity.getHouseAddress();//房屋地址  新飞路1500弄2017号5单元2607室
        int ordinal = realTimeRankEntity.getOrdinal();//序号（成交的第几单）2
        boolean newSign = realTimeRankEntity.getIsNewSign();//是否是新签的
        String content = "";
        if (newSign) {
            content = "恭喜" + purchaserName + "(" + departmentName + ")" + "成功签约了" + houseAddress + "，是今天成交的第" + ordinal + "单！";
        } else {
            content = "恭喜" + purchaserName + "(" + departmentName + ")" + "成功续签了" + houseAddress + "，是今天成交的第" + ordinal + "单！";
        }

        int index = content.indexOf(purchaserName.charAt(0));
        String str1 = content.substring(0, index);
        String str2 = content.substring(index + purchaserName.length(), content.length());
        String message = String.format(Locale.getDefault(), "%s%s%s", str1, purchaserName, str2);
        SpannableString ss = new SpannableString(message);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#426EC4")), message.indexOf(purchaserName), message.indexOf(purchaserName) + purchaserName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(20, true), message.indexOf(purchaserName), message.indexOf(purchaserName) + purchaserName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.content.setText(ss);

//        String content = realTimeRankEntity.getViewStr();
//        if (content.contains(userName)) {
//            int index = content.indexOf(userName.charAt(0));
//            String str1 = content.substring(0, index);
//            String str2 = content.substring(index + userName.length(), content.length());
//            String message = String.format(Locale.getDefault(), "%s%s%s", str1, userName, str2);
//            SpannableString ss = new SpannableString(message);
//            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#00Ab84")), message.indexOf(userName), message.indexOf(userName) + userName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            ss.setSpan(new AbsoluteSizeSpan(20, true), message.indexOf(userName), message.indexOf(userName) + userName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            holder.content.setText(ss);
//        } else {
//        holder.content.setText(ss);
//        }
        return convertView;
    }

    class Holder {
        private TextView time;
        private TextView content;
//        private TextView number;//成交数
    }
}
