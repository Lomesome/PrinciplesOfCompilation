package pers.lomesome.compliation.model;

import java.util.HashMap;
import java.util.Map;

public class FinalAttribute {
    //关键字
    private static final String[] keyword = new String[]{"char", "int", "float", "break", "const", "return", "void", "continue", "do", "while", "if", "else", "for", "auto", "case", "default", "double", "enum", "extern", "goto", "long", "register", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "volatile"};

    //分界符
    private static final String[] delimiter ={"{", "}", ";", ","};

    //运算符
    private static final String[] operator ={"(", ")", "[", "]", "!", "*", "/", "%", "+", "-", "++", "--", "<", "<=", ">", ">=", "==", "!=", "&&", "||", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", ">>=", "<<=", "&", "|", "~", "^", "<<", ">>", "."};

    private static final Map<String, Integer> tokenMap = new HashMap<>();

    //初始化关键字、分界符、运算符的token
    public static void initToken(){
        setToken(101, keyword);
        setToken(201, operator);
        setToken(301, delimiter);
    }

    //设置关键字、分界符、运算符的token
    private static void setToken(int start, String[] strings){
        for(String s : strings){
            tokenMap.put(s, start);
            start++;
        }
    }

    //查找token
    public static int findToken(String word){
        if(tokenMap.get(word) == null){
            return 700;
        }else {
            return tokenMap.get(word);
        }
    }

    //获取分界符
    public static String[] getDelimiter() {
        return delimiter;
    }

    //获取运算符
    public static String[] getOperator() {
        return operator;
    }
}
