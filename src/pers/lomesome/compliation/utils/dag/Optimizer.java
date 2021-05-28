package pers.lomesome.compliation.utils.dag;

import pers.lomesome.compliation.model.Quaternary;
import java.util.ArrayList;
import java.util.List;

public class Optimizer {
    private List<Quaternary> quaternaryList;
    public Optimizer(List<Quaternary> quaternaryList) {
        quaternaryList = quaternaryList;
    }

    public List<Quaternary> optimize() {
        List<Quaternary> result_QTs = new ArrayList<>();
        List<Quaternary> cache = new ArrayList<>();
        for (int i = 0; i < quaternaryList.size(); i++){
            if (!isArithmeticOperator(quaternaryList.get(i).getFirst().getWord())) {
                if (!cache.isEmpty()) {
                    System.out.println("******");
                    DAG DAG = new DAG(cache);
                    cache = DAG.optimite();
                    result_QTs.addAll(cache);
                    cache.clear();
                }

                result_QTs.add(quaternaryList.get(i));
                continue;
            }
            cache.add(quaternaryList.get(i));
        }

        return result_QTs;
    }

    public static boolean isArithmeticOperator(String operator) {
        return operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/")
                || operator.equals("=") || operator.equals(">") || operator.equals(">=")
                || operator.equals("<") || operator.equals("<=") || operator.equals("==")
                || operator.equals("!=") ;
    }
}
