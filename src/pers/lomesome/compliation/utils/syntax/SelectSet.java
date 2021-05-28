package pers.lomesome.compliation.utils.syntax;

import java.util.*;

public class SelectSet {
    private final Map<String, List<List<String>>> grammars;

    public SelectSet(Map<String, List<List<String>>> grammars){
        this.grammars = grammars;
    }

    public Map<List<List<String>>, Set<String>> getSelectSet() {
        Map<List<List<String>>, Set<String>> selectSet = new LinkedHashMap<>();
        grammars.forEach((k, v)->{
            for (List<String> list : v){
                Set<String> vnSelect = new LinkedHashSet<>();
                getSelect(k, vnSelect, list);
                List<List<String>> listList = new ArrayList<>();
                listList.add(Collections.singletonList(k));
                listList.add(list);
                selectSet.put(listList, vnSelect);
            }
        });
        return selectSet;
    }

    private Set<String> getSelect(String vn, Set<String> vnSelect) {
        if (!grammars.containsKey(vn)) {
            vnSelect.add(vn);
            return vnSelect;
        }
        int count = 0;
        boolean nullFlag = false;
        for (List<String> infer : grammars.get(vn)) {
            for (String singleInfer : infer) {
                Set<String> newVnFirst = new LinkedHashSet<>();
                Set<String> checkNull = getSelect(singleInfer, newVnFirst);
                vnSelect.addAll(checkNull);
                vnSelect.remove("ε");
                if (singleInfer.equals("ε")){
                    nullFlag = true;
                }
                if (checkNull.contains("ε") && grammars.containsKey(singleInfer)){
                    count++;
                }
                else if (!checkNull.contains("ε")) {
                    break;
                }
            }
        }
        if (count == grammars.get(vn).size() || nullFlag) {
            vnSelect.add("ε");
        }
        return vnSelect;
    }

    private void getSelect(String vn, Set<String> vnSelect, List<String> infer) {
        if (!grammars.containsKey(vn)) {
            vnSelect.add(vn);
            return;
        }
        int count = 0;
        boolean nullFlag = false;

        for (String singleInfer : infer) {
            Set<String> newVnFirst = new LinkedHashSet<>();
            Set<String> checkNull = getSelect(singleInfer, newVnFirst);
            vnSelect.addAll(checkNull);
            vnSelect.remove("ε");
            if (singleInfer.equals("ε")) {
                nullFlag = true;
            }
            if (checkNull.contains("ε") && grammars.containsKey(singleInfer)) {
                count++;
            } else if (!checkNull.contains("ε")) {
                break;
            }
        }
        if (count == grammars.get(vn).size() || nullFlag) {
            vnSelect.add("ε");
        }
    }
}