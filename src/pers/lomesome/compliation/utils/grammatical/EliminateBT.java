package pers.lomesome.compliation.utils.grammatical;

import pers.lomesome.compliation.model.Counter;

import java.util.*;

public class EliminateBT {
    private final AllGrammer grammers;
    private List<String> vt = new LinkedList<>();
    private List<List<List<String>>> replaceableList = new LinkedList<>();

    public EliminateBT(AllGrammer grammers) {
        this.grammers = grammers;
        grammers.getGrammarMap().forEach((k, v) -> {
            vt.add(k);
            replaceableList.add(v);
        });
    }

    public void eliminate() {
        for (int i = grammers.getGrammarMap().size() - 1; i >= 0; i--) {
            int range = replaceableList.get(i).size();
            String vn = vt.get(i);
            Set<String> set = new HashSet();
            for (List<String> lists : replaceableList.get(i)) {
                set.add(lists.get(0));
            }
            if (set.size() < range){
                backtrackingElimination(i, range, vn);
            }
        }
        for (int i = 0; i < vt.size(); i++){
            grammers.getGrammarMap().put(vt.get(i), replaceableList.get(i));
        }
    }

    private void backtrackingElimination(int i, int range, String vn){

        Counter counter = new Counter();
        for (List<String> l : replaceableList.get(i)){
            counter.addS(l.get(0));
        }
        int num = 0;
        for (String same : counter.getRepeat()){
            num++;
            StringBuilder newVn = new StringBuilder(vn);
            for (int k = 0; k < num; k++){
                newVn.append("'");
            }
            vt.add(newVn.toString());
            replaceableList.add(new ArrayList<>());
            for (int j = 0; j < range;) {
                List<String> vnInfer = replaceableList.get(i).get(j);
                List<String> newVnInfer = new ArrayList<>(vnInfer.subList(0, vnInfer.size()));
                newVnInfer.remove(0);
                if (newVnInfer.size()==0){
                    newVnInfer.add("ε");
                }
                if ((vnInfer.get(0)).equals(same)) {
                    replaceableList.get(i).remove(vnInfer);
                    if (!replaceableList.get(vt.size() - 1).contains(newVnInfer))
                        replaceableList.get(vt.size() - 1).add(newVnInfer);
                }else {
                    j++;
                    range++;
                }
                range--;
            }
            List<String> newList = new ArrayList<>();
            newList.add(same);
            newList.add(newVn.toString());
            replaceableList.get(i).add(newList);
        }
    }
}
