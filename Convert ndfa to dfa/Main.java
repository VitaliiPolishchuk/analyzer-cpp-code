package com.company;


import java.util.List;

public class Main {

    public static void main(String[] args) {
        FileStructure fileStructure = new FileStructure();
        fileStructure.load("automat_test.aut");

        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(fileStructure);
        finiteAutomaton.determine();
        fileStructure.upgrade(finiteAutomaton);

        fileStructure.save("automat_test2.aut");
    }
}
