package com.cpp.analyzer;

import com.sun.javafx.collections.MappingChange;

import java.util.HashMap;
import java.util.Map;

public class LexemClass {
    private static int digit = 0;
    private static int string = 1;
    private static int derective = 2;
    private static int comment = 3;
    private static int specialWord = 4;
    private static int comma = 5;
    private static int identifier = 6;

    static Map<String, String> lexemDictionary = new HashMap<String, String>();

    public LexemClass() {
        lexemDictionary.put("0", "Digit");
        lexemDictionary.put("1", "String");
        lexemDictionary.put("2", "Derective");
        lexemDictionary.put("3", "Comment");
        lexemDictionary.put("4", "Special Word");
        lexemDictionary.put("5", "Comma");
        lexemDictionary.put("6", "Identifier");
    }
}
