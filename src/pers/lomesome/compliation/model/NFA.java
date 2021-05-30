package pers.lomesome.compliation.model;

public class NFA {
    private State start;
    private State end;

    public NFA(State start, State end, char c) {  //输入两个节点，直接创造
        this.start = start;
        this.end = end;
        start.setNext(end, c);  //开始连接终结
    }

    public NFA(NFA a, NFA b, State start, State end) {// '|'操作
        this.start = start;
        this.end = end;  //两个新开始结束状态
        this.start.setNext(a.getStart(), 'ε');  //当前新起始状态读ε，到a的开始状态
        this.start.setNext(b.getStart(), 'ε');  //当前新起始状态读ε,到b的开始状态
        a.getEnd().setNext(end, 'ε');  //a的结束状态读‘ε’进入到新的结束状态
        b.getEnd().setNext(end, 'ε');  //b的结束状态读‘ε’进入到新的ε
    }

    public NFA(NFA a, State start, State end) { // '*'操作
        this.start = start;
        this.end = end;//两个新开始结束状态
        this.start.setNext(a.getStart(), 'ε');  //当前新起始状态读ε，到a的开始状态
        this.start.setNext(end, 'ε');   //当前新起始状态读'ε',到当前nfa的结束状态
        a.getEnd().setNext(end, 'ε');  //a的结束状态读‘ε’进入到新的结束状态
        a.getEnd().setNext(a.getStart(), 'ε');  //a的结束状态读‘ε’，进入a原先的开始状态
    }

    public NFA(NFA latter, NFA former) {  // '.'操作
        this.start = former.getStart();  //NFA开始就是前者的开始
        this.end = latter.getEnd();  //NFA的结束就是后者的结束
        former.getEnd().setNext(latter.getStart(), 'ε');
    }

    public void setEnd(State e) {
        end = e;
    }

    public State getStart() {
        return start;
    }

    public State getEnd() {
        return end;
    }
}
