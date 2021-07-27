package cn.edu.neu.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 32098
 */
public class StringHandleUtil {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @param s location string
     * @return province string
     */
    public static String getProvince(String s) {
        String[] str = s.split(" ");
        if ("其它".equals(str[0])) {
            return "其它";
        }
        return str[0];
    }

    /**
     * 时间转换标准格式
     * @param text text
     * @return string
     */
    public static String getStandardTime(String text) {
        return SDF.format(new Date(text));
    }

    public static void main(String[] args) {

    }
}



