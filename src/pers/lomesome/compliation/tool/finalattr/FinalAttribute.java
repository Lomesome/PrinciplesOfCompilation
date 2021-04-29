package pers.lomesome.compliation.tool.finalattr;

import java.util.HashMap;
import java.util.Map;

public class FinalAttribute {
    //关键字
    private static final String[] keyword = new String[]{"char", "int", "float", "break", "const", "return", "void", "continue", "do", "while", "if", "else", "for", "auto", "case", "default", "double", "enum", "extern", "goto", "long", "register", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "volatile"};

    //分界符
    private static final String[] delimiter ={"{", "}", ";", ","};

    //运算符
    private static final String[] operator ={"(", ")", "[", "]", "!", "*", "/", "%", "+", "-", "++", "--", "<", "<=", ">", ">=", "==", "!=", "&&", "||", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", ">>=", "<<=", "&", "|", "~", "^", "<<", ">>", "."};

    private static final HashMap<String, Integer> tokenMap = new HashMap<>();

    private static final HashMap<Integer, String> stringMap = new HashMap<>();

    //初始化关键字、分界符、运算符的token
    public static void init(){
        setToken(101, keyword);
        setToken(201, operator);
        setToken(301, delimiter);
        setString();
    }

    //设置关键字、分界符、运算符的token
    private static void setToken(int start, String[] strings){
        for(String s : strings){
            tokenMap.put(s, start++);
        }
    }

    //查找token
    public static int findToken(String word){
        if(tokenMap.get(word) == null)
            return 700;
        return tokenMap.get(word);
    }

    private static void setString(){
        int start = 101;
        stringMap.put(700, "IDENTIFIER");
        stringMap.put(800, "CONSTANT");
        stringMap.put(400, "CONSTANT");
        stringMap.put(600, "STRING_LITERAL");
        for(String s : keyword){
            stringMap.put(start++, s.toUpperCase());
        }
    }

    public static String findString(int token, String s){
        if(stringMap.get(token) == null)
            return s;
        return stringMap.get(token);
    }

    //获取分界符
    public static String[] getDelimiter() {
        return delimiter;
    }

    //获取运算符
    public static String[] getOperator() {
        return operator;
    }

    public static HashMap<String, Integer> getTokenMap() {
        return tokenMap;
    }
}
