package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.Production;

public class ErrorProduction extends Production {

    private String error;//错误描述
    private String solution;//处理描述

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ErrorProduction(Production father) {
        this.no = father.no;
        this.left = father.left;
        this.right = father.right;
    }

    public ErrorProduction(int no, String left, String[] symbol) {
        super(no, left, symbol);
    }

    public ErrorProduction(int no, String left) {
        super(no, left);
    }
}
