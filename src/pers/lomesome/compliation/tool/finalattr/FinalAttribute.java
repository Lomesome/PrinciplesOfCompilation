package pers.lomesome.compliation.tool.finalattr;

import pers.lomesome.compliation.model.MakeJson;
import pers.lomesome.compliation.utils.semantic.SymbolTable;
import pers.lomesome.compliation.utils.syntax.AllGrammer;
import java.util.*;

public class FinalAttribute {
    /**
     * 关键字
     */
    private static final String[] keyword = new String[]{"char", "int", "float", "break", "const", "return", "void", "continue", "do", "while", "if", "else", "for", "auto", "case", "default", "double", "enum", "extern", "goto", "long", "register", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "volatile", "print"};

    /**
     * 分界符
     */
    private static final String[] delimiter ={"{", "}", ";", ","};

    /**
     * 运算符
     */
    private static final String[] operator ={"(", ")", "[", "]", "!", "*", "/", "%", "+", "-", "++", "--", "<", "<=", ">", ">=", "==", "!=", "&&", "||", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", ">>=", "<<=", "&", "|", "~", "^", "<<", ">>", "."};

    /**
     * 语义标记
     */
    private static final String[] sem = {"PUSH", "GEQA", "GEQS", "GEQM", "GEQD", "ASSI", "GREA", "LESS", "EQUA", "GREQ", "LEEQ", "NOEQ", "IF", "EL", "IEFIR", "IESEC", "WH", "DO", "WE", "PUSHNUM", "LEVELA", "LEVELS", "ADDFUN", "DOW", "CALLFUN", "ADDARG", "RE", "RET", "CALLFUNARG", "PUSHARG", "CALLS", "ARG", "PRINT", "INDE"};

    /**
     * word对应的token
     */
    private static final HashMap<String, Integer> tokenMap = new HashMap<>();

    /**
     *
     */
    private static final HashMap<Integer, String> stringMap = new HashMap<>();

    private static final HashMap<Integer, String> nameMap = new HashMap<>();

    /**
     * 预测分析first集
     */
    private static Map<String, Set<String>> firstmap = new LinkedHashMap<>();

    /**
     * 预测分析follow集
     */
    private static Map<String, Set<String>> followmap = new LinkedHashMap<>();

    /**
     * 预测分析select集
     */
    private static Map<List<List<String>>, Set<String>> selectMap = new LinkedHashMap<>();

    /**
     * 预测表
     */
    private static LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> predictMap;

    /**
     * 带语义标记的预测分析表
     */
    private static LinkedHashMap<String, LinkedHashMap<String, List<String>>> semPredictMap;

    /**
     * 所有非终结符
     */
    private static Set<String> allVn = new LinkedHashSet<>();

    /**
     * 所有终结符
     */
    private static Set<String> allVt = new LinkedHashSet<>();

    /**
     * 文法
     */
    private static AllGrammer allGrammer;

    /**
     * 带语义动作的文法
     */
    private static AllGrammer allGrammerWithAction;

    /**
     * 符号表
     */
    private static Map<String, SymbolTable> symbolTableMap = new LinkedHashMap<>();

    //初始化关键字、分界符、运算符的token
    public static void init(){
        setToken(101, keyword);
        setToken(201, operator);
        setToken(301, delimiter);
        setString();
        setNameMap();
    }

    //设置关键字、分界符、运算符的token
    private static void setToken(int start, String[] strings){
        for(String s : strings){
            tokenMap.put(s, start++);
        }
    }

    /**
     * 在tokenMap查找word对应的token
     * @param word
     * @return
     */
    public static int findToken(String word){
        if(tokenMap.get(word) == null)
            return 700;
        return tokenMap.get(word);
    }

    private static void setString(){
        int start = 101;
        stringMap.put(700, "id");
        stringMap.put(500, "char_const");
        stringMap.put(800, "float_const");
        stringMap.put(400, "int_const");
        stringMap.put(600, "string_const");
        for(String s : keyword){
            stringMap.put(start++, s);
        }
    }

    /**
     * 通过查找对应的内部表示名称
     * @param token
     * @param s
     * @return
     */
    public static String findString(int token, String s){
        if(stringMap.get(token) == null)
            return s;
        else if (token == 800 && s.equals("0")){
            return stringMap.get(400);
        }
        return stringMap.get(token);
    }

    private static void setNameMap(){
        int start = 101;
        nameMap.put(700, "id");
        nameMap.put(500, "char");
        nameMap.put(800, "float");
        nameMap.put(400, "int");
        nameMap.put(600, "string");
        for(String s : keyword){
            nameMap.put(start++, s.toUpperCase());
        }
    }

    public static String findName(int token, String s){
        if(nameMap.get(token) == null)
            return s;
        return nameMap.get(token);
    }

    //获取分界符
    public static String[] getDelimiter() {
        return delimiter;
    }

    //获取运算符
    public static String[] getOperator() {
        return operator;
    }

    //获取关键词
    public static String[] getKeyword() {
        return keyword;
    }

    //获取tokenmap
    public static HashMap<String, Integer> getTokenMap() {
        return tokenMap;
    }

    //获取预测分析表
    public static LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> getPredictMap() {
        return predictMap;
    }

    //保存预测分析表
    public static void setPredictMap(LinkedHashMap<String, LinkedHashMap<String, List<MakeJson>>> predictMap) {
        FinalAttribute.predictMap = predictMap;
    }

    public static Map<String, Set<String>> getFirstmap() {
        return firstmap;
    }

    //保存first集
    public static void setFirstmap(Map<String, Set<String>> firstmap) {
        FinalAttribute.firstmap = firstmap;
    }

    public static Map<String, Set<String>> getFollowmap() {
        return followmap;
    }

    //保存follow集
    public static void setFollowmap(Map<String, Set<String>> followmap) {
        FinalAttribute.followmap = followmap;
    }


    public static Map<List<List<String>>, Set<String>> getSelectMap() {
        return selectMap;
    }

    //保存select集
    public static void setSelectMap(Map<List<List<String>>, Set<String>> selectMap) {
        FinalAttribute.selectMap = selectMap;
    }

    public static Set<String> getAllVn() {
        return allVn;
    }

    //保存文法中的非终结符
    public static void setAllVn(Set<String> allVn) {
        FinalAttribute.allVn = allVn;
    }

    public static Set<String> getAllVt() {
        return allVt;
    }

    //保存文法中的终结符
    public static void setAllVt(Set<String> allVt) {
        FinalAttribute.allVt = allVt;
    }

    public static AllGrammer getAllGrammer() {
        return allGrammer;
    }

    //保存普通文法
    public static void setAllGrammer(AllGrammer allGrammer) {
        FinalAttribute.allGrammer = allGrammer;
    }

    public static AllGrammer getAllGrammerWithAction() {
        return allGrammerWithAction;
    }

    //保存带语义动作的文法
    public static void setAllGrammerWithAction(AllGrammer allGrammerWithAction) {
        FinalAttribute.allGrammerWithAction = allGrammerWithAction;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, List<String>>> getSemPredictMap() {
        return semPredictMap;
    }

    public static void setSemPredictMap(LinkedHashMap<String, LinkedHashMap<String, List<String>>> semPredictMap) {
        FinalAttribute.semPredictMap = semPredictMap;
    }

    public static void addSymbolTable(String func, SymbolTable symbolTable){
        symbolTableMap.put(func, symbolTable);
    }

    public static SymbolTable getSymbolTable(String func){
        return symbolTableMap.get(func);
    }

    public static Map<String, SymbolTable> getSymbolTableMap() {
        return symbolTableMap;
    }

    public static void clearSymbolTableMap() {
        symbolTableMap.clear();
    }

    public static String[] getSem() {
        return sem;
    }
}
