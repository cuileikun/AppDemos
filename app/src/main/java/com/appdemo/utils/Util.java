package com.appdemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.qk.applibrary.util.CommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：popular cui
 * 时间：2017/6/7 13:40
 * 功能:工具类
 */
public class Util {

    /**
     * 获取手机设备号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String deviceId = TelephonyMgr.getDeviceId();
        return deviceId;
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     * @author SHANHY
     * @date 2015年10月28日
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 编号转为对应的状态
     *
     * @param result
     * @return
     */
    public static String getStatus(int result) {
        String companyResult = "";
        switch (result) {
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 18:
            case 20:
                companyResult = "通过";
                break;
            case 1:
            case 2:
            case 4:
            case 5:
            case 7:
            case 17:
                companyResult = "待审批";
                break;
            case 3:
            case 6:
            case 8:
            case 19:
                companyResult = "驳回";
                break;
        }
        return companyResult;
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName(包名)(若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     * @return
     */
    public static boolean isAvilibleAPP(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 将2018-10-16 10:19:10转换为2018-10-16
     *
     * @param date1
     * @return
     */
    public static String parseDate(String date1) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        String checkoutDate = "";
        try {
            if (!CommonUtil.isEmpty(date1)) {
                date = sdf.parse(date1);
                checkoutDate = sdf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return checkoutDate;
    }

    /**
     * 将2018-10-16 10:19:10转换为10:19:10
     *
     * @param date
     * @return
     */
    public static String format(String date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {
            return df.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取给定日期的年值
     *
     * @param date
     * @return
     */
    public static String formatNian(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        try {
            return df.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取给定日期的月值
     *
     * @param date
     * @return
     */
    public static String formatYue(String date) {
        SimpleDateFormat df = new SimpleDateFormat("MM");
        try {
            return df.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取给定日期的日
     *
     * @param date
     * @return
     */
    public static String formatRi(String date) {
        SimpleDateFormat df = new SimpleDateFormat("dd");
        try {
            return df.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取给定日期的小时值
     *
     * @param date
     * @return
     */
    public static String formatShi(String date) {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        try {
            return df.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取给定日期的分值
     *
     * @param date
     * @return
     */
    public static String formatFen(String date) {
        SimpleDateFormat df = new SimpleDateFormat("mm");
        try {
            return df.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取给定日期的秒值
     *
     * @param date
     * @return
     */
    public static String formatMiao(String date) {
        SimpleDateFormat df = new SimpleDateFormat("ss");
        try {
            return df.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化时间格式
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.format(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把2017-3转为2017-03  转换周和月
     *
     * @param date
     * @return
     */
    public static String formatMonthOrWeek(String date) {
        if (!CommonUtil.isEmpty(date)) {
            if (date.contains("-")) {
                String str[] = date.split("-");
                String monthOrWeek = str[1];
                if (!CommonUtil.isEmpty(monthOrWeek)) {
                    if (Integer.parseInt(monthOrWeek) < 10) {
                        String formatMonthOrWeek = "0" + monthOrWeek;
                        return str[0] + "-" + formatMonthOrWeek;
                    } else {
                        return date;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断手机号是否合法
     * add by clk
     *
     * @param mobiles
     * @return
     */
    public static boolean checkPhoneNumber(String mobiles) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobiles);
        b = m.matches();
        return b;
    }

    /**
     * 2017-06-20转成2017年06月20日
     *
     * @param date
     * @return
     */
    public static String formatNYR(String date) {
        SimpleDateFormat srcSDF = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dstSDF = new SimpleDateFormat("yyyy年MM月dd日");

        Date data = null;
        try {
            data = srcSDF.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dstSDF.format(data);
    }


}
