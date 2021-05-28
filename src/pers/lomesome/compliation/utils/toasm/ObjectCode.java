package pers.lomesome.compliation.utils.toasm;

import pers.lomesome.compliation.model.LiveStatu;
import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.model.Word;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectCode {
    public static void Blocked(LiveStatu liveStatu) {
        LinkedList<Word> Block = new LinkedList<>();
        Word str ;
        for (int i = 0; i < liveStatu.getQt().size(); i++) {
            str = liveStatu.getQt().get(i).getFirst();
            if (str.getWord().equals("j")) {
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

    private static void WriteAcTable(LinkedList<Word> Block, LiveStatu liveStatu) {
        // 初始化活跃信息表
        Word str1 = new Word();
        Word str2 = new Word();
        Word str3 = new Word();
        Word str4 = new Word();
        Map<Word, Boolean> SYMBL = new HashMap<>();
        for (int i = 0; i < Block.size(); i++) {
            if (i % 4 == 0) {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).getWord().equals("_")) {
                    str1 = new Word("Nosense");
                } else if (isTemvar(Block.get(i))) {
                    str1 = new Word("Nonactive");
                    SYMBL.put(Block.get(i), false);
                } else {
                    str1 = new Word("Active");
                    SYMBL.put(Block.get(i), true);
                }
            } else if (i % 4 == 1) {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).getWord().equals("_")) {
                    str2 = new Word("Nosense");
                } else if (isTemvar(Block.get(i))) {
                    str2 = new Word("Nonactive");
                    SYMBL.put(Block.get(i), false);
                } else {
                    str2 = new Word("Active");
                    SYMBL.put(Block.get(i), true);
                }
            } else if (i % 4 == 2) {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).getWord().equals("_")) {
                    str3 = new Word("Nosense");
                } else if (isTemvar(Block.get(i))) {
                    str3 = new Word("Nonactive");
                    SYMBL.put(Block.get(i), false);
                } else {
                    str3 = new Word("Active");
                    SYMBL.put(Block.get(i), true);
                }
            } else {
                if (isKeyWord(Block.get(i)) || isDelimiter(Block.get(i)) || isNumber(Block.get(i)) || Block.get(i).getWord().equals("_")) {
                    str4 = new Word("Nosense");
                } else if (isTemvar(Block.get(i))) {
                    str4 = new Word("Nonactive");
                    SYMBL.put(Block.get(i), false);
                } else {
                    str4 = new Word("Active");
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
                if (!liveStatu.getActiveLable().get(number - 1).getFourth().getWord().equals("Nosense")) {
                    boolean tem = false;
                    tem = SYMBL.get(Block.get(i));
                    if (tem) {
                        str4 = new Word("Active");
                    } else {
                        str4 = new Word("NonActive");
                    }
                    SYMBL.put(Block.get(i), false);
                } else {
                    str4 = new Word("Nosense");
                }
            } else if (i % 4 == 2) {
                if (!liveStatu.getActiveLable().get(number - 1).getThird().getWord().equals("Nosense")) {
                    boolean tem = false;
                    tem = SYMBL.get(Block.get(i));
                    if (tem) {
                        str3 = new Word("Active");
                    } else {
                        str3 = new Word("NonActive");
                    }
                    SYMBL.put(Block.get(i), true);
                } else {
                    str3 = new Word("Nosense");
                }
            } else if (i % 4 == 1) {
                if (!liveStatu.getActiveLable().get(number - 1).getSecond().getWord().equals("Nosense")) {
                    boolean tem = false;
                    tem = SYMBL.get(Block.get(i));
                    if (tem) {
                        str2 = new Word("Active");
                    } else {
                        str2 = new Word("NonActive");
                    }
                    SYMBL.put(Block.get(i), true);
                } else {
                    str2 = new Word("Nosense");
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

    private static Boolean isKeyWord(Word str) {
        Pattern pattern = Pattern.compile("j");
        Matcher matcher = pattern.matcher(str.getWord());
        return matcher.matches();
    }

    private static Boolean isDelimiter(Word str) {
        Pattern pattern = Pattern.compile("<|>|=|<=|>=|==|\\+|-|\\*|/");
        Matcher matcher = pattern.matcher(str.getWord());
        return matcher.matches();
    }

    private static Boolean isNumber(Word str) {
        return str.getName().contains("const");
    }

    private static Boolean isTemvar(Word str) {
        return str.getName().equals("temp");
    }
}
