package pers.lomesome.compliation.utils.reg2nfa;

import pers.lomesome.compliation.model.NfaEdge;
import pers.lomesome.compliation.model.NFA;
import pers.lomesome.compliation.model.State;

import java.util.*;

public class Reg2Nfa {

    public NFA express_2_NFA(String expression) {
        State.ID = 0;
        int length = expression.length();
        char element;
        NFA nfa, left, right;
        Stack<NFA> nfaStack = new Stack<>();
        for (int i = 0; i < length; i++) {
            element = expression.charAt(i);
            switch (element) {
                case '|':
                    right = nfaStack.pop();
                    left = nfaStack.pop();
                    nfa = do_Unite(left, right);
                    nfaStack.push(nfa);
                    break;
                case '*':
                    left = nfaStack.peek();
                    nfaStack.pop();
                    nfa = do_Star(left);
                    nfaStack.push(nfa);
                    break;
                case '.':
                    right = nfaStack.peek();
                    nfaStack.pop();
                    left = nfaStack.peek();
                    nfaStack.pop();
                    nfa = do_Join(left, right);
                    nfaStack.push(nfa);
                    break;
                default:
                    nfa = do_Cell(element);
                    nfaStack.push(nfa);
            }
        }
        nfa = nfaStack.peek();
        nfaStack.pop();
        return nfa;
    }

    //处理 a|b
    private NFA do_Unite(NFA Left, NFA Right) {
        NFA newNfa = new NFA();
        State StartState = new State();
        State EndState = new State();
        cell_EdgeSet_Copy(newNfa, Left);
        cell_EdgeSet_Copy(newNfa, Right);
        newNfa.getEdgeList().add(new NfaEdge(StartState, "ε", Left.getBeginState()));
        newNfa.getEdgeList().add(new NfaEdge(StartState, "ε", Right.getBeginState()));
        newNfa.getEdgeList().add(new NfaEdge(Left.getEndState(), "ε", EndState));
        newNfa.getEdgeList().add(new NfaEdge(Right.getEndState(), "ε", EndState));
        newNfa.setBeginState(StartState);
        newNfa.setEndState(EndState);
        return newNfa;
    }

    //处理 ab
    private NFA do_Join(NFA Left, NFA Right) {
        NFA newNfa = new NFA();
        cell_EdgeSet_Copy(newNfa, Left);
        cell_EdgeSet_Copy(newNfa, Right);
        newNfa.getEdgeList().add(new NfaEdge(Left.getEndState(), "ε", Right.getBeginState()));
        newNfa.setBeginState(Left.getBeginState());
        newNfa.setEndState(Right.getEndState());
        return newNfa;
    }

    //处理 a*
    private NFA do_Star(NFA nfa) {
        NFA newNfa = new NFA();
        State StartState = new State();
        State EndState = new State();
        cell_EdgeSet_Copy(newNfa, nfa);
        newNfa.getEdgeList().add(new NfaEdge(StartState,"ε",EndState));
        newNfa.getEdgeList().add(new NfaEdge(nfa.getEndState(),"ε",nfa.getBeginState()));
        newNfa.getEdgeList().add(new NfaEdge(StartState,"ε",nfa.getBeginState()));
        newNfa.getEdgeList().add(new NfaEdge(nfa.getEndState(),"ε",EndState));
        newNfa.setBeginState(StartState);
        newNfa.setEndState(EndState);
        return newNfa;
    }

    private NFA do_Cell(char element) {
        NFA newNfa = new NFA();
        State StartState = new State();
        State EndState = new State();
        newNfa.getEdgeList().add(new NfaEdge(StartState, String.valueOf(element),EndState));
        newNfa.setBeginState(newNfa.getEdgeList().get(0).getBegin());
        newNfa.setEndState(newNfa.getEdgeList().get(0).getEnd());
        return newNfa;
    }

    private void cell_EdgeSet_Copy(NFA Destination, NFA Source) {
        Destination.getEdgeList().addAll(Source.getEdgeList());
    }

    public List<String> getResults(NFA nfa) {
        Set<String> list = new LinkedHashSet<>();
        for (NfaEdge nfaEdge : nfa.getEdgeList()) {
            list.add(nfaEdge.toString());
        }
        list.add(nfa.getEndState()+ " [shape=doublecircle];");
        return new ArrayList<>(list);
    }
}
