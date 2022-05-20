package pers.lomesome.compliation.utils.nfadfamfa;

import java.util.*;

public class Rpn {

    ArrayList<Character> words = new ArrayList<>();//用于保存正则式中出现的所有字符
    public Rpn() { }

    /**
     * 将中缀表达式转化为后缀表达式
     *
     * @param expression 中缀表达式
     * @return String 转化得到的后缀表达式
     */
    public String reverse2Rpn(String expression) {
        expression = prepareString(expression);
        StringBuilder postfix = new StringBuilder();
        Stack<String> operatorStack = new Stack<>(); //存储操作符的栈
        StringTokenizer tokens = new StringTokenizer(expression, "()*|.", true); // 将操作符与操作数分开
        // 扫描符号串
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.length() == 0) {  //空格
                continue;
            }
            switch (token) {
                case "|":
                    // 处理符号栈顶部的所有 '*' 和 '.'
                    while (!operatorStack.isEmpty() && (operatorStack.peek().equals("*") || operatorStack.peek().equals("."))) {
                        postfix.append(operatorStack.pop());
                    }
                    // 把 | 放入符号栈
                    operatorStack.push(token);

                    break;
                case ".":
                    // 处理符号栈顶部的所有 '.'
                    while (!operatorStack.isEmpty() && operatorStack.peek().equals(".")) {
                        postfix.append(operatorStack.pop());
                    }
                    // 把 '.' 放入符号栈
                    operatorStack.push(token);

                    break;
                case "*":
                    postfix.append(token);
                    break;
                case "(":
                    // 把 '(' 放入符号栈
                    operatorStack.push("(");

                    break;
                case ")":
                    // 处理符号栈顶部的所有符号直到遇到 '('
                    while (!operatorStack.peek().equals("(")) {
                        postfix.append(operatorStack.pop());
                    }
                    operatorStack.pop();

                    break;
                default:
                    postfix.append(token);
                    break;
            }
        }
        // 处理符号栈中所有剩余的运算符
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }

    public ArrayList<Character> getWords() {
        return words;
    }

    public List<Character> getSymbol(String regex) {
        regex = replaceAll(regex, new String[]{"(", ")", "|", ".", "*"});
        char[] chars = regex.toCharArray();
        Set<Character> characterSet = new LinkedHashSet<>();
        for (char c : chars) {
            characterSet.add(c);
        }
        List<Character> characters = new ArrayList<>();
        for (int i = 0; i < characterSet.size(); i++) {
            characters.add((Character) characterSet.toArray()[i]);
        }
        return characters;
    }

    private String replaceAll(String string, String[] delete) {
        for (String s : delete) {
            string = string.replace(s, "");
        }
        return string;
    }

    private String prepareString(String _regex) {
        StringBuilder regex = new StringBuilder();
        char[] regexs = _regex.replaceAll(" ", "").toCharArray();
        for (int i = 0; i < regexs.length; i++) {
            if (i == 0)
                regex.append(regexs[i]);
            else {
                if (regexs[i] == '|' || regexs[i] == '*' || regexs[i] == ')') {
                    regex.append(regexs[i]);
                } else {
                    if (regexs[i - 1] == '(' || regexs[i - 1] == '|')
                        regex.append(regexs[i]);
                    else
                        regex.append(".").append(regexs[i]);
                }
            }
        }
        return regex.toString();
    }
}
