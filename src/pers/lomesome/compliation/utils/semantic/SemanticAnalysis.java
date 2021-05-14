package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.model.Pair;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticAnalysis {

    public static String arrNum = "";
    public static int thelevel = 0;

    public static void call(String Action, int TokenIndex, Word curstr, SymbolTable table, LiveStatu liveStatu) throws IOException {
        switch (FinalAttribute.getActionMap().get(Action)) {
            case 1: { // PUSH动作
                Pair<Word, Integer> _pair = new Pair<>(curstr, TokenIndex);
                System.out.println("push");
                liveStatu.getSEM().push(_pair);
                break;
            }
            case 2: { // GEQA动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                System.out.println("pop1: " +str1);
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                System.out.println("pop2: " + str2);
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("+"));
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
                tem.setFourth(new Word("t" + Analysis.i));
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 3: { // GEQS动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("-"));
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
                tem.setFourth(new Word("t" + Analysis.i));
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 4: { // GEQM动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("*"));
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
                tem.setFourth(new Word("t" + Analysis.i));
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 5: { // GEQD动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("/"));
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
                tem.setFourth(new Word("t" + Analysis.i));
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 6: { // ASSI动作
                Word tem1 = liveStatu.getSEM().peek().getFirst();
                System.out.println("ASSI pop1: "+ tem1);
                liveStatu.getSEM().pop();
                Word tem2 = liveStatu.getSEM().peek().getFirst();
                System.out.println("ASSI pop2: "+ tem2);
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("="));
                tem.setSecond(tem1);
                tem.setThird(new Word("_"));
                if (!arrNum.equals("")) {
                    tem2.setWord(tem2.getWord() + "[" + arrNum + "]");
                }
                tem.setFourth(tem2);

                if (tem1.getWord().startsWith("t") || tem2.getWord().startsWith("t")){
                    if (!cheackType(tem2, table).equals(cheackType(tem1, table))){
//                        Analysis.flag = false;
                        System.out.println(tem2.getWord() + cheackType(tem2, table) + "类型错误");
                        System.out.println(tem1.getWord() +  cheackType(tem1, table)+ "类型错误");
                        Analysis.errorMsg.add(tem2.getWord() + "类型错误");
                    }
                } else if (!isAssignmentRight(tem1.getName(), cheackType(tem2, table))){
                    Analysis.flag = false;
                    System.out.println(tem2.getWord() + "类型错误");
                    Analysis.errorMsg.add(tem2.getWord() + "类型错误");
                }else if (!isDefined(tem.getSecond(), table)) {
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
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word(">"));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word("t" + Analysis.i));
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
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 8: { // GEQL动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("<"));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word("t" + Analysis.i));
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
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 9: { // GEQE动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("=="));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word("t" + Analysis.i));
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
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 10: { // GEQGE动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word(">="));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word("t" + Analysis.i));
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
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 11: { // GEQLE动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("<="));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word("t" + Analysis.i));
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
                Pair<Word, Integer> _pair = new Pair<>(new Word("t" + Analysis.i, str1.getName()), TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case 12: { // IF动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("if"));
                tem.setSecond(str1);
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                tem.setLevel(thelevel);
                System.out.println("thelevel"+thelevel);
                liveStatu.getQt().add(tem);
                liveStatu.getSEM().pop();
                break;
            }
            case 13: { // EL动作
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("el"));
                tem.setSecond(new Word("_"));
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("if")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("if"));
                        tem1.setSecond(liveStatu.getQt().get(i).getSecond());
                        tem1.setThird(new Word("_"));
                        tem1.setFourth(new Word(String.valueOf(liveStatu.getQt().size())));
                        liveStatu.getQt().set(i, tem1);
                        break;
                    }
                }
                break;
            }
            case 14: { // IEFIR动作
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("ie"));
                tem.setSecond(new Word("_"));
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("if")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_"))) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("if"));
                        tem1.setSecond(liveStatu.getQt().get(i).getSecond());
                        tem1.setThird(new Word("_"));
                        tem1.setFourth( new Word(String.valueOf(liveStatu.getQt().size() - 1)));
                        liveStatu.getQt().set(i, tem1);
                        break;
                    }
                }
                break;
            }

            case 15: { // IESEC动作
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("ie"));
                tem.setSecond(new Word("_"));
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                int Number = 0;
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("el")) && Number >= 0) {
                        Number++;
                        if (liveStatu.getQt().get(i).getFourth().equals(new Word("_"))) {
                            Quaternary tem1 = new Quaternary();
                            tem1.setFirst(new Word("el"));
                            tem1.setSecond(new Word("_"));
                            tem1.setThird(new Word("_"));
                            tem1.setFourth(new Word(String.valueOf(liveStatu.getQt().size() - 1)));
                            liveStatu.getQt().set(i, tem1);
                        }
                    }
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("if"))) {
                        Number--;
                        if (Number < 0) {
                            break;
                        }
                    }
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("el")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("el"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth(new Word(String.valueOf(liveStatu.getQt().size() - 1)));
                        liveStatu.getQt().set(i, tem1);
                    }
                }
                break;
            }
            case 16: { // WH动作
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("wh"));
                tem.setSecond(new Word("_"));
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                liveStatu.getQt().add(tem);
                break;
            }
            case 17: { // DO动作
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("do"));
                tem.setSecond(liveStatu.getSEM().peek().getFirst());
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                liveStatu.getQt().add(tem);
                liveStatu.getSEM().pop();
                break;
            }
            case 18: { // WE动作
                int isFindDo = 0;//如果找到空DO则变为1
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("do")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && isFindDo == 0) {
                        isFindDo = 1;
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("do"));
                        tem1.setSecond(liveStatu.getQt().get(i).getSecond());
                        tem1.setThird(new Word("_"));
                        tem1.setFourth( new Word(String.valueOf(liveStatu.getQt().size() + 1)));
                        liveStatu.getQt().set(i, tem1);
                    }
                    if (isFindDo == 1 && liveStatu.getQt().get(i).getFirst().equals(new Word("wh"))) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("we"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth(new Word(String.valueOf(i + 1)));
                        liveStatu.getQt().add(tem1);
                        break;
                    }
                }
                break;
            }
            case 19: { // PUSHNUM动作
                arrNum = curstr.getWord();
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

            case 22: {
                Analysis.nowFunc = curstr.getWord();
                SymbolTable symbolTable = new SymbolTable(Analysis.nowFunc);
                FinalAttribute.addSymbolTable(Analysis.nowFunc, symbolTable);
            }
        }
    }

    public static void getTemLable(Quaternary tem, SymbolTable table) {
        SymbolTable.Var newtemp = new SymbolTable.Var();
        if (tem.getFirst().equals(new Word("+")) || tem.getFirst().equals(new Word("-")) || tem.getFirst().equals(new Word("*")) || tem.getFirst().equals(new Word("/"))) {
            newtemp.name = tem.getFourth().getWord();
            newtemp.offset = 0;
            newtemp.tp = 0;
            String type1 = "";
            String type2 = "";
            switch (tem.getSecond().getName()) {
                case "int_const":
                    type1 = "int";
                    break;
                case "char_const":
                    type1 = "char";
                    break;
                case "float_const":
                    type1 = "float";
                    break;
                default:
                    for (SymbolTable.Var v : table.Synbl) {
                        if (v.name.equals(tem.getSecond().getWord())) {
                            type1 = v.type;
                            break;
                        }
                    }
                    break;
            }
            switch (tem.getThird().getName()) {
                case "int_const":
                    type2 = "int";
                    break;
                case "char_const":
                    type2 = "char";
                    break;
                case "float_const":
                    type2 = "float";
                    break;
                default:
                    for (SymbolTable.Var v : table.Synbl) {
                        if (v.name.equals(tem.getSecond().getWord())) {
                            type2 = v.type;
                            break;
                        }
                    }
                    break;
            }
            if ((type1.contains("int")) && (type2.contains("int"))) {
                newtemp.type = "int";
            } else if ((type1.contains("char")) && type2.contains("char")) {
                newtemp.type = "char";
            } else if ((type1.contains("int") && type2.contains("char")) || (type1.contains("char") && type2.contains("int"))) {
                newtemp.type = "int";
            } else {
                if ((isDefined(tem.getSecond(), table) && isDefined(tem.getThird(), table)) && !isTempvar(tem.getSecond().getWord()) && !isTempvar(tem.getThird().getWord())) {
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
                newtemp.offset = table.Synbl.get(table.Synbl.size() - 1).offset + size;
                table.Temp.add(newtemp);
            }
        }
    }
    

    private static boolean isDefined(Word name, SymbolTable table) {
        if (isNumber(name)) {
            return true;
        } else if (isTempvar(name.getWord())) {
            return true;
        } else if (name.getName().contains("[")) {
            String[] s = name.getWord().split("\\[");
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
                if (v.name.equals(name.getWord())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isAssignmentRight(String a1, String a2){
        if (a2.equals("float")){
            switch (a1){
                case "int_const" :
                case "float_const":
                    return true;
                default:
                    return false;
            }
        }else {
            return a1.contains(a2);
        }
    }

    private static String cheackType(Word word, SymbolTable table){
        String type = null;
        for (SymbolTable.Var v : table.Synbl) {
            if (v.name.equals(word.getWord())) {
                type =  v.type;
            }
        }
        for (SymbolTable.Var v : table.Temp) {
            if (v.name.equals(word.getWord())) {
                type =  v.type;
            }
        }
        return type;
    }
    
    public static Boolean isNumber(Word word){
        switch (word.getName()){
            case "int_const":
            case "char_const":
            case "float_const": 
                return true;
            default:
                return false;
        }
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
