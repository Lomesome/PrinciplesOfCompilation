package pers.lomesome.compliation.utils.grammatical;

import org.json.JSONException;
import pers.lomesome.compliation.view.CodeInterface;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException, JSONException {
        NewAnalysis grammaticalAnalysis = new NewAnalysis();
        grammaticalAnalysis.first_follow();
    }
}
