package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.model.Pair;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticAnalysis {

    public static String arrNum = "";
    public static int thelevel = 0;

    public static void call(String Action, int TokenIndex, String curstr, SymbolTable table, LiveStatu liveStatu) throws IOException {
        switch (FinalAttribute.getActionMap().get(Action)) {
            case 1: { // PUSH动作
                Pair<String, Integer> _pair = new Pair<>(curstr, TokenIndex);
                liveStatu.getSEM().push(_pair);
                break;
            }
            case 2: { // GEQA动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("+");
                tem.setSecond(str2);
                tem.setThird(str1);

                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                    System.out.println(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                    System.out.println(tem.getThird() + "未定义");
                }
                tem.setFourth("t" + Analysis.i);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 3: { // GEQS动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("-");
                tem.setSecond(str2);
                tem.setThird(str1);

                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");

                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                tem.setFourth("t" + Analysis.i);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 4: { // GEQM动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("*");
                tem.setSecond(str2);
                tem.setThird(str1);

                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                tem.setFourth("t" + Analysis.i);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<String, Integer> _pair = new Pair<>("t" + String.valueOf(Analysis.i), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 5: { // GEQD动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("/");
                tem.setSecond(str2);
                tem.setThird(str1);

                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                tem.setFourth("t" + Analysis.i);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 6: { // ASSI动作
                String tem1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String tem2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("=");
                tem.setSecond(tem1);
                tem.setThird("_");
                if (!arrNum.equals("")) {
                    tem2 = tem2 + "[" + arrNum + "]";
                }
                tem.setFourth(tem2);

                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getFourth(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getFourth() + "未定义");
                    Analysis.errorMsg.add(tem.getFourth() + "未定义");
                }
                liveStatu.getQt().add(tem);
                arrNum = "";
                break;
            }
            case 7: { // GEQG动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(">");
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth("t" + Analysis.i);
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                liveStatu.getQt().add(tem);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 8: { // GEQL动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("<");
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth("t" + Analysis.i);
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                liveStatu.getQt().add(tem);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 9: { // GEQE动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("==");
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth("t" + Analysis.i);
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                liveStatu.getQt().add(tem);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 10: { // GEQGE动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(">=");
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth("t" + Analysis.i);
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                liveStatu.getQt().add(tem);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 11: { // GEQLE动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                String str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst("<=");
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth("t" + Analysis.i);
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getSecond() + "未定义");
                    Analysis.errorMsg.add(tem.getSecond() + "未定义");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println(tem.getThird() + "未定义");
                    Analysis.errorMsg.add(tem.getThird() + "未定义");
                }
                liveStatu.getQt().add(tem);
                Pair<String, Integer> _pair = new Pair<>("t" + Analysis.i, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 12: { // IF动作
                String str1 = liveStatu.getSEM().peek().getFirst();
                Quaternary tem = new Quaternary();
                tem.setFirst("if");
                tem.setSecond(str1);
                tem.setThird("_");
                tem.setFourth("_");
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                liveStatu.getSEM().pop();
                break;
            }
            case 13: { // EL动作
                Quaternary tem = new Quaternary();
                tem.setFirst("el");
                tem.setSecond("_");
                tem.setThird("_");
                tem.setFourth("_");
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals("if") && liveStatu.getQt().get(i).getFourth().equals("_") && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst("if");
                        tem1.setSecond(liveStatu.getQt().get(i).getSecond());
                        tem1.setThird("_");
                        tem1.setFourth(String.valueOf(liveStatu.getQt().size()));
                        liveStatu.getQt().set(i, tem1);
                        break;
                    }
                }
                break;
            }
            case 14: { // IEFIR动作
                Quaternary tem = new Quaternary();
                tem.setFirst("ie");
                tem.setSecond("_");
                tem.setThird("_");
                tem.setFourth("_");
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals("if") && liveStatu.getQt().get(i).getFourth().equals("_")) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst("if");
                        tem1.setSecond(liveStatu.getQt().get(i).getSecond());
                        tem1.setThird("_");
                        tem1.setFourth(String.valueOf(liveStatu.getQt().size() - 1));
                        liveStatu.getQt().set(i, tem1);
                        break;
                    }
                }
                break;
            }

            case 15: { // IESEC动作
                Quaternary tem = new Quaternary();
                tem.setFirst("ie");
                tem.setSecond("_");
                tem.setThird("_");
                tem.setFourth("_");
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                int Number = 0;
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals("el") && Number >= 0) {
                        Number++;
                        if (liveStatu.getQt().get(i).getFourth().equals("_")) {
                            Quaternary tem1 = new Quaternary();
                            tem1.setFirst("el");
                            tem1.setSecond("_");
                            tem1.setThird("_");
                            tem1.setFourth(String.valueOf(liveStatu.getQt().size() - 1));
                            liveStatu.getQt().set(i, tem1);
                        }
                    }
                    if (liveStatu.getQt().get(i).getFirst().equals("if")) {
                        Number--;
                        if (Number < 0) {
                            break;
                        }
                    }
                    if (liveStatu.getQt().get(i).getFirst().equals("el") && liveStatu.getQt().get(i).getFourth().equals("_") && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst("el");
                        tem1.setSecond("_");
                        tem1.setThird("_");
                        tem1.setFourth(String.valueOf(liveStatu.getQt().size() - 1));
                        liveStatu.getQt().set(i, tem1);
                    }
                }
                break;
            }
            case 16: { // WH动作
                Quaternary tem = new Quaternary();
                tem.setFirst("wh");
                tem.setSecond("_");
                tem.setThird("_");
                tem.setFourth("_");
                liveStatu.getQt().add(tem);
                break;
            }
            case 17: { // DO动作
                Quaternary tem = new Quaternary();
                tem.setFirst("do");
                tem.setSecond(liveStatu.getSEM().peek().getFirst());
                tem.setThird("_");
                tem.setFourth("_");
                liveStatu.getQt().add(tem);
                liveStatu.getSEM().pop();
                break;
            }
            case 18: { // WE动作
                int isFindDo = 0;//如果找到空DO则变为1
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals("do") && liveStatu.getQt().get(i).getFourth().equals("_") && isFindDo == 0) {
                        isFindDo = 1;
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst("do");
                        tem1.setSecond(liveStatu.getQt().get(i).getSecond());
                        tem1.setThird("_");
                        tem1.setFourth(String.valueOf(liveStatu.getQt().size() + 1));
                        liveStatu.getQt().set(i, tem1);
                    }
                    if (isFindDo == 1 && liveStatu.getQt().get(i).getFirst().equals("wh")) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst("we");
                        tem1.setSecond("_");
                        tem1.setThird("_");
                        tem1.setFourth(String.valueOf(i + 1));
                        liveStatu.getQt().add(tem1);
                        break;
                    }
                }
                break;
            }
            case 19: { // PUSHNUM动作
                arrNum = curstr;
                break;
            }

            case 20: { // LEVELA动作
                thelevel++;
                break;
            }
            case 21: { // LEVELS动作
                thelevel--;
                break;
            }
        }
    }

    public static void getTemLable(Quaternary tem, SymbolTable table) throws IOException {
        SymbolTable.Var newtemp = new SymbolTable.Var();
        if (tem.getFirst().equals("+") || tem.getFirst().equals("-") || tem.getFirst().equals("*") || tem.getFirst().equals("/")) {
            newtemp.name = tem.getFourth();
            newtemp.offset = 0;
            newtemp.tp = 0;
            String type1 = "";
            String type2 = "";
            if (isNumber(tem.getSecond())) {
                type1 = "int";
            } else if (tem.getSecond().charAt(0) == '\'') {
                type1 = "char";
            } else {
                for (SymbolTable.Var v : table.Synbl) {
                    if (v.name.equals(tem.getSecond())) {
                        type1 = v.type;
                        break;
                    }
                }
            }
            if (isNumber(tem.getThird())) {
                type2 = "int";
            } else if (tem.getThird().charAt(0) == '\'') {
                type2 = "char";
            } else {
                for (SymbolTable.Var v : table.Synbl) {
                    if (v.name.equals(tem.getThird())) {
                        type2 = v.type;
                        break;
                    }
                }
            }
            if ((type1.contains("int")) && (type2.contains("int"))) {
                newtemp.type = "int";
            } else if ((type1.contains("char")) && type2.contains("char")) {
                newtemp.type = "char";
            } else if ((type1.contains("int") && type2.contains("char")) || (type1.contains("char") && type2.contains("int"))) {
                newtemp.type = "int";
            } else {
                if ((isDefined(tem.getSecond(), table) && isDefined(tem.getThird(), table)) && !isTempvar(tem.getSecond()) && !isTempvar(tem.getThird())) {
                    Analysis.flag = false;
                    System.out.println("类型不匹配");
                    Analysis.errorMsg.add("类型不匹配");
                }
                newtemp.type = "";
            }
            if (!newtemp.type.equals("")) {
                int size = 0;
                switch (table.Synbl.get(table.Synbl.size() - 1).type) {
                    case "int":
                        size = 1;
                        break;
                    case "char":
                        size = 1;
                        break;
                    case "float":
                        size = 2;
                        break;
                    case "String":
                        size = 50;
                        break;
                }
                newtemp.offset = table.Synbl.get(table.Synbl.size() - 1).offset + size;
                table.Synbl.add(newtemp);
            }
        }
    }

    static boolean isDefined(String name, SymbolTable table) {
        if (isNumber(name)) {
            return true;
        } else if (name.charAt(0) == '\'') {
            return true;
        } else if (isTempvar(name)) {
            return true;
        } else if (name.contains("[")) {
            String[] s = name.split("\\[");
            String[] ss = s[1].split("\\]");
            for (SymbolTable.Var v : table.Synbl) {
                if (v.name.equals(s[0])) {
                    if (Integer.parseInt(ss[0]) >= v.tp) {
                        System.out.println(name + "数组越界");
                        Analysis.flag = false;
                        Analysis.errorMsg.add(name + "数组越界");
                    }
                    return true;
                }
            }
        } else {
            for (SymbolTable.Var v : table.Synbl) {
                if (v.name.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static Boolean isTempvar(String str) {
        Pattern pattern = Pattern.compile("t[0-9]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static void printQuaternary(LiveStatu liveStatu) {
        System.out.println("四元式：");
        for (int i = 0; i < liveStatu.getQt().size(); i++) {
            System.out.println(i + " " + liveStatu.getQt().get(i));
        }
    }
}
