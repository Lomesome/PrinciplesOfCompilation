package pers.lomesome.compliation.utils.dag;

import pers.lomesome.compliation.model.Node;
import pers.lomesome.compliation.model.Quaternary;
import pers.lomesome.compliation.model.Word;
import java.util.ArrayList;
import java.util.List;

/**
 * DAG优化算法类，只在package内可用
 */
class DAG {

    private List<Quaternary> qts;

    private List<Node> nodes;

    DAG(List<Quaternary> qts) {
        nodes = new ArrayList<>();
        this.qts = qts;
    }

    public List<Quaternary> optimite() {
        generateDAG();
        return generateQts();
    }

    /**
     * 根据优化了的DAG图重组四元式
     */
    private List<Quaternary> generateQts() {
        List<Quaternary> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.getOperator() == null) {
                //如果没有操作符，就说明这是叶子节点
                for (Word label : node.getExtra_labels()) {
                    if (!label.getName().equals("temp")) {
                        result.add(new Quaternary(new Word("="), node.getMain_label(), new Word("_"), label));
                    }
                }
            } else {
                //非叶节点
                Node left_child = node.getLeft_child();
                Node right_child = node.getRight_child();
                if (right_child == null) {
                    result.add(new Quaternary(node.getOperator(), left_child.getMain_label(), new Word("_"), node.getMain_label()));
                } else {
                    result.add(new Quaternary(node.getOperator(), left_child.getMain_label(), right_child.getMain_label(), node.getMain_label()));
                }
                //把node中的附加标记处理
                for (Word label : node.getExtra_labels()) {
                    if (!label.getName().equals("temp")) {
                        result.add(new Quaternary(new Word("="), node.getMain_label(), new Word("_"), label));
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据qts中的四元式序列生成DAG图
     * 存储在nodes中
     */
    private void generateDAG() {
        for (Quaternary qt : qts) {
            if (qt.getFirst().getWord().equals("=")) {
                //1. 若是赋值四元式: A=B  (=, B, _, A)
                // 查找之前定义的A（作为附加标号的）并删除， 如果是主标记，则不删除
                // 算法改进：赋值不能超越当前最新定义节点，如：a = 1；查找1的节点的时候如果碰到a的定义就不再继续查找
                List<Node> find_nodes = getAllDefineNodesAsExtraLabel(qt.getFourth());
                for (Node node : find_nodes) {
                    node.getExtra_labels().remove(qt.getFourth());
                }

                //算法改进:赋值不能超越当前最新定义节点，如：a = 1；查找1的节点的时候如果碰到a的定义就不再继续查找
                Node defined = getFirstDefindNode(qt.getSecond());
                Node newest_define = getFirstDefindNode(qt.getFourth());
                if (defined == null || (newest_define != null && nodes.indexOf(newest_define) > nodes.indexOf(defined))) {
                    //如果赋值右边没有定义
                    defined = new Node(qt.getSecond(), null, null, null);
                    nodes.add(defined);
                }
                defined.addLabel(qt.getFourth());

            } else if (qt.getSecond().getName().contains("const") && (qt.getThird().getName().contains("const"))) {
                //如果是常值表达式
                // 查找之前定义的A（作为附加标号的）并删除， 如果是主标记，则不删除
                List<Node> find_nodes = getAllDefineNodesAsExtraLabel(qt.getFourth());
                for (Node node : find_nodes) {
                    node.getExtra_labels().remove(qt.getFourth());
                }
                //计算常值
                Word const_result = calculateConstExpression(qt.getFirst(), qt.getSecond(), qt.getThird());
                Node defined = getFirstDefindNode(const_result);
                if (defined == null) {
                    //如果该常数未定义
                    defined = new Node(const_result, null, null, null);
                    nodes.add(defined);
                }
                defined.addLabel(qt.getFourth());

            } else {
                //查找之前定义的A（作为附加标号的）并删除， 如果是主标记，则不删除
                List<Node> find_nodes = getAllDefineNodesAsExtraLabel(qt.getFourth());
                for (Node node : find_nodes) {
                    node.getExtra_labels().remove(qt.getFourth());
                }
                Node public_expression = getExpression(qt);
                if (public_expression == null) {
                    Node left_node = null;
                    if (qt.getSecond() != null) {
                        left_node = getFirstDefindNode(qt.getSecond());
                        if (left_node == null) {
                            left_node = new Node(qt.getSecond(), null, null, null);
                            nodes.add(left_node);
                        }
                    }
                    Node right_node = null;
                    if (qt.getFirst() != null) {
                        right_node = getFirstDefindNode(qt.getThird());
                        if (right_node == null) {
                            right_node = new Node(qt.getThird(), null, null, null);
                            nodes.add(right_node);
                        }
                    }
                    Node new_node = new Node(qt.getFourth(), qt.getFirst(), left_node, right_node);
                    nodes.add(new_node);
                } else {
                    public_expression.addLabel(qt.getFourth());
                }

            }
        }
        nodes.forEach(System.out::println);
    }

    /**
     * 获取最新定义这个标号的节点
     * 如果没找到就返回null
     *
     * @param label 标号
     * @return Node 最新定义这个标号的节点
     */
    private Node getFirstDefindNode(Word label) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            if (node.containLabel(label)) {
                return node;
            }
        }
        return null;
    }

    /**
     * 获取定义这个标号的节点
     * 必须是这个节点的附加标号
     * 如果没有返回null
     *
     * @param label 标号
     * @return Node 在这里定义的节点
     */
    private ArrayList<Node> getAllDefineNodesAsExtraLabel(Word label) {
        ArrayList<Node> r = new ArrayList<>();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node node = nodes.get(i);
            if (node.containLabel(label) && !node.isMainLabel(label)) {
                r.add(node);
            }
        }
        return r;
    }

