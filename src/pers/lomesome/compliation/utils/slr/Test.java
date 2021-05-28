package pers.lomesome.compliation.utils.slr;

import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.utils.syntax.GrammaticalHandle;

public class Test {
    public static void main(String[] args) {
        String content = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/SLR_Grammer.txt");
        GrammaticalHandle grammaticalHandle = new GrammaticalHandle(content);
        grammaticalHandle.grammaticlHandel().forEach((k, v)->{
            System.out.println(k + " -> " +v);
        });
    }
}
