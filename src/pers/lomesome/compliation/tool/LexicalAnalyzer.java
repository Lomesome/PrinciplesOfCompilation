package pers.lomesome.compliation.tool;

import pers.lomesome.compliation.model.FinalAttribute;
import pers.lomesome.compliation.model.Constants;
import pers.lomesome.compliation.model.Word;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    private PushbackReader pushbackReader = null;
    private final List<Word> words = new ArrayList<>();
    private final List<Word> errorMsgList = new ArrayList<>();
    private final static int BUFFERSIZE = 2;
    private final static char FILETAIL = (char) -1;
    private int row = 1;
    private int col = 1;

    //构造函数，输入是文件名
    public LexicalAnalyzer(String fileName) {
        File fp = new File(fileName);

        //文件阅读器
        try {
            FileReader fr = new FileReader(fp);
            pushbackReader = new PushbackReader(fr, BUFFERSIZE); //用可以回退的回退流封装
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

//    //词法分析
//    public void Analysis() {
//        char ch;
//        int state;
//        int row = 1;
//        while (true) {
//            boolean error = false;
//            ch = getNextChar();
//            if (ch == (char) FILETAIL) { //读取文件尾，说明文件分析完成，退出
//                break;
//            }
//
//            switch (ch) {
//                //空字符
//
//                case '\n':
//                    row += 1;
//                case ' ':
//                case '\t':
//                case '\r':
//                    break;
//
//                //单字符分界符
//                case ';':
//                case ',':
//                case '{':
//                case '}':
//                    words.add(new Word("" + ch, Constants.DELIMITER));
//                    break;
//
//                case '(':
//                case ')':
//                case '[':
//                case ']':
//                case '*':
//                case '%':
//                case '+':
//                case '-':
//                case '.':
//                    words.add(new Word("" + ch, Constants.OPERATOR));
//                    break;
//
//                case '/':
//                    ch = getNextChar();
//                    switch (ch) {
//                        case '*':
//                            while (true) {
//                                ch = getNextChar();
//                                if (ch == '\n') {
//                                    row += 1;
//                                } else if (ch == '*') {
//                                    ch = getNextChar();
//                                    if (ch == '\n') {
//                                        row += 1;
//                                    } else if (ch == '/') {
//                                        break;
//                                    }
//                                }
//                            }
//                            break;
//
//                        case '/':
//                            do {
//                                ch = getNextChar();
//                                if (ch == '\n') {
//                                    row += 1;
//                                }
//                            } while (ch != '\n');
//                            break;
//                        default:
//                            words.add(new Word("" + ch, Constants.OPERATOR));
//                            backLastChar(ch);
//                            break;
//                    }
//                    break;
//
//                case '&':
//                    ch = getNextChar();
//                    if (ch == '&') {
//                        words.add(new Word("&&", Constants.OPERATOR));
//                    } else {
//                        backLastChar(ch);
//                    }
//                    break;
//
//                case '|':
//                    ch = getNextChar();
//                    if (ch == '|') {
//                        words.add(new Word("||", Constants.OPERATOR));
//                    } else {
//                        backLastChar(ch);
//                    }
//                    break;
//
//                case '=':
//                    ch = getNextChar();
//                    if (ch == '=') {
//                        words.add(new Word("==", Constants.OPERATOR));
//                    } else {
//                        words.add(new Word("=", Constants.OPERATOR));
//                        backLastChar(ch);
//                    }
//                    break;
//
//                case '!':
//                    ch = getNextChar();
//                    if (ch == '=') {
//                        words.add(new Word("!=", Constants.OPERATOR));
//                    } else {
//                        words.add(new Word("!", Constants.OPERATOR));
//                        backLastChar(ch);
//                    }
//                    break;
//
//                case '<':
//                    ch = getNextChar();
//                    if (ch == '=') {
//                        words.add(new Word("<=", Constants.OPERATOR));
//                    } else {
//                        words.add(new Word("<", Constants.OPERATOR));
//                        backLastChar(ch);
//                    }
//                    break;
//
//                case '>':
//                    ch = getNextChar();
//                    if (ch == '=') {
//                        words.add(new Word(">=", Constants.OPERATOR));
//                    } else {
//                        words.add(new Word(">", Constants.OPERATOR));
//                        backLastChar(ch);
//                    }
//                    break;
//                //标识符和实数
//                default:
//                    String word = "";
//                    state = Constants.STATE_BEGIN;
//                    while (state != Constants.STATE_END) {
//                        switch (state) {
//                            case Constants.STATE_BEGIN:
//                                if (!isLetter(ch) || !isDigit(ch)) {
//                                    //出错
//                                    state = Constants.STATE_END;
//                                    break;
//                                } else if (isDigit(ch))
//                                    state = Constants.STATE_1;
//                                else
//                                    state = Constants.STATE_2;
//                                break;
//                            case 1:
//                                if (ch == '.') {
//                                    state = Constants.STATE_3;
//                                } else if (isDigit(ch))
//                                    state = Constants.STATE_1;
//                                else if (isLetter(ch)) {
//                                    state = Constants.STATE_2;
//                                    error = true;
//                                } else {
//                                    state = Constants.STATE_END;
//                                    backLastChar(ch);
//                                    words.add(new Word(Constants.INTEGER_TOKEN, word, Constants.INTEGER));
//                                    ch = ' ';
//                                }
//                                break;
//                            case Constants.STATE_2:
//                                if (isLetter(ch) || isDigit(ch))
//                                    state = Constants.STATE_2;
//                                else {
//                                    state = Constants.STATE_END;
//                                    backLastChar(ch);
//                                    if (error) {
//                                        errorMsgList.add(new Word(Constants.ERROR_TOKEN, word, Constants.ERROR + ":第" + row + "行"));
//                                    } else if (FinalAttribute.findToken(word) == Constants.IDENTIFIER_TOKEN) {
//                                        words.add(new Word(Constants.IDENTIFIER_TOKEN, word, Constants.IDENTIFIER));
//                                    } else {
//                                        words.add(new Word(word, Constants.KEYWORD));
//                                    }
//                                    ch = ' ';
//                                }
//                                break;
//                            case Constants.STATE_3:
//                                if (isDigit(ch))
//                                    state = Constants.STATE_3;
//                                else if (isLetter(ch)) {
//                                    state = Constants.STATE_2;
//                                    error = true;
//                                } else {
//                                    state = Constants.STATE_END;
//                                    backLastChar(ch);
//                                    words.add(new Word(Constants.REALNUMBER_TOKEN, word, Constants.REALNUMBER));
//                                    ch = ' ';
//                                }
//                                break;
//                        }
//                        if (ch != ' ')
//                            word += ch;
//                        if (state != Constants.STATE_END)
//                            ch = getNextChar(); //读取下一个字符
//                    }
//                    break;
//            }
//        }
//    }

    public boolean isUnderline(char ch) {
        if (ch == '_') {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLetter(char ch) {
        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDigit(char ch, char min, char max) {
        if (ch >= min && ch <= max) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDigit(char ch) {
        if (ch >= '0' && ch <= '9') {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNumEnd(char ch) {
        String end = "()[]!*/%+-<>=&|. \n;,{}";
        if (end.indexOf(ch) >= 0 || ch == (char) -1) {
            return true;
        } else {
            return false;
        }
    }

    public char getNextChar(){
        try {
            col += 1;
            return (char)pushbackReader.read();
        } catch (IOException e) {
            return FILETAIL;
        }
    }

    public void backLastChar(char ch){
        try {
            pushbackReader.unread(ch);
            col -= 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEndState(int state) {
        switch (state) {
            case Constants.STATE_2:
            case Constants.STATE_6:
            case Constants.STATE_9:
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

    public void recognizeId(char ch){
        StringBuilder word = new StringBuilder();
        char state = Constants.STATE_BEGIN;
        while (state != Constants.STATE_2){
            switch (state) {
                case Constants.STATE_BEGIN:
                    if (isLetter(ch) || isUnderline(ch)){
                        word.append(ch);
                        state = Constants.STATE_1;
                    }
                    break;
                case Constants.STATE_1:
                    ch = getNextChar();
                    if (isUnderline(ch) || isLetter(ch) || isDigit(ch)){
                        word.append(ch);
                        state = Constants.STATE_1;
                    }else {
                        backLastChar(ch);
                        state = Constants.STATE_2;
                    }
                    break;
            }
        }
        col -= 1;
        if (FinalAttribute.findToken(word.toString()) == Constants.IDENTIFIER_TOKEN) {
            words.add(new Word(Constants.IDENTIFIER_TOKEN, word.toString(), Constants.IDENTIFIER, row, col));
        } else {
            words.add(new Word(word.toString(), Constants.KEYWORD, row, col));
        }
    }

    public void pretreatment(){
        char ch = getNextChar();
        while (true){
            if (ch == FILETAIL){
                break;
            }
            if (isUnderline(ch) || isLetter(ch)){
                recognizeId(ch);
            }
        }
    }

    //词法分析
    public void Analysis2() {

        while (true) {
            char ch = getNextChar();
            int state = Constants.STATE_BEGIN;
            if (ch == FILETAIL) {
                break;
            } else if (ch == ' ') {
                continue;
            } else if (ch == '\n') {
                row += 1;
                col = 1;
            }

            StringBuilder word = new StringBuilder();
            while (true) {

                switch (state) {
                    case Constants.STATE_BEGIN: {
                        if (isLetter(ch) || ch == '_') {
                            word.append(ch);
                            state = Constants.STATE_1;
                        } else if (isDigit(ch, '1', '9')) {
                            word.append(ch);
                            state = Constants.STATE_3;
                        } else if (ch == '0') {
                            word.append(ch);
                            state = Constants.STATE_5;
                        } else if (ch == '(') {
                            word.append(ch);
                            state = Constants.STATE_19;
                        } else if (ch == ')') {
                            word.append(ch);
                            state = Constants.STATE_20;
                        } else if (ch == '[') {
                            word.append(ch);
                            state = Constants.STATE_21;
                        } else if (ch == ']') {
                            word.append(ch);
                            state = Constants.STATE_22;
                        } else if (ch == '!') {
                            word.append(ch);
                            state = Constants.STATE_23;
                        } else if (ch == '*') {
                            word.append(ch);
                            state = Constants.STATE_26;
                        } else if (ch == '/') {
                            state = Constants.STATE_28;
                        } else if (ch == '%') {
                            word.append(ch);
                            state = Constants.STATE_35;
                        } else if (ch == '+') {
                            word.append(ch);
                            state = Constants.STATE_37;
                        } else if (ch == '-') {
                            word.append(ch);
                            state = Constants.STATE_39;
                        } else if (ch == '<') {
                            word.append(ch);
                            state = Constants.STATE_41;
                        } else if (ch == '>') {
                            word.append(ch);
                            state = Constants.STATE_44;
                        } else if (ch == '=') {
                            word.append(ch);
                            state = Constants.STATE_47;
                        } else if (ch == '&') {
                            word.append(ch);
                            state = Constants.STATE_50;
                        } else if (ch == '|') {
                            word.append(ch);
                            state = Constants.STATE_53;
                        } else if (ch == '.') {
                            word.append(ch);
                            state = Constants.STATE_56;
                        } else if (ch == '{') {
                            word.append(ch);
                            state = Constants.STATE_58;
                        } else if (ch == '}') {
                            word.append(ch);
                            state = Constants.STATE_59;
                        } else if (ch == ';') {
                            word.append(ch);
                            state = Constants.STATE_60;
                        } else if (ch == ',') {
                            word.append(ch);
                            state = Constants.STATE_61;
                        } else if (ch == '"'){
                            word.append(ch);
                            state = Constants.STATE_62;
                        } else if (ch == '\''){
                            word.append(ch);
                            state = Constants.STATE_65;
                        } else {
                            state = Constants.STATE_18;
                        }
                    }
                    break;
                    case Constants.STATE_1: {
                        ch = getNextChar();
                        if (isLetter(ch) || isDigit(ch) || ch == '_') {
                            word.append(ch);
                            state = Constants.STATE_1;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_2;
                        }
                    }
                    break;
                    case Constants.STATE_3: {
                        ch = getNextChar();
                        if (isDigit(ch)) {
                            word.append(ch);
                            state = Constants.STATE_3;
                        } else if ('.' == ch) {
                            word.append(ch);
                            state = Constants.STATE_10;
                        } else if ('E' == ch || 'e' == ch) {
                            word.append(ch);
                            state = Constants.STATE_12;
                        } else if (isNumEnd(ch)) {
                            backLastChar(ch);
                            state = Constants.STATE_17;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_18;
                        }
                    }
                    break;

                    case Constants.STATE_5: {
                        ch = getNextChar();
                        if (isDigit(ch, '0', '7')) {
                            word.append(ch);
                            state = Constants.STATE_5;
                        } else if (ch == '.') {
                            word.append(ch);
                            state = Constants.STATE_10;
                        } else if (ch == 'x' || ch == 'X') {
                            word.append(ch);
                            state = Constants.STATE_7;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_6;
                        }
                    }
                    break;

                    case Constants.STATE_7: {
                        ch = getNextChar();
                        if (isDigit(ch) || isLetter(ch)) {
                            word.append(ch);
                            state = Constants.STATE_8;
                        } else {
                            state = Constants.STATE_END;
                        }
                    }
                    break;

                    case Constants.STATE_8: {
                        ch = getNextChar();
                        if (isDigit(ch) || isLetter(ch)) {
                            word.append(ch);
                            state = Constants.STATE_8;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_9;
                        }
                    }
                    break;

                    case Constants.STATE_10: {
                        ch = getNextChar();
                        if (isDigit(ch)) {
                            word.append(ch);
                            state = Constants.STATE_11;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_18;
                        }
                    }
                    break;
                    case Constants.STATE_11: {
                        ch = getNextChar();
                        if (isDigit(ch)) {
                            word.append(ch);
                            state = Constants.STATE_11;
                        } else if ('E' == ch || 'e' == ch) {
                            word.append(ch);
                            state = Constants.STATE_12;
                        } else if (isNumEnd(ch)) {
                            backLastChar(ch);
                            state = Constants.STATE_16;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_18;
                        }
                    }
                    break;
                    case Constants.STATE_12: {
                        ch = getNextChar();
                        if ('+' == ch || '-' == ch) {
                            word.append(ch);
                            state = Constants.STATE_13;
                        } else if (isDigit(ch)) {
                            word.append(ch);
                            state = Constants.STATE_14;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_18;
                        }
                    }
                    break;
                    case Constants.STATE_13: {
                        ch = getNextChar();
                        if (isDigit(ch)) {
                            word.append(ch);
                            state = Constants.STATE_14;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_18;
                        }
                    }
                    break;
                    case Constants.STATE_14: {
                        ch = getNextChar();
                        if (isDigit(ch)) {
                            word.append(ch);
                            state = Constants.STATE_14;
                        } else if (isNumEnd(ch)) {
                            backLastChar(ch);
                            state = Constants.STATE_15;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_18;
                        }
                    }
                    break;
                    case Constants.STATE_23: {
                        ch = getNextChar();
                        if (ch == '=') {
                            word.append(ch);
                            state = Constants.STATE_24;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_25;
                        }
                    }
                    break;
                    case Constants.STATE_26: {
                        ch = getNextChar();
                        backLastChar(ch);
                        state = Constants.STATE_27;
                    }
                    break;
                    case Constants.STATE_28: {
                        ch = getNextChar();
                        if (ch == '/') {
                            state = Constants.STATE_30;
                        } else if (ch == '*') {
                            state = Constants.STATE_32;
                        } else {
                            word.append('/');
                            state = Constants.STATE_29;
                        }
                    }
                    break;
                    case Constants.STATE_30: {
                        ch = getNextChar();
                        if (ch == '\n') {
                            row += 1;
                            state = Constants.STATE_31;
                        } else if (ch == FILETAIL) {
                            state = Constants.STATE_31;
                        } else {
                            state = Constants.STATE_30;
                        }
                    }
                    break;
                    case Constants.STATE_32: {
                        ch = getNextChar();
                        if (ch == '\n') {
                            row += 1;
                        }
                        if (ch == '*') {
                            state = Constants.STATE_33;
                        } else {
                            state = Constants.STATE_32;
                        }
                    }
                    break;
                    case Constants.STATE_33: {
                        ch = getNextChar();
                        if (ch == '\n') {
                            row += 1;
                        }
                        if (ch == '/') {
                            state = Constants.STATE_34;
                        } else {
                            state = Constants.STATE_32;
                        }
                    }
                    break;
                    case Constants.STATE_35: {
                        ch = getNextChar();
                        backLastChar(ch);
                        state = Constants.STATE_36;
                    }
                    break;

                    case Constants.STATE_37: {
                        ch = getNextChar();
                        backLastChar(ch);
                        state = Constants.STATE_38;
                    }
                    break;
                    case Constants.STATE_39: {
                        ch = getNextChar();
                        backLastChar(ch);
                        state = Constants.STATE_40;
                    }
                    break;
                    case Constants.STATE_41: {
                        ch = getNextChar();
                        if (ch == '=') {
                            word.append(ch);
                            state = Constants.STATE_42;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_43;
                        }
                    }
                    break;
                    case Constants.STATE_44: {
                        ch = getNextChar();
                        if (ch == '=') {
                            word.append(ch);
                            state = Constants.STATE_45;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_46;
                        }
                    }
                    break;
                    case Constants.STATE_47: {
                        ch = getNextChar();
                        if (ch == '=') {
                            word.append(ch);
                            state = Constants.STATE_48;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_49;
                        }
                    }
                    break;
                    case Constants.STATE_50: {
                        ch = getNextChar();
                        if (ch == '&') {
                            word.append(ch);
                            state = Constants.STATE_51;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_52;
                        }
                    }
                    break;
                    case Constants.STATE_53: {
                        ch = getNextChar();
                        if (ch == '|') {
                            word.append(ch);
                            state = Constants.STATE_54;
                        } else {
                            backLastChar(ch);
                            state = Constants.STATE_55;
                        }
                    }
                    break;
                    case Constants.STATE_56: {
                        ch = getNextChar();
                        backLastChar(ch);
                        state = Constants.STATE_57;
                    }
                    break;

                    case Constants.STATE_62: {
                        ch = getNextChar();
                        if(ch == '"'){
                            word.append(ch);
                            state = Constants.STATE_63;
                        }else if (ch == '\n'){
                            backLastChar(ch);
                            state = Constants.STATE_64;
                        }else {
                            word.append(ch);
                            state = Constants.STATE_62;
                        }
                    }
                    break;

                    case Constants.STATE_65: {
                        ch = getNextChar();
                        word.append(ch);
                        state = Constants.STATE_66;
                    }
                    break;

                    case Constants.STATE_66: {
                        ch = getNextChar();
                        if(ch == '\''){
                            word.append(ch);
                            state = Constants.STATE_67;
                        }else {
                            backLastChar(ch);
                            state = Constants.STATE_68;
                        }
                    }
                    break;
                }
                if (isEndState(state)) {
                    break;
                }
            }
            if (word.length() > 0) {
                setToken(state, word.toString());

            }
            System.out.println(col);
        }
    }

    public void setToken(int state, String word) {
        switch (state) {
            case Constants.STATE_2: {
                if (FinalAttribute.findToken(word) == Constants.IDENTIFIER_TOKEN) {
                    words.add(new Word(Constants.IDENTIFIER_TOKEN, word, Constants.IDENTIFIER, row, col));
                } else {
                    words.add(new Word(word, Constants.KEYWORD, row, col));
                }
            }
            break;

            case Constants.STATE_6:
            case Constants.STATE_9:
            case Constants.STATE_15:
            case Constants.STATE_16:
                words.add(new Word(Constants.REALNUMBER_TOKEN, word, Constants.REALNUMBER, row, col));
                break;

            case Constants.STATE_17:
                words.add(new Word(Constants.INTEGER_TOKEN, word, Constants.INTEGER, row, col));
                break;

            case Constants.STATE_18:
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, word, "error: 数值类型出错 ", row, col));
                break;

            case Constants.STATE_57:
            case Constants.STATE_58:
            case Constants.STATE_59:
            case Constants.STATE_60:
                words.add(new Word(word, Constants.DELIMITER, row, col));
                break;

            case Constants.STATE_63:
                words.add(new Word(600, word, Constants.STRING, row, col));
                break;

            case Constants.STATE_64:
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, word, "error: 缺少双引号 ", row, col));
                break;

            case Constants.STATE_67:
                words.add(new Word(500, word, Constants.CHARACTER, row, col));
                break;

            case Constants.STATE_68:
                errorMsgList.add(new Word(Constants.ERROR_TOKEN, word, "error: 缺少单引号 ", row, col));
                break;

            default:
                words.add(new Word(word, Constants.OPERATOR, row, col));
        }
    }

    public List<Word> getWords() {
        return words;
    }

    public List<Word> getErrorMsgList() {
        return errorMsgList;
    }


    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("/Users/leiyunhong/Desktop/test.txt");

        lexicalAnalyzer.pretreatment();
        for (Word word : lexicalAnalyzer.getWords()) {
            System.out.println(word);
        }
    }
}