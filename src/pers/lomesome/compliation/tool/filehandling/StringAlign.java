package pers.lomesome.compliation.tool.filehandling;

import java.io.Serializable;
import java.text.*;

public class StringAlign extends Format implements Serializable {

    private static final long serialVersion = 1L;

    /*枚举，哪种对齐方式*/
    public enum Alignment{
        /*左对齐*/
        LEFT,
        /*居中对齐*/
        CENTER,
        /*右对齐*/
        RIGHT,
    }

    private Alignment aligment;//当前对齐
    private int maxPages;//当前最大长度

    /*构造方法，用来设置字符串的居中方式以及最大长度*/
    public StringAlign(int maxPages, Alignment alignment) {

        switch(alignment) {
            case LEFT:
            case CENTER:
            case RIGHT:
                this.aligment = alignment;//将传过来的对齐方式赋值给全局的alignment变量
                break;

            default:
                throw new IllegalArgumentException("对齐参数错误！");
        }

        if(maxPages < 0) {//长度为负数时会抛出异常
            throw new IllegalArgumentException("页数参数错误");
        }

        this.maxPages = maxPages;

    }

    @Override
    public StringBuffer format(Object input, StringBuffer where, FieldPosition ignore) {
        String s = input.toString();
        String wanted = s.substring(0,Math.min(s.length(), maxPages));

        //得到右侧的空格
        switch(aligment) {
            case RIGHT:
                pad(where, maxPages - wanted.length());
                where.append(wanted);
                break;
            case CENTER:
                int toAdd = maxPages - wanted.length();
                pad(where, toAdd/2);
                where.append(wanted);
                pad(where, toAdd-toAdd/2);
                break;
            case LEFT:
                where.append(wanted);
                pad(where, maxPages-wanted.length());
                break;
        }
        return where;
    }

    private void pad(StringBuffer where, int howMany) {
        for(int i = 0; i < howMany; i++) {
            where.append(' ');//添加空格
        }
    }

    String format(String s) {
        return format(s, new StringBuffer(), null).toString();
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return source;
    }

}