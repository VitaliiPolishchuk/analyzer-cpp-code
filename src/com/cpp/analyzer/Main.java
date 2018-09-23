package com.cpp.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader("code.cpp"))) {
            String sCurrentLine;
            ArrayList<Lexem> lexems = new ArrayList<Lexem>();
            while ((sCurrentLine = br.readLine()) != null) {
                String subline = sCurrentLine;
                    for (int i = 0; i < subline.length(); i++) {
                        if(Character.isSpaceChar(subline.charAt(i))){
                            continue;
                        }
                        String token = new String();
                        if (subline.charAt(i) == '"'){
                            for(i++; i < subline.length(); i++){
                                if(Character.isSpaceChar(subline.charAt(i))){
                                    continue;
                                }
                                if(subline.charAt(i) == '"'){
                                    break;
                                }
                                else {
                                    token += subline.charAt(i);
                                }
                            }
                            lexems.add(new Lexem(token, LexemClass.string));
                        } else if (subline.charAt(i) == '\''){
                            for(i++; i < subline.length(); i++){
                                if(subline.charAt(i) == '\''){
                                    break;
                                }
                                else {
                                    token += subline.charAt(i);
                                }
                            }
                            lexems.add(new Lexem(token, LexemClass.constantSymbol));
                        } else if (Character.isDigit(subline.charAt(i)) ){
                             token += subline.charAt(i);
                             boolean exDot = false;
                             for(i++; i < subline.length(); i++){
                                 if(Character.isDigit(subline.charAt(i))){
                                     token += subline.charAt(i);
                                 } else if (subline.charAt(i) == '.' && !exDot){
                                     token += subline.charAt(i);
                                     exDot = true;
                                 } else {
                                     i--;
                                     break;
                                 }
                             }
                             if(token.charAt(token.length() - 1) == '.'){
                                 token = token.substring(0, token.length() - 1);
                             }
                            lexems.add(new Lexem(token, LexemClass.digit));
                        } else if (subline.charAt(i) == '#'){
                            for(i++; i < subline.length(); i++){
                                if(subline.charAt(i) == '_' || Character.isLetter(subline.charAt(i))){
                                    token += subline.charAt(i);
                                } else {
                                    i--;
                                    break;
                                }
                            }

                            lexems.add(new Lexem(token, LexemClass.derective));
                        } else if(subline.charAt(i) == '/'){
                            i++;
                            if(subline.charAt(i) == '*'){
                                for(i++; i < subline.length(); i++){
                                    if(Character.isSpaceChar(subline.charAt(i))){
                                        continue;
                                    }
                                    if(subline.charAt(i) == '*'){
                                        i++;
                                        if(i < subline.length()){
                                            if(subline.charAt(i) == '/'){
                                                break;
                                            } else {
                                                token += '*' + subline.charAt(i);
                                            }
                                        }
                                    } else {
                                        token += subline.charAt(i);
                                    }
                                }

                                lexems.add(new Lexem(token, LexemClass.comment));
                            } else {
                                i--;
                            }
                        } else if(subline.charAt(i) == ','){
                            lexems.add(new Lexem(",", LexemClass.comma));
                        } else if(Character.isLetter(subline.charAt(i))){
                            token += subline.charAt(i);
                            for(i++; i <subline.length(); i++){
                                if(Character.isLetterOrDigit(subline.charAt(i)) || subline.charAt(i) == '_'){
                                    token += subline.charAt(i);
                                } else {
                                    i--;
                                    break;
                                }
                            }
                            if(LexemClass.specialWords.contains(token)){
                                lexems.add(new Lexem(token, LexemClass.specialWord));
                            } else {
                                lexems.add(new Lexem(token, LexemClass.identifier));
                            }
                        } else if(subline.charAt(i) == ';'){
                            lexems.add(new Lexem(";", LexemClass.dotComma));
                        } else if(LexemClass.isOperation(subline.charAt(i))){
                            token += subline.charAt(i);
                            i++;
                            if(i < subline.length()){
                                if(subline.charAt(i) == '='){
                                    token += subline.charAt(i);
                                } else if(subline.charAt(i - 1) == '<' && subline.charAt(i) == '<'){
                                    i++;
                                    if(subline.charAt(i) == '=') {
                                        token = "<<=";
                                    } else {
                                        i--;
                                        token = "<<";
                                    }
                                } else if(subline.charAt(i - 1) == '>' && subline.charAt(i) == '>'){
                                    i++;
                                    if(subline.charAt(i) == '=') {
                                        token = ">>=";
                                    } else {
                                        i--;
                                        token = ">>";
                                    }
                                } else {
                                    i--;
                                }
                            }
                            lexems.add(new Lexem(token, LexemClass.operator));
                        }
                    }
            }

            for(Lexem lexem: lexems){
                System.out.println(lexem.getToken() + " - " + LexemClass.lexemDictionary.get(Integer.toString(lexem.getClassLexem())));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
