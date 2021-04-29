package pers.lomesome.compliation.utils.grammatical;

import java.util.*;

public class EliminateLR {
    private final AllGrammer grammers;
    private List<String> vt = new LinkedList<>();
    private List<List<List<String>>> replaceableList = new LinkedList<>();

    public EliminateLR(AllGrammer grammers){
        this.grammers = grammers;
        grammers.getGrammarMap().forEach((k, v)->{
            vt.add(k);
            replaceableList.add(v);
        });
    }

    public void eliminate() {
        List<String> passedVT = new ArrayList<>();
        for (int i = grammers.getGrammarMap().size() - 1; i >= 0; i--) {
            int range = replaceableList.get(i).size();
            String vn = vt.get(i);
            for (int j = 0; j < range; j++) {        //若当前非终结符与对应各个导出式的首个标识符相同,说明为直接左递归
                if ((replaceableList.get(i).get(j).get(0)).equals(vn)) {
                    directEliminate(i, range, vn);  //消除直接左递归
                    break;
                }
            }
        }
        for (int i = 0; i < vt.size(); i++){
            grammers.getGrammarMap().put(vt.get(i), replaceableList.get(i));
        }

        List<String> vtCopy = new LinkedList<>();
        List<List<List<String>>> vnInferCopy = new LinkedList<>();
        grammers.copyGrammarMap().forEach((k, v)->{
            vtCopy.add(k);
            vnInferCopy.add(v);
        });
        for (int i = vtCopy.size() - 1; i >= 0; i--) {      //倒序遍历,因为输入文法一般为由上而下书写顺序
            String vn_cur = vtCopy.get(i);
            int range = vnInferCopy.get(i).size();          //获取每一个非终结符对应导出式个数

            for (int j = 0; j < range; j++) {
                List<String> vnInfer = vnInferCopy.get(i).get(j);     //获取非终结符导出式
                if (passedVT.contains(vnInfer.get(0))) {        //已处理左递归的非终结符中是否包含当前字符
                    vnInferCopy.get(i).remove(vnInfer);
                    List<List<String>> list = vnInferCopy.get(vtCopy.indexOf(vnInfer.get(0)));
                    vnInfer.remove(0);
                    for (List<String> str : list) {
                        List<String> temp = new ArrayList<>();
                        temp.addAll(str);
                        temp.addAll(vnInfer);
                        vnInferCopy.get(i).add(temp);        //代入非终结符直至非终结符起始
                        range = vnInferCopy.get(i).size();
                    }
                }
            }
            range = vnInferCopy.get(i).size();
            for (int j = 0; j < range; j++) {
                if ((vnInferCopy.get(i).get(j).get(0)).equals(vn_cur)) {
                    vt = vtCopy;
                    replaceableList = vnInferCopy;
                    directEliminate(i, range, vn_cur);
                    for (int k = 0; k < vt.size(); k++){
                        grammers.getGrammarMap().put(vt.get(k), replaceableList.get(k));
                    }
                    removeExcess(grammers);
                    break;
                }
            }
            passedVT.add(vn_cur);
        }
    }

    private void directEliminate(int i, int range, String vn) {
        vt.add(vn + "'");
        replaceableList.add(new ArrayList<>());
        for (int j = 0; j < range; ) {
            List<String> vnInfer = replaceableList.get(i).get(j);
            replaceableList.get(i).remove(vnInfer);
            if ((vnInfer.get(0)).equals(vn)) {
                vnInfer.remove(0);
                vnInfer.add(vn + "'");
                replaceableList.get(vt.size() - 1).add(vnInfer);
            }
            else {
                vnInfer.add(vn + "'");
                replaceableList.get(i).add(vnInfer);
            }
            range--;
        }
        replaceableList.get(vt.size() - 1).add(Collections.singletonList("ε"));
    }

    private void removeExcess(AllGrammer grammers) {
        Set<String> reachable = new HashSet<>();
        List<String> vtCopy = new LinkedList<>();
        List<List<List<String>>> vnInferCopy = new LinkedList<>();
        grammers.copyGrammarMap().forEach((k, v)->{
            vtCopy.add(k);
            vnInferCopy.add(v);
        });
        for (List<List<String>> grammar: vnInferCopy){
            for (List<String> list : grammar){
                reachable.addAll(list);
            }
        }
        for (String list : vtCopy){
            if (!reachable.contains(list)){
                int index = vt.indexOf(list);
                vt.remove(index);
                replaceableList.remove(index);
                grammers.getGrammarMap().remove(list);
            }
        }
    }
}
