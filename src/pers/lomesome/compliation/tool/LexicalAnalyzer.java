package pers.lomesome.compliation.tool;

import pers.lomesome.compliation.controller.ReadAndWriteFile;
import pers.lomesome.compliation.model.FinalAttribute;
import pers.lomesome.compliation.model.Constants;
import pers.lomesome.compliation.model.Word;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    private StringBuilder word = null;
    private final List<Word> words = new ArrayList<>();
    private final List<Word> errorMsgList = new ArrayList<>();
    private int row = 1;
    private int col = 1;
    private final char[] content;
    private int index = -1;

    //构造函数，输入是文件内容
    public LexicalAnalyzer(String fileName) {
        this.content = ReadAndWriteFile.readFileContent(fileName).toCharArray();
    }

    //判断是否是下等号
    public boolean isEqualSign(char ch) {
        return ch == '=';
    }

    //判断是否是下划线
    private boolean isUnderline(char ch) {
        return ch == '_';
    }

    //判断是否是点
    private boolean isSpot(char ch) {
        return ch == '.';
    }

    //判断是否是E｜e
    private boolean isE_e(char ch) {
        return ch == 'E' || ch == 'e';
    }

    //判断是否是X｜x
    private boolean isX_x(char ch) {
        return ch == 'X' || ch == 'x';
    }

    //判断是否是+｜-
    private boolean isAdd_Sub(char ch) {
        return ch == '+' || ch == '-';
    }

    //判断是否是字母
    private boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    //判断指定范围的数字
    private boolean isDigit(char ch, char min, char max) {
        return ch >= min && ch <= max;
    }

    //判断是否是数字
    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    //判断是否是除号
    private boolean isDiv(char ch) {
        return ch == '/';
    }

    //判断是否是换行符
    private boolean isWrap(char ch) {
        return ch == '\n';
    }

    //判断是否是乘号
    private boolean isMul(char ch) {
        return ch == '*';
    }

    //判断是否是单引号
    private boolean isSingleQuote(char ch) {
        return ch == '\'';
    }

    //判断是否是双引号
    private boolean isDoubleQuote(char ch) {
        return ch == '"';
    }

    //判断是否是数字结尾（包括界符、运算符、空字符）
    private boolean isNumEnd(char ch) {
        return "()[]!*/%+-<>=&|. ^~\n;,{}".indexOf(ch) >= 0 || ch == (char) -1;
    }

    //判断是否是空字符
    private boolean isWhiteSpace(char ch) {
        if (ch == '\n') { //遇到换行符行数加1，列数置为1
            row += 1; col = 1;
            return true;
        } else return ch == '\t' || ch == '\f' || ch == '\0' || ch == ' ';
    }

    //判断是否是分界符
    private boolean isDelimiter(char ch) {
        for (String s : FinalAttribute.getDelimiter())
            if (s.charAt(0) == ch)
                return true;
        return false;
    }

    //判断是否是运算符
    private boolean isOperator(char ch) {
        for (String s : FinalAttribute.getOperator())
            if (s.charAt(0) == ch)
                return true;
        return false;
    }

    //获取下一个字符
    private char getNextChar() {
        col += 1; //当前列标加1
        return content[++index];//当前字符位置加1
    }

    //回退上一个字符
    private void backLastChar() {
        col -= 1;  //当前列标减1
        index -= 1;  //当前字符位置减1
        word.deleteCharAt(word.length() - 1); //删除word的最后一个字符
    }

    //判断是否是结束状态
    private boolean isEndState(int state) {
        switch (state) {
            case Constants.STATE_2: backLastChar();
                if (FinalAttribute.findToken(word.toString()) == Constants.IDENTIFIER_TOKEN) {
                    words.add(new Word(Constants.IDENTIFIER_TOKEN, word.toString(), Constants.IDENTIFIER, row, col));
                } else {
                    words.add(new Word(word.toString(), Constants.KEYWORD, row, col));
                }
                break;
            case Constants.STATE_5:
            case Constants.STATE_8:
            case Constants.STATE_14:
            case Constants.STATE_15: backLastChar();words.add(new Word(Constants.REALNUMBER_TOKEN, word.toString(), Constants.REALNUMBER, row, col)); break;
            case Constants.STATE_16: backLastChar();words.add(new Word(Constants.INTEGER_TOKEN, word.toString(), Constants.INTEGER, row, col));break;
            case Constants.STATE_17: backLastChar();errorMsgList.add(new Word(Constants.ERROR_TOKEN, word.toString(), "error:数值错误 ", row, col));break;
            case Constants.STATE_18:
            case Constants.STATE_26:
            case Constants.STATE_29:
            case Constants.STATE_37:
            case Constants.STATE_41:
            case Constants.STATE_42:
            case Constants.STATE_45:
            case Constants.STATE_46:
            case Constants.STATE_48:
            case Constants.STATE_50:
            case Constants.STATE_54:
            case Constants.STATE_56:
            case Constants.STATE_60:
            case Constants.STATE_63:
            case Constants.STATE_64:
            case Constants.STATE_67:
            case Constants.STATE_68:
            case Constants.STATE_71:
            case Constants.STATE_73:
            case Constants.STATE_74: words.add(new Word(word.toString(), Constants.OPERATOR, row, col));break;
            case Constants.STATE_19: words.add(new Word(word.toString(), Constants.OPERATOR, row, col));break;
            case Constants.STATE_20: words.add(new Word(word.toString(), Constants.OPERATOR, row, col));break;
            case Constants.STATE_21: words.add(new Word(word.toString(), Constants.OPERATOR, row, col));break;
            case Constants.STATE_23: words.add(new Word(word.toString(), Constants.OPERATOR, row, col));break;
            case Constants.STATE_24:
            case Constants.STATE_27:
            case Constants.STATE_35:
            case Constants.STATE_38:
            case Constants.STATE_40:
            case Constants.STATE_44:
            case Constants.STATE_51:
            case Constants.STATE_52:
            case Constants.STATE_57:
            case Constants.STATE_58:
            case Constants.STATE_61:
            case Constants.STATE_65:
            case Constants.STATE_69:
            case Constants.STATE_72: backLastChar();words.add(new Word(word.toString(), Constants.OPERATOR, row, col));break;
            case Constants.STATE_31:
            case Constants.STATE_34: break;
            case Constants.STATE_75:
            case Constants.STATE_76:
            case Constants.STATE_77:
            case Constants.STATE_78: words.add(new Word(word.toString(), Constants.DELIMITER, row, col));break;
            case Constants.STATE_80: words.add(new Word(Constants.STRING_TOKEN, word.toString(), Constants.STRING, row, col));break;
            case Constants.STATE_81: backLastChar();errorMsgList.add(new Word(Constants.ERROR_TOKEN, word.toString(), "error: 缺少双引号 ", row, col));break;
            case Constants.STATE_84: words.add(new Word(Constants.CHARACTER_TOKEN, word.toString(), Constants.CHARACTER, row, col));break;
            case Constants.STATE_85: backLastChar();errorMsgList.add(new Word(Constants.ERROR_TOKEN, word.toString(), "error: 缺少单引号 ", row, col));break;
            default:
                return false;
        }
        return true;
    }

    //识别关键字、标识符
    private void recognizeId(char ch) {
        char state = Constants.STATE_BEGIN; //初始状态
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isLetter(ch) || isUnderline(ch)) {
                        state = Constants.STATE_1;
                    }
                    break;
                case Constants.STATE_1:
                    ch = getNextChar();
                    if (isUnderline(ch) || isLetter(ch) || isDigit(ch)) {
                        state = Constants.STATE_1;
                    } else {
                        state = Constants.STATE_2;
                    }
                    break;
            }
            word.append(ch);
        }
    }

    //识别所有数值
    private void recognizeNum(char ch) {
        char state = Constants.STATE_BEGIN; //初始状态
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isDigit(ch, '1', '9')) {
                        state = Constants.STATE_3;
                    } else {
                        state = Constants.STATE_4;
                    }
                    break;
                case Constants.STATE_3:
                    ch = getNextChar();
                    if (isDigit(ch)) {
                        state = Constants.STATE_3;
                    } else if (isSpot(ch)) {
                        state = Constants.STATE_9;
                    } else if (isE_e(ch)) {
                        state = Constants.STATE_11;
                    } else if (isNumEnd(ch)) {
                        state = Constants.STATE_16;
                    } else {
                        state = Constants.STATE_17;
                    }
                    break;
                case Constants.STATE_4:
                    ch = getNextChar();
                    if (isDigit(ch, '0', '7')) {
                        state = Constants.STATE_4;
                    } else if (isSpot(ch)) {
                        state = Constants.STATE_9;
                    } else if (isX_x(ch)) {
                        state = Constants.STATE_6;
                    } else {
                        state = Constants.STATE_5;
                    }
                    break;
                case Constants.STATE_6:
                    ch = getNextChar();
                    if (isDigit(ch) || isLetter(ch)) {
                        state = Constants.STATE_7;
                    }
                    break;
                case Constants.STATE_7:
                    ch = getNextChar();
                    if (isDigit(ch) || isLetter(ch)) {
                        state = Constants.STATE_7;
                    } else {
                        state = Constants.STATE_8;
                    }
                    break;
                case Constants.STATE_9:
                    ch = getNextChar();
                    if (isDigit(ch)) {
                        state = Constants.STATE_10;
                    } else {
                        state = Constants.STATE_17;
                    }
                    break;
                case Constants.STATE_10:
                    ch = getNextChar();
                    if (isDigit(ch)) {
                        state = Constants.STATE_10;
                    } else if (isE_e(ch)) {
                        state = Constants.STATE_11;
                    } else if (isNumEnd(ch)) {
                        state = Constants.STATE_15;
                    } else {
                        state = Constants.STATE_17;
                    }
                    break;
                case Constants.STATE_11:
                    ch = getNextChar();
                    if (isAdd_Sub(ch)) {
                        state = Constants.STATE_12;
                    } else if (isDigit(ch)) {
                        state = Constants.STATE_13;
                    } else {
                        state = Constants.STATE_17;
                    }
                    break;
                case Constants.STATE_12:
                    ch = getNextChar();
                    if (isDigit(ch)) {
                        state = Constants.STATE_13;
                    } else {
                        state = Constants.STATE_17;
                    }
                    break;
                case Constants.STATE_13:
                    ch = getNextChar();
                    if (isDigit(ch)) {
                        state = Constants.STATE_13;
                    } else if (isNumEnd(ch)) {
                        state = Constants.STATE_14;
                    } else {
                        state = Constants.STATE_17;
                    }
                    break;
            }
            word.append(ch);
        }
    }

    //识别'/'、注释
    private void recognizeMulAndNotes(char ch) {
        char state = Constants.STATE_BEGIN; //初始状态
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isDiv(ch)) {
                        state = Constants.STATE_28;
                    }
                    break;
                case Constants.STATE_28:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_29;
                    } else if (isDiv(ch)) {
                        state = Constants.STATE_30;
                    } else if (isMul(ch)) {
                        state = Constants.STATE_32;
                    } else {
                        state = Constants.STATE_35;
                    }
                    break;
                case Constants.STATE_30:
                    ch = getNextChar();
                    if (isWrap(ch)) {
                        row += 1;
                        state = Constants.STATE_31;
                    } else if (index == content.length - 1) {
                        state = Constants.STATE_31;
                    } else {
                        state = Constants.STATE_30;
                    }
                    break;
                case Constants.STATE_32:
                    ch = getNextChar();
                    if (isWrap(ch)) {
                        row += 1;
                    } else if (isMul(ch)) {
                        state = Constants.STATE_33;
                    } else {
                        state = Constants.STATE_32;
                    }
                    break;
                case Constants.STATE_33:
                    ch = getNextChar();
                    if (isWrap(ch)) {
                        row += 1;
                    } else if (isDiv(ch)) {
                        state = Constants.STATE_34;
                    } else if (isMul(ch)) {
                        state = Constants.STATE_33;
                    } else {
                        state = Constants.STATE_32;
                    }
                    break;
            }
            word.append(ch);
        }
    }

    //识别字符
    private void recognizeChar(char ch) {
        char state = Constants.STATE_BEGIN; //初始状态
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isSingleQuote(ch)) {
                        state = Constants.STATE_82;
                    }
                    break;
                case Constants.STATE_82:
                    ch = getNextChar();
                    state = Constants.STATE_83;
                    break;
                case Constants.STATE_83:
                    ch = getNextChar();
                    if (isSingleQuote(ch)) {
                        state = Constants.STATE_84;
                    } else {
                        state = Constants.STATE_85;
                    }
                    break;
            }
            word.append(ch);
        }
    }

    //识别字符串
    public void recognizeString(char ch) {
        char state = Constants.STATE_BEGIN; //初始状态
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isDoubleQuote(ch)) {
                        state = Constants.STATE_79;
                    }
                    break;
                case Constants.STATE_79:
                    ch = getNextChar();
                    if (isDoubleQuote(ch)) {
                        state = Constants.STATE_80;
                    } else if (isWrap(ch)) {
                        state = Constants.STATE_81;
                    } else {
                        state = Constants.STATE_79;
                    }
                    break;
            }
            word.append(ch);
        }
    }

    //识别分界符
    private void recognizeDelimiter(char ch) {
        char state = Constants.STATE_BEGIN; //初始状态
        while (!isEndState(state)) {
            if (state == Constants.STATE_BEGIN) {
                switch (ch) {
                    case '{': state = Constants.STATE_75;break;
                    case '}': state = Constants.STATE_76;break;
                    case ';': state = Constants.STATE_77;break;
                    case ',': state = Constants.STATE_78;break;
                }
            }
            word.append(ch);
        }
    }

    //识别运算符
    private void recognizeOperator(char ch) {
        char state = Constants.STATE_BEGIN; //初始状态
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    switch (ch) {
                        case '(': state = Constants.STATE_18;break;
                        case ')': state = Constants.STATE_19;break;
                        case '[': state = Constants.STATE_20;break;
                        case ']': state = Constants.STATE_21;break;
                        case '!': state = Constants.STATE_22;break;
                        case '*': state = Constants.STATE_25;break;
                        case '%': state = Constants.STATE_36;break;
                        case '+': state = Constants.STATE_39;break;
                        case '-': state = Constants.STATE_43;break;
                        case '<': state = Constants.STATE_47;break;
                        case '>': state = Constants.STATE_53;break;
                        case '=': state = Constants.STATE_59;break;
                        case '&': state = Constants.STATE_62;break;
                        case '|': state = Constants.STATE_66;break;
                        case '^': state = Constants.STATE_70;break;
                        case '~': state = Constants.STATE_73;break;
                        case '.': state = Constants.STATE_74;break;
                    }
                    break;
                case Constants.STATE_22:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_23;
                    } else {
                        state = Constants.STATE_24;
                    }
                    break;
                case Constants.STATE_25:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_26;
                    } else {
                        state = Constants.STATE_27;
                    }
                    break;
                case Constants.STATE_36:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_37;
                    } else {
                        state = Constants.STATE_38;
                    }
                    break;
                case Constants.STATE_39:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_42;
                    } else if (ch == '+') {
                        state = Constants.STATE_41;
                    } else {
                        state = Constants.STATE_40;
                    }
                    break;
                case Constants.STATE_43:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_46;
                    } else if (ch == '-') {
                        state = Constants.STATE_45;
                    } else {
                        state = Constants.STATE_44;
                    }
                    break;
                case Constants.STATE_47:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_48;
                    } else if (ch == '<') {
                        state = Constants.STATE_49;
                    } else {
                        state = Constants.STATE_52;
                    }
                    break;
                case Constants.STATE_49:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_50;
                    } else {
                        state = Constants.STATE_51;
                    }
                    break;
                case Constants.STATE_53:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_54;
                    } else if (ch == '>') {
                        state = Constants.STATE_55;
                    } else {
                        state = Constants.STATE_58;
                    }
                    break;
                case Constants.STATE_55:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_56;
                    } else {
                        state = Constants.STATE_57;
                    }
                    break;
                case Constants.STATE_59:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_60;
                    } else {
                        state = Constants.STATE_61;
                    }
                    break;
                case Constants.STATE_62:
                    ch = getNextChar();
                    if (ch == '&') {
                        state = Constants.STATE_63;
                    } else if (isEqualSign(ch)) {
                        state = Constants.STATE_64;
                    } else {
                        state = Constants.STATE_65;
                    }
                    break;
                case Constants.STATE_66:
                    ch = getNextChar();
                    if (ch == '|') {
                        state = Constants.STATE_67;
                    } else if (isEqualSign(ch)) {
                        state = Constants.STATE_68;
                    } else {
                        state = Constants.STATE_69;
                    }
                    break;
                case Constants.STATE_70:
                    ch = getNextChar();
                    if (isEqualSign(ch)) {
                        state = Constants.STATE_71;
                    } else {
                        state = Constants.STATE_72;
                    }
                    break;
            }
            word.append(ch);
        }
    }

    //预处理
    private void pretreatment() {
        char ch;
        while (index < content.length - 1) {
            word = new StringBuilder();
            ch = getNextChar();
            if (isWhiteSpace(ch)) {
                continue;
            } else if (isUnderline(ch) || isLetter(ch)) {
                recognizeId(ch);
            } else if (isDigit(ch)) {
                recognizeNum(ch);
            } else if (isDiv(ch)) {
                recognizeMulAndNotes(ch);
            } else if (isSingleQuote(ch)) {
                recognizeChar(ch);
            } else if (isDoubleQuote(ch)) {
                recognizeString(ch);
            } else if (isDelimiter(ch)) {
                recognizeDelimiter(ch);
            } else if (isOperator(ch)) {
                recognizeOperator(ch);
            } else {
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, "" + ch, "error: 不能识别的字符 ", row, col));
            }
        }
    }

    //词法分析启动入口
    public void runAnalyzer() {
        this.pretreatment();
    }

    //返回词法分析后的列表
    public List<Word> getWords() {
        return words;
    }

    //返回词法分析后的错误信息
    public List<Word> getErrorMsgList() {
        return errorMsgList;
    }
}