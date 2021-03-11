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

    private boolean isUnderline(char ch) {
        return ch == '_';
    }

    private boolean isSpot(char ch) {
        return ch == '.';
    }

    private boolean isE_e(char ch) {
        return ch == 'E' || ch == 'e';
    }

    private boolean isX_x(char ch) {
        return ch == 'X' || ch == 'x';
    }

    private boolean isAdd_Sub(char ch) {
        return ch == '+' || ch == '-';
    }

    private boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    private boolean isDigit(char ch, char min, char max) {
        return ch >= min && ch <= max;
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isDiv(char ch) {
        return ch == '/';
    }

    private boolean isWrap(char ch) {
        return ch == '\n';
    }

    private boolean isMul(char ch) {
        return ch == '*';
    }

    private boolean isSingleQuote(char ch) {
        return ch == '\'';
    }

    private boolean isDoubleQuote(char ch) {
        return ch == '"';
    }

    private boolean isNumEnd(char ch) {
        String end = "()[]!*/%+-<>=&|. \n;,{}";
        return end.indexOf(ch) >= 0 || ch == (char) -1;
    }

    private boolean isWhiteSpace(char ch) {
        if (ch == '\n') {
            row += 1;
            col = 1;
            return true;
        } else return ch == '\t' || ch == '\f' || ch == '\0' || ch == ' ';
    }

    private boolean isDelimiter(char ch) {
        for(String s: FinalAttribute.getDelimiter()){
            if(s.charAt(0) == ch){
                return true;
            }
        }
        return false;
    }

    private boolean isOperator(char ch) {
        for(String s: FinalAttribute.getOperator()){
            if(s.charAt(0) == ch){
                return true;
            }
        }
        return false;
    }

    private boolean isEndState(int state) {
        switch (state) {
            case Constants.STATE_2:
            case Constants.STATE_5:
            case Constants.STATE_8:
            case Constants.STATE_14:
            case Constants.STATE_15:
            case Constants.STATE_16:
            case Constants.STATE_17:
            case Constants.STATE_18:
            case Constants.STATE_19:
            case Constants.STATE_20:
            case Constants.STATE_21:
            case Constants.STATE_22:
            case Constants.STATE_24:
            case Constants.STATE_25:
            case Constants.STATE_27:
            case Constants.STATE_29:
            case Constants.STATE_31:
            case Constants.STATE_34:
            case Constants.STATE_36:
            case Constants.STATE_38:
            case Constants.STATE_40:
            case Constants.STATE_42:
            case Constants.STATE_43:
            case Constants.STATE_45:
            case Constants.STATE_46:
            case Constants.STATE_48:
            case Constants.STATE_49:
            case Constants.STATE_51:
            case Constants.STATE_52:
            case Constants.STATE_54:
            case Constants.STATE_55:
            case Constants.STATE_57:
            case Constants.STATE_58:
            case Constants.STATE_59:
            case Constants.STATE_60:
            case Constants.STATE_61:
            case Constants.STATE_63:
            case Constants.STATE_64:
            case Constants.STATE_67:
            case Constants.STATE_68:
                return true;
            default:
                return false;
        }
    }

    private char getNextChar() {
        col += 1;
        index += 1;
        return content[index];
    }

    private void backLastChar() {
        col -= 1;
        index -= 1;
        word.deleteCharAt(word.length() - 1);
    }

    private void recognizeId(char ch) {
        word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
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
        backLastChar();
        if (FinalAttribute.findToken(word.toString()) == Constants.IDENTIFIER_TOKEN) {
            words.add(new Word(Constants.IDENTIFIER_TOKEN, word.toString(), Constants.IDENTIFIER, row, col));
        } else {
            words.add(new Word(word.toString(), Constants.KEYWORD, row, col));
        }
    }

    private void recognizeNum(char ch) {
        word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
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
        backLastChar();
        switch (state) {
            case Constants.STATE_5:
            case Constants.STATE_8:
            case Constants.STATE_14:
            case Constants.STATE_15:
                words.add(new Word(Constants.REALNUMBER_TOKEN, word.toString(), Constants.REALNUMBER, row, col));
                break;
            case Constants.STATE_16:
                words.add(new Word(Constants.INTEGER_TOKEN, word.toString(), Constants.INTEGER, row, col));
                break;
            case Constants.STATE_17:
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, word.toString(), "error:数值错误 ", row, col));
                break;
        }
    }

    private void recognizeMulAndNotes(char ch) {
        word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isDiv(ch)) {
                        state = Constants.STATE_28;
                    }
                    break;
                case Constants.STATE_28:
                    ch = getNextChar();
                    if (isDiv(ch)) {
                        state = Constants.STATE_30;
                    } else if (isMul(ch)) {
                        state = Constants.STATE_32;
                    } else {
                        state = Constants.STATE_29;
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
        if (state == Constants.STATE_29) {
            backLastChar();
            words.add(new Word(word.toString(), Constants.OPERATOR, row, col));
        }
    }

    private void recognizeChar(char ch) {
        word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isSingleQuote(ch)) {
                        state = Constants.STATE_65;
                    }
                    break;
                case Constants.STATE_65:
                    ch = getNextChar();
                    state = Constants.STATE_66;
                    break;
                case Constants.STATE_66:
                    ch = getNextChar();
                    if (isSingleQuote(ch)) {
                        state = Constants.STATE_67;
                    } else {
                        state = Constants.STATE_68;
                    }
                    break;
            }
            word.append(ch);
        }
        switch (state) {
            case Constants.STATE_67:
                words.add(new Word(Constants.CHARACTER_TOKEN, word.toString(), Constants.CHARACTER, row, col));
                break;
            case Constants.STATE_68:
                backLastChar();
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, word.toString(), "error: 缺少单引号 ", row, col));
                break;
        }
    }

    public void recognizeString(char ch) {
        word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isDoubleQuote(ch)) {
                        state = Constants.STATE_62;
                    }
                    break;
                case Constants.STATE_62:
                    ch = getNextChar();
                    if (isDoubleQuote(ch)) {
                        state = Constants.STATE_63;
                    } else if (isWrap(ch)) {
                        state = Constants.STATE_64;
                    } else {
                        state = Constants.STATE_62;
                    }
                    break;
            }
            word.append(ch);
        }
        switch (state) {
            case Constants.STATE_63:
                words.add(new Word(Constants.STRING_TOKEN, word.toString(), Constants.STRING, row, col));
                break;
            case Constants.STATE_64:
                backLastChar();
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, word.toString(), "error: 缺少双引号 ", row, col));
                break;
        }
    }

    private void recognizeDelimiter(char ch) {
        word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
        while (!isEndState(state)) {
            if (state == Constants.STATE_BEGIN) {
                switch (ch) {
                    case '{':
                        state = Constants.STATE_58;
                        break;
                    case '}':
                        state = Constants.STATE_59;
                        break;
                    case ';':
                        state = Constants.STATE_60;
                        break;
                    case ',':
                        state = Constants.STATE_61;
                        break;
                }
            }
            word.append(ch);
        }
        words.add(new Word(word.toString(), Constants.DELIMITER, row, col));
    }

    private void recognizeOperator(char ch) {
        word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
        while (!isEndState(state)) {
            switch (state) {
                case Constants.STATE_BEGIN:
                    switch (ch){
                        case '(' : state = Constants.STATE_18;break;
                        case ')' : state = Constants.STATE_19;break;
                        case '[' : state = Constants.STATE_20;break;
                        case ']' : state = Constants.STATE_21;break;
                        case '!' : state = Constants.STATE_23;break;
                        case '*' : state = Constants.STATE_26;break;
                        case '%' : state = Constants.STATE_35;break;
                        case '+' : state = Constants.STATE_37;break;
                        case '-' : state = Constants.STATE_39;break;
                        case '<' : state = Constants.STATE_41;break;
                        case '>' : state = Constants.STATE_44;break;
                        case '=' : state = Constants.STATE_47;break;
                        case '&' : state = Constants.STATE_50;break;
                        case '|' : state = Constants.STATE_53;break;
                        case '.' : state = Constants.STATE_56;break;
                    }
                    break;
                case Constants.STATE_23:
                    ch = getNextChar();
                    if (ch == '=') {
                        state = Constants.STATE_24;
                    } else {
                        state = Constants.STATE_25;
                    }
                    break;
                case Constants.STATE_26:
                    ch = getNextChar();
                    state = Constants.STATE_27;
                    break;
                case Constants.STATE_35:
                    ch = getNextChar();
                    state = Constants.STATE_36;
                    break;
                case Constants.STATE_37:
                    ch = getNextChar();
                    state = Constants.STATE_38;
                    break;
                case Constants.STATE_39:
                    ch = getNextChar();
                    state = Constants.STATE_40;
                    break;
                case Constants.STATE_41:
                    ch = getNextChar();
                    if (ch == '=') {
                        state = Constants.STATE_42;
                    } else {
                        state = Constants.STATE_43;
                    }
                    break;
                case Constants.STATE_44:
                    ch = getNextChar();
                    if (ch == '=') {
                        state = Constants.STATE_45;
                    } else {
                        state = Constants.STATE_46;
                    }
                    break;
                case Constants.STATE_47:
                    ch = getNextChar();
                    if (ch == '=') {
                        state = Constants.STATE_48;
                    } else {
                        state = Constants.STATE_49;
                    }
                    break;
                case Constants.STATE_50:
                    ch = getNextChar();
                    if (ch == '&') {
                        state = Constants.STATE_51;
                    } else {
                        state = Constants.STATE_52;
                    }
                    break;
                case Constants.STATE_53:
                    ch = getNextChar();
                    if (ch == '|') {
                        state = Constants.STATE_54;
                    } else {
                        state = Constants.STATE_55;
                    }
                    break;
                case Constants.STATE_56:
                    ch = getNextChar();
                    state = Constants.STATE_57;
                    break;
            }
            word.append(ch);
        }
        switch (state){
            case Constants.STATE_25:
            case Constants.STATE_27:
            case Constants.STATE_36:
            case Constants.STATE_38:
            case Constants.STATE_40:
            case Constants.STATE_43:
            case Constants.STATE_46:
            case Constants.STATE_49:
            case Constants.STATE_52:
            case Constants.STATE_55:
            case Constants.STATE_57:
                backLastChar();
                break;
        }
        words.add(new Word(word.toString(), Constants.OPERATOR, row, col));
    }

    private void pretreatment() {
        char ch;
        while (index < content.length - 1) {
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
            } else if (isOperator(ch)){
                recognizeOperator(ch);
            }else {
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, "" + ch, "error: 不能识别的字符 ", row, col));
            }
        }
    }

    public void runAnalyzer(){
        this.pretreatment();
    }
    public List<Word> getWords() {
        return words;
    }

    public List<Word> getErrorMsgList() {
        return errorMsgList;
    }

    public static void main(String[] args) {
        FinalAttribute.initToken();

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(ReadAndWriteFile.readFileContent("/Users/leiyunhong/Desktop/test.txt"));

        lexicalAnalyzer.pretreatment();
        for (Word word : lexicalAnalyzer.getWords()) {
            System.out.println(word);
        }
    }
}