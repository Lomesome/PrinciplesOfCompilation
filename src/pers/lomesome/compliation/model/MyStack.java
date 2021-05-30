package pers.lomesome.compliation.model;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStack<T> extends Stack<T> {

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        this.forEach( s -> result.append(" ").append(((MakeJson)s).getName()));
        return result.toString();
    }

}
