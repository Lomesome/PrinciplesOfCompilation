package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.filehandling.ReadAndWriteFile;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.lexer.Lexer;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        String content  = ReadAndWriteFile.readFileContent("/Users/leiyunhong/Desktop/test.txt");

        FinalAttribute.init();
        List<String> lex_result_stack = new LinkedList<>();
        Lexer lexer = null;
        try {
            lexer = new Lexer(new FileReader("/Users/leiyunhong/Desktop/test.txt"));
            lexer.next_token();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Word word : lexer.getWords()) {
            lex_result_stack.add(FinalAttribute.findString(word.getTypenum(), word.getWord()));
        }

        List<Word> lex_error_stack = lexer.getErrorMsgList();
        // 若是存在词法分析错误
        if(lex_error_stack.size()!=0){
            System.out.println("error");
        }
        else {
            // 语义分析
            SemanticAnalysis semanticanalyse = new SemanticAnalysis(content);
            semanticanalyse.Parsing();
        }
    }
}
