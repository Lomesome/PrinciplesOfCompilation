package pers.lomesome.compliation.model;

import java.util.LinkedHashSet;

/**
 * DAG图中的一个节点的类
 */
public class Node {
    private int id;
    private Word mainLabel;
    private LinkedHashSet<Word> extraLabels;
    private Word operator;
    private Node leftChild;
    private Node middleChild;
    private Node rightChild;

    /**
     * 三个子节点的构造方法
     *
     * @param mainLabel 主标记
     * @param operator 操作符(如果是常数这一项为null)
     * @param leftChild 左子节点
     * @param middleChild 中子节点
     * @param rightChild 右子节点
     */
    public Node(Word mainLabel, Word operator, Node leftChild, Node middleChild, Node rightChild) {
        this.extraLabels = new LinkedHashSet<>();
        this.mainLabel = mainLabel;
        this.operator = operator;
        this.leftChild = leftChild;
        this.middleChild = middleChild;
        this.rightChild = rightChild;
    }

    /**
     * 两个子节点的构造方法
     *
     * @param mainLabel 主标记
     * @param operator 操作符(如果是常数这一项为null)
     * @param leftChild 左子节点
     * @param rightChild 右子节点
     */
    public Node(Word mainLabel, Word operator, Node leftChild, Node rightChild) {
        this.extraLabels = new LinkedHashSet<>();
        this.mainLabel = mainLabel;
        this.operator = operator;
        this.leftChild = leftChild;
        this.middleChild = null;
        this.rightChild = rightChild;
    }

    public Word getMainLabel() {
        return mainLabel;
    }

    public LinkedHashSet<Word> getExtraLabels() {
        return extraLabels;
    }

    public Word getOperator() {
        return operator;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getMiddleChild() {
        return middleChild;
    }

    public Node getRightChild() {
        return rightChild;
    }


    /**
     * 判断是否包含一个标号
     *
     * @param s 标号
     * @return boolean
     */
    public boolean containLabel(Word s) {
        if (mainLabel.getWord().equals(s.getWord())) {
            return true;
        }
        for (Word label : extraLabels) {
            if (label.getWord().equals(s.getWord())) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 判断是不是主标记
     *
     * @param label 标记
     * @return boolean
     */
    public boolean isMainLabel(Word label) {
        return mainLabel.getWord().equals(label.getWord());
    }

    /**
     * 添加标记
     * 优先级，常数 > 用户定义变量> 临时变量
     * @param label 标记
     */
    public void addLabel(Word label) {
        if (label.getName().startsWith("const")) {
            extraLabels.add(mainLabel);
            mainLabel = label;
        } else if (label.getName().startsWith("temp")) {
            extraLabels.add(label);
        } else {
            if (mainLabel.getName().startsWith("temp")) {
                extraLabels.add(mainLabel);
                mainLabel = label;
            } else {
                extraLabels.add(label);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder("(" + operator + ")  " + mainLabel + " | ");
        for (Word label : extraLabels) {
            r.append(label).append(",  ");
        }

        return r.toString();
    }
}
