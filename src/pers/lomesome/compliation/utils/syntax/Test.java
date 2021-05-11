package pers.lomesome.compliation.utils.syntax;

import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.lexer.Lexer;
import pers.lomesome.compliation.utils.semantic.Analysis;
import pers.lomesome.compliation.utils.semantic.SymbolTable;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {

        String content = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/MyGrammer.txt"); //C语言官方文法
        GrammaticalHandle grammaticalHandle = new GrammaticalHandle(content);
        FinalAttribute.setAllGrammer(new AllGrammer(grammaticalHandle.grammaticlHandel()));
        String newG = ReadAndWriteFile.readFileContent("/Users/leiyunhong/IdeaProjects/PrinciplesOfCompilation/src/resources/grammar/MyGrammerWithAction.txt"); //C语言官方文法
        GrammaticalHandle newGrammaticalHandle = new GrammaticalHandle(newG);
        FinalAttribute.setAllGrammerWithAction(new AllGrammer(newGrammaticalHandle.grammaticlHandel()));
        FinalAttribute.init();
        SyntaxAnalysis.init();
        Analysis.init();

        List<Word> list = new LinkedList<>();
        Lexer lexer = null;
        try {
            lexer = new Lexer(new FileReader("/Users/leiyunhong/Desktop/test.txt"));
            lexer.next_token();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert lexer != null;
        for (Word word : lexer.getWords()) {
            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
            list.add(word);
        }
        list.add(new Word("#", "end", -1,-1));
        list.get(list.size() - 1).setName("#");

        SymbolTable Table = new SymbolTable();
        Table.getTable(list);
        SyntaxAnalysis.analysis(list, Table);
        Table.printtable();
        Analysis.analysis(list, Table);

    }
}
