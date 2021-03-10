package pers.lomesome.compliation.model;

import java.util.HashMap;
import java.util.Map;

public class FinalAttribute {
    //关键字
    private static final String[] keyword = new String[]{"char", "int", "float", "break", "const", "return", "void", "continue", "do", "while", "if", "else", "for"};

    //分界符
    private static final String[] delimiter ={"{", "}", ";", ","};

    //运算符
    private static final String[] operator ={"(", ")", "[", "]", "!", "*", "/", "%", "+", "-", "<", "<=", ">", ">=","==","!=","&&","||","=","."};

    private static final Map<String, Integer> tokenMap = new HashMap<>();

    public static void initToken(){
        setToken(101, keyword);
        setToken(201, operator);
        setToken(301, delimiter);
    }

    private static void setToken(int start, String[] strings){
        for(String s : strings){
            tokenMap.put(s, start);
            start++;
        }
    }

    public static int findToken(String word){
        if(tokenMap.get(word) == null){
            return 700;
        }else {
            return tokenMap.get(word);
        }
    }

    public static String[] getDelimiter() {
        return delimiter;
    }

    public static String[] getOperator() {
        return operator;
    }
}
