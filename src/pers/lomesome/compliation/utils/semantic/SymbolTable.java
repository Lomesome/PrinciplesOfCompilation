package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Pair;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.filehandling.StringAlign;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SymbolTable {

    public static class Var {
        public String name; // 变量名
        public List<Integer> scope = new ArrayList<>(); //作用域
        public String type; // 变量类型，int,char,float,string
        public String value; // 变量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
        public boolean isTemp = false;
        @Override
        public String toString(){
            StringAlign align = new StringAlign(10, StringAlign.Alignment.CENTER);//调用构造方法，设置字符串对齐为居中对齐，最大长度为50
            if (value == null)
                value = "null";
            return name + "\t" + align.format(type) + "\t" + align.format(value) + "\t" + align.format(offset) + "\t"+ align.format(tp) + "\t" + scope;
        }
    }

    public static class Cons{
        public String name; // 常量名
        public List<Integer> scope = new ArrayList<>(); //作用域
        public String type; // 常量类型，int,char,float,string
        public String value; // 常量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
        @Override
        public String toString(){
            StringAlign align = new StringAlign(10, StringAlign.Alignment.CENTER);//调用构造方法，设置字符串对齐为居中对齐，最大长度为50
            if (value == null)
                value = "null";
            return name + "\t" + align.format(type) + "\t" + align.format(value)+ "\t" + align.format(offset)  + "\t" + align.format(tp) + "\t" + scope;
        }
    }

    public static class Fun {
        public String name; // 函数名
        public String type; // 返回值类型，int,char,float，void
        public List<Pair<String, String>> args = new ArrayList<>();

        @Override
        public String toString(){
            return name + "\t\t" + type + "\t\t\t" + args;
        }

    }

    public List<Var> Synbl = new ArrayList<>(); //变量表
    public List<Var> Temp = new ArrayList<>(); //中间变量表
    public List<Fun> Func = new ArrayList<>(); //函数表
    public List<Cons> Const = new ArrayList<>(); //常量表
    public LiveStatu liveStatu;


    private int state = 0;
    private String stype;
    private String sname;
    private Word sword;
    private int off = 0;
    private int offs = 0;
    private boolean isArr = false;
    private boolean isCons = false;
    private int len = 0;
    public final String funcname;
    public final String functype;
    public List<Pair<String, String>> argsList= new ArrayList<>();

    public SymbolTable(String funcname, String functype){
        this.funcname = funcname;
        this.functype = functype;
        liveStatu = new LiveStatu();
    }

    boolean SearchExist(String s) {
        int count;
        for (Var v : Synbl) {
            if (v.name.equals(s)) {
                count = 0;
                for (int i = 0; i < v.scope.size(); i++) {
                    if (v.scope.get(i).equals(Analysis.actionscope.get(i))) {
                        count++;
                    }
                }
                if (count == Analysis.actionscope.size() && Analysis.actionscope.size() == v.scope.size()) {
                    return true;
                }
            }
        }
        for (Cons c : Const) {
            if (c.name.equals(s)) {
                count = 0;
                for (int i = 0; i < c.scope.size(); i++) {
                    if (c.scope.get(i).equals(Analysis.actionscope.get(i))) {
                        count++;
                    }
                }
                if (count == Analysis.actionscope.size() && Analysis.actionscope.size() == c.scope.size()) {
                    return true;
                }
            }
        }
        return false;
    }

    void addSymbol() {
        if (isCons){
            Cons c = new Cons();
            c.name = sname;
            c.type = stype;
            int size = 0;
            switch (stype) {
                case "int":
                    size = 4;
                    break;
                case "char":
                    size = 1;
                    break;
                case "float":
                    size = 8;
                    break;
                case "String":
                    size = 50;
                    break;
            }
            if (isArr) {
                c.tp = len;
                c.type = stype + "[]";
            }
            if (!SearchExist(sname)) {
                off = offs;
                c.offset = off;
                offs = off + size * len;//加入该变量后末尾地址等于原偏移地址加上该变量的长度
                c.scope = new ArrayList<>(Analysis.actionscope);
                Const.add(c);
            } else {
                Analysis.flag = false;
                Analysis.errorMsg.add("error: " + sword.getWord() + " 重定义 position (" + sword.getRow() + ", " + sword.getCol() + ")");
                System.out.println("error: " + sword.getWord() + " 重定义 position (" + sword.getRow() + ", " + sword.getCol() + ")");
            }
        }else {
            Var v = new Var();
            v.name = sname;
            v.type = stype;
            int size = 0;
            switch (stype) {
                case "int":
                    size = 4;
                    break;
                case "char":
                    size = 1;
                    break;
                case "float":
                    size = 8;
                    break;
                case "String":
                    size = 50;
                    break;
            }
            if (isArr) {
                v.tp = len;
                v.type = stype + "[]";
            }
            if (!SearchExist(sname)) {
                off = offs;
                v.offset = off;
                offs = off + size * len;//加入该变量后末尾地址等于原偏移地址加上该变量的长度
                v.scope = new ArrayList<>(Analysis.actionscope);
                Synbl.add(v);
            } else {
                Analysis.flag = false;
                Analysis.errorMsg.add("error: " + sword.getWord() + " 重定义 position (" + sword.getRow() + ", " + sword.getCol() + ")");
                System.out.println("error: " + sword.getWord() + " 重定义 position (" + sword.getRow() + ", " + sword.getCol() + ")");
            }
        }
    }

    public void setTable(Word word){
        state = changeState(word, state);//自动机状态变换

    }

    int changeState(Word s, int state) {
        String word = s.getWord();
        switch (state) {
            case 0:
                if (word.equals("int") || word.equals("char") || word.equals("String") || word.equals("float")) {
                    stype = word;
                    state = 2;
                } else if (word.equals("const")){
                    isCons = true;
                    state = 1;
                } else {
                    isCons = false;
                    state = 0;
                }
                break;
            case 1:
                if (word.equals("int") || word.equals("char") || word.equals("String") || word.equals("float")) {
                    stype = word;
                    state = 2;
                } else
                    state = 0;
                break;
            case 2:
                sname = word;
                sword = s;
                state = 3;
                break;
            case 3:
                if (word.equals(",")) {
                    isArr = false;
                    len = 1;
                    addSymbol();
                    state = 2;
                } else if (word.equals("[")) {
                    state = 4;
                    isArr = true;
                    isCons = false;
                } else if (!(word.equals(";"))) {
                    state = 3;
                }
                else {
                    isArr = false;
                    len = 1;
                    addSymbol();
                    isCons = false;
                    state = 0;
                }
                break;
            case 4:
                len = Integer.parseInt(s.getWord());
                isCons = false;
                state = 5;
                break;
            case 5:
                if (word.equals("]")) {
                    isCons = false;
                    addSymbol();
                    state = 6;
                }
                break;
            case 6:
                if (word.equals(",")) {
                    state = 2;
                } else {
                    isCons = false;
                    state = 0;
                }
                break;
        }

        return state;
    }

    public List<List<Object>> printTable() {
        List<List<Object>> results = new ArrayList<>();
        System.out.println("变量表：");
        List<Object> synblList = new ArrayList<>();
        for (Var v : Synbl) {
            if (!v.isTemp) {
                synblList.add(v);
                System.out.printf("%10s", v.name);
                System.out.printf("%10s", v.type);
                System.out.printf("%10s", v.value);
                System.out.printf("%10s", v.offset);
                System.out.printf("%10s", v.tp);
                System.out.printf("\t\t\t%-10s", v.scope);
                System.out.println();
            }
        }

        System.out.println("常量表：");
        List<Object> constList = new ArrayList<>();
        for (Cons v : Const) {
            constList.add(v);
            System.out.printf("%10s", v.name);
            System.out.printf("%10s", v.type);
            System.out.printf("%10s", v.value);
            System.out.printf("%10s", v.offset);
            System.out.printf("%10s", v.tp);
            System.out.printf("\t\t\t%-10s", v.scope);
            System.out.println();
        }

        System.out.println("函数表：");
        List<Object> funclList = new ArrayList<>();
        for (Fun f : Func) {
            funclList.add(f);
            System.out.printf("%10s", f.name);
            System.out.printf("%10s", f.type);
            System.out.printf("\t\t\t%-10s", f.args);
            System.out.println();
        }
        results.add(synblList);
        results.add(constList);
        results.add(funclList);
        return results;
    }

    public String getValue(Word word) {
        String value = null;
        for (Var v : Synbl) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }

            if (v.name.equals(word.getWord()) && isScope(v.scope, stack)) {
                value = v.value ;
                return value;
            }
        }
        for (Var v : Synbl) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }
            do {
                if (v.name.equals(word.getWord()) && isScope(v.scope, stack)) {
                    value = v.value ;
                    return value;
                }
                if (stack.size() > 0)
                    stack.pop();
            }while (stack.size() > 0);
        }

        for (Cons c : Const) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }

            if (c.name.equals(word.getWord()) && isScope(c.scope, stack)) {
                value = c.value ;
                return value;
            }
        }
        for (Cons c : Const) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }
            do {
                if (c.name.equals(word.getWord()) && isScope(c.scope, stack)) {
                    value = c.value ;
                    return value;
                }
                if (stack.size() > 0)
                    stack.pop();
            }while (stack.size() > 0);
        }
        return value;
    }

    private boolean isScope(List<Integer> scopeList, Stack<Integer> actionscope){
        if (scopeList.size() != actionscope.size()){
            return false;
        }
        for (int i = 0; i < scopeList.size(); i++){
            if (!scopeList.get(i).equals(actionscope.get(i))){
                return false;
            }
        }
        return true;
    }

    public LiveStatu getLiveStatu() {
        return liveStatu;
    }

    public void setLiveStatu(LiveStatu liveStatu) {
        this.liveStatu = liveStatu;
    }
}