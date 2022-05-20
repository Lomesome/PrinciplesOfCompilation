package pers.lomesome.compliation.utils.nfadfamfa;

import pers.lomesome.compliation.model.Edge;
import pers.lomesome.compliation.model.State;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DfaToMfa {

    DataTransfer dataTransfer;

    public DfaToMfa(DataTransfer dataTransfer){
        this.dataTransfer = dataTransfer;
    }

    public List<String> getMFAS() {
        makeMFA();
        List<String> results = new ArrayList<>();
        dataTransfer.mfaEdges.forEach(edge -> results.add(edge.toString()));
        return results;
    }

    private void makeMFA() {
        Queue<List<Integer>> needMFAqueue = new LinkedList<>();
        List<List<Integer>> mfas = new ArrayList<>(); //用来保存MFA读取信息
        List<Integer> end = new ArrayList<>(); //终态集合
        List<Integer> nonEnd = new ArrayList<>(); //非终态集合
        for (State state : dataTransfer.dfaStates) {
            if (state.getIsEnd()) {//是终态
                end.add(state.getNum());//加入终态集合
                continue;
            }
            nonEnd.add(state.getNum());//加入非终态集合
        }
        //将终态集合和非终态集合加入还需分割的队列中
        if (end.size() != 0) needMFAqueue.add(end);
        if (nonEnd.size() != 0) needMFAqueue.add(nonEnd);
        int size;
        do {
            size = needMFAqueue.size();
            List<Integer> gather = needMFAqueue.poll();//队头出列
            if (gather.size() == 1) {
                mfas.add(gather);
                continue;
            }
            for (Character word : dataTransfer.words) {
                ArrayList<State> readCharNextStates = new ArrayList<>();//读了当前字符后的下一个状态们
                for (Integer integer : gather) {//得到所有gather读了char的下一状态
                    State now = getStateFromNum_DFA(integer);//得到gather中的一个状态
                    readCharNextStates.add(now.getNext(word));//下一状态,加入状态集合
                }
                int[] visit = new int[readCharNextStates.size()];//得到是否已有集合的数组
                for (int j = 0; j < readCharNextStates.size(); j++) {
                    if (visit[j] != 0 && readCharNextStates.get(j).getNum() != -1)//已加入集合就跳过,当前状态-1不抵达跳过
                        continue;
                    List<Integer> newGather = new ArrayList<>();//为当前状态相同的状态创造集合
                    newGather.add(gather.get(j));//当前加入
                    visit[j] = 1;
                    for (int k = j + 1; k < readCharNextStates.size(); k++) {//挨个访问之后的状态，是否等价
                        if (haveCommonGather(needMFAqueue, gather, readCharNextStates.get(j), readCharNextStates.get(k))) {
                            //如果当前后继状态相同，加入
                            newGather.add(gather.get(k));
                            visit[k] = 1;
                        }
                    }
                    if (!isNewGather(needMFAqueue, newGather))  //判定当前DFA是否是新集合//直接加入
                        needMFAqueue.add(newGather);
                }
            }
        } while (size != needMFAqueue.size() && !needMFAqueue.isEmpty());//队列的个数不在增多,并且队列不为空
        List<List<Integer>> needRemove = new ArrayList<>();
        while (!needMFAqueue.isEmpty()) {
            List<Integer> newGather = needMFAqueue.poll();
            if (newGather.size() == 1) {//若为单个，必定为单一集合，直接加入最终MFAS
                mfas.add(newGather);
                continue;
            }
            needRemove.add(newGather);
        }
        RemoveRepeat(needRemove, mfas);
        for (int i = 0; i < mfas.size(); i++) {
            State s = new State(i);//每一个MFAS都是一个状态
            for (Integer integer : dataTransfer.dfaBegins) {
                if (mfas.get(i).contains(integer)) {
                    s.setStart();//设为开始
                    break;
                }
            }
            for (Integer integer : dataTransfer.dfaEnds) {
                if (mfas.get(i).contains(integer)) {
                    s.setEnd(true);//设为终结
                    break;
                }
            }
            dataTransfer.mfaStates.add(s);
        }
        for (int i = 0; i < dataTransfer.mfaStates.size(); i++) {//链接
            List<Integer> g = mfas.get(i);//当前MFAS对应所包含的所有DFA元素
            State inGState = getStateFromNum_DFA(g.get(0));//选g中的第一个作为代表
            //链接
            for (int j = 0; j < dataTransfer.words.size(); j++) {//当前状态读每一个单词，是否有下一状态，选第一个作为代表
                int isend = 0;
                State next = inGState.getNext(dataTransfer.words.get(j));//next的下一状态
                if (next.getNum() == -1) {
                    isend++;
                    if (isend == dataTransfer.words.size()) {//终态
                        dataTransfer.mfaStates.get(i).setEnd(true);
                    }
                    continue;
                }
                int k = 0;
                for (; k < mfas.size(); k++) {
                    if (mfas.get(k).contains(next.getNum())) {
                        break;
                    }
                }
                dataTransfer.mfaStates.get(i).setNext(dataTransfer.mfaStates.get(k), dataTransfer.words.get(j));
            }
        }

        for (State s : dataTransfer.mfaStates) {
            for (char subchar : dataTransfer.words) {
                State next = s.getNext(subchar);
                if (next.getNum() != -1) {//有下一状态
                    Edge edge = new Edge("" + s.getNum(), "" + subchar, "" + next.getNum());
                    dataTransfer.mfaEdges.add(edge);
                }
            }
        }
    }

    //去重
    private void RemoveRepeat(List<List<Integer>> needRemove, List<List<Integer>> MFA) {
        for (int i = 0; i < needRemove.size(); i++) {
            List<Integer> g = needRemove.get(i);//当前需要判断需不需要去重的集合
            int MFAhas = 0;
            OUT:
            for (List<Integer> integers : MFA) {//在每一个MFA中寻找有没有包含g中元素的
                for (Integer integer : g) {
                    if (integers.contains(integer)) {
                        MFAhas = 1;
                        break OUT;
                    }
                }
            }
            if (MFAhas == 1) //MFA中已包含该状态，该状态为废状态
                continue;
            //判断接下来的集合中有没有包含他的元素，但大小比他小的集合
            int haveContainButSmallG = 0;

            boolean flag = false;
            for (int j = i + 1; j < needRemove.size(); j++) {
                for (int k = 0; k < g.size(); k++) {
                    if (needRemove.get(j).contains(g.get(k)) && needRemove.get(j).size() < g.size()) {
                        haveContainButSmallG = 1;
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
            if (haveContainButSmallG == 1)
                continue;
            MFA.add(g);
        }

    }

    //判断当前两个状态是不是新的MFAS
    private boolean isNewGather(Queue<List<Integer>> que, List<Integer> state) {
        boolean r = false;
        int size = que.size();
        for (int i = 0; i < size; i++) {
            List<Integer> gather = que.poll();
            que.add(gather);
            if (gather.size() != state.size())
                continue;
            int count = 0;
            for (int j = 0; j < gather.size(); j++) {
                if (gather.contains(state.get(j)))
                    count++;
            }
            if (count == gather.size())
                r = true;
        }
        return r;
    }

    //判断两个状态是否在同一集合
    public boolean haveCommonGather(Queue<List<Integer>> que, List<Integer> nowGther, State s1, State s2) {
        //判断当前在不在nowGther中
        if (nowGther.contains(s1.getNum()) && nowGther.contains(s2.getNum()))
            return true;
        int size = que.size();
        boolean r = false;
        for (int i = 0; i < size; i++) {
            List<Integer> gather = que.poll();
            if (gather.contains(s2.getNum()) && gather.contains(s1.getNum()))
                r = true;
            que.add(gather);
        }
        return r;
    }

    //得到当前编号在NFA中对应状态
    private State getStateFromNum_DFA(int num) {
        State r = new State(-1);
        for (State state : dataTransfer.dfaStates) {
            r = state;
            if (r.getNum() == num)
                return r;
        }
        return r;
    }
}
