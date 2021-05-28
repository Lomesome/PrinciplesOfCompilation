package pers.lomesome.compliation.utils.reg2nfa;

import java.util.*;

public class Rpn {

    public Rpn() {
    }

    /**
     * 将中缀表达式转化为后缀表达式
     *
     * @param expression 中缀表达式
     * @return 转化得到的后缀表达式
     */
    public String reverse2Rpn(String expression) {
        expression = prepareString(expression);
        StringBuilder postfix = new StringBuilder();
        //存储操作符的栈
        Stack<String> operatorStack = new Stack<>();
        // 将操作符与操作数分开
        StringTokenizer tokens = new StringTokenizer(expression, "()*|.", true);
        // 阶段1: 扫描符号串
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.length() == 0) {  //空格
                continue;
            }
            switch (token) {
                case "|":
                    // Process all * , . in the top of the operator stack
                    while (!operatorStack.isEmpty() && (operatorStack.peek().equals("*") || operatorStack.peek().equals("."))) {
                        postfix.append(operatorStack.pop());
                    }
                    // Push the | operator into the operator stack
                    operatorStack.push(token);

                    break;
                case ".":
                    // Process all . in the top of the operator stack
                    while (!operatorStack.isEmpty() && operatorStack.peek().equals(".")) {
                        postfix.append(operatorStack.pop());
                    }
                    // Push the . operator into the operator stack
                    operatorStack.push(token);

                    break;
                case "*":
                    postfix.append(token);
                    break;
                case "(":
                    operatorStack.push("("); // Push '(' to stack

                    break;
                case ")":
                    // Process all the operators in the stack until seeing '('
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
        // 阶段 2: process all the remaining operators in the stack
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }

    public String[] getSymbol(String regex) {
        regex = replaceAll(regex, new String[]{"(", ")", "|", ".", "*"});
        char[] chars = regex.toCharArray();
        Set<String> characterSet = new LinkedHashSet<>();
        for (char c : chars) {
            characterSet.add(String.valueOf(c));
        }
        String[] strings = new String[characterSet.size()];
        for (int i = 0; i < characterSet.size(); i++) {
            strings[i] = (String) characterSet.toArray()[i];
        }
        return strings;
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
