package com.cpp.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //open file
        String fileName = "code.cpp"; //file that is opened
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String sCurrentLine; //for read every line from file
            ArrayList<Lexem> lexems = new ArrayList<Lexem>(); //for adding created lexem
            String wholeFileCode = new String();//for saving all context from file

            //reading whole file to variable wholeFileCode
            while ((sCurrentLine = br.readLine()) != null) {
                wholeFileCode += sCurrentLine;
            }

            //starting parsing by index in string variable wholeFileCode
            for (int i = 0; i < wholeFileCode.length(); i++) {
                if(Character.isSpaceChar(wholeFileCode.charAt(i))){
                    continue;
                }

                //token exist for creating new Lexem
                String token = new String();

                //==========================
                //Constant String: "sadfa...adsf"
                //==========================
                if (wholeFileCode.charAt(i) == '"'){
                    for(i++; i < wholeFileCode.length(); i++){
                        if(Character.isSpaceChar(wholeFileCode.charAt(i))){
                            continue;
                        }
                        if(wholeFileCode.charAt(i) == '"'){
                            break;
                        }
                        else {
                            token += wholeFileCode.charAt(i);
                        }
                    }


                    lexems.add(new Lexem(token, CppLexemClass.string));
                }

                //==========================
                //Constant char: 'fas....asdf'
                //==========================
                else if (wholeFileCode.charAt(i) == '\''){

                    for(i++; i < wholeFileCode.length(); i++){
                        if(wholeFileCode.charAt(i) == '\''){
                            break;
                        }
                        else {
                            token += wholeFileCode.charAt(i);
                        }
                    }


                    lexems.add(new Lexem(token, CppLexemClass.constantSymbol));
                }


                //==========================
                //Digit: 12...31 or 123243.12...32
                //==========================
                else if (Character.isDigit(wholeFileCode.charAt(i)) ){

                     token += wholeFileCode.charAt(i);

                     boolean exDot = false;

                     for(i++; i < wholeFileCode.length(); i++){
                         if(Character.isDigit(wholeFileCode.charAt(i))){
                             token += wholeFileCode.charAt(i);
                         } else if (wholeFileCode.charAt(i) == '.' && !exDot){
                             token += wholeFileCode.charAt(i);
                             exDot = true;
                         } else {
                             i--;
                             break;
                         }
                     }

                     //add only once '.' to out token
                     if(token.charAt(token.length() - 1) == '.'){
                         token = token.substring(0, token.length() - 1);
                     }


                    lexems.add(new Lexem(token, CppLexemClass.digit));
                }

                //==========================
                //Derective: #some_text
                //==========================
                else if (wholeFileCode.charAt(i) == '#'){

                    for(i++; i < wholeFileCode.length(); i++){
                        if(wholeFileCode.charAt(i) == '_' || Character.isLetter(wholeFileCode.charAt(i))){
                            token += wholeFileCode.charAt(i);
                        } else {
                            i--;
                            break;
                        }
                    }

                    lexems.add(new Lexem(token, CppLexemClass.derective));
                }

                //==========================
                //Comment: /* ... */
                //==========================
                else if(wholeFileCode.charAt(i) == '/'){
                    i++;
                    if(wholeFileCode.charAt(i) == '*'){
                        for(i++; i < wholeFileCode.length(); i++){

                            if(Character.isSpaceChar(wholeFileCode.charAt(i))){
                                continue;
                            }

                            //check commit end comment
                            if(wholeFileCode.charAt(i) == '*'){
                                i++;
                                if(i < wholeFileCode.length()){
                                    if(wholeFileCode.charAt(i) == '/'){
                                        break;
                                    }
                                    //just add to token * and char at index i
                                    else {
                                        token += '*' + wholeFileCode.charAt(i);
                                    }
                                }
                            }
                            //just add to token
                            else {
                                token += wholeFileCode.charAt(i);
                            }
                        }

                        lexems.add(new Lexem(token, CppLexemClass.comment));
                    } else {
                        i--;
                    }
                }

                //==========================
                //Comma: , (it is very easy, right?)
                //==========================
                else if(wholeFileCode.charAt(i) == ','){
                    lexems.add(new Lexem(",", CppLexemClass.comma));
                }

                //==========================
                //DotComma: ;
                //==========================
                else if(wholeFileCode.charAt(i) == ';'){
                    lexems.add(new Lexem(";", CppLexemClass.dotComma));
                }

                //==========================
                //Letter or specialWords: asdfa_fsad...SDfd or _fadf...FDSF__SDFE_
                //==========================
                else if(Character.isLetter(wholeFileCode.charAt(i)) || wholeFileCode.charAt(i) == '_'){
                    token += wholeFileCode.charAt(i);


                    for(i++; i <wholeFileCode.length(); i++){

                        if(Character.isLetterOrDigit(wholeFileCode.charAt(i)) || wholeFileCode.charAt(i) == '_'){
                            token += wholeFileCode.charAt(i);
                        }

                        else {
                            i--;
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
                }

                //==========================
                //Operation: whole operation C++
                //==========================
                else if(CppLexemClass.isOperation(wholeFileCode.charAt(i))){
                    token += wholeFileCode.charAt(i);
                    i++;

                    //operation with end by = (like += or /=)
                    if(i < wholeFileCode.length()){
                        if(wholeFileCode.charAt(i) == '='){
                            token += wholeFileCode.charAt(i);
                        }

                        //operation <<= or <<
                        else if(wholeFileCode.charAt(i - 1) == '<' && wholeFileCode.charAt(i) == '<'){
                            i++;
                            if(wholeFileCode.charAt(i) == '=') {
                                token = "<<=";
                            } else {
                                i--;
                                token = "<<";
                            }
                        }

                        //operation >>= or >>
                        else if(wholeFileCode.charAt(i - 1) == '>' && wholeFileCode.charAt(i) == '>'){
                            i++;
                            if(wholeFileCode.charAt(i) == '=') {
                                token = ">>=";
                            } else {
                                i--;
                                token = ">>";
                            }
                        } else {
                            i--;
                        }
                    }


                    lexems.add(new Lexem(token, CppLexemClass.operator));
                }
            }

            //print all lexems
            for(Lexem lexem: lexems){
                System.out.println(lexem.getToken() + " - " + CppLexemClass.lexemDictionary.get(Integer.toString(lexem.getClassLexem())));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
