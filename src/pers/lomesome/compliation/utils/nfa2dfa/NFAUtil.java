package pers.lomesome.compliation.utils.nfa2dfa;

import pers.lomesome.compliation.model.NfaEdge;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NFAUtil {

    private final List<NfaEdge> nfaNfaEdgeList;

    public NFAUtil(List<NfaEdge> nfaNfaEdgeList) {
        this.nfaNfaEdgeList = nfaNfaEdgeList;
    }

    public Set<String> getEpsilonClosure(String key) {
        Set<String> res = new HashSet<>();
        for (NfaEdge nfaEdge : nfaNfaEdgeList) {
            if (nfaEdge.getBegin().toString().equals(key) && nfaEdge.getLabel().equals("ε")) {
                res.add(nfaEdge.getEnd().toString());
            }
        }
        if (res.size() > 0) {
            Set<String> tmpSet = new HashSet<>();
            for (String next : res) {
                Set<String> closure = getEpsilonClosure(next);
                tmpSet.addAll(closure);
            }
            res.addAll(tmpSet);
        }
        res.add(key);
        return res;
    }

    public Set<String> getMove(Set<String> set, String path) {
        Set<String> res = new HashSet<>();
        for (String setElem : set) {
            for (NfaEdge nfaEdge : nfaNfaEdgeList) {
                if (nfaEdge.getLabel().equals(path) && nfaEdge.getBegin().toString().equals(setElem)) {
                    res.add(nfaEdge.getEnd().toString());
                }
            }
        }
        return res;
    }
}
