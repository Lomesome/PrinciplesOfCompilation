package pers.lomesome.compliation.utils.grammatical;

import javafx.application.Platform;
import org.json.JSONException;
import pers.lomesome.compliation.model.PropertyWord;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.lexer.Lexer;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        GrammaticalAnalysis.init();
        FinalAttribute.init();

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
        GrammaticalAnalysis.analysis(list);

    }
}
