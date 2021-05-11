package pers.lomesome.compliation.utils.toasm;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Quaternary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectCode {
    public static void Blocked(LiveStatu liveStatu) {
        LinkedList<String> Block = new LinkedList<String>();
        String str = "";
        for (int i = 0; i < liveStatu.getQt().size(); i++) {
            str = liveStatu.getQt().get(i).getFirst();
            if (str.equals("if") || str.equals("el") || str.equals("ie") || str.equals("wh") || str.equals("do") || str.equals("we")) {
                Block.add(str);
                str = liveStatu.getQt().get(i).getSecond();
                Block.add(str);
                str = liveStatu.getQt().get(i).getThird();
                Block.add(str);
                str = liveStatu.getQt().get(i).getFourth();
                Block.add(str);
                WriteAcTable(Block, liveStatu); // 调用WriteAcTable函数填活跃信息表
                Block.clear();
                continue;
            }
            Block.add(str);
            str = liveStatu.getQt().get(i).getSecond();
            Block.add(str);
            str = liveStatu.getQt().get(i).getThird();
            Block.add(str);
            str = liveStatu.getQt().get(i).getFourth();
            Block.add(str);
        }
        if (Block.size() != 0) {
            WriteAcTable(Block, liveStatu); // 调用WriteAcTable函数填活跃信息表
            Block.clear();
        }
    }

    private static void WriteAcTable(LinkedList<String> Block, LiveStatu liveStatu) {
        // 初始化活跃信息表
        String str1 = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        Map<String, Boolean> SYMBL = new HashMap<>();
        for (int i = 0; i < Block.size(); i++) {
            if (i % 4 == 0) {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).equals("_")) {
                    str1 = "Nosense";
                } else if (isTemvar(Block.get(i))) {
                    str1 = "Nonactive";
                    SYMBL.put(Block.get(i), false);
                } else {
                    str1 = "Active";
                    SYMBL.put(Block.get(i), true);
                }
            } else if (i % 4 == 1) {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).equals("_")) {
                    str2 = "Nosense";
                } else if (isTemvar(Block.get(i))) {
                    str2 = "Nonactive";
                    SYMBL.put(Block.get(i), false);
                } else {
                    str2 = "Active";
                    SYMBL.put(Block.get(i), true);
                }
            } else if (i % 4 == 2) {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).equals("_")) {
                    str3 = "Nosense";
                } else if (isTemvar(Block.get(i))) {
                    str3 = "Nonactive";
                    SYMBL.put(Block.get(i), false);
                } else {
                    str3 = "Active";
                    SYMBL.put(Block.get(i), true);
                }
            } else {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).equals("_")) {
                    str4 = "Nosense";
                } else if (isTemvar(Block.get(i))) {
                    str4 = "Nonactive";
                    SYMBL.put(Block.get(i), false);
                } else {
                    str4 = "Active";
                    SYMBL.put(Block.get(i), true);
                }
                Quaternary tem = new Quaternary();
                tem.setFirst(str1);
                tem.setSecond(str2);
                tem.setThird(str3);
                tem.setFourth(str4);
                liveStatu.getActiveLable().add(tem);
            }
        } // 初始化完成
        int number = liveStatu.getActiveLable().size();
        for (int i = Block.size() - 1; i >= 0; i--) {
            if (i % 4 == 3) {
                if (!liveStatu.getActiveLable().get(number - 1).getFourth().equals("Nosense")) {
                    boolean tem = false;
                    tem = SYMBL.get(Block.get(i));
                    if (tem) {
                        str4 = "Active";
                    } else {
                        str4 = "NonActive";
                    }
                    SYMBL.put(Block.get(i), false);
                } else {
                    str4 = "Nosense";
                }
            } else if (i % 4 == 2) {
                if (!liveStatu.getActiveLable().get(number - 1).getThird().equals("Nosense")) {
                    boolean tem = false;
                    tem = SYMBL.get(Block.get(i));
                    if (tem) {
                        str3 = "Active";
                    } else {
                        str3 = "NonActive";
                    }
                    SYMBL.put(Block.get(i), true);
                } else {
                    str3 = "Nosense";
                }
            } else if (i % 4 == 1) {
                if (!liveStatu.getActiveLable().get(number - 1).getSecond().equals("Nosense")) {
                    boolean tem = false;
                    tem = SYMBL.get(Block.get(i));
                    if (tem) {
                        str2 = "Active";
                    } else {
                        str2 = "NonActive";
                    }
                    SYMBL.put(Block.get(i), true);
                } else {
                    str2 = "Nosense";
                }
            } else {
                str1 = liveStatu.getActiveLable().get(number - 1).getFirst();
                Quaternary tem = new Quaternary();
                tem.setFirst(str1);
                tem.setSecond(str2);
                tem.setThird(str3);
                tem.setFourth(str4);
                liveStatu.getActiveLable().set(number - 1, tem);
                number--;
            }
        } // 逆序扫描基本块内的四元式
    }

    private static Boolean isKeyWord(String str) {
        Pattern pattern = Pattern.compile("if|el|ie|wh|do|we");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private static Boolean isDelimiter(String str) {
        Pattern pattern = Pattern.compile("<|>|=|<=|>=|==|\\+|-|\\*|/");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private static Boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private static Boolean isTemvar(String str) {
        Pattern pattern = Pattern.compile("t[0-9]");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
