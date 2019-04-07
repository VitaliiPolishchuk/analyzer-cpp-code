package com.cpp.analyzer;

public class Lexem {
    String token;
    int classLexem;

    public Lexem(String token, int classLexem) {
        this.token = token;
        this.classLexem = classLexem;
    }

    public String getToken() {
        return token;
    }

    public int getClassLexem() {
        return classLexem;
    }

    public String toString(){
        return this.getToken() + " - " + CppLexemClass.lexemDictionary.get(Integer.toString(this.getClassLexem()));
    }
}
