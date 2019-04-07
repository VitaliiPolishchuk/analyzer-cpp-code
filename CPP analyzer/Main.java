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
            String cppCode = new String();//for saving all context from file

            //reading whole file to variable cppCode
            while ((sCurrentLine = br.readLine()) != null) {
                cppCode += sCurrentLine;
            }

            CppAnalyzer cppAnalyzer = new CppAnalyzer(cppCode);//initialize analyzer

            System.out.println(cppAnalyzer.toString());//print all lexems

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
