package pers.lomesome.compliation.utils.semantic;

import pers.lomesome.compliation.model.Id;
import pers.lomesome.compliation.model.Word;
import pers.lomesome.compliation.tool.finalattr.FinalAttribute;
import pers.lomesome.compliation.utils.lexer.Lexer;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SemanticAnalysis {
    String text_input;

    public SemanticAnalysis(String text_input) {
        this.text_input = text_input;
    }

    public void Parsing() {
        Lexer lexer = null;
        try {
            lexer = new Lexer(new FileReader("/Users/leiyunhong/Desktop/test.txt"));
            lexer.next_token();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Word word : lexer.getWords()) {
            word.setName(FinalAttribute.findName(word.getTypenum(), word.getWord()));
        }
        GrammarComplier gc = new GrammarComplier();
        gc.analysis(lexer.getWords());
        List<String> codes = gc.getCodes();
        List<Id> ids = gc.getIds();
        String[] output = new String[5];
        System.out.println("符号表");
        codes.add("END");
        for (int i = 0; i < ids.size(); i++) {  // 输出符号表
            output[0] = "<" + (i + 1) + ">";
            output[1] = ids.get(i).getName();
            StringBuilder type = new StringBuilder(ids.get(i).getType());
            for (int m = 0; m < ids.get(i).arr_list.size(); m++) {
                type.append("[").append(ids.get(i).arr_list.get(m)).append("]");
            }
            output[2] = type.toString();
            output[3] = ids.get(i).getOffset() + "";
            output[4] = ids.get(i).getLength() + "";
            System.out.println(output[1] + " " + output[2] + " " + output[4] + " " + output[3]);
        }
        System.out.println("三地址指令：");
        for (int n = 0; n < codes.size(); n++) {   // 输出三地址指令
            System.out.println(n + " " + codes.get(n));
        }
    }

}