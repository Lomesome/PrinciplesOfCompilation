package pers.lomesome.compliation.utils.grammatical;

import pers.lomesome.compliation.tool.filehandling.FileUtil;

import java.util.*;

public class AllGrammer {

    private Map<String, List<List<String>>> grammarMap;

    public AllGrammer(Map<String, List<List<String>>> grammarMap){
        this.grammarMap = grammarMap;
    }

    public Map<String, List<List<String>>> getGrammarMap() {
        return grammarMap;
    }

    public void setGrammarMap(Map<String, List<List<String>>> grammarMap) {
        this.grammarMap = grammarMap;
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<List<String>>> copyGrammarMap(){
        Map<String, List<List<String>>> stringListMap = new LinkedHashMap<>();
        try{
            stringListMap = (Map<String, List<List<String>>>) FileUtil.deepCopy(grammarMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringListMap;
    }
}
