package pers.lomesome.compliation.utils.grammatical;

import javafx.application.Platform;
import org.json.JSONException;
import pers.lomesome.compliation.model.PropertyWord;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.lexer.Lexer;
import pers.lomesome.compliation.view.CodeInterface;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException, JSONException {
        NewAnalysis grammaticalAnalysis = new NewAnalysis();
        grammaticalAnalysis.first_follow();
        grammaticalAnalysis.makePredict();
        FinalAttribute.init();
        List<String> list = new LinkedList<>();
        Lexer lexer = null;
        try {
            lexer = new Lexer(new FileReader("/Users/leiyunhong/Desktop/test.txt"));
            lexer.next_token();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Word word : lexer.getWords()) {
            word.setName(FinalAttribute.findName(word.getTypenum(), word.getWord()));
            list.add(word.getWord());
        }
        System.out.println(lexer.getWords());

        grammaticalAnalysis.analysis(list);

    }
}