    /**
     * 获取公共表达式
     */
    private Node getExpression(Quaternary qt) {
        Node node;
        Node left_node = getFirstDefindNode(qt.getSecond());
        if (left_node == null) {
            return null;
        }
        Node right_node = getFirstDefindNode(qt.getThird());
        if (right_node == null) {
            return null;
        }
        for (int i = nodes.size() - 1; i >= 0; i--) {
            node = nodes.get(i);
            if (node.getOperator() == null) {
                continue;
            }
            if (node.getLeft_child() == left_node && node.getRight_child() == right_node) {
                return node;
            }
        }
        return null;
    }

    private Word calculateConstExpression(Word operator, Word operand_left, Word operand_right) {
        if (operand_right == null) {
            return operand_left;
        }
        StringBuilder result = new StringBuilder("");
        String left_type = operand_left.getName();
        String right_type = operand_right.getName();
        if (left_type.equals("float_const") || right_type.equals("float_const")) {
            double r = calculate(operator, operand_left, operand_right);
            result.append(r);
        } else if (left_type.equals("char_const") || right_type.equals("char_const")) {
            double r = calculate(operator, operand_left, operand_right);
            result.append((char) r);
        } else if (left_type.equals("int_const") || right_type.equals("int_const")) {
            double r = calculate(operator, operand_left, operand_right);
            result.append((int) r);
        } else {
            System.out.println("QtException: " + operand_left + " or " + operand_right + " is not a valid const\n");
        }
        return new Word(result.toString());
    }

    private double toNumber(Word label)  {
        if (!label.getName().contains("const")) {
            System.out.println("QtException: " + label + "is not a const and can not be converted to number\n");
        }

        try {
            switch (label.getName()) {
                case "int_const":
                    return Integer.parseInt(label.getWord());
                case "float_const":
                    return Double.parseDouble(label.getWord());
                case "char_const":
                    if (label.getWord().length() != 1) {
                        System.out.println("QtException const char error: " + label + " can not be converted to character\n");
                    }
                    return label.getWord().charAt(0);
            }
        } catch (NumberFormatException e) {
            System.out.println("QtException const error: " + label + " can not be converted to number\n");
        }
        System.out.println("QtException: try to convert null to number\n");
        return Double.parseDouble(null);
    }

    private double calculate(Word operator, Word number1, Word number2){
        switch (operator.getWord()) {
            case "+":
                return toNumber(number1) + toNumber(number2);
            case "-":
                return toNumber(number1) - toNumber(number2);
            case "*":
                return toNumber(number1) * toNumber(number2);
            case "/":
                return toNumber(number1) / toNumber(number2);
            case ">":
                if (toNumber(number1) > toNumber(number2)) {
                    return 1;
                } else {
                    return 0;
                }
            case ">=":
                if (toNumber(number1) >= toNumber(number2)) {
                    return 1;
                } else {
                    return 0;
                }
            case "<":
                if (toNumber(number1) < toNumber(number2)) {
                    return 1;
                } else {
                    return 0;
                }
            case "<=":
                if (toNumber(number1) <= toNumber(number2)) {
                    return 1;
                } else {
                    return 0;
                }
            case "==":
                if (toNumber(number1) == toNumber(number2)) {
                    return 1;
                } else {
                    return 0;
                }
            case "!=":
                if (toNumber(number1) != toNumber(number2)) {
                    return 1;
                } else {
                    return 0;
                }
            default:
                System.out.println("QtException: " + operator + " is not a valid operator\n");
                return Double.parseDouble(null);
        }
    }
}
