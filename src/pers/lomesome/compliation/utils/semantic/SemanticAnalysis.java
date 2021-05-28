package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.*;
import pers.lomesome.compliation.tool.finalattr.Constants;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SemanticAnalysis {

    public static String arrNum = "";
    public static int thelevel = 0;

    public static void call(String Action, int TokenIndex, Word word, SymbolTable table, LiveStatu liveStatu) {
        switch (Action) {
            case Constants.PUSH: { // PUSH动作
                Pair<Word, Integer> _pair = new Pair<>(word, TokenIndex);
                liveStatu.getSEM().push(_pair);
                break;
            }

            case Constants.PUSHARG: { // PUSHARG动作
                Pair<Word, Integer> _pair = new Pair<>(word, TokenIndex);
                liveStatu.getArgsStack().push(_pair);
                break;
            }
            case Constants.GEQA: { // GEQA动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("+"));
                tem.setSecond(str2);
                tem.setThird(str1);

                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                Word tempWord = new Word("t" + Analysis.i,"temp");
                tem.setFourth(tempWord);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(tempWord, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }

            case Constants.GEQS: { // GEQS动作
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
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");

                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                Word tempWord = new Word("t" + Analysis.i,"temp");
                tem.setFourth(tempWord);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(tempWord, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case Constants.GEQM: { // GEQM动作
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
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error :" + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                Word tempWord = new Word("t" + Analysis.i,"temp");
                tem.setFourth(tempWord);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(tempWord, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case Constants.GEQD: { // GEQD动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("/"));
                tem.setSecond(str2);
                tem.setThird(str1);
                if (str1.getWord().equals("0")){
                    Analysis.flag = false;
                    System.out.println("error:除数不能为零 position:(" + str1.getRow() + ", " + str1.getCol() + ")");
                    Analysis.errorMsg.add("除数不能为零");
                } else if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                Word tempWord = new Word("t" + Analysis.i,"temp");
                tem.setFourth(tempWord);
                liveStatu.getQt().add(tem);
                getTemLable(tem, table);
                Pair<Word, Integer> _pair = new Pair<>(tempWord, TokenIndex);
                liveStatu.getSEM().push(_pair);
                Analysis.i++;
                break;
            }
            case Constants.ASSI: { // ASSI动作
                Word tem1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word tem2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("="));
                tem.setSecond(tem1);
                tem.setThird(new Word("_"));
                if (!arrNum.equals("")) {
                    tem2.setWord(tem2.getWord() + "[" + arrNum + "]");
                }
                tem.setFourth(tem2);
                if((tem1.getName().contains("const") || tem1.getName().contains("temp"))&& (tem2.getName().contains("const") || tem2.getName().contains("temp"))){
                    setValue(new Word(""), tem2, table);
                } else if (tem1.getName().equals("temp") || tem2.getName().equals("temp")){
                    if (!cheackType(tem2, table).equals(cheackType(tem1, table))){
//                        Analysis.flag = false;
//                        System.out.println(tem2.getWord() + cheackType(tem2, table) + "类型错误");
//                        System.out.println(tem1.getWord() +  cheackType(tem1, table)+ "类型错误");
//                        Analysis.errorMsg.add(tem2.getWord() + "类型错误");
                    }
                } else if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getFourth(), table)) {
                    Analysis.flag = false;
                } else if (!isAssignmentRight(tem1.getName(), cheackType(tem2, table))){
//                    Analysis.flag = false;
                    System.out.println(tem2.getWord() + "类型错误 position :("+tem2.getRow() +","+tem2.getCol()+")");
                    Analysis.errorMsg.add(tem2.getWord() + "类型错误 position :("+tem2.getRow() +","+tem2.getCol()+")");
                } else{
                    setValue(tem1, tem2, table);
                }
                liveStatu.getQt().add(tem);
                arrNum = "";
                break;
            }
            case Constants.GREA: { // GREA动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word(">"));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word(String.valueOf(liveStatu.getQt().size() + 2)));
                if (liveStatu.getSIGNHEAD().size() != 0) {
                    String[] strings = liveStatu.getSIGNHEAD().peek();
                    if (strings[0].equals("dow") && strings[1].equals(String.valueOf(thelevel))) {
                        tem.setFourth(new Word(String.valueOf(strings[2])));
                        liveStatu.getSIGNHEAD().pop();
                    }
                }
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                liveStatu.getQt().add(tem);
                break;
            }
            case Constants.LESS: { // LESS动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("<"));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word(String.valueOf(liveStatu.getQt().size() + 2)));
                if (liveStatu.getSIGNHEAD().size() != 0) {
                    String[] strings = liveStatu.getSIGNHEAD().peek();
                    if (strings[0].equals("dow") && strings[1].equals(String.valueOf(thelevel))) {
                        tem.setFourth(new Word(String.valueOf(strings[2])));
                        liveStatu.getSIGNHEAD().pop();
                    }
                }
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                liveStatu.getQt().add(tem);
                break;
            }
            case Constants.EQUA: { // EQUA动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("=="));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word(String.valueOf(liveStatu.getQt().size() + 2)));
                if (liveStatu.getSIGNHEAD().size() != 0) {
                    String[] strings = liveStatu.getSIGNHEAD().peek();
                    if (strings[0].equals("dow") && strings[1].equals(String.valueOf(thelevel))) {
                        tem.setFourth(new Word(String.valueOf(strings[2])));
                        liveStatu.getSIGNHEAD().pop();
                    }

                }
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                liveStatu.getQt().add(tem);
                break;
            }
            case Constants.GREQ: { // GREQ动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word(">="));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word(String.valueOf(liveStatu.getQt().size() + 2)));
                if (liveStatu.getSIGNHEAD().size() != 0) {
                    String[] strings = liveStatu.getSIGNHEAD().peek();
                    if (strings[0].equals("dow") && strings[1].equals(String.valueOf(thelevel))) {
                        tem.setFourth(new Word(String.valueOf(strings[2])));
                        liveStatu.getSIGNHEAD().pop();
                    }
                }
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                liveStatu.getQt().add(tem);
                break;
            }
            case Constants.LEEQ: { // LEEQ动作
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("<="));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word(String.valueOf(liveStatu.getQt().size() + 2)));
                if (liveStatu.getSIGNHEAD().size() != 0) {
                    String[] strings = liveStatu.getSIGNHEAD().peek();
                    if (strings[0].equals("dow") && strings[1].equals(String.valueOf(thelevel))) {
                        tem.setFourth(new Word(String.valueOf(strings[2])));
                        liveStatu.getSIGNHEAD().pop();
                    }
                }
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                liveStatu.getQt().add(tem);
                break;
            }

            case Constants.NOEQ:{
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("!="));
                tem.setSecond(str2);
                tem.setThird(str1);
                tem.setFourth(new Word(String.valueOf(liveStatu.getQt().size() + 2)));
                if (liveStatu.getSIGNHEAD().size() != 0) {
                    String[] strings = liveStatu.getSIGNHEAD().peek();
                    if (strings[0].equals("dow") && strings[1].equals(String.valueOf(thelevel))) {
                        tem.setFourth(new Word(String.valueOf(strings[2])));
                        liveStatu.getSIGNHEAD().pop();
                    }
                }
                if (!isDefined(tem.getSecond(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getSecond() + " 未定义 position :("+tem.getSecond().getRow() +","+tem.getSecond().getCol()+")");
                } else if (!isDefined(tem.getThird(), table)) {
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                    Analysis.errorMsg.add("error: " + tem.getThird() + " 未定义 position :("+tem.getThird().getRow() +","+tem.getThird().getCol()+")");
                }
                liveStatu.getQt().add(tem);
                break;
            }

            case Constants.IF: { // IF动作
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("if"));
                tem.setSecond(new Word("_"));
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
                break;
            }
            case Constants.EL: { // EL动作
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
                        tem1.setFirst(new Word("j"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth(new Word(String.valueOf(liveStatu.getQt().size())));
                        liveStatu.getQt().set(i, tem1);
                        break;
                    }
                }
                break;
            }
            case Constants.IEFIR: { // IEFIR动作
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("if")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("j"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth( new Word(String.valueOf(liveStatu.getQt().size())));
                        liveStatu.getQt().set(i, tem1);
                        break;
                    }
                }
                break;
            }

            case Constants.IESEC: { // IESEC动作
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("el")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("j"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth(new Word(String.valueOf(liveStatu.getQt().size())));
                        liveStatu.getQt().set(i, tem1);
                    }
                }
                break;
            }

            case Constants.DO: { // DO动作
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("do"));
                tem.setSecond(new Word("_"));
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                liveStatu.getQt().add(tem);
                break;
            }

            case Constants.WE: { // WE动作
                int isFindDo = 0;//如果找到空DO则变为1
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("do")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && isFindDo == 0) {
                        isFindDo = 1;
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("j"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth( new Word(String.valueOf(liveStatu.getQt().size() + 1)));
                        liveStatu.getQt().set(i, tem1);
                    }
                    if (isFindDo == 1 && liveStatu.getQt().get(i).getFirst().equals(new Word("j"))) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("j"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth(new Word(String.valueOf(i - 1)));
                        liveStatu.getQt().add(tem1);
                        break;
                    }
                }
                break;
            }

            case Constants.DOW: { // DO_While动作
                liveStatu.getSIGNHEAD().add(new String[]{"dow", String.valueOf(thelevel), String.valueOf(liveStatu.getQt().size())});
                break;
            }

            case Constants.CALLFUN:
                SymbolTable.Fun f = new SymbolTable.Fun();
                f.name = word.getWord();
                if (FinalAttribute.getSymbolTableMap().get(word.getWord()) == null){
                    System.out.println(f.name + "()函数未定义");
                    Analysis.errorMsg.add(f.name + "()函数未定义");
                    Analysis.flag = false;
                }else {
                    f.type = FinalAttribute.getSymbolTableMap().get(word.getWord()).functype;
                    f.args = FinalAttribute.getSymbolTableMap().get(word.getWord()).argsList;
                    table.Func.add(f);
                    Quaternary q = new Quaternary();
                    q.setFirst(new Word("call"));
                    q.setSecond(new Word(f.name));
                    q.setThird(new Word("_"));
                    q.setFourth(new Word("_"));
                    q.setLevel(thelevel);
                    liveStatu.getQt().add(q);
                }
                break;

            case Constants.PUSHNUM: { // PUSHNUM动作
                arrNum = word.getWord();
                break;
            }

            case Constants.LEVELA: { // LEVELA动作
                thelevel++;
                Analysis.scope++;
                Analysis.actionscope.push(Analysis.scope);
                break;
            }
            case Constants.LEVELS: { // LEVELS动作
                Analysis.actionscope.pop();
                thelevel--;
                break;
            }

            case Constants.ADDFUN: {
                Analysis.nowFunc = word.getWord();
                SymbolTable symbolTable = new SymbolTable(Analysis.nowFunc, liveStatu.getSEM().pop().getFirst().getWord());
                FinalAttribute.addSymbolTable(Analysis.nowFunc, symbolTable);
                if (word.getWord().equals("main")){
                    Quaternary q = new Quaternary();
                    q.setFirst(new Word("main"));
                    q.setSecond(new Word("_"));
                    q.setThird(new Word("_"));
                    q.setFourth(new Word("_"));
                    FinalAttribute.getSymbolTable(word.getWord()).getLiveStatu().getQt().add(q);
                }
            }
            break;

            case Constants.ADDARG: {
                Word str1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word str2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Pair<String, String> pair = new Pair<>(str2.getWord(), str1.getWord());
                FinalAttribute.getSymbolTableMap().get(Analysis.nowFunc).argsList.add(pair);
            }
            break;

            case Constants.RE: {
                Quaternary tem = new Quaternary();
                tem.setFirst(new Word("ret"));
                tem.setSecond(new Word("_"));
                tem.setThird(new Word("_"));
                tem.setFourth(new Word("_"));
                tem.setLevel(thelevel);
                liveStatu.getQt().add(tem);
            }
            break;

            case Constants.RET: {
                Word str = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("ret")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        Quaternary tem1 = new Quaternary();
                        tem1.setFirst(new Word("ret"));
                        tem1.setSecond(new Word("_"));
                        tem1.setThird(new Word("_"));
                        tem1.setFourth(new Word(str.getWord()));
                        liveStatu.getQt().set(i, tem1);
                    }
                }
            }
            break;

            case Constants.CALLS: {
                List<Word> list = new ArrayList<>();
                while (!liveStatu.getArgsStack().empty()){
                    list.add(0, liveStatu.getArgsStack().pop().getFirst());
                }
                for (int i = liveStatu.getQt().size() - 1; i >= 0; i--) {
                    if (liveStatu.getQt().get(i).getFirst().equals(new Word("call")) && liveStatu.getQt().get(i).getFourth().equals(new Word("_")) && liveStatu.getQt().get(i).getLevel() == thelevel) {
                        liveStatu.getQt().get(i).setThird(new Word(list.toString()));
                    }
                }
            }
            break;

            case Constants.PRINT: {
                Word word1 = liveStatu.getSEM().peek().getFirst();
                Quaternary quaternary = new Quaternary();
                quaternary.setFirst(new Word("print"));
                quaternary.setSecond(new Word("_"));
                quaternary.setThird(new Word("_"));
                quaternary.setFourth(word1);
                liveStatu.getQt().add(quaternary);
            }
            break;

            case Constants.INDE: {
                Word word1 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Word word2 = liveStatu.getSEM().peek().getFirst();
                liveStatu.getSEM().pop();
                Quaternary quaternary = new Quaternary();
                if (word1.getWord().equals("++")){
                    quaternary.setFirst(new Word("+"));
                }else if (word1.getWord().equals("--")){
                    quaternary.setFirst(new Word("-"));
                }
                quaternary.setSecond(word2);
                quaternary.setThird(new Word("1"));
                quaternary.setFourth(word2);
                liveStatu.getQt().add(quaternary);
            }
            break;

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
                    for (SymbolTable.Cons c : table.Const) {
                        if (c.name.equals(tem.getSecond().getWord())) {
                            type1 = c.type;
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
                        if (v.name.equals(tem.getThird().getWord())) {
                            type2 = v.type;
                            break;
                        }
                    }
                    for (SymbolTable.Cons c : table.Const) {
                        if (c.name.equals(tem.getThird().getWord())) {
                            type2 = c.type;
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
            } else if ((type1.contains("int") && type2.contains("float")) || (type1.contains("float") && type2.contains("int"))) {
                newtemp.type = "float";
            } else if ((type1.contains("float") && type2.contains("float"))) {
                newtemp.type = "float";
            } else {
                if (isDefined(tem.getSecond(), table) && !tem.getSecond().getName().equals("temp") ) {
                    Analysis.flag = false;
                    System.out.println("error: "+ tem.getSecond().getWord()  + " 类型不匹配 position: ("+ tem.getSecond().getRow() + ", "+ tem.getSecond().getCol() +")");
                    Analysis.errorMsg.add("error: "+ tem.getSecond().getWord()  + " 类型不匹配 position: ("+ tem.getSecond().getRow() + ", "+ tem.getSecond().getCol() +")");
                }else if (isDefined(tem.getThird(), table) && !tem.getThird().getName().equals("temp")){
                    Analysis.flag = false;
                    System.out.println("error: " + tem.getThird().getWord() + " 类型不匹配 position: ("+ tem.getThird().getRow() + ", "+ tem.getThird().getCol() +")");
                    Analysis.errorMsg.add("error: " + tem.getThird().getWord() + " 类型不匹配 position: ("+ tem.getThird().getRow() + ", "+ tem.getThird().getCol() +")");
                }
                newtemp.type = "";
            }
            if (!newtemp.type.equals("")) {
                int size = 0;
                String num1 = tem.getSecond().getWord();
                String num2 = tem.getThird().getWord();

                if (tem.getSecond().getName().equals("id") || tem.getSecond().getName().equals("temp")){
                    num1 = table.getValue(tem.getSecond());
                }
                if (tem.getThird().getName().equals("id") || tem.getThird().getName().equals("temp")){
                    num2 = table.getValue(tem.getThird());
                }

                if (num1 == null){
                    Analysis.flag = false;
                    System.out.println("error: "+tem.getSecond().getWord() +" 值未初始化 positon: (" + tem.getSecond().getRow() +", " + tem.getSecond().getCol() +")");
                    Analysis.errorMsg.add("error: "+tem.getSecond().getWord() +" 值未初始化 positon: (" + tem.getSecond().getRow() +", " + tem.getSecond().getCol() +")");
                    return;
                }
                if (num2 == null){
                    Analysis.flag = false;
                    System.out.println("error: "+tem.getThird().getWord() +" 值未初始化 positon: (" + tem.getThird().getRow() +", " + tem.getThird().getCol() +")");
                    Analysis.errorMsg.add("error: "+tem.getThird().getWord() +" 值未初始化 positon: (" + tem.getThird().getRow() +", " + tem.getThird().getCol() +")");
                    return;
                }
                switch (newtemp.type) {
                    case "int":
                        size = 4;
                        newtemp.value = "";
                        break;
                    case "char":
                        size = 1;
                        break;
                    case "float":
                        size = 8;
                        newtemp.value = "";
                        break;
                    case "String":
                        size = 50;
                        break;
                }

                newtemp.offset = table.Synbl.get(table.Synbl.size() - 1).offset + size;
                newtemp.scope = new ArrayList<>(Analysis.actionscope);
                newtemp.isTemp = true;
                table.Synbl.add(newtemp);
            }
        }
    }

    private static String calculation(int num1, int num2, String sybol){
        switch (sybol){
            case "+" : return String.valueOf(num1 + num2);
            case "-" : return String.valueOf(num1 - num2);
            case "*" : return String.valueOf(num1 * num2);
            case "/" :
                if (num2 == 0)
                    return null;
                return String.valueOf(num1 / num2);
        }
        return null;
    }

    private static String calculation(float num1, float num2, String sybol){
        switch (sybol){
            case "+" : return String.valueOf(num1 + num2);
            case "-" : return String.valueOf(num1 - num2);
            case "*" : return String.valueOf(num1 * num2);
            case "/" :
                if (num2 == 0)
                    return null;
                return String.valueOf(num1 / num2);
        }
        return null;
    }

    private static boolean isDefined(Word name, SymbolTable table) {
        if (isNumber(name)) {
            return true;
        } else if (name.getName().equals("temp")) {
            return true;
        } else if (name.getWord().contains("[")) {
            String[] s = name.getWord().split("\\[");
            String[] ss = s[1].split("\\]");
            for (SymbolTable.Var v : table.Synbl) {
                if (v.name.equals(s[0])) {
                    if (Integer.parseInt(ss[0]) >= v.tp) {
                        System.out.println(name + "数组越界");
                        Analysis.flag = false;
                        Analysis.errorMsg.add("error: "+name + " 数组越界 position :("+name.getRow() +","+name.getCol()+")");
                        return false;
                    }
                    return true;
                }
            }
            for (SymbolTable.Cons c : table.Const) {
                if (c.name.equals(s[0])) {
                    if (Integer.parseInt(ss[0]) >= c.tp) {
                        System.out.println(name + "数组越界");
                        Analysis.flag = false;
                        Analysis.errorMsg.add("error: "+name + " 数组越界 position :("+name.getRow() +","+name.getCol()+")");
                        return false;
                    }
                    return true;
                }
            }
        } else {
            for (SymbolTable.Var v : table.Synbl) {
                Stack<Integer> stack = new Stack<>();
                for (Integer integer : Analysis.actionscope){
                    stack.push(integer);
                }
                do {
                    if (v.name.equals(name.getWord()) && isScope(v.scope, stack)) {
                        return true;
                    }
                    if (stack.size() > 0)
                        stack.pop();
                }while (stack.size() > 0);
            }
            for (SymbolTable.Cons c : table.Const) {
                Stack<Integer> stack = new Stack<>();
                for (Integer integer : Analysis.actionscope){
                    stack.push(integer);
                }
                do {
                    if (c.name.equals(name.getWord()) && isScope(c.scope, stack)) {
                        return true;
                    }
                    if (stack.size() > 0)
                        stack.pop();
                }while (stack.size() > 0);
            }
        }
        System.out.println("error: " + name.getWord() + " 未定义 position :("+name.getRow() +","+name.getCol()+")");
        Analysis.flag = false;
        Analysis.errorMsg.add("error: " + name.getWord() + " 未定义 position :("+name.getRow() +","+name.getCol()+")");
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
            return a1.contains(a2.replace("[","").replace("]",""));
        }
    }

    private static String cheackType(Word word, SymbolTable table){
        String type = null;
        String name = word.getWord() ;
        if (word.getWord().contains("[")) {
            name = word.getWord().split("\\[")[0];
        }

        for (SymbolTable.Var v : table.Synbl) {
            if (v.name.equals(name)) {
                type =  v.type;
            }
        }
        for (SymbolTable.Var v : table.Temp) {
            if (v.name.equals(name)) {
                type =  v.type;
            }
        }
        for (SymbolTable.Cons c : table.Const) {
            if (c.name.equals(name)) {
                type =  c.type;
            }
        }
        return type;
    }

    private static void setValue(Word word1, Word word2, SymbolTable table){
        for (SymbolTable.Var v : table.Synbl) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }
            if (v.name.equals(word2.getWord()) && isScope(v.scope, stack)) {
                v.value = word1.getWord();
                return;
            }
        }
        for (SymbolTable.Cons c : table.Const) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }
            if (c.name.equals(word2.getWord()) && isScope(c.scope, stack)) {
                if (c.value == null)
                    c.value = word1.getWord();
                else {
                    Analysis.flag = false;
                    Analysis.errorMsg.add("error: " + word2.getWord() + " 常量不能被重复赋值 position :(" + word2.getRow() + "," + word2.getCol() + ")");
                    System.out.println("error: " + word2.getWord() + " 常量不能被重复赋值 position :(" + word2.getRow() + "," + word2.getCol() + ")");
                }
                return;
            }
        }
        for (SymbolTable.Var v : table.Synbl) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }
            do {
                if (v.name.equals(word2.getWord()) && isScope(v.scope, stack)) {
                    v.value = word1.getWord();
                    return;
                }
                if (stack.size() > 0)
                    stack.pop();
            }while (stack.size() > 0);
        }

        for (SymbolTable.Cons c : table.Const) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionscope){
                stack.push(integer);
            }
            do {
                if (c.name.equals(word2.getWord()) && isScope(c.scope, stack)) {
                    if (c.value == null)
                        c.value = word1.getWord();
                    else{
                        Analysis.flag = false;
                        Analysis.errorMsg.add("error: " + word2.getWord() + " 常量不能被重复赋值 position :(" + word2.getRow() + "," + word2.getCol() + ")");
                        System.out.println("error: " + word2.getWord() + " 常量不能被重复赋值 position :(" + word2.getRow() + "," + word2.getCol() + ")");
                    }
                    return;
                }
                if (stack.size() > 0)
                    stack.pop();
            }while (stack.size() > 0);
        }
    }

    private static boolean isScope(List<Integer> scopeList, Stack<Integer> actionscope){
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

    public static void printQuaternary(LiveStatu liveStatu) {
        System.out.println("四元式：");
        for (int i = 0; i < liveStatu.getQt().size(); i++) {
            System.out.println(i + " " + liveStatu.getQt().get(i));
        }
    }
}
