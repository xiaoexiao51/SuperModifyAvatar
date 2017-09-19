package com.supermodifyavatar.utils;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关的工具类
 * 1、判断字符串是否为字符串、包含、为空、一致
 * 2、判断是否为手机号、密码、数字
 * 3、格式化金额、手机号、银行卡号
 * 4、拼接字符串
 * 5、隐藏敏感字符
 *
 * Created by lilei on 2017/5/5.
 */
public class StringUtils {

    public static boolean isString(Object value) {
        return value != null && value.getClass() == String.class;
    }

    public static boolean isContains(String value, String value2) {
        return value != null && value.contains(value2);
    }

    public static boolean isNull(String value) {
        return value == null || value.trim().length() == 0 || isEqual(value, "null");
    }

    public static boolean isEqual(String value, String value2) {
        return (value != null && value.equals(value2)) || (value == null && value2 == null);
    }

    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static boolean isMobile2(String phoneNumber) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0|6|7|8]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean isEmial(String phoneNumber) {
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean isPassword(String password) {
        Pattern p = Pattern.compile("^[a-z0-9A-Z]{6,}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int count(String content, String child) {
        int count = 0;
        if (!isNull(content)) {
            Pattern p = Pattern.compile(child, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(content);
            while (m.find()) {
                count++;
            }
        }
        return count;
    }

    public static String join(String seperate, List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        String returnValue = "";
        for (String item : list) {
            returnValue += item + seperate;
        }
        return returnValue.substring(0, returnValue.length() - 1);
    }

//    public static void setHtml(final Context context, final TextView tvText, final String html) {
//        if (!StringUtils.isNull(html)) {
//            final Handler mHandler = new Handler();
//            final int displayWidth = DisplayUtil.getDisplayWidth(context) - 2 * DisplayUtil.dip2px(context, 10);
//            new Thread() {
//                public void run() {
//                    EventBus.getDefault().post(new LoadEvent(false));
//                    final Spanned span = Html.fromHtml(html, new Html.ImageGetter() {
//                        @Override
//                        public Drawable getDrawable(String source) {
//                            Drawable drawable = null;
//                            try {
//                                drawable = new BitmapDrawable(Picasso.with(context)
//                                        .load(UrlHelper.GetFullUrl(source)).transform(new ImageTransformation(800)).get());
//                                if (drawable.getIntrinsicWidth() != 0) {
//                                    int height = displayWidth * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
//                                    drawable.setBounds(0, 0, displayWidth, height);
//                                }
//                            } catch (IOException e) {
//                                drawable = new ColorDrawable(context.getResources().getColor(R.color.color_bg2));
//                                drawable.setBounds(0, 0, displayWidth, (int) (displayWidth / 1.4));
//                            }
//                            return drawable;
//                        }
//                    }, null);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("UI线程ID=" + Thread.currentThread().getId());
//                            tvText.setText(span);
//                            EventBus.getDefault().post(new LoadEvent(true));
//                        }
//                    });
//                }
//            }.start();
//        } else {
//            tvText.setText(null);
//        }
//    }

//    public static String getHtmlContent(String html) {
//        Document document = Jsoup.parseBodyFragment(html);
//        return document.text();
//    }


    /**
     * 将“0.00”形式的价格解析成double类型的价格。
     *
     * @param price
     * @return
     */
    public static double parsePrice(String price) {
        DecimalFormat dfspeed = new DecimalFormat("0.00");
        try {
            return dfspeed.parse(price).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将double类型的价格转换成"0.00"形式的字符串。
     *
     * @param price
     * @return
     */
    public static String formatPrice(double price) {
        DecimalFormat dfspeed = new DecimalFormat("0.00");
        return dfspeed.format(price);
    }

    /**
     * 将double类型的价格转换成"0"形式的字符串。
     *
     * @param price
     * @return
     */
    public static String formatPrice2(double price) {
        DecimalFormat dfspeed = new DecimalFormat("0");
        return dfspeed.format(price);
    }


    /**
     * 拼接字符串代替“+”拼接。
     *
     * @param items
     * @return
     */
    public static String append(String... items) {
        StringBuffer buffer = new StringBuffer();
        for (String item : items) {
            if (!TextUtils.isEmpty(item)) {
                buffer.append(item);
            }
        }
        return buffer.toString();
    }


    /**
     * 提取出城市名称。
     *
     * @param city
     * @return
     */
    public static String extractLocation(final String city) {
        if (city.endsWith("自治州") || city.endsWith("自治县") || city.endsWith("自治区")) {
            return city.substring(0, city.length() - 3);
        } else if (city.endsWith("地区")) {
            return city.substring(0, city.length() - 2);
        } else if (city.endsWith("市") || city.endsWith("盟") || city.endsWith("省")
                || city.endsWith("县") || city.endsWith("区") || city.endsWith("旗")) {
            return city.substring(0, city.length() - 1);
        }
        return city;
    }

    //金额的格式化
    public static String formatMoney(double value) {
        DecimalFormat format = new DecimalFormat("###,###.###");
        return format.format(value);
    }

    //金额的格式化
    public static String formatMoney(String value) {
        if (!StringUtils.isNull(value)) {
            DecimalFormat format = new DecimalFormat("###,###.###");
            return format.format(Double.parseDouble(value));
        }
        return "0";
    }

    //手机号码的格式
    public static void formatMobile(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 3 || index == 8)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    //银行卡号码的格式
    public static void formatBank(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    public static String hideMobile(String mobile) {

        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static String hideEmile(String email) {

        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    /**
     * 版本号比较
     * @param serverVersion
     * @param localVersion
     * @return
     */
    public static int compareVersion(String serverVersion, String localVersion) {
        if (serverVersion.equals(localVersion)) {
            return 0;
        }
        String[] version1Array = serverVersion.split("\\.");
        String[] version2Array = localVersion.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}
