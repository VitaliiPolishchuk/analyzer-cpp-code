package com.cpp.analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LexemClass {
    public static int digit = 0;
    public static int string = 1;
    public static int derective = 2;
    public static int comment = 3;
    public static int specialWord = 4;
    public static int comma = 5;
    public static int identifier = 6;
    public static int constantSymbol = 7;
    public static int dotComma = 8;
    public static int operator = 9;

    public static Set<String> specialWords = new HashSet<String>();

    static Map<String, String> lexemDictionary = new HashMap<String, String>();

    static {
        lexemDictionary.put("0", "Digit");
        lexemDictionary.put("1", "String");
        lexemDictionary.put("2", "Derective");
        lexemDictionary.put("3", "Comment");
        lexemDictionary.put("4", "Special Word");
        lexemDictionary.put("5", "Comma");
        lexemDictionary.put("6", "Identifier");
        lexemDictionary.put("7", "Constant Symbol");
        lexemDictionary.put("8", "Dot Comma");
        lexemDictionary.put("9", "Operator");

        specialWords.add("int");
        specialWords.add("print");
        specialWords.add("void");
        specialWords.add("struct");
        specialWords.add("char");
        specialWords.add("for");
        specialWords.add("static");
        specialWords.add("if");
        specialWords.add("while");
        specialWords.add("else");
    }

    public static boolean isOperation(char ch){
        if("<+>=-/*%".contains(Character.toString(ch))){
            return true;
        }
        else {
            return false;
        }
    }
}
