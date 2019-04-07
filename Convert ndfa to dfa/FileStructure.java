package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileStructure {
    String alphabet, states, startState, finalStates;
    List<String> transactions = new ArrayList<String>();

    public FileStructure() {

    }

    public String getAlphabet() {
        return alphabet;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    //load data from file
    public void load(String fileName){
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = (BufferedReader) Files.newBufferedReader(Paths.get(fileName), charset)) {
            String line = reader.readLine();

            //read by line
            this.alphabet = line;
            line = reader.readLine();
            this.states = line;
            line = reader.readLine();
            this.startState = line;
            line = reader.readLine();
            this.finalStates = line;

            //read transaction
            List<String> _transactions = new ArrayList<String>();
            while((line = reader.readLine()) != null){
                _transactions.add(line);
            }
            this.transactions = _transactions;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String fileName) {
        try {
            // Assume default encoding.
            FileWriter fileWriter =
                    new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write(this.alphabet);
            bufferedWriter.newLine();
            bufferedWriter.write(this.states);
            bufferedWriter.newLine();
            bufferedWriter.write(this.startState);
            bufferedWriter.newLine();
            bufferedWriter.write(this.finalStates);
            bufferedWriter.newLine();

            //add trasactions
            for (String transaction : this.transactions) {
                bufferedWriter.write(transaction);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }

    public void upgrade(FiniteAutomaton automaton){
        this.transactions = automaton.getTransaction();

        this.states = automaton.getStates();
    }
}
