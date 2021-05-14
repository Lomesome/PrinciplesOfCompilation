package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Word;
import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    public static class Var {
        public String name; // 变量名
        public Integer scope; //作用域
        public String type; // 变量类型，int,char,float,string
        public String value; // 变量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
        @Override
        public String toString(){
            return name + "\t" + type + "\t" + tp + "\t" + offset;
        }
    }

    public static class Cons{
        public String name; // 常量名
        public Integer scope; //作用域
        public String type; // 常量类型，int,char,float,string
        public String value; // 常量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
        @Override
        public String toString(){
            return name + "\t" + type + "\t" + tp + "\t" + offset;
        }
    }

    public static class Fun<SymbolTablem> {
        public String name; // 函数名
        public String type; // 返回值类型，int,char,float，void
        public SymbolTablem symbolTablem ;

    }
    public static class Struc {
        public String name; // 变量名
        public String type; // 变量类型，int,char,float,string
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
    }

    public List<Var> Synbl = new ArrayList<>(); //变量表
    public List<Var> Temp = new ArrayList<>(); //中间变量表
    public List<Fun> Func = new ArrayList<>(); //函数表
    public List<Cons> Const = new ArrayList<>(); //常量表
    public List<Struc> Rinfl = new ArrayList<>(); //结构体表，用来存放结构体中的所有变量
    public LiveStatu liveStatu;

    private int state = 1;
    private String stype;
    private String sname;
    private int off = 0;
    private int offs = 0;
    private boolean isArr = false;
    private int len = 0;

    public final String funcname;

    public SymbolTable(String funcname){
        this.funcname = funcname;
        liveStatu = new LiveStatu();

    }

    int SearchSynbl(String s) {
        int i = 0;
        for (Var v : Synbl) {
            if (v.name.equals(s)) {
                return i;
            }
            i++;
        }
        return Integer.MAX_VALUE; // 查找一个变量在变量表中的位置，若表中没有这个变量则返回Integer.MAX_VALUE
    }

    int SearchRinfi(int location, String s) {
        for (int i = location; i < Rinfl.size(); i++) {
            if (Rinfl.get(i).name.equals(s)) {
                return i;
            }
            i++;
        }
        return Integer.MAX_VALUE; // 查找一个变量在结构体表中的位置，若表中没有这个变量则返回Integer.MAX_VALUE
    }

    int SearchCons(int location, String s) {
        for (int i = location; i < Const.size(); i++) {
            if (Const.get(i).name.equals(s)) {
                return i;
            }
            i++;
        }
        return Integer.MAX_VALUE; // 查找一个常量在常量表中的位置，若表中没有这个变量则返回Integer.MAX_VALUE
    }

    void addSynbl() {
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
        if (!sname.equals("main")) {
            if (SearchSynbl(sname) == Integer.MAX_VALUE) {
                off = offs;
                v.offset = off;
                offs = off + size * len;//加入该变量后末尾地址等于原偏移地址加上该变量的长度
                Synbl.add(v);
            } else {
                System.out.println(sname + "重定义");
            }
        }
    }

    void addRinfi() {
        Struc s = new Struc();
        s.name = sname;
        s.type = stype;
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
            s.tp = len;
            s.type = stype + "[]";
        }
        int location = Synbl.get(Synbl.size() - 1).tp;
        if (SearchRinfi(location, sname) == Integer.MAX_VALUE) {
            off = offs;
            s.offset = off;
            offs = off + size * len;//加入该变量后末尾地址等于原偏移地址加上该变量的长度
            Rinfl.add(s);
        } else if (isArr) {
            System.out.println(stype + " " + sname + "[" + len + "]" + "重定义");
        }
    }

    public void getTable(List<Word> lex) {
        state = 1;
        int i = 0;
        while (i < lex.size()) {
            String s = lex.get(i).getWord();
            state = changeState(s, state);//自动机状态变换
            i++;
        }
    }

    public void setTable(Word word){
        String s = word.getWord();
        System.out.println("word::"+s);
        state = changeState(s, state);//自动机状态变换
    }

    int changeState(String s, int state) {
        switch (state) {
            case 1:
                if (s.equals("int") || s.equals("char") || s.equals("String") || s.equals("float")) {
                    stype = s;
                    state = 2;
                } else if (s.equals("struct")) {
                    stype = s;
                    state = 7;
                } else
                    state = 1;
                break;
            case 2:
                sname = s;
                state = 3;
                break;
            case 3:
                if (s.equals(",")) {
                    isArr = false;
                    len = 1;
                    addSynbl();
                    state = 2;
                } else if (s.equals("[")) {
                    state = 4;
                    isArr = true;
                } else if (!(s.equals(";") || s.equals("(") || s.equals(")"))) {
                    state = 3;
                }
                else {
                    isArr = false;
                    len = 1;
                    addSynbl();
                    state = 1;
                }
                break;
            case 4:
                len = Integer.parseInt(s);
                state = 5;
                break;
            case 5:
                if (s.equals("]")) {
                    addSynbl();
                    state = 6;
                }
                break;
            case 6:
                if (s.equals(",")) {
                    state = 2;
                } else {
                    state = 1;
                }
                break;
            case 7:
                sname = s;
                Var v = new Var();
                v.name = sname;
                v.type = stype;
                v.tp = Rinfl.size();
                v.offset = offs;
                Synbl.add(v);
                System.out.println("add" + v);
                state = 8;
                break;
            case 8:
                if (s.equals("{")) {
                    state = 9;
                } else state = 8;
                break;
            case 9:
                if (s.equals("}")) {
                    state = 1;
                } else {
                    stype = s;
                    state = 10;
                }
                break;
            case 10:
                sname = s;
                state = 11;
                break;
            case 11:
                if (s.equals(",")) {
                    isArr = false;
                    len = 1;
                    addRinfi();
                    state = 10;
                } else if (s.equals("[")) {
                    state = 13;
                } else if (s.equals("}")) {
                    isArr = false;
                    len = 1;
                    addRinfi();
                    state = 1;
                } else if (!(s.equals(";") || s.equals("(") || s.equals(")"))) {
                    state = 11;
                } else {
                    isArr = false;
                    len = 1;
                    addRinfi();
                    state = 9;
                }
                break;
            case 12:
                state = 1;
                break;
            case 13:
                len = Integer.parseInt(s);
                state = 14;
                break;
            case 14:
                if (s.equals("]")) {
                    isArr = true;
                    addRinfi();
                    state = 15;
                }
                break;
            case 15:
                if (s.equals(",")) {
                    state = 10;
                } else if (s.equals(";")) {
                    state = 9;
                }
                break;
        }
        return state;
    }

    public void printTable() {
        List<List<Object>> results = new ArrayList<>();
        System.out.println("符号表：");

        List<Object> synblList = new ArrayList<>();
        for (Var v : Synbl) {
            synblList.add(v);
            System.out.printf("%10s", v.name);
            System.out.printf("%10s", v.type);
            System.out.printf("%10s", v.offset);
            System.out.printf("%10s", v.tp);
            System.out.println();
        }
        results.add(synblList);

        List<Object> rinfllList = new ArrayList<>();
        for (Struc s : Rinfl) {
            rinfllList.add(s);
            System.out.printf("%10s", s.name);
            System.out.printf("%10s", s.type);
            System.out.printf("%10s", s.offset);
            System.out.printf("%10s", s.tp);
            System.out.println();
        }
        results.add(rinfllList);
//        return results;
    }

    public LiveStatu getLiveStatu() {
        return liveStatu;
    }

    public void setLiveStatu(LiveStatu liveStatu) {
        this.liveStatu = liveStatu;
    }
}