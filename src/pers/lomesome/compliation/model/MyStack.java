package pers.lomesome.compliation.model;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStack<T> extends Stack<T> {

    @Override
    public String toString(){
//        String regex = "\"name\":(\"(.*?)\")";
//        Matcher matcher = Pattern.compile(regex).matcher(Arrays.toString(this.toArray()));
//        String value;
//        StringBuilder result = new StringBuilder();
//        while (matcher.find()) {
//            value = matcher.group(1);
//            result.append(" ").append(value, 1, value.length() - 1);
//        }
        StringBuilder result = new StringBuilder();
        this.forEach( s -> result.append(" ").append(((MakeJson)s).getName()));
        return result.toString();
    }
}
