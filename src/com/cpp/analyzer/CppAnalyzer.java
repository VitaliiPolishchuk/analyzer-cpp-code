package com.cpp.analyzer;

import java.util.ArrayList;

public class CppAnalyzer {
    private ArrayList<Lexem> lexems = new ArrayList<Lexem>(); //lexems
    private String cppCode = new String();//cppCode

    public CppAnalyzer(String cppCode) {
        this.cppCode = cppCode;

        //starting parsing by index in string variable cppCode
        for (int currentIndex = 0; currentIndex < cppCode.length(); currentIndex++) {

            if(Character.isSpaceChar(cppCode.charAt(currentIndex))){
                continue;
            }

            if (cppCode.charAt(currentIndex) == '"'){
                currentIndex = addConstantString(currentIndex);
            }
            else if (cppCode.charAt(currentIndex) == '\''){
                currentIndex = addConstantChar(currentIndex);
            }
            else if (Character.isDigit(cppCode.charAt(currentIndex)) ){
                currentIndex = addDigit(currentIndex);
            }
            else if (cppCode.charAt(currentIndex) == '#'){
                currentIndex = addDerective(currentIndex);
            }
            else if(cppCode.charAt(currentIndex) == '/'){
                currentIndex = addComment(currentIndex);
            }
            else if(cppCode.charAt(currentIndex) == ','){
                lexems.add(new Lexem(",", CppLexemClass.comma));
            }
            else if(cppCode.charAt(currentIndex) == ';'){
                lexems.add(new Lexem(";", CppLexemClass.dotComma));
            }

            //check identifier or special words
            else if(Character.isLetter(cppCode.charAt(currentIndex)) || cppCode.charAt(currentIndex) == '_'){
                currentIndex = addIdentifierOrSpecialWord(currentIndex);
            }
            else if(CppLexemClass.isOperation(cppCode.charAt(currentIndex))){
                currentIndex = addOperation(currentIndex);
            }
        }
    }

    private int addConstantString(int currentIndex){
        String token = "";

        for(currentIndex++; currentIndex < cppCode.length(); currentIndex++){
            if(Character.isSpaceChar(cppCode.charAt(currentIndex))){
                continue;
            }
            if(cppCode.charAt(currentIndex) == '"'){
                break;
            }
            else {
                token += cppCode.charAt(currentIndex);
            }
        }


        lexems.add(new Lexem(token, CppLexemClass.string));

        return currentIndex;
    }

    private int addConstantChar(int currentIndex){
        String token = "";

        for(currentIndex++; currentIndex < cppCode.length(); currentIndex++){
            if(cppCode.charAt(currentIndex) == '\''){
                break;
            }
            else {
                token += cppCode.charAt(currentIndex);
            }
        }

        lexems.add(new Lexem(token, CppLexemClass.constantSymbol));

        return currentIndex;
    }

    private int addDigit(int currentIndex){
        String token = "";
        token += cppCode.charAt(currentIndex);

        boolean exDot = false;

        for(currentIndex++; currentIndex < cppCode.length(); currentIndex++){
            if(Character.isDigit(cppCode.charAt(currentIndex))){
                token += cppCode.charAt(currentIndex);
            } else if (cppCode.charAt(currentIndex) == '.' && !exDot){
                token += cppCode.charAt(currentIndex);
                exDot = true;
            } else {
                currentIndex--;
                break;
            }
        }

        //add only once '.' to out token
        if(token.charAt(token.length() - 1) == '.'){
            token = token.substring(0, token.length() - 1);
        }


        lexems.add(new Lexem(token, CppLexemClass.digit));

        return currentIndex;
    }

    private int addDerective(int currentIndex){
        String token = "";

        for(currentIndex++; currentIndex < cppCode.length(); currentIndex++){
            if(cppCode.charAt(currentIndex) == '_' || Character.isLetter(cppCode.charAt(currentIndex))){
                token += cppCode.charAt(currentIndex);
            } else {
                currentIndex--;
                break;
            }
        }

        lexems.add(new Lexem(token, CppLexemClass.derective));

        return currentIndex;
    }

    private int addComment(int currentIndex){
        String token = "";

        currentIndex++;
        if(cppCode.charAt(currentIndex) == '*'){
            for(currentIndex++; currentIndex < cppCode.length(); currentIndex++){

                if(Character.isSpaceChar(cppCode.charAt(currentIndex))){
                    continue;
                }

                //check commit end comment
                if(cppCode.charAt(currentIndex) == '*'){
                    currentIndex++;
                    if(currentIndex < cppCode.length()){
                        if(cppCode.charAt(currentIndex) == '/'){
                            break;
                        }
                        //just add to token * and char at index currentIndex
                        else {
                            token += '*' + cppCode.charAt(currentIndex);
                        }
                    }
                }
                //just add to token
                else {
                    token += cppCode.charAt(currentIndex);
                }
            }

            lexems.add(new Lexem(token, CppLexemClass.comment));
        } else {
            currentIndex--;
        }

        return currentIndex;
    }

    private int addIdentifierOrSpecialWord(int currentIndex){
        String token = "";

        token += cppCode.charAt(currentIndex);


        for(currentIndex++; currentIndex <cppCode.length(); currentIndex++){

            if(Character.isLetterOrDigit(cppCode.charAt(currentIndex)) || cppCode.charAt(currentIndex) == '_'){
                token += cppCode.charAt(currentIndex);
            }

            else {
                currentIndex--;
                break;
            }
        }

        //add as special word
        if(CppLexemClass.specialWords.contains(token)){
            lexems.add(new Lexem(token, CppLexemClass.specialWord));
        }

        //add as identifier
        else {
            lexems.add(new Lexem(token, CppLexemClass.identifier));
        }

        return currentIndex;
    }

    private int addOperation(int currentIndex){
        String token = "";

        token += cppCode.charAt(currentIndex);
        currentIndex++;

        //operation with end by = (like += or /=)
        if(currentIndex < cppCode.length()){
            if(cppCode.charAt(currentIndex) == '='){
                token += cppCode.charAt(currentIndex);
            }

            //operation <<= or <<
            else if(cppCode.charAt(currentIndex - 1) == '<' && cppCode.charAt(currentIndex) == '<'){
                currentIndex++;
                if(cppCode.charAt(currentIndex) == '=') {
                    token = "<<=";
                } else {
                    currentIndex--;
                    token = "<<";
                }
            }

            //operation >>= or >>
            else if(cppCode.charAt(currentIndex - 1) == '>' && cppCode.charAt(currentIndex) == '>'){
                currentIndex++;
                if(cppCode.charAt(currentIndex) == '=') {
                    token = ">>=";
                } else {
                    currentIndex--;
                    token = ">>";
                }
            } else {
                currentIndex--;
            }
        }


        lexems.add(new Lexem(token, CppLexemClass.operator));
        return currentIndex;
    }

    public String toString(){
        String printedLexems = "";

        for(Lexem lexem: lexems){
           printedLexems += lexem.toString() + "\n";
        }

        return printedLexems;
    }
}
